package com.baidu.b.a;

import com.baidu.b.a.e;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
final class h extends g {
    private a f;

    /* loaded from: classes.dex */
    public static class a {
        private Class<?> a;
        private Method b;
        private Method c;

        private a() {
            a();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long a(Object obj) {
            try {
                return ((Long) this.c.invoke(obj, new Object[0])).longValue();
            } catch (Exception unused) {
                throw new e.a("");
            }
        }

        private void a() {
            try {
                this.a = Class.forName(e.a(d.a()), true, Object.class.getClassLoader());
                String a = e.a(d.b());
                Class<?> cls = this.a;
                Class cls2 = Integer.TYPE;
                this.b = e.a(cls, a, new Class[]{byte[].class, cls2, cls2});
                this.c = e.a(this.a, e.a(d.c()), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(Object obj, byte[] bArr, int i, int i2) {
            try {
                this.b.invoke(obj, bArr, Integer.valueOf(i), Integer.valueOf(i2));
            } catch (Exception unused) {
                throw new e.a("");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Object b() {
            return this.a.newInstance();
        }
    }

    public h(int i, int i2) {
        this.a = 1099511627775L;
        this.b = 4;
        this.c = 32;
        this.d = i;
        this.e = i2;
        this.f = new a();
    }

    @Override // com.baidu.b.a.g
    public b a(byte[] bArr, int i, int i2) {
        long j;
        try {
            Object b = this.f.b();
            this.f.a(b, bArr, i, i2);
            j = this.f.a(b);
        } catch (Exception unused) {
            j = 4294967295L;
        }
        return b.a(new long[]{j});
    }
}
