package com.sap.ase.poker;

import static org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.sap.ase.poker.security.JwtAuthenticationRequestFilter;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/login").hasAuthority("USER").antMatchers("/*")
				.permitAll().and().httpBasic().and().csrf().disable();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

		inMemoryUserDetailsManager.createUser(buildUser("al-capone", "welcomeAl"));
		inMemoryUserDetailsManager.createUser(buildUser("pat-garret", "welcomePat"));
		inMemoryUserDetailsManager.createUser(buildUser("wyatt-earp", "welcomeWyatt"));
		inMemoryUserDetailsManager.createUser(buildUser("doc-holiday", "welcomeDoc"));
		inMemoryUserDetailsManager.createUser(buildUser("wild-bill", "welcomeBill"));
		inMemoryUserDetailsManager.createUser(buildUser("calamity-jane", "welcomeJane"));
		inMemoryUserDetailsManager.createUser(buildUser("kitty-leroy", "welcomeKitty"));
		inMemoryUserDetailsManager.createUser(buildUser("madame-moustache", "welcomeMadame"));
		inMemoryUserDetailsManager.createUser(buildUser("poker-alice", "welcomeAlice"));

		return inMemoryUserDetailsManager;
	}

	@SuppressWarnings("deprecation")
	private UserDetails buildUser(String username, String password) {
		return withDefaultPasswordEncoder().username(username).password(password).authorities("USER").build();
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationRequestFilter> jwtAuthenticationFilterRegistrationBean() {
		FilterRegistrationBean<JwtAuthenticationRequestFilter> filterRegistrationBean = new FilterRegistrationBean<JwtAuthenticationRequestFilter>();
		JwtAuthenticationRequestFilter jwtAuthenticationFilter = new JwtAuthenticationRequestFilter();
		filterRegistrationBean.setFilter(jwtAuthenticationFilter);
		filterRegistrationBean.addUrlPatterns("/api/tables/*");
		filterRegistrationBean.addUrlPatterns("/api/login/user");
		filterRegistrationBean.setOrder(2);
		return filterRegistrationBean;
	}

}