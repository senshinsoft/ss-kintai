package jp.co.senshinsoft.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.co.senshinsoft.domain.User;

public class GetLoginUserDetails {
	/**
	 * 認証情報からユーザ情報を取得し、Modelに追加する
	 * @return
	 */
	@ModelAttribute("loginUser")
	public User getLoginUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = null;
		
		if(authentication.getPrincipal() instanceof LoginUserDetails) {
			LoginUserDetails userDetails = LoginUserDetails.class.cast(authentication.getPrincipal());
			user = userDetails.getUser();
		}
		
		return user;
	}

}
