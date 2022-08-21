package com.miui.gallery.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.BaseMiscUtil;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes2.dex */
public class ActionBarWrapper extends ActionBar {
    public ActionBar mWrapped;

    @Override // miuix.appcompat.app.ActionBar
    public View getEndView() {
        return this.mWrapped.getEndView();
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setResizable(boolean z) {
        this.mWrapped.setResizable(z);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setExpandState(int i) {
        this.mWrapped.setExpandState(i);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setTabsMode(boolean z) {
        this.mWrapped.setTabsMode(z);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setFragmentViewPagerMode(FragmentActivity fragmentActivity) {
        this.mWrapped.setFragmentViewPagerMode(fragmentActivity);
    }

    @Override // miuix.appcompat.app.ActionBar
    public int getFragmentTabCount() {
        return this.mWrapped.getFragmentTabCount();
    }

    @Override // miuix.appcompat.app.ActionBar
    public Fragment getFragmentAt(int i) {
        return this.mWrapped.getFragmentAt(i);
    }

    @Override // miuix.appcompat.app.ActionBar
    public int addFragmentTab(String str, ActionBar.Tab tab, Class<? extends Fragment> cls, Bundle bundle, boolean z) {
        return this.mWrapped.addFragmentTab(str, tab, cls, bundle, z);
    }

    @Override // miuix.appcompat.app.ActionBar
    public int addFragmentTab(String str, ActionBar.Tab tab, int i, Class<? extends Fragment> cls, Bundle bundle, boolean z) {
        return this.mWrapped.addFragmentTab(str, tab, i, cls, bundle, z);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void removeFragmentTabAt(int i) {
        this.mWrapped.removeFragmentTabAt(i);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void removeAllFragmentTab() {
        this.mWrapped.removeAllFragmentTab();
    }

    @Override // miuix.appcompat.app.ActionBar
    public void addOnFragmentViewPagerChangeListener(ActionBar.FragmentViewPagerChangeListener fragmentViewPagerChangeListener) {
        this.mWrapped.addOnFragmentViewPagerChangeListener(fragmentViewPagerChangeListener);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void removeOnFragmentViewPagerChangeListener(ActionBar.FragmentViewPagerChangeListener fragmentViewPagerChangeListener) {
        this.mWrapped.removeOnFragmentViewPagerChangeListener(fragmentViewPagerChangeListener);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setStartView(View view) {
        this.mWrapped.setStartView(view);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setEndView(View view) {
        this.mWrapped.setEndView(view);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setCustomView(View view) {
        this.mWrapped.setCustomView(view);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        this.mWrapped.setCustomView(view, layoutParams);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setCustomView(int i) {
        this.mWrapped.setCustomView(i);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setTitle(CharSequence charSequence) {
        this.mWrapped.setTitle(charSequence);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setTitle(int i) {
        this.mWrapped.setTitle(i);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayOptions(int i) {
        this.mWrapped.setDisplayOptions(i);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayOptions(int i, int i2) {
        this.mWrapped.setDisplayOptions(i, i2);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayShowHomeEnabled(boolean z) {
        this.mWrapped.setDisplayShowHomeEnabled(z);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayShowTitleEnabled(boolean z) {
        this.mWrapped.setDisplayShowTitleEnabled(z);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayShowCustomEnabled(boolean z) {
        this.mWrapped.setDisplayShowCustomEnabled(z);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setBackgroundDrawable(Drawable drawable) {
        this.mWrapped.setBackgroundDrawable(drawable);
    }

    @Override // androidx.appcompat.app.ActionBar
    public View getCustomView() {
        return this.mWrapped.getCustomView();
    }

    @Override // androidx.appcompat.app.ActionBar
    public int getDisplayOptions() {
        return this.mWrapped.getDisplayOptions();
    }

    @Override // androidx.appcompat.app.ActionBar
    public ActionBar.Tab newTab() {
        return this.mWrapped.newTab();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void selectTab(ActionBar.Tab tab) {
        this.mWrapped.selectTab(tab);
    }

    @Override // androidx.appcompat.app.ActionBar
    public ActionBar.Tab getTabAt(int i) {
        return this.mWrapped.getTabAt(i);
    }

    @Override // androidx.appcompat.app.ActionBar
    public int getTabCount() {
        return this.mWrapped.getTabCount();
    }

    @Override // androidx.appcompat.app.ActionBar
    public int getHeight() {
        return this.mWrapped.getHeight();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void show() {
        this.mWrapped.show();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void hide() {
        this.mWrapped.hide();
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean isShowing() {
        return this.mWrapped.isShowing();
    }

    @Override // miuix.appcompat.app.ActionBar
    public void selectTab(ActionBar.Tab tab, boolean z) {
        this.mWrapped.selectTab(tab, z);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setShowHideAnimationEnabled(boolean z) {
        BaseMiscUtil.invokeSafely(this.mWrapped, "setShowHideAnimationEnabled", new Class[]{Boolean.TYPE}, Boolean.valueOf(z));
    }
}
