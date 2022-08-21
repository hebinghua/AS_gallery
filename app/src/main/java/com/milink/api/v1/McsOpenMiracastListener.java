package com.milink.api.v1;

import android.os.RemoteException;
import com.milink.api.v1.aidl.IMcsOpenMiracastListener;

/* loaded from: classes.dex */
public class McsOpenMiracastListener extends IMcsOpenMiracastListener.Stub {
    public MiLinkClientOpenMiracastListener mOpenMiracastListener;

    @Override // com.milink.api.v1.aidl.IMcsOpenMiracastListener
    public void openSuccess(String str, String str2, String str3) throws RemoteException {
        MiLinkClientOpenMiracastListener miLinkClientOpenMiracastListener = this.mOpenMiracastListener;
        if (miLinkClientOpenMiracastListener != null) {
            miLinkClientOpenMiracastListener.openSuccess(str, str2, str3);
        }
    }

    @Override // com.milink.api.v1.aidl.IMcsOpenMiracastListener
    public void openFailure(String str, String str2, String str3, String str4) throws RemoteException {
        MiLinkClientOpenMiracastListener miLinkClientOpenMiracastListener = this.mOpenMiracastListener;
        if (miLinkClientOpenMiracastListener != null) {
            miLinkClientOpenMiracastListener.openFailure(str, str2, str3, str4);
        }
    }
}
