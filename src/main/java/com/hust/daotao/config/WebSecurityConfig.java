package com.hust.daotao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private DataSource dataSource;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin/**").hasAnyAuthority("ADMIN", "TEACHER")
				.antMatchers("/administrator/**").hasAuthority("ADMIN")
				.antMatchers("/teacher/**").hasAnyAuthority("ADMIN", "TEACHER")
				.antMatchers("/change-password").fullyAuthenticated()
				.antMatchers("/criteria").fullyAuthenticated()
				.antMatchers("/courses/my-courses/**").fullyAuthenticated()
				.antMatchers("/forum/post").fullyAuthenticated()
				.antMatchers("/test").fullyAuthenticated()
				.antMatchers("/users").fullyAuthenticated()
				.antMatchers("/courses/**").permitAll()
				.antMatchers("/client/**").permitAll()
				.antMatchers("/static/**").permitAll()
				.and()
				.formLogin().loginPage("/login").usernameParameter("email").passwordParameter("password")
				.defaultSuccessUrl("/success-login").failureUrl("/login?error=true").and().exceptionHandling()
				.accessDeniedPage("/").and().rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400);
		http.csrf().disable();
		http.headers().frameOptions().disable();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");

	}
}