package com.github.water.conf;

import org.jasig.cas.authentication.handler.PasswordEncoder;
/**
* @author water.Wang
* @date 2016年2月23日 
* @todo 自定义密码加密方式
*/ 

public class MyPasswordEncoder implements PasswordEncoder {
	
	private static final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

	//rawPassword 明文
	public String encode(String rawPassword) {
		return  passwordEncoder.encode(rawPassword);
	}
	//rawPassword 明文。 password 密文
	public  boolean match(String rawPassword, String password) {  
        return passwordEncoder.matches(rawPassword, password);  
   } 
	
}