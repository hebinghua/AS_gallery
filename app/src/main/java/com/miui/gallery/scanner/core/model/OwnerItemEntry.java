package com.miui.gallery.scanner.core.model;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.xiaomi.stat.a.j;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class OwnerItemEntry extends OwnerEntry implements IItemEntry {
    public static final String[] FILE_ENTRY_PROJECTION = {j.c, "localFile", "thumbnailFile", "realSize", "realDateModified", "localFlag", "serverStatus", "localGroupId"};
    public long mFileSize;
    public String mLocalFile;
    public long mLocalGroupId;
    public String mPath;
    public String mThumbnail;

    @Override // com.miui.gallery.scanner.core.model.OwnerEntry
    public boolean isLatest(BasicFileAttributes basicFileAttributes) {
        return (((this.mDateModified / 1000) > (basicFileAttributes.lastModifiedTime().toMillis() / 1000) ? 1 : ((this.mDateModified / 1000) == (basicFileAttributes.lastModifiedTime().toMillis() / 1000) ? 0 : -1)) == 0) && ((this.mFileSize > basicFileAttributes.size() ? 1 : (this.mFileSize == basicFileAttributes.size() ? 0 : -1)) == 0);
    }

    public static OwnerItemEntry fromCursor(Cursor cursor) {
        OwnerItemEntry ownerItemEntry = new OwnerItemEntry();
        ownerItemEntry.mId = cursor.getInt(0);
        ownerItemEntry.mLocalFile = cursor.getString(1);
        ownerItemEntry.mThumbnail = cursor.getString(2);
        String str = TextUtils.isEmpty(ownerItemEntry.mLocalFile) ? ownerItemEntry.mThumbnail : ownerItemEntry.mLocalFile;
        ownerItemEntry.mPath = str;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (ownerItemEntry.mPath.equals(GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH)) {
            ownerItemEntry.mPath = "";
            ownerItemEntry.mThumbnail = "";
            ownerItemEntry.mLocalFile = "";
        }
        ownerItemEntry.mFileSize = cursor.getLong(3);
        ownerItemEntry.mDateModified = cursor.getLong(4);
        ownerItemEntry.mLocalFlag = cursor.getInt(5);
        ownerItemEntry.mServerStatus = cursor.getString(6);
        ownerItemEntry.mLocalGroupId = cursor.getLong(7);
        return ownerItemEntry;
    }

    public static Map<String, OwnerItemEntry> fromFilePath(Context context, Path path) {
        OwnerItemEntry fromCursor;
        HashMap hashMap = new HashMap();
        Cursor query = context.getContentResolver().query(GalleryContract.Cloud.CLOUD_URI, FILE_ENTRY_PROJECTION, "(localFile=? COLLATE NOCASE OR thumbnailFile=? COLLATE NOCASE) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", new String[]{path.toString(), path.toString()}, null);
        if (query != null) {
            try {
                if (query.moveToNext() && (fromCursor = fromCursor(query)) != null) {
                    hashMap.put(fromCursor.mPath.toLowerCase(), fromCursor);
                }
            } catch (Throwable th) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return hashMap;
    }

    public String toString() {
        return "path=" + this.mPath + ", size=" + this.mFileSize + ", dateModified=" + this.mDateModified;
    }
}
