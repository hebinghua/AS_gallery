package com.xiaomi.push;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.nexstreaming.nexeditorsdk.nexEngine;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.Map;

/* loaded from: classes3.dex */
public class ep extends es {
    public int a;
    public Bitmap b;
    public Bitmap c;

    public ep(Context context, String str) {
        super(context, str);
        this.a = nexEngine.ExportHEVCMainTierLevel62;
    }

    @Override // com.xiaomi.push.es
    /* renamed from: a */
    public ep mo2129a(Bitmap bitmap) {
        if (m2137b() && bitmap != null) {
            if (bitmap.getWidth() != 984 || 184 > bitmap.getHeight() || bitmap.getHeight() > 1678) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("colorful notification banner image resolution error, must belong to [984*184, 984*1678]");
            } else {
                this.b = bitmap;
            }
        }
        return this;
    }

    @Override // com.xiaomi.push.eq
    public ep a(String str) {
        if (m2137b() && !TextUtils.isEmpty(str)) {
            try {
                this.a = Color.parseColor(str);
            } catch (Exception unused) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("parse banner notification image text color error");
            }
        }
        return this;
    }

    @Override // com.xiaomi.push.es, android.app.Notification.Builder
    /* renamed from: a  reason: collision with other method in class */
    public es setLargeIcon(Bitmap bitmap) {
        return this;
    }

    @Override // com.xiaomi.push.es, com.xiaomi.push.eq
    /* renamed from: a */
    public String mo2134a() {
        return "notification_banner";
    }

    @Override // com.xiaomi.push.es, com.xiaomi.push.eq
    /* renamed from: a  reason: collision with other method in class */
    public void mo2134a() {
        RemoteViews mo2134a;
        Bitmap bitmap;
        if (!m2137b() || this.b == null) {
            m2136b();
            return;
        }
        super.mo2134a();
        Resources resources = mo2134a().getResources();
        String packageName = mo2134a().getPackageName();
        int a = a(resources, "bg", "id", packageName);
        if (m.a(mo2134a()) >= 10) {
            mo2134a = mo2134a();
            bitmap = a(this.b, 30.0f);
        } else {
            mo2134a = mo2134a();
            bitmap = this.b;
        }
        mo2134a.setImageViewBitmap(a, bitmap);
        int a2 = a(resources, CallMethod.RESULT_ICON, "id", packageName);
        if (this.c != null) {
            mo2134a().setImageViewBitmap(a2, this.c);
        } else {
            a(a2);
        }
        int a3 = a(resources, "title", "id", packageName);
        mo2134a().setTextViewText(a3, ((es) this).f306a);
        Map<String, String> map = ((es) this).f309a;
        if (map != null && this.a == 16777216) {
            a(map.get("notification_image_text_color"));
        }
        RemoteViews mo2134a2 = mo2134a();
        int i = this.a;
        mo2134a2.setTextColor(a3, (i == 16777216 || !m2135a(i)) ? -1 : -16777216);
        setCustomContentView(mo2134a());
        Bundle bundle = new Bundle();
        bundle.putBoolean("miui.customHeight", true);
        addExtras(bundle);
    }

    @Override // com.xiaomi.push.es, com.xiaomi.push.eq
    /* renamed from: a */
    public boolean mo2134a() {
        if (!m.m2399a()) {
            return false;
        }
        Resources resources = mo2134a().getResources();
        String packageName = mo2134a().getPackageName();
        return (a(mo2134a().getResources(), "bg", "id", mo2134a().getPackageName()) == 0 || a(resources, CallMethod.RESULT_ICON, "id", packageName) == 0 || a(resources, "title", "id", packageName) == 0 || m.a(mo2134a()) < 9) ? false : true;
    }

    public ep b(Bitmap bitmap) {
        if (m2137b() && bitmap != null) {
            this.c = bitmap;
        }
        return this;
    }

    @Override // com.xiaomi.push.es
    public String b() {
        return null;
    }
}
