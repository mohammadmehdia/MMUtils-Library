package ir.alizadeh.mmui.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import java.io.IOException;
import java.io.InputStream;

public class AssetsReader {

    public static Drawable getDrawableFromAssets(Context context, String filename){
        Drawable drawable = null;
        InputStream inputStream = null;
        try {
            Resources resources = context.getResources();
            inputStream = resources.getAssets().open(filename);
            //drawable = Drawable.createFromStream(inputStream, null);
            drawable =  Drawable.createFromResourceStream(resources,new TypedValue(), inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return drawable;
    }

}
