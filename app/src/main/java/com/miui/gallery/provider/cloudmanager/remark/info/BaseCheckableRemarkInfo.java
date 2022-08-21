package com.miui.gallery.provider.cloudmanager.remark.info;

import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public abstract class BaseCheckableRemarkInfo extends BaseRemarkInfo {
    public String mDstFilePath;
    public String mScrFilePath;
    public CheckableValues mValues = new CheckableValues();

    /* loaded from: classes2.dex */
    public static class CheckableValues {
        public String[] mValuesArr = {"null", "null"};

        public CheckableValues setScrPath(String str) {
            this.mValuesArr[0] = str;
            return this;
        }

        public CheckableValues setDstPath(String str) {
            this.mValuesArr[1] = str;
            return this;
        }

        public String getScrPath() {
            return this.mValuesArr[0];
        }

        public String getDstPath() {
            return this.mValuesArr[1];
        }

        public String get() {
            return String.join("_<delimiter>_", this.mValuesArr);
        }

        public void parseValues(String str) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            this.mValuesArr = str.split("_<delimiter>_");
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.remark.info.ICheckable
    public void setCheckValues(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mContentValues = str;
        parseValues();
    }

    public void parseValues() {
        this.mValues.parseValues(this.mContentValues);
        this.mScrFilePath = this.mValues.getScrPath();
        this.mDstFilePath = this.mValues.getDstPath();
    }

    public String buildValues(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            str = "null";
        }
        this.mScrFilePath = str;
        if (TextUtils.isEmpty(str2)) {
            str2 = "null";
        }
        this.mDstFilePath = str2;
        return this.mValues.setScrPath(this.mScrFilePath).setDstPath(this.mDstFilePath).get();
    }

    @Override // com.miui.gallery.provider.cloudmanager.remark.info.ICheckable
    public boolean isUnHandleMedia(String str) {
        return TextUtils.equals(str, this.mScrFilePath) || (!"null".equals(this.mDstFilePath) && TextUtils.equals(str, this.mDstFilePath));
    }

    public String toString() {
        return getClass().getSimpleName() + "{mCloudId=" + this.mCloudId + ", mOperationType=" + this.mOperationType + ", mMethodType=" + getMethod(this.mMethodType) + ", mScrFilePath='" + this.mScrFilePath + CoreConstants.SINGLE_QUOTE_CHAR + ", mDstFilePath='" + this.mDstFilePath + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }
}
