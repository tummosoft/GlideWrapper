package com.tummosoft.glide;

import java.io.IOException;

import android.R;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.keywords.Regex;
import anywheresoftware.b4a.objects.collections.*;
import anywheresoftware.b4a.objects.streams.File;

public class EventOnTouch implements View.OnTouchListener, View.OnClickListener {
    private ImageView mImageView;
    private String eventname;
    private BA mBA;
    private GestureDetector mDetector;
    private float StartX, StartY;
    private boolean ResetStartPos;
    private int mBorderWidth, mBorderColor;
    private int mGlideType;
    private int mRadius;
    private ImageView temp;
            
    public EventOnTouch(BA ba, String event, ImageView imageview) {
        mImageView = imageview;
        eventname = event;
        mBA = ba;
        mDetector = new GestureDetector(new eventGestureDetector(ba, eventname, mImageView));            
    }

    public EventOnTouch(BA ba, String event, ImageView imageview, int borderWidth, int borderColor, int glideType, int radius) {
        mBorderWidth = borderWidth;
        mBorderColor = borderColor;
        mGlideType = glideType;
        mImageView = imageview;
        mRadius = radius;
        eventname = event;
        mBA = ba;
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent ev) {
        final int action = ev.getAction();
                    final int actionCode = action & MotionEvent.ACTION_MASK;
                    boolean IsHandled = true;
				if (mBA.subExists(eventname + "_ontouch")) {
					Object Result = mBA.raiseEvent(mImageView, eventname + "_ontouch", ev.getRawX(), ev.getRawY());
					if (Result instanceof Boolean)
						IsHandled = (boolean) Result;
				}
				
				if (actionCode == MotionEvent.ACTION_POINTER_DOWN && mBA.subExists(eventname + "_onpointerdown")) {
					final int actionPtrIndex = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT; 
					mBA.raiseEvent(mImageView, eventname + "_onpointerdown", ev.getRawX(), ev.getRawY());
				}
				else if (actionCode == MotionEvent.ACTION_POINTER_UP && mBA.subExists(eventname + "_onpointerup")) {
					final int actionPtrIndex = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
					ResetStartPos = true;
					mBA.raiseEvent(mImageView, eventname + "_onpointerup", ev.getRawX(), ev.getRawY());
				}
                else if (actionCode == MotionEvent.ACTION_UP) {
                    if (mBA.subExists(eventname + "_cancel")){
						mBA.raiseEvent(mImageView, eventname + "_cancel", ev.getRawX(), ev.getRawY());
					}
				} else if (actionCode == MotionEvent.ACTION_DOWN) {
					ResetStartPos = true;
                    
					if (mBA.subExists(eventname + "_ondown")){
						mBA.raiseEvent(mImageView, eventname + "_ondown", ev.getRawX(), ev.getRawY());
					}
				}
				else if (actionCode == MotionEvent.ACTION_MOVE && mBA.subExists(eventname + "_ondrag")) {
					if (ResetStartPos) {
						StartX = ev.getRawX();
						StartY = ev.getRawY();
						ResetStartPos = false;                        
					}
                    
					float deltaX = ev.getRawX() - StartX;
					float deltaY = ev.getRawY() - StartY;
					mBA.raiseEvent(mImageView, eventname + "_ondrag", deltaX, deltaY);
				}
				
				mDetector.onTouchEvent(ev);
                return false;
			}    
            
        private static int getPID(MotionEvent ev, int ptrIndex)
        {
            return ev.getPointerId(ptrIndex);
        }

        @Override
        public void onClick(View arg0) {                  
            GlideClickEvent evn = new GlideClickEvent();
            if (temp == null) {
                temp = mImageView;
            }
            temp.setDrawingCacheEnabled(true);  
            int opt = mGlideType;
            
            switch (opt) {
                case 2:    
                           
                    evn.CircleClick(mImageView, temp, mBorderWidth, mBorderColor);  
                  
                    if (mBA.subExists(eventname + "_click")){
						mBA.raiseEvent(mImageView, eventname + "_click");
					}              
                    break;                
                default:
                    //evn.RectangleClick(mImageView, temp, mBorderWidth, mBorderColor, mRadius);
                    Map map1 = readCache();
                    //File.Delete(File.getDirInternalCache(), "cache.txt");
                    if (map1.getSize() > 0) {
                        if (map1.ContainsKey(eventname) == true) {
                            String temp1 = (String) map1.Get(eventname);
                            String[] rec = Regex.Split(",", temp1);
                            //BA.Log("Value:" + temp);
                            RectF tempRectF = new RectF();
                            tempRectF.left = Float.parseFloat(rec[0]);
                            tempRectF.top = Float.parseFloat(rec[1]);
                            tempRectF.right = Float.parseFloat(rec[2]);
                            tempRectF.bottom = Float.parseFloat(rec[3]);
                            //(final ImageView img, final ImageView backup, int borderWidth, int borderColor, int radius, RectF rect)
                            evn.RectangleClick(mImageView, temp, mBorderWidth, mBorderColor, mRadius, tempRectF);
                        }
                    } else {
                        RectF tempRectF = getBitmapPositionInsideImageView(mImageView);
                        map1.Put(eventname, tempRectF.left + "," + tempRectF.top + "," + tempRectF.right + "," + tempRectF.bottom);
                        saveCache(map1);
                        evn.RectangleClick(mImageView, temp, mBorderWidth, mBorderColor, mRadius, tempRectF);
                    }

                    if (mBA.subExists(eventname + "_click")){
						mBA.raiseEvent(mImageView, eventname + "_click");
					}              
                    break;
            }
        }
        private Map readCache() {
            Map map1 = new Map();
            map1.Initialize();
            try {
                map1 = File.ReadMap(File.getDirInternalCache(), "cache.txt");                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return map1;
        }

        private void saveCache(Map value) {
            
            try {
              File.WriteMap(File.getDirInternalCache(), "cache.txt", value);
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
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

