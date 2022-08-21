package com.miui.gallery.collage.app.common;

import android.os.Bundle;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.collage.app.common.CollageRenderFragment;
import com.miui.gallery.collage.core.CollagePresenter;

/* loaded from: classes.dex */
public abstract class CollageMenuFragment<P extends CollagePresenter, R extends CollageRenderFragment> extends AndroidFragment {
    public P mPresenter;
    public R mRenderFragment;

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void setPresenter(P p) {
        this.mPresenter = p;
    }

    public R getRenderFragment() {
        return this.mRenderFragment;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setRenderFragment(CollageRenderFragment collageRenderFragment) {
        this.mRenderFragment = collageRenderFragment;
    }
}
