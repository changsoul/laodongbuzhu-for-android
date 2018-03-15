package com.wudaosoft.laodongbuzhu;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.wudaosoft.laodongbuzhu.utils.BitmapCallback;
import com.wudaosoft.laodongbuzhu.utils.DomainConfig;
import com.wudaosoft.laodongbuzhu.utils.HttpRequest;
import com.wudaosoft.laodongbuzhu.utils.StringCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Request;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class OkHttpTest {

    private static final String TAG = "OkHttpTest";

    private Context appContext;
    private HttpRequest http;

    @Before
    public void setUp() throws Exception {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();
        http = HttpRequest.getInstance(appContext);
    }

    @Test
    public void testString() throws Exception {
        Request req = new Request.Builder().url(DomainConfig.LOGIN_PAGE).build();

        http.async(req, new StringCallback() {
            @Override
            public void onFail(Call call, Exception e) {
                assertNull(e);
                Log.d(TAG, "testString onFail: " + e.getMessage());
            }

            @Override
            public void onSuccess(Call call, String response) {
                assertNotNull(response);
                Log.d(TAG, "testString onSuccess: " + response);
            }
        });
    }

    @Test
    public void testImage() throws Exception {

        Request req = new Request.Builder().url(DomainConfig.IMAGE_CHECK).build();

        http.async(req, new BitmapCallback() {
            @Override
            public void onFail(Call call, Exception e) {
                assertNull(e);
                Log.d(TAG, "testImage onFail: " + e.getMessage());
            }

            @Override
            public void onSuccess(Call call, Bitmap response) {
                assertNotNull(response);
                Log.d(TAG, "testImage onSuccess: width:" + response.getWidth() + " height:" + response.getHeight());
            }
        });

    }

    @Test
    public void testCookie() throws Exception {

        printCookies();

        Request req = new Request.Builder().url(DomainConfig.LOGIN_PAGE).build();

        http.async(req, new StringCallback() {
            @Override
            public void onFail(Call call, Exception e) {
                assertNull(e);
                Log.d(TAG, "testString onFail: " + e.getMessage());
            }

            @Override
            public void onSuccess(Call call, String response) {
                assertNotNull(response);
                Log.d(TAG, "testString onSuccess: " + response);

                printCookies();
            }
        });
    }

    private void printCookies() {
        List<Cookie> cookies = http.getOkHttpClient().cookieJar().loadForRequest(HttpUrl.parse(DomainConfig.DOMAIN));

        for (Cookie co : cookies) {
            Log.i(TAG, "printCookies: name:" + co.name() + " value:" + co.value());
        }
    }
}
