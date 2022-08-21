package com.xiaomi.stat.a;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.xiaomi.stat.ak;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class h implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ c b;

    public h(c cVar, String str) {
        this.b = cVar;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        a aVar;
        String str;
        String[] strArr;
        try {
            aVar = this.b.l;
            SQLiteDatabase writableDatabase = aVar.getWritableDatabase();
            if (TextUtils.equals(this.a, ak.b())) {
                str = "sub is null";
                strArr = null;
            } else {
                str = "sub = ?";
                strArr = new String[]{this.a};
            }
            writableDatabase.delete(j.b, str, strArr);
        } catch (Exception e) {
            com.xiaomi.stat.d.k.b("EventManager", "removeAllEventsForApp exception: " + e.toString());
        }
    }
}
