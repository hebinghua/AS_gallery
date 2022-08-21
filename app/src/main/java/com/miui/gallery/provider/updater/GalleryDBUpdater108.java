package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Pair;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.updater.GalleryDBUpdater108;
import com.miui.gallery.provider.updater.UpdateResult;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences;
import com.miui.gallery.ui.album.main.utils.splitgroup.BaseSplitGroupMode;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class GalleryDBUpdater108 extends GalleryDBUpdater {
    public List<String> mRecreateAlbumNames = new LinkedList();
    public final Map<Long, Long> mDefaultSystemSortMap = new HashMap(6, 1.0f);
    public final Map<Pair<Long, String>, String> mVirtualAlbumSortMap = new HashMap(6, 1.0f);

    /* renamed from: $r8$lambda$eda74b08l-Eyd9bWyHcfbN5sKC4 */
    public static /* synthetic */ Boolean m1244$r8$lambda$eda74b08lEyd9bWyHcfbN5sKC4(Map map, TempBean tempBean) {
        return lambda$hookSort$0(map, tempBean);
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        this.mDefaultSystemSortMap.put(-2147483644L, 1001L);
        this.mDefaultSystemSortMap.put(-2147483642L, 1000L);
        this.mDefaultSystemSortMap.put(1L, 999L);
        this.mDefaultSystemSortMap.put(2L, 995L);
        this.mDefaultSystemSortMap.put(2147483639L, 994L);
        this.mDefaultSystemSortMap.put(-2147483647L, 998L);
        this.mVirtualAlbumSortMap.put(Pair.create(-2147483644L, GalleryPreferences.PrefKeys.SORT_POSITION_RECENT_ALBUM_INDEX), GalleryPreferences.Album.getVirtualAlbumSortPosition(2147483644L));
        this.mVirtualAlbumSortMap.put(Pair.create(-2147483647L, GalleryPreferences.PrefKeys.SORT_POSITION_VIDEO_ALBUM_INDEX), GalleryPreferences.Album.getVirtualAlbumSortPosition(2147483647L));
        this.mVirtualAlbumSortMap.put(Pair.create(-2147483642L, GalleryPreferences.PrefKeys.SORT_POSITION_FAVORITES_ALBUM_INDEX), GalleryPreferences.Album.getVirtualAlbumSortPosition(2147483642L));
        this.mVirtualAlbumSortMap.put(Pair.create(-2147483645L, GalleryPreferences.PrefKeys.SORT_POSITION_SCREENSHOTS_RECORDERS_ALBUM_INDEX), GalleryPreferences.Album.getVirtualAlbumSortPosition(2147483645L));
        this.mVirtualAlbumSortMap.put(Pair.create(2147483639L, GalleryPreferences.PrefKeys.SORT_POSITION_AI_ALBUM_INDEX), GalleryPreferences.Album.getFixedAlbumSortInfo(2147483639L));
        this.mVirtualAlbumSortMap.put(Pair.create(2147483641L, GalleryPreferences.PrefKeys.SORT_POSITION_OTHER_ALBUM_INDEX), GalleryPreferences.Album.getFixedAlbumSortInfo(2147483641L));
        this.mVirtualAlbumSortMap.put(Pair.create(2147483638L, GalleryPreferences.PrefKeys.SORT_POSITION_TRASH_ALBUM_INDEX), GalleryPreferences.Album.getFixedAlbumSortInfo(2147483638L));
        upgrade(supportSQLiteDatabase, updateResult);
        UpdateResult.Builder builder = new UpdateResult.Builder();
        if (this.mRecreateAlbumNames.contains("album")) {
            builder = builder.recreateTableAlbum();
        }
        if (this.mRecreateAlbumNames.contains("shareAlbum")) {
            builder = builder.recreateTableShareAlbum();
        }
        return builder.build();
    }

    public final void upgrade(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        try {
            supportSQLiteDatabase.beginTransaction();
            DefaultLogger.fd("GalleryDBUpdater108", "------------------------upgrade 108 start");
            DefaultLogger.fd("GalleryDBUpdater108", "start update shareAlbum Table datas");
            updateHeadAlbumSortPosition(supportSQLiteDatabase, true, updateResult);
            DefaultLogger.fd("GalleryDBUpdater108", "start update Album Table datas");
            boolean z = false;
            List<TempBean> updateHeadAlbumSortPosition = updateHeadAlbumSortPosition(supportSQLiteDatabase, false, updateResult);
            DefaultLogger.fd("GalleryDBUpdater108", "update virtual album and fixed album sortInfo");
            SharedPreferences.Editor edit = AlbumConfigSharedPreferences.getInstance().edit();
            for (Pair<Long, String> pair : this.mVirtualAlbumSortMap.keySet()) {
                String str = this.mVirtualAlbumSortMap.get(pair);
                if (!TextUtils.isEmpty(str)) {
                    double parseDouble = Double.parseDouble(str);
                    String str2 = (String) pair.second;
                    long longValue = ((Long) pair.first).longValue();
                    if (parseDouble < SearchStatUtils.POW) {
                        String valueOf = String.valueOf(Math.abs(parseDouble));
                        DefaultLogger.fd("GalleryDBUpdater108", "update key: %s ,sortInfo:%s", pair, valueOf);
                        edit.putString(str2, valueOf);
                    } else {
                        if (!str2.equals(GalleryPreferences.PrefKeys.SORT_POSITION_OTHER_ALBUM_INDEX) && !str2.equals(GalleryPreferences.PrefKeys.SORT_POSITION_TRASH_ALBUM_INDEX)) {
                            if (parseDouble > 2.147483647E9d) {
                                if (updateHeadAlbumSortPosition == null) {
                                    updateHeadAlbumSortPosition = new LinkedList<>();
                                }
                                updateHeadAlbumSortPosition.add(new TempBean(longValue, Double.parseDouble(str), String.valueOf(longValue), (String) pair.second));
                            }
                        }
                        if (parseDouble == 31.5144d) {
                            edit.putString(str2, String.valueOf(2147484647L));
                        } else if (parseDouble == 31.5108d) {
                            edit.putString(str2, String.valueOf(2147483747L));
                        }
                    }
                    z = true;
                }
            }
            if (z) {
                edit.remove(GalleryPreferences.PrefKeys.SORT_POSITION_NEXT_TOP_ALBUM);
                edit.putString(GalleryPreferences.PrefKeys.SORT_POSITION_NEXT_TOP_ALBUM, String.valueOf(2147483646));
                edit.apply();
            }
            if (updateHeadAlbumSortPosition != null) {
                resetSystemAlbumSortIfNeed(supportSQLiteDatabase, updateHeadAlbumSortPosition);
            }
            supportSQLiteDatabase.setTransactionSuccessful();
        } finally {
            supportSQLiteDatabase.endTransaction();
            DefaultLogger.fd("GalleryDBUpdater108", "------------------------upgrade 108 end");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:124:0x0150  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<com.miui.gallery.provider.updater.GalleryDBUpdater108.TempBean> updateHeadAlbumSortPosition(androidx.sqlite.db.SupportSQLiteDatabase r22, boolean r23, com.miui.gallery.provider.updater.UpdateResult r24) {
        /*
            Method dump skipped, instructions count: 340
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.updater.GalleryDBUpdater108.updateHeadAlbumSortPosition(androidx.sqlite.db.SupportSQLiteDatabase, boolean, com.miui.gallery.provider.updater.UpdateResult):java.util.List");
    }

    public final void resetSystemAlbumSortIfNeed(SupportSQLiteDatabase supportSQLiteDatabase, List<TempBean> list) {
        ContentValues contentValues;
        BigDecimal bigDecimal;
        List<TempBean> list2 = (List) list.stream().filter(new Predicate<TempBean>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater108.1
            {
                GalleryDBUpdater108.this = this;
            }

            @Override // java.util.function.Predicate
            public boolean test(TempBean tempBean) {
                return Album.isSystemAlbum(tempBean.getServerId()) || tempBean.getId() == 2147483639;
            }
        }).collect(Collectors.toList());
        if (!list2.isEmpty()) {
            String str = this.mVirtualAlbumSortMap.get(Pair.create(2147483641L, GalleryPreferences.PrefKeys.SORT_POSITION_OTHER_ALBUM_INDEX));
            String str2 = this.mVirtualAlbumSortMap.get(Pair.create(2147483638L, GalleryPreferences.PrefKeys.SORT_POSITION_TRASH_ALBUM_INDEX));
            LinkedList<Pair> linkedList = new LinkedList();
            LinkedList linkedList2 = new LinkedList();
            LinkedList linkedList3 = new LinkedList();
            for (int i = 0; i < list2.size(); i++) {
                TempBean tempBean = (TempBean) list2.get(i);
                long longValue = Long.valueOf(tempBean.getServerId()).longValue();
                if (this.mDefaultSystemSortMap.containsKey(Long.valueOf(longValue))) {
                    double longValue2 = this.mDefaultSystemSortMap.get(Long.valueOf(longValue)).longValue();
                    linkedList2.add(Pair.create(Long.valueOf(tempBean.getId()), Double.valueOf(longValue2)));
                    if (tempBean.getSort() > 2.147483647E9d) {
                        linkedList3.add(String.format(Locale.US, "cast(sortInfo as int) == %s ", String.valueOf(longValue2)));
                        if (!TextUtils.isEmpty(str) && Double.parseDouble(str) == longValue2) {
                            linkedList.add(Pair.create(Long.valueOf(tempBean.getId()), Double.valueOf(longValue2)));
                        }
                        if (!TextUtils.isEmpty(str2) && Double.parseDouble(str2) == longValue2) {
                            linkedList.add(Pair.create(Long.valueOf(tempBean.getId()), Double.valueOf(longValue2)));
                        }
                    }
                }
            }
            if (!linkedList3.isEmpty()) {
                String join = TextUtils.join(" OR ", linkedList3);
                String[] strArr = {j.c, "sortInfo"};
                Pair<Boolean, List<Pair<Long, Double>>> queryConflictData = queryConflictData(supportSQLiteDatabase, false, strArr, join);
                Pair<Boolean, List<Pair<Long, Double>>> queryConflictData2 = queryConflictData(supportSQLiteDatabase, true, strArr, join);
                if (((Boolean) queryConflictData.first).booleanValue() || ((Boolean) queryConflictData2.first).booleanValue()) {
                    linkedList.addAll((Collection) queryConflictData.second);
                    linkedList.addAll((Collection) queryConflictData2.second);
                }
            }
            long j = 2147483639;
            if (!linkedList.isEmpty()) {
                if (!TextUtils.isEmpty(str) && Double.parseDouble(str) < SearchStatUtils.POW) {
                    linkedList.add(Pair.create(2147483641L, Double.valueOf(Math.abs(Double.parseDouble(str)))));
                }
                if (!TextUtils.isEmpty(str2) && Double.parseDouble(str2) < SearchStatUtils.POW) {
                    linkedList.add(Pair.create(2147483638L, Double.valueOf(Math.abs(Double.parseDouble(str2)))));
                }
                linkedList.addAll(linkedList2);
                LinkedList<Pair> linkedList4 = new LinkedList();
                HashMap hashMap = new HashMap();
                for (Pair pair : linkedList) {
                    if (!hashMap.containsKey(pair.first)) {
                        linkedList4.add(pair);
                        hashMap.put((Long) pair.first, null);
                    }
                }
                linkedList4.sort(new Comparator<Pair<Long, Double>>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater108.2
                    {
                        GalleryDBUpdater108.this = this;
                    }

                    @Override // java.util.Comparator
                    public int compare(Pair<Long, Double> pair2, Pair<Long, Double> pair3) {
                        return Double.compare(((Double) pair2.second).doubleValue(), ((Double) pair3.second).doubleValue());
                    }
                });
                BigDecimal bigDecimal2 = new BigDecimal(1073741823);
                BigDecimal bigDecimal3 = new BigDecimal(100);
                ContentValues contentValues2 = new ContentValues(1);
                for (Pair pair2 : linkedList4) {
                    long longValue3 = ((Long) pair2.first).longValue();
                    BigDecimal add = bigDecimal2.add(bigDecimal3);
                    if (longValue3 == j || longValue3 == 2147483641 || longValue3 == 2147483638) {
                        contentValues = contentValues2;
                        bigDecimal = bigDecimal3;
                        GalleryPreferences.Album.setFixedAlbumSortInfo(longValue3, add.toPlainString());
                    } else if (Album.isVirtualAlbumByServerId(longValue3)) {
                        GalleryPreferences.Album.setVirtualAlbumSortPosition(longValue3, add.toPlainString());
                        contentValues = contentValues2;
                        bigDecimal = bigDecimal3;
                    } else {
                        boolean isOtherShareAlbumId = ShareAlbumHelper.isOtherShareAlbumId(longValue3);
                        contentValues2.clear();
                        contentValues2.put("sortInfo", add.toPlainString());
                        String str3 = isOtherShareAlbumId ? "shareAlbum" : "album";
                        Locale locale = Locale.US;
                        Object[] objArr = new Object[1];
                        if (isOtherShareAlbumId) {
                            longValue3 = ShareAlbumHelper.getOriginalAlbumId(longValue3);
                        }
                        objArr[0] = Long.valueOf(longValue3);
                        String format = String.format(locale, "_id = %s", objArr);
                        contentValues = contentValues2;
                        bigDecimal = bigDecimal3;
                        supportSQLiteDatabase.update(str3, 0, contentValues2, format, null);
                    }
                    bigDecimal3 = bigDecimal;
                    bigDecimal2 = add;
                    contentValues2 = contentValues;
                    j = 2147483639;
                }
                return;
            }
            ContentValues contentValues3 = new ContentValues(1);
            for (TempBean tempBean2 : list2) {
                long id = tempBean2.getId();
                long longValue4 = Long.valueOf(tempBean2.getServerId()).longValue();
                if (this.mDefaultSystemSortMap.containsKey(Long.valueOf(longValue4))) {
                    double abs = Math.abs(this.mDefaultSystemSortMap.get(Long.valueOf(longValue4)).longValue());
                    if (id != 2147483639 && id != 2147483641 && id != 2147483638) {
                        if (Album.isVirtualAlbumByServerId(id)) {
                            GalleryPreferences.Album.setVirtualAlbumSortPosition(id, String.valueOf(abs));
                        } else {
                            boolean isOtherShareAlbumId2 = ShareAlbumHelper.isOtherShareAlbumId(id);
                            contentValues3.clear();
                            contentValues3.put("sortInfo", String.valueOf(abs));
                            String str4 = isOtherShareAlbumId2 ? "shareAlbum" : "album";
                            Locale locale2 = Locale.US;
                            Object[] objArr2 = new Object[1];
                            if (isOtherShareAlbumId2) {
                                id = ShareAlbumHelper.getOriginalAlbumId(id);
                            }
                            objArr2[0] = Long.valueOf(id);
                            supportSQLiteDatabase.update(str4, 0, contentValues3, String.format(locale2, "_id = %s", objArr2), null);
                        }
                    }
                    GalleryPreferences.Album.setFixedAlbumSortInfo(id, String.valueOf(abs));
                }
            }
        }
    }

    public final Pair<Boolean, List<Pair<Long, Double>>> queryConflictData(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, String[] strArr, String str) {
        LinkedList linkedList = new LinkedList();
        String str2 = "shareAlbum";
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder(z ? str2 : "album").columns(strArr).selection("cast(sortInfo as int) < 2147483647", null).create());
        try {
            fillResult(query, z, linkedList);
            if (query != null) {
                query.close();
            }
            boolean z2 = false;
            if (!z) {
                str2 = "album";
            }
            query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder(str2).columns(strArr).selection(str, null).create());
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        z2 = true;
                    }
                } finally {
                }
            }
            if (query != null) {
                query.close();
            }
            return Pair.create(Boolean.valueOf(z2), linkedList);
        } finally {
        }
    }

    public final void fillResult(Cursor cursor, boolean z, List<Pair<Long, Double>> list) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long j = cursor.getLong(0);
            if (z) {
                j = ShareAlbumHelper.getUniformAlbumId(j);
            }
            list.add(Pair.create(Long.valueOf(j), Double.valueOf(cursor.getDouble(1))));
            cursor.moveToNext();
        }
    }

    public final Map<String, Integer> getFixedSortMap() {
        HashMap hashMap = new HashMap(6, 1.0f);
        hashMap.put("tencent/micromsg/weixin", 1);
        hashMap.put("pictures/weixin", 1);
        hashMap.put("tencent/qq_images", 2);
        return hashMap;
    }

    public final void hookSort(List<TempBean> list, SupportSQLiteDatabase supportSQLiteDatabase) {
        try {
            final Map<String, Integer> fixedSortMap = getFixedSortMap();
            List list2 = (List) list.stream().filter(new Predicate<TempBean>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater108.4
                {
                    GalleryDBUpdater108.this = this;
                }

                @Override // java.util.function.Predicate
                public boolean test(TempBean tempBean) {
                    return Album.isThirdAlbum(tempBean.serverId, tempBean.path);
                }
            }).sorted(new Comparator<TempBean>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater108.3
                {
                    GalleryDBUpdater108.this = this;
                }

                @Override // java.util.Comparator
                public int compare(TempBean tempBean, TempBean tempBean2) {
                    return -Double.compare(tempBean.sort, tempBean2.sort);
                }
            }).collect(Collectors.toList());
            Map map = (Map) list2.stream().collect(Collectors.groupingBy(new Function() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater108$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return GalleryDBUpdater108.m1244$r8$lambda$eda74b08lEyd9bWyHcfbN5sKC4(fixedSortMap, (GalleryDBUpdater108.TempBean) obj);
                }
            }));
            Boolean bool = Boolean.TRUE;
            if (!map.containsKey(bool)) {
                return;
            }
            List list3 = (List) map.get(bool);
            list3.sort(new Comparator<TempBean>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater108.5
                {
                    GalleryDBUpdater108.this = this;
                }

                @Override // java.util.Comparator
                public int compare(TempBean tempBean, TempBean tempBean2) {
                    return -Integer.compare(((Integer) fixedSortMap.get(tempBean.getPath().toLowerCase())).intValue(), ((Integer) fixedSortMap.get(tempBean2.getPath().toLowerCase())).intValue());
                }
            });
            BigDecimal bigDecimal = new BigDecimal(10);
            BigDecimal bigDecimal2 = new BigDecimal(((TempBean) list2.get(0)).sort);
            ContentValues contentValues = new ContentValues(1);
            Iterator it = list3.iterator();
            while (it.hasNext()) {
                bigDecimal2 = bigDecimal2.add(bigDecimal);
                contentValues.clear();
                contentValues.put("sortInfo", BaseSplitGroupMode.packSortInfo(bigDecimal2.doubleValue(), "group_third"));
                supportSQLiteDatabase.update("album", 0, contentValues, String.format("_id=%s", String.valueOf(((TempBean) it.next()).id)), null);
            }
        } catch (Exception e) {
            DefaultLogger.e("GalleryDBUpdater108", e);
        }
    }

    public static /* synthetic */ Boolean lambda$hookSort$0(Map map, TempBean tempBean) {
        return Boolean.valueOf(map.containsKey(tempBean.path.toLowerCase()));
    }

    /* loaded from: classes2.dex */
    public static class TempBean {
        public long id;
        public String path;
        public String serverId;
        public double sort;

        public TempBean(long j, double d, String str, String str2) {
            this.id = j;
            this.sort = d;
            this.serverId = str;
            this.path = str2;
        }

        public String getPath() {
            return this.path;
        }

        public String getServerId() {
            return this.serverId;
        }

        public long getId() {
            return this.id;
        }

        public double getSort() {
            return this.sort;
        }

        public void setSort(double d) {
            this.sort = d;
        }

        public String toString() {
            return "TempBean{id=" + this.id + ", sort=" + this.sort + '}';
        }
    }
}
