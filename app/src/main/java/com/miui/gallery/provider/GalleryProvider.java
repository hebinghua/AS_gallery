package com.miui.gallery.provider;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Pair;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.tracing.Trace;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.net.SyslogConstants;
import com.google.common.base.Joiner;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.dao.GalleryLiteEntityManager;
import com.miui.gallery.data.ClusteredList;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.picker.helper.PickerSqlHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.album.config.QueryFlagsBuilder;
import com.miui.gallery.provider.album.config.QueryUriConfig$Album;
import com.miui.gallery.provider.cache.AlbumCacheItem;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.provider.cache.IMediaProcessor;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.provider.cloudmanager.CloudManager;
import com.miui.gallery.provider.deprecated.GalleryCloudProvider;
import com.miui.gallery.trash.TrashBinItem;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.face.PeopleItem;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class GalleryProvider extends GalleryCloudProvider {
    public static final String[] sAlbumOperation;
    public ContentResolver mContentResolver;
    public MediaManager.InitializeListener mInitializeListener = new MediaManager.InitializeListener() { // from class: com.miui.gallery.provider.GalleryProvider.1
        @Override // com.miui.gallery.provider.cache.MediaManager.InitializeListener
        public void onProgressUpdate(MediaManager.InitializeStatus initializeStatus) {
            if (initializeStatus == MediaManager.InitializeStatus.UPDATE) {
                GalleryProvider.this.notifyChange(GalleryContract.Cloud.CLOUD_URI, null, 0L);
            }
        }
    };
    public AlbumCacheManager.InitializeListener mAlbumInitializeListener = new AlbumCacheManager.InitializeListener() { // from class: com.miui.gallery.provider.GalleryProvider.2
        @Override // com.miui.gallery.provider.cache.AlbumCacheManager.InitializeListener
        public void onProgressUpdate() {
            GalleryProvider.this.notifyChange(GalleryContract.Album.URI, null, 0L);
        }
    };

    public static boolean isSpecificUri(int i, int i2, int i3) {
        return i >= i2 && i <= i3;
    }

    public final int isBlockedByMediaManager(int i) {
        if (i == 110 || i == 170 || i == 171) {
            return -1;
        }
        switch (i) {
            case 70:
            case 71:
                return 1;
            case 72:
                return -1;
            default:
                return 0;
        }
    }

    public final boolean isNeedCheckAlbumCacheManagerInitilized(int i) {
        if (i == 51 || i == 52 || i == 92 || i == 114 || i == 130 || i == 180 || i == 200 || i == 132 || i == 133 || i == 170 || i == 171) {
            return false;
        }
        switch (i) {
            case 150:
            case 151:
            case SyslogConstants.LOG_LOCAL3 /* 152 */:
                return false;
            default:
                return true;
        }
    }

    static {
        UriMatcher uriMatcher = GalleryCloudProvider.sUriMatcher;
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "album", 72);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "album/#", 62);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "album_media_cache_list", 61);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "media", 51);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "media/#", 52);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "album_media", 73);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_album_media/#", 113);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "recent_media", 53);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "persons", 90);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "ignore_persons", 97);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "person", 91);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "gallery_cloud", 70);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "gallery_cloud/#", 71);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloud_owner_sububi/#", 74);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_image", 110);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_image/#", 111);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_image_sububi/#", 115);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "person_recommend", 94);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "peopleFace", 92);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloud_and_share", 50);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "owner_and_other_album", 112);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "other_share_album", 114);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloud_write_bulk_notify", 75);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "person_item", 93);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_user", BaiduSceneResult.VISA);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_user/#", BaiduSceneResult.INVOICE);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloud_user", BaiduSceneResult.VARIOUS_TICKETS);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloud_user/#", BaiduSceneResult.EXPRESS_ORDER);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "people_face_cover", 95);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "discovery_message", 150);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "discovery_message/#", 151);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "recent_discovered_media", SyslogConstants.LOG_LOCAL3);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloudControl", 170);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloudControl/#", 171);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "people_cover", 96);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "image_face", 98);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "people_tag", 99);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "favorites", nexClip.kClip_Rotate_180);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "extended_cloud", 76);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "pick_cloud_and_share", 87);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "persons_item", 100);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "people_snapshot", 101);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "trash_bin", 200);
        sAlbumOperation = new String[]{"rename_album", "set_album_attributes", "delete_album", "change_album_sort_position", "replace_album_cover", "create_album"};
    }

    @Override // com.miui.gallery.provider.deprecated.GalleryCloudProvider, android.content.ContentProvider
    public boolean onCreate() {
        Trace.beginSection("ProviderCreate");
        super.onCreate();
        this.mContentResolver = new ContentResolver();
        MediaManager mediaManager = MediaManager.getInstance();
        this.mMediaManager = mediaManager;
        mediaManager.addInitializeListener(this.mInitializeListener);
        this.mMediaManager.load(GalleryCloudProvider.sDBHelper);
        Trace.endSection();
        return true;
    }

    public <T extends CacheItem, R> List<R> queryCachedItem(Uri uri, String str, String[] strArr, String str2, IMediaProcessor<T, R> iMediaProcessor) {
        int match = GalleryCloudProvider.sUriMatcher.match(uri);
        if (match != 50 && match != 51 && match != 53) {
            if (match == 61) {
                return queryCachedAlbum(uri, str, strArr, str2, iMediaProcessor);
            }
            if (match != 73 && match != 113) {
                throw new IllegalArgumentException("Unsupported uri: " + uri);
            }
        }
        return queryCachedMedia(uri, str, strArr, str2, iMediaProcessor);
    }

    public <R> List<R> queryCachedAlbum(Uri uri, String str, String[] strArr, String str2, IMediaProcessor<AlbumCacheItem, R> iMediaProcessor) {
        if (GalleryCloudProvider.sUriMatcher.match(uri) == 61) {
            return AlbumCacheManager.getInstance().query(str, strArr, str2, UriUtil.getGroupBy(uri), UriUtil.getLimit(uri), (Bundle) null, iMediaProcessor);
        }
        throw new IllegalArgumentException("Unsupported uri: " + uri);
    }

    public <R> List<R> queryCachedMedia(Uri uri, String str, String[] strArr, String str2, IMediaProcessor<MediaCacheItem, R> iMediaProcessor) {
        String str3 = str;
        int match = GalleryCloudProvider.sUriMatcher.match(uri);
        List<R> list = null;
        if (match == 50 || match == 51) {
            if (uri.getBooleanQueryParameter("require_full_load", false)) {
                this.mMediaManager.isInitializedLock();
            } else {
                this.mMediaManager.ensureMinimumPartDone();
            }
            Bundle repackQueryOptions = repackQueryOptions(uri);
            boolean booleanQueryParameter = uri.getBooleanQueryParameter("remove_duplicate_items", false);
            if (uri.getBooleanQueryParameter("remove_processing_items", false)) {
                str3 = buildNonProcessingSelection(str3);
            }
            String str4 = str3;
            List<R> query = this.mMediaManager.query(str4, strArr, str2, booleanQueryParameter ? "sha1" : null, UriUtil.getLimit(uri), repackQueryOptions, iMediaProcessor);
            if (match == 50) {
                list = ShareMediaManager.getInstance().query(str4, strArr, str2, "sha1", (String) null, (Bundle) null, iMediaProcessor);
            }
            if (query == null) {
                return list != null ? list : Collections.emptyList();
            } else if (list == null) {
                return query;
            } else {
                if ((query instanceof ClusteredList) || (list instanceof ClusteredList)) {
                    throw new IllegalStateException("Generate headers for URI_MEDIA_ALL is unsupported");
                }
                return (List) Stream.concat(query.stream(), list.stream()).collect(Collectors.toList());
            }
        } else if (match == 53) {
            this.mMediaManager.ensureMinimumPartDone();
            boolean booleanQueryParameter2 = uri.getBooleanQueryParameter("remove_duplicate_items", false);
            boolean booleanQueryParameter3 = uri.getBooleanQueryParameter("generate_headers", false);
            boolean booleanQueryParameter4 = uri.getBooleanQueryParameter("remove_processing_items", false);
            boolean booleanQueryParameter5 = uri.getBooleanQueryParameter("param_show_headers", true);
            Bundle bundle = new Bundle();
            bundle.putBoolean("extra_generate_header", booleanQueryParameter3);
            bundle.putInt("extra_media_group_by", 8);
            bundle.putBoolean("extra_show_headers", booleanQueryParameter5);
            return this.mMediaManager.query(DatabaseUtils.concatenateWhere(DatabaseUtils.concatenateWhere(generateRecentMediaIdsSelection(), generateRecentMediaCommonSelection()), booleanQueryParameter4 ? buildNonProcessingSelection(str3) : str3), strArr, str2, booleanQueryParameter2 ? "sha1" : null, (String) null, bundle, iMediaProcessor);
        } else if (match == 73) {
            Bundle repackQueryOptions2 = repackQueryOptions(uri);
            boolean booleanQueryParameter6 = uri.getBooleanQueryParameter("remove_duplicate_items", false);
            return this.mMediaManager.query(uri.getBooleanQueryParameter("remove_processing_items", false) ? buildNonProcessingSelection(str3) : str3, strArr, str2, booleanQueryParameter6 ? "sha1" : null, (String) null, repackQueryOptions2, iMediaProcessor);
        } else if (match == 113) {
            return ShareMediaManager.getInstance().query(parseSelection(str3, "localGroupId = %s", new String[]{String.valueOf(ShareAlbumHelper.getOriginalAlbumId(ContentUris.parseId(uri)))}), strArr, str2, uri.getBooleanQueryParameter("remove_duplicate_items", false) ? "sha1" : null, (String) null, repackQueryOptions(uri), iMediaProcessor);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }
    }

    public List<Uri> getNotifyUris(Uri uri) {
        int match = GalleryCloudProvider.sUriMatcher.match(uri);
        if (match != 50) {
            if (match == 53) {
                return Collections.singletonList(GalleryContract.Media.URI_RECENT_MEDIA);
            }
            if (match == 73) {
                return Collections.singletonList(GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA);
            }
            if (match == 113) {
                return Collections.singletonList(GalleryContract.Media.URI_OTHER_ALBUM_MEDIA);
            }
            return Collections.singletonList(GalleryContract.Media.URI);
        }
        return Collections.singletonList(GalleryContract.Media.URI_ALL);
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, CancellationSignal cancellationSignal) {
        int i;
        Cursor query;
        int i2;
        Cursor query2;
        long parseLong;
        String str3;
        String str4 = str;
        SupportSQLiteDatabase readableDatabase = GalleryCloudProvider.sDBHelper.getReadableDatabase();
        int match = GalleryCloudProvider.sUriMatcher.match(uri);
        if (match == 50 || match == 51) {
            if (uri.getBooleanQueryParameter("require_full_load", false)) {
                this.mMediaManager.isInitializedLock();
            } else {
                this.mMediaManager.ensureMinimumPartDone();
            }
            Bundle repackQueryOptions = repackQueryOptions(uri);
            boolean booleanQueryParameter = uri.getBooleanQueryParameter("remove_duplicate_items", false);
            if (uri.getBooleanQueryParameter("remove_processing_items", false)) {
                str4 = buildNonProcessingSelection(str4);
            }
            Cursor query3 = this.mMediaManager.query(strArr, str4, strArr2, str2, booleanQueryParameter ? "sha1" : null, UriUtil.getLimit(uri), repackQueryOptions);
            if (query3 != null) {
                registerNotifyUri(query3, match);
            }
            if (match != 50) {
                return query3;
            }
            Cursor query4 = ShareMediaManager.getInstance().query(strArr, str4, strArr2, str2, "sha1", (String) null, (Bundle) null);
            if (query4 != null) {
                registerNotifyUri(query4, match);
            }
            return new MergeCursor(new Cursor[]{query3, query4});
        }
        if (match == 53) {
            i = match;
            this.mMediaManager.ensureMinimumPartDone();
            boolean booleanQueryParameter2 = uri.getBooleanQueryParameter("remove_duplicate_items", false);
            boolean booleanQueryParameter3 = uri.getBooleanQueryParameter("generate_headers", false);
            boolean booleanQueryParameter4 = uri.getBooleanQueryParameter("remove_processing_items", false);
            boolean booleanQueryParameter5 = uri.getBooleanQueryParameter("param_show_headers", true);
            Bundle bundle = new Bundle();
            bundle.putBoolean("extra_generate_header", booleanQueryParameter3);
            bundle.putInt("extra_media_group_by", 8);
            bundle.putBoolean("extra_show_headers", booleanQueryParameter5);
            query = this.mMediaManager.query(strArr, DatabaseUtils.concatenateWhere(DatabaseUtils.concatenateWhere(generateRecentMediaIdsSelection(), generateRecentMediaCommonSelection()), booleanQueryParameter4 ? buildNonProcessingSelection(str4) : str4), strArr2, str2, booleanQueryParameter2 ? "sha1" : null, (String) null, bundle);
            Bundle extras = query.getExtras();
            if (extras == null || extras == Bundle.EMPTY) {
                extras = new Bundle();
                query.setExtras(extras);
            }
            extras.putBoolean("is_full_load", this.mMediaManager.isInitialized());
        } else {
            int i3 = 61;
            if (match != 61) {
                if (match == 76) {
                    query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "extended_cloud"));
                } else if (match == 87) {
                    query = GalleryCloudProvider.rawQuery(PickerSqlHelper.buildPickerResultQuery(strArr, str4, strArr2, UriUtil.getGroupBy(uri), str2), null);
                } else if (match == 130) {
                    query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "shareUser"));
                } else if (match == 132) {
                    query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "cloudUser"));
                } else if (match != 180) {
                    if (match == 200) {
                        String str5 = AccountCache.getAccount() == null ? null : AccountCache.getAccount().name;
                        if (TextUtils.isEmpty(str5)) {
                            str3 = "albumLocalId != -1000 AND cloudServerId IS NULL ";
                        } else {
                            str3 = "(albumLocalId != -1000 AND cloudServerId IS NULL )  OR creatorId IS NULL  OR creatorId = '" + str5 + "'";
                        }
                        i2 = match;
                        query = GalleryEntityManager.getInstance().rawQuery(TrashBinItem.class, strArr, DatabaseUtils.concatenateWhere(str4, str3), strArr2, UriUtil.getGroupBy(uri), UriUtil.getHaving(uri), str2, UriUtil.getLimit(uri));
                    } else if (match == 170) {
                        query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "cloudControl"));
                    } else if (match != 171) {
                        switch (match) {
                            case 70:
                                query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "cloud"));
                                break;
                            case 71:
                                i2 = match;
                                query = readableDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(strArr).selection(parseSelection(str4, "_id = %s", new String[]{String.valueOf(ContentUris.parseId(uri))}), strArr2).orderBy(str2).create());
                                break;
                            case 72:
                                i3 = 61;
                                break;
                            case 73:
                                i2 = match;
                                query = this.mMediaManager.query(strArr, uri.getBooleanQueryParameter("remove_processing_items", false) ? buildNonProcessingSelection(str4) : str4, strArr2, str2, uri.getBooleanQueryParameter("remove_duplicate_items", false) ? "sha1" : null, (String) null, repackQueryOptions(uri));
                                break;
                            case 74:
                                uri.getLastPathSegment();
                                i = match;
                                query = null;
                                break;
                            default:
                                switch (match) {
                                    case 90:
                                        query = GalleryCloudProvider.rawQuery(addLimitSelectionIfNeed(FaceManager.buildPersonsQuery(), UriUtil.getLimit(uri)), null);
                                        break;
                                    case 91:
                                        query = FaceManager.queryOnePersonAlbum(readableDatabase, strArr, str, str2, strArr2, uri.getBooleanQueryParameter("generate_headers", false));
                                        break;
                                    case 92:
                                        query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "peopleFace"));
                                        break;
                                    case 93:
                                        i2 = match;
                                        query = GalleryCloudProvider.rawQuery(FaceManager.buildOnePersonItemQuery(strArr, strArr2), null);
                                        break;
                                    case 94:
                                        i2 = match;
                                        query = GalleryCloudProvider.rawQuery(FaceManager.buildRecommendFacesOfOnePersonQuery(strArr, str2, strArr2), null);
                                        break;
                                    case 95:
                                        i2 = match;
                                        String limit = UriUtil.getLimit(uri);
                                        if (TextUtils.isEmpty(limit)) {
                                            query = AlbumManager.queryFaceAlbumCover(readableDatabase);
                                            break;
                                        } else {
                                            query = AlbumManager.queryFaceAlbumCover(readableDatabase, Integer.parseInt(limit));
                                            break;
                                        }
                                    case 96:
                                        i2 = match;
                                        query = GalleryCloudProvider.rawQuery(FaceManager.buildPeopleCoverQuery(strArr, uri.getQueryParameter("serverId"), uri.getQueryParameter(j.c)), null);
                                        break;
                                    case 97:
                                        i2 = match;
                                        query = GalleryCloudProvider.rawQuery(addLimitSelectionIfNeed(FaceManager.buildIgnorePersonsQuery(), UriUtil.getLimit(uri)), null);
                                        break;
                                    case 98:
                                        i2 = match;
                                        query = GalleryCloudProvider.rawQuery(FaceManager.buildImageFaceQuery(strArr, uri.getQueryParameter("serverId"), uri.getQueryParameter("image_server_id")), null);
                                        break;
                                    case 99:
                                        i2 = match;
                                        query = GalleryCloudProvider.rawQuery(FaceManager.buildPeopleTagQuery(), null);
                                        break;
                                    case 100:
                                        i2 = match;
                                        query = GalleryCloudProvider.rawQuery(FaceManager.buildPersonsItemQuery(strArr, str4, str2, strArr2), null);
                                        break;
                                    case 101:
                                        i2 = match;
                                        query = GalleryLiteEntityManager.getInstance().rawQuery(PeopleItem.class, strArr, str, strArr2, UriUtil.getGroupBy(uri), UriUtil.getHaving(uri), str2, UriUtil.getLimit(uri));
                                        break;
                                    default:
                                        switch (match) {
                                            case 110:
                                                query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "shareImage"));
                                                break;
                                            case 111:
                                                i2 = match;
                                                query = readableDatabase.query(SupportSQLiteQueryBuilder.builder("shareImage").columns(strArr).selection(parseSelection(str4, "_id = %s", new String[]{uri.getLastPathSegment()}), strArr2).orderBy(str2).create());
                                                break;
                                            case 112:
                                                i2 = match;
                                                query = AlbumManager.queryShareAll(getContext(), readableDatabase, strArr, str, strArr2, str2);
                                                break;
                                            case 113:
                                                String valueOf = String.valueOf(ShareAlbumHelper.getOriginalAlbumId(ContentUris.parseId(uri)));
                                                boolean booleanQueryParameter6 = uri.getBooleanQueryParameter("remove_duplicate_items", false);
                                                Bundle repackQueryOptions2 = repackQueryOptions(uri);
                                                ShareMediaManager shareMediaManager = ShareMediaManager.getInstance();
                                                String parseSelection = parseSelection(str4, "localGroupId = %s", new String[]{valueOf});
                                                String str6 = booleanQueryParameter6 ? "sha1" : null;
                                                i2 = match;
                                                query = shareMediaManager.query(strArr, parseSelection, strArr2, str2, str6, (String) null, repackQueryOptions2);
                                                break;
                                            case 114:
                                                query = readableDatabase.query(SupportSQLiteQueryBuilder.builder("shareAlbum").columns(strArr).selection(str4, strArr2).orderBy(str2).create());
                                                break;
                                            case 115:
                                                uri.getLastPathSegment();
                                                i = match;
                                                query = null;
                                                break;
                                            default:
                                                switch (match) {
                                                    case 150:
                                                        query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "discoveryMessage"));
                                                        break;
                                                    case 151:
                                                        query = readableDatabase.query(SupportSQLiteQueryBuilder.builder("discoveryMessage").columns(strArr).selection(parseSelection(str4, "_id = %s", new String[]{uri.getLastPathSegment()}), strArr2).orderBy(str2).create());
                                                        break;
                                                    case SyslogConstants.LOG_LOCAL3 /* 152 */:
                                                        query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "recentDiscoveredMedia"));
                                                        break;
                                                    default:
                                                        query = super.query(uri, strArr, str, strArr2, str2);
                                                        break;
                                                }
                                        }
                                }
                        }
                    } else {
                        query = readableDatabase.query(SupportSQLiteQueryBuilder.builder("cloudControl").columns(strArr).selection(parseSelection(str4, "_id = %s", new String[]{String.valueOf(ContentUris.parseId(uri))}), strArr2).orderBy(str2).create());
                    }
                    i = i2;
                } else {
                    query = readableDatabase.query(buildSQLiteQuery(uri, strArr, str, strArr2, str2, "favorites"));
                }
                i = match;
            }
            query = null;
            boolean z = match == i3;
            if (QueryUriConfig$Album.isHaveAlbumQueryParam(uri)) {
                QueryFlagsBuilder queryFlagsBuilder = new QueryFlagsBuilder();
                String queryParameter = uri.getQueryParameter("query_flags");
                if (queryParameter == null) {
                    if (uri.getBooleanQueryParameter("param_join_all_virtual_album", false)) {
                        queryFlagsBuilder.joinAllVirtualAlbum();
                    } else {
                        if (uri.getBooleanQueryParameter("param_join_virtual_video", false)) {
                            queryFlagsBuilder.joinVideoAlbum();
                        }
                        if (uri.getBooleanQueryParameter("param_join_virtual_all_photos", false)) {
                            queryFlagsBuilder.joinAllPhotoAlbum();
                        }
                        if (uri.getBooleanQueryParameter("param_join_virtual_favorites", false)) {
                            queryFlagsBuilder.joinFavoritesAlbum();
                        }
                        if (uri.getBooleanQueryParameter("param_join_virtual_screenshots_and_recorders", false)) {
                            queryFlagsBuilder.joinVirtualScreenshotsRecorders();
                        }
                    }
                    if (uri.getBooleanQueryParameter("param_exclude_real_screenshots_and_recorders", false)) {
                        queryFlagsBuilder.excludeRealScreenshotsAndRecorders();
                    }
                    if (uri.getBooleanQueryParameter("param_exclude_system", false)) {
                        queryFlagsBuilder.excludeSystemAlbum();
                    }
                    if (uri.getBooleanQueryParameter("join_share", false)) {
                        queryFlagsBuilder.joinOtherShareAlbums();
                    }
                    if (uri.getBooleanQueryParameter("param_exclude_empty_album", false)) {
                        queryFlagsBuilder.excludeEmptyAlbum();
                    }
                    if (uri.getBooleanQueryParameter("param_exclude_hidden_album", false)) {
                        queryFlagsBuilder.excludeHiddenAlbum();
                    }
                    if (uri.getBooleanQueryParameter("param_exclude_rubbish_album", false)) {
                        queryFlagsBuilder.excludeRubbishAlbum();
                    }
                    if (uri.getBooleanQueryParameter("param_exclude_other_album", false)) {
                        queryFlagsBuilder.excludeOtherAlbum();
                    }
                    if (uri.getBooleanQueryParameter("param_query_all", false)) {
                        queryFlagsBuilder.queryAll();
                    }
                    if (uri.getBooleanQueryParameter("param_query_only_by_image_media_type", false)) {
                        queryFlagsBuilder.onlyImageMediaType();
                    }
                    if (uri.getBooleanQueryParameter("param_query_only_by_video_media_type", false)) {
                        queryFlagsBuilder.onlyVideoMediaType();
                    }
                    if (uri.getBooleanQueryParameter("param_exclude_normal_albums", false)) {
                        queryFlagsBuilder.excludeNormalAlbums();
                    }
                    parseLong = queryFlagsBuilder.build();
                } else {
                    parseLong = Long.parseLong(queryParameter);
                }
                if (z) {
                    if (AlbumCacheManager.getInstance().isInitializedLock()) {
                        Bundle bundle2 = new Bundle(1);
                        bundle2.putLong("query_flags", parseLong);
                        i2 = match;
                        query = AlbumCacheManager.getInstance().query(strArr, str, strArr2, str2, UriUtil.getGroupBy(uri), UriUtil.getLimit(uri), bundle2);
                    } else {
                        i2 = match;
                    }
                } else {
                    i2 = match;
                    query = AlbumManager.query(readableDatabase, strArr, str, strArr2, UriUtil.getGroupBy(uri), UriUtil.getHaving(uri), str2, UriUtil.getLimit(uri), UriUtil.getDistinct(uri), parseLong, cancellationSignal);
                }
            } else {
                i2 = match;
                if (z) {
                    query2 = AlbumCacheManager.getInstance().query(strArr, str, strArr2, str2, UriUtil.getGroupBy(uri), UriUtil.getLimit(uri), (Bundle) null);
                } else {
                    boolean distinct = UriUtil.getDistinct(uri);
                    SupportSQLiteQueryBuilder limit2 = SupportSQLiteQueryBuilder.builder("album").columns(strArr).selection(str4, strArr2).groupBy(UriUtil.getGroupBy(uri)).having(UriUtil.getHaving(uri)).orderBy(str2).limit(UriUtil.getLimit(uri));
                    if (distinct) {
                        limit2.distinct();
                    }
                    query2 = readableDatabase.query(limit2.create());
                }
                query = query2;
            }
            i = i2;
        }
        if (query != null) {
            registerNotifyUri(query, i);
        }
        return query;
    }

    public final Bundle repackQueryOptions(Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("extra_generate_header", uri.getBooleanQueryParameter("generate_headers", false));
        bundle.putBoolean("extra_show_headers", uri.getBooleanQueryParameter("param_show_headers", true));
        String queryParameter = uri.getQueryParameter("media_group_by");
        int parseInt = (TextUtils.isEmpty(queryParameter) || !TextUtils.isDigitsOnly(queryParameter)) ? 1 : Integer.parseInt(queryParameter);
        boolean booleanQueryParameter = uri.getBooleanQueryParameter("extra_timeline_only_show_valid_location", true);
        bundle.putInt("extra_media_group_by", parseInt);
        bundle.putBoolean("extra_timeline_only_show_valid_location", booleanQueryParameter);
        return bundle;
    }

    public final SupportSQLiteQuery buildSQLiteQuery(Uri uri, String[] strArr, String str, String[] strArr2, String str2, String str3) {
        SupportSQLiteQueryBuilder limit = SupportSQLiteQueryBuilder.builder(str3).columns(strArr).selection(str, strArr2).groupBy(UriUtil.getGroupBy(uri)).having(UriUtil.getHaving(uri)).orderBy(str2).limit(UriUtil.getLimit(uri));
        if (UriUtil.getDistinct(uri)) {
            limit.distinct();
        }
        return limit.create();
    }

    @Override // com.miui.gallery.provider.deprecated.GalleryCloudProvider, android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return query(uri, strArr, str, strArr2, str2, null);
    }

    public final String addLimitSelectionIfNeed(String str, String str2) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        try {
            acquire.append(str);
            if (!TextUtils.isEmpty(str2) && Integer.parseInt(str2) > 0) {
                acquire.append(" ");
                acquire.append("limit");
                acquire.append(" ");
                acquire.append(str2);
            }
            return acquire.toString();
        } finally {
            Pools.getStringBuilderPool().release(acquire);
        }
    }

    public final String buildNonProcessingSelection(String str) {
        List<String> queryProcessingMediaPaths = ProcessingMediaManager.queryProcessingMediaPaths();
        if (BaseMiscUtil.isValid(queryProcessingMediaPaths)) {
            StringBuilder sb = new StringBuilder("localFile NOT IN (");
            for (int i = 0; i < queryProcessingMediaPaths.size(); i++) {
                if (!TextUtils.isEmpty(queryProcessingMediaPaths.get(i))) {
                    DatabaseUtils.appendEscapedSQLString(sb, queryProcessingMediaPaths.get(i));
                    if (i != queryProcessingMediaPaths.size() - 1) {
                        sb.append(", ");
                    }
                }
            }
            sb.append(")");
            return DatabaseUtils.concatenateWhere(str, sb.toString());
        }
        return str;
    }

    public final String parseSelection(String str, String str2, String[] strArr) {
        String str3;
        String format = String.format(str2, strArr);
        StringBuilder sb = new StringBuilder();
        sb.append(format);
        if (!TextUtils.isEmpty(str)) {
            str3 = " AND (" + str + ")";
        } else {
            str3 = "";
        }
        sb.append(str3);
        return sb.toString();
    }

    @Override // com.miui.gallery.provider.deprecated.GalleryCloudProvider, android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        long doInsertWithNoNotify;
        int match = GalleryCloudProvider.sUriMatcher.match(uri);
        int isBlockedByMediaManager = isBlockedByMediaManager(match);
        if (isBlockedByMediaManager != 0 || this.mMediaManager.isInitializedLock()) {
            if (isNeedCheckAlbumCacheManagerInitilized(match) && !AlbumCacheManager.getInstance().isInitializedLock()) {
                return null;
            }
            SupportSQLiteDatabase writableDatabase = GalleryCloudProvider.sDBHelper.getWritableDatabase();
            switch (match) {
                case 70:
                case 72:
                case 75:
                case 92:
                case 110:
                    doInsertWithNoNotify = doInsertWithNoNotify(uri, match, contentValues, 1 == isBlockedByMediaManager && !this.mMediaManager.isInitialized(), false, null);
                    break;
                case BaiduSceneResult.VISA /* 130 */:
                    doInsertWithNoNotify = writableDatabase.insert("shareUser", 0, contentValues);
                    break;
                case BaiduSceneResult.VARIOUS_TICKETS /* 132 */:
                    doInsertWithNoNotify = writableDatabase.insert("cloudUser", 0, contentValues);
                    break;
                case 150:
                    doInsertWithNoNotify = writableDatabase.insert("discoveryMessage", 0, contentValues);
                    break;
                case SyslogConstants.LOG_LOCAL3 /* 152 */:
                    doInsertWithNoNotify = writableDatabase.insert("recentDiscoveredMedia", 5, contentValues);
                    break;
                case 170:
                    doInsertWithNoNotify = writableDatabase.insert("cloudControl", 5, contentValues);
                    break;
                case nexClip.kClip_Rotate_180 /* 180 */:
                    doInsertWithNoNotify = writableDatabase.insert("favorites", 0, contentValues);
                    if (doInsertWithNoNotify > 0 && contentValues.containsKey("cloud_id") && contentValues.containsKey("isFavorite") && contentValues.getAsInteger("isFavorite").intValue() > 0) {
                        this.mMediaManager.insertToFavorites(contentValues.getAsLong("cloud_id"));
                        break;
                    }
                    break;
                default:
                    return super.insert(uri, contentValues);
            }
            if (doInsertWithNoNotify != -1) {
                notifyChange(uri, null, parseSyncReason(uri, contentValues));
            }
            return ContentUris.withAppendedId(uri, doInsertWithNoNotify);
        }
        return null;
    }

    public final long doInsertWithNoNotify(Uri uri, int i, ContentValues contentValues, boolean z, boolean z2, List<Pair<Long, ContentValues>> list) {
        long onPreInsert;
        SupportSQLiteDatabase writableDatabase = GalleryCloudProvider.sDBHelper.getWritableDatabase();
        if (i != 70 && i != 72 && i != 75) {
            if (i == 92) {
                return writableDatabase.insert("peopleFace", 0, appendValuesForCloud(contentValues, true));
            }
            if (i != 110) {
                if (i == 152) {
                    return writableDatabase.insert("recentDiscoveredMedia", 5, contentValues);
                }
                return -1L;
            }
            long insert = writableDatabase.insert("shareImage", 0, appendValuesForCloud(contentValues, true));
            ShareMediaManager.getInstance().insert(insert, contentValues);
            return insert;
        }
        boolean z3 = i == 72;
        if (z3) {
            AlbumDataHelper.fixValueForContentValues(contentValues);
        }
        writableDatabase.beginTransactionNonExclusive();
        String str = z3 ? "album" : "cloud";
        if (!z2) {
            try {
                onPreInsert = onPreInsert(writableDatabase, str, contentValues);
            } finally {
                writableDatabase.endTransaction();
            }
        } else {
            onPreInsert = -1;
        }
        if (onPreInsert == -1) {
            onPreInsert = writableDatabase.insert(str, 0, z3 ? contentValues : appendValuesForCloud(contentValues, true));
            if (onPreInsert != -1) {
                if (z3) {
                    AlbumCacheManager.getInstance().insert(onPreInsert, contentValues);
                } else if (list != null) {
                    list.add(new Pair<>(Long.valueOf(onPreInsert), contentValues));
                } else if (z) {
                    this.mMediaManager.insertByPass(onPreInsert, contentValues);
                } else {
                    this.mMediaManager.insert(onPreInsert, contentValues);
                }
            }
        }
        writableDatabase.setTransactionSuccessful();
        return onPreInsert;
    }

    @Override // com.miui.gallery.provider.deprecated.GalleryCloudProvider, android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        int match = GalleryCloudProvider.sUriMatcher.match(uri);
        int isBlockedByMediaManager = isBlockedByMediaManager(match);
        if ((isBlockedByMediaManager == 0 || 1 == isBlockedByMediaManager) && !this.mMediaManager.isInitializedLock()) {
            return 0;
        }
        if (isNeedCheckAlbumCacheManagerInitilized(match) && !AlbumCacheManager.getInstance().isInitializedLock()) {
            return 0;
        }
        SupportSQLiteDatabase writableDatabase = GalleryCloudProvider.sDBHelper.getWritableDatabase();
        int i = -1;
        switch (match) {
            case 51:
            case 52:
                break;
            case 70:
            case 75:
                this.mMediaManager.delete(genIDSelection(str, strArr, false), null);
                i = writableDatabase.delete("cloud", str, strArr);
                break;
            case 72:
                AlbumCacheManager.getInstance().delete(genAlbumIDSelection(str, strArr, false), null);
                i = writableDatabase.delete("album", str, strArr);
                break;
            case 92:
                i = writableDatabase.delete("peopleFace", str, strArr);
                break;
            case 110:
                i = writableDatabase.delete("shareImage", str, strArr);
                String genIDSelection = genIDSelection(str, strArr, true);
                if (genIDSelection == null) {
                    DefaultLogger.e("GalleryProvider", "delete shareImage mediaManager cache error:selection[%s],args:[%s]", strArr, strArr);
                    break;
                } else {
                    ShareMediaManager.getInstance().delete(genIDSelection, null);
                    break;
                }
            case BaiduSceneResult.VISA /* 130 */:
                i = writableDatabase.delete("shareUser", str, strArr);
                break;
            case BaiduSceneResult.VARIOUS_TICKETS /* 132 */:
                i = writableDatabase.delete("cloudUser", str, strArr);
                break;
            case 150:
                i = writableDatabase.delete("discoveryMessage", str, strArr);
                break;
            case SyslogConstants.LOG_LOCAL3 /* 152 */:
                i = RecentDiscoveryMediaManager.delete(writableDatabase, str, strArr);
                break;
            case 170:
                i = writableDatabase.delete("cloudControl", str, strArr);
                break;
            case nexClip.kClip_Rotate_180 /* 180 */:
                List<Long> cloudIdListFromFavorites = getCloudIdListFromFavorites(str, strArr, true);
                i = writableDatabase.delete("favorites", str, strArr);
                if (i > 0 && BaseMiscUtil.isValid(cloudIdListFromFavorites)) {
                    for (Long l : cloudIdListFromFavorites) {
                        this.mMediaManager.removeFromFavorites(l);
                    }
                    break;
                }
                break;
            default:
                return super.delete(uri, str, strArr);
        }
        if (i > 0) {
            notifyChange(uri, null, parseSyncReason(uri, null));
        }
        return i;
    }

    @Override // com.miui.gallery.provider.deprecated.GalleryCloudProvider, android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        int update;
        int update2;
        int match = GalleryCloudProvider.sUriMatcher.match(uri);
        int isBlockedByMediaManager = isBlockedByMediaManager(match);
        boolean z = true;
        if ((isBlockedByMediaManager == 0 || 1 == isBlockedByMediaManager) && !this.mMediaManager.isInitializedLock()) {
            return 0;
        }
        if (isNeedCheckAlbumCacheManagerInitilized(match) && !AlbumCacheManager.getInstance().isInitializedLock()) {
            return 0;
        }
        SupportSQLiteDatabase writableDatabase = GalleryCloudProvider.sDBHelper.getWritableDatabase();
        if (match == 62) {
            Set<String> keySet = contentValues.keySet();
            if (keySet.size() != 1 || !keySet.contains("sync_status")) {
                throw new IllegalArgumentException("only support key: sync_status");
            }
            update = AlbumCacheManager.getInstance().update(genAlbumIDSelection(str, strArr, false), null, contentValues);
        } else {
            if (match != 75) {
                if (match != 92) {
                    if (match == 110) {
                        update2 = writableDatabase.update("shareImage", 0, appendValuesForCloud(contentValues, false), str, strArr);
                        String genIDSelection = genIDSelection(str, strArr, true);
                        if (genIDSelection == null) {
                            DefaultLogger.e("GalleryProvider", "delete shareImage mediaManager cache error:selection[%s],args:[%s]", strArr, strArr);
                        } else {
                            ShareMediaManager.getInstance().update(genIDSelection, null, contentValues);
                        }
                    } else if (match == 114) {
                        update = writableDatabase.update("shareAlbum", 0, contentValues, str, strArr);
                    } else if (match == 180) {
                        update2 = writableDatabase.update("favorites", 0, contentValues, str, strArr);
                        if (update2 > 0 && contentValues.containsKey("isFavorite")) {
                            if (contentValues.getAsInteger("isFavorite").intValue() <= 0) {
                                z = false;
                            }
                            for (Long l : getCloudIdListFromFavorites(str, strArr, false)) {
                                if (z) {
                                    this.mMediaManager.insertToFavorites(l);
                                } else {
                                    this.mMediaManager.removeFromFavorites(l);
                                }
                            }
                        }
                    } else if (match == 200) {
                        update = GalleryEntityManager.getInstance().update(TrashBinItem.class, contentValues, str, strArr);
                    } else if (match == 150 || match == 151) {
                        update = writableDatabase.update("discoveryMessage", 0, contentValues, str, strArr);
                    } else if (match != 170 && match != 171) {
                        switch (match) {
                            case 70:
                                break;
                            case 71:
                                Set<String> keySet2 = contentValues.keySet();
                                if (keySet2.size() != 1 || !keySet2.contains("sync_status")) {
                                    throw new IllegalArgumentException("only support key: sync_status");
                                }
                                update = this.mMediaManager.update(genIDSelection(str, strArr, false), null, contentValues);
                                break;
                                break;
                            case 72:
                                update = internalUpdateDbAlbumData(writableDatabase, contentValues, str, strArr);
                                break;
                            default:
                                switch (match) {
                                    case BaiduSceneResult.VISA /* 130 */:
                                    case BaiduSceneResult.INVOICE /* 131 */:
                                        update = writableDatabase.update("shareUser", 0, contentValues, str, strArr);
                                        break;
                                    case BaiduSceneResult.VARIOUS_TICKETS /* 132 */:
                                    case BaiduSceneResult.EXPRESS_ORDER /* 133 */:
                                        update = writableDatabase.update("cloudUser", 0, contentValues, str, strArr);
                                        break;
                                    default:
                                        update = super.update(uri, contentValues, str, strArr);
                                        break;
                                }
                        }
                    } else {
                        update = writableDatabase.update("cloudControl", 0, contentValues, str, strArr);
                    }
                    update = update2;
                } else {
                    update = writableDatabase.update("peopleFace", 0, contentValues, str, strArr);
                }
            }
            appendValuesForCloud(contentValues, false);
            this.mMediaManager.update(genIDSelection(str, strArr, false), null, contentValues);
            update = writableDatabase.update("cloud", 0, contentValues, str, strArr);
        }
        if (update > 0 && needNotifyUpdate(uri, match, contentValues)) {
            notifyChange(uri, null, parseSyncReason(uri, contentValues));
        }
        return update;
    }

    public final int internalUpdateDbAlbumData(SupportSQLiteDatabase supportSQLiteDatabase, ContentValues contentValues, String str, String[] strArr) {
        String genAlbumIDSelection = genAlbumIDSelection(str, strArr, false);
        if (contentValues.containsKey("attributes")) {
            updateAttributes(supportSQLiteDatabase, "album", genAlbumIDSelection, contentValues);
        }
        AlbumCacheManager.getInstance().update(genAlbumIDSelection, null, contentValues);
        AlbumDataHelper.genUpdateExtraValueForContentValuesIfNeed(contentValues);
        String asString = contentValues.getAsString(CallMethod.ARG_EXTRA_STRING);
        if (!TextUtils.isEmpty(asString)) {
            if (strArr == null) {
                supportSQLiteDatabase.execSQL(String.format(Locale.US, "UPDATE album SET extra = %s WHERE %s", asString, str));
            } else {
                supportSQLiteDatabase.execSQL(String.format(Locale.US, "UPDATE album SET extra = %s WHERE %s", asString, str), strArr);
            }
            if (contentValues.size() == 1) {
                Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("album").columns(new String[]{"count(*)"}).selection(str, strArr).create());
                if (query != null && query.moveToFirst()) {
                    return query.getInt(0);
                }
                return 0;
            }
            contentValues.remove(CallMethod.ARG_EXTRA_STRING);
        }
        return supportSQLiteDatabase.update("album", 0, contentValues, str, strArr);
    }

    public final boolean needNotifyUpdate(Uri uri, int i, ContentValues contentValues) {
        if (i == 71 || i == 72 || i == 92 || i == 150) {
            return true;
        }
        if (i == 75 || i == 70) {
            return contentValues.containsKey("sha1") || contentValues.containsKey("title") || contentValues.containsKey("specialTypeFlags") || contentValues.containsKey("duration") || contentValues.containsKey("attributes");
        } else if (i == 180 && contentValues.containsKey("cloud_id")) {
            return true;
        } else {
            if (i == 200 && (contentValues.containsKey("microFilePath") || contentValues.containsKey("trashFilePath"))) {
                return true;
            }
            return uri.getBooleanQueryParameter("requireNotifyUri", false);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0, types: [java.util.List, java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.miui.gallery.provider.cache.CacheManager, com.miui.gallery.provider.cache.MediaManager] */
    @Override // android.content.ContentProvider
    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        ContentValues[] contentValuesArr2 = contentValuesArr;
        int match = GalleryCloudProvider.sUriMatcher.match(uri);
        int isBlockedByMediaManager = isBlockedByMediaManager(match);
        if (isBlockedByMediaManager != 0 || this.mMediaManager.isInitializedLock()) {
            if ((isNeedCheckAlbumCacheManagerInitilized(match) && !AlbumCacheManager.getInstance().isInitializedLock()) || contentValuesArr2 == null) {
                return 0;
            }
            SupportSQLiteDatabase writableDatabase = GalleryCloudProvider.sDBHelper.getWritableDatabase();
            writableDatabase.beginTransaction();
            try {
                ?? arrayList = new ArrayList(contentValuesArr2.length);
                boolean z = 1 == isBlockedByMediaManager && !this.mMediaManager.isInitialized();
                boolean booleanQueryParameter = uri.getBooleanQueryParameter("insert_without_dedup", false);
                boolean booleanQueryParameter2 = uri.getBooleanQueryParameter("bulk_notify_media", false);
                int length = contentValuesArr2.length;
                ContentObserver contentObserver = null;
                int i = 0;
                int i2 = 0;
                ContentValues contentValues = null;
                while (i < length) {
                    ContentValues contentValues2 = contentValuesArr2[i];
                    ContentValues contentValues3 = contentValues;
                    int i3 = i;
                    int i4 = match;
                    ContentObserver contentObserver2 = contentObserver;
                    int i5 = length;
                    if (doInsertWithNoNotify(uri, match, contentValues2, z, booleanQueryParameter, booleanQueryParameter2 ? arrayList : contentObserver) != -1) {
                        i2++;
                        contentValues = contentValues2;
                    } else {
                        contentValues = contentValues3;
                    }
                    i = i3 + 1;
                    contentValuesArr2 = contentValuesArr;
                    contentObserver = contentObserver2;
                    match = i4;
                    length = i5;
                }
                ContentValues contentValues4 = contentValues;
                ContentObserver contentObserver3 = contentObserver;
                if (booleanQueryParameter2) {
                    this.mMediaManager.bulkInsert(arrayList, booleanQueryParameter);
                }
                writableDatabase.setTransactionSuccessful();
                if (i2 > 0) {
                    notifyChange(uri, contentObserver3, parseSyncReason(uri, contentValues4));
                }
                return i2;
            } finally {
                writableDatabase.endTransaction();
            }
        }
        return 0;
    }

    @Override // android.content.ContentProvider
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> arrayList) throws OperationApplicationException {
        if (!this.mMediaManager.isInitializedLock()) {
            return null;
        }
        SupportSQLiteDatabase writableDatabase = GalleryCloudProvider.sDBHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            ContentProviderResult[] applyBatch = super.applyBatch(arrayList);
            writableDatabase.setTransactionSuccessful();
            return applyBatch;
        } finally {
            writableDatabase.endTransaction();
        }
    }

    @Override // com.miui.gallery.provider.deprecated.GalleryCloudProvider, android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        if (!this.mMediaManager.isInitializedLock()) {
            return null;
        }
        SupportSQLiteDatabase writableDatabase = GalleryCloudProvider.sDBHelper.getWritableDatabase();
        if (CloudManager.canHandle(str)) {
            try {
                Bundle call = CloudManager.call(getContext(), writableDatabase, this.mMediaManager, str, str2, bundle);
                long j = 0;
                if (call.getBoolean("should_request_sync")) {
                    j = 561;
                }
                notifyChange(getNotifyUri(str), null, j, call);
                return call;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return super.call(str, str2, bundle);
    }

    @Override // android.content.ContentProvider
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mMediaManager.dump(printWriter);
        AlbumCacheManager.getInstance().dump(printWriter);
        PendingTaskManager.getInstance().dump(printWriter);
    }

    public final Uri getNotifyUri(String str) {
        for (String str2 : sAlbumOperation) {
            if (str2.equals(str)) {
                return GalleryContract.Album.URI;
            }
        }
        return GalleryContract.Cloud.CLOUD_URI;
    }

    public static boolean shouldRequestSync(Uri uri, ContentValues contentValues) {
        if (uri.getQueryParameter("URI_PARAM_REQUEST_SYNC") != null) {
            return uri.getBooleanQueryParameter("URI_PARAM_REQUEST_SYNC", false);
        }
        return false;
    }

    public static String matchTableName(Uri uri) {
        return GalleryCloudProvider.sUriMatcher.match(uri) != 110 ? GalleryCloudProvider.matchTableName(uri) : "shareImage";
    }

    public static boolean isAlbumUri(int i) {
        return isSpecificUri(i, 60, 62);
    }

    public static boolean isMediaUri(int i) {
        return isSpecificUri(i, 50, 54);
    }

    public static boolean isCloudUri(int i) {
        return isSpecificUri(i, 70, 76);
    }

    public static boolean isFaceUri(int i) {
        return isSpecificUri(i, 90, 101);
    }

    public static boolean isShareUri(int i) {
        return isSpecificUri(i, 110, 115);
    }

    public static boolean isUserUri(int i) {
        return isSpecificUri(i, BaiduSceneResult.VISA, BaiduSceneResult.EXPRESS_ORDER);
    }

    public static boolean isFavoriteUri(int i) {
        return isSpecificUri(i, nexClip.kClip_Rotate_180, nexClip.kClip_Rotate_180);
    }

    public static long parseSyncReason(Uri uri, ContentValues contentValues) {
        if (shouldRequestSync(uri, contentValues)) {
            int match = GalleryCloudProvider.sUriMatcher.match(uri);
            if (isMediaUri(match) || isAlbumUri(match) || isCloudUri(match) || isFavoriteUri(match)) {
                return 33L;
            }
            if (isFaceUri(match)) {
                return 136L;
            }
            return (isShareUri(match) || isUserUri(match)) ? 528L : 0L;
        }
        return 0L;
    }

    public final void notifyChange(Uri uri, ContentObserver contentObserver, long j) {
        notifyChange(uri, contentObserver, j, null);
    }

    public final void notifyChange(Uri uri, ContentObserver contentObserver, long j, Bundle bundle) {
        if (needDelayNotify(uri)) {
            this.mContentResolver.notifyChangeDelay(uri, contentObserver, j, bundle);
        } else {
            this.mContentResolver.notifyChange(uri, contentObserver, j, bundle);
        }
    }

    public final boolean needDelayNotify(Uri uri) {
        if (GalleryCloudProvider.sUriMatcher.match(uri) != 75) {
            return uri.getBooleanQueryParameter("delay_notify", false);
        }
        return true;
    }

    public final void registerNotifyUri(Cursor cursor, int i) {
        if (i == 61) {
            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Album.URI_CACHE);
        } else if (i == 76) {
            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.ExtendedCloud.URI);
        } else if (i == 152) {
            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.RecentDiscoveredMedia.URI);
        } else if (i == 180) {
            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Favorites.URI);
        } else if (i == 200) {
            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.TrashBin.TRASH_BIN_URI);
        } else if (i == 90) {
            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.PeopleFace.PERSONS_URI);
        } else if (i == 91) {
            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.PeopleFace.ONE_PERSON_URI);
        } else if (i == 112) {
            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Album.URI_SHARE_ALL);
        } else if (i == 113) {
            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Media.URI_OTHER_ALBUM_MEDIA);
        } else {
            switch (i) {
                case 50:
                    cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Media.URI_ALL);
                    return;
                case 51:
                case 52:
                    cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Media.URI);
                    return;
                case 53:
                    cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Media.URI_RECENT_MEDIA);
                    return;
                default:
                    switch (i) {
                        case 70:
                        case 71:
                            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Cloud.CLOUD_URI);
                            return;
                        case 72:
                            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Album.URI);
                            return;
                        case 73:
                            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA);
                            return;
                        default:
                            switch (i) {
                                case 95:
                                    cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.PeopleFace.PERSONS_URI);
                                    return;
                                case 96:
                                    cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.PeopleFace.PERSONS_URI);
                                    return;
                                case 97:
                                    cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.PeopleFace.IGNORE_PERSONS_URI);
                                    return;
                                default:
                                    switch (i) {
                                        case BaiduSceneResult.VISA /* 130 */:
                                        case BaiduSceneResult.INVOICE /* 131 */:
                                            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.ShareUser.SHARE_USER_URI);
                                            return;
                                        case BaiduSceneResult.VARIOUS_TICKETS /* 132 */:
                                        case BaiduSceneResult.EXPRESS_ORDER /* 133 */:
                                            cursor.setNotificationUri(getContext().getContentResolver(), GalleryContract.CloudUser.CLOUD_USER_URI);
                                            return;
                                        default:
                                            return;
                                    }
                            }
                    }
            }
        }
    }

    public final List<Long> getCloudIdListFromFavorites(String str, String[] strArr, boolean z) {
        if (z) {
            try {
                if (TextUtils.isEmpty(str)) {
                    str = "isFavorite NOT NULL AND isFavorite> 0";
                } else {
                    str = str + " AND (isFavorite NOT NULL AND isFavorite> 0)";
                }
            } catch (Throwable th) {
                BaseMiscUtil.closeSilently(null);
                throw th;
            }
        }
        Cursor query = GalleryCloudProvider.sDBHelper.getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("favorites").columns(new String[]{"cloud_id"}).selection(str, strArr).create());
        if (query != null && query.getCount() > 0) {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(Long.valueOf(query.getLong(0)));
            }
            BaseMiscUtil.closeSilently(query);
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList();
        BaseMiscUtil.closeSilently(query);
        return arrayList2;
    }

    public final String generateRecentMediaIdsSelection() {
        List list = (List) SafeDBUtil.safeQuery(GalleryCloudProvider.sDBHelper.getReadableDatabase(), "recentDiscoveredMedia", new String[]{"mediaId"}, "dateAdded>=?", new String[]{String.valueOf(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(28L))}, (String) null, GalleryProvider$$ExternalSyntheticLambda0.INSTANCE);
        StringBuilder sb = new StringBuilder();
        sb.append(j.c);
        sb.append(" IN (");
        sb.append(BaseMiscUtil.isValid(list) ? Joiner.on((char) CoreConstants.COMMA_CHAR).skipNulls().join(list) : "");
        sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
        return sb.toString();
    }

    public static /* synthetic */ List lambda$generateRecentMediaIdsSelection$0(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        LinkedList linkedList = new LinkedList();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            linkedList.add(Long.valueOf(cursor.getLong(0)));
            cursor.moveToNext();
        }
        return linkedList;
    }

    public final String generateRecentMediaCommonSelection() {
        String str = "alias_modify_date >= " + GalleryDateUtils.format(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(28L)) + " AND sha1 NOT NULL AND alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != -1000";
        if (GalleryPreferences.LocalMode.isOnlyShowLocalPhoto()) {
            return str + " AND " + InternalContract$Cloud.ALIAS_LOCAL_MEDIA;
        }
        return str;
    }

    /* loaded from: classes2.dex */
    public class ContentResolver extends GalleryContentResolver {
        public ContentResolver() {
        }

        @Override // com.miui.gallery.provider.GalleryContentResolver
        public Object matchUri(Uri uri) {
            return Integer.valueOf(GalleryCloudProvider.sUriMatcher.match(uri));
        }

        @Override // com.miui.gallery.provider.GalleryContentResolver
        public void notifyInternal(Uri uri, ContentObserver contentObserver, long j, Bundle bundle) {
            int match = GalleryCloudProvider.sUriMatcher.match(uri);
            if (match != 61) {
                if (match != 75) {
                    if (match == 92) {
                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.PERSONS_URI, contentObserver, false);
                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.IGNORE_PERSONS_URI, contentObserver, false);
                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.ONE_PERSON_URI, contentObserver, false);
                    } else if (match == 110) {
                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI, contentObserver, false);
                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_OTHER_ALBUM_MEDIA, contentObserver, false);
                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_ALL, contentObserver, false);
                    } else {
                        if (match != 112) {
                            if (match == 150) {
                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.DiscoveryMessage.URI, contentObserver, false);
                            } else if (match == 152) {
                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_RECENT_MEDIA, contentObserver, false);
                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.RecentDiscoveredMedia.URI, contentObserver, false);
                            } else if (match == 180) {
                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Favorites.URI, contentObserver, false);
                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI, contentObserver, false);
                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI, contentObserver, false);
                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA, contentObserver, false);
                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_ALL, contentObserver, false);
                            } else if (match != 200) {
                                switch (match) {
                                    case 70:
                                    case 71:
                                        break;
                                    case 72:
                                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI, contentObserver, false);
                                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, contentObserver, false);
                                        break;
                                    default:
                                        switch (match) {
                                            case BaiduSceneResult.VARIOUS_TICKETS /* 132 */:
                                            case BaiduSceneResult.EXPRESS_ORDER /* 133 */:
                                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.CloudUser.CLOUD_USER_URI, contentObserver, false);
                                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI_SHARE_ALL, contentObserver, false);
                                                break;
                                        }
                                }
                            } else {
                                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.TrashBin.TRASH_BIN_URI, contentObserver, false);
                            }
                        }
                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.ShareUser.SHARE_USER_URI, contentObserver, false);
                        GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI_SHARE_ALL, contentObserver, false);
                    }
                }
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Cloud.CLOUD_URI, contentObserver, false);
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI, contentObserver, false);
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI, contentObserver, false);
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA, contentObserver, false);
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_ALL, contentObserver, false);
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_OTHER_ALBUM_MEDIA, contentObserver, false);
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.ONE_PERSON_URI, contentObserver, false);
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_RECENT_MEDIA, contentObserver, false);
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.PERSONS_URI, contentObserver, false);
            } else {
                GalleryProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, contentObserver, false);
            }
            Uri[] extraNotififyUri = CloudUtils.extraNotififyUri(bundle);
            if (extraNotififyUri != null) {
                for (Uri uri2 : extraNotififyUri) {
                    GalleryProvider.this.getContext().getContentResolver().notifyChange(uri2, contentObserver, false);
                }
            }
            if (j != 0) {
                SyncUtil.requestSync(GalleryProvider.this.getContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(j).setDelayUpload(true).build());
            }
        }
    }
}
