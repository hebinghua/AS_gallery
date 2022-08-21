package com.miui.gallery.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.Config$SecretAlbumConfig;
import com.miui.gallery.R;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.PhotoOperationsUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;

/* loaded from: classes2.dex */
public class CloudItem extends BaseDataItem {
    public transient OnCompleteListenerWrapper mAddToFavoritesListener;
    private String mCreatorId;
    private boolean mHasFace;
    private long mId;
    private int mIsFavorite;
    private boolean mIsShareItem;
    private boolean mIsSynced;
    private int mOrientation = 0;
    public transient OnCompleteListenerWrapper mRemoveFromFavoritesListener;
    private String mServerId;
    private String mSha1;
    public static final String[] SHARE_DETAIL_INFO_PROJECTION = {"fileName", "exifModel", "exifMake", "exifFNumber", "exifFocalLength", "exifISOSpeedRatings", "exifExposureTime", "exifFlash", "exifOrientation"};
    public static final String[] DETAIL_INFO_PROJECTION = {"fileName", "exifModel", "exifMake", "exifFNumber", "exifFocalLength", "exifISOSpeedRatings", "exifExposureTime", "exifFlash", "exifOrientation", "source_pkg"};

    public CloudItem setId(long j) {
        this.mId = j;
        return this;
    }

    public long getId() {
        return this.mId;
    }

    public CloudItem setShare(boolean z) {
        this.mIsShareItem = z;
        return this;
    }

    public CloudItem setSynced(boolean z) {
        this.mIsSynced = z;
        return this;
    }

    public boolean isShare() {
        return this.mIsShareItem;
    }

    public CloudItem setSha1(String str) {
        this.mSha1 = str;
        return this;
    }

    public String getSha1() {
        return this.mSha1;
    }

    public CloudItem setCreatorId(String str) {
        this.mCreatorId = str;
        return this;
    }

    public CloudItem setServerId(String str) {
        this.mServerId = str;
        return this;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    /* renamed from: setOrientation  reason: collision with other method in class */
    public CloudItem mo1096setOrientation(int i) {
        this.mOrientation = i;
        return this;
    }

    public CloudItem setIsFavorite(int i) {
        this.mIsFavorite = i;
        return this;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public BaseDataItem setFilePath(String str) {
        super.setFilePath(str);
        if (!needDownloadOrigin()) {
            removeSupportOperation(256L);
        }
        return this;
    }

    public void setHasFace(boolean z) {
        this.mHasFace = z;
    }

    public String getServerId() {
        return this.mServerId;
    }

    public boolean isEverSynced() {
        return !TextUtils.isEmpty(this.mServerId);
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public boolean isSynced() {
        return this.mIsSynced;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public int getOrientation() {
        return this.mOrientation;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public boolean hasFace() {
        return this.mHasFace;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public PhotoDetailInfo getDetailInfo(Context context) {
        String str;
        final boolean z;
        Uri uri;
        DocumentFile documentFile;
        final PhotoDetailInfo detailInfo = super.getDetailInfo(context);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudItem", "getDetailInfo");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String originalPath = getOriginalPath();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
        DocumentFile documentFile2 = storageStrategyManager.getDocumentFile(originalPath, permission, appendInvokerTag);
        if (documentFile2 == null || !documentFile2.exists()) {
            str = null;
            z = false;
        } else {
            str = getOriginalPath();
            z = true;
        }
        if (str == null && (documentFile = StorageSolutionProvider.get().getDocumentFile(getThumnailPath(), permission, appendInvokerTag)) != null && documentFile.exists()) {
            str = getThumnailPath();
        }
        if (!z) {
            if (isVideo() || isGif()) {
                detailInfo.removeDetail(200);
                detailInfo.addDetail(8, context.getString(R.string.tip_not_download));
            } else if (!TextUtils.isEmpty(str)) {
                detailInfo.addDetail(200, str);
                detailInfo.addDetail(2, BaseFileUtils.getFileName(str));
                detailInfo.addDetail(3, Long.valueOf(new File(str).length()));
                try {
                    BitmapFactory.Options bitmapSize = miuix.graphics.BitmapFactory.getBitmapSize(str);
                    detailInfo.addDetail(4, Integer.valueOf(bitmapSize.outWidth));
                    detailInfo.addDetail(5, Integer.valueOf(bitmapSize.outHeight));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (z) {
            PhotoDetailInfo.extractSmartFusionExifInfo(detailInfo, str);
        }
        if (isSecret()) {
            detailInfo.removeDetail(200);
        }
        SafeDBUtil.QueryHandler queryHandler = new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.model.CloudItem.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Object mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    DefaultLogger.e("CloudItem", "cursor is null or empty");
                    return detailInfo;
                }
                String string = cursor.getString(0);
                if (TextUtils.isEmpty((String) detailInfo.getDetail(2))) {
                    detailInfo.addDetail(2, string);
                } else if (CloudItem.this.isSecret()) {
                    if (z) {
                        detailInfo.addDetail(2, string);
                    } else {
                        PhotoDetailInfo photoDetailInfo = detailInfo;
                        photoDetailInfo.addDetail(2, BaseFileUtils.getFileTitle(string) + ".jpg");
                    }
                }
                detailInfo.addDetail(101, cursor.getString(1));
                detailInfo.addDetail(100, cursor.getString(2));
                detailInfo.addDetail(105, cursor.getString(3));
                String string2 = cursor.getString(4);
                if (!TextUtils.isEmpty(string2)) {
                    detailInfo.addDetail(103, PhotoDetailInfo.wrapFocalLength(string2));
                }
                detailInfo.addDetail(108, cursor.getString(5));
                detailInfo.addDetail(107, cursor.getString(6));
                detailInfo.addDetail(102, cursor.getString(7));
                detailInfo.addDetail(10, cursor.getString(8));
                int columnIndex = cursor.getColumnIndex("source_pkg");
                if (-1 != columnIndex) {
                    String string3 = cursor.getString(columnIndex);
                    if (!TextUtils.isEmpty(string3)) {
                        detailInfo.addDetail(9, PackageUtils.getAppNameByPackage(string3));
                    }
                }
                return detailInfo;
            }
        };
        if (isShare()) {
            uri = GalleryContract.ShareImage.SHARE_URI;
        } else {
            uri = GalleryContract.Cloud.CLOUD_URI;
        }
        return (PhotoDetailInfo) SafeDBUtil.safeQuery(context, uri, isShare() ? SHARE_DETAIL_INFO_PROJECTION : DETAIL_INFO_PROJECTION, "_id=?", new String[]{String.valueOf(isShare() ? ShareMediaManager.getOriginalMediaId(getId()) : getId())}, (String) null, queryHandler);
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public long initSupportOperations() {
        long imageSupportedOperations;
        String pathDisplayBetter = getPathDisplayBetter();
        if (isVideo()) {
            imageSupportedOperations = PhotoOperationsUtil.getVideoSupportedOperations(pathDisplayBetter);
        } else {
            imageSupportedOperations = PhotoOperationsUtil.getImageSupportedOperations(pathDisplayBetter, this.mMimeType, this.mLatitude, this.mLongitude);
        }
        long j = imageSupportedOperations | 65536 | 8388608 | 1;
        if (!isGif()) {
            j |= 512;
        }
        long j2 = needDownloadOrigin() ? j | 256 : j & (-257);
        if (isShare()) {
            long j3 = j2 & (-262145) & (-524289) & (-513);
            return !canDelete() ? j3 & (-2) : j3;
        } else if (isSecret()) {
            long j4 = j2 & (-262145) & (-65537) & (-33) & (-2049) & (-4097) & (-131073) & (-16777217);
            if (isVideo()) {
                j4 &= -513;
            }
            return j4 | 524288;
        } else {
            long j5 = ((!PicToPdfHelper.isPicToPdfSupport() || !PicToPdfHelper.isPicToPdfSupportType(this.mMimeType)) ? j2 & (-16777217) : j2 | 16777216) & (-524289);
            return ApplicationHelper.isSecretAlbumFeatureOpen() ? (!isVideo() || Config$SecretAlbumConfig.isVideoSupported()) ? j5 | 262144 : j5 : j5 & (-262145);
        }
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public Uri getDownloadUri() {
        return CloudUriAdapter.getDownloadUri(getId());
    }

    public boolean needDownloadOrigin() {
        if (!isBurstItem()) {
            return TextUtils.isEmpty(getOriginalPath()) && !TextUtils.isEmpty(getServerId());
        }
        for (BaseDataItem baseDataItem : getBurstGroup()) {
            if ((baseDataItem instanceof CloudItem) && TextUtils.isEmpty(baseDataItem.getOriginalPath()) && !TextUtils.isEmpty(((CloudItem) baseDataItem).getServerId())) {
                return true;
            }
        }
        return false;
    }

    public boolean canDelete() {
        return isOwner() || isMine();
    }

    public final boolean isOwner() {
        return !isShare();
    }

    public final boolean isCreatorFromAlbumOwner() {
        return TextUtils.isEmpty(this.mCreatorId) && this.mIsSynced;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public FavoriteInfo queryFavoriteInfo(boolean z) {
        return getFavoriteInfo();
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public FavoriteInfo getFavoriteInfo() {
        this.mFavoriteInfo.setFavorite(this.mIsFavorite > 0);
        return this.mFavoriteInfo;
    }

    public final boolean isCreatorEqualsCurrentAccount() {
        return TextUtils.equals(GalleryCloudUtils.getAccountName(), this.mCreatorId);
    }

    public boolean isMine() {
        return isShare() ? isCreatorEqualsCurrentAccount() || !this.mIsSynced : isCreatorFromAlbumOwner() || isCreatorEqualsCurrentAccount() || !this.mIsSynced;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public String getMicroPath() {
        return TextUtils.isEmpty(super.getMicroPath()) ? StorageUtils.getSafePriorMicroThumbnailPath(this.mSha1) : super.getMicroPath();
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public boolean isSecret() {
        return super.isSecret() || this.mLocalGroupId == -1000;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public boolean checkOriginalFileExist() {
        return super.checkOriginalFileExist() && new File(getOriginalPath()).length() >= getSize();
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public void onLoadCache() {
        super.onLoadCache();
        String originalPath = getOriginalPath();
        if (!TextUtils.isEmpty(originalPath) && isEverSynced() && new File(originalPath).length() < getSize()) {
            setFilePath(null);
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(getThumnailPath(), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("CloudItem", "onLoadCache"));
        if (documentFile == null || !documentFile.exists()) {
            setThumbPath(null);
        }
    }

    public MediaAndAlbumOperations.OnCompleteListener wrapAddToFavoritesListener(MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        OnCompleteListenerWrapper onCompleteListenerWrapper = this.mAddToFavoritesListener;
        if (onCompleteListenerWrapper == null || !Objects.equals(onCompleteListener, onCompleteListenerWrapper.mWrappedRef.get())) {
            this.mAddToFavoritesListener = new OnCompleteListenerWrapper(onCompleteListener, this, 1);
        }
        return this.mAddToFavoritesListener;
    }

    public MediaAndAlbumOperations.OnCompleteListener wrapRemoveFromFavoritesListener(MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        OnCompleteListenerWrapper onCompleteListenerWrapper = this.mRemoveFromFavoritesListener;
        if (onCompleteListenerWrapper == null || !Objects.equals(onCompleteListener, onCompleteListenerWrapper.mWrappedRef.get())) {
            this.mRemoveFromFavoritesListener = new OnCompleteListenerWrapper(onCompleteListener, this, 2);
        }
        return this.mRemoveFromFavoritesListener;
    }

    /* loaded from: classes2.dex */
    public static class OnCompleteListenerWrapper implements MediaAndAlbumOperations.OnCompleteListener {
        public final WeakReference<CloudItem> mCloudItemRef;
        public final int mOperationType;
        public final WeakReference<MediaAndAlbumOperations.OnCompleteListener> mWrappedRef;

        public OnCompleteListenerWrapper(MediaAndAlbumOperations.OnCompleteListener onCompleteListener, CloudItem cloudItem, int i) {
            this.mWrappedRef = new WeakReference<>(onCompleteListener);
            this.mCloudItemRef = new WeakReference<>(cloudItem);
            this.mOperationType = i;
        }

        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
        public void onComplete(long[] jArr) {
            CloudItem cloudItem;
            if (jArr != null) {
                int i = 0;
                if (jArr[0] > 0 && (cloudItem = this.mCloudItemRef.get()) != null) {
                    if (this.mOperationType == 1) {
                        i = 1;
                    }
                    cloudItem.setIsFavorite(i);
                }
            }
            MediaAndAlbumOperations.OnCompleteListener onCompleteListener = this.mWrappedRef.get();
            if (onCompleteListener != null) {
                onCompleteListener.onComplete(jArr);
            }
        }
    }
}
