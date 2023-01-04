package com.utils.net.proxy.ok_http;

import com.utils.string.StrUtils;

abstract class AbstractOkHttpBuilderProxyConfigurator implements OkHttpBuilderProxyConfigurator {

	AbstractOkHttpBuilderProxyConfigurator() {
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
