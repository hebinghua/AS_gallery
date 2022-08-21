package com.miui.gallery.ui.photoPage.bars.manager;

import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;

/* loaded from: classes2.dex */
public interface IManagerOwner {
    PhotoPageAdapter getAdapter();

    BaseActivity getBaseActivity();

    IDataProvider getDataProvider();

    GalleryFragment getFragment();
}
