package com.milink.api.v1;

import android.os.RemoteException;
import com.milink.api.v1.aidl.IMcsDataSource;

/* loaded from: classes.dex */
public class McsDataSource extends IMcsDataSource.Stub {
    public MilinkClientManagerDataSource mDataSource = null;

    public void setDataSource(MilinkClientManagerDataSource milinkClientManagerDataSource) {
        this.mDataSource = milinkClientManagerDataSource;
    }

    @Override // com.milink.api.v1.aidl.IMcsDataSource
    public String getPrevPhoto(String str, boolean z) throws RemoteException {
        MilinkClientManagerDataSource milinkClientManagerDataSource = this.mDataSource;
        if (milinkClientManagerDataSource == null) {
            return null;
        }
        return milinkClientManagerDataSource.getPrevPhoto(str, z);
    }

    @Override // com.milink.api.v1.aidl.IMcsDataSource
    public String getNextPhoto(String str, boolean z) throws RemoteException {
        MilinkClientManagerDataSource milinkClientManagerDataSource = this.mDataSource;
        if (milinkClientManagerDataSource == null) {
            return null;
        }
        return milinkClientManagerDataSource.getNextPhoto(str, z);
    }
}
