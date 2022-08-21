package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.content.Intent;
import android.net.Uri;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.PhotoPagerSamplingStatHelper;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.VlogLibraryLoaderHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.vlog.VideoEditorEntranceUtils;
import com.miui.mediaeditor.api.MediaEditorApiHelper;
import com.miui.mediaeditor.api.MediaEditorIntentUtils;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class Edit extends BaseMenuItemDelegate {
    public boolean isClickable;
    public DownloadStateListener mDownloadStateListener;
    public PhotoEditorManager mEditorManager;
    public Runnable mFastClickCheckTask;

    /* renamed from: $r8$lambda$HJn17V5oa-H_kDRXhIOuUmtffRg */
    public static /* synthetic */ void m1626$r8$lambda$HJn17V5oaH_kDRXhIOuUmtffRg(Edit edit, BaseDataItem baseDataItem) {
        edit.lambda$onClick$4(baseDataItem);
    }

    /* renamed from: $r8$lambda$SF-uEcQllxJGfVaKqD79vMfyUgE */
    public static /* synthetic */ void m1627$r8$lambda$SFuEcQllxJGfVaKqD79vMfyUgE(Edit edit, BaseDataItem baseDataItem) {
        edit.lambda$onClick$2(baseDataItem);
    }

    /* renamed from: $r8$lambda$ddC_Ue8Z8vFvjJ_HVPiI-8R-20Q */
    public static /* synthetic */ void m1629$r8$lambda$ddC_Ue8Z8vFvjJ_HVPiI8R20Q(Edit edit) {
        edit.lambda$new$0();
    }

    public /* synthetic */ void lambda$new$0() {
        this.isClickable = true;
    }

    public static Edit instance(IMenuItem iMenuItem) {
        return new Edit(iMenuItem);
    }

    public Edit(IMenuItem iMenuItem) {
        super(iMenuItem);
        this.mFastClickCheckTask = new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Edit$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                Edit.m1629$r8$lambda$ddC_Ue8Z8vFvjJ_HVPiI8R20Q(Edit.this);
            }
        };
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate
    public void doInitFunction() {
        PhotoEditorManager photoEditorManager = new PhotoEditorManager(this.mDataProvider, (PhotoPageFragment) this.mFragment);
        this.mEditorManager = photoEditorManager;
        BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack = this.mConfigMenuCallBack;
        if (iConfigMenuCallBack != null) {
            iConfigMenuCallBack.setPhotoEditorManager(photoEditorManager);
        }
        this.isClickable = true;
        super.doInitFunction();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(final BaseDataItem baseDataItem) {
        if (!this.isClickable || !this.isFunctionInit || !MediaEditorInstaller.getInstance().installIfNotExist(this.mContext, null, false)) {
            return;
        }
        this.isClickable = false;
        ThreadManager.getMainHandler().postDelayed(this.mFastClickCheckTask, 600L);
        String str = "video";
        String str2 = "edit_video";
        if (MediaEditorUtils.isMediaEditorAvailable()) {
            if (baseDataItem.isVideo() && MediaEditorApiHelper.isVideoEditorAvailable()) {
                if (!MediaEditorApiHelper.isVlogAvailable()) {
                    MediaEditorIntentUtils.loadLibraryInMediaEditor(this.mContext, "vlog");
                    return;
                }
            } else if (baseDataItem.isImage() && baseDataItem.isSecret() && MediaEditorUtils.isMediaEditorSupportSecretAlbum() && !MediaEditorApiHelper.canAccessSecretAlbum()) {
                MediaEditorIntentUtils.requestSecretAlumAccessPermissionInMediaEditor(this.mContext);
                return;
            }
            PhotoPageMenuManager.IMenuOwner iMenuOwner = this.mOwner;
            String itemClickEventCategory = getItemClickEventCategory(baseDataItem);
            if (!baseDataItem.isVideo()) {
                str2 = "edit_photo";
            }
            iMenuOwner.postRecordCountEvent(itemClickEventCategory, str2);
            String ref = AutoTracking.getRef();
            if (!baseDataItem.isVideo()) {
                str = "image";
            }
            TrackController.trackClick("403.11.5.1.11160", ref, str);
            if (this.mOwner.isCurrentImageOverDisplayArea()) {
                this.mOwner.doAfterHideAnimByClickSpecialEnter(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Edit$$ExternalSyntheticLambda1
                    @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
                    public final void duringAction() {
                        Edit.this.lambda$onClick$1(baseDataItem);
                    }
                });
                return;
            } else if (this.mOwner.isLandscapeWindowMode()) {
                if (!IntentUtil.editPreCheck(baseDataItem, this.mContext, this.mFragment)) {
                    return;
                }
                this.mOwner.hideBars(false);
                ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Edit$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        Edit.m1627$r8$lambda$SFuEcQllxJGfVaKqD79vMfyUgE(Edit.this, baseDataItem);
                    }
                }, 10L);
                return;
            } else {
                lambda$onClick$1(baseDataItem);
                return;
            }
        }
        if (baseDataItem.isVideo() && VideoEditorEntranceUtils.isAvailable()) {
            VlogLibraryLoaderHelper.getInstance().addDownloadStateListener(getDownloadStateListener());
            if (!VlogLibraryLoaderHelper.getInstance().checkAbleOrDownload(this.mContext)) {
                return;
            }
        }
        PhotoPageMenuManager.IMenuOwner iMenuOwner2 = this.mOwner;
        String itemClickEventCategory2 = getItemClickEventCategory(baseDataItem);
        if (!baseDataItem.isVideo()) {
            str2 = "edit_photo";
        }
        iMenuOwner2.postRecordCountEvent(itemClickEventCategory2, str2);
        String ref2 = AutoTracking.getRef();
        if (!baseDataItem.isVideo()) {
            str = "image";
        }
        TrackController.trackClick("403.11.5.1.11160", ref2, str);
        if (this.mOwner.isCurrentImageOverDisplayArea()) {
            this.mOwner.doAfterHideAnimByClickSpecialEnter(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Edit$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
                public final void duringAction() {
                    Edit.this.lambda$onClick$3(baseDataItem);
                }
            });
        } else if (this.mOwner.isLandscapeWindowMode()) {
            if (!IntentUtil.editPreCheck(baseDataItem, this.mContext, this.mFragment)) {
                return;
            }
            this.mOwner.hideBars(false);
            ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Edit$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    Edit.m1626$r8$lambda$HJn17V5oaH_kDRXhIOuUmtffRg(Edit.this, baseDataItem);
                }
            }, 10L);
        } else {
            lambda$onClick$3(baseDataItem);
        }
    }

    public /* synthetic */ void lambda$onClick$2(BaseDataItem baseDataItem) {
        if (!lambda$onClick$1(baseDataItem)) {
            this.mOwner.showBars(false);
        }
    }

    public /* synthetic */ void lambda$onClick$4(BaseDataItem baseDataItem) {
        if (!lambda$onClick$3(baseDataItem)) {
            this.mOwner.showBars(false);
        }
    }

    /* renamed from: doEditForResult */
    public final boolean lambda$onClick$3(BaseDataItem baseDataItem) {
        IDataProvider iDataProvider = this.mDataProvider;
        if (iDataProvider == null || iDataProvider.getFieldData().mCurrent.itemView == null || !IntentUtil.startEditAction(baseDataItem, this.mContext, this.mFragment, this.mMenuItemManager.isFavorite(), this.mDataProvider.getFieldData().mCurrentClassification, this.mDataProvider.getFieldData().mCurrent.itemView.getPhotoView())) {
            return false;
        }
        PhotoEditorManager photoEditorManager = this.mEditorManager;
        if (photoEditorManager == null) {
            return true;
        }
        photoEditorManager.onStartEditor();
        return true;
    }

    /* renamed from: doEditForResultWithMediaEditor */
    public final boolean lambda$onClick$1(BaseDataItem baseDataItem) {
        IDataProvider iDataProvider = this.mDataProvider;
        if (iDataProvider == null || iDataProvider.getFieldData().mCurrent.itemView == null || !MediaEditorIntentUtils.startEditAction(baseDataItem, this.mContext, this.mFragment, this.mMenuItemManager.isFavorite(), this.mDataProvider.getFieldData().mCurrentClassification, this.mDataProvider.getFieldData().mCurrent.itemView.getPhotoView())) {
            return false;
        }
        PhotoEditorManager photoEditorManager = this.mEditorManager;
        if (photoEditorManager == null) {
            return true;
        }
        photoEditorManager.onStartEditor();
        return true;
    }

    public final LibraryLoaderHelper.DownloadStateListener getDownloadStateListener() {
        if (this.mDownloadStateListener == null) {
            this.mDownloadStateListener = new DownloadStateListener(this);
        }
        return this.mDownloadStateListener;
    }

    /* loaded from: classes2.dex */
    public static class DownloadStateListener implements LibraryLoaderHelper.DownloadStateListener {
        public WeakReference<Edit> mItem;

        public DownloadStateListener(Edit edit) {
            this.mItem = new WeakReference<>(edit);
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onDownloading() {
            if (BaseBuildUtil.isInternational()) {
                toast(R.string.photo_vlog_module_loading);
                return;
            }
            WeakReference<Edit> weakReference = this.mItem;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.mItem.get().setTitleId(R.string.loading);
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onFinish(boolean z, int i) {
            if (BaseBuildUtil.isInternational()) {
                toast(z ? R.string.photo_vlog_module_finish : R.string.photo_vlog_module_failed);
                return;
            }
            WeakReference<Edit> weakReference = this.mItem;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.mItem.get().setTitleId(R.string.operation_edit_video);
        }

        public final void toast(int i) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), GalleryApp.sGetAndroidContext().getResources().getString(i));
        }

        public void release() {
            VlogLibraryLoaderHelper.getInstance().removeDownloadStateListener(this);
            WeakReference<Edit> weakReference = this.mItem;
            if (weakReference != null) {
                weakReference.clear();
                this.mItem = null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class PhotoEditorManager extends TransitionEditorManager {
        public PhotoEditorManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment) {
            super(iDataProvider, photoPageFragment);
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager
        public boolean handleEditorResult(Intent intent) {
            if (intent != null) {
                long longExtra = intent.getLongExtra("photo_secret_id", 0L);
                if (longExtra > 0) {
                    setTargetId(longExtra);
                    notifyDataSetChange(longExtra);
                    return true;
                }
                Uri data = intent.getData();
                if (data != null) {
                    String lastPathSegment = data.getLastPathSegment();
                    PhotoPagerSamplingStatHelper.onEditorSaved(Uri.parse("file://" + lastPathSegment));
                    MediaEditorApiHelper.updateJustEditExportedPath(Scheme.FILE.wrap(lastPathSegment));
                    notifyDataSetChange(lastPathSegment);
                    setTargetPath(lastPathSegment);
                    return true;
                }
            }
            return false;
        }

        public void onDestroy() {
            release();
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        DownloadStateListener downloadStateListener = this.mDownloadStateListener;
        if (downloadStateListener != null) {
            downloadStateListener.release();
        }
        if (this.mFastClickCheckTask != null) {
            ThreadManager.getMainHandler().removeCallbacks(this.mFastClickCheckTask);
            this.mFastClickCheckTask = null;
        }
    }
}
