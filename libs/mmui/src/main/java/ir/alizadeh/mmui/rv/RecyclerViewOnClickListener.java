package ir.alizadeh.mmui.rv;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

public abstract class RecyclerViewOnClickListener implements View.OnClickListener{
    protected WeakReference<RecyclerView> rvRef;
    public RecyclerViewOnClickListener(RecyclerView recyclerView){
        this.rvRef = new WeakReference<>(recyclerView);
    }

    @Override
    public void onClick(View v) {
        RecyclerView rv = rvRef.get();
        if(rv == null) return;
        int pos = rv.getChildLayoutPosition(v);
        onClick(v, pos, rv);
    }

    public abstract void onClick(View v, int position, RecyclerView recyclerView);


}