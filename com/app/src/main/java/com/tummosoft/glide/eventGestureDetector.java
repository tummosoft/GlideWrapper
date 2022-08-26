package com.tummosoft.glide;

import anywheresoftware.b4a.BA;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class eventGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private String _eventPrefix;
    private View _view;
    private BA _ba;
    private ImageView mImageView;
    private String mEventDragDrop;
    private String mEventClick;
    private BA mBA;
    private int mBorderWidth, mBorderColor;
    	
    public eventGestureDetector(BA xba, String event, ImageView imageview) {
        _ba = xba;
        mImageView = imageview;        
        _eventPrefix = event;
    }

    @Override
		public boolean onSingleTapUp(MotionEvent ev) {           
            
			if (_ba.subExists(_eventPrefix + "_onsingletapup")){
				_ba.raiseEvent(_view, _eventPrefix + "_onsingletapup", ev.getX(), ev.getY());
			}
			return true;
		}
		@Override
		public boolean onSingleTapConfirmed(MotionEvent ev) {
            BA.Log("onSingleTapConfirmed");
			if (_ba.subExists(_eventPrefix + "_onsingletapconfirmed")){
				_ba.raiseEvent(_view, _eventPrefix + "_onsingletapconfirmed", ev.getX(), ev.getY());
			}
			return true;
		}
		@Override
		public boolean onDoubleTap(MotionEvent ev) {
            BA.Log("onDoubleTap");
			if (_ba.subExists(_eventPrefix + "_ondoubletap")){
				_ba.raiseEvent(_view, _eventPrefix + "_ondoubletap", ev.getX(), ev.getY());
			}
			return true;
		}
		@Override
		public void onShowPress(MotionEvent ev) {
            BA.Log("onShowPress");
			if (_ba.subExists(_eventPrefix + "_onshowpress")){
				_ba.raiseEvent(_view, _eventPrefix + "_onshowpress", ev.getX(), ev.getY());
			}
		}
		@Override
		public void onLongPress(MotionEvent ev) {            
			if (_ba.subExists(_eventPrefix + "_onlongpress")){
				_ba.raiseEvent(_view, _eventPrefix + "_onlongpress", ev.getX(), ev.getY());
			}
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Point offset = new Point((int) e1.getX(), (int) e1.getY());
			int status = 0;
        switch (e1.getAction()) {                    
            case MotionEvent.ACTION_DOWN:
				status = 0;
			case MotionEvent.ACTION_BUTTON_RELEASE:
				status = 1;
			case MotionEvent.ACTION_SCROLL:
				status = 2;
            default:
                break;
        }
           if (_ba.subExists(_eventPrefix + "_onscroll")){
				_ba.raiseEvent(_view, _eventPrefix + "_onscroll", e1.getRawX(), e1.getRawY(), status);
			}
			return true;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (_ba.subExists(_eventPrefix + "_onfling")){
				_ba.raiseEvent(_view, _eventPrefix + "_onfling", e1.getRawX(), e1.getRawY());
			}
			return true;
		}
   
    
}
