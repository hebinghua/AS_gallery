package com.miui.gallery.ui.album.common;

import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import java.util.Objects;

/* loaded from: classes2.dex */
public class CustomViewItemViewBean extends BaseViewBean {
    public boolean isFillSpan;
    public int layoutId;
    public View mView;
    public int spanCount;

    public CustomViewItemViewBean(View view, CustomViewLayoutParamConfig customViewLayoutParamConfig, boolean z) {
        this.isFillSpan = z;
        view.setTag(R.id.custom_view_item_layout_param_tag, customViewLayoutParamConfig);
        setView(view);
    }

    public void setView(View view) {
        if (view != null) {
            if (view.getId() == 0) {
                throw new IllegalArgumentException("view can't no id");
            }
            setId(view.getId());
            this.mView = view;
        }
    }

    public View getView() {
        return this.mView;
    }

    public int getSpanCount() {
        return this.spanCount;
    }

    public boolean isFillSpan() {
        return this.isFillSpan;
    }

    public int getLayoutId() {
        return this.layoutId;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean, java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CustomViewItemViewBean customViewItemViewBean = (CustomViewItemViewBean) obj;
        return this.spanCount == customViewItemViewBean.spanCount && this.isFillSpan == customViewItemViewBean.isFillSpan && this.id == customViewItemViewBean.id && Objects.equals(this.mView, customViewItemViewBean.mView) && this.layoutId == customViewItemViewBean.layoutId;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.spanCount), Boolean.valueOf(this.isFillSpan), Long.valueOf(this.id), this.mView, Integer.valueOf(this.layoutId));
    }

    /* loaded from: classes2.dex */
    public static final class CustomViewLayoutParamConfig {
        public int height;
        public int[] margins;
        public int width;

        public CustomViewLayoutParamConfig(int[] iArr, int i, int i2) {
            this.margins = iArr;
            this.width = i;
            this.height = i2;
        }

        public int[] getMargins() {
            return this.margins;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
