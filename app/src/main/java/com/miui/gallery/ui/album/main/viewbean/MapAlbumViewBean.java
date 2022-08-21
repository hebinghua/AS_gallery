package com.miui.gallery.ui.album.main.viewbean;

import com.miui.gallery.R;
import com.miui.gallery.provider.cache.IMedia;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ResourceUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class MapAlbumViewBean extends BaseViewBean<List<IMedia>, MapAlbumViewBean> {
    public String mActionUri;
    public String mAlbumName;
    public List<IMedia> mCovers;
    public String mDescription;

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(List<IMedia> list) {
        super.mapping((MapAlbumViewBean) list);
        setAlbumName(ResourceUtils.getString(R.string.map_album));
        setContentDescription(ResourceUtils.getString(R.string.map_album));
        setCovers(list);
        setId(hashCode());
    }

    public void setAlbumName(String str) {
        this.mAlbumName = str;
    }

    public String getAlbumName() {
        return this.mAlbumName;
    }

    public void setContentDescription(String str) {
        this.mDescription = str;
    }

    public void setCovers(List<IMedia> list) {
        if (BaseMiscUtil.isValid(list)) {
            this.mCovers = new ArrayList();
            for (IMedia iMedia : list) {
                if (this.mCovers.size() == 4) {
                    return;
                }
                if (iMedia.hasValidLocationInfo() && iMedia.getSmallSizeThumb() != null) {
                    this.mCovers.add(iMedia);
                }
            }
        }
    }

    public void setIntentActionURI(String str) {
        this.mActionUri = str;
    }

    public List<IMedia> getCovers() {
        return this.mCovers;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean, java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        MapAlbumViewBean mapAlbumViewBean = (MapAlbumViewBean) obj;
        return Objects.equals(this.mAlbumName, mapAlbumViewBean.mAlbumName) && Objects.equals(this.mDescription, mapAlbumViewBean.mDescription) && Objects.equals(this.mCovers, mapAlbumViewBean.mCovers) && Objects.equals(this.mActionUri, mapAlbumViewBean.mActionUri);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), this.mAlbumName, this.mDescription, this.mCovers, this.mActionUri);
    }
}
