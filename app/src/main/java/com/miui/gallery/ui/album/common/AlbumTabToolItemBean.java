package com.miui.gallery.ui.album.common;

import com.google.gson.annotations.SerializedName;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.ResourceUtils;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class AlbumTabToolItemBean extends BaseViewBean {
    @SerializedName("title")
    private String mDefaultTitle;
    @SerializedName("event")
    private String mEventTip;
    @SerializedName("goto")
    private String mGotoLink;
    @SerializedName("icons")
    private List<Icon> mIcon;
    @SerializedName("sort")
    private int mSort;
    @SerializedName("subTitle")
    private String mSubTitle;
    @SerializedName("titleResName")
    private String mTitleRes;

    public AlbumTabToolItemBean() {
        this.mSubTitle = "";
    }

    public AlbumTabToolItemBean(long j) {
        super(j);
        this.mSubTitle = "";
    }

    public Icon getIconBean() {
        boolean isGridModeByAlbumTabToolGroup = AlbumPageConfig.getAlbumTabConfig().isGridModeByAlbumTabToolGroup();
        for (Icon icon : this.mIcon) {
            if (icon.isGridMode() && isGridModeByAlbumTabToolGroup) {
                return icon;
            }
            if (!icon.isGridMode() && !isGridModeByAlbumTabToolGroup) {
                return icon;
            }
        }
        return null;
    }

    public String getTitle() {
        int stringResourceIdentifier = ResourceUtils.getStringResourceIdentifier(this.mTitleRes, GalleryApp.sGetAndroidContext());
        if (stringResourceIdentifier != 0) {
            return ResourceUtils.getString(stringResourceIdentifier);
        }
        return this.mDefaultTitle;
    }

    public int getSort() {
        return this.mSort;
    }

    public String getGotoLink() {
        return this.mGotoLink;
    }

    public String getEventTip() {
        return this.mEventTip;
    }

    public String getSubTitle() {
        return this.mSubTitle;
    }

    public String getTitleRes() {
        return this.mTitleRes;
    }

    public String getDefaultTitle() {
        return this.mDefaultTitle;
    }

    public List<Icon> getIcon() {
        return this.mIcon;
    }

    public AlbumTabToolItemBean setSubTitle(String str) {
        this.mSubTitle = str;
        return this;
    }

    public AlbumTabToolItemBean setIcon(List<Icon> list) {
        this.mIcon = list;
        return this;
    }

    public AlbumTabToolItemBean setTitleRes(String str) {
        this.mTitleRes = str;
        return this;
    }

    public AlbumTabToolItemBean setGotoLink(String str) {
        this.mGotoLink = str;
        return this;
    }

    public AlbumTabToolItemBean setDefaultTitle(String str) {
        this.mDefaultTitle = str;
        return this;
    }

    public AlbumTabToolItemBean setSort(int i) {
        this.mSort = i;
        return this;
    }

    public AlbumTabToolItemBean setEventTip(String str) {
        this.mEventTip = str;
        return this;
    }

    /* loaded from: classes2.dex */
    public static class Icon {
        @SerializedName("data")
        private String data;
        @SerializedName("mode")
        private String mode;

        public String getData() {
            return this.data;
        }

        public boolean isGridMode() {
            return this.mode.equals("grid");
        }

        public boolean isNeedUseBase64DecoderIcon() {
            return FileUtils.isBase64Url(this.data);
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
        AlbumTabToolItemBean albumTabToolItemBean = (AlbumTabToolItemBean) obj;
        return Objects.equals(this.mDefaultTitle, albumTabToolItemBean.mDefaultTitle) && Objects.equals(this.mSubTitle, albumTabToolItemBean.mSubTitle);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), this.mDefaultTitle, this.mSubTitle);
    }
}
