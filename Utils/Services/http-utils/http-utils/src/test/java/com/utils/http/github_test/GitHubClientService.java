package com.utils.http.github_test;

import com.utils.http.github_test.data.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubClientService {

	@GET("/users/{username}")
	Call<User> createCall(
			@Path("username") String username);
}
