package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.text.TextUtils;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.VideoCompressDialogFragment;
import com.miui.gallery.ui.photoPage.bars.menuitem.VideoCompress;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.video.compress.Resolution;
import com.miui.gallery.video.compress.VideoCompressCheckHelper;
import com.miui.gallery.video.compress.VideoCompressDownloadStateListener;
import com.miui.gallery.video.compress.VideoCompressHelper;
import com.miui.gallery.video.compress.VideoCompressSavingFragment;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.mediaeditor.utils.FilePermissionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class VideoCompress extends BaseMenuItemDelegate {
    public VideoCompressManager mVideoCompressManager;

    public static VideoCompress instance(IMenuItem iMenuItem) {
        return new VideoCompress(iMenuItem);
    }

    public VideoCompress(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate
    public void doInitFunction() {
        VideoCompressManager videoCompressManager = new VideoCompressManager();
        this.mVideoCompressManager = videoCompressManager;
        BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack = this.mConfigMenuCallBack;
        if (iConfigMenuCallBack != null) {
            iConfigMenuCallBack.setVideoCompressManager(videoCompressManager);
        }
        super.doInitFunction();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        this.mVideoCompressManager.prepare(baseDataItem);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onResume(LifecycleOwner lifecycleOwner) {
        this.mVideoCompressManager.resume();
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        this.mVideoCompressManager.onDestroy();
    }

    /* loaded from: classes2.dex */
    public class VideoCompressManager {
        public VideoCompressDialogFragment mCompressDialog;
        public VideoCompressDialogFragment.OnCompressListener mCompressListener;
        public List<Resolution> mDatas;
        public ProgressDialog mDownloadProgressDialog;
        public boolean mIsShowing;
        public String mPath;
        public VideoCompressHelper.CompressCallback mProbeCallback;
        public int mProbeResult;
        public VideoCompressSavingFragment mSaveDialog;
        public long mSize;
        public VideoCompressDownloadStateListener mVideoCompressDownloadStateListener;
        public VideoCompressHelper mVideoCompressHelper;

        public VideoCompressManager() {
            VideoCompressSavingFragment videoCompressSavingFragment;
            VideoCompress.this = r2;
            this.mProbeResult = 0;
            this.mCompressListener = new AnonymousClass1();
            this.mProbeCallback = new VideoCompressHelper.CompressCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.VideoCompress.VideoCompressManager.2
                {
                    VideoCompressManager.this = this;
                }

                @Override // com.miui.gallery.video.compress.VideoCompressHelper.CompressCallback
                public void onProbeResult(int i, List<Resolution> list) {
                    VideoCompressManager.this.mProbeResult = i;
                    VideoCompressManager.this.mDatas = list;
                    VideoCompressManager.this.initDialog();
                }

                @Override // com.miui.gallery.video.compress.VideoCompressHelper.CompressCallback
                public void onCompressProgress(int i) {
                    VideoCompressManager.this.mSaveDialog.setProgress(i);
                }

                @Override // com.miui.gallery.video.compress.VideoCompressHelper.CompressCallback
                public void onCompressFinish(String str) {
                    VideoCompressManager.this.mIsShowing = false;
                    VideoCompressManager.this.mSaveDialog.dismissSafely();
                    VideoCompressManager.this.notifyDataSetChange(str, false);
                }

                @Override // com.miui.gallery.video.compress.VideoCompressHelper.CompressCallback
                public void onCompressCancel() {
                    VideoCompressManager.this.mIsShowing = false;
                    VideoCompressManager.this.mSaveDialog.dismissSafely();
                }

                @Override // com.miui.gallery.video.compress.VideoCompressHelper.CompressCallback
                public void onCompressFailed(int i) {
                    VideoCompressManager.this.mIsShowing = false;
                    ToastUtils.makeText(VideoCompress.this.mContext, (int) R.string.video_compress_not_with_format);
                    VideoCompressManager.this.mSaveDialog.dismissSafely();
                }
            };
            this.mVideoCompressDownloadStateListener = new VideoCompressDownloadStateListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.VideoCompress.VideoCompressManager.3
                @Override // com.miui.gallery.video.compress.VideoCompressDownloadStateListener
                public void onDownloading(int i) {
                }

                {
                    VideoCompressManager.this = this;
                }

                @Override // com.miui.gallery.video.compress.VideoCompressDownloadStateListener
                public void onDownloadStart() {
                    VideoCompressManager.this.mDownloadProgressDialog = new ProgressDialog(VideoCompress.this.mContext);
                    VideoCompressManager.this.mDownloadProgressDialog.setMessage(VideoCompress.this.mContext.getString(R.string.loading_video_compress_lib));
                    VideoCompressManager.this.mDownloadProgressDialog.setCancelable(true);
                    VideoCompressManager.this.mDownloadProgressDialog.setCanceledOnTouchOutside(true);
                    VideoCompressManager.this.mDownloadProgressDialog.setIndeterminate(true);
                    VideoCompressManager.this.mDownloadProgressDialog.show();
                }

                @Override // com.miui.gallery.video.compress.VideoCompressDownloadStateListener
                public void onFinish(boolean z, int i) {
                    VideoCompressManager.this.mDownloadProgressDialog.dismiss();
                    if (z) {
                        if (VideoCompressManager.this.mVideoCompressHelper == null) {
                            VideoCompressManager.this.mVideoCompressHelper = new VideoCompressHelper(VideoCompress.this.mContext);
                            VideoCompressManager.this.mVideoCompressHelper.setCompressCallback(VideoCompressManager.this.mProbeCallback);
                        }
                        if (TextUtils.isEmpty(VideoCompressManager.this.mPath)) {
                            return;
                        }
                        VideoCompressManager.this.mVideoCompressHelper.prepareVideoCompress(VideoCompressManager.this.mPath, VideoCompressManager.this.mSize);
                        return;
                    }
                    ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.video_compress_download_failed_for_notwork);
                }
            };
            GalleryActivity galleryActivity = r2.mContext;
            if (galleryActivity == null || (videoCompressSavingFragment = (VideoCompressSavingFragment) galleryActivity.getSupportFragmentManager().findFragmentByTag("VideoCompressSavingFragment")) == null) {
                return;
            }
            videoCompressSavingFragment.dismissSafely();
        }

        public void resume() {
            VideoCompressSavingFragment videoCompressSavingFragment = this.mSaveDialog;
            if (videoCompressSavingFragment == null || this.mIsShowing) {
                return;
            }
            videoCompressSavingFragment.dismissSafely();
        }

        public final boolean isNeedDownloadVideoCompress() {
            return !VideoCompressCheckHelper.isVideoCompressAvailable();
        }

        public final void downloadVideoCompressLib() {
            ProgressDialog progressDialog;
            VideoCompressCheckHelper.getInstance().setDownloadStateListener(this.mVideoCompressDownloadStateListener);
            VideoCompressCheckHelper.getInstance().startDownloadWithCheck(VideoCompress.this.mContext);
            if (!VideoCompressCheckHelper.getInstance().isDownloading() || (progressDialog = this.mDownloadProgressDialog) == null) {
                return;
            }
            progressDialog.show();
        }

        public void prepare(BaseDataItem baseDataItem) {
            this.mPath = baseDataItem.getOriginalPath();
            this.mSize = baseDataItem.getSize();
            if (isNeedDownloadVideoCompress()) {
                downloadVideoCompressLib();
                return;
            }
            if (this.mVideoCompressHelper == null) {
                VideoCompressHelper videoCompressHelper = new VideoCompressHelper(VideoCompress.this.mContext);
                this.mVideoCompressHelper = videoCompressHelper;
                videoCompressHelper.setCompressCallback(this.mProbeCallback);
            }
            if (TextUtils.isEmpty(this.mPath)) {
                return;
            }
            this.mVideoCompressHelper.prepareVideoCompress(this.mPath, this.mSize);
        }

        public final void initDialog() {
            int i = this.mProbeResult;
            if (i == 0) {
                if (this.mCompressDialog == null) {
                    this.mCompressDialog = new VideoCompressDialogFragment();
                }
                this.mCompressDialog.setOnCompressListener(this.mCompressListener);
                this.mCompressDialog.setSubTitle(getSubTitle());
                this.mCompressDialog.setDatas(this.mDatas);
                this.mCompressDialog.setVideoSize(this.mSize);
                if (!this.mCompressDialog.isAdded()) {
                    this.mCompressDialog.showAllowingStateLoss(VideoCompress.this.mContext.getSupportFragmentManager(), "VideoCompressDialogFragment");
                }
            } else if (i == 1) {
                ToastUtils.makeText(VideoCompress.this.mContext, (int) R.string.video_compress_not_with_format);
            } else if (i == 2) {
                GalleryActivity galleryActivity = VideoCompress.this.mContext;
                ToastUtils.makeText(galleryActivity, galleryActivity.getString(R.string.video_compress_not_with_8k, new Object[]{8}));
            } else if (i == 3) {
                ToastUtils.makeText(VideoCompress.this.mContext, (int) R.string.video_compress_not_with_low_quality);
            }
            HashMap hashMap = new HashMap();
            hashMap.put("resolution", String.format(Locale.US, "%dx%d", Integer.valueOf(this.mVideoCompressHelper.getVideoWidth()), Integer.valueOf(this.mVideoCompressHelper.getVideoHeight())));
            SamplingStatHelper.recordCountEvent("video", "video_compress_before", hashMap);
        }

        public void onDestroy() {
            VideoCompressSavingFragment videoCompressSavingFragment = this.mSaveDialog;
            if (videoCompressSavingFragment != null) {
                videoCompressSavingFragment.cancelCompress();
                this.mSaveDialog.dismissSafely();
            }
            ProgressDialog progressDialog = this.mDownloadProgressDialog;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            VideoCompressDialogFragment videoCompressDialogFragment = this.mCompressDialog;
            if (videoCompressDialogFragment != null) {
                videoCompressDialogFragment.dismissSafely();
            }
            VideoCompressHelper videoCompressHelper = this.mVideoCompressHelper;
            if (videoCompressHelper != null) {
                videoCompressHelper.release();
            }
            VideoCompressCheckHelper.getInstance().release();
        }

        public final String getSubTitle() {
            return this.mVideoCompressHelper.initSubTitle(VideoCompress.this.mContext, this.mSize);
        }

        /* renamed from: com.miui.gallery.ui.photoPage.bars.menuitem.VideoCompress$VideoCompressManager$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements VideoCompressDialogFragment.OnCompressListener {
            /* renamed from: $r8$lambda$dHPfHk5nD9bQBa8pFc39cQBh-tA */
            public static /* synthetic */ void m1636$r8$lambda$dHPfHk5nD9bQBa8pFc39cQBhtA(AnonymousClass1 anonymousClass1) {
                anonymousClass1.lambda$onResolutionSelect$0();
            }

            public AnonymousClass1() {
                VideoCompressManager.this = r1;
            }

            @Override // com.miui.gallery.ui.VideoCompressDialogFragment.OnCompressListener
            public void onResolutionSelect(int i, int i2) {
                VideoCompressManager videoCompressManager = VideoCompressManager.this;
                if (!FilePermissionUtils.checkFileCreatePermissions(VideoCompress.this.mContext, videoCompressManager.mVideoCompressHelper.getTempFile(), VideoCompressManager.this.mVideoCompressHelper.getOutputFile())) {
                    return;
                }
                VideoCompressManager.this.mVideoCompressHelper.setCompressSize(i, i2);
                if (VideoCompressManager.this.mSaveDialog == null) {
                    VideoCompressManager.this.mSaveDialog = new VideoCompressSavingFragment();
                }
                VideoCompressManager.this.mSaveDialog.show(VideoCompress.this.mContext.getSupportFragmentManager(), new VideoCompressSavingFragment.SaveCancelListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.VideoCompress$VideoCompressManager$1$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.video.compress.VideoCompressSavingFragment.SaveCancelListener
                    public final void onCancelCompress() {
                        VideoCompress.VideoCompressManager.AnonymousClass1.m1636$r8$lambda$dHPfHk5nD9bQBa8pFc39cQBhtA(VideoCompress.VideoCompressManager.AnonymousClass1.this);
                    }
                });
                VideoCompressManager.this.mIsShowing = true;
                VideoCompressManager.this.mVideoCompressHelper.compressVideo();
            }

            public /* synthetic */ void lambda$onResolutionSelect$0() {
                VideoCompressManager.this.mVideoCompressHelper.cancelCompress();
            }
        }

        public void notifyDataSetChange(String str, boolean z) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            BaseDataSet dataSet = VideoCompress.this.mDataProvider.getFieldData().mCurrent.getDataSet();
            if (dataSet != null) {
                dataSet.addNewFile(str, VideoCompress.this.mDataProvider.getFieldData().mCurrent.getPosition() + 1);
            }
            VideoCompress.this.mDataProvider.getFieldData().mArguments.putString("photo_focused_path", str);
            if (z) {
                VideoCompress.this.mDataProvider.loadInBackground();
            } else {
                VideoCompress.this.mDataProvider.onContentChanged();
            }
        }
    }
}
