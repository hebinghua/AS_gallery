package com.miui.gallery.pendingtask;

import com.miui.gallery.assistant.library.DeleteLibraryTask;
import com.miui.gallery.assistant.library.LibraryDownloadTask;
import com.miui.gallery.assistant.process.ExistAnalyticFaceAndSceneTask;
import com.miui.gallery.assistant.process.ExistImageFeatureChargingTask;
import com.miui.gallery.assistant.process.ExistImageFeatureTask;
import com.miui.gallery.card.preprocess.ScenarioAlbumChargingTask;
import com.miui.gallery.card.preprocess.ScenarioAlbumTask;
import com.miui.gallery.cloudcontrol.CloudControlPendingTask;
import com.miui.gallery.pendingtask.base.PendingTask;

/* loaded from: classes2.dex */
public class PendingTaskFactory {
    public static PendingTask create(int i) {
        if (i != 2) {
            if (i == 3) {
                return new CloudControlPendingTask(i);
            }
            switch (i) {
                case 6:
                    return new ExistImageFeatureTask(i);
                case 7:
                    return new LibraryDownloadTask(i);
                case 8:
                    return new DeleteLibraryTask(i);
                case 9:
                    return new ExistImageFeatureChargingTask(i);
                case 10:
                    return new ScenarioAlbumChargingTask(i);
                case 11:
                    return new ExistAnalyticFaceAndSceneTask(i);
                case 12:
                    return new ExistImageFeatureChargingTask(i);
                default:
                    return null;
            }
        }
        return new ScenarioAlbumTask(i);
    }
}
