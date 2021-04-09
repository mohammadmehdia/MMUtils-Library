package ir.alizadeh.mmui.components.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.alizadeh.mmui.R;
import ir.alizadeh.mmui.cntrl.MyLocaleManager;

@SuppressLint("SourceLockedOrientationActivity")

public abstract class MyBaseActivity extends AppCompatActivity implements IScreenLabelPresenter  {
    public static final String KEY_PAGE_TITLE = "page_title";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MyLocaleManager.update(this);
        super.onCreate(savedInstanceState);
        checkDarkTheme();

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            if(forceScreenOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                setRequestedOrientation(forceScreenOrientation());
            }
        }
    }

    @StyleRes
    protected int darkThemeStyleResId() {
        return 0;
    }

    protected void checkDarkTheme()  {
        try {

            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                int darkThemeResId = darkThemeStyleResId();
                if(darkThemeResId != 0) {
                    setTheme(darkThemeResId);
                }
            }
        } catch (Exception ignored){}
    }

    protected int forceScreenOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context base = ViewPumpContextWrapper.wrap(newBase);
        super.attachBaseContext(base);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void changeStatusBarColor(@ColorInt int color) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    public void changeStatusBarWithColorResId(@ColorRes int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);
        changeStatusBarColor(color);
    }

    public void transparentStatusBarFullScreen(){
        transparentStatusBar(true);
    }

    public void transparentStatusBar(boolean fullscreen){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(fullscreen){
                flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            }
            window.getDecorView().setSystemUiVisibility(flags);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected boolean fragmentsOnBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
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

    @Override
    public void onBackPressed() {
        if(fragmentsOnBackPressed() || popFragmentBackStack()) {
            return;
        }
        super.onBackPressed();
    }


    /*********************************
     Fragment Utils
     **********************************/

    protected FragmentTransaction ft() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);
        return transaction;
    }

    protected FragmentTransaction ft(int animIn, int animOut, int popEnter, int popExit) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut, popEnter, popExit);
        return transaction;
    }

    protected FragmentTransaction ft(int animIn, int animOut) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut);
        return transaction;
    }

    protected void animatedReplaceFragment(Fragment fragment, int containerId, boolean addToBackStack) {
        FragmentTransaction transaction = ft();
        if(addToBackStack){
            int cnt = getSupportFragmentManager().getBackStackEntryCount();
            transaction.addToBackStack(String.valueOf(cnt + 1));
        }
        transaction.replace(containerId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public boolean popFragmentBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
//            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
//            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            manager.popBackStack();
            return true;
        }
        return false;
    }

    @Override
    public void updateScreenLabel(@StringRes int  labelResId) { }

}
