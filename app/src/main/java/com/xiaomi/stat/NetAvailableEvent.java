package com.xiaomi.stat;

/* loaded from: classes3.dex */
public class NetAvailableEvent {
    public static final int RESULT_TYPE_0 = 0;
    public static final int RESULT_TYPE_1 = 1;
    public static final int RESULT_TYPE_2 = 2;
    private String a;
    private int b;
    private int c;
    private String d;
    private int e;
    private long f;
    private int g;
    private String h;

    public String getFlag() {
        return this.a;
    }

    public int getResponseCode() {
        return this.b;
    }

    public int getStatusCode() {
        return this.c;
    }

    public String getException() {
        return this.d;
    }

    public int getResultType() {
        return this.e;
    }

    public long getRequestStartTime() {
        return this.f;
    }

    public int getRetryCount() {
        return this.g;
    }

    public String getExt() {
        return this.h;
    }

    private NetAvailableEvent(Builder builder) {
        this.a = builder.a;
        this.b = builder.b;
        this.c = builder.c;
        this.d = builder.d;
        this.e = builder.e;
        this.f = builder.f;
        this.g = builder.g;
        this.h = builder.h;
    }

    /* loaded from: classes3.dex */
    public static final class Builder {
        private String a;
        private int b;
        private int c;
        private String d;
        private int e;
        private long f;
        private int g;
        private String h;

        public Builder flag(String str) {
            this.a = str;
            return this;
        }

        public Builder responseCode(int i) {
            this.b = i;
            return this;
        }

        public Builder statusCode(int i) {
            this.c = i;
            return this;
        }

        public Builder exception(String str) {
            this.d = str;
            return this;
        }

        public Builder resultType(int i) {
            this.e = i;
            return this;
        }

        public Builder requestStartTime(long j) {
            this.f = j;
            return this;
        }

        public Builder retryCount(int i) {
            this.g = i;
            return this;
        }

        public Builder ext(String str) {
            this.h = str;
            return this;
        }

        public NetAvailableEvent build() {
            return new NetAvailableEvent(this);
        }
    }
}
