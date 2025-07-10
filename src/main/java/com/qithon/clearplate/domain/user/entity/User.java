package com.qithon.clearplate.domain.user.entity;

import com.qithon.clearplate.domain.coupon.entity.Coupon;
import com.qithon.clearplate.global.security.core.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String socialEmail;

  @Column(length = 10, nullable = false)
  private String nickname;

  @Column(nullable = false)
  private Integer cpPoint;


  @Enumerated(EnumType.STRING) // admin, user, owner
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Coupon> coupons = new ArrayList<>();

  @Builder
  private User(String socialEmail, String nickname, Integer cpPoint, Role role,
      List<Coupon> coupons) {
    this.socialEmail = socialEmail;
    this.nickname = nickname;
    this.cpPoint = cpPoint;
    this.role = role;
    this.coupons = coupons;
  }

  /**
   * 일반 사용자를 생성하는 메서드 입니다
   *
   * @param nickname 소셜로그인 후 반환되는 사용자 닉네임 입니다.
   * @param cpPoint  서비스에서 사용되는 포인트 입니다
   * @return 생성된 User 객체
   */
  //TODO: 쿠폰 연관관계 매핑 후 메서드 인자로 쿠폰 객체를 추가해야함.
  public static User createStandardUserOf(String socialEmail, String nickname, Integer cpPoint,
      List<Coupon> coupons) {
    return User.builder()
        .socialEmail(socialEmail)
        .nickname(nickname)
        .cpPoint(cpPoint)
        .role(Role.ROLE_USER)
        .coupons(coupons)
        .build();
  }


  /**
   * 사장님 사용자를 생성하는 메서드 입니다
   *
   * @param nickname 소셜로그인 후 반환되는 사용자 닉네임 입니다.
   * @param cpPoint  서비스에서 사용되는 포인트 입니다
   * @return 생성된 User 객체
   */
  //TODO: 쿠폰 연관관계 매핑 후 메서드 인자로 쿠폰 객체를 추가해야함.
  public static User createOwnerUserOf(String socialEmail, String nickname, Integer cpPoint) {
    return User.builder()
        .socialEmail(socialEmail)
        .nickname(nickname)
        .cpPoint(cpPoint)
        .role(Role.ROLE_OWNER)
        .coupons(null)
        .build();
  }


  /**
   * 어드민 사용자를 생성하는 메서드 입니다
   *
   * @param nickname 소셜로그인 후 반환되는 사용자 닉네임 입니다.
   * @param cpPoint  서비스에서 사용되는 포인트 입니다
   * @return 생성된 User 객체
   */
  //TODO: 쿠폰 연관관계 매핑 후 메서드 인자로 쿠폰 객체를 추가해야함.
  public static User createAdminUserOf(String socialEmail, String nickname, Integer cpPoint) {
    return User.builder()
        .socialEmail(socialEmail)
        .nickname(nickname)
        .cpPoint(cpPoint)
        .role(Role.ROLE_ADMIN)
        .coupons(null)
        .build();

  }
}
