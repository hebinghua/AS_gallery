package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.content.DialogInterface;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.request.OCRRequestHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.view.menu.IMenuItem;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class Ocr extends BaseMenuItemDelegate {
    public ProgressDialog mOCRProgressDialog;
    public OCRRequestHelper mOCRRequestHelper;
    public OCRRequestHelper.OCRRequestListener ocrRequestListener;

    public static Ocr instance(IMenuItem iMenuItem) {
        return new Ocr(iMenuItem);
    }

    public Ocr(IMenuItem iMenuItem) {
        super(iMenuItem);
        this.ocrRequestListener = new OCRRequestHelper.OCRRequestListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Ocr.1
            @Override // com.miui.gallery.request.OCRRequestHelper.OCRRequestListener
            public void onRequestStart() {
                Ocr.this.mOCRProgressDialog = new ProgressDialog(Ocr.this.mContext);
                Ocr.this.mOCRProgressDialog.setMessage(GalleryApp.sGetAndroidContext().getString(R.string.ocr_ongoing));
                Ocr.this.mOCRProgressDialog.setCanceledOnTouchOutside(false);
                Ocr.this.mOCRProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Ocr.1.1
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.ocr_cancelled);
                        if (Ocr.this.mOCRRequestHelper != null) {
                            Ocr.this.mOCRRequestHelper.cancelReuqest();
                        }
                    }
                });
                Ocr.this.mOCRProgressDialog.show();
            }

            @Override // com.miui.gallery.request.OCRRequestHelper.OCRRequestListener
            public void onRequestEnd() {
                if (Ocr.this.mOCRProgressDialog == null || !Ocr.this.mOCRProgressDialog.isShowing()) {
                    return;
                }
                Ocr.this.mOCRProgressDialog.dismiss();
            }
        };
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        this.mOwner.postRecordCountEvent("OCR", "ocr_clicked");
        this.mOCRRequestHelper = new OCRRequestHelper(this.mContext, this.mDataProvider.getFieldData().mCurrent.itemView.getPhotoView(), baseDataItem, this.ocrRequestListener);
        TrackController.trackClick("403.11.5.1.11165", AutoTracking.getRef());
        this.mOCRRequestHelper.startRequest();
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        OCRRequestHelper oCRRequestHelper = this.mOCRRequestHelper;
        if (oCRRequestHelper != null) {
            oCRRequestHelper.release();
        }
    }
}
