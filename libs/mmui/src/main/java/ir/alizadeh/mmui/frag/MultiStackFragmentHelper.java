package ir.alizadeh.mmui.frag;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;


public abstract class MultiStackFragmentHelper<ACTIVITY extends AppCompatActivity> implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {
    private static final String TAG = "FrHelper";

    protected WeakReference<ACTIVITY> activityRef;
    private Map<Integer, Stack<String>> tagStacks;
    private Stack<Integer> menuStacks;
    private List<Integer> stackList;
    private int tabCount;
    private int currentTab;
    private Fragment currentFragment;

    private Listener listener;
    private int transactionAnimEnter = 0, transactionAnimExit = 0;

    public MultiStackFragmentHelper(ACTIVITY activity) {
        this.activityRef = new WeakReference<>(activity);
        this.tabCount = Math.max(getTabsCount(), 1);
        init();
    }

    public MultiStackFragmentHelper<ACTIVITY> withListener(Listener listener) {
        this.listener = listener;
        return this;
    }


    private void init() {
        menuStacks = new Stack<>();
        menuStacks.push(0);

        tagStacks = new LinkedHashMap<>();
        for (int index = 0; index < tabCount; index++) {
            tagStacks.put(index, new Stack<>());
        }

        BottomNavigationView bottomNavigationView = uiBottomNavigationView();
        if(bottomNavigationView != null) {
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
            bottomNavigationView.setOnNavigationItemReselectedListener(this);
            bottomNavigationView.setSelectedItemId(getTabId(0));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        for(int index = 0 ; index < tabCount ; index++) {
            int tabId = getTabId(index);
            if(itemId == tabId) {
                setCurrentTab(index);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
        popStackExceptFirst();
    }

    private Stack<String> getTagStack(int tabIndex) {
        Stack<String> stack = tagStacks.get(tabIndex);
        if(stack == null) {
            stack = new Stack<>();
            tagStacks.put(tabIndex, stack);
        }
        return stack;
    }

    private void showHideTabFragment(FragmentManager fm, Fragment fragmentToShow, Fragment fragmentToHide) {
        Log.d(TAG, "showHideTabFragment: (" + (fragmentToHide==null ? "null" : fragmentToHide.getTag()) +
                ") -> (" + (fragmentToShow==null? "null" : fragmentToShow.getTag()) +")");
        if(fragmentToHide != fragmentToShow) {
            FragmentTransaction ft = ft(fm);
            if(fragmentToHide != null) {
                ft.hide(fragmentToHide);
            }
            if(fragmentToShow != null){
                ft.show(fragmentToShow);
            }
            ft.commitAllowingStateLoss();
        }
    }

    private void removeFragment(FragmentManager fm, Fragment fragmentToShow, Fragment fragmentToRemove) {
        Log.d(TAG, "removeFragment: (" + (fragmentToRemove==null ? "null" : fragmentToRemove.getTag()) +
                ") -> (" + (fragmentToShow==null? "null" : fragmentToShow.getTag()) +")");
        if(fragmentToRemove != fragmentToShow) {
            FragmentTransaction ft = ft(fm);
            if(fragmentToRemove != null) {
                ft.remove(fragmentToRemove);
            }
            if(fragmentToShow != null) {
                ft.show(fragmentToShow);
            }
            ft.commitAllowingStateLoss();
        }
    }

    protected void assignCurrentFragment(Fragment fragment) {
        this.currentFragment = fragment;
        onFragmentAssigned(fragment);
    }

    private void popFragment() {
        Stack<String> stack = getTagStack(currentTab);
        String tag = stack.elementAt(stack.size() - 2);
        FragmentManager fm = fm();
        Fragment fragment = fm.findFragmentByTag(tag);


        if(fragment != null) {
            removeFragment(fm, fragment, currentFragment);
            assignCurrentFragment(fragment);
        }

        /* pop current fragment from stack */
        stack.pop();
    }

    /*Pops the last fragment inside particular tab and goes to the second tab in the stack*/
    private void popAndNavigateToPreviousMenu() {
        int tempCurrent = menuStacks.pop();
        int nextTab = menuStacks.peek();

        Log.d(TAG, "popAndNavigateToPreviousMenu: from " + tempCurrent + " -> " + nextTab);

        Stack<String> stack = getTagStack(nextTab);
        FragmentManager fm = fm();
        Fragment targetFragment = !stack.isEmpty() ? ( fm.findFragmentByTag(stack.lastElement()) ) : null ;

        if(targetFragment != null) {
            if(getTagStack(tempCurrent).size() > 1) {
                getTagStack(tempCurrent).pop();
            }
            if(recreateBaseFragmentOnReselect(tempCurrent)) {
                removeFragment(fm, targetFragment, currentFragment);
            } else {
                showHideTabFragment(fm, targetFragment, currentFragment);
            }
            assignCurrentFragment(targetFragment);
            this.currentTab = nextTab;
            uiSelectTab(nextTab);
        }
    }


    private void popStackExceptFirst() {
        Stack<String> stack = getTagStack(currentTab);
        if(stack.empty()) {
            Log.d(TAG, "popStackExceptFirst: stack is empty -> so add fragment");
            addTabFragment(getBaseFragment(currentTab), getBaseFragmentTag(currentTab));
        } else if(stack.size() > 1) {
            FragmentManager fm = fm();
            while(stack.size() > 1) {
                String tag = stack.pop();
                Fragment fragment = fm.findFragmentByTag(tag);
                if(fragment != null) {
                    Log.d(TAG, "popStackExceptFirst: removing fragment " + fragment.getTag());
                    fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
                }
            }
            Fragment fragment = fm.findFragmentByTag(stack.peek());
            if(fragment != currentFragment) {
                removeFragment(fm, fragment, currentFragment);
                assignCurrentFragment(fragment);
            }
        }
    }

    public void reselectCurrentTab() {
        popStackExceptFirst();
    }

    private FragmentManager fm() {
        return activityRef.get().getSupportFragmentManager();
    }

    private FragmentTransaction ft(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        if(transactionAnimEnter > 0 || transactionAnimExit > 0) {
            ft.setCustomAnimations(transactionAnimEnter, transactionAnimExit);
        }
        return ft;
    }

    private FragmentTransaction ft() {
        FragmentTransaction ft = activityRef.get().getSupportFragmentManager().beginTransaction();
        if(transactionAnimEnter > 0 || transactionAnimExit > 0) {
            ft.setCustomAnimations(transactionAnimEnter, transactionAnimExit);
        }
        return ft;
    }

    public void setFragmentTransactionAnimations(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        this.transactionAnimEnter = enterAnim;
        this.transactionAnimExit = exitAnim;
    }

    public void setCurrentTab(int tabIndex) {
        this.currentTab = tabIndex;
        Stack<String> stack = getTagStack(currentTab);
        if(stack.size() == 0) {
            Fragment fragment = getBaseFragment(tabIndex);
            addTabFragment(tabIndex, fragment, getBaseFragmentTag(tabIndex));
            assignCurrentFragment(fragment);
        } else {
            Fragment targetFragment = fm().findFragmentByTag(stack.peek());
            if(targetFragment != null) {
                showHideTabFragment(fm(), targetFragment, currentFragment);
            }
            assignCurrentFragment(targetFragment);
        }
        updateMenuStacks(tabIndex);

        if(listener != null) {
            listener.fragmentHelper_onTabSelected(tabIndex);
        }
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public boolean isTabVisible(int tabIndex) {
        return currentTab == tabIndex && !getTagStack(tabIndex).isEmpty();
    }

    private void updateMenuStacks(int tabIndex) {
        if(tabIndex == 0) {
            while(menuStacks.size() > 0) {
                menuStacks.pop();
            }
        }
        if(menuStacks.empty() || menuStacks.peek() != tabIndex) {
            menuStacks.push(tabIndex);
        }
    }

    private void uiSelectTab(int tabIndex) {
        BottomNavigationView bottomNavigationView = uiBottomNavigationView();
        if(bottomNavigationView != null) {
            int tabId = getTabId(tabIndex);
            bottomNavigationView.setOnNavigationItemSelectedListener(null);
            bottomNavigationView.setOnNavigationItemReselectedListener(null);
            bottomNavigationView.setSelectedItemId(tabId);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
            bottomNavigationView.setOnNavigationItemReselectedListener(this);
        }
    }

    public void addTabFragment(Fragment fragment) {
        addTabFragment(fragment, null);
    }

    public void addTabFragment(Fragment fragment, String tag) {
        this.addTabFragment(currentTab, fragment, tag);
    }

    public void addTabFragment(int tabIndex, Fragment fragment, String tag) {
        if(TextUtils.isEmpty(tag)) {
            int cnt = getTagStack(tabIndex).size();
            tag = String.format(Locale.US, "Tab%dF%d", tabIndex, cnt);
        }

        Log.d(TAG, "addTabFragment: " + tabIndex + " | " + tag);
        FragmentManager fm = fm();
        FragmentTransaction ft = ft(fm);
        if(currentFragment != null) {
            ft.hide(currentFragment);
        }
        if(fragment != null) {
            ft.add(getContainerId(tabIndex), fragment, tag);
            ft.show(fragment);
            Stack<String> stack = getTagStack(tabIndex);
            stack.push(tag);
            assignCurrentFragment(fragment);
        }
        ft.commitAllowingStateLoss();
    }

    public boolean resolveBackPressed() {
        Stack<String> currentStack = getTagStack(currentTab);
        if(currentStack.empty()) return false;
        if(currentStack.size() > 1) {
            popFragment();
            return true;
        } else {
            if(menuStacks.size() > 1) {
                popAndNavigateToPreviousMenu();
                return true;
            } else {
                return false;
            }
        }
    }

    protected boolean recreateBaseFragmentOnReselect(int tabIndex) {
        return false;
    }

    @NonNull
    protected abstract Fragment getBaseFragment(int tabIndex);
    @NonNull
    protected abstract String getBaseFragmentTag(int tabIndex);
    protected abstract int getTabsCount();
    protected abstract int getTabId(int index);
    @IdRes
    protected abstract int getContainerId(int index);
    protected abstract BottomNavigationView uiBottomNavigationView();
    protected void onFragmentAssigned(Fragment fragment){}

    public interface Listener {
        void fragmentHelper_onTabSelected(int tabIndex);
    }

}
