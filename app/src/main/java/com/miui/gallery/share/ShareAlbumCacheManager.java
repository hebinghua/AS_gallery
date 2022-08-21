package com.miui.gallery.share;

import android.database.Cursor;
import com.miui.gallery.model.dto.ShareAlbum;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/* loaded from: classes2.dex */
public class ShareAlbumCacheManager {
    public final StampedLock mReadWriteLock;
    public volatile Map<String, ShareAlbum> mSharedAlbums;

    public static ShareAlbumCacheManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final ShareAlbumCacheManager INSTANCE = new ShareAlbumCacheManager();
    }

    public ShareAlbumCacheManager() {
        this.mReadWriteLock = new StampedLock();
    }

    public boolean putSharedAlbums(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            if (this.mSharedAlbums != null) {
                this.mSharedAlbums.clear();
            }
            return false;
        }
        long j = 0;
        try {
            try {
                j = this.mReadWriteLock.writeLockInterruptibly();
                if (this.mSharedAlbums != null) {
                    this.mSharedAlbums.clear();
                }
                if (cursor.moveToFirst()) {
                    do {
                        ShareAlbum shareAlbum = new ShareAlbum();
                        shareAlbum.mAlbumId = cursor.getString(0);
                        shareAlbum.mCreatorId = cursor.getString(1);
                        shareAlbum.mUserCount = cursor.getInt(2);
                        shareAlbum.mOwnerNickName = cursor.getString(3);
                        if (this.mSharedAlbums == null) {
                            this.mSharedAlbums = new HashMap(cursor.getCount());
                        }
                        this.mSharedAlbums.put(shareAlbum.mAlbumId, shareAlbum);
                        if (!cursor.moveToNext()) {
                            break;
                        }
                    } while (!Thread.currentThread().isInterrupted());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mReadWriteLock.unlockWrite(j);
            return this.mSharedAlbums != null && this.mSharedAlbums.size() > 0;
        } catch (Throwable th) {
            this.mReadWriteLock.unlockWrite(j);
            throw th;
        }
    }

    public ShareAlbum getShareAlbum(long j) {
        if (this.mSharedAlbums == null) {
            return null;
        }
        return getShareAlbumMaps().get(String.valueOf(j));
    }

    public Map<String, ShareAlbum> getShareAlbumMaps() {
        long tryOptimisticRead = this.mReadWriteLock.tryOptimisticRead();
        try {
            if (!this.mReadWriteLock.validate(tryOptimisticRead)) {
                tryOptimisticRead = this.mReadWriteLock.readLockInterruptibly();
                return Collections.unmodifiableMap(this.mSharedAlbums);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.mReadWriteLock.unlockRead(tryOptimisticRead);
        }
        if (this.mSharedAlbums == null) {
            return null;
        }
        return Collections.unmodifiableMap(this.mSharedAlbums);
    }

    public Collection<ShareAlbum> getShareAlbumList() {
        if (this.mSharedAlbums == null) {
            return null;
        }
        return Collections.unmodifiableCollection(getShareAlbumMaps().values());
    }

    public boolean containsKey(long j) {
        if (this.mSharedAlbums == null) {
            return false;
        }
        return getShareAlbumMaps().containsKey(String.valueOf(j));
    }
}
