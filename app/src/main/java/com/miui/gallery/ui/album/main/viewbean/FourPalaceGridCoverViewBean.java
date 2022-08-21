package com.miui.gallery.ui.album.main.viewbean;

import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.util.CheckEmptyDataSubscriber;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class FourPalaceGridCoverViewBean<T extends BaseAlbumCover> extends BaseViewBean<CoverList, FourPalaceGridCoverViewBean> implements CheckEmptyDataSubscriber.onCheckEmpty {
    public String albumDescription;
    public String albumName;
    public List<T> covers;

    public List<T> getCovers() {
        return this.covers;
    }

    public void setCovers(List<T> list) {
        this.covers = list;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String str) {
        this.albumName = str;
    }

    public String getAlbumDescription() {
        return this.albumDescription;
    }

    public void setAlbumDescription(String str) {
        this.albumDescription = str;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean, java.util.Comparator
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof FourPalaceGridCoverViewBean)) {
            FourPalaceGridCoverViewBean fourPalaceGridCoverViewBean = (FourPalaceGridCoverViewBean) obj;
            return fourPalaceGridCoverViewBean.getId() == getId() && Objects.equals(fourPalaceGridCoverViewBean.getCovers(), getCovers()) && Objects.equals(fourPalaceGridCoverViewBean.getAlbumDescription(), getAlbumDescription()) && Objects.equals(fourPalaceGridCoverViewBean.getAlbumName(), getAlbumName());
        }
        return false;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public int hashCode() {
        return Objects.hash(Long.valueOf(this.id), this.covers, this.albumName, this.albumDescription);
    }

    public String toString() {
        return "FourPalaceGridCoverViewBean{albumName='" + this.albumName + CoreConstants.SINGLE_QUOTE_CHAR + ", albumDescription='" + this.albumDescription + CoreConstants.SINGLE_QUOTE_CHAR + ", covers=" + this.covers + '}';
    }

    @Override // com.miui.gallery.util.CheckEmptyDataSubscriber.onCheckEmpty
    public boolean isEmpty() {
        List<T> list = this.covers;
        return list == null || list.isEmpty();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(CoverList coverList) {
        if (coverList != null) {
            setCovers(coverList.getCovers());
            setId(coverList.getId());
        }
    }
}
