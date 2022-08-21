package com.miui.gallery.ui.album.aialbum.viewbean;

import android.net.Uri;
import com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider;
import com.miui.gallery.dao.LocationAndTagsAlbumTableServices;
import com.miui.gallery.model.dto.SuggestionData;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import java.util.List;

/* loaded from: classes2.dex */
public class LocationAndTagsAlbumItemViewBean extends BaseViewBean<SuggestionData, LocationAndTagsAlbumItemViewBean> implements AlbumDetailInfoProvider {
    public String actionUri;
    public String mAlbumName;
    public Uri mCoverUri;

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public void set(int i, Object obj) {
        if (i == 47) {
            this.mAlbumName = (String) obj;
        } else if (i != 111) {
        } else {
            this.mCoverUri = (Uri) obj;
        }
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public Uri getCoverUri() {
        return this.mCoverUri;
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public boolean isMoreStyle() {
        return needToShowMoreStyle();
    }

    public String getIntentActionURI() {
        return getSource() != null ? getSource().getIntentActionURI() : this.actionUri;
    }

    public Uri getMoreActionUri() {
        return GalleryContract.Search.URI_LOCATION_LIST_PAGE;
    }

    public void setIntentActionURI(String str) {
        if (getSource() != null) {
            getSource().setIntentActionURI(str);
        } else {
            this.actionUri = str;
        }
    }

    public List<String> getBackupIcons() {
        if (getSource() != null) {
            return getSource().getBackupIcons();
        }
        return null;
    }

    public boolean isEmptyBackupIcons() {
        List<String> backupIcons = getBackupIcons();
        return backupIcons != null && !backupIcons.isEmpty();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(SuggestionData suggestionData) {
        super.mapping((LocationAndTagsAlbumItemViewBean) suggestionData);
        String icon = suggestionData.getIcon();
        set(111, Uri.parse(icon));
        String title = suggestionData.getTitle();
        set(47, title);
        setId(LocationAndTagsAlbumTableServices.getInstance().parseAlbumCoverServerId(icon) + title.hashCode());
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public String getTitle() {
        return this.mAlbumName;
    }
}
