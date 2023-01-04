package com.utils.net.proxy.ok_http;

import com.utils.net.proxy.settings.FactoryProxySettings;
import com.utils.net.proxy.settings.ProxySettings;

public final class FactoryOkHttpBuilderProxyConfigurator {

	private FactoryOkHttpBuilderProxyConfigurator() {
	}

	public static OkHttpBuilderProxyConfigurator newInstance() {

		final OkHttpBuilderProxyConfigurator okHttpBuilderProxyConfigurator;
		final ProxySettings proxySettings = FactoryProxySettings.newInstance();
		if (proxySettings != null) {
			okHttpBuilderProxyConfigurator = new OkHttpBuilderProxyConfiguratorProxy(proxySettings);
		} else {
			okHttpBuilderProxyConfigurator = new OkHttpBuilderProxyConfiguratorRegular();
		}
		return okHttpBuilderProxyConfigurator;
	}
}
