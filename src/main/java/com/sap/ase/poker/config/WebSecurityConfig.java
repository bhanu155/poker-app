package com.sap.ase.poker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.ase.poker.security.JsonUsernamePasswordAuthenticationFilter;
import com.sap.ase.poker.security.JwtAuthenticationRequestFilter;
import com.sap.ase.poker.security.JwtTools;
import com.sap.ase.poker.security.PokerUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PokerUserRepository userRepository;
    private final ObjectMapper objectMapper;

    public WebSecurityConfig(PokerUserRepository userRepository, ObjectMapper objectMapper) {
        super();
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Bean
    public UserDetailsService users(PokerUserRepository userRepository) {
        List<UserDetails> users = userRepository.findAll().stream().map(user -> User.builder()
                        .username(user.getName())
                        .password("{noop}" + user.getPassword())
                        .roles("USER")
                        .build())
                .collect(Collectors.toList());
        return new InMemoryUserDetailsManager(users);
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
                .addFilter(new JwtAuthenticationRequestFilter(authenticationManager(), userRepository, jwtTools))
                .addFilter(new JsonUsernamePasswordAuthenticationFilter(authenticationManager(), objectMapper, jwtTools))
                .formLogin(form -> form.loginPage("/login/index.html").permitAll())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
    }
}