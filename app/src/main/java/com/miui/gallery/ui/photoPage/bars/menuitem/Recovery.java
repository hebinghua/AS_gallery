package com.miui.gallery.ui.photoPage.bars.menuitem;

import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class Recovery extends AbstractTrash {
    public static /* synthetic */ void $r8$lambda$KTTEGvXNC3LRcSX5zM4PAFAS8Lk(Recovery recovery) {
        recovery.lambda$onClick$0();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.AbstractTrash
    public String getInvokerTag() {
        return "PhotoPageFragment_MenuManager_MenuItem_Recovery";
    }

    public static Recovery instance(IMenuItem iMenuItem) {
        return new Recovery(iMenuItem);
    }

    public Recovery(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        TrackController.trackClick("403.21.2.1.11282", AutoTracking.getRef());
        executeTask(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Recovery$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
            public final void duringAction() {
                Recovery.$r8$lambda$KTTEGvXNC3LRcSX5zM4PAFAS8Lk(Recovery.this);
            }
        });
    }

    public /* synthetic */ void lambda$onClick$0() {
        TrashUtils.doRecovery(this.mContext, getPurgeOrRecoveryList());
    }
}
