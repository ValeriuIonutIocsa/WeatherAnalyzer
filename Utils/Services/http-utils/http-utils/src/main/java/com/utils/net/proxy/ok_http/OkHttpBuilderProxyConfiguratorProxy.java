package com.utils.net.proxy.ok_http;

import java.net.Proxy;

import com.utils.net.proxy.ProxyUtils;
import com.utils.net.proxy.settings.ProxySettings;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

class OkHttpBuilderProxyConfiguratorProxy extends AbstractOkHttpBuilderProxyConfigurator {

	private final ProxySettings proxySettings;

	OkHttpBuilderProxyConfiguratorProxy(
			final ProxySettings proxySettings) {

		this.proxySettings = proxySettings;
	}

	@Override
	public void configureProxy(
			final OkHttpClient.Builder okHttpClientBuilder) {

		final Proxy proxy = ProxyUtils.createProxy(proxySettings);
		okHttpClientBuilder.proxy(proxy);

		final Authenticator authenticator = (
				route,
				response) -> {

			final String httpUsername = proxySettings.getHttpUsername();
			final String httpPassword = proxySettings.getHttpPassword();
			final String credential = Credentials.basic(httpUsername, httpPassword);
			return response.request().newBuilder()
					.header("Proxy-Authorization", credential)
					.build();
		};
		okHttpClientBuilder.proxyAuthenticator(authenticator);
	}
}
