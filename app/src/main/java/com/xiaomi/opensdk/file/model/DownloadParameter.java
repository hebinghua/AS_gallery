package com.xiaomi.opensdk.file.model;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class DownloadParameter {
    public String mKssDownloadString = null;
    public String mAwsDownloadString = null;

    public String getKssDownloadString() {
        return this.mKssDownloadString;
    }

    public void setKssDownloadString(String str) {
        this.mKssDownloadString = str;
    }

    public JSONObject toJsonObject() throws JSONException {
        return new JSONObject(this.mKssDownloadString);
    }
}
