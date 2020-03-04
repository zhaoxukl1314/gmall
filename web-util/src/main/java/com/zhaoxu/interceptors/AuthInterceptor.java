package com.zhaoxu.interceptors;

import com.zhaoxu.annotations.LoginRequired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(LoginRequired.class);
        if ((methodAnnotation == null)) {
            return true;
        }



        return true;
    }
}