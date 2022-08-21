package com.miui.gallery.loader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.UriItem;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.MediaAndAlbumOperations;
import java.io.File;

/* loaded from: classes2.dex */
public class UriSetLoader extends BaseLoader {
    public String mMimeType;
    public Uri mUri;

    public UriSetLoader(Context context, Uri uri, Bundle bundle) {
        super(context);
        this.mUri = uri;
        this.mMimeType = bundle.getString("mime_type");
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public BaseDataSet mo1444loadInBackground() {
        UriItem uriItem = new UriItem(this.mUri);
        String str = this.mMimeType;
        if (Action.FILE_ATTRIBUTE.equals(this.mUri.getScheme())) {
            if (str == null || "image/*".equalsIgnoreCase(str)) {
                String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(this.mUri.toString());
                if (!TextUtils.isEmpty(fileExtensionFromUrl)) {
                    String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl.toLowerCase());
                    if (!TextUtils.isEmpty(str)) {
                        str = mimeTypeFromExtension;
                    }
                }
            }
            uriItem.setKey(this.mUri.getPath().hashCode()).setCreateTime(new File(this.mUri.getPath()).lastModified());
        } else {
            if (str == null || "image/*".equalsIgnoreCase(str)) {
                str = GalleryApp.sGetAndroidContext().getContentResolver().getType(this.mUri);
            }
            uriItem.setKey(this.mUri.hashCode());
        }
        uriItem.setMimeType(str);
        if (BaseFileMimeUtil.isRawFromMimeType(uriItem.getMimeType())) {
            uriItem.setSpecialTypeFlags(FileAppender.DEFAULT_BUFFER_SIZE);
        }
        return new UriDataSet(uriItem);
    }

    /* loaded from: classes2.dex */
    public static class UriDataSet extends BaseDataSet {
        public UriItem mItem;

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean addToAlbum(FragmentActivity fragmentActivity, int i, boolean z, boolean z2, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
            return false;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public void bindItem(BaseDataItem baseDataItem, int i) {
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public int doDelete(int i, BaseDataItem baseDataItem, boolean z) {
            return 0;
        }

        @Override // com.miui.gallery.model.BaseDataSet, com.miui.gallery.projection.IConnectController.DataSet
        public int getCount() {
            return 1;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public void onRelease() {
        }

        public UriDataSet(UriItem uriItem) {
            this.mItem = uriItem;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public BaseDataItem createItem(int i) {
            return this.mItem;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public long getItemKey(int i) {
            UriItem uriItem = this.mItem;
            if (uriItem != null) {
                return uriItem.getKey();
            }
            return -1L;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public String getItemPath(int i) {
            UriItem uriItem = this.mItem;
            if (uriItem != null) {
                return uriItem.getOriginalPath();
            }
            return null;
        }
    }
}
