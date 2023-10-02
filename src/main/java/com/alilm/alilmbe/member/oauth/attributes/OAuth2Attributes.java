package com.alilm.alilmbe.member.oauth.attributes;

import com.alilm.alilmbe.member.domain.Member;
import com.alilm.alilmbe.member.domain.Role;
import com.alilm.alilmbe.member.oauth.socialtype.SocialType;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2Attributes {
  private Map<String, Object> attributes;
  private String nameAttributesKey;
  private String name;
  private String email;

  @Builder
  public OAuth2Attributes(Map<String, Object> attributes, String nameAttributesKey, String name, String email) {
    this.attributes = attributes;
    this.nameAttributesKey = nameAttributesKey;
    this.name = name;
    this.email = email;
  }

  public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
    SocialType socialType = SocialType.of(registrationId);

    return switch (socialType) {
      default -> ofNaver(socialType.getUserNameAttributeName(), attributes);
    };
  }

  public static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    return OAuth2Attributes.builder()
        .name((String) response.get("name"))
        .email((String) response.get("email"))
        .attributes(response)
        .nameAttributesKey(userNameAttributeName)
        .build();
  }

  public Member toEntity() {
    return Member.builder()
        .name(name)
        .email(email)
        .role(Role.GUEST)
        .build();
  }
}
