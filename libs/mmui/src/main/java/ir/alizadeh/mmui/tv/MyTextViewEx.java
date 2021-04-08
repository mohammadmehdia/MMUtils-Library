package ir.alizadeh.mmui.tv;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;

import io.github.inflationx.calligraphy3.CalligraphyUtils;
import ir.alizadeh.mmui.R;


public class MyTextViewEx extends TextViewEx {

    public MyTextViewEx(Context context) {
        super(context);
        checkFontPath(context, null);
    }

    public MyTextViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        checkFontPath(context, attrs);
    }

    public MyTextViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        checkFontPath(context, attrs);
    }

    private void checkFontPath(Context context, AttributeSet attrs) {
        String fontPath = pullFontPathFromView(context, attrs, new int[]{R.attr.fontPath});
        if(!TextUtils.isEmpty(fontPath)) {
            CalligraphyUtils.applyFontToTextView(context, this, fontPath);
        }
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

}
