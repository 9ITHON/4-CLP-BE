package com.qithon.clearplate.global.oauth2;

import com.qithon.clearplate.domain.user.entity.User;
import com.qithon.clearplate.domain.user.repository.UserRepository;
import com.qithon.clearplate.global.security.core.CustomUserDetails;
import com.qithon.clearplate.global.security.core.Role;
import com.qithon.clearplate.global.security.jwt.JwtTokenProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

   private final UserRepository userRepository;

   // jwt 토큰 발급을 위한 유틸 객체
   private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.accessTokenExpirationTime}")
    private Long jwtAccessTokenExpirationTime;
    @Value("${jwt.refreshTokenExpirationTime}")
    private Long jwtRefreshTokenExpirationTime;




    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String socialEmail, nickname;



        if ("kakao".equals(provider)) {

          // 고유 id + "@kakao" 형태로 고유한 소셜 이메일을 생성합니다
            socialEmail = attributes.get("id").toString()+"@kakao";

            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            nickname = (String) profile.get("nickname");

        } else {
            // 기타 provider 처리 안함
            socialEmail = null;
            nickname = null;
        }

        System.out.println(provider+" 로그인 확인 socialEmail = " + socialEmail);
        System.out.println(provider+" 로그인 확인 nickname = " + nickname);

        // 회원 정보가 DB에 존재하는지 확인
        User user = userRepository.findBySocialEmail(socialEmail)
                .orElseGet(() -> {
                    // 회원이 없다면 자동 회원가입 처리
                    return userRepository.save(User.createStandardUserOf(
                        socialEmail,
                        nickname,
                        0
                    ));   // 회원가입 저장
                });

        // 시큐리티에서 사용할 인증 객체 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails,
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));


        // JWT 액세스 & 리프레시 토큰 발급
        String accessToken = jwtTokenProvider.createToken(authentication,jwtAccessTokenExpirationTime);
        String refreshToken = jwtTokenProvider.createToken(authentication,jwtRefreshTokenExpirationTime);


        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("accessToken", accessToken);
        customAttributes.put("refreshToken", refreshToken);
        customAttributes.put("id", user.getId());

        // 최종적으로 Spring Security에 전달할 OAuth2User 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                customAttributes,
                "id"
        );
    }
}