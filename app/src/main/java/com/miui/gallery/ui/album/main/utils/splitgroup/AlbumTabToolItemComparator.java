package com.miui.gallery.ui.album.main.utils.splitgroup;

import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import java.util.Comparator;

/* loaded from: classes2.dex */
public class AlbumTabToolItemComparator implements Comparator<AlbumTabToolItemBean> {
    @Override // java.util.Comparator
    public int compare(AlbumTabToolItemBean albumTabToolItemBean, AlbumTabToolItemBean albumTabToolItemBean2) {
        return Integer.compare(albumTabToolItemBean.getSort(), albumTabToolItemBean2.getSort());
    }
}
