package com.personal.weather.server;

import com.utils.http.server.AbstractHttpHandlerWorker;

public class HttpHandlerWorkerWeatherAnalyzerRoot
		extends AbstractHttpHandlerWorker implements HttpHandlerWorkerWeatherAnalyzer {

	@Override
	public String getName() {
		return "";
	}

	@Override
	public void work() {
		setResponseBody("Weather Analyzer Server");
	}
}
