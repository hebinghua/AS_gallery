package com.miui.gallery.scanner.utils;

import com.miui.gallery.backup.GalleryBackupProtos;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class MiMoverImageValueCalculator extends DefaultImageValueCalculator {
    public GalleryBackupProtos.BackupMessage.CloudProfile mProfile;

    public MiMoverImageValueCalculator(String str) {
        super(str);
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public String calcSha1() {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcSha1();
        }
        return cloudProfile.getSha1();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifModel() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifModel();
        }
        return cloudProfile.getExifModel();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifImageWidth() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifImageWidth();
        }
        return cloudProfile.getWidth();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifImageHeight() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifImageHeight();
        }
        return cloudProfile.getHeight();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifOrientation() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifOrientation();
        }
        return cloudProfile.getOrientation();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifMake() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifMake();
        }
        return cloudProfile.getExifMake();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public Integer calcExifFlash() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifFlash();
        }
        return Integer.valueOf(cloudProfile.getExifFlash());
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifExposureTime() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifExposureTime();
        }
        return cloudProfile.getExifExposureTime();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifAperture() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifAperture();
        }
        return cloudProfile.getExifAperture();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifISO() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifISO();
        }
        return cloudProfile.getExifISO();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public int calcExifWhiteBalance() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifWhiteBalance();
        }
        return cloudProfile.getExifWhiteBalance();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifGpsAltitude() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifGpsAltitude();
        }
        return cloudProfile.getExifGpsAltitude();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public int calcExifGpsAltitudeRef() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifGpsAltitudeRef();
        }
        return cloudProfile.getExifGpsAltitudeRef();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifFocalLength() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifFocalLength();
        }
        return cloudProfile.getExifFocalLength();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifGpsProcessingMethod() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifGpsProcessingMethod();
        }
        return cloudProfile.getExifGpsProcessingMethod();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public Map<String, String> calcExifGpsLocation() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifGpsLocation();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("GPSLatitude", cloudProfile.getLatitude());
        hashMap.put("GPSLongitude", cloudProfile.getLongitude());
        hashMap.put("GPSLatitudeRef", cloudProfile.getLatitudeRef());
        hashMap.put("GPSLongitudeRef", cloudProfile.getLongitudeRef());
        return hashMap;
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifGpsTimeStamp(long j, boolean z) throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifGpsTimeStamp(j, z);
        }
        return cloudProfile.getExifGpsTimeStamp();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifGpsDateStamp(long j, boolean z) throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifGpsDateStamp(j, z);
        }
        return cloudProfile.getExifGpsDateStamp();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcExifDateTime(long j, boolean z) throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifDateTime(j, z);
        }
        return cloudProfile.getExifDateTime();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsImageValueCalculator
    public String calcScreenshotsLocation() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcScreenshotsLocation();
        }
        return cloudProfile.getLocation();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public long calcDateTaken(long j, long j2, boolean z) throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcDateTaken(j, j2, z);
        }
        return cloudProfile.getDateTaken();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultImageValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public long calcSpecialTypeFlags() {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverImageValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcSpecialTypeFlags();
        }
        return cloudProfile.getSpecialTypeFlags();
    }

    public final GalleryBackupProtos.BackupMessage.CloudProfile getCloudProfile(String str) {
        if (this.mProfile == null) {
            Map map = (Map) ScanCache.getInstance().get("key_mi_mover_cloud_profiles");
            this.mProfile = BaseMiscUtil.isValid(map) ? (GalleryBackupProtos.BackupMessage.CloudProfile) map.get(str.toLowerCase()) : null;
        }
        return this.mProfile;
    }
}
