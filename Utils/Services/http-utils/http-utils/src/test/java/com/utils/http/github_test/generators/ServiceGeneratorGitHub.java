package com.utils.http.github_test.generators;

import com.utils.http.client.okhttp.AbstractServiceGenerator;
import com.utils.http.client.okhttp.converters.FactoryConverterFactoryMoshi;

public class ServiceGeneratorGitHub extends AbstractServiceGenerator {

	public ServiceGeneratorGitHub() {

		super(new FactoryConverterFactoryMoshi());
	}

	@Override
	protected String getBaseUrl() {
		return "https://api.github.com/";
	}

	@Override
	protected boolean checkAddLoggingInterceptor() {
		return true;
	}

	@Override
	protected boolean checkFormatJson() {
		return true;
	}
}
