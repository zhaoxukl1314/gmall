package com.zhaoxu.interceptors;

import com.zhaoxu.annotations.LoginRequired;
import com.zhaoxu.util.CookieUtil;
import com.zhaoxu.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
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

        String token = "";
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
        if (StringUtils.isNotBlank(oldToken)) {
            token = oldToken;
        }
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)) {
            token = newToken;
        }

        boolean loginSuccess = methodAnnotation.loginSuccess();

        String success = "fail";
        if (StringUtils.isNotBlank(token)) {
            success = HttpclientUtil.doGet("http://localhost:8085/verify?token=" + token);
        }

        if (loginSuccess) {
            if (success.equalsIgnoreCase("success")) {
                request.setAttribute("memberId", "1");
                request.setAttribute("nickname", "nickname");
                return true;
            } else {
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("http://localhost:8085/index?RetureUrl=" + requestURL);
                return false;
            }
        } else {

            if (!success.equalsIgnoreCase("success")) {
                request.setAttribute("memberId", "1");
                request.setAttribute("nickname", "nickname");
            } else {

            }
        }

        if (StringUtils.isNotBlank(token)) {
            CookieUtil.setCookie(request,response, "oldToken", token, 60*60*2, true);
        }

        return true;
    }
}
