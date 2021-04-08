package ir.alizadeh.mmui.custompager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class ClickableViewPager extends ViewPager {

    private OnItemClickListener mOnItemClickListener;
    GestureDetector tapGestureDetector;

    public ClickableViewPager(Context context) {
        super(context);
        setup();
    }

    public ClickableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        tapGestureDetector = new    GestureDetector(getContext(), new TapGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean tap = (tapGestureDetector.onTouchEvent(ev));
        if(tap) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onPagerItemClick(int position);
    }

    private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onPagerItemClick(getCurrentItem());
            }
            return true;
       }
    }
}