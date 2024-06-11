package com.project.workaholic.config.security;

import com.project.workaholic.vcs.service.OAuthPrincipalUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final OAuthPrincipalUserService oAuthPrincipalUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(
                        authorizeRequest -> authorizeRequest
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/**"))
                                .permitAll()
                ).oauth2Login(
                        oauth -> oauth.defaultSuccessUrl("/success")
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(oAuthPrincipalUserService))
                ).logout(
                        logout -> logout.logoutSuccessUrl("/success")
                );

        return http.build();
    }
}


