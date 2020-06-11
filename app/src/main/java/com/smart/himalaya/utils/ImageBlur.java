package com.smart.himalaya.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

public class ImageBlur {
    public static void makeBlur(Context context, ImageView imageview) {
        BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap blurred = blurRenderScript(bitmap, 10, context); //second parametre is radius max:25
        imageview.setImageBitmap(blurred); //radius decide blur amount
    }

    public static void makeBlur(Context context, Drawable drawable, ImageView imageView) {
        Bitmap bitmap = ImageTools.drawableToBitmap(drawable);
        Bitmap blurred = blurRenderScript(bitmap, 10, context); //second parametre is radius max:25
        imageView.setImageBitmap(blurred); //radius decide blur amount
    }

    public static void makeBlur(Context context, Bitmap bitmap, ImageView imageview) {
        Bitmap blurred = blurRenderScript(bitmap, 10, context); //second parametre is radius max:25
        imageview.setImageBitmap(blurred); //radius decide blur amount
    }

    private static Bitmap blurRenderScript(Bitmap smallBitmap, int radius, Context context) {
        smallBitmap = RGB565toARGB888(smallBitmap);
        Bitmap bitmap = Bitmap.createBitmap(smallBitmap.getWidth(), smallBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript renderScript = RenderScript.create(context);
        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);
        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        return bitmap;
    }

    private static Bitmap RGB565toARGB888(Bitmap img) {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }
}