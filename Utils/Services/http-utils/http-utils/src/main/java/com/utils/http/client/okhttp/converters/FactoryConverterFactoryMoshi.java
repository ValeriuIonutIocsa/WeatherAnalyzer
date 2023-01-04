package com.utils.http.client.okhttp.converters;

import retrofit2.Converter;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class FactoryConverterFactoryMoshi implements FactoryConverterFactory {

	@Override
	public Converter.Factory createConverterFactory() {
		return MoshiConverterFactory.create();
	}
}
