package com.github.water.restlet.v1;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.support.WebRequestDataBinder;

import com.github.water.domain.UserInfo;
import com.github.water.service.UserInfoService;
import com.github.water.service.util.RestletWebRequest;

public class AccountResource extends ServerResource {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AccountResource.class);

	@NotNull
	public UserInfoService userInfoService;

	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	public AccountResource() {
		setNegotiated(false);
	}

	@Post
	public final void acceptRepresentation(Representation entity) {
		UserInfo userInfo = obtainCredentials();

		Formatter fmt = null;
		try {
			userInfoService.cerateUser(userInfo);
			getResponse().setStatus(determineStatus());

			fmt = new Formatter();
			fmt.format(
					"<!DOCTYPE HTML PUBLIC \\\"-//IETF//DTD HTML 2.0//EN\\\"><html><head><title>",
					new Object[0]);
			fmt.format(
					"%s %s",
					new Object[] {
							Integer.valueOf(getResponse().getStatus().getCode()),
							getResponse().getStatus().getDescription() })
					.format("</title></head><body><h1>Account Created</h1><form action=\"%s",
							new Object[] { userInfo.getLogin() + "is created" })
					.format("\" method=\"POST\">Service:<input type=\"text\" name=\"service\" value=\"\">",
							new Object[0])
					.format("<br></form></body></html>",
							new Object[0]);
			
			getResponse().setEntity(fmt.toString(), MediaType.TEXT_HTML);
			LOGGER.info(getResponse().getEntityAsText());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
					e.getMessage());
		} finally {
			IOUtils.closeQuietly(fmt);
		}
	}

	protected Status determineStatus() {
		return Status.SUCCESS_CREATED;
	}

	protected UserInfo obtainCredentials() {
		UserInfo userInfo = new UserInfo();
		WebRequestDataBinder binder = new WebRequestDataBinder(userInfo);
		RestletWebRequest webRequest = new RestletWebRequest(getRequest());

		logFormRequest(new Form(getRequest().getEntity()));
		binder.bind(webRequest);

		return userInfo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void logFormRequest(Form form) {
		if (LOGGER.isDebugEnabled()) {
			Set pairs = new HashSet();
			for (String name : form.getNames()) {
				StringBuilder builder = new StringBuilder();
				builder.append(name);
				builder.append(": ");
				if (!"password".equalsIgnoreCase(name))
					builder.append(form.getValues(name));
				else {
					builder.append("*****");
				}
				pairs.add(builder.toString());
			}
			LOGGER.debug(StringUtils.join(pairs, ", "));
		}
	}

	
}