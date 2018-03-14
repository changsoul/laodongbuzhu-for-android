package com.wudaosoft.laodongbuzhu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.Response;

/**
 * Created 2018/3/14 10:53.
 *
 * @author Changsoul.Wu
 */
public abstract class BitmapCallback extends AsyncCallback<Bitmap> {


    private int mTargetWidth;
    private int mTargetHeight;

    public BitmapCallback() {
    }

    public BitmapCallback(int targetWidth, int targetHeight) {
        mTargetWidth = targetWidth;
        mTargetHeight = targetHeight;
    }

    public BitmapCallback(ImageView imageView) {
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        if (width <= 0 || height <= 0) {
            throw new RuntimeException("无法获取ImageView的width或height");
        }
        mTargetWidth = width;
        mTargetHeight = height;
    }

    @Override
    public Bitmap parse(Response response) throws IOException {
        if (mTargetWidth == 0 || mTargetHeight == 0) {
            return BitmapFactory.decodeStream(response.body().byteStream());
        } else {
            return getZoomBitmap(response);
        }
    }

    /**
     * 压缩图片
     */
    private Bitmap getZoomBitmap(Response response) throws IOException {

        byte[] data = response.body().bytes();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        int sampleSize = 1;
        int heightRatio = (int) Math.floor((float) picWidth / (float) mTargetWidth);
        int widthRatio = (int) Math.floor((float) picHeight / (float) mTargetHeight);
        if (heightRatio > 1 || widthRatio > 1) {
            sampleSize = Math.max(heightRatio, widthRatio);
        }

        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        if (bitmap == null) {
            throw new RuntimeException("Failed to decode stream.");
        }

        return bitmap;
    }

}