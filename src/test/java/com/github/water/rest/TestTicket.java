package com.github.water.rest;

import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class TestTicket {

	private static void ticketValidate(String serverValidate,
			String serviceTicket, String service) {
		notNull(serviceTicket, "paramter 'serviceTicket' is not null");
		notNull(service, "paramter 'service' is not null");

		final HttpClient client = new HttpClient();
		GetMethod post = null;

		try {
			post = new GetMethod(serverValidate + "?" + "ticket="
					+ serviceTicket + "&service="
					+ URLEncoder.encode(service, "UTF-8"));
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();
			System.out.println("------"+ response);
			switch (post.getStatusCode()) {
			case 200: {
				info("成功取得用户数据");
			}
			default: {
			}
			}

		} catch (Exception e) {
			warning(e.getMessage());
		} finally {
			// 释放资源
			post.releaseConnection();
		}
	}

	private static void notNull(final Object object, final String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void main(final String[] args) throws Exception {
		String serviceTicket = "ST-52-F9MoJHAfGi0dYKOdSOgp-cas01.example.org";
		final String service = "http://172.16.40.40:8080/j_spring_cas_security_check";
		final String proxyValidate = "https://wangweiwei:8443/cas/proxyValidate";

		ticketValidate(proxyValidate, serviceTicket, service);
	}

	private static void warning(String msg) {
		System.out.println(msg);
	}

	private static void info(String msg) {
		System.out.println(msg);
	}

}