package com.miui.gallery.cloud.policy;

import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloudcontrol.strategies.BackupPolicisStrategy;
import com.miui.gallery.preference.GalleryPreferences;

/* loaded from: classes.dex */
public class PolicyImpl implements IPolicy {
    public final BackupPolicisStrategy.PolicyWrapper mProxy;
    public final SyncType mType;

    public PolicyImpl(SyncType syncType, BackupPolicisStrategy.PolicyWrapper policyWrapper) {
        this.mType = syncType;
        this.mProxy = policyWrapper;
    }

    @Override // com.miui.gallery.cloud.policy.IPolicy
    public boolean isEnable() {
        BackupPolicisStrategy.PolicyWrapper policyWrapper = this.mProxy;
        if (policyWrapper != null) {
            return policyWrapper.isEnable();
        }
        return true;
    }

    @Override // com.miui.gallery.cloud.policy.IPolicy
    public SyncType getType() {
        return this.mType;
    }

    @Override // com.miui.gallery.cloud.policy.IPolicy
    public boolean isDisallowMetered() {
        if (!GalleryPreferences.Sync.getBackupOnlyInWifi()) {
            return false;
        }
        BackupPolicisStrategy.PolicyWrapper policyWrapper = this.mProxy;
        if (policyWrapper == null) {
            return true;
        }
        return policyWrapper.isDisallowMetered();
    }

    @Override // com.miui.gallery.cloud.policy.IPolicy
    public boolean isRequireCharging() {
        BackupPolicisStrategy.PolicyWrapper policyWrapper = this.mProxy;
        if (policyWrapper != null) {
            return policyWrapper.isRequireCharging();
        }
        return false;
    }

    @Override // com.miui.gallery.cloud.policy.IPolicy
    public boolean isIgnoreBatteryLow() {
        BackupPolicisStrategy.PolicyWrapper policyWrapper = this.mProxy;
        if (policyWrapper != null) {
            return policyWrapper.isIgnoreBattery();
        }
        return false;
    }
}
