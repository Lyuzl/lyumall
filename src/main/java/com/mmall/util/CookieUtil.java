package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by Christophe
 * 2018/5/8 下午8:23
 */
@Slf4j
public class CookieUtil {
    private static final String COOKIE_DOMAIN = ".lyu.com";
    private static final String COOKIE_NAME = "lyu_login_token";

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for(Cookie ck : cks){
                log.info("read Cookie name:{}, Cookie value:{}", ck.getName(), ck.getValue());
                if(StringUtils.equals(ck.getName(), COOKIE_NAME)){
                    log.info("return Cookie name:{}, Cookie value:{}", ck.getName(), ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/"); //代表设置在根目录

        //单位是秒
        //如果不设置maxAge，Cookie就不会写入硬盘，而是写在内存，只在当前页面有效
        ck.setMaxAge(60 * 60 * 24 * 365);  //如果是-1代表永久
        log.info("write Cookie name:{}, Cookie value:{}", ck.getName(), ck.getValue());
        response.addCookie(ck);
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for(Cookie ck : cks){
                if(StringUtils.equals(ck.getName(), COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0); //设置成0，代表删除此Cookie
                    log.info("delete Cookie name:{}, Cookie value:{}", ck.getName(), ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }
}
