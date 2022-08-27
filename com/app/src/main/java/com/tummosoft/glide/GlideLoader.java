package com.tummosoft.glide;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper;
import anywheresoftware.b4a.objects.streams.File;

import java.io.IOException;
import java.net.MalformedURLException;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.Rotate;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.caverock.androidsvg.SVGParseException;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.util.TypedValue;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.graphics.Bitmap;
import android.R;
import android.R.anim;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.TypedArray;

@Version(2.02f)
@ShortName("GlideLoader")
@Events(values={"Click", "OnTouch(X As Float, Y As Float) As Boolean",
"OnDown(X As Float, Y As Float)", "OnDrag(X As Float, Y As Float)",
"OnFling(X As Float, Y As Float)", "OnPointerUp(X As Float, Y As Float)",
"OnScroll(X As Float, Y As Float, status as int), Cancel(X As Float, Y As Float)"})
public class GlideLoader {
    private ImageView mImageView = null;
    private BA mBA;
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
        callGlideType(opt);
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
            mImageView.setOnClickListener(new EventOnTouch(mBA, eventname, mImageView, getBorderWidth(), getBorderColor(), getGlideType(), getCornerRadius()));
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
            mImageView.setOnClickListener(new EventOnTouch(mBA, eventname, mImageView, getBorderWidth(), getBorderColor(), getGlideType(), getCornerRadius()));
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
        TypedValue outValue = new TypedValue();
        mBA.context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        mImageView.setClickable(true);
        mImageView.setBackgroundResource(outValue.resourceId);  
        
        if (getTouchType() == 1) {            
            mImageView.setOnClickListener(new EventOnTouch(mBA, eventname, mImageView, getBorderWidth(), getBorderColor(), getGlideType(), getCornerRadius()));
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

    private void callGlideType(int value) {
        if (value == 0) {
            options.centerCrop();     
        } else if (value == 1) {
            options.centerInside();
        } else if (value == 2) {
            options.circleCrop();           
        } else if (value == 3) {            
            options.fitCenter();
        } else if (value == 4) {  
            mImageView.setScaleType(ScaleType.FIT_END);
        } else if (value == 5) {  
            mImageView.setScaleType(ScaleType.FIT_START);   
        } else if (value == 6) {  
            mImageView.setScaleType(ScaleType.FIT_XY);               
        }
    }

	public void setGlideType(int value) {
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
    
    public int getVisibility() {
        return mImageView.getVisibility();
    }

    public void setVisibility(int value) {
        mImageView.setVisibility(value);
    }


    public void TestAnimation() {
        //ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(mImageView, "scaleX", 1.0f, 2.0f);
        //scaleAnim.setDuration(3000);
        //scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
        //scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
        //scaleAnim.start();

        
        //Animator anim =
        //ViewAnimationUtils.createCircularReveal(mImageView, mImageView.getWidth()/2, mImageView.getHeight()/2, 0, 100);
        //anim.setDuration(700);
        //anim.start();

        //ObjectAnimator fadeAnim = ObjectAnimator. .ofFloat(tvLabel, "alpha", 0.2f);
        //fadeAnim.start();

        // Create an array of the attributes we want to resolve
    // using values from a theme
    // android.R.attr.selectableItemBackground requires API LEVEL 11
   // int[] attrs = new int[] { android.R.attr.selectableItemBackgroundBorderless /* index 0 */};

    // Obtain the styled attributes. 'themedContext' is a context with a
    // theme, typically the current Activity (i.e. 'this')
    //TypedArray ta = mBA.context.obtainStyledAttributes(attrs);

    // Now get the value of the 'listItemBackground' attribute that was
    // set in the theme used in 'themedContext'. The parameter is the index
    // of the attribute in the 'attrs' array. The returned Drawable
    // is what you are after
    //Drawable drawableFromTheme = ta.getDrawable(0 /* index */);

    // Finally free resources used by TypedArray
    //ta.recycle();

    // setBackground(Drawable) requires API LEVEL 16, 
    // otherwise you have to use deprecated setBackgroundDrawable(Drawable) method. 
    //mImageView.setBackground(drawableFromTheme);
    // imageButton.setBackgroundDrawable(drawableFromTheme);
    //int[] attrs = new int[]{R.attr.actionBarTheme};
    //TypedArray typedArray = mBA.context.obtainStyledAttributes(attrs);
    //int backgroundResource = typedArray.getResourceId(0, 0);
    //mImageView.setBackgroundResource(backgroundResource);
    //typedArray.recycle();
    TypedValue outValue = new TypedValue();
    mBA.context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
    mImageView.setBackgroundResource(outValue.resourceId);                     
    
    }
}