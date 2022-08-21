package com.miui.gallery.ui.album.common;

import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider;
import com.miui.gallery.base_optimization.util.ExceptionUtils;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Locale;
import java.util.Objects;

/* loaded from: classes2.dex */
public class CommonAlbumItemViewBean<DTO extends Entity, R extends CommonAlbumItemViewBean> extends BaseViewBean<DTO, R> implements AlbumDetailInfoProvider {
    public String albumContentDescription;
    public String albumCoverPath;
    public Uri albumCoverUri;
    public String albumName;
    public CharSequence albumSubTitleText;
    public long coverSize;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public /* bridge */ /* synthetic */ void mapping(Object obj) {
        mapping((CommonAlbumItemViewBean<DTO, R>) ((Entity) obj));
    }

    public CommonAlbumItemViewBean() {
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.miui.gallery.dao.base.Entity, SOURCE] */
    public CommonAlbumItemViewBean(R r) {
        super(r);
        this.mSource = (Entity) r.mSource;
        this.id = r.id;
        this.mNeedShowMoreStyle = r.mNeedShowMoreStyle;
        this.albumName = r.albumName;
        this.albumCoverUri = r.albumCoverUri;
        this.albumCoverPath = r.albumCoverPath;
        this.coverSize = r.coverSize;
        this.albumSubTitleText = r.albumSubTitleText;
        this.albumContentDescription = r.albumContentDescription;
    }

    public void setAlbumSubTitleText(CharSequence charSequence) {
        this.albumSubTitleText = charSequence;
    }

    public void setAlbumName(String str) {
        this.albumName = str;
    }

    public void setAlbumCoverUri(Uri uri) {
        this.albumCoverUri = uri;
    }

    public void setAlbumCoverPath(String str) {
        this.albumCoverPath = str;
    }

    public void setCoverSize(long j) {
        this.coverSize = j;
    }

    public void setAlbumContentDescription(String str) {
        this.albumContentDescription = str;
    }

    public String toString() {
        return "AlbumViewBean{id='" + this.id + CoreConstants.SINGLE_QUOTE_CHAR + ", name=" + this.albumName + '}';
    }

    public void mapping(DTO dto) {
        String str;
        if (dto instanceof Album) {
            super.mapping((CommonAlbumItemViewBean<DTO, R>) dto);
            Album album = (Album) dto;
            setId(album.getAlbumId());
            setAlbumCoverPath(album.getCoverPath());
            setAlbumName(album.getDisplayedAlbumName());
            if (album.getCoverId() != 0) {
                setAlbumCoverUri(Album.getCoverUri(album.getCoverSyncState(), album.getCoverId()));
            }
            setCoverSize(album.getCoverSize());
            if (!album.isBabyAlbum() || album.isShareAlbum()) {
                setAlbumSubTitleText(String.format(Locale.getDefault(), "%d", Integer.valueOf(album.getPhotoCount())));
            } else {
                String shareAlbumInfoTipTextIfNeed = ShareAlbumHelper.getShareAlbumInfoTipTextIfNeed(album);
                StringBuilder sb = new StringBuilder();
                sb.append(ResourceUtils.getQuantityString(R.plurals.album_photo_count_text, album.getPhotoCount(), Integer.valueOf(album.getPhotoCount())));
                if (TextUtils.isEmpty(shareAlbumInfoTipTextIfNeed)) {
                    str = "";
                } else {
                    str = " | " + shareAlbumInfoTipTextIfNeed;
                }
                sb.append(str);
                setAlbumSubTitleText(sb.toString());
            }
            updateDefaultContentDescription();
        }
    }

    public void updateDefaultContentDescription() {
        CharSequence subTitle = getSubTitle();
        if (TextUtils.isEmpty(subTitle)) {
            setAlbumContentDescription(getTitle());
        } else if (TextUtils.equals(subTitle, "0") || TextUtils.equals(subTitle, "1")) {
            setAlbumContentDescription(ResourceUtils.getQuantityString(R.plurals.talkback_album_item_content, 1, getTitle(), subTitle));
        } else {
            setAlbumContentDescription(ResourceUtils.getQuantityString(R.plurals.talkback_album_item_content, 2, getTitle(), subTitle));
        }
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean, java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        CommonAlbumItemViewBean commonAlbumItemViewBean = (CommonAlbumItemViewBean) obj;
        if (this.coverSize == commonAlbumItemViewBean.coverSize && Objects.equals(this.albumName, commonAlbumItemViewBean.albumName) && Objects.equals(this.albumCoverUri, commonAlbumItemViewBean.albumCoverUri) && Objects.equals(this.albumCoverPath, commonAlbumItemViewBean.albumCoverPath) && TextUtils.equals(this.albumSubTitleText, commonAlbumItemViewBean.albumSubTitleText) && (commonAlbumItemViewBean.mSource instanceof Album)) {
            SOURCE source = this.mSource;
            if (source instanceof Album) {
                if (Objects.isNull(source) && Objects.isNull(commonAlbumItemViewBean.mSource)) {
                    return true;
                }
                if (!Objects.isNull(this.mSource) && !Objects.isNull(commonAlbumItemViewBean.mSource) && ((Album) this.mSource).getAttributes() == ((Album) commonAlbumItemViewBean.mSource).getAttributes() && Objects.equals(((Album) this.mSource).getAlbumSortInfo(), ((Album) commonAlbumItemViewBean.mSource).getAlbumSortInfo())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public int hashCode() {
        Object[] objArr = new Object[7];
        objArr[0] = Long.valueOf(this.coverSize);
        objArr[1] = this.albumName;
        objArr[2] = this.albumCoverUri;
        objArr[3] = this.albumCoverPath;
        objArr[4] = this.albumSubTitleText;
        SOURCE source = this.mSource;
        objArr[5] = Long.valueOf(source instanceof Album ? ((Album) source).getAttributes() : 0L);
        SOURCE source2 = this.mSource;
        objArr[6] = source2 instanceof Album ? ((Album) source2).getAlbumSortInfo() : 0;
        return Objects.hash(objArr);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public String getTitle() {
        return this.albumName;
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public String getCoverPath() {
        return this.albumCoverPath;
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public CharSequence getSubTitle() {
        CharSequence charSequence = this.albumSubTitleText;
        if (charSequence != null) {
            return charSequence.toString();
        }
        return null;
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public String getContentDescription() {
        return this.albumContentDescription;
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public Uri getCoverUri() {
        return this.albumCoverUri;
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public long getCoverSize() {
        return this.coverSize;
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public void set(int i, Object obj) {
        try {
            if (i == 31) {
                setId(((Long) obj).longValue());
            } else if (i == 47) {
                setAlbumName((String) obj);
            } else if (i == 63) {
                setAlbumSubTitleText((String) obj);
            } else if (i == 79) {
                if (obj == null) {
                    updateDefaultContentDescription();
                } else {
                    setAlbumContentDescription((String) obj);
                }
            } else if (i == 95) {
                setAlbumCoverPath((String) obj);
            } else if (i == 111) {
                setAlbumCoverUri((Uri) obj);
            } else if (i == 127) {
                setCoverSize(((Long) obj).longValue());
            } else if (i == 143) {
                setSource((Entity) obj);
            } else {
                throw new IllegalArgumentException("cant support update field:" + i);
            }
        } catch (Exception e) {
            DefaultLogger.w(getClass().getSimpleName(), ExceptionUtils.getStackTraceString(e));
        }
    }

    public boolean isMoreStyle() {
        return needToShowMoreStyle();
    }
}
