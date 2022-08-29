package com.tummosoft.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import anywheresoftware.b4a.BA;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

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
    private Boolean mGradient = false;
    private int mStartColor, mEndColor;
    private int []mPadding;
        
    public BorderImage(int borderWidth, int color, int radius, int bgk, Boolean xGradient, int xStart, int xEnd, int[] xPadding) {
        mBorderWidth = borderWidth;
        mColor = color;
        mRadius = radius;
        mBackground = bgk;
        mGradient = xGradient;
        mStartColor = xStart;
        mEndColor = xEnd;
        mPadding = xPadding;
    }
    
    protected Bitmap addBorder(Bitmap srcBitmap, int borderWidth, int borderColor, int radius){
        Bitmap bmpWithBorder = Bitmap.createBitmap(srcBitmap.getWidth() + (mBorderWidth*2), srcBitmap.getHeight() + (mBorderWidth*2), srcBitmap.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        Paint mpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mpaint.setAntiAlias(true);
        
        if (mBackground != 0) {           
            if (mGradient == true) {                     
                Path pa = new Path();    
                mpaint.setColor(mBackground);      
                mpaint.setStyle(Paint.Style.FILL);           
                pa.addRoundRect((new RectF(borderWidth, borderWidth, srcBitmap.getWidth() + borderWidth, srcBitmap.getHeight())), radius, radius,  Path.Direction.CW);
                mpaint.setShader(new LinearGradient(0, 0, 0, bmpWithBorder.getHeight(), mStartColor, mEndColor, Shader.TileMode.MIRROR));
                canvas.drawPath(pa, mpaint);
            } else {
                mpaint.setColor(mBackground); 
                mpaint.setStyle(Paint.Style.FILL);           
                canvas.drawRoundRect((new RectF(borderWidth, borderWidth, srcBitmap.getWidth() + borderWidth, srcBitmap.getHeight())), radius, radius, mpaint);
            }            
        } else if (borderWidth > 0) {            
           mpaint.setColor(borderColor);   
           mpaint.setStrokeWidth(borderWidth);        
           mpaint.setStyle(Paint.Style.STROKE);           
           canvas.drawRoundRect((new RectF(borderWidth, borderWidth, srcBitmap.getWidth() + borderWidth, srcBitmap.getHeight())), radius, radius, mpaint);
        } else if (mGradient == true) {           
            Path pa = new Path();    
            mpaint.setStyle(Paint.Style.FILL);           
            pa.addRoundRect((new RectF(borderWidth, borderWidth, srcBitmap.getWidth() + borderWidth, srcBitmap.getHeight())), radius, radius,  Path.Direction.CW);
            mpaint.setShader(new LinearGradient(0, 0, 0, bmpWithBorder.getHeight(), mStartColor, mEndColor, Shader.TileMode.MIRROR));
            canvas.drawPath(pa, mpaint);
        }

        if (mPadding[0] > 0) {
            //canvas.drawBitmap(srcBitmap, mPadding[0], mPadding[1], null);
            //canvas.drawBitmap(srcBitmap, null, dst, paint);
            int width = bmpWithBorder.getWidth() - mPadding[0];
            int height = bmpWithBorder.getHeight() - mPadding[1];
            int left = mPadding[0];
            int top = mPadding[1];
            RectF rectf = new RectF(left, top, width, height);
            canvas.drawBitmap(srcBitmap, null, rectf, null);
            
        } else {
            canvas.drawBitmap(srcBitmap, mBorderWidth, mBorderWidth, null);
        }
        
        

        return bmpWithBorder;         
    }

    @Override
    public void updateDiskCacheKey(MessageDigest arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected Bitmap transform(BitmapPool arg0, Bitmap arg1, int arg2, int arg3) {        
        Bitmap mBitmap = addBorder(arg1, mBorderWidth, mColor, mRadius);
        return mBitmap; 
    }

    
}