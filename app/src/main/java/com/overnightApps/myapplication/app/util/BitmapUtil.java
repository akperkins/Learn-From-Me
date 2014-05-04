package com.overnightApps.myapplication.app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by andre on 5/1/14.
 */
public class BitmapUtil {
    public static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baoStream);
        return baoStream.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] data) {
        if(data == null){
            return null;
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap) {
        final double MAXIMUM_WIDTH = 100;
        final double MAXIMUM_HEIGHT = 100;
        double scaleFactorWidth = MAXIMUM_WIDTH/bitmap.getWidth();
        double scaleFactorHeight = MAXIMUM_HEIGHT/bitmap.getHeight();
        double finalScaleFactor = Math.min(scaleFactorWidth,scaleFactorHeight);
        int finalWidth = (int)Math.floor(finalScaleFactor*bitmap.getWidth());
        int finalHeight = (int)Math.floor(finalScaleFactor*bitmap.getHeight());
        bitmap = Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, false);
        return bitmap;
    }
}
