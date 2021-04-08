package ir.alizadeh.mmui.utils;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;

import java.lang.ref.WeakReference;

public class BlurBehindDialogTask extends AsyncTask<Integer, Void, Drawable> {
    private WeakReference<Activity> activityRef;
    private WeakReference<Dialog> dialogRef;
    private int blurRadius = 70;
    private WeakReference<View> decorViewRef = null;
    private Bitmap b1 = null;

    public BlurBehindDialogTask(Activity activity, Dialog dialog) {
        this.activityRef = new WeakReference<>(activity);
        this.dialogRef = new WeakReference<>(dialog);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Activity activity = activityRef.get();
        if(activity == null){
            decorViewRef = null;
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        this.decorViewRef = new WeakReference<>(decorView);
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        b1 = decorView.getDrawingCache();
    }

    @Override
    protected Drawable doInBackground(Integer... params) {
        Activity activity = activityRef.get();
        View decorView = decorViewRef != null ? decorViewRef.get() : null;
        if(activity == null || decorView == null){
            return null;
        }

        if(params != null && params.length > 0 && params[0] > 10){
            this.blurRadius = params[0];
        }
        Rect frame = new Rect();
        decorView.getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        b1 = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
        b1 = Utils.fastBlur(b1, blurRadius);
        return new BitmapDrawable(activity.getResources(),b1);
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        super.onPostExecute(drawable);
        View decorView = decorViewRef != null ? decorViewRef.get() : null;
        if(decorView != null){
            decorView.destroyDrawingCache();
        }
        b1 = null;
        if(drawable != null) {
            Dialog dialog = dialogRef.get();
            if(dialog != null) {
                if(dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(drawable);
                }
                dialog.show();
            }
        }
    }

}
