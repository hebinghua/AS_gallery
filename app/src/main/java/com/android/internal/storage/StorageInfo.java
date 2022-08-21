package com.android.internal.storage;

/* loaded from: classes.dex */
public class StorageInfo {
    public final boolean isMounted;
    public final boolean isPrimary;
    public final boolean isSd;
    public final boolean isUsb;
    public final boolean isVisible;
    public final boolean isXspace;
    public final String mDescription;
    public final String mPath;
    public final String mUuid;
    public final Object mWrapped;

    public StorageInfo(Builder builder) {
        this.mPath = builder.path;
        this.mDescription = builder.description;
        this.isMounted = builder.isMounted;
        this.isPrimary = builder.isPrimary;
        this.isSd = builder.isSd;
        this.isUsb = builder.isUsb;
        this.isXspace = builder.isXspace;
        this.isVisible = builder.isVisible;
        this.mWrapped = builder.wrapped;
        this.mUuid = builder.uuid;
    }

    public String getPath() {
        return this.mPath;
    }

    public String getDescription() {
        String str = this.mDescription;
        return str == null ? "" : str;
    }

    public boolean isMounted() {
        return this.isMounted;
    }

    public boolean isPrimary() {
        return this.isPrimary;
    }

    public boolean isSd() {
        return this.isSd;
    }

    public boolean isUsb() {
        return this.isUsb;
    }

    public boolean isXspace() {
        return this.isXspace;
    }

    public String getUUid() {
        return this.mUuid;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public String description;
        public boolean isMounted;
        public boolean isPrimary;
        public boolean isSd;
        public boolean isUsb;
        public boolean isVisible;
        public boolean isXspace;
        public String path;
        public String uuid;
        public Object wrapped;

        public Builder(String str) {
            this.path = str;
        }

        public Builder setDescription(String str) {
            this.description = str;
            return this;
        }

        public Builder setMounted(boolean z) {
            this.isMounted = z;
            return this;
        }

        public Builder setPrimary(boolean z) {
            this.isPrimary = z;
            return this;
        }

        public Builder setSd(boolean z) {
            this.isSd = z;
            return this;
        }

        public Builder setUsb(boolean z) {
            this.isUsb = z;
            return this;
        }

        public Builder setXspace(boolean z) {
            this.isXspace = z;
            return this;
        }

        public Builder setVisible(boolean z) {
            this.isVisible = z;
            return this;
        }

        public Builder setWrapped(Object obj) {
            this.wrapped = obj;
            return this;
        }

        public Builder setUuid(String str) {
            this.uuid = str;
            return this;
        }

        public StorageInfo build() {
            return new StorageInfo(this);
        }
    }

    public String toString() {
        return "Storage{ mPath=" + this.mPath + ",mDescription=" + this.mDescription + ",isMounted=" + this.isMounted + ",isPrimary=" + this.isPrimary + ",isSD=" + this.isSd + ",isUsb=" + this.isUsb + ",isXspace=" + this.isXspace + ",mUuid=" + this.mUuid + ", }";
    }
}
