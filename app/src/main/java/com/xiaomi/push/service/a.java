package com.xiaomi.push.service;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/* loaded from: classes3.dex */
public class a {
    public static volatile a a;

    /* renamed from: a  reason: collision with other field name */
    public Context f854a;
    public volatile String e;
    public volatile String f;

    /* renamed from: a  reason: collision with other field name */
    public final Object f855a = new Object();
    public final Object b = new Object();

    /* renamed from: a  reason: collision with other field name */
    public final String f856a = "mipush_region";

    /* renamed from: b  reason: collision with other field name */
    public final String f857b = "mipush_country_code";
    public final String c = "mipush_region.lock";
    public final String d = "mipush_country_code.lock";

    public a(Context context) {
        this.f854a = context;
    }

    public static a a(Context context) {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a(context);
                }
            }
        }
        return a;
    }

    public String a() {
        if (TextUtils.isEmpty(this.e)) {
            this.e = a(this.f854a, "mipush_region", "mipush_region.lock", this.f855a);
        }
        return this.e;
    }

    public final String a(Context context, String str, String str2, Object obj) {
        RandomAccessFile randomAccessFile;
        FileLock fileLock;
        File file = new File(context.getFilesDir(), str);
        FileLock fileLock2 = null;
        if (!file.exists()) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("No ready file to get data from " + str);
            return null;
        }
        synchronized (obj) {
            try {
                File file2 = new File(context.getFilesDir(), str2);
                com.xiaomi.push.ab.m1930a(file2);
                randomAccessFile = new RandomAccessFile(file2, "rw");
            } catch (Exception e) {
                e = e;
                randomAccessFile = null;
                fileLock = null;
            } catch (Throwable th) {
                th = th;
                randomAccessFile = null;
            }
            try {
                fileLock = randomAccessFile.getChannel().lock();
                try {
                    try {
                        String a2 = com.xiaomi.push.ab.a(file);
                        if (fileLock != null && fileLock.isValid()) {
                            try {
                                fileLock.release();
                            } catch (IOException e2) {
                                com.xiaomi.channel.commonutils.logger.b.a(e2);
                            }
                        }
                        com.xiaomi.push.ab.a(randomAccessFile);
                        return a2;
                    } catch (Exception e3) {
                        e = e3;
                        com.xiaomi.channel.commonutils.logger.b.a(e);
                        if (fileLock != null && fileLock.isValid()) {
                            try {
                                fileLock.release();
                            } catch (IOException e4) {
                                com.xiaomi.channel.commonutils.logger.b.a(e4);
                            }
                        }
                        com.xiaomi.push.ab.a(randomAccessFile);
                        return null;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileLock2 = fileLock;
                    if (fileLock2 != null && fileLock2.isValid()) {
                        try {
                            fileLock2.release();
                        } catch (IOException e5) {
                            com.xiaomi.channel.commonutils.logger.b.a(e5);
                        }
                    }
                    com.xiaomi.push.ab.a(randomAccessFile);
                    throw th;
                }
            } catch (Exception e6) {
                e = e6;
                fileLock = null;
            } catch (Throwable th3) {
                th = th3;
                if (fileLock2 != null) {
                    fileLock2.release();
                }
                com.xiaomi.push.ab.a(randomAccessFile);
                throw th;
            }
        }
    }

    public final void a(Context context, String str, String str2, String str3, Object obj) {
        RandomAccessFile randomAccessFile;
        synchronized (obj) {
            FileLock fileLock = null;
            try {
                try {
                    File file = new File(context.getFilesDir(), str3);
                    com.xiaomi.push.ab.m1930a(file);
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    try {
                        try {
                            fileLock = randomAccessFile.getChannel().lock();
                            com.xiaomi.push.ab.a(new File(context.getFilesDir(), str2), str);
                            if (fileLock != null && fileLock.isValid()) {
                                try {
                                    fileLock.release();
                                } catch (IOException e) {
                                    com.xiaomi.channel.commonutils.logger.b.a(e);
                                }
                            }
                        } catch (Exception e2) {
                            e = e2;
                            com.xiaomi.channel.commonutils.logger.b.a(e);
                            if (fileLock != null && fileLock.isValid()) {
                                try {
                                    fileLock.release();
                                } catch (IOException e3) {
                                    com.xiaomi.channel.commonutils.logger.b.a(e3);
                                }
                            }
                            com.xiaomi.push.ab.a(randomAccessFile);
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (fileLock != null && fileLock.isValid()) {
                            try {
                                fileLock.release();
                            } catch (IOException e4) {
                                com.xiaomi.channel.commonutils.logger.b.a(e4);
                            }
                        }
                        com.xiaomi.push.ab.a(randomAccessFile);
                        throw th;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            } catch (Exception e5) {
                e = e5;
                randomAccessFile = null;
            } catch (Throwable th3) {
                th = th3;
                randomAccessFile = null;
                if (fileLock != null) {
                    fileLock.release();
                }
                com.xiaomi.push.ab.a(randomAccessFile);
                throw th;
            }
            com.xiaomi.push.ab.a(randomAccessFile);
        }
    }

    public void a(String str) {
        if (!TextUtils.equals(str, this.e)) {
            this.e = str;
            a(this.f854a, this.e, "mipush_region", "mipush_region.lock", this.f855a);
        }
    }

    public String b() {
        if (TextUtils.isEmpty(this.f)) {
            this.f = a(this.f854a, "mipush_country_code", "mipush_country_code.lock", this.b);
        }
        return this.f;
    }

    public void b(String str) {
        if (!TextUtils.equals(str, this.f)) {
            this.f = str;
            a(this.f854a, this.f, "mipush_country_code", "mipush_country_code.lock", this.b);
        }
    }
}
