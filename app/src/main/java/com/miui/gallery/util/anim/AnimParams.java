package com.miui.gallery.util.anim;

/* loaded from: classes2.dex */
public class AnimParams {
    public float mAlpha;
    public Bounds mBounds;
    public long mDelay;
    public Move mMove;
    public float mScale;
    public Tint mTint;

    /* loaded from: classes2.dex */
    public static class Bounds {
    }

    /* loaded from: classes2.dex */
    public static class Move {
    }

    public AnimParams(float f, float f2, long j, Bounds bounds, Tint tint, Move move) {
        this.mAlpha = -1.0f;
        this.mScale = -1.0f;
        this.mDelay = -1L;
        this.mAlpha = f;
        this.mScale = f2;
        this.mDelay = j;
        this.mTint = tint;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public float getScale() {
        return this.mScale;
    }

    public Tint getTint() {
        return this.mTint;
    }

    public long getDelay() {
        return this.mDelay;
    }

    public Bounds getBounds() {
        return this.mBounds;
    }

    public Move getMove() {
        return this.mMove;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public Bounds mBounds;
        public Move mMove;
        public Tint mTint;
        public float mAlpha = -1.0f;
        public float mScale = -1.0f;
        public long mDelay = -1;

        public Builder setAlpha(float f) {
            this.mAlpha = f;
            return this;
        }

        public Builder setScale(float f) {
            this.mScale = f;
            return this;
        }

        public Builder setTint(float f, float f2, float f3, float f4) {
            this.mTint = new Tint(f, f2, f3, f4);
            return this;
        }

        public AnimParams build() {
            return new AnimParams(this.mAlpha, this.mScale, this.mDelay, this.mBounds, this.mTint, this.mMove);
        }
    }

    /* loaded from: classes2.dex */
    public static class Tint {
        public float a;
        public float b;
        public float g;
        public float r;

        public Tint(float f, float f2, float f3, float f4) {
            this.a = f;
            this.r = f2;
            this.g = f3;
            this.b = f4;
        }

        public float getA() {
            return this.a;
        }

        public float getR() {
            return this.r;
        }

        public float getG() {
            return this.g;
        }

        public float getB() {
            return this.b;
        }
    }
}
