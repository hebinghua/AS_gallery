package com.xiaomi.push;

import com.xiaomi.push.df;
import java.io.File;
import java.util.Date;

/* loaded from: classes3.dex */
public class dg extends df.b {
    public final /* synthetic */ int a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ df f201a;

    /* renamed from: a  reason: collision with other field name */
    public File f202a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f203a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ Date f204a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ boolean f205a;
    public final /* synthetic */ String b;

    /* renamed from: b  reason: collision with other field name */
    public final /* synthetic */ Date f206b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public dg(df dfVar, int i, Date date, Date date2, String str, String str2, boolean z) {
        super();
        this.f201a = dfVar;
        this.a = i;
        this.f204a = date;
        this.f206b = date2;
        this.f203a = str;
        this.b = str2;
        this.f205a = z;
    }

    @Override // com.xiaomi.push.df.b, com.xiaomi.push.ao.b
    /* renamed from: b */
    public void mo2039b() {
        if (!ad.d()) {
            return;
        }
        try {
            File file = new File(this.f201a.f194a.getExternalFilesDir(null) + "/.logcache");
            file.mkdirs();
            if (!file.isDirectory()) {
                return;
            }
            de deVar = new de();
            deVar.a(this.a);
            this.f202a = deVar.a(this.f201a.f194a, this.f204a, this.f206b, file);
        } catch (NullPointerException unused) {
        }
    }

    @Override // com.xiaomi.push.ao.b
    /* renamed from: c */
    public void mo2040c() {
        File file = this.f202a;
        if (file != null && file.exists()) {
            this.f201a.f195a.add(new df.c(this.f203a, this.b, this.f202a, this.f205a));
        }
        this.f201a.a(0L);
    }
}
