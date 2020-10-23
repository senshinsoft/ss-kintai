package jp.co.senshinsoft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jp.co.senshinsoft.auth.LoginUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	LoginUserDetailsService userDetailsService;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/favicon.ico", "/css/**", "/js/**", "/images/**", "/fonts/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// セッション管理 セキュリティ設定
		http.sessionManagement()
				.and()
					.authorizeRequests()
					.antMatchers("/").permitAll()
					.anyRequest().authenticated()
				.and()
					.formLogin()
					.loginPage("/login")
					.loginProcessingUrl("/loginConf")
					.defaultSuccessUrl("/monthlyList", true)
					.failureUrl("/login?error").permitAll()
					.usernameParameter("userId")
					.passwordParameter("password")
				.and()
					.logout().permitAll()
					.logoutSuccessUrl("/login").permitAll()
					.deleteCookies("JSESSIONID")
					.invalidateHttpSession(true).permitAll();
					
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}
