package com.miui.gallery.cloud;

import android.database.Cursor;

/* loaded from: classes.dex */
public class RegularPagingProvider {
    public int mCurrentQueryCount;
    public int mPage;
    public boolean mShouldContinue;
    public int mTotalLimit;
    public int mTotalQueriedCount;

    public int getLimitGrownSize() {
        return 1;
    }

    public RegularPagingProvider() {
        this(Integer.MAX_VALUE);
    }

    public RegularPagingProvider(int i) {
        this.mShouldContinue = true;
        this.mPage = 0;
        this.mTotalQueriedCount = 0;
        this.mCurrentQueryCount = 0;
        this.mTotalLimit = i;
    }

    public int getQueryLimit() {
        int min = Math.min(getLimitGrownSize() * 1000, this.mTotalLimit - this.mTotalQueriedCount);
        this.mCurrentQueryCount = min;
        return min;
    }

    public int getOffset() {
        return this.mTotalQueriedCount;
    }

    public boolean updateShouldContinue(Cursor cursor) {
        if (cursor == null) {
            this.mShouldContinue = false;
            return false;
        }
        return updateShouldContinue(cursor.getCount());
    }

    public boolean updateShouldContinue(long j) {
        boolean z = false;
        if (j == 0) {
            this.mShouldContinue = false;
        }
        int i = this.mTotalQueriedCount;
        int i2 = this.mCurrentQueryCount;
        int i3 = i + i2;
        this.mTotalQueriedCount = i3;
        if (j >= i2 && i3 < this.mTotalLimit) {
            z = true;
        }
        this.mShouldContinue = z;
        this.mPage++;
        return z;
    }
}
