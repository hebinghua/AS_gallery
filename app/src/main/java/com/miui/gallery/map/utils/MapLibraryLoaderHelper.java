package com.miui.gallery.map.utils;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class MapLibraryLoaderHelper extends LibraryLoaderHelper {
    public static LibraryLoaderHelper sInstance;

    public static /* synthetic */ void $r8$lambda$7BEgrKcdM3hoYrrDZBuASaieOxk(MapLibraryLoaderHelper mapLibraryLoaderHelper, Library library, boolean z, boolean z2) {
        mapLibraryLoaderHelper.lambda$showConfirmLoadDialog$0(library, z, z2);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public long getLibraryId() {
        return 104702L;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showConfirmDownloadDialog(FragmentActivity fragmentActivity, Library library, LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
    }

    public static LibraryLoaderHelper getInstance() {
        if (sInstance == null) {
            sInstance = new MapLibraryLoaderHelper();
        }
        return sInstance;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showDownloadResultToast(boolean z) {
        if (!z) {
            MapStatHelper.trackViewMapError(LibraryLoaderHelper.Error.DOWNLOAD_LIBRARY_FAIL);
        }
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showNoNetworkToast() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.map_common_download_failed_for_network_msg);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showLoadResultToast(boolean z, LibraryLoaderHelper.Error error) {
        if (!z || !MapInitializerImpl.checkInitialized()) {
            if (error != LibraryLoaderHelper.Error.NO_WLAN) {
                ToastUtils.makeText(StaticContext.sGetAndroidContext(), error == LibraryLoaderHelper.Error.NO_NETWORK ? R.string.map_common_download_failed_for_network_msg : R.string.map_common_download_failed_msg);
            }
            if (z) {
                error = LibraryLoaderHelper.Error.INITIALIZE_LIBRARY_FAIL;
            }
            MapStatHelper.trackViewMapError(error);
        }
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showConfirmLoadDialog(FragmentActivity fragmentActivity, final Library library) {
        NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.map.utils.MapLibraryLoaderHelper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                MapLibraryLoaderHelper.$r8$lambda$7BEgrKcdM3hoYrrDZBuASaieOxk(MapLibraryLoaderHelper.this, library, z, z2);
            }
        });
    }

    public /* synthetic */ void lambda$showConfirmLoadDialog$0(Library library, boolean z, boolean z2) {
        if (z) {
            startLoadWithCheckLibrary(library, true);
        } else {
            refreshLoadLibraryResult(false, LibraryLoaderHelper.Error.NO_WLAN);
        }
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public boolean initLibrary(boolean z) {
        if (!z) {
            DefaultLogger.w("MapLibraryLoaderHelper", "load map library failed");
            return false;
        }
        return MapInitializerImpl.init();
    }
}
