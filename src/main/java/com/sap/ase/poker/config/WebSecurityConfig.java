package com.sap.ase.poker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.ase.poker.security.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService users() {
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        UserDetails user = users
                .username("al-capone")
                .password("all-in")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }


    @Bean
    public FilterRegistrationBean<UsernamePasswordAuthenticationFilter> jwtAuthenticationFilterRegistrationBean(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        FilterRegistrationBean<UsernamePasswordAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter = new JsonUsernamePasswordAuthenticationFilter(authenticationManager, objectMapper);
        filterRegistrationBean.setFilter(jsonUsernamePasswordAuthenticationFilter);
        return filterRegistrationBean;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form.loginPage("/login/index.html").permitAll())
                .authorizeRequests()
                .antMatchers("/table/**").authenticated()
                .antMatchers("/login/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
//		http.authorizeRequests()
//				.antMatchers(HttpMethod.POST, LoginService.PATH)
//				.hasAuthority(OfflineUserDetailsService.POKER_USER_AUTHORITY)
//				.antMatchers("/*").permitAll().and()
//				.httpBasic().and().csrf().disable();

//        http.authorizeRequests()
//                .antMatchers(LoginService.PATH + "/**")
//                .permitAll()
//                .antMatchers(LoginService.PATH)
//                .permitAll()
////                .antMatchers(HttpMethod.GET, "/table")
//                .anyRequest()
////                .authenticated()
//                .hasAuthority(OfflineUserDetailsService.POKER_USER_AUTHORITY)
//                .and().csrf().disable();
    }
}