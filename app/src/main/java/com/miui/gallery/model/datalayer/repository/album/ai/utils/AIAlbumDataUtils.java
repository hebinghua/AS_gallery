package com.miui.gallery.model.datalayer.repository.album.ai.utils;

import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.FaceAlbumCover;
import com.miui.gallery.model.dto.SuggestionData;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class AIAlbumDataUtils {

    /* loaded from: classes2.dex */
    public static class FaceList extends EmptyArrayListDatas<FaceAlbumCover> {
    }

    /* loaded from: classes2.dex */
    public static class LocationList extends EmptyArrayListDatas<BaseAlbumCover> {
    }

    /* loaded from: classes2.dex */
    public static class TagList extends EmptyArrayListDatas<BaseAlbumCover> {
    }

    public static boolean isFaceEmptyList(List<? extends BaseAlbumCover> list) {
        return list != null && list.getClass().isAssignableFrom(FaceList.class);
    }

    public static boolean isLocationEmptyList(List<? extends BaseAlbumCover> list) {
        return list != null && list.getClass().isAssignableFrom(LocationList.class);
    }

    public static boolean isTagEmptyList(List<? extends BaseAlbumCover> list) {
        return list != null && list.getClass().isAssignableFrom(TagList.class);
    }

    public static FaceList getFaceDataEmptyList() {
        return new FaceList();
    }

    public static LocationList getLocationDataEmptyList() {
        return new LocationList();
    }

    public static TagList getTagDataEmptyList() {
        return new TagList();
    }

    public static List<SuggestionData> getEmptySuggestionDatas() {
        return new ArrayList(0);
    }

    /* loaded from: classes2.dex */
    public static class EmptyArrayListDatas<T> extends ArrayList<T> {
        public EmptyArrayListDatas() {
            super(0);
        }
    }
}
