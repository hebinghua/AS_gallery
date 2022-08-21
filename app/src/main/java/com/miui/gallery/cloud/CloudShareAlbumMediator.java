package com.miui.gallery.cloud;

import com.google.common.collect.Lists;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CloudShareAlbumMediator {
    public static final CloudShareAlbumMediator sSingleton = new CloudShareAlbumMediator();
    public final ArrayList<OnShareAlbumExitedListener> mListeners = Lists.newArrayList();

    /* loaded from: classes.dex */
    public interface OnShareAlbumExitedListener {
        void onExit(String str);
    }

    public static CloudShareAlbumMediator getInstance() {
        return sSingleton;
    }

    public void onShareAlbumExited(String str) {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            this.mListeners.get(size).onExit(str);
        }
    }
}
