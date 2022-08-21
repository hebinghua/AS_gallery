package com.miui.gallery.ui.photodetail.viewbean;

import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.model.PhotoDetailInfo;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;
import java.util.Locale;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class PhotoDetailViewBean extends BaseViewBean<PhotoDetailInfo, Void> {
    public String cacheLocation;
    public String dateText;
    public long dateTime;
    public String displayFilePath;
    public String fileName;
    public long fileSize;
    public String fileSizeText;
    public long id;
    public boolean isFile;
    public boolean isSmartFusion;
    public double[] location;
    public String locationText;
    public String notDownLoad;
    public String path;
    public String phoneModel;
    public String takenParam;
    public String timeText;

    public boolean isHaveDateTime() {
        return getDateText() != null;
    }

    public boolean isHaveFileInfo() {
        return !StringUtils.isEmpty(getFileSizeText());
    }

    public boolean isHaveDownLoadTip() {
        return !StringUtils.isEmpty(getNotDownLoad());
    }

    public boolean isHaveLocation() {
        return getLocation() != null;
    }

    public boolean isHaveFilePath() {
        return !StringUtils.isEmpty(getDisplayFilePath());
    }

    public boolean isHaveCacheLocation() {
        return !StringUtils.isEmpty(getCacheLocation());
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public long getId() {
        return this.id;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public void setId(long j) {
        this.id = j;
    }

    public String getDateText() {
        return this.dateText;
    }

    public void setDateText(String str) {
        this.dateText = str;
    }

    public String getTimeText() {
        return this.timeText;
    }

    public void setTimeText(String str) {
        this.timeText = str;
    }

    public long getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(long j) {
        this.dateTime = j;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public void setFileSize(long j) {
        this.fileSize = j;
        if (j > 0) {
            this.fileSizeText = this.fileSizeText.replaceAll("[\\s\\S]+[K|k|M|m][B|b]", FormatUtil.formatFileSize(GalleryApp.sGetAndroidContext(), j));
        }
    }

    public String getFileSizeText() {
        return this.fileSizeText;
    }

    public void setFileSizeText(String str) {
        this.fileSizeText = str;
    }

    public String getPhoneModel() {
        return this.phoneModel;
    }

    public void setPhoneModel(String str) {
        this.phoneModel = str;
    }

    public String getTakenParam() {
        return this.takenParam;
    }

    public void setTakenParam(String str) {
        this.takenParam = str;
    }

    public String getDisplayFilePath() {
        return this.displayFilePath;
    }

    public void setDisplayFilePath(String str) {
        this.displayFilePath = str;
    }

    public String getLocationText() {
        return this.locationText;
    }

    public void setLocation(double[] dArr) {
        if (dArr != null) {
            this.location = dArr;
            this.locationText = genLocationText(dArr);
        }
    }

    public double[] getLocation() {
        return this.location;
    }

    public String getNotDownLoad() {
        return this.notDownLoad;
    }

    public boolean isFile() {
        return this.isFile;
    }

    public void setFile(boolean z) {
        this.isFile = z;
    }

    public void setCacheLocation(String str) {
        this.cacheLocation = str;
    }

    public String getCacheLocation() {
        return this.cacheLocation;
    }

    public void setPath(String str) {
        this.path = str;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(PhotoDetailInfo photoDetailInfo) {
        if (photoDetailInfo == null) {
            return;
        }
        setDateText(genTimeMainTitle(photoDetailInfo));
        setTimeText(genTimeSubTitle(photoDetailInfo));
        setDateTime(photoDetailInfo.getTakenTime().longValue());
        setFileName(photoDetailInfo.getFileName());
        setFileSizeText(genFileSizeInfo(photoDetailInfo));
        setPhoneModel(genTakenMainTitle(photoDetailInfo));
        String genTakenSubTitle = genTakenSubTitle(photoDetailInfo);
        String genTakenThirdTitle = genTakenThirdTitle(photoDetailInfo);
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        if (!StringUtils.isEmpty(genTakenSubTitle)) {
            acquire.append(genTakenSubTitle);
        }
        if (!StringUtils.isEmpty(genTakenThirdTitle)) {
            if (acquire.length() > 0) {
                acquire.append("\n");
            }
            acquire.append(genTakenThirdTitle);
        }
        setTakenParam(acquire.toString());
        Pools.getStringBuilderPool().release(acquire);
        setDisplayFilePath(photoDetailInfo.getFileLocalPath());
        setLocation(photoDetailInfo.getLocation());
        setFile(photoDetailInfo.isFilePath());
        setCacheLocation(photoDetailInfo.getCacheLocation());
        setSmartFusion(photoDetailInfo.isSmartFusion());
        setPath(photoDetailInfo.getPath());
    }

    public final String genTimeMainTitle(PhotoDetailInfo photoDetailInfo) {
        return photoDetailInfo.getTakenTime(896);
    }

    public final String genTimeSubTitle(PhotoDetailInfo photoDetailInfo) {
        if (photoDetailInfo.getTakenTime() == null) {
            return null;
        }
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        acquire.append(photoDetailInfo.getTakenTime(1024));
        acquire.append("    ");
        acquire.append(photoDetailInfo.getTakenTime(44));
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public final String genFileSizeInfo(PhotoDetailInfo photoDetailInfo) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        Long fileSize = photoDetailInfo.getFileSize();
        if (fileSize != null) {
            acquire.append(FormatUtil.formatFileSize(GalleryApp.sGetAndroidContext(), fileSize.longValue()));
            acquire.append("    ");
        }
        Integer[] fileWidthAndHeight = photoDetailInfo.getFileWidthAndHeight();
        Integer orientation = photoDetailInfo.getOrientation();
        if (fileWidthAndHeight != null) {
            acquire.append(genPixels(fileWidthAndHeight[0], fileWidthAndHeight[1], orientation));
            acquire.append("    ");
        }
        Integer fileDuataion = photoDetailInfo.getFileDuataion();
        if (fileDuataion != null) {
            acquire.append(FormatUtil.formatDuration(GalleryApp.sGetAndroidContext(), fileDuataion.intValue()));
        }
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public String genTakenMainTitle(PhotoDetailInfo photoDetailInfo) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        String model = photoDetailInfo.getModel();
        if (model != null) {
            acquire.append(model);
        }
        String make = photoDetailInfo.getMake();
        if (make != null) {
            acquire.append(", ");
            acquire.append(make);
        }
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public String genTakenSubTitle(PhotoDetailInfo photoDetailInfo) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        String aperture = photoDetailInfo.getAperture();
        if (aperture != null) {
            acquire.append(genAperture(aperture));
            acquire.append("    ");
        }
        String exposureTime = photoDetailInfo.getExposureTime();
        if (exposureTime != null) {
            acquire.append(genExposureTime(exposureTime));
            acquire.append("    ");
        }
        String iso = photoDetailInfo.getISO();
        if (iso != null) {
            acquire.append(iso);
        }
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public String genTakenThirdTitle(PhotoDetailInfo photoDetailInfo) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        Double focalLength = photoDetailInfo.getFocalLength();
        if (focalLength != null) {
            acquire.append(genFocalLength(focalLength));
            acquire.append("    ");
        }
        String flashStatus = photoDetailInfo.getFlashStatus();
        if (flashStatus != null) {
            acquire.append(genFlashFired(Integer.parseInt(flashStatus)));
        }
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public final String genLocationText(double[] dArr) {
        if (dArr == null || !LocationUtil.isValidateCoordinate(dArr[0], dArr[1])) {
            return null;
        }
        return dArr[0] + ", " + dArr[1];
    }

    public final String genPixels(Integer num, Integer num2, Integer num3) {
        if (num3 != null) {
            try {
                int exifOrientationToDegrees = ExifUtil.exifOrientationToDegrees(num3.intValue());
                if (exifOrientationToDegrees == 90 || exifOrientationToDegrees == 270) {
                    return num2 + "x" + num + "px";
                }
            } catch (Exception e) {
                DefaultLogger.w("PhotoDetailViewBean", e);
            }
        }
        return num + "x" + num2 + "px";
    }

    public final String genAperture(String str) {
        String replaceAll = str.replaceAll("0+?$", "");
        if (replaceAll.endsWith(".")) {
            replaceAll = replaceAll + "0";
        }
        return "f/" + replaceAll;
    }

    public final String genExposureTime(String str) {
        int i;
        double d;
        try {
            double doubleValue = Double.valueOf(str).doubleValue();
            if (doubleValue < 1.0d) {
                str = String.format(Locale.US, "1/%d", Long.valueOf(Math.round(1.0d / doubleValue)));
            } else {
                str = String.valueOf(i) + "''";
                if (doubleValue - ((int) doubleValue) > 1.0E-4d) {
                    str = str + String.format(Locale.US, " 1/%d", Long.valueOf(Math.round(1.0d / d)));
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return str;
    }

    public final String genFocalLength(Double d) {
        return d + "mm";
    }

    public final String genFlashFired(int i) {
        return GalleryApp.sGetAndroidContext().getResources().getString(new PhotoDetailInfo.FlashState(i).isFlashFired() ? R.string.flash_on : R.string.flash_off);
    }

    public void setSmartFusion(boolean z) {
        this.isSmartFusion = z;
    }

    public boolean isSmartFusion() {
        return this.isSmartFusion;
    }

    public String toString() {
        return "PhotoDetailViewBean{id=" + this.id + ", dateTime=" + this.dateTime + ", dateText='" + this.dateText + CoreConstants.SINGLE_QUOTE_CHAR + ", timeText='" + this.timeText + CoreConstants.SINGLE_QUOTE_CHAR + ", fileSize=" + this.fileSize + ", fileName='" + this.fileName + CoreConstants.SINGLE_QUOTE_CHAR + ", fileSizeText='" + this.fileSizeText + CoreConstants.SINGLE_QUOTE_CHAR + ", notDownLoad='" + this.notDownLoad + CoreConstants.SINGLE_QUOTE_CHAR + ", phoneModel='" + this.phoneModel + CoreConstants.SINGLE_QUOTE_CHAR + ", takenParam='" + this.takenParam + CoreConstants.SINGLE_QUOTE_CHAR + ", displayFilePath='" + this.displayFilePath + CoreConstants.SINGLE_QUOTE_CHAR + ", path='" + this.path + CoreConstants.SINGLE_QUOTE_CHAR + ", isFile=" + this.isFile + ", location=" + Arrays.toString(this.location) + ", locationText='" + this.locationText + CoreConstants.SINGLE_QUOTE_CHAR + ", cacheLocation='" + this.cacheLocation + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }
}
