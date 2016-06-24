package com.github.water.authentication;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;


import com.github.water.service.UserInfoService;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import java.security.GeneralSecurityException;

public class AcceptJdbcUsersAuthenticationHandler extends
		AbstractUsernamePasswordAuthenticationHandler {

	@NotNull
	protected UserInfoService userInfoService;

	protected JdbcTemplate jdbcTemplate;

	public Md5PasswordEncoder myPasswordEncoder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jasig.cas.authentication.handler.support.
	 * AbstractUsernamePasswordAuthenticationHandler
	 * #authenticateUsernamePasswordInternal
	 * (org.jasig.cas.authentication.UsernamePasswordCredential)
	 */
	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(
			UsernamePasswordCredential credential)
			throws GeneralSecurityException, PreventedException {
		long start = System.currentTimeMillis();
		final String username = credential.getUsername();
		String dbPassword = this.userInfoService.getPWD(username);

		if (null == dbPassword) {
			logger.debug("{} was not found in the map.", username);
			throw new AccountNotFoundException(username
					+ " not found in backing map.");
		}

		String inputPwd = credential.getPassword(); // 明文密码
		long start5 = System.currentTimeMillis();
		boolean isMatch = myPasswordEncoder.isPasswordValid(dbPassword,
				inputPwd, username);
		long end5 = System.currentTimeMillis(); // 获取结束时间
		System.err.println("passwordEncoder~~~~~~： " + (end5 - start5) + "ms");

		if (!isMatch) {
			throw new FailedLoginException();
		}
		long end = System.currentTimeMillis(); // 获取结束时间
		System.err.println("HandlerResult~~~~~~： " + (end - start) + "ms");
		return createHandlerResult(credential,
				this.principalFactory.createPrincipal(username), null);
	}

	public boolean logout(HttpServletResponse response) {
		return true;

	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	public Md5PasswordEncoder getMyPasswordEncoder() {
		return myPasswordEncoder;
	}

	public void setMyPasswordEncoder(Md5PasswordEncoder myPasswordEncoder) {
		this.myPasswordEncoder = myPasswordEncoder;
	}
}
