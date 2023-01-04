package com.utils.http.github_test;

import com.utils.http.client.okhttp.HttpUtils;
import com.utils.http.github_test.data.User;
import com.utils.http.github_test.generators.ServiceGeneratorGitHub;
import com.utils.log.Logger;

import retrofit2.Call;

final class GitHubClient {

	private GitHubClient() {
	}

	static void run() {

		final GitHubClientService service = new ServiceGeneratorGitHub()
				.createService(GitHubClientService.class);
		final Call<User> call = service.createCall("eugenp");
		final User user = HttpUtils.executeCall(call);
		if (user != null) {
			Logger.printLine(user);
		}
	}
}
