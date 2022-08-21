package com.miui.gallery.search.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.classic.spi.CallerData;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.source.server.DataListSource;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexCrop;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class QueryTagCountHelper {
    public static final String[] PROJECTION = {"serverId", "serverStatus", "localFlag"};

    public static int querySecureCardCount() {
        QueryInfo build = new QueryInfo.Builder(SearchConstants.SearchType.SEARCH_TYPE_RESULT_LIST).addParam(nexExportFormat.TAG_FORMAT_TYPE, SearchConstants.SectionType.SECTION_TYPE_IMAGE_LIST.getName()).addParam("query_all", String.valueOf(true)).addParam("pos", String.valueOf(0)).addParam("use_persistent_response", String.valueOf(true)).build();
        QueryInfo build2 = new QueryInfo.Builder().cloneFrom(build).addParam("tagId", String.valueOf(100001)).build();
        QueryInfo build3 = new QueryInfo.Builder().cloneFrom(build).addParam("tagId", String.valueOf((int) nexCrop.ABSTRACT_DIMENSION)).build();
        DataListSource dataListSource = new DataListSource(GalleryApp.sGetAndroidContext());
        SourceResult mo1333getSuggestions = dataListSource.mo1333getSuggestions(build2);
        SourceResult mo1333getSuggestions2 = dataListSource.mo1333getSuggestions(build3);
        if ((mo1333getSuggestions.getData() instanceof ListSuggestionCursor) || (mo1333getSuggestions2.getData() instanceof ListSuggestionCursor)) {
            DefaultLogger.e("QueryTagCountHelper", "source result's data isn't ListSuggestionCursor");
            return -1;
        }
        ListSuggestionCursor listSuggestionCursor = (ListSuggestionCursor) mo1333getSuggestions.getData();
        ListSuggestionCursor listSuggestionCursor2 = (ListSuggestionCursor) mo1333getSuggestions2.getData();
        if (listSuggestionCursor == null && listSuggestionCursor2 == null) {
            return -1;
        }
        if (listSuggestionCursor != null) {
            listSuggestionCursor.add(listSuggestionCursor2);
        } else {
            listSuggestionCursor = listSuggestionCursor2;
        }
        return getValidCountFromList(listSuggestionCursor);
    }

    public static int getValidCountFromList(ListSuggestionCursor listSuggestionCursor) {
        int i = 0;
        if (listSuggestionCursor == null || listSuggestionCursor.getSuggestions() == null) {
            return 0;
        }
        List list = (List) listSuggestionCursor.getSuggestions().stream().map(new Function<Suggestion, String>() { // from class: com.miui.gallery.search.utils.QueryTagCountHelper.1
            @Override // java.util.function.Function
            public String apply(Suggestion suggestion) {
                return Uri.parse(suggestion.getSuggestionIcon()).getQueryParameter("serverId");
            }
        }).collect(Collectors.toList());
        int size = list.size();
        List<String> findInvalidPics = findInvalidPics(list);
        if (findInvalidPics != null) {
            i = findInvalidPics.size();
        }
        return size - i;
    }

    public static List<String> findInvalidPics(final List<String> list) {
        if (list == null) {
            return null;
        }
        List nCopies = Collections.nCopies(list.size(), CallerData.NA);
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Uri uri = GalleryContract.Cloud.CLOUD_URI;
        String[] strArr = PROJECTION;
        return (List) SafeDBUtil.safeQuery(sGetAndroidContext, uri, strArr, "serverId in (" + TextUtils.join(",", nCopies) + ")", (String[]) list.toArray(new String[list.size()]), (String) null, new SafeDBUtil.QueryHandler<List<String>>() { // from class: com.miui.gallery.search.utils.QueryTagCountHelper.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<String> mo1808handle(Cursor cursor) {
                return QueryTagCountHelper.getInvalidPicsFromCursor(cursor, list);
            }
        });
    }

    public static List<String> getInvalidPicsFromCursor(Cursor cursor, List<String> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null && !list.isEmpty()) {
            ArrayList arrayList2 = new ArrayList();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    arrayList2.add(cursor.getString(cursor.getColumnIndex("serverId")));
                    if (isInvalid(cursor)) {
                        arrayList.add(cursor.getString(cursor.getColumnIndex("serverId")));
                    }
                }
            }
            if (arrayList2.size() != list.size()) {
                ArrayList arrayList3 = new ArrayList(list);
                arrayList3.removeAll(arrayList2);
                arrayList.addAll(arrayList3);
            }
        }
        return arrayList;
    }

    public static boolean isInvalid(Cursor cursor) {
        int i = cursor.getInt(cursor.getColumnIndex("localFlag"));
        if (i == -1 || i == 2 || i == 11) {
            return true;
        }
        int columnIndex = cursor.getColumnIndex("serverStatus");
        if (cursor.isNull(columnIndex)) {
            return false;
        }
        String string = cursor.getString(columnIndex);
        return string.equals("deleted") || string.equals("purged");
    }
}
