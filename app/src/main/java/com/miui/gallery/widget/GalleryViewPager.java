package com.miui.gallery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.miui.gallery.widget.ViewPager;

/* loaded from: classes2.dex */
public class GalleryViewPager extends ViewPager {
    public int mLayoutDirection;
    public ReversingOnPageChangeListener mReversingOnPageChangeListener;
    public ReversingOnPageSettledListener mReversingOnPageSettledListener;

    public static int processingIndex(int i, int i2, boolean z) {
        return z ? (i2 - i) - 1 : i;
    }

    public GalleryViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mReversingOnPageChangeListener = new ReversingOnPageChangeListener();
        this.mReversingOnPageSettledListener = new ReversingOnPageSettledListener();
        init(context);
    }

    public final void init(Context context) {
        super.setOnPageChangeListener(this.mReversingOnPageChangeListener);
        super.setOnPageSettledListener(this.mReversingOnPageSettledListener);
        this.mLayoutDirection = context.getResources().getConfiguration().getLayoutDirection();
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (this.mLayoutDirection != i) {
            int currentItem = getCurrentItem();
            this.mLayoutDirection = i;
            PagerAdapter adapter = getAdapter();
            if (adapter == null) {
                return;
            }
            adapter.notifyDataSetChanged();
            setCurrentItem(currentItem);
        }
    }

    @Override // com.miui.gallery.widget.ViewPager
    public void setAdapter(PagerAdapter pagerAdapter) {
        if (pagerAdapter != null) {
            pagerAdapter = new ReversingAdapter(pagerAdapter);
        }
        super.setAdapter(pagerAdapter);
        setCurrentItem(0);
    }

    @Override // com.miui.gallery.widget.ViewPager
    public PagerAdapter getAdapter() {
        ReversingAdapter reversingAdapter = (ReversingAdapter) super.getAdapter();
        if (reversingAdapter == null) {
            return null;
        }
        return reversingAdapter.getDelegate();
    }

    public final boolean isRtl() {
        return this.mLayoutDirection == 1;
    }

    @Override // com.miui.gallery.widget.ViewPager
    public Object getItem(int i) {
        PagerAdapter adapter = super.getAdapter();
        if (adapter != null) {
            i = processingIndex(i, adapter.getCount(), isRtl());
        }
        return super.getItem(i);
    }

    @Override // com.miui.gallery.widget.ViewPager
    public int getCurrentItem() {
        int currentItem = super.getCurrentItem();
        PagerAdapter adapter = super.getAdapter();
        return adapter != null ? processingIndex(currentItem, adapter.getCount(), isRtl()) : currentItem;
    }

    @Override // com.miui.gallery.widget.ViewPager
    public void setCurrentItem(int i, boolean z) {
        PagerAdapter adapter = super.getAdapter();
        if (adapter != null) {
            i = processingIndex(i, adapter.getCount(), isRtl());
        }
        super.setCurrentItem(i, z);
    }

    @Override // com.miui.gallery.widget.ViewPager
    public void setCurrentItem(int i) {
        PagerAdapter adapter = super.getAdapter();
        if (adapter != null) {
            i = processingIndex(i, adapter.getCount(), isRtl());
        }
        super.setCurrentItem(i);
    }

    @Override // com.miui.gallery.widget.ViewPager
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mReversingOnPageChangeListener.mListener = onPageChangeListener;
    }

    @Override // com.miui.gallery.widget.ViewPager
    public void setOnPageSettledListener(ViewPager.OnPageSettledListener onPageSettledListener) {
        this.mReversingOnPageSettledListener.mListener = onPageSettledListener;
    }

    /* loaded from: classes2.dex */
    public class ReversingOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public ViewPager.OnPageChangeListener mListener;

        public ReversingOnPageChangeListener() {
        }

        @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            ViewPager.OnPageChangeListener onPageChangeListener = this.mListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrolled(i, f, i2);
            }
        }

        @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            if (this.mListener != null) {
                PagerAdapter adapter = GalleryViewPager.super.getAdapter();
                if (adapter != null) {
                    i = GalleryViewPager.processingIndex(i, adapter.getCount(), GalleryViewPager.this.isRtl());
                }
                this.mListener.onPageSelected(i);
            }
        }

        @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
            ViewPager.OnPageChangeListener onPageChangeListener = this.mListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(i);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class ReversingOnPageSettledListener implements ViewPager.OnPageSettledListener {
        public ViewPager.OnPageSettledListener mListener;

        public ReversingOnPageSettledListener() {
        }

        @Override // com.miui.gallery.widget.ViewPager.OnPageSettledListener
        public void onPageSettled(int i) {
            if (this.mListener != null) {
                PagerAdapter adapter = GalleryViewPager.super.getAdapter();
                if (adapter != null) {
                    i = GalleryViewPager.processingIndex(i, adapter.getCount(), GalleryViewPager.this.isRtl());
                }
                this.mListener.onPageSettled(i);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class ReversingAdapter extends DelegatingPagerAdapter {
        public ReversingAdapter(PagerAdapter pagerAdapter) {
            super(pagerAdapter);
        }

        @Override // com.miui.gallery.widget.DelegatingPagerAdapter, com.miui.gallery.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            super.destroyItem(viewGroup, GalleryViewPager.processingIndex(i, getCount(), GalleryViewPager.this.isRtl()), obj);
        }

        @Override // com.miui.gallery.widget.DelegatingPagerAdapter, com.miui.gallery.widget.PagerAdapter
        public float getPageWidth(int i) {
            return super.getPageWidth(GalleryViewPager.processingIndex(i, getCount(), GalleryViewPager.this.isRtl()));
        }

        @Override // com.miui.gallery.widget.DelegatingPagerAdapter, com.miui.gallery.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            return super.instantiateItem(viewGroup, GalleryViewPager.processingIndex(i, getCount(), GalleryViewPager.this.isRtl()));
        }

        @Override // com.miui.gallery.widget.DelegatingPagerAdapter, com.miui.gallery.widget.PagerAdapter
        public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
            super.setPrimaryItem(viewGroup, GalleryViewPager.processingIndex(i, getCount(), GalleryViewPager.this.isRtl()), obj);
        }

        @Override // com.miui.gallery.widget.DelegatingPagerAdapter, com.miui.gallery.widget.PagerAdapter
        public void refreshItem(Object obj, int i) {
            super.refreshItem(obj, GalleryViewPager.processingIndex(i, getCount(), GalleryViewPager.this.isRtl()));
        }

        @Override // com.miui.gallery.widget.DelegatingPagerAdapter, com.miui.gallery.widget.PagerAdapter
        public int getItemPosition(Object obj, int i) {
            return super.getItemPosition(obj, GalleryViewPager.processingIndex(i, getCount(), GalleryViewPager.this.isRtl()));
        }
    }
}
