package com.personal.weather.server;

import java.util.List;

import com.utils.string.StrUtils;

public class CitiesPojo {

	private final List<CityPojo> cityPojoList;

	public CitiesPojo(
			final List<CityPojo> cityPojoList) {

		this.cityPojoList = cityPojoList;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public List<CityPojo> getCityPojoList() {
		return cityPojoList;
	}
}
