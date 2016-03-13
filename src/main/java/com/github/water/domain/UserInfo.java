package com.github.water.domain;

import java.io.Serializable;


/**
 * @author Administrator
 * 
 */
public class UserInfo  extends BaseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -982711660913369581L;
	
	private Long 	id;
	private String 	login;
	private String 	password;
	private String 	name;
	private String 	phone_number;
	private String 		status;
	private String 	email;
	private int 	activated;
	private String 		lang_key;
	private String 		avatar;
	

	public UserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserInfo(String login) {
		super();
		this.login=login;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getActivated() {
		return activated;
	}

	public void setActivated(int activated) {
		this.activated = activated;
	}

	public String getLang_key() {
		return lang_key;
	}

	public void setLang_key(String lang_key) {
		this.lang_key = lang_key;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", login=" + login + ", password="
				+ password + ", name=" + name + ", phone_number="
				+ phone_number + ", status=" + status + ", email=" + email
				+ ", activated=" + activated + ", lang_key=" + lang_key
				+ ", avatar=" + avatar + "]";
	}

}
