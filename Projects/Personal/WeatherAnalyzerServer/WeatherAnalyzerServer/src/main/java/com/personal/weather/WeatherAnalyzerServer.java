package com.personal.weather;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.personal.weather.server.HttpHandlerWorkerWeatherAnalyzer;
import com.personal.weather.server.HttpHandlerWorkerWeatherAnalyzerRetrieveTableData;
import com.personal.weather.server.HttpHandlerWorkerWeatherAnalyzerRoot;
import com.personal.weather.server.WeatherAnalyzerCache;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.utils.concurrency.ThreadUtils;
import com.utils.http.server.HttpHandlerImpl;
import com.utils.http.server.HttpHandlerWorker;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;

final class WeatherAnalyzerServer {

	private WeatherAnalyzerServer() {
	}

	static void work(
			final int threadCount,
			final String hostname,
			final int port,
			final int backlog,
			final String databaseFolderPathString) {

		try {
			Logger.printProgress("starting WeatherAnalyzer server");
			Logger.printLine("hostname: " + hostname);
			Logger.printLine("port: " + port);
			Logger.printLine("backlog: " + backlog);

			FactoryFolderCreator.getInstance().createDirectories(databaseFolderPathString, false, true);

			final WeatherAnalyzerCache weatherAnalyzerCache =
					new WeatherAnalyzerCache(threadCount, databaseFolderPathString);
			final Timer timer = new Timer();
			final TimerTask hourlyTask = new TimerTask() {

				@Override
				public void run() {

					Logger.printNewLine();
					Logger.printProgress("running recurrent task of creating cached data file");
					weatherAnalyzerCache.readOrComputeData();
				}
			};
			timer.schedule(hourlyTask, 0, 1000 * 60 * 60);

			final InetSocketAddress inetSocketAddress =
					new InetSocketAddress(hostname, port);
			final HttpServer httpServer = HttpServer.create(inetSocketAddress, backlog);

			final HttpHandlerWorkerWeatherAnalyzer[] httpHandlerWorkerWeatherAnalyzerArray = {
					new HttpHandlerWorkerWeatherAnalyzerRetrieveTableData(weatherAnalyzerCache),
					new HttpHandlerWorkerWeatherAnalyzerRoot()
			};
			for (final HttpHandlerWorker httpHandlerWorker : httpHandlerWorkerWeatherAnalyzerArray) {

				final String name = httpHandlerWorker.getName();
				final String path = "/" + name;
				final HttpHandler httpHandler = new HttpHandlerImpl(httpHandlerWorker, true);
				httpServer.createContext(path, httpHandler);
			}

			final ExecutorService executorService;
			if (threadCount >= 1) {
				executorService = Executors.newFixedThreadPool(threadCount);
			} else {
				executorService = Executors.newCachedThreadPool();
			}
			httpServer.setExecutor(executorService);
			httpServer.start();

			Logger.printStatus("WeatherAnalyzer server is running");
			ThreadUtils.trySleep(Long.MAX_VALUE);

		} catch (final Exception exc) {
			Logger.printError("error occurred in the WeatherAnalyzer server");
			Logger.printException(exc);
		}
	}
}
