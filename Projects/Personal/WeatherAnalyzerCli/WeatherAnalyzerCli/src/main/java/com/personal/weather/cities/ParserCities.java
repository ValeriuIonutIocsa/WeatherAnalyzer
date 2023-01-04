package com.personal.weather.cities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.personal.weather.cities.data.City;
import com.personal.weather.cities.data.FactoryCity;
import com.utils.concurrency.progress.ConcurrencyUtilsShowProgressRegular;
import com.utils.io.ResourceFileUtils;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicator;
import com.utils.log.progress.ProgressIndicators;
import com.utils.xml.dom.XmlDomUtils;

public final class ParserCities {

	private ParserCities() {
	}

	public static List<City> createCityList() {

		List<City> cityList;
		final String resourceFilePathString = "com/personal/weather/cities/cities.xml";
		try (InputStream inputStream = ResourceFileUtils.resourceFileToInputStream(resourceFilePathString)) {
			cityList = createCityList(inputStream);

		} catch (final Exception exc) {
			cityList = new ArrayList<>();
			Logger.printException(exc);
		}
		return cityList;
	}

	public static List<City> createCityList(
			final InputStream inputStream) throws Exception {

		final List<City> cityList = new ArrayList<>();
		final Document document = XmlDomUtils.openDocument(inputStream);
		final Element documentElement = document.getDocumentElement();

		final List<Element> cityElementList =
				XmlDomUtils.getElementsByTagName(documentElement, "City");
		for (final Element cityElement : cityElementList) {

			final City city = FactoryCity.newInstance(cityElement);
			cityList.add(city);
		}
		return cityList;
	}

	public static void parseWeather(
			final List<City> cityList,
			final int threadCountParam,
			final ProgressIndicator progressIndicator) {

		try {
			final List<Runnable> runnableList = new ArrayList<>();
			for (final City city : cityList) {
				runnableList.add(() -> city.parseWeather(null));
			}

			ProgressIndicators.setInstance(progressIndicator);

			final int threadCount = Math.max(1, threadCountParam);
			final int showProgressInterval = 1;
			final ConcurrencyUtilsShowProgressRegular concurrencyUtilsShowProgressRegular =
					new ConcurrencyUtilsShowProgressRegular(threadCount, showProgressInterval);
			concurrencyUtilsShowProgressRegular.executeMultiThreadedTask(runnableList);

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
	}
}
