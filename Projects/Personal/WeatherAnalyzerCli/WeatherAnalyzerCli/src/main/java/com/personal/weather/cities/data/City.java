package com.personal.weather.cities.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.utils.log.Logger;
import com.utils.net.proxy.url_conn.FactoryUrlConnectionOpener;
import com.utils.net.proxy.url_conn.UrlConnectionOpener;
import com.utils.string.StrUtils;
import com.utils.string.replacements.StringReplacementsRegular;
import com.utils.xml.dom.XmlDomUtils;

public class City {

	private final String cityName;
	private final String accuWeatherName;
	private final String accuWeatherLocationKey;

	private Integer currHighTemp;
	private Integer currLowTemp;
	private Integer histHighTemp;
	private Integer histLowTemp;

	City(
			final String cityName,
			final String accuWeatherName,
			final String accuWeatherLocationKey) {

		this.cityName = cityName;
		this.accuWeatherName = accuWeatherName;
		this.accuWeatherLocationKey = accuWeatherLocationKey;
	}

	public void parseWeather(
			final PrintStream printStream) {

		final String urlString = "https://www.accuweather.com/en/ro/" +
				accuWeatherName + "/" +
				accuWeatherLocationKey + "/daily-weather-forecast/" +
				accuWeatherLocationKey + "?day=1";
		Logger.printLine(urlString);

		for (int i = 0; i < 3; i++) {

			final boolean success = tryParseWeather(urlString, printStream);
			if (success) {
				break;
			}
		}
	}

	private boolean tryParseWeather(
			final String urlString,
			final PrintStream printStream) {

		boolean success = false;
		try {
			currHighTemp = null;
			currLowTemp = null;
			histHighTemp = null;
			histLowTemp = null;

			final URL url = new URL(urlString);

			final UrlConnectionOpener urlConnectionOpener = FactoryUrlConnectionOpener.newInstance();
			final URLConnection urlConnection = urlConnectionOpener.openURLConnection(url);
			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X)");

			final StringBuilder sbHtmlContent = new StringBuilder();
			try (BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()))) {

				boolean inside = false;
				int outCount = 0;
				String line;
				while ((line = bufferedReader.readLine()) != null) {

					if (printStream != null) {
						printStream.println(line);
					}
					if (line.contains("<div class=\"temp-history content-module")) {
						inside = true;
					}
					if (inside) {

						sbHtmlContent.append(line).append(System.lineSeparator());
						if (line.contains("<div")) {
							outCount++;
						}
						if (line.contains("</div>")) {

							outCount--;
							if (outCount == 0) {
								break;
							}
						}
					}
				}
			}

			String htmlContent = sbHtmlContent.toString();
			final StringReplacementsRegular stringReplacementsRegular = new StringReplacementsRegular();
			stringReplacementsRegular.addReplacement("&#xB0;", "");
			htmlContent = stringReplacementsRegular.performReplacements(htmlContent);

			try (InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes())) {

				final Document document = XmlDomUtils.openDocument(inputStream);
				final Element documentElement = document.getDocumentElement();

				Element currentTempElement = null;
				Element historicalTempElement = null;
				final List<Element> divElementList =
						XmlDomUtils.getElementsByTagName(documentElement, "div");
				for (final Element divElement : divElementList) {

					final String textContent = XmlDomUtils.getFirstLevelTextContent(divElement);
					if ("Forecast".equals(textContent)) {
						currentTempElement = (Element) divElement.getParentNode();
					} else if ("Average".equals(textContent)) {
						historicalTempElement = (Element) divElement.getParentNode();
					}
				}

				if (currentTempElement != null) {

					final List<Element> currentTempElementChildList =
							XmlDomUtils.getChildElements(currentTempElement);

					final Element currentHighTempElement = currentTempElementChildList.get(1);
					final String currentHighTempString =
							XmlDomUtils.getFirstLevelTextContent(currentHighTempElement);
					currHighTemp = StrUtils.tryParseInt(currentHighTempString.trim());

					final Element currentLowTempElement = currentTempElementChildList.get(2);
					final String currentLowTempString =
							XmlDomUtils.getFirstLevelTextContent(currentLowTempElement);
					currLowTemp = StrUtils.tryParseInt(currentLowTempString.trim());
				}

				if (historicalTempElement != null) {

					final List<Element> historicalTempElementChildList =
							XmlDomUtils.getChildElements(historicalTempElement);

					final Element historicalHighTempElement = historicalTempElementChildList.get(1);
					final String historicalHighTempString =
							XmlDomUtils.getFirstLevelTextContent(historicalHighTempElement);
					histHighTemp = StrUtils.tryParseInt(historicalHighTempString.trim());

					final Element historicalLowTempElement = historicalTempElementChildList.get(2);
					final String historicalLowTempString =
							XmlDomUtils.getFirstLevelTextContent(historicalLowTempElement);
					histLowTemp = StrUtils.tryParseInt(historicalLowTempString.trim());
				}

				if (currLowTemp == null || currHighTemp == null ||
						histLowTemp == null || histHighTemp == null) {
					Logger.printWarning("Received null temp for:" + System.lineSeparator() + urlString);

				} else {
					success = true;
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to parse weather for: " + cityName);
			Logger.printException(exc);
		}
		return success;
	}

	public void printWeather(
			final PrintStream printStream) {

		printStream.print("city: ");
		printStream.printf("%-15s", cityName);
		printStream.print("  curr_high: ");
		printTemp(currHighTemp, printStream);
		printStream.print("  curr_low: ");
		printTemp(currLowTemp, printStream);
		printStream.print("  hist_high: ");
		printTemp(histHighTemp, printStream);
		printStream.print("  hist_low: ");
		printTemp(histLowTemp, printStream);
		printStream.print("  diff_high: ");
		final Integer diffHighTemp = computeDiff(currHighTemp, histHighTemp);
		printTemp(diffHighTemp, printStream);
		printStream.print("  diff_low: ");
		final Integer diffLowTemp = computeDiff(currLowTemp, histLowTemp);
		printTemp(diffLowTemp, printStream);
		printStream.println();
	}

	private static void printTemp(
			final Integer temp,
			final PrintStream printStream) {

		StrUtils.printLeftPaddedString(String.valueOf(temp), 3, printStream);
		printStream.print("\u00B0C");
	}

	public String createCurrentHighTempString() {
		return createTempString(currHighTemp);
	}

	public String createCurrentLowTempString() {
		return createTempString(currLowTemp);
	}

	public String createHistoricalHighTempString() {
		return createTempString(histHighTemp);
	}

	public String createHistoricalLowTempString() {
		return createTempString(histLowTemp);
	}

	public String createDiffHighTempString() {
		return createTempString(computeDiff(currHighTemp, histHighTemp));
	}

	public String createDiffLowTempString() {
		return createTempString(computeDiff(currLowTemp, histLowTemp));
	}

	private static String createTempString(
			final Integer temp) {
		return String.valueOf(temp);
	}

	private static Integer computeDiff(
			final Integer currentTemp,
			final Integer historicalTemp) {

		Integer diffTemp = null;
		if (currentTemp != null && historicalTemp != null) {
			diffTemp = currentTemp - historicalTemp;
		}
		return diffTemp;
	}

	public String createTypescriptString() {
		return "new City(\"" + cityName + "\", \"" + accuWeatherName + "\", \"" + accuWeatherLocationKey + "\"),";
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public String getCityName() {
		return cityName;
	}

	public Integer getCurrHighTemp() {
		return currHighTemp;
	}

	public Integer getCurrLowTemp() {
		return currLowTemp;
	}

	public Integer getHistHighTemp() {
		return histHighTemp;
	}

	public Integer getHistLowTemp() {
		return histLowTemp;
	}
}
