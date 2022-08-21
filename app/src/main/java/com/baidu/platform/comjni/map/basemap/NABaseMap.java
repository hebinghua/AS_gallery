package com.baidu.platform.comjni.map.basemap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Surface;
import com.baidu.platform.comapi.map.ae;
import com.baidu.platform.comjni.NativeComponent;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public class NABaseMap extends NativeComponent {
    private long b;
    private ThreadPoolExecutor a = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    private volatile boolean c = false;
    private final ReadWriteLock d = new ReentrantReadWriteLock(true);
    private final Set<Long> e = new CopyOnWriteArraySet();

    private void a() {
        try {
            ThreadPoolExecutor threadPoolExecutor = this.a;
            if (threadPoolExecutor == null) {
                return;
            }
            if (threadPoolExecutor.getQueue() != null) {
                this.a.getQueue().clear();
            }
            this.a.shutdown();
            this.a.awaitTermination(100L, TimeUnit.MILLISECONDS);
            this.a.shutdownNow();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(long j) {
        return this.e.contains(Long.valueOf(j)) && j != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b() {
        return !this.a.isShutdown() && !this.a.isTerminated();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeAddItemData(long j, Bundle bundle, boolean z);

    private native long nativeAddLayer(long j, int i, int i2, String str);

    private native void nativeAddPopupData(long j, Bundle bundle);

    private native void nativeAddRtPopData(long j, Bundle bundle);

    private native void nativeAddStreetCustomMarker(long j, Bundle bundle, Bitmap bitmap);

    private native void nativeAttachDC(long j, long j2);

    private native boolean nativeBeginLocationLayerAnimation(long j);

    private native boolean nativeCleanCache(long j, int i);

    private native void nativeClearHeatMapLayerCache(long j, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeClearLayer(long j, long j2);

    private native void nativeClearLocationLayerData(long j, Bundle bundle);

    private native void nativeClearMistmapLayer(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeClearSDKLayer(long j, long j2);

    private native void nativeClearUniversalLayer(long j);

    private native boolean nativeCloseCache(long j);

    private native void nativeCloseParticleEffect(long j, String str);

    private native long nativeCreate();

    private native long nativeCreateDuplicate(long j);

    private native int nativeDraw(long j);

    private native void nativeEnablePOIAnimation(long j, boolean z);

    private native void nativeEntrySearchTopic(long j, int i, String str, String str2);

    private native void nativeExitSearchTopic(long j);

    private native void nativeFocusTrafficUGCLabel(long j);

    private native String nativeGeoPtToScrPoint(long j, int i, int i2);

    private native float nativeGetAdapterZoomUnitsEx(long j);

    private native int nativeGetCacheSize(long j, int i);

    private native String nativeGetCityInfoByID(long j, int i);

    private static native boolean nativeGetDEMEnable(long j);

    private static native boolean nativeGetDrawHouseHeightEnable(long j);

    private native Bundle nativeGetDrawingMapStatus(long j);

    private native float nativeGetFZoomToBoundF(long j, Bundle bundle, Bundle bundle2);

    private native String nativeGetFocusedBaseIndoorMapInfo(long j);

    private native int nativeGetFontSizeLevel(long j);

    private static native long nativeGetLayerIDByTag(long j, String str);

    private native int nativeGetLayerPos(long j, long j2);

    private native boolean nativeGetMapBarData(long j, Bundle bundle);

    private native int nativeGetMapLanguage(long j);

    private native int nativeGetMapRenderType(long j);

    private native int nativeGetMapScene(long j);

    private native Bundle nativeGetMapStatus(long j, boolean z);

    private static native Bundle nativeGetMapStatusLimits(long j);

    private native boolean nativeGetMapStatusLimitsLevel(long j, int[] iArr);

    private native int nativeGetMapTheme(long j);

    private native String nativeGetNearlyObjID(long j, long j2, int i, int i2, int i3);

    private static native void nativeGetProjectionMatrix(long j, float[] fArr);

    private native String nativeGetProjectionPt(long j, String str);

    private native int nativeGetScaleLevel(long j, int i, int i2);

    private native int nativeGetVMPMapCityInfo(long j, Bundle bundle);

    private static native void nativeGetViewMatrix(long j, float[] fArr);

    private native float nativeGetZoomToBound(long j, Bundle bundle, int i, int i2);

    private native float nativeGetZoomToBoundF(long j, Bundle bundle);

    private native boolean nativeImportMapTheme(long j, int i);

    private native boolean nativeInit(long j, String str, String str2, String str3, String str4, String str5, String str6, String str7, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z, boolean z2);

    private native boolean nativeInitCustomStyle(long j, String str, String str2);

    private native int nativeInitLayerCallback(long j);

    private native boolean nativeInitWithBundle(long j, Bundle bundle, boolean z);

    private native long nativeInsertLayerAt(long j, int i, int i2, int i3, String str);

    private native boolean nativeIsAnimationRunning(long j);

    private native boolean nativeIsBaseIndoorMapMode(long j);

    private native boolean nativeIsNaviMode(long j);

    private native boolean nativeIsPointInFocusBarBorder(long j, double d, double d2, double d3);

    private native boolean nativeIsPointInFocusIDRBorder(long j, double d, double d2);

    private native boolean nativeIsStreetArrowShown(long j);

    private native boolean nativeIsStreetCustomMarkerShown(long j);

    private native boolean nativeIsStreetPOIMarkerShown(long j);

    private native boolean nativeIsStreetRoadClickable(long j);

    private native boolean nativeLayersIsShow(long j, long j2);

    private native boolean nativeMoveLayerBelowTo(long j, long j2, int i);

    private native void nativeMoveToScrPoint(long j, int i, int i2);

    private native void nativeNewSetMapStatus(long j, Bundle bundle);

    private native void nativeOnBackground(long j);

    private native void nativeOnForeground(long j);

    private native String nativeOnHotcityGet(long j);

    private native void nativeOnPause(long j);

    private native boolean nativeOnRecordAdd(long j, int i);

    private native String nativeOnRecordGetAll(long j);

    private native String nativeOnRecordGetAt(long j, int i);

    private native boolean nativeOnRecordImport(long j, boolean z, boolean z2);

    private native boolean nativeOnRecordReload(long j, int i, boolean z);

    private native boolean nativeOnRecordRemove(long j, int i, boolean z);

    private native boolean nativeOnRecordStart(long j, int i, boolean z, int i2);

    private native boolean nativeOnRecordSuspend(long j, int i, boolean z, int i2);

    private native void nativeOnResume(long j);

    private native String nativeOnSchcityGet(long j, String str);

    private native boolean nativeOnUsrcityMsgInterval(long j, int i);

    private native int nativeOnWifiRecordAdd(long j, int i);

    private native boolean nativePerformAction(long j, String str);

    private native int nativeQueryInterface(long j);

    private native void nativeRecycleMemory(long j, int i);

    private native int nativeRelease(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public native boolean nativeRemoveItemData(long j, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    public native int nativeRemoveLayer(long j, long j2);

    private native void nativeRemoveStreetAllCustomMarker(long j);

    private native void nativeRemoveStreetCustomMaker(long j, String str);

    private static native void nativeRenderClearShaderCache(String str);

    private static native void nativeRenderInit(long j, int i, int i2, Surface surface, int i3);

    private native void nativeRenderResize(long j, int i, int i2);

    private native void nativeResetImageRes(long j);

    private native boolean nativeResumeCache(long j);

    private native boolean nativeSaveCache(long j);

    private native void nativeSaveScreenToLocal(long j, String str, String str2);

    private native String nativeScrPtToGeoPoint(long j, int i, int i2);

    private native void nativeSetAllStreetCustomMarkerVisibility(long j, boolean z);

    private native void nativeSetCustomStyleEnable(long j, boolean z);

    private static native void nativeSetDEMEnable(long j, boolean z);

    private native void nativeSetDpiScale(long j, float f);

    private static native void nativeSetDrawHouseHeightEnable(long j, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public native String nativeSetFocus(long j, long j2, long j3, boolean z, Bundle bundle);

    private native void nativeSetFontSizeLevel(long j, int i);

    private native boolean nativeSetItsPreTime(long j, int i, int i2, int i3);

    private native boolean nativeSetLayerSceneMode(long j, long j2, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeSetLayersClickable(long j, long j2, boolean z);

    private native void nativeSetLocationLayerData(long j, Bundle bundle);

    private native int nativeSetMapControlMode(long j, int i);

    private native void nativeSetMapLanguage(long j, int i);

    private native boolean nativeSetMapScene(long j, int i);

    private native void nativeSetMapStatus(long j, Bundle bundle);

    private static native void nativeSetMapStatusLimits(long j, Bundle bundle);

    private native boolean nativeSetMapStatusLimitsLevel(long j, int i, int i2);

    private native boolean nativeSetMapTheme(long j, int i, Bundle bundle);

    private native boolean nativeSetMapThemeScene(long j, int i, int i2, Bundle bundle);

    private static native void nativeSetMaxAndMinZoomLevel(long j, Bundle bundle);

    private native void nativeSetRecommendPOIScene(long j, int i);

    private native void nativeSetStreetArrowShow(long j, boolean z);

    private native void nativeSetStreetMarkerClickable(long j, String str, boolean z);

    private native void nativeSetStreetRoadClickable(long j, boolean z);

    private native void nativeSetStyleMode(long j, int i);

    private native void nativeSetTargetStreetCustomMarkerVisibility(long j, boolean z, String str);

    private native boolean nativeSetTestSwitch(long j, boolean z);

    private native void nativeSetTrafficUGCData(long j, String str);

    private native void nativeSetUniversalFilter(long j, String str);

    private native void nativeShowBaseIndoorMap(long j, boolean z);

    private native void nativeShowFootMarkGrid(long j, boolean z, String str);

    private native void nativeShowHotMap(long j, boolean z, int i);

    private native void nativeShowHotMapWithUid(long j, boolean z, int i, String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeShowLayers(long j, long j2, boolean z);

    private native void nativeShowMistMap(long j, boolean z, String str);

    private native boolean nativeShowParticleEffect(long j, int i);

    private native boolean nativeShowParticleEffectByName(long j, String str, boolean z);

    private native boolean nativeShowParticleEffectByType(long j, int i);

    private native void nativeShowSatelliteMap(long j, boolean z);

    private native void nativeShowStreetPOIMarker(long j, boolean z);

    private native void nativeShowStreetRoadMap(long j, boolean z);

    private native void nativeShowTrafficMap(long j, boolean z);

    private native void nativeShowTrafficUGCMap(long j, boolean z);

    private native void nativeShowUniversalLayer(long j, Bundle bundle);

    private native void nativeStartIndoorAnimation(long j);

    private native void nativeSurfaceDestroyed(long j, Surface surface);

    private native boolean nativeSwitchBaseIndoorMapFloor(long j, String str, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public native boolean nativeSwitchLayer(long j, long j2, long j3);

    private native void nativeUnFocusTrafficUGCLabel(long j);

    private native void nativeUpdateBaseLayers(long j);

    private native void nativeUpdateDrawFPS(long j);

    private native void nativeUpdateFootMarkGrid(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeUpdateLayers(long j, long j2);

    private native String nativeworldPointToScreenPoint(long j, float f, float f2, float f3);

    public static void renderClearShaderCache(String str) {
        nativeRenderClearShaderCache(str);
    }

    public void addItemData(Bundle bundle, boolean z) {
        if (!b()) {
            return;
        }
        this.a.submit(new p(this, bundle, z));
    }

    public long addLayer(int i, int i2, String str) {
        long nativeAddLayer = nativeAddLayer(this.b, i, i2, str);
        this.e.remove(Long.valueOf(nativeAddLayer));
        return nativeAddLayer;
    }

    public void addOneOverlayItem(Bundle bundle) {
        if (!b()) {
            return;
        }
        this.a.submit(new d(this, bundle));
    }

    public void addOverlayItems(Bundle[] bundleArr, int i) {
        if (!b()) {
            return;
        }
        this.a.submit(new e(this, bundleArr, i));
    }

    public void addPopupData(Bundle bundle) {
        nativeAddPopupData(this.b, bundle);
    }

    public void addRtPopData(Bundle bundle) {
        nativeAddRtPopData(this.b, bundle);
    }

    public void addStreetCustomMarker(Bundle bundle, Bitmap bitmap) {
        long j = this.b;
        if (j != 0) {
            nativeAddStreetCustomMarker(j, bundle, bitmap);
        }
    }

    public void beginLocationLayerAnimation() {
        nativeBeginLocationLayerAnimation(this.b);
    }

    public boolean cleanCache(int i) {
        return nativeCleanCache(this.b, i);
    }

    public void clearHeatMapLayerCache(long j) {
        long j2 = this.b;
        if (j2 != 0) {
            nativeClearHeatMapLayerCache(j2, j);
        }
    }

    public void clearLayer(long j) {
        if (!b()) {
            return;
        }
        this.a.submit(new n(this, j));
    }

    public void clearLocationLayerData(Bundle bundle) {
        nativeClearLocationLayerData(this.b, bundle);
    }

    public void clearMistmapLayer() {
        nativeClearMistmapLayer(this.b);
    }

    public void clearSDKLayer(long j) {
        if (!b()) {
            return;
        }
        this.a.submit(new m(this, j));
    }

    public void clearUniversalLayer() {
        nativeClearUniversalLayer(this.b);
    }

    public boolean closeCache() {
        return nativeCloseCache(this.b);
    }

    public void closeParticleEffect(String str) {
        nativeCloseParticleEffect(this.b, str);
    }

    @Override // com.baidu.platform.comjni.NativeComponent
    public long create() {
        long nativeCreate = nativeCreate();
        this.b = nativeCreate;
        nativeInitLayerCallback(nativeCreate);
        return this.b;
    }

    public long createByDuplicate(long j) {
        long nativeCreateDuplicate = nativeCreateDuplicate(j);
        this.b = nativeCreateDuplicate;
        if (nativeCreateDuplicate != 0) {
            nativeInitLayerCallback(nativeCreateDuplicate);
        }
        return this.b;
    }

    public long createDuplicate() {
        return nativeCreateDuplicate(this.b);
    }

    @Override // com.baidu.platform.comjni.NativeComponent
    public int dispose() {
        if (this.b != 0) {
            this.c = true;
            a();
            int nativeRelease = nativeRelease(this.b);
            this.b = 0L;
            return nativeRelease;
        }
        return 0;
    }

    public int draw() {
        long j = this.b;
        if (j != 0) {
            return nativeDraw(j);
        }
        return 0;
    }

    public void enablePOIAnimation(boolean z) {
        long j = this.b;
        if (j != 0) {
            nativeEnablePOIAnimation(j, z);
        }
    }

    public void entrySearchTopic(int i, String str, String str2) {
        long j = this.b;
        if (j != 0) {
            nativeEntrySearchTopic(j, i, str, str2);
        }
    }

    public void exitSearchTopic() {
        long j = this.b;
        if (j != 0) {
            nativeExitSearchTopic(j);
        }
    }

    public void focusTrafficUGCLabel() {
        nativeFocusTrafficUGCLabel(this.b);
    }

    public String geoPtToScrPoint(int i, int i2) {
        return nativeGeoPtToScrPoint(this.b, i, i2);
    }

    public float getAdapterZoomUnitsEx() {
        return nativeGetAdapterZoomUnitsEx(this.b);
    }

    public int getCacheSize(int i) {
        return nativeGetCacheSize(this.b, i);
    }

    public String getCityInfoByID(int i) {
        return nativeGetCityInfoByID(this.b, i);
    }

    public boolean getDEMEnable() {
        long j = this.b;
        if (j != 0) {
            nativeGetDEMEnable(j);
            return false;
        }
        return false;
    }

    public boolean getDrawHouseHeightEnable() {
        long j = this.b;
        if (j != 0) {
            return nativeGetDrawHouseHeightEnable(j);
        }
        return false;
    }

    public Bundle getDrawingMapStatus() {
        return nativeGetDrawingMapStatus(this.b);
    }

    public float getFZoomToBoundF(Bundle bundle, Bundle bundle2) {
        return nativeGetFZoomToBoundF(this.b, bundle, bundle2);
    }

    public String getFocusedBaseIndoorMapInfo() {
        long j = this.b;
        if (j != 0) {
            return nativeGetFocusedBaseIndoorMapInfo(j);
        }
        return null;
    }

    public int getFontSizeLevel() {
        long j = this.b;
        if (j != 0) {
            return nativeGetFontSizeLevel(j);
        }
        return 1;
    }

    public long getLayerIDByTag(String str) {
        long j = this.b;
        if (j != 0) {
            return nativeGetLayerIDByTag(j, str);
        }
        return 0L;
    }

    public boolean getMapBarData(Bundle bundle) {
        return nativeGetMapBarData(this.b, bundle);
    }

    public int getMapLanguage() {
        long j = this.b;
        if (j != 0) {
            return nativeGetMapLanguage(j);
        }
        return 0;
    }

    public int getMapRenderType() {
        return nativeGetMapRenderType(this.b);
    }

    public int getMapScene() {
        return nativeGetMapScene(this.b);
    }

    public Bundle getMapStatus(boolean z) {
        return nativeGetMapStatus(this.b, z);
    }

    public Bundle getMapStatusLimits() {
        long j = this.b;
        if (j != 0) {
            return nativeGetMapStatusLimits(j);
        }
        return null;
    }

    public boolean getMapStatusLimitsLevel(int[] iArr) {
        long j = this.b;
        if (j != 0) {
            return nativeGetMapStatusLimitsLevel(j, iArr);
        }
        return false;
    }

    public int getMapTheme() {
        return nativeGetMapTheme(this.b);
    }

    public long getNativeMapPointer() {
        return this.b;
    }

    public String getNearlyObjID(long j, int i, int i2, int i3) {
        boolean z = false;
        try {
            z = this.d.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (!z) {
                if (z) {
                    this.d.readLock().unlock();
                }
                return "";
            } else if (a(j)) {
                if (z) {
                    this.d.readLock().unlock();
                }
                return "";
            } else {
                String nativeGetNearlyObjID = nativeGetNearlyObjID(this.b, j, i, i2, i3);
                if (z) {
                    this.d.readLock().unlock();
                }
                return nativeGetNearlyObjID;
            }
        } catch (Exception unused) {
            if (z) {
                this.d.readLock().unlock();
            }
            return "";
        } catch (Throwable th) {
            if (z) {
                this.d.readLock().unlock();
            }
            throw th;
        }
    }

    public void getProjectMatrix(float[] fArr) {
        long j = this.b;
        if (j != 0) {
            nativeGetProjectionMatrix(j, fArr);
        }
    }

    public String getProjectionPt(String str) {
        return nativeGetProjectionPt(this.b, str);
    }

    public int getScaleLevel(int i, int i2) {
        long j = this.b;
        if (j != 0) {
            return nativeGetScaleLevel(j, i, i2);
        }
        return -1;
    }

    public int getVMPMapCityInfo(Bundle bundle) {
        return nativeGetVMPMapCityInfo(this.b, bundle);
    }

    public void getViewMatrix(float[] fArr) {
        long j = this.b;
        if (j != 0) {
            nativeGetViewMatrix(j, fArr);
        }
    }

    public float getZoomToBound(long j, Bundle bundle, int i, int i2) {
        return nativeGetZoomToBound(j, bundle, i, i2);
    }

    public float getZoomToBound(Bundle bundle, int i, int i2) {
        return nativeGetZoomToBound(this.b, bundle, i, i2);
    }

    public float getZoomToBoundF(Bundle bundle) {
        return nativeGetZoomToBoundF(this.b, bundle);
    }

    public boolean importMapTheme(int i) {
        return nativeImportMapTheme(this.b, i);
    }

    public boolean init(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z, boolean z2) {
        long j = this.b;
        return j != 0 && nativeInit(j, str, str2, str3, str4, str5, str6, str7, i, i2, i3, i4, i5, i6, i7, z, z2);
    }

    public boolean initCustomStyle(String str, String str2) {
        long j = this.b;
        if (j != 0) {
            return nativeInitCustomStyle(j, str, str2);
        }
        return false;
    }

    public boolean initWithOptions(Bundle bundle, boolean z) {
        long j = this.b;
        return j != 0 && nativeInitWithBundle(j, bundle, z);
    }

    public boolean isAnimationRunning() {
        return nativeIsAnimationRunning(this.b);
    }

    public boolean isBaseIndoorMapMode() {
        long j = this.b;
        return j != 0 && nativeIsBaseIndoorMapMode(j);
    }

    public boolean isNaviMode() {
        return nativeIsNaviMode(this.b);
    }

    public boolean isPointInFocusBarBorder(double d, double d2, double d3) {
        long j = this.b;
        return j != 0 && nativeIsPointInFocusBarBorder(j, d, d2, d3);
    }

    public boolean isPointInFocusIDRBorder(double d, double d2) {
        long j = this.b;
        return j != 0 && nativeIsPointInFocusIDRBorder(j, d, d2);
    }

    public boolean isStreetArrowShown() {
        return nativeIsStreetArrowShown(this.b);
    }

    public boolean isStreetCustomMarkerShown() {
        return nativeIsStreetCustomMarkerShown(this.b);
    }

    public boolean isStreetPOIMarkerShown() {
        long j = this.b;
        return j != 0 && nativeIsStreetPOIMarkerShown(j);
    }

    public boolean isStreetRoadClickable() {
        return nativeIsStreetRoadClickable(this.b);
    }

    public boolean layersIsShow(long j) {
        boolean z;
        boolean z2 = false;
        try {
            z = this.d.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (!z) {
                if (z) {
                    this.d.readLock().unlock();
                }
                return false;
            }
            try {
                if (a(j)) {
                    if (z) {
                        this.d.readLock().unlock();
                    }
                    return false;
                }
                boolean nativeLayersIsShow = nativeLayersIsShow(this.b, j);
                if (z) {
                    this.d.readLock().unlock();
                }
                return nativeLayersIsShow;
            } catch (Exception unused) {
                if (z) {
                    this.d.readLock().unlock();
                }
                return false;
            } catch (Throwable th) {
                th = th;
                z2 = z;
                if (z2) {
                    this.d.readLock().unlock();
                }
                throw th;
            }
        } catch (Exception unused2) {
            z = false;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public boolean moveLayerBelowTo(long j, int i) {
        long j2 = this.b;
        if (j2 != 0) {
            return nativeMoveLayerBelowTo(j2, j, i);
        }
        return false;
    }

    public void moveToScrPoint(int i, int i2) {
        nativeMoveToScrPoint(this.b, i, i2);
    }

    public native void nativeAddOneOverlayItem(long j, Bundle bundle);

    public native void nativeAddOverlayItems(long j, Bundle[] bundleArr, int i);

    public native boolean nativeAddTileOverlay(long j, Bundle bundle);

    public native boolean nativeCleanSDKTileDataCache(long j, long j2);

    public native void nativeRemoveOneOverlayItem(long j, Bundle bundle);

    public native void nativeUpdateOneOverlayItem(long j, Bundle bundle);

    public native boolean nativeUpdateSDKTile(long j, Bundle bundle);

    public void onBackground() {
        long j = this.b;
        if (j != 0) {
            nativeOnBackground(j);
        }
    }

    public void onForeground() {
        long j = this.b;
        if (j != 0) {
            nativeOnForeground(j);
        }
    }

    public String onHotcityGet() {
        return nativeOnHotcityGet(this.b);
    }

    public void onPause() {
        long j = this.b;
        if (j != 0) {
            nativeOnPause(j);
        }
    }

    public boolean onRecordAdd(int i) {
        return nativeOnRecordAdd(this.b, i);
    }

    public String onRecordGetAll() {
        return nativeOnRecordGetAll(this.b);
    }

    public String onRecordGetAt(int i) {
        return nativeOnRecordGetAt(this.b, i);
    }

    public boolean onRecordImport(boolean z, boolean z2) {
        return nativeOnRecordImport(this.b, z, z2);
    }

    public boolean onRecordReload(int i, boolean z) {
        return nativeOnRecordReload(this.b, i, z);
    }

    public boolean onRecordRemove(int i, boolean z) {
        return nativeOnRecordRemove(this.b, i, z);
    }

    public boolean onRecordStart(int i, boolean z, int i2) {
        return nativeOnRecordStart(this.b, i, z, i2);
    }

    public boolean onRecordSuspend(int i, boolean z, int i2) {
        return nativeOnRecordSuspend(this.b, i, z, i2);
    }

    public void onResume() {
        long j = this.b;
        if (j != 0) {
            nativeOnResume(j);
        }
    }

    public String onSchcityGet(String str) {
        return nativeOnSchcityGet(this.b, str);
    }

    public boolean onUsrcityMsgInterval(int i) {
        return nativeOnUsrcityMsgInterval(this.b, i);
    }

    public int onWifiRecordAdd(int i) {
        return nativeOnWifiRecordAdd(this.b, i);
    }

    public boolean performAction(String str) {
        return nativePerformAction(this.b, str);
    }

    public void recycleMemory(int i) {
        long j = this.b;
        if (j != 0) {
            nativeRecycleMemory(j, i);
        }
    }

    public boolean removeItemData(Bundle bundle) {
        if (!b()) {
            return false;
        }
        this.a.submit(new c(this, bundle));
        return true;
    }

    public void removeLayer(long j) {
        if (!b()) {
            return;
        }
        this.a.submit(new k(this, j));
    }

    public void removeOneOverlayItem(Bundle bundle) {
        if (!b()) {
            return;
        }
        this.a.submit(new g(this, bundle));
    }

    public void removeOneOverlayItems(Bundle[] bundleArr) {
        if (bundleArr == null || !b()) {
            return;
        }
        this.a.submit(new h(this, bundleArr));
    }

    public void removeStreetAllCustomMarker() {
        nativeRemoveStreetAllCustomMarker(this.b);
    }

    public void removeStreetCustomMaker(String str) {
        nativeRemoveStreetCustomMaker(this.b, str);
    }

    @Deprecated
    public void renderDone() {
    }

    public void renderInit(int i, int i2, Surface surface, int i3) {
        long j = this.b;
        if (j != 0) {
            nativeRenderInit(j, i, i2, surface, i3);
        }
    }

    @Deprecated
    public int renderRender() {
        long j = this.b;
        if (j != 0) {
            return nativeDraw(j);
        }
        return 0;
    }

    public void renderResize(int i, int i2) {
        long j = this.b;
        if (j != 0) {
            nativeRenderResize(j, i, i2);
        }
    }

    public void resetImageRes() {
        long j = this.b;
        if (j != 0) {
            nativeResetImageRes(j);
        }
    }

    public boolean resumeCache() {
        return nativeResumeCache(this.b);
    }

    public boolean saveCache() {
        try {
            return nativeSaveCache(this.b);
        } catch (Throwable unused) {
            return false;
        }
    }

    public void saveScreenToLocal(String str, String str2) {
        nativeSaveScreenToLocal(this.b, str, str2);
    }

    public String scrPtToGeoPoint(int i, int i2) {
        return nativeScrPtToGeoPoint(this.b, i, i2);
    }

    public void setAllStreetCustomMarkerVisibility(boolean z) {
        long j = this.b;
        if (j != 0) {
            nativeSetAllStreetCustomMarkerVisibility(j, z);
        }
    }

    public void setCallback(ae aeVar) {
        BaseMapCallback.setMapCallback(this.b, aeVar);
    }

    public void setCustomStyleEnable(boolean z) {
        long j = this.b;
        if (j != 0) {
            nativeSetCustomStyleEnable(j, z);
        }
    }

    public void setDEMEnable(boolean z) {
        long j = this.b;
        if (j != 0) {
            nativeSetDEMEnable(j, z);
        }
    }

    public void setDrawHouseHeightEnable(boolean z) {
        long j = this.b;
        if (j != 0) {
            nativeSetDrawHouseHeightEnable(j, z);
        }
    }

    public void setFocus(long j, long j2, boolean z, Bundle bundle) {
        if (!b()) {
            return;
        }
        this.a.submit(new o(this, j, j2, z, bundle));
    }

    public void setFontSizeLevel(int i) {
        long j = this.b;
        if (j != 0) {
            nativeSetFontSizeLevel(j, i);
        }
    }

    public boolean setItsPreTime(int i, int i2, int i3) {
        return nativeSetItsPreTime(this.b, i, i2, i3);
    }

    public boolean setLayerSceneMode(long j, int i) {
        return nativeSetLayerSceneMode(this.b, j, i);
    }

    public void setLayersClickable(long j, boolean z) {
        if (!b()) {
            return;
        }
        this.a.submit(new i(this, j, z));
    }

    public void setLocationLayerData(Bundle bundle) {
        nativeSetLocationLayerData(this.b, bundle);
    }

    public int setMapControlMode(int i) {
        return nativeSetMapControlMode(this.b, i);
    }

    public void setMapLanguage(int i) {
        long j = this.b;
        if (j != 0) {
            nativeSetMapLanguage(j, i);
        }
    }

    public void setMapScene(int i) {
        nativeSetMapScene(this.b, i);
    }

    public void setMapStatus(Bundle bundle) {
        nativeSetMapStatus(this.b, bundle);
    }

    public void setMapStatusLimits(Bundle bundle) {
        long j = this.b;
        if (j != 0) {
            nativeSetMapStatusLimits(j, bundle);
        }
    }

    public boolean setMapStatusLimitsLevel(int i, int i2) {
        long j = this.b;
        if (j != 0) {
            return nativeSetMapStatusLimitsLevel(j, i, i2);
        }
        return false;
    }

    public boolean setMapTheme(int i, Bundle bundle) {
        return nativeSetMapTheme(this.b, i, bundle);
    }

    public boolean setMapThemeScene(int i, int i2, Bundle bundle) {
        return nativeSetMapThemeScene(this.b, i, i2, bundle);
    }

    public void setMaxAndMinZoomLevel(Bundle bundle) {
        long j = this.b;
        if (j != 0) {
            nativeSetMaxAndMinZoomLevel(j, bundle);
        }
    }

    public void setNewMapStatus(Bundle bundle) {
        nativeNewSetMapStatus(this.b, bundle);
    }

    public void setRecommendPOIScene(int i) {
        nativeSetRecommendPOIScene(this.b, i);
    }

    public void setStreetArrowShow(boolean z) {
        nativeSetStreetArrowShow(this.b, z);
    }

    public void setStreetMarkerClickable(String str, boolean z) {
        nativeSetStreetMarkerClickable(this.b, str, z);
    }

    public void setStreetRoadClickable(boolean z) {
        nativeSetStreetRoadClickable(this.b, z);
    }

    public void setStyleMode(int i) {
        nativeSetStyleMode(this.b, i);
    }

    public void setTargetStreetCustomMarkerVisibility(boolean z, String str) {
        long j = this.b;
        if (j != 0) {
            nativeSetTargetStreetCustomMarkerVisibility(j, z, str);
        }
    }

    public boolean setTestSwitch(boolean z) {
        return nativeSetTestSwitch(this.b, z);
    }

    public void setTrafficUGCData(String str) {
        nativeSetTrafficUGCData(this.b, str);
    }

    public void setUniversalFilter(String str) {
        nativeSetUniversalFilter(this.b, str);
    }

    public void showBaseIndoorMap(boolean z) {
        nativeShowBaseIndoorMap(this.b, z);
    }

    public void showFootMarkGrid(boolean z, String str) {
        long j = this.b;
        if (j != 0) {
            nativeShowFootMarkGrid(j, z, str);
        }
    }

    public void showHotMap(boolean z, int i) {
        nativeShowHotMap(this.b, z, i);
    }

    public void showHotMap(boolean z, int i, String str) {
        nativeShowHotMapWithUid(this.b, z, i, str);
    }

    public void showLayers(long j, boolean z) {
        if (!b()) {
            return;
        }
        this.a.submit(new b(this, j, z));
    }

    public void showMistMap(boolean z, String str) {
        nativeShowMistMap(this.b, z, str);
    }

    public boolean showParticleEffect(int i) {
        return nativeShowParticleEffect(this.b, i);
    }

    public boolean showParticleEffectByName(String str, boolean z) {
        return nativeShowParticleEffectByName(this.b, str, z);
    }

    public boolean showParticleEffectByType(int i) {
        return nativeShowParticleEffectByType(this.b, i);
    }

    public void showSatelliteMap(boolean z) {
        nativeShowSatelliteMap(this.b, z);
    }

    public void showStreetPOIMarker(boolean z) {
        long j = this.b;
        if (j != 0) {
            nativeShowStreetPOIMarker(j, z);
        }
    }

    public void showStreetRoadMap(boolean z) {
        nativeShowStreetRoadMap(this.b, z);
    }

    public void showTrafficMap(boolean z) {
        nativeShowTrafficMap(this.b, z);
    }

    public void showTrafficUGCMap(boolean z) {
        nativeShowTrafficUGCMap(this.b, z);
    }

    public void showUniversalLayer(Bundle bundle) {
        nativeShowUniversalLayer(this.b, bundle);
    }

    public void startIndoorAnimation() {
        nativeStartIndoorAnimation(this.b);
    }

    public void surfaceDestroyed(Surface surface) {
        long j = this.b;
        if (j != 0) {
            nativeSurfaceDestroyed(j, surface);
        }
    }

    public boolean switchBaseIndoorMapFloor(String str, String str2) {
        return nativeSwitchBaseIndoorMapFloor(this.b, str, str2);
    }

    public boolean switchLayer(long j, long j2) {
        if (!b()) {
            return false;
        }
        this.a.submit(new l(this, j, j2));
        return true;
    }

    public void unFocusTrafficUGCLabel() {
        nativeUnFocusTrafficUGCLabel(this.b);
    }

    public void updateBaseLayers() {
        nativeUpdateBaseLayers(this.b);
    }

    public void updateDrawFPS() {
        long j = this.b;
        if (j != 0) {
            nativeUpdateDrawFPS(j);
        }
    }

    public void updateFootMarkGrid() {
        nativeUpdateFootMarkGrid(this.b);
    }

    public void updateLayers(long j) {
        if (!b()) {
            return;
        }
        this.a.submit(new j(this, j));
    }

    public void updateOneOverlayItem(Bundle bundle) {
        if (!b()) {
            return;
        }
        this.a.submit(new f(this, bundle));
    }

    public String worldPointToScreenPoint(float f, float f2, float f3) {
        return nativeworldPointToScreenPoint(this.b, f, f2, f3);
    }
}
