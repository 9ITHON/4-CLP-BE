package com.qithon.clearplate.global.oauth2;


import com.qithon.clearplate.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    private final JwtTokenProvider jwtTokenProvider;


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
<<<<<<< HEAD
        refreshTokenCookie.setSecure(true);      
=======
        refreshTokenCookie.setSecure(true);
>>>>>>> 6455080 (chore: 도커용으로 db 설정 변경)
        refreshTokenCookie.setDomain("clearplate.store");
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
      


        // TODO: 소셜로그인이 완료된 후 사용자를 어디로 리다이렉트할지 결정해야 합니다.
        response.sendRedirect("https://www.clearplate.store");
    }
}
