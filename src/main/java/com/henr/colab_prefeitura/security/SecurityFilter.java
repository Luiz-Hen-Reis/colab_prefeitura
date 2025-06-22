package com.henr.colab_prefeitura.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.henr.colab_prefeitura.providers.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String header = request.getHeader("Authorization");

        if (header != null) {
            var subjectId = this.jwtUtil.validateToken(header);

            if (subjectId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            request.setAttribute("user_id", subjectId);

            var roles = jwtUtil.extractRoles(header);
            var authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role)).toList();

             UsernamePasswordAuthenticationToken auth = 
             new UsernamePasswordAuthenticationToken(subjectId, null, authorities);

             SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
    
}
