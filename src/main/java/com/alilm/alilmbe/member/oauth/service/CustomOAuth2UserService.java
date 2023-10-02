package com.alilm.alilmbe.member.oauth.service;

import com.alilm.alilmbe.member.domain.Member;
import com.alilm.alilmbe.member.domain.MemberRepository;
import com.alilm.alilmbe.member.oauth.attributes.OAuth2Attributes;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = service.loadUser(userRequest);

    Map<String, Object> originAttributes = oAuth2User.getAttributes();

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();

    OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName,
        originAttributes);

    Member member = saveOrUpdate(attributes);
    return new DefaultOAuth2User(
      Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
      attributes.getAttributes(),
      attributes.getNameAttributesKey()
    );
  }

  private Member saveOrUpdate(OAuth2Attributes attributes) {
    Member user = memberRepository.findByEmail(attributes.getEmail())
        .map(entity -> entity.update(attributes.getName(), attributes.getEmail()))
        .orElse(attributes.toEntity());

    return memberRepository.save(user);
  }
}
