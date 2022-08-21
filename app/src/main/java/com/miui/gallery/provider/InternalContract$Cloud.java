package com.miui.gallery.provider;

import android.text.TextUtils;
import com.miui.gallery.provider.GalleryContract;
import com.xiaomi.stat.a.j;
import java.util.Locale;

/* loaded from: classes2.dex */
public interface InternalContract$Cloud {
    public static final String ALIAS_CLEAR_FIRST;
    public static final String ALIAS_FILE_VALID;
    public static final String ALIAS_IS_REAL_SCREENSHOT_OR_RECORDER;
    public static final String ALIAS_LOCAL_IMAGE;
    public static final String ALIAS_LOCAL_MEDIA;
    public static final String ALIAS_LOCAL_VIDEO;
    public static final String ALIAS_MICRO_VALID;
    public static final String ALIAS_ORIGIN_FILE_VALID;
    public static final String ALIAS_SIZE_FIRST;
    public static final String ALIAS_THUMBNAIL_VALID;
    public static final String ALIAS_VALID_ORIGIN_OR_THUMBNAIL;
    public static final String SELECTION_FORMAT_QUERY_MEDIA_GROUP;

    static {
        Locale locale = Locale.US;
        String format = String.format(locale, "(%s NOT NULL and %s != '')", "microthumbfile", "microthumbfile");
        ALIAS_MICRO_VALID = format;
        String format2 = String.format(locale, "(%s NOT NULL and %s != '')", "thumbnailFile", "thumbnailFile");
        ALIAS_THUMBNAIL_VALID = format2;
        String format3 = String.format(locale, "(%s NOT NULL and %s != '')", "localFile", "localFile");
        ALIAS_ORIGIN_FILE_VALID = format3;
        ALIAS_FILE_VALID = String.format(locale, "(%s OR %s)", format2, format3);
        StringBuilder sb = new StringBuilder();
        sb.append("(localGroupId IN (SELECT _id FROM album WHERE ");
        sb.append(String.format(locale, "localPath COLLATE NOCASE IN (%s)", "'" + TextUtils.join("', '", GalleryContract.Album.SCREENSHOTS_AND_RECORDERS_PATH) + "'"));
        sb.append("))");
        ALIAS_IS_REAL_SCREENSHOT_OR_RECORDER = sb.toString();
        ALIAS_SIZE_FIRST = " CASE WHEN " + format + " THEN microthumbfile WHEN " + format2 + " THEN thumbnailFile ELSE localFile END ";
        ALIAS_CLEAR_FIRST = " CASE WHEN " + format3 + " THEN localFile WHEN " + format2 + " THEN thumbnailFile ELSE microthumbfile END ";
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" CASE WHEN ");
        sb2.append(format3);
        sb2.append(" THEN ");
        sb2.append("localFile");
        sb2.append(" WHEN ");
        sb2.append(format2);
        sb2.append(" THEN ");
        sb2.append("thumbnailFile");
        sb2.append(" END ");
        ALIAS_VALID_ORIGIN_OR_THUMBNAIL = sb2.toString();
        ALIAS_LOCAL_IMAGE = "(" + format3 + " OR " + format2 + ")";
        StringBuilder sb3 = new StringBuilder();
        sb3.append("(");
        sb3.append(format3);
        sb3.append(")");
        ALIAS_LOCAL_VIDEO = sb3.toString();
        ALIAS_LOCAL_MEDIA = "((serverType=2 AND " + format3 + ") OR (serverType=1 AND (" + format3 + " OR " + format2 + ")))";
        SELECTION_FORMAT_QUERY_MEDIA_GROUP = "((specialTypeFlags !=0 AND specialTypeFlags & %s != 0) OR (mimeType='image/gif' OR title LIKE '%%BURST%%')) AND localGroupId != " + String.valueOf(-1000L) + " AND localGroupId NOT IN(SELECT " + j.c + " FROM album WHERE  ( attributes & 2048 = 0) AND (attributes & 16 = 0 ))";
    }
}
