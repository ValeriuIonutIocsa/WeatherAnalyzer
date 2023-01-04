package com.personal.weather.server;

import com.personal.weather.cities.data.City;

final class FactoryCityPojo {

	private FactoryCityPojo() {
	}

	static CityPojo newInstance(
			final City city) {

		final String cityName = city.getCityName();
		final Integer currHighTemp = city.getCurrHighTemp();
		final Integer currLowTemp = city.getCurrLowTemp();
		final Integer histHighTemp = city.getHistHighTemp();
		final Integer histLowTemp = city.getHistLowTemp();
		return new CityPojo(cityName, currHighTemp, currLowTemp, histHighTemp, histLowTemp);
	}
}
