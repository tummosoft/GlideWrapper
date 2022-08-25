package com.tummosoft.glide;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import anywheresoftware.b4a.BA;

public class EventOnTouch implements View.OnTouchListener, View.OnClickListener {
    private ImageView mImageView;
    private String eventname;
    private BA mBA;
    private GestureDetector mDetector;
    private float StartX, StartY;
    private boolean ResetStartPos;
    private int mBorderWidth, mBorderColor;
    private int mGlideType;

    public EventOnTouch(BA ba, String event, ImageView imageview) {
        mImageView = imageview;
        eventname = event;
        mBA = ba;
        mDetector = new GestureDetector(new eventGestureDetector(ba, eventname, mImageView));            
    }

    public EventOnTouch(BA ba, String event, ImageView imageview, int borderWidth, int borderColor, int glideType) {
        mBorderWidth = borderWidth;
        mBorderColor = borderColor;
        mGlideType = glideType;
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent ev) {
        final int action = ev.getAction();
                    final int actionCode = action & MotionEvent.ACTION_MASK;
                    boolean IsHandled = true;
				if (mBA.subExists(eventname + "_ontouch")) {
					Object Result = mBA.raiseEvent(mImageView, eventname + "_ontouch", ev.getX(), ev.getY());
					if (Result instanceof Boolean)
						IsHandled = (boolean) Result;
				}
				
				if (actionCode == MotionEvent.ACTION_POINTER_DOWN && mBA.subExists(eventname + "_onpointerdown")) {
					final int actionPtrIndex = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT; 
					mBA.raiseEvent(mImageView, eventname + "_onpointerdown", ev.getX(), ev.getY());
				}
				else if (actionCode == MotionEvent.ACTION_POINTER_UP && mBA.subExists(eventname + "_onpointerup")) {
					final int actionPtrIndex = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
					ResetStartPos = true;
					mBA.raiseEvent(mImageView, eventname + "_onpointerup", ev.getX(), ev.getY());
				}
                else if (actionCode == MotionEvent.ACTION_UP) {
                    if (mBA.subExists(eventname + "_cancel")){
						mBA.raiseEvent(mImageView, eventname + "_cancel", ev.getX(), ev.getY());
					}
				} else if (actionCode == MotionEvent.ACTION_DOWN) {
					ResetStartPos = true;
                    
					if (mBA.subExists(eventname + "_ondown")){
						mBA.raiseEvent(mImageView, eventname + "_ondown", ev.getX(), ev.getY());
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
            ImageView temp = mImageView;            
                    GlideClickEvent evn = new GlideClickEvent();
                    temp.setDrawingCacheEnabled(true);  
                    int opt = mGlideType;
                    
                    switch (opt) {
                        case 2:                                           
                        evn.CircleClick(mImageView, temp, mBorderWidth, mBorderColor);                
                            break;                
                        default:
                            break;
                    }
        }
}  

