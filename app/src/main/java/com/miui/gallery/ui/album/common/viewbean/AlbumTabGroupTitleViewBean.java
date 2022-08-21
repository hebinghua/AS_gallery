package com.miui.gallery.ui.album.common.viewbean;

import com.miui.gallery.ui.album.common.base.BaseViewBean;
import java.util.Objects;

/* loaded from: classes2.dex */
public class AlbumTabGroupTitleViewBean extends BaseViewBean {
    public int mState;
    public final int mTitleRes;

    public AlbumTabGroupTitleViewBean(long j, int i) {
        super(j);
        this.mTitleRes = i;
        this.mState = 2;
    }

    public AlbumTabGroupTitleViewBean(long j, int i, int i2) {
        super(j);
        this.mTitleRes = i;
        this.mState = i2 == 0 ? 2 : i2;
    }

    public int getTitleRes() {
        return this.mTitleRes;
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int i) {
        if (i != 0) {
            this.mState = i;
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
        AlbumTabGroupTitleViewBean albumTabGroupTitleViewBean = (AlbumTabGroupTitleViewBean) obj;
        return this.mTitleRes == albumTabGroupTitleViewBean.mTitleRes && this.mState == albumTabGroupTitleViewBean.mState;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.mTitleRes), Integer.valueOf(this.mState));
    }
}
