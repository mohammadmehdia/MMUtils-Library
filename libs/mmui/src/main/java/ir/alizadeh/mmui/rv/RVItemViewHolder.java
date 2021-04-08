package ir.alizadeh.mmui.rv;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import ir.alizadeh.mmui.utils.Utils;


public class RVItemViewHolder<VDBINDING extends ViewDataBinding> extends RecyclerView.ViewHolder implements View.OnClickListener{

    protected VDBINDING binding;
    private boolean isItemClickable = false;

    public RVItemViewHolder(View view, boolean isItemClickable) {
        super(view);
        this.isItemClickable = isItemClickable;
        if(isItemClickable) {
            this.itemView.setOnClickListener(this);
        }
    }

    public RVItemViewHolder(VDBINDING binding, boolean isItemClickable) {
        this(binding.getRoot(), isItemClickable);
        this.binding = binding;
    }

    public RVItemViewHolder(View view) {
        this(view, false);
    }

    public RVItemViewHolder(VDBINDING binding) {
        this(binding.getRoot(), false);
    }

    public void bind(Object obj) {
        if(this.binding != null) {
            binding.setVariable(getBindingModelVariableId(), obj);
            binding.executePendingBindings();
        }
    }

    public void removeClickListener() {
        this.itemView.setOnClickListener(null);
    }

    public void setItemViewWidthMatchParent() {
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        itemView.setLayoutParams(lp);
    }

    public void setItemViewHeightMatchParent() {
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        itemView.setLayoutParams(lp);
    }

    public void setItemViewWidthPercent(int percent) {
        if(percent > 0 && percent <= 100) {
            DisplayMetrics metrics = itemView.getResources().getDisplayMetrics();
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = metrics.widthPixels * percent / 100;
            itemView.setLayoutParams(lp);
        }
    }

    public void setItemViewHeightPercent(int percent) {
        if(percent > 0 && percent <= 100) {
            DisplayMetrics metrics = itemView.getResources().getDisplayMetrics();
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.height = metrics.heightPixels * percent / 100;
            itemView.setLayoutParams(lp);
        }
    }

    @Override
    public void onClick(View v) {
        String tag = Utils.getViewTagAsString(v);
        onItemClick(v, tag, getLayoutPosition());
    }

    protected int getBindingModelVariableId() { return 0; }

    protected void onItemClick(View v, String tag, int position) {}

}
