package com.miui.gallery.hybrid.pulltorefresh;

import android.graphics.drawable.Drawable;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class LoadingLayoutProxy implements ILoadingLayout {
    public final HashSet<LoadingLayout> mLoadingLayouts = new HashSet<>();

    public void addLayout(LoadingLayout loadingLayout) {
        if (loadingLayout != null) {
            this.mLoadingLayouts.add(loadingLayout);
        }
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public void setLastUpdatedLabel(CharSequence charSequence) {
        Iterator<LoadingLayout> it = this.mLoadingLayouts.iterator();
        while (it.hasNext()) {
            it.next().setLastUpdatedLabel(charSequence);
        }
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public void setLoadingDrawable(Drawable drawable) {
        Iterator<LoadingLayout> it = this.mLoadingLayouts.iterator();
        while (it.hasNext()) {
            it.next().setLoadingDrawable(drawable);
        }
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public void setRefreshingLabel(CharSequence charSequence) {
        Iterator<LoadingLayout> it = this.mLoadingLayouts.iterator();
        while (it.hasNext()) {
            it.next().setRefreshingLabel(charSequence);
        }
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public void setPullLabel(CharSequence charSequence) {
        Iterator<LoadingLayout> it = this.mLoadingLayouts.iterator();
        while (it.hasNext()) {
            it.next().setPullLabel(charSequence);
        }
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.ILoadingLayout
    public void setReleaseLabel(CharSequence charSequence) {
        Iterator<LoadingLayout> it = this.mLoadingLayouts.iterator();
        while (it.hasNext()) {
            it.next().setReleaseLabel(charSequence);
        }
    }
}
