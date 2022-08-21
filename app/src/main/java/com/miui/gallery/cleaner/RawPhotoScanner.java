package com.miui.gallery.cleaner;

import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.stat.SamplingStatHelper;
import java.util.HashMap;

/* loaded from: classes.dex */
public class RawPhotoScanner extends BaseScanner {
    @Override // com.miui.gallery.cleaner.BaseScanner
    public String getSelection() {
        return "mimeType = 'image/x-adobe-dng' AND alias_hidden = 0 AND localGroupId != -1000";
    }

    public RawPhotoScanner() {
        super(4);
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public void recordClickScanResultEvent() {
        HashMap hashMap = new HashMap();
        hashMap.put("result", String.valueOf(this.mMediaItems.size()));
        SamplingStatHelper.recordCountEvent("cleaner", "cleaner_result_raw_click", hashMap);
        TrackController.trackClick("403.27.1.1.11316", AutoTracking.getRef());
    }
}
