package ir.alizadeh.mmui.rv;


import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.alizadeh.mmui.utils.Utils;

public class HideKeyboardOnItemTouchListener implements  RecyclerView.OnItemTouchListener {

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv,@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv,@NonNull MotionEvent e) {
        Log.d("HideKbHelper", "onTouchEvent: action = "  + e.getAction());
        Utils.hideInputMethod(rv);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
