package com.miui.gallery.util.baby;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class SendNotificationUtilForSharedBabyAlbum {
    public final int SEND_NOTIFICATION_INTERVAL;
    public ArrayList<SendNotificationRunnable> mNotificationRunnableList;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final SendNotificationUtilForSharedBabyAlbum mInstance = new SendNotificationUtilForSharedBabyAlbum();
    }

    public static final SendNotificationUtilForSharedBabyAlbum getInstance() {
        return SingletonHolder.mInstance;
    }

    public SendNotificationUtilForSharedBabyAlbum() {
        this.SEND_NOTIFICATION_INTERVAL = 600000;
        this.mNotificationRunnableList = new ArrayList<>();
    }

    public void sendNotification(String str, long j, String str2, boolean z, long j2) {
        SendNotificationRunnable sendNotificationRunnable;
        int i = 0;
        while (true) {
            if (i >= this.mNotificationRunnableList.size()) {
                sendNotificationRunnable = null;
                break;
            } else if (str.equalsIgnoreCase(this.mNotificationRunnableList.get(i).mAlbumId)) {
                sendNotificationRunnable = this.mNotificationRunnableList.get(i);
                break;
            } else {
                i++;
            }
        }
        if (sendNotificationRunnable == null) {
            sendNotificationRunnable = new SendNotificationRunnable();
            sendNotificationRunnable.setPathAndName(str, z, j, str2);
            this.mNotificationRunnableList.add(sendNotificationRunnable);
        }
        if (GalleryPreferences.Baby.getMinServerTagOfNewPhoto(getUniformAlbumLocalId(j, z)).longValue() == 0) {
            GalleryPreferences.Baby.saveMinServerTagOfNewPhoto(getUniformAlbumLocalId(j, z), Long.valueOf(j2));
        }
        ThreadManager.getMainHandler().postDelayed(sendNotificationRunnable, 600000L);
    }

    public final long getUniformAlbumLocalId(long j, boolean z) {
        return z ? ShareAlbumHelper.getUniformAlbumId(j) : j;
    }

    /* loaded from: classes2.dex */
    public static class SendNotificationRunnable implements Runnable {
        public String mAlbumId;
        public long mAlbumLocalId;
        public boolean mIsOtherShared;
        public String mName;

        public void setPathAndName(String str, boolean z, long j, String str2) {
            this.mAlbumId = str;
            this.mIsOtherShared = z;
            this.mAlbumLocalId = j;
            this.mName = str2;
        }

        @Override // java.lang.Runnable
        public void run() {
            CloudUtils.sendBabyAlbumNewPhotoNotification(GalleryApp.sGetAndroidContext(), GalleryApp.sGetAndroidContext().getString(R.string.baby_album_new_photo_remind), NotificationHelper.getBabyAlbumNotificationId(this.mIsOtherShared, (int) this.mAlbumLocalId), null, this.mIsOtherShared ? ShareAlbumHelper.getUniformAlbumId(this.mAlbumLocalId) : this.mAlbumLocalId, this.mName, this.mIsOtherShared);
        }
    }
}
