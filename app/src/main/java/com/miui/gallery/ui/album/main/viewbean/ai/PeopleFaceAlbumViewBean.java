package com.miui.gallery.ui.album.main.viewbean.ai;

import android.net.Uri;
import com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider;
import com.miui.gallery.provider.PeopleFaceSnapshotHelper;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.face.PeopleItem;
import java.util.Objects;

/* loaded from: classes2.dex */
public class PeopleFaceAlbumViewBean extends BaseViewBean<PeopleItem, PeopleFaceAlbumViewBean> implements AlbumDetailInfoProvider {
    public Uri mAlbumCoverUri;
    public String mAlbumName;
    public String mCoverPath;
    public FaceRegionRectF mFaceRectF;

    public int getRelationType() {
        if (getSource() != null) {
            return getSource().getRelationType();
        }
        return 0;
    }

    public FaceRegionRectF getFaceRectF() {
        return this.mFaceRectF;
    }

    public void setFaceRectF(FaceRegionRectF faceRegionRectF) {
        this.mFaceRectF = faceRegionRectF;
    }

    public DownloadType getDownloadType() {
        return getSource() != null ? PeopleFaceSnapshotHelper.getThumbnailDownloadType(getSource()) : DownloadType.MICRO;
    }

    public String getPeopleServerId() {
        if (getSource() != null) {
            return getSource().getServerId();
        }
        return null;
    }

    public long getPeopleLocalId() {
        if (getSource() != null) {
            return getSource().getLocalId();
        }
        return 0L;
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public String getTitle() {
        return this.mAlbumName;
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public boolean isMoreStyle() {
        return needToShowMoreStyle();
    }

    public void setMoreStyle(boolean z) {
        setNeedShowMoreStyle(z);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public String getCoverPath() {
        return this.mCoverPath;
    }

    public void setCoverPath(String str) {
        this.mCoverPath = str;
    }

    public Uri getAlbumCoverUri() {
        return this.mAlbumCoverUri;
    }

    public void setAlbumCoverUri(Uri uri) {
        this.mAlbumCoverUri = uri;
    }

    public void setTitle(String str) {
        this.mAlbumName = str;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(PeopleItem peopleItem) {
        super.mapping((PeopleFaceAlbumViewBean) peopleItem);
        setFaceRectF(PeopleFaceSnapshotHelper.getFaceRegionRectF(peopleItem));
        setCoverPath(PeopleFaceSnapshotHelper.getThumbnailPath(peopleItem));
        setAlbumCoverUri(PeopleFaceSnapshotHelper.getThumbnailDownloadUri(peopleItem));
        setTitle(peopleItem.getName());
        setId(getPeopleLocalId() + Boolean.hashCode(isMoreStyle()));
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean, java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PeopleFaceAlbumViewBean) || !super.equals(obj)) {
            return false;
        }
        PeopleFaceAlbumViewBean peopleFaceAlbumViewBean = (PeopleFaceAlbumViewBean) obj;
        return this.id == peopleFaceAlbumViewBean.id && Objects.equals(this.mAlbumName, peopleFaceAlbumViewBean.mAlbumName) && Objects.equals(this.mCoverPath, peopleFaceAlbumViewBean.mCoverPath) && Objects.equals(this.mAlbumCoverUri, peopleFaceAlbumViewBean.mAlbumCoverUri) && Objects.equals(this.mFaceRectF, peopleFaceAlbumViewBean.mFaceRectF) && getPeopleLocalId() == peopleFaceAlbumViewBean.getPeopleLocalId() && Objects.equals(getPeopleServerId(), peopleFaceAlbumViewBean.getPeopleServerId()) && isMoreStyle() == peopleFaceAlbumViewBean.isMoreStyle();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public int hashCode() {
        return Objects.hash(Long.valueOf(this.id), this.mAlbumName, this.mCoverPath, this.mAlbumCoverUri, this.mFaceRectF, Long.valueOf(getPeopleLocalId()), getPeopleServerId(), Boolean.valueOf(isMoreStyle()));
    }
}
