package com.miui.gallery.search;

import com.miui.gallery.search.core.suggestion.RankInfo;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class SearchConstants {
    public static final int comparePriority(int i, int i2) {
        return i2 - i;
    }

    public static String getDefaultOrder() {
        return "ASC";
    }

    public static int getErrorStatusPriority(int i) {
        if (i == -1 || i == 0) {
            return 5;
        }
        if (i != 1 && i != 2) {
            if (i != 3) {
                switch (i) {
                    case 12:
                        break;
                    case 13:
                    case 14:
                        break;
                    default:
                        return 4;
                }
            }
            return 1;
        }
        return 2;
    }

    public static boolean isErrorStatus(int i) {
        return (i == 0 || i == -1) ? false : true;
    }

    public static boolean isNoAccountStatus(int i) {
        return i == 3;
    }

    /* loaded from: classes2.dex */
    public enum SearchType {
        SEARCH_TYPE_HINT("hint"),
        SEARCH_TYPE_HISTORY("history"),
        SEARCH_TYPE_NAVIGATION("navigation"),
        SEARCH_TYPE_SUGGESTION("suggestion"),
        SEARCH_TYPE_SEARCH(MiStat.Event.SEARCH),
        SEARCH_TYPE_RESULT("result"),
        SEARCH_TYPE_RESULT_LIST("list"),
        SEARCH_TYPE_FILTER("filter"),
        SEARCH_TYPE_FEEDBACK_LIKELY_RESULT("likelyResult");
        
        private final String name;

        SearchType(String str) {
            this.name = str;
        }
    }

    /* loaded from: classes2.dex */
    public enum SectionType {
        SECTION_TYPE_DEFAULT("default"),
        SECTION_TYPE_RECOMMEND("recommend"),
        SECTION_TYPE_PEOPLE("people"),
        SECTION_TYPE_ALBUM("album"),
        SECTION_TYPE_LOCATION("location"),
        SECTION_TYPE_TAG(nexExportFormat.TAG_FORMAT_TAG),
        SECTION_TYPE_FEATURE("feature"),
        SECTION_TYPE_LOCATION_LIST("locationList"),
        SECTION_TYPE_TAG_LIST("tagList"),
        SECTION_TYPE_IMAGE_LIST("imageList"),
        SECTION_TYPE_HISTORY("history"),
        SECTION_TYPE_WARNING("warning"),
        SECTION_TYPE_FILTER("facet"),
        SECTION_TYPE_OCR("ocr"),
        SECTION_TYPE_OTHER("other"),
        SECTION_TYPE_NO_RESULT("noResult"),
        SECTION_TYPE_GUIDE("guide"),
        SECTION_TYPE_SUGGESTION("suggestion"),
        SECTION_TYPE_APP_SCREENSHOT("appScreenshot"),
        SECTION_TYPE_PHOTO_NAME("photoname"),
        SECTION_TYPE_SECRET_ALBUM("secretAlbum"),
        SECTION_TYPE_MEDIA_TYPE_FOLD("mediatype");
        
        private final String name;

        SectionType(String str) {
            this.name = str;
        }

        public String getName() {
            return this.name;
        }

        public static SectionType fromName(String str) {
            SectionType[] values;
            for (SectionType sectionType : values()) {
                if (sectionType.name.equals(str)) {
                    return sectionType;
                }
            }
            return SECTION_TYPE_DEFAULT;
        }
    }

    public static int compareErrorStatus(int i, int i2) {
        return comparePriority(getErrorStatusPriority(i), getErrorStatusPriority(i2));
    }

    public static int getConvertedStyle(RankInfo rankInfo) {
        return (rankInfo == null || !"HD".equals(rankInfo.getStyle())) ? 0 : 1;
    }

    public static boolean isHorizontalDocumentStyle(RankInfo rankInfo) {
        return getConvertedStyle(rankInfo) == 1;
    }

    public static String getDefaultOrder(String str) {
        return "date".equalsIgnoreCase(str) ? "DESC" : getDefaultOrder();
    }
}
