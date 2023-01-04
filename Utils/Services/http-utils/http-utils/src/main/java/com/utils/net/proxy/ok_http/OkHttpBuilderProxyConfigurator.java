package com.utils.net.proxy.ok_http;

import okhttp3.OkHttpClient;

public interface OkHttpBuilderProxyConfigurator {

	void configureProxy(
			OkHttpClient.Builder okHttpClientBuilder);
}
