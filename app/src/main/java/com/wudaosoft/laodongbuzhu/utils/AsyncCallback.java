package com.wudaosoft.laodongbuzhu.utils;

import android.os.Handler;
import android.os.Looper;

import com.wudaosoft.laodongbuzhu.exception.ServiceException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created on 2018/3/13 00:55.
 *
 * @author Changsoul.Wu
 */
public abstract class AsyncCallback<T> implements Callback {

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    @Override
    public final void onFailure(final Call call, final IOException e) {
        postFail(call, e);
    }

    @Override
    public final void onResponse(final Call call, final Response response) throws IOException {

        if (response.isRedirect())
            return;

        if (response.isSuccessful()) {

            try {

                final T result = parse(response);

                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            onSuccess(call, result);
                        } catch (Exception e) {
                            postFail(call, e);
                        }
                    }
                });
            } catch (Exception e) {
                postFail(call, e);
            }

            return;
        }

        int statusCode = response.code();

        if (statusCode == 400)
            postFail(call, new ServiceException("400 Bad Request"));
        else if (statusCode == 403)
            postFail(call, new ServiceException("403 Forbidden"));
        else if (statusCode == 404)
            postFail(call, new ServiceException("404 Notfound"));
        else if (statusCode == 500)
            postFail(call, new ServiceException("500 500 Internal Server Error"));
        else if (statusCode == 502)
            postFail(call, new ServiceException("502 Bad Gateway"));
        else if (statusCode < 200 || statusCode >= 300)
            postFail(call, new ServiceException(statusCode + " Request Fail"));

    }

    private void postFail(final Call call, final Exception e) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onFail(call, e);
            }
        });
    }

    public abstract <T> T parse(final Response response) throws IOException;

    public abstract void onFail(final Call call, final Exception e);

    public abstract void onSuccess(final Call call, final T response);

}
