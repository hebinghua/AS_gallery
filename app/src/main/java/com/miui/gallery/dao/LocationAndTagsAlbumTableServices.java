package com.miui.gallery.dao;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.query.QueryLoader;
import com.miui.gallery.search.resultpage.DataListResultProcessor;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes.dex */
public class LocationAndTagsAlbumTableServices {
    public static LocationAndTagsAlbumTableServices getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    public static class SingleTonHolder {
        public static final LocationAndTagsAlbumTableServices INSTANCE = new LocationAndTagsAlbumTableServices();
    }

    public LocationAndTagsAlbumTableServices() {
    }

    public QueryLoader buildLocationsLoader(Context context, int i) {
        return buildQueryLoaderByType(context, SearchConstants.SectionType.SECTION_TYPE_LOCATION_LIST, i);
    }

    public QueryLoader buildTagsLoader(Context context, int i) {
        return buildQueryLoaderByType(context, SearchConstants.SectionType.SECTION_TYPE_TAG_LIST, i);
    }

    public final QueryLoader buildQueryLoaderByType(Context context, SearchConstants.SectionType sectionType, int i) {
        QueryInfo.Builder builder = new QueryInfo.Builder(SearchConstants.SearchType.SEARCH_TYPE_RESULT_LIST);
        builder.addParam(nexExportFormat.TAG_FORMAT_TYPE, sectionType.getName());
        builder.addParam("pos", "0");
        builder.addParam("num", String.valueOf(i));
        builder.addParam("secureMode", String.valueOf(true));
        builder.addParam("use_persistent_response", String.valueOf(true));
        return new QueryLoader(context, builder.build(), new DataListResultProcessor());
    }

    public long parseAlbumCoverServerId(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                String queryParameter = Uri.parse(str).getQueryParameter("serverId");
                if (TextUtils.isEmpty(queryParameter)) {
                    return -1L;
                }
                return Long.parseLong(queryParameter);
            } catch (Exception unused) {
                DefaultLogger.e("LocationAndTagsAlbumTableServices", "Invalid album cover Uri: %s", str);
                return -1L;
            }
        }
        return -1L;
    }
}
