package com.example.jitpms.util;

import com.example.jitpms.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
        preHandle：在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制、权限校验等处理；
        postHandle：在业务处理器处理请求执行完成后，生成视图之前执行。后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView；
        afterCompletion：在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
*/
public class HandlerInterceptorUtil implements HandlerInterceptor {

    /**
     * 请求调用前执行
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        System.out.println("preHandle");
        User user = (User) request.getSession().getAttribute("userInfo");
        System.out.println(user);
        if (user == null || user.getS_id() != 1) {
            response.sendRedirect("/login.html");
            return false;
        }
        return true; //返回true则继续执行后续的请求,false就拦截请求
    }

    /**
     * 请求完成前执行,还没有真正响应到client端执行
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
        //modelAndView.setViewName("redirect:/upload.jsp");
    }

    /**
     * 请求完成后执行
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("afterCompletion");
    }
}
