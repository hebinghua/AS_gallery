package com.miui.gallery.editor.photo.app.sky.sdk;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.ToastUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class SkyLibraryLoaderHelper extends LibraryLoaderHelper {
    public static final SkyLibraryLoaderHelper INSTANCE = new SkyLibraryLoaderHelper();

    /* renamed from: $r8$lambda$4lYuid-hoK_oNAywhqGS5xmexx8 */
    public static /* synthetic */ void m773$r8$lambda$4lYuidhoK_oNAywhqGS5xmexx8(SkyLibraryLoaderHelper skyLibraryLoaderHelper, Library library, LibraryLoaderHelper.DownloadStartListener downloadStartListener, boolean z, boolean z2) {
        skyLibraryLoaderHelper.lambda$showConfirmDownloadDialog$0(library, downloadStartListener, z, z2);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public long getLibraryId() {
        return SkyCheckHelper.getLibraryId();
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showDownloadResultToast(boolean z) {
        if (!z) {
            ToastUtils.makeText(StaticContext.sGetAndroidContext(), (int) R.string.photo_editor_common_download_failed_msg);
        }
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showNoNetworkToast() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.filter_sky_download_failed);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showConfirmDownloadDialog(FragmentActivity fragmentActivity, final Library library, final LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.editor.photo.app.sky.sdk.SkyLibraryLoaderHelper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                SkyLibraryLoaderHelper.m773$r8$lambda$4lYuidhoK_oNAywhqGS5xmexx8(SkyLibraryLoaderHelper.this, library, downloadStartListener, z, z2);
            }
        });
    }

    public /* synthetic */ void lambda$showConfirmDownloadDialog$0(Library library, LibraryLoaderHelper.DownloadStartListener downloadStartListener, boolean z, boolean z2) {
        if (z) {
            startDownloadWithCheckLibrary(library, true, downloadStartListener);
            sampleClick("confirm");
            return;
        }
        refreshDownloadResult(false, -1);
        sampleClick("cancel");
    }

    public final void sampleClick(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        SamplingStatHelper.recordCountEvent("photo_editor", "sky_so_download_by_data", hashMap);
    }
}
