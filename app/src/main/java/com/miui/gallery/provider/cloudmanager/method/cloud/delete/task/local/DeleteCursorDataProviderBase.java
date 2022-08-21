package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.local;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class DeleteCursorDataProviderBase extends CursorTask2 {
    public Cursor mCursor;
    public int mDeleteReason;
    public long mId;

    public DeleteCursorDataProviderBase(Context context, ArrayList<Long> arrayList, SupportSQLiteDatabase supportSQLiteDatabase, long j, int i) {
        super(context, arrayList);
        this.mDeleteReason = i;
        this.mId = j;
        this.mCursor = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(Contracts.PROJECTION).selection("_id=?", new String[]{String.valueOf(j)}).create());
    }

    public Cursor getCursor() {
        return this.mCursor;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void release() {
        super.release();
        Cursor cursor = this.mCursor;
        if (cursor != null) {
            cursor.close();
        }
        this.mCursor = null;
    }
}
