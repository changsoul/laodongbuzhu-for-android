package com.wudaosoft.laodongbuzhu.utils;

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
}
