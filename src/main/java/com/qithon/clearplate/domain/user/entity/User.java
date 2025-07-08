package com.qithon.clearplate.domain.user.entity;

import com.qithon.clearplate.global.security.core.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User {

  @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;

  @Column(length = 10, nullable = false)
  private String nickname;

  @Column(nullable = false)
  private Integer cpPoint;

  //TODO: 쿠폰 연관관계 매핑 필요
  //private Coupon coupon;

  @Enumerated(EnumType.STRING) // admin, user
  private Role role;

  @Builder
  private User(String nickname, Integer cpPoint, Role role) {
    this.nickname = nickname;
    this.cpPoint = cpPoint;
    this.role = role;
  }

  public static User of(String nickname, Integer cpPoint, Role role) {
    return User.builder()
        .nickname(nickname)
        .cpPoint(cpPoint)
        .role(role)
        .build();
  }











}
