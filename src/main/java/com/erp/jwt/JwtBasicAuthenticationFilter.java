package com.erp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.erp.auth.PrincipalDetails;
import com.erp.dao.ManagerDAO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;


public class JwtBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtProperties jwtProperties;
    private final ManagerDAO managerDAO;
    public JwtBasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                        ManagerDAO managerDAO, JwtProperties jwtProperties) {
        super(authenticationManager);
        this.managerDAO = managerDAO;
        this.jwtProperties = jwtProperties;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtToken = request.getHeader(JwtProperties.HEADER_STRING);
        if (jwtToken == null || !jwtToken.trim().startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        jwtToken = jwtToken.replace(JwtProperties.TOKEN_PREFIX, "");
//        String username = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret())).
//                build().verify(jwtToken).getClaim("username").asString();
//        if(username != null){
//            User user = userRepository.findByUsername(username);
//            PrincipalDetails details = new PrincipalDetails(user);
//            Authentication auth = new UsernamePasswordAuthenticationToken(details,
//                    null, details.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
        chain.doFilter(request, response);
    }
}
