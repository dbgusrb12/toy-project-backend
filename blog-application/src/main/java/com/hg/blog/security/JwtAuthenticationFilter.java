package com.hg.blog.security;

import static com.hg.blog.constants.Constants.TOKEN_TYPE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.hg.blog.util.JWTProvider;
import java.io.IOException;
import java.security.AccessControlException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JwtAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        this.authenticate(request);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        final String requestURI = request.getRequestURI();

        final String excludeUrl = "/api/accounts";
        return requestURI.startsWith(excludeUrl);
    }

    private void authenticate(HttpServletRequest request) {
        final String token = this.getToken(request);
        if (token == null) {
            return;
        }

        checkValidToken(token);
        processSecurity(request, token);
    }

    private String getToken(HttpServletRequest request) {
        final String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(token)) {
            return token.replace(TOKEN_TYPE + " ", "");
        }
        return null;
    }

    private void checkValidToken(String token) {
        if (isBlankString(token) || !JWTProvider.validateToken(token)) {
            throw new AccessControlException("Token is not valid");
        }
    }

    private boolean isBlankString(String str) {
        return str == null || str.isBlank();
    }

    /**
     * 토큰 기반 유저 정보 조회 후 SecurityContextHolder 에 전달
     *
     * @param request
     * @param token
     */
    private void processSecurity(HttpServletRequest request, String token) {
        final String userId = JWTProvider.getUserIdFromJWT(token);
        UserDetails userDetails = userService.loadUserByUsername(userId);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
