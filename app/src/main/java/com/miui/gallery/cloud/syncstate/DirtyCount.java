package com.miui.gallery.cloud.syncstate;

import java.util.Objects;

/* loaded from: classes.dex */
public class DirtyCount {
    public int mOversizedImageCount;
    public int mOversizedVideoCount;
    public int mSyncableImageCount;
    public int mSyncableVideoCount;

    public void setSyncableImageCount(int i) {
        this.mSyncableImageCount = i;
    }

    public void setSyncableVideoCount(int i) {
        this.mSyncableVideoCount = i;
    }

    public void setOversizedImageCount(int i) {
        this.mOversizedImageCount = i;
    }

    public void setOversizedVideoCount(int i) {
        this.mOversizedVideoCount = i;
    }

    public int getSyncableCount() {
        return this.mSyncableImageCount + this.mSyncableVideoCount;
    }

    public int getOversizedCount() {
        return this.mOversizedImageCount + this.mOversizedVideoCount;
    }

    public int getTotalImageCount() {
        return this.mSyncableImageCount + this.mOversizedImageCount;
    }

    public int getTotalVideoCount() {
        return this.mSyncableVideoCount + this.mOversizedVideoCount;
    }

    public DirtyCount plus(DirtyCount dirtyCount) {
        if (dirtyCount != null) {
            this.mSyncableImageCount += dirtyCount.mSyncableImageCount;
            this.mSyncableVideoCount += dirtyCount.mSyncableVideoCount;
            this.mOversizedImageCount += dirtyCount.mOversizedImageCount;
            this.mOversizedVideoCount += dirtyCount.mOversizedVideoCount;
        }
        return this;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mSyncableImageCount), Integer.valueOf(this.mSyncableVideoCount), Integer.valueOf(this.mOversizedImageCount), Integer.valueOf(this.mOversizedVideoCount));
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DirtyCount)) {
            return false;
        }
        DirtyCount dirtyCount = (DirtyCount) obj;
        return this.mSyncableImageCount == dirtyCount.mSyncableImageCount && this.mSyncableVideoCount == dirtyCount.mSyncableVideoCount && this.mOversizedImageCount == dirtyCount.mOversizedImageCount && this.mOversizedVideoCount == dirtyCount.mOversizedVideoCount;
    }

    public String toString() {
        return "DirtyCount{mSyncableImageCount=" + this.mSyncableImageCount + ", mSyncableVideoCount=" + this.mSyncableVideoCount + ", mOversizedImageCount=" + this.mOversizedImageCount + ", mOversizedVideoCount=" + this.mOversizedVideoCount + '}';
    }
}
