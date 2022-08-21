package com.miui.gallery.ui.photoPage.bars.menuitem;

import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.view.menu.IMenuItem;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class AddCloud extends BaseMenuItemDelegate {
    public boolean isSupportAddSecret;
    public AddToAlbumListener mListener;

    public static AddCloud instance(IMenuItem iMenuItem) {
        return new AddCloud(iMenuItem);
    }

    public AddCloud(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    public void setIsSupportAddSecret(boolean z) {
        this.isSupportAddSecret = z;
    }

    public boolean getIsSupportAddSecret() {
        return this.isSupportAddSecret;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        TimeMonitor.createNewTimeMonitor("403.26.0.1.13762");
        BaseDataSet dataSet = this.mDataProvider.getFieldData().mCurrent.getDataSet();
        if (dataSet != null) {
            if (this.mListener == null) {
                this.mListener = new AddToAlbumListener(this);
            }
            dataSet.addToAlbum(this.mContext, this.mDataProvider.getFieldData().mCurrent.getPosition(), true, !this.mDataProvider.getFieldData().isStartWhenLockedAndSecret && this.isSupportAddSecret, this.mListener);
        }
        this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "add_to_album");
        TrackController.trackClick("403.11.5.1.11472", AutoTracking.getRef());
        TrackController.trackExpose("403.26.0.1.11240", AutoTracking.getRef());
    }

    /* loaded from: classes2.dex */
    public static class AddToAlbumListener implements MediaAndAlbumOperations.OnAddAlbumListener {
        public WeakReference<AddCloud> mAddCloud;

        public AddToAlbumListener(AddCloud addCloud) {
            this.mAddCloud = new WeakReference<>(addCloud);
        }

        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
        public void onComplete(long[] jArr, boolean z) {
            WeakReference<AddCloud> weakReference;
            if (!z || jArr == null || jArr[0] <= 0 || (weakReference = this.mAddCloud) == null || weakReference.get() == null) {
                return;
            }
            this.mAddCloud.get().mDataProvider.onContentChanged();
        }

        public void release() {
            WeakReference<AddCloud> weakReference = this.mAddCloud;
            if (weakReference != null) {
                weakReference.clear();
                this.mAddCloud = null;
            }
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        AddToAlbumListener addToAlbumListener = this.mListener;
        if (addToAlbumListener != null) {
            addToAlbumListener.release();
        }
    }
}
