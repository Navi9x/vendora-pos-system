package com.vendora.vendorapos.config;


import com.vendora.vendorapos.security.tenant.TenantContext;
import com.vendora.vendorapos.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private com.vendora.vendorapos.util.JwtUtil jwtUtil;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            Long tenantId = jwtUtil.extractTenantId(jwt);
            if (tenantId != null) {
                TenantContext.setTenantId(tenantId);   // ✅ HERE
            }
            try{
                username = jwtUtil.getUsernameFromToken(jwt);
                System.out.println("username from JWT: "+username);
            }catch (IllegalArgumentException e){
                System.out.println("Unable to get username from token");
            }catch (ExpiredJwtException e){
                System.out.println("Token has expired");
            }
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = jwtService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt, userDetails)) {
                System.out.println("token valid");
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                System.out.println("token invalid");
            }
        }

        filterChain.doFilter(request, response);
    }
}
