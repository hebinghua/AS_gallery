package com.baidu.vi;

import android.net.NetworkInfo;

/* loaded from: classes.dex */
public class VNetworkInfo {
    public int state;
    public int type;
    public String typename;

    public VNetworkInfo(NetworkInfo networkInfo) {
        this.typename = networkInfo.getTypeName();
        this.type = networkInfo.getType();
        int i = i.a[networkInfo.getState().ordinal()];
        if (i == 1) {
            this.state = 2;
        } else if (i != 2) {
            this.state = 0;
        } else {
            this.state = 1;
        }
    }
}
