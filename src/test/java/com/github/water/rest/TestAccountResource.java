package com.github.water.rest;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.github.water.domain.UserInfo;

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
			String password, String email, String phoneNumber) {

		notNull(server, "server must not be null");
		notNull(login, "username must not be null");
		notNull(password, "password must not be null");
		String result = "";
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod(server);

		post.setRequestBody(new NameValuePair[] {
				new NameValuePair("login", login),
				new NameValuePair("password", password),
				new NameValuePair("email", email),
				new NameValuePair("phone_number", phoneNumber) });
		try {
			client.executeMethod(post);
			if (post.getStatusCode() == 201) {
				result = "the account being created Successful !";
			} else {
				result = "the " + login + " already exists!";
			}
		} catch (final IOException e) {
			LOG.warning(e.getMessage());
		} finally {
			post.releaseConnection();
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	private static String updatePwd(String server) {

		String resStr = null;  
        HttpClient htpClient = new HttpClient();  
        PutMethod putMethod = new PutMethod(server);  
        putMethod.addRequestHeader( "Content-Type","application/json" );  
        putMethod.getParams().setParameter( HttpMethodParams.HTTP_CONTENT_CHARSET , "sdf" );  
        putMethod.setRequestBody( "www?123" );  
        try{  
            int statusCode = htpClient.executeMethod( putMethod );  
//            log.info(statusCode);  
            if(statusCode != HttpStatus.SC_OK){  
                return null;  
            }    
            byte[] responseBody = putMethod.getResponseBody();           
            resStr = responseBody.toString();
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
            putMethod.releaseConnection();  
        }  
        return resStr;  
	}

	private static void notNull(final Object object, final String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void main(final String[] args) {
		// API 地址
		final String server = "https://wangweiwei:8443/cas/v1/accounts";
//		final String login = "3";
//		final String password = "4";
//		final String email = "5";
//		final String phoneNumber = "6";
//		LOG.info(createAccount(server, login, password, email, phoneNumber));
		
		final String password = "4";
		final Long userId = 4530L;
		
		LOG.info(updatePwd(server));
	}
}