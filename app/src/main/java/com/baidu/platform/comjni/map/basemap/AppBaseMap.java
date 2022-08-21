package com.baidu.platform.comjni.map.basemap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Surface;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public class AppBaseMap {
    private NABaseMap b;
    private long a = 0;
    private final ReadWriteLock c = new ReentrantReadWriteLock(true);

    public AppBaseMap() {
        this.b = null;
        this.b = new NABaseMap();
    }

    public static void renderClearShaderCache(String str) {
        NABaseMap.renderClearShaderCache(str);
    }

    public void AddItemData(Bundle bundle) {
        AddItemData(bundle, false);
    }

    public void AddItemData(Bundle bundle, boolean z) {
        this.b.addItemData(bundle, z);
    }

    public long AddLayer(int i, int i2, String str) {
        return this.b.addLayer(i, i2, str);
    }

    public void AddPopupData(Bundle bundle) {
        this.b.addPopupData(bundle);
    }

    public void AddRtPopData(Bundle bundle) {
        this.b.addRtPopData(bundle);
    }

    public void AddStreetCustomMarker(Bundle bundle, Bitmap bitmap) {
        if (this.a != 0) {
            this.b.addStreetCustomMarker(bundle, bitmap);
        }
    }

    public void BeginLocationLayerAnimation() {
        this.b.beginLocationLayerAnimation();
    }

    public boolean CleanCache(int i) {
        return this.b.cleanCache(i);
    }

    public void ClearLayer(long j) {
        this.b.clearLayer(j);
    }

    public void ClearLocationLayerData(Bundle bundle) {
        this.b.clearLocationLayerData(bundle);
    }

    public void ClearMistmapLayer() {
        this.b.clearMistmapLayer();
    }

    public void ClearSDKLayer(long j) {
        this.b.clearSDKLayer(j);
    }

    public boolean CloseCache() {
        return this.b.closeCache();
    }

    public boolean Create() {
        try {
            this.c.writeLock().lock();
            this.a = this.b.create();
            return true;
        } finally {
            this.c.writeLock().unlock();
        }
    }

    public boolean CreateByDuplicate(long j) {
        long createByDuplicate = this.b.createByDuplicate(j);
        this.a = createByDuplicate;
        return createByDuplicate != 0;
    }

    public long CreateDuplicate() {
        return this.b.createDuplicate();
    }

    public int Draw() {
        if (this.a != 0) {
            return this.b.draw();
        }
        return 0;
    }

    public String GeoPtToScrPoint(int i, int i2) {
        return this.b.geoPtToScrPoint(i, i2);
    }

    public float GetAdapterZoomUnitsEx() {
        return this.b.getAdapterZoomUnitsEx();
    }

    public int GetCacheSize(int i) {
        return this.b.getCacheSize(i);
    }

    public String GetCityInfoByID(int i) {
        return this.b.getCityInfoByID(i);
    }

    public Bundle GetDrawingMapStatus() {
        return this.b.getDrawingMapStatus();
    }

    public float GetFZoomToBoundF(Bundle bundle, Bundle bundle2) {
        return this.b.getFZoomToBoundF(bundle, bundle2);
    }

    public String GetFocusedBaseIndoorMapInfo() {
        if (this.a != 0) {
            return this.b.getFocusedBaseIndoorMapInfo();
        }
        return null;
    }

    public long GetId() {
        return this.a;
    }

    public int GetMapRenderType() {
        return this.b.getMapRenderType();
    }

    public Bundle GetMapStatus() {
        return this.b.getMapStatus(true);
    }

    public Bundle GetMapStatus(boolean z) {
        return this.b.getMapStatus(z);
    }

    public String GetNearlyObjID(long j, int i, int i2, int i3) {
        return this.b.getNearlyObjID(j, i, i2, i3);
    }

    public int GetVMPMapCityInfo(Bundle bundle) {
        return this.b.getVMPMapCityInfo(bundle);
    }

    public float GetZoomToBound(Bundle bundle, int i, int i2) {
        return this.b.getZoomToBound(bundle, i, i2);
    }

    public float GetZoomToBoundF(Bundle bundle) {
        return this.b.getZoomToBoundF(bundle);
    }

    @Deprecated
    public boolean Init(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z, boolean z2) {
        return this.a != 0 && this.b.init(str, str2, str3, str4, str5, str6, str7, i, i2, i3, i4, i5, i6, i7, z, z2);
    }

    public boolean IsBaseIndoorMapMode() {
        return this.a != 0 && this.b.isBaseIndoorMapMode();
    }

    public boolean IsPointInFocusBarBorder(double d, double d2, double d3) {
        return this.a != 0 && this.b.isPointInFocusBarBorder(d, d2, d3);
    }

    public boolean IsPointInFocusIDRBorder(double d, double d2) {
        return this.a != 0 && this.b.isPointInFocusIDRBorder(d, d2);
    }

    public boolean IsStreetArrowShown() {
        return this.b.isStreetArrowShown();
    }

    public boolean IsStreetCustomMarkerShown() {
        return this.b.isStreetCustomMarkerShown();
    }

    public boolean IsStreetPOIMarkerShown() {
        return this.a != 0 && this.b.isStreetPOIMarkerShown();
    }

    public boolean IsStreetRoadClickable() {
        return this.b.isStreetRoadClickable();
    }

    public boolean LayersIsShow(long j) {
        return this.b.layersIsShow(j);
    }

    public void MoveToScrPoint(int i, int i2) {
        this.b.moveToScrPoint(i, i2);
    }

    public void OnBackground() {
        try {
            this.c.readLock().lock();
            if (this.a != 0) {
                this.b.onBackground();
            }
        } finally {
            this.c.readLock().unlock();
        }
    }

    public void OnForeground() {
        try {
            this.c.readLock().lock();
            if (this.a != 0) {
                this.b.onForeground();
            }
        } finally {
            this.c.readLock().unlock();
        }
    }

    public String OnHotcityGet() {
        return this.b.onHotcityGet();
    }

    public void OnPause() {
        try {
            this.c.readLock().lock();
            if (this.a != 0) {
                this.b.onPause();
            }
        } finally {
            this.c.readLock().unlock();
        }
    }

    public boolean OnRecordAdd(int i) {
        return this.b.onRecordAdd(i);
    }

    public String OnRecordGetAll() {
        return this.b.onRecordGetAll();
    }

    public String OnRecordGetAt(int i) {
        return this.b.onRecordGetAt(i);
    }

    public boolean OnRecordImport(boolean z, boolean z2) {
        return this.b.onRecordImport(z, z2);
    }

    public boolean OnRecordReload(int i, boolean z) {
        return this.b.onRecordReload(i, z);
    }

    public boolean OnRecordRemove(int i, boolean z) {
        return this.b.onRecordRemove(i, z);
    }

    public boolean OnRecordStart(int i, boolean z, int i2) {
        return this.b.onRecordStart(i, z, i2);
    }

    public boolean OnRecordSuspend(int i, boolean z, int i2) {
        return this.b.onRecordSuspend(i, z, i2);
    }

    public void OnResume() {
        try {
            this.c.readLock().lock();
            if (this.a != 0) {
                this.b.onResume();
            }
        } finally {
            this.c.readLock().unlock();
        }
    }

    public String OnSchcityGet(String str) {
        return this.b.onSchcityGet(str);
    }

    public boolean OnUsrcityMsgInterval(int i) {
        return this.b.onUsrcityMsgInterval(i);
    }

    public int OnWifiRecordAdd(int i) {
        return this.b.onWifiRecordAdd(i);
    }

    public boolean Release() {
        boolean z;
        try {
            this.c.writeLock().lock();
            long j = this.a;
            if (j != 0) {
                BaseMapCallback.release(j);
                this.b.dispose();
                this.a = 0L;
                z = true;
            } else {
                z = false;
            }
            return z;
        } finally {
            this.c.writeLock().unlock();
        }
    }

    public void Remo() {
    }

    public boolean RemoveItemData(Bundle bundle) {
        return this.b.removeItemData(bundle);
    }

    public void RemoveLayer(long j) {
        this.b.removeLayer(j);
    }

    public void RemoveStreetAllCustomMarker() {
        this.b.removeStreetAllCustomMarker();
    }

    public void RemoveStreetCustomMaker(String str) {
        this.b.removeStreetCustomMaker(str);
    }

    public void ResetImageRes() {
        if (this.a != 0) {
            this.b.resetImageRes();
        }
    }

    public boolean ResumeCache() {
        return this.b.resumeCache();
    }

    public boolean SaveCache() {
        try {
            return this.b.saveCache();
        } catch (Throwable unused) {
            return false;
        }
    }

    public void SaveScreenToLocal(String str, String str2) {
        this.b.saveScreenToLocal(str, str2);
    }

    public String ScrPtToGeoPoint(int i, int i2) {
        return this.b.scrPtToGeoPoint(i, i2);
    }

    public void SetAllStreetCustomMarkerVisibility(boolean z) {
        if (this.a != 0) {
            this.b.setAllStreetCustomMarkerVisibility(z);
        }
    }

    public boolean SetCallback(a aVar) {
        if (aVar != null) {
            long j = this.a;
            if (j != 0 && BaseMapCallback.setMapCallback(j, aVar)) {
                return true;
            }
        }
        return false;
    }

    public void SetFocus(long j, long j2, boolean z, Bundle bundle) {
        this.b.setFocus(j, j2, z, bundle);
    }

    public boolean SetItsPreTime(int i, int i2, int i3) {
        return this.b.setItsPreTime(i, i2, i3);
    }

    public boolean SetLayerSceneMode(long j, int i) {
        return this.b.setLayerSceneMode(j, i);
    }

    public void SetLayersClickable(long j, boolean z) {
        this.b.setLayersClickable(j, z);
    }

    public void SetLocationLayerData(Bundle bundle) {
        this.b.setLocationLayerData(bundle);
    }

    public int SetMapControlMode(int i) {
        return this.b.setMapControlMode(i);
    }

    public void SetMapStatus(Bundle bundle) {
        this.b.setMapStatus(bundle);
    }

    public void SetNewMapStatus(Bundle bundle) {
        this.b.setNewMapStatus(bundle);
    }

    public boolean SetSDKLayerCallback(com.baidu.mapsdkplatform.comjni.a.a.a aVar) {
        if (aVar != null) {
            long j = this.a;
            if (j != 0 && BaseMapCallback.setMapSDKCallback(j, aVar)) {
                return true;
            }
        }
        return false;
    }

    public void SetStreetArrowShow(boolean z) {
        this.b.setStreetArrowShow(z);
    }

    public void SetStreetMarkerClickable(String str, boolean z) {
        this.b.setStreetMarkerClickable(str, z);
    }

    public void SetStreetRoadClickable(boolean z) {
        this.b.setStreetRoadClickable(z);
    }

    public void SetStyleMode(int i) {
        this.b.setStyleMode(i);
    }

    public void SetTargetStreetCustomMarkerVisibility(boolean z, String str) {
        if (this.a != 0) {
            this.b.setTargetStreetCustomMarkerVisibility(z, str);
        }
    }

    public void ShowBaseIndoorMap(boolean z) {
        this.b.showBaseIndoorMap(z);
    }

    public void ShowHotMap(boolean z, int i) {
        this.b.showHotMap(z, i);
    }

    public void ShowHotMap(boolean z, int i, String str) {
        this.b.showHotMap(z, i, str);
    }

    public void ShowLayers(long j, boolean z) {
        if (this.a != 0) {
            this.b.showLayers(j, z);
        }
    }

    public void ShowMistMap(boolean z, String str) {
        this.b.showMistMap(z, str);
    }

    public void ShowSatelliteMap(boolean z) {
        this.b.showSatelliteMap(z);
    }

    public void ShowStreetPOIMarker(boolean z) {
        if (this.a != 0) {
            this.b.showStreetPOIMarker(z);
        }
    }

    public void ShowStreetRoadMap(boolean z) {
        this.b.showStreetRoadMap(z);
    }

    public void ShowTrafficMap(boolean z) {
        this.b.showTrafficMap(z);
    }

    public void StartIndoorAnimation() {
        this.b.startIndoorAnimation();
    }

    public boolean SwitchBaseIndoorMapFloor(String str, String str2) {
        return this.b.switchBaseIndoorMapFloor(str, str2);
    }

    public boolean SwitchLayer(long j, long j2) {
        return this.b.switchLayer(j, j2);
    }

    public void UpdateLayers(long j) {
        this.b.updateLayers(j);
    }

    public void addOneOverlayItem(Bundle bundle) {
        this.b.addOneOverlayItem(bundle);
    }

    public void addOverlayItems(Bundle[] bundleArr, int i) {
        this.b.addOverlayItems(bundleArr, i);
    }

    public boolean addSDKTileData(Bundle bundle) {
        return this.b.nativeAddTileOverlay(this.a, bundle);
    }

    public boolean cleanSDKTileDataCache(long j) {
        return this.b.nativeCleanSDKTileDataCache(this.a, j);
    }

    public void clearHeatMapLayerCache(long j) {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return;
        }
        nABaseMap.clearHeatMapLayerCache(j);
    }

    public void clearUniversalLayer() {
        this.b.clearUniversalLayer();
    }

    public void closeParticleEffect(String str) {
        this.b.closeParticleEffect(str);
    }

    public void enablePOIAnimation(boolean z) {
        try {
            this.c.readLock().lock();
            this.b.enablePOIAnimation(z);
        } finally {
            this.c.readLock().unlock();
        }
    }

    public void entryFeedTopic(int i, String str, String str2) {
        this.b.entrySearchTopic(i, str, str2);
    }

    public void entrySearchTopic(int i) {
        this.b.entrySearchTopic(i, "", "");
    }

    public void exitSearchTopic() {
        this.b.exitSearchTopic();
    }

    public void focusTrafficUGCLabel() {
        this.b.focusTrafficUGCLabel();
    }

    public boolean getDEMEnable() {
        return this.b.getDEMEnable();
    }

    public boolean getDrawHouseHeightEnable() {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return false;
        }
        return nABaseMap.getDrawHouseHeightEnable();
    }

    public int getFontSizeLevel() {
        return this.b.getFontSizeLevel();
    }

    public long getLayerIDByTag(String str) {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return 0L;
        }
        return nABaseMap.getLayerIDByTag(str);
    }

    public boolean getMapBarData(Bundle bundle) {
        return this.b.getMapBarData(bundle);
    }

    public int getMapLanguage() {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return 0;
        }
        return nABaseMap.getMapLanguage();
    }

    public int getMapScene() {
        return this.b.getMapScene();
    }

    public Bundle getMapStatusLimits() {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return null;
        }
        return nABaseMap.getMapStatusLimits();
    }

    public boolean getMapStatusLimitsLevel(int[] iArr) {
        return this.b.getMapStatusLimitsLevel(iArr);
    }

    public int getMapTheme() {
        return this.b.getMapTheme();
    }

    public float[] getProjectionMatrix() {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return null;
        }
        float[] fArr = new float[16];
        nABaseMap.getProjectMatrix(fArr);
        return fArr;
    }

    public String getProjectionPt(String str) {
        return this.b.getProjectionPt(str);
    }

    public int getScaleLevel(int i, int i2) {
        return this.b.getScaleLevel(i, i2);
    }

    public float[] getViewMatrix() {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return null;
        }
        float[] fArr = new float[16];
        nABaseMap.getViewMatrix(fArr);
        return fArr;
    }

    public boolean importMapTheme(int i) {
        return this.b.importMapTheme(i);
    }

    public boolean initCustomStyle(String str, String str2) {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return false;
        }
        return nABaseMap.initCustomStyle(str, str2);
    }

    public boolean initWithOptions(Bundle bundle, boolean z) {
        return this.a != 0 && this.b.initWithOptions(bundle, z);
    }

    public boolean isAnimationRunning() {
        return this.b.isAnimationRunning();
    }

    public boolean isNaviMode() {
        return this.b.isNaviMode();
    }

    public boolean moveLayerBelowTo(long j, int i) {
        return this.b.moveLayerBelowTo(j, i);
    }

    public boolean performAction(String str) {
        return this.b.performAction(str);
    }

    public void recycleMemory(int i) {
        this.b.recycleMemory(i);
    }

    public boolean releaseFromOfflineMap() {
        boolean z;
        try {
            this.c.writeLock().lock();
            if (this.a != 0) {
                this.b.dispose();
                this.a = 0L;
                z = true;
            } else {
                z = false;
            }
            return z;
        } finally {
            this.c.writeLock().unlock();
        }
    }

    public void removeOneOverlayItem(Bundle bundle) {
        this.b.removeOneOverlayItem(bundle);
    }

    public void removeOverlayItems(Bundle[] bundleArr) {
        this.b.removeOneOverlayItems(bundleArr);
    }

    public void renderDone() {
        try {
            this.c.readLock().lock();
            this.b.renderDone();
        } finally {
            this.c.readLock().unlock();
        }
    }

    public void renderInit(int i, int i2, Surface surface, int i3) {
        try {
            this.c.readLock().lock();
            this.b.renderInit(i, i2, surface, i3);
        } finally {
            this.c.readLock().unlock();
        }
    }

    public int renderRender() {
        try {
            this.c.readLock().lock();
            return this.b.renderRender();
        } finally {
            this.c.readLock().unlock();
        }
    }

    public void renderResize(int i, int i2) {
        try {
            this.c.readLock().lock();
            this.b.renderResize(i, i2);
        } finally {
            this.c.readLock().unlock();
        }
    }

    public void resize(int i, int i2) {
        if (this.a != 0) {
            this.b.renderResize(i, i2);
        }
    }

    public void setCustomStyleEnable(boolean z) {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return;
        }
        nABaseMap.setCustomStyleEnable(z);
    }

    public void setDEMEnable(boolean z) {
        this.b.setDEMEnable(z);
    }

    public void setDrawHouseHeightEnable(boolean z) {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return;
        }
        nABaseMap.setDrawHouseHeightEnable(z);
    }

    public void setFontSizeLevel(int i) {
        this.b.setFontSizeLevel(i);
    }

    public void setMapLanguage(int i) {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return;
        }
        nABaseMap.setMapLanguage(i);
    }

    public void setMapScene(int i) {
        this.b.setMapScene(i);
    }

    public void setMapStatusLimits(Bundle bundle) {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return;
        }
        nABaseMap.setMapStatusLimits(bundle);
    }

    public boolean setMapStatusLimitsLevel(int i, int i2) {
        return this.b.setMapStatusLimitsLevel(i, i2);
    }

    public boolean setMapTheme(int i, Bundle bundle) {
        return this.b.setMapTheme(i, bundle);
    }

    public boolean setMapThemeScene(int i, int i2, Bundle bundle) {
        return this.b.setMapThemeScene(i, i2, bundle);
    }

    public void setMaxAndMinZoomLevel(Bundle bundle) {
        NABaseMap nABaseMap = this.b;
        if (nABaseMap == null) {
            return;
        }
        nABaseMap.setMaxAndMinZoomLevel(bundle);
    }

    public void setRecommendPOIScene(int i) {
        this.b.setRecommendPOIScene(i);
    }

    public boolean setTestSwitch(boolean z) {
        return this.b.setTestSwitch(z);
    }

    public void setTrafficUGCData(String str) {
        this.b.setTrafficUGCData(str);
    }

    public void setUniversalFilter(String str) {
        this.b.setUniversalFilter(str);
    }

    public void showFootMarkGrid(boolean z, String str) {
        this.b.showFootMarkGrid(z, str);
    }

    public boolean showParticleEffect(int i) {
        return this.b.showParticleEffect(i);
    }

    public boolean showParticleEffectByName(String str, boolean z) {
        return this.b.showParticleEffectByName(str, z);
    }

    public boolean showParticleEffectByType(int i) {
        return this.b.showParticleEffectByType(i);
    }

    public void showTrafficUGCMap(boolean z) {
        this.b.showTrafficUGCMap(z);
    }

    public void showUniversalLayer(Bundle bundle) {
        this.b.showUniversalLayer(bundle);
    }

    public void surfaceDestroyed(Surface surface) {
        try {
            this.c.readLock().lock();
            this.b.surfaceDestroyed(surface);
        } finally {
            this.c.readLock().unlock();
        }
    }

    public void unFocusTrafficUGCLabel() {
        this.b.unFocusTrafficUGCLabel();
    }

    public void updateBaseLayers() {
        this.b.updateBaseLayers();
    }

    public void updateDrawFPS() {
        this.b.updateDrawFPS();
    }

    public void updateFootMarkGrid() {
        this.b.updateFootMarkGrid();
    }

    public void updateOneOverlayItem(Bundle bundle) {
        this.b.updateOneOverlayItem(bundle);
    }

    public boolean updateSDKTile(Bundle bundle) {
        return this.b.nativeUpdateSDKTile(this.a, bundle);
    }

    public String worldPointToScreenPoint(float f, float f2, float f3) {
        return this.b.worldPointToScreenPoint(f, f2, f3);
    }
}
