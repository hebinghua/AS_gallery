package com.miui.gallery.sdk.uploadstatus;

import com.google.common.collect.Sets;
import com.miui.gallery.util.deviceprovider.UploadStatusController;
import java.util.Set;

/* loaded from: classes2.dex */
public class UploadStatusProxy {
    public Set<UploadStatusChangedListener> mListeners = Sets.newHashSet();

    /* loaded from: classes2.dex */
    public interface UploadStatusChangedListener {
        void onUploadStatusChanged(UploadStatusItem uploadStatusItem);
    }

    public synchronized void addUploadStatusChangedListener(UploadStatusChangedListener uploadStatusChangedListener) {
        this.mListeners.add(uploadStatusChangedListener);
    }

    public synchronized void removeUploadStatusChangedListener(UploadStatusChangedListener uploadStatusChangedListener) {
        this.mListeners.remove(uploadStatusChangedListener);
    }

    public synchronized void onUploadStatusChanged(UploadStatusItem uploadStatusItem) {
        for (UploadStatusChangedListener uploadStatusChangedListener : this.mListeners) {
            uploadStatusChangedListener.onUploadStatusChanged(uploadStatusItem);
        }
    }

    public UploadStatusItem getUploadStatus(UploadStatusItem uploadStatusItem) {
        return UploadStatusController.getInstance().getUploadStatus(uploadStatusItem);
    }
}
