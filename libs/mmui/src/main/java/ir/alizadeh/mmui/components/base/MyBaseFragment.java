package ir.alizadeh.mmui.components.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.util.List;

import ir.alizadeh.mmui.cntrl.MyLocaleManager;
import ir.alizadeh.mmui.utils.Utils;

public class MyBaseFragment extends Fragment implements IFrBackPressed, IFrLabel {

    public IPageController ipageController;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        MyLocaleManager.update(getContext());
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();

        if(parentFragment instanceof MyBaseFragment) {
            ipageController = ((MyBaseFragment) parentFragment).ipageController;
        } else if(context instanceof IPageController) {
            ipageController = (IPageController) context;
        } else {
            ipageController = null;
        }
        if(context instanceof IScreenLabelPresenter) {
            ((IScreenLabelPresenter) context).updateScreenLabel(getScreenLabel());
        }
    }

    @Override
    public boolean onBackPressed() {
        if(Utils.hideInputMethod(getView()))
            return true;

        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
        for(Fragment f : fragmentList) {
            if(f instanceof IFrBackPressed) {
                boolean handled = ((IFrBackPressed)f).onBackPressed();
                if(handled) {
                    return true;
                }
            }
        }
        return false;
    }

    @StringRes
    public int getScreenLabel() {
        return 0;
    }
}
