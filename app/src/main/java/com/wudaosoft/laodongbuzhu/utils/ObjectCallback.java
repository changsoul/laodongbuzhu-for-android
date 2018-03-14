package com.wudaosoft.laodongbuzhu.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import okhttp3.Response;

/**
 * Created 2018/3/14 10:53.
 *
 * @author Changsoul.Wu
 */
public abstract class ObjectCallback<T extends Serializable> extends AsyncCallback<T> {

    @Override
    public T parse(Response response) throws IOException {

        return JSON.parseObject(response.body().string(), new TypeReference<T>(){});
    }
}
