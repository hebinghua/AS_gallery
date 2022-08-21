package com.xiaomi.push;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public abstract class es extends eq {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public Bitmap f304a;

    /* renamed from: a  reason: collision with other field name */
    public RemoteViews f305a;

    /* renamed from: a  reason: collision with other field name */
    public CharSequence f306a;

    /* renamed from: a  reason: collision with other field name */
    public String f307a;

    /* renamed from: a  reason: collision with other field name */
    public ArrayList<Notification.Action> f308a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f309a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f310a;
    public int b;

    /* renamed from: b  reason: collision with other field name */
    public CharSequence f311b;

    /* renamed from: b  reason: collision with other field name */
    public boolean f312b;

    public es(Context context, int i, String str) {
        super(context);
        this.f308a = new ArrayList<>();
        this.b = 0;
        this.f307a = str;
        this.a = i;
        m2138c();
    }

    public es(Context context, String str) {
        this(context, 0, str);
    }

    public int a(float f) {
        return (int) ((f * mo2134a().getResources().getDisplayMetrics().density) + 0.5f);
    }

    @Override // com.xiaomi.push.eq
    /* renamed from: a */
    public final Bitmap mo2134a() {
        return com.xiaomi.push.service.al.a(h.m2212a(mo2134a(), this.f307a));
    }

    public Bitmap a(Bitmap bitmap, float f) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRoundRect(new RectF(rect), f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    @Override // com.xiaomi.push.eq
    /* renamed from: a  reason: collision with other method in class */
    public final RemoteViews mo2134a() {
        return this.f305a;
    }

    public eq a(Map<String, String> map) {
        this.f309a = map;
        return this;
    }

    @Override // android.app.Notification.Builder
    /* renamed from: a */
    public es addAction(int i, CharSequence charSequence, PendingIntent pendingIntent) {
        addAction(new Notification.Action(i, charSequence, pendingIntent));
        return this;
    }

    @Override // android.app.Notification.Builder
    /* renamed from: a */
    public es addAction(Notification.Action action) {
        if (action != null) {
            this.f308a.add(action);
        }
        int i = this.b;
        this.b = i + 1;
        a(i, action);
        return this;
    }

    @Override // android.app.Notification.Builder
    /* renamed from: a */
    public es setLargeIcon(Bitmap bitmap) {
        this.f304a = bitmap;
        return this;
    }

    @Override // android.app.Notification.Builder
    /* renamed from: a */
    public es setContentTitle(CharSequence charSequence) {
        this.f306a = charSequence;
        return this;
    }

    @Override // com.xiaomi.push.eq
    /* renamed from: a */
    public abstract String mo2134a();

    @Override // com.xiaomi.push.eq
    /* renamed from: a  reason: collision with other method in class */
    public void mo2134a() {
        super.a();
        Bundle bundle = new Bundle();
        if (m2140d()) {
            bundle.putBoolean("mipush.customCopyLayout", this.f312b);
        } else {
            bundle.putBoolean("mipush.customCopyLayout", false);
        }
        bundle.putBoolean("miui.customHeight", false);
        bundle.putBoolean("mipush.customNotification", true);
        bundle.putInt("mipush.customLargeIconId", a("large_icon"));
        if (this.f308a.size() > 0) {
            Notification.Action[] actionArr = new Notification.Action[this.f308a.size()];
            this.f308a.toArray(actionArr);
            bundle.putParcelableArray("mipush.customActions", actionArr);
        }
        if (m2139c() || !com.xiaomi.push.service.ay.m2486a(mo2134a().getContentResolver())) {
            d();
        } else {
            bundle.putCharSequence("mipush.customTitle", this.f306a);
            bundle.putCharSequence("mipush.customContent", this.f311b);
        }
        addExtras(bundle);
    }

    public void a(int i) {
        Bitmap mo2134a = mo2134a();
        if (mo2134a != null) {
            mo2134a().setImageViewBitmap(i, mo2134a);
            return;
        }
        int b = h.b(mo2134a(), this.f307a);
        if (b == 0) {
            return;
        }
        mo2134a().setImageViewResource(i, b);
    }

    public void a(int i, Notification.Action action) {
    }

    @Override // com.xiaomi.push.eq
    /* renamed from: a */
    public abstract boolean mo2134a();

    /* renamed from: a  reason: collision with other method in class */
    public final boolean m2135a(int i) {
        return ((((double) Color.red(i)) * 0.299d) + (((double) Color.green(i)) * 0.587d)) + (((double) Color.blue(i)) * 0.114d) < 192.0d;
    }

    @Override // android.app.Notification.Builder
    /* renamed from: b */
    public es setContentText(CharSequence charSequence) {
        this.f311b = charSequence;
        return this;
    }

    public abstract String b();

    /* renamed from: b  reason: collision with other method in class */
    public final void m2136b() {
        super.setContentTitle(this.f306a);
        super.setContentText(this.f311b);
        Bitmap bitmap = this.f304a;
        if (bitmap != null) {
            super.setLargeIcon(bitmap);
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    public final boolean m2137b() {
        return this.f310a;
    }

    public final String c() {
        boolean e = e();
        this.f312b = e;
        return e ? b() : mo2134a();
    }

    /* renamed from: c  reason: collision with other method in class */
    public final void m2138c() {
        int a = a(mo2134a().getResources(), c(), "layout", mo2134a().getPackageName());
        if (a == 0) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("create RemoteViews failed, no such layout resource was found");
            return;
        }
        this.f305a = new RemoteViews(mo2134a().getPackageName(), a);
        this.f310a = mo2134a();
    }

    /* renamed from: c  reason: collision with other method in class */
    public final boolean m2139c() {
        Map<String, String> map = this.f309a;
        return map != null && Boolean.parseBoolean(map.get("custom_builder_set_title"));
    }

    public final void d() {
        super.setContentTitle(this.f306a);
        super.setContentText(this.f311b);
    }

    /* renamed from: d  reason: collision with other method in class */
    public final boolean m2140d() {
        return !TextUtils.isEmpty(b()) && !TextUtils.isEmpty(this.f307a);
    }

    public final boolean e() {
        return m2140d() && f();
    }

    public final boolean f() {
        List<StatusBarNotification> m2484b;
        if (Build.VERSION.SDK_INT >= 20 && (m2484b = com.xiaomi.push.service.ax.a(mo2134a(), this.f307a).m2484b()) != null && !m2484b.isEmpty()) {
            for (StatusBarNotification statusBarNotification : m2484b) {
                if (statusBarNotification.getId() == this.a) {
                    Notification notification = statusBarNotification.getNotification();
                    if (notification != null) {
                        return !notification.extras.getBoolean("mipush.customCopyLayout", true);
                    }
                    return false;
                }
            }
        }
        return false;
    }
}
