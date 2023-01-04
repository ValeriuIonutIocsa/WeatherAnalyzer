package com.utils.http.server;

import java.util.List;
import java.util.Map;

public interface HttpHandlerWorker {

	String getName();

	void setQueryParameterMap(
			Map<String, List<String>> queryParameterMap);

	void setRequestBody(
			String requestBody);

	void work();

	Map<String, String> getResponseHeaderMap();

	String getResponseBody();
}
