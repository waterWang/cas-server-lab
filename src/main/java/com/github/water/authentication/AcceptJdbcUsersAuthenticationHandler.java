package com.github.water.authentication;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.water.conf.MyPasswordEncoder;
import com.github.water.domain.UserInfo;
import com.github.water.service.UserInfoService;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import java.security.GeneralSecurityException;


public class AcceptJdbcUsersAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
	
	private static Logger log = LoggerFactory.getLogger(AbstractUsernamePasswordAuthenticationHandler.class);
	
	
	private static final String LOCK_USER_UPDATE_STATEMENT = "UPDATE USERINFO SET ISLOCKED = ?  , UNLOCKTIME = ? WHERE ID = ?";
	
	private static final String UNLOCK_USER_UPDATE_STATEMENT = "UPDATE USERINFO SET ISLOCKED = ? , UNLOCKTIME = ? WHERE ID = ?";
	
	private static final String BADPASSWORDCOUNT_UPDATE_STATEMENT = "UPDATE USERINFO SET BADPASSWORDCOUNT = ? , BADPASSWORDTIME = ?  WHERE ID = ?";
	
	private static final String BADPASSWORDCOUNT_RESET_UPDATE_STATEMENT = "UPDATE USERINFO SET BADPASSWORDCOUNT = ? , ISLOCKED = ? ,UNLOCKTIME = ?  WHERE ID = ?";
	
	private static final String HISTORY_LOGIN_INSERT_STATEMENT = "INSERT INTO LOGIN_HISTORY (ID , SESSIONID , UID , USERNAME , DISPLAYNAME , LOGINTYPE , MESSAGE , CODE , PROVIDER , SOURCEIP , BROWSER , PLATFORM , APPLICATION , LOGINURL )VALUES( ? , ? , ? , ? , ?, ? , ? , ?, ? , ? , ?, ? , ? , ?)";
	
	private static final String LOGIN_USERINFO_UPDATE_STATEMENT  = "UPDATE USERINFO SET LASTLOGINTIME = ?  , LASTLOGINIP = ? , LOGINCOUNT = ?  WHERE ID = ?";
	
	private static final String LOGOUT_USERINFO_UPDATE_STATEMENT = "UPDATE USERINFO SET LASTLOGOFFTIME = ?   WHERE ID = ?";
	
	private static final String HISTORY_LOGOUT_UPDATE_STATEMENT = "UPDATE LOGIN_HISTORY SET LOGOUTTIME = ?  WHERE  SESSIONID = ?";
	
	public final static String FORMAT_DATE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	

    /** The list of users we will accept. */
    @NotNull
    protected UserInfoService userInfoService;
    
    protected  JdbcTemplate jdbcTemplate;

    /**
     * 
     **/
    @Override
    protected  HandlerResult authenticateUsernamePasswordInternal( UsernamePasswordCredential credential)
            throws GeneralSecurityException, PreventedException {

        final String username = credential.getUsername();
        final UserInfo u = this.userInfoService.loadUserInfo(username);

        if (u == null) {
           logger.debug("{} was not found in the map.", username);
           throw new AccountNotFoundException(username + " not found in backing map.");
        }

        final String rawPassword = credential.getPassword();  //明文密码
        final String dbPassword = u.getPassword(); //密文密码
        
        if (!MyPasswordEncoder.match(rawPassword, dbPassword)) {
            throw new FailedLoginException();
        }
//        WebContext.setUserInfo(u);
        
//        insertLoginHistory(u,"WebLogin","Web","100000","Success");
        return createHandlerResult(credential, new SimplePrincipal(username), null);
    }


/**
  * login log write to log db
  * @param uid
  * @param j_username
  * @param type
  * @param code
  * @param message
  */
	/*public boolean insertLoginHistory(UserInfo userInfo,String type,String provider,String code,String message){
		Date loginDate=new Date();
		String sessionId=WebContext.genId();
		WebContext.setAttribute(WebConstants.CURRENT_USER_SESSION_ID, sessionId);
		String ipAddress=WebContext.getRequestIpAddress();
		String platform="";
		String browser="";
		String userAgent = WebContext.getRequest().getHeader("User-Agent");  
 	String []arrayUserAgent=null;
 	if(userAgent.indexOf("MSIE")>0){
 		arrayUserAgent=userAgent.split(";");
 		browser=arrayUserAgent[1].trim();
 		platform=arrayUserAgent[2].trim();
 	}else if(userAgent.indexOf("Trident")>0){
 		arrayUserAgent=userAgent.split(";");
 		browser="MSIE/"+arrayUserAgent[3].split("\\)")[0];;
 		platform=arrayUserAgent[0].split("\\(")[1];
 	}else if(userAgent.indexOf("Chrome")>0){
 		arrayUserAgent=userAgent.split(" ");
 		//browser=arrayUserAgent[8].trim();
 		for(int i=0;i<arrayUserAgent.length;i++){
 			if(arrayUserAgent[i].contains("Chrome")){
 				browser=arrayUserAgent[i].trim();
 				browser=browser.substring(0, browser.indexOf('.'));
 			}
 		}
 		platform=(arrayUserAgent[1].substring(1)+" "+arrayUserAgent[2]+" "+arrayUserAgent[3].substring(0, arrayUserAgent[3].length()-1)).trim();
 	}else if(userAgent.indexOf("Firefox")>0){
 		arrayUserAgent=userAgent.split(" ");
 		for(int i=0;i<arrayUserAgent.length;i++){
 			if(arrayUserAgent[i].contains("Firefox")){
 				browser=arrayUserAgent[i].trim();
 				browser=browser.substring(0, browser.indexOf('.'));
 			}
 		}
 		platform=(arrayUserAgent[1].substring(1)+" "+arrayUserAgent[2]+" "+arrayUserAgent[3].substring(0, arrayUserAgent[3].length()-1)).trim();
 		
 	}
 	
		jdbcTemplate.update(HISTORY_LOGIN_INSERT_STATEMENT, 
				new Object[] { 
					WebContext.genId(),
					sessionId,
					userInfo.getId(),
					userInfo.getUsername(),
					userInfo.getDisplayName(),
					type,
					message,
					code,
					provider,
					ipAddress,
					browser,
					platform,
					"Browser",
					loginDate},
				new int[] {Types.VARCHAR, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.VARCHAR,Types.TIMESTAMP });
		
		userInfo.setLastLoginTime(new SimpleDateFormat(FORMAT_DATE_YYYY_MM_DD_HH_MM_SS).format(loginDate));
		
		jdbcTemplate.update(LOGIN_USERINFO_UPDATE_STATEMENT, 
				new Object[] { 
					loginDate,
					ipAddress,
					userInfo.getLoginCount()+1,
					userInfo.getId()},
				new int[] {Types.TIMESTAMP, Types.VARCHAR,Types.INTEGER,Types.VARCHAR});
		
		return true;
	}*/
	
	public boolean logout(HttpServletResponse response){
//		Object sessionIdAttribute=WebContext.getAttribute(WebConstants.CURRENT_USER_SESSION_ID);
//		if(sessionIdAttribute!=null){
//			UserInfo userInfo=WebContext.getUserInfo();
//			Date logoutDateTime=new Date();
//			if(sessionIdAttribute!=null){
//				
//				jdbcTemplate.update(HISTORY_LOGOUT_UPDATE_STATEMENT, 
//						new Object[] { 
//							logoutDateTime,
//							sessionIdAttribute.toString()},
//						new int[] {Types.TIMESTAMP ,Types.VARCHAR});
//			}
//			
//			jdbcTemplate.update(LOGOUT_USERINFO_UPDATE_STATEMENT, 
//					new Object[] { 
//						logoutDateTime,
//						userInfo.getId()},
//					new int[] {Types.TIMESTAMP ,Types.VARCHAR});
//			log.info(" user "+userInfo.getLogin()+" Logout, datetime "+logoutDateTime+" .");
//			log.info("Session " +WebContext.getAttribute(WebConstants.CURRENT_USER_SESSION_ID)+ ", user "+userInfo.getLogin()+" Logout, datetime "+logoutDateTime+" .");
//		}
		return true;
		
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

}
