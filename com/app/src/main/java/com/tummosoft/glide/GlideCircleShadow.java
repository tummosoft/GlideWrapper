package com.tummosoft.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class GlideCircleShadow extends BitmapTransformation {
    private static final int SHADOW_RADIUS = 10;
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.CircleShadown";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName(STRING_CHARSET_NAME));
    private int mColor;
    
    public GlideCircleShadow(int xColor) {
        mColor = xColor;
    }
   
    @Override
    public boolean equals(Object obj) {
        return obj instanceof GlideCircleShadow;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap sourceBitmap, int outWidth, int outHeight) {
        int size = sourceBitmap.getWidth();

        Bitmap outputBitmap = Bitmap.createBitmap(
                size + SHADOW_RADIUS * 2,
                size + SHADOW_RADIUS * 2,
                Bitmap.Config.ARGB_8888);

        Canvas outputCanvas = new Canvas(outputBitmap);

        Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadow.setStyle(Paint.Style.FILL);
        shadow.setColor(mColor);
        shadow.setShadowLayer(SHADOW_RADIUS, 0, 0, mColor);
        outputCanvas.drawCircle(
                (size + SHADOW_RADIUS * 2) / 2,
                (size + SHADOW_RADIUS * 2) / 2,
                (size) / 2,
                shadow);

        outputCanvas.drawBitmap(sourceBitmap, SHADOW_RADIUS, SHADOW_RADIUS, null);

        return outputBitmap;
    }    
}
