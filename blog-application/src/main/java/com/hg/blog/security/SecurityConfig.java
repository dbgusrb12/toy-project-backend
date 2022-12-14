package com.hg.blog.security;

import com.hg.blog.constants.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
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

    /**
     * Resource ??? SecurityFilterChain ??? ??????
     *
     * @param http: http security
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    @Order(0)
    public SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http.requestMatchers(matchers -> matchers
                .antMatchers(
                    "/swagger*/**",
                    "/v3/api-docs*/**")
                .requestMatchers(
                    PathRequest
                        .toStaticResources()
                        .atCommonLocations()
                )
            )
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .requestCache(RequestCacheConfigurer::disable)
            .securityContext(AbstractHttpConfigurer::disable)
            .sessionManagement(AbstractHttpConfigurer::disable)
            .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // ????????? origin ??? ??????
        http.headers(headerConfig ->
            headerConfig.frameOptions(FrameOptionsConfig::sameOrigin));

        // csrf(html tag ??? ?????? ??????) disabled
        http.csrf(AbstractHttpConfigurer::disable);

        // http basic disabled (?????? ????????? enable => ???????????? id, pwd ?????? ?????????????????? ??????????????? ???)
        http.httpBasic(AbstractHttpConfigurer::disable);

        // security ?????? logout ???????????? ??????
        http.logout(AbstractHttpConfigurer::disable);

        // JWT ????????? ???????????? ????????? session ?????? ???????????? ??????
        http.sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // url ?????? ??????
        http.authorizeRequests(authorizeRequests ->
            authorizeRequests
                .antMatchers(ACCOUNT_WILDCARD_API).permitAll()
                .anyRequest().hasRole("USER")
        );

        // security error handle
        http.exceptionHandling(exceptionHandling ->
            exceptionHandling
                .authenticationEntryPoint(new CustomEntryPoint(resolver)) // ????????? ?????? ???????????? ?????? ????????? ????????? handler ??? ???????????? ???
                .accessDeniedHandler(new JwtAccessDeniedHandler(resolver)) // ????????? ??? ???????????? ?????? ????????? ????????? ?????? ?????? ???
        );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // jwt auth filter
            .addFilterBefore(authenticationWrapperFilter(), JwtAuthenticationFilter.class); // jwt auth filter wrapper

        return http.build();
    }
}
