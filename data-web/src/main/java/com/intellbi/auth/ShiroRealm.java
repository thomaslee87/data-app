package com.intellbi.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intellbi.dataobject.UserDO;
import com.intellbi.service.UserService;

/**
 * The Spring/Hibernate sample application's one and only configured Apache
 * Shiro Realm.
 * 
 * <p>
 * Because a Realm is really just a security-specific DAO, we could have just
 * made Hibernate calls directly in the implementation and named it a
 * 'HibernateRealm' or something similar.
 * </p>
 * 
 * <p>
 * But we've decided to make the calls to the database using a UserDAO, since a
 * DAO would be used in other areas of a 'real' application in addition to here.
 * We felt it better to use that same DAO to show code re-use.
 * </p>
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	public ShiroRealm() {
		setName("ShiroRealm"); // This name must match the name in the User
								// class's getPrincipals() method
		// setCredentialsMatcher(new Sha256CredentialsMatcher());
		setCredentialsMatcher(new AllowAllCredentialsMatcher());
	}

	// 认证信息，主要针对用户登录，（下文讲述在action或者controller登录过程代码）
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String password = String.valueOf(token.getPassword());
		
		// 调用操作数据库的方法查询user信息
		UserDO user = userService.getUser(token.getUsername());
		if(user != null) {
			if(password.equals(user.getPassword())) {
				Session session =  SecurityUtils.getSubject().getSession();
				session.setAttribute("username", user.getUsername());
				return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
			}
		}
		return null;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
//		String userId = (String) principals.fromRealm(getName()).iterator().next();
//		User user = userOperator.getById(userId);
//		if (user != null) {
//			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//			Role role = userOperator.getByRoleId(user.getRoleId());
//			info.addRole(role.getRoleName());
//			// info.addStringPermissions( role.getPermissions()
//			// );//如果你添加了对权限的表，打开此注释，添加角色具有的权限
//
//			return info;
//		} else {
//			return null;
//		}
//		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		info.addRole("admin");
//		return info;
	}

}
