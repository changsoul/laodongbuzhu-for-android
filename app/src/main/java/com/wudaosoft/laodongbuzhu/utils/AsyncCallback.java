package com.wudaosoft.laodongbuzhu.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created on 2018/3/13 00:55.
 *
 * @author Changsoul.Wu
 */
public interface AsyncCallback {

    void onResponse(Call call, Response response) throws IOException;
}
