package com.tummosoft.glide;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;
import anywheresoftware.b4a.BALayout.LayoutParams;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper;
import anywheresoftware.b4a.objects.streams.File;

import java.io.IOException;
import java.net.MalformedURLException;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.Rotate;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.caverock.androidsvg.SVGParseException;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.content.ClipData;
import android.graphics.Bitmap;

import com.tummosoft.glide.GlideSVG;
import com.tummosoft.glide.GlideClickEvent;
import com.tummosoft.glide.TouchType;
import com.tummosoft.glide.eventGestureDetector;

@Version(2.01f)
@ShortName("GlideLoader")
@Events(values={"Click", "OnTouch(X As Float, Y As Float) As Boolean",
"OnDown(X As Float, Y As Float)", "OnDrag(X As Float, Y As Float)",
"OnFling(X As Float, Y As Float)", "OnPointerUp(X As Float, Y As Float)",
"OnScroll(X As Float, Y As Float, status as int), Cancel(X As Float, Y As Float)"})
public class GlideLoader {
    private ImageView mImageView = null;
    private BA mBA;
    private String mEventClick;
    private String mEventDragDrop;
    private int mGravity;
    public GlideGravity GlideTypes;
    public GlideFilterType FilterTypes;
    public TouchType TouchTypes;
    private RequestOptions options = new RequestOptions();
    private int mBorderWidth = 0;
    private Boolean mShadown = false;
    private int mCornerRadius = 1;
    private int mRotate = 0;
    private int mBorderColor = Color.parseColor("#00000000");
    private int mBackground = Color.parseColor("#00000000");
    private MultiTransformation multiTransformation = null;
    private int mShadownColor = Color.parseColor("#00000000");
    private Boolean mIsFilter = false;
    private int mFilterType = 0;
    private int mFilterColor = 0;
    private int mBlurRadius = 0;
    private int mTouchType = 0;
    private String eventname;
    private anywheresoftware.b4a.objects.drawable.BitmapDrawable mBitmapCache;
   
    
    public void initialize(BA ba, String EventName, ImageViewWrapper imv) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
       this.mBA = ba;
       eventname = EventName;
       mImageView = imv.getObject();              
    }
  
    public void fromUrl(String url) {
        SetOptions();
        url = url.toLowerCase();
        
        if (url.endsWith("svg")) {            
            try {
                SVG(url);
            } catch (SVGParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }          
        } else {
            ReadURL(url);
        }
    }
    
    private void SetOptions() {
        int opt = getGlideType();
       
        switch (opt) {
            case 2:
                if (getShadown() == true) { 
                    if (getFilter() == true) {
                        if (mFilterType == 2) {
                            multiTransformation = new MultiTransformation<Bitmap>(
                            new CircleCropBorder(getBorderWidth(), getBorderColor(), getBackground()),
                            new GlideCircleShadow(getShadownColor()),
                            new GlideBlurTransformation(getBlurRadius()));   
                        } else if (mFilterType == 3) {
                            multiTransformation = new MultiTransformation<Bitmap>(
                                new CircleCropBorder(getBorderWidth(), getBorderColor(), getBackground()),
                                new GlideCircleShadow(getShadownColor()),
                                new ColorFilterTransformation(getFilterColor()));  
                        } else {
                            multiTransformation = new MultiTransformation<Bitmap>(
                            new CircleCropBorder(getBorderWidth(), getBorderColor(), getBackground()),
                            new GlideCircleShadow(getShadownColor()));    
                        }                        
                    } else {
                        multiTransformation = new MultiTransformation<Bitmap>(
                        new CircleCropBorder(getBorderWidth(), getBorderColor(), getBackground()),
                        new GlideCircleShadow(getShadownColor()));    
                    }
                    
                    break;        
                } else {
                    multiTransformation = new MultiTransformation<Bitmap>(
                    new CircleCropBorder(getBorderWidth(), getBorderColor(), getBackground()));    
                    break;        
                }                
            default:
                multiTransformation = new MultiTransformation<Bitmap>(
                    new RoundedCorners(getCornerRadius()), 
                    new GlideShadow(getShadownColor()),                    
                    new BorderImage(getBorderWidth(), getBorderColor(), getCornerRadius(), getBackground()),            
                    new Rotate(mRotate));
                    break;
        }        
    }

    public void fromFile(String Dir, String FileName) {
        SetOptions();
        anywheresoftware.b4a.objects.streams.File f1 = new anywheresoftware.b4a.objects.streams.File();
            String path = f1.Combine(Dir, FileName);
        
        if (multiTransformation != null) {
            Glide.with(mBA.context).load(path).apply(options).transform(multiTransformation).into(mImageView);      
        } else {
            Glide.with(mBA.context).load(path).apply(options).into(mImageView);      
        }
        mImageView.setLongClickable(true);
        mImageView.setBackgroundColor(Color.parseColor("#00000000"));
        
        if (getTouchType() == 1) {
            mImageView.setOnClickListener(new EventOnTouch(mBA, eventname, mImageView, getBorderWidth(), getBorderColor(), getGlideType()));
        } else if (getTouchType() == 2) {
            mImageView.setLongClickable(true);    
            mImageView.setOnTouchListener(new EventOnTouch(mBA, eventname, mImageView));                   
        }
        
    }

    private void LoadRawable(Bitmap bm) {
        SetOptions();
        if (multiTransformation != null) { 
            Glide.with(mBA.context).load(bm).apply(options).transform(multiTransformation).into(mImageView);      
            
        } else {          
            Glide.with(mBA.context).load(bm).apply(options).into(mImageView);      
        }
                
        mImageView.setBackgroundColor(Color.parseColor("#00000000"));
        
        if (getTouchType() == 1) {
            mImageView.setOnClickListener(new EventOnTouch(mBA, eventname, mImageView, getBorderWidth(), getBorderColor(), getGlideType()));
        } else if (getTouchType() == 2) {
            mImageView.setLongClickable(true);    
            mImageView.setOnTouchListener(new EventOnTouch(mBA, eventname, mImageView));                   
        }
    }

    // ------------------ SVG --------------------------------------
    private void SVG(String url) throws SVGParseException {
        anywheresoftware.b4a.objects.streams.File f1 = new anywheresoftware.b4a.objects.streams.File();
        String path = f1.Combine(File.getDirInternalCache(), "out.xml");
        //options.format(DecodeFormat.PREFER_ARGB_8888);
        GlideSVG svg = new GlideSVG(path);
        try {           
            Bitmap bm = svg.GetFormURL(url);
            LoadRawable(bm);                        
        } catch (MalformedURLException e) {            
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
           // BA.Log("ES: " + e.getCause());
        }
    }
    // ----------------------------- LOAD URL ----------------------   
    private void ReadURL(String url) {
        Uri uri = Uri.parse(url);

        if (multiTransformation != null) {           
            Glide.with(mBA.context).load(uri).apply(options).transform(multiTransformation).into(mImageView);      
        } else {
            Glide.with(mBA.context).load(uri).apply(options).into(mImageView);      
        }
        
        mImageView.setBackgroundColor(Color.parseColor("#00000000"));
        if (getTouchType() == 1) {
            mImageView.setOnClickListener(new EventOnTouch(mBA, eventname, mImageView, getBorderWidth(), getBorderColor(), getGlideType()));
        } else if (getTouchType() == 2) {
            mImageView.setLongClickable(true);    
            mImageView.setOnTouchListener(new EventOnTouch(mBA, eventname, mImageView));                   
        }        
    }

    public void reSize(int width, int height) {
        options.override(width, height);
    }

    public void Clear(ImageViewWrapper imv) {
        Glide.with(mBA.context).clear(imv.getObject());
    }
    
    // --------------------------------------

    public BitmapWrapper Resources() {
        BitmapWrapper bm = new BitmapWrapper();
        mImageView.setDrawingCacheEnabled(true);
        bm.Initialize3(mImageView.getDrawingCache());
        
		return  bm;
	}
    
    // --------------------------------------

    public float getAlpha() {
		return  mImageView.getAlpha();
	}

	public void setAlpha(float value) {
        mImageView.setAlpha(value);        
	}

     // --------------------------------------

     public int getTouchType() {
		return  mTouchType;
	}

	public void setTouchType(int value) {
        mTouchType = value;
	}

     // --------------------------------------

     public int getBlurRadius() {
		return  mBlurRadius;
	}

	public void setBlurRadius(int value) {
        mBlurRadius = value;
	}
    
    // --------------------------------------

    public int getFilterType() {
		return  mFilterType;
	}

	public void setFilterType(int value) {
        mFilterType = value;
	}
    // --------------------------------------
    public int getFilterColor() {
		return  mFilterColor;
	}

	public void setFilterColor(int value) {
        mFilterColor = value;
	}
    
    // --------------------------------------
     public int getShadownColor() {
		return  mShadownColor;
	}

	public void setShadownColor(int value) {
        mShadownColor = value;
	}
     // --------------------------------------

     public int getBackground() {
		return mBackground;
	}

	public void setBackground(int value) {
        mBackground = value;
	}
     // --------------------------------------

     public int getRotate() {
		return mRotate;
	}

	public void setRotate(int value) {
        mRotate = value;
	}
     // --------------------------------------

     public int getCornerRadius() {
		return mCornerRadius;
	}

	public void setCornerRadius(int value) {
        mCornerRadius = value;
	}
    // --------------------------------------

    public Boolean getFilter() {
		return mIsFilter;
	}

	public void setFilter(Boolean value) {
        mIsFilter = value;
	}
    // --------------------------------------

    public Boolean getShadown() {
		return mShadown;
	}

	public void setShadown(Boolean value) {
        mShadown = value;
	}
    // -------------------------------------
    public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int value) {
        mBorderWidth = value;
	}
    // ----------------------------------
    public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int value) {
        mBorderColor = value;
	}
    // --------------------------------
    public int getGlideType() {
		return mGravity;
	}

	public void setGlideType(int value) {
        if (value == 0) {
            options.centerCrop();     
        } else if (value == 1) {
            options.centerInside();
        } else if (value == 2) {
            options.circleCrop();           
        } else if (value == 3) {
            options.fitCenter();            
        }
        //options.transform(transformation)
		mGravity = value;
	}
   // -------------------------------------

    public void ClearDiskCache() {
        new Thread( new Runnable() { @Override public void run() { 
            Glide.get(mBA.context).clearDiskCache();
          } } ).start();            
    }
    
    public void ClearMemory() {   
        new Thread( new Runnable() { @Override public void run() { 
            Glide.get(mBA.context).clearMemory();
          } } ).start();
    }  
        
}
