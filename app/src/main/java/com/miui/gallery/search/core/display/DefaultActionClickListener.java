package com.miui.gallery.search.core.display;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.history.SearchHistoryService;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.ActionURIHandler;

/* loaded from: classes2.dex */
public class DefaultActionClickListener implements OnActionClickListener {
    public Context mContext;

    public DefaultActionClickListener(Context context) {
        this.mContext = context;
    }

    @Override // com.miui.gallery.search.core.display.OnActionClickListener
    public void onClick(View view, int i, Object obj, Bundle bundle) {
        String extractSourceFromBundle = SearchStatUtils.extractSourceFromBundle(bundle);
        if (i != 0 && i != 1) {
            if (i == 2) {
                SearchHistoryService.clearHistory(this.mContext);
                SearchStatUtils.reportEvent(extractSourceFromBundle, "clear_search_history");
                return;
            } else if (i != 3) {
                return;
            } else {
                if (obj instanceof Suggestion) {
                    SearchHistoryService.removeHistory(this.mContext, (Suggestion) obj);
                    return;
                } else {
                    SearchLog.d("DefaultActionClickListener", "Cannot recognize data, remove failed.");
                    return;
                }
            }
        }
        String str = null;
        TimeMonitor.createNewTimeMonitor("403.50.0.1.14020");
        if (obj instanceof Suggestion) {
            Suggestion suggestion = (Suggestion) obj;
            recordHistoryIfNeeded(suggestion, extractSourceFromBundle);
            str = suggestion.getIntentActionURI();
        } else if (obj != null) {
            str = obj.toString();
        }
        try {
            handleUri((FragmentActivity) this.mContext, Uri.parse(str), bundle);
            if (!TextUtils.equals(str, GalleryContract.Common.URI_MAP_ALNBUM.toString())) {
                return;
            }
            TrackController.trackClick("403.48.0.1.15342");
        } catch (Exception unused) {
            String bundle2 = bundle == null ? "null" : bundle.toString();
            if (extractSourceFromBundle == null) {
                extractSourceFromBundle = "null";
            }
            SearchLog.w("DefaultActionClickListener", "Action uri parse failed for extra [%s] from %s", bundle2, extractSourceFromBundle);
        }
    }

    public void handleUri(FragmentActivity fragmentActivity, Uri uri, Bundle bundle) {
        ActionURIHandler.handleUri(fragmentActivity, uri, bundle);
    }

    public final void recordHistoryIfNeeded(Suggestion suggestion, String str) {
        if (SearchConfig.get().getHistoryConfig().shouldRecordHistory(str)) {
            SearchHistoryService.addHistory(this.mContext, suggestion);
        }
    }
}
