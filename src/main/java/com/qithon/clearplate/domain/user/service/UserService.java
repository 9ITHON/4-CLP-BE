package com.qithon.clearplate.domain.user.service;

import com.qithon.clearplate.domain.user.dto.response.UserResponse;
import com.qithon.clearplate.domain.user.entity.User;
import com.qithon.clearplate.domain.user.repository.UserRepository;
import com.qithon.clearplate.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public UserResponse getLoginUser(HttpServletRequest request) {

    String refreshToken = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("refreshToken".equals(cookie.getName())) {
          refreshToken = cookie.getValue();
          break;
        }
      }
    }

    Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);


    if (userRepository.findById(userId).isPresent()) {
      User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
      return UserResponse.from(userRepository.findById(userId).get());
    } else {
      throw new RuntimeException("로그인된 유저가 없습니다.");
    }
  }

}
