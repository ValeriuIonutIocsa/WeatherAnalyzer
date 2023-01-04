package com.utils.http.client.okhttp.converters;

import retrofit2.Converter;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FactoryConverterFactoryScalars implements FactoryConverterFactory {

	@Override
	public Converter.Factory createConverterFactory() {
		return ScalarsConverterFactory.create();
	}
}
