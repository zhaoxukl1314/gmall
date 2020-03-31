package com.zhaoxu.interceptors;

import com.alibaba.fastjson.JSON;
import com.zhaoxu.annotations.LoginRequired;
import com.zhaoxu.util.CookieUtil;
import com.zhaoxu.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        if (handler instanceof ParameterizableViewController) {
            return true;
        }

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
        Map<String,String> successMap = new HashMap<>();
        if (StringUtils.isNotBlank(token)) {
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            String successJson  = HttpclientUtil.doGet("http://localhost:8085/verify?token=" + token+"&currentIp="+ip);
            successMap = JSON.parseObject(successJson,Map.class);
            success = successMap.get("status");
        }

        if (loginSuccess) {
            if (success.equalsIgnoreCase("success")) {
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickname", successMap.get("nickname"));
                return true;
            } else {
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("http://localhost:8085/index?RetureUrl=" + requestURL);
                return false;
            }
        } else {

            if (!success.equalsIgnoreCase("success")) {
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickname", successMap.get("nickname"));
            } else {

            }
        }

        if (StringUtils.isNotBlank(token)) {
            CookieUtil.setCookie(request,response, "oldToken", token, 60*60*2, true);
        }

        return true;
    }
}
