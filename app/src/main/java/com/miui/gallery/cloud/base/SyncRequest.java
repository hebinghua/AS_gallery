package com.miui.gallery.cloud.base;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SyncRequest.kt */
/* loaded from: classes.dex */
public final class SyncRequest {
    public boolean isDelayUpload;
    public boolean isManual;
    public long syncReason;
    public SyncType syncType;

    public /* synthetic */ SyncRequest(SyncType syncType, long j, boolean z, boolean z2, DefaultConstructorMarker defaultConstructorMarker) {
        this(syncType, j, z, z2);
    }

    public SyncRequest(SyncType syncType, long j, boolean z, boolean z2) {
        this.syncType = syncType;
        this.syncReason = j;
        this.isDelayUpload = z;
        this.isManual = z2;
    }

    public final SyncType getSyncType() {
        return this.syncType;
    }

    public final long getSyncReason() {
        return this.syncReason;
    }

    public final boolean isDelayUpload() {
        return this.isDelayUpload;
    }

    public final boolean isManual() {
        return this.isManual;
    }

    public String toString() {
        return "SyncRequest {syncType=" + this.syncType + ", reason=" + ((Object) Long.toBinaryString(this.syncReason)) + ", delayUpload=" + this.isDelayUpload + ", manual=" + this.isManual + '}';
    }

    /* compiled from: SyncRequest.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        public boolean delayUpload;
        public boolean manual;
        public long syncReason;
        public SyncType syncType = SyncType.NORMAL;

        /* renamed from: setSyncType  reason: collision with other method in class */
        public final /* synthetic */ void m697setSyncType(SyncType syncType) {
            Intrinsics.checkNotNullParameter(syncType, "<set-?>");
            this.syncType = syncType;
        }

        /* renamed from: setSyncReason  reason: collision with other method in class */
        public final /* synthetic */ void m696setSyncReason(long j) {
            this.syncReason = j;
        }

        /* renamed from: setDelayUpload  reason: collision with other method in class */
        public final /* synthetic */ void m694setDelayUpload(boolean z) {
            this.delayUpload = z;
        }

        /* renamed from: setManual  reason: collision with other method in class */
        public final /* synthetic */ void m695setManual(boolean z) {
            this.manual = z;
        }

        public final Builder setSyncType(SyncType syncType) {
            Intrinsics.checkNotNullParameter(syncType, "syncType");
            m697setSyncType(syncType);
            return this;
        }

        public final Builder setSyncReason(long j) {
            m696setSyncReason(j);
            return this;
        }

        public final Builder setManual(boolean z) {
            m695setManual(z);
            return this;
        }

        public final Builder setDelayUpload(boolean z) {
            m694setDelayUpload(z);
            return this;
        }

        public final Builder cloneFrom(SyncRequest other) {
            Intrinsics.checkNotNullParameter(other, "other");
            this.syncType = other.getSyncType();
            this.syncReason = other.getSyncReason();
            this.delayUpload = other.isDelayUpload();
            this.manual = other.isManual();
            return this;
        }

        public final SyncRequest build() {
            return new SyncRequest(this.syncType, this.syncReason, this.delayUpload, this.manual, null);
        }
    }
}
