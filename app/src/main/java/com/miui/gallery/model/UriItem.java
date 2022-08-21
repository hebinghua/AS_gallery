package com.miui.gallery.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.ui.SaveUriDialogFragment;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.gifdecoder.NSGifDecode;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* loaded from: classes2.dex */
public class UriItem extends BaseDataItem {
    private File mCacheFile;
    public transient Uri mUri;

    public UriItem(Uri uri) {
        this.mUri = uri;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public String getOriginalPath() {
        if (isCacheValidate()) {
            return this.mCacheFile.getAbsolutePath();
        }
        return this.mUri.toString();
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public NSGifDecode createNSGifDecoder(ThreadPool.JobContext jobContext) {
        if (isCacheValidate()) {
            return NSGifDecode.create(this.mCacheFile.getAbsolutePath());
        }
        ParcelFileDescriptor openOrDownloadInner = openOrDownloadInner();
        if (openOrDownloadInner == null) {
            return null;
        }
        try {
            if (!jobContext.isCancelled()) {
                return NSGifDecode.create(openOrDownloadInner.getFileDescriptor(), null);
            }
            return null;
        } finally {
            BaseMiscUtil.closeSilently(openOrDownloadInner);
        }
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public boolean checkOriginalFileExist() {
        ParcelFileDescriptor openOrDownloadInner;
        if (isCacheValidate()) {
            return true;
        }
        if (this.mUri == null || (openOrDownloadInner = openOrDownloadInner()) == null) {
            return false;
        }
        try {
            openOrDownloadInner.close();
        } catch (Exception unused) {
            DefaultLogger.e("UriItem", "File descriptor close failed");
        }
        return true;
    }

    public final ParcelFileDescriptor openOrDownloadInner() {
        String scheme = this.mUri.getScheme();
        if (MiStat.Param.CONTENT.equals(scheme) || "android.resource".equals(scheme) || Action.FILE_ATTRIBUTE.equals(scheme)) {
            try {
                return GalleryApp.sGetAndroidContext().getContentResolver().openFileDescriptor(this.mUri, "r");
            } catch (Exception e) {
                DefaultLogger.w("UriItem", "fail to open %s %s", this.mUri, e);
                return null;
            }
        }
        return null;
    }

    public final boolean isCacheValidate() {
        File file = this.mCacheFile;
        return file != null && file.exists();
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public PhotoDetailInfo getDetailInfo(Context context) {
        String absolutePath;
        InputStream openInputStream;
        PhotoDetailInfo detailInfo = super.getDetailInfo(context);
        InputStream inputStream = null;
        if (Action.FILE_ATTRIBUTE.equals(this.mUri.getScheme())) {
            absolutePath = this.mUri.getPath();
        } else {
            absolutePath = isCacheValidate() ? this.mCacheFile.getAbsolutePath() : null;
        }
        if (!TextUtils.isEmpty(absolutePath)) {
            if (isVideo()) {
                PhotoDetailInfo.extractVideoAttr(detailInfo, getOriginalPath());
            } else {
                PhotoDetailInfo.extractExifInfo(detailInfo, getOriginalPath(), true);
            }
        } else if (!isVideo()) {
            try {
                try {
                    openInputStream = context.getContentResolver().openInputStream(this.mUri);
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                long available = openInputStream.available();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(openInputStream, null, options);
                detailInfo.addDetail(3, Long.valueOf(available));
                detailInfo.addDetail(4, Integer.valueOf(options.outWidth));
                detailInfo.addDetail(5, Integer.valueOf(options.outHeight));
                BaseMiscUtil.closeSilently(openInputStream);
            } catch (Exception e2) {
                e = e2;
                inputStream = openInputStream;
                DefaultLogger.w("UriItem", e);
                BaseMiscUtil.closeSilently(inputStream);
                return detailInfo;
            } catch (Throwable th2) {
                th = th2;
                inputStream = openInputStream;
                BaseMiscUtil.closeSilently(inputStream);
                throw th;
            }
        }
        return detailInfo;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public long initSupportOperations() {
        return BitmapUtils.isSupportedByRegionDecoder(getMimeType()) ? 2129984L : 2129920L;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public void save(FragmentActivity fragmentActivity, SaveUriDialogFragment.OnCompleteListener onCompleteListener) {
        SaveUriDialogFragment.show(fragmentActivity, this.mUri, onCompleteListener);
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public String getViewTitle(Context context) {
        if (UriUtil.isNetUri(this.mUri)) {
            return context.getString(R.string.photo);
        }
        return super.getViewTitle(context);
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public String getViewSubTitle(Context context) {
        if (UriUtil.isNetUri(this.mUri)) {
            return context.getString(R.string.view_by_gallery_tip);
        }
        return super.getViewSubTitle(context);
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public void onCacheLoaded() {
        ParcelFileDescriptor openOrDownloadInner = openOrDownloadInner();
        if (openOrDownloadInner != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(openOrDownloadInner.getFileDescriptor());
                this.mDisplayBetterFileSize = fileInputStream.available();
                if (this.mDisplayBetterFileSize > 0 && (this.mDisplayBetterFileSize != this.mSize || this.mWidth == 0 || this.mHeight == 0)) {
                    this.mSize = this.mDisplayBetterFileSize;
                    BitmapFactory.Options bitmapSize = miuix.graphics.BitmapFactory.getBitmapSize(GalleryApp.sGetAndroidContext(), this.mUri);
                    this.mWidth = bitmapSize.outWidth;
                    this.mHeight = bitmapSize.outHeight;
                }
                fileInputStream.close();
            } catch (IOException e) {
                DefaultLogger.e("UriItem", "onCacheLoaded.", e);
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.mUri.toString());
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.mUri = Uri.parse((String) objectInputStream.readObject());
    }
}
