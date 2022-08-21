package com.miui.gallery.search;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.MapBackedSuggestionExtras;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class SearchConfig {
    public static volatile SearchConfig sConfig;
    public HistoryConfig mHistoryConfig;
    public NavigationConfig mNavigationConfig;
    public ResultConfig mResultConfig;
    public SuggestionConfig mSuggestionConfig;

    public SearchConfig() {
        init(GalleryApp.sGetAndroidContext());
    }

    public static SearchConfig get() {
        if (sConfig == null) {
            synchronized (SearchConfig.class) {
                if (sConfig == null) {
                    sConfig = new SearchConfig();
                }
            }
        }
        return sConfig;
    }

    public void init(Context context) {
        if (this.mNavigationConfig == null) {
            this.mNavigationConfig = new NavigationConfig(context);
        }
        if (this.mSuggestionConfig == null) {
            this.mSuggestionConfig = new SuggestionConfig(context);
        }
        if (this.mHistoryConfig == null) {
            this.mHistoryConfig = new HistoryConfig(context);
        }
        if (this.mResultConfig == null) {
            this.mResultConfig = new ResultConfig(context, null);
        }
    }

    public NavigationConfig getNavigationConfig() {
        NavigationConfig navigationConfig = this.mNavigationConfig;
        if (navigationConfig != null) {
            return navigationConfig;
        }
        throw new RuntimeException("SearchConfig haven't been initiated yet!");
    }

    public SuggestionConfig getSuggestionConfig() {
        return this.mSuggestionConfig;
    }

    public HistoryConfig getHistoryConfig() {
        return this.mHistoryConfig;
    }

    public ResultConfig getResultConfig() {
        return this.mResultConfig;
    }

    public boolean showSection(SearchConstants.SectionType sectionType) {
        return sectionType != SearchConstants.SectionType.SECTION_TYPE_PEOPLE || AIAlbumStatusHelper.isFaceAlbumEnabled();
    }

    /* renamed from: com.miui.gallery.search.SearchConfig$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType;

        static {
            int[] iArr = new int[SearchConstants.SectionType.values().length];
            $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType = iArr;
            try {
                iArr[SearchConstants.SectionType.SECTION_TYPE_TAG.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_LOCATION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_PEOPLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_ALBUM.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_RECOMMEND.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_SUGGESTION.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_OTHER.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_HISTORY.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_APP_SCREENSHOT.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_PHOTO_NAME.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_SECRET_ALBUM.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_GUIDE.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_NO_RESULT.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_WARNING.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_MEDIA_TYPE_FOLD.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
        }
    }

    public Suggestion getDefaultMoreItem(Context context, SearchConstants.SectionType sectionType, boolean z) {
        Uri uri;
        if (context == null || sectionType == null) {
            return null;
        }
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()];
        if (i == 1) {
            uri = GalleryContract.Search.URI_TAG_LIST_PAGE;
        } else if (i == 2) {
            uri = GalleryContract.Search.URI_LOCATION_LIST_PAGE;
        } else {
            uri = i != 3 ? null : GalleryContract.Common.URI_PEOPLE_LIST_PAGE;
        }
        if (uri == null) {
            return null;
        }
        BaseSuggestion baseSuggestion = new BaseSuggestion();
        baseSuggestion.setSuggestionTitle(context.getString(R.string.search_title_more));
        baseSuggestion.setIntentActionURI(uri.toString());
        baseSuggestion.setSuggestionExtras(new MapBackedSuggestionExtras("fixed", String.valueOf(z)));
        return baseSuggestion;
    }

    public BaseSuggestion genMapSuggestion(Context context, List<String> list) {
        BaseSuggestion baseSuggestion = new BaseSuggestion();
        baseSuggestion.setSuggestionTitle(context.getResources().getString(R.string.map_album));
        if (BaseMiscUtil.isValid(list)) {
            baseSuggestion.setSuggestionIcons(list);
        }
        baseSuggestion.setIntentActionURI(GalleryContract.Common.URI_MAP_ALNBUM.toString());
        return baseSuggestion;
    }

    public String getTitleForSection(SearchConstants.SectionType sectionType) {
        int i;
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()]) {
            case 1:
                i = R.string.search_title_tag;
                break;
            case 2:
                i = R.string.search_title_location;
                break;
            case 3:
                i = R.string.search_title_people;
                break;
            case 4:
                i = R.string.search_title_album;
                break;
            case 5:
                i = R.string.search_title_recommend;
                break;
            case 6:
                i = R.string.search_title_suggestion;
                break;
            case 7:
                i = R.string.search_title_others;
                break;
            case 8:
                i = R.string.search_title_history;
                break;
            case 9:
                i = R.string.search_title_app_screenshot;
                break;
            case 10:
                i = R.string.search_title_photo_name;
                break;
            case 11:
                i = R.string.secret_album_display_name;
                break;
            default:
                return null;
        }
        return GalleryApp.sGetAndroidContext().getString(i);
    }

    public ArrayList<String> getVoiceAssistantSuggestion(Context context) {
        String[] stringArray = context.getResources().getStringArray(R.array.search_voice_assistant_suggestion);
        if (stringArray.length > 0) {
            ArrayList<String> arrayList = new ArrayList<>(stringArray.length);
            for (String str : stringArray) {
                arrayList.add(str);
            }
            return arrayList;
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public static class SuggestionConfig {
        public Map<String, Map<String, String>> mQueryExtras = null;
        public boolean mShouldJumpWhenSingleSug;

        public SuggestionConfig(Context context) {
            this.mShouldJumpWhenSingleSug = false;
            this.mShouldJumpWhenSingleSug = context.getResources().getBoolean(R.bool.search_should_jump_when_single_suggestion);
        }

        public boolean supportShortcut(SearchConstants.SectionType sectionType) {
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()]) {
                case 12:
                case 13:
                case 14:
                    return false;
                default:
                    return true;
            }
        }

        public boolean countToRecall(SearchConstants.SectionType sectionType) {
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()]) {
                case 12:
                case 13:
                case 14:
                    return false;
                default:
                    return true;
            }
        }

        public boolean shouldDrawSectionHeader(SearchConstants.SectionType sectionType) {
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()];
            if (i != 6) {
                switch (i) {
                    case 12:
                    case 13:
                    case 14:
                        return true;
                    default:
                        return false;
                }
            }
            return true;
        }

        public void addQueryExtra(String str, String str2, String str3) {
            if (this.mQueryExtras == null) {
                this.mQueryExtras = new HashMap();
            }
            HashMap hashMap = new HashMap(2);
            hashMap.put("name", str);
            hashMap.put("peopleName", str2);
            hashMap.put("peopleId", str3);
            this.mQueryExtras.put(str, hashMap);
        }

        public String getQueryExtras(String str) {
            if (this.mQueryExtras != null && !TextUtils.isEmpty(str)) {
                ArrayList arrayList = new ArrayList();
                for (String str2 : this.mQueryExtras.keySet()) {
                    if (str.contains(str2)) {
                        arrayList.add(this.mQueryExtras.get(str2));
                    }
                }
                if (!arrayList.isEmpty()) {
                    HashMap hashMap = new HashMap(1);
                    hashMap.put("extraInfo", arrayList);
                    try {
                        return GsonUtils.toJson((Map) hashMap);
                    } catch (Exception unused) {
                        DefaultLogger.e("SearchConfig", "Failed to transform to json [%s]", arrayList);
                    }
                }
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class HistoryConfig {
        public int maxStoreCount;
        public int navigationReturnCount;

        public HistoryConfig(Context context) {
            this.maxStoreCount = context.getResources().getInteger(R.integer.search_history_max_store_count);
            this.navigationReturnCount = context.getResources().getInteger(R.integer.search_history_navigation_return_count);
        }

        public int getNavigationReturnCount() {
            return this.navigationReturnCount;
        }

        public String getTitle(Context context) {
            return context.getString(R.string.search_title_history);
        }

        public String getSubTitle(Context context) {
            return context.getString(R.string.search_clear_histories);
        }

        public SearchConstants.SectionType getSectionType() {
            return SearchConstants.SectionType.SECTION_TYPE_HISTORY;
        }

        public boolean shouldRecordHistory(String str) {
            return !TextUtils.isEmpty(str) && ("from_location_list".equals(str) || "from_tag_list".equals(str) || "from_suggestion".equals(str) || "from_navigation_history".equals(str) || "from_image_result_filter".equals(str));
        }
    }

    /* loaded from: classes2.dex */
    public static class ResultConfig {
        public int imageLoadCount;
        public int likelyImageLoadCount;
        public int tagLocationLoadCount;

        public /* synthetic */ ResultConfig(Context context, AnonymousClass1 anonymousClass1) {
            this(context);
        }

        public ResultConfig(Context context) {
            this.tagLocationLoadCount = context.getResources().getInteger(R.integer.search_result_tag_location_load_count);
            this.imageLoadCount = context.getResources().getInteger(R.integer.search_result_image_load_count);
            this.likelyImageLoadCount = context.getResources().getInteger(R.integer.search_likely_image_load_count);
        }

        public int getTagLocationLoadCount() {
            return this.tagLocationLoadCount;
        }

        public int getImageLoadCount() {
            return this.imageLoadCount;
        }

        public int getLikelyImageLoadCount() {
            return this.likelyImageLoadCount;
        }
    }

    /* loaded from: classes2.dex */
    public static class NavigationConfig {
        public int categoryItemCount;
        public int locationItemCount;
        public boolean mIsMultiWindow = false;
        public int peopleItemCount;
        public int recommendItemCount;

        public boolean isFatalCondition(int i) {
            if (i == 3 || i == 10) {
                return true;
            }
            switch (i) {
                case 12:
                case 13:
                case 14:
                    return true;
                default:
                    return false;
            }
        }

        public NavigationConfig(Context context) {
            refreshConfig(context);
        }

        public boolean checkConfig(AppCompatActivity appCompatActivity) {
            this.mIsMultiWindow = ActivityCompat.isInMultiWindowMode(appCompatActivity) && !BaseBuildUtil.isLargeHorizontalWindow();
            return refreshConfig(appCompatActivity);
        }

        public boolean isMultiWindow() {
            return this.mIsMultiWindow;
        }

        public boolean refreshConfig(Context context) {
            int i = this.recommendItemCount;
            int i2 = this.peopleItemCount;
            int i3 = this.locationItemCount;
            int i4 = this.categoryItemCount;
            this.recommendItemCount = context.getResources().getInteger(R.integer.search_navigation_recommend_item_count);
            this.peopleItemCount = context.getResources().getInteger(R.integer.search_navigation_people_item_count);
            this.locationItemCount = context.getResources().getInteger(R.integer.search_navigation_location_item_count);
            int integer = context.getResources().getInteger(R.integer.search_navigation_category_item_count);
            this.categoryItemCount = integer;
            return (i == this.recommendItemCount && i2 == this.peopleItemCount && i3 == this.locationItemCount && i4 == integer) ? false : true;
        }

        public int getSectionMaxItemCount(SearchConstants.SectionType sectionType) {
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()];
            if (i != 1) {
                if (i == 2) {
                    return this.locationItemCount;
                }
                if (i == 3) {
                    return this.peopleItemCount;
                }
                if (i == 5) {
                    return this.recommendItemCount;
                }
                return Integer.MAX_VALUE;
            }
            return this.categoryItemCount;
        }

        public boolean useBatchContent(SearchConstants.SectionType sectionType) {
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()];
            return i == 1 || i == 2 || i == 3 || i == 5 || i == 8 || i == 15;
        }

        public boolean isNeedAppendMore(SuggestionSection suggestionSection) {
            if (suggestionSection == null || suggestionSection.getCount() == 0) {
                return false;
            }
            SearchConstants.SectionType sectionType = suggestionSection.getSectionType();
            return (sectionType == SearchConstants.SectionType.SECTION_TYPE_HISTORY && suggestionSection.getCount() > 0) || suggestionSection.getCount() > getSectionMaxItemCount(sectionType);
        }
    }
}
