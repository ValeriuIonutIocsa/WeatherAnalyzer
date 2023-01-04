package com.personal.weather.server;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;
import com.utils.http.server.AbstractHttpHandlerWorker;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class HttpHandlerWorkerWeatherAnalyzerRetrieveTableData
		extends AbstractHttpHandlerWorker implements HttpHandlerWorkerWeatherAnalyzer {

	private final WeatherAnalyzerCache weatherAnalyzerCache;

	public HttpHandlerWorkerWeatherAnalyzerRetrieveTableData(
			final WeatherAnalyzerCache weatherAnalyzerCache) {

		this.weatherAnalyzerCache = weatherAnalyzerCache;
	}

	@Override
	public String getName() {
		return "retrieve_table_data";
	}

	@Override
	public void work() {

		Logger.printLine("time: " + StrUtils.createDisplayDateTimeString());

		final Map<String, List<String>> queryParameterMap = getQueryParameterMap();
		final String ipAddress = computeFirstQueryParameter(queryParameterMap, "ipAddress");
		Logger.printLine("IP address: " + ipAddress);

		final Map<String, String> responseHeaderMap = new HashMap<>();
		responseHeaderMap.put("Access-Control-Allow-Origin", "*");
		setResponseHeaderMap(responseHeaderMap);

		final CitiesPojo citiesPojo = weatherAnalyzerCache.readOrComputeData();
		final List<CityPojo> cityPojoList = citiesPojo.getCityPojoList();

		final String sortColumn = computeFirstQueryParameter(queryParameterMap, "sortColumn");
		final String sortDirection = computeFirstQueryParameter(queryParameterMap, "sortDirection");
		if ("cityName".equals(sortColumn)) {
			if ("asc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getCityName));
			} else if ("desc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getCityName, Comparator.reverseOrder()));
			}
		} else if ("currHighTemp".equals(sortColumn)) {
			if ("asc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getCurrHighTemp));
			} else if ("desc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getCurrHighTemp, Comparator.reverseOrder()));
			}
		} else if ("currLowTemp".equals(sortColumn)) {
			if ("asc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getCurrLowTemp));
			} else if ("desc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getCurrLowTemp, Comparator.reverseOrder()));
			}
		} else if ("histHighTemp".equals(sortColumn)) {
			if ("asc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getHistHighTemp));
			} else if ("desc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getHistHighTemp, Comparator.reverseOrder()));
			}
		} else if ("histLowTemp".equals(sortColumn)) {
			if ("asc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getHistLowTemp));
			} else if ("desc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::getHistLowTemp, Comparator.reverseOrder()));
			}
		} else if ("diffHighTemp".equals(sortColumn)) {
			if ("asc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::computeDiffHighTemp));
			} else if ("desc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::computeDiffHighTemp, Comparator.reverseOrder()));
			}
		} else if ("diffLowTemp".equals(sortColumn)) {
			if ("asc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::computeDiffLowTemp));
			} else if ("desc".equals(sortDirection)) {
				cityPojoList.sort(Comparator.comparing(CityPojo::computeDiffLowTemp, Comparator.reverseOrder()));
			}
		}

		final JsonAdapter<CitiesPojo> citiesJsonAdapter =
				WeatherAnalyzerCache.createCitiesPojoJsonAdapter();
		final String responseBody = citiesJsonAdapter.toJson(citiesPojo);
		setResponseBody(responseBody);
	}
}
