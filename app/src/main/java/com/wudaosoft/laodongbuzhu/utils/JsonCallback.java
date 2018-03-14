package com.wudaosoft.laodongbuzhu.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created 2018/3/14 10:53.
 *
 * @author Changsoul.Wu
 */
public abstract class JsonCallback extends AsyncCallback<JSONObject>{

    @Override
    public JSONObject parse(final Response response) throws IOException {
        return JSON.parseObject(response.body().string());
    }
}
