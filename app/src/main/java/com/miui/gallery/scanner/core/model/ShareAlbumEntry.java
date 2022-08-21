package com.miui.gallery.scanner.core.model;

import android.content.Context;
import android.database.Cursor;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.SafeDBUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ShareAlbumEntry extends ShareEntry implements IAlbumEntry {
    public static final String[] SHARE_ALBUM_ENTRY_PROJECTION = {"albumId", "2147383647+_id", "serverId"};
    public String mAlbumServerId;
    public List<String> mImageSha1s;

    public static Map<String, ShareAlbumEntry> getAlbumEntryMap(Context context) {
        return (Map) SafeDBUtil.safeQuery(context, GalleryContract.ShareAlbum.OTHER_SHARE_URI, SHARE_ALBUM_ENTRY_PROJECTION, "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<HashMap<String, ShareAlbumEntry>>() { // from class: com.miui.gallery.scanner.core.model.ShareAlbumEntry.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public HashMap<String, ShareAlbumEntry> mo1808handle(Cursor cursor) {
                HashMap<String, ShareAlbumEntry> hashMap = new HashMap<>();
                while (cursor != null && cursor.moveToNext()) {
                    hashMap.put(cursor.getString(cursor.getColumnIndex("albumId")), ShareAlbumEntry.fromCursor(cursor));
                }
                return hashMap;
            }
        });
    }

    public static ShareAlbumEntry fromCursor(Cursor cursor) {
        ShareAlbumEntry shareAlbumEntry = new ShareAlbumEntry();
        shareAlbumEntry.mId = cursor.getLong(cursor.getColumnIndex("2147383647+_id"));
        shareAlbumEntry.mAlbumServerId = cursor.getString(cursor.getColumnIndex("serverId"));
        shareAlbumEntry.mImageSha1s = getImageSha1s(shareAlbumEntry.mId);
        return shareAlbumEntry;
    }

    public static ArrayList<String> getImageSha1s(long j) {
        return (ArrayList) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.ShareImage.SHARE_URI, new String[]{"sha1"}, "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND localGroupId = " + (j - 2147383647), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<ArrayList<String>>() { // from class: com.miui.gallery.scanner.core.model.ShareAlbumEntry.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public ArrayList<String> mo1808handle(Cursor cursor) {
                ArrayList<String> arrayList = new ArrayList<>();
                if (cursor == null || !cursor.moveToFirst()) {
                    return arrayList;
                }
                do {
                    arrayList.add(cursor.getString(0));
                } while (cursor.moveToNext());
                return arrayList;
            }
        });
    }
}
