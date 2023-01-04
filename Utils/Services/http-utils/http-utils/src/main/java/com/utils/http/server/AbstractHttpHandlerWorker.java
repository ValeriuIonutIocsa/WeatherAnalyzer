package com.utils.http.server;

import java.util.List;
import java.util.Map;

import com.utils.string.StrUtils;

public abstract class AbstractHttpHandlerWorker implements HttpHandlerWorker {

	private Map<String, List<String>> queryParameterMap;
	private String requestBody;

	private Map<String, String> responseHeaderMap;
	private String responseBody;

	protected AbstractHttpHandlerWorker() {

		responseHeaderMap = null;
		responseBody = "";
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	@Override
	public void setQueryParameterMap(
			final Map<String, List<String>> queryParameterMap) {
		this.queryParameterMap = queryParameterMap;
	}

	protected Map<String, List<String>> getQueryParameterMap() {
		return queryParameterMap;
	}

	@Override
	public void setRequestBody(
			final String requestBody) {
		this.requestBody = requestBody;
	}

	protected String getRequestBody() {
		return requestBody;
	}

	protected void setResponseHeaderMap(
			final Map<String, String> responseHeaderMap) {
		this.responseHeaderMap = responseHeaderMap;
	}

	@Override
	public Map<String, String> getResponseHeaderMap() {
		return responseHeaderMap;
	}

	protected void setResponseBody(
			final String responseBody) {
		this.responseBody = responseBody;
	}

	@Override
	public String getResponseBody() {
		return responseBody;
	}

	protected static String computeFirstQueryParameter(
			final Map<String, List<String>> queryParameterMap,
			final String parameterName) {

		String firstQueryParameter = null;
		final List<String> valueList = queryParameterMap.get(parameterName);
		if (valueList != null) {
			firstQueryParameter = valueList.get(0);
		}
		return firstQueryParameter;
	}
}
