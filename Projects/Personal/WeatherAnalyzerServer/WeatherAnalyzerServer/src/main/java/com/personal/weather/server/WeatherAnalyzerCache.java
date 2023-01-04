package com.personal.weather.server;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.personal.weather.cities.ParserCities;
import com.personal.weather.cities.data.City;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.WriterUtils;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicatorConsole;

public final class WeatherAnalyzerCache {

	private final int threadCount;
	private final String databaseFolderPathString;

	public WeatherAnalyzerCache(
			final int threadCount,
			final String databaseFolderPathString) {

		this.threadCount = threadCount;
		this.databaseFolderPathString = databaseFolderPathString;
	}

	public CitiesPojo readOrComputeData() {

		CitiesPojo citiesPojo = null;
		try {
			synchronized (this) {

				final String hourDateTimeString =
						new SimpleDateFormat("yyyyMMdd_HH", Locale.US).format(new Date());
				final String databaseFilePathString =
						PathUtils.computePath(databaseFolderPathString, hourDateTimeString + ".json");
				if (IoUtils.fileExists(databaseFilePathString)) {

					Logger.printProgress("reading data from cached data file:");
					Logger.printLine(databaseFilePathString);

					final String jsonString =
							ReaderUtils.fileToString(databaseFilePathString, StandardCharsets.UTF_8);

					final JsonAdapter<CitiesPojo> citiesJsonAdapter = createCitiesPojoJsonAdapter();
					citiesPojo = citiesJsonAdapter.fromJson(jsonString);

				} else {
					Logger.printProgress("creating cached data file:");
					Logger.printLine(databaseFilePathString);

					final List<City> cityList = ParserCities.createCityList();
					ParserCities.parseWeather(cityList, threadCount, ProgressIndicatorConsole.INSTANCE);

					final List<CityPojo> cityPojoList = new ArrayList<>();
					for (final City city : cityList) {

						final CityPojo cityPojo = FactoryCityPojo.newInstance(city);
						cityPojoList.add(cityPojo);
					}
					citiesPojo = new CitiesPojo(cityPojoList);

					final JsonAdapter<CitiesPojo> citiesJsonAdapter = createCitiesPojoJsonAdapter();
					final String jsonString = citiesJsonAdapter.toJson(citiesPojo);

					WriterUtils.stringToFile(jsonString, StandardCharsets.UTF_8, databaseFilePathString);
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to get or create data");
			Logger.printException(exc);
		}
		return citiesPojo;
	}

	static JsonAdapter<CitiesPojo> createCitiesPojoJsonAdapter() {

		final Moshi.Builder moshiBuilder = new Moshi.Builder();
		final Moshi moshi = moshiBuilder.build();
		return moshi.adapter(CitiesPojo.class).indent("    ");
	}
}
