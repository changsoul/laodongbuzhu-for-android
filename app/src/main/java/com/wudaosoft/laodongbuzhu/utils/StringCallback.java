package com.wudaosoft.laodongbuzhu.utils;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created 2018/3/14 10:53.
 *
 * @author Changsoul.Wu
 */
public abstract class StringCallback extends AsyncCallback<String> {

    @Override
    public String parse(Response response) throws IOException {
        return response.body().string();
    }
}
