package com.baidu.platform.comapi.b;

import android.content.res.AssetManager;
import android.text.TextUtils;
import com.baidu.platform.comapi.util.SysOSUtil;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/* loaded from: classes.dex */
public class b {
    public static final b a = new b();
    private c b = new c(com.baidu.platform.comapi.b.e(), "res.json");

    private b() {
    }

    private boolean a(AssetManager assetManager, byte[] bArr, String str, String str2) {
        InputStream inputStream = null;
        try {
            if (TextUtils.isEmpty(str) || !str.endsWith(".dir")) {
                inputStream = assetManager.open(str);
                File file = new File(str2);
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.isDirectory()) {
                    parentFile.mkdirs();
                }
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                a.a(inputStream, new FileOutputStream(file), bArr);
            } else {
                String substring = str.substring(0, str.indexOf(".dir"));
                String substring2 = str2.substring(0, str2.indexOf(".dir"));
                String[] list = assetManager.list(substring);
                if (list != null && list.length > 0) {
                    File file2 = new File(substring2);
                    if (file2.exists()) {
                        file2.delete();
                    }
                    file2.mkdirs();
                    for (String str3 : list) {
                        if (!TextUtils.isEmpty(str3)) {
                            a(assetManager, bArr, substring + h.g + str3, substring2 + h.g + str3);
                        }
                    }
                }
            }
            a.a(inputStream);
            return true;
        } catch (Exception unused) {
            a.a(null);
            return false;
        } catch (Throwable th) {
            a.a(null);
            throw th;
        }
    }

    private boolean a(File file, byte[] bArr) {
        FileInputStream fileInputStream;
        if (file != null && file.exists() && bArr != null) {
            FileInputStream fileInputStream2 = null;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (IOException unused) {
            } catch (Throwable th) {
                th = th;
            }
            try {
                byte[] bArr2 = new byte[fileInputStream.available()];
                fileInputStream.read(bArr2);
                if (Arrays.equals(bArr2, bArr)) {
                    a.a(fileInputStream);
                    return false;
                }
                a.a(fileInputStream);
            } catch (IOException unused2) {
                fileInputStream2 = fileInputStream;
                a.a(fileInputStream2);
                return true;
            } catch (Throwable th2) {
                th = th2;
                fileInputStream2 = fileInputStream;
                a.a(fileInputStream2);
                throw th;
            }
        }
        return true;
    }

    private String b() {
        String outputDirPath = SysOSUtil.getInstance().getOutputDirPath();
        File file = new File(outputDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return outputDirPath;
    }

    private boolean b(File file, byte[] bArr) {
        if (file != null && bArr != null) {
            FileOutputStream fileOutputStream = null;
            try {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                try {
                    fileOutputStream2.write(bArr);
                    a.a(fileOutputStream2);
                    return true;
                } catch (Exception unused) {
                    fileOutputStream = fileOutputStream2;
                    a.a(fileOutputStream);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    a.a(fileOutputStream);
                    throw th;
                }
            } catch (Exception unused2) {
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0062 A[Catch: all -> 0x0084, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0008, B:6:0x0013, B:8:0x001f, B:10:0x0031, B:18:0x0058, B:19:0x005c, B:21:0x0062), top: B:30:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void a() {
        /*
            r13 = this;
            monitor-enter(r13)
            android.content.Context r0 = com.baidu.platform.comapi.b.e()     // Catch: java.lang.Throwable -> L84
            r1 = 0
            r2 = 0
            r3 = 1
            java.lang.String r4 = r13.b()     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L84
            java.io.File r5 = new java.io.File     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L84
            java.lang.String r6 = "/ver.dat"
            r5.<init>(r4, r6)     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L84
            com.baidu.platform.comapi.b.c r6 = r13.b     // Catch: java.lang.Throwable -> L53 java.lang.Throwable -> L84
            byte[] r1 = r6.a()     // Catch: java.lang.Throwable -> L53 java.lang.Throwable -> L84
            boolean r6 = r13.a(r5, r1)     // Catch: java.lang.Throwable -> L53 java.lang.Throwable -> L84
            if (r6 == 0) goto L50
            android.content.res.AssetManager r0 = r0.getAssets()     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            com.baidu.platform.comapi.b.c r7 = r13.b     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            java.lang.String[] r7 = r7.b()     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            r8 = 65536(0x10000, float:9.18355E-41)
            byte[] r8 = new byte[r8]     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            int r9 = r7.length     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            r10 = r2
        L2f:
            if (r10 >= r9) goto L50
            r3 = r7[r10]     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            r11.<init>()     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            r11.append(r4)     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            java.lang.String r12 = "/"
            r11.append(r12)     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            r11.append(r3)     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            java.lang.String r11 = r11.toString()     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            boolean r3 = r13.a(r0, r8, r3, r11)     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L84
            int r10 = r10 + 1
            goto L2f
        L4e:
            r3 = r6
            goto L53
        L50:
            r2 = r3
            goto L54
        L52:
            r5 = r1
        L53:
            r6 = r3
        L54:
            if (r6 == 0) goto L5c
            if (r2 == 0) goto L5c
            boolean r2 = r13.b(r5, r1)     // Catch: java.lang.Throwable -> L84
        L5c:
            boolean r0 = com.baidu.mapapi.OpenLogUtil.isMapLogEnable()     // Catch: java.lang.Throwable -> L84
            if (r0 == 0) goto L82
            com.baidu.mapsdkplatform.comapi.commonutils.b r0 = com.baidu.mapsdkplatform.comapi.commonutils.b.a()     // Catch: java.lang.Throwable -> L84
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L84
            r1.<init>()     // Catch: java.lang.Throwable -> L84
            java.lang.String r3 = "initEngineRes firstInit = "
            r1.append(r3)     // Catch: java.lang.Throwable -> L84
            r1.append(r6)     // Catch: java.lang.Throwable -> L84
            java.lang.String r3 = "; isInitSucceed = "
            r1.append(r3)     // Catch: java.lang.Throwable -> L84
            r1.append(r2)     // Catch: java.lang.Throwable -> L84
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L84
            r0.a(r1)     // Catch: java.lang.Throwable -> L84
        L82:
            monitor-exit(r13)
            return
        L84:
            r0 = move-exception
            monitor-exit(r13)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.b.b.a():void");
    }
}
