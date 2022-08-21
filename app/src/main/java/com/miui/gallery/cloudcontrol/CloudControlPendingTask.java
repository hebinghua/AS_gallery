package com.miui.gallery.cloudcontrol;

import com.miui.gallery.push.PendingPullTask;

/* loaded from: classes.dex */
public class CloudControlPendingTask extends PendingPullTask {
    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireCharging() {
        return false;
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireDeviceIdle() {
        return false;
    }

    public CloudControlPendingTask(int i) {
        super(i);
    }
}
