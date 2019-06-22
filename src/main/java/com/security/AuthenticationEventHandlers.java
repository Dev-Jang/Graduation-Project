package com.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class AuthenticationEventHandlers {

	public String nowTime() {
		Date now = new Date();
		TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss] ");
		sdf.setTimeZone(tz);
		String time = sdf.format(now);

		return time;
	}

	@EventListener
	public void handleSuccess(AuthenticationSuccessEvent event) {
		System.out.println("> " + nowTime() + ((UserDetails)(event.getAuthentication().getPrincipal())).getUsername() + ": Authentication Success");
	}

	@EventListener
	public void handleBadCredentials(AuthenticationFailureBadCredentialsEvent event) {
		System.out.println("> " + nowTime() + event.getAuthentication().getPrincipal() + ": Invalid E-mail or Password.");
	}

}

