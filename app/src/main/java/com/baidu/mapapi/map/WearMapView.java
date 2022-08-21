package com.baidu.mapapi.map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
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
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

@TargetApi(20)
/* loaded from: classes.dex */
public class WearMapView extends ViewGroup implements View.OnApplyWindowInsetsListener {
    public static final int BT_INVIEW = 1;
    private static String c;
    private static final SparseArray<Integer> x;
    private float A;
    private int B;
    private int C;
    private int D;
    private int E;
    private int F;
    private int G;
    private boolean H;
    public ScreenShape a;
    private MapSurfaceView f;
    private BaiduMap g;
    private ImageView h;
    private Bitmap i;
    private com.baidu.mapsdkplatform.comapi.map.ac j;
    private boolean k;
    private Point l;
    private Point m;
    public AnimationTask mTask;
    public Timer mTimer;
    public a mTimerHandler;
    private RelativeLayout n;
    private SwipeDismissView o;
    private TextView p;
    private TextView q;
    private ImageView r;
    private boolean v;
    private Context w;
    private boolean y;
    private boolean z;
    private static final String b = MapView.class.getSimpleName();
    private static int d = 0;
    private static int e = 0;
    private static int s = 0;
    private static int t = 0;
    private static int u = 10;

    /* loaded from: classes.dex */
    public class AnimationTask extends TimerTask {
        public AnimationTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Message message = new Message();
            message.what = 1;
            WearMapView.this.mTimerHandler.sendMessage(message);
        }
    }

    /* loaded from: classes.dex */
    public interface OnDismissCallback {
        void onDismiss();

        void onNotify();
    }

    /* loaded from: classes.dex */
    public enum ScreenShape {
        ROUND,
        RECTANGLE,
        UNDETECTED
    }

    /* loaded from: classes.dex */
    public class a extends Handler {
        private final WeakReference<Context> b;

        public a(Context context) {
            this.b = new WeakReference<>(context);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (this.b.get() == null) {
                return;
            }
            super.handleMessage(message);
            if (message.what != 1 || WearMapView.this.j == null) {
                return;
            }
            WearMapView.this.a(true);
        }
    }

    static {
        SparseArray<Integer> sparseArray = new SparseArray<>();
        x = sparseArray;
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

    public WearMapView(Context context) {
        super(context);
        this.k = true;
        this.v = true;
        this.a = ScreenShape.ROUND;
        this.y = true;
        this.z = true;
        this.H = false;
        a(context, (BaiduMapOptions) null);
    }

    public WearMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.k = true;
        this.v = true;
        this.a = ScreenShape.ROUND;
        this.y = true;
        this.z = true;
        this.H = false;
        a(context, (BaiduMapOptions) null);
    }

    public WearMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.k = true;
        this.v = true;
        this.a = ScreenShape.ROUND;
        this.y = true;
        this.z = true;
        this.H = false;
        a(context, (BaiduMapOptions) null);
    }

    public WearMapView(Context context, BaiduMapOptions baiduMapOptions) {
        super(context);
        this.k = true;
        this.v = true;
        this.a = ScreenShape.ROUND;
        this.y = true;
        this.z = true;
        this.H = false;
        a(context, baiduMapOptions);
    }

    private int a(int i, int i2) {
        return i - ((int) Math.sqrt(Math.pow(i, 2.0d) - Math.pow(i2, 2.0d)));
    }

    private void a(int i) {
        MapSurfaceView mapSurfaceView = this.f;
        if (mapSurfaceView == null) {
            return;
        }
        if (i == 0) {
            mapSurfaceView.onPause();
            b();
        } else if (i != 1) {
        } else {
            mapSurfaceView.onResume();
            c();
        }
    }

    private static void a(Context context) {
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions) {
        Point point;
        Point point2;
        a(context);
        setOnApplyWindowInsetsListener(this);
        this.w = context;
        this.mTimerHandler = new a(context);
        this.mTimer = new Timer();
        AnimationTask animationTask = this.mTask;
        if (animationTask != null) {
            animationTask.cancel();
        }
        AnimationTask animationTask2 = new AnimationTask();
        this.mTask = animationTask2;
        this.mTimer.schedule(animationTask2, 5000L);
        com.baidu.mapsdkplatform.comapi.map.i.a();
        BMapManager.init();
        a(context, baiduMapOptions, c);
        this.f.getController().set3DGestureEnable(false);
        this.f.getController().setOverlookGestureEnable(false);
        c(context);
        d(context);
        b(context);
        if (baiduMapOptions != null && !baiduMapOptions.h) {
            this.j.setVisibility(4);
        }
        e(context);
        if (baiduMapOptions != null && !baiduMapOptions.i) {
            this.n.setVisibility(4);
        }
        if (baiduMapOptions != null && (point2 = baiduMapOptions.l) != null) {
            this.m = point2;
        }
        if (baiduMapOptions == null || (point = baiduMapOptions.k) == null) {
            return;
        }
        this.l = point;
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions, String str) {
        this.f = new MapSurfaceView(context);
        if (baiduMapOptions != null) {
            this.g = new BaiduMap(context, this.f, baiduMapOptions.a());
        } else {
            this.g = new BaiduMap(context, this.f, (com.baidu.mapsdkplatform.comapi.map.u) null);
        }
        addView(this.f);
        this.f.getBaseMap().a(new ag(this));
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

    private void a(View view, boolean z) {
        AnimatorSet animatorSet;
        if (z) {
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "TranslationY", 0.0f, -50.0f), ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f));
            animatorSet.addListener(new aj(this, view));
        } else {
            view.setVisibility(0);
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "TranslationY", -50.0f, 0.0f), ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f));
        }
        animatorSet.setDuration(1200L);
        animatorSet.start();
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
        MapSurfaceView mapSurfaceView = this.f;
        if (mapSurfaceView == null || mapSurfaceView.getBaseMap() == null) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            Log.e(b, "customStyleFilePath is empty or null, please check!");
        } else if (!str.endsWith(".sty")) {
            Log.e(b, "customStyleFile format is incorrect , please check!");
        } else if (!new File(str).exists()) {
            Log.e(b, "customStyleFile does not exist , please check!");
        } else {
            this.f.getBaseMap().b(str, "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z) {
        if (!this.k) {
            return;
        }
        a(this.j, z);
    }

    private void b() {
        if (this.f != null && !this.v) {
            d();
            this.v = true;
        }
    }

    private void b(Context context) {
        this.o = new SwipeDismissView(context, this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int) ((context.getResources().getDisplayMetrics().density * 34.0f) + 0.5f), t);
        this.o.setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.o.setLayoutParams(layoutParams);
        addView(this.o);
    }

    private void c() {
        if (this.f != null && this.v) {
            e();
            this.v = false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void c(android.content.Context r10) {
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
            r9.i = r0
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
            r9.i = r2
        L41:
            android.graphics.Bitmap r0 = r9.i
            if (r0 == 0) goto L56
            android.widget.ImageView r0 = new android.widget.ImageView
            r0.<init>(r10)
            r9.h = r0
            android.graphics.Bitmap r10 = r9.i
            r0.setImageBitmap(r10)
            android.widget.ImageView r10 = r9.h
            r9.addView(r10)
        L56:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.WearMapView.c(android.content.Context):void");
    }

    private void d() {
        MapSurfaceView mapSurfaceView = this.f;
        if (mapSurfaceView == null) {
            return;
        }
        mapSurfaceView.onBackground();
    }

    private void d(Context context) {
        com.baidu.mapsdkplatform.comapi.map.ac acVar = new com.baidu.mapsdkplatform.comapi.map.ac(context, true);
        this.j = acVar;
        if (!acVar.a()) {
            return;
        }
        this.j.b(new ah(this));
        this.j.a(new ai(this));
        addView(this.j);
    }

    private void e() {
        MapSurfaceView mapSurfaceView = this.f;
        if (mapSurfaceView == null) {
            return;
        }
        mapSurfaceView.onForeground();
    }

    private void e(Context context) {
        this.n = new RelativeLayout(context);
        this.n.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.p = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(14);
        this.p.setTextColor(Color.parseColor("#FFFFFF"));
        this.p.setTextSize(2, 11.0f);
        TextView textView = this.p;
        textView.setTypeface(textView.getTypeface(), 1);
        this.p.setLayoutParams(layoutParams);
        this.p.setId(Integer.MAX_VALUE);
        this.n.addView(this.p);
        this.q = new TextView(context);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.width = -2;
        layoutParams2.height = -2;
        layoutParams2.addRule(14);
        this.q.setTextColor(Color.parseColor("#000000"));
        this.q.setTextSize(2, 11.0f);
        this.q.setLayoutParams(layoutParams2);
        this.n.addView(this.q);
        this.r = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams3.width = -2;
        layoutParams3.height = -2;
        layoutParams3.addRule(14);
        layoutParams3.addRule(3, this.p.getId());
        this.r.setLayoutParams(layoutParams3);
        Bitmap a2 = com.baidu.mapsdkplatform.comapi.commonutils.a.a("icon_scale.9.png", context);
        byte[] ninePatchChunk = a2.getNinePatchChunk();
        NinePatch.isNinePatchChunk(ninePatchChunk);
        this.r.setBackgroundDrawable(new NinePatchDrawable(a2, ninePatchChunk, new Rect(), null));
        this.n.addView(this.r);
        addView(this.n);
    }

    @Deprecated
    public static void setCustomMapStylePath(String str) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("BDMapSDKException: customMapStylePath String is illegal");
        }
        if (!new File(str).exists()) {
            throw new RuntimeException("BDMapSDKException: please check whether the customMapStylePath file exits");
        }
        c = str;
    }

    @Deprecated
    public static void setIconCustom(int i) {
        e = i;
    }

    @Deprecated
    public static void setLoadCustomMapStyleFileMode(int i) {
        d = i;
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

    public final BaiduMap getMap() {
        BaiduMap baiduMap = this.g;
        baiduMap.c = this;
        return baiduMap;
    }

    public final int getMapLevel() {
        return x.get((int) this.f.getZoomLevel()).intValue();
    }

    public int getScaleControlViewHeight() {
        return this.F;
    }

    public int getScaleControlViewWidth() {
        return this.G;
    }

    @Override // android.view.View.OnApplyWindowInsetsListener
    public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        this.a = windowInsets.isRound() ? ScreenShape.ROUND : ScreenShape.RECTANGLE;
        return windowInsets;
    }

    public void onCreate(Context context, Bundle bundle) {
        if (bundle == null) {
            return;
        }
        MapStatus mapStatus = (MapStatus) bundle.getParcelable("mapstatus");
        if (this.l != null) {
            this.l = (Point) bundle.getParcelable("scalePosition");
        }
        if (this.m != null) {
            this.m = (Point) bundle.getParcelable("zoomPosition");
        }
        this.y = bundle.getBoolean("mZoomControlEnabled");
        this.z = bundle.getBoolean("mScaleControlEnabled");
        setPadding(bundle.getInt("paddingLeft"), bundle.getInt("paddingTop"), bundle.getInt("paddingRight"), bundle.getInt("paddingBottom"));
        a(context, new BaiduMapOptions().mapStatus(mapStatus));
    }

    public final void onDestroy() {
        if (this.w != null) {
            this.f.unInit();
        }
        Bitmap bitmap = this.i;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.i.recycle();
            this.i = null;
        }
        this.j.b();
        BMapManager.destroy();
        com.baidu.mapsdkplatform.comapi.map.i.b();
        AnimationTask animationTask = this.mTask;
        if (animationTask != null) {
            animationTask.cancel();
        }
        this.w = null;
    }

    public final void onDismiss() {
        removeAllViews();
    }

    public final void onEnterAmbient(Bundle bundle) {
        a(0);
    }

    public void onExitAmbient() {
        a(1);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                this.mTimer = new Timer();
                AnimationTask animationTask = this.mTask;
                if (animationTask != null) {
                    animationTask.cancel();
                }
                AnimationTask animationTask2 = new AnimationTask();
                this.mTask = animationTask2;
                this.mTimer.schedule(animationTask2, 5000L);
            }
        } else if (this.j.getVisibility() == 0) {
            Timer timer = this.mTimer;
            if (timer != null) {
                if (this.mTask != null) {
                    timer.cancel();
                    this.mTask.cancel();
                }
                this.mTimer = null;
                this.mTask = null;
            }
        } else if (this.j.getVisibility() == 4) {
            if (this.mTimer != null) {
                AnimationTask animationTask3 = this.mTask;
                if (animationTask3 != null) {
                    animationTask3.cancel();
                }
                this.mTimer.cancel();
                this.mTask = null;
                this.mTimer = null;
            }
            a(false);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    @TargetApi(20)
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        int i5;
        int i6;
        int i7;
        int i8;
        int childCount = getChildCount();
        a(this.h);
        float f2 = 1.0f;
        if (((getWidth() - this.B) - this.C) - this.h.getMeasuredWidth() <= 0 || ((getHeight() - this.D) - this.E) - this.h.getMeasuredHeight() <= 0) {
            this.B = 0;
            this.C = 0;
            this.E = 0;
            this.D = 0;
            f = 1.0f;
        } else {
            f2 = ((getHeight() - this.D) - this.E) / getHeight();
            f = ((getWidth() - this.B) - this.C) / getWidth();
        }
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            MapSurfaceView mapSurfaceView = this.f;
            if (childAt == mapSurfaceView) {
                mapSurfaceView.layout(0, 0, getWidth(), getHeight());
            } else if (childAt == this.h) {
                int i10 = (int) (this.E + (12.0f * f2));
                if (this.a == ScreenShape.ROUND) {
                    a(this.j);
                    int i11 = s / 2;
                    i7 = a(i11, this.j.getMeasuredWidth() / 2);
                    i8 = ((s / 2) - a(i11, i11 - i7)) + u;
                } else {
                    i7 = 0;
                    i8 = 0;
                }
                int i12 = (t - i7) - i10;
                int i13 = s - i8;
                this.h.layout(i13 - this.h.getMeasuredWidth(), i12 - this.h.getMeasuredHeight(), i13, i12);
            } else {
                com.baidu.mapsdkplatform.comapi.map.ac acVar = this.j;
                if (childAt == acVar) {
                    if (acVar.a()) {
                        a(this.j);
                        Point point = this.m;
                        if (point == null) {
                            int a2 = (int) ((12.0f * f2) + this.D + (this.a == ScreenShape.ROUND ? a(t / 2, this.j.getMeasuredWidth() / 2) : 0));
                            int measuredWidth = (s - this.j.getMeasuredWidth()) / 2;
                            this.j.layout(measuredWidth, a2, this.j.getMeasuredWidth() + measuredWidth, this.j.getMeasuredHeight() + a2);
                        } else {
                            com.baidu.mapsdkplatform.comapi.map.ac acVar2 = this.j;
                            int i14 = point.x;
                            acVar2.layout(i14, point.y, acVar2.getMeasuredWidth() + i14, this.m.y + this.j.getMeasuredHeight());
                        }
                    }
                } else if (childAt == this.n) {
                    if (this.a == ScreenShape.ROUND) {
                        a(acVar);
                        int i15 = s / 2;
                        i5 = a(i15, this.j.getMeasuredWidth() / 2);
                        i6 = ((s / 2) - a(i15, i15 - i5)) + u;
                    } else {
                        i5 = 0;
                        i6 = 0;
                    }
                    a(this.n);
                    Point point2 = this.l;
                    if (point2 == null) {
                        this.G = this.n.getMeasuredWidth();
                        this.F = this.n.getMeasuredHeight();
                        int i16 = (int) (this.B + (5.0f * f) + i6);
                        int i17 = (t - ((int) (this.E + (12.0f * f2)))) - i5;
                        this.n.layout(i16, i17 - this.n.getMeasuredHeight(), this.G + i16, i17);
                    } else {
                        RelativeLayout relativeLayout = this.n;
                        int i18 = point2.x;
                        relativeLayout.layout(i18, point2.y, relativeLayout.getMeasuredWidth() + i18, this.l.y + this.n.getMeasuredHeight());
                    }
                } else {
                    View view = this.o;
                    if (childAt == view) {
                        a(view);
                        this.o.layout(0, 0, this.o.getMeasuredWidth(), t);
                    } else {
                        ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                        if (layoutParams instanceof MapViewLayoutParams) {
                            MapViewLayoutParams mapViewLayoutParams = (MapViewLayoutParams) layoutParams;
                            Point a3 = mapViewLayoutParams.c == MapViewLayoutParams.ELayoutMode.absoluteMode ? mapViewLayoutParams.b : this.f.getBaseMap().a(CoordUtil.ll2mc(mapViewLayoutParams.a));
                            a(childAt);
                            int measuredWidth2 = childAt.getMeasuredWidth();
                            int measuredHeight = childAt.getMeasuredHeight();
                            float f3 = mapViewLayoutParams.d;
                            int i19 = (int) (a3.x - (f3 * measuredWidth2));
                            int i20 = ((int) (a3.y - (mapViewLayoutParams.e * measuredHeight))) + mapViewLayoutParams.f;
                            childAt.layout(i19, i20, measuredWidth2 + i19, measuredHeight + i20);
                        }
                    }
                }
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        BaiduMap baiduMap;
        if (bundle == null || (baiduMap = this.g) == null) {
            return;
        }
        bundle.putParcelable("mapstatus", baiduMap.getMapStatus());
        Point point = this.l;
        if (point != null) {
            bundle.putParcelable("scalePosition", point);
        }
        Point point2 = this.m;
        if (point2 != null) {
            bundle.putParcelable("zoomPosition", point2);
        }
        bundle.putBoolean("mZoomControlEnabled", this.y);
        bundle.putBoolean("mScaleControlEnabled", this.z);
        bundle.putInt("paddingLeft", this.B);
        bundle.putInt("paddingTop", this.D);
        bundle.putInt("paddingRight", this.C);
        bundle.putInt("paddingBottom", this.E);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        if (view == this.h) {
            return;
        }
        super.removeView(view);
    }

    public void setCustomStyleFilePathAndMode(String str, int i) {
    }

    public void setMapCustomStyle(MapCustomStyleOptions mapCustomStyleOptions, CustomMapStyleCallBack customMapStyleCallBack) {
        if (mapCustomStyleOptions == null) {
            return;
        }
        String customMapStyleId = mapCustomStyleOptions.getCustomMapStyleId();
        if (customMapStyleId != null && !customMapStyleId.isEmpty()) {
            com.baidu.mapsdkplatform.comapi.map.e.a().a(this.w, customMapStyleId, new af(this, customMapStyleCallBack, mapCustomStyleOptions));
            return;
        }
        String localCustomStyleFilePath = mapCustomStyleOptions.getLocalCustomStyleFilePath();
        if (localCustomStyleFilePath == null || localCustomStyleFilePath.isEmpty()) {
            return;
        }
        a(localCustomStyleFilePath, "");
    }

    public void setMapCustomStyleEnable(boolean z) {
    }

    public void setMapCustomStylePath(String str) {
        a(str, "");
    }

    public void setOnDismissCallbackListener(OnDismissCallback onDismissCallback) {
        SwipeDismissView swipeDismissView = this.o;
        if (swipeDismissView == null) {
            return;
        }
        swipeDismissView.setCallback(onDismissCallback);
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        this.B = i;
        this.D = i2;
        this.C = i3;
        this.E = i4;
    }

    public void setScaleControlPosition(Point point) {
        int i;
        if (point != null && (i = point.x) >= 0 && point.y >= 0 && i <= getWidth() && point.y <= getHeight()) {
            this.l = point;
            requestLayout();
        }
    }

    public void setShape(ScreenShape screenShape) {
        this.a = screenShape;
    }

    public void setViewAnimitionEnable(boolean z) {
        this.k = z;
    }

    public void setZoomControlsPosition(Point point) {
        int i;
        if (point != null && (i = point.x) >= 0 && point.y >= 0 && i <= getWidth() && point.y <= getHeight()) {
            this.m = point;
            requestLayout();
        }
    }

    public void showScaleControl(boolean z) {
        this.n.setVisibility(z ? 0 : 8);
        this.z = z;
    }

    public void showZoomControls(boolean z) {
        if (this.j.a()) {
            this.j.setVisibility(z ? 0 : 8);
            this.y = z;
        }
    }
}
