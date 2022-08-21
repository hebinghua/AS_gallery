package com.miui.gallery.search.core.source.local;

import android.content.Context;
import android.net.Uri;
import com.miui.gallery.R;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.Suggestion;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SecretAlbumSource extends LocalSingleSectionSuggestionSource {
    public static final String[] HIT_WORDS = {"私", "隐", "私密", "私密相册", "隐私", "隐私相册", "秘密相册", "私密相册入口", "隐私相册入口", "隐藏相册", "隐私相", "秘相册", "私密相", "私密照片", "隐私照片"};

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "secret_album_source";
    }

    public SecretAlbumSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.local.LocalSingleSectionSuggestionSource
    public List<Suggestion> querySuggestion(String str, QueryInfo queryInfo) {
        for (String str2 : HIT_WORDS) {
            if (str2.equals(str)) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(createPhotoNameSuggestion(str));
                return arrayList;
            }
        }
        if (str.equalsIgnoreCase(this.mContext.getString(R.string.secret_album_display_name))) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(createPhotoNameSuggestion(str));
            return arrayList2;
        }
        return null;
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        return super.isFatalCondition(queryInfo, i) || i == 14;
    }

    @Override // com.miui.gallery.search.core.source.local.LocalSingleSectionSuggestionSource
    public SearchConstants.SectionType getSectionType() {
        return SearchConstants.SectionType.SECTION_TYPE_SECRET_ALBUM;
    }

    public final Suggestion createPhotoNameSuggestion(String str) {
        BaseSuggestion baseSuggestion = new BaseSuggestion();
        baseSuggestion.setSuggestionTitle(str);
        baseSuggestion.setSuggestionIcon(new Uri.Builder().scheme("android.resource").authority("com.miui.gallery").path(String.valueOf((int) R.drawable.secret_album_search)).build().toString());
        baseSuggestion.setIntentActionURI(GalleryContract.Common.URI_SECRET_ALBUM.buildUpon().appendQueryParameter("id", String.valueOf(-1000L)).appendQueryParameter("query", str).toString());
        return baseSuggestion;
    }
}
