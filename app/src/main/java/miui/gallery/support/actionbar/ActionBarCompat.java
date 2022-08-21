package miui.gallery.support.actionbar;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;
import miuix.springback.view.SpringBackLayout;
import miuix.viewpager.widget.ViewPager;

/* loaded from: classes3.dex */
public class ActionBarCompat {
    public static void setResizable(AppCompatActivity appCompatActivity, boolean z) {
        ActionBar appCompatActionBar = appCompatActivity.getAppCompatActionBar();
        if (appCompatActionBar == null) {
            return;
        }
        appCompatActionBar.setResizable(z);
    }

    public static void setExpandState(AppCompatActivity appCompatActivity, int i) {
        ActionBar appCompatActionBar = appCompatActivity.getAppCompatActionBar();
        if (appCompatActionBar == null) {
            return;
        }
        appCompatActionBar.setExpandState(i);
    }

    public static View setCustomStartViewOnly(AppCompatActivity appCompatActivity, int i) {
        ActionBar appCompatActionBar = appCompatActivity.getAppCompatActionBar();
        if (appCompatActionBar == null) {
            return null;
        }
        View inflate = appCompatActivity.getLayoutInflater().inflate(i, (ViewGroup) null);
        appCompatActionBar.setStartView(inflate);
        return inflate;
    }

    public static View setCustomEndViewOnly(AppCompatActivity appCompatActivity, int i) {
        ActionBar appCompatActionBar = appCompatActivity.getAppCompatActionBar();
        if (appCompatActionBar == null) {
            return null;
        }
        View inflate = appCompatActivity.getLayoutInflater().inflate(i, (ViewGroup) null);
        appCompatActionBar.setEndView(inflate);
        return inflate;
    }

    public static void enableViewPagerSpring(ViewPager viewPager, boolean z) {
        for (ViewParent parent = viewPager.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof SpringBackLayout) {
                ((SpringBackLayout) parent).setEnabled(z);
            }
        }
    }
}
