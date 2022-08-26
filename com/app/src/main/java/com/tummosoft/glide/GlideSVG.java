package com.tummosoft.glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.net.MalformedURLException;
import java.net.URL;

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
