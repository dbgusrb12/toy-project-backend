package com.hg.blog.config;

import static com.hg.blog.constants.Constants.USER_ID;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.hg.blog.annotation.Permit;
import com.hg.blog.annotation.Role;
import com.hg.blog.util.JWTProvider;
import java.security.AccessControlException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

public class Interceptor implements HandlerInterceptor {


    /**
     * 컨트롤러 이전에 실행 되는 메서드
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), null);
        }

        Permit permit = ((HandlerMethod) handler).getMethodAnnotation(Permit.class);
        if (Objects.isNull(permit)) {
            return true;
        }

        Role role = permit.role();
        if (role.permitAll()) {
            return true;
        }

        String token = request.getHeader(AUTHORIZATION);
        checkTokenValidate(token);
        request.setAttribute(USER_ID, JWTProvider.getUserIdFromJWT(token));
        return true;
    }

    private void checkTokenValidate(String token) {
        if (isBlankString(token) || !JWTProvider.validateToken(token)) {
            throw new AccessControlException("Token is not valid");
        }
    }

    private boolean isBlankString(String str) {
        return str == null || str.isBlank();
    }

    /**
     * 컨트롤러 이후에 실행 되는 메서드 Dispatcher Servlet 이 화면을 처리하기 전에 동작.
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
    }
}
