package com.baidu.platform.comapi.map;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class OverlayItem {
    public static final int ALIGN_BOTTON = 2;
    public static final int ALIGN_TOP = 3;
    public static final int ALING_CENTER = 1;
    public GeoPoint a;
    public String b;
    public String c;
    private int e;
    private int f;
    private Bundle m;
    private Bundle n;
    private float o;
    private byte[] p;
    private float q;
    private int s;
    private CoordType i = CoordType.CoordType_BD09;
    private Drawable g = null;
    private int r = 0;
    private int d = 2;
    private String h = "";
    private float j = 0.5f;
    private float k = 1.0f;
    private ArrayList<Bundle> l = new ArrayList<>();

    /* loaded from: classes.dex */
    public enum AnimEffect {
        NONE,
        GROWTH,
        WAVE,
        SHRINK,
        FADE_OUT,
        FADE_IN,
        GROWTH_FADE_IN,
        SHRINK_FADE_OUT,
        GROWTH_REBOUND,
        ALPHA,
        ANCHOR_GROUTH,
        ROTATE
    }

    /* loaded from: classes.dex */
    public enum AnimationSubType {
        NONE,
        RADAR
    }

    /* loaded from: classes.dex */
    public enum CoordType {
        CoordType_BD09LL,
        CoordType_BD09
    }

    public OverlayItem(GeoPoint geoPoint, String str, String str2) {
        this.a = geoPoint;
        this.b = str;
        this.c = str2;
    }

    public void addClickRect(Bundle bundle) {
        if (this.l == null) {
            this.l = new ArrayList<>();
        }
        this.l.add(bundle);
    }

    public float getAnchorX() {
        return this.j;
    }

    public float getAnchorY() {
        return this.k;
    }

    public Bundle getAnimate() {
        return this.m;
    }

    public int getBound() {
        return this.d;
    }

    public ArrayList<Bundle> getClickRect() {
        return this.l;
    }

    public CoordType getCoordType() {
        return this.i;
    }

    public Bundle getDelay() {
        return this.n;
    }

    public float getGeoZ() {
        return this.o;
    }

    public byte[] getGifData() {
        return this.p;
    }

    public String getId() {
        return this.h;
    }

    public int getIndoorPoi() {
        return this.s;
    }

    public int getLevel() {
        return this.e;
    }

    public final Drawable getMarker() {
        return this.g;
    }

    public int getMask() {
        return this.f;
    }

    public float getMultiplyDpi() {
        return this.r;
    }

    public GeoPoint getPoint() {
        return this.a;
    }

    public int getResId() {
        if (getMarker() == null) {
            return -1;
        }
        return getMarker().hashCode();
    }

    public float getScale() {
        return this.q;
    }

    public String getSnippet() {
        return this.c;
    }

    public String getTitle() {
        return this.b;
    }

    public void setAnchor(float f, float f2) {
        this.j = f;
        this.k = f2;
    }

    public void setAnchor(int i) {
        float f;
        if (i == 1) {
            setAnchor(0.5f, 0.5f);
            return;
        }
        if (i == 2) {
            f = 1.0f;
        } else if (i != 3) {
            return;
        } else {
            f = 0.0f;
        }
        setAnchor(0.5f, f);
    }

    public void setAnimate(Bundle bundle) {
        this.m = bundle;
    }

    public void setAnimateDuration(int i) {
        if (this.m == null) {
            this.m = new Bundle();
        }
        this.m.putInt("dur", i);
    }

    public void setAnimateEffect(AnimEffect animEffect) {
        Bundle bundle;
        int i;
        if (this.m == null) {
            this.m = new Bundle();
        }
        switch (ac.a[animEffect.ordinal()]) {
            case 1:
                bundle = this.m;
                i = 1;
                break;
            case 2:
                bundle = this.m;
                i = 2;
                break;
            case 3:
                bundle = this.m;
                i = 3;
                break;
            case 4:
                bundle = this.m;
                i = 4;
                break;
            case 5:
                bundle = this.m;
                i = 5;
                break;
            case 6:
                bundle = this.m;
                i = 6;
                break;
            case 7:
                bundle = this.m;
                i = 7;
                break;
            case 8:
                bundle = this.m;
                i = 8;
                break;
            case 9:
                bundle = this.m;
                i = 9;
                break;
            case 10:
                bundle = this.m;
                i = 10;
                break;
            case 11:
                bundle = this.m;
                i = 11;
                break;
            default:
                bundle = this.m;
                i = 0;
                break;
        }
        bundle.putInt(nexExportFormat.TAG_FORMAT_TYPE, i);
    }

    public void setAnimateEndSize(int i, int i2) {
        if (this.m == null) {
            this.m = new Bundle();
        }
        this.m.putInt("en_w", i);
        this.m.putInt("en_h", i2);
    }

    public void setAnimateStartSize(int i, int i2) {
        if (this.m == null) {
            this.m = new Bundle();
        }
        this.m.putInt("st_w", i);
        this.m.putInt("st_h", i2);
    }

    public void setBound(int i) {
        this.d = i;
    }

    public void setClickRect(ArrayList<Bundle> arrayList) {
        this.l = arrayList;
    }

    public void setCoordType(CoordType coordType) {
        this.i = coordType;
    }

    public void setDelay(Bundle bundle) {
        this.n = bundle;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.a = geoPoint;
    }

    public void setGeoZ(float f) {
        this.o = f;
    }

    public void setGifData(byte[] bArr) {
        this.p = bArr;
    }

    public void setId(String str) {
        this.h = str;
    }

    public void setIndoorPoi(int i) {
        this.s = i;
    }

    public void setLevel(int i) {
        this.e = i;
    }

    public void setMarker(Drawable drawable) {
        this.g = drawable;
    }

    public void setMask(int i) {
        this.f = i;
    }

    public void setMultiplyDpi(int i) {
        this.r = i;
    }

    public void setScale(float f) {
        this.q = f;
    }

    public void setSnippet(String str) {
        this.c = str;
    }

    public void setSubAnimateEffect(AnimationSubType animationSubType) {
        Bundle bundle;
        if (this.m == null) {
            this.m = new Bundle();
        }
        int i = 1;
        if (ac.b[animationSubType.ordinal()] != 1) {
            bundle = this.m;
            i = 0;
        } else {
            bundle = this.m;
        }
        bundle.putInt("sub_type", i);
    }

    public void setTitle(String str) {
        this.b = str;
    }
}
