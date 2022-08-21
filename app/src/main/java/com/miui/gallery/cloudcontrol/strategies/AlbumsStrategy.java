package com.miui.gallery.cloudcontrol.strategies;

import android.text.TextUtils;
import android.util.ArrayMap;
import ch.qos.logback.core.CoreConstants;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/* loaded from: classes.dex */
public class AlbumsStrategy extends BaseStrategy {
    public LazyValue<Void, ArrayMap<Pattern, Attributes>> mAlbumPatternMap = new LazyValue<Void, ArrayMap<Pattern, Attributes>>() { // from class: com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public ArrayMap<Pattern, Attributes> mo1272onInit(Void r5) {
            ArrayMap<Pattern, Attributes> arrayMap = new ArrayMap<>(AlbumsStrategy.this.mAlbumPatterns.size());
            for (AlbumPattern albumPattern : AlbumsStrategy.this.mAlbumPatterns) {
                if (!TextUtils.isEmpty(albumPattern.getPattern()) && albumPattern.getAttributes() != null) {
                    arrayMap.put(Pattern.compile(albumPattern.getPattern(), 2), albumPattern.getAttributes());
                }
            }
            return arrayMap;
        }
    };
    @SerializedName("patterns")
    private List<AlbumPattern> mAlbumPatterns;
    @SerializedName("albums")
    private List<Album> mAlbums;
    public transient HashMap<String, Album> mAlbumsMap;
    public transient ArrayList<String> mWhiteList;
    public transient ArrayList<Pattern> mWhiteListPatterns;

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        try {
            if (BaseMiscUtil.isValid(this.mAlbums)) {
                HashMap<String, Album> hashMap = this.mAlbumsMap;
                if (hashMap == null) {
                    this.mAlbumsMap = new HashMap<>();
                } else {
                    hashMap.clear();
                }
                ArrayList<String> arrayList = this.mWhiteList;
                if (arrayList == null) {
                    this.mWhiteList = new ArrayList<>();
                } else {
                    arrayList.clear();
                }
                for (Album album : this.mAlbums) {
                    if (album.getPath() != null) {
                        this.mAlbumsMap.put(album.getPath().toLowerCase(), album);
                        if (album.getAttributes() != null && album.getAttributes().isInWhiteList()) {
                            this.mWhiteList.add(album.getPath());
                        }
                    }
                }
            }
            if (!BaseMiscUtil.isValid(this.mAlbumPatterns)) {
                return;
            }
            ArrayList<Pattern> arrayList2 = this.mWhiteListPatterns;
            if (arrayList2 == null) {
                this.mWhiteListPatterns = new ArrayList<>();
            } else {
                arrayList2.clear();
            }
            for (AlbumPattern albumPattern : this.mAlbumPatterns) {
                if (!TextUtils.isEmpty(albumPattern.getPattern()) && albumPattern.getAttributes() != null && albumPattern.getAttributes().isInWhiteList()) {
                    this.mWhiteListPatterns.add(Pattern.compile(albumPattern.getPattern(), 2));
                }
            }
        } catch (PatternSyntaxException e) {
            DefaultLogger.e("AlbumsStrategy", e);
        }
    }

    public ArrayList<String> getAlbumsInWhiteList() {
        return this.mWhiteList;
    }

    public Album getAlbumByPath(String str) {
        HashMap<String, Album> hashMap;
        if (str == null || (hashMap = this.mAlbumsMap) == null || hashMap.size() <= 0) {
            return null;
        }
        return this.mAlbumsMap.get(str.toLowerCase());
    }

    public ArrayMap<Pattern, Attributes> getAlbumPatternsMap() {
        return this.mAlbumPatternMap.get(null);
    }

    public ArrayList<Pattern> getWhiteListPatterns() {
        return this.mWhiteListPatterns;
    }

    public Attributes getAlbumAttributesByPath(String str) {
        ArrayMap<Pattern, Attributes> albumPatternsMap;
        if (str != null && !GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH.equals(str) && (albumPatternsMap = getAlbumPatternsMap()) != null) {
            for (Map.Entry<Pattern, Attributes> entry : albumPatternsMap.entrySet()) {
                if (entry.getKey().matcher(str).find()) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public String toString() {
        return "AlbumsStrategy{mAlbums=" + this.mAlbums + ", mAlbumPatterns=" + this.mAlbumPatterns + '}';
    }

    /* loaded from: classes.dex */
    public static class Attributes {
        @SerializedName("auto-upload")
        private boolean mAutoUpload;
        @SerializedName("hide")
        private boolean mHide;
        @SerializedName("in-whitelist")
        private boolean mInWhiteList;
        @SerializedName("manual-rename-restricted")
        private boolean mManualRenameRestricted;
        @SerializedName("show-in-photos-tab")
        private boolean mShowInPhotosTab;

        public boolean isHide() {
            return this.mHide;
        }

        public boolean isAutoUpload() {
            return this.mAutoUpload;
        }

        public boolean isShowInPhotosTab() {
            return this.mShowInPhotosTab;
        }

        @Deprecated
        public boolean isInWhiteList() {
            return this.mInWhiteList;
        }

        public boolean isManualRenameRestricted() {
            return this.mManualRenameRestricted;
        }

        public String toString() {
            return "Attributes{mHide=" + this.mHide + ", mAutoUpload=" + this.mAutoUpload + ", mShowInPhotosTab=" + this.mShowInPhotosTab + ", mInWhiteList=" + this.mInWhiteList + ", mManualRenameRestricted=" + this.mManualRenameRestricted + '}';
        }

        public long checkAndFixAlbumAttributes(String str) {
            return checkAndFixAlbumAttributes(this, str);
        }

        public static long checkAndFixAlbumAttributes(Attributes attributes, String str) {
            boolean isHide = attributes.isHide();
            WhiteAlbumsStrategy whiteAlbumsStrategy = CloudControlStrategyHelper.getWhiteAlbumsStrategy();
            boolean z = whiteAlbumsStrategy != null && whiteAlbumsStrategy.isWhiteAlbum(str);
            if (z || !isHide) {
                long j = 0;
                long j2 = (attributes.isAutoUpload() ? 1L : 0L) | 0;
                if (attributes.isShowInPhotosTab()) {
                    j = 4;
                }
                long j3 = j2 | j;
                return z ? j3 : 64 | j3;
            }
            return 2048L;
        }
    }

    /* loaded from: classes.dex */
    public static class Album {
        @SerializedName("attributes")
        private Attributes mAttributes;
        @SerializedName("name-string-res")
        private String mNameStringRes;
        @SerializedName("names")
        private List<NameData> mNames;
        @SerializedName("package-name")
        private String mPackageName;
        @SerializedName(nexExportFormat.TAG_FORMAT_PATH)
        private String mPath;

        /* JADX WARN: Removed duplicated region for block: B:13:0x002f A[RETURN] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.String getBestName() {
            /*
                r8 = this;
                android.content.Context r0 = com.miui.gallery.GalleryApp.sGetAndroidContext()
                android.content.res.Resources r0 = r0.getResources()
                java.lang.String r1 = r8.mNameStringRes
                boolean r1 = android.text.TextUtils.isEmpty(r1)
                r2 = 0
                if (r1 != 0) goto L30
                java.lang.String r1 = r8.mNameStringRes
                java.lang.String r3 = "string"
                java.lang.String r4 = "com.miui.gallery"
                int r1 = r0.getIdentifier(r1, r3, r4)
                if (r1 == 0) goto L28
                java.lang.String r1 = r0.getString(r1)     // Catch: android.content.res.Resources.NotFoundException -> L22
                goto L29
            L22:
                r1 = move-exception
                java.lang.String r3 = "AlbumsStrategy"
                com.miui.gallery.util.logger.DefaultLogger.d(r3, r1)
            L28:
                r1 = r2
            L29:
                boolean r3 = android.text.TextUtils.isEmpty(r1)
                if (r3 != 0) goto L31
                return r1
            L30:
                r1 = r2
            L31:
                java.util.List<com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy$Album$NameData> r3 = r8.mNames
                if (r3 == 0) goto L8e
                java.lang.String r3 = r8.getLanguageCode()
                java.util.Locale r4 = java.util.Locale.ENGLISH
                java.lang.String r4 = r8.getLanguageCode(r4)
                android.content.res.Configuration r0 = r0.getConfiguration()
                java.util.Locale r0 = r0.locale
                java.lang.String r0 = r0.getLanguage()
                boolean r0 = android.text.TextUtils.equals(r4, r0)
                java.util.List<com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy$Album$NameData> r5 = r8.mNames
                java.util.Iterator r5 = r5.iterator()
            L53:
                boolean r6 = r5.hasNext()
                if (r6 == 0) goto L80
                java.lang.Object r6 = r5.next()
                com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy$Album$NameData r6 = (com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy.Album.NameData) r6
                java.lang.String r7 = r6.getLanguageCode()
                boolean r7 = r7.equals(r3)
                if (r7 == 0) goto L6f
                java.lang.String r0 = r6.getName()
                r1 = r0
                goto L80
            L6f:
                if (r0 == 0) goto L53
                java.lang.String r7 = r6.getLanguageCode()
                boolean r7 = r7.equals(r4)
                if (r7 == 0) goto L53
                java.lang.String r2 = r6.getName()
                goto L53
            L80:
                boolean r0 = android.text.TextUtils.isEmpty(r1)
                if (r0 != 0) goto L87
                return r1
            L87:
                boolean r0 = android.text.TextUtils.isEmpty(r2)
                if (r0 != 0) goto L8e
                return r2
            L8e:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy.Album.getBestName():java.lang.String");
        }

        public final String getLanguageCode() {
            return getLanguageCode(GalleryApp.sGetAndroidContext().getResources().getConfiguration().locale);
        }

        public final String getLanguageCode(Locale locale) {
            String language = locale.getLanguage();
            if (!TextUtils.isEmpty(locale.getCountry())) {
                return language + "_" + locale.getCountry();
            }
            return language;
        }

        public String getPath() {
            return this.mPath;
        }

        public Attributes getAttributes() {
            return this.mAttributes;
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public String toString() {
            return "AlbumsStrategy{mPath='" + this.mPath + CoreConstants.SINGLE_QUOTE_CHAR + ", mNames=" + this.mNames + ", mAttributes=" + this.mAttributes + ", mNameStringRes=" + this.mNameStringRes + '}';
        }

        /* loaded from: classes.dex */
        public static class NameData {
            @SerializedName("language-code")
            private String mLanguageCode;
            @SerializedName("name")
            private String mName;

            public String getLanguageCode() {
                return this.mLanguageCode;
            }

            public String getName() {
                return this.mName;
            }

            public String toString() {
                return "NameData{mLanguageCode='" + this.mLanguageCode + CoreConstants.SINGLE_QUOTE_CHAR + ", mName='" + this.mName + CoreConstants.SINGLE_QUOTE_CHAR + '}';
            }
        }
    }

    /* loaded from: classes.dex */
    public static class AlbumPattern {
        @SerializedName("attributes")
        private Attributes mAttributes;
        @SerializedName("pattern")
        private String mPattern;

        public String getPattern() {
            return this.mPattern;
        }

        public Attributes getAttributes() {
            return this.mAttributes;
        }

        public String toString() {
            return "AlbumPattern{mPattern='" + this.mPattern + CoreConstants.SINGLE_QUOTE_CHAR + ", mAttributes=" + this.mAttributes + '}';
        }
    }
}
