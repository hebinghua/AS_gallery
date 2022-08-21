package com.baidu.platform.comapi.map;

import com.baidu.platform.comjni.map.basemap.AppBaseMap;

/* loaded from: classes.dex */
public class an extends InnerOverlay {
    public an() {
        super(30);
    }

    @Override // com.baidu.platform.comapi.map.InnerOverlay
    public boolean addedToMapView() {
        AppBaseMap appBaseMap = this.mBaseMap;
        if (appBaseMap == null) {
            return false;
        }
        long AddLayer = appBaseMap.AddLayer(2, 0, MapController.STREETPOPUP_LAYER_TAG);
        this.mLayerID = AddLayer;
        if (AddLayer == 0) {
            return false;
        }
        this.mBaseMap.SetLayersClickable(AddLayer, true);
        this.mBaseMap.ShowLayers(this.mLayerID, false);
        return true;
    }
}
