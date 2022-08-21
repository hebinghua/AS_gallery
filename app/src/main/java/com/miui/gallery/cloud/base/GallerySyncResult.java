package com.miui.gallery.cloud.base;

/* loaded from: classes.dex */
public class GallerySyncResult<T> {
    public final GallerySyncCode code;
    public final T data;
    public final Exception exception;
    public final long retryAfter;

    public GallerySyncResult(Builder<T> builder) {
        this.code = builder.code;
        this.retryAfter = builder.retryAfter;
        this.exception = builder.exception;
        this.data = (T) builder.data;
    }

    public String toString() {
        return String.format("code %s, exception %s", this.code, this.exception);
    }

    /* loaded from: classes.dex */
    public static final class Builder<T> {
        public GallerySyncCode code;
        public T data;
        public Exception exception;
        public long retryAfter = -1;

        public Builder setCode(GallerySyncCode gallerySyncCode) {
            this.code = gallerySyncCode;
            return this;
        }

        public Builder setRetryAfter(long j) {
            this.retryAfter = j;
            return this;
        }

        public Builder setException(Exception exc) {
            this.exception = exc;
            return this;
        }

        public Builder setData(T t) {
            this.data = t;
            return this;
        }

        public GallerySyncResult build() {
            return new GallerySyncResult(this);
        }

        public Builder clone(GallerySyncResult<T> gallerySyncResult) {
            this.code = gallerySyncResult.code;
            this.retryAfter = gallerySyncResult.retryAfter;
            this.exception = gallerySyncResult.exception;
            this.data = gallerySyncResult.data;
            return this;
        }
    }
}
