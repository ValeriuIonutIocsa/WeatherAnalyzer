package com.personal.weather;

import org.junit.jupiter.api.Test;

class WeatherAnalyzerCliTest {

	@Test
	void testWork() {

		final int threadCount = 12;
		WeatherAnalyzerCli.work(threadCount);
	}
}
