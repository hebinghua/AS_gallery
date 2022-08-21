package miuix.appcompat.app;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;

/* loaded from: classes3.dex */
public abstract class ActionBar extends androidx.appcompat.app.ActionBar {

    /* loaded from: classes3.dex */
    public interface FragmentViewPagerChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, boolean z, boolean z2);

        void onPageSelected(int i);
    }

    /* loaded from: classes3.dex */
    public interface OnScrollListener {
        boolean onContentScrolled();

        void onFling(float f, int i);

        void onScroll(int i, float f);

        void onStartScroll();

        void onStopScroll();
    }

    public abstract int addFragmentTab(String str, ActionBar.Tab tab, int i, Class<? extends androidx.fragment.app.Fragment> cls, Bundle bundle, boolean z);

    public abstract int addFragmentTab(String str, ActionBar.Tab tab, Class<? extends androidx.fragment.app.Fragment> cls, Bundle bundle, boolean z);

    public abstract void addOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener fragmentViewPagerChangeListener);

    public abstract View getEndView();

    public abstract androidx.fragment.app.Fragment getFragmentAt(int i);

    public abstract int getFragmentTabCount();

    public abstract void removeAllFragmentTab();

    public abstract void removeFragmentTabAt(int i);

    public abstract void removeOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener fragmentViewPagerChangeListener);

    public abstract void selectTab(ActionBar.Tab tab, boolean z);

    public abstract void setEndView(View view);

    public abstract void setExpandState(int i);

    public abstract void setFragmentViewPagerMode(FragmentActivity fragmentActivity);

    public abstract void setResizable(boolean z);

    public abstract void setStartView(View view);

    public abstract void setTabsMode(boolean z);
}
