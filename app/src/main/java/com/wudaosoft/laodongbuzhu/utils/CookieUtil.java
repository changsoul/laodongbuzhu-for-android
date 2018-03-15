package com.wudaosoft.laodongbuzhu.utils;

import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

/**
 * @author Changsoul Wu
 */
public class CookieUtil {

    public static String getCookieValue(String name, OkHttpClient okHttpClient) {

        if (okHttpClient == null || name == null)
            return null;

        JavaNetCookieJar cookieJar = (JavaNetCookieJar) okHttpClient.cookieJar();

        List<Cookie> cookies = cookieJar.loadForRequest(HttpUrl.parse(DomainConfig.LOGIN_PAGE));

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.name()))
                return cookie.value();
        }

        return null;
    }

    public static void setCookies(OkHttpClient okHttpClient, String name, String value) {

        HttpUrl httpUrl = HttpUrl.parse(DomainConfig.DOMAIN.endsWith("/") ? DomainConfig.DOMAIN : DomainConfig.DOMAIN + "/");
        Cookie cookie = new Cookie.Builder().name(name).value(value).domain(httpUrl.host()).path("/").build();

        okHttpClient.cookieJar().saveFromResponse(httpUrl, Collections.singletonList(cookie));
    }

    public static void setCookies(OkHttpClient okHttpClient, Cookie cookie) {
        okHttpClient.cookieJar().saveFromResponse(HttpUrl.parse(DomainConfig.DOMAIN.endsWith("/") ? DomainConfig.DOMAIN : DomainConfig.DOMAIN + "/"), Collections.singletonList(cookie));
    }

}
