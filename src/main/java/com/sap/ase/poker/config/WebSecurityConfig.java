package com.sap.ase.poker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.ase.poker.security.JsonUsernamePasswordAuthenticationFilter;
import com.sap.ase.poker.security.JwtAuthenticationRequestFilter;
import com.sap.ase.poker.security.JwtTools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;

    public WebSecurityConfig(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                        .username("al-capone")
                        .password("all-in")
                        .roles("USER")
                        .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtTools jwtTools = new JwtTools(JwtTools.SECRET);
        http
                .authorizeRequests()
                .antMatchers("/table/**").authenticated()
                .antMatchers("/login/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationRequestFilter(authenticationManager(), jwtTools))
                .addFilter(new JsonUsernamePasswordAuthenticationFilter(authenticationManager(), objectMapper, jwtTools))
                .formLogin(form -> form.loginPage("/login/index.html").permitAll())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
    }
}