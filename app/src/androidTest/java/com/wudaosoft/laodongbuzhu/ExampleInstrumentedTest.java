package com.wudaosoft.laodongbuzhu;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wudaosoft.laodongbuzhu.utils.BitmapCallback;
import com.wudaosoft.laodongbuzhu.utils.DomainConfig;
import com.wudaosoft.laodongbuzhu.utils.HttpRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.Call;
import okhttp3.Request;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Context appContext;

    @Before
    public void setUp() throws Exception {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testImage() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        HttpRequest http = HttpRequest.getInstance(appContext);

        Request req = new Request.Builder().url("http://" + DomainConfig.DOMAIN + DomainConfig.IMAGE_CHECK).build();


        http.async(req, new BitmapCallback() {
            @Override
            public void onFail(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, Bitmap response) {
                assertNull(response);
            }
        });

    }
}
