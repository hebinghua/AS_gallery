package com.miui.gallery.util;

import android.text.TextUtils;
import android.util.ArrayMap;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.ExtraSourceProvider;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

/* loaded from: classes2.dex */
public class AlbumSortHelper {
    public static Collator sCollator;
    public static final Comparator<Album> sCreateTimeASCComparator;
    public static final Comparator<Album> sCreateTimeDESCComparator;
    public static int sCurrentSortSpec;
    public static final Comparator<Album> sCustomComparator;
    public static boolean sIsLocaleChina;
    public static Comparator<Album> sNameASCComparator;
    public static Comparator<Album> sNameDESCComparator;
    public static ViewBeanCompartor sViewBeanCompartor;
    public static final int ALBUM_SORT_TYPE_CUSTOM = SortSpec.makeSortSpec(1, Integer.MIN_VALUE);
    public static final Object sBabyLock = new Object();
    public static final Object sNormalLock = new Object();
    public static final Calendar sCalendar = Calendar.getInstance();
    public static final BigDecimal sDivisor = new BigDecimal(2);
    public static String mPrevBabySort = "0";

    /* loaded from: classes2.dex */
    public static class SortSpec {
        public static int getOrder(int i) {
            return i & (-1073741824);
        }

        public static int getSort(int i) {
            return i & 1073741823;
        }

        public static int makeSortSpec(int i, int i2) {
            return (i & 1073741823) | (i2 & (-1073741824));
        }
    }

    static {
        init();
        sViewBeanCompartor = new ViewBeanCompartor();
        sCustomComparator = new Comparator<Album>() { // from class: com.miui.gallery.util.AlbumSortHelper.1
            @Override // java.util.Comparator
            public int compare(Album album, Album album2) {
                boolean isHeadAlbum = isHeadAlbum(AlbumSplitGroupHelper.getSplitGroupMode().getGroupType(album));
                boolean isHeadAlbum2 = isHeadAlbum(AlbumSplitGroupHelper.getSplitGroupMode().getGroupType(album2));
                if (isHeadAlbum != isHeadAlbum2) {
                    return -Integer.compare(isHeadAlbum ? 1 : 0, isHeadAlbum2 ? 1 : 0);
                }
                boolean isOtherAlbum = album.isOtherAlbum();
                boolean isOtherAlbum2 = album2.isOtherAlbum();
                if (isOtherAlbum != isOtherAlbum2) {
                    return Integer.compare(isOtherAlbum ? 1 : 0, isOtherAlbum2 ? 1 : 0);
                }
                return AlbumSortHelper.customComparatorFunction(album.getAlbumSortPosition(), album2.getAlbumSortPosition(), false);
            }

            public final boolean isHeadAlbum(String str) {
                return TextUtils.equals(str, AlbumSplitGroupHelper.getSplitGroupMode().getSupportGroups()[0]);
            }
        };
        sCreateTimeASCComparator = new Comparator<Album>() { // from class: com.miui.gallery.util.AlbumSortHelper.2
            @Override // java.util.Comparator
            public int compare(Album album, Album album2) {
                return AlbumSortHelper.sortAlbumByCreateTime(album, album2, false);
            }
        };
        sCreateTimeDESCComparator = new Comparator<Album>() { // from class: com.miui.gallery.util.AlbumSortHelper.3
            @Override // java.util.Comparator
            public int compare(Album album, Album album2) {
                return AlbumSortHelper.sortAlbumByCreateTime(album, album2, true);
            }
        };
        sNameASCComparator = new Comparator<Album>() { // from class: com.miui.gallery.util.AlbumSortHelper.4
            @Override // java.util.Comparator
            public int compare(Album album, Album album2) {
                return AlbumSortHelper.sortAlbumByName(album, album2, false);
            }
        };
        sNameDESCComparator = new Comparator<Album>() { // from class: com.miui.gallery.util.AlbumSortHelper.5
            @Override // java.util.Comparator
            public int compare(Album album, Album album2) {
                return AlbumSortHelper.sortAlbumByName(album, album2, true);
            }
        };
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0058 A[Catch: all -> 0x0063, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0019, B:9:0x002a, B:11:0x003f, B:13:0x0058, B:14:0x0061, B:10:0x003b), top: B:19:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String calculateSortPositionByBabyAlbum() {
        /*
            java.lang.Object r0 = com.miui.gallery.util.AlbumSortHelper.sBabyLock
            monitor-enter(r0)
            com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences r1 = com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences.getInstance()     // Catch: java.lang.Throwable -> L63
            java.lang.String r2 = "sort_position_baby_album_prev_index"
            java.lang.String r3 = com.miui.gallery.util.AlbumSortHelper.mPrevBabySort     // Catch: java.lang.Throwable -> L63
            java.lang.String r1 = r1.getString(r2, r3)     // Catch: java.lang.Throwable -> L63
            long r2 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L63
            boolean r4 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> L63
            if (r4 != 0) goto L3b
            double r4 = java.lang.Double.parseDouble(r1)     // Catch: java.lang.Throwable -> L63
            java.lang.String r1 = java.lang.Long.toString(r2)     // Catch: java.lang.Throwable -> L63
            double r6 = java.lang.Double.parseDouble(r1)     // Catch: java.lang.Throwable -> L63
            int r1 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r1 == 0) goto L2a
            goto L3b
        L2a:
            java.lang.String r1 = calculateSortPositionByNormalAlbum(r2)     // Catch: java.lang.Throwable -> L63
            double r1 = java.lang.Double.parseDouble(r1)     // Catch: java.lang.Throwable -> L63
            double r1 = randomNextSortPosition(r1)     // Catch: java.lang.Throwable -> L63
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch: java.lang.Throwable -> L63
            goto L3f
        L3b:
            java.lang.String r1 = calculateSortPositionByNormalAlbum(r2)     // Catch: java.lang.Throwable -> L63
        L3f:
            com.miui.gallery.util.AlbumSortHelper.mPrevBabySort = r1     // Catch: java.lang.Throwable -> L63
            com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences r2 = com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences.getInstance()     // Catch: java.lang.Throwable -> L63
            java.lang.String r3 = "sort_position_baby_album_prev_index"
            java.lang.String r4 = com.miui.gallery.util.AlbumSortHelper.mPrevBabySort     // Catch: java.lang.Throwable -> L63
            r2.putString(r3, r4)     // Catch: java.lang.Throwable -> L63
            com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences r2 = com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences.getInstance()     // Catch: java.lang.Throwable -> L63
            java.lang.String r3 = "sort_position_baby_album_first_index"
            boolean r2 = r2.contains(r3)     // Catch: java.lang.Throwable -> L63
            if (r2 != 0) goto L61
            com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences r2 = com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences.getInstance()     // Catch: java.lang.Throwable -> L63
            java.lang.String r3 = "sort_position_baby_album_first_index"
            r2.putString(r3, r1)     // Catch: java.lang.Throwable -> L63
        L61:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L63
            return r1
        L63:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L63
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.AlbumSortHelper.calculateSortPositionByBabyAlbum():java.lang.String");
    }

    public static String calculateSortPositionByUserCreativeAlbum() {
        synchronized (sBabyLock) {
            String string = AlbumConfigSharedPreferences.getInstance().getString(GalleryPreferences.PrefKeys.SORT_POSITION_BABY_FIRST_INDEX, "");
            if (string.isEmpty()) {
                return calculateSortPositionByNormalAlbum(System.currentTimeMillis());
            }
            return String.valueOf(Double.parseDouble(string) - 5.0d);
        }
    }

    public static String calculateSortPositionByNormalAlbum(long j) {
        String l;
        synchronized (sNormalLock) {
            if (j <= 0) {
                DefaultLogger.w("AlbumSortHelper", "calculateSortPositionByNormalAlbum is error,dateTaken is: %s", String.valueOf(j));
                j = System.currentTimeMillis();
            }
            Calendar calendar = sCalendar;
            calendar.setTimeInMillis(j);
            if (calendar.get(14) == 0) {
                int i = AlbumConfigSharedPreferences.getInstance().getInt(GalleryPreferences.PrefKeys.SORT_POSITION_NANO_NEXT_INDEX, 1);
                int i2 = i + 1;
                j += i;
                if (i2 > 999) {
                    AlbumConfigSharedPreferences.getInstance().putInt(GalleryPreferences.PrefKeys.SORT_POSITION_NANO_NEXT_INDEX, 0);
                } else {
                    AlbumConfigSharedPreferences.getInstance().putInt(GalleryPreferences.PrefKeys.SORT_POSITION_NANO_NEXT_INDEX, i2);
                }
            }
            l = Long.toString(j);
        }
        return l;
    }

    public static double randomNextSortPosition(double d) {
        return randomSortPosition(5.0d + d, d);
    }

    public static double randomPrevSortPosition(double d) {
        return randomSortPosition(d - 5.0d, d);
    }

    public static double randomSortPosition(double d, double d2) {
        if (d == SearchStatUtils.POW && d2 == SearchStatUtils.POW) {
            return -1.0d;
        }
        return new BigDecimal(Double.toString(d)).add(new BigDecimal(Double.toString(d2))).divide(sDivisor).doubleValue();
    }

    public static void init() {
        sCurrentSortSpec = getLastAlbumSortSpec();
        Locale locale = GalleryApp.sGetAndroidContext().getResources().getConfiguration().getLocales().get(0);
        sCollator = Collator.getInstance(locale);
        sIsLocaleChina = locale.getLanguage().equals(Locale.CHINESE.getLanguage());
    }

    public static int getCurrentSort() {
        return SortSpec.getSort(sCurrentSortSpec);
    }

    public static boolean isDescOrder() {
        return SortSpec.getOrder(sCurrentSortSpec) == Integer.MIN_VALUE;
    }

    public static Comparator<Album> getCurrentComparator() {
        boolean isDescOrder = isDescOrder();
        int currentSort = getCurrentSort();
        if (currentSort == 2) {
            return isDescOrder ? sNameDESCComparator : sNameASCComparator;
        } else if (currentSort == 3) {
            return isDescOrder ? sCreateTimeDESCComparator : sCreateTimeASCComparator;
        } else if (currentSort != 4) {
            return sCustomComparator;
        } else {
            return isDescOrder ? new UpdateTimeDESCComparator() : new UpdateTimeASCComparator();
        }
    }

    public static String getCurrentSortBasis() {
        int currentSort = getCurrentSort();
        return currentSort != 2 ? currentSort != 3 ? currentSort != 4 ? "sortInfo" : "dateModified" : "dateTaken" : "name";
    }

    public static void trackSortChange() {
        int currentSort = getCurrentSort();
        TrackController.trackClick(currentSort != 2 ? currentSort != 3 ? currentSort != 4 ? "403.29.0.1.10323" : "403.29.0.1.10326" : "403.29.0.1.10325" : "403.29.0.1.10324", "403.7.0.1.10328");
    }

    public static boolean isUpdateTimeSortMode() {
        return getCurrentSort() == 4;
    }

    public static boolean isCustomSortOrder() {
        return SortSpec.getSort(sCurrentSortSpec) == 1;
    }

    public static Comparator<BaseViewBean> getViewBeanComparator() {
        sViewBeanCompartor.setRealAlbumComparator(getCurrentComparator());
        return sViewBeanCompartor;
    }

    /* loaded from: classes2.dex */
    public static class ViewBeanCompartor implements Comparator<BaseViewBean> {
        public Comparator<Album> mRealAlbumComparator;

        public void setRealAlbumComparator(Comparator<Album> comparator) {
            this.mRealAlbumComparator = comparator;
        }

        @Override // java.util.Comparator
        public int compare(BaseViewBean baseViewBean, BaseViewBean baseViewBean2) {
            Object source = getSource(baseViewBean);
            Object source2 = getSource(baseViewBean2);
            if (!(source instanceof Album) || !(source2 instanceof Album)) {
                return 0;
            }
            return this.mRealAlbumComparator.compare((Album) source, (Album) source2);
        }

        public final Object getSource(BaseViewBean baseViewBean) {
            if (baseViewBean instanceof ExtraSourceProvider) {
                return ((ExtraSourceProvider) baseViewBean).mo1601provider();
            }
            return baseViewBean.getSource();
        }
    }

    public static final int customComparatorFunction(double d, double d2, boolean z) {
        int compare = Double.compare(d, d2);
        return z ? compare : -compare;
    }

    public static int sortAlbumByCreateTime(Album album, Album album2, boolean z) {
        int compare = Long.compare(album.getDateTaken(), album2.getDateTaken());
        return z ? -compare : compare;
    }

    public static int sortAlbumByName(Album album, Album album2, boolean z) {
        int compare;
        if (sIsLocaleChina) {
            compare = sCollator.compare(PinyinUtil.get(album.getDisplayedAlbumName()), PinyinUtil.get(album2.getDisplayedAlbumName()));
        } else {
            compare = sCollator.compare(album.getDisplayedAlbumName(), album2.getDisplayedAlbumName());
        }
        return z ? -compare : compare;
    }

    /* loaded from: classes2.dex */
    public static final class UpdateTimeASCComparator implements Comparator<Album> {
        public final ArrayMap<Long, Long> mDate;

        public UpdateTimeASCComparator() {
            this.mDate = new ArrayMap<>(16);
        }

        @Override // java.util.Comparator
        public int compare(Album album, Album album2) {
            return AlbumSortHelper.sortByLastModified(this.mDate, album, album2);
        }
    }

    public static int sortByLastModified(ArrayMap<Long, Long> arrayMap, Album album, Album album2) {
        if (album.getLocalPath() == null || album2.getLocalPath() == null) {
            return Long.compare(album.getDateModified(), album2.getDateModified());
        }
        Long l = arrayMap.get(Long.valueOf(album.getAlbumId()));
        if (l == null) {
            l = Long.valueOf(StorageUtils.getLastModifiedByRelativePath(GalleryApp.sGetAndroidContext(), album.getLocalPath()));
            if (l.longValue() <= 0) {
                Long valueOf = Long.valueOf(StorageUtils.getLastModifiedByRelativePath(GalleryApp.sGetAndroidContext(), album2.getLocalPath()));
                if (valueOf.longValue() <= 0) {
                    return Long.compare(album.getDateModified(), album2.getDateModified());
                }
                return Long.compare(album.getDateModified(), valueOf.longValue());
            }
            arrayMap.put(Long.valueOf(album.getAlbumId()), l);
        }
        Long l2 = arrayMap.get(Long.valueOf(album2.getAlbumId()));
        if (l2 == null) {
            l2 = Long.valueOf(StorageUtils.getLastModifiedByRelativePath(GalleryApp.sGetAndroidContext(), album2.getLocalPath()));
            if (l2.longValue() <= 0) {
                return Long.compare(l.longValue(), album2.getDateModified());
            }
            arrayMap.put(Long.valueOf(album2.getAlbumId()), l2);
        }
        return Long.compare(l.longValue(), l2.longValue());
    }

    /* loaded from: classes2.dex */
    public static final class UpdateTimeDESCComparator implements Comparator<Album> {
        public final ArrayMap<Long, Long> mDate;

        public UpdateTimeDESCComparator() {
            this.mDate = new ArrayMap<>(16);
        }

        @Override // java.util.Comparator
        public int compare(Album album, Album album2) {
            return -AlbumSortHelper.sortByLastModified(this.mDate, album, album2);
        }
    }

    public static int getLastAlbumSortSpec() {
        return AlbumConfigSharedPreferences.getInstance().getInt(GalleryPreferences.PrefKeys.ALBUM_SORT_TYPE, ALBUM_SORT_TYPE_CUSTOM);
    }

    public static void setAlbumSortSpec(int i, int i2) {
        setAlbumSortSpec(SortSpec.makeSortSpec(i, i2));
    }

    public static void setAlbumSortSpec(int i) {
        sCurrentSortSpec = i;
        AlbumConfigSharedPreferences.getInstance().putInt(GalleryPreferences.PrefKeys.ALBUM_SORT_TYPE, i);
    }
}
