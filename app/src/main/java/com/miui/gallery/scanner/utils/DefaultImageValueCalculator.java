package com.miui.gallery.scanner.utils;

import android.text.TextUtils;
import androidx.exifinterface.media.ExifInterface;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import miuix.graphics.BitmapFactory;

/* loaded from: classes2.dex */
public class DefaultImageValueCalculator extends AbsImageValueCalculator {
    public ExifInterface exifInterface;
    public android.media.ExifInterface exifInterface1;

    public DefaultImageValueCalculator(String str) {
        super(str);
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public String calcSha1() {
        return FileUtils.getSha1(this.path);
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifModel() throws IOException {
        String attribute = getExifInterface().getAttribute("XiaomiProduct");
        return !TextUtils.isEmpty(attribute) ? attribute : getExifInterface().getAttribute("Model");
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifImageWidth() throws IOException {
        int attributeInt = getExifInterface().getAttributeInt("ImageWidth", 0);
        return attributeInt > 0 ? attributeInt : BitmapFactory.getBitmapSize(this.path).outWidth;
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifImageHeight() throws IOException {
        int attributeInt = getExifInterface().getAttributeInt("ImageLength", 0);
        return attributeInt > 0 ? attributeInt : BitmapFactory.getBitmapSize(this.path).outHeight;
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifOrientation() throws IOException {
        return getExifInterface().getAttributeInt("Orientation", 0);
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifMake() throws IOException {
        return getExifInterface().getAttribute("Make");
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public Integer calcExifFlash() throws IOException {
        String attribute = getExifInterface().getAttribute("Flash");
        if (attribute == null) {
            return null;
        }
        try {
            return Integer.valueOf(Integer.parseInt(attribute));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifExposureTime() throws IOException {
        return getExifInterface().getAttribute("ExposureTime");
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifAperture() throws IOException {
        return getExifInterface().getAttribute("FNumber");
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifISO() throws IOException {
        return getExifInterface().getAttribute("PhotographicSensitivity");
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public int calcExifWhiteBalance() throws IOException {
        String attribute = getExifInterface().getAttribute("WhiteBalance");
        if (attribute == null) {
            return 0;
        }
        try {
            return Integer.parseInt(attribute);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifGpsAltitude() throws IOException {
        return getExifInterface().getAttribute("GPSAltitude");
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public int calcExifGpsAltitudeRef() throws IOException {
        String attribute = getExifInterface().getAttribute("GPSAltitudeRef");
        if (attribute == null) {
            return 0;
        }
        try {
            return Integer.parseInt(attribute);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifFocalLength() throws IOException {
        return getExifInterface().getAttribute("FocalLength");
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifGpsProcessingMethod() throws IOException {
        return getExifInterface().getAttribute("GPSProcessingMethod");
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public Map<String, String> calcExifGpsLocation() throws IOException {
        String attribute = getExifInterface().getAttribute("GPSLatitude");
        String attribute2 = getExifInterface().getAttribute("GPSLongitude");
        String attribute3 = getExifInterface().getAttribute("GPSLatitudeRef");
        String attribute4 = getExifInterface().getAttribute("GPSLongitudeRef");
        if (attribute == null || attribute2 == null || attribute3 == null || attribute4 == null) {
            if (attribute == null || attribute2 == null) {
                attribute = getExifInterface1().getAttribute("GPSLatitude");
                attribute2 = getExifInterface1().getAttribute("GPSLongitude");
            }
            if (attribute3 == null || attribute4 == null) {
                attribute3 = getExifInterface1().getAttribute("GPSLatitudeRef");
                attribute4 = getExifInterface1().getAttribute("GPSLongitudeRef");
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("GPSLatitude", attribute);
        hashMap.put("GPSLongitude", attribute2);
        hashMap.put("GPSLatitudeRef", attribute3);
        hashMap.put("GPSLongitudeRef", attribute4);
        return hashMap;
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifGpsTimeStamp(long j, boolean z) throws IOException {
        String attribute = getExifInterface().getAttribute("GPSTimeStamp");
        if (!TextUtils.isEmpty(attribute)) {
            return attribute;
        }
        if (j < 0 && z) {
            return getExifInterface1().getAttribute("GPSTimeStamp");
        }
        return null;
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifGpsDateStamp(long j, boolean z) throws IOException {
        String attribute = getExifInterface().getAttribute("GPSDateStamp");
        if (!TextUtils.isEmpty(attribute)) {
            return attribute;
        }
        if (j < 0 && z) {
            return getExifInterface1().getAttribute("GPSDateStamp");
        }
        return null;
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifDateTime(long j, boolean z) throws IOException {
        String attribute = getExifInterface().getAttribute("DateTime");
        if (!TextUtils.isEmpty(attribute)) {
            return attribute;
        }
        if (j < 0 && z) {
            return getExifInterface1().getAttribute("DateTime");
        }
        return null;
    }

    @Override // com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcScreenshotsLocation() throws IOException {
        String gePackageNameForScreenshot = PackageUtils.gePackageNameForScreenshot(BaseFileUtils.getFileName(this.path));
        if (gePackageNameForScreenshot == null) {
            return null;
        }
        return PackageUtils.getAppNameByPackage(gePackageNameForScreenshot);
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public long calcDateTaken(long j, long j2, boolean z) throws IOException {
        int i = (j2 > 0L ? 1 : (j2 == 0L ? 0 : -1));
        if (i <= 0) {
            j2 = getDateTaken(this.exifInterface, j);
        }
        return j2 != j ? j2 : (i >= 0 || !z) ? j : getDateTaken(getExifInterface1(), j);
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public long calcSpecialTypeFlags() {
        return SpecialTypeMediaUtils.parseFlagsForImage(this.path);
    }

    public static long getDateTaken(ExifInterface exifInterface, long j) {
        long dateTime = ExifUtil.getDateTime(exifInterface, true);
        if (dateTime == -1) {
            dateTime = ExifUtil.getGpsDateTime(exifInterface);
        }
        return dateTime < 0 ? j : dateTime;
    }

    public static long getDateTaken(android.media.ExifInterface exifInterface, long j) {
        long dateTime = ExifUtil.getDateTime(exifInterface, true);
        if (dateTime == -1) {
            dateTime = ExifUtil.getGpsDateTime(exifInterface);
        }
        return dateTime < 0 ? j : dateTime;
    }

    public final ExifInterface getExifInterface() throws IOException {
        if (this.exifInterface == null) {
            this.exifInterface = new ExifInterface(this.path);
        }
        return this.exifInterface;
    }

    public final android.media.ExifInterface getExifInterface1() throws IOException {
        if (this.exifInterface1 == null) {
            this.exifInterface1 = new android.media.ExifInterface(this.path);
        }
        return this.exifInterface1;
    }
}
