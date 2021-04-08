package ir.alizadeh.mmui.rv;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RtlGridLayoutManager extends GridLayoutManager {
    private boolean isRtl = true;

    public RtlGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public RtlGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public RtlGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    protected boolean isLayoutRTL(){
        return isRtl;
    }

    public RecyclerView.LayoutManager isRtl(boolean isRtl) {
        this.isRtl = isRtl;
        return this;
    }
}