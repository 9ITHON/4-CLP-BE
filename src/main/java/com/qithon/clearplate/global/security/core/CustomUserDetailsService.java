package com.qithon.clearplate.global.security.core;

import com.qithon.clearplate.domain.user.entity.User;
import com.qithon.clearplate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * 사용자 인증 객체를 생성하는 메서드입니다.
   *
   * @param SocialEmail 사용자 식별자인 소셜 이메일 값이 들어갑니다.
   * @return CustomUserDetails 객체
   * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
   */
  @Override
  public UserDetails loadUserByUsername(String SocialEmail) throws UsernameNotFoundException {
    User user = userRepository.findBySocialEmail(SocialEmail).orElseThrow(
        () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + SocialEmail));
    return new CustomUserDetails(user);
  }


  /**
   * 사용자 ID로 사용자 인증 객체를 생성하는 메서드입니다.
   *
   * @param id 사용자 ID
   * @return CustomUserDetails 객체
   * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
   */
  public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
    User user = userRepository.findById(id).orElseThrow(
        () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + id));
    return new CustomUserDetails(user);
  }
}