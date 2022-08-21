package com.miui.gallery.search.core.source.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.Pair;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.SearchContract;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.util.AlbumsCursorHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.Scheme;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumSource extends LocalCacheBackedSuggestionSource<List<Pair<String, Suggestion>>> {
    public static final Uri CONTENT_URI = GalleryContract.Album.URI_QUERY_ALL_AND_EXCEPT_RUBBISH;
    public final String[] PROJECTION;
    public final String[] SHARE_PROJECTION;

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "local_album_source";
    }

    public AlbumSource(Context context) {
        super(context);
        this.PROJECTION = new String[]{j.c, "serverId", "name", "coverId", "coverPath"};
        this.SHARE_PROJECTION = new String[]{j.c, "creatorId"};
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        return super.isFatalCondition(queryInfo, i) || i == 14;
    }

    @Override // com.miui.gallery.search.core.source.local.ContentCacheProvider
    /* renamed from: loadContent  reason: collision with other method in class */
    public List<Pair<String, Suggestion>> mo1330loadContent() {
        return (List) SafeDBUtil.safeQuery(this.mContext, CONTENT_URI, this.PROJECTION, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Pair<String, Suggestion>>>() { // from class: com.miui.gallery.search.core.source.local.AlbumSource.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<Pair<String, Suggestion>> mo1808handle(Cursor cursor) {
                Suggestion createAlbumSuggestion;
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                LongSparseArray shareAlbumInfo = AlbumSource.this.getShareAlbumInfo();
                ArrayList arrayList = new ArrayList(cursor.getCount());
                do {
                    String albumName = AlbumsCursorHelper.getAlbumName(AlbumSource.this.mContext, cursor.getLong(0), cursor.getString(1), cursor.getString(2));
                    if (!TextUtils.isEmpty(albumName) && (createAlbumSuggestion = AlbumSource.this.createAlbumSuggestion(albumName, cursor, shareAlbumInfo)) != null) {
                        arrayList.add(new Pair(albumName.toLowerCase(), createAlbumSuggestion));
                    }
                } while (cursor.moveToNext());
                return arrayList;
            }
        });
    }

    public final LongSparseArray<String> getShareAlbumInfo() {
        return (LongSparseArray) SafeDBUtil.safeQuery(this.mContext, GalleryContract.Album.URI_SHARE_ALL, this.SHARE_PROJECTION, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<LongSparseArray<String>>() { // from class: com.miui.gallery.search.core.source.local.AlbumSource.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public LongSparseArray<String> mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                LongSparseArray<String> longSparseArray = new LongSparseArray<>();
                do {
                    if (cursor.getLong(0) >= 2147383647) {
                        longSparseArray.append(cursor.getLong(0), cursor.getString(1));
                    }
                } while (cursor.moveToNext());
                return longSparseArray;
            }
        });
    }

    @Override // com.miui.gallery.search.core.source.local.ContentCacheProvider
    public Uri getContentUri() {
        return CONTENT_URI;
    }

    @Override // com.miui.gallery.search.core.source.local.LocalCacheBackedSuggestionSource
    public List<Suggestion> handleQuery(List<Pair<String, Suggestion>> list, String str, QueryInfo queryInfo) {
        if (BaseMiscUtil.isValid(list)) {
            ArrayList arrayList = new ArrayList();
            for (Pair<String, Suggestion> pair : list) {
                if (((String) pair.first).contains(str)) {
                    arrayList.add((Suggestion) pair.second);
                }
            }
            return arrayList;
        }
        return null;
    }

    @Override // com.miui.gallery.search.core.source.local.LocalSingleSectionSuggestionSource
    public SearchConstants.SectionType getSectionType() {
        return SearchConstants.SectionType.SECTION_TYPE_ALBUM;
    }

    public final Suggestion createAlbumSuggestion(String str, Cursor cursor, LongSparseArray<String> longSparseArray) {
        long j = cursor.getLong(0);
        BaseSuggestion baseSuggestion = new BaseSuggestion();
        baseSuggestion.setSuggestionSource(this);
        baseSuggestion.setSuggestionTitle(str);
        Uri.Builder buildUpon = GalleryContract.Common.URI_ALBUM_PAGE.buildUpon();
        String string = cursor.getString(1);
        if (!TextUtils.isEmpty(string)) {
            buildUpon.appendQueryParameter("serverId", string);
            if (!TextUtils.isEmpty(longSparseArray.get(j))) {
                buildUpon.appendQueryParameter("creatorId", longSparseArray.get(j));
            }
        } else {
            buildUpon.appendQueryParameter("id", String.valueOf(j));
        }
        baseSuggestion.setIntentActionURI(buildUpon.build().toString());
        String string2 = cursor.getString(4);
        if (!TextUtils.isEmpty(string2)) {
            baseSuggestion.setSuggestionIcon(Scheme.FILE.wrap(string2));
        } else {
            long j2 = cursor.getLong(3);
            if (j2 >= 0) {
                baseSuggestion.setSuggestionIcon(SearchContract.Icon.LOCAL_IMAGE_URI.buildUpon().appendQueryParameter("id", String.valueOf(j2)).build().toString());
            }
        }
        return baseSuggestion;
    }
}
