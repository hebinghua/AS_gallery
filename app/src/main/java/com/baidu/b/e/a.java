package com.baidu.b.e;

import android.content.Context;
import com.baidu.b.c.b.c;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class a {
    private Context a;
    private C0004a b;

    /* renamed from: com.baidu.b.e.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public final class C0004a {
        private File b;
        private String c;
        private C0004a d;
        private boolean e;

        public C0004a(File file) {
            this.e = false;
            this.e = true;
            this.b = file;
            this.c = file.getName();
        }

        public C0004a(String str, C0004a c0004a) {
            this.e = false;
            this.c = str;
            this.d = c0004a;
            this.e = false;
        }

        public C0004a a(File file) {
            if (!this.e) {
                ArrayList arrayList = new ArrayList();
                C0004a c0004a = this;
                do {
                    arrayList.add(c0004a.c());
                    c0004a = c0004a.d();
                } while (c0004a != null);
                int size = arrayList.size() - 1;
                while (size >= 0) {
                    size--;
                    file = new File(file, (String) arrayList.get(size));
                }
                return new C0004a(file);
            }
            throw new IllegalStateException("isolate session is not support");
        }

        public C0004a a(String str) {
            return new C0004a(str, this);
        }

        public String a(String str, boolean z) {
            return a.a(b(), str, Keyczar.DEFAULT_ENCODING, z);
        }

        public void a() {
            b().mkdirs();
        }

        public boolean a(String str, String str2, boolean z) {
            return a.a(b(), str, str2, Keyczar.DEFAULT_ENCODING, z);
        }

        public File b() {
            File file = this.b;
            if (file != null) {
                return file;
            }
            File file2 = this.d == null ? new File(a.this.a(), this.c) : new File(this.d.b(), this.c);
            this.b = file2;
            return file2;
        }

        public String c() {
            return this.c;
        }

        public C0004a d() {
            return this.d;
        }
    }

    public a(Context context) {
        this.a = context;
        c().mkdirs();
    }

    public static String a(File file, String str, String str2, boolean z) {
        FileInputStream fileInputStream;
        Throwable th;
        ByteArrayOutputStream byteArrayOutputStream;
        a(file);
        File file2 = new File(file, str);
        FileInputStream fileInputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                fileInputStream = new FileInputStream(file2);
                try {
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    if (z) {
                        byteArray = new c().b(byteArray);
                    }
                    String str3 = new String(byteArray, str2);
                    com.baidu.b.f.c.a(fileInputStream);
                    com.baidu.b.f.c.a(byteArrayOutputStream);
                    return str3;
                } catch (Exception unused) {
                    fileInputStream2 = fileInputStream;
                    com.baidu.b.f.c.a(fileInputStream2);
                    com.baidu.b.f.c.a(byteArrayOutputStream);
                    return "";
                } catch (Throwable th2) {
                    th = th2;
                    com.baidu.b.f.c.a(fileInputStream);
                    com.baidu.b.f.c.a(byteArrayOutputStream);
                    throw th;
                }
            } catch (Exception unused2) {
            } catch (Throwable th3) {
                fileInputStream = null;
                th = th3;
            }
        } catch (Exception unused3) {
            byteArrayOutputStream = null;
        } catch (Throwable th4) {
            fileInputStream = null;
            th = th4;
            byteArrayOutputStream = null;
        }
    }

    public static void a(File file) {
        file.mkdirs();
    }

    public static boolean a(File file, String str, String str2, String str3, boolean z) {
        FileOutputStream fileOutputStream;
        Throwable th;
        a(file);
        File file2 = new File(file, str);
        FileOutputStream fileOutputStream2 = null;
        try {
            fileOutputStream = new FileOutputStream(file2);
            try {
                if (z) {
                    fileOutputStream.write(new c().a(str2.getBytes()));
                } else {
                    fileOutputStream.write(str2.getBytes(str3));
                }
                com.baidu.b.f.c.a(fileOutputStream);
                return true;
            } catch (Exception unused) {
                fileOutputStream2 = fileOutputStream;
                com.baidu.b.f.c.a(fileOutputStream2);
                return false;
            } catch (Throwable th2) {
                th = th2;
                com.baidu.b.f.c.a(fileOutputStream);
                throw th;
            }
        } catch (Exception unused2) {
        } catch (Throwable th3) {
            fileOutputStream = null;
            th = th3;
        }
    }

    private File c() {
        return new File(a(), ".cesium");
    }

    public File a() {
        return new File(this.a.getApplicationInfo().dataDir);
    }

    public synchronized C0004a b() {
        if (this.b == null) {
            this.b = new C0004a(".cesium", null);
        }
        return this.b;
    }
}
