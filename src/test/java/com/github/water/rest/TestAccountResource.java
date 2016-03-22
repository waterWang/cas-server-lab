package com.github.water.rest;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/*
 * 测试rest方式插入用户
 * 
 * */
public final class TestAccountResource {

	private static Logger LOG = Logger.getLogger(TestAccountResource.class
			.getName());

	private TestAccountResource() {
	}

	private static String createAccount(String server, String login,
			String password) {

		notNull(server, "server must not be null");
		notNull(login, "username must not be null");
		notNull(password, "password must not be null");

		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod(server);

		post.setRequestBody(new NameValuePair[] {
				new NameValuePair("login", login),
				new NameValuePair("password", password) });

		try {
			client.executeMethod(post);
			final String response = post.getResponseBodyAsString();
			LOG.info(response);
			switch (post.getStatusCode()) {
			case 201: {
				final Matcher matcher = Pattern.compile(
						".*action=\".*/(.*?)\".*").matcher(response);
				if (matcher.matches())
					return matcher.group(1);
				LOG.info("the account being created Successful !");
				break;
			}
			default:
				LOG.info("Invalid response code (" + post.getStatusCode()
						+ ") from DlCAS server!");
				break;
			}
		} catch (final IOException e) {
			LOG.warning(e.getMessage());
		} finally {
			post.releaseConnection();
		}
		return null;
	}

	private static void notNull(final Object object, final String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void main(final String[] args) {
		// API 地址
		final String server = "https://wangweiwei:8443/cas/v1/accounts";
		final String login = "user@localhost";
		final String password = "user";

		LOG.info(createAccount(server, login, password));
	}
}