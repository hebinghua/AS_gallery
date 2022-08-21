package com.miui.gallery.scanner.core.scanner;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.model.ShareAlbumEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileUtils;
import java.nio.file.Path;

/* loaded from: classes2.dex */
public class ShareImageScanner implements IMediaScanner<ShareAlbumEntry, Object> {
    @Override // com.miui.gallery.scanner.core.scanner.IMediaScanner
    public ScanResult scanFile(Context context, Path path, ShareAlbumEntry shareAlbumEntry, ScanTaskConfig scanTaskConfig) {
        String userCommentSha1 = ExifUtil.getUserCommentSha1(path.toString());
        if (TextUtils.isEmpty(userCommentSha1)) {
            userCommentSha1 = FileUtils.getSha1(path.toString());
        }
        if (!shareAlbumEntry.mImageSha1s.contains(userCommentSha1)) {
            return SaveToCloudUtil.saveToCloudDB(context, new SaveParams.Builder().setSaveFile(path.toFile()).setAlbumId(shareAlbumEntry.mId).setLocalFlag(8).setCredible(true).build());
        }
        return ScanResult.failed(ScanContracts$ScanResultReason.SHARE_ALBUM_ALREADY_CONTAINS_SHA1).build();
    }
}
