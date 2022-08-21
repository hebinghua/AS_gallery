package com.miui.gallery.adapter;

import android.util.SparseIntArray;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.widget.ViewPager;

/* loaded from: classes.dex */
public class BurstPhotoPageAdapter extends PhotoPageAdapter {
    public int mMaxPagerItemWidth;
    public ViewPager mPagerView;

    @Override // com.miui.gallery.adapter.PhotoPageAdapter
    public int getLayoutId(int i) {
        return R.layout.photo_page_burst_item;
    }

    @Override // com.miui.gallery.adapter.PhotoPageAdapter
    public int getViewType(int i) {
        return 4;
    }

    @Override // com.miui.gallery.adapter.PhotoPageAdapter
    public int getViewTypeCount() {
        return 5;
    }

    @Override // com.miui.gallery.adapter.PhotoPageAdapter
    public boolean isNeedPostInstantiateItemTask() {
        return false;
    }

    public BurstPhotoPageAdapter(FragmentActivity fragmentActivity, int i, ImageLoadParams imageLoadParams, ItemViewInfo itemViewInfo, PhotoPageAdapter.OnPreviewedListener onPreviewedListener, PhotoPageAdapter.PhotoPageInteractionListener photoPageInteractionListener) {
        super(fragmentActivity, i, imageLoadParams, itemViewInfo, onPreviewedListener, photoPageInteractionListener);
    }

    @Override // com.miui.gallery.adapter.PhotoPageAdapter
    public boolean isTypeMatch(Object obj, int i) {
        return PhotoPageAdapter.isItemValidate(obj) && ((Integer) ((PhotoPageItem) obj).getTag(R.id.tag_view_type)).intValue() == 4;
    }

    @Override // com.miui.gallery.adapter.PhotoPageAdapter
    public SparseIntArray getLayoutArray() {
        SparseIntArray layoutArray = super.getLayoutArray();
        layoutArray.put(4, getLayoutId(4));
        return layoutArray;
    }

    public void setPagerView(ViewPager viewPager) {
        this.mPagerView = viewPager;
    }

    public void setMaxPagerItemWidth(int i) {
        this.mMaxPagerItemWidth = i;
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public float getPageWidth(int i) {
        float f;
        float f2;
        BaseDataItem dataItem = getDataItem(i);
        if (this.mPagerView != null && dataItem != null) {
            int height = dataItem.getHeight();
            int width = dataItem.getWidth();
            int height2 = this.mPagerView.getHeight();
            int width2 = this.mPagerView.getWidth();
            if (height2 > 0 && width2 > 0 && height > 0 && width > 0) {
                if (ExifUtil.isWidthHeightRotated(dataItem.getOrientation())) {
                    f = height2 * 1.0f * height;
                    f2 = width;
                } else {
                    f = height2 * 1.0f * width;
                    f2 = height;
                }
                float f3 = f / f2;
                int i2 = this.mMaxPagerItemWidth;
                if (f3 > i2) {
                    f3 = i2;
                }
                return f3 / width2;
            }
        }
        return super.getPageWidth(i);
    }
}
