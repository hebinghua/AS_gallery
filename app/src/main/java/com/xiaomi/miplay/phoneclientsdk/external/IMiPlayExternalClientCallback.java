package com.xiaomi.miplay.phoneclientsdk.external;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface IMiPlayExternalClientCallback extends IInterface {
    void onBuffering() throws RemoteException;

    int onCirculateEnd() throws RemoteException;

    int onCirculateFail(String str) throws RemoteException;

    void onCirculateModeChange(int i) throws RemoteException;

    int onCirculateStart() throws RemoteException;

    void onConnectMirrorSuccess() throws RemoteException;

    void onInitError() throws RemoteException;

    void onInitSuccess() throws RemoteException;

    int onNext() throws RemoteException;

    int onNotifyPropertiesInfo(String str) throws RemoteException;

    int onPaused() throws RemoteException;

    int onPlayed() throws RemoteException;

    int onPositionChanged(long j) throws RemoteException;

    int onPrev() throws RemoteException;

    void onResumeMirrorFail() throws RemoteException;

    void onResumeMirrorSuccess() throws RemoteException;

    int onResumed() throws RemoteException;

    int onSeekDoneNotify() throws RemoteException;

    int onSeekedTo(long j) throws RemoteException;

    int onStopped(int i) throws RemoteException;

    void onVolumeChange(double d) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IMiPlayExternalClientCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    onInitSuccess();
                    return true;
                case 2:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    onInitError();
                    return true;
                case 3:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onPositionChanged = onPositionChanged(parcel.readLong());
                    parcel2.writeNoException();
                    parcel2.writeInt(onPositionChanged);
                    return true;
                case 4:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onPlayed = onPlayed();
                    parcel2.writeNoException();
                    parcel2.writeInt(onPlayed);
                    return true;
                case 5:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onStopped = onStopped(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(onStopped);
                    return true;
                case 6:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onNotifyPropertiesInfo = onNotifyPropertiesInfo(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(onNotifyPropertiesInfo);
                    return true;
                case 7:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onNext = onNext();
                    parcel2.writeNoException();
                    parcel2.writeInt(onNext);
                    return true;
                case 8:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onPrev = onPrev();
                    parcel2.writeNoException();
                    parcel2.writeInt(onPrev);
                    return true;
                case 9:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onCirculateStart = onCirculateStart();
                    parcel2.writeNoException();
                    parcel2.writeInt(onCirculateStart);
                    return true;
                case 10:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onCirculateEnd = onCirculateEnd();
                    parcel2.writeNoException();
                    parcel2.writeInt(onCirculateEnd);
                    return true;
                case 11:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onPaused = onPaused();
                    parcel2.writeNoException();
                    parcel2.writeInt(onPaused);
                    return true;
                case 12:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onResumed = onResumed();
                    parcel2.writeNoException();
                    parcel2.writeInt(onResumed);
                    return true;
                case 13:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onSeekedTo = onSeekedTo(parcel.readLong());
                    parcel2.writeNoException();
                    parcel2.writeInt(onSeekedTo);
                    return true;
                case 14:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onCirculateFail = onCirculateFail(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(onCirculateFail);
                    return true;
                case 15:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    int onSeekDoneNotify = onSeekDoneNotify();
                    parcel2.writeNoException();
                    parcel2.writeInt(onSeekDoneNotify);
                    return true;
                case 16:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    onVolumeChange(parcel.readDouble());
                    parcel2.writeNoException();
                    return true;
                case 17:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    onCirculateModeChange(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 18:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    onConnectMirrorSuccess();
                    parcel2.writeNoException();
                    return true;
                case 19:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    onBuffering();
                    parcel2.writeNoException();
                    return true;
                case 20:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    onResumeMirrorSuccess();
                    parcel2.writeNoException();
                    return true;
                case 21:
                    parcel.enforceInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback");
                    onResumeMirrorFail();
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }
}
