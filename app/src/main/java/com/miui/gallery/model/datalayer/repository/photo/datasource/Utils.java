package com.miui.gallery.model.datalayer.repository.photo.datasource;

import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.cache.BurstInfo;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/* loaded from: classes2.dex */
public class Utils {
    public static boolean isSupport(long j, long j2, String str) {
        return (j & j2) != 0;
    }

    public static Map<Long, Integer> groupMediaByType(Cursor cursor, int i, int i2, int i3, int i4, int i5, long[] jArr, boolean z) {
        long j;
        Map<Long, Integer> linkedHashMap;
        Long valueOf;
        int i6;
        boolean z2;
        boolean z3;
        HashMap hashMap;
        BurstInfo generateBurstInfo;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            DefaultLogger.fd("CloudDataSourceUtils", "groupMediaByType start");
            if (!cursor.moveToFirst()) {
                DefaultLogger.fd("CloudDataSourceUtils", "groupMediaByType cursor is empty");
                linkedHashMap = new HashMap<>(0);
                valueOf = Long.valueOf(System.currentTimeMillis() - currentTimeMillis);
            } else {
                HashMap hashMap2 = new HashMap();
                HashMap hashMap3 = new HashMap();
                boolean isOnlyShowLocalPhoto = GalleryPreferences.LocalMode.isOnlyShowLocalPhoto();
                cursor.moveToFirst();
                boolean z4 = false;
                while (!cursor.isAfterLast()) {
                    String string = cursor.getString(i4);
                    try {
                        if (!isOnlyShowLocalPhoto || !TextUtils.isEmpty(string)) {
                            String string2 = cursor.getString(i5);
                            if (TextUtils.isEmpty(string)) {
                                i6 = i3;
                            } else {
                                i6 = i3;
                                string2 = string;
                            }
                            String string3 = cursor.getString(i6);
                            z2 = isOnlyShowLocalPhoto;
                            z3 = z4;
                            String string4 = cursor.getString(i2);
                            long j2 = cursor.getLong(i);
                            if (isNeedDynamicCheck(string4, string3)) {
                                boolean z5 = true;
                                if (!z || !BurstInfo.maybeBurst(string3) || (generateBurstInfo = BurstInfo.generateBurstInfo(Long.valueOf(cursor.getLong(cursor.getColumnIndex("localGroupId"))), string3, string4)) == null) {
                                    j = currentTimeMillis;
                                } else {
                                    j = currentTimeMillis;
                                    if (!hashMap3.containsKey(Long.valueOf(generateBurstInfo.mGroupKey))) {
                                        hashMap3.put(Long.valueOf(generateBurstInfo.mGroupKey), null);
                                        j2 = generateBurstInfo.mIsTimeBurst ? 8388608L : 64L;
                                        z3 = true;
                                    }
                                }
                                if (!z3) {
                                    j2 = preParse(string4, string3, string2);
                                    if (-1 == j2) {
                                        Iterator<SpecialTypeMediaUtils.FlagTypeParser> it = SpecialTypeMediaUtils.getNeedDynamicCheckFlagTypeParser().iterator();
                                        while (true) {
                                            if (!it.hasNext()) {
                                                z5 = false;
                                                break;
                                            }
                                            SpecialTypeMediaUtils.FlagTypeParser next = it.next();
                                            if (!TextUtils.isEmpty(string2)) {
                                                long parseFlags = next.parseFlags(string2);
                                                j2 = parseFlags;
                                                if (parseFlags != 0) {
                                                    break;
                                                }
                                            }
                                        }
                                        if (!z5) {
                                        }
                                    }
                                }
                            } else {
                                j = currentTimeMillis;
                            }
                            long j3 = j2;
                            int length = jArr.length;
                            int i7 = 0;
                            while (i7 < length) {
                                int i8 = length;
                                HashMap hashMap4 = hashMap3;
                                long j4 = jArr[i7];
                                if (isSupport(j3, j4, string)) {
                                    ((List) hashMap2.computeIfAbsent(Long.valueOf(j4), new Function<Long, List<MediaInfoBean>>() { // from class: com.miui.gallery.model.datalayer.repository.photo.datasource.Utils.1
                                        @Override // java.util.function.Function
                                        public List<MediaInfoBean> apply(Long l) {
                                            return new LinkedList();
                                        }
                                    })).add(new MediaInfoBean(j3));
                                }
                                i7++;
                                hashMap3 = hashMap4;
                                length = i8;
                            }
                            hashMap = hashMap3;
                            z3 = false;
                            cursor.moveToNext();
                            hashMap3 = hashMap;
                            isOnlyShowLocalPhoto = z2;
                            z4 = z3;
                            currentTimeMillis = j;
                        } else {
                            j = currentTimeMillis;
                            z2 = isOnlyShowLocalPhoto;
                            z3 = z4;
                        }
                        cursor.moveToNext();
                        hashMap3 = hashMap;
                        isOnlyShowLocalPhoto = z2;
                        z4 = z3;
                        currentTimeMillis = j;
                    } catch (Throwable th) {
                        th = th;
                        DefaultLogger.fd("CloudDataSourceUtils", "groupMediaByType end cost:[%s]", Long.valueOf(System.currentTimeMillis() - j));
                        throw th;
                    }
                    hashMap = hashMap3;
                }
                j = currentTimeMillis;
                linkedHashMap = new LinkedHashMap<>();
                for (long j5 : jArr) {
                    List list = (List) hashMap2.get(Long.valueOf(j5));
                    if (BaseMiscUtil.isValid(list)) {
                        linkedHashMap.put(Long.valueOf(j5), Integer.valueOf(list.size()));
                    }
                }
                DefaultLogger.fd("CloudDataSourceUtils", "groupMediaByType groups:[%s]", linkedHashMap.toString());
                valueOf = Long.valueOf(System.currentTimeMillis() - j);
            }
            DefaultLogger.fd("CloudDataSourceUtils", "groupMediaByType end cost:[%s]", valueOf);
            return linkedHashMap;
        } catch (Throwable th2) {
            th = th2;
            j = currentTimeMillis;
        }
    }

    public static boolean isNeedDynamicCheck(String str, String str2) {
        return TextUtils.equals(str, "image/gif") || BurstInfo.maybeBurst(str2);
    }

    public static long preParse(String str, String str2, String str3) {
        return BaseFileMimeUtil.isGifFromMimeType(str) ? 8589934592L : -1L;
    }

    /* loaded from: classes2.dex */
    public static class MediaInfoBean {
        public long flag;

        public MediaInfoBean(long j) {
            this.flag = j;
        }
    }
}
