package com.github.water.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Administrator
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfo extends BaseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -982711660913369581L;

	private Long id;

	private String login;

	private String password;

	private String name;

	private String phone_number;

	private String status;

	private String email;

	private int activated;

	private String lang_key;

	private String avatar;

	public UserInfo() {
		super();
	}

	public UserInfo(String login) {
		super();
		this.login = login;
	}
}
