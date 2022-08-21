package com.miui.gallery.ui.actionBar;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.tracing.Trace;
import com.miui.gallery.R;
import java.util.ArrayList;
import java.util.List;
import miui.gallery.support.actionbar.ActionBarCompat;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.internal.app.widget.ScrollingTabTextView;

/* loaded from: classes2.dex */
public class TabActionBarHelper {
    public boolean isImmerseEnable;
    public boolean isInChoiceMode;
    public boolean isLargeScreenAndWindow;
    public boolean isShowImmerse;
    public ActionBar mActionBar;
    public View mActionTabContainerView;
    public AppCompatActivity mContext;
    public int mCurrentPosition;
    public View mCustomEndView;
    public View mCustomStartView;
    public OnTabChangeListener mOnTabChangeListener;
    public float mScrollerProgress;
    public List<IAnimDrawable> mAnimNormalDrawables = new ArrayList(1);
    public List<IAnimDrawable> mAnimImmerseDrawables = new ArrayList(2);
    public SparseArray<TransitionDrawable> mNormalToImmerseDrawables = new SparseArray<>(4);

    /* loaded from: classes2.dex */
    public interface OnTabChangeListener {
        void onTabChange(int i);
    }

    public TabActionBarHelper(AppCompatActivity appCompatActivity) {
        this.mContext = appCompatActivity;
        this.mActionBar = appCompatActivity.getAppCompatActionBar();
    }

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        this.mOnTabChangeListener = onTabChangeListener;
    }

    public void inflateActionBar() {
        if (this.mActionBar == null) {
            return;
        }
        Trace.beginSection("inflateActionBar");
        try {
            this.mActionBar.setFragmentViewPagerMode(this.mContext);
            ActionBarCompat.setExpandState(this.mContext, 0);
        } finally {
            Trace.endSection();
        }
    }

    public void showNormalActionBar() {
        this.mContext.setTranslucentStatus(1);
        for (IAnimDrawable iAnimDrawable : this.mAnimImmerseDrawables) {
            iAnimDrawable.hideByAnimator();
        }
        for (IAnimDrawable iAnimDrawable2 : this.mAnimNormalDrawables) {
            iAnimDrawable2.showByAnimator();
        }
        for (int i = 0; i < this.mNormalToImmerseDrawables.size(); i++) {
            this.mNormalToImmerseDrawables.valueAt(i).reverseTransition(120);
        }
        this.isShowImmerse = false;
    }

    public void showImmerseActionBar() {
        if (!this.isInChoiceMode) {
            this.mActionBar.setBackgroundDrawable(null);
            this.mContext.setTranslucentStatus(2);
        }
        for (IAnimDrawable iAnimDrawable : this.mAnimNormalDrawables) {
            iAnimDrawable.hideByAnimator();
        }
        for (IAnimDrawable iAnimDrawable2 : this.mAnimImmerseDrawables) {
            iAnimDrawable2.showByAnimator();
        }
        for (int i = 0; i < this.mNormalToImmerseDrawables.size(); i++) {
            this.mNormalToImmerseDrawables.valueAt(i).startTransition(120);
        }
        this.isShowImmerse = true;
    }

    public void refreshTopBar(float f) {
        float f2 = this.mScrollerProgress;
        if (f2 > 0.25f && f <= 0.25f) {
            this.isImmerseEnable = true;
            if (this.isLargeScreenAndWindow && !this.isShowImmerse) {
                showImmerseActionBar();
                changeCustomTextColor();
            }
        } else if (f2 < 0.25f && f >= 0.25f) {
            if (this.isLargeScreenAndWindow && this.isShowImmerse) {
                showNormalActionBar();
                changeCustomTextColor();
            }
            this.isImmerseEnable = false;
        }
        this.mScrollerProgress = f;
    }

    public void setDefaultStyleActionBar() {
        for (IAnimDrawable iAnimDrawable : this.mAnimNormalDrawables) {
            iAnimDrawable.setVisible();
        }
        for (IAnimDrawable iAnimDrawable2 : this.mAnimImmerseDrawables) {
            iAnimDrawable2.setInvisible();
        }
    }

    public void addActionBarBg(Drawable drawable) {
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(drawable);
        }
    }

    public void removeActionBarBg() {
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(null);
        }
    }

    public void addActionBarTabContainerBg(IAnimDrawable iAnimDrawable) {
        View view = this.mActionTabContainerView;
        if (view == null || iAnimDrawable == null) {
            return;
        }
        view.setBackground(iAnimDrawable.getDrawable());
    }

    public void addImmerseAnimDrawable(IAnimDrawable iAnimDrawable) {
        this.mAnimImmerseDrawables.add(iAnimDrawable);
    }

    public void putNormalToImmerseDrawable(int i, TransitionDrawable transitionDrawable) {
        this.mNormalToImmerseDrawables.put(i, transitionDrawable);
    }

    public ViewGroup getActionTabContainerView() {
        View customView;
        ActionBar actionBar = this.mActionBar;
        if (actionBar == null || actionBar.getTabCount() == 0 || (customView = this.mActionBar.getTabAt(0).getCustomView()) == null) {
            return null;
        }
        ViewParent parent = customView.getParent();
        ViewParent parent2 = parent != null ? parent.getParent() : null;
        if (!(parent2 instanceof ViewGroup)) {
            return null;
        }
        return (ViewGroup) parent2;
    }

    public void setCurrentPosition(int i) {
        this.mCurrentPosition = i;
    }

    public TextView createTabCustomTextView(int i, int i2) {
        ScrollingTabTextView scrollingTabTextView = new ScrollingTabTextView(this.mContext, null, 16843509);
        scrollingTabTextView.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        scrollingTabTextView.setTextSize(0, this.mContext.getResources().getDimensionPixelSize(R.dimen.action_bar_tab_text_size));
        layoutParams.gravity = 16;
        if (i == 0) {
            layoutParams.setMarginStart(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_first_tab_text_margin_start));
            layoutParams.setMarginEnd(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_first_tab_text_margin_end));
            scrollingTabTextView.setTextColor(this.mContext.getColor(R.color.action_bar_immerse_tab_text_color_pressed));
        } else if (i == 2) {
            layoutParams.setMarginStart(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_last_tab_text_margin_start));
            layoutParams.setMarginEnd(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_last_tab_text_margin_end));
            scrollingTabTextView.setTextColor(this.mContext.getColor(R.color.action_bar_tab_text_color_normal));
        } else {
            layoutParams.setMarginStart(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_tab_text_margin_start));
            layoutParams.setMarginEnd(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_tab_text_margin_end));
            scrollingTabTextView.setTextColor(this.mContext.getColor(R.color.action_bar_tab_text_color_normal));
        }
        scrollingTabTextView.setLayoutParams(layoutParams);
        scrollingTabTextView.setText(i2);
        return scrollingTabTextView;
    }

    public void changeCustomTextColor() {
        ActionBar actionBar = this.mActionBar;
        if (actionBar == null || actionBar.getTabCount() == 0) {
            return;
        }
        for (int i = 0; i < this.mActionBar.getTabCount(); i++) {
            ActionBar.Tab tabAt = this.mActionBar.getTabAt(i);
            if (tabAt.getCustomView() != null && (tabAt.getCustomView() instanceof TextView)) {
                TextView textView = (TextView) tabAt.getCustomView();
                if (i == this.mCurrentPosition) {
                    textView.setTextColor(this.mContext.getColor(R.color.action_bar_immerse_tab_text_color_pressed));
                } else if (this.isLargeScreenAndWindow && this.isShowImmerse) {
                    textView.setTextColor(this.mContext.getColor(R.color.action_bar_immerse_tab_text_color_normal));
                } else {
                    textView.setTextColor(this.mContext.getColor(R.color.action_bar_tab_text_color_normal));
                }
            }
        }
    }

    public void refreshTabCustomViewMargin() {
        miuix.appcompat.app.ActionBar actionBar = this.mActionBar;
        if (actionBar == null || actionBar.getTabCount() == 0) {
            return;
        }
        for (int i = 0; i < this.mActionBar.getTabCount(); i++) {
            ActionBar.Tab tabAt = this.mActionBar.getTabAt(i);
            if (tabAt.getCustomView() != null && (tabAt.getCustomView() instanceof TextView)) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ((TextView) tabAt.getCustomView()).getLayoutParams();
                if (i == 0) {
                    layoutParams.setMarginStart(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_first_tab_text_margin_start));
                    layoutParams.setMarginEnd(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_tab_text_margin_end));
                } else if (i == this.mActionBar.getTabCount() - 1) {
                    layoutParams.setMarginStart(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_tab_text_margin_start));
                    layoutParams.setMarginEnd(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_last_tab_text_margin_end));
                } else {
                    layoutParams.setMarginStart(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_tab_text_margin_start));
                    layoutParams.setMarginEnd(this.mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_tab_text_margin_end));
                }
            }
        }
    }

    public int getFragmentTabCount() {
        return this.mActionBar.getFragmentTabCount();
    }

    public Fragment getFragmentAt(int i) {
        return this.mActionBar.getFragmentAt(i);
    }

    public int getActionBarHeight() {
        return this.mActionBar.getHeight();
    }

    public boolean isShowImmerse() {
        return this.isShowImmerse;
    }

    public int getCurrentPosition() {
        return this.mCurrentPosition;
    }

    public void setInChoiceMode(boolean z) {
        this.isInChoiceMode = z;
    }
}
