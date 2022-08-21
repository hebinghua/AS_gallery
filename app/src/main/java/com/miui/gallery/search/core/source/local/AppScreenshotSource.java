package com.miui.gallery.search.core.source.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.SearchContract;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.Scheme;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/* loaded from: classes2.dex */
public class AppScreenshotSource extends LocalCacheBackedSuggestionSource<List<Pair<String, Suggestion>>> {
    public static final Uri CONTENT_URI = GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA;
    public static final String[] PROJECTION = {j.c, "alias_clear_thumbnail", "location"};

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "local_app_screenshot_source";
    }

    public AppScreenshotSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        return super.isFatalCondition(queryInfo, i) || i == 14;
    }

    @Override // com.miui.gallery.search.core.source.local.ContentCacheProvider
    /* renamed from: loadContent  reason: collision with other method in class */
    public List<Pair<String, Suggestion>> mo1330loadContent() {
        Long valueOf = Long.valueOf(AlbumDataHelper.queryScreenshotAlbumId(this.mContext));
        if (valueOf != null) {
            return (List) SafeDBUtil.safeQuery(this.mContext, CONTENT_URI, PROJECTION, "localGroupId = ? AND location NOT NULL", new String[]{valueOf.toString()}, "alias_create_time DESC", new SafeDBUtil.QueryHandler<List<Pair<String, Suggestion>>>() { // from class: com.miui.gallery.search.core.source.local.AppScreenshotSource.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public List<Pair<String, Suggestion>> mo1808handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToFirst()) {
                        return null;
                    }
                    ArrayList arrayList = new ArrayList();
                    HashSet hashSet = new HashSet();
                    do {
                        String string = cursor.getString(2);
                        if (!TextUtils.isEmpty(string) && !hashSet.contains(string)) {
                            arrayList.add(new Pair(string.toLowerCase(), AppScreenshotSource.this.createAppScreenshotSuggestion(string, cursor)));
                            hashSet.add(string);
                        }
                    } while (cursor.moveToNext());
                    return arrayList;
                }
            });
        }
        return null;
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
        return SearchConstants.SectionType.SECTION_TYPE_APP_SCREENSHOT;
    }

    public final Suggestion createAppScreenshotSuggestion(String str, Cursor cursor) {
        String wrap;
        BaseSuggestion baseSuggestion = new BaseSuggestion();
        baseSuggestion.setSuggestionTitle(str);
        String string = cursor.getString(1);
        if (TextUtils.isEmpty(string)) {
            wrap = SearchContract.Icon.LOCAL_IMAGE_URI.buildUpon().appendQueryParameter("id", cursor.getString(0)).build().toString();
        } else {
            wrap = Scheme.FILE.wrap(string);
        }
        baseSuggestion.setSuggestionIcon(wrap);
        baseSuggestion.setIntentActionURI(GalleryContract.Common.URI_ALBUM_PAGE.buildUpon().appendQueryParameter("serverId", String.valueOf(2L)).appendQueryParameter("screenshotAppName", str).toString());
        return baseSuggestion;
    }
}
