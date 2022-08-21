package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import com.google.common.collect.Lists;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.operation.copy.CopyImageToSharerAlbum;
import com.miui.gallery.cloud.operation.delete.DeleteCloudItem;
import com.miui.gallery.cloud.operation.move.MoveItemToSharerAlbum;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.data.DBShareImage;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.SyncLogger;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class SyncSharerFromLocal extends SyncFromLocalBase {
    public ArrayList<RequestCloudItem> mCopyImageToSharerItems;
    public ArrayList<RequestCloudItem> mDeleteShareImageItems;
    public long mLargestElapsedTime;
    public ArrayList<RequestCloudItem> mManualCreateSharerImageItems;
    public ArrayList<RequestCloudItem> mManualCreateSharerVideoItems;
    public ArrayList<RequestCloudItem> mMoveImageToSharerItems;
    public int mPhotosToBeSynced;
    public int mVideosToBeSynced;

    public SyncSharerFromLocal(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        super(context, account, galleryExtendedAuthToken);
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void syncStart() {
        super.syncStart();
        this.mPhotosToBeSynced = 0;
        this.mVideosToBeSynced = 0;
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void syncEnd() {
        super.syncEnd();
        if (this.mPhotosToBeSynced > 0) {
            HashMap hashMap = new HashMap();
            hashMap.put(MiStat.Param.COUNT, String.valueOf(this.mPhotosToBeSynced));
            SamplingStatHelper.recordCountEvent("items_to_be_synced", "sharer_photos_to_be_synced", hashMap);
        }
        if (this.mVideosToBeSynced > 0) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put(MiStat.Param.COUNT, String.valueOf(this.mVideosToBeSynced));
            SamplingStatHelper.recordCountEvent("items_to_be_synced", "sharer_videos_to_be_synced", hashMap2);
        }
        if (this.mLargestElapsedTime > 0) {
            HashMap hashMap3 = new HashMap();
            hashMap3.put("elapse_time", String.valueOf(this.mLargestElapsedTime));
            SamplingStatHelper.recordCountEvent("items_to_be_synced", "sharer_largest_elapsed_time", hashMap3);
        }
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public Uri getBaseUri() {
        return GalleryCloudUtils.SHARE_IMAGE_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    /* renamed from: generateDBImage */
    public DBImage mo689generateDBImage(Cursor cursor) {
        return new DBShareImage(cursor);
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public String getSelectionClause() {
        return DatabaseUtils.concatenateWhere(super.getSelectionClause(), String.format(Locale.US, "((serverType = 1 AND size < %s) OR (serverType = 2 AND size < %s))", Long.valueOf(CloudUtils.getMaxImageSizeLimit()), Long.valueOf(CloudUtils.getMaxVideoSizeLimit())));
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void initRequestCloudItemList() {
        this.mManualCreateSharerImageItems = new ArrayList<>();
        this.mCopyImageToSharerItems = new ArrayList<>();
        this.mDeleteShareImageItems = new ArrayList<>();
        this.mManualCreateSharerVideoItems = Lists.newArrayList();
        this.mMoveImageToSharerItems = Lists.newArrayList();
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void putToRequestCloudItemList(DBItem dBItem) {
        DBImage dBImage = (DBImage) dBItem;
        int localFlag = dBImage.getLocalFlag();
        if (localFlag == 2) {
            this.mDeleteShareImageItems.add(new RequestCloudItem(0, dBImage));
        } else if (localFlag == 11) {
            SyncLogger.d("SyncSharerFromLocal", "don't handle move from flag.");
        } else if (localFlag != 5) {
            if (localFlag != 6) {
                if (localFlag == 8) {
                    int serverType = dBImage.getServerType();
                    if (serverType == 1) {
                        this.mManualCreateSharerImageItems.add(new RequestCloudItem(5, dBImage));
                        this.mPhotosToBeSynced++;
                        this.mLargestElapsedTime = Math.max(this.mLargestElapsedTime, System.currentTimeMillis() - dBImage.getDateModified());
                        return;
                    } else if (serverType != 2) {
                        return;
                    } else {
                        this.mManualCreateSharerVideoItems.add(new RequestCloudItem(3, dBImage));
                        this.mVideosToBeSynced++;
                        this.mLargestElapsedTime = Math.max(this.mLargestElapsedTime, System.currentTimeMillis() - dBImage.getDateModified());
                        return;
                    }
                } else if (localFlag != 9) {
                    SyncLogger.e("SyncSharerFromLocal", "unsupport local flag %d", Integer.valueOf(dBImage.getLocalFlag()));
                    return;
                }
            }
            this.mCopyImageToSharerItems.add(new RequestCloudItem(0, dBImage));
        } else if (!dBImage.isItemType()) {
        } else {
            this.mMoveImageToSharerItems.add(new RequestCloudItem(0, dBImage));
        }
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void handleRequestCloudItemList() throws Exception {
        if (this.mManualCreateSharerVideoItems.size() > 0) {
            SyncLogger.v("SyncSharerFromLocal", "start upload manual create videos");
            UpDownloadManager.dispatchList(this.mManualCreateSharerVideoItems);
        }
        if (this.mManualCreateSharerImageItems.size() > 0) {
            SyncLogger.v("SyncSharerFromLocal", "start upload manual create images");
            UpDownloadManager.dispatchList(this.mManualCreateSharerImageItems);
        }
        if (this.mCopyImageToSharerItems.size() > 0) {
            SyncLogger.v("SyncSharerFromLocal", "start copy image items");
            GallerySyncResult doOperation = RetryOperation.doOperation(this.mContext, this.mAccount, this.mExtendedAuthToken, this.mCopyImageToSharerItems, new CopyImageToSharerAlbum(this.mContext));
            if (doOperation != null && doOperation.code == GallerySyncCode.CONDITION_INTERRUPTED) {
                return;
            }
        }
        if (this.mMoveImageToSharerItems.size() > 0) {
            SyncLogger.v("SyncSharerFromLocal", "start move image items");
            GallerySyncResult doOperation2 = RetryOperation.doOperation(this.mContext, this.mAccount, this.mExtendedAuthToken, this.mMoveImageToSharerItems, new MoveItemToSharerAlbum(this.mContext));
            if (doOperation2 != null && doOperation2.code == GallerySyncCode.CONDITION_INTERRUPTED) {
                return;
            }
        }
        if (this.mDeleteShareImageItems.size() > 0) {
            SyncLogger.v("SyncSharerFromLocal", "start delete image items");
            if (RetryOperation.doOperation(this.mContext, this.mAccount, this.mExtendedAuthToken, this.mDeleteShareImageItems, new DeleteCloudItem(this.mContext)) == null) {
                return;
            }
            GallerySyncCode gallerySyncCode = GallerySyncCode.CONDITION_INTERRUPTED;
        }
    }
}
