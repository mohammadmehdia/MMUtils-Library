package ir.alizadeh.mmui.rv;

import android.content.Context;

import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class CustomSmoothScroller extends LinearSmoothScroller {

    public CustomSmoothScroller(Context context) {
        super(context);
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }

    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_START;
    }


    public void smoothScrollToPosition(RecyclerView recyclerView, int position) {
        if(recyclerView != null && recyclerView.getLayoutManager() != null) {
            setTargetPosition(position);
            recyclerView.getLayoutManager().startSmoothScroll(this);
        }
    }

}
