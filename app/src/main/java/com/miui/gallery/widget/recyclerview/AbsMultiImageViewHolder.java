package com.miui.gallery.widget.recyclerview;

import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.ui.pictures.view.IMultiImageView;

/* loaded from: classes3.dex */
public abstract class AbsMultiImageViewHolder extends AbsViewHolder {
    public boolean mIsPaused;
    public final IMultiImageView mView;

    public AbsMultiImageViewHolder(View view, Lifecycle lifecycle) {
        super(view, lifecycle);
        this.mView = (IMultiImageView) view;
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onResume(LifecycleOwner lifecycleOwner) {
        if (this.mIsPaused) {
            this.mView.optionalInvalidateAfterResumed();
            this.mIsPaused = false;
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onPause(LifecycleOwner lifecycleOwner) {
        this.mIsPaused = true;
    }

    @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
    public void recycle() {
        super.recycle();
        this.mView.recycle();
    }
}
