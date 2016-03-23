package com.github.water.rest;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/*
 * 测试rest方式登录cas server
 * 
 * */
public final class TestCasRestClient {

	private static Logger LOG = Logger.getLogger(TestCasRestClient.class
			.getName());

	private TestCasRestClient() {
	}

	/*
	 * public static String getTicket(final String server, final String
	 * username, final String password, final String service) { notNull(server,
	 * "server must not be null"); notNull(username,
	 * "username must not be null"); notNull(password,
	 * "password must not be null"); notNull(service,
	 * "service must not be null"); String ticketGrantingTicket =
	 * getTicketGrantingTicket(server, username, password);
	 * LOG.info("TGT="+ticketGrantingTicket); return
	 * getServiceTicket(server,ticketGrantingTicket , service); }
	 */
	private static String getServiceTicket(final String server,
			final String ticketGrantingTicket, final String service) {
		if (ticketGrantingTicket == null)
			return null;

		final HttpClient client = new HttpClient();

		final PostMethod post = new PostMethod(server + "/"
				+ ticketGrantingTicket);

		post.setRequestBody(new NameValuePair[] { new NameValuePair("service",
				service) });

		try {
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();

			switch (post.getStatusCode()) {
			case 200:
				return response;

			default:
				LOG.warning("Invalid response code (" + post.getStatusCode()
						+ ") from CAS server!");
				LOG.info("Response (1k): "
						+ response.substring(0,
								Math.min(1024, response.length())));
				break;
			}
		}

		catch (final IOException e) {
			LOG.warning(e.getMessage());
		}

		finally {
			post.releaseConnection();
		}

		return null;
	}

	private static String getTicketGrantingTicket(final String server,
			final String username, final String password) {

		notNull(server, "server must not be null");
		notNull(username, "username must not be null");
		notNull(password, "password must not be null");

		final HttpClient client = new HttpClient();

		final PostMethod post = new PostMethod(server);

		post.setRequestBody(new NameValuePair[] {
				new NameValuePair("username", username),
				new NameValuePair("password", password) });

		try {
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();

			switch (post.getStatusCode()) {
			case 201: {
				final Matcher matcher = Pattern.compile(
						".*action=\".*/(.*?)\".*").matcher(response);

				if (matcher.matches())
					return matcher.group(1);

				LOG.warning("Successful ticket granting request, but no ticket found!");
				LOG.info("Response (1k): "
						+ response.substring(0,
								Math.min(1024, response.length())));
				break;
			}

			default:
				LOG.warning("Invalid response code (" + post.getStatusCode()
						+ ") from CAS server!");
				LOG.info("Response (1k): "
						+ response.substring(0,
								Math.min(1024, response.length())));
				break;
			}
		}

		catch (final IOException e) {
			LOG.warning(e.getMessage());
		}

		finally {
			post.releaseConnection();
		}

		return null;
	}

	private static String logout(String server, String ticketGrantingTicket) {

		notNull(ticketGrantingTicket, "ticketGrantingTicket must not be null");
		notNull(server, "server must not be null");

		String result = "success";
		final HttpClient client = new HttpClient();
		final DeleteMethod delete = new DeleteMethod(server + "/"
				+ ticketGrantingTicket);
		try {
			client.executeMethod(delete);
//			final String response = delete.getResponseBodyAsString();
			switch (delete.getStatusCode()) {
			case 200:
				break;
			default:
				result = "error";
				break;
			}
		} catch (IOException e) {
			result = "error";

		} finally {
			delete.releaseConnection();
		}
		return result;
	}

	private static void notNull(final Object object, final String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void main(final String[] args) {
		// cas server的地址，http或者https都可以
		final String server = "https://wangweiwei:8443/cas/v1/tickets";
		// 现在使用邮箱、手机登录
//		 String username = "user@localhost";
//		 String password = "user";
//		 String service = "http://172.16.40.85:9000/j_spring_cas_security_check";
//
//		String ticketGrantingTicket = getTicketGrantingTicket(server, username,
//				password);
//		String serviceTicket = getServiceTicket(server, ticketGrantingTicket,
//				service);
//
//		System.out.println("TGT-------" + ticketGrantingTicket);
//		System.out.println("ST-------" + serviceTicket);
		// 浏览器上输入 service?ticket=上面获取的ST 即可登录
		
		String logoutResult = logout(server , "TGT-2-TntP4pn1rQAowRKVlW7DuofZbQhxNOxsGhMjBRtmCxL7xhaRgD-cas01.example.org");
		System.out.println("logoutResult-------" + logoutResult);

	}
}