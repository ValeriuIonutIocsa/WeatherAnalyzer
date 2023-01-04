package com.personal.weather.server;

import java.util.List;

import com.utils.string.StrUtils;

class CitiesPojo {

	private final List<CityPojo> cityPojoList;

	CitiesPojo(
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
