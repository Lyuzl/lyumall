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
    //X:domain=".happymmall.com"
    //a:A.happymmall.com            cookie:domain=A.happymmall.com;path="/"
    //b:B.happymmall.com            cookie:domain=B.happymmall.com;path="/"
    //c:A.happymmall.com/test/cc    cookie:domain=A.happymmall.com;path="/test/cc"
    //d:A.happymmall.com/test/dd    cookie:domain=A.happymmall.com;path="/test/dd"
    //e:A.happymmall.com/test       cookie:domain=A.happymmall.com;path="/test"

    /**
     * a,b,c,d,e都能拿到种在".happymmall.com"一级域名下的Cookie
     * a,b都是种的二级域名下的Cookie，所以互相拿不到Cookie
     * c,d能够共享a的Cookie，由于c,d的path设置，c,d也能共享e的Cookie；但是c拿不到d的，d也拿不到c的
     * c和d也拿不到b的
     */

    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/"); //代表设置在根目录
        ck.setHttpOnly(true);

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
