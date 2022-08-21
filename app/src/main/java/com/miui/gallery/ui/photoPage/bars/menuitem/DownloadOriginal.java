package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.DownloadFragment;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.BulkDownloadHelper;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListenerAdapter;
import com.miui.gallery.view.menu.IMenuItem;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DownloadOriginal extends BaseMenuItemDelegate {
    public final CloudImageLoadingListener mDownloadListener;

    /* loaded from: classes2.dex */
    public interface DownloadCallback {
        void downloadSuccess(String str, BaseDataItem baseDataItem);
    }

    /* renamed from: $r8$lambda$nc_8eX1bplVvs8X4G4H-6F7lFeI */
    public static /* synthetic */ void m1625$r8$lambda$nc_8eX1bplVvs8X4G4H6F7lFeI(DownloadOriginal downloadOriginal, BaseDataItem baseDataItem, DownloadCallback downloadCallback, boolean z, boolean z2) {
        downloadOriginal.lambda$downloadOrigin$0(baseDataItem, downloadCallback, z, z2);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate
    public CharSequence getItemName() {
        return "下载";
    }

    public static DownloadOriginal instance(IMenuItem iMenuItem) {
        return new DownloadOriginal(iMenuItem);
    }

    public DownloadOriginal(IMenuItem iMenuItem) {
        super(iMenuItem);
        this.mDownloadListener = new PhotoPageCloudImageLoadingListenerAdapter();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate, com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setConfigMenuCallBack(BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack) {
        super.setConfigMenuCallBack(iConfigMenuCallBack);
        if (iConfigMenuCallBack != null) {
            iConfigMenuCallBack.setCloudImageLoadingListener(this.mDownloadListener);
        }
    }

    public void onFilterFinished(BaseDataItem baseDataItem, ArrayList<IMenuItemDelegate> arrayList, ArrayList<IMenuItemDelegate> arrayList2) {
        refreshDownloadItem(baseDataItem, isVisible(), isEnable());
    }

    public final void refreshDownloadItem(boolean z, boolean z2) {
        if (!this.isFunctionInit) {
            return;
        }
        refreshDownloadItem(this.mDataProvider.getFieldData().mCurrent.getDataItem(), z, z2);
    }

    public final void refreshDownloadItem(BaseDataItem baseDataItem, boolean z, boolean z2) {
        if (!this.isFunctionInit) {
            return;
        }
        setTitle(getDownloadOriginTitle(baseDataItem));
        setVisible(z);
        setEnable(z2);
    }

    public final String getDownloadOriginTitle(BaseDataItem baseDataItem) {
        if (baseDataItem == null) {
            return null;
        }
        if (baseDataItem.isBurstItem()) {
            long j = 0;
            List<BaseDataItem> burstGroup = baseDataItem.getBurstGroup();
            for (BaseDataItem baseDataItem2 : burstGroup) {
                j += baseDataItem2.getSize();
            }
            return this.mFragment.getResources().getString(R.string.burst_image_download_item_title, this.mFragment.getResources().getQuantityString(R.plurals.burst_image_download_item_title_count, burstGroup.size(), Integer.valueOf(burstGroup.size())), FormatUtil.formatFileSize(this.mFragment.getActivity(), j));
        }
        return this.mFragment.getResources().getString(R.string.image_download_item_title, FormatUtil.formatFileSize(this.mFragment.getActivity(), baseDataItem.getSize()));
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        PhotoPageItem photoPageItem;
        if (this.isFunctionInit && (photoPageItem = this.mDataProvider.getFieldData().mCurrent.itemView) != null) {
            photoPageItem.downloadOrigin();
            this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "download_origin");
            TrackController.trackClick("403.11.5.1.15888");
        }
    }

    public void downloadOrigin(final BaseDataItem baseDataItem, final DownloadCallback downloadCallback) {
        if (!this.isFunctionInit) {
            return;
        }
        if (BaseNetworkUtils.isActiveNetworkMetered()) {
            NetworkConsider.consider(this.mContext, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    DownloadOriginal.m1625$r8$lambda$nc_8eX1bplVvs8X4G4H6F7lFeI(DownloadOriginal.this, baseDataItem, downloadCallback, z, z2);
                }
            });
            return;
        }
        refreshDownloadItem(true, false);
        doDownloadOrigin(baseDataItem, DownloadType.ORIGIN, downloadCallback);
    }

    public /* synthetic */ void lambda$downloadOrigin$0(BaseDataItem baseDataItem, DownloadCallback downloadCallback, boolean z, boolean z2) {
        if (z) {
            refreshDownloadItem(true, false);
            doDownloadOrigin(baseDataItem, DownloadType.ORIGIN_FORCE, downloadCallback);
        }
    }

    /* renamed from: com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements DownloadFragment.OnDownloadListener {
        public final /* synthetic */ DownloadCallback val$downloadCallback;
        public final /* synthetic */ DownloadType val$imageType;
        public final /* synthetic */ BaseDataItem val$item;

        public static /* synthetic */ void $r8$lambda$KeIgkN8iF_XLs6GCIwiOEN2CkhA(AnonymousClass1 anonymousClass1, BaseDataItem baseDataItem, DownloadType downloadType, DialogInterface dialogInterface, int i) {
            anonymousClass1.lambda$onDownloadComplete$1(baseDataItem, downloadType, dialogInterface, i);
        }

        public static /* synthetic */ void $r8$lambda$LJ8sw3EeVR230P3V92dCONe2NHs(AnonymousClass1 anonymousClass1, BaseDataItem baseDataItem, DownloadCallback downloadCallback, DialogInterface dialogInterface, int i) {
            anonymousClass1.lambda$onDownloadComplete$0(baseDataItem, downloadCallback, dialogInterface, i);
        }

        public AnonymousClass1(BaseDataItem baseDataItem, DownloadCallback downloadCallback, DownloadType downloadType) {
            DownloadOriginal.this = r1;
            this.val$item = baseDataItem;
            this.val$downloadCallback = downloadCallback;
            this.val$imageType = downloadType;
        }

        @Override // com.miui.gallery.ui.DownloadFragment.OnDownloadListener
        public void onDownloadComplete(List<BulkDownloadHelper.BulkDownloadItem> list, List<BulkDownloadHelper.BulkDownloadItem> list2) {
            if (list == null || list.size() < 1) {
                GalleryActivity galleryActivity = DownloadOriginal.this.mContext;
                String string = galleryActivity.getString(R.string.download_retry_message);
                String string2 = DownloadOriginal.this.mContext.getString(R.string.download_retry_title);
                final BaseDataItem baseDataItem = this.val$item;
                final DownloadCallback downloadCallback = this.val$downloadCallback;
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal$1$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        DownloadOriginal.AnonymousClass1.$r8$lambda$LJ8sw3EeVR230P3V92dCONe2NHs(DownloadOriginal.AnonymousClass1.this, baseDataItem, downloadCallback, dialogInterface, i);
                    }
                };
                final BaseDataItem baseDataItem2 = this.val$item;
                final DownloadType downloadType = this.val$imageType;
                DialogUtil.showInfoDialog(galleryActivity, string, string2, (int) R.string.download_retry_text, 17039360, onClickListener, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal$1$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        DownloadOriginal.AnonymousClass1.$r8$lambda$KeIgkN8iF_XLs6GCIwiOEN2CkhA(DownloadOriginal.AnonymousClass1.this, baseDataItem2, downloadType, dialogInterface, i);
                    }
                });
                return;
            }
            this.val$item.setFilePath(list.get(0).getDownloadPath());
            this.val$downloadCallback.downloadSuccess(list.get(0).getDownloadPath(), this.val$item);
            DownloadOriginal.this.mDownloadListener.onDownloadComplete(this.val$item.getDownloadUri(), this.val$imageType, null, this.val$item.getOriginalPath());
        }

        public /* synthetic */ void lambda$onDownloadComplete$0(BaseDataItem baseDataItem, DownloadCallback downloadCallback, DialogInterface dialogInterface, int i) {
            DownloadOriginal.this.downloadOrigin(baseDataItem, downloadCallback);
        }

        public /* synthetic */ void lambda$onDownloadComplete$1(BaseDataItem baseDataItem, DownloadType downloadType, DialogInterface dialogInterface, int i) {
            DownloadOriginal.this.mDownloadListener.onLoadingCancelled(baseDataItem.getDownloadUri(), downloadType, null);
        }

        @Override // com.miui.gallery.ui.DownloadFragment.OnDownloadListener
        public void onCanceled() {
            DownloadOriginal.this.mDownloadListener.onLoadingCancelled(this.val$item.getDownloadUri(), this.val$imageType, null);
        }
    }

    public final void doDownloadOrigin(BaseDataItem baseDataItem, DownloadType downloadType, DownloadCallback downloadCallback) {
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(baseDataItem, downloadCallback, downloadType);
        BulkDownloadHelper.BulkDownloadItem bulkDownloadItem = new BulkDownloadHelper.BulkDownloadItem(baseDataItem.getDownloadUri(), downloadType, baseDataItem.getSize());
        ArrayList arrayList = new ArrayList();
        arrayList.add(bulkDownloadItem);
        DownloadFragment newInstance = DownloadFragment.newInstance(arrayList);
        newInstance.setOnDownloadListener(anonymousClass1);
        newInstance.showAllowingStateLoss(this.mFragment.getFragmentManager(), "DownloadFragment");
    }

    /* loaded from: classes2.dex */
    public class PhotoPageCloudImageLoadingListenerAdapter extends CloudImageLoadingListenerAdapter {
        public PhotoPageCloudImageLoadingListenerAdapter() {
            DownloadOriginal.this = r1;
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingFailed(Uri uri, DownloadType downloadType, View view, ErrorCode errorCode, String str) {
            BaseDataItem dataItem;
            if (DownloadOriginal.this.isFunctionInit && DownloadType.isOrigin(downloadType) && (dataItem = DownloadOriginal.this.mDataProvider.getFieldData().mCurrent.getDataItem()) != null && !IncompatibleMediaType.isUnsupportedMediaType(dataItem.getMimeType())) {
                DownloadOriginal.this.refreshDownloadItem(true, true);
            }
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListenerAdapter, com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingStarted(Uri uri, DownloadType downloadType, View view) {
            if (DownloadOriginal.this.isFunctionInit && DownloadType.isOrigin(downloadType)) {
                DownloadOriginal.this.refreshDownloadItem(true, false);
            }
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListenerAdapter, com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingCancelled(Uri uri, DownloadType downloadType, View view) {
            if (DownloadOriginal.this.isFunctionInit && DownloadType.isOrigin(downloadType)) {
                DownloadOriginal.this.refreshDownloadItem(true, true);
            }
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onDownloadComplete(Uri uri, DownloadType downloadType, View view, String str) {
            if (DownloadOriginal.this.isFunctionInit && DownloadType.isOrigin(downloadType)) {
                DownloadOriginal.this.refreshDownloadItem(false, false);
                DownloadOriginal.this.mOwner.onDownloadComplete(DownloadOriginal.this.mDataProvider.getFieldData().mCurrent.getDataItem());
            }
        }
    }
}
