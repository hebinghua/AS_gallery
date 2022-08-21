package com.miui.gallery.util;

import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.provider.cache.BurstInfo;
import com.miui.gallery.util.BurstFilterCursor;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.micloudsdk.utils.FileMimeUtils;
import java.util.HashMap;
import java.util.TreeSet;

/* loaded from: classes2.dex */
public abstract class BurstKeyFilterCursor extends BurstFilterCursor {
    public abstract int getBurstKeyIndex();

    public abstract String getLocalGroupIdColumnName();

    public abstract String getMimeTypeColumnName();

    public BurstKeyFilterCursor(Cursor cursor) {
        super(cursor);
    }

    @Override // com.miui.gallery.util.BurstFilterCursor
    public void initResultList() {
        int burstKeyIndex;
        Cursor wrappedCursor = getWrappedCursor();
        if (getWrappedCursor() != null && (burstKeyIndex = getBurstKeyIndex()) >= 0) {
            long currentTimeMillis = System.currentTimeMillis();
            HashMap hashMap = new HashMap();
            for (int i = 0; i < wrappedCursor.getCount(); i++) {
                if (wrappedCursor.moveToPosition(i)) {
                    String string = wrappedCursor.getString(burstKeyIndex);
                    int columnIndex = wrappedCursor.getColumnIndex(getMimeTypeColumnName());
                    String str = null;
                    if (columnIndex > -1) {
                        str = wrappedCursor.getString(columnIndex);
                    }
                    if (TextUtils.isEmpty(str)) {
                        str = getMimeType(string);
                    }
                    int columnIndex2 = wrappedCursor.getColumnIndex(getLocalGroupIdColumnName());
                    BurstInfo generateBurstInfo = BurstInfo.generateBurstInfo(Long.valueOf(columnIndex2 > -1 ? wrappedCursor.getLong(columnIndex2) : 0L), wrapKey(string), str);
                    if (generateBurstInfo != null) {
                        long j = generateBurstInfo.mGroupKey;
                        if (j > 0) {
                            if (hashMap.containsKey(Long.valueOf(j))) {
                                ((TreeSet) hashMap.get(Long.valueOf(j))).add(new BurstFilterCursor.BurstPosition(generateBurstInfo.mBurstIndex, i));
                            } else {
                                TreeSet<BurstFilterCursor.BurstPosition> treeSet = new TreeSet<>();
                                treeSet.add(new BurstFilterCursor.BurstPosition(generateBurstInfo.mBurstIndex, i));
                                this.mResultPos.add(treeSet);
                                hashMap.put(Long.valueOf(j), treeSet);
                            }
                        }
                    }
                    TreeSet<BurstFilterCursor.BurstPosition> treeSet2 = new TreeSet<>();
                    treeSet2.add(new BurstFilterCursor.BurstPosition(0, i));
                    this.mResultPos.add(treeSet2);
                }
            }
            DefaultLogger.d("BurstKeyFilterCursor", "init cost %d, total %d, filter by length %d, valid burst %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Integer.valueOf(wrappedCursor.getCount()), 0, 0);
        }
    }

    public static String wrapKey(String str) {
        return BaseFileUtils.getFileNameWithoutExtension(str);
    }

    public static String getMimeType(String str) {
        return FileMimeUtils.getMimeType(str);
    }

    @Override // com.miui.gallery.util.BurstFilterCursor
    public boolean isBurstPosition(int i) {
        if (i < 0 || i > this.mResultPos.size()) {
            return false;
        }
        moveToPosition(i);
        String string = getString(getBurstKeyIndex());
        if (string == null) {
            return false;
        }
        return string.contains("_BURST") || string.contains("_TIMEBURST");
    }

    @Override // com.miui.gallery.util.BurstFilterCursor
    public boolean isTimeBurstPosition(int i) {
        if (i < 0 || i > this.mResultPos.size()) {
            return false;
        }
        moveToPosition(i);
        String string = getString(getBurstKeyIndex());
        return string != null && string.contains("_TIMEBURST");
    }
}
