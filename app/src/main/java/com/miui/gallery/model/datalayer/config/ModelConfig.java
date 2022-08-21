package com.miui.gallery.model.datalayer.config;

import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.model.datalayer.repository.ILocationRepository;
import com.miui.gallery.model.datalayer.repository.album.AlbumRepositoryImpl;
import com.miui.gallery.model.datalayer.repository.location.LocationRepositoryImpl;
import com.miui.gallery.model.datalayer.repository.photo.CloudRepositoryImpl;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class ModelConfig {
    public static final HashMap<String, Class<?>> mConfigs = new HashMap<>(6);

    /* loaded from: classes2.dex */
    public interface ModelNames {
        public static final Class<AbstractCloudRepository> CLOUD_REPOSITORY = AbstractCloudRepository.class;
        public static final Class<ILocationRepository> LOCATION_REPOSITORY = ILocationRepository.class;
        public static final Class<AbstractAlbumRepository> ALBUM_REPOSITORY = AbstractAlbumRepository.class;
    }

    public static HashMap<String, Class<?>> getModelConfigs() {
        HashMap<String, Class<?>> hashMap = mConfigs;
        if (hashMap.isEmpty()) {
            synchronized (hashMap) {
                if (hashMap.isEmpty()) {
                    initModels();
                }
            }
            return hashMap;
        }
        return hashMap;
    }

    public static void initModels() {
        HashMap<String, Class<?>> hashMap = mConfigs;
        hashMap.put(ModelNames.CLOUD_REPOSITORY.getName(), CloudRepositoryImpl.class);
        hashMap.put(ModelNames.LOCATION_REPOSITORY.getName(), LocationRepositoryImpl.class);
        hashMap.put(ModelNames.ALBUM_REPOSITORY.getName(), AlbumRepositoryImpl.class);
    }
}
