package com.github.water.service.util;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.restlet.Request;
import org.restlet.data.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.WebRequest;


public class RestletWebRequest implements WebRequest {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestletWebRequest.class);
	private Form form;
	private Request request;

	public RestletWebRequest(Request request) {
		this.form = new Form(request.getEntity());
		this.request = request;
	}

	public boolean checkNotModified(String s) {
		return false;
	}

	public boolean checkNotModified(long lastModifiedTimestamp) {
		return false;
	}

	public String getContextPath() {
		return this.request.getResourceRef().getPath();
	}

	public String getDescription(boolean includeClientInfo) {
		return null;
	}

	public Locale getLocale() {
		return LocaleContextHolder.getLocale();
	}

	public String getParameter(String paramName) {
		return this.form.getFirstValue(paramName);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String[]> getParameterMap() {
		Map conversion = new HashMap();

		for (Map.Entry entry : this.form.getValuesMap().entrySet()) {
			conversion.put(entry.getKey(), new String[] { (String) entry.getValue() });
		}

		return conversion;
	}

	public String[] getParameterValues(String paramName) {
		return this.form.getValuesArray(paramName);
	}

	public String getRemoteUser() {
		return null;
	}

	public Principal getUserPrincipal() {
		return null;
	}

	public boolean isSecure() {
		return this.request.isConfidential();
	}

	public boolean isUserInRole(String role) {
		return false;
	}

	public Object getAttribute(String name, int scope) {
		return null;
	}

	public String[] getAttributeNames(int scope) {
		return null;
	}

	public String getSessionId() {
		return null;
	}

	public Object getSessionMutex() {
		return null;
	}

	public void registerDestructionCallback(String name, Runnable callback, int scope) {
	}

	public void removeAttribute(String name, int scope) {
	}

	public void setAttribute(String name, Object value, int scope) {
	}

	public String getHeader(String s) {
		return null;
	}

	public String[] getHeaderValues(String s) {
		return new String[0];
	}

	public Iterator<String> getHeaderNames() {
		return null;
	}

	public Iterator<String> getParameterNames() {
		return null;
	}

	public Object resolveReference(String s) {
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void logFormRequest(Form form) {
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