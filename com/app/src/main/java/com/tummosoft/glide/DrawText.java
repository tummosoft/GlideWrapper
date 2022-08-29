package com.tummosoft.glide;

import java.security.MessageDigest;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class DrawText extends BitmapTransformation {

    @Override
    public void updateDiskCacheKey(MessageDigest arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap sourceBitmap, int outWidth, int outHeight) {
        Bitmap bm = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint();
            paint.setColor(Color.WHITE); // Text Color
            paint.setTextSize(12); // Text Size
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
            // some more settings...

            canvas.drawBitmap(sourceBitmap, 0, 0, paint);
            canvas.drawText("Testing...", 10, 10, paint);

        return bm;
    }
      
}
