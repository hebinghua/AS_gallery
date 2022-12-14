package com.xiaomi.push.service;

import android.os.Process;
import android.text.TextUtils;
import ch.qos.logback.core.spi.ComponentTracker;
import com.xiaomi.push.dw$a;
import com.xiaomi.push.fh;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/* loaded from: classes3.dex */
public class ap {

    /* renamed from: a  reason: collision with other field name */
    public static final Pattern f885a = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
    public static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    public static ThreadPoolExecutor f884a = new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, new LinkedBlockingQueue());

    public static String a(String str) {
        BufferedReader bufferedReader;
        Throwable th;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(str)));
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        String sb2 = sb.toString();
                        com.xiaomi.push.ab.a(bufferedReader);
                        return sb2;
                    }
                    sb.append("\n");
                    sb.append(readLine);
                }
            } catch (Exception unused) {
                com.xiaomi.push.ab.a(bufferedReader);
                return null;
            } catch (Throwable th2) {
                th = th2;
                com.xiaomi.push.ab.a(bufferedReader);
                throw th;
            }
        } catch (Exception unused2) {
            bufferedReader = null;
        } catch (Throwable th3) {
            bufferedReader = null;
            th = th3;
        }
    }

    public static void a() {
        dw$a m2509a;
        long currentTimeMillis = System.currentTimeMillis();
        if ((f884a.getActiveCount() <= 0 || currentTimeMillis - a >= ComponentTracker.DEFAULT_TIMEOUT) && fh.m2151a().m2156a() && (m2509a = bv.a().m2509a()) != null && m2509a.e() > 0) {
            a = currentTimeMillis;
            a(m2509a.mo2116a(), true);
        }
    }

    public static void a(List<String> list, boolean z) {
        f884a.execute(new aq(list, z));
    }

    public static void b() {
        String a2 = a("/proc/self/net/tcp");
        if (!TextUtils.isEmpty(a2)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("dump tcp for uid = " + Process.myUid());
            com.xiaomi.channel.commonutils.logger.b.m1859a(a2);
        }
        String a3 = a("/proc/self/net/tcp6");
        if (!TextUtils.isEmpty(a3)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("dump tcp6 for uid = " + Process.myUid());
            com.xiaomi.channel.commonutils.logger.b.m1859a(a3);
        }
    }

    public static boolean b(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            com.xiaomi.channel.commonutils.logger.b.m1859a("ConnectivityTest: begin to connect to " + str);
            Socket socket = new Socket();
            socket.connect(com.xiaomi.push.ct.m2023a(str, 5222), 5000);
            socket.setTcpNoDelay(true);
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            com.xiaomi.channel.commonutils.logger.b.m1859a("ConnectivityTest: connect to " + str + " in " + currentTimeMillis2);
            socket.close();
            return true;
        } catch (Throwable th) {
            com.xiaomi.channel.commonutils.logger.b.d("ConnectivityTest: could not connect to:" + str + " exception: " + th.getClass().getSimpleName() + " description: " + th.getMessage());
            return false;
        }
    }
}
