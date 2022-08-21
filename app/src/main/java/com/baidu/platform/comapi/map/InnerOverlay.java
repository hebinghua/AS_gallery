package com.baidu.platform.comapi.map;

import android.os.Bundle;
import android.text.TextUtils;
import com.baidu.platform.comjni.map.basemap.AppBaseMap;

/* loaded from: classes.dex */
public abstract class InnerOverlay extends Overlay {
    private boolean a;
    public String b;
    public Bundle c;
    public AppBaseMap mBaseMap;

    public InnerOverlay() {
        this.mBaseMap = null;
        this.b = null;
        this.c = null;
        this.a = true;
    }

    public InnerOverlay(int i) {
        this.mBaseMap = null;
        this.b = null;
        this.c = null;
        this.a = true;
        setType(i);
    }

    public InnerOverlay(int i, AppBaseMap appBaseMap) {
        this.mBaseMap = null;
        this.b = null;
        this.c = null;
        this.a = true;
        setType(i);
        this.mBaseMap = appBaseMap;
    }

    public boolean IsOverlayShow() {
        AppBaseMap appBaseMap;
        return (this.mLayerID == 0 || (appBaseMap = this.mBaseMap) == null || appBaseMap.GetId() == 0 || !this.mBaseMap.LayersIsShow(this.mLayerID)) ? false : true;
    }

    public void SetMapParam(long j, AppBaseMap appBaseMap) {
        this.mLayerID = j;
        this.mBaseMap = appBaseMap;
    }

    public void SetOverlayShow(boolean z) {
        AppBaseMap appBaseMap;
        long j = 0;
        if (this.mLayerID == 0 || (appBaseMap = this.mBaseMap) == null || appBaseMap.GetId() == 0) {
            return;
        }
        if (y.a) {
            j = System.currentTimeMillis();
        }
        this.mBaseMap.ShowLayers(this.mLayerID, z);
        if (!y.a) {
            return;
        }
        y.a("InnerOverlay", "ShowLayer:" + this.mLayerID + ":" + z + " tag:" + getLayerTag() + " [" + (System.currentTimeMillis() - j) + "ms]");
    }

    public void UpdateOverlay() {
        AppBaseMap appBaseMap;
        long j = 0;
        if (this.mLayerID == 0 || (appBaseMap = this.mBaseMap) == null || appBaseMap.GetId() == 0) {
            return;
        }
        if (y.a) {
            j = System.currentTimeMillis();
        }
        this.mBaseMap.UpdateLayers(this.mLayerID);
        if (!y.a) {
            return;
        }
        y.a("InnerOverlay", "UpdateLayer:" + this.mLayerID + " tag:" + getLayerTag() + " [" + (System.currentTimeMillis() - j) + "ms]");
    }

    public boolean addedToMapView() {
        AppBaseMap appBaseMap = this.mBaseMap;
        if (appBaseMap != null && appBaseMap.GetId() != 0) {
            long currentTimeMillis = y.a ? System.currentTimeMillis() : 0L;
            this.mLayerID = this.mBaseMap.AddLayer(getUpdateType(), getUpdateTimeInterval(), getLayerTag());
            if (y.a) {
                y.a("InnerOverlay", "AddLayer:" + this.mLayerID + " type:" + this.mType + " tag:" + getLayerTag() + " [" + (System.currentTimeMillis() - currentTimeMillis) + "ms]");
            }
            long j = this.mLayerID;
            if (j != 0) {
                this.mBaseMap.SetLayersClickable(j, this.a);
                SetOverlayShow(getDefaultShowStatus());
                return true;
            }
        }
        return false;
    }

    public void clear() {
        long currentTimeMillis = y.a ? System.currentTimeMillis() : 0L;
        if (!TextUtils.isEmpty(this.b)) {
            this.b = null;
            AppBaseMap appBaseMap = this.mBaseMap;
            if (appBaseMap != null) {
                appBaseMap.ClearLayer(this.mLayerID);
            }
        }
        if (y.a) {
            y.a("InnerOverlay", "ClearLayer:" + this.mLayerID + " tag:" + getLayerTag() + " [" + (System.currentTimeMillis() - currentTimeMillis) + "ms]");
        }
    }

    public String getData() {
        return this.b;
    }

    public boolean getDefaultShowStatus() {
        return false;
    }

    public String getLayerTag() {
        return "default";
    }

    public Bundle getParam() {
        return this.c;
    }

    public int getType() {
        return this.mType;
    }

    public int getUpdateTimeInterval() {
        return 0;
    }

    public int getUpdateType() {
        return 0;
    }

    public void setClickAble(boolean z) {
        this.a = z;
        AppBaseMap appBaseMap = this.mBaseMap;
        if (appBaseMap == null || appBaseMap.GetId() == 0) {
            return;
        }
        long j = this.mLayerID;
        if (j == 0) {
            return;
        }
        this.mBaseMap.SetLayersClickable(j, z);
    }

    public void setData(String str) {
        if (str != null) {
            this.b = str;
        }
    }

    public void setFocus(int i, boolean z) {
        setFocus(i, z, null);
    }

    public void setFocus(int i, boolean z, String str) {
        AppBaseMap appBaseMap = this.mBaseMap;
        if (appBaseMap == null || appBaseMap.GetId() == 0) {
            return;
        }
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(str)) {
            bundle.putString("uid", str);
        }
        this.mBaseMap.SetFocus(this.mLayerID, i, z, bundle);
    }

    public void setParam(Bundle bundle) {
        this.c = bundle;
    }

    public void setType(int i) {
        this.mType = i;
    }
}
