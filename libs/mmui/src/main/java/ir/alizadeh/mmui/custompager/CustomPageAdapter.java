package ir.alizadeh.mmui.custompager;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomPageAdapter<T> extends ObjectAtPositionPagerAdapter {
    private List<T> list;

    public CustomPageAdapter() {
        this.list = new ArrayList<>();
    }

    public void setList(List<T> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public T getItem(int position) {
        try {
            return list.get(position);
        } catch (Exception e){
            return null;
        }
    }

    public void onBind(View view, int position) {
        T model = getItem(position);
        if(model != null) {
            this.onBindModel(view, position, model);
        }
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItemObject(ViewGroup container, int position) {
        // TODO: remove
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d("PagerAdapter1", "PagerAdapter1: instantiateItem : " + container.getResources().getConfiguration().getLocales().get(0).getDisplayLanguage());
        }
        View view = this.createView(container, position, getLayoutId(position));
        onBind(view, position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItemObject(ViewGroup container, int position, Object object) {
        if(object instanceof View) {
            container.removeView((View) object);
        }
    }

    @LayoutRes
    public abstract int getLayoutId(int position);

    public abstract View createView(ViewGroup container, int position, @LayoutRes int layoutId);
    public abstract void onBindModel(View view, int position, T model);

}