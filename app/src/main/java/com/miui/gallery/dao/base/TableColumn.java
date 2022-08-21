package com.miui.gallery.dao.base;

/* loaded from: classes.dex */
public class TableColumn {
    public final String check;
    public final String collateType;
    public final String defaultValue;
    public final boolean isUnique;
    public final String name;
    public final String type;

    public TableColumn(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.defaultValue = builder.defaultValue;
        this.isUnique = builder.isUnique;
        this.check = builder.check;
        this.collateType = builder.collateType;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public String check;
        public String collateType;
        public String defaultValue;
        public boolean isUnique;
        public String name;
        public String type;

        public Builder setName(String str) {
            this.name = str;
            return this;
        }

        public Builder setType(String str) {
            this.type = str;
            return this;
        }

        public Builder setDefaultValue(String str) {
            this.defaultValue = str;
            return this;
        }

        public Builder setUnique(boolean z) {
            this.isUnique = z;
            return this;
        }

        public TableColumn build() {
            if (this.name == null || this.type == null) {
                throw new IllegalArgumentException();
            }
            return new TableColumn(this);
        }
    }
}
