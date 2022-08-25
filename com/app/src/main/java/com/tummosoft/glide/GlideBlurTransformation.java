package com.tummosoft.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.RSRuntimeException;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

import com.tummosoft.glide.FastBlur;
import com.tummosoft.glide.RSBlur;


public class GlideBlurTransformation extends BitmapTransformation {
    
    private static final int VERSION = 1;
  private static final String ID =
    "jp.wasabeef.glide.transformations.BlurTransformation." + VERSION;

  private static final int MAX_RADIUS = 25;
  private static final int DEFAULT_DOWN_SAMPLING = 1;

  private final int radius;
  private final int sampling;

  public GlideBlurTransformation() {
    this(MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
  }

  public GlideBlurTransformation(int radius) {
    this(radius, DEFAULT_DOWN_SAMPLING);
  }

  public GlideBlurTransformation(int radius, int sampling) {
    this.radius = radius;
    this.sampling = sampling;
  }

@Override
public void updateDiskCacheKey(MessageDigest arg0) {
    // TODO Auto-generated method stub
    
}

@Override
protected Bitmap transform(BitmapPool pool, Bitmap sourceBitmap, int outWidth, int outHeight) {
    int width = sourceBitmap.getWidth();
    int height = sourceBitmap.getHeight();
    int scaledWidth = width / sampling;
    int scaledHeight = height / sampling;

    Bitmap bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

    //setCanvasBitmapDensity(sourceBitmap, bitmap);

    Canvas canvas = new Canvas(bitmap);
    canvas.scale(1 / (float) sampling, 1 / (float) sampling);
    Paint paint = new Paint();
    paint.setFlags(Paint.FILTER_BITMAP_FLAG);
    canvas.drawBitmap(sourceBitmap, 0, 0, paint);

    bitmap = FastBlur.blur(bitmap, radius, true);

    return bitmap;
}


    
}
