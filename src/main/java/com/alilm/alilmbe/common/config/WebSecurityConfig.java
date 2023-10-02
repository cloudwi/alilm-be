package com.alilm.alilmbe.common.config;

import com.alilm.alilmbe.member.domain.Role;
import com.alilm.alilmbe.member.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .cors((corsCustomizer) -> {
          corsCustomizer.configurationSource(httpServletRequest -> {
            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
            corsConfiguration.addAllowedOrigin("*");
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.addAllowedHeader("*");
            return corsConfiguration;
          });
        })
        .headers(AbstractHttpConfigurer::disable)
        .sessionManagement(
            httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
          authorizationManagerRequestMatcherRegistry.requestMatchers("/api/**")
              .hasRole(Role.USER.name());
          authorizationManagerRequestMatcherRegistry.anyRequest().permitAll();
        })
        .oauth2Login(
            httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                .userInfoEndpoint(
                    userInfoEndpointConfigurer -> userInfoEndpointConfigurer
                        .userService(customOAuth2UserService)
                )
        );

    return http.build();
  }

}
