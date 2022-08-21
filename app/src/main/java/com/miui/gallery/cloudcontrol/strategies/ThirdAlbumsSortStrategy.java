package com.miui.gallery.cloudcontrol.strategies;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.util.BaseMiscUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/* loaded from: classes.dex */
public class ThirdAlbumsSortStrategy extends BaseStrategy {
    public transient Map<String, Map<String, Integer>> mSortMap;
    @SerializedName("sorts")
    private List<SortItem> mSorts;

    public static /* synthetic */ Map $r8$lambda$QsmBph_dPLlMNbKAgurZ9wPwW88(List list, SortItem.Item item, String str) {
        return lambda$doAdditionalProcessing$0(list, item, str);
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        if (BaseMiscUtil.isValid(this.mSorts)) {
            this.mSortMap = new LinkedHashMap();
            for (SortItem sortItem : this.mSorts) {
                if (sortItem.getPaths() != null) {
                    final List<String> paths = sortItem.getPaths();
                    for (final SortItem.Item item : sortItem.getSorts()) {
                        if (this.mSortMap.containsKey(item.mLanguageCode)) {
                            for (String str : paths) {
                                this.mSortMap.get(item.mLanguageCode).put(str, Integer.valueOf(item.getSort()));
                            }
                        } else {
                            this.mSortMap.computeIfAbsent(item.mLanguageCode, new Function() { // from class: com.miui.gallery.cloudcontrol.strategies.ThirdAlbumsSortStrategy$$ExternalSyntheticLambda0
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    return ThirdAlbumsSortStrategy.$r8$lambda$QsmBph_dPLlMNbKAgurZ9wPwW88(paths, item, (String) obj);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public static /* synthetic */ Map lambda$doAdditionalProcessing$0(List list, SortItem.Item item, String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            linkedHashMap.put((String) it.next(), Integer.valueOf(item.getSort()));
        }
        return linkedHashMap;
    }

    public Map<String, Integer> getSorts() {
        Map<String, Integer> map = this.mSortMap.get(getCurrentLanguage());
        return map == null ? this.mSortMap.get("default") : map;
    }

    public final String getCurrentLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    /* loaded from: classes.dex */
    public class SortItem {
        @SerializedName(nexExportFormat.TAG_FORMAT_PATH)
        private List<String> mPaths;
        @SerializedName("sorts")
        private List<Item> mSorts;

        public List<String> getPaths() {
            return this.mPaths;
        }

        /* loaded from: classes.dex */
        public class Item {
            @SerializedName("language-code")
            private String mLanguageCode;
            @SerializedName("sort")
            private int mSort;

            public int getSort() {
                return this.mSort;
            }

            public String toString() {
                return "SortItem{mLanguageCode='" + this.mLanguageCode + CoreConstants.SINGLE_QUOTE_CHAR + ", mSort=" + this.mSort + '}';
            }
        }

        public List<Item> getSorts() {
            return this.mSorts;
        }
    }
}
