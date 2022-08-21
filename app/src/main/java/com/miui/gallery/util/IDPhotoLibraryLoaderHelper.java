package com.miui.gallery.util;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.ui.NetworkConsider;

/* loaded from: classes2.dex */
public class IDPhotoLibraryLoaderHelper extends LibraryLoaderHelper {
    public static LibraryLoaderHelper sInstance;

    public static LibraryLoaderHelper getInstance() {
        if (sInstance == null) {
            sInstance = new IDPhotoLibraryLoaderHelper();
        }
        return sInstance;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public long getLibraryId() {
        return IDPhotoEntranceUtils.getLibraryId();
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showNoNetworkToast() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.id_photo_download_failed_for_notwork);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showConfirmDownloadDialog(FragmentActivity fragmentActivity, final Library library, final LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.util.IDPhotoLibraryLoaderHelper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                IDPhotoLibraryLoaderHelper.this.lambda$showConfirmDownloadDialog$0(library, downloadStartListener, z, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showConfirmDownloadDialog$0(Library library, LibraryLoaderHelper.DownloadStartListener downloadStartListener, boolean z, boolean z2) {
        if (z) {
            startDownloadWithCheckLibrary(library, true, downloadStartListener);
        } else {
            refreshDownloadResult(false, -1);
        }
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showDownloadResultToast(boolean z) {
        if (!z) {
            ToastUtils.makeText(StaticContext.sGetAndroidContext(), (int) R.string.photo_editor_common_download_failed_msg);
        }
    }
}
