package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.exifinterface.media.ExifInterface;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.VideoAttrsReader;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.cloud.util.SyncAutoSettingUtil;

/* loaded from: classes.dex */
public class GalleryCloudUtils {
    public static final Uri ALBUM_URI;
    public static final Uri BASE_URI;
    public static final Uri CLOUD_CACHE_URI;
    public static final Uri CLOUD_SETTING_URI;
    public static final Uri CLOUD_URI;
    public static final Uri CLOUD_USER_URI;
    public static final Uri OWNER_SUB_UBIFOCUS_URI;
    public static final Uri SHARE_ALBUM_URI;
    public static final Uri SHARE_IMAGE_URI;
    public static final Uri SHARE_SUB_UBIFOCUS_URI;
    public static final Uri SHARE_USER_URI;
    public static final Uri USER_INFO_URI;
    public static Account sAccount;

    static {
        Uri parse = Uri.parse("content://com.miui.gallery.cloud.provider");
        BASE_URI = parse;
        CLOUD_URI = parse.buildUpon().appendPath("cloud").build();
        ALBUM_URI = parse.buildUpon().appendPath("album").build();
        CLOUD_SETTING_URI = parse.buildUpon().appendPath("cloudSetting").build();
        SHARE_ALBUM_URI = parse.buildUpon().appendPath("shareAlbum").build();
        SHARE_USER_URI = parse.buildUpon().appendPath("shareUser").build();
        SHARE_IMAGE_URI = parse.buildUpon().appendPath("shareImage").build();
        CLOUD_USER_URI = parse.buildUpon().appendPath("cloudUser").build();
        CLOUD_CACHE_URI = parse.buildUpon().appendPath("cloudCache").build();
        USER_INFO_URI = parse.buildUpon().appendPath("userInfo").build();
        OWNER_SUB_UBIFOCUS_URI = parse.buildUpon().appendPath("ownerSubUbifocus").build();
        SHARE_SUB_UBIFOCUS_URI = parse.buildUpon().appendPath("shareSubUbifocus").build();
    }

    public static String getAccountName() {
        String str;
        Account account = getAccount();
        return (account == null || (str = account.name) == null) ? "" : str;
    }

    public static synchronized void resetAccountCache() {
        synchronized (GalleryCloudUtils.class) {
            sAccount = null;
        }
    }

    public static synchronized Account getAccount() {
        Account account;
        synchronized (GalleryCloudUtils.class) {
            if (sAccount == null) {
                sAccount = AccountHelper.getXiaomiAccount(GalleryApp.sGetAndroidContext());
            }
            account = sAccount;
        }
        return account;
    }

    public static boolean isGalleryCloudSyncable(Context context) {
        Account account = getAccount();
        return account != null && SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider");
    }

    public static void putExifIntToContentValues(ExifInterface exifInterface, String str, ContentValues contentValues, String str2) {
        String attribute = exifInterface.getAttribute(str);
        if (attribute == null) {
            return;
        }
        try {
            contentValues.put(str2, Integer.valueOf(attribute));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static String getFocusIndexColumnElement(boolean z) {
        return transformToEditedColumnsElement(z ? 53 : 59);
    }

    public static String transformToEditedColumnsElement(int i) {
        return "," + i + ",";
    }

    public static int findEditedColumnsElementHitCount(int i, String str) {
        int i2 = 0;
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        Matcher matcher = Pattern.compile(transformToEditedColumnsElement(i)).matcher(str);
        int i3 = 0;
        while (matcher.find(i2)) {
            i2 = matcher.end();
            i3++;
        }
        return i3;
    }

    public static String mergeEditedColumns(String str, String str2) {
        String[] split;
        String str3;
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        if (TextUtils.isEmpty(str2)) {
            return str;
        }
        for (String str4 : str.split(",+")) {
            if (!TextUtils.isEmpty(str4)) {
                str2 = str2.replace(str3, "") + ("," + str4 + ",");
            }
        }
        return str2;
    }

    public static boolean putValuesForVideo(String str, ContentValues contentValues) throws IOException {
        try {
            VideoAttrsReader read = VideoAttrsReader.read(str);
            contentValues.put("title", read.getTitle());
            contentValues.put("duration", Long.valueOf(read.getDuration() / 1000));
            contentValues.put("dateTaken", Long.valueOf(read.getDateTaken()));
            contentValues.put("exifImageWidth", Integer.valueOf(read.getVideoWidth()));
            contentValues.put("exifImageLength", Integer.valueOf(read.getVideoHeight()));
            contentValues.put("exifOrientation", Integer.valueOf(read.getOrientation()));
            ensureDateTakenAndLocation(str, false, contentValues);
            return true;
        } catch (VideoAttrsReader.VideoAttrsUnretrievableException unused) {
            HashMap hashMap = new HashMap();
            hashMap.put("name", BaseFileUtils.getFileName(str));
            SamplingStatHelper.recordCountEvent("Sync", "read_video_attrs_failed", hashMap);
            return false;
        }
    }

    public static void putValuesForImage(String str, ContentValues contentValues) throws IOException {
        ExifInterface exifInterface = new ExifInterface(str);
        String attribute = exifInterface.getAttribute("XiaomiProduct");
        contentValues.put("title", BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(str)));
        int attributeInt = exifInterface.getAttributeInt("ImageWidth", 0);
        int attributeInt2 = exifInterface.getAttributeInt("ImageLength", 0);
        if (attributeInt <= 0 || attributeInt2 <= 0) {
            BitmapFactory.Options bitmapSize = ApplicationHelper.getBitmapProvider().getBitmapSize(str);
            int i = bitmapSize.outWidth;
            attributeInt2 = bitmapSize.outHeight;
            attributeInt = i;
        }
        contentValues.put("exifImageWidth", Integer.valueOf(attributeInt));
        contentValues.put("exifImageLength", Integer.valueOf(attributeInt2));
        contentValues.put("exifOrientation", Integer.valueOf(exifInterface.getAttributeInt("Orientation", 0)));
        contentValues.put("exifGPSLatitude", exifInterface.getAttribute("GPSLatitude"));
        contentValues.put("exifGPSLongitude", exifInterface.getAttribute("GPSLongitude"));
        contentValues.put("exifMake", exifInterface.getAttribute("Make"));
        if (TextUtils.isEmpty(attribute)) {
            attribute = exifInterface.getAttribute("Model");
        }
        contentValues.put("exifModel", attribute);
        putExifIntToContentValues(exifInterface, "Flash", contentValues, "exifFlash");
        contentValues.put("exifGPSLatitudeRef", exifInterface.getAttribute("GPSLatitudeRef"));
        contentValues.put("exifGPSLongitudeRef", exifInterface.getAttribute("GPSLongitudeRef"));
        contentValues.put("exifExposureTime", exifInterface.getAttribute("ExposureTime"));
        contentValues.put("exifFNumber", exifInterface.getAttribute("FNumber"));
        contentValues.put("exifISOSpeedRatings", exifInterface.getAttribute("ISOSpeedRatings"));
        contentValues.put("exifGPSAltitude", exifInterface.getAttribute("GPSAltitude"));
        putExifIntToContentValues(exifInterface, "GPSAltitudeRef", contentValues, "exifGPSAltitudeRef");
        contentValues.put("exifGPSTimeStamp", exifInterface.getAttribute("GPSTimeStamp"));
        contentValues.put("exifGPSDateStamp", exifInterface.getAttribute("GPSDateStamp"));
        putExifIntToContentValues(exifInterface, "WhiteBalance", contentValues, "exifWhiteBalance");
        contentValues.put("exifFocalLength", exifInterface.getAttribute("FocalLength"));
        contentValues.put("exifGPSProcessingMethod", exifInterface.getAttribute("GPSProcessingMethod"));
        contentValues.put("exifDateTime", exifInterface.getAttribute("DateTime"));
        ensureDateTakenAndLocation(str, true, contentValues);
    }

    public static void ensureDateTakenAndLocation(String str, boolean z, final ContentValues contentValues) {
        String[] strArr;
        Uri uri = z ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        if (z) {
            strArr = new String[]{"datetaken", "latitude", "longitude"};
        } else {
            strArr = new String[]{"datetaken", "latitude", "longitude"};
        }
        GalleryUtils.safeQuery(UriUtil.appendLimit(uri, 1), strArr, "_data = ? ", new String[]{str}, (String) null, new GalleryUtils.QueryHandler<Void>() { // from class: com.miui.gallery.cloud.GalleryCloudUtils.1
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public Void mo1712handle(Cursor cursor) {
                if (cursor != null && cursor.moveToNext()) {
                    long j = cursor.getLong(0);
                    double d = cursor.getDouble(1);
                    double d2 = cursor.getDouble(2);
                    if (j > 0) {
                        contentValues.put("dateTaken", Long.valueOf(j));
                    }
                    if (d != SearchStatUtils.POW && contentValues.get("exifGPSLatitude") == null) {
                        contentValues.put("exifGPSLatitude", GalleryCloudUtils.convertDoubleToLaLon(d));
                    }
                    if (d2 != SearchStatUtils.POW && contentValues.get("exifGPSLongitude") == null) {
                        contentValues.put("exifGPSLongitude", GalleryCloudUtils.convertDoubleToLaLon(d2));
                    }
                }
                return null;
            }
        });
    }

    public static String convertDoubleToLaLon(double d) {
        int floor = (int) Math.floor(Math.abs(d));
        double d2 = floor;
        double floor2 = Math.floor((Math.abs(d) - d2) * 60.0d);
        double floor3 = Math.floor(((Math.abs(d) - d2) - (floor2 / 60.0d)) * 3600000.0d);
        if (d < SearchStatUtils.POW) {
            return "-" + floor + "/1," + floor2 + "/1," + floor3 + "/1000";
        }
        return floor + "/1," + floor2 + "/1," + floor3 + "/1000";
    }
}
