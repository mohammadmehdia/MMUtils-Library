package ir.alizadeh.mmui.tv;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import io.github.inflationx.calligraphy3.CalligraphyUtils;
import ir.alizadeh.mmui.R;
import ir.alizadeh.mmui.utils.Utils;


@BindingMethods({
        @BindingMethod(type= MyEditText.class, attribute = "mytv_isBold", method = "setBold")
})
public class MyEditText extends AppCompatEditText {
    private static final String FONT_ASSET_DIR = "fonts";

    private String fontPathAttr;

    private boolean isBold = false;
    private boolean hasAttrIsBold = false;
    private boolean isDebugEnabled = false;

    public MyEditText(Context context) {
        super(context);
        init(context, null);
        refresh();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        refresh();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        refresh();
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextView, 0, 0);
            this.hasAttrIsBold = ta.hasValue(R.styleable.MyTextView_mytv_isBold);
            this.isBold = ta.getBoolean(R.styleable.MyTextView_mytv_isBold, false);
            this.isDebugEnabled = ta.getBoolean(R.styleable.MyTextView_enDebug, false);
            ta.recycle();
        }

        fontPathAttr = pullFontPathFromView(getContext(), attrs, new int[] { R.attr.fontPath });
    }

    public void setBold(boolean isBold) {
        this.isBold = isBold;
        this.hasAttrIsBold = true;
        refresh();
    }

    private void checkFontPath() {
        if(!TextUtils.isEmpty(fontPathAttr) && hasAttrIsBold) {
            if(!isBold && fontPathAttr.contains("-Bold")) {
                String temp = this.fontPathAttr.replace("-Bold", "");
                if(fontPathExists(temp)){
                    this.fontPathAttr = temp;
                }
                return;
            }
            if(fontPathAttr.contains("Bold")) return;
            String ext = Utils.fileExtention(fontPathAttr);
            String temp = fontPathAttr;
            if(!TextUtils.isEmpty(ext)) {
                temp = fontPathAttr.replaceFirst("." + ext, "");
            }
            temp = temp + "-Bold." + ext;
            if(fontPathExists(temp)){
                this.fontPathAttr = temp;
            }
        }
    }

    private boolean fontPathExists(String fontPath) {
        try {
            if(TextUtils.isEmpty(fontPath)) return false;
            String[] list = getResources().getAssets().list(FONT_ASSET_DIR);
            if(list == null || list.length == 0) return false;
            String name = fontPath.replace(FONT_ASSET_DIR + "/" , "");
            if(TextUtils.isEmpty(name)) return false;
            for (String asset:list) {
                if(name.equals(asset)) return true;
            }
            return false;
        } catch (Exception ignored){ }
        return false;
    }

    private void refresh() {
        debug("refresh: fontPath = " + fontPathAttr);
        checkFontPath();
        if(!TextUtils.isEmpty(this.fontPathAttr)) {
            CalligraphyUtils.applyFontToTextView(getContext(), this, fontPathAttr);
        }
    }

    private void debug(String text, boolean force) {
        if( (force || isDebugEnabled) && !TextUtils.isEmpty(text)) {
            String locale = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    locale = getContext().getApplicationContext().getResources().getConfiguration().getLocales().toString();
                } catch (Exception ignored){}
            }
            Log.d("MyET", "ctxRes(" + getContext().getResources() + "), res(" + getResources() + "), locale: " + locale + " | " + text );
        }
    }

    private void debug(String text) {
        this.debug(text, false);
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

    public String getTextStr() {
        CharSequence temp = this.getText();
        if(temp == null) return null;
        return temp.toString();
    }

}
