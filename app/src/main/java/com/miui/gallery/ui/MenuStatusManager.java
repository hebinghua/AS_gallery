package com.miui.gallery.ui;

import android.content.Context;
import android.text.TextUtils;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.FileSize;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;

/* loaded from: classes2.dex */
public class MenuStatusManager {
    public boolean isRubbishAlbum;
    public String mAlbumServerId;
    public boolean mAlbumUnwriteable;
    public boolean mAutoUpload;
    public Context mContext;
    public int mFromType;
    public boolean mIsAllPhotosAlbum;
    public boolean mIsCustomQueryAlbum;
    public boolean mIsManualRenameRestricted;
    public boolean mIsMustVisibleAlbum;
    public boolean mIsOtherShareAlbum;
    public boolean mIsQueryMediaType;
    public boolean mIsShareAlbum;
    public boolean mIsShareToDevice;
    public String mLocalPath;
    public boolean mShowInOtherAlbums;
    public boolean mShowInPhotosTab;
    public long mSupportOperationFlag;

    public MenuStatusManager(Context context, String str, long j, boolean z, boolean z2, String str2, int i, boolean z3, boolean z4, boolean z5) {
        this.mContext = context;
        this.mAlbumServerId = str;
        this.mLocalPath = str2;
        boolean z6 = true;
        this.mAutoUpload = (j & 1) != 0;
        this.mShowInPhotosTab = (j & 4) != 0;
        this.mShowInOtherAlbums = (j & 64) != 0;
        this.mAlbumUnwriteable = z;
        this.mIsOtherShareAlbum = z2;
        this.mIsManualRenameRestricted = isSystemAlbum(str) || z2 || z || isManualRenameRestricted(str2);
        this.mIsShareToDevice = (j & 1280) != 0;
        this.mFromType = i;
        this.isRubbishAlbum = Album.isRubbishAlbum(j);
        this.mIsShareAlbum = z3;
        this.mIsAllPhotosAlbum = z4;
        this.mIsCustomQueryAlbum = z5;
        this.mIsQueryMediaType = i != 1016 ? false : z6;
        this.mIsMustVisibleAlbum = Album.isMustVisibleAlbum(this.mLocalPath);
        genSupportedOperationFlags();
    }

    public final void genSupportedOperationFlags() {
        long j = 0;
        this.mSupportOperationFlag = 0L;
        if (this.mIsQueryMediaType) {
            this.mSupportOperationFlag = 0 | 6144;
        } else if (this.mIsCustomQueryAlbum) {
        } else {
            long j2 = ((canEnableAddPhotos() ? 1L : 0L) & 1) | 0;
            this.mSupportOperationFlag = j2;
            long j3 = j2 | ((canDisableShowInOtherAlbums() ? 2L : 0L) & 2);
            this.mSupportOperationFlag = j3;
            long j4 = j3 | ((canEnableShowInRubbishAlbums() ? 4L : 0L) & 4);
            this.mSupportOperationFlag = j4;
            long j5 = j4 | ((canEnableShowInOtherAlbums() ? 8L : 0L) & 8);
            this.mSupportOperationFlag = j5;
            long j6 = j5 | ((canDisableShowInRubbishAlbums() ? 16L : 0L) & 16);
            this.mSupportOperationFlag = j6;
            long j7 = j6 | ((canEnableShowInPhotosTab() ? 32L : 0L) & 32);
            this.mSupportOperationFlag = j7;
            long j8 = j7 | ((canDisableShowInPhotosTab() ? 64L : 0L) & 64);
            this.mSupportOperationFlag = j8;
            long j9 = j8 | ((canEnableAutoUpload() ? 128L : 0L) & 128);
            this.mSupportOperationFlag = j9;
            long j10 = j9 | ((canDisableAutoUpload() ? 256L : 0L) & 256);
            this.mSupportOperationFlag = j10;
            long j11 = j10 | ((canEnableShare() ? 512L : 0L) & 512);
            this.mSupportOperationFlag = j11;
            long j12 = j11 | ((canEnableRename() ? 1024L : 0L) & FileSize.KB_COEFFICIENT);
            this.mSupportOperationFlag = j12;
            long j13 = j12 | ((canEnableShowOrHideTimeGroup() ? 2048L : 0L) & 2048);
            this.mSupportOperationFlag = j13;
            long j14 = j13 | ((canEnableShowSort() ? 4096L : 0L) & 4096);
            this.mSupportOperationFlag = j14;
            if (canEnableReplaceAlbumCover()) {
                j = 8192;
            }
            this.mSupportOperationFlag = (j & FileAppender.DEFAULT_BUFFER_SIZE) | j14;
        }
    }

    public void onAutoUploadStatusChanged(boolean z) {
        this.mAutoUpload = z;
        genSupportedOperationFlags();
    }

    public void onShowInPhotosTabStatusChanged(boolean z) {
        this.mShowInPhotosTab = z;
        genSupportedOperationFlags();
    }

    public void onShowInOtherAlbumsStatusChanged(boolean z) {
        this.mShowInOtherAlbums = z;
        genSupportedOperationFlags();
    }

    public void onShowInRubbishAlbumPageStatusChanged(boolean z) {
        this.isRubbishAlbum = z;
        genSupportedOperationFlags();
    }

    public boolean isUnWriteable() {
        return this.mAlbumUnwriteable;
    }

    public boolean isShareToDevice() {
        return this.mIsShareToDevice;
    }

    public boolean canShowInPhotosTab() {
        return this.mShowInPhotosTab;
    }

    public boolean isHiddenOrRubbishAlbum() {
        int i = this.mFromType;
        return i == 1001 || i == 1004 || this.isRubbishAlbum;
    }

    public final boolean canEnableAutoUpload() {
        if (isScreenRecorderAlbum(this.mLocalPath) || isSystemAlbumButNotScreenshotsRecorders(this.mAlbumServerId) || isHiddenOrRubbishAlbum() || this.mAlbumUnwriteable || this.mIsOtherShareAlbum) {
            return false;
        }
        if (SyncUtil.isGalleryCloudSyncable(this.mContext)) {
            return !this.mAutoUpload;
        }
        return true;
    }

    public final boolean canDisableAutoUpload() {
        if (isScreenRecorderAlbum(this.mLocalPath) || isSystemAlbumButNotScreenshotsRecorders(this.mAlbumServerId) || this.mAlbumUnwriteable || this.mIsOtherShareAlbum || isHiddenOrRubbishAlbum() || !SyncUtil.isGalleryCloudSyncable(this.mContext)) {
            return false;
        }
        return this.mAutoUpload;
    }

    public final boolean isVirtualAlbumButNotScreenshotsRecorders(String str) {
        return String.valueOf(-2147483647L).equals(str) || String.valueOf(1000L).equals(str) || String.valueOf(-2147483643L).equals(str) || String.valueOf(-2147483642L).equals(str);
    }

    public final boolean isSystemAlbumButNotScreenshotsRecorders(String str) {
        return String.valueOf(1L).equals(str) || String.valueOf(-2147483647L).equals(str) || String.valueOf(1000L).equals(str) || String.valueOf(-2147483643L).equals(str) || String.valueOf(-2147483642L).equals(str) || String.valueOf(-2147483644L).equals(str);
    }

    public final boolean isSystemAlbum(String str) {
        return isSystemAlbumButNotScreenshotsRecorders(str) || String.valueOf(2L).equals(str) || String.valueOf(-2147483645L).equals(str);
    }

    public final boolean isScreenRecorderAlbum(String str) {
        return Album.isScreenRecorderAlbum(str);
    }

    public final boolean isRawAlbum() {
        return MIUIStorageConstants.DIRECTORY_CAMERA_RAW_PATH.equalsIgnoreCase(this.mLocalPath);
    }

    public final boolean isDailyAlbum(String str) {
        return String.valueOf(-2147483643L).equals(str);
    }

    public final boolean canEnableShowInOtherAlbums() {
        if (isScreenRecorderAlbum(this.mLocalPath) || isSystemAlbum(this.mAlbumServerId) || isRawAlbum() || this.mIsOtherShareAlbum || this.mAlbumUnwriteable || isHiddenOrRubbishAlbum()) {
            return false;
        }
        return !this.mShowInOtherAlbums;
    }

    public final boolean canDisableShowInOtherAlbums() {
        return !isScreenRecorderAlbum(this.mLocalPath) && !isSystemAlbum(this.mAlbumServerId) && !isRawAlbum() && !this.mIsOtherShareAlbum && !this.mAlbumUnwriteable && !isHiddenOrRubbishAlbum() && this.mShowInOtherAlbums && !this.isRubbishAlbum;
    }

    public final boolean canEnableShowInPhotosTab() {
        if (isScreenRecorderAlbum(this.mLocalPath) || isSystemAlbumButNotScreenshotsRecorders(this.mAlbumServerId) || this.mIsOtherShareAlbum || this.mAlbumUnwriteable || isHiddenOrRubbishAlbum()) {
            return false;
        }
        return !this.mShowInPhotosTab;
    }

    public final boolean canDisableShowInPhotosTab() {
        if (isScreenRecorderAlbum(this.mLocalPath) || isSystemAlbumButNotScreenshotsRecorders(this.mAlbumServerId) || isHiddenOrRubbishAlbum()) {
            return false;
        }
        return this.mShowInPhotosTab;
    }

    public final boolean canEnableShare() {
        return (!isHiddenOrRubbishAlbum() && !isSystemAlbum(this.mAlbumServerId) && !isRawAlbum() && !this.mAlbumUnwriteable && ApplicationHelper.supportShare() && Album.isUserCreateAlbum(this.mLocalPath)) || isShareToDevice();
    }

    public final boolean canEnableAddPhotos() {
        return !this.mIsAllPhotosAlbum && !isVirtualAlbumButNotScreenshotsRecorders(this.mAlbumServerId) && !this.mAlbumUnwriteable && !isHiddenOrRubbishAlbum();
    }

    public final boolean canEnableShowOrHideTimeGroup() {
        return !isDailyAlbum(this.mAlbumServerId);
    }

    public final boolean canEnableShowSort() {
        return !isDailyAlbum(this.mAlbumServerId);
    }

    public final boolean canEnableReplaceAlbumCover() {
        return !isScreenRecorderAlbum(this.mLocalPath) && !isHiddenOrRubbishAlbum() && !this.mIsOtherShareAlbum && !String.valueOf(1000L).equals(this.mAlbumServerId) && !String.valueOf(-2147483643L).equals(this.mAlbumServerId);
    }

    public final boolean isManualRenameRestricted(String str) {
        AlbumsStrategy.Album albumByPath;
        return !TextUtils.isEmpty(str) && (albumByPath = CloudControlStrategyHelper.getAlbumByPath(StorageUtils.ensureCommonRelativePath(str))) != null && albumByPath.getAttributes() != null && albumByPath.getAttributes().isManualRenameRestricted();
    }

    public final boolean canEnableRename() {
        return !isSystemAlbum(this.mAlbumServerId) && !this.mAlbumUnwriteable && !this.mIsOtherShareAlbum && !this.mIsManualRenameRestricted && !isHiddenOrRubbishAlbum();
    }

    public final boolean canDisableShowInRubbishAlbums() {
        return !this.mIsShareAlbum && !this.mIsShareToDevice && this.mFromType == 1003 && this.isRubbishAlbum && this.mShowInOtherAlbums;
    }

    public boolean canEnableShowInRubbishAlbums() {
        return !this.mIsShareAlbum && !this.mIsShareToDevice && !this.mIsMustVisibleAlbum && this.mFromType == 1003 && !this.isRubbishAlbum && !Album.isUserCreateAlbum(this.mLocalPath) && this.mShowInOtherAlbums;
    }

    public long getSupportOperationFlag() {
        return this.mSupportOperationFlag;
    }

    public boolean checkOperationSupport(long j) {
        return (j & this.mSupportOperationFlag) != 0;
    }
}
