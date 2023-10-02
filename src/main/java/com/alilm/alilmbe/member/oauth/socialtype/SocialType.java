package com.alilm.alilmbe.member.oauth.socialtype;

import java.util.HashMap;
import java.util.Map;

public enum SocialType {
  NAVER("naver", "id");

  private final String name;
  private final String userNameAttributeName;
  private static final Map<String, SocialType> nameToEnumMap = new HashMap<>();

  SocialType(String name, String userNameAttributeName) {
    this.name = name;
    this.userNameAttributeName = userNameAttributeName;
  }

  public String getName() {
    return name;
  }

  public String getUserNameAttributeName() {
    return userNameAttributeName;
  }

  static {
    for (SocialType type : SocialType.values()) {
      nameToEnumMap.put(type.getName(), type);
    }
  }

  public static SocialType of(String name) {
    SocialType socialType = nameToEnumMap.get(name);
    if (socialType == null) {
      throw new IllegalArgumentException("No enum constant with name " + name);
    }
    return socialType;
  }
}
