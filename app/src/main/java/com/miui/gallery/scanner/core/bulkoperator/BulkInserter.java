package com.miui.gallery.scanner.core.bulkoperator;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.miui.gallery.scanner.core.bulkoperator.IBulkInserter;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class BulkInserter implements IBulkInserter {
    public final int mBulkCount;
    public Uri mUri;
    public List<ContentValues> mValues;
    public List<IBulkInserter.Behavior> mBehaviors = new LinkedList();
    public final Object mLock = new Object();

    public BulkInserter(Uri uri, int i) {
        this.mUri = uri;
        this.mBulkCount = i;
        this.mValues = new ArrayList(i);
    }

    public BulkInserter(Uri uri, int i, IBulkInserter.Behavior... behaviorArr) {
        this.mUri = uri;
        this.mBulkCount = i;
        this.mValues = new ArrayList(i);
        for (IBulkInserter.Behavior behavior : behaviorArr) {
            setupBehavior(behavior);
        }
    }

    @Override // com.miui.gallery.scanner.core.bulkoperator.IBulkInserter
    public final void insert(Context context, ContentValues contentValues) {
        if (contentValues == null) {
            return;
        }
        synchronized (this.mLock) {
            this.mValues.add(contentValues);
            for (IBulkInserter.Behavior behavior : this.mBehaviors) {
                behavior.onInsert(context, contentValues);
            }
            if (this.mValues.size() >= this.mBulkCount) {
                flush(context);
            }
        }
    }

    @Override // com.miui.gallery.scanner.core.bulkoperator.IBulkInserter
    public final void flush(Context context) {
        synchronized (this.mLock) {
            if (this.mValues.size() <= 0) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            int bulkInsert = context.getContentResolver().bulkInsert(this.mUri, (ContentValues[]) this.mValues.toArray(new ContentValues[0]));
            for (IBulkInserter.Behavior behavior : this.mBehaviors) {
                behavior.onFlush(context);
            }
            this.mValues.clear();
            DefaultLogger.d("BulkInserter", "bulk insert [%d] items to [%s], cost [%d] ms.", Integer.valueOf(bulkInsert), this.mUri, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        }
    }

    @Override // com.miui.gallery.scanner.core.bulkoperator.IBulkInserter
    public List<ContentValues> getValues() {
        return this.mValues;
    }

    public void setupBehavior(IBulkInserter.Behavior behavior) {
        this.mBehaviors.add(behavior);
    }
}
