package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;
import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import java.util.List;

/* loaded from: classes.dex */
public class AlbumTabToolsStrategy extends BaseStrategy {
    @SerializedName("albums")
    private List<AlbumTabToolItemBean> mTools;

    public AlbumTabToolItemBean getToolById(long j) {
        List<AlbumTabToolItemBean> list = this.mTools;
        if (list == null || list.size() <= 0) {
            return null;
        }
        for (AlbumTabToolItemBean albumTabToolItemBean : this.mTools) {
            if (albumTabToolItemBean.getId() == j) {
                return new AlbumTabToolItemBean(albumTabToolItemBean.getId()).setDefaultTitle(albumTabToolItemBean.getDefaultTitle()).setTitleRes(albumTabToolItemBean.getTitleRes()).setIcon(albumTabToolItemBean.getIcon()).setSort(albumTabToolItemBean.getSort()).setGotoLink(albumTabToolItemBean.getGotoLink()).setEventTip(albumTabToolItemBean.getEventTip()).setSubTitle(albumTabToolItemBean.getSubTitle());
            }
        }
        return null;
    }
}
