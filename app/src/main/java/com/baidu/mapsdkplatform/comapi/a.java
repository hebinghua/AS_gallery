package com.baidu.mapsdkplatform.comapi;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.baidu.mapapi.JNIInitializer;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;
import com.baidu.mapsdkplatform.comapi.util.PermissionCheck;
import com.baidu.mapsdkplatform.comapi.util.SysUpdateObservable;
import com.baidu.mapsdkplatform.comapi.util.h;
import com.baidu.platform.comapi.UIMsg;

/* loaded from: classes.dex */
public class a implements PermissionCheck.c {
    private static final String a = "a";
    private static a f = null;
    private static int g = -100;
    private Context b;
    private Handler c;
    private f d;
    private int e;

    static {
        NativeLoader.getInstance().loadLibrary("gnustl_shared");
        NativeLoader.getInstance().loadLibrary(VersionInfo.getKitName());
        com.baidu.mapsdkplatform.comjni.tools.a.b();
    }

    private a() {
    }

    public static a a() {
        if (f == null) {
            f = new a();
        }
        return f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Message message) {
        Intent intent;
        if (this.b == null) {
            Context cachedContext = JNIInitializer.getCachedContext();
            this.b = cachedContext;
            if (cachedContext == null) {
                return;
            }
        }
        if (message.what != 2012) {
            if (message.arg2 == 3) {
                this.b.sendBroadcast(new Intent(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR));
            }
            int i = message.arg2;
            if (i != 2 && i != 404 && i != 5 && i != 8) {
                return;
            }
            intent = new Intent(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        } else if (message.arg1 == 0) {
            intent = new Intent(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        } else {
            Intent intent2 = new Intent(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
            intent2.putExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, message.arg1);
            intent2.putExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_MESSAGE, (String) message.obj);
            intent = intent2;
        }
        this.b.sendBroadcast(intent);
    }

    private void f() {
        f fVar;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        Context context = this.b;
        if (context == null || (fVar = this.d) == null) {
            return;
        }
        context.registerReceiver(fVar, intentFilter);
    }

    private void g() {
        Context context;
        f fVar = this.d;
        if (fVar == null || (context = this.b) == null) {
            return;
        }
        context.unregisterReceiver(fVar);
    }

    public void a(Context context) {
        this.b = context;
    }

    @Override // com.baidu.mapsdkplatform.comapi.util.PermissionCheck.c
    public void a(PermissionCheck.b bVar) {
        int i;
        if (bVar == null) {
            return;
        }
        if (bVar.a == 0) {
            h.d = bVar.e;
            h.a(bVar.b, bVar.c);
        } else {
            Log.e("baidumapsdk", "Authentication Error\n" + bVar.toString());
        }
        int i2 = bVar.a;
        if (i2 != PermissionCheck.b && i2 != PermissionCheck.a && i2 != PermissionCheck.c) {
            com.baidu.mapsdkplatform.comapi.util.c.a().a(bVar.f);
        }
        Handler handler = this.c;
        if (handler == null || (i = bVar.a) == g) {
            return;
        }
        g = i;
        Message obtainMessage = handler.obtainMessage();
        obtainMessage.what = UIMsg.MsgDefine.MSG_ONLINE_UPDATA;
        obtainMessage.arg1 = bVar.a;
        obtainMessage.obj = bVar.d;
        this.c.sendMessage(obtainMessage);
    }

    public void b() {
        if (this.e == 0) {
            if (this.b == null) {
                Context cachedContext = JNIInitializer.getCachedContext();
                this.b = cachedContext;
                if (cachedContext == null) {
                    Log.e("BDMapSDK", "BDMapSDKException: you have not supplyed the global app context info from SDKInitializer.initialize(Context) function.");
                    return;
                }
            }
            this.d = new f();
            f();
            SysUpdateObservable.getInstance().updateNetworkInfo(this.b);
        }
        this.e++;
    }

    public boolean c() {
        if (this.b == null) {
            Context cachedContext = JNIInitializer.getCachedContext();
            this.b = cachedContext;
            if (cachedContext == null) {
                Log.e("BDMapSDK", "BDMapSDKException: you have not supplyed the global app context info from SDKInitializer.initialize(Context) function.");
                return false;
            }
        }
        this.c = new b(this);
        h.b(this.b);
        com.baidu.mapsdkplatform.comapi.util.c.a().a(this.b);
        h.g();
        PermissionCheck.init(this.b);
        PermissionCheck.setPermissionCheckResultListener(this);
        PermissionCheck.permissionCheck();
        return true;
    }

    public void d() {
        int i = this.e - 1;
        this.e = i;
        if (i == 0) {
            g();
            h.a();
        }
    }

    public Context e() {
        if (this.b == null) {
            this.b = JNIInitializer.getCachedContext();
        }
        return this.b;
    }
}
