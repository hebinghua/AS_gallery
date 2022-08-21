package com.miui.gallery.util;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.net.library.LibraryLoaderHelper;

/* loaded from: classes2.dex */
public class VideoPostLibraryLoaderHelper extends LibraryLoaderHelper {
    public static VideoPostLibraryLoaderHelper sInstance;

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public long getLibraryId() {
        return 20040004L;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showConfirmDownloadDialog(FragmentActivity fragmentActivity, Library library, LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showDownloadResultToast(boolean z) {
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showNoNetworkToast() {
    }

    public static VideoPostLibraryLoaderHelper getInstance() {
        if (sInstance == null) {
            sInstance = new VideoPostLibraryLoaderHelper();
        }
        return sInstance;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public boolean checkAbleOrDownload(FragmentActivity fragmentActivity, LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        Library library = LibraryManager.getInstance().getLibrary(getLibraryId());
        return library != null && getLoaderState(library) == 0;
    }

    public void startDownloadLibrary(boolean z, LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        startDownloadWithCheckLibrary(LibraryManager.getInstance().getLibrary(getLibraryId()), z, downloadStartListener);
    }
}
