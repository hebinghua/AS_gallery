package com.miui.gallery.ui.album.common;

import android.os.Bundle;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.album.config.QueryFlagsBuilder;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class AlbumConstants {
    public static final int[] ALBUM_TAB_CANT_CHECK_ITEMS = {2147483638, 2147483639, 2147483641};

    /* loaded from: classes2.dex */
    public static class QueryScene {
        public static final long SCENE_ADD_TO_ALBUM;
        public static final long SCENE_ADD_TO_ALBUM_EXTRA_JOIN_SHARER;
        public static final long SCENE_ALBUM_DETAIL_ADD_TO_ALBUM;
        public static final long SCENE_ALBUM_DETAIL_GALLERY_WIDGET;
        public static final long SCENE_CLOUD_GUIDE_AUTO_BACKUP;
        public static final long SCENE_COLLAGE;
        public static final long SCENE_DEFAULT_PICKER;
        public static final long SCENE_MI_CLIP;
        public static final long SCENE_PHOTO_MOVIE;
        public static final long SCENE_RUBBISH_ALBUM_COVER;
        public static final long SCENE_RUBBISH_ALBUM_LIST;
        public static final long SCENE_ALBUM_TAB_PAGE = new QueryFlagsBuilder().joinOtherShareAlbums().joinAllVirtualAlbum().excludeRealScreenshotsAndRecorders().excludeUnimportantAlbum().excludeEmptySystemAlbum().excludeEmptyThirdPartyAlbum().build();
        public static final long SCENE_OTHER_ALBUM_COVER = new QueryFlagsBuilder().returnAlbumTypeByAttributes(2112).excludeHiddenAlbum().excludeEmptyThirdPartyAlbum().build();
        public static final long SCENE_OTHER_ALBUM_LIST = new QueryFlagsBuilder().returnAlbumTypeByAttributes(64).excludeHiddenAlbum().excludeRubbishAlbum().excludeEmptyThirdPartyAlbum().excludeRealScreenshotsAndRecorders().build();
        public static final long SCENE_HIDDEN_ALBUM_LIST = new QueryFlagsBuilder().joinOtherShareAlbums().returnAlbumTypeByAttributes(16).excludeRealScreenshotsAndRecorders().excludeRubbishAlbum().excludeEmptyThirdPartyAlbum().build();

        static {
            long build = new QueryFlagsBuilder().returnAlbumTypeByAttributes(2048L).excludeEmptyThirdPartyAlbum().build();
            SCENE_RUBBISH_ALBUM_COVER = build;
            SCENE_RUBBISH_ALBUM_LIST = new QueryFlagsBuilder(build).excludeEmptyAlbum().excludeEmptyThirdPartyAlbum().excludeRealScreenshotsAndRecorders().build();
            SCENE_CLOUD_GUIDE_AUTO_BACKUP = new QueryFlagsBuilder().excludeHiddenAlbum().excludeRubbishAlbum().excludeImmutableAlbum().excludeEmptyThirdPartyAlbum().joinVirtualScreenshotsRecorders().excludeRealScreenshotsAndRecorders().build();
            long build2 = new QueryFlagsBuilder().joinOtherShareAlbums().joinAllVirtualAlbum().excludeRealScreenshotsAndRecorders().excludeUnimportantAlbum().excludeEmptyAlbum().excludeEmptyThirdPartyAlbum().build();
            SCENE_DEFAULT_PICKER = build2;
            long build3 = new QueryFlagsBuilder().joinFavoritesAlbum().excludeRealScreenshotsAndRecorders().excludeRubbishAlbum().excludeHiddenAlbum().excludeImmutableAlbum().excludeRawAlbum().excludeEmptyThirdPartyAlbum().build();
            SCENE_ADD_TO_ALBUM = build3;
            SCENE_ADD_TO_ALBUM_EXTRA_JOIN_SHARER = new QueryFlagsBuilder(build3).joinOtherShareAlbums().excludeEmptyThirdPartyAlbum().build();
            SCENE_MI_CLIP = new QueryFlagsBuilder().joinOtherShareAlbums().onlyVideoMediaType().joinAllVirtualAlbum().excludeRealScreenshotsAndRecorders().excludeUnimportantAlbum().excludeEmptyAlbum().excludeEmptyThirdPartyAlbum().build();
            SCENE_PHOTO_MOVIE = new QueryFlagsBuilder().onlyImageMediaType().joinOtherShareAlbums().joinAllPhotoAlbum().excludeEmptyAlbum().joinFavoritesAlbum().joinVirtualScreenshotsRecorders().excludeRealScreenshotsAndRecorders().excludeUnimportantAlbum().excludeEmptyThirdPartyAlbum().build();
            SCENE_COLLAGE = new QueryFlagsBuilder().onlyImageMediaType().joinOtherShareAlbums().joinAllPhotoAlbum().joinFavoritesAlbum().joinVirtualScreenshotsRecorders().excludeRealScreenshotsAndRecorders().excludeUnimportantAlbum().excludeEmptyAlbum().build();
            SCENE_ALBUM_DETAIL_ADD_TO_ALBUM = build2;
            SCENE_ALBUM_DETAIL_GALLERY_WIDGET = new QueryFlagsBuilder().joinOtherShareAlbums().onlyImageMediaType().excludeRealScreenshotsAndRecorders().excludeUnimportantAlbum().excludeEmptyAlbum().joinAllPhotoAlbum().joinFavoritesAlbum().excludeRawAlbum().build();
        }
    }

    /* loaded from: classes2.dex */
    public static class MedidTypeScene {
        public static final long[] SCENE_ALBUM_TAB_PAGE;
        public static final long[] SCENE_SEARCH_PAGE;

        static {
            long[] jArr = GalleryContract.CloudBase.MEDIA_TYPE_QUERY_SUPPORT;
            SCENE_ALBUM_TAB_PAGE = jArr;
            SCENE_SEARCH_PAGE = jArr;
        }
    }

    public static boolean isAlbumCheckable(int i) {
        return Arrays.binarySearch(ALBUM_TAB_CANT_CHECK_ITEMS, i) < 0;
    }

    /* loaded from: classes2.dex */
    public static class AddToAlbumPage {
        public static Bundle secretScene(long... jArr) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("show_share_album", false);
            bundle.putBoolean("show_add_secret", false);
            bundle.putBoolean("is_from_secret", true);
            bundle.putBoolean("is_from_other_share_album", false);
            bundle.putLongArray("media_id_array", jArr);
            bundle.putInt("extra_from_type", 1019);
            return bundle;
        }
    }
}
