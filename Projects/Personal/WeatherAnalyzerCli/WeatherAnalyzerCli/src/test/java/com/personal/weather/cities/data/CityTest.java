package com.personal.weather.cities.data;

import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.personal.weather.WeatherAnalyzerTestUtils;
import com.utils.io.PathUtils;
import com.utils.io.StreamUtils;
import com.utils.log.Logger;
import com.utils.net.proxy.url_conn.FactoryUrlConnectionOpener;
import com.utils.net.proxy.url_conn.UrlConnectionOpener;

class CityTest {

	@Test
	void testParseAndPrintWeather() throws Exception {

		final UrlConnectionOpener urlConnectionOpener = FactoryUrlConnectionOpener.newInstance();
		urlConnectionOpener.configureProperties();

		final String cityName = "Timisoara";
		final String accuWeatherName = "timisoara";
		final String accuWeatherLocationKey = "290867";

		final String htmlOutputPathString = PathUtils.computePath(
				WeatherAnalyzerTestUtils.createTestTmpPathString(), "_debug", cityName + ".html");
		Logger.printNewLine();
		Logger.printProgress("generating temporary output HTML file:");
		Logger.printLine(htmlOutputPathString);

		try (PrintStream printStream = StreamUtils.openPrintStream(htmlOutputPathString)) {

			final City city = new City(cityName, accuWeatherName, accuWeatherLocationKey);
			city.parseWeather(printStream);
			city.printWeather(System.out);
		}
	}
}
