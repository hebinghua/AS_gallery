package com.miui.gallery.search.core.suggestion;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes2.dex */
public class RankInfo implements Serializable {
    private final String mName;
    private final String mOrder;
    private final String mStyle;
    private final String mTitle;

    public RankInfo(String str, String str2, String str3, String str4) {
        this.mTitle = str;
        this.mName = str2;
        this.mStyle = str3;
        this.mOrder = str4;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getName() {
        return this.mName;
    }

    public String getStyle() {
        return this.mStyle;
    }

    public String getOrder() {
        return this.mOrder;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RankInfo)) {
            return false;
        }
        RankInfo rankInfo = (RankInfo) obj;
        return TextUtils.equals(this.mName, rankInfo.getName()) && TextUtils.equals(this.mOrder, rankInfo.getOrder()) && TextUtils.equals(this.mStyle, rankInfo.getStyle()) && TextUtils.equals(this.mTitle, rankInfo.getTitle());
    }

    public int hashCode() {
        return Objects.hash(this.mTitle, this.mName, this.mStyle, this.mOrder);
    }

    public String toString() {
        return String.format("RankInfo: [name=%s, order=%s, title=%s, style=%s]", this.mName, this.mOrder, this.mTitle, this.mStyle);
    }
}
