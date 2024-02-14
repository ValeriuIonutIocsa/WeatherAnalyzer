package com.personal.weather;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.cli.CliUtils;
import com.utils.io.PathUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

final class AppStartWeatherAnalyzer {

	private AppStartWeatherAnalyzer() {
	}

	public static void main(
			final String[] args) {

		Logger.setDebugMode(true);

		final Map<String, String> cliArgsByNameMap = new HashMap<>();
		CliUtils.fillCliArgsByNameMap(args, cliArgsByNameMap);

		final String threadCountString = cliArgsByNameMap.get("threadCount");
		final int threadCount = StrUtils.tryParsePositiveInt(threadCountString);

		final String mode = cliArgsByNameMap.get("mode");
		if ("CLI".equals(mode)) {

			final Instant start = Instant.now();
			WeatherAnalyzerCli.work(threadCount);
			Logger.printFinishMessage(start);

		} else if ("SERVER".equals(mode)) {

			final String hostname = cliArgsByNameMap.get("hostname");
			if (StringUtils.isBlank(hostname)) {
				Logger.printError("missing or invalid \"hostname\" CLI argument");

			} else {
				final String portString = cliArgsByNameMap.get("port");
				final int port = StrUtils.tryParsePositiveInt(portString);
				if (port < 0) {
					Logger.printError("missing or invalid \"port\" CLI argument");

				} else {
					final String backlogString = cliArgsByNameMap.get("backlog");
					final int backlog = StrUtils.tryParsePositiveInt(backlogString);

					final String tmpDatabaseFolderPathString = cliArgsByNameMap.get("databaseFolderPath");
					final String databaseFolderPathString =
							PathUtils.computeNormalizedPath("database folder path", tmpDatabaseFolderPathString);
					if (StringUtils.isBlank(databaseFolderPathString)) {
						Logger.printError("missing or invalid \"databaseFolderPathString\" CLI argument");

					} else {
						WeatherAnalyzerServer.work(threadCount,
								hostname, port, backlog, databaseFolderPathString);
					}
				}
			}
		}
	}
}
