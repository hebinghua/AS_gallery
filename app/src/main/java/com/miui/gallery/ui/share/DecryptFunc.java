package com.miui.gallery.ui.share;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SecretAlbumCryptoUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes2.dex */
public class DecryptFunc implements PrepareFunc<DecryptItem> {
    public File mCacheFolder;
    public SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS", Locale.US);

    @Override // com.miui.gallery.ui.share.PrepareFunc
    public void release() {
    }

    @Override // com.miui.gallery.ui.share.PrepareFunc
    public /* bridge */ /* synthetic */ Uri prepare(DecryptItem decryptItem, PrepareProgressCallback<DecryptItem> prepareProgressCallback) {
        return prepare2(decryptItem, (PrepareProgressCallback) prepareProgressCallback);
    }

    public DecryptFunc(File file) {
        this.mCacheFolder = file;
    }

    /* renamed from: prepare  reason: avoid collision after fix types in other method */
    public Uri prepare2(DecryptItem decryptItem, PrepareProgressCallback prepareProgressCallback) {
        String decryptFile;
        Uri uri = null;
        if (this.mCacheFolder.exists() || this.mCacheFolder.mkdirs()) {
            Uri preparedUriInLastStep = decryptItem.getPreparedUriInLastStep();
            String path = preparedUriInLastStep.getPath();
            if (CloudUtils.SecretAlbumUtils.isEncryptedVideoByPath(path)) {
                uri = SecretAlbumCryptoUtils.decryptVideo2CacheFolder(Uri.fromFile(new File(path)), decryptItem.getSecretKey(), decryptItem.getSecretId());
            } else if (CloudUtils.SecretAlbumUtils.isUnencryptedVideoByPath(path) || CloudUtils.SecretAlbumUtils.isUnencryptedImageByPath(path)) {
                uri = preparedUriInLastStep;
            } else {
                File file = new File(this.mCacheFolder, getDecryptedName(decryptItem));
                String path2 = file.getPath();
                if (decryptItem.getSecretKey() != null) {
                    decryptFile = CloudUtils.SecretAlbumUtils.decryptFile(path, path2, false, decryptItem.getSecretKey(), false);
                } else {
                    decryptFile = CloudUtils.SecretAlbumUtils.decryptFile(path, path2, false, decryptItem.getSecretId(), false);
                    DefaultLogger.d("DecryptFunc", "decode %s and secret key is null", path);
                }
                if (decryptFile != null) {
                    DefaultLogger.d("DecryptFunc", "decode %s is success:%s", path, file);
                    uri = Uri.fromFile(file);
                } else {
                    DefaultLogger.d("DecryptFunc", "decode %s is failed", path);
                }
            }
            if (prepareProgressCallback != null) {
                prepareProgressCallback.onPreparing(decryptItem, 1.0f);
            }
            return uri;
        }
        return null;
    }

    public final String getDecryptedName(DecryptItem decryptItem) {
        String str = (String) SafeDBUtil.safeQuery(StaticContext.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{"fileName"}, "_id=?", new String[]{String.valueOf(decryptItem.getSecretId())}, (String) null, new SafeDBUtil.QueryHandler<String>() { // from class: com.miui.gallery.ui.share.DecryptFunc.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public String mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return cursor.getString(0);
            }
        });
        return String.format(Locale.US, "%s.%s", this.mFormat.format(new Date(System.currentTimeMillis())), TextUtils.isEmpty(str) ? "jpg" : BaseFileUtils.getExtension(str));
    }
}
