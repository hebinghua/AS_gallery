package com.xiaomi.push;

import android.util.Log;
import android.util.Pair;
import ch.qos.logback.classic.Level;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/* loaded from: classes3.dex */
public class dk implements Runnable {
    public final /* synthetic */ dj a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f213a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ Throwable f214a;

    public dk(dj djVar, String str, Throwable th) {
        this.a = djVar;
        this.f213a = str;
        this.f214a = th;
    }

    @Override // java.lang.Runnable
    public void run() {
        List list;
        SimpleDateFormat simpleDateFormat;
        String str;
        List list2;
        String str2;
        String str3;
        List list3;
        List list4;
        SimpleDateFormat simpleDateFormat2;
        String str4;
        List list5;
        List list6;
        list = dj.f210a;
        simpleDateFormat = dj.f209a;
        str = this.a.b;
        list.add(new Pair(String.format("%1$s %2$s %3$s ", simpleDateFormat.format(new Date()), str, this.f213a), this.f214a));
        list2 = dj.f210a;
        if (list2.size() > 20000) {
            list3 = dj.f210a;
            int size = (list3.size() - Level.INFO_INT) + 50;
            for (int i = 0; i < size; i++) {
                try {
                    list5 = dj.f210a;
                    if (list5.size() > 0) {
                        list6 = dj.f210a;
                        list6.remove(0);
                    }
                } catch (IndexOutOfBoundsException unused) {
                }
            }
            list4 = dj.f210a;
            simpleDateFormat2 = dj.f209a;
            str4 = this.a.b;
            list4.add(new Pair(String.format("%1$s %2$s %3$s ", simpleDateFormat2.format(new Date()), str4, "flush " + size + " lines logs."), null));
        }
        try {
            if (ad.d()) {
                this.a.m2043a();
                return;
            }
            str3 = this.a.b;
            Log.w(str3, "SDCard is unavailable.");
        } catch (Exception e) {
            str2 = this.a.b;
            Log.e(str2, "", e);
        }
    }
}
