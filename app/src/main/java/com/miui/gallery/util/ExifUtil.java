package com.miui.gallery.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.exifinterface.media.ExifInterface;
import ch.qos.logback.core.net.SyslogConstants;
import ch.qos.logback.core.util.FileSize;
import com.baidu.location.BDLocation;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery3d.exif.ExifInvalidFormatException;
import com.miui.gallery3d.exif.ExifTag;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import miuix.hybrid.Response;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ExifUtil {
    public static final ExifCreator<ExifInterface> sSupportExifCreator = new ExifCreator<ExifInterface>() { // from class: com.miui.gallery.util.ExifUtil.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.ExifUtil.ExifCreator
        /* renamed from: create */
        public ExifInterface mo1691create(InputStream inputStream) {
            if (inputStream == null) {
                return null;
            }
            try {
                return new ExifInterface(inputStream);
            } catch (IOException e) {
                DefaultLogger.e("ExifUtil", "failed to create exif interface from %s, %s", inputStream, e);
                return null;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.ExifUtil.ExifCreator
        /* renamed from: create */
        public ExifInterface mo1692create(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                return new ExifInterface(str);
            } catch (Throwable th) {
                DefaultLogger.e("ExifUtil", "failed to create exif interface from %s, %s", str, th);
                return null;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.ExifUtil.ExifCreator
        /* renamed from: create */
        public ExifInterface mo1690create(FileDescriptor fileDescriptor) {
            DefaultLogger.d("ExifUtil", "Not support create android.support.media.ExifInterface from fileDescriptor");
            return null;
        }
    };
    public static final ExifCreator<android.media.ExifInterface> sMediaExifCreator = new ExifCreator<android.media.ExifInterface>() { // from class: com.miui.gallery.util.ExifUtil.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.ExifUtil.ExifCreator
        @TargetApi(24)
        /* renamed from: create */
        public android.media.ExifInterface mo1691create(InputStream inputStream) {
            if (inputStream == null) {
                return null;
            }
            try {
                return new android.media.ExifInterface(inputStream);
            } catch (IOException e) {
                DefaultLogger.e("ExifUtil", "failed to create exif interface from %s, %s", inputStream, e);
                return null;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.ExifUtil.ExifCreator
        /* renamed from: create */
        public android.media.ExifInterface mo1692create(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                return new android.media.ExifInterface(str);
            } catch (IOException e) {
                DefaultLogger.e("ExifUtil", "failed to create exif interface from %s, %s", str, e);
                return null;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.ExifUtil.ExifCreator
        @TargetApi(24)
        /* renamed from: create */
        public android.media.ExifInterface mo1690create(FileDescriptor fileDescriptor) {
            if (fileDescriptor == null) {
                return null;
            }
            try {
                return new android.media.ExifInterface(fileDescriptor);
            } catch (IOException e) {
                DefaultLogger.e("ExifUtil", "failed to create exif interface from %s, %s", fileDescriptor, e);
                return null;
            }
        }
    };
    public static final ExifCreator<com.miui.gallery3d.exif.ExifInterface> sGallery3DExifCreator = new ExifCreator<com.miui.gallery3d.exif.ExifInterface>() { // from class: com.miui.gallery.util.ExifUtil.3
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.ExifUtil.ExifCreator
        /* renamed from: create */
        public com.miui.gallery3d.exif.ExifInterface mo1691create(InputStream inputStream) {
            if (inputStream == null) {
                return null;
            }
            try {
                com.miui.gallery3d.exif.ExifInterface exifInterface = new com.miui.gallery3d.exif.ExifInterface();
                exifInterface.readExif(inputStream);
                return exifInterface;
            } catch (ExifInvalidFormatException e) {
                DefaultLogger.e("ExifUtil", "failed to create exif interface from %s, %s", inputStream, e);
                return null;
            } catch (IOException e2) {
                DefaultLogger.e("ExifUtil", "failed to create exif interface from %s, %s", inputStream, e2);
                return null;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.ExifUtil.ExifCreator
        /* renamed from: create */
        public com.miui.gallery3d.exif.ExifInterface mo1692create(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                com.miui.gallery3d.exif.ExifInterface exifInterface = new com.miui.gallery3d.exif.ExifInterface();
                exifInterface.readExif(str);
                return exifInterface;
            } catch (ExifInvalidFormatException e) {
                DefaultLogger.e("ExifUtil", "failed to create exif interface from %s, %s", str, e);
                return null;
            } catch (IOException e2) {
                DefaultLogger.e("ExifUtil", "failed to create exif interface from %s, %s", str, e2);
                return null;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.ExifUtil.ExifCreator
        /* renamed from: create */
        public com.miui.gallery3d.exif.ExifInterface mo1690create(FileDescriptor fileDescriptor) {
            DefaultLogger.d("ExifUtil", "Not support create com.miui.gallery3d.exif.ExifInterface from fileDescriptor");
            return null;
        }
    };

    /* loaded from: classes2.dex */
    public interface ExifCreator<T> {
        /* renamed from: create */
        T mo1690create(FileDescriptor fileDescriptor);

        /* renamed from: create */
        T mo1691create(InputStream inputStream);

        /* renamed from: create */
        T mo1692create(String str);
    }

    public static int exifOrientationToDegrees(int i) {
        if (i != 3) {
            if (i == 6) {
                return 90;
            }
            if (i == 8) {
                return nexClip.kClip_Rotate_270;
            }
            return 0;
        }
        return nexClip.kClip_Rotate_180;
    }

    public static boolean isWidthHeightRotated(int i) {
        return i == 5 || i == 6 || i == 7 || i == 8;
    }

    public static void setUserCommentData(android.media.ExifInterface exifInterface, UserCommentData userCommentData) throws Exception {
        if (exifInterface == null || userCommentData == null) {
            return;
        }
        new UserComment(new ExifInterfaceWrapper(exifInterface)).setData(userCommentData);
    }

    public static String getUserCommentSha1(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                UserCommentData userCommentData = getUserCommentData(str);
                if (userCommentData == null) {
                    return null;
                }
                return userCommentData.getSha1();
            } catch (Exception e) {
                DefaultLogger.e("ExifUtil", "Failed to read exif!!", e);
                return null;
            }
        }
        return null;
    }

    public static UserCommentData getUserCommentData(String str) throws Exception {
        DocumentFile documentFile;
        UserComment userComment;
        if (!TextUtils.isEmpty(str) && (documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("ExifUtil", "getUserCommentData"))) != null && documentFile.exists() && documentFile.isFile()) {
            InputStream openInputStream = StorageSolutionProvider.get().openInputStream(documentFile);
            try {
                try {
                    userComment = new UserComment(new ExifInterfaceWrapper(sSupportExifCreator.mo1691create(openInputStream)));
                } catch (Exception e) {
                    e = e;
                    DefaultLogger.d("ExifUtil", "Failed to read user comment using support exif interface, %s", e);
                }
                if (userComment.isOriginalUserCommentUsable()) {
                    UserCommentData userCommentData = new UserCommentData(userComment.getSha1(), userComment.getFileExt());
                    if (openInputStream != null) {
                        openInputStream.close();
                    }
                    return userCommentData;
                }
                e = null;
                if (documentFile.length() <= FileSize.MB_COEFFICIENT) {
                    try {
                        com.miui.gallery3d.exif.ExifInterface mo1691create = sGallery3DExifCreator.mo1691create(openInputStream);
                        if (mo1691create != null) {
                            UserComment userComment2 = new UserComment(new ExifInterfaceWrapper(mo1691create));
                            if (userComment2.isOriginalUserCommentUsable() && !TextUtils.isEmpty(userComment2.getSha1())) {
                                UserCommentData userCommentData2 = new UserCommentData(userComment2.getSha1(), userComment2.getFileExt());
                                DefaultLogger.e("ExifUtil", "path: %s, exception: %s", str, e != null ? e.toString() : "unrecognizable UserComment");
                                rewriteUserComment(openInputStream, userCommentData2);
                                if (openInputStream != null) {
                                    openInputStream.close();
                                }
                                return userCommentData2;
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        DefaultLogger.d("ExifUtil", "Failed to read user comment using gallery 3d exif interface, %s", e);
                    }
                    try {
                        UserComment userComment3 = new UserComment(new ExifInterfaceWrapper(sMediaExifCreator.mo1691create(openInputStream)));
                        if (userComment3.isOriginalUserCommentUsable()) {
                            UserCommentData userCommentData3 = new UserCommentData(userComment3.getSha1(), userComment3.getFileExt());
                            if (openInputStream != null) {
                                openInputStream.close();
                            }
                            return userCommentData3;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        DefaultLogger.d("ExifUtil", "Failed to read user comment using media exif interface, %s", e);
                    }
                }
                if (openInputStream != null) {
                    openInputStream.close();
                }
                if (e == null) {
                    return null;
                }
                DefaultLogger.e("ExifUtil", "model: %s, version: %s, path: %s, exception: %s", Build.MODEL, Build.VERSION.INCREMENTAL, str, e.toString());
                throw e;
            } catch (Throwable th) {
                if (openInputStream != null) {
                    try {
                        openInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        return null;
    }

    public static void rewriteUserComment(InputStream inputStream, UserCommentData userCommentData) {
        try {
            DefaultLogger.d("ExifUtil", "Try to rewrite UserComment using android.media.ExifInterface");
            android.media.ExifInterface mo1691create = sMediaExifCreator.mo1691create(inputStream);
            setUserCommentData(mo1691create, userCommentData);
            mo1691create.saveAttributes();
        } catch (Exception e) {
            DefaultLogger.w("ExifUtil", "Failed to rewrite UserComment using android.media.ExifInterface, %s", e);
        }
    }

    public static String getXiaomiComment(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                com.miui.gallery3d.exif.ExifInterface mo1692create = sGallery3DExifCreator.mo1692create(str);
                if (mo1692create == null) {
                    return null;
                }
                return mo1692create.getXiaomiComment();
            } catch (Exception e) {
                DefaultLogger.w("ExifUtil", e);
                return null;
            }
        }
        return null;
    }

    public static String getXiaomiCommentSensorType(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                String xiaomiComment = getXiaomiComment(str);
                if (TextUtils.isEmpty(xiaomiComment)) {
                    return null;
                }
                JSONObject jSONObject = new JSONObject(xiaomiComment);
                String optString = jSONObject.optString("sensor_type");
                return TextUtils.isEmpty(optString) ? jSONObject.optString("sensorType") : optString;
            } catch (Exception e) {
                DefaultLogger.w("ExifUtil", e);
                return null;
            }
        }
        return null;
    }

    public static double getXiaomiCommentZoomMultiple(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                String xiaomiComment = getXiaomiComment(str);
                if (!TextUtils.isEmpty(xiaomiComment)) {
                    JSONObject jSONObject = new JSONObject(xiaomiComment);
                    double optDouble = jSONObject.optDouble("ZoomMultiple", -1.0d);
                    return optDouble == -1.0d ? jSONObject.optDouble("zoomMultiple", -1.0d) : optDouble;
                }
            } catch (Exception e) {
                DefaultLogger.w("ExifUtil", e);
            }
        }
        return -1.0d;
    }

    public static int getXiaomiCommentFilterId(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                String xiaomiComment = getXiaomiComment(str);
                if (!TextUtils.isEmpty(xiaomiComment)) {
                    JSONObject jSONObject = new JSONObject(xiaomiComment);
                    int optInt = jSONObject.optInt("FilterId", -1);
                    return optInt == -1 ? jSONObject.optInt("filterId", -1) : optInt;
                }
            } catch (Exception e) {
                DefaultLogger.w("ExifUtil", e);
            }
        }
        return -1;
    }

    public static boolean isFromFrontCamera(String str) {
        return "front".equalsIgnoreCase(getXiaomiCommentSensorType(str));
    }

    @Deprecated
    public static long getDateTime(ExifInterface exifInterface) {
        if (exifInterface == null) {
            return -1L;
        }
        return GalleryTimeUtils.getDateTime(exifInterface.getAttribute("DateTime"), exifInterface.getAttribute("SubSecTime"));
    }

    @Deprecated
    public static long getDateTime(android.media.ExifInterface exifInterface) {
        if (exifInterface == null) {
            return -1L;
        }
        return GalleryTimeUtils.getDateTime(exifInterface.getAttribute("DateTime"), exifInterface.getAttribute("SubSecTime"));
    }

    public static long getDateTime(android.media.ExifInterface exifInterface, boolean z) {
        if (exifInterface == null) {
            return -1L;
        }
        return z ? getDateTime(exifInterface) : GalleryTimeUtils.getDateTime(exifInterface.getAttribute("DateTime"));
    }

    public static long getDateTime(ExifInterface exifInterface, boolean z) {
        if (exifInterface == null) {
            return -1L;
        }
        return z ? getDateTime(exifInterface) : GalleryTimeUtils.getDateTime(exifInterface.getAttribute("DateTime"));
    }

    public static void setDateTime(android.media.ExifInterface exifInterface, String str) {
        if (exifInterface == null || TextUtils.isEmpty(str)) {
            return;
        }
        exifInterface.setAttribute("DateTimeOriginal", str);
        exifInterface.setAttribute("DateTime", str);
    }

    public static void setDateTime(com.miui.gallery3d.exif.ExifInterface exifInterface, String str) {
        if (exifInterface == null || TextUtils.isEmpty(str)) {
            return;
        }
        exifInterface.addDateTimeStampTag(com.miui.gallery3d.exif.ExifInterface.TAG_DATE_TIME_ORIGINAL, str);
        exifInterface.addDateTimeStampTag(com.miui.gallery3d.exif.ExifInterface.TAG_DATE_TIME, str);
    }

    public static void setSubSecTime(android.media.ExifInterface exifInterface, long j) {
        if (exifInterface == null || j <= 0) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i = calendar.get(14);
        if (i <= 0) {
            return;
        }
        exifInterface.setAttribute("SubSecTime", String.valueOf(i));
    }

    public static void setSubSecTime(com.miui.gallery3d.exif.ExifInterface exifInterface, long j) {
        if (exifInterface == null || j <= 0) {
            return;
        }
        exifInterface.addSubSecTimeStampTag(j);
    }

    public static boolean setDateTime(android.media.ExifInterface exifInterface, long j, boolean z) {
        if (exifInterface != null && j != 0) {
            try {
                SimpleDateFormat defaultDateFormat = GalleryTimeUtils.getDefaultDateFormat();
                Date date = new Date(j);
                if (z) {
                    setDateTime(exifInterface, defaultDateFormat.format(date));
                    if (getGpsDateTime(exifInterface) != -1) {
                        String format = GalleryTimeUtils.getUTCDateFormat().format(date);
                        String[] split = format.split(" ");
                        setGpsDateTime(exifInterface, split[0], split[1]);
                        DefaultLogger.d("ExifUtil", "修改照片时间(包含GPS),修改后的时间为:%s", format);
                    }
                } else {
                    setDateTime(exifInterface, defaultDateFormat.format(date));
                }
                return true;
            } catch (Exception e) {
                DefaultLogger.e("ExifUtil", e);
            }
        }
        return false;
    }

    public static boolean setDateTime(com.miui.gallery3d.exif.ExifInterface exifInterface, long j, boolean z) {
        if (exifInterface != null && j != 0) {
            try {
                SimpleDateFormat defaultDateFormat = GalleryTimeUtils.getDefaultDateFormat();
                Date date = new Date(j);
                if (z) {
                    setDateTime(exifInterface, defaultDateFormat.format(date));
                    String format = GalleryTimeUtils.getUTCDateFormat().format(date);
                    String[] split = format.split(" ");
                    setGpsDateTime(exifInterface, split[0], split[1]);
                    DefaultLogger.d("ExifUtil", "修改照片时间(包含GPS),修改后的时间为:%s", format);
                } else {
                    setDateTime(exifInterface, defaultDateFormat.format(date));
                }
                return true;
            } catch (Exception e) {
                DefaultLogger.e("ExifUtil", e);
            }
        }
        return false;
    }

    public static void setGpsDateTime(android.media.ExifInterface exifInterface, String str, String str2) {
        if (exifInterface == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        exifInterface.setAttribute("GPSDateStamp", str);
        exifInterface.setAttribute("GPSTimeStamp", str2);
    }

    public static void setGpsDateTime(com.miui.gallery3d.exif.ExifInterface exifInterface, String str, String str2) {
        if (exifInterface == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        exifInterface.addGpsDateTimeStampTag(str, str2);
    }

    public static int getOrientation(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        Integer tagIntValue = exifInterface == null ? null : exifInterface.getTagIntValue(com.miui.gallery3d.exif.ExifInterface.TAG_ORIENTATION);
        if (tagIntValue == null) {
            return 1;
        }
        return tagIntValue.intValue();
    }

    public static int getRotationDegrees(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        return exifOrientationToDegrees(getOrientation(exifInterface));
    }

    public static int getRotationDegrees(ExifInterface exifInterface) {
        int i = 1;
        if (exifInterface != null) {
            i = exifInterface.getAttributeInt("Orientation", 1);
        }
        return exifOrientationToDegrees(i);
    }

    public static int degreesToExifOrientation(int i) {
        int i2 = (i + 360) % 360;
        if (i2 != 90) {
            if (i2 == 180) {
                return 3;
            }
            return i2 != 270 ? 1 : 8;
        }
        return 6;
    }

    public static long getGpsDateTime(ExifInterface exifInterface) {
        if (exifInterface == null) {
            return -1L;
        }
        return GalleryTimeUtils.getGpsDateTime(exifInterface.getAttribute("GPSDateStamp"), exifInterface.getAttribute("GPSTimeStamp"));
    }

    public static long getGpsDateTime(android.media.ExifInterface exifInterface) {
        if (exifInterface == null) {
            return -1L;
        }
        return GalleryTimeUtils.getGpsDateTime(exifInterface.getAttribute("GPSDateStamp"), exifInterface.getAttribute("GPSTimeStamp"));
    }

    public static boolean supportRefocus(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        return exifInterface != null && (!TextUtils.isEmpty(exifInterface.getTagStringValue(34960, 2)) || exifInterface.getTag(34968, 2) != null);
    }

    public static boolean supportRefocus(String str) {
        try {
            com.miui.gallery3d.exif.ExifInterface exifInterface = new com.miui.gallery3d.exif.ExifInterface();
            exifInterface.readExif(str);
            return supportRefocus(exifInterface);
        } catch (ExifInvalidFormatException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isMotionPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        Byte tagByteValue;
        return (exifInterface == null || (tagByteValue = exifInterface.getTagByteValue(34967, 2)) == null || tagByteValue.byteValue() != 1) ? false : true;
    }

    public static boolean isWatermarkAdded(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        Integer tagIntValue;
        return (exifInterface == null || (tagIntValue = exifInterface.getTagIntValue(34971, 2)) == null || tagIntValue.intValue() != 1) ? false : true;
    }

    public static boolean isDocPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        Byte tagByteValue;
        return (exifInterface == null || (tagByteValue = exifInterface.getTagByteValue(34969, 2)) == null || tagByteValue.byteValue() != 1) ? false : true;
    }

    public static boolean isDocPhoto(String str) {
        try {
            com.miui.gallery3d.exif.ExifInterface exifInterface = new com.miui.gallery3d.exif.ExifInterface();
            exifInterface.readExif(str);
            return isDocPhoto(exifInterface);
        } catch (ExifInvalidFormatException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isMotionPhoto(String str) {
        try {
            com.miui.gallery3d.exif.ExifInterface exifInterface = new com.miui.gallery3d.exif.ExifInterface();
            exifInterface.readExif(str);
            return isMotionPhoto(exifInterface);
        } catch (ExifInvalidFormatException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isPortraitPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        return (exifInterface == null || (tag = exifInterface.getTag(42593, 2)) == null || !tag.forceGetValueAsString().trim().equals(String.valueOf(171))) ? false : true;
    }

    public static boolean isFrontPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        return (exifInterface == null || (tag = exifInterface.getTag(39321, 2)) == null || !tag.forceGetValueAsString().trim().contains("\"sensorType\":\"front\"")) ? false : true;
    }

    public static boolean isPanoPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        if (exifInterface == null || (tag = exifInterface.getTag(42593, 2)) == null) {
            return false;
        }
        String trim = tag.forceGetValueAsString().trim();
        return TextUtils.equals(trim, String.valueOf(166)) || TextUtils.equals(trim, String.valueOf((int) SyslogConstants.LOG_LOCAL6));
    }

    public static boolean isClonePhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        return (exifInterface == null || (tag = exifInterface.getTag(42593, 2)) == null || !tag.forceGetValueAsString().trim().contains(String.valueOf(185))) ? false : true;
    }

    public static boolean isSuperMoonPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        return (exifInterface == null || (tag = exifInterface.getTag(42593, 2)) == null || !tag.forceGetValueAsString().trim().contains(String.valueOf(188))) ? false : true;
    }

    public static boolean isProAmbilightPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        return (exifInterface == null || (tag = exifInterface.getTag(42593, 2)) == null || !tag.forceGetValueAsString().trim().contains(String.valueOf(187))) ? false : true;
    }

    public static boolean isSuperNightPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        return (exifInterface == null || (tag = exifInterface.getTag(42593, 2)) == null || !tag.forceGetValueAsString().trim().contains(String.valueOf(173))) ? false : true;
    }

    public static boolean isProVideoPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        return (exifInterface == null || (tag = exifInterface.getTag(42593, 2)) == null || !tag.forceGetValueAsString().trim().contains(String.valueOf((int) BDLocation.TypeServerError))) ? false : true;
    }

    public static boolean isMiMoJiPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        return (exifInterface == null || (tag = exifInterface.getTag(42593, 2)) == null || !tag.forceGetValueAsString().trim().contains(String.valueOf((int) SyslogConstants.LOG_LOCAL7))) ? false : true;
    }

    public static boolean isAiWatermarkPhoto(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        ExifTag tag;
        return (exifInterface == null || (tag = exifInterface.getTag(42593, 2)) == null || !tag.forceGetValueAsString().trim().contains(String.valueOf((int) Response.CODE_ACTION_ERROR))) ? false : true;
    }

    public static int getMTSpecialAITypeValue(com.miui.gallery3d.exif.ExifInterface exifInterface) {
        Integer tagIntValue = exifInterface != null ? exifInterface.getTagIntValue(42243, 2) : null;
        if (tagIntValue == null) {
            return -1;
        }
        return tagIntValue.intValue();
    }

    public static int getMTSpecialAITypeValue(String str) {
        try {
            com.miui.gallery3d.exif.ExifInterface exifInterface = new com.miui.gallery3d.exif.ExifInterface();
            exifInterface.readExif(str);
            return getMTSpecialAITypeValue(exifInterface);
        } catch (ExifInvalidFormatException | IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getXiaomiProduct(String str) {
        try {
            com.miui.gallery3d.exif.ExifInterface exifInterface = new com.miui.gallery3d.exif.ExifInterface();
            exifInterface.readExif(str);
            return exifInterface.getTagStringValue(39424, 2);
        } catch (ExifInvalidFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Rect adjustRectOrientation(int i, int i2, Rect rect, int i3, boolean z) {
        RectF adjustRectOrientation = adjustRectOrientation(i, i2, new RectF(rect), i3, z);
        return new Rect((int) adjustRectOrientation.left, (int) adjustRectOrientation.top, (int) adjustRectOrientation.right, (int) adjustRectOrientation.bottom);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static RectF adjustRectOrientation(int i, int i2, RectF rectF, int i3, boolean z) {
        float f = rectF.left;
        float f2 = rectF.top;
        float width = rectF.width();
        float height = rectF.height();
        if (!z) {
            if (i3 == 5) {
                i3 = 7;
            } else if (i3 == 6) {
                i3 = 8;
            } else if (i3 == 7) {
                i3 = 5;
            } else if (i3 == 8) {
                i3 = 6;
            }
        }
        boolean z2 = false;
        switch (i3) {
            case 2:
                z2 = true;
                break;
            case 3:
                f = (i - rectF.left) - rectF.width();
                f2 = (i2 - rectF.top) - rectF.height();
                break;
            case 4:
                z2 = true;
                f = (i - rectF.left) - rectF.width();
                f2 = (i2 - rectF.top) - rectF.height();
                break;
            case 5:
                z2 = true;
                f = rectF.top;
                f2 = (i - rectF.left) - rectF.width();
                width = rectF.height();
                height = rectF.width();
                i = i2;
                break;
            case 6:
                f = (i2 - rectF.top) - rectF.height();
                f2 = rectF.left;
                width = rectF.height();
                height = rectF.width();
                i = i2;
                break;
            case 7:
                z2 = true;
                f = (i2 - rectF.top) - rectF.height();
                f2 = rectF.left;
                width = rectF.height();
                height = rectF.width();
                i = i2;
                break;
            case 8:
                f = rectF.top;
                f2 = (i - rectF.left) - rectF.width();
                width = rectF.height();
                height = rectF.width();
                i = i2;
                break;
        }
        if (z2) {
            f = (i - f) - width;
        }
        return new RectF(f, f2, width + f, height + f2);
    }

    public static ExifInfo parseRotationInfo(String str, byte[] bArr) {
        if (bArr == null) {
            return parseRotationInfo((ExifInterface) createExifInterface(str, bArr, sSupportExifCreator));
        }
        com.miui.gallery3d.exif.ExifInterface exifInterface = (com.miui.gallery3d.exif.ExifInterface) createExifInterface(str, bArr, sGallery3DExifCreator);
        if (exifInterface == null) {
            return parseRotationInfo((ExifInterface) createExifInterfaceByDecryptFile(str, bArr, sSupportExifCreator));
        }
        return parseRotationInfo(exifInterface);
    }

    public static ExifInfo parseRotationInfo(ContentResolver contentResolver, Uri uri, byte[] bArr) {
        if (bArr == null) {
            try {
                return parseRotationInfo((ExifInterface) createExifInterface(contentResolver, uri, null, sSupportExifCreator));
            } catch (Exception e) {
                DefaultLogger.w("ExifUtil", "Can't read EXIF tags from uri ", e);
                return null;
            }
        }
        return parseRotationInfo((com.miui.gallery3d.exif.ExifInterface) createExifInterface(contentResolver, uri, bArr, sGallery3DExifCreator));
    }

    public static <T> ExifInfo parseRotationInfo(T t) {
        int attributeInt;
        if (t != null) {
            if (t instanceof ExifInterface) {
                attributeInt = ((ExifInterface) t).getAttributeInt("Orientation", 1);
            } else if (t instanceof com.miui.gallery3d.exif.ExifInterface) {
                attributeInt = getOrientation((com.miui.gallery3d.exif.ExifInterface) t);
            } else if (t instanceof android.media.ExifInterface) {
                attributeInt = ((android.media.ExifInterface) t).getAttributeInt("Orientation", 1);
            } else {
                DefaultLogger.w("ExifUtil", "Not supported exif interface %s", t);
            }
            return parseRotationInfo(attributeInt);
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0047 A[RETURN] */
    /* JADX WARN: Type inference failed for: r1v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <T> T createExifInterface(java.lang.String r5, byte[] r6, com.miui.gallery.util.ExifUtil.ExifCreator<T> r7) {
        /*
            r0 = 0
            if (r7 != 0) goto L4
            return r0
        L4:
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r1 = com.miui.gallery.storage.StorageSolutionProvider.get()
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r2 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.QUERY
            java.lang.String r3 = "ExifUtil"
            java.lang.String r4 = "createExifInterface"
            java.lang.String r4 = com.miui.gallery.util.FileHandleRecordHelper.appendInvokerTag(r3, r4)
            androidx.documentfile.provider.DocumentFile r1 = r1.getDocumentFile(r5, r2, r4)
            if (r1 == 0) goto L4c
            boolean r1 = r1.exists()
            if (r1 != 0) goto L1f
            goto L4c
        L1f:
            java.io.InputStream r1 = com.miui.gallery.util.BaseBitmapUtils.createInputStream(r5, r6)     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            java.lang.Object r2 = r7.mo1691create(r1)     // Catch: java.lang.Throwable -> L31 java.lang.Exception -> L34
            if (r2 == 0) goto L2d
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            return r2
        L2d:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            goto L40
        L31:
            r5 = move-exception
            r0 = r1
            goto L48
        L34:
            r2 = move-exception
            goto L3a
        L36:
            r5 = move-exception
            goto L48
        L38:
            r2 = move-exception
            r1 = r0
        L3a:
            java.lang.String r4 = "Can't read EXIF tags from file [%s] %s"
            com.miui.gallery.util.logger.DefaultLogger.w(r3, r4, r5, r2)     // Catch: java.lang.Throwable -> L31
            goto L2d
        L40:
            if (r6 == 0) goto L47
            java.lang.Object r5 = createExifInterfaceByDecryptFile(r5, r6, r7)
            return r5
        L47:
            return r0
        L48:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r0)
            throw r5
        L4c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.ExifUtil.createExifInterface(java.lang.String, byte[], com.miui.gallery.util.ExifUtil$ExifCreator):java.lang.Object");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [android.content.ContentResolver] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    public static <T> T createExifInterface(ContentResolver contentResolver, Uri uri, byte[] bArr, ExifCreator<T> exifCreator) {
        InputStream inputStream;
        Closeable closeable = null;
        try {
            if (exifCreator == null) {
                return null;
            }
            try {
                inputStream = BaseBitmapUtils.createInputStream(contentResolver, uri, bArr);
                try {
                    T mo1691create = exifCreator.mo1691create(inputStream);
                    contentResolver = inputStream;
                    if (mo1691create != null) {
                        BaseMiscUtil.closeSilently(inputStream);
                        return mo1691create;
                    }
                } catch (Exception e) {
                    e = e;
                    DefaultLogger.w("ExifUtil", "Can't read EXIF tags from uri [%s] %s", uri.toString(), e);
                    contentResolver = inputStream;
                    BaseMiscUtil.closeSilently(contentResolver);
                    return null;
                }
            } catch (Exception e2) {
                e = e2;
                inputStream = null;
            } catch (Throwable th) {
                th = th;
                BaseMiscUtil.closeSilently(closeable);
                throw th;
            }
            BaseMiscUtil.closeSilently(contentResolver);
            return null;
        } catch (Throwable th2) {
            th = th2;
            closeable = contentResolver;
        }
    }

    public static <T> T createExifInterface(InputStream inputStream, ExifCreator<T> exifCreator) {
        if (exifCreator != null) {
            try {
                if (inputStream != null) {
                    try {
                        T mo1691create = exifCreator.mo1691create(inputStream);
                        if (mo1691create != null) {
                            return mo1691create;
                        }
                    } catch (Exception e) {
                        DefaultLogger.w("ExifUtil", "Can't read EXIF tags from file ", e);
                    }
                    return null;
                }
            } finally {
                BaseMiscUtil.closeSilently(inputStream);
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x006e, code lost:
        if (r7 != null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0070, code lost:
        r7.delete();
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x008f, code lost:
        if (r7 != null) goto L23;
     */
    /* JADX WARN: Type inference failed for: r3v2, types: [boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <T> T createExifInterfaceByDecryptFile(java.lang.String r7, byte[] r8, com.miui.gallery.util.ExifUtil.ExifCreator<T> r9) {
        /*
            r0 = 0
            if (r9 != 0) goto L4
            return r0
        L4:
            java.lang.String r1 = "ExifUtil"
            java.lang.String r2 = "createExifInterfaceByDecryptFile"
            java.lang.String r2 = com.miui.gallery.util.FileHandleRecordHelper.appendInvokerTag(r1, r2)
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r3 = com.miui.gallery.storage.StorageSolutionProvider.get()
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r4 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.QUERY
            androidx.documentfile.provider.DocumentFile r3 = r3.getDocumentFile(r7, r4, r2)
            if (r3 == 0) goto Lab
            boolean r3 = r3.exists()
            if (r3 != 0) goto L20
            goto Lab
        L20:
            java.io.File r3 = new java.io.File     // Catch: java.lang.Throwable -> L76 java.lang.Exception -> L78
            android.content.Context r4 = com.miui.gallery.util.StaticContext.sGetAndroidContext()     // Catch: java.lang.Throwable -> L76 java.lang.Exception -> L78
            java.io.File r4 = r4.getFilesDir()     // Catch: java.lang.Throwable -> L76 java.lang.Exception -> L78
            long r5 = android.os.SystemClock.elapsedRealtimeNanos()     // Catch: java.lang.Throwable -> L76 java.lang.Exception -> L78
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch: java.lang.Throwable -> L76 java.lang.Exception -> L78
            r3.<init>(r4, r5)     // Catch: java.lang.Throwable -> L76 java.lang.Exception -> L78
            java.lang.String r4 = r3.getAbsolutePath()     // Catch: java.lang.Exception -> L74 java.lang.Throwable -> L93
            boolean r8 = com.miui.gallery.util.CryptoUtil.decryptFile(r7, r4, r8)     // Catch: java.lang.Exception -> L74 java.lang.Throwable -> L93
            if (r8 == 0) goto L60
            java.lang.String r8 = "create exif by decrypt file"
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r8)     // Catch: java.lang.Exception -> L74 java.lang.Throwable -> L93
            java.lang.String r8 = r3.getAbsolutePath()     // Catch: java.lang.Exception -> L74 java.lang.Throwable -> L93
            java.lang.Object r7 = r9.mo1692create(r8)     // Catch: java.lang.Exception -> L74 java.lang.Throwable -> L93
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r8 = com.miui.gallery.storage.StorageSolutionProvider.get()
            java.lang.String r9 = r3.getAbsolutePath()
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r0 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.DELETE
            androidx.documentfile.provider.DocumentFile r8 = r8.getDocumentFile(r9, r0, r2)
            if (r8 == 0) goto L5f
            r8.delete()
        L5f:
            return r7
        L60:
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r7 = com.miui.gallery.storage.StorageSolutionProvider.get()
            java.lang.String r8 = r3.getAbsolutePath()
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r9 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.DELETE
            androidx.documentfile.provider.DocumentFile r7 = r7.getDocumentFile(r8, r9, r2)
            if (r7 == 0) goto L92
        L70:
            r7.delete()
            goto L92
        L74:
            r8 = move-exception
            goto L7a
        L76:
            r7 = move-exception
            goto L95
        L78:
            r8 = move-exception
            r3 = r0
        L7a:
            java.lang.String r9 = "Can't read EXIF tags from file [%s] %s"
            com.miui.gallery.util.logger.DefaultLogger.w(r1, r9, r7, r8)     // Catch: java.lang.Throwable -> L93
            if (r3 == 0) goto L92
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r7 = com.miui.gallery.storage.StorageSolutionProvider.get()
            java.lang.String r8 = r3.getAbsolutePath()
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r9 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.DELETE
            androidx.documentfile.provider.DocumentFile r7 = r7.getDocumentFile(r8, r9, r2)
            if (r7 == 0) goto L92
            goto L70
        L92:
            return r0
        L93:
            r7 = move-exception
            r0 = r3
        L95:
            if (r0 == 0) goto Laa
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r8 = com.miui.gallery.storage.StorageSolutionProvider.get()
            java.lang.String r9 = r0.getAbsolutePath()
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r0 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.DELETE
            androidx.documentfile.provider.DocumentFile r8 = r8.getDocumentFile(r9, r0, r2)
            if (r8 == 0) goto Laa
            r8.delete()
        Laa:
            throw r7
        Lab:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.ExifUtil.createExifInterfaceByDecryptFile(java.lang.String, byte[], com.miui.gallery.util.ExifUtil$ExifCreator):java.lang.Object");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    public static ExifInfo parseRotationInfo(int i) {
        int i2;
        int i3 = 0;
        boolean z = 1;
        switch (i) {
            case 1:
            default:
                z = 0;
                break;
            case 2:
                break;
            case 3:
                i2 = nexClip.kClip_Rotate_180;
                int i4 = i2;
                z = i3;
                i3 = i4;
                break;
            case 4:
                i3 = 1;
                i2 = nexClip.kClip_Rotate_180;
                int i42 = i2;
                z = i3;
                i3 = i42;
                break;
            case 5:
                i3 = 1;
                i2 = nexClip.kClip_Rotate_270;
                int i422 = i2;
                z = i3;
                i3 = i422;
                break;
            case 6:
                i2 = 90;
                int i4222 = i2;
                z = i3;
                i3 = i4222;
                break;
            case 7:
                i3 = 1;
                i2 = 90;
                int i42222 = i2;
                z = i3;
                i3 = i42222;
                break;
            case 8:
                i2 = nexClip.kClip_Rotate_270;
                int i422222 = i2;
                z = i3;
                i3 = i422222;
                break;
        }
        return new ExifInfo(i, i3, z);
    }

    /* loaded from: classes2.dex */
    public static class ExifInfo {
        public final int exifOrientation;
        public final boolean flip;
        public final int rotation;

        public ExifInfo(int i, int i2, boolean z) {
            this.exifOrientation = i;
            this.rotation = i2;
            this.flip = z;
        }
    }

    /* loaded from: classes2.dex */
    public static final class UserCommentData {
        public String mExt;
        public String mSha1;

        public UserCommentData(String str, String str2) {
            this.mSha1 = str;
            this.mExt = str2;
        }

        public String getSha1() {
            return this.mSha1;
        }

        public String getExt() {
            return this.mExt;
        }

        public String getFileName(String str) {
            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(getExt())) {
                return null;
            }
            return str + "." + getExt();
        }
    }
}
