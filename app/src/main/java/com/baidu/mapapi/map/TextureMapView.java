package com.baidu.mapapi.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ch.qos.logback.classic.Level;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.platform.comapi.map.MapTextureView;
import com.nexstreaming.nexeditorsdk.nexCrop;
import java.io.File;

/* loaded from: classes.dex */
public final class TextureMapView extends ViewGroup {
    private static String i;
    private static final SparseArray<Integer> q;
    private int A;
    private boolean B;
    private MapTextureView b;
    private BaiduMap c;
    private ImageView d;
    private Bitmap e;
    private com.baidu.mapsdkplatform.comapi.map.ac f;
    private Point g;
    private Point h;
    private RelativeLayout l;
    private TextView m;
    private TextView n;
    private ImageView o;
    private Context p;
    private float r;
    private int s;
    private boolean t;
    private boolean u;
    private int v;
    private int w;
    private int x;
    private int y;
    private int z;
    private static final String a = TextureMapView.class.getSimpleName();
    private static int j = 0;
    private static int k = 0;

    static {
        SparseArray<Integer> sparseArray = new SparseArray<>();
        q = sparseArray;
        sparseArray.append(3, 2000000);
        sparseArray.append(4, 1000000);
        sparseArray.append(5, 500000);
        sparseArray.append(6, 200000);
        sparseArray.append(7, Integer.valueOf((int) nexCrop.ABSTRACT_DIMENSION));
        sparseArray.append(8, 50000);
        sparseArray.append(9, 25000);
        sparseArray.append(10, Integer.valueOf((int) Level.INFO_INT));
        sparseArray.append(11, 10000);
        sparseArray.append(12, 5000);
        sparseArray.append(13, 2000);
        sparseArray.append(14, 1000);
        sparseArray.append(15, 500);
        sparseArray.append(16, 200);
        sparseArray.append(17, 100);
        sparseArray.append(18, 50);
        sparseArray.append(19, 20);
        sparseArray.append(20, 10);
        sparseArray.append(21, 5);
        sparseArray.append(22, 2);
    }

    public TextureMapView(Context context) {
        super(context);
        this.s = LogoPosition.logoPostionleftBottom.ordinal();
        this.t = true;
        this.u = true;
        this.B = false;
        a(context, (BaiduMapOptions) null);
    }

    public TextureMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.s = LogoPosition.logoPostionleftBottom.ordinal();
        this.t = true;
        this.u = true;
        this.B = false;
        a(context, (BaiduMapOptions) null);
    }

    public TextureMapView(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.s = LogoPosition.logoPostionleftBottom.ordinal();
        this.t = true;
        this.u = true;
        this.B = false;
        a(context, (BaiduMapOptions) null);
    }

    public TextureMapView(Context context, BaiduMapOptions baiduMapOptions) {
        super(context);
        this.s = LogoPosition.logoPostionleftBottom.ordinal();
        this.t = true;
        this.u = true;
        this.B = false;
        a(context, baiduMapOptions);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(android.content.Context r10) {
        /*
            r9 = this;
            int r0 = com.baidu.mapapi.common.SysOSUtil.getDensityDpi()
            r1 = 180(0xb4, float:2.52E-43)
            if (r0 >= r1) goto Lb
            java.lang.String r1 = "logo_l.png"
            goto Ld
        Lb:
            java.lang.String r1 = "logo_h.png"
        Ld:
            android.graphics.Bitmap r2 = com.baidu.mapsdkplatform.comapi.commonutils.a.a(r1, r10)
            r1 = 480(0x1e0, float:6.73E-43)
            if (r0 <= r1) goto L31
            android.graphics.Matrix r7 = new android.graphics.Matrix
            r7.<init>()
            r0 = 1073741824(0x40000000, float:2.0)
        L1c:
            r7.postScale(r0, r0)
            r3 = 0
            r4 = 0
            int r5 = r2.getWidth()
            int r6 = r2.getHeight()
            r8 = 1
            android.graphics.Bitmap r0 = android.graphics.Bitmap.createBitmap(r2, r3, r4, r5, r6, r7, r8)
            r9.e = r0
            goto L41
        L31:
            r3 = 320(0x140, float:4.48E-43)
            if (r0 <= r3) goto L3f
            if (r0 > r1) goto L3f
            android.graphics.Matrix r7 = new android.graphics.Matrix
            r7.<init>()
            r0 = 1069547520(0x3fc00000, float:1.5)
            goto L1c
        L3f:
            r9.e = r2
        L41:
            android.graphics.Bitmap r0 = r9.e
            if (r0 == 0) goto L56
            android.widget.ImageView r0 = new android.widget.ImageView
            r0.<init>(r10)
            r9.d = r0
            android.graphics.Bitmap r10 = r9.e
            r0.setImageBitmap(r10)
            android.widget.ImageView r10 = r9.d
            r9.addView(r10)
        L56:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.TextureMapView.a(android.content.Context):void");
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions) {
        Point point;
        Point point2;
        LogoPosition logoPosition;
        setBackgroundColor(-1);
        this.p = context;
        com.baidu.mapsdkplatform.comapi.map.i.a();
        BMapManager.init();
        a(context, baiduMapOptions, i, k);
        a(context);
        b(context);
        if (baiduMapOptions != null && !baiduMapOptions.h) {
            this.f.setVisibility(4);
        }
        c(context);
        if (baiduMapOptions != null && !baiduMapOptions.i) {
            this.l.setVisibility(4);
        }
        if (baiduMapOptions != null && (logoPosition = baiduMapOptions.j) != null) {
            this.s = logoPosition.ordinal();
        }
        if (baiduMapOptions != null && (point2 = baiduMapOptions.l) != null) {
            this.h = point2;
        }
        if (baiduMapOptions == null || (point = baiduMapOptions.k) == null) {
            return;
        }
        this.g = point;
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions, String str, int i2) {
        MapTextureView mapTextureView = new MapTextureView(context);
        this.b = mapTextureView;
        addView(mapTextureView);
        if (baiduMapOptions != null) {
            this.c = new BaiduMap(context, this.b, baiduMapOptions.a());
        } else {
            this.c = new BaiduMap(context, this.b, (com.baidu.mapsdkplatform.comapi.map.u) null);
        }
        this.b.getBaseMap().a(new ab(this));
    }

    private void a(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -2);
        }
        int i2 = layoutParams.width;
        int makeMeasureSpec = i2 > 0 ? View.MeasureSpec.makeMeasureSpec(i2, 1073741824) : View.MeasureSpec.makeMeasureSpec(0, 0);
        int i3 = layoutParams.height;
        view.measure(makeMeasureSpec, i3 > 0 ? View.MeasureSpec.makeMeasureSpec(i3, 1073741824) : View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, MapCustomStyleOptions mapCustomStyleOptions) {
        if (TextUtils.isEmpty(str)) {
            str = mapCustomStyleOptions.getLocalCustomStyleFilePath();
            if (TextUtils.isEmpty(str)) {
                return;
            }
        }
        a(str, "");
        setMapCustomStyleEnable(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, String str2) {
        MapTextureView mapTextureView = this.b;
        if (mapTextureView == null || mapTextureView.getBaseMap() == null) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            Log.e(a, "customStyleFilePath is empty or null, please check!");
        } else if (!str.endsWith(".sty")) {
            Log.e(a, "customStyleFile format is incorrect , please check!");
        } else if (!new File(str).exists()) {
            Log.e(a, "customStyleFile does not exist , please check!");
        } else {
            this.b.getBaseMap().b(str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        com.baidu.mapsdkplatform.comapi.map.ac acVar = this.f;
        if (acVar == null || !acVar.a()) {
            return;
        }
        float f = this.b.getBaseMap().C().a;
        boolean z = true;
        this.f.b(f > this.b.getBaseMap().b);
        com.baidu.mapsdkplatform.comapi.map.ac acVar2 = this.f;
        if (f >= this.b.getBaseMap().a) {
            z = false;
        }
        acVar2.a(z);
    }

    private void b(Context context) {
        com.baidu.mapsdkplatform.comapi.map.ac acVar = new com.baidu.mapsdkplatform.comapi.map.ac(context);
        this.f = acVar;
        if (!acVar.a()) {
            return;
        }
        this.f.b(new ac(this));
        this.f.a(new ad(this));
        addView(this.f);
    }

    private void c(Context context) {
        this.l = new RelativeLayout(context);
        this.l.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.m = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(14);
        this.m.setTextColor(Color.parseColor("#FFFFFF"));
        this.m.setTextSize(2, 11.0f);
        TextView textView = this.m;
        textView.setTypeface(textView.getTypeface(), 1);
        this.m.setLayoutParams(layoutParams);
        this.m.setId(Integer.MAX_VALUE);
        this.l.addView(this.m);
        this.n = new TextView(context);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.width = -2;
        layoutParams2.height = -2;
        layoutParams2.addRule(14);
        this.n.setTextColor(Color.parseColor("#000000"));
        this.n.setTextSize(2, 11.0f);
        this.n.setLayoutParams(layoutParams2);
        this.l.addView(this.n);
        this.o = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams3.width = -2;
        layoutParams3.height = -2;
        layoutParams3.addRule(14);
        layoutParams3.addRule(3, this.m.getId());
        this.o.setLayoutParams(layoutParams3);
        Bitmap a2 = com.baidu.mapsdkplatform.comapi.commonutils.a.a("icon_scale.9.png", context);
        byte[] ninePatchChunk = a2.getNinePatchChunk();
        NinePatch.isNinePatchChunk(ninePatchChunk);
        this.o.setBackgroundDrawable(new NinePatchDrawable(a2, ninePatchChunk, new Rect(), null));
        this.l.addView(this.o);
        addView(this.l);
    }

    @Deprecated
    public static void setCustomMapStylePath(String str) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("BDMapSDKException: customMapStylePath String is illegal");
        }
        if (!new File(str).exists()) {
            throw new RuntimeException("BDMapSDKException: please check whether the customMapStylePath file exits");
        }
        i = str;
    }

    @Deprecated
    public static void setIconCustom(int i2) {
        k = i2;
    }

    @Deprecated
    public static void setLoadCustomMapStyleFileMode(int i2) {
        j = i2;
    }

    @Deprecated
    public static void setMapCustomEnable(boolean z) {
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof MapViewLayoutParams) {
            super.addView(view, layoutParams);
        }
    }

    public final LogoPosition getLogoPosition() {
        int i2 = this.s;
        return i2 != 1 ? i2 != 2 ? i2 != 3 ? i2 != 4 ? i2 != 5 ? LogoPosition.logoPostionleftBottom : LogoPosition.logoPostionRightTop : LogoPosition.logoPostionRightBottom : LogoPosition.logoPostionCenterTop : LogoPosition.logoPostionCenterBottom : LogoPosition.logoPostionleftTop;
    }

    public final BaiduMap getMap() {
        BaiduMap baiduMap = this.c;
        baiduMap.b = this;
        return baiduMap;
    }

    public final int getMapLevel() {
        return q.get((int) this.b.getBaseMap().C().a).intValue();
    }

    public Point getScaleControlPosition() {
        return this.g;
    }

    public int getScaleControlViewHeight() {
        return this.A;
    }

    public int getScaleControlViewWidth() {
        return this.A;
    }

    public void onCreate(Context context, Bundle bundle) {
        if (bundle == null) {
            return;
        }
        MapStatus mapStatus = (MapStatus) bundle.getParcelable("mapstatus");
        if (this.g != null) {
            this.g = (Point) bundle.getParcelable("scalePosition");
        }
        if (this.h != null) {
            this.h = (Point) bundle.getParcelable("zoomPosition");
        }
        this.t = bundle.getBoolean("mZoomControlEnabled");
        this.u = bundle.getBoolean("mScaleControlEnabled");
        this.s = bundle.getInt("logoPosition");
        setPadding(bundle.getInt("paddingLeft"), bundle.getInt("paddingTop"), bundle.getInt("paddingRight"), bundle.getInt("paddingBottom"));
        a(context, new BaiduMapOptions().mapStatus(mapStatus));
    }

    public final void onDestroy() {
        if (this.p != null) {
            this.b.onDestroy();
        }
        Bitmap bitmap = this.e;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.e.recycle();
        }
        this.f.b();
        BMapManager.destroy();
        com.baidu.mapsdkplatform.comapi.map.i.b();
        this.p = null;
    }

    @Override // android.view.ViewGroup, android.view.View
    @SuppressLint({"NewApi"})
    public final void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        float f;
        int measuredHeight;
        int measuredWidth;
        int childCount = getChildCount();
        a(this.d);
        float f2 = 1.0f;
        if (((getWidth() - this.v) - this.w) - this.d.getMeasuredWidth() <= 0 || ((getHeight() - this.x) - this.y) - this.d.getMeasuredHeight() <= 0) {
            this.v = 0;
            this.w = 0;
            this.y = 0;
            this.x = 0;
            f = 1.0f;
        } else {
            f2 = ((getWidth() - this.v) - this.w) / getWidth();
            f = ((getHeight() - this.x) - this.y) / getHeight();
        }
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt != null) {
                MapTextureView mapTextureView = this.b;
                if (childAt == mapTextureView) {
                    mapTextureView.layout(0, 0, getWidth(), getHeight());
                } else {
                    ImageView imageView = this.d;
                    if (childAt == imageView) {
                        float f3 = f2 * 5.0f;
                        int i7 = (int) (this.v + f3);
                        int i8 = (int) (this.w + f3);
                        float f4 = 5.0f * f;
                        int i9 = (int) (this.x + f4);
                        int i10 = (int) (this.y + f4);
                        int i11 = this.s;
                        if (i11 != 1) {
                            if (i11 == 2) {
                                measuredHeight = getHeight() - i10;
                                i9 = measuredHeight - this.d.getMeasuredHeight();
                            } else if (i11 != 3) {
                                if (i11 == 4) {
                                    measuredHeight = getHeight() - i10;
                                    i9 = measuredHeight - this.d.getMeasuredHeight();
                                } else if (i11 != 5) {
                                    measuredHeight = getHeight() - i10;
                                    measuredWidth = this.d.getMeasuredWidth() + i7;
                                    i9 = measuredHeight - this.d.getMeasuredHeight();
                                } else {
                                    measuredHeight = i9 + imageView.getMeasuredHeight();
                                }
                                measuredWidth = getWidth() - i8;
                                i7 = measuredWidth - this.d.getMeasuredWidth();
                            } else {
                                measuredHeight = i9 + imageView.getMeasuredHeight();
                            }
                            i7 = (((getWidth() - this.d.getMeasuredWidth()) + this.v) - this.w) / 2;
                            measuredWidth = (((getWidth() + this.d.getMeasuredWidth()) + this.v) - this.w) / 2;
                        } else {
                            measuredHeight = imageView.getMeasuredHeight() + i9;
                            measuredWidth = this.d.getMeasuredWidth() + i7;
                        }
                        this.d.layout(i7, i9, measuredWidth, measuredHeight);
                    } else {
                        com.baidu.mapsdkplatform.comapi.map.ac acVar = this.f;
                        if (childAt != acVar) {
                            RelativeLayout relativeLayout = this.l;
                            if (childAt == relativeLayout) {
                                a(relativeLayout);
                                Point point = this.g;
                                if (point == null) {
                                    this.A = this.l.getMeasuredWidth();
                                    this.z = this.l.getMeasuredHeight();
                                    int i12 = (int) (this.v + (5.0f * f2));
                                    int height = (getHeight() - ((int) ((this.y + (f * 5.0f)) + 56.0f))) - this.d.getMeasuredHeight();
                                    this.l.layout(i12, height, this.A + i12, this.z + height);
                                } else {
                                    RelativeLayout relativeLayout2 = this.l;
                                    int i13 = point.x;
                                    relativeLayout2.layout(i13, point.y, relativeLayout2.getMeasuredWidth() + i13, this.g.y + this.l.getMeasuredHeight());
                                }
                            } else {
                                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                                if (layoutParams instanceof MapViewLayoutParams) {
                                    MapViewLayoutParams mapViewLayoutParams = (MapViewLayoutParams) layoutParams;
                                    Point a2 = mapViewLayoutParams.c == MapViewLayoutParams.ELayoutMode.absoluteMode ? mapViewLayoutParams.b : this.b.getBaseMap().a(CoordUtil.ll2mc(mapViewLayoutParams.a));
                                    a(childAt);
                                    int measuredWidth2 = childAt.getMeasuredWidth();
                                    int measuredHeight2 = childAt.getMeasuredHeight();
                                    int i14 = (int) (a2.x - (mapViewLayoutParams.d * measuredWidth2));
                                    int i15 = ((int) (a2.y - (mapViewLayoutParams.e * measuredHeight2))) + mapViewLayoutParams.f;
                                    childAt.layout(i14, i15, measuredWidth2 + i14, measuredHeight2 + i15);
                                }
                            }
                        } else if (acVar.a()) {
                            a(this.f);
                            Point point2 = this.h;
                            if (point2 == null) {
                                int height2 = (int) (((getHeight() - 15) * f) + this.x);
                                int width = (int) (((getWidth() - 15) * f2) + this.v);
                                int measuredWidth3 = width - this.f.getMeasuredWidth();
                                int measuredHeight3 = height2 - this.f.getMeasuredHeight();
                                if (this.s == 4) {
                                    height2 -= this.d.getMeasuredHeight();
                                    measuredHeight3 -= this.d.getMeasuredHeight();
                                }
                                this.f.layout(measuredWidth3, measuredHeight3, width, height2);
                            } else {
                                com.baidu.mapsdkplatform.comapi.map.ac acVar2 = this.f;
                                int i16 = point2.x;
                                acVar2.layout(i16, point2.y, acVar2.getMeasuredWidth() + i16, this.h.y + this.f.getMeasuredHeight());
                            }
                        }
                    }
                }
            }
        }
    }

    public final void onPause() {
        this.b.onPause();
    }

    public final void onResume() {
        this.b.onResume();
    }

    public void onSaveInstanceState(Bundle bundle) {
        BaiduMap baiduMap;
        if (bundle == null || (baiduMap = this.c) == null) {
            return;
        }
        bundle.putParcelable("mapstatus", baiduMap.getMapStatus());
        Point point = this.g;
        if (point != null) {
            bundle.putParcelable("scalePosition", point);
        }
        Point point2 = this.h;
        if (point2 != null) {
            bundle.putParcelable("zoomPosition", point2);
        }
        bundle.putBoolean("mZoomControlEnabled", this.t);
        bundle.putBoolean("mScaleControlEnabled", this.u);
        bundle.putInt("logoPosition", this.s);
        bundle.putInt("paddingLeft", this.v);
        bundle.putInt("paddingTop", this.x);
        bundle.putInt("paddingRight", this.w);
        bundle.putInt("paddingBottom", this.y);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        if (view == this.d) {
            return;
        }
        super.removeView(view);
    }

    public void setCustomStyleFilePathAndMode(String str, int i2) {
    }

    public final void setLogoPosition(LogoPosition logoPosition) {
        if (logoPosition == null) {
            logoPosition = LogoPosition.logoPostionleftBottom;
        }
        this.s = logoPosition.ordinal();
        requestLayout();
    }

    public void setMapCustomStyle(MapCustomStyleOptions mapCustomStyleOptions, CustomMapStyleCallBack customMapStyleCallBack) {
        if (mapCustomStyleOptions == null) {
            return;
        }
        String customMapStyleId = mapCustomStyleOptions.getCustomMapStyleId();
        if (customMapStyleId != null && !customMapStyleId.isEmpty()) {
            com.baidu.mapsdkplatform.comapi.map.e.a().a(this.p, customMapStyleId, new aa(this, customMapStyleCallBack, mapCustomStyleOptions));
            return;
        }
        String localCustomStyleFilePath = mapCustomStyleOptions.getLocalCustomStyleFilePath();
        if (localCustomStyleFilePath == null || localCustomStyleFilePath.isEmpty()) {
            return;
        }
        a(localCustomStyleFilePath, "");
    }

    public void setMapCustomStyleEnable(boolean z) {
        MapTextureView mapTextureView = this.b;
        if (mapTextureView == null) {
            return;
        }
        mapTextureView.getBaseMap().o(z);
    }

    public void setMapCustomStylePath(String str) {
        a(str, "");
    }

    @Override // android.view.View
    public void setPadding(int i2, int i3, int i4, int i5) {
        this.v = i2;
        this.x = i3;
        this.w = i4;
        this.y = i5;
    }

    public void setScaleControlPosition(Point point) {
        int i2;
        if (point != null && (i2 = point.x) >= 0 && point.y >= 0 && i2 <= getWidth() && point.y <= getHeight()) {
            this.g = point;
            requestLayout();
        }
    }

    public void setZoomControlsPosition(Point point) {
        int i2;
        if (point != null && (i2 = point.x) >= 0 && point.y >= 0 && i2 <= getWidth() && point.y <= getHeight()) {
            this.h = point;
            requestLayout();
        }
    }

    public void showScaleControl(boolean z) {
        this.l.setVisibility(z ? 0 : 8);
        this.u = z;
    }

    public void showZoomControls(boolean z) {
        if (this.f.a()) {
            this.f.setVisibility(z ? 0 : 8);
            this.t = z;
        }
    }
}
