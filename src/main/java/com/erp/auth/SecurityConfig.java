package com.erp.auth;

import com.erp.auth.LoginSuccessHandler;
import com.erp.dao.ManagerDAO;
import com.erp.jwt.JwtAuthenticationFilter;
import com.erp.jwt.JwtBasicAuthenticationFilter;
import com.erp.jwt.JwtProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final CorsFilter corsFilter;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration ac) throws Exception {
        return ac.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager am, ManagerDAO managerDAO, JwtProperties jwtProperties) throws Exception {

        http.csrf(csrf-> csrf.disable()); //
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(form -> form.disable());
        http.logout(logout -> logout.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());



        http.authorizeHttpRequests(auth ->
                auth
                .requestMatchers("/image/**", "/css/**", "/js/**").permitAll()
                .requestMatchers("/loginView").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/store/**").hasRole("STORE"   )
                .anyRequest().authenticated());


        http.addFilter(corsFilter);
        http.addFilter(new JwtAuthenticationFilter(am,jwtProperties));
        http.addFilter(new JwtBasicAuthenticationFilter(am,managerDAO,jwtProperties));

        http.exceptionHandling(ex -> {
           ex.accessDeniedHandler(new AccessDeniedHandler() {
               @Override
               public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                   response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                   request.getRequestDispatcher("/noPermission").forward(request, response);
               }
           });
        });


        return http.build();
    }
}
