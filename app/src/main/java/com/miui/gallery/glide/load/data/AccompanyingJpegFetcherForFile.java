package com.miui.gallery.glide.load.data;

import android.graphics.BitmapFactory;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFeatureUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.XmpHelper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class AccompanyingJpegFetcherForFile implements IThumbFetcher<String, DocumentFile> {
    public static final Pattern sRawNamePattern = Pattern.compile("(.*)(RAW/)(IMG_[0-9]*_[0-9]*)(_*[0-9]*)(\\.dng)", 2);

    @Override // com.miui.gallery.glide.load.data.IThumbFetcher
    public DocumentFile load(String str) throws IOException {
        if (BaseFeatureUtil.isDisableFastDisplayRaw()) {
            throw new IllegalArgumentException("device disable fast display raw");
        }
        String wrapRawFilePath = wrapRawFilePath(Scheme.FILE.crop(str));
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(wrapRawFilePath, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("AccompanyingJpegFetcherForFile", "load"));
        if (documentFile != null && documentFile.exists() && checkJpeg(wrapRawFilePath, documentFile)) {
            return documentFile;
        }
        throw new IllegalArgumentException(String.format("jpeg is null or not exists or checkJpeg [%s, %s] failed", wrapRawFilePath, documentFile));
    }

    public static String wrapRawFilePath(String str) throws FileNotFoundException {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("AccompanyingJpegFetcherForFile", "wrapRawFilePath");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(str, permission, appendInvokerTag);
        if (documentFile == null || !documentFile.exists() || !documentFile.isFile()) {
            return str;
        }
        Matcher matcher = sRawNamePattern.matcher(str);
        if (!matcher.find()) {
            throw new IllegalArgumentException(String.format("%s is not a dng from system camera or has been moved.", str));
        }
        String str2 = matcher.group(1) + matcher.group(3) + ".jpg";
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str2, permission, appendInvokerTag);
        return (documentFile2 == null || !documentFile2.exists() || !documentFile2.isFile()) ? str : str2;
    }

    public static boolean checkJpeg(String str, DocumentFile documentFile) throws IOException {
        if (documentFile.getName() == null || !documentFile.getName().endsWith(".jpg")) {
            return false;
        }
        BitmapFactory.Options bitmapSize = miuix.graphics.BitmapFactory.getBitmapSize(str);
        int i = bitmapSize.outWidth;
        int i2 = bitmapSize.outHeight;
        if (!BaseMiscUtil.floatNear(0.75f, Math.min(i, i2) / Math.max(i, i2), 0.001f) || !BaseMiscUtil.doubleEquals(ExifUtil.getXiaomiCommentZoomMultiple(str), 1.0d) || ExifUtil.getXiaomiCommentFilterId(str) != 66048) {
            return false;
        }
        InputStream openInputStream = StorageSolutionProvider.get().openInputStream(documentFile);
        try {
            if (XmpHelper.extractXMPMeta(openInputStream) != null) {
                if (openInputStream != null) {
                    openInputStream.close();
                }
                return false;
            } else if (openInputStream == null) {
                return true;
            } else {
                openInputStream.close();
                return true;
            }
        } catch (Throwable th) {
            if (openInputStream != null) {
                try {
                    openInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
