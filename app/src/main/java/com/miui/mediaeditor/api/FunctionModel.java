package com.miui.mediaeditor.api;

import android.net.Uri;
import android.os.Bundle;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public class FunctionModel extends ParcelableFunctionModel {
    public FunctionModel(String str, Uri uri, String str2, int i, int i2, ArrayList<String> arrayList, String str3, String str4, String str5, String str6, boolean z, Bundle bundle) {
        super(str, uri, str2, i, i2, arrayList, str3, str4, str5, str6, z, bundle);
    }

    public String getGuideActivity() {
        if (getExtraInfo() != null) {
            return getExtraInfo().getString("guide_activity", null);
        }
        return null;
    }
}
