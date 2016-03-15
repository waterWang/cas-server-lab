package com.github.water.service.persondir.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.AttributeNamedPersonImpl;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

import com.github.water.domain.UserInfo;
import com.github.water.service.UserInfoService;
import com.github.water.service.util.CasServerUtil;

/**
 * @author water
 *	返回更多user信息 的类
 */
public class AccoutAttributeDao extends StubPersonAttributeDao {

//	@NotNull
	public UserInfoService userInfoService;
	
	public String server = "https://wangweiwei:8443/cas/login";
	public String service = "http://172.16.40.99:8080/j_spring_cas_security_check";
	
	@Override
	public IPersonAttributes getPerson(String uid) {
		
		UserInfo u = this.userInfoService.loadUserInfo(uid);
		
		String ticket = CasServerUtil.getTicket(server, uid, u.getPassword(), service);
		
		Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
		attributes.put("username",Collections.singletonList((Object)u.getLogin()));
		attributes.put("email",Collections.singletonList((Object)u.getEmail()));
		attributes.put("phonenumber",Collections.singletonList((Object)u.getPhone_number()));
		attributes.put("id",Collections.singletonList((Object)u.getId()));
		attributes.put("ticket",Collections.singletonList((Object)ticket));
		
		return new AttributeNamedPersonImpl(attributes);
	}
	
//	public String getTicket(){
//		CasServerUtil casServerUtil = new CasServerUtil();
//		String ticket = casServerUtil.getTicket(server, uid, u.getPassword(), service)
//	}
	
	
	
}