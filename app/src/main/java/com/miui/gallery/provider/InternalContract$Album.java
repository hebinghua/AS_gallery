package com.miui.gallery.provider;

import android.text.TextUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.constants.StorageConstants;
import java.io.File;
import java.util.Locale;

/* loaded from: classes2.dex */
public interface InternalContract$Album {
    public static final String ALBUM_LOCAL_FLAG_CREATE_OR_MANUAL_CREATE;
    public static final String ALBUM_LOCAL_FLAG_SYNCED;
    public static final String ALIAS_COVER_PATH;
    public static final String ALIAS_IN_SYSTEM_ALBUM;
    public static final String ALIAS_NON_SYSTEM_ALBUM;
    public static final String ALIAS_NOT_SYSTEM_ALBUM;
    public static final String ALIAS_USER_CREATE_ALBUM;
    public static final String SELECTION_EXCLUDE_EMPTY_SYSTEM_ALBUMS;
    public static final String SELECTION_EXCLUDE_EMPTY_THIRD_PARTY_ALBUMS;
    public static final String SELECTION_EXCLUDE_EMPTY_USER_CREATE_ALBUMS;
    public static final String SELECTION_NON_REAL_SCREENSHOT_RECORDER_LOCAL_PATH;
    public static final String SELECTION_NON_USER_CREATE_ALBUM;
    public static final String SELECTION_SYNCED_OR_CREATE;
    public static final String SELECTION_USER_CREATE_ALBUM;
    public static final String SQL_QUERY_NON_SYSTEM_ALBUMS;

    static {
        Long[] lArr;
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("(serverId IS NULL OR serverId NOT IN ('");
        sb.append(TextUtils.join("','", GalleryContract.Album.ALL_SYSTEM_ALBUM_SERVER_IDS));
        sb.append("'))");
        String sb2 = sb.toString();
        ALIAS_NON_SYSTEM_ALBUM = sb2;
        Locale locale = Locale.US;
        String format = String.format(locale, "(%s = %d AND %s = '%s')", "localFlag", 0, "serverStatus", "custom");
        ALBUM_LOCAL_FLAG_SYNCED = format;
        String format2 = String.format(locale, "(%s = %d OR %s = %d)", "localFlag", 8, "localFlag", 7);
        ALBUM_LOCAL_FLAG_CREATE_OR_MANUAL_CREATE = format2;
        SELECTION_NON_REAL_SCREENSHOT_RECORDER_LOCAL_PATH = String.format(locale, "localPath COLLATE NOCASE NOT IN (%s)", "'" + TextUtils.join("', '", GalleryContract.Album.SCREENSHOTS_AND_RECORDERS_PATH) + "'");
        SELECTION_SYNCED_OR_CREATE = "( " + format + " OR " + format2 + " ) ";
        StringBuilder sb3 = new StringBuilder();
        sb3.append("localPath LIKE '");
        sb3.append(StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM);
        sb3.append(File.separator);
        sb3.append("%' COLLATE NOCASE");
        ALIAS_USER_CREATE_ALBUM = sb3.toString();
        ALIAS_IN_SYSTEM_ALBUM = "(serverId IN ('" + TextUtils.join("','", lArr) + "'))";
        ALIAS_NOT_SYSTEM_ALBUM = "(serverId NOT IN ('" + TextUtils.join("','", lArr) + "'))";
        ALIAS_COVER_PATH = "(" + InternalContract$Cloud.ALIAS_SIZE_FIRST + ") AS coverPath";
        String str2 = "instr(LOWER(localPath), '" + str.toLowerCase() + "' ) == 0";
        SELECTION_NON_USER_CREATE_ALBUM = str2;
        String str3 = "instr(LOWER(localPath), '" + str.toLowerCase() + "' ) == 1";
        SELECTION_USER_CREATE_ALBUM = str3;
        SELECTION_EXCLUDE_EMPTY_SYSTEM_ALBUMS = "((photoCount IS NULL  OR photoCount<0) AND (serverId IS NULL OR (serverId NOT NULL AND serverId NOT IN (" + TextUtils.join(",", lArr) + "))))";
        SELECTION_EXCLUDE_EMPTY_USER_CREATE_ALBUMS = "((photoCount IS NULL  OR photoCount<0) AND (localPath NOT NULL AND " + str2 + "))";
        SELECTION_EXCLUDE_EMPTY_THIRD_PARTY_ALBUMS = "((photoCount IS NULL  OR photoCount<0) AND (serverId IN (" + TextUtils.join(",", lArr) + ") OR localPath NOT NULL AND " + str3 + "))";
        StringBuilder sb4 = new StringBuilder();
        sb4.append("SELECT _id FROM album WHERE ");
        sb4.append(sb2);
        SQL_QUERY_NON_SYSTEM_ALBUMS = sb4.toString();
    }
}
