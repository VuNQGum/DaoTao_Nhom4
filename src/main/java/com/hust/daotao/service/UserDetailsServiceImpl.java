package com.hust.daotao.service;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hust.daotao.entity.Role;
import com.hust.daotao.entity.User;
import com.hust.daotao.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	UserService userService;
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmailAndDeletedAt(email);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		Set<Role> roles = new HashSet<>();
		roles.add(user.getRole());
		for (Role role : roles) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getCode()));
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				grantedAuthorities);
	}
}
