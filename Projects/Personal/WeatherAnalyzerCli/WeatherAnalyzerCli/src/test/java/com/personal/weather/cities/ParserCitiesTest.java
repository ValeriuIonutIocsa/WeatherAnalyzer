package com.personal.weather.cities;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.personal.weather.cities.data.City;
import com.utils.log.Logger;

class ParserCitiesTest {

	@Test
	void testFillCityList() {

		final List<City> cityList = ParserCities.createCityList();

		Logger.printNewLine();
		for (final City city : cityList) {

			final String typescriptString = city.createTypescriptString();
			Logger.printLine(typescriptString);
		}
	}
}
