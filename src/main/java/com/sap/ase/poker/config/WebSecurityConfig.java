package com.sap.ase.poker.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;

import com.sap.ase.poker.rest.TableService;
import com.sap.ase.poker.security.JwtAuthenticationRequestFilter;
import com.sap.ase.poker.security.LoginService;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.POST, LoginService.PATH)
				.hasAuthority(OfflineUserDetailsService.POKER_USER_AUTHORITY).antMatchers("/*").permitAll().and()
				.httpBasic().and().csrf().disable();
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationRequestFilter> jwtAuthenticationFilterRegistrationBean() {
		FilterRegistrationBean<JwtAuthenticationRequestFilter> filterRegistrationBean = new FilterRegistrationBean<JwtAuthenticationRequestFilter>();
		JwtAuthenticationRequestFilter jwtAuthenticationFilter = new JwtAuthenticationRequestFilter();
		filterRegistrationBean.setFilter(jwtAuthenticationFilter);
		filterRegistrationBean.addUrlPatterns(TableService.PATH + "/*");
		filterRegistrationBean.addUrlPatterns(LoginService.PATH + "/user");
		filterRegistrationBean.setOrder(2);
		return filterRegistrationBean;
	}

	@Bean
	public PasswordEncoder getSimpleButInsecurePasswordEncoder() {
		final String secret = "Salt 'n' Pepa";
		final int hashWidth = 512;
		final int iterations = 20;

		Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(secret,
				iterations, hashWidth);
		pbkdf2PasswordEncoder.setAlgorithm(SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
		return pbkdf2PasswordEncoder;
	}
}