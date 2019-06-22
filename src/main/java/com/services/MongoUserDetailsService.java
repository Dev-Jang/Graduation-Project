package com.services;

import com.models.UserVO;
import com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MongoUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	public UserVO findUserByuserId(String userId) {
		return repository.findByuserId(userId);
	}

	public void saveUser(UserVO user) {
		user.setUserPw(bCryptPasswordEncoder.encode(user.getUserPw()));
		repository.save(user);
	}

	public boolean matchPw(String userPw, String encodePw) {
		return bCryptPasswordEncoder.matches(userPw, encodePw);
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserVO user = repository.findByuserId(userId);
		if(user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));
		return new User(user.getUserId(), user.getUserPw(), authorities);
	}
}
