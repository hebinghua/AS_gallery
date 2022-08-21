package com.miui.gallery.editor.photo.app.crop.sdk;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.net.library.LibraryLoaderHelper;

/* loaded from: classes2.dex */
public class CropLibraryLoaderHelper extends LibraryLoaderHelper {
    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public long getLibraryId() {
        return 10000003L;
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

    public CropLibraryLoaderHelper() {
    }

    /* loaded from: classes2.dex */
    public static class CropLibraryLoaderHelperHolder {
        public static CropLibraryLoaderHelper INSTANCE = new CropLibraryLoaderHelper();
    }

    public static CropLibraryLoaderHelper getInstance() {
        return CropLibraryLoaderHelperHolder.INSTANCE;
    }
}
