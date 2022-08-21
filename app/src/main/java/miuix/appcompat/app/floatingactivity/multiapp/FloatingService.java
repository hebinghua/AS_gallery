package miuix.appcompat.app.floatingactivity.multiapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.LinkedList;
import miuix.appcompat.app.floatingactivity.multiapp.IFloatingService;

/* loaded from: classes3.dex */
public class FloatingService extends Service {
    public RemoteCallbackList<IServiceNotify> mServiceNotify = new RemoteCallbackList<>();
    public LinkedList<String> mNotifyIdentity = new LinkedList<>();
    public IFloatingService mBinder = new IFloatingService.Stub() { // from class: miuix.appcompat.app.floatingactivity.multiapp.FloatingService.1
        @Override // miuix.appcompat.app.floatingactivity.multiapp.IFloatingService
        public Bundle callServiceMethod(int i, Bundle bundle) throws RemoteException {
            Bundle bundle2 = new Bundle();
            if (i == 6) {
                bundle2.putInt(String.valueOf(6), FloatingService.this.getPageCount());
            } else if (i == 7) {
                String findPreviousIdentity = FloatingService.this.findPreviousIdentity(bundle.getString("key_request_identity"));
                int beginBroadcast = FloatingService.this.mServiceNotify.beginBroadcast();
                int i2 = 0;
                while (true) {
                    if (i2 >= beginBroadcast) {
                        break;
                    } else if (TextUtils.equals(findPreviousIdentity, FloatingService.this.mServiceNotify.getBroadcastCookie(i2).toString())) {
                        ((IServiceNotify) FloatingService.this.mServiceNotify.getBroadcastItem(i2)).notifyFromService(8, bundle);
                        break;
                    } else {
                        i2++;
                    }
                }
                FloatingService.this.mServiceNotify.finishBroadcast();
            } else if (i == 9) {
                bundle2.putBoolean("check_finishing", FloatingService.this.checkFinishing(i, bundle.getString("key_request_identity")));
            } else if (i != 10) {
                FloatingService.this.onMethodCall(i);
            } else {
                FloatingService.this.notifyPreviousSlide(i, bundle.getString("execute_slide"));
            }
            return bundle2;
        }

        @Override // miuix.appcompat.app.floatingactivity.multiapp.IFloatingService
        public int registerServiceNotify(IServiceNotify iServiceNotify, String str) throws RemoteException {
            FloatingService.this.mNotifyIdentity.remove(str);
            FloatingService.this.mServiceNotify.unregister(iServiceNotify);
            int registeredCallbackCount = FloatingService.this.mServiceNotify.getRegisteredCallbackCount();
            FloatingService.this.mServiceNotify.register(iServiceNotify, str);
            FloatingService.this.mNotifyIdentity.add(str);
            return registeredCallbackCount;
        }

        @Override // miuix.appcompat.app.floatingactivity.multiapp.IFloatingService
        public void unregisterServiceNotify(IServiceNotify iServiceNotify, String str) throws RemoteException {
            FloatingService.this.mServiceNotify.unregister(iServiceNotify);
            FloatingService.this.mNotifyIdentity.remove(str);
        }
    };

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder.asBinder();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        stopSelf();
        return super.onUnbind(intent);
    }

    public final void onMethodCall(int i) throws RemoteException {
        int beginBroadcast = this.mServiceNotify.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            this.mServiceNotify.getBroadcastItem(i2).notifyFromService(i, null);
        }
        this.mServiceNotify.finishBroadcast();
    }

    public final String findNextIdentity(String str) {
        Iterator<String> it = this.mNotifyIdentity.iterator();
        boolean z = false;
        while (it.hasNext()) {
            String next = it.next();
            if (z) {
                return next;
            }
            if (TextUtils.equals(str, next)) {
                z = true;
            }
        }
        return null;
    }

    public final boolean checkFinishing(int i, String str) throws RemoteException {
        int beginBroadcast = this.mServiceNotify.beginBroadcast();
        String findNextIdentity = findNextIdentity(str);
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (i2 >= beginBroadcast) {
                break;
            } else if (TextUtils.equals(findNextIdentity, this.mServiceNotify.getBroadcastCookie(i2).toString())) {
                z = this.mServiceNotify.getBroadcastItem(i2).notifyFromService(i, null).getBoolean("check_finishing");
                break;
            } else {
                i2++;
            }
        }
        this.mServiceNotify.finishBroadcast();
        return z;
    }

    public final String findPreviousIdentity(String str) {
        Iterator<String> it = this.mNotifyIdentity.iterator();
        String str2 = null;
        while (it.hasNext()) {
            String next = it.next();
            if (TextUtils.equals(str, next)) {
                break;
            }
            str2 = next;
        }
        return str2;
    }

    public final void notifyPreviousSlide(int i, String str) throws RemoteException {
        int beginBroadcast = this.mServiceNotify.beginBroadcast();
        String findPreviousIdentity = findPreviousIdentity(str);
        int i2 = 0;
        while (true) {
            if (i2 >= beginBroadcast) {
                break;
            } else if (TextUtils.equals(findPreviousIdentity, this.mServiceNotify.getBroadcastCookie(i2).toString())) {
                this.mServiceNotify.getBroadcastItem(i2).notifyFromService(i, null);
                break;
            } else {
                i2++;
            }
        }
        this.mServiceNotify.finishBroadcast();
    }

    public final int getPageCount() {
        return this.mServiceNotify.getRegisteredCallbackCount();
    }
}
