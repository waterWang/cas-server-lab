package com.github.water.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	private final String DEFAULT_SELECT_STATEMENT = "select * from jhi_user where login = ? UNION ALL select * from jhi_user where phone_number = ? UNION ALL select * from jhi_user where email  = ?";
	private final String DEFAULT_SELECT_PWD_STATEMENT = "select password from jhi_user where login = ? UNION ALL select password from jhi_user where phone_number = ? UNION ALL select password from jhi_user where email = ?";
	private final String DEFAULT_CREATE_STATEMENT = "INSERT INTO jhi_user (login,password,activated,created_by,created_date,email,phone_number) VALUES(?,?,?,?,?,?,?)";
	private final String DEFAULT_UPDATE_STATEMENT = "update jhi_user set password = ? where id = ?";
	private final String DEFAULT_GET_STATEMENT = "select * from jhi_user where id = ?";

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String updatePwd(String newPwd, Long id) {
		List<UserInfo> listUserInfo = jdbcTemplate.query(DEFAULT_GET_STATEMENT,
				new UserInfoRowMapper(), id);

		if (listUserInfo == null) {
			return "the account is not exist";
		} else {
			String pwd = myPasswordEncoder.encode(newPwd);
			jdbcTemplate.update(DEFAULT_UPDATE_STATEMENT, new Object[] { pwd,
					id });
			return "success";
		}

	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public UserInfo loadUserInfo(String username) {
		/*
		 * List<UserInfo> listUserInfo = jdbcTemplate.query(
		 * DEFAULT_SELECT_STATEMENT, new UserInfoRowMapper(), username,
		 * username, username); log.info("query UserInfo List " + listUserInfo);
		 * if (listUserInfo != null && listUserInfo.size() > 0) { return
		 * listUserInfo.get(0); } return null;
		 */
		String lowerName = username.toLowerCase();
		UserInfo userInfo = jdbcTemplate.queryForObject(
				DEFAULT_SELECT_STATEMENT, new UserInfoRowMapper(), lowerName,
				lowerName, lowerName);
		log.info("query UserInfo List " + userInfo);
		if (userInfo != null) {
			return userInfo;
		}
		return null;
	}

	/**
	 * @param username
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String getPWD(String username) {
		
		String lowerName = username.toLowerCase();
		String pwd = jdbcTemplate.queryForObject(DEFAULT_SELECT_PWD_STATEMENT,
				String.class, lowerName, lowerName, lowerName);
		if (null == pwd) {
			return null;
		}
		return pwd;
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Map<String, Object> cerateUser(UserInfo userInfo) {
		Map<String, Object> result = new HashMap<String, Object>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String pwd = myPasswordEncoder.encode(userInfo.getPassword());
		// 检测数据库中是否已经存在改注册的用户
		UserInfo user = loadUserInfo(userInfo.getLogin());
		if (user == null) {
			jdbcTemplate.update(
					DEFAULT_CREATE_STATEMENT,
					new Object[] { userInfo.getLogin(), pwd, 0,
							userInfo.getLogin(), df.format(new Date()),
							userInfo.getEmail(), userInfo.getPhone_number() });
			user = loadUserInfo(userInfo.getLogin());
			result.put("message", "success");
			result.put("userId", user.getId());
			return result;
		}
		result.put("message", "the " + userInfo.getLogin() + " already exists!");
		result.put("userId", null);
		return result;
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
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
