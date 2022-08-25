/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tummosoft.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import java.security.MessageDigest;

public class CircleCropBorder extends BitmapTransformation {
    // The version of this transformation, incremented to correct an error in a previous version.
    // See #455.
    private static final int VERSION = 1;
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.CircleCrop." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private int mBorderWidth = 0;
    private int mColor = 0;
    private int mBackground;
    
    public CircleCropBorder(int borderWidth, int color, int bacgroundColor) {
        mBorderWidth = borderWidth;
        mColor = color;
        mBackground = bacgroundColor;
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof CircleCropBorder;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    // Custom method to add a border around circular bitmap
    protected Bitmap addBorderToCircularBitmap(Bitmap srcBitmap, int borderWidth, int borderColor){
        // Calculate the circular bitmap width with border
        int dstBitmapWidth = srcBitmap.getWidth()+borderWidth*2;

        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(dstBitmap);
        Paint paint = new Paint();        
        paint.setAntiAlias(true);

        if (mBackground != Color.parseColor("#00000000")) {
            paint.setColor(mBackground);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(
                canvas.getWidth() / 2, // cx
                canvas.getWidth() / 2, // cy
                canvas.getWidth()/2 - borderWidth / 2, // Radius
                paint // Paint
            );
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mColor);        
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(
                canvas.getWidth() / 2, // cx
                canvas.getWidth() / 2, // cy
                canvas.getWidth()/2 - borderWidth / 2, // Radius
                paint // Paint
            );
        
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
        srcBitmap.recycle();
        return dstBitmap;
    }   

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap sourceBitmap, int outWidth, int outHeight) {
        Bitmap circle = TransformationUtils.circleCrop(pool, sourceBitmap, outWidth, outHeight);
        
        Bitmap mBitmap = addBorderToCircularBitmap(circle, mBorderWidth, mColor);
        return mBitmap;
    }
}