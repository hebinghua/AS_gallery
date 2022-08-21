package com.miui.gallery.adapter;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.util.photoview.ItemViewInfo;

/* loaded from: classes.dex */
public class PhotoPreviewAdapter extends PhotoPageAdapter {
    @Override // com.miui.gallery.adapter.PhotoPageAdapter
    public boolean isNeedPostInstantiateItemTask() {
        return false;
    }

    public PhotoPreviewAdapter(FragmentActivity fragmentActivity, int i, ImageLoadParams imageLoadParams, PhotoPageAdapter.ViewProvider viewProvider, ItemViewInfo itemViewInfo, PhotoPageAdapter.OnPreviewedListener onPreviewedListener, PhotoPageAdapter.PhotoPageInteractionListener photoPageInteractionListener) {
        super(fragmentActivity, i, imageLoadParams, viewProvider, itemViewInfo, onPreviewedListener, photoPageInteractionListener);
    }
}
