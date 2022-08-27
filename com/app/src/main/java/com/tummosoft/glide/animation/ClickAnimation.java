package com.tummosoft.glide.animation;

import android.R;
import android.app.Activity;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.ImageView;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;

@ShortName("ClickAnimation")
public class ClickAnimation {
    public void Test(ImageView mImageView, Activity act) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = act.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        mImageView.setBackgroundResource(backgroundResource);
    }

public void animationX() {
    
}
}
