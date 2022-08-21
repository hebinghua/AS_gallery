package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.xiaomi.push.al;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/* loaded from: classes3.dex */
public abstract class dt extends al.a {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public Context f220a;

    public dt(Context context, int i) {
        this.a = i;
        this.f220a = context;
    }

    public static void a(Context context, hr hrVar) {
        dm m2044a = dn.a().m2044a();
        String a = m2044a == null ? "" : m2044a.a();
        if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(hrVar.a())) {
            a(context, hrVar, a);
        }
    }

    public static void a(Context context, hr hrVar, String str) {
        BufferedOutputStream bufferedOutputStream;
        RandomAccessFile randomAccessFile;
        FileLock lock;
        File file;
        byte[] b = dp.b(str, it.a(hrVar));
        if (b == null || b.length == 0) {
            return;
        }
        synchronized (dq.a) {
            FileLock fileLock = null;
            try {
                try {
                    File file2 = new File(context.getExternalFilesDir(null), "push_cdata.lock");
                    ab.m1930a(file2);
                    randomAccessFile = new RandomAccessFile(file2, "rw");
                    try {
                        lock = randomAccessFile.getChannel().lock();
                        try {
                            file = new File(context.getExternalFilesDir(null), "push_cdata.data");
                            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file, true));
                        } catch (IOException e) {
                            e = e;
                            bufferedOutputStream = null;
                        } catch (Throwable th) {
                            th = th;
                            bufferedOutputStream = null;
                        }
                    } catch (IOException e2) {
                        e = e2;
                        bufferedOutputStream = null;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedOutputStream = null;
                    }
                    try {
                        bufferedOutputStream.write(af.a(b.length));
                        bufferedOutputStream.write(b);
                        bufferedOutputStream.flush();
                        file.setLastModified(0L);
                        if (lock != null && lock.isValid()) {
                            try {
                                lock.release();
                            } catch (IOException unused) {
                            }
                        }
                        ab.a(bufferedOutputStream);
                    } catch (IOException e3) {
                        e = e3;
                        fileLock = lock;
                        try {
                            e.printStackTrace();
                            if (fileLock != null && fileLock.isValid()) {
                                try {
                                    fileLock.release();
                                } catch (IOException unused2) {
                                }
                            }
                            ab.a(bufferedOutputStream);
                            ab.a(randomAccessFile);
                        } catch (Throwable th3) {
                            th = th3;
                            if (fileLock != null && fileLock.isValid()) {
                                try {
                                    fileLock.release();
                                } catch (IOException unused3) {
                                }
                            }
                            ab.a(bufferedOutputStream);
                            ab.a(randomAccessFile);
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        fileLock = lock;
                        if (fileLock != null) {
                            fileLock.release();
                        }
                        ab.a(bufferedOutputStream);
                        ab.a(randomAccessFile);
                        throw th;
                    }
                } catch (Throwable th5) {
                    throw th5;
                }
            } catch (IOException e4) {
                e = e4;
                bufferedOutputStream = null;
                randomAccessFile = null;
            } catch (Throwable th6) {
                th = th6;
                bufferedOutputStream = null;
                randomAccessFile = null;
            }
            ab.a(randomAccessFile);
        }
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public abstract hl mo2050a();

    @Override // com.xiaomi.push.al.a
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo2050a() {
        return dp.a(this.f220a, String.valueOf(mo2050a()), this.a);
    }

    public abstract String b();

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2047b() {
        return true;
    }

    public final String c() {
        return "dc_job_result_time_" + mo2050a();
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2048c() {
        return false;
    }

    public final String d() {
        return "dc_job_result_" + mo2050a();
    }

    @Override // java.lang.Runnable
    public void run() {
        String b = b();
        if (TextUtils.isEmpty(b)) {
            return;
        }
        if (mo2050a()) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("DC run job mutual: " + mo2050a());
            return;
        }
        dm m2044a = dn.a().m2044a();
        String a = m2044a == null ? "" : m2044a.a();
        if (TextUtils.isEmpty(a) || !m2047b()) {
            return;
        }
        if (m2048c()) {
            SharedPreferences sharedPreferences = this.f220a.getSharedPreferences("mipush_extra", 0);
            if (bp.a(b).equals(sharedPreferences.getString(d(), null))) {
                long j = sharedPreferences.getLong(c(), 0L);
                int a2 = com.xiaomi.push.service.ba.a(this.f220a).a(ho.DCJobUploadRepeatedInterval.a(), 604800);
                if ((System.currentTimeMillis() - j) / 1000 < this.a) {
                    return;
                }
                if ((System.currentTimeMillis() - j) / 1000 < a2) {
                    b = "same_" + j;
                }
            }
        }
        hr hrVar = new hr();
        hrVar.a(b);
        hrVar.a(System.currentTimeMillis());
        hrVar.a(mo2050a());
        a(this.f220a, hrVar, a);
    }
}
