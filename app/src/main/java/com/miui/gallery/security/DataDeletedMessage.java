package com.miui.gallery.security;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class DataDeletedMessage {
    @SerializedName("deleteInfo")
    private DeleteInfo mDeleteInfo;
    public transient Map<String, String> mDeleteNames;
    @SerializedName("expireTime")
    private long mExpireTime;
    public transient String mJsonMsg;

    public long getDeleteTime() {
        DeleteInfo deleteInfo = this.mDeleteInfo;
        return deleteInfo != null ? deleteInfo.mDeleteTime : System.currentTimeMillis();
    }

    public String getDeviceId() {
        DeleteInfo deleteInfo = this.mDeleteInfo;
        if (deleteInfo != null) {
            return deleteInfo.mDeviceId;
        }
        return null;
    }

    public String getDeviceTag() {
        DeleteInfo deleteInfo = this.mDeleteInfo;
        if (deleteInfo != null) {
            return deleteInfo.mDeviceTag;
        }
        return null;
    }

    public String getDeviceName() {
        Map<String, String> map = this.mDeleteNames;
        if (map == null || map.size() <= 0) {
            return "";
        }
        String str = this.mDeleteNames.get(String.format(Locale.US, "%s_%s", Locale.getDefault().getLanguage(), BaseBuildUtil.getRegion()));
        if (str == null) {
            str = this.mDeleteNames.get("default");
        }
        if (str == null) {
            str = this.mDeleteNames.values().iterator().next();
        }
        return str != null ? str : "";
    }

    public String getTag() {
        DeleteInfo deleteInfo = this.mDeleteInfo;
        if (deleteInfo != null) {
            return deleteInfo.mTag;
        }
        return null;
    }

    public void setJsonMsg(String str) {
        this.mJsonMsg = str;
    }

    public String getJsonMsg() {
        return this.mJsonMsg;
    }

    public String getDeleteInfoJson() {
        DeleteInfo deleteInfo = this.mDeleteInfo;
        if (deleteInfo != null) {
            return deleteInfo.mJsonMsg;
        }
        return null;
    }

    public void setDeviceNames(Map<String, String> map) {
        this.mDeleteNames = map;
    }

    public boolean isValid() {
        return this.mExpireTime > System.currentTimeMillis();
    }

    public String toString() {
        return "DataDeletedMessage{mExpireTime='" + this.mExpireTime + CoreConstants.SINGLE_QUOTE_CHAR + ", mDeviceInfo='" + this.mDeleteInfo + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }

    /* loaded from: classes2.dex */
    public static class DeleteInfo {
        @SerializedName("lastDeleteTime")
        private long mDeleteTime;
        @SerializedName("fid")
        public String mDeviceFid;
        @SerializedName("devId")
        public String mDeviceId;
        @SerializedName("devTag")
        public String mDeviceTag;
        public transient String mJsonMsg;
        @SerializedName(nexExportFormat.TAG_FORMAT_TAG)
        public String mTag;

        public void setJsonMsg(String str) {
            this.mJsonMsg = str;
        }

        public String toString() {
            return "DeleteInfo{mDeleteTime='" + this.mDeleteTime + CoreConstants.SINGLE_QUOTE_CHAR + "mDeviceId='" + this.mDeviceId + CoreConstants.SINGLE_QUOTE_CHAR + "mDeviceTag='" + this.mDeviceTag + CoreConstants.SINGLE_QUOTE_CHAR + ", mDeviceFid='" + this.mDeviceFid + CoreConstants.SINGLE_QUOTE_CHAR + ", mTag='" + this.mTag + CoreConstants.SINGLE_QUOTE_CHAR + '}';
        }

        public static Map<String, String> parseDeviceNames(String str) {
            HashMap hashMap = new HashMap();
            try {
                JSONObject jSONObject = new JSONObject(str);
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    hashMap.put(next, jSONObject.optString(next));
                }
            } catch (JSONException e) {
                DefaultLogger.e("DataDeletedMessage", e);
            }
            return hashMap;
        }
    }

    public static DataDeletedMessage convert(String str, boolean z) {
        try {
            DataDeletedMessage dataDeletedMessage = (DataDeletedMessage) GsonUtils.fromJson(str, new TypeToken<DataDeletedMessage>() { // from class: com.miui.gallery.security.DataDeletedMessage.1
            }.getType());
            dataDeletedMessage.setJsonMsg(str);
            if (z) {
                appendDeleteInfo(dataDeletedMessage, str);
            }
            return dataDeletedMessage;
        } catch (Exception e) {
            DefaultLogger.e("DataDeletedMessage", e);
            return null;
        }
    }

    public static void appendDeleteInfo(DataDeletedMessage dataDeletedMessage, String str) {
        try {
            JSONObject optJSONObject = new JSONObject(str).optJSONObject("deleteInfo");
            dataDeletedMessage.mDeleteInfo.setJsonMsg(optJSONObject.toString());
            dataDeletedMessage.setDeviceNames(DeleteInfo.parseDeviceNames(optJSONObject.optString("devNameInfo")));
        } catch (JSONException e) {
            DefaultLogger.e("DataDeletedMessage", e);
        }
    }
}
