package ir.alizadeh.mmui.rv;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class RtlHorizontalLayoutManager extends LinearLayoutManager {


    private boolean isRtl = true;

    public RtlHorizontalLayoutManager(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public RtlHorizontalLayoutManager(Context context, boolean reverseLayout) {
        super(context, LinearLayoutManager.HORIZONTAL, reverseLayout);
    }

    public RtlHorizontalLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public RtlHorizontalLayoutManager isRtl(boolean isRtl) {
        this.isRtl = isRtl;
        return this;
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
    @Override
    protected boolean isLayoutRTL(){
        return isRtl;
    }

}
