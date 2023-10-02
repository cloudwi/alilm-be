package com.alilm.alilmbe.member.domain;

import com.alilm.alilmbe.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Builder
  public Member(Long id, String name, String email, Role role) {
    this.name = name;
    this.email = email;
    this.role = role;
  }

  public Member update(String name, String email) {
    this.name = name;
    this.email = email;
    return this;
  }

  public String getRoleKey() {
    return this.role.getKey();
  }
}
