package com.miui.gallery.vlog.rule;

import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.model.MediaSceneTagManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class Shooting {
    public static List<MediaSceneTagManager.Tag_Version_0> sNearList = new ArrayList();
    public static List<MediaSceneTagManager.Tag_Version_0> sPanoramicList = new ArrayList();
    public static List<MediaSceneTagManager.Tag_Version_0> sCloseupList = new ArrayList();
    public static List<MediaSceneTagManager.Tag_Version_0> sLargePanoramicList = new ArrayList();

    static {
        sNearList.add(MediaSceneTagManager.Tag_Version_0.E_SHOT_TYPE_RENJINZHONGJING);
        sNearList.add(MediaSceneTagManager.Tag_Version_0.E_SHOT_TYPE_WUJINZHONGJING);
        sPanoramicList.add(MediaSceneTagManager.Tag_Version_0.E_SHOT_TYPE_WUQUANJING);
        sPanoramicList.add(MediaSceneTagManager.Tag_Version_0.E_SHOT_TYPE_RENQUANJING);
        sCloseupList.add(MediaSceneTagManager.Tag_Version_0.E_SHOT_TYPE_RENTEXIE);
        sCloseupList.add(MediaSceneTagManager.Tag_Version_0.E_SHOT_TYPE_WUTEXIE);
        sLargePanoramicList.add(MediaSceneTagManager.Tag_Version_0.E_SHOT_TYPE_DAQUANJING);
    }

    public static boolean contains(List<MediaSceneTagManager.Tag_Version_0> list, MediaScene mediaScene) {
        for (MediaSceneTagManager.Tag_Version_0 tag_Version_0 : list) {
            if (tag_Version_0.getTagValue() == mediaScene.getSceneTag()) {
                return true;
            }
        }
        return false;
    }

    public static int getShooting(MediaScene mediaScene) {
        if (mediaScene == null) {
            return -1;
        }
        if (contains(sNearList, mediaScene)) {
            return 1;
        }
        if (contains(sPanoramicList, mediaScene)) {
            return 2;
        }
        if (contains(sCloseupList, mediaScene)) {
            return 3;
        }
        return contains(sLargePanoramicList, mediaScene) ? 4 : -1;
    }

    public static boolean isShootingScene(MediaScene mediaScene) {
        return getShooting(mediaScene) != -1;
    }
}
