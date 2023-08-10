package com.spring.authentication.filter;

import com.spring.authentication.enums.RoleEnum;
import com.spring.authentication.service.AuthenticationService;
import com.spring.authentication.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = jwtUtil.extractTokenFromRequest(request);
        if (Objects.nonNull(jwtToken)) {
            try {
                String username = jwtUtil.getUsernameFromToken(jwtToken);
                RoleEnum role = jwtUtil.getRoleFromToken(jwtToken);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = this.authenticationService.getUsernamePasswordAuthenticationToken(username, role);

                if (jwtUtil.validateToken(jwtToken, usernamePasswordAuthenticationToken.getName())) {
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException
                     | IllegalArgumentException | UsernameNotFoundException ex) {
                log.error("Authentication Error : {}", ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
