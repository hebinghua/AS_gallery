package com.miui.gallery.ui.photoPage.bars.menuitem;

import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.util.SoundUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class Delete extends BaseMenuItemDelegate {
    public boolean isClickable;

    public static /* synthetic */ void $r8$lambda$0ezOuoBFUKr8SEiki7L1Uv_TXOo(Delete delete) {
        delete.lambda$onClick$1();
    }

    public static /* synthetic */ void $r8$lambda$l71qAE0wpSSzCZbSt6oyMYbPrTw(Delete delete, BaseDataItem baseDataItem, int i, long[] jArr) {
        delete.lambda$onClick$0(baseDataItem, i, jArr);
    }

    public static Delete instance(IMenuItem iMenuItem) {
        return new Delete(iMenuItem);
    }

    public Delete(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate
    public void doInitFunction() {
        this.isClickable = true;
        super.doInitFunction();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(final BaseDataItem baseDataItem) {
        BaseDataSet dataSet;
        if (!this.isClickable || baseDataItem == null || this.mContext == null || (dataSet = this.mDataProvider.getFieldData().mCurrent.getDataSet()) == null) {
            return;
        }
        this.isClickable = false;
        int position = this.mDataProvider.getFieldData().mCurrent.getPosition();
        DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Delete", "delete => %d", Integer.valueOf(position));
        baseDataItem.getOriginalPath();
        dataSet.delete(this.mContext, position, new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Delete$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
            public final void onDeleted(int i, long[] jArr) {
                Delete.$r8$lambda$l71qAE0wpSSzCZbSt6oyMYbPrTw(Delete.this, baseDataItem, i, jArr);
            }
        }, null);
        ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Delete$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Delete.$r8$lambda$0ezOuoBFUKr8SEiki7L1Uv_TXOo(Delete.this);
            }
        }, 500L);
        this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "delete_photo");
        TrackController.trackClick("403.11.5.1.11162", AutoTracking.getRef());
    }

    public /* synthetic */ void lambda$onClick$0(BaseDataItem baseDataItem, int i, long[] jArr) {
        this.mDataProvider.onContentChanged();
        ExtraPhotoSDK.sendDeletePhotoStatic(baseDataItem.getSpecialTypeFlags());
        this.mOwner.hideNarBarForFullScreenGesture();
        if (i > 0) {
            SoundUtils.playSoundForOperation(this.mContext, 0);
            TimeMonitor.trackTimeMonitor("403.45.0.1.13761", "403.11.5.1.11162", i);
            return;
        }
        TimeMonitor.cancelTimeMonitor("403.45.0.1.13761");
    }

    public /* synthetic */ void lambda$onClick$1() {
        this.isClickable = true;
    }
}
