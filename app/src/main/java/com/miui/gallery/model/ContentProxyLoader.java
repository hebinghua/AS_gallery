package com.miui.gallery.model;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.loader.BaseLoader;
import com.miui.gallery.loader.StorageSetLoader;
import com.miui.gallery.loader.UriSetLoader;
import com.miui.gallery.util.ContentUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;

/* loaded from: classes2.dex */
public class ContentProxyLoader extends BaseLoader {
    public final Bundle mData;
    public BaseLoader mProxy;
    public final Uri mUri;

    public ContentProxyLoader(Context context, Uri uri, Bundle bundle) {
        super(context);
        this.mUri = uri;
        this.mData = bundle;
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public BaseDataSet mo1444loadInBackground() {
        if (this.mProxy == null) {
            String validFilePathForContentUri = ContentUtils.getValidFilePathForContentUri(getContext(), this.mUri);
            DefaultLogger.d("ContentProxyLoader", "uri %s, path %s", this.mUri, validFilePathForContentUri);
            if (!TextUtils.isEmpty(validFilePathForContentUri)) {
                this.mProxy = new StorageSetLoader(getContext(), Uri.fromFile(new File(validFilePathForContentUri)), this.mData);
            } else {
                this.mProxy = new UriSetLoader(getContext(), this.mUri, this.mData);
            }
        }
        BaseLoader baseLoader = this.mProxy;
        if (baseLoader != null) {
            return baseLoader.mo1444loadInBackground();
        }
        return null;
    }
}
