package com.miui.gallery.model;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.exifinterface.media.ExifInterface;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.VideoAttrsReader;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import miuix.pickerwidget.date.DateUtils;

/* loaded from: classes2.dex */
public class PhotoDetailInfo implements Iterable<Map.Entry<Integer, Object>> {
    public TreeMap<Integer, Object> mDetails = new TreeMap<>();

    /* loaded from: classes2.dex */
    public static class FlashState {
        public static int FLASH_FIRED_MASK = 1;
        public int mState;

        public FlashState(int i) {
            this.mState = i;
        }

        public boolean isFlashFired() {
            return (this.mState & FLASH_FIRED_MASK) != 0;
        }
    }

    public void addDetail(int i, Object obj) {
        this.mDetails.put(Integer.valueOf(i), obj);
    }

    public void removeDetail(int i) {
        this.mDetails.remove(Integer.valueOf(i));
    }

    public Object getDetail(int i) {
        return this.mDetails.get(Integer.valueOf(i));
    }

    public boolean isSmartFusion() {
        if (this.mDetails == null) {
            return false;
        }
        Object detail = getDetail(202);
        if (!(detail instanceof Boolean)) {
            return false;
        }
        return ((Boolean) detail).booleanValue();
    }

    @Override // java.lang.Iterable
    public Iterator<Map.Entry<Integer, Object>> iterator() {
        return this.mDetails.entrySet().iterator();
    }

    public static void extractExifInfo(PhotoDetailInfo photoDetailInfo, String str, boolean z) {
        if (photoDetailInfo == null || TextUtils.isEmpty(str)) {
            return;
        }
        try {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile("camera_first", str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("PhotoDetailInfo", "extractExifInfo"));
            if (documentFile != null && documentFile.exists()) {
                ExifInterface exifInterface = new ExifInterface(StorageSolutionProvider.get().openInputStream(documentFile));
                if (z) {
                    int attributeInt = exifInterface.getAttributeInt("ImageWidth", 0);
                    int attributeInt2 = exifInterface.getAttributeInt("ImageLength", 0);
                    if (attributeInt <= 0 || attributeInt2 <= 0) {
                        BitmapFactory.Options bitmapSize = miuix.graphics.BitmapFactory.getBitmapSize(str);
                        int i = bitmapSize.outWidth;
                        attributeInt2 = bitmapSize.outHeight;
                        attributeInt = i;
                    }
                    photoDetailInfo.addDetail(4, Integer.valueOf(attributeInt));
                    photoDetailInfo.addDetail(5, Integer.valueOf(attributeInt2));
                    long gpsDateTime = ExifUtil.getGpsDateTime(exifInterface);
                    if (gpsDateTime <= 0) {
                        gpsDateTime = new File(str).lastModified();
                    }
                    photoDetailInfo.addDetail(1, Long.valueOf(gpsDateTime));
                    double[] latLong = exifInterface.getLatLong();
                    if (latLong != null) {
                        photoDetailInfo.addDetail(6, new double[]{latLong[0], latLong[1]});
                    }
                }
                String attribute = exifInterface.getAttribute("XiaomiProduct");
                if (TextUtils.isEmpty(attribute)) {
                    attribute = exifInterface.getAttribute("Model");
                }
                photoDetailInfo.addDetail(101, attribute);
                photoDetailInfo.addDetail(100, exifInterface.getAttribute("Make"));
                photoDetailInfo.addDetail(105, exifInterface.getAttribute("FNumber"));
                photoDetailInfo.addDetail(103, wrapFocalLength(exifInterface.getAttribute("FocalLength")));
                photoDetailInfo.addDetail(107, exifInterface.getAttribute("ExposureTime"));
                photoDetailInfo.addDetail(108, exifInterface.getAttribute("ISOSpeedRatings"));
                photoDetailInfo.addDetail(102, exifInterface.getAttribute("Flash"));
                photoDetailInfo.addDetail(10, exifInterface.getAttribute("Orientation"));
                photoDetailInfo.addDetail(202, Boolean.valueOf("1".equals(exifInterface.getAttribute("SmartFusion"))));
                return;
            }
            DefaultLogger.d("PhotoDetailInfo", "documentFile is null.");
        } catch (Exception e) {
            DefaultLogger.w("PhotoDetailInfo", e);
        }
    }

    public static void extractSmartFusionExifInfo(PhotoDetailInfo photoDetailInfo, String str) {
        if (photoDetailInfo == null || TextUtils.isEmpty(str)) {
            return;
        }
        try {
            photoDetailInfo.addDetail(202, Boolean.valueOf("1".equals(new ExifInterface(str).getAttribute("SmartFusion"))));
        } catch (IOException e) {
            DefaultLogger.w("PhotoDetailInfo", e);
        }
    }

    public static void extractVideoAttr(PhotoDetailInfo photoDetailInfo, String str) {
        if (photoDetailInfo == null || TextUtils.isEmpty(str)) {
            return;
        }
        try {
            VideoAttrsReader read = VideoAttrsReader.read(str);
            long dateTaken = read.getDateTaken();
            if (dateTaken > 0) {
                photoDetailInfo.addDetail(1, Long.valueOf(dateTaken));
            }
            long duration = read.getDuration();
            if (duration > 0) {
                photoDetailInfo.addDetail(7, Integer.valueOf((int) (duration / 1000)));
            }
            if (!LocationUtil.isValidateCoordinate(read.getLatitude(), read.getLongitude())) {
                return;
            }
            photoDetailInfo.addDetail(6, read.getLatLong());
        } catch (Exception e) {
            DefaultLogger.w("PhotoDetailInfo", e);
        }
    }

    public static Double wrapFocalLength(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            int indexOf = str.indexOf(h.g);
            if (indexOf == -1) {
                return null;
            }
            double parseDouble = Double.parseDouble(str.substring(indexOf + 1));
            if (parseDouble != SearchStatUtils.POW) {
                return Double.valueOf(Double.parseDouble(str.substring(0, indexOf)) / parseDouble);
            }
            return null;
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    public boolean isHaveTimeData() {
        return getDetail(1) != null;
    }

    public boolean isHaveFileNameInfo() {
        return getDetail(2) != null;
    }

    public boolean isHaveFileSizeInfo() {
        return getDetail(3) != null;
    }

    public boolean isHaveFileWidthAndHeight() {
        Object detail = getDetail(4);
        Object detail2 = getDetail(5);
        return (detail == null || -1 == ((Integer) detail).intValue() || detail2 == null || -1 == ((Integer) detail2).intValue()) ? false : true;
    }

    public boolean isHaveDuration() {
        return getDetail(7) != null;
    }

    public boolean isHaveAperture() {
        return getDetail(105) != null;
    }

    public boolean isHaveExposureTimeInfo() {
        return getDetail(107) != null;
    }

    public boolean isHaveISOInfo() {
        return getDetail(108) != null;
    }

    public boolean isHaveFocalLengthInfo() {
        return getDetail(103) != null;
    }

    public boolean isHaveFlashInfo() {
        return getDetail(102) != null;
    }

    public boolean isHaveFileLocalPathDataInfo() {
        return (getDetail(200) == null && getDetail(201) == null) ? false : true;
    }

    public boolean isHaveLocationInfo() {
        return getDetail(6) != null;
    }

    public Long getTakenTime() {
        if (!isHaveTimeData()) {
            return null;
        }
        return (Long) getDetail(1);
    }

    public String getPath() {
        return (String) getDetail(200);
    }

    public String getTakenTime(int i) {
        if (!isHaveTimeData()) {
            return null;
        }
        return DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), ((Long) getDetail(1)).longValue(), i);
    }

    public String getFileName() {
        if (!isHaveFileNameInfo()) {
            return null;
        }
        return (String) getDetail(2);
    }

    public Long getFileSize() {
        if (!isHaveFileSizeInfo()) {
            return null;
        }
        return (Long) getDetail(3);
    }

    public Integer[] getFileWidthAndHeight() {
        if (!isHaveFileWidthAndHeight()) {
            return null;
        }
        return new Integer[]{(Integer) getDetail(4), (Integer) getDetail(5)};
    }

    public Integer getFileDuataion() {
        if (!isHaveDuration()) {
            return null;
        }
        return (Integer) getDetail(7);
    }

    public String getAperture() {
        if (!isHaveAperture()) {
            return null;
        }
        return (String) getDetail(105);
    }

    public String getExposureTime() {
        if (!isHaveExposureTimeInfo()) {
            return null;
        }
        return (String) getDetail(107);
    }

    public String getISO() {
        if (!isHaveISOInfo()) {
            return null;
        }
        return "ISO" + ((String) getDetail(108));
    }

    public Double getFocalLength() {
        if (!isHaveFocalLengthInfo()) {
            return null;
        }
        return (Double) getDetail(103);
    }

    public String getFlashStatus() {
        if (!isHaveFlashInfo()) {
            return null;
        }
        return (String) getDetail(102);
    }

    public String getFileLocalPath() {
        if (!isHaveFileLocalPathDataInfo()) {
            return null;
        }
        Object detail = getDetail(201);
        return detail == null ? (String) getDetail(201) : (String) detail;
    }

    public double[] getLocation() {
        double[] dArr;
        if (isHaveLocationInfo() && (dArr = (double[]) getDetail(6)) != null && LocationUtil.isValidateCoordinate(dArr[0], dArr[1])) {
            return dArr;
        }
        return null;
    }

    public boolean isHaveOrientation() {
        return getDetail(10) != null;
    }

    public Integer getOrientation() {
        if (!isHaveOrientation()) {
            return null;
        }
        return Integer.valueOf(Integer.parseInt((String) getDetail(10)));
    }

    public boolean isHaveModel() {
        return getDetail(101) != null;
    }

    public boolean isHaveMake() {
        return getDetail(100) != null;
    }

    public String getModel() {
        if (!isHaveModel()) {
            return null;
        }
        return (String) getDetail(101);
    }

    public String getMake() {
        if (!isHaveMake()) {
            return null;
        }
        return (String) getDetail(100);
    }

    public boolean isFilePath() {
        if (getDetail(109) == null) {
            return false;
        }
        return ((Boolean) getDetail(109)).booleanValue();
    }

    public boolean isHaveCacheLocation() {
        return getDetail(9) != null;
    }

    public String getCacheLocation() {
        if (!isHaveCacheLocation()) {
            return null;
        }
        return (String) getDetail(9);
    }

    public String toString() {
        return "PhotoDetailInfo{mDetails=" + this.mDetails + '}';
    }
}
