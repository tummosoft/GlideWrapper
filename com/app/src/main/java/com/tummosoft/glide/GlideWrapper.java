package com.tummosoft.glide;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ImageViewWrapper;

public abstract class GlideWrapper {
    public abstract void initialize(BA ba, String EventName, ImageViewWrapper imv);
    public abstract void fromUrl(String url);
    public abstract void fromFile(String Dir, String FileName);    
}
