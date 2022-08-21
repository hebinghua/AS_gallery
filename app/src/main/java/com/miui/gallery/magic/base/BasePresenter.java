package com.miui.gallery.magic.base;

import android.content.Intent;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.base.BaseModel;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public abstract class BasePresenter<V extends BaseFragment, M extends BaseModel, CONTRACT> extends SuperBase<CONTRACT> {
    public M mModel = getModelInstance();
    public WeakReference<V> mView;

    public abstract M getModelInstance();

    public abstract void result(int i, int i2, Intent intent);

    public void bindView(V v) {
        this.mView = new WeakReference<>(v);
    }

    public void unBindView() {
        synchronized (this) {
            this.mView = null;
        }
    }

    public BaseFragmentActivity getActivity() {
        WeakReference<V> weakReference = this.mView;
        if (weakReference == null) {
            return null;
        }
        return (BaseFragmentActivity) weakReference.get().getActivity();
    }

    public BaseFragmentActivity getActivityWithSync() {
        V v;
        synchronized (this) {
            WeakReference<V> weakReference = this.mView;
            v = weakReference == null ? null : weakReference.get();
        }
        if (v != null) {
            return (BaseFragmentActivity) v.getActivity();
        }
        return null;
    }

    public V getRealV() {
        V v = null;
        if (this.mView != null) {
            synchronized (this) {
                WeakReference<V> weakReference = this.mView;
                if (weakReference != null) {
                    v = weakReference.get();
                }
            }
        }
        return v;
    }
}
