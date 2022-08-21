package com.miui.gallery.base_optimization.mvp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;

/* loaded from: classes.dex */
public abstract class Fragment<P extends IPresenter> extends GalleryFragment implements IView<P> {
    public FragmentDelegate<P> mFragmentDelegate;

    public abstract int getLayoutId();

    public int getThemeRes() {
        return 16974383;
    }

    public abstract void initView(View view, Bundle bundle, View view2);

    public void onFragmentViewVisible(boolean z) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        FragmentDelegate<P> fragmentDelegate = new FragmentDelegate<>(this);
        this.mFragmentDelegate = fragmentDelegate;
        fragmentDelegate.onAttach(activity);
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFragmentDelegate.onCreate(bundle);
    }

    public String getPageName() {
        return this.mFragmentDelegate.getPageName();
    }

    public FragmentActivity getSafeActivity() {
        return this.mFragmentDelegate.getSafeActivity();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.mFragmentDelegate.onResume();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.mFragmentDelegate.onPause();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.mFragmentDelegate.onStop();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mFragmentDelegate.onDetach();
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.mFragmentDelegate.onInflateView(layoutInflater, viewGroup, bundle);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public View getView() {
        return this.mFragmentDelegate.getView();
    }

    @Override // androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (this.mFragmentDelegate == null) {
            this.mFragmentDelegate = new FragmentDelegate<>(this);
        }
        this.mFragmentDelegate.setUserVisibleHint(z);
    }

    public P getPresenter() {
        FragmentDelegate<P> fragmentDelegate = this.mFragmentDelegate;
        if (fragmentDelegate == null) {
            return null;
        }
        return fragmentDelegate.getPresenter();
    }

    public <V extends View> V findViewById(int i) {
        return (V) this.mFragmentDelegate.findViewById(i);
    }
}
