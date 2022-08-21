package com.xiaomi.onetrack.b;

import android.os.HandlerThread;
import android.text.TextUtils;
import com.xiaomi.onetrack.util.x;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class p {
    public l g;

    public p() {
        b();
    }

    /* loaded from: classes3.dex */
    public static class a {
        public static final p a = new p();
    }

    public static p a() {
        return a.a;
    }

    public synchronized void a(int i, boolean z) {
        l lVar = this.g;
        if (lVar != null) {
            lVar.a(i, z);
        } else {
            com.xiaomi.onetrack.util.p.b("UploaderEngine", "*** impossible, upload timer should not be null");
        }
    }

    public boolean a(int i) {
        com.xiaomi.onetrack.util.p.a("UploaderEngine", "即将读取数据库并上传数据");
        while (true) {
            g a2 = b.a().a(i);
            if (a2 == null) {
                com.xiaomi.onetrack.util.p.a("UploaderEngine", "满足条件的记录为空，即将返回, priority=" + i);
                return true;
            }
            ArrayList<Long> arrayList = a2.c;
            boolean a3 = a(a2.a);
            com.xiaomi.onetrack.util.p.a("UploaderEngine", "upload success:" + a3);
            if (a3) {
                if (b.a().a(arrayList) == 0) {
                    com.xiaomi.onetrack.util.p.b("UploaderEngine", "delete DB failed!", new Throwable());
                    break;
                } else if (a2.d) {
                    com.xiaomi.onetrack.util.p.a("UploaderEngine", "No more records for prio=" + i);
                    break;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public final boolean a(JSONArray jSONArray) {
        try {
            String b = x.a().b();
            String jSONArray2 = jSONArray.toString();
            com.xiaomi.onetrack.util.p.a("UploaderEngine", " payload:" + jSONArray2);
            byte[] a2 = a(a(jSONArray2));
            com.xiaomi.onetrack.util.p.a("UploaderEngine", "before zip and encrypt, len=" + jSONArray2.length() + ", after=" + a2.length);
            String a3 = com.xiaomi.onetrack.f.b.a(b, a2);
            StringBuilder sb = new StringBuilder();
            sb.append("sendDataToServer response: ");
            sb.append(a3);
            com.xiaomi.onetrack.util.p.a("UploaderEngine", sb.toString());
            if (!TextUtils.isEmpty(a3)) {
                return b(a3);
            }
            return false;
        } catch (Exception e) {
            com.xiaomi.onetrack.util.p.b("UploaderEngine", "Exception while uploading ", e);
            return false;
        }
    }

    public final void b() {
        HandlerThread handlerThread = new HandlerThread("onetrack_uploader_worker");
        handlerThread.start();
        this.g = new l(handlerThread.getLooper());
    }

    public static byte[] a(String str) {
        GZIPOutputStream gZIPOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        byte[] bArr = null;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream(str.getBytes(Keyczar.DEFAULT_ENCODING).length);
            } catch (Throwable th) {
                th = th;
            }
            try {
                gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                try {
                    gZIPOutputStream.write(str.getBytes(Keyczar.DEFAULT_ENCODING));
                    gZIPOutputStream.finish();
                    bArr = byteArrayOutputStream.toByteArray();
                } catch (Exception e) {
                    e = e;
                    com.xiaomi.onetrack.util.p.b("UploaderEngine", " zipData failed! " + e.toString());
                    com.xiaomi.onetrack.util.m.a((OutputStream) byteArrayOutputStream);
                    com.xiaomi.onetrack.util.m.a((OutputStream) gZIPOutputStream);
                    return bArr;
                }
            } catch (Exception e2) {
                e = e2;
                gZIPOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                gZIPOutputStream = null;
                byteArrayOutputStream2 = byteArrayOutputStream;
                com.xiaomi.onetrack.util.m.a((OutputStream) byteArrayOutputStream2);
                com.xiaomi.onetrack.util.m.a((OutputStream) gZIPOutputStream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            byteArrayOutputStream = null;
            gZIPOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            gZIPOutputStream = null;
            com.xiaomi.onetrack.util.m.a((OutputStream) byteArrayOutputStream2);
            com.xiaomi.onetrack.util.m.a((OutputStream) gZIPOutputStream);
            throw th;
        }
        com.xiaomi.onetrack.util.m.a((OutputStream) byteArrayOutputStream);
        com.xiaomi.onetrack.util.m.a((OutputStream) gZIPOutputStream);
        return bArr;
    }

    public final byte[] a(byte[] bArr) {
        if (bArr == null) {
            com.xiaomi.onetrack.util.p.b("UploaderEngine", "content is null");
            return null;
        }
        return com.xiaomi.onetrack.c.a.a(bArr, com.xiaomi.onetrack.c.c.a(com.xiaomi.onetrack.c.f.a().b()[0]));
    }

    public final boolean b(String str) {
        boolean z = false;
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("code");
            if (optInt == 0) {
                com.xiaomi.onetrack.util.p.a("UploaderEngine", "成功发送数据到服务端");
                com.xiaomi.onetrack.a.a.a(jSONObject);
                z = true;
            } else if (optInt == -3) {
                com.xiaomi.onetrack.util.p.b("UploaderEngine", "signature expired, will update");
                com.xiaomi.onetrack.c.f.a().c();
            } else {
                com.xiaomi.onetrack.util.p.b("UploaderEngine", "Error: status code=" + optInt);
            }
        } catch (Exception e) {
            com.xiaomi.onetrack.util.p.b("UploaderEngine", "parseUploadingResult exception ", e);
        }
        return z;
    }
}
