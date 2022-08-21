package com.baidu.platform.comapi.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.baidu.platform.comapi.map.OverlayItem;
import com.baidu.platform.comjni.tools.ParcelItem;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ItemizedOverlay<Item extends OverlayItem> extends Overlay implements Comparator<Integer> {
    public ArrayList<OverlayItem> a;
    public ArrayList<Integer> b;
    public Drawable c;
    public MapSurfaceView d;
    public MapTextureView e;
    public boolean f;
    public Drawable g;
    public OverlayItem h;
    public int i = 0;
    public int j;

    public ItemizedOverlay(Drawable drawable, MapSurfaceView mapSurfaceView) {
        this.mType = 27;
        this.c = drawable;
        this.a = new ArrayList<>();
        this.b = new ArrayList<>();
        this.d = mapSurfaceView;
        this.mLayerID = 0L;
    }

    public ItemizedOverlay(Drawable drawable, MapTextureView mapTextureView) {
        this.mType = 27;
        this.c = drawable;
        this.a = new ArrayList<>();
        this.b = new ArrayList<>();
        this.e = mapTextureView;
        this.mLayerID = 0L;
    }

    private void a(List<OverlayItem> list, boolean z) {
        a(list, z, false);
    }

    private void a(List<OverlayItem> list, boolean z, boolean z2) {
        if (z2) {
            synchronized (this) {
                ArrayList<OverlayItem> arrayList = this.a;
                if (arrayList != null) {
                    arrayList.clear();
                }
            }
        }
        if (this.mLayerID == 0) {
            if (z) {
                return;
            }
            synchronized (this) {
                ArrayList<OverlayItem> arrayList2 = this.a;
                if (arrayList2 != null && list != null) {
                    arrayList2.addAll(list);
                }
            }
            return;
        }
        Bundle bundle = new Bundle();
        bundle.clear();
        ArrayList arrayList3 = new ArrayList();
        bundle.putLong("itemaddr", this.mLayerID);
        bundle.putInt("bshow", 1);
        if (z) {
            bundle.putString("extparam", "update");
        }
        for (int i = 0; i < list.size(); i++) {
            OverlayItem overlayItem = list.get(i);
            if (overlayItem.getMarker() == null) {
                overlayItem.setMarker(this.c);
            }
            if (TextUtils.isEmpty(overlayItem.getId())) {
                overlayItem.setId(ad.a());
            }
            ParcelItem parcelItem = new ParcelItem();
            Drawable marker = overlayItem.getMarker();
            byte[] gifData = overlayItem.getGifData();
            if (marker != null || gifData != null) {
                Bundle bundle2 = new Bundle();
                GeoPoint a = overlayItem.getCoordType() == OverlayItem.CoordType.CoordType_BD09LL ? z.a(overlayItem.getPoint()) : overlayItem.getPoint();
                bundle2.putInt("x", (int) a.getLongitude());
                bundle2.putInt("y", (int) a.getLatitude());
                bundle2.putFloat(MapBundleKey.MapObjKey.OBJ_GEO_Z, overlayItem.getGeoZ());
                bundle2.putInt(MapBundleKey.MapObjKey.OBJ_INDOOR_POI, overlayItem.getIndoorPoi());
                bundle2.putInt("showLR", 1);
                bundle2.putInt("iconwidth", 0);
                bundle2.putInt("iconlayer", 1);
                bundle2.putFloat("ax", overlayItem.getAnchorX());
                bundle2.putFloat("ay", overlayItem.getAnchorY());
                bundle2.putInt("bound", overlayItem.getBound());
                bundle2.putInt("level", overlayItem.getLevel());
                bundle2.putInt("mask", overlayItem.getMask());
                bundle2.putString("popname", "" + overlayItem.getId());
                if (gifData != null) {
                    bundle2.putFloat("gifscale", overlayItem.getScale());
                    bundle2.putInt("gifsize", gifData.length);
                    bundle2.putByteArray("imgdata", gifData);
                    bundle2.putInt("imgindex", c());
                } else {
                    Bitmap a2 = com.baidu.platform.comapi.util.d.a(marker);
                    if (a2 != null) {
                        bundle2.putInt("imgindex", overlayItem.getResId());
                        bundle2.putInt("imgW", a2.getWidth());
                        bundle2.putInt("imgH", a2.getHeight());
                        if (z || !a(overlayItem)) {
                            ByteBuffer allocate = ByteBuffer.allocate(a2.getWidth() * a2.getHeight() * 4);
                            a2.copyPixelsToBuffer(allocate);
                            bundle2.putByteArray("imgdata", allocate.array());
                        } else {
                            bundle2.putByteArray("imgdata", null);
                        }
                    }
                }
                String[] a3 = a(overlayItem.getClickRect());
                if (a3 != null && a3.length > 0) {
                    bundle2.putStringArray("clickrect", a3);
                }
                bundle2.putBundle("animate", overlayItem.getAnimate());
                bundle2.putBundle("delay", overlayItem.getDelay());
                parcelItem.setBundle(bundle2);
                arrayList3.add(parcelItem);
                if (!z) {
                    this.a.add(overlayItem);
                }
            }
        }
        if (arrayList3.size() > 0) {
            ParcelItem[] parcelItemArr = new ParcelItem[arrayList3.size()];
            for (int i2 = 0; i2 < arrayList3.size(); i2++) {
                parcelItemArr[i2] = (ParcelItem) arrayList3.get(i2);
            }
            bundle.putParcelableArray("itemdatas", parcelItemArr);
            this.d.getController().getBaseMap().AddItemData(bundle, z2);
        }
        synchronized (this) {
            this.f = true;
        }
    }

    private int b(boolean z) {
        ArrayList arrayList;
        if (this.a == null) {
            return 0;
        }
        synchronized (this) {
            if (this.a.size() == 0) {
                return 0;
            }
            synchronized (this) {
                arrayList = new ArrayList(this.a);
            }
            int i = Integer.MIN_VALUE;
            int i2 = Integer.MAX_VALUE;
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                GeoPoint point = ((OverlayItem) it.next()).getPoint();
                int latitude = (int) (z ? point.getLatitude() : point.getLongitude());
                if (latitude > i) {
                    i = latitude;
                }
                if (latitude < i2) {
                    i2 = latitude;
                }
            }
            return i - i2;
        }
    }

    private int c() {
        int i = this.j;
        if (i < Integer.MAX_VALUE) {
            int i2 = i + 1;
            this.j = i2;
            return i2;
        }
        return 0;
    }

    public int a(int i) {
        synchronized (this) {
            ArrayList<OverlayItem> arrayList = this.a;
            if (arrayList != null && arrayList.size() != 0) {
                return i;
            }
            return -1;
        }
    }

    public void a() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.a);
        }
        removeAll();
        addItem(arrayList);
    }

    public synchronized void a(boolean z) {
        this.f = z;
    }

    public boolean a(OverlayItem overlayItem) {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.a);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            OverlayItem overlayItem2 = (OverlayItem) it.next();
            if (overlayItem.getResId() == -1) {
                return false;
            }
            if (overlayItem2.getResId() != -1 && overlayItem.getResId() == overlayItem2.getResId()) {
                return true;
            }
        }
        return false;
    }

    public String[] a(ArrayList<Bundle> arrayList) {
        if (arrayList == null || arrayList.size() <= 0) {
            return null;
        }
        int size = arrayList.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            JSONObject jSONObject = new JSONObject();
            Bundle bundle = arrayList.get(i);
            for (String str : bundle.keySet()) {
                try {
                    jSONObject.put(str, bundle.get(str));
                } catch (JSONException unused) {
                }
            }
            strArr[i] = jSONObject.toString();
        }
        return strArr;
    }

    public void addItem(OverlayItem overlayItem) {
        if (overlayItem != null) {
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(overlayItem);
            addItem(arrayList);
        }
    }

    public void addItem(List<OverlayItem> list) {
        a(list, false, false);
    }

    public void addItemsByReplace(List<OverlayItem> list) {
        a(list, false, true);
    }

    public boolean b() {
        return this.f;
    }

    @Override // java.util.Comparator
    public int compare(Integer num, Integer num2) {
        GeoPoint point;
        GeoPoint point2;
        synchronized (this) {
            point = this.a.get(num.intValue()).getPoint();
            point2 = this.a.get(num2.intValue()).getPoint();
        }
        if (point.getLatitude() > point2.getLatitude()) {
            return -1;
        }
        if (point.getLatitude() < point2.getLatitude()) {
            return 1;
        }
        if (point.getLongitude() < point2.getLongitude()) {
            return -1;
        }
        return point.getLongitude() == point2.getLongitude() ? 0 : 1;
    }

    public ArrayList<OverlayItem> getAllItem() {
        return this.a;
    }

    public GeoPoint getCenter() {
        int a = a(0);
        if (a == -1) {
            return null;
        }
        return getItem(a).getPoint();
    }

    public final OverlayItem getItem(int i) {
        ArrayList arrayList;
        if (this.a == null) {
            return null;
        }
        synchronized (this) {
            arrayList = new ArrayList(this.a);
        }
        if (arrayList.size() > i && i >= 0) {
            return (OverlayItem) arrayList.get(i);
        }
        return null;
    }

    public int getLatSpanE6() {
        return b(true);
    }

    public int getLonSpanE6() {
        return b(false);
    }

    public int getUpdateType() {
        return this.i;
    }

    public void initLayer() {
        long AddLayer = this.d.getController().getBaseMap().AddLayer(0, 0, MapController.ITEM_LAYER_TAG);
        this.mLayerID = AddLayer;
        if (AddLayer != 0) {
            return;
        }
        throw new RuntimeException("can not add new layer");
    }

    public boolean onTap(int i) {
        return false;
    }

    public boolean onTap(int i, int i2, GeoPoint geoPoint) {
        return false;
    }

    public boolean onTap(GeoPoint geoPoint, MapSurfaceView mapSurfaceView) {
        return false;
    }

    public boolean removeAll() {
        synchronized (this) {
            if (this.a.isEmpty()) {
                return false;
            }
            if (this.d.getController() != null && this.d.getController().getBaseMap() != null) {
                this.d.getController().getBaseMap().ClearLayer(this.mLayerID);
            }
            synchronized (this) {
                this.a.clear();
                this.f = true;
            }
            return true;
        }
    }

    public boolean removeItem(OverlayItem overlayItem) {
        if (this.mLayerID == 0) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("itemaddr", this.mLayerID);
        if (overlayItem.getId().equals("")) {
            return false;
        }
        bundle.putString("id", overlayItem.getId());
        if (!this.d.getController().getBaseMap().RemoveItemData(bundle)) {
            return false;
        }
        synchronized (this) {
            this.a.remove(overlayItem);
            this.f = true;
        }
        return true;
    }

    public boolean removeOneItem(Iterator<OverlayItem> it, OverlayItem overlayItem) {
        if (this.mLayerID == 0) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("itemaddr", this.mLayerID);
        if (overlayItem.getId().equals("")) {
            return false;
        }
        bundle.putString("id", overlayItem.getId());
        if (!this.d.getController().getBaseMap().RemoveItemData(bundle)) {
            return false;
        }
        it.remove();
        synchronized (this) {
            this.f = true;
        }
        return true;
    }

    public void setFocus(int i, boolean z) {
        OverlayItem item;
        ArrayList arrayList;
        if (this.h == null || (item = getItem(i)) == null) {
            return;
        }
        if (z) {
            this.h.setGeoPoint(new GeoPoint(item.getPoint().getLatitude(), item.getPoint().getLongitude()));
            synchronized (this) {
                arrayList = new ArrayList(this.a);
            }
            if (arrayList.contains(this.h)) {
                updateItem(this.h);
            } else {
                addItem(this.h);
            }
        } else {
            removeItem(this.h);
        }
        MapSurfaceView mapSurfaceView = this.d;
        if (mapSurfaceView == null) {
            return;
        }
        mapSurfaceView.refresh(this);
    }

    public void setFocusMarker(Drawable drawable) {
        this.g = drawable;
        if (this.h == null) {
            this.h = new OverlayItem(null, "", "");
        }
        this.h.setMarker(this.g);
    }

    public void setFocusMarker(Drawable drawable, float f, float f2) {
        this.g = drawable;
        if (this.h == null) {
            OverlayItem overlayItem = new OverlayItem(null, "", "");
            this.h = overlayItem;
            overlayItem.setAnchor(f, f2);
        }
        this.h.setMarker(this.g);
    }

    public void setUpdateType(int i) {
        this.i = i;
    }

    public void setmMarker(Drawable drawable) {
        this.c = drawable;
    }

    public synchronized int size() {
        ArrayList<OverlayItem> arrayList;
        arrayList = this.a;
        return arrayList == null ? 0 : arrayList.size();
    }

    public boolean updateItem(OverlayItem overlayItem) {
        ArrayList arrayList;
        boolean z;
        if (overlayItem != null && !overlayItem.getId().equals("")) {
            synchronized (this) {
                arrayList = new ArrayList(this.a);
            }
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                } else if (overlayItem.getId().equals(((OverlayItem) it.next()).getId())) {
                    z = true;
                    break;
                }
            }
            if (!z) {
                return false;
            }
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(overlayItem);
            a(arrayList2, true);
            return true;
        }
        return false;
    }

    public boolean updateItem(List<OverlayItem> list) {
        if (list == null) {
            return false;
        }
        a(list, true);
        return true;
    }
}
