package com.miui.gallery.loader;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.StringBuilderPrinter;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.loader.MediaSetLoader;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CursorDataSet;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.model.MediaItem;
import com.miui.gallery.provider.ProcessingMediaManager;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.trash.TrashManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BucketIdUtils;
import com.miui.gallery.util.BurstFilterCursor;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.MediaStoreUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.xiaomi.mirror.synergy.CallMethod;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public abstract class MediaSetLoader extends CursorSetLoader {
    public static final String[] BUCKET_PROJECTION = {"relative_path", "bucket_id"};
    public static final List<Long> sMarkDeletedIds = new LinkedList();
    public String[] mBucketIds;
    public final boolean mFromCamera;
    public long mInitId;
    public int mInitPos;
    public final boolean mIsFromScreenshot;
    public boolean mIsInternal;
    public ArrayList<Uri> mLimitUris;
    public List<Long> mProcessingIds;
    public Uri mUri;

    /* renamed from: $r8$lambda$W-jx0k7uG7h--IEAcN6AwjD43Pc */
    public static /* synthetic */ String[] m1009$r8$lambda$Wjx0k7uG7hIEAcN6AwjD43Pc(MediaSetLoader mediaSetLoader, Cursor cursor) {
        return mediaSetLoader.lambda$loadInBackground$0(cursor);
    }

    public abstract Uri getContentUri(boolean z);

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getOrder() {
        return "datetaken DESC, _id DESC";
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String[] getSelectionArgs() {
        return null;
    }

    public MediaSetLoader(Context context, Uri uri, Bundle bundle, boolean z) {
        super(context);
        this.mInitPos = -1;
        this.mIsInternal = z;
        this.mUri = uri;
        this.mFromCamera = bundle.getBoolean("from_MiuiCamera", false);
        this.mIsFromScreenshot = bundle.getBoolean("from_MiuiScreenShot", false);
        this.mLimitUris = bundle.getParcelableArrayList("SecureUri");
        if (bundle.getBoolean("com.miui.gallery.extra.preview_single_item", false)) {
            ArrayList<Uri> arrayList = new ArrayList<>(1);
            this.mLimitUris = arrayList;
            arrayList.add(uri);
        }
        ImageLoadParams imageLoadParams = (ImageLoadParams) bundle.getParcelable("photo_transition_data");
        if (imageLoadParams != null) {
            this.mInitId = imageLoadParams.getKey();
        }
    }

    public final boolean isLimitedUris() {
        return BaseMiscUtil.isValid(this.mLimitUris);
    }

    @Override // com.miui.gallery.loader.CursorSetLoader, androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public CursorDataSet mo1444loadInBackground() {
        TimingTracing.beginTracing(isLimitedUris() ? "MediaSetLoader_limited_load" : "MediaSetLoader_load", "loadInBackground");
        try {
            long j = this.mInitId;
            if (j <= 0) {
                j = ContentUris.parseId(this.mUri);
            }
            if (isLimitedUris()) {
                CursorDataSet mo1444loadInBackground = super.mo1444loadInBackground();
                if (mo1444loadInBackground == null) {
                    return null;
                }
                if (this.mInitPos == -1) {
                    BaseDataItem baseDataItem = new BaseDataItem();
                    baseDataItem.setKey(j);
                    this.mInitPos = mo1444loadInBackground.getIndexOfItem(baseDataItem, -1);
                }
                mo1444loadInBackground.setInitPosition(this.mInitPos);
                long stopTracing = TimingTracing.stopTracing(null);
                if (stopTracing > 500) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("cost_time", isLimitedUris() + "_" + stopTracing);
                    SamplingStatHelper.recordCountEvent("load_performance", "MediaSetLoader", hashMap);
                }
                return mo1444loadInBackground;
            }
            if (this.mBucketIds == null) {
                String[] strArr = (String[]) SafeDBUtil.safeQuery(getContext(), this.mUri, BUCKET_PROJECTION, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.loader.MediaSetLoader$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                    /* renamed from: handle */
                    public final Object mo1808handle(Cursor cursor) {
                        return MediaSetLoader.m1009$r8$lambda$Wjx0k7uG7hIEAcN6AwjD43Pc(MediaSetLoader.this, cursor);
                    }
                });
                this.mBucketIds = strArr;
                if (strArr == null && this.mFromCamera) {
                    this.mBucketIds = BucketIdUtils.genAllBucketIds(getContext(), MIUIStorageConstants.DIRECTORY_CAMERA_PATH, null);
                }
                TimingTracing.addSplit("genAllBucketIds");
            }
            if (this.mBucketIds != null) {
                this.mProcessingIds = ProcessingMediaManager.queryProcessingMediaIds();
                CursorDataSet mo1444loadInBackground2 = super.mo1444loadInBackground();
                TimingTracing.addSplit("super.loadInBackground");
                if (mo1444loadInBackground2 != null) {
                    if (this.mInitPos == -1) {
                        BaseDataItem baseDataItem2 = new BaseDataItem();
                        baseDataItem2.setKey(j);
                        this.mInitPos = mo1444loadInBackground2.getIndexOfItem(baseDataItem2, -1);
                        TimingTracing.addSplit("getIndexOfItem");
                    }
                    mo1444loadInBackground2.setInitPosition(this.mInitPos);
                    long stopTracing2 = TimingTracing.stopTracing(null);
                    if (stopTracing2 > 500) {
                        HashMap hashMap2 = new HashMap();
                        hashMap2.put("cost_time", isLimitedUris() + "_" + stopTracing2);
                        SamplingStatHelper.recordCountEvent("load_performance", "MediaSetLoader", hashMap2);
                    }
                    return mo1444loadInBackground2;
                }
            }
            long stopTracing3 = TimingTracing.stopTracing(null);
            if (stopTracing3 > 500) {
                HashMap hashMap3 = new HashMap();
                hashMap3.put("cost_time", isLimitedUris() + "_" + stopTracing3);
                SamplingStatHelper.recordCountEvent("load_performance", "MediaSetLoader", hashMap3);
            }
            return null;
        } finally {
            long stopTracing4 = TimingTracing.stopTracing(null);
            if (stopTracing4 > 500) {
                HashMap hashMap4 = new HashMap();
                hashMap4.put("cost_time", isLimitedUris() + "_" + stopTracing4);
                SamplingStatHelper.recordCountEvent("load_performance", "MediaSetLoader", hashMap4);
            }
        }
    }

    public /* synthetic */ String[] lambda$loadInBackground$0(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        TimingTracing.addSplit("query buckets");
        return BucketIdUtils.genAllBucketIds(getContext(), cursor.getString(0), cursor.getString(1));
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public Uri getUri() {
        return getContentUri(this.mIsInternal);
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getSelection() {
        String str;
        if (isLimitedUris()) {
            ArrayList arrayList = new ArrayList(this.mLimitUris.size());
            Iterator<Uri> it = this.mLimitUris.iterator();
            while (it.hasNext()) {
                Uri next = it.next();
                if (next != null) {
                    long parseId = ContentUris.parseId(next);
                    List<Long> list = sMarkDeletedIds;
                    synchronized (list) {
                        if (!RemarkManager.CacheMarkManager.appendMarkIds(list).contains(Long.valueOf(parseId))) {
                            arrayList.add(Long.valueOf(parseId));
                        } else {
                            DefaultLogger.d("MediaSetLoader", "filter mark deleted id %d", Long.valueOf(parseId));
                        }
                    }
                }
            }
            return "_id in (" + TextUtils.join(",", arrayList) + ")";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("bucket_id in (");
        sb.append(TextUtils.join(",", this.mBucketIds));
        sb.append(")");
        if (BaseMiscUtil.isValid(this.mProcessingIds)) {
            str = " OR _id in (" + TextUtils.join(",", this.mProcessingIds) + ")";
        } else {
            str = "";
        }
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = null;
        List<Long> list2 = sMarkDeletedIds;
        synchronized (list2) {
            List<Long> appendMarkIds = RemarkManager.CacheMarkManager.appendMarkIds(list2);
            if (appendMarkIds.size() > 0) {
                str2 = TextUtils.join(",", appendMarkIds);
            }
        }
        if (TextUtils.isEmpty(str2)) {
            return sb2;
        }
        DefaultLogger.d("MediaSetLoader", "filter mark deleted ids %s", str2);
        return String.format(Locale.US, "(%s) AND (%s)", sb2, "_id not in (" + str2 + ")");
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getLimit() {
        int cameraPreviewCount = CloudControlStrategyHelper.getDataLoadStrategy().getCameraPreviewCount();
        if (cameraPreviewCount > 0) {
            DefaultLogger.d("MediaSetLoader", "query limit %d", Integer.valueOf(cameraPreviewCount));
            return String.valueOf(cameraPreviewCount);
        }
        return null;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, String str3) {
        Bundle createSqlQueryBundle = createSqlQueryBundle(str, strArr2, str2, str3);
        ContentResolver contentResolver = getContext().getContentResolver();
        if (str3 != null) {
            uri = UriUtil.appendLimit(uri, Integer.parseInt(str3));
        }
        return contentResolver.query(uri, strArr, createSqlQueryBundle, null);
    }

    public void printLog(MediaDataSet mediaDataSet) {
        if (mediaDataSet == null || mediaDataSet.getCount() <= 0) {
            DefaultLogger.d(getTAG(), "empty dataset");
            return;
        }
        for (int i = 0; i < Math.min(5, mediaDataSet.getCount()); i++) {
            DefaultLogger.d(getTAG(), "pos = [%d], item = [%s]", Integer.valueOf(i), mediaDataSet.createItem(i));
        }
    }

    public static Bundle createSqlQueryBundle(String str, String[] strArr, String str2, String str3) {
        if (str == null && strArr == null && str2 == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        if (str != null) {
            bundle.putString("android:query-arg-sql-selection", str);
        }
        if (strArr != null) {
            bundle.putStringArray("android:query-arg-sql-selection-args", strArr);
        }
        if (str2 != null) {
            bundle.putString("android:query-arg-sql-sort-order", str2);
        }
        int i = GalleryApp.sGetAndroidContext().getApplicationInfo().targetSdkVersion;
        if (str3 != null && i >= 30) {
            bundle.putString("android:query-arg-sql-limit", str3);
        }
        return bundle;
    }

    /* loaded from: classes2.dex */
    public abstract class MediaDataSet extends CursorDataSet {
        /* renamed from: $r8$lambda$-6oTRvz_7rTDNXtDU2uY_-UV9H8 */
        public static /* synthetic */ void m1011$r8$lambda$6oTRvz_7rTDNXtDU2uY_UV9H8(HashMap hashMap, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener, long[] jArr, boolean z) {
            lambda$addToAlbum$1(hashMap, onAddAlbumListener, jArr, z);
        }

        public static /* synthetic */ void $r8$lambda$YiwGCQ01vElYUHzxMfBvcOhdU0k(MediaDataSet mediaDataSet, String[] strArr, Long[] lArr, boolean z) {
            mediaDataSet.lambda$markDelete$0(strArr, lArr, z);
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean foldBurst() {
            return true;
        }

        public abstract void wrapItemByCursor(BaseDataItem baseDataItem, Cursor cursor);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MediaDataSet(Cursor cursor) {
            super(cursor);
            MediaSetLoader.this = r1;
        }

        @Override // com.miui.gallery.model.CursorDataSet, com.miui.gallery.model.BaseDataSet
        public BaseDataItem createItem(int i) {
            MediaItem mediaItem = new MediaItem();
            bindItem(mediaItem, i);
            return mediaItem;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public void bindItem(BaseDataItem baseDataItem, int i) {
            if (isValidate(i)) {
                this.mCursor.moveToPosition(i);
                wrapItemByCursor(baseDataItem, this.mCursor);
                if (!foldBurst()) {
                    return;
                }
                BurstFilterCursor burstFilterCursor = (BurstFilterCursor) this.mCursor;
                if (!burstFilterCursor.isBurstPosition(i)) {
                    return;
                }
                ArrayList<Integer> burstGroup = burstFilterCursor.getBurstGroup(i);
                ArrayList arrayList = new ArrayList(burstGroup.size());
                for (Integer num : burstGroup) {
                    Cursor contentCursorAtPosition = burstFilterCursor.getContentCursorAtPosition(num.intValue());
                    BaseDataItem baseDataItem2 = new BaseDataItem();
                    wrapItemByCursor(baseDataItem2, contentCursorAtPosition);
                    arrayList.add(baseDataItem2);
                }
                baseDataItem.setBurstItem(true);
                baseDataItem.setBurstGroup(arrayList);
                if (burstFilterCursor.isTimeBurstPosition(i)) {
                    baseDataItem.setTimeBurstItem(true);
                    baseDataItem.setSpecialTypeFlags(8388608L);
                    return;
                }
                baseDataItem.setSpecialTypeFlags(64L);
            }
        }

        public final boolean isFromCamera() {
            return MediaSetLoader.this.mFromCamera;
        }

        public final boolean isFromScreenshot() {
            return MediaSetLoader.this.mIsFromScreenshot;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean deletingIncludeCloud() {
            return isFromCamera() || isFromScreenshot();
        }

        public final void markDelete(List<BaseDataItem> list, final boolean z) {
            final Long[] lArr = new Long[list.size()];
            final String[] strArr = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                BaseDataItem baseDataItem = list.get(i);
                lArr[i] = Long.valueOf(baseDataItem.getKey());
                strArr[i] = baseDataItem.getOriginalPath();
            }
            synchronized (MediaSetLoader.sMarkDeletedIds) {
                MediaSetLoader.sMarkDeletedIds.addAll(Arrays.asList(lArr));
            }
            MediaStoreUtils.makeInvalid(GalleryApp.sGetAndroidContext(), Arrays.asList(strArr));
            ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.loader.MediaSetLoader$MediaDataSet$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaSetLoader.MediaDataSet.$r8$lambda$YiwGCQ01vElYUHzxMfBvcOhdU0k(MediaSetLoader.MediaDataSet.this, strArr, lArr, z);
                }
            });
        }

        public /* synthetic */ void lambda$markDelete$0(String[] strArr, Long[] lArr, boolean z) {
            TimingTracing.beginTracing("MediaSetLoader_delete", "delete");
            if (strArr != null) {
                for (String str : strArr) {
                    try {
                        TrashManager.moveFileToTrash(str, 30, "MediaSetLoader");
                    } catch (StoragePermissionMissingException unused) {
                        DefaultLogger.e("MediaSetLoader", "move file to trash failed for permission missing");
                    }
                }
            }
            TimingTracing.addSplit("provider delete " + SafeDBUtil.safeDelete(MediaSetLoader.this.getContext(), MediaSetLoader.this.getUri(), String.format("_id in (%s)", TextUtils.join(",", lArr)), null));
            MediaAndAlbumOperations.deleteInService(MediaSetLoader.this.getContext(), !z ? 1 : 0, 30, strArr);
            synchronized (MediaSetLoader.sMarkDeletedIds) {
                MediaSetLoader.sMarkDeletedIds.removeAll(Arrays.asList(lArr));
            }
            StringBuilder sb = new StringBuilder();
            long stopTracing = TimingTracing.stopTracing(new StringBuilderPrinter(sb));
            DefaultLogger.w("MediaSetLoader", "delete : %s", sb.toString());
            if (stopTracing > 500) {
                HashMap hashMap = new HashMap();
                hashMap.put("cost_time", String.valueOf(stopTracing));
                hashMap.put(CallMethod.ARG_EXTRA_STRING, sb.toString());
                SamplingStatHelper.recordCountEvent("delete_performance", "MediaSetLoader", hashMap);
            }
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public int doDelete(int i, BaseDataItem baseDataItem, boolean z) {
            if (baseDataItem == null) {
                return 0;
            }
            LinkedList linkedList = new LinkedList();
            if (baseDataItem.isBurstItem() && baseDataItem.getBurstGroup().size() > 0) {
                linkedList.addAll(baseDataItem.getBurstGroup());
            } else {
                linkedList.add(baseDataItem);
            }
            int size = linkedList.size();
            markDelete(linkedList, z);
            return size;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean addToAlbum(FragmentActivity fragmentActivity, int i, boolean z, boolean z2, final MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
            BaseDataItem item = getItem(null, i);
            if (item != null && !TextUtils.isEmpty(item.getOriginalPath())) {
                ArrayList arrayList = new ArrayList(1);
                final HashMap hashMap = new HashMap();
                if (item.isBurstItem() && item.getBurstGroup() != null) {
                    List<BaseDataItem> burstGroup = item.getBurstGroup();
                    for (int i2 = 0; i2 < burstGroup.size(); i2++) {
                        String originalPath = burstGroup.get(i2).getOriginalPath();
                        arrayList.add(Uri.fromFile(new File(originalPath)));
                        hashMap.put(originalPath, Long.valueOf(burstGroup.get(i2).getKey()));
                    }
                } else {
                    String originalPath2 = item.getOriginalPath();
                    arrayList.add(Uri.fromFile(new File(originalPath2)));
                    hashMap.put(originalPath2, Long.valueOf(item.getKey()));
                }
                MediaAndAlbumOperations.addToAlbum(fragmentActivity, new MediaAndAlbumOperations.OnAddAlbumListener() { // from class: com.miui.gallery.loader.MediaSetLoader$MediaDataSet$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
                    public final void onComplete(long[] jArr, boolean z3) {
                        MediaSetLoader.MediaDataSet.m1011$r8$lambda$6oTRvz_7rTDNXtDU2uY_UV9H8(hashMap, onAddAlbumListener, jArr, z3);
                    }
                }, arrayList, z2, item.isVideo());
            }
            return true;
        }

        public static /* synthetic */ void lambda$addToAlbum$1(HashMap hashMap, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener, long[] jArr, boolean z) {
            if (z && jArr != null && jArr[0] > 0) {
                RemarkManager.CacheMarkManager.markData(hashMap);
            }
            if (onAddAlbumListener != null) {
                onAddAlbumListener.onComplete(jArr, z);
            }
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean addToFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
            BaseDataItem item = getItem(null, i);
            if (item != null && !TextUtils.isEmpty(item.getOriginalPath())) {
                if (!item.isBurstItem() || item.getBurstGroup() == null) {
                    MediaAndAlbumOperations.addToFavoritesByPath(fragmentActivity, onCompleteListener, item.getOriginalPath());
                } else {
                    List<BaseDataItem> burstGroup = item.getBurstGroup();
                    int size = burstGroup.size();
                    String[] strArr = new String[size];
                    for (int i2 = 0; i2 < size; i2++) {
                        strArr[i2] = burstGroup.get(i2).getOriginalPath();
                    }
                    MediaAndAlbumOperations.addToFavoritesByPath(fragmentActivity, onCompleteListener, strArr);
                }
            }
            return true;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean removeFromFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
            BaseDataItem item = getItem(null, i);
            if (item != null && !TextUtils.isEmpty(item.getOriginalPath())) {
                if (!item.isBurstItem() || item.getBurstGroup() == null) {
                    MediaAndAlbumOperations.removeFromFavoritesByPath(fragmentActivity, onCompleteListener, item.getOriginalPath());
                } else {
                    List<BaseDataItem> burstGroup = item.getBurstGroup();
                    int size = burstGroup.size();
                    String[] strArr = new String[size];
                    for (int i2 = 0; i2 < size; i2++) {
                        strArr[i2] = burstGroup.get(i2).getOriginalPath();
                    }
                    MediaAndAlbumOperations.removeFromFavoritesByPath(fragmentActivity, onCompleteListener, strArr);
                }
            }
            return true;
        }
    }
}
