package com.miui.gallery.dao;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.model.AlbumConstants;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.datalayer.utils.loader.CustomCursorLoader;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.List;

/* loaded from: classes.dex */
public class AlbumTableServices {
    public static final Uri QUERYURL = GalleryContract.Album.URI;
    public static final String[] ALBUM_ONLY_ID_MODE_PROJECTION = {j.c, "name"};

    public static final CustomCursorLoader getQueryShareAlbumLoader(Context context) {
        CustomCursorLoader customCursorLoader = new CustomCursorLoader(context, true);
        customCursorLoader.setUri(GalleryContract.Album.URI_SHARE_ALL);
        customCursorLoader.setProjection(AlbumConstants.SHARED_ALBUM_PROJECTION);
        customCursorLoader.setSelection("count > 0");
        return customCursorLoader;
    }

    public static CustomCursorLoader getQueryTrashAlbumPhotoCountLoader(Context context) {
        try {
            TrashUtils.UserInfo lastUserInfo = TrashUtils.getLastUserInfo();
            CustomCursorLoader customCursorLoader = new CustomCursorLoader(context, true);
            customCursorLoader.setUri(GalleryContract.TrashBin.TRASH_BIN_URI);
            customCursorLoader.setProjection(new String[]{"count(*)"});
            customCursorLoader.setSelection("deleteTime>=" + TrashUtils.getTrashBinStartMs(lastUserInfo) + " AND status=0");
            return customCursorLoader;
        } catch (Exception e) {
            DefaultLogger.e("AlbumTableServices", e);
            return null;
        }
    }

    @Deprecated
    public static final List<Album> queryAlbumSnapDatas(QueryParam queryParam) {
        return GalleryLiteEntityManager.getInstance().query(Album.class, queryParam.getSelection(), queryParam.getBindArgs());
    }

    public static final boolean changeAlbumHiddenStatus(Context context, long j, boolean z, boolean z2) {
        return MediaAndAlbumOperations.doChangeHiddenStatus(context, j, z, z2);
    }

    public static final CustomCursorLoader getQueryAlbumsLoader(Context context, Uri uri, QueryParam queryParam, long j) {
        CustomCursorLoader customCursorLoader = new CustomCursorLoader(context, true);
        Uri.Builder appendQueryParameter = uri.buildUpon().appendQueryParameter("query_flags", String.valueOf(j));
        if (queryParam != null) {
            String limit = queryParam.getLimit();
            if (!TextUtils.isEmpty(limit) && Integer.parseInt(limit) > 0) {
                appendQueryParameter.appendQueryParameter("limit", String.valueOf(limit));
            }
            if (queryParam.isDistinct()) {
                appendQueryParameter.appendQueryParameter("distinct", String.valueOf(true));
            }
            if (!TextUtils.isEmpty(queryParam.getGroupBy())) {
                appendQueryParameter.appendQueryParameter("groupBy", queryParam.getGroupBy());
            }
            if (!TextUtils.isEmpty(queryParam.getHaving())) {
                appendQueryParameter.appendQueryParameter("having", queryParam.getHaving());
            }
            customCursorLoader.setSelectionArgs(queryParam.getBindArgs());
            customCursorLoader.setSelection(queryParam.getSelection());
        }
        customCursorLoader.setProjection((queryParam == null || queryParam.getColumns() == null) ? AlbumConstants.QUERY_ALBUM_PROJECTION : queryParam.getColumns());
        customCursorLoader.setUri(appendQueryParameter.build());
        return customCursorLoader;
    }
}
