package com.personal.weather;

import org.junit.jupiter.api.Test;

import com.utils.io.PathUtils;

class WeatherAnalyzerServerTest {

	@Test
	void testWork() {

		final int threadCount = 12;
		final String hostname = "localhost";
		final int port = 9010;
		final int backlog = 0;
		final String databaseFolderPathString = PathUtils.computePath(
				WeatherAnalyzerTestUtils.createTestTmpPathString(), "database");
		WeatherAnalyzerServer.work(threadCount, hostname, port, backlog, databaseFolderPathString);
	}
}
