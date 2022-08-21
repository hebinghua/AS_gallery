package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.model.AlbumConstants;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class DeleteAlbumBaseDataProvider extends CursorTask2 {
    public final boolean isLocalAlbum;
    public final boolean isOnlyDeleteLocal;
    public final boolean isSystemAlbum;
    public Cursor mAlbumCursor;
    public final long mAlbumId;
    public String mCheckPath;
    public Cursor mCursor;
    public final int mDeleteReason;

    public DeleteAlbumBaseDataProvider(Context context, ArrayList<Long> arrayList, SupportSQLiteDatabase supportSQLiteDatabase, long j, int i, boolean z) {
        super(context, arrayList);
        this.mAlbumId = j;
        this.mDeleteReason = i;
        this.isOnlyDeleteLocal = z;
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("album").columns(AlbumConstants.DB_REAL_PROJECTION).selection("_id = ?", new String[]{String.valueOf(j)}).create());
        this.mAlbumCursor = query;
        if (query == null || query.getCount() != 1) {
            throw new IllegalArgumentException();
        }
        this.mAlbumCursor.moveToFirst();
        if (!this.mAlbumCursor.isNull(8)) {
            this.isLocalAlbum = false;
            this.isSystemAlbum = Album.isSystemAlbum(String.valueOf(this.mAlbumCursor.getLong(8)));
        } else {
            this.isLocalAlbum = true;
            this.isSystemAlbum = false;
        }
        Cursor query2 = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(Contracts.PROJECTION).selection("localGroupId=?", new String[]{String.valueOf(j)}).create());
        this.mCursor = query2;
        if (query2 == null || query2.isClosed() || this.mCursor.getCount() <= 0) {
            return;
        }
        this.mCursor.moveToFirst();
        while (true) {
            if (this.mCursor.isAfterLast()) {
                break;
            } else if (!TextUtils.isEmpty(this.mCursor.getString(7))) {
                this.mCheckPath = this.mCursor.getString(7);
                break;
            } else if (TextUtils.isEmpty(this.mCursor.getString(8))) {
                this.mCursor.moveToNext();
            } else {
                this.mCheckPath = this.mCursor.getString(8);
                break;
            }
        }
        this.mCursor.moveToFirst();
    }

    public boolean isSystemAlbum() {
        return this.isSystemAlbum;
    }

    public long getAlbumId() {
        return this.mAlbumId;
    }

    public String getCheckPath() {
        return this.mCheckPath;
    }

    public boolean isOnlyDeleteLocal() {
        return this.isOnlyDeleteLocal;
    }

    public int getDeleteReason() {
        return this.mDeleteReason;
    }

    public boolean isLocalAlbum() {
        return this.isLocalAlbum;
    }

    public Cursor getCursor() {
        return this.mCursor;
    }

    public Cursor getAlbumCursor() {
        return this.mAlbumCursor;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void release() {
        super.release();
        Cursor cursor = this.mCursor;
        if (cursor != null) {
            cursor.close();
        }
        Cursor cursor2 = this.mAlbumCursor;
        if (cursor2 != null) {
            cursor2.close();
        }
        this.mCursor = null;
        this.mAlbumCursor = null;
    }
}
