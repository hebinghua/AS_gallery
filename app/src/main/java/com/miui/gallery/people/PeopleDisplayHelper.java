package com.miui.gallery.people;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.glide.CloudImageLoader;

/* loaded from: classes2.dex */
public class PeopleDisplayHelper {
    public static void bindImage(ImageView imageView, String str, Uri uri, RequestOptions requestOptions, DownloadType downloadType) {
        BindImageHelper.bindImage(str, uri, BaseNetworkUtils.isActiveNetworkMetered() ? DownloadType.MICRO : DownloadType.THUMBNAIL, imageView, requestOptions, true, true);
        if (DownloadType.MICRO != downloadType || TextUtils.isEmpty(str) || BaseNetworkUtils.isActiveNetworkMetered()) {
            return;
        }
        CloudImageLoader.getInstance().displayImage(uri, DownloadType.THUMBNAIL, imageView, requestOptions, true, false);
    }
}
