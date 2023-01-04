package com.personal.weather;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.utils.io.PathUtils;

class WeatherAnalyzerServerTest {

	@Test
	void testWork() {

		final int threadCount = 12;
		final String hostname = "localhost";
		final int port = 9010;
		final int backlog = 0;
		final String databaseFolderPathString = PathUtils.computePath(PathUtils.createRootPath(),
				"tmp", "WeatherAnalyzer", "database");
		WeatherAnalyzerServer.work(threadCount, hostname, port, backlog, databaseFolderPathString);
	}
}
