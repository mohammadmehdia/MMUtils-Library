package ir.alizadeh.mmui.iv;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import ir.alizadeh.mmui.R;
import ir.alizadeh.mmui.utils.Utils;


public class AspectRatioImageView extends AppCompatImageView {

    private int ratioW = 9;
    private int ratioH = 16;
    private boolean isFixedWidth = true;

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.myiv, 0, 0);
        ratioW = ta.getInteger(R.styleable.myiv_aspect_w, 9);
        ratioH = ta.getInteger(R.styleable.myiv_aspect_h, 16);
        isFixedWidth = ta.getBoolean(R.styleable.myiv_calc_by_w, true);
        boolean centerCrop = ta.getBoolean(R.styleable.myiv_center_crop, true);
        if(centerCrop) {
            setScaleType(ScaleType.CENTER_CROP);
        }
        ta.recycle();
    }

    public void setRatio(int w, int h){
        this.ratioH = h > 0 ? h : 16;
        this.ratioW = w > 0 ? w : 9;
        int gcd = Utils.gcdInt(ratioW, ratioH);
        ratioW /= gcd;
        ratioH /= gcd;
        requestLayout();
    }

    public void setFixedWidth(boolean fixedWidth) {
        isFixedWidth = fixedWidth;
    }

    public int getRatioW() {
        return ratioW;
    }

    public int getRatioH() {
        return ratioH;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(isFixedWidth) {
            //int width = getMeasuredWidth();
            int height = widthSize * ratioH / ratioW;
            setMeasuredDimension(widthSize, height);
        } else {
            //int height = getMeasuredHeight();
            int width = heightSize * ratioW / ratioH;
            setMeasuredDimension(width, heightSize);
        }
    }
}
