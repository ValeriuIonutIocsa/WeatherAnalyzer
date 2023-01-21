package com.utils.http.client.okhttp;

import com.utils.annotations.ApiMethod;
import com.utils.log.Logger;

import retrofit2.Call;
import retrofit2.Response;

public final class HttpUtils {

	private HttpUtils() {
	}

	@ApiMethod
	public static <
			ObjectT> ObjectT executeCall(
					final Call<ObjectT> call) {

		return executeCall(call, true);
	}

	@ApiMethod
	public static <
			ObjectT> ObjectT executeCall(
					final Call<ObjectT> call,
					final boolean verbose) {

		ObjectT responseObject = null;
		try {
			final Response<ObjectT> response = call.execute();
			responseObject = response.body();

		} catch (final Exception exc) {
			if (verbose) {
				Logger.printError("failed to execute HTTP call");
			}
			Logger.printException(exc);
		}
		return responseObject;
	}
}
