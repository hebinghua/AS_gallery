package com.miui.gallery.vlog.ratio;

import android.text.TextUtils;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.sdk.interfaces.BaseVlogManager;
import com.miui.gallery.vlog.sdk.interfaces.IRatioManager;
import com.xiaomi.milab.videosdk.XmsTimeline;

/* loaded from: classes2.dex */
public class MiVideoRatioManager extends BaseVlogManager implements IRatioManager {
    public Callback mCallback;
    public String mRatioLabel;

    /* loaded from: classes2.dex */
    public interface Callback {
        void onCallBack(String str, int i);
    }

    public MiVideoRatioManager(MiVideoSdkManager miVideoSdkManager, XmsTimeline xmsTimeline) {
        super(miVideoSdkManager, xmsTimeline);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IRatioManager
    public void setLiveWindowRatio(String str, int i) {
        this.mRatioLabel = str;
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCallBack(str, i);
        } else {
            setRatioWithNoRatioFragment(i);
        }
    }

    public void setLiveWindowRatio(int i) {
        if (i == -1) {
            return;
        }
        this.mSdkManager.setLiveWindowRatio(i);
    }

    public final void setRatioWithNoRatioFragment(int i) {
        if (i == -1) {
            if (!this.mSdkManager.isSingleVideoEdit()) {
                return;
            }
            this.mSdkManager.setLiveWindowRatio(5);
            return;
        }
        setLiveWindowRatio(i);
    }

    public boolean hasTemplate() {
        return !TextUtils.isEmpty(this.mRatioLabel);
    }

    public void setRatioType(int i) {
        this.mSdkManager.setRatioType(i);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
