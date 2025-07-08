package com.qithon.clearplate.global.security.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.qithon.clearplate.global.common.dto.response.ResponseDTO;
import com.qithon.clearplate.global.exception.ErrorCode;
import com.qithon.clearplate.global.oauth2.OAuth2LoginSuccessHandler;
import com.qithon.clearplate.global.oauth2.OAuth2UserService;
import com.qithon.clearplate.global.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenFilter jwtTokenFilter;
  private final ObjectMapper objectMapper;
  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final OAuth2UserService oAuth2UserService;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // 인증 실패 시 반환할 JSON 응답
    String invalidAuthenticationResponse = objectMapper
        .writeValueAsString(ResponseDTO.response(
            ErrorCode.TOKEN_INVALID.getHttpStatus(),
            ErrorCode.TOKEN_INVALID.getMessage(),
            null
        ));

    // 인가 실패 시 반환할 JSON 응답
    String invalidAuthorizationResponse = objectMapper
        .writeValueAsString(ResponseDTO.response(
            ErrorCode.ACCESS_DENIED.getHttpStatus(),
            ErrorCode.ACCESS_DENIED.getMessage(),
            null
        ));

    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/oauth2/**", "/login/oauth2/code/**").permitAll()
            .requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(e -> e

            //인증 실패 시 응답 핸들링
            .authenticationEntryPoint(((request, response, authException) -> {
              response.setStatus(ErrorCode.TOKEN_INVALID.getHttpStatus().value());
              response.setContentType("application/json");
              response.getWriter().write(invalidAuthenticationResponse);
            }))

            //인가 실패 시 응답 핸들링
            .accessDeniedHandler((request, response, authException) -> {
              response.setStatus(ErrorCode.TOKEN_INVALID.getHttpStatus().value());
              response.setContentType("application/json");
              response.getWriter().write(invalidAuthorizationResponse);
            }))

        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
            .successHandler(oAuth2LoginSuccessHandler)
        );
    return http.build();
  }


}
