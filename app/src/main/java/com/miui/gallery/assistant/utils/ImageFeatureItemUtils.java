package com.miui.gallery.assistant.utils;

import android.text.TextUtils;
import com.miui.gallery.assistant.model.ImageFeatureItem;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ImageFeatureItemUtils<E extends ImageFeatureItem> {
    public void bindMediaFeatures(List<E> list) {
        if (BaseMiscUtil.isValid(list)) {
            ArrayList arrayList = new ArrayList(list.size());
            for (E e : list) {
                arrayList.add(Long.valueOf(e.getMediaId()));
            }
            List query = GalleryEntityManager.getInstance().query(MediaFeature.class, String.format("%s IN (%s)", "mediaId", TextUtils.join(",", arrayList)), null, "createTime DESC", null);
            if (!BaseMiscUtil.isValid(query)) {
                return;
            }
            for (E e2 : list) {
                Iterator it = query.iterator();
                while (true) {
                    if (it.hasNext()) {
                        MediaFeature mediaFeature = (MediaFeature) it.next();
                        if (mediaFeature.getMediaId() == e2.getMediaId()) {
                            e2.setMediaFeature(mediaFeature);
                            break;
                        }
                    }
                }
            }
        }
    }
}
