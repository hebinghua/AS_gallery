package com.miui.gallery.scanner.core.scanner;

import android.content.Context;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.utils.AbsDeDupStrategy;
import com.miui.gallery.scanner.utils.AbsImageValueCalculator;
import com.miui.gallery.scanner.utils.AbsVideoValueCalculator;
import com.miui.gallery.scanner.utils.DefaultDeDupStrategy;
import com.miui.gallery.scanner.utils.DefaultImageValueCalculator;
import com.miui.gallery.scanner.utils.DefaultVideoValueCalculator;
import com.miui.gallery.scanner.utils.MiMoverDeDupStrategy;
import com.miui.gallery.scanner.utils.MiMoverImageValueCalculator;
import com.miui.gallery.scanner.utils.MiMoverVideoValueCalculator;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;

/* loaded from: classes2.dex */
public class ScannerFileClient {
    public ScanTaskConfig mConfig;

    public ScannerFileClient(ScanTaskConfig scanTaskConfig) {
        this.mConfig = scanTaskConfig;
    }

    public ScanResult scanFile(Context context, File file, OwnerAlbumEntry ownerAlbumEntry) {
        AbsDeDupStrategy miMoverDeDupStrategy;
        AbsImageValueCalculator miMoverImageValueCalculator;
        AbsVideoValueCalculator miMoverVideoValueCalculator;
        if (ownerAlbumEntry.isOnlyLinkFolder) {
            if (!this.mConfig.linkFile()) {
                return ScanResult.success(ScanContracts$ScanResultReason.CONFIG_BANNED_LINK_FILE).build();
            }
            linkFile(context, file, ownerAlbumEntry.mId, ownerAlbumEntry.isShareAlbum);
            DefaultLogger.i("ScannerFileClient", "linkFile %s", file.getAbsolutePath());
            return ScanResult.success(ScanContracts$ScanResultReason.ONLY_LINK_FILE).build();
        }
        long uniformAlbumId = ownerAlbumEntry.isShareAlbum ? ShareAlbumHelper.getUniformAlbumId(ownerAlbumEntry.mId) : ownerAlbumEntry.mId;
        if (this.mConfig.getSceneCode() == 20) {
            miMoverDeDupStrategy = new MiMoverDeDupStrategy();
            miMoverImageValueCalculator = new MiMoverImageValueCalculator(file.getAbsolutePath());
            miMoverVideoValueCalculator = new MiMoverVideoValueCalculator(file.getAbsolutePath());
        } else {
            miMoverDeDupStrategy = new DefaultDeDupStrategy();
            miMoverImageValueCalculator = new DefaultImageValueCalculator(file.getAbsolutePath());
            miMoverVideoValueCalculator = new DefaultVideoValueCalculator(file.getAbsolutePath());
        }
        return SaveToCloudUtil.saveToCloudDB(context, new SaveParams.Builder().setSaveFile(file).setFileState(this.mConfig.getFileState()).setAlbumId(uniformAlbumId).setAlbumAttributes(ownerAlbumEntry.mAttributes).setAlbumSyncable(this.mConfig.needTriggerSync() && ownerAlbumEntry.isSyncable()).setLocalFlag(7).setBulkNotify(this.mConfig.isBulkNotify()).setInserter(this.mConfig.getInserter()).setBatchOperator(this.mConfig.getBatchOperator()).setCredible(this.mConfig.isCredible()).setDeDupStrategy(miMoverDeDupStrategy).setImageValueCalculator(miMoverImageValueCalculator).setVideoValueCalculator(miMoverVideoValueCalculator).build());
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x005d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void linkFile(android.content.Context r10, java.io.File r11, long r12, boolean r14) {
        /*
            r9 = this;
            java.lang.String r0 = r11.getAbsolutePath()
            java.lang.String r1 = com.miui.gallery.util.BaseFileMimeUtil.getMimeType(r0)
            boolean r1 = com.miui.gallery.util.BaseFileMimeUtil.isImageFromMimeType(r1)
            r2 = 0
            if (r1 == 0) goto L2f
            com.miui.gallery.util.ExifUtil$UserCommentData r1 = com.miui.gallery.util.ExifUtil.getUserCommentData(r0)     // Catch: java.lang.Exception -> L26
            if (r1 == 0) goto L2f
            java.lang.String r2 = r1.getSha1()     // Catch: java.lang.Exception -> L26
            java.lang.String r3 = r11.getName()     // Catch: java.lang.Exception -> L26
            java.lang.String r3 = com.miui.gallery.util.BaseFileUtils.getFileTitle(r3)     // Catch: java.lang.Exception -> L26
            java.lang.String r1 = r1.getFileName(r3)     // Catch: java.lang.Exception -> L26
            goto L30
        L26:
            r10 = move-exception
            java.lang.String r11 = "ScannerFileClient"
            java.lang.String r12 = "exif exifSha1 read fail %s"
            com.miui.gallery.util.logger.DefaultLogger.w(r11, r12, r10)
            return
        L2f:
            r1 = r2
        L30:
            if (r14 == 0) goto L35
            android.net.Uri r14 = com.miui.gallery.provider.GalleryContract.ShareImage.SHARE_URI
            goto L37
        L35:
            android.net.Uri r14 = com.miui.gallery.provider.GalleryContract.Cloud.CLOUD_URI
        L37:
            android.content.ContentValues r3 = new android.content.ContentValues
            r3.<init>()
            boolean r4 = android.text.TextUtils.isEmpty(r2)
            r5 = 2
            r6 = 1
            r7 = 0
            r8 = 3
            if (r4 != 0) goto L5d
            java.lang.String r11 = "thumbnailFile"
            r3.put(r11, r0)
            java.lang.String[] r11 = new java.lang.String[r8]
            java.lang.String r12 = java.lang.String.valueOf(r12)
            r11[r7] = r12
            r11[r6] = r1
            r11[r5] = r2
            java.lang.String r12 = "localGroupId=? AND (serverType=1 OR serverType=2) AND fileName=? AND sha1=?"
            com.miui.gallery.util.SafeDBUtil.safeUpdate(r10, r14, r3, r12, r11)
            goto L7f
        L5d:
            java.lang.String r1 = "localFile"
            r3.put(r1, r0)
            java.lang.String[] r0 = new java.lang.String[r8]
            java.lang.String r12 = java.lang.String.valueOf(r12)
            r0[r7] = r12
            java.lang.String r12 = r11.getName()
            r0[r6] = r12
            long r11 = r11.length()
            java.lang.String r11 = java.lang.String.valueOf(r11)
            r0[r5] = r11
            java.lang.String r11 = "localGroupId=? AND (serverType=1 OR serverType=2) AND fileName=? AND size=?"
            com.miui.gallery.util.SafeDBUtil.safeUpdate(r10, r14, r3, r11, r0)
        L7f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.scanner.core.scanner.ScannerFileClient.linkFile(android.content.Context, java.io.File, long, boolean):void");
    }
}
