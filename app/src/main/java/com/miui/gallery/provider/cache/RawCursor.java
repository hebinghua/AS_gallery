package com.miui.gallery.provider.cache;

import android.database.AbstractCursor;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.List;

/* loaded from: classes2.dex */
public class RawCursor extends AbstractCursor {
    public String[] mColumns;
    public List<? extends CacheItem> mContents;
    public int[] mIndexes;

    public RawCursor(List<? extends CacheItem> list, String[] strArr, CacheItem.ColumnMapper columnMapper) {
        this.mContents = list;
        this.mColumns = strArr;
        this.mIndexes = buildIndex(strArr, columnMapper);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getCount() {
        return this.mContents.size();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String[] getColumnNames() {
        return (String[]) this.mColumns.clone();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String getString(int i) {
        checkPosition();
        Object obj = this.mContents.get(((AbstractCursor) this).mPos).get(this.mIndexes[i], false);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public short getShort(int i) {
        checkPosition();
        Object obj = this.mContents.get(((AbstractCursor) this).mPos).get(this.mIndexes[i], false);
        if (obj == null) {
            return (short) 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }
        return Short.parseShort(obj.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getInt(int i) {
        checkPosition();
        Object obj = this.mContents.get(((AbstractCursor) this).mPos).get(this.mIndexes[i], false);
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return Integer.parseInt(obj.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public long getLong(int i) {
        checkPosition();
        Object obj = this.mContents.get(((AbstractCursor) this).mPos).get(this.mIndexes[i], false);
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        return Long.parseLong(obj.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public float getFloat(int i) {
        checkPosition();
        Object obj = this.mContents.get(((AbstractCursor) this).mPos).get(this.mIndexes[i], false);
        if (obj == null) {
            return 0.0f;
        }
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        return Float.parseFloat(obj.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public double getDouble(int i) {
        checkPosition();
        Object obj = this.mContents.get(((AbstractCursor) this).mPos).get(this.mIndexes[i], false);
        if (obj == null) {
            return SearchStatUtils.POW;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        return Double.parseDouble(obj.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean isNull(int i) {
        checkPosition();
        return -1 == i || this.mContents.get(((AbstractCursor) this).mPos).get(this.mIndexes[i], false) == null;
    }

    public static int[] buildIndex(String[] strArr, CacheItem.ColumnMapper columnMapper) {
        int[] iArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            iArr[i] = columnMapper.getIndex(strArr[i]);
        }
        return iArr;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public byte[] getBlob(int i) {
        checkPosition();
        Object obj = this.mContents.get(((AbstractCursor) this).mPos).get(this.mIndexes[i], false);
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        throw new IllegalArgumentException("not a blob");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String getColumnName(int i) {
        return this.mColumns[i];
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getType(int i) {
        checkPosition();
        return this.mContents.get(((AbstractCursor) this).mPos).getType(this.mIndexes[i]);
    }
}
