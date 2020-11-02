package jp.co.senshinsoft.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jp.co.senshinsoft.domain.User;

public class LoginUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	private final User user;
	private final Collection<GrantedAuthority> authorites;

	public LoginUserDetails(User user, Collection<GrantedAuthority> authorites) {
		this.user = user;
		this.authorites = authorites;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return this.authorites;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public User getUser() {
		return this.user;
	}
	
}
