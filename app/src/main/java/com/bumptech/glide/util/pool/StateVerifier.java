package com.bumptech.glide.util.pool;

/* loaded from: classes.dex */
public abstract class StateVerifier {
    public abstract void setRecycled(boolean z);

    public abstract void throwIfRecycled();

    public static StateVerifier newInstance() {
        return new DefaultStateVerifier();
    }

    public StateVerifier() {
    }

    /* loaded from: classes.dex */
    public static class DefaultStateVerifier extends StateVerifier {
        public volatile boolean isReleased;

        public DefaultStateVerifier() {
            super();
        }

        @Override // com.bumptech.glide.util.pool.StateVerifier
        public void throwIfRecycled() {
            if (!this.isReleased) {
                return;
            }
            throw new IllegalStateException("Already released");
        }

        @Override // com.bumptech.glide.util.pool.StateVerifier
        public void setRecycled(boolean z) {
            this.isReleased = z;
        }
    }
}
