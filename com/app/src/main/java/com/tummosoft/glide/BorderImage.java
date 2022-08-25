package com.tummosoft.glide;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import androidx.annotation.NonNull;
import anywheresoftware.b4a.BA;

import static com.bumptech.glide.load.Key.CHARSET;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;
import com.bumptech.glide.util.Util;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class BorderImage extends BitmapTransformation {
    // The version of this transformation, incremented to correct an error in a previous version.
    // See #455.
    private static final int VERSION = 1;
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.CircleCrop." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private int mBorderWidth = 0;
    private int mColor = 0;
    private int mRadius = 0;
    private int mBackground;
        
    public BorderImage(int borderWidth, int color, int radius, int bgk) {
        mBorderWidth = borderWidth;
        mColor = color;
        mRadius = radius;
        mBackground = bgk;  
            
    }
    
    protected Bitmap addBorder(Bitmap srcBitmap, int borderWidth, int borderColor, int radius){
        Bitmap bmpWithBorder = Bitmap.createBitmap(srcBitmap.getWidth() + (mBorderWidth*2), srcBitmap.getHeight() + (mBorderWidth*2), srcBitmap.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        Paint mpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mpaint.setAntiAlias(true);

        canvas.drawBitmap(srcBitmap, mBorderWidth, mBorderWidth, null);

        if (mBackground != 0) {
            mpaint.setColor(mBackground); 
            mpaint.setStyle(Paint.Style.FILL);           
            canvas.drawRoundRect((new RectF(borderWidth, borderWidth, srcBitmap.getWidth() + borderWidth, srcBitmap.getHeight())), radius, radius, mpaint);
        } else if (borderWidth > 0) {
           mpaint.setColor(borderColor);   
           mpaint.setStrokeWidth(borderWidth);        
           mpaint.setStyle(Paint.Style.STROKE);           
           canvas.drawRoundRect((new RectF(borderWidth, borderWidth, srcBitmap.getWidth() + borderWidth, srcBitmap.getHeight())), radius, radius, mpaint);
        }
        
        return bmpWithBorder;         
    }

    @Override
    public void updateDiskCacheKey(MessageDigest arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap sourceBitmap, int outWidth, int outHeight) {
        Bitmap mBitmap = addBorder(sourceBitmap, mBorderWidth, mColor, mRadius);
        return mBitmap;        
    }

     
}