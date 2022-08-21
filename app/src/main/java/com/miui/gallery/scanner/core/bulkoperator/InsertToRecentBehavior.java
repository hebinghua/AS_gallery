package com.miui.gallery.scanner.core.bulkoperator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import ch.qos.logback.classic.spi.CallerData;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.RecentDiscoveryMediaManager;
import com.miui.gallery.scanner.core.bulkoperator.IBulkInserter;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class InsertToRecentBehavior implements IBulkInserter.Behavior {
    public static final String[] PROJECTION = {j.c, "localGroupId", "localFile", "dateModified"};
    public List<String> mWhereArgs;
    public StringBuilder mWhereClause = new StringBuilder();

    public InsertToRecentBehavior(int i) {
        this.mWhereArgs = new ArrayList(i);
    }

    @Override // com.miui.gallery.scanner.core.bulkoperator.IBulkInserter.Behavior
    public void onInsert(Context context, ContentValues contentValues) {
        String asString = contentValues.getAsString("localFile");
        if (TextUtils.isEmpty(asString)) {
            return;
        }
        if (this.mWhereClause.length() != 0) {
            this.mWhereClause.append(",");
        }
        this.mWhereClause.append(CallerData.NA);
        this.mWhereArgs.add(asString);
    }

    @Override // com.miui.gallery.scanner.core.bulkoperator.IBulkInserter.Behavior
    public void onFlush(Context context) {
        if (this.mWhereArgs.size() <= 0) {
            return;
        }
        insertToRecent(context, (String[]) this.mWhereArgs.toArray(new String[0]), this.mWhereClause.toString());
        this.mWhereClause.setLength(0);
        this.mWhereArgs.clear();
    }

    public final void insertToRecent(Context context, String[] strArr, String str) {
        Cursor cursor = null;
        try {
            try {
                cursor = context.getContentResolver().query(GalleryContract.Cloud.CLOUD_URI, PROJECTION, "localFile IN (" + str + ")", strArr, "dateModified DESC ");
                if (cursor != null && cursor.getCount() > 0) {
                    RecentDiscoveryMediaManager.RecentMediaEntry[] recentMediaEntryArr = new RecentDiscoveryMediaManager.RecentMediaEntry[cursor.getCount()];
                    cursor.moveToFirst();
                    int i = 0;
                    while (!cursor.isAfterLast()) {
                        recentMediaEntryArr[i] = new RecentDiscoveryMediaManager.RecentMediaEntry(cursor.getLong(1), cursor.getLong(0), cursor.getString(2), cursor.getLong(3));
                        cursor.moveToNext();
                        i++;
                    }
                    RecentDiscoveryMediaManager.insertToRecent(context, recentMediaEntryArr);
                }
            } catch (Exception e) {
                DefaultLogger.e("InsertToRecentBehavior", e);
            }
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }
}
