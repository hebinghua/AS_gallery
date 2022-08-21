package com.miui.gallery.cloud;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DecodeUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;

/* loaded from: classes.dex */
public class ThumbnailMetaWriter {
    public final String mDateTime;
    public final String mGPSDateStamp;
    public final String mGPSTimeStamp;
    public final long mMixDateTime;
    public final String mOriginalFileName;
    public final String mSha1;

    public ThumbnailMetaWriter(DBImage dBImage) {
        this(dBImage, dBImage.getSha1(), dBImage.getFileName());
    }

    public ThumbnailMetaWriter(DBImage dBImage, String str, String str2) {
        this.mSha1 = str;
        this.mOriginalFileName = str2;
        this.mMixDateTime = dBImage.getMixedDateTime();
        this.mDateTime = dBImage.getJsonExifString("dateTime");
        this.mGPSDateStamp = dBImage.getJsonExifString("GPSDateStamp");
        this.mGPSTimeStamp = dBImage.getJsonExifString("GPSTimeStamp");
    }

    public ThumbnailMetaWriter(String str, String str2, long j, String str3, String str4, String str5) {
        this.mSha1 = str;
        this.mOriginalFileName = str2;
        this.mMixDateTime = j;
        this.mDateTime = str3;
        this.mGPSDateStamp = str4;
        this.mGPSTimeStamp = str5;
    }

    public boolean write(String str) {
        return write(str, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x0154  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x016d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean write(java.lang.String r18, boolean r19) {
        /*
            Method dump skipped, instructions count: 387
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.ThumbnailMetaWriter.write(java.lang.String, boolean):boolean");
    }

    public final boolean createFileAndWrite(String str) {
        File saveBitmapToFile;
        String fileName = BaseFileUtils.getFileName(str);
        String addPostfixToFileName = DownloadPathHelper.addPostfixToFileName(fileName, String.valueOf(System.currentTimeMillis()));
        String parentFolderPath = BaseFileUtils.getParentFolderPath(str);
        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(parentFolderPath)) {
            DefaultLogger.e("ThumbnailMetaWriter", "error path");
        }
        Bitmap bitmap = null;
        try {
            try {
                String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("ThumbnailMetaWriter", "createFileAndWrite");
                DecodeUtils.GalleryOptions galleryOptions = new DecodeUtils.GalleryOptions();
                ((BitmapFactory.Options) galleryOptions).inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = DecodeUtils.requestDecodeThumbNail(str, galleryOptions);
                if (bitmap != null && (saveBitmapToFile = CloudUtils.saveBitmapToFile(bitmap, parentFolderPath, addPostfixToFileName)) != null) {
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile != null) {
                        documentFile.delete();
                    }
                    if (StorageSolutionProvider.get().moveFile(saveBitmapToFile.getAbsolutePath(), new File(parentFolderPath, fileName).getAbsolutePath(), appendInvokerTag)) {
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                        return true;
                    }
                    DefaultLogger.e("ThumbnailMetaWriter", "create file failed when move");
                }
                if (bitmap == null || bitmap.isRecycled()) {
                    return false;
                }
            } catch (Exception e) {
                DefaultLogger.e("ThumbnailMetaWriter", e);
                e.printStackTrace();
                if (bitmap == null || bitmap.isRecycled()) {
                    return false;
                }
            }
            bitmap.recycle();
            return false;
        } catch (Throwable th) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            throw th;
        }
    }
}
