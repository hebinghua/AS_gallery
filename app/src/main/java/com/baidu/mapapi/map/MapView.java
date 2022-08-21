package com.baidu.mapapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ch.qos.logback.classic.Level;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.platform.comapi.map.MapSurfaceView;
import com.nexstreaming.nexeditorsdk.nexCrop;
import java.io.File;

/* loaded from: classes.dex */
public final class MapView extends ViewGroup {
    private static String b;
    private static final SparseIntArray q;
    private int A;
    private boolean B;
    private MapSurfaceView e;
    private BaiduMap f;
    private ImageView g;
    private Bitmap h;
    private com.baidu.mapsdkplatform.comapi.map.ac i;
    private Point j;
    private Point k;
    private RelativeLayout l;
    private TextView m;
    private TextView n;
    private ImageView o;
    private Context p;
    private int r;
    private boolean s;
    private boolean t;
    private float u;
    private int v;
    private int w;
    private int x;
    private int y;
    private int z;
    private static final String a = MapView.class.getSimpleName();
    private static int c = 0;
    private static int d = 0;

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        q = sparseIntArray;
        sparseIntArray.append(3, 2000000);
        sparseIntArray.append(4, 1000000);
        sparseIntArray.append(5, 500000);
        sparseIntArray.append(6, 200000);
        sparseIntArray.append(7, nexCrop.ABSTRACT_DIMENSION);
        sparseIntArray.append(8, 50000);
        sparseIntArray.append(9, 25000);
        sparseIntArray.append(10, Level.INFO_INT);
        sparseIntArray.append(11, 10000);
        sparseIntArray.append(12, 5000);
        sparseIntArray.append(13, 2000);
        sparseIntArray.append(14, 1000);
        sparseIntArray.append(15, 500);
        sparseIntArray.append(16, 200);
        sparseIntArray.append(17, 100);
        sparseIntArray.append(18, 50);
        sparseIntArray.append(19, 20);
        sparseIntArray.append(20, 10);
        sparseIntArray.append(21, 5);
        sparseIntArray.append(22, 2);
        sparseIntArray.append(23, 2);
        sparseIntArray.append(24, 2);
        sparseIntArray.append(25, 2);
        sparseIntArray.append(26, 2);
    }

    public MapView(Context context) {
        super(context);
        this.r = LogoPosition.logoPostionleftBottom.ordinal();
        this.s = true;
        this.t = true;
        this.B = false;
        a(context, (BaiduMapOptions) null);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.r = LogoPosition.logoPostionleftBottom.ordinal();
        this.s = true;
        this.t = true;
        this.B = false;
        a(context, (BaiduMapOptions) null);
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.r = LogoPosition.logoPostionleftBottom.ordinal();
        this.s = true;
        this.t = true;
        this.B = false;
        a(context, (BaiduMapOptions) null);
    }

    public MapView(Context context, BaiduMapOptions baiduMapOptions) {
        super(context);
        this.r = LogoPosition.logoPostionleftBottom.ordinal();
        this.s = true;
        this.t = true;
        this.B = false;
        a(context, baiduMapOptions);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
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
            if (r2 != 0) goto L14
            return
        L14:
            r1 = 480(0x1e0, float:6.73E-43)
            if (r0 <= r1) goto L34
            android.graphics.Matrix r7 = new android.graphics.Matrix
            r7.<init>()
            r0 = 1073741824(0x40000000, float:2.0)
        L1f:
            r7.postScale(r0, r0)
            r3 = 0
            r4 = 0
            int r5 = r2.getWidth()
            int r6 = r2.getHeight()
            r8 = 1
            android.graphics.Bitmap r0 = android.graphics.Bitmap.createBitmap(r2, r3, r4, r5, r6, r7, r8)
            r9.h = r0
            goto L42
        L34:
            r1 = 320(0x140, float:4.48E-43)
            if (r0 <= r1) goto L40
            android.graphics.Matrix r7 = new android.graphics.Matrix
            r7.<init>()
            r0 = 1069547520(0x3fc00000, float:1.5)
            goto L1f
        L40:
            r9.h = r2
        L42:
            android.graphics.Bitmap r0 = r9.h
            if (r0 == 0) goto L57
            android.widget.ImageView r0 = new android.widget.ImageView
            r0.<init>(r10)
            r9.g = r0
            android.graphics.Bitmap r10 = r9.h
            r0.setImageBitmap(r10)
            android.widget.ImageView r10 = r9.g
            r9.addView(r10)
        L57:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.MapView.a(android.content.Context):void");
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions) {
        Point point;
        Point point2;
        LogoPosition logoPosition;
        this.p = context;
        com.baidu.mapsdkplatform.comapi.map.i.a();
        BMapManager.init();
        a(context, baiduMapOptions, b, c);
        a(context);
        b(context);
        if (baiduMapOptions != null && !baiduMapOptions.h) {
            this.i.setVisibility(4);
        }
        c(context);
        if (baiduMapOptions != null && !baiduMapOptions.i) {
            this.l.setVisibility(4);
        }
        if (baiduMapOptions != null && (logoPosition = baiduMapOptions.j) != null) {
            this.r = logoPosition.ordinal();
        }
        if (baiduMapOptions != null && (point2 = baiduMapOptions.l) != null) {
            this.k = point2;
        }
        if (baiduMapOptions == null || (point = baiduMapOptions.k) == null) {
            return;
        }
        this.j = point;
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions, String str, int i) {
        this.e = new MapSurfaceView(context);
        if (baiduMapOptions != null) {
            this.f = new BaiduMap(context, this.e, baiduMapOptions.a());
        } else {
            this.f = new BaiduMap(context, this.e, (com.baidu.mapsdkplatform.comapi.map.u) null);
        }
        addView(this.e);
        this.e.getBaseMap().a(new r(this));
    }

    private void a(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -2);
        }
        int i = layoutParams.width;
        int makeMeasureSpec = i > 0 ? View.MeasureSpec.makeMeasureSpec(i, 1073741824) : View.MeasureSpec.makeMeasureSpec(0, 0);
        int i2 = layoutParams.height;
        view.measure(makeMeasureSpec, i2 > 0 ? View.MeasureSpec.makeMeasureSpec(i2, 1073741824) : View.MeasureSpec.makeMeasureSpec(0, 0));
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
        MapSurfaceView mapSurfaceView = this.e;
        if (mapSurfaceView == null || mapSurfaceView.getBaseMap() == null) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            Log.e(a, "customStyleFilePath is empty or null, please check!");
        } else if (!str.endsWith(".sty")) {
            Log.e(a, "customStyleFile format is incorrect , please check!");
        } else if (!new File(str).exists()) {
            Log.e(a, "customStyleFile does not exist , please check!");
        } else {
            this.e.getBaseMap().b(str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        com.baidu.mapsdkplatform.comapi.map.ac acVar = this.i;
        if (acVar == null || !acVar.a()) {
            return;
        }
        float f = this.e.getBaseMap().C().a;
        boolean z = true;
        this.i.b(f > this.e.getBaseMap().b);
        com.baidu.mapsdkplatform.comapi.map.ac acVar2 = this.i;
        if (f >= this.e.getBaseMap().a) {
            z = false;
        }
        acVar2.a(z);
    }

    private void b(Context context) {
        com.baidu.mapsdkplatform.comapi.map.ac acVar = new com.baidu.mapsdkplatform.comapi.map.ac(context, false);
        this.i = acVar;
        if (!acVar.a()) {
            return;
        }
        this.i.b(new s(this));
        this.i.a(new t(this));
        addView(this.i);
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
        ImageView imageView = this.o;
        if (imageView != null) {
            imageView.setLayoutParams(layoutParams3);
            Bitmap a2 = com.baidu.mapsdkplatform.comapi.commonutils.a.a("icon_scale.9.png", context);
            if (a2 != null) {
                byte[] ninePatchChunk = a2.getNinePatchChunk();
                if (NinePatch.isNinePatchChunk(ninePatchChunk)) {
                    this.o.setBackgroundDrawable(new NinePatchDrawable(a2, ninePatchChunk, new Rect(), null));
                }
            }
            this.l.addView(this.o);
        }
        addView(this.l);
    }

    private boolean c() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    @Deprecated
    public static void setCustomMapStylePath(String str) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("BDMapSDKException: customMapStylePath String is illegal");
        }
        if (!new File(str).exists()) {
            throw new RuntimeException("BDMapSDKException: please check whether the customMapStylePath file exits");
        }
        b = str;
    }

    @Deprecated
    public static void setIconCustom(int i) {
        d = i;
    }

    @Deprecated
    public static void setLoadCustomMapStyleFileMode(int i) {
        c = i;
    }

    @Deprecated
    public static void setMapCustomEnable(boolean z) {
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof MapViewLayoutParams) {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            super.addView(view, layoutParams);
        }
    }

    public void cancelRenderMap() {
    }

    public final LogoPosition getLogoPosition() {
        int i = this.r;
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? LogoPosition.logoPostionleftBottom : LogoPosition.logoPostionRightTop : LogoPosition.logoPostionRightBottom : LogoPosition.logoPostionCenterTop : LogoPosition.logoPostionCenterBottom : LogoPosition.logoPostionleftTop;
    }

    public final BaiduMap getMap() {
        BaiduMap baiduMap = this.f;
        baiduMap.a = this;
        return baiduMap;
    }

    public final int getMapLevel() {
        return q.get(Math.round(this.e.getZoomLevel()));
    }

    public Point getScaleControlPosition() {
        return this.j;
    }

    public int getScaleControlViewHeight() {
        return this.z;
    }

    public int getScaleControlViewWidth() {
        return this.A;
    }

    public Point getZoomControlsPosition() {
        return this.k;
    }

    public boolean handleMultiTouch(float f, float f2, float f3, float f4) {
        return false;
    }

    public void handleTouchDown(float f, float f2) {
    }

    public boolean handleTouchMove(float f, float f2) {
        return false;
    }

    public boolean handleTouchUp(float f, float f2) {
        return false;
    }

    public boolean inRangeOfView(float f, float f2) {
        MapSurfaceView mapSurfaceView = this.e;
        return mapSurfaceView != null && mapSurfaceView.inRangeOfView(f, f2);
    }

    public boolean isShowScaleControl() {
        return this.t;
    }

    public void onCreate(Context context, Bundle bundle) {
        if (bundle == null) {
            return;
        }
        MapStatus mapStatus = (MapStatus) bundle.getParcelable("mapstatus");
        if (this.j != null) {
            this.j = (Point) bundle.getParcelable("scalePosition");
        }
        if (this.k != null) {
            this.k = (Point) bundle.getParcelable("zoomPosition");
        }
        this.s = bundle.getBoolean("mZoomControlEnabled");
        this.t = bundle.getBoolean("mScaleControlEnabled");
        this.r = bundle.getInt("logoPosition");
        setPadding(bundle.getInt("paddingLeft"), bundle.getInt("paddingTop"), bundle.getInt("paddingRight"), bundle.getInt("paddingBottom"));
        a(context, new BaiduMapOptions().mapStatus(mapStatus));
    }

    public final void onDestroy() {
        BaiduMap baiduMap = this.f;
        if (baiduMap != null) {
            baiduMap.c();
        }
        MapSurfaceView mapSurfaceView = this.e;
        if (mapSurfaceView != null) {
            mapSurfaceView.unInit();
        }
        Bitmap bitmap = this.h;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.h.recycle();
            this.h = null;
        }
        if (b != null) {
            b = null;
        }
        this.i.b();
        BMapManager.destroy();
        com.baidu.mapsdkplatform.comapi.map.i.b();
        this.p = null;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        int measuredHeight;
        int measuredWidth;
        int childCount = getChildCount();
        a(this.g);
        float f2 = 1.0f;
        if (((getWidth() - this.v) - this.w) - this.g.getMeasuredWidth() <= 0 || ((getHeight() - this.x) - this.y) - this.g.getMeasuredHeight() <= 0) {
            this.v = 0;
            this.w = 0;
            this.y = 0;
            this.x = 0;
            f = 1.0f;
        } else {
            f2 = ((getWidth() - this.v) - this.w) / getWidth();
            f = ((getHeight() - this.x) - this.y) / getHeight();
        }
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt != null) {
                MapSurfaceView mapSurfaceView = this.e;
                if (childAt == mapSurfaceView) {
                    mapSurfaceView.layout(0, 0, getWidth(), getHeight());
                } else {
                    ImageView imageView = this.g;
                    if (childAt == imageView) {
                        float f3 = f2 * 5.0f;
                        int i6 = (int) (this.v + f3);
                        int i7 = (int) (this.w + f3);
                        float f4 = 5.0f * f;
                        int i8 = (int) (this.x + f4);
                        int i9 = (int) (this.y + f4);
                        int i10 = this.r;
                        if (i10 != 1) {
                            if (i10 == 2) {
                                measuredHeight = getHeight() - i9;
                                i8 = measuredHeight - this.g.getMeasuredHeight();
                            } else if (i10 != 3) {
                                if (i10 == 4) {
                                    measuredHeight = getHeight() - i9;
                                    i8 = measuredHeight - this.g.getMeasuredHeight();
                                } else if (i10 != 5) {
                                    measuredHeight = getHeight() - i9;
                                    measuredWidth = this.g.getMeasuredWidth() + i6;
                                    i8 = measuredHeight - this.g.getMeasuredHeight();
                                } else {
                                    measuredHeight = i8 + imageView.getMeasuredHeight();
                                }
                                measuredWidth = getWidth() - i7;
                                i6 = measuredWidth - this.g.getMeasuredWidth();
                            } else {
                                measuredHeight = i8 + imageView.getMeasuredHeight();
                            }
                            i6 = (((getWidth() - this.g.getMeasuredWidth()) + this.v) - this.w) / 2;
                            measuredWidth = (((getWidth() + this.g.getMeasuredWidth()) + this.v) - this.w) / 2;
                        } else {
                            measuredHeight = imageView.getMeasuredHeight() + i8;
                            measuredWidth = this.g.getMeasuredWidth() + i6;
                        }
                        this.g.layout(i6, i8, measuredWidth, measuredHeight);
                    } else {
                        com.baidu.mapsdkplatform.comapi.map.ac acVar = this.i;
                        if (childAt != acVar) {
                            RelativeLayout relativeLayout = this.l;
                            if (childAt == relativeLayout) {
                                a(relativeLayout);
                                Point point = this.j;
                                if (point == null) {
                                    this.A = this.l.getMeasuredWidth();
                                    this.z = this.l.getMeasuredHeight();
                                    int i11 = (int) (this.v + (5.0f * f2));
                                    int height = (getHeight() - ((int) ((this.y + (f * 5.0f)) + 56.0f))) - this.g.getMeasuredHeight();
                                    this.l.layout(i11, height, this.A + i11, this.z + height);
                                } else {
                                    RelativeLayout relativeLayout2 = this.l;
                                    int i12 = point.x;
                                    relativeLayout2.layout(i12, point.y, relativeLayout2.getMeasuredWidth() + i12, this.j.y + this.l.getMeasuredHeight());
                                }
                            } else {
                                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                                if (layoutParams instanceof MapViewLayoutParams) {
                                    MapViewLayoutParams mapViewLayoutParams = (MapViewLayoutParams) layoutParams;
                                    Point a2 = mapViewLayoutParams.c == MapViewLayoutParams.ELayoutMode.absoluteMode ? mapViewLayoutParams.b : this.e.getBaseMap().a(CoordUtil.ll2mc(mapViewLayoutParams.a));
                                    a(childAt);
                                    int measuredWidth2 = childAt.getMeasuredWidth();
                                    int measuredHeight2 = childAt.getMeasuredHeight();
                                    int i13 = (int) (a2.x - (mapViewLayoutParams.d * measuredWidth2));
                                    int i14 = ((int) (a2.y - (mapViewLayoutParams.e * measuredHeight2))) + mapViewLayoutParams.f;
                                    childAt.layout(i13, i14, measuredWidth2 + i13, measuredHeight2 + i14);
                                }
                            }
                        } else if (acVar.a()) {
                            a(this.i);
                            Point point2 = this.k;
                            if (point2 == null) {
                                int height2 = (int) (((getHeight() - 15) * f) + this.x);
                                int width = (int) (((getWidth() - 15) * f2) + this.v);
                                int measuredWidth3 = width - this.i.getMeasuredWidth();
                                int measuredHeight3 = height2 - this.i.getMeasuredHeight();
                                if (this.r == 4) {
                                    height2 -= this.g.getMeasuredHeight();
                                    measuredHeight3 -= this.g.getMeasuredHeight();
                                }
                                this.i.layout(measuredWidth3, measuredHeight3, width, height2);
                            } else {
                                com.baidu.mapsdkplatform.comapi.map.ac acVar2 = this.i;
                                int i15 = point2.x;
                                acVar2.layout(i15, point2.y, acVar2.getMeasuredWidth() + i15, this.k.y + this.i.getMeasuredHeight());
                            }
                        }
                    }
                }
            }
        }
    }

    public final void onPause() {
        this.e.onPause();
    }

    public final void onResume() {
        this.e.onResume();
    }

    public void onSaveInstanceState(Bundle bundle) {
        BaiduMap baiduMap;
        if (bundle == null || (baiduMap = this.f) == null) {
            return;
        }
        bundle.putParcelable("mapstatus", baiduMap.getMapStatus());
        bundle.putBoolean("mZoomControlEnabled", this.s);
        bundle.putBoolean("mScaleControlEnabled", this.t);
        bundle.putInt("logoPosition", this.r);
        bundle.putInt("paddingLeft", this.v);
        bundle.putInt("paddingTop", this.x);
        bundle.putInt("paddingRight", this.w);
        bundle.putInt("paddingBottom", this.y);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        if (view == this.g) {
            return;
        }
        if (c()) {
            super.removeView(view);
        } else {
            com.baidu.platform.comapi.util.i.a(new u(this, view), 0L);
        }
    }

    public void renderMap() {
    }

    public void setCustomStyleFilePathAndMode(String str, int i) {
    }

    public final void setLogoPosition(LogoPosition logoPosition) {
        if (logoPosition == null) {
            logoPosition = LogoPosition.logoPostionleftBottom;
        }
        this.r = logoPosition.ordinal();
        requestLayout();
    }

    public void setMapCustomStyle(MapCustomStyleOptions mapCustomStyleOptions, CustomMapStyleCallBack customMapStyleCallBack) {
        if (mapCustomStyleOptions == null) {
            return;
        }
        String customMapStyleId = mapCustomStyleOptions.getCustomMapStyleId();
        if (customMapStyleId != null && !customMapStyleId.isEmpty()) {
            com.baidu.mapsdkplatform.comapi.map.e.a().a(this.p, customMapStyleId, new q(this, customMapStyleCallBack, mapCustomStyleOptions));
            return;
        }
        String localCustomStyleFilePath = mapCustomStyleOptions.getLocalCustomStyleFilePath();
        if (localCustomStyleFilePath == null || localCustomStyleFilePath.isEmpty()) {
            return;
        }
        a(localCustomStyleFilePath, "");
        setMapCustomStyleEnable(true);
    }

    public void setMapCustomStyleEnable(boolean z) {
        MapSurfaceView mapSurfaceView = this.e;
        if (mapSurfaceView == null) {
            return;
        }
        mapSurfaceView.getBaseMap().o(z);
    }

    public void setMapCustomStylePath(String str) {
        a(str, "");
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        this.v = i;
        this.x = i2;
        this.w = i3;
        this.y = i4;
    }

    public void setScaleControlPosition(Point point) {
        int i;
        if (point != null && (i = point.x) >= 0 && point.y >= 0 && i <= getWidth() && point.y <= getHeight()) {
            this.j = point;
            requestLayout();
        }
    }

    public void setUpViewEventToMapView(MotionEvent motionEvent) {
        this.e.onTouchEvent(motionEvent);
    }

    public final void setZOrderMediaOverlay(boolean z) {
        MapSurfaceView mapSurfaceView = this.e;
        if (mapSurfaceView == null) {
            return;
        }
        mapSurfaceView.setZOrderMediaOverlay(z);
    }

    public void setZoomControlsPosition(Point point) {
        int i;
        if (point != null && (i = point.x) >= 0 && point.y >= 0 && i <= getWidth() && point.y <= getHeight()) {
            this.k = point;
            requestLayout();
        }
    }

    public void showScaleControl(boolean z) {
        this.l.setVisibility(z ? 0 : 8);
        this.t = z;
    }

    public void showZoomControls(boolean z) {
        if (this.i.a()) {
            this.i.setVisibility(z ? 0 : 8);
            this.s = z;
        }
    }
}
