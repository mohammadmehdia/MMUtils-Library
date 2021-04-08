package ir.alizadeh.mmui.utils;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

public class SimpleProgressBar extends ProgressBar {

    public SimpleProgressBar(Context context) {
        super(context);
    }

    public SimpleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public synchronized void setProgress(int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.setProgress(progress, true);
        } else {
            super.setProgress(progress);
        }
    }

}
