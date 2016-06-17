package com.github.water.authentication;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.water.conf.MyPasswordEncoder;
import com.github.water.service.UserInfoService;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import java.security.GeneralSecurityException;

public class AcceptJdbcUsersAuthenticationHandler extends
		AbstractUsernamePasswordAuthenticationHandler {

	public final static String FORMAT_DATE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/** The list of users we will accept. */
	@NotNull
	protected UserInfoService userInfoService;

	protected JdbcTemplate jdbcTemplate;

	/**
     * 
     **/
	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(
			UsernamePasswordCredential credential)
			throws GeneralSecurityException, PreventedException {
		long start = System.currentTimeMillis();
		final String username = credential.getUsername();
		final String dbPassword = this.userInfoService.getPWD(username);

		if (dbPassword == null) {
			logger.debug("{} was not found in the map.", username);
			throw new AccountNotFoundException(username
					+ " not found in backing map.");
		}

		final String rawPassword = credential.getPassword(); // 明文密码
		// final String dbPassword = u.getPassword(); //密文密码

		if (!MyPasswordEncoder.match(rawPassword, dbPassword)) {
			throw new FailedLoginException();
		}
		long end = System.currentTimeMillis(); // 获取结束时间

		System.err.println("authenticate~~~~~~： " + (end - start)
				+ "ms");
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

}
