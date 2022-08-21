package com.miui.gallery.provider;

import android.net.Uri;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.FileSize;
import com.miui.gallery.provider.album.config.QueryUriConfig$Album;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.trash.TrashBinItem;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class GalleryContract {
    public static final Uri AUTHORITY_URI = Uri.parse("content://com.miui.gallery.cloud.provider");
    public static final Uri HTTPS_AUTHORITY_URI = Uri.parse("https://gallery.i.mi.com");

    /* loaded from: classes2.dex */
    public interface CloudBase {
        public static final long[] MEDIA_TYPE_QUERY_SUPPORT = {268435457, 536870912, 139611605829681180L, 8589934592L, 32, FileSize.GB_COEFFICIENT, 34359738368L, 8388672, FileAppender.DEFAULT_BUFFER_SIZE, 65536, 4294967296L, 13196287016960L};
    }

    /* loaded from: classes2.dex */
    public interface CloudControl {
        public static final Uri URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("cloudControl").build();
    }

    /* loaded from: classes2.dex */
    public interface CloudUser {
        public static final Uri CLOUD_USER_URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("cloud_user").build();
    }

    /* loaded from: classes2.dex */
    public interface CloudWriteBulkNotify {
        public static final Uri CLOUD_WRITE_BULK_NOTIFY_URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("cloud_write_bulk_notify").build();
    }

    /* loaded from: classes2.dex */
    public interface DiscoveryMessage {
        public static final Uri URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("discovery_message").build();
    }

    /* loaded from: classes2.dex */
    public interface ExtendedCloud {
        public static final Uri URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("extended_cloud").build();
    }

    /* loaded from: classes2.dex */
    public interface RecentDiscoveredMedia {
        public static final Uri URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("recent_discovered_media").build();
    }

    /* loaded from: classes2.dex */
    public interface SearchResultPhoto {
        public static final Uri URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("searchResultPhoto").build();
    }

    /* loaded from: classes2.dex */
    public interface ShareAlbum {
        public static final Uri OTHER_SHARE_URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("other_share_album").build();
    }

    /* loaded from: classes2.dex */
    public interface ShareUser {
        public static final Uri SHARE_USER_URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("share_user").build();
    }

    /* loaded from: classes2.dex */
    public interface TrashBin {
        public static final Uri TRASH_BIN_URI = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("trash_bin").build();
        public static final String SERVER_TAG = TrashBinItem.SERVER_TAG;
    }

    /* loaded from: classes2.dex */
    public interface Media {
        public static final Uri URI;
        public static final Uri URI_ALL;
        public static final Uri URI_OTHER_ALBUM_MEDIA;
        public static final Uri URI_OWNER_ALBUM_DETAIL_MEDIA;
        public static final Uri URI_PICKER;
        public static final Uri URI_RECENT_MEDIA;

        static {
            Uri uri = GalleryContract.AUTHORITY_URI;
            URI = uri.buildUpon().appendPath("media").build();
            URI_OWNER_ALBUM_DETAIL_MEDIA = uri.buildUpon().appendPath("album_media").build();
            URI_OTHER_ALBUM_MEDIA = uri.buildUpon().appendPath("share_album_media").build();
            URI_ALL = uri.buildUpon().appendPath("cloud_and_share").build();
            URI_PICKER = uri.buildUpon().appendPath("pick_cloud_and_share").build();
            URI_RECENT_MEDIA = uri.buildUpon().appendPath("recent_media").build();
        }
    }

    /* loaded from: classes2.dex */
    public interface Album {
        public static final Long[] ALL_SYSTEM_ALBUM_SERVER_IDS;
        public static final Integer[] ALL_VIRTUAL_ALBUM_IDS;
        public static final Long[] ALL_VIRTUAL_ALBUM_SERVER_IDS;
        public static final String[] EXTRA_INFO_CHILDS;
        public static final Uri URI;
        public static final Uri URI_CACHE;
        public static final Uri URI_QUERY_ALL_AND_EXCEPT_DELETED;
        public static final Uri URI_QUERY_ALL_AND_EXCEPT_RUBBISH;
        public static final Uri URI_QUERY_ALL_MODE;
        public static final Uri URI_SHARE_ALL;
        public static final long[] ALL_ALBUM_ATTRIBUTES = {1, 2, 4, 8, 16, 32, 64, 128, 2048, 4096, 1280};
        public static final String[] SCREENSHOTS_AND_RECORDERS_PATH = {MIUIStorageConstants.DIRECTORY_SCREENSHOT_PATH, MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH};

        static {
            Uri uri = GalleryContract.AUTHORITY_URI;
            URI = uri.buildUpon().appendPath("album").build();
            URI_CACHE = uri.buildUpon().appendPath("album_media_cache_list").build();
            URI_QUERY_ALL_MODE = QueryUriConfig$Album.URI_ALL;
            URI_QUERY_ALL_AND_EXCEPT_DELETED = QueryUriConfig$Album.URI_ALL_EXCEPT_DELETED;
            URI_QUERY_ALL_AND_EXCEPT_RUBBISH = QueryUriConfig$Album.URI_ALL_EXCEPT_RUBBISH;
            URI_SHARE_ALL = uri.buildUpon().appendPath("owner_and_other_album").build();
            EXTRA_INFO_CHILDS = new String[]{"babyInfoJson", "sharerInfo", "peopleId", "thumbnailInfo", "description", "appKey", "sortBy", "isPublic", "publicUrl"};
            ALL_SYSTEM_ALBUM_SERVER_IDS = new Long[]{-2147483647L, -2147483644L, 1L, 2L, -2147483642L, -2147483643L, 1000L, 4L, -2147483645L};
            ALL_VIRTUAL_ALBUM_SERVER_IDS = new Long[]{1000L, -2147483642L, -2147483644L, -2147483647L, -2147483645L};
            ALL_VIRTUAL_ALBUM_IDS = new Integer[]{Integer.MAX_VALUE, 2147483644, 2147483643, 2147483642, 2147483645};
        }
    }

    /* loaded from: classes2.dex */
    public interface PeopleFace {
        public static final Uri IGNORE_PERSONS_URI;
        public static final Uri IMAGE_FACE_URI;
        public static final Uri ONE_PERSON_ITEM_URI;
        public static final Uri ONE_PERSON_URI;
        public static final Uri PEOPLE_COVER_URI;
        public static final Uri PEOPLE_FACE_COVER_URI;
        public static final Uri PEOPLE_FACE_URI;
        public static final Uri PEOPLE_SNAPSHOT_URI;
        public static final Uri PEOPLE_TAG_URI;
        public static final Uri PERSONS_ITEM_URI;
        public static final Uri PERSONS_URI;
        public static final Uri RECOMMEND_FACES_OF_ONE_PERSON_URI;

        static {
            Uri uri = GalleryContract.AUTHORITY_URI;
            PERSONS_URI = uri.buildUpon().appendPath("persons").build();
            IGNORE_PERSONS_URI = uri.buildUpon().appendPath("ignore_persons").build();
            ONE_PERSON_URI = uri.buildUpon().appendPath("person").build();
            ONE_PERSON_ITEM_URI = uri.buildUpon().appendPath("person_item").build();
            RECOMMEND_FACES_OF_ONE_PERSON_URI = uri.buildUpon().appendPath("person_recommend").build();
            PEOPLE_FACE_URI = uri.buildUpon().appendPath("peopleFace").build();
            PEOPLE_FACE_COVER_URI = uri.buildUpon().appendPath("people_face_cover").build();
            PEOPLE_COVER_URI = uri.buildUpon().appendPath("people_cover").build();
            PEOPLE_TAG_URI = uri.buildUpon().appendPath("people_tag").build();
            IMAGE_FACE_URI = uri.buildUpon().appendPath("image_face").build();
            PERSONS_ITEM_URI = uri.buildUpon().appendPath("persons_item").build();
            PEOPLE_SNAPSHOT_URI = uri.buildUpon().appendPath("people_snapshot").build();
        }
    }

    /* loaded from: classes2.dex */
    public interface Cloud extends CloudBase {
        public static final Uri CLOUD_SUBUBI;
        public static final Uri CLOUD_URI;

        static {
            Uri uri = GalleryContract.AUTHORITY_URI;
            CLOUD_URI = uri.buildUpon().appendPath("gallery_cloud").build();
            CLOUD_SUBUBI = uri.buildUpon().appendPath("cloud_owner_sububi").build();
        }
    }

    /* loaded from: classes2.dex */
    public interface ShareImage extends CloudBase {
        public static final Uri SHARE_URI;
        public static final Uri SHARE_URI_SUBUBI;

        static {
            Uri uri = GalleryContract.AUTHORITY_URI;
            SHARE_URI = uri.buildUpon().appendPath("share_image").build();
            SHARE_URI_SUBUBI = uri.buildUpon().appendPath("share_image_sububi").build();
        }
    }

    /* loaded from: classes2.dex */
    public interface Favorites {
        public static final Uri DELAY_NOTIFY_URI;
        public static final Uri SHORTCUT_FULL_URI;
        public static final Uri SHORTCUT_URI;
        public static final Uri URI;

        static {
            Uri uri = GalleryContract.AUTHORITY_URI;
            URI = uri.buildUpon().appendPath("favorites").build();
            DELAY_NOTIFY_URI = uri.buildUpon().appendPath("favorites").appendQueryParameter("delay_notify", String.valueOf(true)).build();
            Uri uri2 = GalleryContract.HTTPS_AUTHORITY_URI;
            SHORTCUT_URI = uri2.buildUpon().appendPath("favorites").build();
            SHORTCUT_FULL_URI = uri2.buildUpon().appendPath("favorites").appendQueryParameter("id", String.valueOf(2147483642)).appendQueryParameter("serverId", String.valueOf(-2147483642L)).build();
        }
    }

    /* loaded from: classes2.dex */
    public interface RecentAlbum {
        public static final Uri ALL_PHOTOS_VIEW_PAGE_URI;
        public static final Uri VIEW_PAGE_URI;

        static {
            Uri uri = GalleryContract.HTTPS_AUTHORITY_URI;
            VIEW_PAGE_URI = uri.buildUpon().appendPath("recent-album/").build();
            ALL_PHOTOS_VIEW_PAGE_URI = uri.buildUpon().appendPath("all-photos-album/").build();
        }
    }

    /* loaded from: classes2.dex */
    public interface Common {
        public static final Uri URI_ALBUM_PAGE;
        public static final Uri URI_ART_STILL;
        public static final Uri URI_CARD_ACTION;
        public static final Uri URI_CLEANER_PAGE;
        public static final Uri URI_CLOUD_GUIDE;
        public static final Uri URI_COLLAGE_FROM_PICKER;
        public static final Uri URI_COLLAGE_PAGE;
        public static final Uri URI_FILTER_SKY;
        public static final Uri URI_HYBRID_PAGE;
        public static final Uri URI_ID_PHOTO;
        public static final Uri URI_MACARONS_PAGE;
        public static final Uri URI_MAGIC_MATTING;
        public static final Uri URI_MAP_ALNBUM;
        public static final Uri URI_PEOPLE_LIST_PAGE;
        public static final Uri URI_PEOPLE_PAGE;
        public static final Uri URI_PHOTO_MOVIE;
        public static final Uri URI_PUSH_LANDING_PAGE;
        public static final Uri URI_RECOMMEND_TAB;
        public static final Uri URI_RUBBISH_PAGE;
        public static final Uri URI_SECRET_ALBUM;
        public static final Uri URI_SYNC_SWITCH;
        public static final Uri URI_TRASH_BIN;
        public static final Uri URI_VIDEO_POST;
        public static final Uri URI_VLOG;

        static {
            Uri uri = GalleryContract.HTTPS_AUTHORITY_URI;
            URI_HYBRID_PAGE = uri.buildUpon().appendPath("hybrid").build();
            URI_PEOPLE_LIST_PAGE = uri.buildUpon().appendPath("peoples").build();
            URI_PEOPLE_PAGE = uri.buildUpon().appendPath("people").build();
            URI_ALBUM_PAGE = uri.buildUpon().appendPath("album").build();
            URI_TRASH_BIN = uri.buildUpon().appendPath("trash_bin").build();
            URI_SECRET_ALBUM = uri.buildUpon().appendPath("secret_album").build();
            URI_CLOUD_GUIDE = uri.buildUpon().appendPath("cloud_guide").build();
            URI_SYNC_SWITCH = uri.buildUpon().appendPath("sync_switch").build();
            URI_COLLAGE_PAGE = uri.buildUpon().appendPath("collage").build();
            URI_MACARONS_PAGE = uri.buildUpon().appendPath("macarons").build();
            URI_VLOG = uri.buildUpon().appendPath("vlog").build();
            URI_MAGIC_MATTING = uri.buildUpon().appendPath("magic_matting").build();
            URI_ID_PHOTO = uri.buildUpon().appendPath("id_photo").build();
            URI_ART_STILL = uri.buildUpon().appendPath("art_still").build();
            URI_VIDEO_POST = uri.buildUpon().appendPath("video_post").build();
            URI_PHOTO_MOVIE = uri.buildUpon().appendPath("photo_movie").build();
            URI_CLEANER_PAGE = uri.buildUpon().appendPath("cleaner").build();
            URI_PUSH_LANDING_PAGE = uri.buildUpon().appendPath("push_landing").build();
            URI_CARD_ACTION = uri.buildUpon().appendPath("card_action").build();
            URI_COLLAGE_FROM_PICKER = uri.buildUpon().appendPath("collage_from_picker").build();
            URI_RUBBISH_PAGE = uri.buildUpon().appendPath("rubbish").build();
            URI_RECOMMEND_TAB = uri.buildUpon().appendPath("home/recommend-tab").build();
            URI_FILTER_SKY = uri.buildUpon().appendPath("filter_sky").build();
            URI_MAP_ALNBUM = uri.buildUpon().appendPath("map_album").build();
        }
    }

    /* loaded from: classes2.dex */
    public interface Search {
        public static final Uri URI_LIKELY_RESULT_PAGE;
        public static final Uri URI_LOCATION_LIST_PAGE;
        public static final Uri URI_RESULT_PAGE;
        public static final Uri URI_SEARCH_PAGE;
        public static final Uri URI_TAG_LIST_PAGE;

        static {
            Uri uri = GalleryContract.HTTPS_AUTHORITY_URI;
            URI_SEARCH_PAGE = uri.buildUpon().appendPath(MiStat.Event.SEARCH).build();
            URI_RESULT_PAGE = uri.buildUpon().appendPath("result").build();
            URI_TAG_LIST_PAGE = uri.buildUpon().appendPath("tags").build();
            URI_LOCATION_LIST_PAGE = uri.buildUpon().appendPath("locations").build();
            URI_LIKELY_RESULT_PAGE = uri.buildUpon().appendPath("likelyResults").appendQueryParameter("inFeedbackTaskMode", String.valueOf(true)).build();
        }
    }
}
