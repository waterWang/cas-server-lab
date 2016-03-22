package com.github.water.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.github.water.conf.MyPasswordEncoder;
import com.github.water.domain.UserInfo;

/**
 * @author water
 *
 */
public class UserInfoService {
	private static final Logger log = LoggerFactory
			.getLogger(UserInfoService.class);
	
	public JdbcTemplate jdbcTemplate;
	public MyPasswordEncoder myPasswordEncoder;

	private  final String DEFAULT_SELECT_STATEMENT = "select * from jhi_user where lower(login) = ? or phone_number = ? or lower(email) = ?";
	private  final String DEFAULT_CREATE_STATEMENT = "INSERT INTO jhi_user (login,password,activated,created_by,created_date) VALUES(?,?,?,?,?)";


	public UserInfo loadUserInfo(String username) {
		List<UserInfo> listUserInfo = jdbcTemplate.query(
				DEFAULT_SELECT_STATEMENT, new UserInfoRowMapper(), username,username,
				username);
		log.info("query UserInfo List " + listUserInfo);
		if (listUserInfo != null && listUserInfo.size() > 0) {
			return listUserInfo.get(0);
		}
		return null;
	}

	public String cerateUser(UserInfo userInfo) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String pwd = myPasswordEncoder.encode(userInfo.getPassword());
			//检测数据库中是否已经存在改注册的用户
			UserInfo user = loadUserInfo(userInfo.getLogin());
			if (user == null) {
				jdbcTemplate.update(DEFAULT_CREATE_STATEMENT,
						new Object[] { userInfo.getLogin(), pwd, 1,
								userInfo.getLogin(), df.format(new Date()) });
				return "success";
			}
			return "the "+userInfo.getLogin() + " already exists!";
	}

	private final class UserInfoRowMapper implements RowMapper<UserInfo> {
		@Override
		public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserInfo u = new UserInfo();
			u.setId(rs.getLong("id"));
			u.setLogin(rs.getString("login"));
			u.setPassword(rs.getString("password"));
			u.setName(rs.getString("name"));
			u.setPhone_number(rs.getString("phone_number"));
			u.setStatus(rs.getString("status"));
			u.setEmail(rs.getString("email"));
			u.setActivated((int) rs.getLong("activated"));
			u.setLang_key(rs.getString("lang_key"));
			u.setAvatar(rs.getString("avatar"));

			return u;
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setMyPasswordEncoder(MyPasswordEncoder myPasswordEncoder) {
		this.myPasswordEncoder = myPasswordEncoder;
	}

}
