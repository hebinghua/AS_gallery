package com.miui.gallery.cloudcontrol.strategies;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class IgnoreAlbumsStrategy extends BaseStrategy {
    @SerializedName("patterns")
    private List<String> mPatterns;
    @SerializedName("version")
    private int mVersion;

    public String toString() {
        return "IgnoreAlbumsStrategy{mVersion" + this.mVersion + "mPatterns=" + this.mPatterns + '}';
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        if (BaseMiscUtil.isValid(this.mPatterns)) {
            ArrayList arrayList = new ArrayList();
            for (String str : this.mPatterns) {
                if (!TextUtils.isEmpty(str)) {
                    arrayList.add(str);
                }
            }
            if (arrayList.size() >= this.mPatterns.size()) {
                return;
            }
            this.mPatterns.clear();
            this.mPatterns.addAll(arrayList);
        }
    }
}
