package com.miui.gallery.ui;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.people.PeopleDisplayHelper;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.glide.BindImageHelper;

/* loaded from: classes2.dex */
public class FaceDisplayItemPreferFromThumbnailSource extends RelativeLayout {
    public ImageView mCover;

    public FaceDisplayItemPreferFromThumbnailSource(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void bindImage(String str, Uri uri, RequestOptions requestOptions, DownloadType downloadType) {
        PeopleDisplayHelper.bindImage(this.mCover, str, uri, requestOptions, downloadType);
    }

    public void clearImage() {
        BindImageHelper.cancel(this.mCover);
    }
}
