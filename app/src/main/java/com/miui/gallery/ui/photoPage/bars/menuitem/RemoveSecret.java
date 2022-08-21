package com.miui.gallery.ui.photoPage.bars.menuitem;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class RemoveSecret extends BaseMenuItemDelegate {
    public static /* synthetic */ void $r8$lambda$T7Utb5QApjNqk_mIrAU3TJzSn_w(RemoveSecret removeSecret, long[] jArr) {
        removeSecret.lambda$onClick$0(jArr);
    }

    public static RemoveSecret instance(IMenuItem iMenuItem) {
        return new RemoveSecret(iMenuItem);
    }

    public RemoveSecret(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        BaseDataSet dataSet = this.mDataProvider.getFieldData().mCurrent.getDataSet();
        if (dataSet != null) {
            dataSet.removeFromSecret(this.mContext, this.mDataProvider.getFieldData().mCurrent.getPosition(), new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.RemoveSecret$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                public final void onComplete(long[] jArr) {
                    RemoveSecret.$r8$lambda$T7Utb5QApjNqk_mIrAU3TJzSn_w(RemoveSecret.this, jArr);
                }
            });
        }
        this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "remove_from_secret");
    }

    public /* synthetic */ void lambda$onClick$0(long[] jArr) {
        if (jArr == null || jArr[0] <= 0) {
            return;
        }
        this.mDataProvider.onContentChanged();
    }
}
