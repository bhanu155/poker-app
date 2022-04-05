package com.sap.ase.poker.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class JwtAuthenticationRequestFilter extends BasicAuthenticationFilter {

    private final PokerUserRepository pokerUserRepository;
    private final JwtTools jwtTools;

    public JwtAuthenticationRequestFilter(AuthenticationManager authenticationManager, PokerUserRepository pokerUserRepository, JwtTools jwtTools) {
        super(authenticationManager);
        this.pokerUserRepository = pokerUserRepository;
        this.jwtTools = jwtTools;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (!cookie.getName().equalsIgnoreCase("jwt")) {
                    continue;
                }
                try {
                    String cookieValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    DecodedJWT decodedJwt = jwtTools.verifyAndDecode(cookieValue);
                    String userId = decodedJwt.getClaim("user_id").asString();
                    String userName = decodedJwt.getClaim("user_name").asString();

                    pokerUserRepository.findUserByName(userId).ifPresent(user -> {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, user.getPassword(), new ArrayList<>());

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    });
                } catch (JWTVerificationException ignored) {
                }
            }
        }

        chain.doFilter(request, response);
    }
}