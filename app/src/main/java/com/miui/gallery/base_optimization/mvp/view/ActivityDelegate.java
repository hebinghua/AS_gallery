package com.miui.gallery.base_optimization.mvp.view;

import android.os.Bundle;
import com.miui.gallery.base_optimization.R$layout;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.base_optimization.util.GenericUtils;

/* loaded from: classes.dex */
public class ActivityDelegate<P extends IPresenter> implements IView<P> {
    public boolean isInflate;
    public Activity mActivity;
    public P mPresenter;

    public ActivityDelegate(Activity activity) {
        this.mActivity = activity;
    }

    public void onCreate(Bundle bundle) {
        if (!this.isInflate) {
            if (-1 == this.mActivity.getLayoutId()) {
                this.mActivity.setContentView(R$layout.base_activity_layout);
            } else {
                Activity activity = this.mActivity;
                activity.setContentView(activity.getLayoutId());
            }
            this.isInflate = true;
        }
        if (this.mPresenter == null) {
            P p = (P) GenericUtils.getSuperClassT(this.mActivity, 0);
            this.mPresenter = p;
            if (p == null) {
                return;
            }
            p.onAttachView(this.mActivity);
        }
    }
}
