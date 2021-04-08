package ir.alizadeh.mmui.custompager;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

import io.github.inflationx.calligraphy3.CalligraphyUtils;
import ir.alizadeh.mmui.R;


public class MyTabLayout extends TabLayout {
    private String fontPath;

    private boolean isIconOnly = false;
    private int[] iconResourceIds = null;
    private int iconTintColor;
    private int iconSelectedTintColor;

    public MyTabLayout(Context context) {
        super(context);
        init(context, null);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initFont(context, attrs);
        if(attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTabLayout, 0, 0);
            this.isIconOnly = ta.getBoolean(R.styleable.MyTabLayout_tablayout_iconOnly, false);
            this.iconTintColor = ta.getColor(R.styleable.MyTabLayout_tablayout_iconColor, Color.LTGRAY);
            this.iconSelectedTintColor = ta.getColor(R.styleable.MyTabLayout_tablayout_iconSelectedColor, iconTintColor);
            int resId = ta.getResourceId(R.styleable.MyTabLayout_tablayout_icons, 0);
            ta.recycle();
            initIconResources(resId);
            Log.e("MyTabLayout", "init: iconResourceIds -> " + (iconResourceIds == null ? "" : Arrays.toString(iconResourceIds)) );
        }
    }

    private void initIconResources(int resId) {
        try {
            if(resId == 0) {
                this.iconResourceIds = null;
            } else {
                TypedArray ta = getResources().obtainTypedArray(resId);
                int len = ta.length();
                iconResourceIds = new int[len];
                for (int i = 0; i < len; i++) {
                    iconResourceIds[i] = ta.getResourceId(i, 0);
                }
                ta.recycle();
            }
        } catch (Exception ignored){}
    }

    private void initFont(Context context, AttributeSet attrs) {
        if(attrs != null) {
            fontPath = pullFontPathFromView(context, attrs, new int[]{R.attr.fontPath});
        }
    }

    @Override
    public void addTab(@NonNull Tab tab, boolean setSelected) {
        Drawable iconDrawable = tab.getIcon();
        Log.d("MyTabLayout", "addTab: " + (iconDrawable == null ? "NULL DRAWABLE" : iconDrawable));

        int tabPosition = getTabCount();
        if(iconResourceIds != null && iconResourceIds.length > tabPosition) {
            if(isIconOnly) {
                if(tab.getIcon() == null) {
                    tab.setIcon(iconResourceIds[tabPosition]);
                }
            }
        }
        super.addTab(tab, setSelected);
    }

    private void addTabFromItemView(TabItem item) {
        if(item != null) {
            final Tab tab = newTab();
            if (item.text != null) {
                tab.setText(item.text);
            }
            if (item.icon != null) {
                tab.setIcon(item.icon);
            }
            if (item.customLayout != 0) {
                tab.setCustomView(item.customLayout);
            }
            if (!TextUtils.isEmpty(item.getContentDescription())) {
                tab.setContentDescription(item.getContentDescription());
            }
            addTab(tab);
        }
    }

    @Override
    public void selectTab(@Nullable Tab tab, boolean updateIndicator) {
        int lastSelectedTabPosition = getSelectedTabPosition();
        super.selectTab(tab, updateIndicator);
        Log.d("MyTabLayout", "selectTab: tab changed from " + lastSelectedTabPosition + " to " + (tab != null ? tab.getPosition() : -1));
        Drawable tabIcon = null;
        if(lastSelectedTabPosition >= 0) {
            Tab currentSelectedTab = getTabAt(lastSelectedTabPosition);
            if(currentSelectedTab != null && (tabIcon = currentSelectedTab.getIcon()) != null) {
                tabIcon.setColorFilter(iconTintColor, PorterDuff.Mode.SRC_IN);
            }
        }
        if(tab != null && (tabIcon = tab.getIcon()) != null) {
            tabIcon.setColorFilter(iconSelectedTintColor, PorterDuff.Mode.SRC_IN);
        }
    }


    /**
     * Tries to pull the Custom Attribute directly from the TextView.
     *
     * @param context Activity Context
     * @param attrs View Attributes
     * @param attributeId if -1 returns null.
     * @return null if attribute is not defined or added to View
     */

    static String pullFontPathFromView(Context context, AttributeSet attrs, int[] attributeId) {
        if (attributeId == null || attrs == null) return null;

        final String attributeName;
        try {
            attributeName = context.getResources().getResourceEntryName(attributeId[0]);
        } catch (Resources.NotFoundException e) {
            // invalid attribute ID
            return null;
        }

        final int stringResourceId = attrs.getAttributeResourceValue(null, attributeName, -1);
        return stringResourceId > 0 ? context.getString(stringResourceId)
                : attrs.getAttributeValue(null, attributeName);
    }

    @Override
    public void addTab(@NonNull Tab tab, int position, boolean setSelected) {
        super.addTab(tab, position, setSelected);
        if(!TextUtils.isEmpty(fontPath)) {
            ViewGroup mainView = (ViewGroup) getChildAt(0);
            ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
            int tabChildCount = tabView.getChildCount();
            for (int i = 0; i < tabChildCount; i++) {
                View tabViewChild = tabView.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    CalligraphyUtils.applyFontToTextView(getContext(), (TextView) tabViewChild, fontPath);
                }
            }
        }
    }

    public void changeTabsFont(Typeface typeface) {
        if(typeface == null) return;
        ViewGroup vg = (ViewGroup) getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }

}
