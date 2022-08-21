package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.text.TextUtils;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.manager.edit.UpdatableEditorManager;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class CorrectDoc extends BaseMenuItemDelegate {
    public DocCorrectionManager mDocCorrectionManager;

    public static CorrectDoc instance(IMenuItem iMenuItem) {
        return new CorrectDoc(iMenuItem);
    }

    public CorrectDoc(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate
    public void doInitFunction() {
        DocCorrectionManager docCorrectionManager = new DocCorrectionManager(this.mDataProvider, (PhotoPageFragment) this.mFragment);
        this.mDocCorrectionManager = docCorrectionManager;
        BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack = this.mConfigMenuCallBack;
        if (iConfigMenuCallBack != null) {
            iConfigMenuCallBack.setDocCorrectionManager(docCorrectionManager);
        }
        super.doInitFunction();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (this.isFunctionInit && ExtraPhotoSDK.isDeviceSupportCorrectDocument(this.mContext)) {
            String pathDisplayBetter = baseDataItem.getPathDisplayBetter();
            if (TextUtils.isEmpty(pathDisplayBetter)) {
                return;
            }
            if (baseDataItem.getWidth() >= 32767 || baseDataItem.getHeight() >= 32767) {
                ToastUtils.makeText(this.mContext, (int) R.string.photo_size_not_support);
                return;
            }
            if (pathDisplayBetter.equals(baseDataItem.getOriginalPath())) {
                if (!BaseFileMimeUtil.isJpgFromMimeType(baseDataItem.getMimeType()) && !BaseFileMimeUtil.isJpegImageFromMimeType(baseDataItem.getMimeType())) {
                    ToastUtils.makeText(this.mContext, (int) R.string.secret_reason_type_not_support);
                    return;
                }
            } else if (!pathDisplayBetter.endsWith(".jpg")) {
                ToastUtils.makeText(this.mContext, (int) R.string.secret_reason_type_not_support);
                return;
            }
            IntentUtil.startCorrectDocAction(pathDisplayBetter, this.mContext, this.mFragment, this.mDataProvider.getFieldData().isStartWhenLocked);
            this.mOwner.postRecordCountEvent("photo", "correct_document");
            TrackController.trackClick("403.11.5.1.11166", AutoTracking.getRef());
            DocCorrectionManager docCorrectionManager = this.mDocCorrectionManager;
            if (docCorrectionManager == null) {
                return;
            }
            docCorrectionManager.onStartEditor();
        }
    }

    /* loaded from: classes2.dex */
    public static class DocCorrectionManager extends UpdatableEditorManager {
        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.UpdatableEditorManager
        public String getTargetPackageName() {
            return "com.miui.extraphoto";
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.UpdatableEditorManager
        public boolean shouldInsertMediaStore() {
            return true;
        }

        public DocCorrectionManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment) {
            super(iDataProvider, photoPageFragment);
        }
    }
}
