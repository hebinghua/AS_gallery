package com.miui.gallery.provider.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import ch.qos.logback.core.CoreConstants;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.common.collect.Sets;
import com.miui.gallery.base_optimization.util.GenericUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.model.AlbumConstants;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.provider.cache.Filter;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.util.CursorUtils;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b.h;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class AlbumCacheItem implements IAlbum, AlbumConstants, CacheItem {
    public static final Map<String, Integer> COLUMN_MAP;
    public static final ContentValues UPDATE_COVER_AND_COUNT = new ContentValues(0);
    public Long id;
    public boolean isManualSetCover;
    public boolean isVirtualAlbum;
    public Long mAlbumSize;
    public Long mAttributes;
    public Long mCoverId;
    public String mCoverPath;
    public String mCoverSha1;
    public Long mCoverSize;
    public Integer mCoverSyncState;
    public Long mDateModified;
    public Long mDateTaken;
    public String mDirectoryPath;
    public String mEditColumns;
    public Album.ExtraInfo mExtraBean;
    public String mExtraInfo;
    public Integer mLocalFlag;
    public String mName;
    public Integer mPhotoCount;
    public Long mRealDateModified;
    public String mServerId;
    public String mServerStatus;
    public Long mServerTag;
    public Integer mSortBy;
    public String mSortInfo;

    public static /* synthetic */ int $r8$lambda$ZzpQgKGN39dXepGKIp3k2O9y_TU(AlbumCacheItem albumCacheItem, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        return albumCacheItem.lambda$internalCalculateCoverInfo$0(mediaCacheItem, mediaCacheItem2);
    }

    static {
        ArrayMap arrayMap = new ArrayMap(23);
        COLUMN_MAP = arrayMap;
        arrayMap.put(j.c, 0);
        arrayMap.put("name", 1);
        arrayMap.put("attributes", 2);
        arrayMap.put("coverId", 10);
        arrayMap.put("coverPath", 18);
        arrayMap.put("coverSize", 17);
        arrayMap.put("coverSyncState", 16);
        arrayMap.put("coverSha1", 19);
        arrayMap.put("is_manual_set_cover", 20);
        arrayMap.put("dateTaken", 3);
        arrayMap.put("dateModified", 4);
        arrayMap.put("sortInfo", 5);
        arrayMap.put("localFlag", 7);
        arrayMap.put("editedColumns", 14);
        arrayMap.put("serverId", 8);
        arrayMap.put("serverTag", 12);
        arrayMap.put("serverStatus", 13);
        arrayMap.put("localPath", 9);
        arrayMap.put(CallMethod.ARG_EXTRA_STRING, 6);
        arrayMap.put("photoCount", 15);
        arrayMap.put("sortBy", 22);
        arrayMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, 21);
        arrayMap.put("realDateModified", 11);
        arrayMap.put("query_flags", -5);
    }

    @Override // com.miui.gallery.provider.cache.IRecord
    public long getId() {
        return this.id.longValue();
    }

    @Override // com.miui.gallery.provider.cache.IAlbum
    public String getName() {
        return this.mName;
    }

    public Long getCoverId() {
        Long l = this.mCoverId;
        return Long.valueOf(l == null ? -1L : l.longValue());
    }

    public String getCoverPath() {
        return this.mCoverPath;
    }

    public String getCoverSha1() {
        return this.mCoverSha1;
    }

    public int getCoverSyncState() {
        return this.mCoverSyncState.intValue();
    }

    public long getCoverSize() {
        Long l = this.mCoverSize;
        if (l == null) {
            return 0L;
        }
        return l.longValue();
    }

    public boolean isManualSetCover() {
        return this.isManualSetCover;
    }

    public int getPhotoCount() {
        Integer num = this.mPhotoCount;
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public Album.ExtraInfo getExtraBean() {
        if (this.mExtraBean == null) {
            String str = this.mExtraInfo;
            this.mExtraBean = (str == null || str.isEmpty()) ? new Album.ExtraInfo() : Album.ExtraInfo.newInstance(this.mExtraInfo);
        }
        return this.mExtraBean;
    }

    public String getServerId() {
        return this.mServerId;
    }

    public long getAttributes() {
        Long l = this.mAttributes;
        if (l == null) {
            return 0L;
        }
        return l.longValue();
    }

    public long getDateTaken() {
        Long l = this.mDateTaken;
        if (l == null) {
            return 0L;
        }
        return l.longValue();
    }

    public long getDateModified() {
        Long l = this.mDateModified;
        if (l == null) {
            return 0L;
        }
        return l.longValue();
    }

    public Integer getSortBy() {
        Integer num = this.mSortBy;
        return Integer.valueOf(num == null ? 0 : num.intValue());
    }

    public String getDirectoryPath() {
        return this.mDirectoryPath;
    }

    public String getSortPosition() {
        String str = this.mSortInfo;
        return str == null ? "0" : str;
    }

    public boolean isDeleted() {
        String str;
        Integer num = this.mLocalFlag;
        return num != null && (num.intValue() == -1 || this.mLocalFlag.intValue() == 2 || this.mLocalFlag.intValue() == 11 || this.mLocalFlag.intValue() == 15 || ((str = this.mServerStatus) != null && str.equals("deleted")));
    }

    public Long getAlbumSize() {
        Long l = this.mAlbumSize;
        return Long.valueOf(l == null ? 0L : l.longValue());
    }

    public Album transform() {
        Album album = new Album(getId());
        album.setPhotoCount(getPhotoCount());
        if (-1 != getCoverId().longValue()) {
            album.setCover(new Album.CoverBean(getCoverId().longValue(), getCoverPath(), getCoverSha1(), getCoverSyncState(), getCoverSize(), isManualSetCover()));
        }
        album.setExtraInfo(getExtraBean());
        if (getSortBy() != null) {
            album.setSortBy(getSortBy().intValue());
        }
        album.setServerId(getServerId());
        album.setSortInfo(getSortPosition());
        album.setAlbumName(getName());
        album.setAttributes(getAttributes());
        album.setLocalPath(getDirectoryPath());
        album.setDateModified(getDateModified());
        album.setDateTaken(getDateTaken());
        if (getAlbumSize() != null) {
            album.setAlbumSize(getAlbumSize().longValue());
        }
        return album;
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    /* renamed from: copy */
    public AlbumCacheItem mo1224copy() {
        AlbumCacheItem albumCacheItem = new AlbumCacheItem();
        albumCacheItem.id = this.id;
        albumCacheItem.mName = this.mName;
        albumCacheItem.mAttributes = this.mAttributes;
        albumCacheItem.mCoverId = this.mCoverId;
        albumCacheItem.mCoverSize = this.mCoverSize;
        albumCacheItem.mCoverSyncState = this.mCoverSyncState;
        albumCacheItem.mCoverSha1 = this.mCoverSha1;
        albumCacheItem.mCoverPath = this.mCoverPath;
        albumCacheItem.isManualSetCover = this.isManualSetCover;
        albumCacheItem.mEditColumns = this.mEditColumns;
        albumCacheItem.mDirectoryPath = this.mDirectoryPath;
        albumCacheItem.mDateTaken = this.mDateTaken;
        albumCacheItem.mSortBy = this.mSortBy;
        albumCacheItem.mSortInfo = this.mSortInfo;
        albumCacheItem.mExtraInfo = this.mExtraInfo;
        albumCacheItem.mPhotoCount = this.mPhotoCount;
        albumCacheItem.mLocalFlag = this.mLocalFlag;
        albumCacheItem.mServerStatus = this.mServerStatus;
        albumCacheItem.mServerTag = this.mServerTag;
        albumCacheItem.mServerId = this.mServerId;
        albumCacheItem.mDateModified = this.mDateModified;
        albumCacheItem.mAlbumSize = this.mAlbumSize;
        return albumCacheItem;
    }

    public String toString() {
        return "AlbumCacheItem{id=" + this.id + ", mPhotoCount=" + this.mPhotoCount + ", mServerId='" + this.mServerId + CoreConstants.SINGLE_QUOTE_CHAR + ", mAttributes=" + this.mAttributes + ", mDateTaken=" + this.mDateTaken + ", mDirectoryPath='" + this.mDirectoryPath + CoreConstants.SINGLE_QUOTE_CHAR + ", mSortPosition=" + this.mSortInfo + ", mLocalFlag=" + this.mLocalFlag + ", mServerStatus='" + this.mServerStatus + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return Objects.equals(this.id, ((AlbumCacheItem) obj).id);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    public Object get(int i, boolean z) {
        switch (i) {
            case 0:
                return this.id;
            case 1:
                String str = this.mName;
                if (str != null) {
                    return str;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 2:
                return this.mAttributes;
            case 3:
                return this.mDateTaken;
            case 4:
                return this.mDateModified;
            case 5:
                return String.valueOf(this.mSortInfo);
            case 6:
                return this.mExtraInfo;
            case 7:
                return this.mLocalFlag;
            case 8:
                return this.mServerId;
            case 9:
                return this.mDirectoryPath;
            case 10:
                Long l = this.mCoverId;
                if (l != null) {
                    return l;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_LONG;
            case 11:
                return this.mRealDateModified;
            case 12:
                return this.mServerTag;
            case 13:
                return this.mServerStatus;
            case 14:
                String str2 = this.mEditColumns;
                if (str2 != null) {
                    return str2;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 15:
                return this.mPhotoCount;
            case 16:
                return this.mCoverSyncState;
            case 17:
                return this.mCoverSize;
            case 18:
                String str3 = this.mCoverPath;
                if (str3 != null) {
                    return str3;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 19:
                String str4 = this.mCoverSha1;
                if (str4 != null) {
                    return str4;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 20:
                return this.isManualSetCover ? CacheItem.TRUE : CacheItem.FALSE;
            case 21:
                return this.mAlbumSize;
            case 22:
                return this.mSortBy;
            default:
                throw new IllegalArgumentException(" not recognized column. " + i);
        }
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    public int getType(int i) {
        switch (i) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 7:
            case 10:
            case 11:
            case 15:
            case 16:
            case 17:
            case 20:
            case 21:
            case 22:
                return 1;
            case 1:
                return this.mName != null ? 3 : 0;
            case 5:
                return 3;
            case 6:
                return this.mExtraInfo != null ? 3 : 0;
            case 8:
                return this.mServerId != null ? 3 : 0;
            case 9:
                return this.mDirectoryPath != null ? 3 : 0;
            case 12:
                return this.mServerTag != null ? 1 : 0;
            case 13:
                return this.mServerStatus != null ? 3 : 0;
            case 14:
                return this.mEditColumns != null ? 3 : 0;
            case 18:
                return this.mCoverPath != null ? 3 : 0;
            case 19:
                return this.mCoverSha1 != null ? 3 : 0;
            default:
                throw new IllegalArgumentException(" not recognized column. " + i);
        }
    }

    public void fillCoverAndPhotoCount() {
        long currentTimeMillis = System.currentTimeMillis();
        DefaultLogger.d("AlbumCacheManager", "fillCoverAndPhotoCount start: [%s]", this.mName);
        internalCalculateCoverInfo(internalCalculateItemCountAndGetItems(null));
        DefaultLogger.d("AlbumCacheManager", "fillCoverAndPhotoCount end: [%s], photoCount:[%d], coverPath:[%s], cost:[%s]", this.mName, this.mPhotoCount, this.mCoverPath, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4, types: [com.miui.gallery.provider.cache.Filter$Comparator, java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v5 */
    public List internalCalculateItemCountAndGetItems(Integer num) {
        List list;
        boolean z;
        List list2;
        ?? r9;
        long longValue = this.id.longValue();
        boolean isOtherShareAlbumId = ShareAlbumHelper.isOtherShareAlbumId(longValue);
        CacheManager shareMediaManager = isOtherShareAlbumId ? ShareMediaManager.getInstance() : MediaManager.getInstance();
        if (!shareMediaManager.isInitialized()) {
            return Collections.emptyList();
        }
        boolean isOnlyShowLocalPhoto = GalleryPreferences.LocalMode.isOnlyShowLocalPhoto();
        LinkedList<Filter> linkedList = new LinkedList();
        boolean isVideoAlbum = Album.isVideoAlbum(longValue);
        boolean isAllPhotosAlbum = Album.isAllPhotosAlbum(longValue);
        boolean isFavoritesAlbum = Album.isFavoritesAlbum(longValue);
        boolean isScreenshotsRecorders = Album.isScreenshotsRecorders(longValue);
        int i = 0;
        if (isVideoAlbum || isAllPhotosAlbum || isFavoritesAlbum || isScreenshotsRecorders) {
            linkedList.add(shareMediaManager.getQueryFactory().getFilter(45, Filter.Comparator.NOT_EQUALS, String.valueOf(2064L)));
            if (isVideoAlbum) {
                if (num != null && num.intValue() != 2) {
                    list = new ArrayList(0);
                    z = true;
                    list2 = list;
                } else {
                    linkedList.add(shareMediaManager.getQueryFactory().getFilter(6, Filter.Comparator.EQUALS, String.valueOf(2)));
                }
            } else if (isFavoritesAlbum) {
                linkedList.add(shareMediaManager.getQueryFactory().getFilter(33, null, null));
            } else if (isScreenshotsRecorders) {
                linkedList.add(shareMediaManager.getQueryFactory().getFilter(2, Filter.Comparator.IN, "(" + AlbumCacheManager.getInstance().getScreenshotsAlbumId() + "," + AlbumCacheManager.getInstance().getScreenRecordersAlbumId() + ")"));
            }
            list = null;
            z = true;
            list2 = list;
        } else {
            CacheItem.QueryFactory queryFactory = shareMediaManager.getQueryFactory();
            Filter.Comparator comparator = Filter.Comparator.EQUALS;
            if (isOtherShareAlbumId) {
                longValue = ShareAlbumHelper.getOriginalAlbumId(longValue);
            }
            linkedList.add(queryFactory.getFilter(2, comparator, String.valueOf(longValue)));
            z = false;
            list2 = null;
        }
        if (num != null) {
            linkedList.add(shareMediaManager.getQueryFactory().getFilter(6, Filter.Comparator.EQUALS, String.valueOf(num)));
        }
        if (isOnlyShowLocalPhoto) {
            r9 = 0;
            linkedList.add(shareMediaManager.getQueryFactory().getFilter(-1, null, null));
        } else {
            r9 = 0;
        }
        if (z && !isOtherShareAlbumId) {
            linkedList.add(shareMediaManager.getQueryFactory().getFilter(-3, r9, r9));
        }
        Filter.CompoundFilter compoundFilter = null;
        for (Filter filter : linkedList) {
            compoundFilter = compoundFilter == null ? filter : new Filter.CompoundFilter(Filter.Compound.AND, compoundFilter, filter);
        }
        if (list2 == null) {
            list2 = shareMediaManager.internalQueryByFilter(compoundFilter);
        }
        if (isAllPhotosAlbum) {
            HashMap hashMap = new HashMap();
            Iterator it = list2.iterator();
            while (it.hasNext()) {
                MediaCacheItem mediaCacheItem = (MediaCacheItem) it.next();
                if (mediaCacheItem.getBurstGroupKey() > 0) {
                    if (hashMap.containsKey(Long.valueOf(mediaCacheItem.getBurstGroupKey()))) {
                        it.remove();
                    } else {
                        hashMap.put(Long.valueOf(mediaCacheItem.getBurstGroupKey()), null);
                    }
                }
                i++;
            }
            this.mPhotoCount = Integer.valueOf(i);
            return list2;
        }
        this.mPhotoCount = Integer.valueOf(list2.size());
        return list2;
    }

    /* JADX WARN: Removed duplicated region for block: B:81:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x00f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void internalCalculateCoverInfo(java.util.List<? extends com.miui.gallery.provider.cache.MediaCacheItem> r10) {
        /*
            r9 = this;
            long r0 = r9.getId()
            boolean r0 = com.miui.gallery.provider.ShareAlbumHelper.isOtherShareAlbumId(r0)
            if (r0 == 0) goto Lf
            com.miui.gallery.provider.cache.ShareMediaManager r0 = com.miui.gallery.provider.cache.ShareMediaManager.getInstance()
            goto L13
        Lf:
            com.miui.gallery.provider.cache.MediaManager r0 = com.miui.gallery.provider.cache.MediaManager.getInstance()
        L13:
            java.lang.Long r1 = r9.mCoverId
            r2 = 0
            r3 = 0
            if (r1 == 0) goto L9b
            long r4 = r1.longValue()
            r6 = 0
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 == 0) goto L9b
            boolean r4 = r9.isManualSetCover
            if (r4 == 0) goto L9b
            long r4 = r1.longValue()
            r6 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 <= 0) goto L34
            r4 = 1
            goto L35
        L34:
            r4 = r2
        L35:
            if (r4 == 0) goto L68
            com.miui.gallery.provider.cache.ShareMediaManager r4 = com.miui.gallery.provider.cache.ShareMediaManager.getInstance()
            com.miui.gallery.provider.cache.CacheItem$QueryFactory r5 = r0.getQueryFactory()
            com.miui.gallery.provider.cache.Filter$Comparator r6 = com.miui.gallery.provider.cache.Filter.Comparator.EQUALS
            java.lang.String r7 = java.lang.String.valueOf(r1)
            r8 = 29
            com.miui.gallery.provider.cache.Filter$CompareFilter r5 = r5.getFilter(r8, r6, r7)
            java.util.List r4 = r4.internalQueryByFilter(r5)
            if (r4 == 0) goto L57
            boolean r5 = r4.isEmpty()
            if (r5 == 0) goto L8c
        L57:
            com.miui.gallery.provider.cache.CacheItem$QueryFactory r4 = r0.getQueryFactory()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            com.miui.gallery.provider.cache.Filter$CompareFilter r1 = r4.getFilter(r8, r6, r1)
            java.util.List r4 = r0.internalQueryByFilter(r1)
            goto L8c
        L68:
            java.lang.Long r4 = r9.mCoverId
            long r4 = r4.longValue()
            boolean r4 = com.miui.gallery.provider.cache.ShareMediaManager.isOtherShareMediaId(r4)
            if (r4 == 0) goto L79
            com.miui.gallery.provider.cache.ShareMediaManager r4 = com.miui.gallery.provider.cache.ShareMediaManager.getInstance()
            goto L7a
        L79:
            r4 = r0
        L7a:
            com.miui.gallery.provider.cache.CacheItem$QueryFactory r0 = r0.getQueryFactory()
            com.miui.gallery.provider.cache.Filter$Comparator r5 = com.miui.gallery.provider.cache.Filter.Comparator.EQUALS
            java.lang.String r1 = java.lang.String.valueOf(r1)
            com.miui.gallery.provider.cache.Filter$CompareFilter r0 = r0.getFilter(r2, r5, r1)
            java.util.List r4 = r4.internalQueryByFilter(r0)
        L8c:
            boolean r0 = r4.isEmpty()
            if (r0 != 0) goto L99
            java.lang.Object r0 = r4.get(r2)
            com.miui.gallery.provider.cache.MediaCacheItem r0 = (com.miui.gallery.provider.cache.MediaCacheItem) r0
            goto L9c
        L99:
            r9.isManualSetCover = r2
        L9b:
            r0 = r3
        L9c:
            if (r0 != 0) goto Lb0
            boolean r1 = r10.isEmpty()
            if (r1 != 0) goto Lb0
            com.miui.gallery.provider.cache.AlbumCacheItem$$ExternalSyntheticLambda0 r0 = new com.miui.gallery.provider.cache.AlbumCacheItem$$ExternalSyntheticLambda0
            r0.<init>()
            java.lang.Object r10 = java.util.Collections.max(r10, r0)
            r0 = r10
            com.miui.gallery.provider.cache.MediaCacheItem r0 = (com.miui.gallery.provider.cache.MediaCacheItem) r0
        Lb0:
            if (r0 == 0) goto Lf1
            long r1 = r0.getId()
            java.lang.Long r10 = java.lang.Long.valueOf(r1)
            r9.mCoverId = r10
            java.lang.String r10 = r0.getAliasClearThumbnail()
            r9.mCoverPath = r10
            java.lang.String r10 = r0.getSha1()
            r9.mCoverSha1 = r10
            java.lang.Long r10 = r0.getAliasSyncState()
            int r10 = r10.intValue()
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)
            r9.mCoverSyncState = r10
            long r0 = r0.getSize()
            java.lang.Long r10 = java.lang.Long.valueOf(r0)
            r9.mCoverSize = r10
            java.lang.String r10 = r9.mCoverPath
            boolean r10 = android.text.TextUtils.isEmpty(r10)
            if (r10 == 0) goto Lfd
            java.lang.String r10 = r9.mCoverSha1
            java.lang.String r10 = calculateCoverBySha1IfHave(r10)
            r9.mCoverPath = r10
            goto Lfd
        Lf1:
            r9.isManualSetCover = r2
            r9.mCoverId = r3
            r9.mCoverPath = r3
            r9.mCoverSha1 = r3
            r9.mCoverSyncState = r3
            r9.mCoverSize = r3
        Lfd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cache.AlbumCacheItem.internalCalculateCoverInfo(java.util.List):void");
    }

    public /* synthetic */ int lambda$internalCalculateCoverInfo$0(MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        if (this.isVirtualAlbum && !Album.isScreenshotsRecorders(this.id.longValue())) {
            return Long.compare(mediaCacheItem.getDateModified(), mediaCacheItem2.getDateModified());
        }
        return Long.compare(mediaCacheItem.getAliasSortTime(), mediaCacheItem2.getAliasSortTime());
    }

    public static String calculateCoverBySha1IfHave(String str) {
        if (!TextUtils.isEmpty(str)) {
            return CloudUtils.getMicroPath(str);
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public static class Generator implements CacheItem.Generator<AlbumCacheItem> {
        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        /* renamed from: from */
        public AlbumCacheItem mo1226from(Cursor cursor) {
            AlbumCacheItem albumCacheItem = new AlbumCacheItem();
            albumCacheItem.id = Long.valueOf(cursor.getLong(0));
            albumCacheItem.isVirtualAlbum = Album.isVirtualAlbum(albumCacheItem.id.longValue());
            albumCacheItem.mExtraInfo = ParseUtils.getString(cursor, 6);
            albumCacheItem.mDirectoryPath = ParseUtils.getString(cursor, 9);
            albumCacheItem.mLocalFlag = ParseUtils.getInt(cursor, 7);
            albumCacheItem.mServerId = ParseUtils.getString(cursor, 8);
            albumCacheItem.mDateTaken = ParseUtils.getLong(cursor, 3);
            albumCacheItem.mDateModified = ParseUtils.getLong(cursor, 4);
            albumCacheItem.mRealDateModified = ParseUtils.getLong(cursor, 11);
            boolean z = true;
            albumCacheItem.mName = ParseUtils.getString(cursor, 1);
            albumCacheItem.mAttributes = ParseUtils.getLong(cursor, 2);
            albumCacheItem.mSortInfo = cursor.getString(5);
            albumCacheItem.mSortBy = ParseUtils.getInt(cursor, 22);
            albumCacheItem.mCoverId = ParseUtils.getLong(cursor, 10);
            albumCacheItem.mExtraInfo = ParseUtils.getString(cursor, 6);
            albumCacheItem.mAlbumSize = ParseUtils.getLong(cursor, 21);
            albumCacheItem.mEditColumns = ParseUtils.getString(cursor, 14);
            albumCacheItem.mServerStatus = ParseUtils.getString(cursor, 13);
            albumCacheItem.mServerTag = ParseUtils.getLong(cursor, 12);
            String cursorString = CursorUtils.getCursorString(cursor, "coverPath");
            if (cursorString != null) {
                albumCacheItem.mCoverPath = cursorString;
            }
            String cursorString2 = CursorUtils.getCursorString(cursor, "coverSha1");
            if (cursorString2 != null) {
                albumCacheItem.mCoverSha1 = cursorString2;
            }
            if (!cursor.isNull(CursorUtils.getColumnIndex(cursor, "coverSize"))) {
                albumCacheItem.mCoverSize = Long.valueOf(CursorUtils.getCursorLong(cursor, "coverSize"));
            }
            if (!cursor.isNull(CursorUtils.getColumnIndex(cursor, "coverSyncState"))) {
                albumCacheItem.mCoverSyncState = Integer.valueOf(CursorUtils.getCursorInt(cursor, "coverSyncState"));
            }
            if (!cursor.isNull(CursorUtils.getColumnIndex(cursor, "is_manual_set_cover"))) {
                if (CursorUtils.getCursorInt(cursor, "is_manual_set_cover") != 1) {
                    z = false;
                }
                albumCacheItem.isManualSetCover = z;
            }
            if (!cursor.isNull(CursorUtils.getColumnIndex(cursor, "photoCount"))) {
                albumCacheItem.mPhotoCount = Integer.valueOf(CursorUtils.getCursorInt(cursor, "photoCount"));
            } else {
                albumCacheItem.mPhotoCount = 0;
            }
            if (TextUtils.isEmpty(albumCacheItem.mCoverPath)) {
                albumCacheItem.mCoverPath = AlbumCacheItem.calculateCoverBySha1IfHave(albumCacheItem.getCoverSha1());
            }
            return albumCacheItem;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        /* renamed from: from */
        public AlbumCacheItem mo1225from(long j, ContentValues contentValues) {
            AlbumCacheItem albumCacheItem = new AlbumCacheItem();
            albumCacheItem.id = contentValues.getAsLong(j.c);
            albumCacheItem.mCoverId = contentValues.getAsLong("coverId");
            albumCacheItem.mExtraInfo = contentValues.getAsString(CallMethod.ARG_EXTRA_STRING);
            albumCacheItem.mDirectoryPath = contentValues.getAsString("localPath");
            albumCacheItem.mLocalFlag = contentValues.getAsInteger("localFlag");
            albumCacheItem.mServerId = contentValues.getAsString("serverId");
            albumCacheItem.mServerStatus = contentValues.getAsString("serverStatus");
            albumCacheItem.mServerTag = contentValues.getAsLong("serverTag");
            albumCacheItem.mDateTaken = contentValues.getAsLong("dateTaken");
            albumCacheItem.mDateModified = contentValues.getAsLong("dateModified");
            albumCacheItem.mRealDateModified = contentValues.getAsLong("realDateModified");
            albumCacheItem.mName = contentValues.getAsString("name");
            if (TextUtils.isEmpty(albumCacheItem.mName)) {
                albumCacheItem.mName = contentValues.getAsString("fileName");
            }
            albumCacheItem.mAttributes = contentValues.getAsLong("attributes");
            albumCacheItem.mSortInfo = contentValues.getAsString("sortInfo");
            albumCacheItem.mEditColumns = contentValues.getAsString("editedColumns");
            albumCacheItem.mAlbumSize = contentValues.getAsLong(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
            String asString = contentValues.getAsString("coverPath");
            if (asString != null) {
                albumCacheItem.mCoverPath = asString;
            }
            String asString2 = contentValues.getAsString("coverSha1");
            if (asString2 != null) {
                albumCacheItem.mCoverSha1 = asString2;
            }
            if (contentValues.containsKey("coverSize")) {
                albumCacheItem.mCoverSize = contentValues.getAsLong("coverSize");
            }
            if (contentValues.containsKey("coverSyncState")) {
                albumCacheItem.mCoverSyncState = contentValues.getAsInteger("coverSyncState");
            }
            if (contentValues.containsKey("is_manual_set_cover")) {
                albumCacheItem.isManualSetCover = contentValues.getAsBoolean("is_manual_set_cover").booleanValue();
            }
            if (contentValues.containsKey("photoCount")) {
                albumCacheItem.mPhotoCount = contentValues.getAsInteger("photoCount");
            }
            if (TextUtils.isEmpty(albumCacheItem.mCoverPath) && !TextUtils.isEmpty(albumCacheItem.getCoverSha1())) {
                albumCacheItem.mCoverPath = CloudUtils.getMicroPath(albumCacheItem.getCoverSha1());
            }
            internalUpdateExtraInfo(albumCacheItem, contentValues);
            albumCacheItem.mSortBy = contentValues.getAsInteger("sortBy");
            return albumCacheItem;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        public void update(AlbumCacheItem albumCacheItem, ContentValues contentValues) {
            boolean z = true;
            int i = 0;
            boolean z2 = contentValues == AlbumCacheItem.UPDATE_COVER_AND_COUNT;
            if (contentValues.size() != 0) {
                if (contentValues.containsKey(j.c)) {
                    albumCacheItem.id = contentValues.getAsLong(j.c);
                }
                if (contentValues.containsKey("attributes")) {
                    albumCacheItem.mAttributes = contentValues.getAsLong("attributes");
                }
                if (contentValues.containsKey("dateModified")) {
                    albumCacheItem.mDateModified = contentValues.getAsLong("dateModified");
                }
                if (contentValues.containsKey("coverId")) {
                    albumCacheItem.mCoverId = contentValues.getAsLong("coverId");
                    albumCacheItem.isManualSetCover = albumCacheItem.mCoverId != null;
                } else {
                    z = z2;
                }
                if (contentValues.containsKey("dateTaken")) {
                    albumCacheItem.mDateTaken = contentValues.getAsLong("dateTaken");
                }
                if (contentValues.containsKey("name")) {
                    albumCacheItem.mName = contentValues.getAsString("name");
                }
                if (contentValues.containsKey("serverId")) {
                    albumCacheItem.mServerId = contentValues.getAsString("serverId");
                }
                if (contentValues.containsKey("serverTag")) {
                    albumCacheItem.mServerTag = contentValues.getAsLong("serverTag");
                }
                if (contentValues.containsKey("serverStatus")) {
                    albumCacheItem.mServerStatus = contentValues.getAsString("serverStatus");
                }
                if (contentValues.containsKey("localPath")) {
                    albumCacheItem.mDirectoryPath = contentValues.getAsString("localPath");
                }
                if (contentValues.containsKey("sortBy")) {
                    Integer asInteger = contentValues.getAsInteger("sortBy");
                    if (asInteger != null) {
                        i = asInteger.intValue();
                    }
                    albumCacheItem.mSortBy = Integer.valueOf(i);
                }
                if (contentValues.containsKey("sortInfo")) {
                    albumCacheItem.mSortInfo = contentValues.getAsString("sortInfo");
                }
                if (contentValues.containsKey("localFlag")) {
                    albumCacheItem.mLocalFlag = contentValues.getAsInteger("localFlag");
                }
                internalUpdateExtraInfo(albumCacheItem, contentValues);
                z2 = z;
            }
            if (z2) {
                albumCacheItem.fillCoverAndPhotoCount();
            }
        }

        public final void internalUpdateExtraInfo(AlbumCacheItem albumCacheItem, ContentValues contentValues) {
            String[] strArr;
            boolean z = false;
            for (String str : GalleryContract.Album.EXTRA_INFO_CHILDS) {
                if (contentValues.containsKey(str)) {
                    albumCacheItem.getExtraBean().update(str, contentValues.get(str));
                    if (!z) {
                        z = true;
                    }
                }
            }
            if (z) {
                albumCacheItem.mExtraInfo = GsonUtils.toJson(albumCacheItem.getExtraBean());
            }
        }

        @Override // com.miui.gallery.provider.cache.Filter.FilterFactory
        public Filter.CompareFilter<AlbumCacheItem> getFilter(int i, Filter.Comparator comparator, String str) {
            if (i == 0) {
                return new QueryFactory.IdFilter(comparator, str);
            }
            return Filter.NOT_SUPPORTED_FILTER;
        }

        @Override // com.miui.gallery.provider.cache.Filter.FilterFactory
        public CacheItem.ColumnMapper getMapper() {
            return QueryFactory.COLUMN_MAPPER;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        public String[] getProjection() {
            return AlbumConstants.DB_REAL_PROJECTION;
        }
    }

    /* loaded from: classes2.dex */
    public static class QueryFactory implements CacheItem.QueryFactory<AlbumCacheItem> {
        public static final CacheItem.ColumnMapper COLUMN_MAPPER = new CacheItem.ColumnMapper() { // from class: com.miui.gallery.provider.cache.AlbumCacheItem.QueryFactory.5
            @Override // com.miui.gallery.provider.cache.CacheItem.ColumnMapper
            public int getIndex(String str) {
                Integer num = (Integer) AlbumCacheItem.COLUMN_MAP.get(str);
                if (num == null) {
                    DefaultLogger.e("AlbumQueryFactory", "column '%s' not found", str);
                    return -1;
                }
                return num.intValue();
            }
        };

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory
        public CacheItem.Merger<AlbumCacheItem> getMerger(int i) {
            return null;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory, com.miui.gallery.provider.cache.Filter.FilterFactory
        public CacheItem.ColumnMapper getMapper() {
            return COLUMN_MAPPER;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory
        public Comparator<AlbumCacheItem> getComparator(int i, final boolean z) {
            if (i == 3) {
                return new Comparator<AlbumCacheItem>() { // from class: com.miui.gallery.provider.cache.AlbumCacheItem.QueryFactory.1
                    {
                        QueryFactory.this = this;
                    }

                    @Override // java.util.Comparator
                    public int compare(AlbumCacheItem albumCacheItem, AlbumCacheItem albumCacheItem2) {
                        int compare = Long.compare(albumCacheItem.mDateTaken.longValue(), albumCacheItem2.mDateTaken.longValue());
                        return z ? -compare : compare;
                    }
                };
            }
            if (i == 1) {
                return new Comparator<AlbumCacheItem>() { // from class: com.miui.gallery.provider.cache.AlbumCacheItem.QueryFactory.2
                    {
                        QueryFactory.this = this;
                    }

                    @Override // java.util.Comparator
                    public int compare(AlbumCacheItem albumCacheItem, AlbumCacheItem albumCacheItem2) {
                        int i2;
                        if (albumCacheItem.mName == null || albumCacheItem2.mName == null) {
                            if (albumCacheItem.mName != null) {
                                i2 = 1;
                            } else if (albumCacheItem2.mName == null) {
                                return 0;
                            } else {
                                i2 = -1;
                            }
                        } else {
                            i2 = albumCacheItem.mName.compareTo(albumCacheItem2.mName);
                        }
                        return z ? -i2 : i2;
                    }
                };
            }
            if (i == 4) {
                return new Comparator<AlbumCacheItem>() { // from class: com.miui.gallery.provider.cache.AlbumCacheItem.QueryFactory.3
                    {
                        QueryFactory.this = this;
                    }

                    @Override // java.util.Comparator
                    public int compare(AlbumCacheItem albumCacheItem, AlbumCacheItem albumCacheItem2) {
                        int compare = Long.compare(albumCacheItem.mDateModified.longValue(), albumCacheItem2.mDateModified.longValue());
                        return z ? -compare : compare;
                    }
                };
            }
            if (i != 5) {
                return null;
            }
            return new Comparator<AlbumCacheItem>() { // from class: com.miui.gallery.provider.cache.AlbumCacheItem.QueryFactory.4
                {
                    QueryFactory.this = this;
                }

                @Override // java.util.Comparator
                public int compare(AlbumCacheItem albumCacheItem, AlbumCacheItem albumCacheItem2) {
                    int compare = Double.compare(AlbumSplitGroupHelper.getSortPosition(albumCacheItem.mSortInfo), AlbumSplitGroupHelper.getSortPosition(albumCacheItem2.mSortInfo));
                    return z ? -compare : compare;
                }
            };
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory, com.miui.gallery.provider.cache.Filter.FilterFactory
        public Filter.CompareFilter<AlbumCacheItem> getFilter(int i, Filter.Comparator comparator, String str) {
            if (i == 0) {
                return new IdFilter(comparator, str);
            }
            if (i == 1) {
                return new NameFilter(comparator, str);
            }
            if (i == 9) {
                return new LocalFileFilter(comparator, str);
            }
            if (i == 7) {
                return new LocalFlagFilter(comparator, str);
            }
            if (i == 12) {
                return new ServerTagFilter(comparator, str);
            }
            if (i == 8) {
                return new ServerIdFilter(comparator, str);
            }
            if (i == 3) {
                return new AliasCreateDateFilter(comparator, str);
            }
            if (i == 2) {
                return new AttributesFilter(comparator, str);
            }
            if (i == -5) {
                return new QueryFlagsFilter(comparator, str);
            }
            if (i == 10) {
                return new CoverIdFilter(comparator, str);
            }
            if (i == 13) {
                return new ServerStatusFilter(comparator, str);
            }
            if (i == 15) {
                return new PhotoCountFilter(comparator, str);
            }
            return Filter.NOT_SUPPORTED_FILTER;
        }

        /* loaded from: classes2.dex */
        public static class CoverIdFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public static final Pattern DELIMITER_PATTERN = Pattern.compile("\\s*,\\s*");
            public Long mCoverId;
            public Set<Long> mIdSet;

            public CoverIdFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS || comparator == Filter.Comparator.NOT_EQUALS) {
                    this.mCoverId = Long.valueOf(Long.parseLong(str));
                } else if (comparator == Filter.Comparator.IN || comparator == Filter.Comparator.NOT_IN) {
                    StringBuilder sb = new StringBuilder(str);
                    int length = sb.length();
                    if (length > 0) {
                        if (sb.charAt(0) == '(') {
                            sb.deleteCharAt(0);
                            while (true) {
                                length--;
                                if (length <= 0 || sb.charAt(0) != ' ') {
                                    break;
                                }
                                sb.deleteCharAt(0);
                            }
                            if (length > 0 && sb.charAt(0) == '\'') {
                                sb.deleteCharAt(0);
                                length--;
                            }
                            if (length > 0) {
                                int i = length - 1;
                                if (sb.charAt(i) == ')') {
                                    sb.deleteCharAt(i);
                                    while (true) {
                                        length--;
                                        if (length <= 0) {
                                            break;
                                        }
                                        int i2 = length - 1;
                                        if (sb.charAt(i2) != ' ') {
                                            break;
                                        }
                                        sb.deleteCharAt(i2);
                                    }
                                    if (length > 0) {
                                        int i3 = length - 1;
                                        if (sb.charAt(i3) == '\'') {
                                            sb.deleteCharAt(i3);
                                            length--;
                                        }
                                    }
                                    this.mIdSet = new HashSet();
                                    if (length <= 0) {
                                        return;
                                    }
                                    for (String str2 : TextUtils.split(sb.toString(), DELIMITER_PATTERN)) {
                                        this.mIdSet.add(Long.valueOf(Long.parseLong(str2)));
                                    }
                                    return;
                                }
                            }
                            throw new IllegalArgumentException("argument must end with ')'");
                        }
                    }
                    throw new IllegalArgumentException("argument must start with '('");
                }
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator != Filter.Comparator.EQUALS || !albumCacheItem.getCoverId().equals(this.mCoverId)) {
                    if (this.mComparator == Filter.Comparator.IN && this.mIdSet.contains(albumCacheItem.getCoverId())) {
                        return albumCacheItem;
                    }
                    return null;
                }
                return albumCacheItem;
            }
        }

        /* loaded from: classes2.dex */
        public static class QueryFlagsFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public final boolean isExcludeEmptyAlbum;
            public final boolean isExcludeEmptySystemAlbum;
            public final boolean isExcludeEmptyThirdPartyAlbum;
            public final boolean isExcludeEmptyUserCreateAlbum;
            public final boolean isExcludeImmutableAlbum;
            public final boolean isExcludeNormalAlbum;
            public final boolean isExcludeRawAlbum;
            public final boolean isExcludeRealScreenshotsAndRecorders;
            public final boolean isExcludeSystemAlbum;
            public final boolean isInLocalMode;
            public final boolean isIncludeDeletedAlbum;
            public final boolean isJoinAllPhotoAlbum;
            public final boolean isJoinFavorites;
            public final boolean isJoinShareAlbum;
            public final boolean isJoinVideoAlbum;
            public final boolean isJoinVirtualScreenshotsRecorders;
            public long mExcludeAlbumAttributes;
            public final Integer mMediaType;
            public long mOnlyAlbumAttributes;

            /* JADX WARN: Removed duplicated region for block: B:262:0x0132  */
            /* JADX WARN: Removed duplicated region for block: B:266:0x013f  */
            /* JADX WARN: Removed duplicated region for block: B:270:0x0146  */
            /* JADX WARN: Removed duplicated region for block: B:276:0x0155  */
            /* JADX WARN: Removed duplicated region for block: B:282:0x0164  */
            /* JADX WARN: Removed duplicated region for block: B:288:0x0173  */
            /* JADX WARN: Removed duplicated region for block: B:294:0x0182 A[ADDED_TO_REGION] */
            /* JADX WARN: Removed duplicated region for block: B:300:0x018d  */
            /* JADX WARN: Removed duplicated region for block: B:303:0x019b  */
            /* JADX WARN: Removed duplicated region for block: B:307:0x01aa  */
            /* JADX WARN: Removed duplicated region for block: B:308:0x01ac  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public QueryFlagsFilter(com.miui.gallery.provider.cache.Filter.Comparator r20, java.lang.String r21) {
                /*
                    Method dump skipped, instructions count: 438
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cache.AlbumCacheItem.QueryFactory.QueryFlagsFilter.<init>(com.miui.gallery.provider.cache.Filter$Comparator, java.lang.String):void");
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (matchJoinAndExcludeFilter(albumCacheItem)) {
                    if (this.mMediaType != null && albumCacheItem.getPhotoCount() > 0) {
                        albumCacheItem = albumCacheItem.mo1224copy();
                        albumCacheItem.internalCalculateCoverInfo(albumCacheItem.internalCalculateItemCountAndGetItems(this.mMediaType));
                    }
                    if (excludeEmptyAlbum(albumCacheItem)) {
                        return null;
                    }
                    if (!this.isIncludeDeletedAlbum && albumCacheItem.isDeleted()) {
                        return null;
                    }
                    return albumCacheItem;
                }
                return null;
            }

            public final boolean excludeEmptyAlbum(AlbumCacheItem albumCacheItem) {
                if (!ShareAlbumHelper.isOtherShareAlbumId(albumCacheItem.getId()) || this.mMediaType != null) {
                    boolean z = albumCacheItem.getPhotoCount() == 0;
                    if (z && (this.isExcludeEmptyAlbum || this.isInLocalMode)) {
                        return true;
                    }
                    if (this.isExcludeEmptyThirdPartyAlbum && z && !Album.isUserCreateAlbum(albumCacheItem.getDirectoryPath()) && !Album.isSystemAlbum(albumCacheItem.getServerId())) {
                        return true;
                    }
                    if (this.isExcludeEmptySystemAlbum && z && Album.isSystemAlbum(albumCacheItem.getServerId())) {
                        return true;
                    }
                    return this.isExcludeEmptyUserCreateAlbum && z && Album.isUserCreateAlbum(albumCacheItem.getDirectoryPath());
                }
                return false;
            }

            public final boolean matchJoinAndExcludeFilter(AlbumCacheItem albumCacheItem) {
                long id = albumCacheItem.getId();
                boolean isOtherShareAlbumId = ShareAlbumHelper.isOtherShareAlbumId(id);
                boolean isVideoAlbum = Album.isVideoAlbum(id);
                boolean isFavoritesAlbum = Album.isFavoritesAlbum(id);
                boolean isAllPhotosAlbum = Album.isAllPhotosAlbum(id);
                boolean isScreenshotsRecorders = Album.isScreenshotsRecorders(id);
                if (!this.isExcludeNormalAlbum || isOtherShareAlbumId || isVideoAlbum || isFavoritesAlbum || isAllPhotosAlbum || isScreenshotsRecorders) {
                    if (this.isExcludeSystemAlbum && Album.isSystemAlbum(albumCacheItem.getServerId())) {
                        return false;
                    }
                    if (this.isExcludeRealScreenshotsAndRecorders && (Album.isScreenshotsAlbum(albumCacheItem.getServerId()) || Album.isScreenRecorderAlbum(albumCacheItem.getDirectoryPath()))) {
                        return false;
                    }
                    if (this.isExcludeImmutableAlbum && !TextUtils.isEmpty(albumCacheItem.getDirectoryPath()) && albumCacheItem.getDirectoryPath().startsWith(h.g)) {
                        return false;
                    }
                    if (this.isExcludeRawAlbum && !TextUtils.isEmpty(albumCacheItem.getDirectoryPath()) && Album.isRawAlbum(albumCacheItem.getDirectoryPath())) {
                        return false;
                    }
                    if (0 != this.mExcludeAlbumAttributes && (albumCacheItem.getAttributes() & this.mExcludeAlbumAttributes) != 0) {
                        return false;
                    }
                    if (0 != this.mOnlyAlbumAttributes) {
                        return (albumCacheItem.getAttributes() & this.mOnlyAlbumAttributes) != 0;
                    } else if (isOtherShareAlbumId) {
                        return this.isJoinShareAlbum && !this.isInLocalMode;
                    } else if (!this.isJoinAllPhotoAlbum && isAllPhotosAlbum) {
                        return false;
                    } else {
                        if (!this.isJoinFavorites && isFavoritesAlbum) {
                            return false;
                        }
                        if (!this.isJoinVideoAlbum && isVideoAlbum) {
                            return false;
                        }
                        return this.isJoinVirtualScreenshotsRecorders || !isScreenshotsRecorders;
                    }
                }
                return false;
            }
        }

        /* loaded from: classes2.dex */
        public static class AttributesFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public AttributesFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator == Filter.Comparator.EQUALS && (albumCacheItem.mAttributes.longValue() & Long.parseLong(this.mArgument)) == 0) {
                    return albumCacheItem;
                }
                if (this.mComparator == Filter.Comparator.NOT_EQUALS && (albumCacheItem.mAttributes.longValue() & Long.parseLong(this.mArgument)) != 0) {
                    return albumCacheItem;
                }
                return null;
            }
        }

        /* loaded from: classes2.dex */
        public static class IdFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public static final Pattern DELIMITER_PATTERN = Pattern.compile("\\s*,\\s*");
            public long mId;
            public Set<Long> mIdSet;

            public IdFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS || comparator == Filter.Comparator.NOT_EQUALS) {
                    this.mId = Long.parseLong(str);
                } else if (comparator == Filter.Comparator.IN || comparator == Filter.Comparator.NOT_IN) {
                    StringBuilder sb = new StringBuilder(str);
                    int length = sb.length();
                    if (length > 0) {
                        if (sb.charAt(0) == '(') {
                            sb.deleteCharAt(0);
                            while (true) {
                                length--;
                                if (length <= 0 || sb.charAt(0) != ' ') {
                                    break;
                                }
                                sb.deleteCharAt(0);
                            }
                            if (length > 0 && sb.charAt(0) == '\'') {
                                sb.deleteCharAt(0);
                                length--;
                            }
                            if (length > 0) {
                                int i = length - 1;
                                if (sb.charAt(i) == ')') {
                                    sb.deleteCharAt(i);
                                    while (true) {
                                        length--;
                                        if (length <= 0) {
                                            break;
                                        }
                                        int i2 = length - 1;
                                        if (sb.charAt(i2) != ' ') {
                                            break;
                                        }
                                        sb.deleteCharAt(i2);
                                    }
                                    if (length > 0) {
                                        int i3 = length - 1;
                                        if (sb.charAt(i3) == '\'') {
                                            sb.deleteCharAt(i3);
                                            length--;
                                        }
                                    }
                                    this.mIdSet = new HashSet();
                                    if (length <= 0) {
                                        return;
                                    }
                                    for (String str2 : TextUtils.split(sb.toString(), DELIMITER_PATTERN)) {
                                        this.mIdSet.add(Long.valueOf(Long.parseLong(str2)));
                                    }
                                    return;
                                }
                            }
                            throw new IllegalArgumentException("argument must end with ')'");
                        }
                    }
                    throw new IllegalArgumentException("argument must start with '('");
                }
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator == Filter.Comparator.EQUALS && albumCacheItem.id.longValue() == this.mId) {
                    return albumCacheItem;
                }
                if (this.mComparator == Filter.Comparator.IN && this.mIdSet.contains(albumCacheItem.id)) {
                    return albumCacheItem;
                }
                if (this.mComparator == Filter.Comparator.NOT_EQUALS && !Numbers.equals(albumCacheItem.id, this.mId)) {
                    return albumCacheItem;
                }
                if (this.mComparator == Filter.Comparator.NOT_IN && !this.mIdSet.contains(albumCacheItem.id)) {
                    return albumCacheItem;
                }
                return null;
            }
        }

        /* loaded from: classes2.dex */
        public static class LocalFlagFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public static final Pattern LOCAL_FLAG_PATTERN = Pattern.compile("[0-9]+");
            public int mLocalFlag;
            public Set<Integer> mLocalFlagSet;

            public LocalFlagFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS || comparator == Filter.Comparator.NOT_EQUALS) {
                    this.mLocalFlag = Integer.parseInt(str);
                }
                if (comparator == Filter.Comparator.IN || comparator == Filter.Comparator.NOT_IN) {
                    this.mLocalFlagSet = new HashSet();
                    Matcher matcher = LOCAL_FLAG_PATTERN.matcher(str);
                    while (matcher.find()) {
                        this.mLocalFlagSet.add(Integer.valueOf(Integer.parseInt(matcher.group())));
                    }
                }
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator != Filter.Comparator.EQUALS || !Numbers.equals(albumCacheItem.mLocalFlag, this.mLocalFlag)) {
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && !Numbers.equals(albumCacheItem.mLocalFlag, this.mLocalFlag)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.IN && this.mLocalFlagSet.contains(albumCacheItem.mLocalFlag)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && !this.mLocalFlagSet.contains(albumCacheItem.mLocalFlag)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.IS_NULL && albumCacheItem.mLocalFlag == null) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_NULL && albumCacheItem.mLocalFlag != null) {
                        return albumCacheItem;
                    }
                    return null;
                }
                return albumCacheItem;
            }
        }

        /* loaded from: classes2.dex */
        public static class LocalFileFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public static final Pattern DELIMITER_PATTERN = Pattern.compile("'\\s*,\\s*'");
            public String mLocalFile;
            public Set<String> mLocalFileSet;

            public LocalFileFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS || comparator == Filter.Comparator.NOT_EQUALS) {
                    this.mLocalFile = str;
                } else if (comparator != Filter.Comparator.IN && comparator != Filter.Comparator.NOT_IN) {
                } else {
                    StringBuilder sb = new StringBuilder(str);
                    int length = sb.length();
                    if (length > 0 && sb.charAt(0) == '(') {
                        sb.deleteCharAt(0);
                        while (true) {
                            length--;
                            if (length <= 0 || sb.charAt(0) != ' ') {
                                break;
                            }
                            sb.deleteCharAt(0);
                        }
                        if (length > 0 && sb.charAt(0) == '\'') {
                            sb.deleteCharAt(0);
                            length--;
                        }
                        if (length > 0) {
                            int i = length - 1;
                            if (sb.charAt(i) == ')') {
                                sb.deleteCharAt(i);
                                while (true) {
                                    length--;
                                    if (length <= 0) {
                                        break;
                                    }
                                    int i2 = length - 1;
                                    if (sb.charAt(i2) != ' ') {
                                        break;
                                    }
                                    sb.deleteCharAt(i2);
                                }
                                if (length > 0) {
                                    int i3 = length - 1;
                                    if (sb.charAt(i3) == '\'') {
                                        sb.deleteCharAt(i3);
                                        length--;
                                    }
                                }
                                HashSet hashSet = new HashSet();
                                this.mLocalFileSet = hashSet;
                                if (length <= 0) {
                                    return;
                                }
                                Collections.addAll(hashSet, TextUtils.split(sb.toString(), DELIMITER_PATTERN));
                                return;
                            }
                        }
                        throw new IllegalArgumentException("argument must end with ')'");
                    }
                    throw new IllegalArgumentException("argument must start with '('");
                }
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(albumCacheItem.mDirectoryPath, this.mLocalFile)) {
                    if (this.mComparator == Filter.Comparator.NOT_NULL && !TextUtils.isEmpty(albumCacheItem.mDirectoryPath)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && !TextUtils.equals(albumCacheItem.mDirectoryPath, this.mLocalFile)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.IN && this.mLocalFileSet.contains(albumCacheItem.mDirectoryPath)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && !this.mLocalFileSet.contains(albumCacheItem.mDirectoryPath)) {
                        return albumCacheItem;
                    }
                    return null;
                }
                return albumCacheItem;
            }
        }

        /* loaded from: classes2.dex */
        public static class ServerTagFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public final long mServerTag;

            public ServerTagFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mServerTag = Long.parseLong(str);
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator != Filter.Comparator.GREATER_OR_EQUAL || Numbers.compare(Long.valueOf(this.mServerTag), albumCacheItem.mServerTag) > 0) {
                    return null;
                }
                return albumCacheItem;
            }
        }

        /* loaded from: classes2.dex */
        public static class ServerIdFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public static final Pattern ID_PATTERN = Pattern.compile("[0-9]+");
            public Set<String> mIdSet;
            public String mServerId;

            public ServerIdFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS) {
                    this.mServerId = str;
                } else if (comparator == Filter.Comparator.IN) {
                    this.mIdSet = new HashSet();
                    Matcher matcher = ID_PATTERN.matcher(str);
                    while (matcher.find()) {
                        this.mIdSet.add(matcher.group());
                    }
                } else if (comparator != Filter.Comparator.NOT_IN) {
                } else {
                    this.mIdSet = new HashSet();
                    Matcher matcher2 = ID_PATTERN.matcher(str);
                    while (matcher2.find()) {
                        this.mIdSet.add(matcher2.group());
                    }
                }
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(albumCacheItem.mServerId, this.mServerId)) {
                    if (this.mComparator == Filter.Comparator.IN && this.mIdSet.contains(albumCacheItem.mServerId)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_NULL && albumCacheItem.mServerId != null) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.IS_NULL && albumCacheItem.mServerId == null) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && !this.mIdSet.contains(albumCacheItem.mServerId)) {
                        return albumCacheItem;
                    }
                    return null;
                }
                return albumCacheItem;
            }
        }

        /* loaded from: classes2.dex */
        public static class AliasCreateDateFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public static final Pattern PATTERN = Pattern.compile("\\d+");
            public Set<Long> mCreateDateSet;
            public int mDateTaken;

            public AliasCreateDateFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS) {
                    this.mDateTaken = Integer.parseInt(str);
                } else if (comparator != Filter.Comparator.IN) {
                } else {
                    this.mCreateDateSet = new HashSet();
                    Matcher matcher = PATTERN.matcher(str);
                    while (matcher.find()) {
                        this.mCreateDateSet.add(Long.valueOf(Long.parseLong(matcher.group())));
                    }
                }
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator == Filter.Comparator.EQUALS && albumCacheItem.mDateTaken.longValue() == this.mDateTaken) {
                    return albumCacheItem;
                }
                if (this.mComparator == Filter.Comparator.IN && this.mCreateDateSet.contains(albumCacheItem.mDateTaken)) {
                    return albumCacheItem;
                }
                return null;
            }
        }

        /* loaded from: classes2.dex */
        public static class NameFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public final String mName;

            public NameFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mName = str;
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(albumCacheItem.mName, this.mName)) {
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && !TextUtils.equals(albumCacheItem.mName, this.mName)) {
                        return albumCacheItem;
                    }
                    return null;
                }
                return albumCacheItem;
            }
        }

        /* loaded from: classes2.dex */
        public static class ServerStatusFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public static final Pattern DELIMITER_PATTERN = Pattern.compile("'\\s*,\\s*'");
            public String mServerStatus;
            public Set<String> mServerStatusSet;

            public ServerStatusFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                int i = AnonymousClass1.$SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator[comparator.ordinal()];
                if (i == 1) {
                    this.mServerStatus = str;
                } else if (i != 2 && i != 3) {
                } else {
                    this.mServerStatusSet = new HashSet();
                    Matcher matcher = DELIMITER_PATTERN.matcher(str);
                    while (matcher.find()) {
                        this.mServerStatusSet.add(matcher.group());
                    }
                }
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(albumCacheItem.mServerStatus, this.mServerStatus)) {
                    if (this.mComparator == Filter.Comparator.IN && this.mServerStatusSet.contains(albumCacheItem.mServerStatus)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_NULL && albumCacheItem.mServerStatus != null) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.IS_NULL && albumCacheItem.mServerStatus == null) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && !this.mServerStatusSet.contains(albumCacheItem.mServerStatus)) {
                        return albumCacheItem;
                    }
                    return null;
                }
                return albumCacheItem;
            }
        }

        /* loaded from: classes2.dex */
        public static class PhotoCountFilter extends Filter.CompareFilter<AlbumCacheItem> {
            public static final Pattern PHOTO_COUNT_PATTERN = Pattern.compile("[0-9]+");
            public Integer mPhotoCount;
            public Set<Integer> mPhotoCountSet;

            public PhotoCountFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                switch (AnonymousClass1.$SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator[comparator.ordinal()]) {
                    case 1:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        this.mPhotoCount = Integer.valueOf(Integer.parseInt(str));
                        return;
                    case 2:
                    case 3:
                        this.mPhotoCountSet = new HashSet();
                        Matcher matcher = PHOTO_COUNT_PATTERN.matcher(str);
                        while (matcher.find()) {
                            this.mPhotoCountSet.add(Integer.valueOf(Integer.parseInt(matcher.group())));
                        }
                        return;
                    default:
                        return;
                }
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                if (this.mComparator != Filter.Comparator.EQUALS || albumCacheItem.mPhotoCount == null || !albumCacheItem.mPhotoCount.equals(this.mPhotoCount)) {
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && albumCacheItem.mPhotoCount != null && !albumCacheItem.mPhotoCount.equals(this.mPhotoCount)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.IN && this.mPhotoCountSet.contains(albumCacheItem.mPhotoCount)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && !this.mPhotoCountSet.contains(albumCacheItem.mPhotoCount)) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_NULL && albumCacheItem.mPhotoCount != null) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.IS_NULL && albumCacheItem.mPhotoCount == null) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.LESS && albumCacheItem.mPhotoCount != null && this.mPhotoCount != null && albumCacheItem.mPhotoCount.intValue() < this.mPhotoCount.intValue()) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.GREATER && albumCacheItem.mPhotoCount != null && this.mPhotoCount != null && albumCacheItem.mPhotoCount.intValue() > this.mPhotoCount.intValue()) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.LESS_OR_EQUAL && albumCacheItem.mPhotoCount != null && this.mPhotoCount != null && albumCacheItem.mPhotoCount.intValue() <= this.mPhotoCount.intValue()) {
                        return albumCacheItem;
                    }
                    if (this.mComparator == Filter.Comparator.GREATER_OR_EQUAL && albumCacheItem.mPhotoCount != null && this.mPhotoCount != null && albumCacheItem.mPhotoCount.intValue() >= this.mPhotoCount.intValue()) {
                        return albumCacheItem;
                    }
                    return null;
                }
                return albumCacheItem;
            }
        }
    }

    /* renamed from: com.miui.gallery.provider.cache.AlbumCacheItem$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator;

        static {
            int[] iArr = new int[Filter.Comparator.values().length];
            $SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator = iArr;
            try {
                iArr[Filter.Comparator.EQUALS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator[Filter.Comparator.IN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator[Filter.Comparator.NOT_IN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator[Filter.Comparator.LESS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator[Filter.Comparator.GREATER.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator[Filter.Comparator.LESS_OR_EQUAL.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator[Filter.Comparator.GREATER_OR_EQUAL.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$Filter$Comparator[Filter.Comparator.NOT_EQUALS.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class UpdateManager {
        public final List<SceneCalculator> mCalculators;
        public final List<SceneChecker> mCheckers;

        public UpdateManager() {
            LinkedList linkedList = new LinkedList();
            this.mCheckers = linkedList;
            linkedList.add(new FileDownloadedChecker(null));
            linkedList.add(new CopyOrMoveValueChecker(null));
            linkedList.add(new FavoritesChecker(null));
            linkedList.add(new ScreenshotRecorderAttributesChecker(null));
            linkedList.add(new FromLocalGroupIdChecker(null));
            LinkedList linkedList2 = new LinkedList();
            this.mCalculators = linkedList2;
            linkedList2.add(new NormalSceneCalculator(null));
            linkedList2.add(new SameAlbumCoverCalculator(null));
            linkedList2.add(new ScreenshotRecorderCalculator(null));
        }

        public SceneChecker matchChecker(List<? extends CacheItem> list, ContentValues contentValues) {
            if (list != null && !list.isEmpty()) {
                for (SceneChecker sceneChecker : this.mCheckers) {
                    if (sceneChecker.isMatchSceneType(list.get(0)) && sceneChecker.check(list, contentValues)) {
                        return sceneChecker;
                    }
                }
            }
            return null;
        }

        public boolean check(List<? extends CacheItem> list, ContentValues contentValues) {
            return matchChecker(list, contentValues) != null;
        }

        public Collection<Long> calculateUpdatedAlbumIds(List<Object> list, boolean z) {
            List<Long> calculate;
            if (list == null || list.isEmpty()) {
                return new LinkedHashSet(0);
            }
            DefaultLogger.d("AlbumCache#UpdateManager", "calculateUpdatedAlbumIds for %s items, checkCover: %b", Integer.valueOf(list.size()), Boolean.valueOf(z));
            LinkedHashSet newLinkedHashSetWithExpectedSize = Sets.newLinkedHashSetWithExpectedSize(list.size());
            for (SceneCalculator sceneCalculator : this.mCalculators) {
                if (z || !(sceneCalculator instanceof SameAlbumCoverCalculator)) {
                    if (sceneCalculator.isMatchSceneType(list.get(0)) && (calculate = sceneCalculator.calculate(list)) != null && !calculate.isEmpty()) {
                        newLinkedHashSetWithExpectedSize.addAll(calculate);
                    }
                }
            }
            DefaultLogger.d("AlbumCache#UpdateManager", "albums to update: %s", newLinkedHashSetWithExpectedSize);
            return newLinkedHashSetWithExpectedSize;
        }

        /* loaded from: classes2.dex */
        public static final class CopyOrMoveValueChecker implements SceneChecker<MediaCacheItem> {
            public CopyOrMoveValueChecker() {
            }

            public /* synthetic */ CopyOrMoveValueChecker(AnonymousClass1 anonymousClass1) {
                this();
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneChecker
            public boolean check(List<MediaCacheItem> list, ContentValues contentValues) {
                if (contentValues.containsKey("localFlag")) {
                    Integer asInteger = contentValues.getAsInteger("localFlag");
                    if (asInteger == null) {
                        return false;
                    }
                    return asInteger.intValue() == 5 || asInteger.intValue() == 6 || asInteger.intValue() == 9;
                }
                return contentValues.containsKey("localGroupId");
            }
        }

        /* loaded from: classes2.dex */
        public static final class FileDownloadedChecker implements SceneChecker<MediaCacheItem> {
            public FileDownloadedChecker() {
            }

            public /* synthetic */ FileDownloadedChecker(AnonymousClass1 anonymousClass1) {
                this();
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneChecker
            public boolean check(List<MediaCacheItem> list, ContentValues contentValues) {
                return contentValues.containsKey("localFile") || contentValues.containsKey("thumbnailFile");
            }
        }

        /* loaded from: classes2.dex */
        public static final class FavoritesChecker implements SceneChecker<MediaCacheItem> {
            public FavoritesChecker() {
            }

            public /* synthetic */ FavoritesChecker(AnonymousClass1 anonymousClass1) {
                this();
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneChecker
            public boolean check(List<MediaCacheItem> list, ContentValues contentValues) {
                if (!contentValues.containsKey("description")) {
                    return contentValues.containsKey("virtual_field_is_favorites");
                }
                String asString = contentValues.getAsString("description");
                return !TextUtils.isEmpty(asString) && asString.contains("isFavorite");
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneChecker
            public List<Long> preCalculateIfNeed(ContentValues contentValues) {
                return Collections.singletonList(2147483642L);
            }
        }

        /* loaded from: classes2.dex */
        public static final class ScreenshotRecorderAttributesChecker implements SceneChecker<AlbumCacheItem> {
            public ScreenshotRecorderAttributesChecker() {
            }

            public /* synthetic */ ScreenshotRecorderAttributesChecker(AnonymousClass1 anonymousClass1) {
                this();
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneChecker
            public boolean check(List<AlbumCacheItem> list, ContentValues contentValues) {
                if (contentValues.containsKey("attributes")) {
                    return list.stream().map(AlbumCacheItem$UpdateManager$ScreenshotRecorderAttributesChecker$$ExternalSyntheticLambda0.INSTANCE).anyMatch(AlbumCacheItem$UpdateManager$ScreenshotRecorderAttributesChecker$$ExternalSyntheticLambda1.INSTANCE);
                }
                return false;
            }

            public static /* synthetic */ boolean lambda$check$0(String str) {
                return !TextUtils.isEmpty(str) && (str.equalsIgnoreCase(MIUIStorageConstants.DIRECTORY_SCREENSHOT_PATH) || str.equalsIgnoreCase(MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH));
            }
        }

        /* loaded from: classes2.dex */
        public static final class FromLocalGroupIdChecker implements SceneChecker {
            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneChecker
            public boolean isMatchSceneType(Object obj) {
                return true;
            }

            public FromLocalGroupIdChecker() {
            }

            public /* synthetic */ FromLocalGroupIdChecker(AnonymousClass1 anonymousClass1) {
                this();
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneChecker
            public boolean check(List list, ContentValues contentValues) {
                return contentValues != null && contentValues.containsKey("fromLocalGroupId");
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneChecker
            public List<Long> preCalculateIfNeed(ContentValues contentValues) {
                if (contentValues == null || !contentValues.containsKey("fromLocalGroupId")) {
                    return null;
                }
                return Collections.singletonList(contentValues.getAsLong("fromLocalGroupId"));
            }
        }

        /* loaded from: classes2.dex */
        public static final class NormalSceneCalculator implements SceneCalculator<MediaCalcItem> {
            public NormalSceneCalculator() {
            }

            public /* synthetic */ NormalSceneCalculator(AnonymousClass1 anonymousClass1) {
                this();
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneCalculator
            public boolean isMatchSceneType(Object obj) {
                return obj instanceof MediaCalcItem;
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneCalculator
            public List<Long> calculate(List<MediaCalcItem> list) {
                Long l;
                Long l2;
                LinkedList linkedList = new LinkedList();
                MediaManager mediaManager = MediaManager.getInstance();
                AlbumCacheManager albumCacheManager = AlbumCacheManager.getInstance();
                boolean z = false;
                boolean z2 = false;
                boolean z3 = false;
                boolean z4 = false;
                for (MediaCalcItem mediaCalcItem : list) {
                    boolean isOtherShareMediaId = ShareMediaManager.isOtherShareMediaId(mediaCalcItem.id);
                    if (!isOtherShareMediaId) {
                        if (!z && mediaManager.isFavorite(mediaCalcItem.id)) {
                            z = true;
                        }
                        if (!z2 && (l2 = mediaCalcItem.albumId) != null && albumCacheManager.isShowInAllPhotoAlbum(l2.longValue())) {
                            z2 = true;
                        }
                        if (!z3 && mediaCalcItem.type == 2) {
                            z3 = true;
                        }
                        if (!z4 && (l = mediaCalcItem.albumId) != null && (l.longValue() == albumCacheManager.getScreenshotsAlbumId() || mediaCalcItem.albumId.longValue() == albumCacheManager.getScreenRecordersAlbumId())) {
                            z4 = true;
                        }
                    }
                    Long l3 = mediaCalcItem.albumId;
                    if (l3 != null) {
                        linkedList.add(Long.valueOf(isOtherShareMediaId ? ShareAlbumHelper.getUniformAlbumId(l3.longValue()) : l3.longValue()));
                    }
                }
                if (z) {
                    linkedList.add(2147483642L);
                }
                if (z2) {
                    linkedList.add(2147483644L);
                }
                if (z3) {
                    linkedList.add(2147483647L);
                }
                if (z4) {
                    linkedList.add(2147483645L);
                }
                return linkedList;
            }
        }

        /* loaded from: classes2.dex */
        public static final class SameAlbumCoverCalculator implements SceneCalculator<MediaCalcItem> {
            public SameAlbumCoverCalculator() {
            }

            public /* synthetic */ SameAlbumCoverCalculator(AnonymousClass1 anonymousClass1) {
                this();
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneCalculator
            public List<Long> calculate(List<MediaCalcItem> list) {
                ArrayList arrayList = null;
                if (list != null && !list.isEmpty()) {
                    Iterator<MediaCalcItem> it = list.iterator();
                    StringBuilder sb = new StringBuilder();
                    sb.append(CoreConstants.LEFT_PARENTHESIS_CHAR);
                    sb.append(it.next().id);
                    while (it.hasNext()) {
                        sb.append(CoreConstants.COMMA_CHAR);
                        sb.append(it.next().id);
                    }
                    sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
                    List<AlbumCacheItem> internalQueryByFilter = AlbumCacheManager.getInstance().internalQueryByFilter(AlbumCacheManager.getInstance().getQueryFactory().getFilter(10, Filter.Comparator.IN, sb.toString()));
                    if (internalQueryByFilter != null && !internalQueryByFilter.isEmpty()) {
                        arrayList = new ArrayList(internalQueryByFilter.size());
                        for (AlbumCacheItem albumCacheItem : internalQueryByFilter) {
                            arrayList.add(Long.valueOf(albumCacheItem.getId()));
                        }
                    }
                }
                return arrayList;
            }
        }

        /* loaded from: classes2.dex */
        public static final class ScreenshotRecorderCalculator implements SceneCalculator<AlbumCalcItem> {
            public ScreenshotRecorderCalculator() {
            }

            public /* synthetic */ ScreenshotRecorderCalculator(AnonymousClass1 anonymousClass1) {
                this();
            }

            @Override // com.miui.gallery.provider.cache.AlbumCacheItem.UpdateManager.SceneCalculator
            public List<Long> calculate(List<AlbumCalcItem> list) {
                if (list.stream().anyMatch(AlbumCacheItem$UpdateManager$ScreenshotRecorderCalculator$$ExternalSyntheticLambda0.INSTANCE)) {
                    return Collections.singletonList(2147483645L);
                }
                return null;
            }

            public static /* synthetic */ boolean lambda$calculate$0(AlbumCalcItem albumCalcItem) {
                return !TextUtils.isEmpty(albumCalcItem.path) && (albumCalcItem.path.equalsIgnoreCase(MIUIStorageConstants.DIRECTORY_SCREENSHOT_PATH) || albumCalcItem.path.equalsIgnoreCase(MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH));
            }
        }

        /* loaded from: classes2.dex */
        public interface SceneChecker<T extends CacheItem> {
            boolean check(List<T> list, ContentValues contentValues);

            default List<Long> preCalculateIfNeed(ContentValues contentValues) {
                return null;
            }

            default boolean isMatchSceneType(Object obj) {
                return GenericUtils.getInterfaceClass(this, 0).equals(obj.getClass());
            }
        }

        /* loaded from: classes2.dex */
        public interface SceneCalculator<T> {
            List<Long> calculate(List<T> list);

            default boolean isMatchSceneType(Object obj) {
                return GenericUtils.getInterfaceClass(this, 0).equals(obj.getClass());
            }
        }
    }
}
