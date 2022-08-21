package com.miui.gallery.magic.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.miui.gallery.magic.base.BasePresenter;

/* loaded from: classes2.dex */
public abstract class BaseFragment<P extends BasePresenter, CONTRACT> extends Fragment implements View.OnClickListener {
    public final int FAST_CLICK_DELAY_TIME = 1000;
    public long lastClickTime;
    public P mPresenter;
    public View rootView;

    public abstract int getLayoutId();

    public abstract P getPresenterInstance();

    /* renamed from: initContract */
    public abstract CONTRACT mo1066initContract();

    public abstract void initData();

    public abstract void initView();

    public boolean isFastClick() {
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = currentTimeMillis - this.lastClickTime < 1000;
        this.lastClickTime = currentTimeMillis;
        return z;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View inflate = layoutInflater.inflate(getLayoutId(), viewGroup, false);
        this.rootView = inflate;
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        P presenterInstance = getPresenterInstance();
        this.mPresenter = presenterInstance;
        if (presenterInstance != null) {
            presenterInstance.bindView(this);
        }
        initView();
        initData();
    }

    public View findViewById(int i) {
        return this.rootView.findViewById(i);
    }

    public String getStringById(int i) {
        return getActivity().getResources().getString(i);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        P p = this.mPresenter;
        if (p != null) {
            p.result(i, i2, intent);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        P p = this.mPresenter;
        if (p != null) {
            p.unBindView();
        }
    }

    public CONTRACT getContract() {
        return mo1066initContract();
    }

    public BaseFragmentActivity getBaseActivity() {
        return (BaseFragmentActivity) getActivity();
    }
}
