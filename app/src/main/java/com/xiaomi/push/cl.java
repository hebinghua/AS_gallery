package com.xiaomi.push;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.xiaomi.push.cj;

/* loaded from: classes3.dex */
public class cl implements Runnable {
    public final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ cj.a f164a;

    public cl(cj.a aVar, Context context) {
        this.f164a = aVar;
        this.a = context;
    }

    @Override // java.lang.Runnable
    public void run() {
        SQLiteDatabase sQLiteDatabase = null;
        try {
            try {
                sQLiteDatabase = this.f164a.a();
                if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
                    sQLiteDatabase.beginTransaction();
                    this.f164a.a(this.a, sQLiteDatabase);
                    sQLiteDatabase.setTransactionSuccessful();
                }
                if (sQLiteDatabase != null) {
                    try {
                        sQLiteDatabase.endTransaction();
                    } catch (Exception e) {
                        e = e;
                        com.xiaomi.channel.commonutils.logger.b.a(e);
                        this.f164a.a(this.a);
                    }
                }
                ch chVar = this.f164a.f155a;
                if (chVar != null) {
                    chVar.close();
                }
            } catch (Exception e2) {
                com.xiaomi.channel.commonutils.logger.b.a(e2);
                if (sQLiteDatabase != null) {
                    try {
                        sQLiteDatabase.endTransaction();
                    } catch (Exception e3) {
                        e = e3;
                        com.xiaomi.channel.commonutils.logger.b.a(e);
                        this.f164a.a(this.a);
                    }
                }
                ch chVar2 = this.f164a.f155a;
                if (chVar2 != null) {
                    chVar2.close();
                }
            }
            this.f164a.a(this.a);
        } catch (Throwable th) {
            if (sQLiteDatabase != null) {
                try {
                    sQLiteDatabase.endTransaction();
                } catch (Exception e4) {
                    com.xiaomi.channel.commonutils.logger.b.a(e4);
                    this.f164a.a(this.a);
                    throw th;
                }
            }
            ch chVar3 = this.f164a.f155a;
            if (chVar3 != null) {
                chVar3.close();
            }
            this.f164a.a(this.a);
            throw th;
        }
    }
}
