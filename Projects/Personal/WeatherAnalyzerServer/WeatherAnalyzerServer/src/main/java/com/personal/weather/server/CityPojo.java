package com.personal.weather.server;

import com.utils.string.StrUtils;

public class CityPojo {

	private final String cityName;
	private final Integer currHighTemp;
	private final Integer currLowTemp;
	private final Integer histHighTemp;
	private final Integer histLowTemp;

	public CityPojo(
			final String cityName,
			final Integer currHighTemp,
			final Integer currLowTemp,
			final Integer histHighTemp,
			final Integer histLowTemp) {

		this.cityName = cityName;
		this.currHighTemp = currHighTemp;
		this.currLowTemp = currLowTemp;
		this.histHighTemp = histHighTemp;
		this.histLowTemp = histLowTemp;
	}

	Integer computeDiffHighTemp() {

		final Integer diffHighTemp;
		if (currHighTemp == null || histHighTemp == null) {
			diffHighTemp = null;
		} else {
			diffHighTemp = currHighTemp - histHighTemp;
		}
		return diffHighTemp;
	}

	Integer computeDiffLowTemp() {

		final Integer diffLowTemp;
		if (currLowTemp == null || histLowTemp == null) {
			diffLowTemp = null;
		} else {
			diffLowTemp = currLowTemp - histLowTemp;
		}
		return diffLowTemp;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	String getCityName() {
		return cityName;
	}

	Integer getCurrHighTemp() {
		return currHighTemp;
	}

	Integer getCurrLowTemp() {
		return currLowTemp;
	}

	Integer getHistHighTemp() {
		return histHighTemp;
	}

	Integer getHistLowTemp() {
		return histLowTemp;
	}
}
