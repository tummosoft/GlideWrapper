package com.tummosoft.glide;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.widget.ImageView;

public class GlideClickEvent {    
    private RectF mRectF;
    public void RectangleClick(final ImageView img, final ImageView backup, int borderWidth, int borderColor, int radius) {
      int dstBitmapWidth = img.getWidth() - (borderWidth*4);
        
      Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);
      int ar = Color.argb(100, 51, 134, 222);
      Canvas canvas = new Canvas(dstBitmap);
      Paint paint = new Paint();
      paint.setColor(ar);
      paint.setStyle(Paint.Style.FILL);        
      //paint.setStrokeWidth(borderWidth);
      paint.setAntiAlias(true);         
      Bitmap bm =  img.getDrawingCache();
      canvas.drawBitmap(bm, borderWidth, borderWidth, null);
      
      mRectF = getBitmapPositionInsideImageView(backup);

      canvas.drawRoundRect(mRectF, radius, radius, paint);
      
     img.setImageBitmap(dstBitmap);
     new CountDownTimer(500,100){

  @Override
  public void onTick(long millisUntilFinished) {
      //img.animate().alpha(0).setDuration(2000);
  }
  @Override
  public void onFinish() {
       final Bitmap bm =  backup.getDrawingCache();
       img.setImageBitmap(bm);  
  }
  }.start();  
    }

  public void CircleClick(final ImageView img, final ImageView backup, int borderWidth, int borderColor){
        int dstBitmapWidth = img.getWidth() - (borderWidth*4);
        
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);
        int ar = Color.argb(100, 51, 134, 222);
        Canvas canvas = new Canvas(dstBitmap);
        Paint paint = new Paint();
        paint.setColor(ar);
        paint.setStyle(Paint.Style.FILL);        
        //paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);         
        Bitmap bm =  img.getDrawingCache();
        canvas.drawBitmap(bm, borderWidth, borderWidth, null);
        
        canvas.drawCircle(
                canvas.getWidth() / 2, // cx
                canvas.getWidth() / 2, // cy
                canvas.getWidth()/2 - borderWidth / 2, // Radius
                paint // Paint
        );
      
        
       img.setImageBitmap(dstBitmap);
       new CountDownTimer(500,100){

    @Override
    public void onTick(long millisUntilFinished) {
        //img.animate().alpha(0).setDuration(2000);
    }
    @Override
    public void onFinish() {
         final Bitmap bm =  backup.getDrawingCache();
         
         img.setImageBitmap(bm);  
    }
    }.start();        
  }
  
  
  private static final RectF getBitmapPositionInsideImageView(ImageView imageView)
  {
    RectF rect = new RectF();

    if (imageView == null || imageView.getDrawable() == null)
    {
        return rect;
    }

    // Get image dimensions
    // Get image matrix values and place them in an array
    float[] f = new float[9];
    imageView.getImageMatrix().getValues(f);

    // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
    final float scaleX = f[Matrix.MSCALE_X];
    final float scaleY = f[Matrix.MSCALE_Y];

    // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
    final Drawable d     = imageView.getDrawable();
    final int      origW = d.getIntrinsicWidth();
    final int      origH = d.getIntrinsicHeight();

    // Calculate the actual dimensions
    final int actW = Math.round(origW * scaleX);
    final int actH = Math.round(origH * scaleY);

    // Get image position
    // We assume that the image is centered into ImageView
    int imgViewW = imageView.getWidth();
    int imgViewH = imageView.getHeight();

    rect.top  = (int) (imgViewH - actH) / 2;
    rect.left = (int) (imgViewW - actW) / 2;

    rect.bottom = rect.top + actH;
    rect.right  = rect.left + actW;

    return rect;
}

}
