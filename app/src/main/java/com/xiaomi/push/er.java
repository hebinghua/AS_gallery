package com.xiaomi.push;

import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.nexstreaming.nexeditorsdk.nexEngine;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.MiStat;
import java.util.Map;

/* loaded from: classes3.dex */
public class er extends es {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public PendingIntent f301a;
    public int b;

    /* renamed from: b  reason: collision with other field name */
    public Bitmap f302b;
    public int c;

    /* renamed from: c  reason: collision with other field name */
    public CharSequence f303c;

    public er(Context context, int i, String str) {
        super(context, i, str);
        this.a = nexEngine.ExportHEVCMainTierLevel62;
        this.b = nexEngine.ExportHEVCMainTierLevel62;
        this.c = nexEngine.ExportHEVCMainTierLevel62;
    }

    public final Drawable a(int i, int i2, int i3, float f) {
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
        shapeDrawable.getPaint().setColor(i);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.setIntrinsicWidth(i2);
        shapeDrawable.setIntrinsicHeight(i3);
        return shapeDrawable;
    }

    @Override // com.xiaomi.push.es
    /* renamed from: a */
    public er mo2129a(Bitmap bitmap) {
        if (m2137b() && bitmap != null) {
            if (bitmap.getWidth() != 984 || bitmap.getHeight() < 177 || bitmap.getHeight() > 207) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("colorful notification bg image resolution error, must [984*177, 984*207]");
            } else {
                this.f302b = bitmap;
            }
        }
        return this;
    }

    public er a(CharSequence charSequence, PendingIntent pendingIntent) {
        if (m2137b()) {
            super.addAction(0, charSequence, pendingIntent);
            this.f303c = charSequence;
            this.f301a = pendingIntent;
        }
        return this;
    }

    @Override // com.xiaomi.push.eq
    public er a(String str) {
        if (m2137b() && !TextUtils.isEmpty(str)) {
            try {
                this.b = Color.parseColor(str);
            } catch (Exception unused) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("parse colorful notification button bg color error");
            }
        }
        return this;
    }

    @Override // com.xiaomi.push.es, com.xiaomi.push.eq
    /* renamed from: a */
    public String mo2134a() {
        return "notification_colorful";
    }

    @Override // com.xiaomi.push.es, com.xiaomi.push.eq
    /* renamed from: a  reason: collision with other method in class */
    public void mo2134a() {
        RemoteViews mo2134a;
        Bitmap bitmap;
        boolean z;
        RemoteViews mo2134a2;
        RemoteViews mo2134a3;
        Drawable a;
        if (!m2137b()) {
            m2136b();
            return;
        }
        super.mo2134a();
        Resources resources = mo2134a().getResources();
        String packageName = mo2134a().getPackageName();
        int a2 = a(resources, CallMethod.RESULT_ICON, "id", packageName);
        if (((es) this).f304a == null) {
            a(a2);
        } else {
            mo2134a().setImageViewBitmap(a2, ((es) this).f304a);
        }
        int a3 = a(resources, "title", "id", packageName);
        int a4 = a(resources, MiStat.Param.CONTENT, "id", packageName);
        mo2134a().setTextViewText(a3, ((es) this).f306a);
        mo2134a().setTextViewText(a4, ((es) this).f311b);
        if (!TextUtils.isEmpty(this.f303c)) {
            int a5 = a(resources, "buttonContainer", "id", packageName);
            int a6 = a(resources, "button", "id", packageName);
            int a7 = a(resources, "buttonBg", "id", packageName);
            mo2134a().setViewVisibility(a5, 0);
            mo2134a().setTextViewText(a6, this.f303c);
            mo2134a().setOnClickPendingIntent(a5, this.f301a);
            if (this.b != 16777216) {
                int a8 = a(70.0f);
                int a9 = a(29.0f);
                mo2134a().setImageViewBitmap(a7, com.xiaomi.push.service.al.a(a(this.b, a8, a9, a9 / 2.0f)));
                mo2134a().setTextColor(a6, m2135a(this.b) ? -1 : -16777216);
            }
        }
        int a10 = a(resources, "bg", "id", packageName);
        int a11 = a(resources, "container", "id", packageName);
        if (this.a != 16777216) {
            if (m.a(mo2134a()) >= 10) {
                mo2134a3 = mo2134a();
                a = a(this.a, 984, 192, 30.0f);
            } else {
                mo2134a3 = mo2134a();
                a = a(this.a, 984, 192, 0.0f);
            }
            mo2134a3.setImageViewBitmap(a10, com.xiaomi.push.service.al.a(a));
            mo2134a2 = mo2134a();
            z = m2135a(this.a);
        } else if (this.f302b == null) {
            if (Build.VERSION.SDK_INT >= 24) {
                mo2134a().setViewVisibility(a2, 8);
                mo2134a().setViewVisibility(a10, 8);
                try {
                    bk.a((Object) this, "setStyle", v.a(mo2134a(), "android.app.Notification$DecoratedCustomViewStyle").getConstructor(new Class[0]).newInstance(new Object[0]));
                } catch (Exception unused) {
                    com.xiaomi.channel.commonutils.logger.b.m1859a("load class DecoratedCustomViewStyle failed");
                }
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean("miui.customHeight", true);
            addExtras(bundle);
            setCustomContentView(mo2134a());
        } else {
            if (m.a(mo2134a()) >= 10) {
                mo2134a = mo2134a();
                bitmap = a(this.f302b, 30.0f);
            } else {
                mo2134a = mo2134a();
                bitmap = this.f302b;
            }
            mo2134a.setImageViewBitmap(a10, bitmap);
            Map<String, String> map = ((es) this).f309a;
            if (map != null && this.c == 16777216) {
                c(map.get("notification_image_text_color"));
            }
            int i = this.c;
            z = i == 16777216 || !m2135a(i);
            mo2134a2 = mo2134a();
        }
        a(mo2134a2, a11, a3, a4, z);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("miui.customHeight", true);
        addExtras(bundle2);
        setCustomContentView(mo2134a());
    }

    public final void a(RemoteViews remoteViews, int i, int i2, int i3, boolean z) {
        int a = a(6.0f);
        remoteViews.setViewPadding(i, a, 0, a, 0);
        int i4 = z ? -1 : -16777216;
        remoteViews.setTextColor(i2, i4);
        remoteViews.setTextColor(i3, i4);
    }

    @Override // com.xiaomi.push.es, com.xiaomi.push.eq
    /* renamed from: a */
    public boolean mo2134a() {
        if (!m.m2399a()) {
            return false;
        }
        Resources resources = mo2134a().getResources();
        String packageName = mo2134a().getPackageName();
        return (a(resources, CallMethod.RESULT_ICON, "id", packageName) == 0 || a(resources, "title", "id", packageName) == 0 || a(resources, MiStat.Param.CONTENT, "id", packageName) == 0) ? false : true;
    }

    public er b(String str) {
        if (m2137b() && !TextUtils.isEmpty(str)) {
            try {
                this.a = Color.parseColor(str);
            } catch (Exception unused) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("parse colorful notification bg color error");
            }
        }
        return this;
    }

    @Override // com.xiaomi.push.es
    public String b() {
        return "notification_colorful_copy";
    }

    public er c(String str) {
        if (m2137b() && !TextUtils.isEmpty(str)) {
            try {
                this.c = Color.parseColor(str);
            } catch (Exception unused) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("parse colorful notification image text color error");
            }
        }
        return this;
    }
}
