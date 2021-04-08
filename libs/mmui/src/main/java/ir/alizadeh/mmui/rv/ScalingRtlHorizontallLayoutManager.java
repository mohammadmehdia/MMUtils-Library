package ir.alizadeh.mmui.rv;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScalingRtlHorizontallLayoutManager extends RtlHorizontalLayoutManager {

    private float mShrinkAmount = 0.15f;
    private float mShrinkDistance = 1.0f;

    public ScalingRtlHorizontallLayoutManager(Context context) {
        super(context);
    }

    public ScalingRtlHorizontallLayoutManager withParams(Float shrinkAmout, Float shrinkDistance) {
        if(shrinkAmout != null && shrinkAmout >= 0.01f) {
            this.mShrinkAmount = shrinkAmout;
        }
        if(shrinkDistance != null && shrinkDistance > 1.0f) {
            this.mShrinkDistance = shrinkDistance;
        }
        return this;
    }

    @Override
    public int scrollVerticallyBy(int dy, @NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == VERTICAL) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            float midpoint = getHeight() / 2.0f;
            float d0 = 0.0f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.0f;
            float s1 = 1.0f - mShrinkAmount;
            // loop through active children and set scale of child
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if(child != null) {
                    float childMidpoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.0f;
                    float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                    float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
            }
            return scrolled;
        } else {
            return 0;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, @NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            float midpoint = getWidth() / 2.0f;
            float d0 = 0.0f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.0f;
            float s1 = 1.0f - mShrinkAmount;
            // loop through active children and set scale of child
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if(child != null) {
                    float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.0f;
                    float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                    float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
            }
            return scrolled;
        } else {
            return 0;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scrollVerticallyBy(0, recycler, state);
    }
}