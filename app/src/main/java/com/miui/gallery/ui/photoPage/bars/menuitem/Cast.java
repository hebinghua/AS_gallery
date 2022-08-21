package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.graphics.RectF;
import android.text.TextUtils;
import android.webkit.URLUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.tracing.Trace;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.projection.ConnectController;
import com.miui.gallery.projection.ConnectControllerMiPlay;
import com.miui.gallery.projection.ConnectControllerSingleton;
import com.miui.gallery.projection.IConnectController;
import com.miui.gallery.projection.RemoteControlReceiver;
import com.miui.gallery.projection.RemoteController;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.ProjectSlideFragment;
import com.miui.gallery.ui.ShareStateRouter;
import com.miui.gallery.ui.photoPage.bars.data.DataProvider;
import com.miui.gallery.ui.photoPage.bars.menuitem.Cast;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.PhotoOperationsUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.Utils;
import com.miui.gallery.view.menu.IMenuItem;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class Cast extends BaseMenuItemDelegate {
    public ProjectionManager mProjectManager;

    public static Cast instance(IMenuItem iMenuItem) {
        return new Cast(iMenuItem);
    }

    public Cast(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate
    public void doInitFunction() {
        if (!(BaseGalleryPreferences.CTA.canConnectNetwork() || !GalleryUtils.needImpunityDeclaration())) {
            return;
        }
        ProjectionManager projectionManager = new ProjectionManager();
        this.mProjectManager = projectionManager;
        projectionManager.initFunction();
        BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack = this.mConfigMenuCallBack;
        if (iConfigMenuCallBack != null) {
            iConfigMenuCallBack.setProjectionManager(this.mProjectManager);
        }
        super.doInitFunction();
    }

    public ProjectionManager getProjectionManager() {
        return this.mProjectManager;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        this.mProjectManager.projectClicked();
        this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "project_photo");
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        if (!this.isFunctionInit) {
            return;
        }
        this.mProjectManager.close();
    }

    public boolean isConnected() {
        return this.isFunctionInit && this.mProjectManager.isConnected();
    }

    public void enterSlideShow(int i) {
        if (!this.isFunctionInit) {
            return;
        }
        this.mProjectManager.enterSlideShow(i);
    }

    public void projectClicked() {
        if (!this.isFunctionInit) {
            return;
        }
        this.mProjectManager.projectClicked();
    }

    public void onFilterFinished(BaseDataItem baseDataItem, ArrayList<IMenuItemDelegate> arrayList, ArrayList<IMenuItemDelegate> arrayList2) {
        if (this.mProjectManager == null) {
            return;
        }
        if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(GalleryApp.sGetAndroidContext()).booleanValue() || (baseDataItem.getOriginalPath() != null && URLUtil.isContentUrl(baseDataItem.getOriginalPath()))) {
            return;
        }
        this.mProjectManager.refreshProjectState();
    }

    public void refreshProjectState() {
        if (!this.isFunctionInit) {
            return;
        }
        this.mProjectManager.refreshProjectState();
    }

    /* loaded from: classes2.dex */
    public class ProjectionManager implements RemoteControlReceiver.RemoteControlListener, IConnectController.OnStatusListener, Observer<BaseDataSet> {
        public IConnectController mConnectController;
        public IConnectController mConnectControllerMiPlay;
        public float mLastDegree;
        public RemoteController mRemoteController;

        public static /* synthetic */ void $r8$lambda$U7t5GgePyYkLSAVMOfuVWKMsoos(ProjectionManager projectionManager, int i) {
            projectionManager.lambda$onConnectStatusChanged$0(i);
        }

        public final float convert2miPlayDegree(float f) {
            return (360.0f - f) % 360.0f;
        }

        @Override // com.miui.gallery.projection.IConnectController.OnStatusListener
        public void onOpenStatusChanged(boolean z) {
        }

        public ProjectionManager() {
            Cast.this = r1;
        }

        public void initFunction() {
            DataProvider.ViewModelData viewModelData = Cast.this.mDataProvider.getViewModelData();
            Cast cast = Cast.this;
            viewModelData.addCurrentDataSetObserver(cast.mContext, cast.mFragment, this);
            this.mRemoteController = new RemoteController(this);
            IConnectController connectControllerSingleton = ConnectControllerSingleton.getInstance();
            this.mConnectController = connectControllerSingleton;
            if (connectControllerSingleton instanceof ConnectController) {
                ((ConnectController) connectControllerSingleton).attachActivity(Cast.this.mContext);
            }
            this.mConnectController.registerStatusListener(this);
            this.mConnectControllerMiPlay = new ConnectControllerMiPlay(Cast.this.mContext);
        }

        public boolean isConnected() {
            return isConnectControllerConnected() || isMiPlayConnected();
        }

        public boolean isMiPlayConnected() {
            IConnectController iConnectController = this.mConnectControllerMiPlay;
            return iConnectController != null && iConnectController.isConnected();
        }

        public final boolean isConnectControllerConnected() {
            IConnectController iConnectController = this.mConnectController;
            return iConnectController != null && iConnectController.isConnected();
        }

        public void enableRemoteControl() {
            if (isConnectControllerConnected()) {
                enableRemoteControllerInternal();
            }
        }

        public void disableRemoteControl() {
            disableRemoteControllerInternal(true);
        }

        public final void enableRemoteControllerInternal() {
            RemoteController remoteController = this.mRemoteController;
            if (remoteController == null) {
                return;
            }
            remoteController.enable(Cast.this.mContext);
        }

        public final void disableRemoteControllerInternal(boolean z) {
            RemoteController remoteController = this.mRemoteController;
            if (remoteController == null) {
                return;
            }
            if (z) {
                remoteController.disableDelay(Cast.this.mContext);
            } else {
                remoteController.disable(Cast.this.mContext);
            }
        }

        @Override // com.miui.gallery.projection.IConnectController.OnStatusListener
        public void onConnectStatusChanged(final int i) {
            Cast.this.mContext.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Cast$ProjectionManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Cast.ProjectionManager.$r8$lambda$U7t5GgePyYkLSAVMOfuVWKMsoos(Cast.ProjectionManager.this, i);
                }
            });
            if (i == 1) {
                enableRemoteControllerInternal();
            } else {
                disableRemoteControllerInternal(false);
            }
        }

        public void close() {
            IConnectController iConnectController = this.mConnectController;
            if (iConnectController == null) {
                return;
            }
            iConnectController.unregisterStatusListener(this);
            IConnectController iConnectController2 = this.mConnectController;
            if (iConnectController2 instanceof ConnectController) {
                ((ConnectController) iConnectController2).detachActivity(Cast.this.mContext);
            }
            this.mConnectController.closeService();
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(BaseDataSet baseDataSet) {
            updateSet(baseDataSet);
        }

        public void updateSet(BaseDataSet baseDataSet) {
            IConnectController iConnectController = this.mConnectControllerMiPlay;
            if (iConnectController != null) {
                iConnectController.updateCurrentFolder(baseDataSet);
            }
            IConnectController iConnectController2 = this.mConnectController;
            if (iConnectController2 != null) {
                iConnectController2.updateCurrentFolder(baseDataSet);
            }
        }

        public void onMirrorResume(String str) {
            IConnectController iConnectController;
            if (!TextUtils.isEmpty(str) && (iConnectController = this.mConnectControllerMiPlay) != null && iConnectController.isConnected()) {
                this.mConnectControllerMiPlay.onMirrorResume(str);
            }
        }

        public void settleItem(BaseDataItem baseDataItem, int i) {
            if (baseDataItem != null && !Cast.this.mOwner.isSecretAlbum() && !Cast.this.mDataProvider.isProcessingMedia(baseDataItem) && !Cast.this.mDataProvider.getFieldData().isFromNote) {
                this.mLastDegree = 0.0f;
                Trace.beginSection("updateCastPhoto");
                try {
                    String pathDisplayBetter = baseDataItem.getPathDisplayBetter();
                    if (TextUtils.isEmpty(pathDisplayBetter)) {
                        return;
                    }
                    IConnectController iConnectController = this.mConnectControllerMiPlay;
                    if (iConnectController != null) {
                        iConnectController.updateCurrentPhoto(pathDisplayBetter, i);
                    }
                    IConnectController iConnectController2 = this.mConnectController;
                    if (iConnectController2 != null) {
                        iConnectController2.updateCurrentPhoto(pathDisplayBetter, i);
                    }
                } finally {
                    Trace.endSection();
                }
            }
        }

        @Override // com.miui.gallery.projection.RemoteControlReceiver.RemoteControlListener
        public void onRemoteControl(boolean z) {
            Cast cast;
            Cast cast2;
            int pageCount = Cast.this.mOwner.getPageCount();
            if (pageCount <= 0) {
                return;
            }
            int position = Cast.this.mDataProvider.getFieldData().mCurrent.getPosition();
            if (z) {
                int i = position - 1;
                if (i < 0) {
                    return;
                }
                Cast.this.mOwner.setCurrentPosition(i, !MiscUtil.isKeyGuardLocked(cast2.mContext));
                return;
            }
            int i2 = position + 1;
            if (i2 >= pageCount) {
                return;
            }
            Cast.this.mOwner.setCurrentPosition(i2, !MiscUtil.isKeyGuardLocked(cast.mContext));
        }

        public final boolean isBigScreenSupported(BaseDataItem baseDataItem) {
            if (baseDataItem == null || baseDataItem.isSecret()) {
                return false;
            }
            if (baseDataItem.getOriginalPath() != null && URLUtil.isContentUrl(baseDataItem.getOriginalPath())) {
                return false;
            }
            return !TextUtils.isEmpty(baseDataItem.getPathDisplayBetter());
        }

        public void enterSlideShow(int i) {
            if (isMiPlayConnected()) {
                this.mConnectControllerMiPlay.showSlide(i);
                Cast cast = Cast.this;
                ProjectSlideFragment.showProjectSlideFragment((BaseActivity) cast.mContext, cast.mFragment, this.mConnectControllerMiPlay.getConnectedDevice());
            } else if (isConnectControllerConnected() && !this.mConnectController.showSlide(i)) {
                GalleryActivity galleryActivity = Cast.this.mContext;
                ToastUtils.makeText(galleryActivity, galleryActivity.getText(R.string.projection_slide_fail_cause_loading));
            } else {
                disableRemoteControllerInternal(false);
                Cast cast2 = Cast.this;
                ProjectSlideFragment.showProjectSlideFragment((BaseActivity) cast2.mContext, cast2.mFragment, this.mConnectController.getConnectedDevice());
            }
        }

        public void exitSlideShow() {
            if (isMiPlayConnected()) {
                this.mConnectControllerMiPlay.stopSlide();
            } else if (!isConnectControllerConnected()) {
            } else {
                settleItem(Cast.this.mDataProvider.getFieldData().mCurrent.getDataItem(), Cast.this.mDataProvider.getFieldData().mCurrent.getPosition());
                enableRemoteControllerInternal();
                if (Cast.this.mFragment.isVisible()) {
                    return;
                }
                disableRemoteControllerInternal(true);
            }
        }

        public void stopMiPlay() {
            IConnectController iConnectController = this.mConnectControllerMiPlay;
            if (iConnectController == null) {
                return;
            }
            iConnectController.stop();
        }

        public void setVideoIsPlaying(boolean z) {
            IConnectController iConnectController = this.mConnectControllerMiPlay;
            if (iConnectController == null) {
                return;
            }
            iConnectController.setVideoIsPlaying(z);
        }

        public final void projectClicked() {
            if (PhotoOperationsUtil.isScreenProjectOn(Cast.this.mContext.getContentResolver(), false)) {
                GalleryActivity galleryActivity = Cast.this.mContext;
                ToastUtils.makeText(galleryActivity, galleryActivity.getText(R.string.projection_screen_project_in_screening));
                return;
            }
            IConnectController iConnectController = this.mConnectController;
            if (iConnectController == null) {
                return;
            }
            iConnectController.project();
        }

        public void refreshProjectState() {
            IConnectController iConnectController = this.mConnectController;
            if (iConnectController == null) {
                return;
            }
            lambda$onConnectStatusChanged$0(iConnectController.getConnectStatus());
        }

        /* renamed from: refreshProjectState */
        public void lambda$onConnectStatusChanged$0(int i) {
            String string;
            Cast cast = Cast.this;
            if (!cast.isFunctionInit || !cast.mFragment.isAdded()) {
                return;
            }
            if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(Cast.this.mContext).booleanValue()) {
                Trace.beginSection("broadcastProjectState");
                try {
                    if (!isBigScreenSupported(Cast.this.mDataProvider.getFieldData().mCurrent.getDataItem())) {
                        ShareStateRouter.getInstance().broadcastProjectState(1);
                        return;
                    }
                    if (i == -1) {
                        ShareStateRouter.getInstance().broadcastProjectState(2);
                    } else if (i == 0 || i == 1) {
                        ShareStateRouter.getInstance().broadcastProjectState(3);
                    }
                    return;
                } finally {
                    Trace.endSection();
                }
            }
            Trace.beginSection("refreshCastItem");
            if (i == 0) {
                string = Cast.this.mContext.getString(R.string.operation_cast_connecting_format, new Object[]{this.mConnectController.getConnectingDevice()});
            } else if (i == 1) {
                string = Cast.this.mContext.getString(R.string.operation_cast_connected_format, new Object[]{this.mConnectController.getConnectedDevice()});
            } else {
                string = Cast.this.mContext.getString(R.string.operation_projection);
            }
            Cast.this.setTitle(string);
        }

        public void updateRotate(float f) {
            IConnectController iConnectController;
            if (this.mLastDegree == f || (iConnectController = this.mConnectControllerMiPlay) == null) {
                return;
            }
            iConnectController.rotate(convert2miPlayDegree(f));
            this.mLastDegree = f;
            HashMap hashMap = new HashMap();
            hashMap.put("value", String.valueOf(this.mLastDegree));
            hashMap.put("tip", "403.11.4.1.11155");
            TrackController.trackDualFinger(hashMap);
        }

        public void updateRemoteView(BaseDataItem baseDataItem, RectF rectF) {
            if (isMiPlayConnected()) {
                doMiPlayUpdateRemoteView(baseDataItem, rectF);
            } else if (!isConnectControllerConnected()) {
            } else {
                doConnectUpdateRemoteView(baseDataItem, rectF);
            }
        }

        public final void doConnectUpdateRemoteView(BaseDataItem baseDataItem, RectF rectF) {
            float width;
            float height;
            float f;
            if (this.mConnectController == null || baseDataItem == null || baseDataItem.getWidth() <= 0 || rectF.width() <= 0.0f) {
                return;
            }
            int pageWidth = Cast.this.mOwner.getPageWidth();
            int pageHeight = Cast.this.mOwner.getPageHeight();
            float width2 = rectF.width() / baseDataItem.getWidth();
            float min = Math.min(pageWidth / baseDataItem.getWidth(), pageHeight / baseDataItem.getHeight());
            if (Utils.floatNear(width2, min, 1.0E-6f) || width2 < min) {
                width = baseDataItem.getWidth() / 2.0f;
                height = baseDataItem.getHeight() / 2.0f;
                f = min;
            } else {
                width = ((pageWidth / 2) - rectF.left) / width2;
                height = ((pageHeight / 2) - rectF.top) / width2;
                f = width2;
            }
            this.mConnectController.syncRemoteView(width, height, Math.round(baseDataItem.getWidth() * min), Math.round(baseDataItem.getHeight() * min), baseDataItem.getWidth(), baseDataItem.getHeight(), f);
        }

        public final void doMiPlayUpdateRemoteView(BaseDataItem baseDataItem, RectF rectF) {
            int height;
            int width;
            float f;
            float f2;
            float f3;
            if (this.mConnectControllerMiPlay == null || baseDataItem == null || baseDataItem.getWidth() <= 0 || rectF.width() <= 0.0f) {
                return;
            }
            if ((rectF.width() > rectF.height() && baseDataItem.getWidth() > baseDataItem.getHeight()) || (rectF.width() < rectF.height() && baseDataItem.getWidth() < baseDataItem.getHeight())) {
                height = baseDataItem.getWidth();
                width = baseDataItem.getHeight();
            } else {
                height = baseDataItem.getHeight();
                width = baseDataItem.getWidth();
            }
            int pageWidth = Cast.this.mOwner.getPageWidth();
            int pageHeight = Cast.this.mOwner.getPageHeight();
            float f4 = height;
            float width2 = rectF.width() / f4;
            float f5 = width;
            float min = Math.min(pageWidth / f4, pageHeight / f5);
            if (Utils.floatNear(width2, min, 1.0E-6f) || width2 < min) {
                f = min;
                f2 = f5 / 2.0f;
                f3 = f4 / 2.0f;
            } else {
                f3 = ((pageWidth / 2) - rectF.left) / width2;
                f2 = ((pageHeight / 2) - rectF.top) / width2;
                f = width2;
            }
            this.mConnectControllerMiPlay.syncRemoteView(f3, f2, Math.round(f4 * min), Math.round(min * f5), f4, f5, f);
        }
    }
}
