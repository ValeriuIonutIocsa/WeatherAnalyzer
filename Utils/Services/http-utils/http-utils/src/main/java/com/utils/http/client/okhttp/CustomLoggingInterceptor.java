package com.utils.http.client.okhttp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

class CustomLoggingInterceptor implements Interceptor {

	private final boolean formatJson;

	CustomLoggingInterceptor(
			final boolean formatJson) {

		this.formatJson = formatJson;
	}

	@Override
	@SuppressWarnings("all")
	public Response intercept(
			final Chain chain) throws IOException {
		return interceptChain(chain);
	}

	private Response interceptChain(
			final Chain chain) throws IOException {

		final Response response;
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (PrintStream printStream = new PrintStream(
				byteArrayOutputStream, false, Charset.defaultCharset())) {
			response = writeRequestChain(chain, printStream);
		}
		final String str = byteArrayOutputStream.toString(Charset.defaultCharset());
		Logger.printLine(str);
		return response;
	}

	private Response writeRequestChain(
			final Chain chain,
			final PrintStream printStream) throws IOException {

		final Request request = chain.request();

		final long start = System.nanoTime();

		final HttpUrl requestUrl = request.url();
		final Headers requestHeaders = request.headers();
		final String requestHeadersString = requestHeaders.toString();
		final String requestBodyString = createRequestBodyString(request);

		printStream.print("sending HTTP request " + requestUrl);
		printStream.println();
		if (StringUtils.isNotBlank(requestHeadersString)) {

			printStream.print("HTTP REQUEST HEADERS BEGIN");
			printStream.println();
			printStream.print(requestHeadersString);
			printStream.println();
			printStream.print("HTTP REQUEST HEADERS END");

		} else {
			printStream.print("HTTP REQUEST HEADERS EMPTY");
		}
		printStream.println();
		if (StringUtils.isNotBlank(requestBodyString)) {

			printStream.print("HTTP REQUEST BODY BEGIN");
			printStream.println();
			printStream.print(requestBodyString);
			printStream.println();
			printStream.print("HTTP REQUEST BODY END");

		} else {
			printStream.print("HTTP REQUEST BODY EMPTY");
		}
		printStream.println();

		Response response = chain.proceed(request);
		final ResponseBody responseBody = response.body();
		final Request responseRequest = response.request();
		final HttpUrl responseRequestUrl = responseRequest.url();
		final Headers responseHeaders = response.headers();
		final String responseHeadersString = responseHeaders.toString();
		final String tmpResponseBodyString = responseBody.string();
		final byte[] responseBodyStringBytes = tmpResponseBodyString.getBytes(Charset.defaultCharset());
		final MediaType responseBodyContentType = responseBody.contentType();

		final ResponseBody newResponseBody =
				ResponseBody.create(responseBodyStringBytes, responseBodyContentType);
		response = response.newBuilder().body(newResponseBody).build();

		final String responseBodyString;
		if (formatJson) {
			responseBodyString = formatJsonString(tmpResponseBodyString);
		} else {
			responseBodyString = tmpResponseBodyString;
		}

		final long end = System.nanoTime();
		final String durationString = StrUtils.nanoTimeToString(end - start);

		printStream.print("received HTTP response for " +
				responseRequestUrl + " in " + durationString);
		printStream.println();
		if (StringUtils.isNotBlank(requestHeadersString)) {

			printStream.print("HTTP RESPONSE HEADERS BEGIN");
			printStream.println();
			printStream.print(responseHeadersString);
			printStream.println();
			printStream.print("HTTP RESPONSE HEADERS END");

		} else {
			printStream.print("HTTP RESPONSE HEADERS EMPTY");
		}
		printStream.println();
		if (StringUtils.isNotBlank(responseBodyString)) {

			printStream.print("HTTP RESPONSE BODY BEGIN");
			printStream.println();
			printStream.print(responseBodyString);
			printStream.println();
			printStream.print("HTTP RESPONSE BODY END");

		} else {
			printStream.print("HTTP RESPONSE BODY EMPTY");
		}

		return response;
	}

	private String createRequestBodyString(
			final Request request) {

		String requestBodyString = "";
		try {
			final Request copyRequest = request.newBuilder().build();
			final RequestBody requestBody = copyRequest.body();
			if (requestBody != null) {

				final Buffer buffer = new Buffer();
				requestBody.writeTo(buffer);
				final String tmpRequestBodyString = buffer.readUtf8();
				if (formatJson) {
					requestBodyString = formatJsonString(tmpRequestBodyString);
				} else {
					requestBodyString = tmpRequestBodyString;
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
		return requestBodyString;
	}

	private static String formatJsonString(
			final String jsonString) {

		String formattedJsonString = jsonString;
		try {
			if (StringUtils.isNotBlank(jsonString)) {

				try (Buffer buffer = new Buffer()) {

					buffer.writeUtf8(jsonString);
					final JsonReader jsonReader = JsonReader.of(buffer);
					final Object value = jsonReader.readJsonValue();
					final JsonAdapter<Object> adapter =
							new Moshi.Builder().build().adapter(Object.class).indent("    ");
					formattedJsonString = adapter.toJson(value);
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
		return formattedJsonString;
	}
}
