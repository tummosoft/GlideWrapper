package com.tummosoft.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class ColorFilterTransformation extends BitmapTransformation {

  private static final int VERSION = 1;
  private static final String ID =
    "jp.wasabeef.glide.transformations.ColorFilterTransformation." + VERSION;

  private final int color;

  public ColorFilterTransformation(int color) {
    this.color = color;
  }

  @Override
  public String toString() {
    return "ColorFilterTransformation(color=" + color + ")";
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof ColorFilterTransformation &&
      ((ColorFilterTransformation) o).color == color;
  }

  @Override
  public int hashCode() {
    return ID.hashCode() + color * 10;
  }

  @Override
  public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    messageDigest.update((ID + color).getBytes(CHARSET));
  }

@Override
protected Bitmap transform(BitmapPool pool, Bitmap sourceBitmap, int outWidth, int outHeight) {
    int width = sourceBitmap.getWidth();
    int height = sourceBitmap.getHeight();

    Bitmap.Config config =
    sourceBitmap.getConfig() != null ? sourceBitmap.getConfig() : Bitmap.Config.ARGB_8888;
    Bitmap bitmap = pool.get(width, height, config);

    //setCanvasBitmapDensity(sourceBitmap, bitmap);

    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
    canvas.drawBitmap(sourceBitmap, 0, 0, paint);

    return bitmap;
}
}