package com.grupoeimsa.sigeim.security.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.grupoeimsa.sigeim.security.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        // Get token from header
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null) {
            // Remove Bearer from token
            token = token.substring(7);

            // Validate token
            DecodedJWT decodedJWT = jwtUtils.validateToken(token);

            String username = decodedJWT.getSubject();
            String claims = decodedJWT.getClaim("authorities").asString();

            // create the authorities
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claims);

            // create the authentication
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));

            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }

}
