package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.text.TextUtils;
import com.google.common.collect.Lists;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.operation.EditCloudFavoriteInfo;
import com.miui.gallery.cloud.operation.EditCloudGeoInfo;
import com.miui.gallery.cloud.operation.EditCloudItem;
import com.miui.gallery.cloud.operation.EditCloudItemName;
import com.miui.gallery.cloud.operation.PurgeCloudItem;
import com.miui.gallery.cloud.operation.RecoveryCloudItem;
import com.miui.gallery.cloud.operation.ReplaceAlbumCoverRequest;
import com.miui.gallery.cloud.operation.copy.CopyImageToOwnerAlbum;
import com.miui.gallery.cloud.operation.create.CreateGroupItem;
import com.miui.gallery.cloud.operation.delete.DeleteAlbumItem;
import com.miui.gallery.cloud.operation.delete.DeleteCloudItem;
import com.miui.gallery.cloud.operation.editextra.EditAlbumDescription;
import com.miui.gallery.cloud.operation.editextra.EditAlbumThumbnailInfo;
import com.miui.gallery.cloud.operation.move.MoveItemToOwnerAlbum;
import com.miui.gallery.cloud.operation.rename.RenameAlbumItem;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBCloud;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.ProcessingMediaManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SyncLogger;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

/* loaded from: classes.dex */
public class SyncOwnerDataFromLocal {
    public static final int[] PUSH_SORT = {1, 7, 8, 9, 10, 11, 12, 13, 14, 4, 5, 15, 2, 3, 16, 17, 18, 6, 19};
    public static final Object SUCCESS = new Object();
    public final List<SyncFromLocalBase> mSyncExecutors;
    public final TaskContainer mSyncTaskContainer;

    public SyncOwnerDataFromLocal(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, boolean z) {
        TaskContainer taskContainer = new TaskContainer();
        this.mSyncTaskContainer = taskContainer;
        LinkedList linkedList = new LinkedList();
        this.mSyncExecutors = linkedList;
        linkedList.add(new SyncOwnerAlbumFromLocal(context, account, galleryExtendedAuthToken, z, taskContainer));
        linkedList.add(new SyncOwnerCloudFromLocal(context, account, galleryExtendedAuthToken, z, taskContainer));
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x005d, code lost:
        com.miui.gallery.util.SyncLogger.e("SyncOwnerDataFromLocal", "push owner data error,please see above log...");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void sync() throws java.lang.Exception {
        /*
            r9 = this;
            java.lang.String r0 = "syncOwnerDataFromLocal is success"
            java.lang.String r1 = "SyncOwnerDataFromLocal"
            java.util.LinkedList r2 = new java.util.LinkedList     // Catch: java.lang.Throwable -> L74
            java.util.List<com.miui.gallery.cloud.SyncFromLocalBase> r3 = r9.mSyncExecutors     // Catch: java.lang.Throwable -> L74
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L74
        Lb:
            java.util.Iterator r3 = r2.iterator()     // Catch: java.lang.Throwable -> L74
        Lf:
            boolean r4 = r3.hasNext()     // Catch: java.lang.Throwable -> L74
            if (r4 == 0) goto L25
            java.lang.Object r4 = r3.next()     // Catch: java.lang.Throwable -> L74
            com.miui.gallery.cloud.SyncFromLocalBase r4 = (com.miui.gallery.cloud.SyncFromLocalBase) r4     // Catch: java.lang.Throwable -> L74
            boolean r4 = r4.startOrContinueBatch()     // Catch: java.lang.Throwable -> L74
            if (r4 != 0) goto Lf
            r3.remove()     // Catch: java.lang.Throwable -> L74
            goto Lf
        L25:
            com.miui.gallery.cloud.SyncOwnerDataFromLocal$TaskContainer r3 = r9.mSyncTaskContainer     // Catch: java.lang.Throwable -> L74
            boolean r3 = r3.isValid()     // Catch: java.lang.Throwable -> L74
            if (r3 == 0) goto L62
            com.miui.gallery.cloud.SyncOwnerDataFromLocal$TaskContainer r3 = r9.mSyncTaskContainer     // Catch: java.lang.Throwable -> L74
            java.util.Map r3 = r3.getContainer()     // Catch: java.lang.Throwable -> L74
            int[] r4 = com.miui.gallery.cloud.SyncOwnerDataFromLocal.PUSH_SORT     // Catch: java.lang.Throwable -> L74
            int r5 = r4.length     // Catch: java.lang.Throwable -> L74
            r6 = 0
        L37:
            if (r6 >= r5) goto L6e
            r7 = r4[r6]     // Catch: java.lang.Throwable -> L74
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Throwable -> L74
            java.lang.Object r7 = r3.get(r7)     // Catch: java.lang.Throwable -> L74
            java.util.List r7 = (java.util.List) r7     // Catch: java.lang.Throwable -> L74
            if (r7 == 0) goto L6b
            java.util.Iterator r7 = r7.iterator()     // Catch: java.lang.Throwable -> L74
        L4b:
            boolean r8 = r7.hasNext()     // Catch: java.lang.Throwable -> L74
            if (r8 == 0) goto L6b
            java.lang.Object r8 = r7.next()     // Catch: java.lang.Throwable -> L74
            java.util.concurrent.Callable r8 = (java.util.concurrent.Callable) r8     // Catch: java.lang.Throwable -> L74
            java.lang.Object r8 = r8.call()     // Catch: java.lang.Throwable -> L74
            if (r8 != 0) goto L4b
            java.lang.String r2 = "push owner data error,please see above log..."
            com.miui.gallery.util.SyncLogger.e(r1, r2)     // Catch: java.lang.Throwable -> L74
        L62:
            com.miui.gallery.cloud.SyncOwnerDataFromLocal$TaskContainer r2 = r9.mSyncTaskContainer
            r2.clear()
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r0)
            return
        L6b:
            int r6 = r6 + 1
            goto L37
        L6e:
            com.miui.gallery.cloud.SyncOwnerDataFromLocal$TaskContainer r3 = r9.mSyncTaskContainer     // Catch: java.lang.Throwable -> L74
            r3.clear()     // Catch: java.lang.Throwable -> L74
            goto Lb
        L74:
            r2 = move-exception
            com.miui.gallery.cloud.SyncOwnerDataFromLocal$TaskContainer r3 = r9.mSyncTaskContainer
            r3.clear()
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.SyncOwnerDataFromLocal.sync():void");
    }

    /* loaded from: classes.dex */
    public static class SyncOwnerAlbumFromLocal extends SyncFromLocalBase {
        public ArrayList<RequestAlbumItem> mCreateGroupItems;
        public ArrayList<RequestAlbumItem> mDeleteGroupItems;
        public ArrayList<RequestAlbumItem> mEditCloudThumbnailInfoItems;
        public ArrayList<RequestAlbumItem> mEditGroupDescriptionItems;
        public boolean mNoDelay;
        public ArrayList<RequestAlbumItem> mRenameGroupItems;
        public ArrayList<RequestAlbumItem> mReplaceAlbumCoverItems;
        public final TaskContainer mTaskContainer;

        public SyncOwnerAlbumFromLocal(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, boolean z, TaskContainer taskContainer) {
            super(context, account, galleryExtendedAuthToken);
            this.mNoDelay = z;
            this.mTaskContainer = taskContainer;
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public Uri getBaseUri() {
            return GalleryCloudUtils.ALBUM_URI;
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        /* renamed from: generateDBImage */
        public DBAlbum mo689generateDBImage(Cursor cursor) {
            return new DBAlbum(cursor);
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public String getSelectionClause() {
            return String.format(" (%s) ", CloudUtils.SELECTION_OWNER_ALBUM_NEED_SYNC);
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public void initRequestCloudItemList() {
            this.mCreateGroupItems = new ArrayList<>();
            this.mDeleteGroupItems = new ArrayList<>();
            this.mRenameGroupItems = new ArrayList<>();
            this.mEditCloudThumbnailInfoItems = Lists.newArrayList();
            this.mEditGroupDescriptionItems = Lists.newArrayList();
            this.mReplaceAlbumCoverItems = Lists.newArrayList();
        }

        public final boolean shouldSyncEditedThumbnailInfo(DBAlbum dBAlbum) {
            ThumbnailInfo thumbnailInfo;
            long localFlag = dBAlbum.getLocalFlag();
            if ((localFlag == 0 || localFlag == 10 || localFlag == 8) && dBAlbum.getEditedColumns().contains(GalleryCloudUtils.transformToEditedColumnsElement(23)) && (thumbnailInfo = dBAlbum.getThumbnailInfo()) != null) {
                long bgImageLocalId = thumbnailInfo.getBgImageLocalId();
                if (bgImageLocalId == -1) {
                    return true;
                }
                DBImage item = CloudUtils.getItem(GalleryContract.Cloud.CLOUD_URI, this.mContext, j.c, String.valueOf(bgImageLocalId));
                return (item == null || 8 == item.getLocalFlag()) ? false : true;
            }
            return false;
        }

        public final boolean shouldSyncEditedDescription(DBAlbum dBAlbum) {
            long localFlag = dBAlbum.getLocalFlag();
            if (localFlag == 0 || localFlag == 10) {
                return dBAlbum.getEditedColumns().contains(GalleryCloudUtils.transformToEditedColumnsElement(22));
            }
            return false;
        }

        public final boolean shouldSyncEditedAlbumCover(DBAlbum dBAlbum) {
            long localFlag = dBAlbum.getLocalFlag();
            if (localFlag == 0 || localFlag == 10 || localFlag == 8) {
                return dBAlbum.getEditedColumns().contains(GalleryCloudUtils.transformToEditedColumnsElement(3));
            }
            return false;
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public void putToRequestCloudItemList(DBItem dBItem) {
            DBAlbum dBAlbum = (DBAlbum) dBItem;
            if (!TextUtils.isEmpty(dBAlbum.getEditedColumns())) {
                if (shouldSyncEditedThumbnailInfo(dBAlbum)) {
                    this.mEditCloudThumbnailInfoItems.add(new RequestAlbumItem(0, dBAlbum, this.mNoDelay));
                }
                if (shouldSyncEditedDescription(dBAlbum)) {
                    this.mEditGroupDescriptionItems.add(new RequestAlbumItem(0, dBAlbum, this.mNoDelay));
                }
                if (shouldSyncEditedAlbumCover(dBAlbum)) {
                    this.mReplaceAlbumCoverItems.add(new RequestAlbumItem(0, dBAlbum, this.mNoDelay));
                }
            }
            int localFlag = (int) dBAlbum.getLocalFlag();
            if (localFlag == 2) {
                this.mDeleteGroupItems.add(new RequestAlbumItem(0, dBAlbum, this.mNoDelay));
            } else if (localFlag == 10) {
                RequestAlbumItem requestAlbumItem = new RequestAlbumItem(0, dBAlbum, this.mNoDelay);
                if (TextUtils.isEmpty(dBAlbum.getServerId())) {
                    this.mCreateGroupItems.add(requestAlbumItem);
                    SamplingStatHelper.recordCountEvent("Sync", "sync_album_renamed_before_sync", new HashMap());
                    return;
                }
                this.mRenameGroupItems.add(requestAlbumItem);
            } else if (localFlag == 7 || localFlag == 8) {
                this.mCreateGroupItems.add(new RequestAlbumItem(0, dBAlbum, this.mNoDelay));
            } else if (!TextUtils.isEmpty(dBAlbum.getEditedColumns())) {
            } else {
                SyncLogger.e("SyncOwnerFromLocal", "unsupport local flag: %d", Long.valueOf(dBAlbum.getLocalFlag()));
            }
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public void handleRequestCloudItemList() throws Exception {
            if (this.mCreateGroupItems.size() > 0) {
                this.mTaskContainer.put(1, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerAlbumFromLocal.1
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start create group items");
                        CreateGroupItem createGroupItem = new CreateGroupItem(SyncOwnerAlbumFromLocal.this.mContext);
                        SyncOwnerAlbumFromLocal syncOwnerAlbumFromLocal = SyncOwnerAlbumFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerAlbumFromLocal.mContext, syncOwnerAlbumFromLocal.mAccount, syncOwnerAlbumFromLocal.mExtendedAuthToken, syncOwnerAlbumFromLocal.mCreateGroupItems, createGroupItem);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mCreateGroupItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mDeleteGroupItems.size() > 0) {
                this.mTaskContainer.put(4, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerAlbumFromLocal.2
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start delete group items");
                        DeleteAlbumItem deleteAlbumItem = new DeleteAlbumItem(SyncOwnerAlbumFromLocal.this.mContext);
                        SyncOwnerAlbumFromLocal syncOwnerAlbumFromLocal = SyncOwnerAlbumFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerAlbumFromLocal.mContext, syncOwnerAlbumFromLocal.mAccount, syncOwnerAlbumFromLocal.mExtendedAuthToken, syncOwnerAlbumFromLocal.mDeleteGroupItems, deleteAlbumItem);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mDeleteGroupItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mRenameGroupItems.size() > 0) {
                this.mTaskContainer.put(5, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerAlbumFromLocal.3
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start rename group items");
                        RenameAlbumItem renameAlbumItem = new RenameAlbumItem(SyncOwnerAlbumFromLocal.this.mContext);
                        SyncOwnerAlbumFromLocal syncOwnerAlbumFromLocal = SyncOwnerAlbumFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerAlbumFromLocal.mContext, syncOwnerAlbumFromLocal.mAccount, syncOwnerAlbumFromLocal.mExtendedAuthToken, syncOwnerAlbumFromLocal.mRenameGroupItems, renameAlbumItem);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mRenameGroupItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mEditCloudThumbnailInfoItems.size() > 0) {
                this.mTaskContainer.put(2, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerAlbumFromLocal.4
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start album thumbnail info");
                        EditAlbumThumbnailInfo editAlbumThumbnailInfo = new EditAlbumThumbnailInfo(SyncOwnerAlbumFromLocal.this.mContext);
                        SyncOwnerAlbumFromLocal syncOwnerAlbumFromLocal = SyncOwnerAlbumFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerAlbumFromLocal.mContext, syncOwnerAlbumFromLocal.mAccount, syncOwnerAlbumFromLocal.mExtendedAuthToken, syncOwnerAlbumFromLocal.mEditCloudThumbnailInfoItems, editAlbumThumbnailInfo);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mEditCloudThumbnailInfoItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mEditGroupDescriptionItems.size() > 0) {
                this.mTaskContainer.put(3, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerAlbumFromLocal.5
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start edit album description");
                        EditAlbumDescription editAlbumDescription = new EditAlbumDescription(SyncOwnerAlbumFromLocal.this.mContext);
                        SyncOwnerAlbumFromLocal syncOwnerAlbumFromLocal = SyncOwnerAlbumFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerAlbumFromLocal.mContext, syncOwnerAlbumFromLocal.mAccount, syncOwnerAlbumFromLocal.mExtendedAuthToken, syncOwnerAlbumFromLocal.mEditGroupDescriptionItems, editAlbumDescription);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mEditGroupDescriptionItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mReplaceAlbumCoverItems.size() > 0) {
                this.mTaskContainer.put(6, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerAlbumFromLocal.6
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start replace album cover");
                        ReplaceAlbumCoverRequest replaceAlbumCoverRequest = new ReplaceAlbumCoverRequest(SyncOwnerAlbumFromLocal.this.mContext);
                        SyncOwnerAlbumFromLocal syncOwnerAlbumFromLocal = SyncOwnerAlbumFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerAlbumFromLocal.mContext, syncOwnerAlbumFromLocal.mAccount, syncOwnerAlbumFromLocal.mExtendedAuthToken, syncOwnerAlbumFromLocal.mReplaceAlbumCoverItems, replaceAlbumCoverRequest);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mEditGroupDescriptionItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
        }
    }

    /* loaded from: classes.dex */
    public static class SyncOwnerCloudFromLocal extends SyncFromLocalBase {
        public ArrayList<RequestCloudItem> mAutoCreateImageItems;
        public ArrayList<RequestCloudItem> mAutoCreateVideoItems;
        public ArrayList<RequestCloudItem> mCopyImageToOwnerItems;
        public ArrayList<RequestCloudItem> mDeleteImageItems;
        public ArrayList<RequestCloudItem> mEditFavoriteInfoItems;
        public ArrayList<RequestCloudItem> mEditGeoInfoItems;
        public ArrayList<RequestCloudItem> mEditNameItems;
        public ArrayList<RequestCloudItem> mEditedItems;
        public long mLargestElapsedTime;
        public ArrayList<RequestCloudItem> mManualCreateOwnerImageItems;
        public ArrayList<RequestCloudItem> mManualCreateOwnerVideoItems;
        public ArrayList<RequestCloudItem> mMoveImageToOwnerItems;
        public boolean mNoDelay;
        public int mPhotosToBeSynced;
        public ArrayList<RequestCloudItem> mPurgeItems;
        public ArrayList<RequestCloudItem> mRevoceryItems;
        public final TaskContainer mTaskContainer;
        public int mVideosToBeSynced;

        public SyncOwnerCloudFromLocal(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, boolean z, TaskContainer taskContainer) {
            super(context, account, galleryExtendedAuthToken);
            this.mNoDelay = z;
            this.mTaskContainer = taskContainer;
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
                SamplingStatHelper.recordCountEvent("items_to_be_synced", "owner_photos_to_be_synced", hashMap);
            }
            if (this.mVideosToBeSynced > 0) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put(MiStat.Param.COUNT, String.valueOf(this.mVideosToBeSynced));
                SamplingStatHelper.recordCountEvent("items_to_be_synced", "owner_videos_to_be_synced", hashMap2);
            }
            if (this.mLargestElapsedTime > 0) {
                HashMap hashMap3 = new HashMap();
                hashMap3.put("elapse_time", String.valueOf(this.mLargestElapsedTime));
                SamplingStatHelper.recordCountEvent("items_to_be_synced", "owner_largest_elapsed_time", hashMap3);
            }
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public Uri getBaseUri() {
            return GalleryCloudUtils.CLOUD_URI;
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        /* renamed from: generateDBImage */
        public DBImage mo689generateDBImage(Cursor cursor) {
            return new DBCloud(cursor);
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public String getSelectionClause() {
            String concatenateWhere = DatabaseUtils.concatenateWhere(CloudUtils.SELECTION_OWNER_NEED_SYNC, String.format(Locale.US, "((serverType = 1 AND size < %s) OR (serverType = 2 AND size < %s))", Long.valueOf(CloudUtils.getMaxImageSizeLimit()), Long.valueOf(CloudUtils.getMaxVideoSizeLimit())));
            List<String> queryProcessingMediaPaths = ProcessingMediaManager.queryProcessingMediaPaths();
            if (BaseMiscUtil.isValid(queryProcessingMediaPaths)) {
                StringBuilder sb = new StringBuilder("localFile NOT IN (");
                for (int i = 0; i < queryProcessingMediaPaths.size(); i++) {
                    if (!TextUtils.isEmpty(queryProcessingMediaPaths.get(i))) {
                        DatabaseUtils.appendEscapedSQLString(sb, queryProcessingMediaPaths.get(i));
                        if (i != queryProcessingMediaPaths.size() - 1) {
                            sb.append(", ");
                        }
                    }
                }
                sb.append(")");
                return DatabaseUtils.concatenateWhere(concatenateWhere, sb.toString());
            }
            return String.format(" (%s) ", concatenateWhere);
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public void initRequestCloudItemList() {
            this.mManualCreateOwnerImageItems = new ArrayList<>();
            this.mAutoCreateImageItems = new ArrayList<>();
            this.mCopyImageToOwnerItems = new ArrayList<>();
            this.mMoveImageToOwnerItems = new ArrayList<>();
            this.mDeleteImageItems = new ArrayList<>();
            this.mRevoceryItems = new ArrayList<>();
            this.mPurgeItems = new ArrayList<>();
            this.mAutoCreateVideoItems = Lists.newArrayList();
            this.mManualCreateOwnerVideoItems = Lists.newArrayList();
            this.mEditedItems = Lists.newArrayList();
            this.mEditGeoInfoItems = Lists.newArrayList();
            this.mEditFavoriteInfoItems = Lists.newArrayList();
            this.mEditNameItems = Lists.newArrayList();
        }

        public final boolean shouldSyncEditedItem(DBImage dBImage) {
            int localFlag = dBImage.getLocalFlag();
            if (localFlag == 0 || localFlag == 5 || localFlag == 6 || localFlag == 9) {
                return dBImage.isUbiFocus() && dBImage.getEditedColumns().contains(GalleryCloudUtils.getFocusIndexColumnElement(dBImage.isShareItem()));
            }
            return false;
        }

        public final boolean shouldSyncEditedGeoInfo(DBImage dBImage) {
            int localFlag = dBImage.getLocalFlag();
            if (localFlag == 0 || localFlag == 5 || localFlag == 6 || localFlag == 9) {
                return dBImage.getEditedColumns().contains(GalleryCloudUtils.transformToEditedColumnsElement(70));
            }
            return false;
        }

        public final boolean shouldSyncFavoriteInfo(DBImage dBImage) {
            int localFlag = dBImage.getLocalFlag();
            if (localFlag == 0 || localFlag == 5 || localFlag == 6 || localFlag == 9) {
                return dBImage.getEditedColumns().contains(GalleryCloudUtils.transformToEditedColumnsElement(-1));
            }
            return false;
        }

        public final boolean shouldSyncEditItemName(DBImage dBImage) {
            int localFlag = dBImage.getLocalFlag();
            if (localFlag == 0 || localFlag == 5 || localFlag == 6 || localFlag == 9) {
                return dBImage.getEditedColumns().contains(GalleryCloudUtils.transformToEditedColumnsElement(7));
            }
            return false;
        }

        public final boolean isStatusSupportUpdateImage(DBImage dBImage) {
            String serverStatus = dBImage.getServerStatus();
            if (!TextUtils.isEmpty(serverStatus)) {
                return !serverStatus.equalsIgnoreCase("recovery") && !serverStatus.equalsIgnoreCase("toBePurged");
            }
            return true;
        }

        public final boolean shoudSyncRecovery(DBImage dBImage) {
            String serverStatus = dBImage.getServerStatus();
            return !TextUtils.isEmpty(serverStatus) && serverStatus.equalsIgnoreCase("recovery");
        }

        public final boolean shoudSyncPurge(DBImage dBImage) {
            String serverStatus = dBImage.getServerStatus();
            return !TextUtils.isEmpty(serverStatus) && serverStatus.equalsIgnoreCase("toBePurged");
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public void putToRequestCloudItemList(DBItem dBItem) {
            DBImage dBImage = (DBImage) dBItem;
            if (!TextUtils.isEmpty(dBImage.getEditedColumns()) && dBImage.isItemType() && isStatusSupportUpdateImage(dBImage)) {
                if (shouldSyncEditedItem(dBImage)) {
                    this.mEditedItems.add(new RequestCloudItem(0, dBImage, this.mNoDelay));
                }
                if (shouldSyncEditedGeoInfo(dBImage)) {
                    this.mEditGeoInfoItems.add(new RequestCloudItem(0, dBImage, this.mNoDelay));
                }
                if (shouldSyncFavoriteInfo(dBImage)) {
                    this.mEditFavoriteInfoItems.add(new RequestCloudItem(0, dBImage, this.mNoDelay));
                }
                if (shouldSyncEditItemName(dBImage)) {
                    this.mEditNameItems.add(new RequestCloudItem(0, dBImage, this.mNoDelay));
                }
            }
            if (dBImage.isItemType()) {
                if (shoudSyncPurge(dBImage)) {
                    this.mPurgeItems.add(new RequestCloudItem(0, dBImage, this.mNoDelay));
                }
                if (shoudSyncRecovery(dBImage)) {
                    this.mRevoceryItems.add(new RequestCloudItem(0, dBImage, this.mNoDelay));
                }
            }
            switch (dBImage.getLocalFlag()) {
                case 2:
                    RequestCloudItem requestCloudItem = new RequestCloudItem(0, dBImage, this.mNoDelay);
                    if (!requestCloudItem.dbCloud.isItemType()) {
                        return;
                    }
                    this.mDeleteImageItems.add(requestCloudItem);
                    return;
                case 3:
                case 10:
                default:
                    if (!TextUtils.isEmpty(dBImage.getEditedColumns())) {
                        return;
                    }
                    SyncLogger.e("SyncOwnerFromLocal", "unsupport local flag: %d", Integer.valueOf(dBImage.getLocalFlag()));
                    return;
                case 4:
                    int serverType = dBImage.getServerType();
                    if (serverType == 1) {
                        this.mAutoCreateImageItems.add(new RequestCloudItem(4, dBImage, this.mNoDelay));
                        return;
                    } else if (serverType == 2) {
                        this.mAutoCreateVideoItems.add(new RequestCloudItem(2, dBImage, this.mNoDelay));
                        return;
                    } else {
                        SyncLogger.e("SyncOwnerFromLocal", "unsupport local flag= %d serverType= %d", Integer.valueOf(dBImage.getLocalFlag()), Integer.valueOf(dBImage.getServerType()));
                        return;
                    }
                case 5:
                    if (!dBImage.isItemType()) {
                        return;
                    }
                    this.mMoveImageToOwnerItems.add(new RequestCloudItem(0, dBImage, this.mNoDelay));
                    return;
                case 6:
                case 9:
                    if (!dBImage.isItemType()) {
                        return;
                    }
                    this.mCopyImageToOwnerItems.add(new RequestCloudItem(0, dBImage, this.mNoDelay));
                    return;
                case 7:
                    int serverType2 = dBImage.getServerType();
                    if (serverType2 == 1) {
                        this.mAutoCreateImageItems.add(new RequestCloudItem(4, dBImage, this.mNoDelay));
                        this.mPhotosToBeSynced++;
                        this.mLargestElapsedTime = Math.max(this.mLargestElapsedTime, System.currentTimeMillis() - dBImage.getDateModified());
                        return;
                    } else if (serverType2 == 2) {
                        this.mAutoCreateVideoItems.add(new RequestCloudItem(2, dBImage, this.mNoDelay));
                        this.mVideosToBeSynced++;
                        this.mLargestElapsedTime = Math.max(this.mLargestElapsedTime, System.currentTimeMillis() - dBImage.getDateModified());
                        return;
                    } else {
                        SyncLogger.e("SyncOwnerFromLocal", "unsupport local flag= %d serverType= %d", Integer.valueOf(dBImage.getLocalFlag()), Integer.valueOf(dBImage.getServerType()));
                        return;
                    }
                case 8:
                    int serverType3 = dBImage.getServerType();
                    if (serverType3 == 1) {
                        this.mManualCreateOwnerImageItems.add(new RequestCloudItem(5, dBImage, this.mNoDelay));
                        this.mPhotosToBeSynced++;
                        this.mLargestElapsedTime = Math.max(this.mLargestElapsedTime, System.currentTimeMillis() - dBImage.getDateModified());
                        return;
                    } else if (serverType3 == 2) {
                        this.mManualCreateOwnerVideoItems.add(new RequestCloudItem(3, dBImage, this.mNoDelay));
                        this.mVideosToBeSynced++;
                        this.mLargestElapsedTime = Math.max(this.mLargestElapsedTime, System.currentTimeMillis() - dBImage.getDateModified());
                        return;
                    } else {
                        SyncLogger.e("SyncOwnerFromLocal", "unsupport local flag=" + dBImage.getLocalFlag() + ", serverType=" + dBImage.getServerType());
                        return;
                    }
                case 11:
                    SyncLogger.d("SyncOwnerFromLocal", "don't handle move from flag.");
                    return;
            }
        }

        @Override // com.miui.gallery.cloud.SyncFromLocalBase
        public void handleRequestCloudItemList() throws Exception {
            if (this.mManualCreateOwnerVideoItems.size() > 0) {
                this.mTaskContainer.put(7, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.1
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start upload manual create videos");
                        UpDownloadManager.dispatchList(SyncOwnerCloudFromLocal.this.mManualCreateOwnerVideoItems);
                        return SyncOwnerDataFromLocal.SUCCESS;
                    }
                });
            }
            if (this.mAutoCreateVideoItems.size() > 0) {
                this.mTaskContainer.put(8, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.2
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start upload auto create videos");
                        UpDownloadManager.dispatchList(SyncOwnerCloudFromLocal.this.mAutoCreateVideoItems);
                        return SyncOwnerDataFromLocal.SUCCESS;
                    }
                });
            }
            if (this.mManualCreateOwnerImageItems.size() > 0) {
                this.mTaskContainer.put(9, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.3
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start upload manual create images");
                        UpDownloadManager.dispatchList(SyncOwnerCloudFromLocal.this.mManualCreateOwnerImageItems);
                        return SyncOwnerDataFromLocal.SUCCESS;
                    }
                });
            }
            if (this.mAutoCreateImageItems.size() > 0) {
                this.mTaskContainer.put(10, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.4
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start upload auto create images");
                        UpDownloadManager.dispatchList(SyncOwnerCloudFromLocal.this.mAutoCreateImageItems);
                        return SyncOwnerDataFromLocal.SUCCESS;
                    }
                });
            }
            if (this.mRevoceryItems.size() > 0) {
                this.mTaskContainer.put(11, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.5
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start recovery image items");
                        RecoveryCloudItem recoveryCloudItem = new RecoveryCloudItem(SyncOwnerCloudFromLocal.this.mContext);
                        SyncOwnerCloudFromLocal syncOwnerCloudFromLocal = SyncOwnerCloudFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerCloudFromLocal.mContext, syncOwnerCloudFromLocal.mAccount, syncOwnerCloudFromLocal.mExtendedAuthToken, syncOwnerCloudFromLocal.mRevoceryItems, recoveryCloudItem);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mRevoceryItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mCopyImageToOwnerItems.size() > 0) {
                this.mTaskContainer.put(12, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.6
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start copy image items");
                        CopyImageToOwnerAlbum copyImageToOwnerAlbum = new CopyImageToOwnerAlbum(SyncOwnerCloudFromLocal.this.mContext);
                        SyncOwnerCloudFromLocal syncOwnerCloudFromLocal = SyncOwnerCloudFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerCloudFromLocal.mContext, syncOwnerCloudFromLocal.mAccount, syncOwnerCloudFromLocal.mExtendedAuthToken, syncOwnerCloudFromLocal.mCopyImageToOwnerItems, copyImageToOwnerAlbum);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mCopyImageToOwnerItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mMoveImageToOwnerItems.size() > 0) {
                this.mTaskContainer.put(13, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.7
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start move image items");
                        MoveItemToOwnerAlbum moveItemToOwnerAlbum = new MoveItemToOwnerAlbum(SyncOwnerCloudFromLocal.this.mContext);
                        SyncOwnerCloudFromLocal syncOwnerCloudFromLocal = SyncOwnerCloudFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerCloudFromLocal.mContext, syncOwnerCloudFromLocal.mAccount, syncOwnerCloudFromLocal.mExtendedAuthToken, syncOwnerCloudFromLocal.mMoveImageToOwnerItems, moveItemToOwnerAlbum);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mMoveImageToOwnerItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mDeleteImageItems.size() > 0) {
                this.mTaskContainer.put(14, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.8
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start delete image items");
                        DeleteCloudItem deleteCloudItem = new DeleteCloudItem(SyncOwnerCloudFromLocal.this.mContext);
                        SyncOwnerCloudFromLocal syncOwnerCloudFromLocal = SyncOwnerCloudFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerCloudFromLocal.mContext, syncOwnerCloudFromLocal.mAccount, syncOwnerCloudFromLocal.mExtendedAuthToken, syncOwnerCloudFromLocal.mDeleteImageItems, deleteCloudItem);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mDeleteImageItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mEditedItems.size() > 0) {
                this.mTaskContainer.put(15, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.9
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start edit items");
                        EditCloudItem editCloudItem = new EditCloudItem(SyncOwnerCloudFromLocal.this.mContext);
                        SyncOwnerCloudFromLocal syncOwnerCloudFromLocal = SyncOwnerCloudFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerCloudFromLocal.mContext, syncOwnerCloudFromLocal.mAccount, syncOwnerCloudFromLocal.mExtendedAuthToken, syncOwnerCloudFromLocal.mEditedItems, editCloudItem);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mEditedItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mEditGeoInfoItems.size() > 0) {
                this.mTaskContainer.put(16, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.10
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start edit geo info");
                        EditCloudGeoInfo editCloudGeoInfo = new EditCloudGeoInfo(SyncOwnerCloudFromLocal.this.mContext);
                        SyncOwnerCloudFromLocal syncOwnerCloudFromLocal = SyncOwnerCloudFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerCloudFromLocal.mContext, syncOwnerCloudFromLocal.mAccount, syncOwnerCloudFromLocal.mExtendedAuthToken, syncOwnerCloudFromLocal.mEditGeoInfoItems, editCloudGeoInfo);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mEditGeoInfoItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mEditFavoriteInfoItems.size() > 0) {
                this.mTaskContainer.put(17, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.11
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start edit favorite info");
                        EditCloudFavoriteInfo editCloudFavoriteInfo = new EditCloudFavoriteInfo(SyncOwnerCloudFromLocal.this.mContext);
                        SyncOwnerCloudFromLocal syncOwnerCloudFromLocal = SyncOwnerCloudFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerCloudFromLocal.mContext, syncOwnerCloudFromLocal.mAccount, syncOwnerCloudFromLocal.mExtendedAuthToken, syncOwnerCloudFromLocal.mEditFavoriteInfoItems, editCloudFavoriteInfo);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mEditFavoriteInfoItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mEditNameItems.size() > 0) {
                this.mTaskContainer.put(18, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.12
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start edit item name info");
                        EditCloudItemName editCloudItemName = new EditCloudItemName(SyncOwnerCloudFromLocal.this.mContext);
                        SyncOwnerCloudFromLocal syncOwnerCloudFromLocal = SyncOwnerCloudFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerCloudFromLocal.mContext, syncOwnerCloudFromLocal.mAccount, syncOwnerCloudFromLocal.mExtendedAuthToken, syncOwnerCloudFromLocal.mEditNameItems, editCloudItemName);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mEditNameItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
            if (this.mPurgeItems.size() > 0) {
                this.mTaskContainer.put(19, new Callable() { // from class: com.miui.gallery.cloud.SyncOwnerDataFromLocal.SyncOwnerCloudFromLocal.13
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        SyncLogger.v("SyncOwnerFromLocal", "start purge image items");
                        PurgeCloudItem purgeCloudItem = new PurgeCloudItem(SyncOwnerCloudFromLocal.this.mContext);
                        SyncOwnerCloudFromLocal syncOwnerCloudFromLocal = SyncOwnerCloudFromLocal.this;
                        GallerySyncResult doOperation = RetryOperation.doOperation(syncOwnerCloudFromLocal.mContext, syncOwnerCloudFromLocal.mAccount, syncOwnerCloudFromLocal.mExtendedAuthToken, syncOwnerCloudFromLocal.mPurgeItems, purgeCloudItem);
                        if (doOperation == null || doOperation.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                            return SyncOwnerDataFromLocal.SUCCESS;
                        }
                        SyncLogger.e("SyncOwnerFromLocal", "mPurgeItems CONDITION_INTERRUPTED");
                        return null;
                    }
                });
            }
        }
    }

    /* loaded from: classes.dex */
    public static class TaskContainer {
        public final Map<Integer, List<Callable>> mContainer;

        public TaskContainer() {
            this.mContainer = new HashMap();
        }

        public void put(int i, Callable callable) {
            List<Callable> list = this.mContainer.get(Integer.valueOf(i));
            if (list == null) {
                list = new LinkedList<>();
                this.mContainer.put(Integer.valueOf(i), list);
            }
            list.add(callable);
        }

        public Map<Integer, List<Callable>> getContainer() {
            return this.mContainer;
        }

        public void clear() {
            this.mContainer.clear();
        }

        public boolean isValid() {
            return !this.mContainer.isEmpty();
        }
    }
}
