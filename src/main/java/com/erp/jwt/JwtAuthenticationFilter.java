package com.erp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.erp.auth.PrincipalDetails;
import com.erp.dto.LoginRequestDTO;
import com.erp.dto.ManagerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper mapper = new ObjectMapper();
        try {
            LoginRequestDTO inputData= mapper.readValue(request.getInputStream(), LoginRequestDTO.class);
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(inputData.getManagerId(),inputData.getPassword());
            Authentication auth = authenticationManager.authenticate(authRequest);
            return auth;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails resultDetails = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = JWT.create().withSubject(resultDetails.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME)).
                withClaim("id",resultDetails.getManager().getManagerId()).
                withClaim("username",resultDetails.getManager().getManagerName()).
                sign(Algorithm.HMAC512(jwtProperties.getSecret().getBytes()));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        response.getWriter().println(Map.of("message","loginOk"));
    }
}
