package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.xiaomi.push.al;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class dv extends al.a {
    public Context a;

    /* renamed from: a  reason: collision with other field name */
    public SharedPreferences f221a;

    /* renamed from: a  reason: collision with other field name */
    public com.xiaomi.push.service.ba f222a;

    public dv(Context context) {
        this.a = context;
        this.f221a = context.getSharedPreferences("mipush_extra", 0);
        this.f222a = com.xiaomi.push.service.ba.a(context);
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "1";
    }

    public final List<hr> a(File file) {
        RandomAccessFile randomAccessFile;
        FileInputStream fileInputStream;
        dm m2044a = dn.a().m2044a();
        String a = m2044a == null ? "" : m2044a.a();
        FileLock fileLock = null;
        if (TextUtils.isEmpty(a)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        byte[] bArr = new byte[4];
        synchronized (dq.a) {
            try {
                File file2 = new File(this.a.getExternalFilesDir(null), "push_cdata.lock");
                ab.m1930a(file2);
                randomAccessFile = new RandomAccessFile(file2, "rw");
                try {
                    FileLock lock = randomAccessFile.getChannel().lock();
                    try {
                        fileInputStream = new FileInputStream(file);
                        while (fileInputStream.read(bArr) == 4) {
                            try {
                                int a2 = af.a(bArr);
                                byte[] bArr2 = new byte[a2];
                                if (fileInputStream.read(bArr2) != a2) {
                                    break;
                                }
                                byte[] a3 = dp.a(a, bArr2);
                                if (a3 != null && a3.length != 0) {
                                    hr hrVar = new hr();
                                    it.a(hrVar, a3);
                                    arrayList.add(hrVar);
                                    a(hrVar);
                                }
                            } catch (Exception unused) {
                                fileLock = lock;
                                if (fileLock != null && fileLock.isValid()) {
                                    try {
                                        fileLock.release();
                                    } catch (IOException unused2) {
                                    }
                                }
                                ab.a(fileInputStream);
                                ab.a(randomAccessFile);
                                return arrayList;
                            } catch (Throwable th) {
                                th = th;
                                fileLock = lock;
                                if (fileLock != null && fileLock.isValid()) {
                                    try {
                                        fileLock.release();
                                    } catch (IOException unused3) {
                                    }
                                }
                                ab.a(fileInputStream);
                                ab.a(randomAccessFile);
                                throw th;
                            }
                        }
                        if (lock != null && lock.isValid()) {
                            try {
                                lock.release();
                            } catch (IOException unused4) {
                            }
                        }
                        ab.a(fileInputStream);
                    } catch (Exception unused5) {
                        fileInputStream = null;
                    } catch (Throwable th2) {
                        th = th2;
                        fileInputStream = null;
                    }
                } catch (Exception unused6) {
                    fileInputStream = null;
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = null;
                }
            } catch (Exception unused7) {
                randomAccessFile = null;
                fileInputStream = null;
            } catch (Throwable th4) {
                th = th4;
                randomAccessFile = null;
                fileInputStream = null;
            }
            ab.a(randomAccessFile);
        }
        return arrayList;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a  reason: collision with other method in class */
    public final void mo2050a() {
        SharedPreferences.Editor edit = this.f221a.edit();
        edit.putLong("last_upload_data_timestamp", System.currentTimeMillis() / 1000);
        edit.commit();
    }

    public final void a(hr hrVar) {
        if (hrVar.f480a != hl.AppInstallList || hrVar.f481a.startsWith("same_")) {
            return;
        }
        SharedPreferences.Editor edit = this.f221a.edit();
        edit.putLong("dc_job_result_time_4", hrVar.f479a);
        edit.putString("dc_job_result_4", bp.a(hrVar.f481a));
        edit.commit();
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public final boolean mo2050a() {
        if (bj.e(this.a)) {
            return false;
        }
        if ((bj.g(this.a) || bj.f(this.a)) && !c()) {
            return true;
        }
        return (bj.h(this.a) && !b()) || bj.i(this.a);
    }

    public final boolean b() {
        if (!this.f222a.a(ho.Upload3GSwitch.a(), true)) {
            return false;
        }
        return Math.abs((System.currentTimeMillis() / 1000) - this.f221a.getLong("last_upload_data_timestamp", -1L)) > ((long) Math.max(86400, this.f222a.a(ho.Upload3GFrequency.a(), 432000)));
    }

    public final boolean c() {
        if (!this.f222a.a(ho.Upload4GSwitch.a(), true)) {
            return false;
        }
        return Math.abs((System.currentTimeMillis() / 1000) - this.f221a.getLong("last_upload_data_timestamp", -1L)) > ((long) Math.max(86400, this.f222a.a(ho.Upload4GFrequency.a(), 259200)));
    }

    @Override // java.lang.Runnable
    public void run() {
        File file = new File(this.a.getExternalFilesDir(null), "push_cdata.data");
        if (!bj.d(this.a)) {
            if (file.length() <= 1863680) {
                return;
            }
            file.delete();
        } else if (mo2050a() || !file.exists()) {
        } else {
            List<hr> a = a(file);
            if (!ag.a(a)) {
                int size = a.size();
                if (size > 4000) {
                    a = a.subList(size - 4000, size);
                }
                ic icVar = new ic();
                icVar.a(a);
                byte[] a2 = ab.a(it.a(icVar));
                ii iiVar = new ii("-1", false);
                iiVar.c(ht.DataCollection.f489a);
                iiVar.a(a2);
                dm m2044a = dn.a().m2044a();
                if (m2044a != null) {
                    m2044a.a(iiVar, hj.Notification, null);
                }
                mo2050a();
            }
            file.delete();
        }
    }
}
