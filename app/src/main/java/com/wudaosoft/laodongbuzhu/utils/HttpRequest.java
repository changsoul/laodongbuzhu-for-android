package com.wudaosoft.laodongbuzhu.utils;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2018/3/13.
 */

public class HttpRequest {

    private static HttpRequest mInstance;
    private Context context;
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

    public HttpRequest(Context context) {
        this.context = context;

        CookieHandler cookieHandler = new CookieManager(new PersistentCookieStore(context),
                CookiePolicy.ACCEPT_ALL);


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(7676, TimeUnit.MILLISECONDS)
                .connectTimeout(7676, TimeUnit.MILLISECONDS)
                .addInterceptor(logging)
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .build();

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

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "网络请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                asyncCallback.onResponse(call, response);
            }
        });
    }

}
