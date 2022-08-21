package com.miui.gallery.search.core.source.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.SearchContract;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.Scheme;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class PhotoNameSource extends LocalSingleSectionSuggestionSource {
    public static final Uri CONTENT_URI = GalleryContract.Media.URI;
    public static final String[] PROJECTION = {j.c, "alias_clear_thumbnail"};

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "local_photo_name_source";
    }

    public PhotoNameSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.local.LocalSingleSectionSuggestionSource
    public List<Suggestion> querySuggestion(final String str, QueryInfo queryInfo) {
        return (List) SafeDBUtil.safeQuery(this.mContext, CONTENT_URI, PROJECTION, "title LIKE ?", new String[]{str}, "alias_create_time DESC", new SafeDBUtil.QueryHandler<List<Suggestion>>() { // from class: com.miui.gallery.search.core.source.local.PhotoNameSource.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<Suggestion> mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.add(PhotoNameSource.this.createPhotoNameSuggestion(str, cursor));
                return arrayList;
            }
        });
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        return super.isFatalCondition(queryInfo, i) || i == 14;
    }

    @Override // com.miui.gallery.search.core.source.local.LocalSingleSectionSuggestionSource
    public SearchConstants.SectionType getSectionType() {
        return SearchConstants.SectionType.SECTION_TYPE_PHOTO_NAME;
    }

    public final Suggestion createPhotoNameSuggestion(String str, Cursor cursor) {
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
        baseSuggestion.setIntentActionURI(GalleryContract.Common.URI_ALBUM_PAGE.buildUpon().appendQueryParameter("id", String.valueOf(2147383646)).appendQueryParameter("query", str).toString());
        return baseSuggestion;
    }
}
