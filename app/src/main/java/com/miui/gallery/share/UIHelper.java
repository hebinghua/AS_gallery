package com.miui.gallery.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.share.baby.BabyShareAlbumOwnerActivity;
import com.miui.gallery.share.baby.BabyShareAlbumSharerActivity;
import com.miui.gallery.util.ToastUtils;

/* loaded from: classes2.dex */
public class UIHelper {
    public static void toast(CharSequence charSequence) {
    }

    public static void showAlbumShareInfo(Activity activity, Path path, int i) {
        Class cls;
        if (path == null) {
            return;
        }
        if (!CloudUtils.isActive(GalleryApp.sGetAndroidContext())) {
            ToastUtils.makeText(activity, (int) R.string.no_network_connected);
        }
        if (!path.isOtherShared()) {
            if (path.isBabyAlbum()) {
                cls = BabyShareAlbumOwnerActivity.class;
            } else {
                cls = NormalShareAlbumOwnerActivity.class;
            }
        } else if (path.isBabyAlbum()) {
            cls = BabyShareAlbumSharerActivity.class;
        } else {
            cls = NormalShareAlbumSharerActivity.class;
        }
        Intent intent = new Intent(activity, cls);
        intent.putExtra("share_path", path.toString());
        try {
            activity.startActivityForResult(intent, i);
        } catch (ActivityNotFoundException unused) {
        }
    }

    public static void toastError(int i) {
        toastError(null, i);
    }

    public static void toastError(Context context, int i) {
        int i2 = R.string.operation_successful;
        if (i == -1002) {
            i2 = R.string.network_connected_rescan;
        } else if (i == -4) {
            i2 = R.string.error_cloud_not_active;
        } else if (i != 0) {
            if (i == 50012) {
                i2 = R.string.error_album_not_exist;
            } else if (i == 50019) {
                i2 = R.string.error_invalid_link;
            } else if (i == 50025) {
                i2 = R.string.error_link_has_expired;
            } else if (i == 50030) {
                i2 = R.string.error_share_with_creator;
            } else if (i == -112) {
                i2 = R.string.error_unknown_host;
            } else if (i == -111) {
                i2 = R.string.error_socket_timeout;
            } else if (i != -10) {
                i2 = i != -9 ? R.string.operation_failed : R.string.error_repeat_apply;
            }
        }
        if (context != null) {
            toast(context, context.getString(i2));
        }
    }

    public static void toast(int i) {
        toast(GalleryApp.sGetAndroidContext().getString(i));
    }

    public static void toast(Context context, CharSequence charSequence) {
        ToastUtils.makeText(context, charSequence);
    }
}
