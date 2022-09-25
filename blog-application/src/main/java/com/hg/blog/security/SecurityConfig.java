package com.hg.blog.security;

import com.hg.blog.constants.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final HandlerExceptionResolver resolver;
    private final UserService userService;
    private static final String ACCOUNT_WILDCARD_API = Constants.API_PREFIX + Constants.ACCOUNT_API + "/**";

    public SecurityConfig(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
        UserService userService) {
        this.resolver = resolver;
        this.userService = userService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userService);
    }

    @Bean
    public AuthenticationWrapperFilter authenticationWrapperFilter() {
        return new AuthenticationWrapperFilter(resolver);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web ->
            web.ignoring()
                .antMatchers(
                    "/swagger*/**",
                    "/v3/api-docs*/**")
                .requestMatchers(
                    PathRequest
                        .toStaticResources()
                        .atCommonLocations()
                );
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // cors 활성화
        http.cors();
        // csrf(html tag를 통한 공격) disabled
        http.csrf(AbstractHttpConfigurer::disable);
        // http basic disabled (id, pwd 기반)
        http.httpBasic(AbstractHttpConfigurer::disable);
        // security 제공 logout 사용하지 않음
        http.logout(AbstractHttpConfigurer::disable);
        // session 방식 사용하지 않음
        http.sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // url 권한 설정
        http.authorizeRequests(authorizeRequests ->
            authorizeRequests
                .antMatchers(ACCOUNT_WILDCARD_API).permitAll()
                .anyRequest().hasRole("USER")
        );

        http.exceptionHandling(exceptionHandling ->
            exceptionHandling
                .authenticationEntryPoint(new CustomEntryPoint(resolver)) // 인증이 안된 사용자가 특정 권한이 필요한 handler 에 접근했을 때
                .accessDeniedHandler(new JwtAccessDeniedHandler(resolver)) // 인증이 된 사용자가 특정 권한을 가지고 있지 않을 때
        );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // jwt auth filter
            .addFilterBefore(authenticationWrapperFilter(), JwtAuthenticationFilter.class); // jwt auth filter wrapper

        return http.build();
    }
}
