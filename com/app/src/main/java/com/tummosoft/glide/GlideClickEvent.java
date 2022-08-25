package com.tummosoft.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.widget.ImageView;

public class GlideClickEvent {    
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
  
}
