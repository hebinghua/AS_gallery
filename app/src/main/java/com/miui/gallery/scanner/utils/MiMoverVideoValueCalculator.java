package com.miui.gallery.scanner.utils;

import com.miui.gallery.backup.GalleryBackupProtos;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class MiMoverVideoValueCalculator extends DefaultVideoValueCalculator {
    public GalleryBackupProtos.BackupMessage.CloudProfile mProfile;

    public MiMoverVideoValueCalculator(String str) {
        super(str);
    }

    @Override // com.miui.gallery.scanner.utils.DefaultVideoValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public String calcSha1() {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverVideoValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcSha1();
        }
        return cloudProfile.getSha1();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultVideoValueCalculator, com.miui.gallery.scanner.utils.AbsVideoValueCalculator
    public long calcDuration() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverVideoValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcDuration();
        }
        return cloudProfile.getDuration();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultVideoValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifImageWidth() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverVideoValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifImageWidth();
        }
        return cloudProfile.getWidth();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultVideoValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifImageHeight() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverVideoValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifImageHeight();
        }
        return cloudProfile.getHeight();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultVideoValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifOrientation() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverVideoValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifOrientation();
        }
        return cloudProfile.getOrientation();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultVideoValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public long calcDateTaken(long j, long j2, boolean z) throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverVideoValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcDateTaken(j, j2, z);
        }
        return cloudProfile.getDateTaken();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultVideoValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public long calcSpecialTypeFlags() {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverVideoValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcSpecialTypeFlags();
        }
        return cloudProfile.getSpecialTypeFlags();
    }

    @Override // com.miui.gallery.scanner.utils.DefaultVideoValueCalculator, com.miui.gallery.scanner.utils.AbsValueCalculator
    public Map<String, String> calcExifGpsLocation() throws IOException {
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = getCloudProfile(this.path);
        if (cloudProfile == null) {
            DefaultLogger.w("MiMoverVideoValueCalculator", "cloud profile [%s] not cached or unexpected file received.", this.path);
            return super.calcExifGpsLocation();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("GPSLatitude", cloudProfile.getLatitude());
        hashMap.put("GPSLongitude", cloudProfile.getLongitude());
        hashMap.put("GPSLatitudeRef", cloudProfile.getLatitudeRef());
        hashMap.put("GPSLongitudeRef", cloudProfile.getLongitudeRef());
        return hashMap;
    }

    public final GalleryBackupProtos.BackupMessage.CloudProfile getCloudProfile(String str) {
        if (this.mProfile == null) {
            Map map = (Map) ScanCache.getInstance().get("key_mi_mover_cloud_profiles");
            this.mProfile = BaseMiscUtil.isValid(map) ? (GalleryBackupProtos.BackupMessage.CloudProfile) map.get(str.toLowerCase()) : null;
        }
        return this.mProfile;
    }
}
