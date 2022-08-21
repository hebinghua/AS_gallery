package com.miui.gallery.model.datalayer.repository.album.common;

import android.os.Bundle;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class QueryParam {
    public static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
    public final String[] mBindArgs;
    public final String[] mColumns;
    public final boolean mDistinct;
    public final Bundle mExtra;
    public final String mGroupBy;
    public final String mHaving;
    public final String mLimit;
    public final String mOrderBy;
    public final String mSelection;

    public QueryParam(boolean z, String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, String str5, Bundle bundle) {
        this.mDistinct = z;
        this.mColumns = strArr;
        this.mSelection = str;
        this.mBindArgs = strArr2;
        this.mGroupBy = str2;
        this.mHaving = str3;
        this.mOrderBy = str4;
        this.mLimit = str5;
        this.mExtra = bundle;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public String[] mBindArgs;
        public Bundle mExtra;
        public String mSelection;
        public boolean mDistinct = false;
        public String[] mColumns = null;
        public String mGroupBy = null;
        public String mHaving = null;
        public String mOrderBy = null;
        public String mLimit = null;

        public Builder columns(String[] strArr) {
            this.mColumns = strArr;
            return this;
        }

        public Builder selection(String str, String[] strArr) {
            this.mSelection = str;
            this.mBindArgs = strArr;
            return this;
        }

        public Builder limitByNum(Integer num) {
            if (num == null) {
                return this;
            }
            this.mLimit = String.valueOf(num.intValue());
            return this;
        }

        public Builder extra(Bundle bundle) {
            this.mExtra = bundle;
            return this;
        }

        public QueryParam build() {
            return new QueryParam(this.mDistinct, this.mColumns, this.mSelection, this.mBindArgs, this.mGroupBy, this.mHaving, this.mOrderBy, this.mLimit, this.mExtra);
        }
    }

    public boolean isDistinct() {
        return this.mDistinct;
    }

    public String[] getColumns() {
        return this.mColumns;
    }

    public String getSelection() {
        return this.mSelection;
    }

    public String[] getBindArgs() {
        return this.mBindArgs;
    }

    public String getGroupBy() {
        return this.mGroupBy;
    }

    public String getHaving() {
        return this.mHaving;
    }

    public String getOrderBy() {
        return this.mOrderBy;
    }

    public String getLimit() {
        return this.mLimit;
    }

    public Bundle getExtra() {
        return this.mExtra;
    }
}
