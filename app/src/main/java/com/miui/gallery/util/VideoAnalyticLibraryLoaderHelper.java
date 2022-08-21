package com.miui.gallery.util;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.ui.NetworkConsider;

/* loaded from: classes2.dex */
public class VideoAnalyticLibraryLoaderHelper extends LibraryLoaderHelper {
    public static LibraryLoaderHelper sInstance;

    public static /* synthetic */ void $r8$lambda$NK6_EPnN5n1qkC9TIqXJqCIKMRQ(VideoAnalyticLibraryLoaderHelper videoAnalyticLibraryLoaderHelper, Library library, LibraryLoaderHelper.DownloadStartListener downloadStartListener, boolean z, boolean z2) {
        videoAnalyticLibraryLoaderHelper.lambda$showConfirmDownloadDialog$0(library, downloadStartListener, z, z2);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public long getLibraryId() {
        return 3414L;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showDownloadResultToast(boolean z) {
    }

    public static LibraryLoaderHelper getInstance() {
        if (sInstance == null) {
            sInstance = new VideoAnalyticLibraryLoaderHelper();
        }
        return sInstance;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showNoNetworkToast() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.video_analytic_download_failed_for_notwork);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showConfirmDownloadDialog(FragmentActivity fragmentActivity, final Library library, final LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.util.VideoAnalyticLibraryLoaderHelper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                VideoAnalyticLibraryLoaderHelper.$r8$lambda$NK6_EPnN5n1qkC9TIqXJqCIKMRQ(VideoAnalyticLibraryLoaderHelper.this, library, downloadStartListener, z, z2);
            }
        });
    }

    public /* synthetic */ void lambda$showConfirmDownloadDialog$0(Library library, LibraryLoaderHelper.DownloadStartListener downloadStartListener, boolean z, boolean z2) {
        if (z) {
            startDownloadWithCheckLibrary(library, true, downloadStartListener);
        } else {
            refreshDownloadResult(false, -1);
        }
    }
}
