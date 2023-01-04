package com.utils.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.utils.io.ReaderUtils;
import com.utils.log.Logger;

public class HttpHandlerImpl implements HttpHandler {

	private static final int STATUS_OK = 200;
	private static final int STATUS_METHOD_NOT_ALLOWED = 405;
	private static final int NO_RESPONSE_LENGTH = -1;

	private final HttpHandlerWorker httpHandlerWorker;
	private final boolean verbose;

	public HttpHandlerImpl(
			final HttpHandlerWorker httpHandlerWorker,
			final boolean verbose) {

		this.httpHandlerWorker = httpHandlerWorker;
		this.verbose = verbose;
	}

	@Override
	public void handle(
			final HttpExchange httpExchange) throws IOException {

		if (verbose) {

			Logger.printNewLine();
			Logger.printProgress("received \"" + httpHandlerWorker.getName() + "\" request");
		}
		try {
			final Headers responseHeaders = httpExchange.getResponseHeaders();
			final String requestMethod = httpExchange.getRequestMethod().toUpperCase();
			if ("GET".equals(requestMethod) || "POST".equals(requestMethod)) {

				final URI requestURI = httpExchange.getRequestURI();
				final String queryString = requestURI.getRawQuery();
				final Map<String, List<String>> queryParameterMap =
						createQueryParameterMap(queryString);
				httpHandlerWorker.setQueryParameterMap(queryParameterMap);

				if ("POST".equals(requestMethod)) {

					final InputStream requestBodyInputStream = httpExchange.getRequestBody();
					if (requestBodyInputStream != null) {

						final String requestBody = ReaderUtils.inputStreamToString(requestBodyInputStream);
						httpHandlerWorker.setRequestBody(requestBody);
					}
				}

				httpHandlerWorker.work();

				final Map<String, String> responseHeaderMap = httpHandlerWorker.getResponseHeaderMap();

				final String responseBody = httpHandlerWorker.getResponseBody();
				final byte[] rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);

				responseHeaders.set("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8.name());
				if (responseHeaderMap != null) {

					for (final Map.Entry<String, String> mapEntry : responseHeaderMap.entrySet()) {

						final String key = mapEntry.getKey();
						final String value = mapEntry.getValue();
						responseHeaders.set(key, value);
					}
				}
				httpExchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
				httpExchange.getResponseBody().write(rawResponseBody);

			} else if ("OPTIONS".equals(requestMethod)) {

				responseHeaders.set("Allow", "GET,OPTIONS");
				httpExchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);

			} else {
				responseHeaders.set("Allow", "GET,OPTIONS");
				httpExchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
			}

		} finally {
			httpExchange.close();
		}
		if (verbose) {
			Logger.printStatus("Finished handling \"" + httpHandlerWorker.getName() + "\" request.");
		}
	}

	private static Map<String, List<String>> createQueryParameterMap(
			final String queryString) {

		final Map<String, List<String>> queryParameterMap = new LinkedHashMap<>();
		if (queryString != null) {

			final String[] rawQueryParameterArray = StringUtils.split(queryString, '&');
			for (final String rawQueryParameter : rawQueryParameterArray) {

				final String[] queryParameter = StringUtils.split(rawQueryParameter, '=');
				final String queryParameterName = decodeUrlComponent(queryParameter[0]);
				queryParameterMap.putIfAbsent(queryParameterName, new ArrayList<>());
				final String queryParameterValue;
				if (queryParameter.length > 1) {
					queryParameterValue = decodeUrlComponent(queryParameter[1]);
				} else {
					queryParameterValue = null;
				}
				queryParameterMap.get(queryParameterName).add(queryParameterValue);
			}
		}
		return queryParameterMap;
	}

	private static String decodeUrlComponent(
			final String urlComponent) {

		return URLDecoder.decode(urlComponent, StandardCharsets.UTF_8);
	}
}
