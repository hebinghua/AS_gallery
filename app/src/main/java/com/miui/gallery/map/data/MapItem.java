package com.miui.gallery.map.data;

import android.text.TextUtils;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.map.cluster.ClusterItem;
import com.miui.gallery.map.cluster.MapLatLng;
import com.miui.gallery.map.utils.LocationConverter;
import com.miui.gallery.provider.cache.IMedia;
import java.util.Objects;

/* loaded from: classes2.dex */
public class MapItem implements ClusterItem {
    public MapLatLng mMapLatLng;
    public IMedia mMediaItem;
    public String mPath;

    @Override // com.miui.gallery.map.cluster.ClusterItem
    public MapLatLng getPosition() {
        return this.mMapLatLng;
    }

    public String getPath() {
        return this.mPath;
    }

    public void mapping(IMedia iMedia) {
        if (iMedia == null) {
            return;
        }
        this.mMediaItem = iMedia;
        if (iMedia.hasValidLocationInfo() && this.mMapLatLng == null) {
            this.mMapLatLng = LocationConverter.convertToMapLatLng(LocationUtil.convertRationalLatLonToDouble(iMedia.getLatitude(), String.valueOf(iMedia.getLatitudeRef())), LocationUtil.convertRationalLatLonToDouble(iMedia.getLongitude(), String.valueOf(iMedia.getLongitudeRef())));
        }
        this.mPath = !TextUtils.isEmpty(iMedia.getThumbnail()) ? iMedia.getThumbnail() : iMedia.getFilePath();
    }

    public IMedia getSource() {
        return this.mMediaItem;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        MapItem mapItem = (MapItem) obj;
        return Objects.equals(this.mPath, mapItem.mPath) && Objects.equals(this.mMapLatLng, mapItem.mMapLatLng);
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), this.mPath, this.mMapLatLng);
    }
}
