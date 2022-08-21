package com.miui.gallery.cloud.policy;

import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.strategies.BackupPolicisStrategy;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class SyncPolicyManager {
    public Map<SyncType, IPolicy> mPolicies;

    /* loaded from: classes.dex */
    public static class Singleton {
        public static SyncPolicyManager sInstance = new SyncPolicyManager();
    }

    public static SyncPolicyManager getInstance() {
        return Singleton.sInstance;
    }

    public SyncPolicyManager() {
        SyncType[] values;
        this.mPolicies = new HashMap();
        BackupPolicisStrategy backupPolicisStrategy = (BackupPolicisStrategy) CloudControlManager.getInstance().queryFeatureStrategy("backup-policies");
        DefaultLogger.d("SyncPolicyManager", "policies %s", backupPolicisStrategy);
        for (SyncType syncType : SyncType.values()) {
            PolicyImpl policyImpl = new PolicyImpl(syncType, backupPolicisStrategy == null ? null : backupPolicisStrategy.getPolicy(syncType.getIdentifier()));
            addPolicy(policyImpl.getType(), policyImpl);
        }
    }

    public void addPolicy(SyncType syncType, IPolicy iPolicy) {
        if (iPolicy == null) {
            return;
        }
        this.mPolicies.put(syncType, iPolicy);
    }

    public IPolicy getPolicy(SyncType syncType) {
        return this.mPolicies.get(syncType);
    }
}
