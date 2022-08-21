package com.miui.gallery.model.datalayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.SparseArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.base_optimization.util.ExceptionUtils;
import com.miui.gallery.util.json.UriDeserializerGsonAdapter;
import com.miui.gallery.util.json.UriSerializerGsonAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.reflect.Type;

/* loaded from: classes2.dex */
public class AlbumFileCache {
    public final Gson mDeserializerGsonAdapter;
    public final Gson mSerializerGsonAdapter;
    public final SparseArray<SharedPreferences> mShareds;

    /* loaded from: classes2.dex */
    public enum AlbumCacheType {
        COMMON,
        AI
    }

    public static AlbumFileCache getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final AlbumFileCache INSTANCE = new AlbumFileCache();
    }

    public AlbumFileCache() {
        this.mShareds = new SparseArray<>(3);
        this.mSerializerGsonAdapter = new GsonBuilder().registerTypeAdapter(Uri.class, new UriSerializerGsonAdapter()).create();
        this.mDeserializerGsonAdapter = new GsonBuilder().registerTypeAdapter(Uri.class, new UriDeserializerGsonAdapter()).create();
    }

    public final void initSharedsForType(AlbumCacheType albumCacheType) {
        if (this.mShareds.get(albumCacheType.ordinal()) != null) {
            return;
        }
        AlbumCacheType albumCacheType2 = AlbumCacheType.AI;
        if (albumCacheType == albumCacheType2) {
            SparseArray<SharedPreferences> sparseArray = this.mShareds;
            int ordinal = albumCacheType2.ordinal();
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            sparseArray.put(ordinal, sGetAndroidContext.getSharedPreferences("album_cache" + albumCacheType2.name(), 0));
            return;
        }
        AlbumCacheType albumCacheType3 = AlbumCacheType.COMMON;
        if (albumCacheType != albumCacheType3) {
            return;
        }
        SparseArray<SharedPreferences> sparseArray2 = this.mShareds;
        int ordinal2 = albumCacheType3.ordinal();
        Context sGetAndroidContext2 = GalleryApp.sGetAndroidContext();
        sparseArray2.put(ordinal2, sGetAndroidContext2.getSharedPreferences("album_cache" + albumCacheType3.name(), 0));
    }

    public void clear() {
        for (int i = 0; i < this.mShareds.size(); i++) {
            SharedPreferences valueAt = this.mShareds.valueAt(i);
            if (valueAt != null) {
                valueAt.edit().clear().commit();
            }
        }
    }

    public <T> T getCache(AlbumCacheType albumCacheType, String str, Type type) {
        return (T) getCache(albumCacheType, str, type, "");
    }

    public <T> T getCache(AlbumCacheType albumCacheType, String str, Type type, String str2) {
        try {
            String string = getDataContainer(albumCacheType).getString(str, str2);
            if (string != null && !string.isEmpty()) {
                return (T) this.mDeserializerGsonAdapter.fromJson(string, type);
            }
            return null;
        } catch (Exception e) {
            DefaultLogger.e("CommonAlbumSharedPreferencesCache", "Gson Deserializer failed,message:%s", ExceptionUtils.getStackTraceString(e));
            return null;
        }
    }

    public void saveCache(AlbumCacheType albumCacheType, String str, Object obj) {
        if (str == null || obj == null) {
            return;
        }
        try {
            DefaultLogger.d("CommonAlbumSharedPreferencesCache", "save cache, isSuccess:%b", Boolean.valueOf(putDataToSp(albumCacheType, str, this.mSerializerGsonAdapter.toJson(obj))));
        } catch (Exception e) {
            DefaultLogger.e("CommonAlbumSharedPreferencesCache", ExceptionUtils.getStackTraceString(e));
        }
    }

    public final boolean putDataToSp(AlbumCacheType albumCacheType, String str, String str2) {
        getDataContainer(albumCacheType).edit().putString(str, str2).apply();
        return true;
    }

    public final SharedPreferences getDataContainer(AlbumCacheType albumCacheType) {
        initSharedsForType(albumCacheType);
        return this.mShareds.get(albumCacheType.ordinal());
    }
}
