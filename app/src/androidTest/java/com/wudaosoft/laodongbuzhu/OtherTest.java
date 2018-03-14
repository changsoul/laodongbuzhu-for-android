package com.wudaosoft.laodongbuzhu;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created on 2018/3/15 01:15.
 *
 * @author Changsoul.Wu
 */
@RunWith(AndroidJUnit4.class)
public class OtherTest {

    private static final String TAG = "OtherTest";

    @Test
    public void testNet() throws Exception {

        Uri uri = Uri.parse("https://wwww.baidu.com:999/?asfasdf=中文");

        String path = uri.getScheme() + "://" + uri.getAuthority();

        Log.d(TAG, "path: " + path);

        assertEquals(path, "https://wwww.baidu.com:999");
        assertEquals(uri.getQueryParameter("asfasdf"), "中文");
    }
}
