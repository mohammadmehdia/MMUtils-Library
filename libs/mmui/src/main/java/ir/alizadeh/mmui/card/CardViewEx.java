package ir.alizadeh.mmui.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import ir.alizadeh.mmui.R;


public class CardViewEx extends CardView {

    boolean circleCorners = false;

    public CardViewEx(@NonNull Context context) {
        super(context);
    }

    public CardViewEx(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CardViewEx(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.cardex, 0, 0);
        circleCorners = ta.getBoolean(R.styleable.cardex_cardex_circle_corners, false);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(circleCorners) {
            int h = getMeasuredHeight();
            setRadius((float) h / 2);
        }
    }

}
