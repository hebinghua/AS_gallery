package com.miui.gallery.cloudcontrol.strategies;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.util.BaseMiscUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class BackupPolicisStrategy extends BaseStrategy {
    @SerializedName("policies")
    private List<PolicyWrapper> mPolicies;
    public transient HashMap<String, PolicyWrapper> mPoliciesMap;

    public PolicyWrapper getPolicy(String str) {
        return this.mPoliciesMap.get(str);
    }

    public String toString() {
        return "BackupPolicisStrategy{mPolicies=" + this.mPolicies + '}';
    }

    /* loaded from: classes.dex */
    public static class PolicyWrapper {
        @SerializedName(CallMethod.RESULT_ENABLE_BOOLEAN)
        private boolean mEnable;
        @SerializedName("policy")
        private Policy mPolicy;
        @SerializedName(nexExportFormat.TAG_FORMAT_TYPE)
        private String mType;

        public String getType() {
            return this.mType;
        }

        public boolean isEnable() {
            return this.mEnable;
        }

        public boolean isDisallowMetered() {
            Policy policy = this.mPolicy;
            if (policy != null) {
                return policy.isDisallowMetered();
            }
            return true;
        }

        public boolean isRequireCharging() {
            Policy policy = this.mPolicy;
            if (policy != null) {
                return policy.isRequireCharging();
            }
            return false;
        }

        public boolean isIgnoreBattery() {
            Policy policy = this.mPolicy;
            if (policy != null) {
                return policy.isIgnoreBattery();
            }
            return false;
        }

        public String toString() {
            return "PolicyWrapper{mType='" + this.mType + ", mPolicy=" + this.mPolicy + '}';
        }
    }

    /* loaded from: classes.dex */
    public static class Policy {
        @SerializedName("disallow-metered")
        private boolean mDisallowMetered;
        @SerializedName("expedited")
        private boolean mExpedited;
        @SerializedName("ignore-battery")
        private boolean mIgnoreBattery;
        @SerializedName("manual")
        private boolean mManual;
        @SerializedName("require-charging")
        private boolean mRequireCharging;

        public boolean isDisallowMetered() {
            return this.mDisallowMetered;
        }

        public boolean isRequireCharging() {
            return this.mRequireCharging;
        }

        public boolean isIgnoreBattery() {
            return this.mIgnoreBattery;
        }

        public String toString() {
            return "Policy{mDisallowMetered='" + this.mDisallowMetered + ", mRequireCharging=" + this.mRequireCharging + ", mIgnoreBattery=" + this.mIgnoreBattery + ", mManual=" + this.mManual + ", mExpedited=" + this.mExpedited + '}';
        }
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        HashMap<String, PolicyWrapper> hashMap = this.mPoliciesMap;
        if (hashMap == null) {
            this.mPoliciesMap = new HashMap<>();
        } else {
            hashMap.clear();
        }
        if (BaseMiscUtil.isValid(this.mPolicies)) {
            for (PolicyWrapper policyWrapper : this.mPolicies) {
                if (!TextUtils.isEmpty(policyWrapper.getType())) {
                    this.mPoliciesMap.put(policyWrapper.getType(), policyWrapper);
                }
            }
        }
    }
}
