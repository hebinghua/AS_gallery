package com.xiaomi.push;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class cj {
    public static volatile cj a;

    /* renamed from: a  reason: collision with other field name */
    public Context f151a;

    /* renamed from: a  reason: collision with other field name */
    public final HashMap<String, ch> f153a = new HashMap<>();

    /* renamed from: a  reason: collision with other field name */
    public ThreadPoolExecutor f154a = new ThreadPoolExecutor(1, 1, 15, TimeUnit.SECONDS, new LinkedBlockingQueue());

    /* renamed from: a  reason: collision with other field name */
    public final ArrayList<a> f152a = new ArrayList<>();

    /* loaded from: classes3.dex */
    public static abstract class a implements Runnable {

        /* renamed from: a  reason: collision with other field name */
        public a f156a;

        /* renamed from: a  reason: collision with other field name */
        public String f157a;

        /* renamed from: a  reason: collision with other field name */
        public WeakReference<Context> f158a;
        public String b;

        /* renamed from: a  reason: collision with other field name */
        public ch f155a = null;

        /* renamed from: a  reason: collision with other field name */
        public Random f159a = new Random();
        public int a = 0;

        public a(String str) {
            this.f157a = str;
        }

        public SQLiteDatabase a() {
            return this.f155a.getWritableDatabase();
        }

        /* renamed from: a  reason: collision with other method in class */
        public Object m2013a() {
            return null;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m2014a() {
            return this.f157a;
        }

        public void a(Context context) {
            a aVar = this.f156a;
            if (aVar != null) {
                aVar.a(context, m2013a());
            }
            b(context);
        }

        public abstract void a(Context context, SQLiteDatabase sQLiteDatabase);

        public void a(Context context, Object obj) {
            cj.a(context).a(this);
        }

        public void a(a aVar) {
            this.f156a = aVar;
        }

        public void b(Context context) {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Context context;
            WeakReference<Context> weakReference = this.f158a;
            if (weakReference == null || (context = weakReference.get()) == null || context.getFilesDir() == null || this.f155a == null || TextUtils.isEmpty(this.f157a)) {
                return;
            }
            File file = new File(this.f157a);
            y.a(context, new File(file.getParentFile(), bo.b(file.getAbsolutePath())), new cl(this, context));
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class b<T> extends a {
        public int a;

        /* renamed from: a  reason: collision with other field name */
        public String f160a;

        /* renamed from: a  reason: collision with other field name */
        public List<String> f161a;

        /* renamed from: a  reason: collision with other field name */
        public String[] f162a;
        public List<T> b;
        public String c;
        public String d;
        public String e;

        public b(String str, List<String> list, String str2, String[] strArr, String str3, String str4, String str5, int i) {
            super(str);
            this.b = new ArrayList();
            this.f161a = list;
            this.f160a = str2;
            this.f162a = strArr;
            this.c = str3;
            this.d = str4;
            this.e = str5;
            this.a = i;
        }

        @Override // com.xiaomi.push.cj.a
        public SQLiteDatabase a() {
            return ((a) this).f155a.getReadableDatabase();
        }

        /* renamed from: a */
        public abstract T mo2011a(Context context, Cursor cursor);

        @Override // com.xiaomi.push.cj.a
        public void a(Context context, SQLiteDatabase sQLiteDatabase) {
            String[] strArr;
            this.b.clear();
            List<String> list = this.f161a;
            String str = null;
            if (list == null || list.size() <= 0) {
                strArr = null;
            } else {
                String[] strArr2 = new String[this.f161a.size()];
                this.f161a.toArray(strArr2);
                strArr = strArr2;
            }
            int i = this.a;
            if (i > 0) {
                str = String.valueOf(i);
            }
            Cursor query = sQLiteDatabase.query(super.b, strArr, this.f160a, this.f162a, this.c, this.d, this.e, str);
            if (query != null && query.moveToFirst()) {
                do {
                    T mo2011a = mo2011a(context, query);
                    if (mo2011a != null) {
                        this.b.add(mo2011a);
                    }
                } while (query.moveToNext());
                query.close();
            }
            a(context, (List) this.b);
        }

        public abstract void a(Context context, List<T> list);
    }

    /* loaded from: classes3.dex */
    public static class d extends a {
        public String a;

        /* renamed from: a  reason: collision with other field name */
        public String[] f163a;

        public d(String str, String str2, String[] strArr) {
            super(str);
            this.a = str2;
            this.f163a = strArr;
        }

        @Override // com.xiaomi.push.cj.a
        public void a(Context context, SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.delete(this.b, this.a, this.f163a);
        }
    }

    /* loaded from: classes3.dex */
    public static class e extends a {
        public ContentValues a;

        public e(String str, ContentValues contentValues) {
            super(str);
            this.a = contentValues;
        }

        @Override // com.xiaomi.push.cj.a
        public void a(Context context, SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.insert(this.b, null, this.a);
        }
    }

    public cj(Context context) {
        this.f151a = context;
    }

    public static cj a(Context context) {
        if (a == null) {
            synchronized (cj.class) {
                if (a == null) {
                    a = new cj(context);
                }
            }
        }
        return a;
    }

    public final ch a(String str) {
        ch chVar = this.f153a.get(str);
        if (chVar == null) {
            synchronized (this.f153a) {
                try {
                    if (chVar == null) {
                        throw null;
                    }
                } finally {
                }
            }
        }
        return chVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2012a(String str) {
        return a(str).a();
    }

    public void a(a aVar) {
        if (aVar == null) {
            return;
        }
        throw new IllegalStateException("should exec init method first!");
    }

    public void a(Runnable runnable) {
        if (!this.f154a.isShutdown()) {
            this.f154a.execute(runnable);
        }
    }
}
