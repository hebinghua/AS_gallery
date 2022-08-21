package com.miui.gallery.picker.helper;

import android.database.Cursor;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.adapter.BaseMediaAdapterDeprecated;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BurstFilterCursor;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class CursorUtils {
    /* renamed from: $r8$lambda$-G_cOfsdbKs3oF8w0wEZ-O3N7_E */
    public static /* synthetic */ void m1179$r8$lambda$G_cOfsdbKs3oF8w0wEZO3N7_E(BurstFilterCursor burstFilterCursor, List list, Integer num) {
        lambda$getBurstGroupIds$0(burstFilterCursor, list, num);
    }

    public static String getSha1(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("sha1");
        if (columnIndex >= 0) {
            return cursor.getString(columnIndex);
        }
        throw new RuntimeException("key needed");
    }

    public static String getFaceId(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("serverId");
        if (columnIndex >= 0) {
            return cursor.getString(columnIndex);
        }
        throw new RuntimeException("key needed");
    }

    public static long getId(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(j.c);
        if (columnIndex >= 0) {
            return cursor.getLong(columnIndex);
        }
        throw new RuntimeException("key needed");
    }

    public static long getFacePhotoId(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("photo_id");
        if (columnIndex >= 0) {
            return cursor.getLong(columnIndex);
        }
        throw new RuntimeException("key needed");
    }

    public static String getMimeType(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("mimeType");
        if (columnIndex >= 0) {
            return cursor.getString(columnIndex);
        }
        throw new RuntimeException("key needed");
    }

    public static int getWidth(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("exifImageWidth");
        if (columnIndex >= 0) {
            return cursor.getInt(columnIndex);
        }
        return -1;
    }

    public static int getHeight(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("exifImageLength");
        if (columnIndex >= 0) {
            return cursor.getInt(columnIndex);
        }
        return -1;
    }

    public static String getMicroPath(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("alias_micro_thumbnail");
        int columnIndex2 = cursor.getColumnIndex("sha1");
        if (columnIndex >= 0 && columnIndex2 >= 0) {
            return BaseMediaAdapterDeprecated.getMicroPath(cursor, columnIndex, columnIndex2);
        }
        throw new RuntimeException("key needed");
    }

    public static long getFileLength(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
        if (columnIndex >= 0) {
            return cursor.getLong(columnIndex);
        }
        throw new RuntimeException("key needed");
    }

    public static long getCreateTime(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("alias_create_time");
        if (columnIndex >= 0) {
            return cursor.getLong(columnIndex);
        }
        return System.currentTimeMillis();
    }

    public static String getLocation(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("location");
        return columnIndex >= 0 ? cursor.getString(columnIndex) : "";
    }

    public static List<String> getBurstGroupIds(Cursor cursor) {
        if (!(cursor instanceof BurstFilterCursor)) {
            return null;
        }
        final BurstFilterCursor burstFilterCursor = (BurstFilterCursor) cursor;
        int position = burstFilterCursor.getPosition();
        if (!burstFilterCursor.isBurstPosition(position)) {
            return null;
        }
        ArrayList<Integer> burstGroup = burstFilterCursor.getBurstGroup(position);
        if (!BaseMiscUtil.isValid(burstGroup)) {
            return null;
        }
        final ArrayList arrayList = new ArrayList();
        burstGroup.forEach(new Consumer() { // from class: com.miui.gallery.picker.helper.CursorUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CursorUtils.m1179$r8$lambda$G_cOfsdbKs3oF8w0wEZO3N7_E(BurstFilterCursor.this, arrayList, (Integer) obj);
            }
        });
        return arrayList;
    }

    public static /* synthetic */ void lambda$getBurstGroupIds$0(BurstFilterCursor burstFilterCursor, List list, Integer num) {
        Cursor contentCursorAtPosition = burstFilterCursor.getContentCursorAtPosition(num.intValue());
        if (contentCursorAtPosition != null) {
            list.add(String.valueOf(getId(contentCursorAtPosition)));
        }
    }
}
