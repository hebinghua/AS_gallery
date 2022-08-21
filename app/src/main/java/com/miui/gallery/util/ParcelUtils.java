package com.miui.gallery.util;

import android.os.Parcel;
import android.os.ResultReceiver;

/* loaded from: classes2.dex */
public class ParcelUtils {
    public static ResultReceiver getParcelableResultReceiver(ResultReceiver resultReceiver) {
        Parcel obtain = Parcel.obtain();
        resultReceiver.writeToParcel(obtain, 0);
        obtain.setDataPosition(0);
        ResultReceiver resultReceiver2 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(obtain);
        obtain.recycle();
        return resultReceiver2;
    }
}
