package com.miui.gallery.editor.photo.app.remover2.sdk;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.ToastUtils;

/* loaded from: classes2.dex */
public class Remover2LibraryLoaderHelper extends LibraryLoaderHelper {
    public static final Remover2LibraryLoaderHelper INSTANCE = new Remover2LibraryLoaderHelper();

    /* renamed from: $r8$lambda$OrSe-v1z6JvXdjZYP3djL2jDNjI */
    public static /* synthetic */ void m770$r8$lambda$OrSev1z6JvXdjZYP3djL2jDNjI(Remover2LibraryLoaderHelper remover2LibraryLoaderHelper, Library library, LibraryLoaderHelper.DownloadStartListener downloadStartListener, boolean z, boolean z2) {
        remover2LibraryLoaderHelper.lambda$showConfirmDownloadDialog$0(library, downloadStartListener, z, z2);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public long getLibraryId() {
        return 7000106L;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showDownloadResultToast(boolean z) {
        if (!z) {
            ToastUtils.makeText(StaticContext.sGetAndroidContext(), (int) R.string.photo_editor_common_download_failed_msg);
        }
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showNoNetworkToast() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.photo_editor_remover2_download_failed);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showConfirmDownloadDialog(FragmentActivity fragmentActivity, final Library library, final LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.editor.photo.app.remover2.sdk.Remover2LibraryLoaderHelper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                Remover2LibraryLoaderHelper.m770$r8$lambda$OrSev1z6JvXdjZYP3djL2jDNjI(Remover2LibraryLoaderHelper.this, library, downloadStartListener, z, z2);
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
