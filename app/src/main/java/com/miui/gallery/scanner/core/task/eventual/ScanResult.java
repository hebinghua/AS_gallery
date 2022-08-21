package com.miui.gallery.scanner.core.task.eventual;

import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.model.IAlbumEntry;
import java.util.function.Supplier;

/* loaded from: classes2.dex */
public class ScanResult {
    public IAlbumEntry albumEntry;
    public long mediaId;
    public ScanContracts$ScanResultReason reasonCode;
    public Supplier<Result> resultSupplier;
    public static Supplier<Result> FAILED = ScanResult$$ExternalSyntheticLambda0.INSTANCE;
    public static Supplier<Result> SUCCESS = ScanResult$$ExternalSyntheticLambda2.INSTANCE;
    public static Supplier<Result> RETRY = ScanResult$$ExternalSyntheticLambda1.INSTANCE;

    /* loaded from: classes2.dex */
    public enum Result {
        SUCCESS,
        FAILED,
        RETRY
    }

    public ScanResult(Builder builder) {
        this.mediaId = builder.mediaId;
        this.albumEntry = builder.albumEntry;
        this.resultSupplier = builder.resultSupplier;
        this.reasonCode = builder.reasonCode;
    }

    public static Builder success(ScanContracts$ScanResultReason scanContracts$ScanResultReason) {
        return new Builder().setResultSupplier(SUCCESS).setReasonCode(scanContracts$ScanResultReason);
    }

    public static Builder failed(ScanContracts$ScanResultReason scanContracts$ScanResultReason) {
        return new Builder().setResultSupplier(FAILED).setReasonCode(scanContracts$ScanResultReason);
    }

    public static Builder retry(ScanContracts$ScanResultReason scanContracts$ScanResultReason) {
        return new Builder().setResultSupplier(RETRY).setReasonCode(scanContracts$ScanResultReason);
    }

    public long getMediaId() {
        return this.mediaId;
    }

    public IAlbumEntry getAlbumEntry() {
        return this.albumEntry;
    }

    public Result getResult() {
        return this.resultSupplier.get();
    }

    public ScanContracts$ScanResultReason getReasonCode() {
        return this.reasonCode;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public Supplier<Result> resultSupplier;
        public long mediaId = -1;
        public IAlbumEntry albumEntry = null;
        public ScanContracts$ScanResultReason reasonCode = ScanContracts$ScanResultReason.DEFAULT;

        public Builder cloneFrom(ScanResult scanResult) {
            this.mediaId = scanResult.mediaId;
            this.albumEntry = scanResult.albumEntry;
            this.resultSupplier = scanResult.resultSupplier;
            this.reasonCode = scanResult.reasonCode;
            return this;
        }

        public Builder setMediaId(long j) {
            this.mediaId = j;
            return this;
        }

        public Builder setAlbumEntry(IAlbumEntry iAlbumEntry) {
            this.albumEntry = iAlbumEntry;
            return this;
        }

        public Builder setResultSupplier(Supplier<Result> supplier) {
            this.resultSupplier = supplier;
            return this;
        }

        public Builder setReasonCode(ScanContracts$ScanResultReason scanContracts$ScanResultReason) {
            this.reasonCode = scanContracts$ScanResultReason;
            return this;
        }

        public ScanResult build() {
            return new ScanResult(this);
        }
    }
}
