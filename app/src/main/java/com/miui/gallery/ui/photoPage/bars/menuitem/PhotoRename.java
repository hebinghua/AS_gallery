package com.miui.gallery.ui.photoPage.bars.menuitem;

import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.ui.photodetail.PhotoRenameDialogFragment;
import com.miui.gallery.ui.photodetail.usecase.RenamePhotoCase;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.view.menu.IMenuItem;
import io.reactivex.subscribers.DisposableSubscriber;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;

/* loaded from: classes2.dex */
public class PhotoRename extends BaseMenuItemDelegate {
    public PhotoRenameDialogFragment mPhotoRenameDialogFragment;

    public static PhotoRename instance(IMenuItem iMenuItem) {
        return new PhotoRename(iMenuItem);
    }

    public PhotoRename(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(final BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        String title = baseDataItem.getTitle();
        final String pathDisplayBetter = baseDataItem.getPathDisplayBetter();
        String appendFileTitleWithExtention = BaseFileUtils.appendFileTitleWithExtention(title, pathDisplayBetter);
        final long key = baseDataItem.getKey();
        PhotoRenameDialogFragment newInstance = PhotoRenameDialogFragment.newInstance(appendFileTitleWithExtention, pathDisplayBetter, 1, new PhotoRenameDialogFragment.OnIntendToRename() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.PhotoRename.1
            @Override // com.miui.gallery.ui.photodetail.PhotoRenameDialogFragment.OnIntendToRename
            public void onIntendToRename(String str) {
                RenamePhotoCase.RequestBean requestBean;
                if (PhotoRename.this.mDataProvider.getFieldData().isFromInternal) {
                    requestBean = new RenamePhotoCase.RequestBean(pathDisplayBetter, str, key);
                } else {
                    requestBean = new RenamePhotoCase.RequestBean(pathDisplayBetter, str, -1L);
                }
                if (baseDataItem.isScreenshot()) {
                    TrackController.trackClick("403.11.5.1.11421", "photo", "screenshot");
                } else if (FileUtils.isFromCamera(pathDisplayBetter)) {
                    TrackController.trackClick("403.11.5.1.11421", "photo", "camera");
                } else {
                    TrackController.trackClick("403.11.5.1.11421", "photo", "others");
                }
                new RenamePhotoCase((AbstractCloudRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.CLOUD_REPOSITORY)).executeWith(new DisposableSubscriber<String>() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.PhotoRename.1.1
                    @Override // org.reactivestreams.Subscriber
                    public void onComplete() {
                    }

                    @Override // io.reactivex.subscribers.DisposableSubscriber
                    public void onStart() {
                        super.onStart();
                    }

                    @Override // org.reactivestreams.Subscriber
                    public void onNext(String str2) {
                        ToastUtils.makeText(PhotoRename.this.mContext, (int) R.string.photodetail_rename_success);
                        PhotoRename.this.dismissRenameDialog();
                        if (PhotoRename.this.mDataProvider.getFieldData().isFromFileManager) {
                            PhotoRename.this.mDataProvider.getFieldData().mCurrent.getDataSet().replaceFile(pathDisplayBetter, Paths.get(Paths.get(pathDisplayBetter, new String[0]).getParent().toString(), str2).toString());
                        }
                        PhotoRename.this.mDataProvider.onContentChanged();
                    }

                    @Override // org.reactivestreams.Subscriber
                    public void onError(Throwable th) {
                        if (th instanceof FileAlreadyExistsException) {
                            ToastUtils.makeText(PhotoRename.this.mContext, (int) R.string.photodetail_failed_already_exist);
                            return;
                        }
                        if (th instanceof StoragePermissionMissingException) {
                            PhotoRename photoRename = PhotoRename.this;
                            if (photoRename.mContext != null) {
                                photoRename.dismissRenameDialog();
                                ((StoragePermissionMissingException) th).offer(PhotoRename.this.mContext);
                                return;
                            }
                        }
                        ToastUtils.makeText(PhotoRename.this.mContext, (int) R.string.photodetail_rename_failed);
                        PhotoRename.this.dismissRenameDialog();
                    }
                }, requestBean, PhotoRename.this.mFragment.getLifecycle());
            }
        });
        this.mPhotoRenameDialogFragment = newInstance;
        newInstance.showAllowingStateLoss(this.mFragment.getFragmentManager(), "PhotoRenameDialogFragment");
    }

    public final void dismissRenameDialog() {
        PhotoRenameDialogFragment photoRenameDialogFragment = this.mPhotoRenameDialogFragment;
        if (photoRenameDialogFragment != null) {
            photoRenameDialogFragment.dismissSafely();
        }
    }
}
