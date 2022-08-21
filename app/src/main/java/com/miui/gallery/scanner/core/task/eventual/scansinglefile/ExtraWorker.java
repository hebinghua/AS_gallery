package com.miui.gallery.scanner.core.task.eventual.scansinglefile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.StringBuilderPrinter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.provider.RecentDiscoveryMediaManager;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.cache.BurstInfo;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.scanner.MediaScannerHelper;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.extra.snapshot.InsertedValue;
import com.miui.gallery.scanner.utils.GenThumbnailUtil;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import java.nio.file.Path;

/* loaded from: classes2.dex */
public class ExtraWorker {
    public OwnerAlbumEntry mAlbumEntry;
    public ScanTaskConfig mConfig;
    public Context mContext;
    public long mId;
    public Path mPath;

    public ExtraWorker(Context context, Path path, ScanTaskConfig scanTaskConfig) {
        this.mContext = context;
        this.mPath = path;
        this.mConfig = scanTaskConfig;
    }

    public void work(ScanResult scanResult) {
        this.mId = scanResult.getMediaId();
        this.mAlbumEntry = (OwnerAlbumEntry) scanResult.getAlbumEntry();
        TimingTracing.beginTracing("ExtraWorker" + Thread.currentThread().getName(), "ExtraWorker");
        requestThumbnail();
        TimingTracing.addSplit("requestThumbnail");
        notifyItemInserted();
        TimingTracing.addSplit("notifyItemInserted");
        dealWithDeletedAlbum();
        TimingTracing.addSplit("dealWithDeletedAlbum");
        insertToRecent();
        TimingTracing.addSplit("insertToRecent");
        LocationManager.getInstance().loadLocationAsync(this.mId);
        TimingTracing.addSplit("loadLocationAsync");
        StringBuilder sb = new StringBuilder();
        TimingTracing.stopTracing(new StringBuilderPrinter(sb));
        DefaultLogger.d("ExtraWorker", this.mPath + sb.toString());
    }

    public final void insertToRecent() {
        if (!this.mConfig.isBulkNotify() && !ShareAlbumHelper.isOtherShareAlbumId(this.mAlbumEntry.mId)) {
            RecentDiscoveryMediaManager.RecentMediaEntry recentMediaEntry = new RecentDiscoveryMediaManager.RecentMediaEntry(this.mAlbumEntry.mId, this.mId, this.mPath.toString(), this.mPath.toFile().lastModified());
            if (this.mConfig.showInRecentAlbum()) {
                RecentDiscoveryMediaManager.insertToRecentUnchecked(this.mContext, recentMediaEntry);
            } else {
                RecentDiscoveryMediaManager.insertToRecent(this.mContext, recentMediaEntry);
            }
        }
    }

    public final void dealWithDeletedAlbum() {
        if (this.mAlbumEntry.isDeletedAlbum()) {
            long j = this.mId;
            if (j > 0) {
                MediaScannerHelper.whileNewMediaFoundInDeletedAlbum(this.mContext, this.mAlbumEntry, j);
            } else if (!this.mConfig.isBulkNotify()) {
            } else {
                MediaScannerHelper.whileNewMediaFoundInDeletedAlbum(this.mContext, this.mAlbumEntry, this.mConfig.getInserter());
            }
        }
    }

    public final void requestThumbnail() {
        if (!this.mConfig.isBulkNotify() && !BaseBuildUtil.isLowRamDevice() && !this.mPath.toString().contains("_8K") && !BurstInfo.maybeBurst(BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(this.mPath.toString())))) {
            GenThumbnailUtil.genMicroThumbnail(this.mPath.toString(), 5000L);
        }
    }

    public final void notifyItemInserted() {
        if (this.mConfig.isBulkNotify()) {
            return;
        }
        Intent intent = new Intent("gallery.scanner.INSERTED");
        Bundle bundle = new Bundle();
        String path = this.mPath.toString();
        OwnerAlbumEntry ownerAlbumEntry = this.mAlbumEntry;
        bundle.putParcelable("gallery.scanner.PARAMS", new InsertedValue(path, ownerAlbumEntry.mId, ownerAlbumEntry.mAttributes));
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(GalleryApp.sGetAndroidContext()).sendBroadcast(intent);
    }
}
