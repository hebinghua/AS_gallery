package com.miui.gallery.search.core.source.server;

import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.BaseSuggestionSection;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import com.miui.gallery.search.core.suggestion.MapBackedSuggestionExtras;
import com.miui.gallery.search.core.suggestion.RankInfo;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionExtras;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ConvertUtil {

    /* loaded from: classes2.dex */
    public interface Filter {
        boolean filter(ItemSuggestion itemSuggestion, String str);
    }

    public static Suggestion createSuggestion(ItemSuggestion itemSuggestion, Source source) {
        return createSuggestion(itemSuggestion, null, source);
    }

    public static Suggestion createSuggestion(ItemSuggestion itemSuggestion, Map<String, String> map, Source source) {
        if (itemSuggestion == null) {
            return createSuggestion(map, source);
        }
        Map<String, String> map2 = itemSuggestion.extraInfo;
        if (map2 != null) {
            if (map != null) {
                map2 = new HashMap<>();
                map2.putAll(itemSuggestion.extraInfo);
                map2.putAll(map);
            }
            map = map2;
        }
        String str = itemSuggestion.title;
        int i = itemSuggestion.resultCount;
        return new BaseSuggestion(str, i > 0 ? String.valueOf(i) : null, itemSuggestion.icon, itemSuggestion.intentActionURI, createSuggestionExtras(map), source, itemSuggestion.backupIcons);
    }

    public static Suggestion createSuggestion(Map<String, String> map, Source source) {
        SuggestionExtras createSuggestionExtras = createSuggestionExtras(map);
        if (createSuggestionExtras == null) {
            return null;
        }
        BaseSuggestion baseSuggestion = new BaseSuggestion();
        baseSuggestion.setSuggestionExtras(createSuggestionExtras);
        baseSuggestion.setSuggestionSource(source);
        return baseSuggestion;
    }

    public static SuggestionExtras createSuggestionExtras(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        return new MapBackedSuggestionExtras(map);
    }

    public static SuggestionCursor createSuggestionCursor(List<ItemSuggestion> list, Source source, QueryInfo queryInfo, String str, Filter filter) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (ItemSuggestion itemSuggestion : list) {
            if (filter == null || filter.filter(itemSuggestion, str)) {
                arrayList.add(createSuggestion(itemSuggestion, source));
            }
        }
        return createSuggestionCursor(arrayList, queryInfo);
    }

    public static SuggestionCursor createSuggestionCursor(List<Suggestion> list, QueryInfo queryInfo) {
        return new ListSuggestionCursor(queryInfo, list);
    }

    public static List<RankInfo> createRankInfos(List<ItemRankInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (ItemRankInfo itemRankInfo : list) {
            arrayList.add(createRankInfo(itemRankInfo));
        }
        return arrayList;
    }

    public static SuggestionSection createSuggestionSection(QueryInfo queryInfo, SectionedSuggestion sectionedSuggestion, Source source, Filter filter) {
        if (sectionedSuggestion == null) {
            return null;
        }
        String str = sectionedSuggestion.sectionType;
        int indexOf = str.indexOf("_");
        if (indexOf > 0) {
            str = str.substring(0, indexOf);
        }
        if ("recommand".equals(str)) {
            str = SearchConstants.SectionType.SECTION_TYPE_RECOMMEND.getName();
        }
        String str2 = str;
        return new BaseSuggestionSection(queryInfo, str2, createSuggestionCursor(sectionedSuggestion.items, source, queryInfo, str2, filter), sectionedSuggestion.dataUrl, sectionedSuggestion.title, null, createSuggestion(sectionedSuggestion.moreItem, source), createRankInfos(sectionedSuggestion.rankInfos), null);
    }

    public static RankInfo createRankInfo(ItemRankInfo itemRankInfo) {
        String str = null;
        if (itemRankInfo == null) {
            return null;
        }
        if ("ASC".equalsIgnoreCase(itemRankInfo.order)) {
            str = "ASC";
        } else if ("DESC".equalsIgnoreCase(itemRankInfo.order)) {
            str = "DESC";
        } else {
            String str2 = itemRankInfo.name;
            if (str2 != null) {
                str = SearchConstants.getDefaultOrder(str2);
            }
        }
        return new RankInfo(itemRankInfo.title, itemRankInfo.name, itemRankInfo.style, str);
    }
}
