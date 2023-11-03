package com.personal.weather;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.personal.weather.cities.ParserCities;
import com.personal.weather.cities.data.City;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicatorConsole;
import com.utils.net.proxy.url_conn.FactoryUrlConnectionOpener;
import com.utils.net.proxy.url_conn.UrlConnectionOpener;

final class WeatherAnalyzerCli {

	private WeatherAnalyzerCli() {
	}

	static void work(
			final int threadCount) {

		try {
			Logger.printProgress("starting WeatherAnalyzer");

			final Date date = new Date();
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
			final String outputFileName = simpleDateFormat.format(date) + ".txt";
			final String outputFilePathString;
			if (IoUtils.directoryExists(PathUtils.createRootPath())) {
				outputFilePathString = PathUtils.computePath(
						WeatherAnalyzerTestUtils.createTestTmpPathString(), outputFileName);
			} else {
				outputFilePathString = PathUtils.computePath(SystemUtils.USER_HOME,
						"WeatherAnalyzer", outputFileName);
			}
			Logger.printProgress("generating output file:");
			Logger.printLine(outputFilePathString);
			FactoryFolderCreator.getInstance().createParentDirectories(outputFilePathString, false, true);

			final UrlConnectionOpener urlConnectionOpener = FactoryUrlConnectionOpener.newInstance();
			urlConnectionOpener.configureProperties();

			final List<City> cityList = ParserCities.createCityList();
			ParserCities.parseWeather(cityList, threadCount, ProgressIndicatorConsole.INSTANCE);

			try (PrintStream printStream = StreamUtils.openPrintStream(outputFilePathString)) {

				for (final City city : cityList) {
					city.printWeather(printStream);
				}
			}

			IoUtils.openFileWithDefaultApp(outputFilePathString);

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
	}
}
