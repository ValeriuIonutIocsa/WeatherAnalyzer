package com.personal.weather;

import com.utils.io.PathUtils;

public final class WeatherAnalyzerTestUtils {

	private WeatherAnalyzerTestUtils() {
	}

	public static String createTestTmpPathString() {

		return PathUtils.computePath(PathUtils.createRootPath(),
				"IVI_MISC", "Tmp", "WeatherAnalyzer");
	}
}
