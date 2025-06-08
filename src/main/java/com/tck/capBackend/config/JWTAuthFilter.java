package com.tck.capBackend.config;

import com.tck.capBackend.Service.CustomerDetailsService;
import com.tck.capBackend.Service.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
        final String authheader;
        authheader= request.getHeader("Authorization");
        final String jwtToken;
        final String customerEmail;

        if(authheader == null || authheader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authheader.substring(7); //Bear token value here
        customerEmail = jwtUtils.extractUsername(jwtToken);

        if(customerEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails customerDetails = customerDetailsService.loadUserByUsername(customerEmail);

            if(jwtUtils.isTokenValid(jwtToken, customerDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        customerDetails, null, customerDetails.getAuthorities()
                );

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);

            }
            filterChain.doFilter(request, response);
        }
    }

}
