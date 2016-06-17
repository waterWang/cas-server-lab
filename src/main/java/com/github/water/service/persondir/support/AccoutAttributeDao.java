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

/**
 * @author water
 *	返回更多user信息 的类
 */
public class AccoutAttributeDao extends StubPersonAttributeDao {

	@NotNull
	public UserInfoService userInfoService;
	
	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}
	
	@Override
	public IPersonAttributes getPerson(String uid) {
		
		UserInfo u = this.userInfoService.loadUserInfo(uid);
		
		System.out.println("~~~~~"+ u.getId());
		System.err.println("~~~~~"+ u.getEmail());
//		String ticket = CasServerUtil.getTicket(server, uid, u.getPassword(), service);
		
		Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
		attributes.put("username",Collections.singletonList((Object)u.getLogin()));
		attributes.put("email",Collections.singletonList((Object)u.getEmail()));
		attributes.put("phonenumber",Collections.singletonList((Object)u.getPhone_number()));
		attributes.put("userId",Collections.singletonList((Object)u.getId()));
//		attributes.put("ticket",Collections.singletonList((Object)ticket));
		
		return new AttributeNamedPersonImpl(attributes);
	}
	
//	public String getTicket(){
//		CasServerUtil casServerUtil = new CasServerUtil();
//		String ticket = casServerUtil.getTicket(server, uid, u.getPassword(), service)
//	}
	
	
	
}