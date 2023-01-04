package com.utils.http.github_test.data;

import com.utils.string.StrUtils;

public class User {

	private String login;
	private long id;
	private String url;

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public void setLogin(
			final String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setId(
			final long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setUrl(
			final String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
