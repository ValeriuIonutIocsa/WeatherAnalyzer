package com.utils.http.client.okhttp;

import com.utils.annotations.ApiMethod;

import okhttp3.OkHttpClient;

public interface ServiceGenerator {

	@ApiMethod
	<
			ServiceT> ServiceT createService(
					Class<ServiceT> serviceClass);

	@ApiMethod
	OkHttpClient createOkHttpClient();
}
