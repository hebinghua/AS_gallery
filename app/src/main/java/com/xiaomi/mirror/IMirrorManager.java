package com.xiaomi.mirror;

import android.content.Intent;
import android.os.Bundle;
import android.view.InputEvent;
import android.view.View;

/* loaded from: classes3.dex */
public interface IMirrorManager {

    /* loaded from: classes3.dex */
    public interface OnMirrorMenuClickListener {
        boolean onMirrorMenuClick(View view);
    }

    String getWorkingBossName();

    boolean isCurrentClickFromMirror();

    boolean isEventFromMirror(InputEvent inputEvent);

    boolean isModelSupport();

    boolean isWorking();

    void notifyStartActivity(Intent intent);

    void notifyStartActivityFromRecents(int i, Bundle bundle);

    void onRemoteMenuActionCall(MirrorMenu mirrorMenu, int i);

    void setOnMirrorMenuClickListener(OnMirrorMenuClickListener onMirrorMenuClickListener);
}
