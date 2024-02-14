package com.personal.weather;

import org.junit.jupiter.api.Test;

class WeatherAnalyzerCliTest {

	@Test
	void testWork() {

		final int threadCount = 1;
		WeatherAnalyzerCli.work(threadCount);
	}
}
