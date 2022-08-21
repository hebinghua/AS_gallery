package com.miui.gallery.vlog;

import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.library.LibraryManager;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes2.dex */
public class AiCaptionLibraryHelper {
    public static boolean isExist() {
        return LibraryManager.getInstance().isLibrarysExist(new Long[]{34567L});
    }

    public static boolean checkAndLoad() {
        Library librarySync = LibraryManager.getInstance().getLibrarySync(34567L);
        if (librarySync == null) {
            return false;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] zArr = new boolean[1];
        LibraryManager.getInstance().downloadLibrary(librarySync, true, new LibraryManager.DownloadListener() { // from class: com.miui.gallery.vlog.AiCaptionLibraryHelper.1
            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadProgress(long j, int i) {
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadResult(long j, int i) {
                countDownLatch.countDown();
                zArr[0] = i == 0;
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zArr[0];
    }
}
