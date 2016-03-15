package com.github.water.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.github.water.domain.UserInfo;


/**
 * @author Administrator
 *
 */
public class UserInfoService {
	private static final Logger log = LoggerFactory.getLogger(UserInfoService.class);
	private final JdbcTemplate jdbcTemplate;
	

	private static final String DEFAULT_SELECT_STATEMENT = "select * from jhi_user where phone_number = ? or lower(email) = ?";
	
	
	public UserInfoService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;	
	}
	
	public UserInfo loadUserInfo(String username){
		List<UserInfo> listUserInfo=jdbcTemplate.query(
				DEFAULT_SELECT_STATEMENT,
				new UserInfoRowMapper(),
				username,username);
		log.info("query UserInfo List "+listUserInfo);
		if(listUserInfo!=null&&listUserInfo.size()>0){
			return listUserInfo.get(0);
		}
		return null;
	}
	
	
	private final class UserInfoRowMapper  implements RowMapper<UserInfo> {
		@Override
		public UserInfo mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			UserInfo u=new UserInfo();
			u.setId(rs.getLong("id"));
			u.setLogin(rs.getString("login"));
			u.setPassword(rs.getString("password"));
			u.setName(rs.getString("name"));
			u.setPhone_number(rs.getString("phone_number"));
			u.setStatus(rs.getString("status"));
			u.setEmail(rs.getString("email"));
			u.setActivated((int)rs.getLong("activated"));
			u.setLang_key(rs.getString("lang_key"));
			u.setAvatar(rs.getString("avatar"));
			
			return u;
		}
	}

}
