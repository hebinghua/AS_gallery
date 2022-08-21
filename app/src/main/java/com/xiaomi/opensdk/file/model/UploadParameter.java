package com.xiaomi.opensdk.file.model;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class UploadParameter {
    public String mKssUploadString;
    public String mUploadId;

    public void setUploadId(String str) {
        this.mUploadId = str;
    }

    public String getUploadId() {
        return this.mUploadId;
    }

    public void setKssUploadString(String str) {
        this.mKssUploadString = str;
    }

    public JSONObject toJsonObject() throws JSONException {
        return new JSONObject(this.mKssUploadString);
    }
}
