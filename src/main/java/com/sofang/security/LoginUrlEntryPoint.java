package com.sofang.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于角色的登录入口控制器
 * Created by gegf
 */
public class LoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private PathMatcher pathMatcher = new AntPathMatcher();

    private final Map<String, String> authEntryMap;

    public LoginUrlEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
        authEntryMap = new HashMap<>();
        //普通用户登录入口
        authEntryMap.put("/user/**", "/user/login");
        //管理员登录入口
        authEntryMap.put("/admin/**", "/admin/login");
    }

    /**
     * 根据请求跳转到指定页面
     * @param request
     * @param response
     * @param exception
     * @return
     */
    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request,
          HttpServletResponse response, AuthenticationException exception) {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        for (Map.Entry<String, String> auth : this.authEntryMap.entrySet()){
            if (this.pathMatcher.match(auth.getKey(), uri)){
                return auth.getValue();
            }
        }
        return super.determineUrlToUseForThisRequest(request, response, exception);
    }
}
