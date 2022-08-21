package com.miui.gallery.cloudcontrol;

import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.xiaomi.mirror.synergy.CallMethod;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class FeatureProfile {
    private static final String JSON_TAG_NAME = "name";
    private static final String JSON_TAG_STATUS = "status";
    private static final String JSON_TAG_STRATEGY = "strategy";
    @SerializedName("name")
    public String mName;
    @SerializedName("status")
    public String mStatus;
    @SerializedName(JSON_TAG_STRATEGY)
    public String mStrategy;

    /* loaded from: classes.dex */
    public enum Status {
        ENABLE(CallMethod.RESULT_ENABLE_BOOLEAN),
        DISABLE("disable"),
        REMOVE("remove"),
        UNAVAILABLE("unavailable");
        
        private final String value;

        Status(String str) {
            this.value = str;
        }

        public String getValue() {
            return this.value;
        }

        public static Status fromValue(String str) {
            Status[] values;
            for (Status status : values()) {
                if (status.value.equals(str)) {
                    return status;
                }
            }
            return UNAVAILABLE;
        }
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public String getStatus() {
        return this.mStatus;
    }

    public void setStatus(String str) {
        this.mStatus = str;
    }

    public String getStrategy() {
        return this.mStrategy;
    }

    public void setStrategy(String str) {
        this.mStrategy = str;
    }

    public int hashCode() {
        String str = this.mName;
        int hashCode = str != null ? 527 + str.hashCode() : 17;
        String str2 = this.mStatus;
        if (str2 != null) {
            hashCode = (hashCode * 31) + str2.hashCode();
        }
        String str3 = this.mStrategy;
        return str3 != null ? (hashCode * 31) + str3.hashCode() : hashCode;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FeatureProfile)) {
            return false;
        }
        FeatureProfile featureProfile = (FeatureProfile) obj;
        return TextUtils.equals(this.mName, featureProfile.getName()) && TextUtils.equals(this.mStrategy, featureProfile.getStrategy()) && TextUtils.equals(this.mStatus, featureProfile.getStatus());
    }

    public String toString() {
        return "FeatureProfile{mName='" + this.mName + CoreConstants.SINGLE_QUOTE_CHAR + ", mStatus='" + this.mStatus + CoreConstants.SINGLE_QUOTE_CHAR + ", mStrategy='" + this.mStrategy + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }

    /* loaded from: classes.dex */
    public static class Deserializer implements JsonDeserializer<FeatureProfile> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        /* renamed from: deserialize */
        public FeatureProfile mo1735deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            FeatureProfile featureProfile = new FeatureProfile();
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            if (asJsonObject.has("name")) {
                featureProfile.setName(asJsonObject.getAsJsonPrimitive("name").getAsString());
            }
            if (asJsonObject.has(FeatureProfile.JSON_TAG_STRATEGY)) {
                JsonElement jsonElement2 = asJsonObject.get(FeatureProfile.JSON_TAG_STRATEGY);
                if (jsonElement2.isJsonPrimitive()) {
                    featureProfile.setStrategy(jsonElement2.getAsString());
                } else {
                    featureProfile.setStrategy(jsonElement2.toString());
                }
            }
            if (asJsonObject.has("status")) {
                featureProfile.setStatus(asJsonObject.getAsJsonPrimitive("status").getAsString());
            }
            return featureProfile;
        }
    }
}
