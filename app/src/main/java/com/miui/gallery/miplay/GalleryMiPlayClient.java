package com.miui.gallery.miplay;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.miplay.phoneclientsdk.external.MiPlayClientManage;
import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;

/* loaded from: classes2.dex */
public class GalleryMiPlayClient extends MiPlayClientManage {
    public static GalleryMiPlayClient mMiPlayClient;

    public GalleryMiPlayClient(Context context) {
        super(context);
    }

    public static GalleryMiPlayClient getInstance() {
        if (mMiPlayClient == null) {
            mMiPlayClient = new GalleryMiPlayClient(GalleryApp.sGetAndroidContext());
        }
        return mMiPlayClient;
    }

    public int play(MediaMetaData mediaMetaData) {
        try {
            return play("com.miui.gallery", mediaMetaData);
        } catch (Exception e) {
            DefaultLogger.e("GalleryMiPlayManager", e);
            return -1;
        }
    }

    public int stop() {
        try {
            return stop("com.miui.gallery");
        } catch (Exception e) {
            DefaultLogger.e("GalleryMiPlayManager", e);
            return -1;
        }
    }

    public void setVolume(int i) {
        try {
            setVolume("com.miui.gallery", i);
        } catch (Exception e) {
            DefaultLogger.e("GalleryMiPlayManager", e);
        }
    }

    public int getVolume() {
        try {
            return getVolume("com.miui.gallery");
        } catch (Exception e) {
            DefaultLogger.e("GalleryMiPlayManager", e);
            return -1;
        }
    }

    public int resume() {
        try {
            return resume("com.miui.gallery");
        } catch (Exception e) {
            DefaultLogger.e("GalleryMiPlayManager", e);
            return -1;
        }
    }

    public int pause() {
        try {
            return pause("com.miui.gallery");
        } catch (Exception e) {
            DefaultLogger.e("GalleryMiPlayManager", e);
            return -1;
        }
    }

    public int seek(long j) {
        try {
            return seek("com.miui.gallery", j);
        } catch (Exception e) {
            DefaultLogger.e("GalleryMiPlayManager", e);
            return -1;
        }
    }

    public int cancelCirculate() {
        try {
            return cancelCirculate("com.miui.gallery", 0);
        } catch (Exception e) {
            DefaultLogger.e("GalleryMiPlayManager", e);
            return -1;
        }
    }
}
