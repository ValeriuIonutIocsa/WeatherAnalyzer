package com.utils.http.client.okhttp;

import java.util.concurrent.TimeUnit;

import com.utils.http.client.okhttp.converters.FactoryConverterFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

public abstract class AbstractServiceGenerator implements ServiceGenerator {

	private final FactoryConverterFactory factoryConverterFactory;

	private Retrofit retrofit;

	protected AbstractServiceGenerator(
			final FactoryConverterFactory factoryConverterFactory) {

		this.factoryConverterFactory = factoryConverterFactory;
	}

	@Override
	public <
			ServiceT> ServiceT createService(
					final Class<ServiceT> serviceClass) {

		final Retrofit retrofit = getOrCreateRetrofit();
		return retrofit.create(serviceClass);
	}

	private Retrofit getOrCreateRetrofit() {

		if (retrofit == null) {
			retrofit = createRetrofit();
		}
		return retrofit;
	}

	private Retrofit createRetrofit() {

		final Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

		final String baseUrl = getBaseUrl();
		retrofitBuilder.baseUrl(baseUrl);

		final Converter.Factory converterFactory = factoryConverterFactory.createConverterFactory();
		if (converterFactory != null) {
			retrofitBuilder.addConverterFactory(converterFactory);
		}

		final OkHttpClient okHttpClient = createOkHttpClient();
		retrofitBuilder.client(okHttpClient);

		return retrofitBuilder.build();
	}

	protected abstract String getBaseUrl();

	@Override
	public OkHttpClient createOkHttpClient() {

		final OkHttpClient.Builder builder = new OkHttpClient.Builder();

		final boolean addLoggingInterceptor = checkAddLoggingInterceptor();
		if (addLoggingInterceptor) {

			final boolean formatJson = checkFormatJson();
			final Interceptor interceptor = new CustomLoggingInterceptor(formatJson);
			builder.addInterceptor(interceptor);
		}

		return builder
				.connectTimeout(50, TimeUnit.HOURS)
				.readTimeout(50, TimeUnit.HOURS)
				.writeTimeout(50, TimeUnit.HOURS)
				.build();
	}

	protected abstract boolean checkAddLoggingInterceptor();

	protected abstract boolean checkFormatJson();
}
