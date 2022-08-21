package com.miui.gallery.cleaner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.activity.CleanerPhotoPickActivity;
import com.miui.gallery.cleaner.ScanResult;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StaticContext;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes.dex */
public abstract class BaseScanner {
    public int mType;
    public final Object mMediaItemLock = new Object();
    public ArrayList<MediaItem> mMediaItems = new ArrayList<>();
    public ScanResult.OnScanResultClickListener mOnScanResultClickListener = new ScanResult.OnScanResultClickListener() { // from class: com.miui.gallery.cleaner.BaseScanner.1
        @Override // com.miui.gallery.cleaner.ScanResult.OnScanResultClickListener
        public void onClick(Context context) {
            Intent intent = new Intent(context, CleanerPhotoPickActivity.class);
            intent.putExtra("extra_cleaner_photo_pick_type", BaseScanner.this.mType);
            context.startActivity(intent);
            BaseScanner.this.recordClickScanResultEvent();
        }
    };
    public CopyOnWriteArraySet<OnScanResultUpdateListener> mListeners = new CopyOnWriteArraySet<>();

    /* loaded from: classes.dex */
    public static class MediaItem {
        public long mCreateTime;
        public int mHeight;
        public long mId;
        public String mPath;
        public String mSha1;
        public long mSize;
        public int mWidth;
    }

    /* loaded from: classes.dex */
    public interface OnScanResultUpdateListener {
        void onUpdate(int i, long j, ScanResult scanResult);
    }

    public abstract String getSelection();

    public boolean isLoadingValid() {
        return false;
    }

    public abstract void recordClickScanResultEvent();

    public BaseScanner(int i) {
        this.mType = i;
    }

    public ScanResult scan() {
        String selection = getSelection();
        if (selection == null) {
            return null;
        }
        ArrayList<MediaItem> arrayList = (ArrayList) SafeDBUtil.safeQuery(StaticContext.sGetAndroidContext(), GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA, CleanerContract$Projection.NORMAL_SCAN_PROJECTION, selection, (String[]) null, "alias_create_time DESC", new SafeDBUtil.QueryHandler<ArrayList<MediaItem>>() { // from class: com.miui.gallery.cleaner.BaseScanner.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public ArrayList<MediaItem> mo1808handle(Cursor cursor) {
                boolean z;
                ArrayList<MediaItem> arrayList2 = null;
                if (cursor == null || !cursor.moveToFirst()) {
                    return arrayList2;
                }
                HashMap hashMap = new HashMap();
                do {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList<>();
                    }
                    MediaItem mediaItem = new MediaItem();
                    mediaItem.mId = cursor.getLong(0);
                    mediaItem.mSize = cursor.getLong(1);
                    mediaItem.mSha1 = cursor.getString(5);
                    String string = cursor.getString(2);
                    mediaItem.mPath = string;
                    if (TextUtils.isEmpty(string)) {
                        mediaItem.mPath = cursor.getString(3);
                    }
                    if (TextUtils.isEmpty(mediaItem.mPath)) {
                        mediaItem.mPath = cursor.getString(4);
                    }
                    if (TextUtils.isEmpty(mediaItem.mPath)) {
                        arrayList2.add(mediaItem);
                    } else {
                        String str = BaseFileUtils.getParentFolderPath(mediaItem.mPath) + File.separator + "1.jpg";
                        if (hashMap.containsKey(str)) {
                            z = ((Boolean) hashMap.get(str)).booleanValue();
                        } else {
                            boolean checkStoragePermission = BaseScanner.this.checkStoragePermission(str);
                            hashMap.put(str, Boolean.valueOf(checkStoragePermission));
                            z = checkStoragePermission;
                        }
                        if (z) {
                            arrayList2.add(mediaItem);
                        }
                    }
                } while (cursor.moveToNext());
                return arrayList2;
            }
        });
        if (!BaseMiscUtil.isValid(arrayList)) {
            return null;
        }
        this.mMediaItems = arrayList;
        return buildScanResult();
    }

    public boolean checkStoragePermission(String str) {
        return StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE).granted;
    }

    public final void updateScanResult(ScanResult scanResult) {
        updateScanResult(0L, scanResult);
    }

    public final void addListener(OnScanResultUpdateListener onScanResultUpdateListener) {
        if (onScanResultUpdateListener != null) {
            this.mListeners.add(onScanResultUpdateListener);
        }
    }

    public final void removeListener(OnScanResultUpdateListener onScanResultUpdateListener) {
        if (onScanResultUpdateListener != null) {
            this.mListeners.remove(onScanResultUpdateListener);
        }
    }

    public final void reset() {
        this.mListeners.clear();
        onReset();
    }

    public void onMediaItemDeleted(long j) {
        if (removeItem(j)) {
            updateScanResult(buildScanResult());
        }
    }

    public void removeItems(long[] jArr) {
        if (jArr == null) {
            return;
        }
        synchronized (this.mMediaItemLock) {
            for (long j : jArr) {
                if (removeItem(j)) {
                    updateScanResult((int) j, buildScanResult());
                }
            }
        }
    }

    public boolean removeItem(long j) {
        boolean z;
        synchronized (this.mMediaItemLock) {
            Iterator<MediaItem> it = this.mMediaItems.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                } else if (it.next().mId == j) {
                    it.remove();
                    z = true;
                    break;
                }
            }
        }
        return z;
    }

    public ScanResult buildScanResult() {
        int i;
        long j;
        ScanResult.ResultImage[] resultImageArr = new ScanResult.ResultImage[10];
        synchronized (this.mMediaItemLock) {
            Iterator<MediaItem> it = this.mMediaItems.iterator();
            i = 0;
            j = 0;
            while (it.hasNext()) {
                MediaItem next = it.next();
                j += next.mSize;
                if (i < 10) {
                    resultImageArr[i] = new ScanResult.ResultImage(next.mId, next.mPath);
                }
                i++;
            }
        }
        return new ScanResult.Builder().setType(this.mType).setSize(j).setImages(resultImageArr).setCount(i).setOnScanResultClickListener(this.mOnScanResultClickListener).build();
    }

    public List<Long> getScanResultIds() {
        ArrayList arrayList;
        synchronized (this.mMediaItemLock) {
            int size = this.mMediaItems.size();
            arrayList = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                arrayList.add(Long.valueOf(this.mMediaItems.get(i).mId));
            }
        }
        return arrayList;
    }

    public void onReset() {
        this.mMediaItems.clear();
    }

    public final void updateScanResult(long j, ScanResult scanResult) {
        Iterator<OnScanResultUpdateListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            OnScanResultUpdateListener next = it.next();
            if (next != null) {
                next.onUpdate(this.mType, j, scanResult);
            }
        }
    }
}
