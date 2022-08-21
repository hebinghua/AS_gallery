package com.miui.gallery.provider;

import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.ShareAlbum;
import com.miui.gallery.share.ShareAlbumCacheManager;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class ShareAlbumHelper {
    public static long getOriginalAlbumId(long j) {
        return j - 2147383647;
    }

    public static long getUniformAlbumId(long j) {
        return j + 2147383647;
    }

    public static boolean isOtherShareAlbumId(long j) {
        return j >= 2147383647 && j < 2147483637;
    }

    public static String getShareAlbumInfoTipTextIfNeed(Album album) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        try {
            if (album.isOtherShareAlbum()) {
                ShareAlbum shareAlbum = ShareAlbumCacheManager.getInstance().getShareAlbum(album.getAlbumId());
                if (shareAlbum != null && !TextUtils.isEmpty(shareAlbum.getOwnerName())) {
                    if (album.isBabyAlbum()) {
                        acquire.append(ResourceUtils.getString(R.string.album_others_share_baby_info_format, shareAlbum.getOwnerName()));
                    } else {
                        acquire.append(ResourceUtils.getString(R.string.album_others_share_info_format, shareAlbum.getOwnerName()));
                    }
                }
            } else if (album.isBabyAlbum()) {
                acquire.append(ResourceUtils.getString(R.string.album_type_baby));
            } else if (album.isOwnerShareAlbum()) {
                if (ShareAlbumCacheManager.getInstance().getShareAlbum(album.getAlbumId()) != null) {
                    acquire.append(ResourceUtils.getString(R.string.already_share));
                }
            } else if (album.isShareToDevice()) {
                acquire.append(ResourceUtils.getString(R.string.already_share));
            }
            return acquire.toString();
        } finally {
            Pools.getStringBuilderPool().release(acquire);
        }
    }

    public static boolean isOwnerShareAlbum(long j) {
        try {
            ShareAlbum shareAlbum = ShareAlbumCacheManager.getInstance().getShareAlbum(j);
            if (shareAlbum == null) {
                return false;
            }
            return Long.parseLong(shareAlbum.mAlbumId) < 2147383647;
        } catch (Exception e) {
            DefaultLogger.e("ShareAlbumHelper", e);
            return false;
        }
    }
}
