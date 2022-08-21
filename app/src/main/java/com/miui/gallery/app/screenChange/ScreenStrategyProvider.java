package com.miui.gallery.app.screenChange;

import android.content.Context;

/* loaded from: classes.dex */
public class ScreenStrategyProvider {
    public Context mContext;
    public HandleInstanceCase mHandleInstance;
    public LargeScreenChangeCase mLargeScreenChangeCase;
    public OrientationChangeCase mOrientationChangeCase;
    public ScreenLayoutSizeCase mScreenLayoutSizeCase;
    public ScreenStrategyContext mScreenStrategyContext;

    public ScreenStrategyProvider(Context context, ScreenStrategyContext screenStrategyContext) {
        this.mContext = context;
        this.mScreenStrategyContext = screenStrategyContext;
    }

    /* renamed from: com.miui.gallery.app.screenChange.ScreenStrategyProvider$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$app$screenChange$ScreenStrategyType;

        static {
            int[] iArr = new int[ScreenStrategyType.values().length];
            $SwitchMap$com$miui$gallery$app$screenChange$ScreenStrategyType = iArr;
            try {
                iArr[ScreenStrategyType.HANDLE_INSTANCE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$app$screenChange$ScreenStrategyType[ScreenStrategyType.LARGE_SCREEN_CHANGE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$app$screenChange$ScreenStrategyType[ScreenStrategyType.ORIENTATION_CHANGE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$app$screenChange$ScreenStrategyType[ScreenStrategyType.SCREEN_LAYOUT_SIZE_CHANGE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public WidgetCase getScreenChangeCase(ScreenStrategyType screenStrategyType) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$app$screenChange$ScreenStrategyType[screenStrategyType.ordinal()];
        if (i == 1) {
            if (this.mHandleInstance == null) {
                HandleInstanceCase handleInstanceCase = new HandleInstanceCase();
                this.mHandleInstance = handleInstanceCase;
                this.mScreenStrategyContext.addWidgetCase(handleInstanceCase);
            }
            return this.mHandleInstance;
        } else if (i == 2) {
            if (this.mLargeScreenChangeCase == null) {
                LargeScreenChangeCase largeScreenChangeCase = new LargeScreenChangeCase(this.mContext);
                this.mLargeScreenChangeCase = largeScreenChangeCase;
                this.mScreenStrategyContext.addWidgetCase(largeScreenChangeCase);
            }
            return this.mLargeScreenChangeCase;
        } else if (i == 3) {
            if (this.mOrientationChangeCase == null) {
                OrientationChangeCase orientationChangeCase = new OrientationChangeCase();
                this.mOrientationChangeCase = orientationChangeCase;
                this.mScreenStrategyContext.addWidgetCase(orientationChangeCase);
            }
            return this.mOrientationChangeCase;
        } else if (i != 4) {
            return null;
        } else {
            if (this.mScreenLayoutSizeCase == null) {
                ScreenLayoutSizeCase screenLayoutSizeCase = new ScreenLayoutSizeCase();
                this.mScreenLayoutSizeCase = screenLayoutSizeCase;
                this.mScreenStrategyContext.addWidgetCase(screenLayoutSizeCase);
            }
            return this.mScreenLayoutSizeCase;
        }
    }
}
