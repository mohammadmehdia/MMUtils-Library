package ir.alizadeh.mmui.spinnerview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.calligraphy3.CalligraphyUtils;
import ir.alizadeh.mmui.R;
import ir.alizadeh.mmui.tv.MyTextView;
import ir.alizadeh.mmui.utils.GroupDividerItemDecoration;
import ir.alizadeh.mmui.utils.Utils;


public class MySpinnerView extends FrameLayout implements View.OnClickListener {
    public static final int ROUND_TOP_LEFT = 1;
    public static final int ROUND_TOP_RIGHT = 2;
    public static final int ROUND_BOTTOM_RIGHT = 4;
    public static final int ROUND_BOTTOM_LEFT = 8;
    public static final int ROUND_RIGHT = 6;
    public static final int ROUND_LEFT = 9;
    public static final int ROUND_TOP = 3;
    public static final int ROUND_BOTTOM = 12;
    public static final int ROUND_ALL = 15;

    private MyTextView tv;
    private AppCompatImageView ivIcon;
    public CustomPopupWindow<String> popupWindow = null;
    private Listener listener;
    private int currentSelectedItem = -1;
    private List<String> options = null;

    private String text = "";
    private boolean expanded = false;
    private int bgColor;
    private int textColor;
    private int iconColor;
    private int borderColor;
    private int iconSize;
    private int cornerRadius;
    private ColorStateList bgColorState;
    private int cornerType;
    private int offsetY = 0;
    private float offsetYScreenHeightPercent = -1;
    private int popupGravity = Gravity.NO_GRAVITY;
    private int textSize = 0;
    private String hint;
    private int hintColor;
    private boolean isBold = false;
    private int icon = 0;
    private boolean fixedIcon = false;
    private String fontPathAttr;

    public MySpinnerView(Context context) {
        super(context);
        loadAttrs(context, null);
        init(context);
    }

    public MySpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        init(context);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        text = "";
        expanded = false;
        bgColor = ContextCompat.getColor(context, R.color.msv_bg_color_default);
        hintColor = textColor = ContextCompat.getColor(context, R.color.msv_text_color_default);
        textSize = getResources().getDimensionPixelSize(R.dimen.text_lg);
        iconColor = textColor;
        borderColor = ContextCompat.getColor(context, R.color.msv_border_color_default);
        iconSize = context.getResources().getDimensionPixelSize(R.dimen.msv_icon_size_default);
        cornerRadius = context.getResources().getDimensionPixelSize(R.dimen.msv_corner_radius_default);
        bgColorState = null;
        cornerType = ROUND_ALL;
        offsetY = 0;
        offsetYScreenHeightPercent = -1;
        popupGravity = Gravity.NO_GRAVITY;

        if(attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MySpinnerView, 0, 0);
            bgColor = ta.getColor(R.styleable.MySpinnerView_msv_bgColor, bgColor);
            expanded = ta.getBoolean(R.styleable.MySpinnerView_msv_expanded, expanded);
            textColor = ta.getColor(R.styleable.MySpinnerView_msv_textColor, textColor);
            textSize = ta.getDimensionPixelSize(R.styleable.MySpinnerView_msv_textSize, textSize);
            iconColor = ta.getColor(R.styleable.MySpinnerView_msv_iconColor, iconColor);
            borderColor = ta.getColor(R.styleable.MySpinnerView_msv_borderColor, borderColor);
            iconSize = ta.getDimensionPixelSize(R.styleable.MySpinnerView_msv_iconSize, iconSize);
            cornerRadius = ta.getDimensionPixelSize(R.styleable.MySpinnerView_msv_cornerRadius, cornerRadius);
            text = ta.hasValue(R.styleable.MySpinnerView_msv_text) ? ta.getString(R.styleable.MySpinnerView_msv_text) : "";
            cornerType = ta.getInteger(R.styleable.MySpinnerView_msv_round_corner_type, cornerType);
            fixedIcon = ta.getBoolean(R.styleable.MySpinnerView_msv_iconFixed, false);
            icon = ta.getResourceId(R.styleable.MySpinnerView_msv_icon, 0);
            bgColorState = ta.hasValue(R.styleable.MySpinnerView_msv_bgColorState) ? ta.getColorStateList(R.styleable.MySpinnerView_msv_bgColorState) : null;

            if(ta.hasValue(R.styleable.MySpinnerView_android_entries)){
                CharSequence[] entries = ta.getTextArray(R.styleable.MySpinnerView_android_entries);
                if(entries != null){
                    options = new ArrayList<>();
                    for(CharSequence entry : entries){
                        if(entry != null){
                            options.add(entry.toString());
                        }
                    }
                }
            }

            offsetY = ta.getDimensionPixelSize(R.styleable.MySpinnerView_msv_popup_offsetVertical, 0);
            if(ta.hasValue(R.styleable.MySpinnerView_msv_popup_offsetVertical_screenHeight_percent)) {
                offsetYScreenHeightPercent = ta.getFloat(R.styleable.MySpinnerView_msv_popup_offsetVertical_screenHeight_percent, -10);
                if(Math.abs(offsetYScreenHeightPercent) < 1) {
                    offsetY = (int) (offsetYScreenHeightPercent * getResources().getDisplayMetrics().heightPixels);
                }
            }

            popupGravity = ta.getInteger(R.styleable.MySpinnerView_msv_popup_anchor_gravity, Gravity.NO_GRAVITY);
            if(popupGravity == 1) popupGravity = Gravity.TOP;
            else if(popupGravity == 2) popupGravity = Gravity.BOTTOM;
            else popupGravity = Gravity.NO_GRAVITY;


            isBold = ta.getBoolean(R.styleable.MySpinnerView_msv_textBold, false);
            hint = ta.hasValue(R.styleable.MySpinnerView_msv_hint) ? ta.getString(R.styleable.MySpinnerView_msv_hint) : "";
            hintColor = ta.getColor(R.styleable.MySpinnerView_msv_hintColor, textColor);
            ta.recycle();
            fontPathAttr = Utils.pullFontPathFromView(getContext(), attrs, new int[] {R.attr.fontPath});
        }
    }

    public void setOnItemSelectedListener(Listener listener) {
        this.listener = listener;
    }

    private void padding(Context context, AttributeSet attrs) {
        if(attrs != null) {
            int [] attributes = new int [] {
                    android.R.attr.paddingStart,
                    android.R.attr.paddingEnd,
                    android.R.attr.paddingLeft,
                    android.R.attr.paddingTop,
                    android.R.attr.paddingRight,
                    android.R.attr.paddingBottom
            };
            int [] padding = new int[6];
            int [] defPadding = new int[] {
                    context.getResources().getDimensionPixelSize(R.dimen.msv_padding_right_left_default),
                    context.getResources().getDimensionPixelSize(R.dimen.msv_padding_right_left_default),
                    context.getResources().getDimensionPixelSize(R.dimen.msv_padding_right_left_default),
                    context.getResources().getDimensionPixelSize(R.dimen.msv_padding_top_default),
                    context.getResources().getDimensionPixelSize(R.dimen.msv_padding_right_left_default),
                    context.getResources().getDimensionPixelSize(R.dimen.msv_padding_bottom_default)
            };
            boolean shouldUpdate = false;
            TypedArray ta = context.obtainStyledAttributes(attrs, attributes);
            for(int index = 0 ; index < 6; index++){
                if(ta.hasValue(index)){
                    padding[index] = ta.getDimensionPixelSize(index, defPadding[index]);
                } else {
                    padding[index] = index < 2 ? -1 : defPadding[index];
                    shouldUpdate = true;
                }
            }
            ta.recycle();
            if(shouldUpdate){
                int pleft = Math.max(padding[0], padding[2]);
                int pright = Math.max(padding[1], padding[4]);
                this.setPadding(pleft, padding[1], pright, padding[3]);
            }
        }
    }

    private void init(Context context){
        View v = inflate(context, R.layout.view_my_spinner, this);
        tv = v.findViewById(R.id.tv);
        ivIcon = v.findViewById(R.id.iv_icon);
        if(icon != 0) {
            ivIcon.setImageResource(icon);
        }

        setIconSize(iconSize);
        setText(text);
        setOptions(options);
        setTextColor(textColor);
        setIconColor(iconColor);
        setClickable(true);
        setFocusable(true);
        bg();
        setExpanded(expanded);
        setOnClickListener(this);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        if(!TextUtils.isEmpty(fontPathAttr)) {
            try {
                CalligraphyUtils.applyFontToTextView(context, this.tv, fontPathAttr);
            } catch (Exception ignored){}
        }
        tv.setBold(isBold);
    }

    public void setIconSize(int iconSize) {
        if(iconSize <0) return;
        this.iconSize = iconSize;
        LayoutParams lp = (LayoutParams) ivIcon.getLayoutParams();
        lp.width = iconSize;
        lp.height = iconSize;
        ivIcon.setLayoutParams(lp);
    }

    public void setText(String value) {
        this.text = !TextUtils.isEmpty(value) ? value : hint;
        tv.setText(text);
        tv.setTextColor(currentSelectedItem < 0 ? hintColor : textColor);

    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        tv.setTextColor(textColor);
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
        ivIcon.setColorFilter(iconColor);
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        bg();
    }

    public void setOptions(List<String> options){
        int lastSelectedItem = currentSelectedItem;

        this.options = options;
        if(popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        if(options == null) return;
        popupWindow = new CustomPopupWindow<>(this, options);
        if(lastSelectedItem < 0 || lastSelectedItem >= options.size()) {
            currentSelectedItem = -1;
            if(!TextUtils.isEmpty(hint)) {
                setText(hint);
            }
        }
    }

    public void toggle(){
        if(expanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        setOptions(this.options);
        setExpanded(true);
        popupWindow.setWidth(this.getWidth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow.setOverlapAnchor(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(this, 0, offsetY, popupGravity);
        } else {
            popupWindow.showAsDropDown(this, 0, offsetY);
        }

        Utils.dimBehindPopupWindow(popupWindow);
    }

    public void collapse(){
        setExpanded(false);
        if(popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    private void bg() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] {
                cornerRadii(ROUND_TOP_LEFT), cornerRadii(ROUND_TOP_LEFT),
                cornerRadii(ROUND_TOP_RIGHT), cornerRadii(ROUND_TOP_RIGHT),
                cornerRadii(ROUND_BOTTOM_RIGHT), cornerRadii(ROUND_BOTTOM_RIGHT),
                cornerRadii(ROUND_BOTTOM_LEFT), cornerRadii(ROUND_BOTTOM_LEFT)});

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(bgColorState != null) {
                shape.setColor(bgColorState);
            } else {
                int[][] states = new int[][] {
                        new int[] { android.R.attr.state_pressed },
                        new int[] { android.R.attr.state_focused },
                        new int[] { android.R.attr.state_enabled },
                        new int[] {-android.R.attr.state_enabled },
                };

                int c = Utils.decreaseRgbChannels(bgColor, 0.2f);
                int[] colors = new int[] {
                        c, c,
                        bgColor, bgColor
                };

                ColorStateList myList = new ColorStateList(states, colors);
                shape.setColor(myList);
            }
        } else {
            shape.setColor(bgColor);
        }
        shape.setStroke(Utils.dp2px(1f, getContext()), borderColor);
        setBackground(shape);
    }

    private int cornerRadii(int corner){
        return (cornerType & corner) != 0 ? cornerRadius : 0;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;

        if(!fixedIcon) {
            if(expanded) {
                ivIcon.setRotation(180);
            } else {
                ivIcon.setRotation(0);
            }
        }

    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    public boolean isExpanded() {
        return expanded;
    }

    public int getSelectedItemPosition() {
        return currentSelectedItem;
    }

    public void setSelectedItemPosition(int pos) {
        if(this.options != null && !this.options.isEmpty()) {
            if(pos >= 0 && pos < this.options.size()) {
                this.currentSelectedItem = pos;
                String str = options.get(pos);
                this.setText(str);
                if(listener != null) {
                    listener.onItemSelected(pos, str);
                }
            }
        }
    }

    public String getSelectedItemText() {
        try {
            return options.get(currentSelectedItem);
        } catch (Exception ignored){}
        return null;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
    }

    public static class CustomPopupWindow<T> extends PopupWindow implements PopupWindow.OnDismissListener {
        private WeakReference<MySpinnerView> spinnerRef;
        private List<T> options;


        public CustomPopupWindow(MySpinnerView view, List<T> options) {
            super(view.getContext());
            this.spinnerRef = new WeakReference<>(view);
            this.options = options;
            initView();
        }

        private void initView(){
            MySpinnerView spinnerView = spinnerRef.get();
            if(spinnerView == null) return;
            if(options == null || options.isEmpty()) return;
            View v = LayoutInflater.from(spinnerView.getContext())
                    .inflate(R.layout.view_my_spinner_header_popup_view, null);
            RecyclerView rv = v.findViewById(R.id.rv);
            ItemAdapter<T> adapter = new ItemAdapter<>(spinnerView, options);
            rv.setLayoutManager(new LinearLayoutManager(spinnerView.getContext()));
            rv.addItemDecoration(new GroupDividerItemDecoration(spinnerView.getContext(), DividerItemDecoration.VERTICAL).drawAllExceptLast());
            rv.setAdapter(adapter);
            setContentView(v);
            this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setOutsideTouchable(true);
            setOnDismissListener(this);
        }

        @Override
        public void onDismiss() {
            MySpinnerView spinnerView = spinnerRef.get();
            if(spinnerView != null) {
                spinnerView.collapse();
            }
        }
    }

    private static class ItemAdapter<T> extends RecyclerView.Adapter<VH> implements OnClickListener {
        private WeakReference<MySpinnerView> spinnerRef;
        private List<T> items;

        public ItemAdapter(MySpinnerView view, List<T> items) {
            this.spinnerRef = new WeakReference<>(view);
            this.items = items;
        }

        @NotNull
        @Override
        public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            int textColor = (spinnerRef != null && spinnerRef.get() != null) ? spinnerRef.get().textColor : Color.LTGRAY;
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_my_spinner_dropdown_list_item, parent, false);
            return new VH(v, textColor);
        }

        @Override
        public void onBindViewHolder(@NotNull VH holder, int position) {
            MySpinnerView spinnerView = spinnerRef.get();
            if(spinnerView != null) {
                T item = items.get(position);
                holder.tv.setText(item.toString());
                holder.icCheck.setVisibility(position == spinnerView.currentSelectedItem ? VISIBLE : GONE);
                holder.itemView.setTag(position);
                holder.itemView.setOnClickListener(this);
            }
        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public void onClick(View v) {
            MySpinnerView spinnerView = spinnerRef.get();
            if(spinnerView == null) return;
            if(v.getTag() != null){
                int pos = (int) v.getTag();
                if(items !=null && pos < items.size()) {
                    if (spinnerView.listener != null) {
                        spinnerView.listener.onItemSelected(pos, items.get(pos).toString());
                    }
                    spinnerView.currentSelectedItem = pos;
                    notifyDataSetChanged();
                    spinnerView.setText(items.get(pos).toString());
                }
            }
            if(spinnerView.popupWindow != null){
                spinnerView.popupWindow.dismiss();
            }
        }
    }

    private static class VH extends RecyclerView.ViewHolder{
        private View icCheck;
        private AppCompatTextView tv;
        public VH(View itemView, int textColor) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            icCheck = itemView.findViewById(R.id.ic_check);
            if(tv != null) {
                tv.setTextColor(textColor);
            }
        }
    }

    public interface Listener {
        void onItemSelected(int position, String text);
    }

}
