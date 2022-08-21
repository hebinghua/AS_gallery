package com.xiaomi.push;

import android.content.SharedPreferences;
import com.xiaomi.push.al;

/* loaded from: classes3.dex */
public class am extends al.b {
    public final /* synthetic */ al a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f90a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ boolean f91a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public am(al alVar, al.a aVar, boolean z, String str) {
        super(aVar);
        this.a = alVar;
        this.f91a = z;
        this.f90a = str;
    }

    @Override // com.xiaomi.push.al.b
    public void a() {
        super.a();
    }

    @Override // com.xiaomi.push.al.b
    public void b() {
        SharedPreferences sharedPreferences;
        if (!this.f91a) {
            sharedPreferences = this.a.f86a;
            sharedPreferences.edit().putLong(this.f90a, System.currentTimeMillis()).commit();
        }
    }
}
