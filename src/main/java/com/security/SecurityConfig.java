package com.security;

import javax.annotation.Resource;
import java.util.List;

import com.services.MongoUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	MongoUserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable();
		http
			.authorizeRequests()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/js/**").permitAll()
			.antMatchers("/scss/**").permitAll()
			.antMatchers("/vendor/**").permitAll()
			.antMatchers("/weather-icons-master/**").permitAll()
			.antMatchers("/callback/").permitAll()
			.antMatchers("/error**").permitAll()
			.antMatchers("/test/**").permitAll()
			.antMatchers("/json/**").permitAll()
			.antMatchers("/login.html").permitAll()
			.antMatchers("/register.html").permitAll()
			.antMatchers("/addAccount").permitAll()
			.antMatchers("/forgot-password.html").permitAll()
			.antMatchers("/mailSender").permitAll()
			.anyRequest().fullyAuthenticated()
			.and()
			.formLogin()
			.loginPage("/login.html")
			.defaultSuccessUrl("/index.html")
			.failureUrl("/login.html?error")
			.usernameParameter("username")
			.permitAll()
			.and()
			.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/login.html?logout")
			.invalidateHttpSession(true)
			.deleteCookies("remember-me")
			.permitAll()
			.and()
			.rememberMe();
	}

	@Bean
	public PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
}
