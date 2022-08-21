package com.miui.gallery.util;

import android.app.Activity;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.magic.fetch.VideoRequest;
import com.miui.gallery.magic.fetch.VideoResourceFetcher;
import com.miui.gallery.net.fetch.FetchManager;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.ui.NetworkConsider;

/* loaded from: classes2.dex */
public class VideoPostDownloadManager {
    public static VideoPostDownloadManager sInstance;
    public DownloadStateListener mDownloadStateListener;
    public boolean mDownloadFailed = false;
    public LibraryLoaderHelper.DownloadStateListener mVideoPostDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.util.VideoPostDownloadManager.2
        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onDownloading() {
            if (VideoPostDownloadManager.this.mDownloadStateListener != null) {
                VideoPostDownloadManager.this.mDownloadStateListener.onDownloading();
            }
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onFinish(boolean z, int i) {
            VideoPostDownloadManager.this.downloadFinish(z);
        }
    };
    public Request.Listener mGuideVideoListener = new Request.Listener() { // from class: com.miui.gallery.util.VideoPostDownloadManager.3
        @Override // com.miui.gallery.net.fetch.Request.Listener
        public void onStart() {
            if (VideoPostDownloadManager.this.mDownloadStateListener != null) {
                VideoPostDownloadManager.this.mDownloadStateListener.onDownloading();
            }
        }

        @Override // com.miui.gallery.net.fetch.Request.Listener
        public void onSuccess() {
            VideoPostDownloadManager.this.downloadFinish(true);
        }

        @Override // com.miui.gallery.net.fetch.Request.Listener
        public void onFail() {
            VideoPostDownloadManager.this.downloadFinish(false);
        }
    };

    /* loaded from: classes2.dex */
    public interface DownloadStateListener {
        void onDownloading();

        void onFinish(boolean z);
    }

    public static VideoPostDownloadManager getInstance() {
        if (sInstance == null) {
            sInstance = new VideoPostDownloadManager();
        }
        return sInstance;
    }

    public boolean checkAbleOrDownload(FragmentActivity fragmentActivity) {
        if (!VlogLibraryLoaderHelper.getInstance().checkHasDownload() || !VideoPostLibraryLoaderHelper.getInstance().checkHasDownload() || !VideoResourceFetcher.INSTANCE.isExistGuideVideo()) {
            startDownloadWithCheck(fragmentActivity, null);
            return false;
        }
        return true;
    }

    public final void startDownloadWithCheck(FragmentActivity fragmentActivity, LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        if (!BaseNetworkUtils.isNetworkConnected() || !BaseGalleryPreferences.CTA.canConnectNetwork()) {
            showNoNetworkToast();
            DownloadStateListener downloadStateListener = this.mDownloadStateListener;
            if (downloadStateListener == null) {
                return;
            }
            downloadStateListener.onFinish(false);
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            showConfirmDialog(fragmentActivity, downloadStartListener);
        } else {
            startDownload(downloadStartListener);
        }
    }

    public final void startDownload(LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        if (!VlogLibraryLoaderHelper.getInstance().checkHasDownload()) {
            VlogLibraryLoaderHelper.getInstance().startDownloadVlogWithCheck(false, new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.util.VideoPostDownloadManager.1
                @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                public void onDownloading() {
                    if (VideoPostDownloadManager.this.mDownloadStateListener != null) {
                        VideoPostDownloadManager.this.mDownloadStateListener.onDownloading();
                    }
                }

                @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                public void onFinish(boolean z, int i) {
                    VideoPostDownloadManager.this.downloadFinish(z);
                }
            });
        }
        VideoPostLibraryLoaderHelper.getInstance().startDownloadLibrary(false, downloadStartListener);
        fetchGuideVideo();
    }

    public void showNoNetworkToast() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.video_post_download_failed_for_notwork);
    }

    public void showConfirmDialog(Activity activity, final LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        NetworkConsider.consider(activity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.util.VideoPostDownloadManager$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                VideoPostDownloadManager.this.lambda$showConfirmDialog$0(downloadStartListener, z, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showConfirmDialog$0(LibraryLoaderHelper.DownloadStartListener downloadStartListener, boolean z, boolean z2) {
        if (z) {
            VideoPostLibraryLoaderHelper.getInstance().startDownloadLibrary(true, downloadStartListener);
            fetchGuideVideo();
            return;
        }
        DownloadStateListener downloadStateListener = this.mDownloadStateListener;
        if (downloadStateListener == null) {
            return;
        }
        downloadStateListener.onFinish(false);
    }

    public final void fetchGuideVideo() {
        VideoRequest videoRequest = new VideoRequest(VideoResourceFetcher.GUIDE_VIDEO_KEY, VideoResourceFetcher.sResIdMap.get(VideoResourceFetcher.GUIDE_VIDEO_KEY).longValue());
        videoRequest.setListener(this.mGuideVideoListener);
        FetchManager.INSTANCE.enqueue(videoRequest);
    }

    public void setDownloadStateListener(DownloadStateListener downloadStateListener) {
        this.mDownloadStateListener = downloadStateListener;
        VideoPostLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mVideoPostDownloadStateListener);
    }

    public void removeDownloadStateListener() {
        this.mDownloadStateListener = null;
        VideoPostLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mVideoPostDownloadStateListener);
    }

    public final void downloadFinish(boolean z) {
        DownloadStateListener downloadStateListener = this.mDownloadStateListener;
        if (downloadStateListener != null) {
            if (!z) {
                downloadStateListener.onFinish(false);
                if (this.mDownloadFailed) {
                    return;
                }
                ToastUtils.makeText(StaticContext.sGetAndroidContext(), (int) R.string.photo_editor_common_download_failed_msg);
                this.mDownloadFailed = true;
            } else if (!VlogLibraryLoaderHelper.getInstance().checkHasDownload() || !VideoPostLibraryLoaderHelper.getInstance().checkHasDownload() || !VideoResourceFetcher.INSTANCE.isExistGuideVideo()) {
            } else {
                this.mDownloadStateListener.onFinish(true);
            }
        }
    }
}
