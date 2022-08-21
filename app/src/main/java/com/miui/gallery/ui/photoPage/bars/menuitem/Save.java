package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.SaveUriDialogFragment;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class Save extends BaseMenuItemDelegate {
    public static /* synthetic */ void $r8$lambda$ifpFipKyhtp2V7WpdUAkvbkSx7w(Save save, String str) {
        save.lambda$onClick$0(str);
    }

    public static Save instance(IMenuItem iMenuItem) {
        return new Save(iMenuItem);
    }

    public Save(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        baseDataItem.save(this.mContext, new SaveUriDialogFragment.OnCompleteListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Save$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.SaveUriDialogFragment.OnCompleteListener
            public final void onComplete(String str) {
                Save.$r8$lambda$ifpFipKyhtp2V7WpdUAkvbkSx7w(Save.this, str);
            }
        });
        this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "save_photo");
    }

    public /* synthetic */ void lambda$onClick$0(String str) {
        int i;
        if (TextUtils.isEmpty(str)) {
            i = R.string.photo_page_save_uri_image_fail;
        } else {
            this.mItemDataState.setVisible(false);
            i = R.string.photo_page_save_uri_image_ok;
        }
        GalleryActivity galleryActivity = this.mContext;
        DialogUtil.showInfoDialog(galleryActivity, galleryActivity.getString(i), null);
    }
}
