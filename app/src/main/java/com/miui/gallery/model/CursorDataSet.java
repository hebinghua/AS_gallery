package com.miui.gallery.model;

import android.database.Cursor;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BurstFilterCursor;
import com.miui.gallery.util.BurstKeyFilterCursor;

/* loaded from: classes2.dex */
public abstract class CursorDataSet extends BaseDataSet {
    public Cursor mCursor;

    public int burstKeyIndex() {
        return -1;
    }

    public String localGroupIdColumnName() {
        return "localGroupId";
    }

    public String mimeTypeColumnName() {
        return "mime_type";
    }

    public CursorDataSet(Cursor cursor) {
        if (foldBurst()) {
            this.mCursor = getBurstCursor(cursor);
        } else {
            this.mCursor = cursor;
        }
    }

    public CursorDataSet(Cursor cursor, int i) {
        super(i);
        if (foldBurst() && cursor != null) {
            this.mCursor = getBurstCursor(cursor);
        } else {
            this.mCursor = cursor;
        }
    }

    @Override // com.miui.gallery.model.BaseDataSet, com.miui.gallery.projection.IConnectController.DataSet
    public int getCount() {
        if (isValidate()) {
            return this.mCursor.getCount();
        }
        return 0;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public void onRelease() {
        BaseMiscUtil.closeSilently(this.mCursor);
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public BaseDataItem createItem(int i) {
        if (isValidate(i)) {
            CloudItem cloudItem = new CloudItem();
            bindItem(cloudItem, i);
            return cloudItem;
        }
        return null;
    }

    public boolean isValidate() {
        Cursor cursor = this.mCursor;
        return cursor != null && !cursor.isClosed();
    }

    public boolean isValidate(int i) {
        return isValidate() && i >= 0 && i < this.mCursor.getCount();
    }

    public boolean moveToPosition(int i) {
        return isValidate(i) && this.mCursor.moveToPosition(i);
    }

    public final Cursor getBurstCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        if (cursor.getColumnIndex("burst_group_id") > 0) {
            return new BurstFilterCursor(cursor);
        }
        return new BurstKeyFilterCursor(cursor) { // from class: com.miui.gallery.model.CursorDataSet.1
            @Override // com.miui.gallery.util.BurstKeyFilterCursor
            public int getBurstKeyIndex() {
                return CursorDataSet.this.burstKeyIndex();
            }

            @Override // com.miui.gallery.util.BurstKeyFilterCursor
            public String getMimeTypeColumnName() {
                return CursorDataSet.this.mimeTypeColumnName();
            }

            @Override // com.miui.gallery.util.BurstKeyFilterCursor
            public String getLocalGroupIdColumnName() {
                return CursorDataSet.this.localGroupIdColumnName();
            }
        };
    }
}
