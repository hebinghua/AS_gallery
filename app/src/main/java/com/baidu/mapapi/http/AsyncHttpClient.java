package com.baidu.mapapi.http;

import android.os.Build;
import com.baidu.mapapi.http.HttpClient;
import com.baidu.mapsdkplatform.comapi.util.PermissionCheck;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class AsyncHttpClient {
    private int a = 10000;
    private int b = 10000;
    private ExecutorService c = Executors.newCachedThreadPool();

    /* loaded from: classes.dex */
    public static abstract class a implements Runnable {
        private a() {
        }

        public /* synthetic */ a(com.baidu.mapapi.http.a aVar) {
            this();
        }

        public abstract void a();

        @Override // java.lang.Runnable
        public void run() {
            a();
        }
    }

    static {
        if (Build.VERSION.SDK_INT <= 8) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    public void get(String str, HttpClient.ProtoResultCallback protoResultCallback) {
        if (str != null) {
            this.c.submit(new com.baidu.mapapi.http.a(this, protoResultCallback, str));
            return;
        }
        throw new IllegalArgumentException("URI cannot be null");
    }

    public boolean isAuthorized() {
        int permissionCheck = PermissionCheck.permissionCheck();
        return permissionCheck == 0 || permissionCheck == 602 || permissionCheck == 601;
    }
}
