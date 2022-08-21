package com.miui.gallery.scanner.core.bulkoperator;

import android.content.ContentValues;
import android.content.Context;
import java.util.List;

/* loaded from: classes2.dex */
public interface IBulkInserter {

    /* loaded from: classes2.dex */
    public interface Behavior {
        void onFlush(Context context);

        void onInsert(Context context, ContentValues contentValues);
    }

    void flush(Context context);

    List<ContentValues> getValues();

    void insert(Context context, ContentValues contentValues);
}
