package com.miui.gallery.cleaner;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.stat.SamplingStatHelper;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class ScreenshotScanner extends BaseScanner {
    public ScreenshotScanner() {
        super(1);
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public String getSelection() {
        long queryScreenshotsAlbumId = AlbumManager.queryScreenshotsAlbumId(GalleryApp.sGetAndroidContext());
        if (queryScreenshotsAlbumId != -1) {
            return String.format(Locale.US, "localGroupId = %d", Long.valueOf(queryScreenshotsAlbumId));
        }
        return null;
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public void recordClickScanResultEvent() {
        HashMap hashMap = new HashMap();
        hashMap.put("result", String.valueOf(this.mMediaItems.size()));
        SamplingStatHelper.recordCountEvent("cleaner", "cleaner_result_screenshot_click", hashMap);
        TrackController.trackClick("403.27.1.1.11315", AutoTracking.getRef());
    }
}
