package com.wudaosoft.laodongbuzhu.utils;

import android.content.Context;
import android.net.Uri;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created 2018/3/13 10:08.
 *
 * @author Changsoul.Wu
 */
public class HttpRequest {

    private static HttpRequest mInstance;
    private OkHttpClient okHttpClient;

    public static HttpRequest getInstance(Context context) {
        if (mInstance == null) {
            synchronized (HttpRequest.class) {
                if (mInstance == null) {
                    mInstance = new HttpRequest(context);
                }
            }
        }
        return mInstance;
    }

    private HttpRequest(Context context) {

        CookieHandler cookieHandler = new CookieManager(new PersistentCookieStore(context),
                CookiePolicy.ACCEPT_ALL);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(7000, TimeUnit.MILLISECONDS)
                .connectTimeout(7000, TimeUnit.MILLISECONDS)
                //.addInterceptor(logging)
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .build();

        CookieUtil.setCookies(okHttpClient, "Cookie1", DomainConfig.SERVER_ID);
    }

    public Response execute(Request request) throws IOException {

        return okHttpClient.newCall(request).execute();
    }

    public String string(Request request) throws IOException {
        return execute(request).body().string();
    }

    public JSONObject json(Request request) throws IOException {
        return JSON.parseObject(string(request));
    }


    public void async(Request request, final AsyncCallback asyncCallback) throws IOException {

        okHttpClient.newCall(request).enqueue(asyncCallback);
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Request get(String url) {
        return get(url, null);
    }

    public Request get(String url, Map<String, String> params) {
        return request("GET", url, params, false);
    }

    public Request getAjax(String url) {
        return getAjax(url, null);
    }

    public Request getAjax(String url, Map<String, String> params) {
        return request("GET", url, params, true);
    }

    public Request post(String url, Map<String, String> params) {
        return request("POST", url, params, false);
    }

    public Request postAjax(String url, Map<String, String> params) {
        return request("POST", url, params, true);
    }

    public Request request(String method, String url, Map<String, String> params, boolean isAjax) {

        Request.Builder builder = new Request.Builder();

        if (isAjax) {
            setAjaxHeader(builder);
        } else {
            setHeader(builder);
        }

        RequestBody body = null;

        if (HttpMethod.permitsRequestBody(method))
            url = buildGetUrl(url, params);

        if (HttpMethod.requiresRequestBody(method)) {

            if (url.indexOf("?") != -1) {
                Uri uri = Uri.parse(url);

                url = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();

                Set<String> queries = uri.getQueryParameterNames();

                if (!queries.isEmpty()) {
                    if (params == null)
                        params = new HashMap<>();

                    for (String name : queries) {
                        params.put(name, uri.getQueryParameter(name));
                    }
                }
            }

            body = genFormParams(params);
        }

        builder.url(url).method(method, body);

        return builder.build();
    }

    public String buildUrl(String url) {
        return buildGetUrl(url, null);
    }

    public String buildGetUrl(String url, Map<String, String> params) {
        if (params == null && url.indexOf("?") == -1)
            return url;

        Uri.Builder builder = Uri.parse(url).buildUpon();

        if (params == null || params.isEmpty())
            return builder.build().toString();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String k = entry.getKey();
            if (k == null)
                continue;

            String v = entry.getValue();

            builder.appendQueryParameter(k, v != null ? v : "");
        }

        return builder.build().toString();
    }

    public FormBody genFormParams(Map<String, String> params) {
        if (params == null || params.isEmpty())
            return null;

        FormBody.Builder builder = new FormBody.Builder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String k = entry.getKey();
            if (k == null)
                continue;

            String v = entry.getValue();

            builder.add(k, v != null ? v : "");
        }

        return builder.build();
    }

    public void setHeader(Request.Builder rb) {
        setHeader(rb, null);
    }

    public void setHeader(Request.Builder rb, Headers hds) {

        Headers.Builder hb;

        if (hds == null) {
            hb = new Headers.Builder();
            hds = hb.build();
        }
        else
            hb = hds.newBuilder();

        if (hds.get("Accept") == null) {
            hb.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        }

        hb.add("Accept-Language", "zh-CN,zh;q=0.8,ja;q=0.6,en;q=0.4");
        hb.add("Cache-Control", "no-cache");
        hb.add("Connection", "close");

        hb.add("Token", CookieUtil.getCookieValue("Token", okHttpClient));

        if (hds.get("Referer") == null) {
            hb.add("Referer", "http://210.76.66.109:7006/gdweb/ggfw/web/wsyw/wsyw.do");
        }
        hb.add("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");

        rb.headers(hb.build());
    }

    public void setAjaxHeader(Request.Builder rb) {
        Headers.Builder hb = new Headers.Builder();
        hb.add("x-requested-with", "XMLHttpRequest");
        hb.add("Accept", "application/json, text/javascript, */*; q=0.01");
        hb.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //hb.add("Referer", "http://210.76.66.109:7006/gdweb/ggfw/web/pub/mainpage/mainpageldl!wsyw.do");
        hb.add("Referer", "http://210.76.66.109:7006/gdweb/ggfw/web/wsyw/app/ldlzy/gryw/grbtsb/btxx.do?MenuId=170201");

        setHeader(rb, hb.build());
    }
}
