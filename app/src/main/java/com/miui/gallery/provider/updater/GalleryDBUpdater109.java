package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Pair;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import com.xiaomi.stat.a.j;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class GalleryDBUpdater109 extends GalleryDBUpdater {
    public static final String sNewOwnerAlbumPath = StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM.toLowerCase();
    public Map<String, Pair<Long, String>> mRealAlbumMapInfoByGroupId;
    public Map<Long, Pair<String, String>> mRealAlbumMapInfoByLocalGroupId;
    public Map<String, Pair<Long, String>> mRealAlbumMapInfoByPath;
    public final List<Pair<Long, TempBean>> mNeedFixList = new LinkedList();
    public final List<Long> mNotShowAlbumIdList = new LinkedList();
    public final List<Message> mFixDetail = new LinkedList();

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public boolean handle(int i) {
        return i == 108;
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        long currentTimeMillis = System.currentTimeMillis();
        DefaultLogger.fd("GalleryDBUpdater109", "------------------------upgrade 109 start");
        DefaultLogger.fd("GalleryDBUpdater109", "last db version 108,start fix data!");
        String str = "( CASE WHEN " + InternalContract$Cloud.ALIAS_ORIGIN_FILE_VALID + " THEN localFile WHEN " + InternalContract$Cloud.ALIAS_THUMBNAIL_VALID + " THEN thumbnailFile END ) as filePath ";
        this.mRealAlbumMapInfoByLocalGroupId = new LinkedHashMap();
        this.mRealAlbumMapInfoByGroupId = new LinkedHashMap();
        this.mRealAlbumMapInfoByPath = new LinkedHashMap();
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("album").columns(new String[]{j.c, "serverId", "localPath", "localFlag", "serverStatus", "attributes"}).create());
        int i = 0;
        int i2 = 1;
        if (query != null && query.moveToFirst()) {
            while (true) {
                long j = query.getLong(i);
                String string = query.getString(i2);
                String string2 = query.getString(2);
                if (!TextUtils.isEmpty(string2)) {
                    string2 = string2.toLowerCase();
                }
                this.mRealAlbumMapInfoByLocalGroupId.put(Long.valueOf(j), Pair.create(string, string2));
                if (!TextUtils.isEmpty(string)) {
                    this.mRealAlbumMapInfoByGroupId.put(string, Pair.create(Long.valueOf(j), string2));
                }
                if (!TextUtils.isEmpty(string2)) {
                    this.mRealAlbumMapInfoByPath.put(string2, Pair.create(Long.valueOf(j), string));
                }
                int i3 = query.getInt(3);
                String string3 = query.getString(4);
                long j2 = query.getLong(5);
                if ((!TextUtils.isEmpty(string3) && string3.equals("deleted")) || i3 == 2 || Album.isHiddenAlbum(j2) || Album.isRubbishAlbum(j2) || Album.isOtherAlbum(j2)) {
                    this.mNotShowAlbumIdList.add(Long.valueOf(j));
                }
                if (!query.moveToNext()) {
                    break;
                }
                i = 0;
                i2 = 1;
            }
        }
        if (query != null) {
            query.close();
        }
        Cursor query2 = supportSQLiteDatabase.query("SELECT _id," + str + ",groupId,localGroupId,localFlag from cloud where fromLocalGroupId is null and localFlag in(0,7,8)");
        if (query2 != null) {
            try {
                if (query2.moveToFirst()) {
                    do {
                        group(query2);
                    } while (query2.moveToNext());
                }
            } finally {
            }
        }
        if (query2 != null) {
            query2.close();
        }
        if (!this.mNotShowAlbumIdList.isEmpty()) {
            query2 = supportSQLiteDatabase.query(String.format(Locale.US, "SELECT _id," + str + ",groupId,localGroupId,localFlag from cloud where fromLocalGroupId is null and localFlag not in(-1,4,5,6,9,10,11,15) and localGroupId in (%s)", TextUtils.join(",", this.mNotShowAlbumIdList)));
            if (query2 != null) {
                try {
                    if (query2.moveToFirst()) {
                        do {
                            group(query2);
                        } while (query2.moveToNext());
                    }
                } finally {
                }
            }
            if (query2 != null) {
                query2.close();
            }
        }
        if (!this.mNeedFixList.isEmpty()) {
            try {
                ContentValues contentValues = new ContentValues(1);
                for (Map.Entry entry : ((Map) this.mNeedFixList.stream().collect(Collectors.groupingBy(new Function<Pair<Long, TempBean>, Long>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater109.1
                    @Override // java.util.function.Function
                    public Long apply(Pair<Long, TempBean> pair) {
                        return (Long) pair.first;
                    }
                }))).entrySet()) {
                    List list = (List) entry.getValue();
                    if (list != null && !list.isEmpty()) {
                        contentValues.clear();
                        long longValue = ((Long) entry.getKey()).longValue();
                        contentValues.put("localGroupId", Long.valueOf(longValue));
                        int update = supportSQLiteDatabase.update("cloud", 0, contentValues, String.format(Locale.US, "_id in (%s)", TextUtils.join(",", (List) list.stream().map(new Function<Pair<Long, TempBean>, Long>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater109.2
                            @Override // java.util.function.Function
                            public Long apply(Pair<Long, TempBean> pair) {
                                return Long.valueOf(((TempBean) pair.second).id);
                            }
                        }).collect(Collectors.toList()))), null);
                        Pair<String, String> pair = this.mRealAlbumMapInfoByLocalGroupId.get(Long.valueOf(longValue));
                        if (pair != null) {
                            this.mFixDetail.add(new Message(longValue, (String) pair.second, list));
                        }
                        DefaultLogger.fd("GalleryDBUpdater109", "fix data localGroupId to [%d],count:[%d]", entry.getKey(), Integer.valueOf(update));
                    }
                }
            } finally {
                DefaultLogger.fd("GalleryDBUpdater109", "fix data success,cost:[%d]", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                if (!this.mFixDetail.isEmpty()) {
                    ThreadManager.execute(31, new Runnable() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater109.3
                        @Override // java.lang.Runnable
                        public void run() {
                            DefaultLogger.fd("GalleryDBUpdater109", "----------------------------fix data detail info");
                            for (Message message : GalleryDBUpdater109.this.mFixDetail) {
                                DefaultLogger.fd("GalleryDBUpdater109", message.toString());
                            }
                            DefaultLogger.fd("GalleryDBUpdater109", "----------------------------fix data detail info end");
                        }
                    });
                }
            }
        }
        return UpdateResult.defaultResult();
    }

    public final void group(Cursor cursor) {
        String str;
        String str2;
        String str3;
        Object obj;
        Pair<String, String> pair;
        Object obj2;
        long j = cursor.getLong(0);
        long j2 = cursor.getLong(3);
        String string = cursor.getString(1);
        String string2 = cursor.getString(2);
        int i = cursor.getInt(4);
        if (!TextUtils.isEmpty(string)) {
            String relativePath = StorageUtils.getRelativePath(GalleryApp.sGetAndroidContext(), BaseFileUtils.getParentFolderPath(string));
            if (TextUtils.isEmpty(relativePath)) {
                return;
            }
            string = string.toLowerCase();
            str = relativePath.toLowerCase();
        } else {
            str = null;
        }
        String str4 = str;
        String str5 = string;
        if (TextUtils.isEmpty(str5) || !str5.contains("miui/gallery/cloud/owner")) {
            str2 = str5;
            str3 = str4;
        } else {
            str2 = str5;
            str3 = str4;
            if (checkUnfinishedDataMigration(j, j2, str5, string2, i, str4)) {
                return;
            }
        }
        if (i == 0) {
            if (string2 == null) {
                DefaultLogger.e("GalleryDBUpdater109", "skip: localFlag is 0,but no groupId");
                return;
            }
            Pair<Long, String> pair2 = this.mRealAlbumMapInfoByGroupId.get(string2);
            if (pair2 == null || (obj = pair2.first) == null || j2 == ((Long) obj).longValue()) {
                return;
            }
            this.mNeedFixList.add(Pair.create((Long) pair2.first, new TempBean(j, j2, str2, string2, i)));
        } else if ((i != 7 && i != 8) || (pair = this.mRealAlbumMapInfoByLocalGroupId.get(Long.valueOf(j2))) == null || TextUtils.isEmpty((CharSequence) pair.second) || TextUtils.isEmpty(str3)) {
        } else {
            String str6 = str3;
            if (((String) pair.second).contains(str6)) {
                return;
            }
            Pair<Long, String> pair3 = this.mRealAlbumMapInfoByPath.get(str6);
            if (pair3 == null || (obj2 = pair3.first) == null) {
                DefaultLogger.e("GalleryDBUpdater109", "skip: find albumInfo by localGroupId,but path:[%s] not find", str6);
            } else {
                this.mNeedFixList.add(Pair.create((Long) obj2, new TempBean(j, j2, str2, string2, i)));
            }
        }
    }

    public final boolean checkUnfinishedDataMigration(long j, long j2, String str, String str2, int i, String str3) {
        Object obj;
        Object obj2;
        if (!TextUtils.isEmpty(str3)) {
            return false;
        }
        Pair<Long, String> pair = this.mRealAlbumMapInfoByPath.get(str3);
        if (pair != null && (obj2 = pair.first) != null) {
            if (((Long) obj2).longValue() == j2) {
                return false;
            }
            DefaultLogger.fd("GalleryDBUpdater109", "checkUnfinishedDataMigration add: cloud.path=[%s],album.path=[%s],newLocalGroupId:[%d]", str, str3, pair.first);
            this.mNeedFixList.add(Pair.create((Long) pair.first, new TempBean(j, j2, str, str2, i)));
            return true;
        }
        Pair<Long, String> pair2 = this.mRealAlbumMapInfoByPath.get(str3.replace("miui/gallery/cloud/owner", sNewOwnerAlbumPath));
        if (pair2 == null || (obj = pair2.first) == null || ((Long) obj).longValue() == j2) {
            return false;
        }
        DefaultLogger.fd("GalleryDBUpdater109", "checkUnfinishedDataMigration add: cloud.path=[%s],album.path=[%s],newLocalGroupId:[%d]", str, str3, pair2.first);
        this.mNeedFixList.add(Pair.create((Long) pair2.first, new TempBean(j, j2, str, str2, i)));
        return true;
    }

    /* loaded from: classes2.dex */
    public static class Message {
        public List<Pair<Long, TempBean>> childs;
        public long newAlbumId;
        public String newAlbumPath;

        public Message(long j, String str, List<Pair<Long, TempBean>> list) {
            this.newAlbumId = j;
            this.newAlbumPath = str;
            this.childs = list;
        }

        public String toString() {
            return "----------------fix data details info{[newAlbumId=" + this.newAlbumId + ",newAlbumPath='" + this.newAlbumPath + CoreConstants.SINGLE_QUOTE_CHAR + "]\n, fix childs=[" + this.childs + "93}";
        }
    }

    /* loaded from: classes2.dex */
    public static class TempBean {
        public String albumPath;
        public String groupId;
        public long id;
        public int localFlag;
        public long localGroupId;
        public String path;

        public TempBean(long j, long j2, String str, String str2, int i) {
            this.id = j;
            this.localGroupId = j2;
            this.path = str;
            this.groupId = str2;
            this.localFlag = i;
        }

        public String toString() {
            return "TempBean{id=" + this.id + ", localGroupId=" + this.localGroupId + ", path='" + this.path + CoreConstants.SINGLE_QUOTE_CHAR + ", groupId='" + this.groupId + CoreConstants.SINGLE_QUOTE_CHAR + ", localFlag=" + this.localFlag + ", albumPath='" + this.albumPath + CoreConstants.SINGLE_QUOTE_CHAR + '}';
        }
    }
}
