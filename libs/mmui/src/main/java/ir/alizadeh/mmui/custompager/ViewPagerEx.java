package ir.alizadeh.mmui.custompager;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import ir.alizadeh.mmui.R;


public class ViewPagerEx extends CustomPager {
    private static final int PAGE_TRANSFORM_MODE_DEFAULT = 0;
    private static final int PAGE_TRANSFORM_MODE_CARDSCALE = 1;
    private int pageTransformMode = 0;
    private int cardScaleBaseElevation = 0, cardScaleRaisingElevation = 0;
    private float cardScaleScaleFactor = 0.90f;
    private int pageMargin = 0;
    public ViewPagerEx(Context context) {
        super(context);
        init(context, null);
    }

    public ViewPagerEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ViewPagerEx, 0, 0);
            pageTransformMode = ta.getInt(R.styleable.ViewPagerEx_vpager_pageTransformation, PAGE_TRANSFORM_MODE_DEFAULT);
            cardScaleBaseElevation = ta.getDimensionPixelSize(R.styleable.ViewPagerEx_vpager_cardScale_baseElevation, 0);
            cardScaleRaisingElevation = ta.getDimensionPixelSize(R.styleable.ViewPagerEx_vpager_cardScale_raisingElevation, 0);
            cardScaleScaleFactor = ta.getFloat(R.styleable.ViewPagerEx_vpager_cardScale_scaleFactor, 0.90f);
            pageMargin = ta.getDimensionPixelSize(R.styleable.ViewPagerEx_vpager_pageMargin, 0);
            ta.recycle();
        }

        if(pageMargin > 0) {
            setPageMargin(pageMargin);
        }

        if(pageTransformMode == PAGE_TRANSFORM_MODE_CARDSCALE) {
            setPageTransformer(false, new CardScaleTransformer(cardScaleBaseElevation, cardScaleRaisingElevation, cardScaleScaleFactor));
        }
    }

}
