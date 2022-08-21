package com.miui.gallery.scanner.core.bulkoperator;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import ch.qos.logback.classic.spi.CallerData;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MediaBulkDeleter {
    public final String mColumnName;
    public final Uri mUri;
    public final StringBuilder mWhereClause = new StringBuilder();
    public final ArrayList<String> mWhereArgs = new ArrayList<>(100);
    public final Object mLock = new Object();

    public MediaBulkDeleter(Uri uri, String str) {
        this.mUri = uri;
        this.mColumnName = str;
    }

    public void delete(Context context, long j) {
        synchronized (this.mLock) {
            if (this.mWhereClause.length() != 0) {
                this.mWhereClause.append(",");
            }
            this.mWhereClause.append(CallerData.NA);
            this.mWhereArgs.add(String.valueOf(j));
            if (this.mWhereArgs.size() > 100) {
                flush(context);
            }
        }
    }

    public void flush(Context context) {
        synchronized (this.mLock) {
            int size = this.mWhereArgs.size();
            if (size > 0) {
                ArrayList<String> arrayList = this.mWhereArgs;
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = this.mUri;
                DefaultLogger.i("MediaBulkDeleter", "rows deleted: %d", Integer.valueOf(contentResolver.delete(uri, this.mColumnName + " IN (" + this.mWhereClause.toString() + ")", (String[]) arrayList.toArray(new String[size]))));
                this.mWhereClause.setLength(0);
                this.mWhereArgs.clear();
            }
        }
    }
}
