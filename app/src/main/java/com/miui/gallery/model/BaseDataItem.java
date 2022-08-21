package com.miui.gallery.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.ui.SaveUriDialogFragment;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.util.VideoAttrsReader;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.gifdecoder.NSGifDecode;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import miuix.pickerwidget.date.DateUtils;

/* loaded from: classes2.dex */
public class BaseDataItem implements Serializable {
    public static String TAG = "BaseDataItem";
    public List<BaseDataItem> mBurstGroup;
    public String mContentDescription;
    public int mDisplayHeight;
    public int mDisplayWidth;
    public String mFilePath;
    public int mHeight;
    private boolean mIsBurstItem;
    public boolean mIsScreenshot;
    public boolean mIsSecret;
    private boolean mIsTimeBurst;
    public long mKey;
    public double mLatitude;
    public long mLocalGroupId;
    public String mLocation;
    public double mLongitude;
    public String mMicroPath;
    public String mMimeType;
    public volatile long mMotionOffset;
    public int mOrientation;
    public byte[] mSecretKey;
    public long mSize;
    public String mThumbPath;
    public String mTitle;
    private int[] mVideoResolution;
    public volatile transient List<Long> mVideoTags;
    public int mWidth;
    public int mDuration = -1;
    public volatile long mAIModeTypeFlags = -1;
    public volatile long mSpecialTypeFlags = -1;
    public transient boolean mIsSpecialTypeEditable = false;
    public transient ReentrantLock mCacheLock = new ReentrantLock();
    public volatile transient DisplayBetterPath mDisplayBetterPath = new DisplayBetterPath();
    public volatile transient long mSupportOperations = -1;
    public volatile transient long mDisplayBetterFileSize = -1;
    public FavoriteInfo mFavoriteInfo = new FavoriteInfo();
    public long mCreateTime = System.currentTimeMillis();

    public static /* synthetic */ Object $r8$lambda$Ic4V5etdPhS_CTnwAF8bpM_8B7E(BaseDataItem baseDataItem) {
        return baseDataItem.lambda$reloadCache$5();
    }

    public static /* synthetic */ Object $r8$lambda$NsWm4rHH5xZXdVFTq0xsmlUVPS4(BaseDataItem baseDataItem, long j) {
        return baseDataItem.lambda$removeSupportOperation$1(j);
    }

    public static /* synthetic */ Long $r8$lambda$WqccSWYy4PivwAuXdRCkeXqg9Nk(BaseDataItem baseDataItem) {
        return baseDataItem.lambda$getSupportOperations$0();
    }

    /* renamed from: $r8$lambda$dTvHfNf-vmPIef7N3kxfg000nCY */
    public static /* synthetic */ String m1092$r8$lambda$dTvHfNfvmPIef7N3kxfg000nCY(BaseDataItem baseDataItem) {
        return baseDataItem.lambda$getPathDisplayBetter$2();
    }

    /* renamed from: $r8$lambda$iYVCKzIn2lb88QhmD54F-W9CXDQ */
    public static /* synthetic */ Object m1093$r8$lambda$iYVCKzIn2lb88QhmD54FW9CXDQ(BaseDataItem baseDataItem, int i, String str) {
        return baseDataItem.lambda$setPathDisplayBetter$4(i, str);
    }

    public static /* synthetic */ Long $r8$lambda$la3KK2OgmfLPokybQPsurH2LT_s(BaseDataItem baseDataItem) {
        return baseDataItem.lambda$getDisplayBetterFileSize$3();
    }

    public Uri getDownloadUri() {
        return null;
    }

    public boolean hasFace() {
        return false;
    }

    public long initSupportOperations() {
        return 0L;
    }

    public void invalidCache() {
    }

    public boolean isNeedQueryFavoriteInfo() {
        return false;
    }

    public boolean isSynced() {
        return true;
    }

    public void save(FragmentActivity fragmentActivity, SaveUriDialogFragment.OnCompleteListener onCompleteListener) {
    }

    public BaseDataItem setKey(long j) {
        this.mKey = j;
        return this;
    }

    public BaseDataItem setTitle(String str) {
        this.mTitle = str;
        return this;
    }

    public BaseDataItem setMicroPath(String str) {
        this.mMicroPath = str;
        setPathDisplayBetter(1, str);
        return this;
    }

    public BaseDataItem setThumbPath(String str) {
        this.mThumbPath = str;
        setPathDisplayBetter(2, str);
        return this;
    }

    public BaseDataItem setFilePath(String str) {
        this.mFilePath = str;
        setPathDisplayBetter(3, str);
        return this;
    }

    public BaseDataItem setMimeType(String str) {
        this.mMimeType = str;
        return this;
    }

    public BaseDataItem setCreateTime(long j) {
        this.mCreateTime = j;
        return this;
    }

    public BaseDataItem setLocation(String str) {
        this.mLocation = str;
        return this;
    }

    public BaseDataItem setSize(long j) {
        this.mSize = j;
        return this;
    }

    public BaseDataItem setLatitude(double d) {
        this.mLatitude = d;
        return this;
    }

    public BaseDataItem setLongitude(double d) {
        this.mLongitude = d;
        return this;
    }

    public BaseDataItem setSecretKey(byte[] bArr) {
        if (bArr != null) {
            this.mIsSecret = true;
        } else {
            this.mIsSecret = false;
        }
        this.mSecretKey = bArr;
        return this;
    }

    public BaseDataItem setWidth(int i) {
        this.mWidth = i;
        return this;
    }

    public BaseDataItem setHeight(int i) {
        this.mHeight = i;
        return this;
    }

    public BaseDataItem setDisplayWidth(int i) {
        this.mDisplayWidth = i;
        return this;
    }

    public BaseDataItem setDisplayHeight(int i) {
        this.mDisplayHeight = i;
        return this;
    }

    public BaseDataItem setDuration(int i) {
        this.mDuration = i;
        return this;
    }

    public BaseDataItem setLocalGroupId(long j) {
        this.mLocalGroupId = j;
        return this;
    }

    /* renamed from: setOrientation */
    public BaseDataItem mo1096setOrientation(int i) {
        this.mOrientation = i;
        return this;
    }

    public BaseDataItem setBurstItem(boolean z) {
        this.mIsBurstItem = z;
        return this;
    }

    public BaseDataItem setTimeBurstItem(boolean z) {
        this.mIsTimeBurst = z;
        return this;
    }

    public BaseDataItem setBurstGroup(List<BaseDataItem> list) {
        this.mBurstGroup = list;
        return this;
    }

    public BaseDataItem setVideoResolution(int[] iArr) {
        this.mVideoResolution = iArr;
        return this;
    }

    public int[] getVideoResolution() {
        return this.mVideoResolution;
    }

    public long getLocalGroupId() {
        return this.mLocalGroupId;
    }

    public long getKey() {
        return this.mKey;
    }

    public String getMicroPath() {
        return this.mMicroPath;
    }

    public String getThumnailPath() {
        return this.mThumbPath;
    }

    public String getOriginalPath() {
        return this.mFilePath;
    }

    public String getMimeType() {
        return this.mMimeType;
    }

    public boolean isImage() {
        if (TextUtils.isEmpty(this.mMimeType)) {
            return false;
        }
        return BaseFileMimeUtil.isImageFromMimeType(this.mMimeType);
    }

    public boolean isVideo() {
        if (TextUtils.isEmpty(this.mMimeType)) {
            return false;
        }
        return BaseFileMimeUtil.isVideoFromMimeType(this.mMimeType);
    }

    public boolean isGif() {
        if (TextUtils.isEmpty(this.mMimeType)) {
            return false;
        }
        return BaseFileMimeUtil.isGifFromMimeType(this.mMimeType);
    }

    public boolean isRaw() {
        if (TextUtils.isEmpty(this.mMimeType)) {
            return false;
        }
        return BaseFileMimeUtil.isRawFromMimeType(this.mMimeType);
    }

    public void setSpecialTypeEditable(boolean z) {
        this.mIsSpecialTypeEditable = z;
    }

    public boolean isSpecialTypeEditable() {
        return (this.mSpecialTypeFlags > 0 || this.mAIModeTypeFlags > 0) && this.mIsSpecialTypeEditable;
    }

    public void setSpecialTypeFlags(long j) {
        this.mSpecialTypeFlags = j;
    }

    public void resetSpecialTypeFlags() {
        this.mSpecialTypeFlags = -1L;
    }

    public long getSpecialTypeFlags() {
        return this.mSpecialTypeFlags;
    }

    public boolean isSpecialTypeRecognized() {
        return this.mSpecialTypeFlags != -1;
    }

    public boolean isSpecialType(long j) {
        return isSpecialTypeRecognized() && (j & this.mSpecialTypeFlags) != 0;
    }

    public boolean isAIModeType(long j) {
        return (this.mAIModeTypeFlags == -1 || (j & this.mAIModeTypeFlags) == 0) ? false : true;
    }

    public void setAIModeTypeFlags(long j) {
        this.mAIModeTypeFlags = j;
    }

    public boolean isWatermarked() {
        return (this.mSpecialTypeFlags == -1 || (this.mSpecialTypeFlags & Long.MIN_VALUE) == 0) ? false : true;
    }

    public synchronized void setWatermarked() {
        if (this.mSpecialTypeFlags == -1) {
            this.mSpecialTypeFlags = Long.MIN_VALUE;
        } else {
            this.mSpecialTypeFlags |= Long.MIN_VALUE;
        }
    }

    public long getAIModeTypeFlags() {
        return this.mAIModeTypeFlags;
    }

    public boolean isMotionPhoto() {
        return isSpecialTypeRecognized() && (this.mSpecialTypeFlags & 32) != 0;
    }

    public boolean isDocPhoto() {
        return isSpecialTypeRecognized() && (this.mSpecialTypeFlags & 65536) != 0;
    }

    public void setMotionOffset(long j) {
        this.mMotionOffset = j;
    }

    public long getMotionOffset() {
        return this.mMotionOffset;
    }

    public boolean isSupportSubtitle() {
        return isSpecialTypeRecognized() && (this.mSpecialTypeFlags & 16384) != 0;
    }

    public boolean isSupportTags() {
        return isSpecialTypeRecognized() && (this.mSpecialTypeFlags & 32768) != 0;
    }

    public boolean is8KVideoRecognized() {
        return isSpecialTypeRecognized() && !((this.mSpecialTypeFlags & 262144) == 0 && (this.mSpecialTypeFlags & 4194304) == 0);
    }

    public boolean isHdr10VideoRecognized() {
        return isSpecialTypeRecognized() && (this.mSpecialTypeFlags & 16777216) != 0;
    }

    public boolean is2KVideo() {
        if (!isVideo()) {
            return false;
        }
        if (SpecialTypeMediaUtils.is2KResolution(getWidth(), getHeight())) {
            return true;
        }
        int[] videoResolution = getVideoResolution();
        if (videoResolution != null && videoResolution.length == 2) {
            return SpecialTypeMediaUtils.is2KResolution(videoResolution[0], videoResolution[1]);
        }
        return false;
    }

    public boolean is8KVideo() {
        if (!isVideo()) {
            return false;
        }
        if (is8KVideoRecognized() || SpecialTypeMediaUtils.is8KResolution(getWidth(), getHeight())) {
            return true;
        }
        int[] videoResolution = getVideoResolution();
        if (videoResolution != null && videoResolution.length == 2) {
            return SpecialTypeMediaUtils.is8KResolution(videoResolution[0], videoResolution[1]);
        }
        String title = getTitle();
        return title != null && title.endsWith("8K");
    }

    public boolean isMovieVideo() {
        return isSpecialTypeRecognized() && (this.mSpecialTypeFlags & 524288) != 0;
    }

    public void setVideoTags(List<Long> list) {
        this.mVideoTags = list;
    }

    public List<Long> getVideoTags() {
        return this.mVideoTags;
    }

    public String getLocation() {
        return LocationManager.getInstance().generateTitleLine(this.mLocation);
    }

    public long getSize() {
        return this.mSize;
    }

    public boolean isSecret() {
        return this.mIsSecret;
    }

    public byte[] getSecretKey() {
        return this.mSecretKey;
    }

    public boolean isScreenshot() {
        return this.mIsScreenshot;
    }

    public boolean queryIsScreenshot(Context context) {
        boolean z = this.mLocalGroupId == AlbumManager.queryScreenshotsAlbumId(context);
        this.mIsScreenshot = z;
        return z;
    }

    public FavoriteInfo getFavoriteInfo() {
        return this.mFavoriteInfo;
    }

    public FavoriteInfo queryFavoriteInfo(boolean z) {
        return this.mFavoriteInfo;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public PhotoDetailInfo getDetailInfo(Context context) {
        int i;
        int i2;
        PhotoDetailInfo photoDetailInfo = new PhotoDetailInfo();
        photoDetailInfo.addDetail(1, Long.valueOf(getCreateTime()));
        photoDetailInfo.addDetail(200, getOriginalPath());
        photoDetailInfo.addDetail(2, BaseFileUtils.getFileName(getOriginalPath()));
        photoDetailInfo.addDetail(3, Long.valueOf(getSize()));
        photoDetailInfo.addDetail(9, getLocation());
        double[] dArr = new double[2];
        getCoordidate(dArr);
        photoDetailInfo.addDetail(6, dArr);
        if (isVideo()) {
            int[] videoResolution = getVideoResolution();
            if (videoResolution == null || videoResolution.length != 2) {
                i = -1;
                i2 = -1;
            } else {
                i2 = videoResolution[0];
                i = videoResolution[1];
            }
            photoDetailInfo.addDetail(7, Integer.valueOf(getDuration()));
        } else {
            i = -1;
            i2 = -1;
        }
        if (i2 == -1 && i == -1) {
            i2 = getWidth();
            i = getHeight();
        }
        photoDetailInfo.addDetail(4, Integer.valueOf(i2));
        photoDetailInfo.addDetail(5, Integer.valueOf(i));
        return photoDetailInfo;
    }

    public long getSupportOperations() {
        return ((Long) safeRun(new Supplier() { // from class: com.miui.gallery.model.BaseDataItem$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return BaseDataItem.$r8$lambda$WqccSWYy4PivwAuXdRCkeXqg9Nk(BaseDataItem.this);
            }
        })).longValue();
    }

    public /* synthetic */ Long lambda$getSupportOperations$0() {
        if (this.mSupportOperations < 0) {
            this.mSupportOperations = initSupportOperations();
        }
        return Long.valueOf(this.mSupportOperations);
    }

    public void removeSupportOperation(final long j) {
        safeRun(new Supplier() { // from class: com.miui.gallery.model.BaseDataItem$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final Object get() {
                return BaseDataItem.$r8$lambda$NsWm4rHH5xZXdVFTq0xsmlUVPS4(BaseDataItem.this, j);
            }
        });
    }

    public /* synthetic */ Object lambda$removeSupportOperation$1(long j) {
        if (this.mSupportOperations > 0) {
            this.mSupportOperations = (~j) & this.mSupportOperations;
            return null;
        }
        return null;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public long getCreateTime() {
        return this.mCreateTime;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getDisplayWidth() {
        int i = this.mWidth;
        return i > 0 ? i : this.mDisplayWidth;
    }

    public int getDisplayHeight() {
        int i = this.mHeight;
        return i > 0 ? i : this.mDisplayHeight;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public boolean isBurstItem() {
        return this.mIsBurstItem;
    }

    public boolean isTimeBurstItem() {
        return this.mIsTimeBurst;
    }

    public List<BaseDataItem> getBurstGroup() {
        return this.mBurstGroup;
    }

    public List<Long> getBurstKeys() {
        if (this.mBurstGroup == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(this.mBurstGroup.size());
        for (BaseDataItem baseDataItem : this.mBurstGroup) {
            arrayList.add(Long.valueOf(baseDataItem.getKey()));
        }
        return arrayList;
    }

    public NSGifDecode createNSGifDecoder(ThreadPool.JobContext jobContext) {
        Throwable th;
        ParcelFileDescriptor parcelFileDescriptor;
        if (!TextUtils.isEmpty(getOriginalPath())) {
            if (isSecret()) {
                try {
                    parcelFileDescriptor = ParcelFileDescriptor.open(new File(getOriginalPath()), 805306368);
                    try {
                        try {
                            NSGifDecode create = NSGifDecode.create(parcelFileDescriptor.getFileDescriptor(), getSecretKey());
                            BaseMiscUtil.closeSilently(parcelFileDescriptor);
                            return create;
                        } catch (Exception e) {
                            e = e;
                            DefaultLogger.e(TAG, "createNSGifDecoder failed %s", e);
                            BaseMiscUtil.closeSilently(parcelFileDescriptor);
                            return null;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    parcelFileDescriptor = null;
                } catch (Throwable th3) {
                    th = th3;
                    parcelFileDescriptor = null;
                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                    throw th;
                }
            } else {
                return NSGifDecode.create(getOriginalPath());
            }
        }
        return null;
    }

    public boolean checkOriginalFileExist() {
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(getOriginalPath(), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag(TAG, "checkOriginalFileExist"));
        return documentFile != null && documentFile.exists();
    }

    public Uri getContentUriForExternal() {
        if (!TextUtils.isEmpty(this.mFilePath)) {
            return Uri.fromFile(new File(this.mFilePath));
        }
        if (TextUtils.isEmpty(this.mThumbPath)) {
            return null;
        }
        return Uri.fromFile(new File(this.mThumbPath));
    }

    public void getCoordidate(double[] dArr) {
        if (dArr != null) {
            dArr[0] = this.mLatitude;
            dArr[1] = this.mLongitude;
        }
    }

    public double[] getCoordidate() {
        return new double[]{this.mLatitude, this.mLongitude};
    }

    public boolean equals(Object obj) {
        if (obj instanceof BaseDataItem) {
            BaseDataItem baseDataItem = (BaseDataItem) obj;
            return this.mKey == baseDataItem.getKey() && StringUtils.nullToEmpty(baseDataItem.getOriginalPath()).equals(StringUtils.nullToEmpty(getOriginalPath())) && StringUtils.nullToEmpty(baseDataItem.getThumnailPath()).equals(StringUtils.nullToEmpty(getThumnailPath()));
        }
        return false;
    }

    public int hashCode() {
        return Long.valueOf(this.mKey).hashCode();
    }

    public boolean isModified(BaseDataItem baseDataItem) {
        return baseDataItem != null && !equals(baseDataItem);
    }

    public final void refillBetterPath() {
        if (checkOriginalFileExist()) {
            setPathDisplayBetter(3, getOriginalPath());
            return;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag(TAG, "refillBetterPath");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String thumnailPath = getThumnailPath();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(thumnailPath, permission, appendInvokerTag);
        if (documentFile != null && documentFile.exists()) {
            setPathDisplayBetter(2, getThumnailPath());
            return;
        }
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(getMicroPath(), permission, appendInvokerTag);
        if (documentFile2 == null || !documentFile2.exists()) {
            return;
        }
        setPathDisplayBetter(1, getMicroPath());
    }

    public String getPathDisplayBetter() {
        return (String) safeRun(new Supplier() { // from class: com.miui.gallery.model.BaseDataItem$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final Object get() {
                return BaseDataItem.m1092$r8$lambda$dTvHfNfvmPIef7N3kxfg000nCY(BaseDataItem.this);
            }
        });
    }

    public /* synthetic */ String lambda$getPathDisplayBetter$2() {
        if (this.mDisplayBetterPath == null) {
            refillBetterPath();
        }
        return this.mDisplayBetterPath.getPath();
    }

    public /* synthetic */ Long lambda$getDisplayBetterFileSize$3() {
        return Long.valueOf(this.mDisplayBetterFileSize);
    }

    public long getDisplayBetterFileSize() {
        return ((Long) safeRun(new Supplier() { // from class: com.miui.gallery.model.BaseDataItem$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                return BaseDataItem.$r8$lambda$la3KK2OgmfLPokybQPsurH2LT_s(BaseDataItem.this);
            }
        })).longValue();
    }

    public void setPathDisplayBetter(final int i, final String str) {
        safeRun(new Supplier() { // from class: com.miui.gallery.model.BaseDataItem$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final Object get() {
                return BaseDataItem.m1093$r8$lambda$iYVCKzIn2lb88QhmD54FW9CXDQ(BaseDataItem.this, i, str);
            }
        });
    }

    public /* synthetic */ Object lambda$setPathDisplayBetter$4(int i, String str) {
        if (this.mDisplayBetterPath == null) {
            this.mDisplayBetterPath = new DisplayBetterPath();
        }
        this.mDisplayBetterPath.setPath(i, str);
        return null;
    }

    public String getViewTitle(Context context) {
        return DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), getCreateTime(), 896);
    }

    public String getViewSubTitle(Context context) {
        return DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), getCreateTime(), 44);
    }

    public String getContentDescription(Context context) {
        if (TextUtils.isEmpty(this.mContentDescription)) {
            this.mContentDescription = TalkBackUtil.getContentDescriptionForImage(context, getCreateTime(), this.mLocation, this.mMimeType);
        }
        return this.mContentDescription;
    }

    public void reloadCache() {
        safeRun(new Supplier() { // from class: com.miui.gallery.model.BaseDataItem$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return BaseDataItem.$r8$lambda$Ic4V5etdPhS_CTnwAF8bpM_8B7E(BaseDataItem.this);
            }
        });
    }

    public /* synthetic */ Object lambda$reloadCache$5() {
        invalidCache();
        onLoadCache();
        onCacheLoaded();
        return null;
    }

    public void onCacheLoaded() {
        long fileSize;
        String path = this.mDisplayBetterPath.getPath();
        this.mDisplayBetterFileSize = BaseFileUtils.getFileSize(path);
        if (TextUtils.equals(this.mFilePath, path)) {
            fileSize = this.mDisplayBetterFileSize;
        } else {
            fileSize = BaseFileUtils.getFileSize(this.mFilePath);
        }
        if (fileSize > 0) {
            if (fileSize == this.mSize && this.mWidth != 0 && this.mHeight != 0) {
                return;
            }
            this.mSize = fileSize;
            try {
                if (isVideo()) {
                    VideoAttrsReader read = VideoAttrsReader.read(this.mFilePath);
                    this.mWidth = read.getVideoWidth();
                    this.mHeight = read.getVideoHeight();
                } else {
                    BitmapFactory.Options bitmapSize = miuix.graphics.BitmapFactory.getBitmapSize(this.mFilePath);
                    this.mWidth = bitmapSize.outWidth;
                    this.mHeight = bitmapSize.outHeight;
                }
            } catch (IOException e) {
                DefaultLogger.w(TAG, e);
            }
        }
    }

    public void onLoadCache() {
        refillBetterPath();
        this.mSupportOperations = initSupportOperations();
    }

    public String toString() {
        return String.format(Locale.US, "key = [%d], path = [%s], thumb = [%s]", Long.valueOf(this.mKey), this.mFilePath, this.mThumbPath);
    }

    /* loaded from: classes2.dex */
    public static class DisplayBetterPath {
        public String mPath;
        public int mType;

        public DisplayBetterPath() {
            this.mType = 0;
            this.mPath = null;
        }

        public String getPath() {
            return this.mPath;
        }

        public void setPath(int i, String str) {
            if (!TextUtils.isEmpty(str) && i >= this.mType) {
                this.mType = i;
                this.mPath = str;
            }
        }
    }

    public final boolean lock() {
        if (this.mCacheLock == null) {
            this.mCacheLock = new ReentrantLock();
        }
        if (ThreadManager.isMainThread()) {
            return false;
        }
        this.mCacheLock.lock();
        return true;
    }

    public final void unlock() {
        this.mCacheLock.unlock();
    }

    public final <T> T safeRun(Supplier<T> supplier) {
        if (lock()) {
            try {
                return supplier.get();
            } finally {
                unlock();
            }
        }
        return supplier.get();
    }
}
