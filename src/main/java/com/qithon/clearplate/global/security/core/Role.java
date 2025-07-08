package com.qithon.clearplate.global.security.core;

public enum Role {

  ROLE_USER("USER"),
  ROLE_ADMIN("ADMIN"),
  ROLE_OWNER("OWNER");
  private String role;

  Role(String role) {
    this.role = role;
  }

  public String getRole() {
    return this.role;
  }
}
