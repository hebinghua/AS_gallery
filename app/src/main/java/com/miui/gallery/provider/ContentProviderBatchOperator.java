package com.miui.gallery.provider;

import android.content.ContentProviderOperation;
import android.content.Context;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ContentProviderBatchOperator {
    public String mAuthority;
    public int mBatchSize;
    public final Object mLock;
    public ArrayList<ContentProviderOperation> mOperations;

    public ContentProviderBatchOperator(String str) {
        this(str, 50);
    }

    public ContentProviderBatchOperator(String str, int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("batchSize must be positive!");
        }
        this.mBatchSize = i;
        this.mAuthority = str;
        this.mOperations = new ArrayList<>(this.mBatchSize);
        this.mLock = new Object();
    }

    public void add(Context context, ContentProviderOperation contentProviderOperation) {
        synchronized (this.mLock) {
            this.mOperations.add(contentProviderOperation);
            if (this.mOperations.size() >= this.mBatchSize) {
                apply(context);
            }
        }
    }

    public void apply(Context context) {
        synchronized (this.mLock) {
            if (this.mOperations.size() > 0) {
                try {
                    context.getContentResolver().applyBatch(this.mAuthority, this.mOperations);
                } catch (Exception e) {
                    DefaultLogger.e("ContentProviderBatchOperator", e);
                }
                this.mOperations.clear();
            }
        }
    }
}
