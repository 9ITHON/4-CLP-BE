package com.qithon.clearplate.global.oauth2;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {


        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = oAuth2User.getAttributes();


        String refreshToken = (String) attributes.get("refreshToken");


        // 리프레시 토큰을 쿠키로 반환합니다.
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);


        // TODO: 소셜로그인이 완료된 후 사용자를 어디로 리다이렉트할지 결정해야 합니다.
        response.sendRedirect("/main");
    }
}
