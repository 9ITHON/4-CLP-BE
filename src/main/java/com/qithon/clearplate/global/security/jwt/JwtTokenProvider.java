package com.qithon.clearplate.global.security.jwt;

import com.qithon.clearplate.global.common.threadlocal.TraceIdHolder;
import com.qithon.clearplate.global.security.core.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * JWT 토큰 생성 및 추출, 검증하는 클래스 입니다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final SecretKey secretKey;

  /**
   * JWT 토큰을 생성하는 메서드입니다.
   *
   * @param authentication   인증 정보
   * @param expirationMillis 토큰 만료 시간 (밀리초 단위)
   * @return 생성된 JWT 토큰
   */
  public String createToken(Authentication authentication, Long expirationMillis) {
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    Date expiryDate = new Date(new Date().getTime() + expirationMillis);

    return Jwts.builder()
        .subject(customUserDetails.getUsername())
        .claim("userId", customUserDetails.getId())
        .issuedAt(new Date())
        .expiration(expiryDate)
        .signWith(secretKey, SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * JWT 토큰에서 사용자 ID를 추출하는 메서드입니다.
   *
   * @param token JWT 토큰
   * @return 사용자 ID
   */
  public Long getUserIdFromToken(String token) {
    return Jwts
        .parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userId", Long.class);
  }


  /**
   * JWT 토큰의 유효성을 검증하는 메서드입니다.
   *
   * @param token JWT 토큰
   * @return 유효성 검사 결과 (true: 유효, false: 무효)
   */
  public Boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
      return true;

    } catch (MalformedJwtException e) {
      //토큰 형식이 잘못됨
      log.error("[" + TraceIdHolder.get() + "]:(토큰 형식이 잘못됨)");
      return false;

    } catch (ExpiredJwtException e) {
      //토큰이 만료됨
      log.error("[" + TraceIdHolder.get() + "]:(토큰이 만료됨)");
      return false;

    } catch (IllegalArgumentException e) {
      //토큰이 비어있거나 잘못된 형식
      log.error("[" + TraceIdHolder.get() + "]:(토큰이 비어있거나 잘못된 형식)");
      return false;

    } catch (SignatureException e) {
      //시그니처 검증 실패
      log.error("[" + TraceIdHolder.get() + "]:(시그니처 검증 실패)");
      return false;

    } catch (JwtException e) {
      //기타 JWT 관련 예외
      log.error("[" + TraceIdHolder.get() + "]:(기타 JWT 예외 발생)");
      return false;
    }

  }

  /**
   * 요청 헤더에서 JWT 토큰을 추출하는 메서드입니다.
   *
   * @param request HTTP 요청 객체
   * @return 요청 헤더에서 추출한 순수 JWT 토큰 문자열을 반환합니다
   */
  private String getTokenFromRequest(HttpServletRequest request) {

    // Authorization 헤더에서 JWT 토큰을 추출합니다.
    String token = request.getHeader("Authorization");

    // 토큰이 "Bearer "로 시작하는지 확인합니다.
    if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {

      //Bearer 제거하고 토큰만을 추출합니다
      token = token.substring(7);
    }

    return token;
  }

}
