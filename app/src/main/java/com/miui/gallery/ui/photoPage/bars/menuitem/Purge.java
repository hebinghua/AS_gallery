package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.content.DialogInterface;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.util.SoundUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.view.menu.IMenuItem;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class Purge extends AbstractTrash {
    /* renamed from: $r8$lambda$9brV8L27vHGwTVW8ukjXqUhzj-Y */
    public static /* synthetic */ void m1631$r8$lambda$9brV8L27vHGwTVW8ukjXqUhzjY(Purge purge, DialogInterface dialogInterface, int i) {
        purge.lambda$onClick$0(dialogInterface, i);
    }

    /* renamed from: $r8$lambda$QEp-aW2E-yO7iAl7d_4W3QMdU-o */
    public static /* synthetic */ void m1632$r8$lambda$QEpaW2EyO7iAl7d_4W3QMdUo(Purge purge) {
        purge.lambda$doPurge$1();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.AbstractTrash
    public String getInvokerTag() {
        return "PhotoPageFragment_MenuManager_MenuItem_Purge";
    }

    public static Purge instance(IMenuItem iMenuItem) {
        return new Purge(iMenuItem);
    }

    public Purge(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        AlertDialog create = new AlertDialog.Builder(this.mContext).setMessage(this.mContext.getString(SyncUtil.isGalleryCloudSyncable(this.mContext) ? R.string.photo_purge_dialog_message_cloud : R.string.photo_purge_dialog_message_local)).setTitle(this.mContext.getString(R.string.photo_purge_dialog_title)).setCancelable(true).setNeutralButton(this.mContext.getString(R.string.trash_purge_positive_button), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Purge$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                Purge.m1631$r8$lambda$9brV8L27vHGwTVW8ukjXqUhzjY(Purge.this, dialogInterface, i);
            }
        }).setNegativeButton(this.mContext.getString(17039360), (DialogInterface.OnClickListener) null).create();
        create.show();
        create.getButton(-3).setTextColor(this.mContext.getResources().getColor(R.color.trash_delete_all_button_text_color, null));
    }

    public /* synthetic */ void lambda$onClick$0(DialogInterface dialogInterface, int i) {
        TimeMonitor.createNewTimeMonitor("403.21.0.1.13769");
        doPurge();
        SoundUtils.playSoundForOperation(this.mContext, 0);
    }

    public final void doPurge() {
        TrackController.trackClick("403.21.2.1.11281", AutoTracking.getRef());
        executeTask(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Purge$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
            public final void duringAction() {
                Purge.m1632$r8$lambda$QEpaW2EyO7iAl7d_4W3QMdUo(Purge.this);
            }
        });
    }

    public /* synthetic */ void lambda$doPurge$1() {
        TrashUtils.doPurge(this.mContext, getPurgeOrRecoveryList());
    }
}
