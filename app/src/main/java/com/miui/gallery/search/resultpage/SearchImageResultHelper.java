package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSuggestionResult;
import com.miui.gallery.search.core.result.ErrorInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.resultprocessor.LinearResultProcessor;
import com.miui.gallery.search.core.resultprocessor.ResultProcessor;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.BaseSuggestionSection;
import com.miui.gallery.search.core.suggestion.CursorBackedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import com.miui.gallery.search.core.suggestion.MapBackedSuggestionExtras;
import com.miui.gallery.search.core.suggestion.RankInfo;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.resultpage.SearchResultHelper;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.StringUtils;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchImageResultHelper extends SearchResultHelper {
    public static final String[] OUT_PROJECTION;
    public static final String[] QUERY_PROJECTION;
    public static final String TITLE_SEPARATOR = GalleryApp.sGetAndroidContext().getString(R.string.name_split);
    public Context mContext;
    public boolean mSupportExpand;

    static {
        String[] strArr = {j.c, "alias_micro_thumbnail", "alias_create_date", "alias_create_time", "location", "sha1", "serverType", "duration", "mimeType", "alias_sync_state", "thumbnailFile", "localFile", "serverId", "alias_is_favorite", "specialTypeFlags", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "exifImageWidth", "exifImageLength"};
        QUERY_PROJECTION = strArr;
        OUT_PROJECTION = StringUtils.mergeStringArray(strArr, new String[]{"item_collapse_visibility"});
    }

    public SearchImageResultHelper(Context context, QueryInfo queryInfo, boolean z, SuggestionSection suggestionSection) {
        super(queryInfo, suggestionSection);
        this.mContext = context;
        this.mSupportExpand = z;
    }

    public SearchImageResultHelper(Context context, QueryInfo queryInfo, boolean z, GroupedSuggestionCursor<SuggestionSection> groupedSuggestionCursor) {
        super(queryInfo, groupedSuggestionCursor);
        this.mContext = context;
        this.mSupportExpand = z;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultHelper
    public ResultProcessor<SuggestionResult> createFilterProcessor() {
        return new FilterProcessor();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultHelper
    /* renamed from: createDataListResultProcessor  reason: collision with other method in class */
    public ImageResultProcessor mo1342createDataListResultProcessor(RankInfo rankInfo) {
        return new ImageResultProcessor(rankInfo);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultHelper
    public int getDataLoadCount(QueryInfo queryInfo) {
        if (queryInfo.getSearchType() == SearchConstants.SearchType.SEARCH_TYPE_FEEDBACK_LIKELY_RESULT) {
            return SearchConfig.get().getResultConfig().getLikelyImageLoadCount();
        }
        return SearchConfig.get().getResultConfig().getImageLoadCount();
    }

    /* loaded from: classes2.dex */
    public static class FilterProcessor extends LinearResultProcessor {
        public FilterProcessor() {
        }

        /* JADX WARN: Type inference failed for: r6v3, types: [com.miui.gallery.search.core.suggestion.SuggestionCursor, android.database.Cursor] */
        @Override // com.miui.gallery.search.core.resultprocessor.LinearResultProcessor, com.miui.gallery.search.core.resultprocessor.ResultProcessor
        public SuggestionResult getMergedResult(List<SourceResult> list) {
            BaseSuggestion suggestion;
            ErrorInfo mergedErrorInfo = getMergedErrorInfo(list);
            ArrayList<Suggestion> arrayList = new ArrayList();
            int i = 0;
            QueryInfo queryInfo = null;
            for (SourceResult sourceResult : list) {
                if (queryInfo == null && sourceResult.getQueryInfo() != null) {
                    queryInfo = sourceResult.getQueryInfo();
                }
                ?? data = sourceResult.getData();
                if (data != 0) {
                    for (int i2 = 0; i2 < data.getCount(); i2++) {
                        if (data.moveToPosition(i2) && (suggestion = toSuggestion(data.getCurrent())) != null) {
                            arrayList.add(suggestion);
                            i |= TextUtils.isEmpty(suggestion.getSuggestionIcon()) ? 1 : 0;
                        }
                    }
                }
            }
            if (i != 0) {
                for (Suggestion suggestion2 : arrayList) {
                    ((BaseSuggestion) suggestion2).setSuggestionIcon(null);
                }
            }
            ListSuggestionCursor listSuggestionCursor = new ListSuggestionCursor(queryInfo, arrayList);
            Bundle extras = listSuggestionCursor.getExtras();
            if (extras == Bundle.EMPTY) {
                extras = new Bundle();
            }
            extras.putInt("filter_style", i ^ 1);
            listSuggestionCursor.setExtras(extras);
            return new BaseSuggestionResult(queryInfo, listSuggestionCursor, mergedErrorInfo);
        }
    }

    /* loaded from: classes2.dex */
    public class ImageResultProcessor extends SearchResultHelper.DataListResultProcessor {
        public SparseBooleanArray mCachedGroupExpandState;

        public ImageResultProcessor(RankInfo rankInfo) {
            super(rankInfo);
            this.mCachedGroupExpandState = null;
        }

        @Override // com.miui.gallery.search.resultpage.SearchResultHelper.DataListResultProcessor
        public SuggestionResult createSuggestionResult(List<Suggestion> list, QueryInfo queryInfo, RankInfo rankInfo, ErrorInfo errorInfo) {
            ImageResultSuggestionCursor imageResultSuggestionCursor;
            long currentTimeMillis = System.currentTimeMillis();
            String suggestionsServerIdSelection = getSuggestionsServerIdSelection(list);
            if (!TextUtils.isEmpty(suggestionsServerIdSelection)) {
                if (!supportExpand(rankInfo)) {
                    imageResultSuggestionCursor = doSimpleQuery(list, queryInfo, rankInfo, suggestionsServerIdSelection);
                } else {
                    ArrayList<String> arrayList = new ArrayList<>();
                    ArrayList arrayList2 = new ArrayList();
                    ArrayList arrayList3 = new ArrayList();
                    doExpandableFirstStepQuery(suggestionsServerIdSelection, arrayList, arrayList2, arrayList3);
                    if (arrayList3.size() > 0) {
                        imageResultSuggestionCursor = doExpandableSecondStepQuery(queryInfo, rankInfo, arrayList3, arrayList, arrayList2);
                    }
                }
                SearchLog.d("ImageResultProcessor", "[%d]ms: query time for [%s]", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), queryInfo);
                return new BaseSuggestionResult(queryInfo, imageResultSuggestionCursor, errorInfo);
            }
            imageResultSuggestionCursor = null;
            SearchLog.d("ImageResultProcessor", "[%d]ms: query time for [%s]", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), queryInfo);
            return new BaseSuggestionResult(queryInfo, imageResultSuggestionCursor, errorInfo);
        }

        public ImageResultSuggestionCursor doSimpleQuery(List<Suggestion> list, QueryInfo queryInfo, RankInfo rankInfo, String str) {
            ImageResultSuggestionCursor doSimpleQueryWithDate;
            if (rankInfo != null && !"date".equals(rankInfo.getName())) {
                doSimpleQueryWithDate = doSimpleQueryWithRankValue(list, rankInfo, queryInfo, str);
            } else {
                doSimpleQueryWithDate = doSimpleQueryWithDate(queryInfo, rankInfo, str);
            }
            if (doSimpleQueryWithDate != null && rankInfo != null) {
                doSimpleQueryWithDate.setExtras(createRankInfoBundle(rankInfo));
            }
            return doSimpleQueryWithDate;
        }

        public ImageResultSuggestionCursor doSimpleQueryWithDate(QueryInfo queryInfo, RankInfo rankInfo, String str) {
            Cursor cursor = null;
            try {
                int i = 2;
                Object[] objArr = new Object[2];
                objArr[0] = "alias_create_time";
                objArr[1] = (rankInfo == null || rankInfo.getOrder() == null) ? SearchConstants.getDefaultOrder("date") : rankInfo.getOrder();
                Cursor query = SearchImageResultHelper.this.mContext.getContentResolver().query(getQueryUri(true), SearchImageResultHelper.QUERY_PROJECTION, appendNotInSecretSelection(str), null, String.format("%s %s", objArr));
                if (query != null) {
                    try {
                        if (query.getCount() > 0) {
                            ArrayList<String> stringArrayList = query.getExtras().getStringArrayList("extra_timeline_group_labels");
                            ArrayList<Integer> integerArrayList = query.getExtras().getIntegerArrayList("extra_timeline_group_start_pos");
                            ArrayList arrayList = new ArrayList(integerArrayList.size());
                            MatrixCursor matrixCursor = new MatrixCursor(SearchImageResultHelper.OUT_PROJECTION);
                            ArrayList arrayList2 = new ArrayList();
                            MatrixCursor matrixCursor2 = matrixCursor;
                            int i2 = 0;
                            while (i2 < query.getCount()) {
                                query.moveToPosition(i2);
                                arrayList2.add(Long.valueOf(query.getLong(0)));
                                matrixCursor2.addRow(toRow(query, MapBundleKey.MapObjKey.OBJ_SL_VISI));
                                int i3 = i2 + 1;
                                if (!integerArrayList.contains(Integer.valueOf(i3)) && i3 != query.getCount()) {
                                    i2 = i3;
                                }
                                ArrayList arrayList3 = arrayList2;
                                ArrayList arrayList4 = arrayList;
                                arrayList4.add(toSection(queryInfo, matrixCursor2, Long.valueOf(query.getLong(3)), Integer.valueOf(query.getInt(i)), stringArrayList.get((i3 == query.getCount() ? integerArrayList.size() : integerArrayList.indexOf(Integer.valueOf(i3))) - 1), null, null, null, arrayList3));
                                BaseMiscUtil.closeSilently(matrixCursor2);
                                matrixCursor2 = new MatrixCursor(SearchImageResultHelper.OUT_PROJECTION);
                                arrayList3.clear();
                                arrayList = arrayList4;
                                arrayList2 = arrayList3;
                                integerArrayList = integerArrayList;
                                i2 = i3;
                                i = 2;
                            }
                            BaseMiscUtil.closeSilently(matrixCursor2);
                            ImageResultSuggestionCursor imageResultSuggestionCursor = new ImageResultSuggestionCursor(queryInfo, arrayList, false);
                            imageResultSuggestionCursor.setNotificationUri(SearchImageResultHelper.this.mContext.getContentResolver(), query.getNotificationUri());
                            query.close();
                            return imageResultSuggestionCursor;
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor = query;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
            }
        }

        public ImageResultSuggestionCursor doSimpleQueryWithRankValue(List<Suggestion> list, RankInfo rankInfo, QueryInfo queryInfo, String str) {
            String str2;
            List list2;
            List<Long> list3;
            ArrayList arrayList;
            int i;
            ArrayList<String> arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList(list.size());
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            Iterator<Suggestion> it = list.iterator();
            while (true) {
                str2 = "#";
                if (!it.hasNext()) {
                    break;
                }
                Suggestion next = it.next();
                String suggestionExtra = getSuggestionExtra(next, "rankValue");
                if (!TextUtils.isEmpty(suggestionExtra)) {
                    str2 = suggestionExtra;
                }
                String suggestionExtra2 = getSuggestionExtra(next, "serverId");
                if (!TextUtils.isEmpty(suggestionExtra2)) {
                    if (!arrayList3.contains(suggestionExtra2)) {
                        arrayList3.add(suggestionExtra2);
                    }
                    if (!arrayList2.contains(str2)) {
                        arrayList2.add(str2);
                    }
                    hashMap.put(suggestionExtra2, str2);
                    String suggestionTitle = next.getSuggestionTitle();
                    if (!TextUtils.isEmpty(suggestionTitle)) {
                        hashMap2.put(suggestionExtra2, suggestionTitle);
                    }
                }
            }
            arrayList2.remove(str2);
            int i2 = 0;
            if ("DESC".equals(rankInfo.getOrder())) {
                arrayList2.add(0, str2);
            } else {
                arrayList2.add(str2);
            }
            HashMap hashMap3 = new HashMap();
            Cursor cursor = null;
            try {
                Cursor query = SearchImageResultHelper.this.mContext.getContentResolver().query(getQueryUri(false), SearchImageResultHelper.QUERY_PROJECTION, appendNotInSecretSelection(str), null, null);
                int i3 = 12;
                if (query != null) {
                    try {
                        if (query.getCount() > 0) {
                            for (int i4 = 0; i4 < query.getCount(); i4++) {
                                query.moveToPosition(i4);
                                String string = query.getString(12);
                                String str3 = (String) hashMap.get(string);
                                if (!TextUtils.isEmpty(str3)) {
                                    if (hashMap3.get(str3) == null) {
                                        hashMap3.put(str3, new SparseIntArray());
                                    }
                                    ((SparseIntArray) hashMap3.get(str3)).put(arrayList3.indexOf(string), i4);
                                }
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor = query;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
                if (hashMap3.size() <= 0) {
                    if (query != null) {
                        query.close();
                    }
                    return null;
                }
                ArrayList arrayList4 = new ArrayList(hashMap3.size());
                List<Long> arrayList5 = new ArrayList<>();
                List arrayList6 = new ArrayList();
                for (String str4 : arrayList2) {
                    SparseIntArray sparseIntArray = (SparseIntArray) hashMap3.get(str4);
                    if (sparseIntArray == null || sparseIntArray.size() <= 0) {
                        list2 = arrayList6;
                        list3 = arrayList5;
                        arrayList = arrayList4;
                        i = i3;
                    } else {
                        MatrixCursor matrixCursor = new MatrixCursor(SearchImageResultHelper.OUT_PROJECTION);
                        int i5 = i2;
                        while (i5 < sparseIntArray.size()) {
                            query.moveToPosition(sparseIntArray.valueAt(i5));
                            arrayList5.add(Long.valueOf(query.getLong(i2)));
                            String str5 = (String) hashMap2.get(query.getString(i3));
                            if (!TextUtils.isEmpty(str5) && !arrayList6.contains(str5)) {
                                arrayList6.add(str5);
                            }
                            matrixCursor.addRow(toRow(query, MapBundleKey.MapObjKey.OBJ_SL_VISI));
                            SearchLog.e("ImageResultProcessor", "On add image " + query.getString(i3));
                            i5++;
                            i2 = 0;
                        }
                        list2 = arrayList6;
                        list3 = arrayList5;
                        i = i3;
                        arrayList = arrayList4;
                        arrayList.add(toSection(queryInfo, matrixCursor, null, null, TextUtils.join(SearchImageResultHelper.TITLE_SEPARATOR, arrayList6), null, str4, null, list3));
                        list3.clear();
                        list2.clear();
                    }
                    arrayList4 = arrayList;
                    arrayList6 = list2;
                    arrayList5 = list3;
                    i3 = i;
                    i2 = 0;
                }
                ImageResultSuggestionCursor imageResultSuggestionCursor = new ImageResultSuggestionCursor(queryInfo, arrayList4, false);
                imageResultSuggestionCursor.setNotificationUri(SearchImageResultHelper.this.mContext.getContentResolver(), query.getNotificationUri());
                query.close();
                return imageResultSuggestionCursor;
            } catch (Throwable th2) {
                th = th2;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x006e A[Catch: all -> 0x009c, LOOP:1: B:12:0x0068->B:14:0x006e, LOOP_END, TRY_LEAVE, TryCatch #0 {all -> 0x009c, blocks: (B:3:0x000b, B:5:0x002b, B:7:0x0031, B:9:0x0050, B:11:0x0064, B:12:0x0068, B:14:0x006e), top: B:25:0x000b }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void doExpandableFirstStepQuery(java.lang.String r15, java.util.ArrayList<java.lang.String> r16, java.util.List<java.lang.Long> r17, java.util.List<java.lang.Integer> r18) {
            /*
                r14 = this;
                r1 = r14
                r2 = r15
                java.lang.String r3 = "[%d]ms: first step query time for [%s]"
                java.lang.String r4 = "ImageResultProcessor"
                long r5 = java.lang.System.currentTimeMillis()
                r7 = 0
                com.miui.gallery.search.resultpage.SearchImageResultHelper r0 = com.miui.gallery.search.resultpage.SearchImageResultHelper.this     // Catch: java.lang.Throwable -> L9c
                android.content.Context r0 = com.miui.gallery.search.resultpage.SearchImageResultHelper.access$200(r0)     // Catch: java.lang.Throwable -> L9c
                android.content.ContentResolver r8 = r0.getContentResolver()     // Catch: java.lang.Throwable -> L9c
                r0 = 1
                android.net.Uri r9 = r14.getQueryUri(r0)     // Catch: java.lang.Throwable -> L9c
                java.lang.String[] r10 = com.miui.gallery.search.resultpage.SearchImageResultHelper.access$100()     // Catch: java.lang.Throwable -> L9c
                java.lang.String r11 = r14.appendNotInSecretSelection(r15)     // Catch: java.lang.Throwable -> L9c
                r12 = 0
                java.lang.String r13 = "alias_create_time DESC "
                android.database.Cursor r7 = r8.query(r9, r10, r11, r12, r13)     // Catch: java.lang.Throwable -> L9c
                if (r7 == 0) goto L8a
                int r0 = r7.getCount()     // Catch: java.lang.Throwable -> L9c
                if (r0 <= 0) goto L8a
                android.os.Bundle r0 = r7.getExtras()     // Catch: java.lang.Throwable -> L9c
                java.lang.String r8 = "extra_timeline_group_labels"
                java.util.ArrayList r0 = r0.getStringArrayList(r8)     // Catch: java.lang.Throwable -> L9c
                r8 = r16
                r8.addAll(r0)     // Catch: java.lang.Throwable -> L9c
                android.os.Bundle r0 = r7.getExtras()     // Catch: java.lang.Throwable -> L9c
                java.lang.String r8 = "extra_timeline_group_start_pos"
                java.util.ArrayList r0 = r0.getIntegerArrayList(r8)     // Catch: java.lang.Throwable -> L9c
                boolean r8 = r7.moveToFirst()     // Catch: java.lang.Throwable -> L9c
                if (r8 == 0) goto L64
            L50:
                r8 = 0
                long r8 = r7.getLong(r8)     // Catch: java.lang.Throwable -> L9c
                java.lang.Long r8 = java.lang.Long.valueOf(r8)     // Catch: java.lang.Throwable -> L9c
                r9 = r17
                r9.add(r8)     // Catch: java.lang.Throwable -> L9c
                boolean r8 = r7.moveToNext()     // Catch: java.lang.Throwable -> L9c
                if (r8 != 0) goto L50
            L64:
                java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> L9c
            L68:
                boolean r8 = r0.hasNext()     // Catch: java.lang.Throwable -> L9c
                if (r8 == 0) goto L8a
                java.lang.Object r8 = r0.next()     // Catch: java.lang.Throwable -> L9c
                java.lang.Integer r8 = (java.lang.Integer) r8     // Catch: java.lang.Throwable -> L9c
                int r8 = r8.intValue()     // Catch: java.lang.Throwable -> L9c
                r7.moveToPosition(r8)     // Catch: java.lang.Throwable -> L9c
                r8 = 2
                int r8 = r7.getInt(r8)     // Catch: java.lang.Throwable -> L9c
                java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch: java.lang.Throwable -> L9c
                r9 = r18
                r9.add(r8)     // Catch: java.lang.Throwable -> L9c
                goto L68
            L8a:
                if (r7 == 0) goto L8f
                r7.close()
            L8f:
                long r7 = java.lang.System.currentTimeMillis()
                long r7 = r7 - r5
                java.lang.Long r0 = java.lang.Long.valueOf(r7)
                com.miui.gallery.search.utils.SearchLog.d(r4, r3, r0, r15)
                return
            L9c:
                r0 = move-exception
                if (r7 == 0) goto La2
                r7.close()
            La2:
                long r7 = java.lang.System.currentTimeMillis()
                long r7 = r7 - r5
                java.lang.Long r5 = java.lang.Long.valueOf(r7)
                com.miui.gallery.search.utils.SearchLog.d(r4, r3, r5, r15)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.search.resultpage.SearchImageResultHelper.ImageResultProcessor.doExpandableFirstStepQuery(java.lang.String, java.util.ArrayList, java.util.List, java.util.List):void");
        }

        /* JADX WARN: Removed duplicated region for block: B:62:0x01d5  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x01ec  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final com.miui.gallery.search.resultpage.ImageResultSuggestionCursor doExpandableSecondStepQuery(com.miui.gallery.search.core.QueryInfo r31, com.miui.gallery.search.core.suggestion.RankInfo r32, java.util.List<java.lang.Integer> r33, java.util.ArrayList<java.lang.String> r34, java.util.List<java.lang.Long> r35) {
            /*
                Method dump skipped, instructions count: 509
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.search.resultpage.SearchImageResultHelper.ImageResultProcessor.doExpandableSecondStepQuery(com.miui.gallery.search.core.QueryInfo, com.miui.gallery.search.core.suggestion.RankInfo, java.util.List, java.util.ArrayList, java.util.List):com.miui.gallery.search.resultpage.ImageResultSuggestionCursor");
        }

        public final Uri getQueryUri(boolean z) {
            return GalleryContract.Media.URI.buildUpon().appendQueryParameter("generate_headers", String.valueOf(z)).build();
        }

        public final String appendNotInSecretSelection(String str) {
            return String.format("(%s) AND (%s != %s)", str, "localGroupId", -1000L);
        }

        public final String getSuggestionsServerIdSelection(List<Suggestion> list) {
            StringBuilder sb = new StringBuilder();
            for (Suggestion suggestion : list) {
                String suggestionExtra = getSuggestionExtra(suggestion, "serverId");
                if (suggestionExtra != null) {
                    sb.append("'");
                    sb.append(suggestionExtra);
                    sb.append("',");
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
                sb.insert(0, "serverId IN (").append(")");
            }
            return sb.toString();
        }

        public final BaseSuggestionSection toSection(QueryInfo queryInfo, Cursor cursor, Long l, Integer num, String str, String str2, String str3, List<Long> list, List<Long> list2) {
            BaseSuggestionSection baseSuggestionSection = new BaseSuggestionSection(queryInfo, SearchConstants.SectionType.SECTION_TYPE_IMAGE_LIST.getName(), new CursorBackedSuggestionCursor(queryInfo, cursor));
            HashMap hashMap = new HashMap();
            hashMap.put("all_ids", TextUtils.join(",", list2));
            if (num != null) {
                hashMap.put("create_date", num.toString());
            }
            if (l != null) {
                hashMap.put("create_time", String.valueOf(l));
            }
            if (str != null) {
                hashMap.put("title", str);
            }
            if (str3 != null) {
                hashMap.put("rank_value", str3);
            }
            if (list != null) {
                hashMap.put("collapse_visible_ids", TextUtils.join(",", list));
            }
            if (str2 != null) {
                hashMap.put("expand_title", str2);
            }
            baseSuggestionSection.setSectionExtras(new MapBackedSuggestionExtras(hashMap));
            return baseSuggestionSection;
        }

        public final Object[] toRow(Cursor cursor, String str) {
            Object[] objArr = new Object[SearchImageResultHelper.OUT_PROJECTION.length];
            objArr[0] = Long.valueOf(cursor.getLong(0));
            objArr[1] = cursor.getString(1);
            objArr[2] = Integer.valueOf(cursor.getInt(2));
            objArr[3] = Long.valueOf(cursor.getLong(3));
            objArr[4] = cursor.getString(4);
            objArr[5] = cursor.getString(5);
            objArr[6] = Integer.valueOf(cursor.getInt(6));
            objArr[7] = Long.valueOf(cursor.getLong(7));
            objArr[8] = cursor.getString(8);
            objArr[9] = Integer.valueOf(cursor.getInt(9));
            objArr[10] = cursor.getString(10);
            objArr[11] = cursor.getString(11);
            objArr[12] = cursor.getString(12);
            objArr[13] = cursor.getString(13);
            objArr[14] = cursor.getString(14);
            objArr[15] = Long.valueOf(cursor.getLong(15));
            objArr[16] = Integer.valueOf(cursor.getInt(16));
            objArr[17] = Integer.valueOf(cursor.getInt(17));
            objArr[18] = str;
            return objArr;
        }

        @Override // com.miui.gallery.search.core.resultprocessor.ResultProcessor
        public BaseSuggestion toSuggestion(Suggestion suggestion) {
            BaseSuggestion suggestion2 = super.toSuggestion(suggestion);
            if (suggestion2 != null) {
                MapBackedSuggestionExtras mapBackedSuggestionExtras = new MapBackedSuggestionExtras("serverId", Uri.parse(suggestion2.getIntentActionURI()).getQueryParameter("serverId"));
                if (suggestion.getSuggestionExtras() != null && suggestion.getSuggestionExtras().getExtraColumnNames() != null) {
                    for (String str : suggestion.getSuggestionExtras().getExtraColumnNames()) {
                        mapBackedSuggestionExtras.putExtra(str, suggestion.getSuggestionExtras().getExtra(str));
                    }
                }
                suggestion2.setSuggestionExtras(mapBackedSuggestionExtras);
            }
            return suggestion2;
        }

        public boolean supportExpand(RankInfo rankInfo) {
            return SearchImageResultHelper.this.mSupportExpand && (rankInfo == null || "date".equals(rankInfo.getName()));
        }

        public String getSuggestionExtra(Suggestion suggestion, String str) {
            if (suggestion == null || suggestion.getSuggestionExtras() == null) {
                return null;
            }
            return suggestion.getSuggestionExtras().getExtra(str);
        }
    }
}
