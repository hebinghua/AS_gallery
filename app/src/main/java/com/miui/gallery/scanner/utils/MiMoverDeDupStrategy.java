package com.miui.gallery.scanner.utils;

import android.content.Context;
import com.miui.gallery.backup.GalleryBackupProtos;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.model.SaveToCloud;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public class MiMoverDeDupStrategy extends DefaultDeDupStrategy {
    @Override // com.miui.gallery.scanner.utils.DefaultDeDupStrategy, com.miui.gallery.scanner.utils.AbsDeDupStrategy
    public ScanResult doDeDup(Context context, SaveToCloud saveToCloud, SaveParams saveParams) {
        Map map = (Map) ScanCache.getInstance().get("key_mi_mover_cloud_profiles");
        if (!BaseMiscUtil.isValid(map)) {
            return super.doDeDup(context, saveToCloud, saveParams);
        }
        GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile = (GalleryBackupProtos.BackupMessage.CloudProfile) map.get(saveToCloud.mPath.toLowerCase());
        if (cloudProfile == null) {
            return super.doDeDup(context, saveToCloud, saveParams);
        }
        Set set = (Set) ScanCache.getInstance().get("key_mi_mover_cloud_sha1_cache");
        Set set2 = (Set) ScanCache.getInstance().get("key_mi_mover_cloud_path_cache");
        if (set == null || set2 == null) {
            return super.doDeDup(context, saveToCloud, saveParams);
        }
        if (!set.contains(cloudProfile.getSha1()) && !set2.contains(cloudProfile.getPath())) {
            return null;
        }
        return super.doDeDup(context, saveToCloud, saveParams);
    }
}
