package com.tummosoft.glide;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import anywheresoftware.b4a.BA;

public class GlideSVG {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    private int mWidth;
    private int mHeight;
    private String mSaveTo;

    public GlideSVG(String saveto) {
        mSaveTo = saveto;
    }

   public Bitmap GetFormURL(String url) throws MalformedURLException, IOException, SVGParseException {

    InputStream input = new URL(url).openStream(); 
    SVG svg = SVG.getFromInputStream(input);   
    Bitmap bm = Bitmap.createBitmap(700, 700, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bm);    
    canvas.drawARGB(0,255, 255, 255);
    svg.renderToCanvas(canvas);
        
    return bm;

   }

    // Plain Java
    private static String convertInputStreamToString(InputStream is) throws IOException {

        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }
        return total.toString();
    }
}
