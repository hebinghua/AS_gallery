package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import com.baidu.mapapi.UIMsg;
import com.nexstreaming.nexeditorsdk.nexCollage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: nexCollageDrawInfo.java */
/* loaded from: classes3.dex */
public class a implements nexCollageInfo, nexCollageInfoDraw {
    private static String d = "nexCollageDrawInfo";
    public nexClip a;
    public RectF b;
    public nexCollage.CollageInfoChangedListener c;
    private float e;
    private float f;
    private RectF g;
    private String i;
    private String j;
    private String k;
    private String l;
    private String m;
    private String n;
    private float q;
    private Rect r;
    private RectF s;
    private RectF t;
    private List<PointF> h = new ArrayList();
    private int o = 0;
    private int p = 0;

    public void a(RectF rectF) {
        this.t = null;
        if (rectF != null) {
            this.t = new RectF(rectF);
        }
    }

    public void a(nexCollage.CollageInfoChangedListener collageInfoChangedListener) {
        this.c = collageInfoChangedListener;
    }

    public boolean a() {
        return this.j.compareTo("system") == 0 || this.j.compareTo("system_mt") == 0;
    }

    public boolean b() {
        return this.j.compareTo("system_mt") == 0;
    }

    public String a(Context context) {
        return nexAssetPackageManager.getAssetPackageMediaPath(context, this.k);
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfo
    public String getId() {
        return this.i;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfo
    public RectF getRectangle() {
        return this.g;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfo
    public List<PointF> getPositions() {
        return this.h;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public int getTagID() {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return 0;
        }
        return this.a.getDrawInfos().get(0).getID();
    }

    private Rect a(nexDrawInfo nexdrawinfo, Rect rect, int i, int i2) {
        int userTranslateX = (nexdrawinfo.getUserTranslateX() * i) / nexCrop.ABSTRACT_DIMENSION;
        int userTranslateY = (nexdrawinfo.getUserTranslateY() * i2) / nexCrop.ABSTRACT_DIMENSION;
        float width = rect.width() * 0.5f;
        float height = rect.height() * 0.5f;
        float f = -width;
        float f2 = -height;
        double d2 = -((float) Math.toRadians(nexdrawinfo.getUserRotateState()));
        float cos = (float) Math.cos(d2);
        float sin = (float) Math.sin(d2);
        float f3 = cos * f;
        float f4 = sin * f2;
        float f5 = f3 - f4;
        float f6 = f * sin;
        float f7 = f2 * cos;
        float f8 = f6 + f7;
        float f9 = cos * width;
        float f10 = f9 - f4;
        float f11 = sin * width;
        float f12 = f7 + f11;
        float f13 = sin * height;
        float f14 = f3 - f13;
        float f15 = cos * height;
        float f16 = f6 + f15;
        float f17 = f9 - f13;
        float f18 = f11 + f15;
        float min = Math.min(Math.min(Math.min(f5, f10), f14), f17);
        float min2 = Math.min(Math.min(Math.min(f8, f12), f16), f18);
        float max = Math.max(Math.max(Math.max(f5, f10), f14), f17);
        float max2 = Math.max(Math.max(Math.max(f8, f12), f16), f18);
        Rect rect2 = new Rect();
        rect2.left = ((int) (min + width)) + userTranslateX + rect.left;
        rect2.top = ((int) (min2 + height)) + userTranslateY + rect.top;
        rect2.right = ((int) (max + width)) + userTranslateX + rect.left;
        rect2.bottom = ((int) (max2 + height)) + userTranslateY + rect.top;
        return rect2;
    }

    private void h() {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return;
        }
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        int width = this.a.getWidth();
        int height = this.a.getHeight();
        if (this.a.getRotateInMeta() == 90 || this.a.getRotateInMeta() == 270) {
            width = this.a.getHeight();
            height = this.a.getWidth();
        }
        if (this.r == null) {
            Rect rect = new Rect(nexdrawinfo.getStartRect());
            this.r = rect;
            nexCollage.a(rect, width, height);
        }
        Rect rect2 = new Rect(this.r);
        Rect a = a(nexdrawinfo, rect2, width, height);
        float exactCenterX = a.exactCenterX();
        float exactCenterY = a.exactCenterY();
        float f = 1.0f;
        float width2 = a.width() * 0.5f;
        float height2 = a.height() * 0.5f;
        if (a.left < 0) {
            f = Math.min(exactCenterX / width2, 1.0f);
        }
        if (a.right > width) {
            f = Math.min((width - exactCenterX) / width2, f);
        }
        if (a.top < 0) {
            f = Math.min(exactCenterY / height2, f);
        }
        if (a.bottom > height) {
            f = Math.min((height - exactCenterY) / height2, f);
        }
        float height3 = rect2.height() * 0.5f * f;
        float f2 = this.q * height3;
        float exactCenterX2 = rect2.exactCenterX();
        float exactCenterY2 = rect2.exactCenterY();
        RectF rectF = this.s;
        rectF.left = exactCenterX2 - f2;
        rectF.right = exactCenterX2 + f2;
        rectF.top = exactCenterY2 - height3;
        rectF.bottom = exactCenterY2 + height3;
        rectF.round(rect2);
        nexCollage.b(rect2, width, height);
        nexdrawinfo.setStartRect(rect2);
        nexdrawinfo.setEndRect(rect2);
    }

    private void i() {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return;
        }
        int i = 0;
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        int width = this.a.getWidth();
        int height = this.a.getHeight();
        if (this.a.getRotateInMeta() == 90 || this.a.getRotateInMeta() == 270) {
            width = this.a.getHeight();
            height = this.a.getWidth();
        }
        Rect rect = new Rect();
        RectF rectF = this.s;
        rect.set((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
        Rect a = a(nexdrawinfo, rect, width, height);
        int i2 = a.left;
        int i3 = i2 < 0 ? 0 - i2 : 0;
        int i4 = a.right;
        if (i4 > width) {
            i3 -= i4 - width;
        }
        int i5 = a.top;
        if (i5 < 0) {
            i = 0 - i5;
        }
        int i6 = a.bottom;
        if (i6 > height) {
            i -= i6 - height;
        }
        if (i3 == 0 && i == 0) {
            return;
        }
        RectF rectF2 = this.s;
        float f = i3;
        rectF2.left += f;
        rectF2.right += f;
        float f2 = i;
        rectF2.top += f2;
        rectF2.bottom += f2;
        rectF2.round(rect);
        nexCollage.b(rect, width, height);
        nexdrawinfo.setStartRect(rect);
        nexdrawinfo.setEndRect(rect);
    }

    private boolean j() {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return false;
        }
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        int width = this.a.getWidth();
        int height = this.a.getHeight();
        if (this.a.getRotateInMeta() == 90 || this.a.getRotateInMeta() == 270) {
            width = this.a.getHeight();
            height = this.a.getWidth();
        }
        Rect rect = new Rect(nexdrawinfo.getStartRect());
        nexCollage.a(rect, width, height);
        Rect a = a(nexdrawinfo, rect, width, height);
        String str = d;
        Log.d(str, "aabb:" + a.toString() + " width:" + width + " height:" + height + " r:" + rect.toString());
        if (a.width() > width || a.height() > height) {
            return false;
        }
        Log.d(d, "aabb checkTranformOk");
        return true;
    }

    private void k() {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return;
        }
        int i = 0;
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        int width = this.a.getWidth();
        int height = this.a.getHeight();
        if (this.a.getRotateInMeta() == 90 || this.a.getRotateInMeta() == 270) {
            width = this.a.getHeight();
            height = this.a.getWidth();
        }
        Rect rect = new Rect(nexdrawinfo.getStartRect());
        nexCollage.a(rect, width, height);
        Rect a = a(nexdrawinfo, rect, width, height);
        int i2 = a.left;
        int i3 = i2 < 0 ? 0 - i2 : 0;
        int i4 = a.right;
        if (i4 > width) {
            i3 -= i4 - width;
        }
        int i5 = a.top;
        if (i5 < 0) {
            i = 0 - i5;
        }
        int i6 = a.bottom;
        if (i6 > height) {
            i -= i6 - height;
        }
        if (i3 == 0 && i == 0) {
            return;
        }
        nexdrawinfo.setUserTranslate(((((nexdrawinfo.getUserTranslateX() * width) / nexCrop.ABSTRACT_DIMENSION) + i3) * nexCrop.ABSTRACT_DIMENSION) / width, ((((nexdrawinfo.getUserTranslateY() * height) / nexCrop.ABSTRACT_DIMENSION) + i) * nexCrop.ABSTRACT_DIMENSION) / height);
    }

    private void l() {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return;
        }
        if (this.t == null) {
            this.t = new RectF(this.s);
        }
        this.a.getDrawInfos().get(0).setRealScale((float) Math.sqrt((this.t.width() * this.t.height()) / (this.s.width() * this.s.height())));
    }

    private void a(Rect rect) {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return;
        }
        if (this.t == null) {
            this.t = new RectF(this.s);
        }
        this.a.getDrawInfos().get(0).setRealScale((float) Math.sqrt((this.t.width() * this.t.height()) / (rect.width() * rect.height())));
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public boolean setTranslate(int i, int i2, int i3, int i4) {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return false;
        }
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        double d2 = -((float) Math.toRadians(nexdrawinfo.getUserRotateState()));
        float cos = (float) Math.cos(d2);
        float sin = (float) Math.sin(d2);
        float f = i;
        float f2 = i2;
        float f3 = (f * cos) - (f2 * sin);
        float f4 = (f * sin) + (f2 * cos);
        int width = this.a.getWidth();
        int height = this.a.getHeight();
        if (this.a.getRotateInMeta() == 90 || this.a.getRotateInMeta() == 270) {
            width = this.a.getHeight();
            height = this.a.getWidth();
        }
        if (this.s == null) {
            Rect rect = new Rect(nexdrawinfo.getStartRect());
            nexCollage.a(rect, width, height);
            this.s = new RectF(rect);
        }
        float sqrt = (float) Math.sqrt((this.s.width() * this.s.height()) / (this.o * this.p));
        nexdrawinfo.setUserTranslate(((((int) (f3 * sqrt)) * nexCrop.ABSTRACT_DIMENSION) / width) + nexdrawinfo.getUserTranslateX(), ((((int) (f4 * sqrt)) * nexCrop.ABSTRACT_DIMENSION) / height) + nexdrawinfo.getUserTranslateY());
        k();
        nexCollage.CollageInfoChangedListener collageInfoChangedListener = this.c;
        if (collageInfoChangedListener == null) {
            return true;
        }
        collageInfoChangedListener.DrawInfoChanged(nexdrawinfo);
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public int getRotate() {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return 0;
        }
        return this.a.getDrawInfos().get(0).getUserRotateState();
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public boolean setRotate(int i, int i2) {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return false;
        }
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        Log.d(d, String.format("setRotate: %d %d", Integer.valueOf(i), Integer.valueOf(i2)));
        if (i != 1 && i != 2) {
            if (this.s == null) {
                int width = this.a.getWidth();
                int height = this.a.getHeight();
                if (this.a.getRotateInMeta() == 90 || this.a.getRotateInMeta() == 270) {
                    width = this.a.getHeight();
                    height = this.a.getWidth();
                }
                Rect rect = new Rect(nexdrawinfo.getStartRect());
                nexCollage.a(rect, width, height);
                this.s = new RectF(rect);
            }
            nexdrawinfo.setUserRotateState(i2);
            h();
            l();
            nexCollage.CollageInfoChangedListener collageInfoChangedListener = this.c;
            if (collageInfoChangedListener != null) {
                collageInfoChangedListener.DrawInfoChanged(nexdrawinfo);
            }
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0058  */
    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean setFlip(int r6) {
        /*
            r5 = this;
            com.nexstreaming.nexeditorsdk.nexClip r0 = r5.a
            r1 = 0
            if (r0 == 0) goto L5c
            java.util.List r0 = r0.getDrawInfos()
            if (r0 == 0) goto L5c
            com.nexstreaming.nexeditorsdk.nexClip r0 = r5.a
            java.util.List r0 = r0.getDrawInfos()
            int r0 = r0.size()
            if (r0 > 0) goto L18
            goto L5c
        L18:
            com.nexstreaming.nexeditorsdk.nexClip r0 = r5.a
            java.util.List r0 = r0.getDrawInfos()
            java.lang.Object r0 = r0.get(r1)
            com.nexstreaming.nexeditorsdk.nexDrawInfo r0 = (com.nexstreaming.nexeditorsdk.nexDrawInfo) r0
            int r1 = r0.getRotateState()
            r2 = 1
            if (r6 != 0) goto L30
            r6 = -196609(0xfffffffffffcffff, float:NaN)
        L2e:
            r6 = r6 & r1
            goto L51
        L30:
            r3 = r6 & 1
            if (r3 != r2) goto L40
            r3 = 65536(0x10000, float:9.18355E-41)
            r4 = r1 & r3
            if (r4 != r3) goto L3f
            r3 = -65537(0xfffffffffffeffff, float:NaN)
            r1 = r1 & r3
            goto L40
        L3f:
            r1 = r1 | r3
        L40:
            r3 = 2
            r6 = r6 & r3
            if (r6 != r3) goto L50
            r6 = 131072(0x20000, float:1.83671E-40)
            r3 = r1 & r6
            if (r3 != r6) goto L4e
            r6 = -131073(0xfffffffffffdffff, float:NaN)
            goto L2e
        L4e:
            r6 = r6 | r1
            goto L51
        L50:
            r6 = r1
        L51:
            r0.setRotateState(r6)
            com.nexstreaming.nexeditorsdk.nexCollage$CollageInfoChangedListener r6 = r5.c
            if (r6 == 0) goto L5b
            r6.DrawInfoChanged(r0)
        L5b:
            return r2
        L5c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.a.setFlip(int):boolean");
    }

    public void a(boolean z) {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return;
        }
        int width = this.a.getWidth();
        int height = this.a.getHeight();
        if (this.a.getRotateInMeta() == 90 || this.a.getRotateInMeta() == 270) {
            width = this.a.getHeight();
            height = this.a.getWidth();
        }
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        if (this.t == null) {
            Rect rect = new Rect(0, 0, width, height);
            nexCollage.a(rect, this.q);
            a(new RectF(rect));
        }
        float centerX = this.t.centerX();
        float centerY = this.t.centerY();
        Rect rect2 = new Rect(nexdrawinfo.getStartRect());
        nexCollage.a(rect2, width, height);
        if (z) {
            a(rect2);
        }
        float realScale = nexdrawinfo.getRealScale();
        float width2 = this.t.width() / realScale;
        float height2 = this.t.height() / realScale;
        float exactCenterX = rect2.exactCenterX();
        float exactCenterY = rect2.exactCenterY();
        double d2 = centerX;
        double d3 = width2 / 2.0d;
        rect2.left = (int) (d2 - d3);
        double d4 = centerY;
        double d5 = height2 / 2.0d;
        int i = width;
        int i2 = height;
        rect2.top = (int) (d4 - d5);
        rect2.right = (int) (d2 + d3);
        rect2.bottom = (int) (d4 + d5);
        nexCollage.a(rect2, this.q);
        int exactCenterX2 = (int) (exactCenterX - rect2.exactCenterX());
        int exactCenterY2 = (int) (exactCenterY - rect2.exactCenterY());
        rect2.left += exactCenterX2;
        rect2.right += exactCenterX2;
        rect2.top += exactCenterY2;
        rect2.bottom += exactCenterY2;
        this.s = new RectF(rect2);
        nexCollage.b(rect2, i, i2);
        nexdrawinfo.setStartRect(rect2);
        nexdrawinfo.setEndRect(rect2);
        this.r = null;
        i();
        h();
        if (!z) {
            return;
        }
        l();
        this.r = null;
    }

    public void a(float f) {
        float f2;
        float f3;
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return;
        }
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        int width = this.a.getWidth();
        int height = this.a.getHeight();
        if (this.a.getRotateInMeta() == 90 || this.a.getRotateInMeta() == 270) {
            width = this.a.getHeight();
            height = this.a.getWidth();
        }
        if (this.t == null) {
            this.t = new RectF(this.s);
        }
        float sqrt = (float) Math.sqrt((this.t.width() * this.t.height()) / (this.s.width() * this.s.height()));
        String str = d;
        Log.d(str, "scale_chk:" + sqrt + " display_rect:" + this.s.toString());
        float centerX = this.s.centerX();
        float centerY = this.s.centerY();
        this.s.width();
        float height2 = this.s.height() * 0.5f;
        if (f > 0.0f) {
            if (sqrt > 2.0f) {
                return;
            }
            f2 = height2 * 0.97f;
            f3 = this.q;
        } else if (sqrt < 0.2f) {
            return;
        } else {
            f2 = height2 * 1.03f;
            f3 = this.q;
        }
        float f4 = f3 * f2;
        RectF rectF = this.s;
        rectF.left = centerX - f4;
        rectF.right = centerX + f4;
        rectF.top = centerY - f2;
        rectF.bottom = centerY + f2;
        Rect rect = new Rect();
        this.s.round(rect);
        nexCollage.b(rect, width, height);
        nexdrawinfo.setStartRect(rect);
        nexdrawinfo.setEndRect(rect);
        this.r = null;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public boolean setScale(float f) {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return false;
        }
        if (f == 0.0f) {
            return true;
        }
        Log.d(d, String.format("setScale %f", Float.valueOf(f)));
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        if (this.s == null) {
            int width = this.a.getWidth();
            int height = this.a.getHeight();
            if (this.a.getRotateInMeta() == 90 || this.a.getRotateInMeta() == 270) {
                width = this.a.getHeight();
                height = this.a.getWidth();
            }
            Rect rect = new Rect(nexdrawinfo.getStartRect());
            nexCollage.a(rect, width, height);
            this.s = new RectF(rect);
            this.t = new RectF(this.s);
        }
        RectF rectF = new RectF(this.s);
        Rect rect2 = new Rect(nexdrawinfo.getStartRect());
        int userTranslateX = nexdrawinfo.getUserTranslateX();
        int userTranslateY = nexdrawinfo.getUserTranslateY();
        a(f);
        k();
        if (!j()) {
            this.s = rectF;
            nexdrawinfo.setStartRect(rect2);
            nexdrawinfo.setEndRect(rect2);
            nexdrawinfo.setUserTranslate(userTranslateX, userTranslateY);
        }
        l();
        nexCollage.CollageInfoChangedListener collageInfoChangedListener = this.c;
        if (collageInfoChangedListener != null) {
            collageInfoChangedListener.DrawInfoChanged(nexdrawinfo);
        }
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public String getLut() {
        nexDrawInfo nexdrawinfo;
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0 || (nexdrawinfo = this.a.getDrawInfos().get(0)) == null) {
            return null;
        }
        return nexdrawinfo.getUserLUT();
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public boolean setLut(String str) {
        nexColorEffect lutColorEffect;
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return false;
        }
        nexDrawInfo nexdrawinfo = this.a.getDrawInfos().get(0);
        if (str == null || str.compareTo("none") == 0) {
            nexdrawinfo.setUserLUT(null);
            nexdrawinfo.setLUT(0);
            nexCollage.CollageInfoChangedListener collageInfoChangedListener = this.c;
            if (collageInfoChangedListener != null) {
                collageInfoChangedListener.DrawInfoChanged(nexdrawinfo);
            }
            return true;
        } else if (str.compareTo("default") == 0) {
            nexdrawinfo.setUserLUT(null);
            if (e() != null && e().compareTo("none") != 0) {
                nexColorEffect lutColorEffect2 = nexColorEffect.getLutColorEffect(e());
                if (lutColorEffect2 != null) {
                    nexdrawinfo.setLUT(lutColorEffect2.getLUTId());
                }
            } else {
                nexdrawinfo.setLUT(0);
            }
            nexCollage.CollageInfoChangedListener collageInfoChangedListener2 = this.c;
            if (collageInfoChangedListener2 != null) {
                collageInfoChangedListener2.DrawInfoChanged(nexdrawinfo);
            }
            return true;
        } else if ((str.compareTo("none") == 0 && str.compareTo("null") == 0) || (lutColorEffect = nexColorEffect.getLutColorEffect(str)) == null) {
            return false;
        } else {
            nexdrawinfo.setLUT(lutColorEffect.getLUTId());
            nexdrawinfo.setUserLUT(str);
            nexCollage.CollageInfoChangedListener collageInfoChangedListener3 = this.c;
            if (collageInfoChangedListener3 != null) {
                collageInfoChangedListener3.DrawInfoChanged(nexdrawinfo);
            }
            return true;
        }
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public boolean getVisible() {
        nexDrawInfo nexdrawinfo;
        nexClip nexclip = this.a;
        return (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0 || (nexdrawinfo = this.a.getDrawInfos().get(0)) == null || nexdrawinfo.getBrightness() == -255) ? false : true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public boolean setVisible(boolean z) {
        nexDrawInfo nexdrawinfo;
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0 || (nexdrawinfo = this.a.getDrawInfos().get(0)) == null) {
            return false;
        }
        if (z) {
            nexdrawinfo.setBrightness(0);
        } else {
            nexdrawinfo.setBrightness(UIMsg.m_AppUI.V_WM_ADDLISTUPDATE);
        }
        nexCollage.CollageInfoChangedListener collageInfoChangedListener = this.c;
        if (collageInfoChangedListener == null) {
            return true;
        }
        collageInfoChangedListener.DrawInfoChanged(nexdrawinfo);
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public boolean changeSource(nexClip nexclip) {
        nexDrawInfo nexdrawinfo;
        nexClip nexclip2 = this.a;
        if (nexclip2 == null || nexclip2.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0 || (nexdrawinfo = this.a.getDrawInfos().get(0)) == null) {
            return false;
        }
        nexclip.clearDrawInfos();
        nexclip.addDrawInfo(nexdrawinfo);
        nexClip nexclip3 = this.a;
        nexclip.mStartTime = nexclip3.mStartTime;
        nexclip.mEndTime = nexclip3.mEndTime;
        if (nexclip.getClipType() == 4) {
            nexclip.setAudioOnOff(false);
        } else {
            if (nexclip.getClipType() == 1) {
                nexclip.setImageClipDuration(this.a.getProjectDuration());
            }
            return false;
        }
        int width = nexclip.getWidth();
        int height = nexclip.getHeight();
        if (nexclip.getRotateInMeta() == 90 || nexclip.getRotateInMeta() == 270) {
            width = nexclip.getHeight();
            height = nexclip.getWidth();
        }
        Rect rect = new Rect(0, 0, width, height);
        Rect rect2 = new Rect(0, 0, 1, 1);
        if (nexclip.getClipType() == 1) {
            RectF rectF = new RectF();
            nexCollage.CollageInfoChangedListener collageInfoChangedListener = this.c;
            if (collageInfoChangedListener != null) {
                rectF = collageInfoChangedListener.FaceRect(nexclip.getPath());
            }
            float f = width;
            float f2 = height;
            rect.set((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
            if (rect.isEmpty()) {
                rect = new Rect(0, 0, width, height);
            } else {
                rect2.set((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
                int i = rect.right;
                int i2 = rect.left;
                int i3 = (i - i2) / 2;
                int i4 = i2 - i3;
                rect.left = i4;
                int i5 = i + i3;
                rect.right = i5;
                if (i4 < 0) {
                    rect.left = 0;
                }
                if (i5 > width) {
                    rect.right = width;
                }
                int i6 = rect.bottom;
                int i7 = rect.top;
                int i8 = (i6 - i7) / 2;
                int i9 = i7 - i8;
                rect.top = i9;
                int i10 = i6 + i8;
                rect.bottom = i10;
                if (i9 < 0) {
                    rect.top = 0;
                }
                if (i10 > height) {
                    rect.bottom = height;
                }
            }
        }
        nexCollage.a(rect, this.q);
        nexCollage.b(rect, width, height);
        nexCollage.b(rect2, width, height);
        nexdrawinfo.setStartRect(rect);
        nexdrawinfo.setEndRect(rect);
        nexdrawinfo.setFaceRect(rect2);
        if (nexclip.getClipType() == 4) {
            nexdrawinfo.setRotateState(nexclip.getRotateInMeta());
        } else {
            nexdrawinfo.setRotateState(0);
        }
        nexdrawinfo.setUserRotateState(0);
        nexdrawinfo.setUserTranslate(0, 0);
        nexdrawinfo.setRealScale(1.0f);
        nexdrawinfo.setBrightness(0);
        this.r = null;
        this.s = null;
        this.b = new RectF(0.0f, 0.0f, width, height);
        nexClip nexclip4 = this.a;
        this.a = nexclip;
        this.t = null;
        a(true);
        nexCollage.CollageInfoChangedListener collageInfoChangedListener2 = this.c;
        if (collageInfoChangedListener2 != null) {
            collageInfoChangedListener2.SourceChanged(nexclip4, this.a);
            return true;
        }
        return false;
    }

    public boolean a(nexClip nexclip, nexDrawInfo nexdrawinfo) {
        nexClip nexclip2;
        if (nexclip == null || nexdrawinfo == null || (nexclip2 = this.a) == null || nexclip2.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return false;
        }
        nexclip.clearDrawInfos();
        nexclip.addDrawInfo(nexdrawinfo);
        nexClip nexclip3 = this.a;
        nexclip.mStartTime = nexclip3.mStartTime;
        nexclip.mEndTime = nexclip3.mEndTime;
        if (nexclip.getClipType() == 1) {
            nexclip.setImageClipDuration(this.a.getProjectDuration());
        }
        int width = nexclip.getWidth();
        int height = nexclip.getHeight();
        if (nexclip.getRotateInMeta() == 90 || nexclip.getRotateInMeta() == 270) {
            width = nexclip.getHeight();
            height = nexclip.getWidth();
        }
        float f = width;
        float f2 = height;
        this.b = new RectF(0.0f, 0.0f, f, f2);
        this.a = nexclip;
        Rect rect = new Rect(0, 0, width, height);
        Rect rect2 = new Rect(0, 0, 1, 1);
        if (nexclip.getClipType() == 1) {
            RectF rectF = new RectF();
            nexCollage.CollageInfoChangedListener collageInfoChangedListener = this.c;
            if (collageInfoChangedListener != null) {
                rectF = collageInfoChangedListener.FaceRect(nexclip.getPath());
            }
            rect.set((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
            if (rect.isEmpty()) {
                rect = new Rect(0, 0, width, height);
            } else {
                rect2.set((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
                int i = rect.right;
                int i2 = rect.left;
                int i3 = (i - i2) / 2;
                int i4 = i2 - i3;
                rect.left = i4;
                int i5 = i + i3;
                rect.right = i5;
                if (i4 < 0) {
                    rect.left = 0;
                }
                if (i5 > width) {
                    rect.right = width;
                }
                int i6 = rect.bottom;
                int i7 = rect.top;
                int i8 = (i6 - i7) / 2;
                int i9 = i7 - i8;
                rect.top = i9;
                int i10 = i6 + i8;
                rect.bottom = i10;
                if (i9 < 0) {
                    rect.top = 0;
                }
                if (i10 > height) {
                    rect.bottom = height;
                }
            }
        }
        nexCollage.a(rect, this.q);
        nexCollage.b(rect, width, height);
        nexCollage.b(rect2, width, height);
        nexdrawinfo.setStartRect(rect);
        nexdrawinfo.setEndRect(rect);
        nexdrawinfo.setFaceRect(rect2);
        this.r = null;
        this.s = null;
        this.b = new RectF(0.0f, 0.0f, f, f2);
        this.t = null;
        a(false);
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public nexClip getBindSource() {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0) {
            return null;
        }
        return this.a;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoDraw
    public boolean setAudioVolume(int i) {
        nexClip nexclip = this.a;
        if (nexclip == null || nexclip.getDrawInfos() == null || this.a.getDrawInfos().size() <= 0 || this.a.getClipType() == 1) {
            return false;
        }
        if (i == 0) {
            this.a.setAudioOnOff(false);
            return true;
        }
        this.a.setAudioOnOff(true);
        this.a.setClipVolume(i);
        return true;
    }

    public float c() {
        return this.e;
    }

    public float d() {
        return this.f;
    }

    public String e() {
        return this.l;
    }

    public float a(PointF pointF, PointF pointF2, PointF pointF3) {
        float f = pointF2.x;
        float f2 = pointF.x;
        float f3 = pointF3.y;
        float f4 = pointF.y;
        return ((f - f2) * (f3 - f4)) - ((pointF3.x - f2) * (pointF2.y - f4));
    }

    public int a(PointF pointF, List<PointF> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size() - 1; i2++) {
            if (list.get(i2).y <= pointF.y) {
                int i3 = i2 + 1;
                if (list.get(i3).y > pointF.y && a(list.get(i2), list.get(i3), pointF) > 0.0f) {
                    i++;
                }
            } else {
                int i4 = i2 + 1;
                if (list.get(i4).y <= pointF.y && a(list.get(i2), list.get(i4), pointF) < 0.0f) {
                    i--;
                }
            }
        }
        return i;
    }

    public boolean a(float f, float f2) {
        List<PointF> list = this.h;
        if (list != null && list.size() > 0) {
            return a(new PointF(f, f2), this.h) > 0;
        }
        RectF rectF = this.g;
        if (rectF == null) {
            return false;
        }
        return rectF.contains(f, f2);
    }

    public void a(nexClip nexclip) {
        this.a = nexclip;
        if (nexclip != null) {
            int width = nexclip.getWidth();
            int height = nexclip.getHeight();
            if (nexclip.getRotateInMeta() == 90 || nexclip.getRotateInMeta() == 270) {
                width = nexclip.getHeight();
                height = nexclip.getWidth();
            }
            this.b = new RectF(0.0f, 0.0f, width, height);
        }
    }

    public void b(float f) {
        this.q = f;
    }

    public String a(String str) {
        if (this.a != null) {
            return new SimpleDateFormat(str).format(new Date(new File(this.a.getPath()).lastModified()));
        }
        return "";
    }

    public String a(nexProject nexproject, int i) {
        float f = i;
        int i2 = (int) (this.e * f);
        String str = this.m;
        if (str == null || str.length() <= 0 || this.m.equals("none")) {
            return null;
        }
        nexproject.updateProject();
        nexClip supportedClip = nexClip.getSupportedClip(nexAssetPackageManager.getAssetPackageMediaPath(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), this.m));
        if (supportedClip == null) {
            return null;
        }
        int totalTime = supportedClip.getTotalTime();
        supportedClip.setCollageDrawInfoID(this.i);
        supportedClip.setAssetResource(true);
        int parseFloat = i2 + ((int) (f * Float.parseFloat(this.n)));
        nexproject.addAudio(supportedClip, parseFloat, totalTime + parseFloat);
        return null;
    }

    public int f() {
        return this.o;
    }

    public int g() {
        return this.p;
    }

    public String a(JSONObject jSONObject) {
        try {
            this.e = Float.parseFloat(jSONObject.getString("start"));
            this.f = Float.parseFloat(jSONObject.getString("end"));
            if (jSONObject.has("position")) {
                String[] split = jSONObject.getString("position").replace(" ", "").split(",");
                if (split == null) {
                    return "Wrong position data of drawinfo";
                }
                this.h.clear();
                if (split.length == 4) {
                    float parseFloat = Float.parseFloat(split[0]);
                    float parseFloat2 = Float.parseFloat(split[1]);
                    float parseFloat3 = Float.parseFloat(split[2]);
                    float parseFloat4 = Float.parseFloat(split[3]);
                    PointF pointF = new PointF(parseFloat, parseFloat2);
                    PointF pointF2 = new PointF(parseFloat3, parseFloat2);
                    PointF pointF3 = new PointF(parseFloat3, parseFloat4);
                    PointF pointF4 = new PointF(parseFloat, parseFloat4);
                    this.h.add(pointF);
                    this.h.add(pointF2);
                    this.h.add(pointF3);
                    this.h.add(pointF4);
                    this.h.add(pointF);
                    this.g = new RectF(parseFloat, parseFloat2, parseFloat3, parseFloat4);
                } else {
                    float f = Float.MIN_VALUE;
                    float f2 = Float.MAX_VALUE;
                    float f3 = Float.MIN_VALUE;
                    float f4 = Float.MAX_VALUE;
                    for (int i = 0; i < split.length; i += 2) {
                        float parseFloat5 = Float.parseFloat(split[i]);
                        float parseFloat6 = Float.parseFloat(split[i + 1]);
                        if (f4 > parseFloat5) {
                            f4 = parseFloat5;
                        }
                        if (f < parseFloat5) {
                            f = parseFloat5;
                        }
                        if (f2 > parseFloat6) {
                            f2 = parseFloat6;
                        }
                        if (f3 < parseFloat6) {
                            f3 = parseFloat6;
                        }
                        this.h.add(new PointF(parseFloat5, parseFloat6));
                    }
                    this.g = new RectF(f4, f2, f, f3);
                }
            }
            this.i = jSONObject.getString("draw_id");
            this.j = jSONObject.getString("source_type");
            this.k = jSONObject.getString("source_default");
            this.l = jSONObject.getString("lut");
            this.m = jSONObject.getString("audio_res");
            this.n = jSONObject.getString("audio_res_pos");
            if (jSONObject.has("draw_width")) {
                this.o = Integer.parseInt(jSONObject.getString("draw_width"));
            }
            if (!jSONObject.has("draw_height")) {
                return null;
            }
            this.p = Integer.parseInt(jSONObject.getString("draw_height"));
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(d, "parse Collage failed : " + e.getMessage());
            return e.getMessage();
        }
    }
}
