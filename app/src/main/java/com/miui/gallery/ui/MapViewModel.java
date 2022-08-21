package com.miui.gallery.ui;

import android.app.Application;
import android.text.TextUtils;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelUtils;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.map.data.MapDataProvider;
import com.miui.gallery.map.data.MapItem;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.IMedia;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.provider.cache.MediaProcessor;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.PhotoPageIntent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/* loaded from: classes2.dex */
public class MapViewModel extends AndroidViewModel {
    public final CacheLiveData<MediaCacheItem, IRecord> mAllMediasShowOnMap;
    public final CacheLiveData<MediaCacheItem, IRecord> mMapAlbumCovers;
    public String mMediaTitle;
    public final MutableLiveData<Boolean> mShowAllPhotos;

    public MapViewModel(Application application) {
        super(application);
        this.mAllMediasShowOnMap = (CacheLiveData) ViewModelUtils.closeOnClear(this, new CacheLiveData(getApplication(), null, null, null, null, null, new MediaProcessor(true)));
        this.mMapAlbumCovers = (CacheLiveData) ViewModelUtils.closeOnClear(this, new CacheLiveData(getApplication(), null, null, null, null, null, new MediaProcessor(true)));
        this.mShowAllPhotos = new MutableLiveData<>();
    }

    public void initMapStatus(boolean z, String str) {
        this.mShowAllPhotos.setValue(Boolean.valueOf(z));
        this.mMediaTitle = str;
        queryItemsShowOnMap();
    }

    public final String getSelection() {
        if (needShowAllPhotosStatus()) {
            return "sha1 NOT NULL AND alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != -1000 AND exifGPSLongitude NOT NULL AND exifGPSLatitude NOT NULL";
        }
        StringBuilder sb = new StringBuilder();
        Locale locale = Locale.US;
        sb.append(String.format(locale, "title like %s", "'%" + this.mMediaTitle + "%'"));
        sb.append(" AND ");
        sb.append("sha1 NOT NULL AND alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != -1000 AND exifGPSLongitude NOT NULL AND exifGPSLatitude NOT NULL");
        return sb.toString();
    }

    public void goToPhotoPage(MapFragment mapFragment, MapItem mapItem) {
        IMedia source = mapItem.getSource();
        new PhotoPageIntent.Builder(mapFragment, InternalPhotoPageActivity.class).setUri(MapDataProvider.PHOTOS_SHOW_ON_MAP).setSelection(String.format(Locale.US, "_id = %d", Long.valueOf(source.getId()))).setOrderBy("dateModified DESC").setImageLoadParams(new ImageLoadParams.Builder().setKey(source.getId()).setFilePath(source.getClearThumbnail()).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setInitPosition(0).setMimeType(source.getMimeType()).setFileLength(source.getSize()).setImageWidth(source.getWidth()).setImageHeight(source.getHeight()).setCreateTime(source.getCreateTime()).setLocation(source.getLocation()).build()).setUnfoldBurst(true).build().gotoPhotoPage();
    }

    public void goToPhotoListPage(MapFragment mapFragment, Collection<MapItem> collection) {
        ArrayList arrayList = new ArrayList();
        for (MapItem mapItem : collection) {
            arrayList.add(Long.valueOf(mapItem.getSource().getId()));
        }
        IntentUtil.goToMapPhotoListPage(mapFragment, TextUtils.join(",", arrayList));
    }

    public CacheLiveData<MediaCacheItem, IRecord> getItemsShowOnMap() {
        return this.mAllMediasShowOnMap;
    }

    public MutableLiveData<Boolean> getShowAllPhotoLiveData() {
        return this.mShowAllPhotos;
    }

    public boolean needShowAllPhotosStatus() {
        return this.mShowAllPhotos.getValue() != null && this.mShowAllPhotos.getValue().booleanValue();
    }

    public void queryItemsShowOnMap() {
        this.mAllMediasShowOnMap.updateQueryArgs(MapDataProvider.PHOTOS_SHOW_ON_MAP, getSelection(), null, "dateModified DESC", true);
    }

    public void queryMapAlbumCovers() {
        this.mShowAllPhotos.setValue(Boolean.TRUE);
        this.mMapAlbumCovers.updateQueryArgs(MapDataProvider.MAP_ALBUM_COVERS, getSelection(), null, "dateModified DESC", false);
    }

    public CacheLiveData<MediaCacheItem, IRecord> getMapCoverItems() {
        return this.mMapAlbumCovers;
    }
}
