package ir.alizadeh.mmui.btn;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.IntRange;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.thekhaeng.pushdownanim.PushDownAnim;
import com.wang.avi.AVLoadingIndicatorView;

import io.github.inflationx.calligraphy3.CalligraphyUtils;
import ir.alizadeh.mmui.R;
import ir.alizadeh.mmui.tv.MyTextView;
import ir.alizadeh.mmui.utils.Utils;

@BindingMethods({
        @BindingMethod(type= MyButton.class, attribute = "mb_text", method = "setText"),
        @BindingMethod(type= MyButton.class, attribute = "mb_bgColor", method = "setBgColor"),
        @BindingMethod(type= MyButton.class, attribute = "mb_bgColor2", method = "setBgColor2"),
        @BindingMethod(type = MyButton.class, attribute = "mb_enable", method = "setEnabled")
})
public class MyButton extends ConstraintLayout {
    private static final int ICON_SIZE_MD = 0;
    private static final int ICON_SIZE_SM = 1;
    private static final int ICON_SIZE_LG = 2;

    private static final float DISABLE_COLOR_DECREASE_PERCENT = 0.20f;

    private int textColor = Color.WHITE;
    private int textSize;
    private String text = "";
    private int leftIcon, rightIcon;
    private int iconSize;
    private int shadowColor;
    private int shadowSize;
    private boolean shadowEnable = false;
    private int bgColor, bgColor2;
    private boolean textAllCaps;

    private int cornerRadius;
    private String loadingIndicator = "BallPulseIndicator";
    private boolean showOutline = false;
    private int font = 0;
    private ColorStateList normalColorState = null;
    private MyTextView tv1;
    private AVLoadingIndicatorView progressView;
    //    private ProgressBar progressView;
    private AppCompatImageView leftIconView, rightIconView;
    private boolean mb_en = true;
    private boolean colorStateEnable = true;
    private boolean pushDownAnim = false;
    private float pushDownAnimScale = 0.95f;
    private boolean fontIsBold = false;
    private String fontPathAttrVal = null;

    public MyButton(Context context) {
        super(context);
        init(context);
        refresh();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        refresh();
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        init(context);
        textColor = ContextCompat.getColor(context, R.color.my_btn_text_color_default);
        leftIcon = rightIcon = 0;
        textSize = -1;
        text = "";
        shadowColor = ContextCompat.getColor(context, R.color.my_btn_shadow_color_default);
        shadowSize = context.getResources().getDimensionPixelSize(R.dimen.my_btn_shadow_size_default);
        shadowEnable = false;
        bgColor = bgColor2 = Color.WHITE;
        cornerRadius = context.getResources().getDimensionPixelSize(R.dimen.my_btn_corner_radius);
        loadingIndicator = context.getString(R.string.avi_BallPulseIndicator);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyButton, 0, 0);
        textColor = ta.getColor(R.styleable.MyButton_mb_textColor, textColor);
        textSize = ta.getDimensionPixelSize(R.styleable.MyButton_mb_textSize, textSize);
        textAllCaps = ta.getBoolean(R.styleable.MyButton_mb_textAllCaps, false);
        if (ta.hasValue(R.styleable.MyButton_mb_text)) {
            text = ta.getString(R.styleable.MyButton_mb_text);
        }
        if (ta.hasValue(R.styleable.MyButton_mb_loadingIndicator)) {
            loadingIndicator = ta.getString(R.styleable.MyButton_mb_loadingIndicator);
        }
        fontIsBold = ta.getBoolean(R.styleable.MyButton_mb_font_bold, false);
        leftIcon = ta.getResourceId(R.styleable.MyButton_mb_leftIcon, 0);
        rightIcon = ta.getResourceId(R.styleable.MyButton_mb_rightIcon, 0);
        iconSize = ta.getInteger(R.styleable.MyButton_mb_iconSize, ICON_SIZE_MD);
        shadowColor = ta.getColor(R.styleable.MyButton_mb_shadowColor, shadowColor);
        shadowSize = ta.getDimensionPixelSize(R.styleable.MyButton_mb_shadowSize, shadowSize);
        shadowEnable = ta.getBoolean(R.styleable.MyButton_mb_shadowSize, shadowEnable);
        bgColor = ta.getColor(R.styleable.MyButton_mb_bgColor, bgColor);
        bgColor2 = ta.getColor(R.styleable.MyButton_mb_bgColor2, bgColor);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.MyButton_mb_cornerRadius, cornerRadius);
        showOutline = ta.getBoolean(R.styleable.MyButton_mb_outlined, false);
        normalColorState = ta.getColorStateList(R.styleable.MyButton_mb_colorState);
        mb_en = (ta.getBoolean(R.styleable.MyButton_mb_enable, true));
        pushDownAnim = ta.getBoolean(R.styleable.MyButton_mb_pushDownAnim, false);
        pushDownAnimScale = ta.getFloat(R.styleable.MyButton_mb_pushDownAnimScale, 0.95f);
        boolean isLoading = ta.getBoolean(R.styleable.MyButton_mb_isLoading, false);
        ta.recycle();
        fontPathAttrVal = pullFontPathFromView(getContext(), attrs, new int[]{R.attr.fontPath} );
        setEnabled(this.mb_en);
        if (pushDownAnim) {
            PushDownAnim.setPushDownAnimTo(this).setScale(PushDownAnim.MODE_SCALE, pushDownAnimScale);
        }

        this.progress(isLoading);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_my_button, this);
        tv1 = findViewById(R.id.tv1);
        progressView = findViewById(R.id.progress_view);
        leftIconView = findViewById(R.id.left_icon_view);
        rightIconView = findViewById(R.id.right_icon_view);
        this.colorStateEnable = true;
    }

    public void refresh() {
        tv1.setTextColor(textColor);
        if(textSize > 0) {
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        tv1.setText(text);
        tv1.setAllCaps(textAllCaps);
        typeface();
        leftIconView.setImageResource(leftIcon);
        leftIconView.setColorFilter(showOutline ? bgColor : textColor);
        rightIconView.setImageResource(rightIcon);
        rightIconView.setColorFilter(showOutline ? bgColor : textColor);
        setIconSize(iconSize);

        progressView.setIndicatorColor(textColor);
        progressView.setIndicator(loadingIndicator);
        bg();
    }


    public void clearIconColorFilter() {
        leftIconView.clearColorFilter();
        leftIconView.setBackgroundResource(leftIcon);
        leftIconView.clearColorFilter();
        leftIconView.setBackgroundResource(rightIcon);
    }

    private String pullFontPathFromView(Context context, AttributeSet attrs, int[] attributeId) {
        try {
            if (attributeId == null || attrs == null) return null;
            final String attributeName;
            attributeName = context.getResources().getResourceEntryName(attributeId[0]);
            final int stringResourceId = attrs.getAttributeResourceValue(null, attributeName, -1);
            return stringResourceId > 0 ? context.getString(stringResourceId) : attrs.getAttributeValue(null, attributeName);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    private void typeface() {
        try {
            if (!TextUtils.isEmpty(fontPathAttrVal)) {
                CalligraphyUtils.applyFontToTextView(getContext(), this.tv1, fontPathAttrVal);
            }
        } catch (Exception ignored){}
        this.tv1.setBold(fontIsBold);
    }

    private void bg() {
        int bgColor = isEnabled() ? this.bgColor : Utils.decreaseRgbChannels(this.bgColor, DISABLE_COLOR_DECREASE_PERCENT);
        int bgColor2 = isEnabled() ? this.bgColor2 : Utils.decreaseRgbChannels(this.bgColor2, DISABLE_COLOR_DECREASE_PERCENT);

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius});
        if (showOutline) {
            int strokeWidth = getContext().getResources().getDimensionPixelSize(R.dimen.my_btn_stroke_width_def);
            shape.setStroke(strokeWidth, bgColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int[][] states = new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_enabled},
                };

                int c = Utils.decreaseRgbChannels(bgColor, 0.4f);
                int[] colors = new int[]{
                        c, c,
                        Color.TRANSPARENT, Color.TRANSPARENT
                };

                ColorStateList myList = new ColorStateList(states, colors);
                shape.setColor(myList);
            } else {
                shape.setColor(Color.TRANSPARENT);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (colorStateEnable && normalColorState != null) {
                    shape.setColor(normalColorState);
                } else {
                    shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                    shape.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                    shape.mutate();
                    shape.setColors(new int[]{bgColor, bgColor2});
                }
            } else {
                shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                shape.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                shape.mutate();
                shape.setColors(new int[]{bgColor, bgColor2});
            }
        }
        setBackground(shape);
    }

    public void setIconSize(@IntRange(from=ICON_SIZE_MD, to=ICON_SIZE_LG) int iconSize) {
        if (iconSize < 0) return;
        this.iconSize = iconSize;
        float widthPercent = iconSize == ICON_SIZE_SM ? 0.08f: iconSize == ICON_SIZE_LG ? 0.12f : 0.1f;

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) leftIconView.getLayoutParams();
        params.matchConstraintPercentWidth = widthPercent;
        leftIconView.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) rightIconView.getLayoutParams();
        params.matchConstraintPercentWidth = widthPercent;
        rightIconView.setLayoutParams(params);
    }

    public void setText(String text) {
        if (text != null) {
            this.text = text;
            tv1.setText(text);
        }
    }

    public void setLeftIcon(int iconResId) {
        this.leftIcon = iconResId;
        leftIconView.setImageResource(iconResId);
    }

    public void setRightIcon(int iconResId) {
        this.leftIcon = iconResId;
        rightIconView.setImageResource(iconResId);
    }

    public void setColorStateEnable(boolean en) {
        this.colorStateEnable = en;
        bg();
    }

    public void hideProgress() {
        progressView.setVisibility(INVISIBLE);
        tv1.setVisibility(VISIBLE);
        leftIconView.setVisibility(VISIBLE);
        rightIconView.setVisibility(VISIBLE);
    }

    public void showProgress() {
        progressView.setVisibility(VISIBLE);
        tv1.setVisibility(INVISIBLE);
        leftIconView.setVisibility(INVISIBLE);
        rightIconView.setVisibility(INVISIBLE);
    }

    public void progress(boolean show) {
        if (show) {
            showProgress();
        } else {
            hideProgress();
        }
    }

    public boolean isInProgress() {
        return progressView.getVisibility() == VISIBLE;
    }

    public void setBgColor(int color) {
        this.bgColor = color;
        bg();
    }

    public void setBgColor(int color1, int color2) {
        this.bgColor = color1;
        this.bgColor2 = color2;
        bg();
    }

    public void setBgColor2(int bgColor2) {
        this.bgColor2 = bgColor2;
        bg();
    }

    public void setShowOutline(boolean showOutline) {
        this.showOutline = showOutline;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.refresh();
    }

}