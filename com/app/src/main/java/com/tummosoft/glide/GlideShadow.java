package com.tummosoft.glide;

import java.security.MessageDigest;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import anywheresoftware.b4a.BA;


public class GlideShadow extends BitmapTransformation {
    // The version of this transformation, incremented to correct an error in a previous version.
    // See #455.
    private static final int VERSION = 1;
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.CircleCrop." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private int mBorderWidth = 0;
    private int mColor = 0;
    private int mRadius = 0;
        
    public GlideShadow(int color) {
        mColor = color;
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof GlideShadow;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
        
    protected Bitmap addShadow(Bitmap srcBitmap, int borderWidth, int borderColor, int radius){
        Bitmap bmpWithBorder = Bitmap.createBitmap(srcBitmap.getWidth() + (mBorderWidth*2), srcBitmap.getHeight() + (mBorderWidth*2), srcBitmap.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        Bitmap shadown = createShadowBitmap(srcBitmap.getWidth() + (mBorderWidth*2), srcBitmap.getHeight() + (mBorderWidth*2), 10, 10, 5, 5, Color.BLUE, Color.TRANSPARENT);
        
        canvas.drawBitmap(shadown, mBorderWidth + 10, mBorderWidth + 10, null);
        canvas.drawBitmap(srcBitmap, mBorderWidth, mBorderWidth, null);

        return bmpWithBorder;         
    }   
    
    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int shadowColor, int fillColor) {

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(output);

        RectF shadowRect = new RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius);

        if (dy > 0) {
            shadowRect.top += dy;
            shadowRect.bottom -= dy;
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy);
            shadowRect.bottom -= Math.abs(dy);
        }

        if (dx > 0) {
            shadowRect.left += dx;
            shadowRect.right -= dx;
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx);
            shadowRect.right -= Math.abs(dx);
        }

        Paint shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(fillColor);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);

        return output;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap sourceBitmap, int outWidth, int outHeight) {
        Bitmap mBitmap = addShadow(sourceBitmap, mBorderWidth, mColor, mRadius);
        return mBitmap;
    }
}