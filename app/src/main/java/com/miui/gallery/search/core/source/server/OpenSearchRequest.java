package com.miui.gallery.search.core.source.server;

import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.search.core.source.server.ServerSearchRequest;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.utils.SearchUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.PrivacyAgreementUtils;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class OpenSearchRequest {
    public static int request() {
        String xiaomiId = SearchUtils.getXiaomiId();
        if (TextUtils.isEmpty(xiaomiId)) {
            return 3;
        }
        if (!PrivacyAgreementUtils.isCloudServiceAgreementEnable(GalleryApp.sGetAndroidContext())) {
            return 4;
        }
        if (!BaseNetworkUtils.isNetworkConnected()) {
            return 1;
        }
        try {
            Object[] executeSync = new ServerSearchRequest.Builder().setMethod(1001).setUserPath(ServerSearchRequest.Builder.getDefaultUserPath(xiaomiId)).setUserId(xiaomiId).setQueryAppendPath("hint").setUseCache(false).build().executeSync();
            if (executeSync != null && executeSync.length > 0) {
                SamplingStatHelper.recordCountEvent(MiStat.Event.SEARCH, "request_open_search_succeeded");
                return 0;
            }
        } catch (Exception e) {
            SearchLog.e("OpenSearchRequest", e);
        }
        SamplingStatHelper.recordCountEvent(MiStat.Event.SEARCH, "request_open_search_failed");
        return 9;
    }
}
