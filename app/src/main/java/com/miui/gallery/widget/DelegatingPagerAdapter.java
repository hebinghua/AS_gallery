package com.miui.gallery.widget;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes2.dex */
public class DelegatingPagerAdapter extends PagerAdapter {
    public final PagerAdapter mDelegate;

    public DelegatingPagerAdapter(PagerAdapter pagerAdapter) {
        this.mDelegate = pagerAdapter;
    }

    public PagerAdapter getDelegate() {
        return this.mDelegate;
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public int getCount() {
        return this.mDelegate.getCount();
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void startUpdate(ViewGroup viewGroup) {
        this.mDelegate.startUpdate(viewGroup);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        return this.mDelegate.instantiateItem(viewGroup, i);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        this.mDelegate.destroyItem(viewGroup, i, obj);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        this.mDelegate.setPrimaryItem(viewGroup, i, obj);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void finishUpdate(ViewGroup viewGroup) {
        this.mDelegate.finishUpdate(viewGroup);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return this.mDelegate.isViewFromObject(view, obj);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public int getItemPosition(Object obj, int i) {
        return this.mDelegate.getItemPosition(obj, i);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void notifyDataSetChanged() {
        this.mDelegate.notifyDataSetChanged();
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.mDelegate.registerDataSetObserver(dataSetObserver);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.mDelegate.unregisterDataSetObserver(dataSetObserver);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public float getPageWidth(int i) {
        return this.mDelegate.getPageWidth(i);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void refreshItem(Object obj, int i) {
        this.mDelegate.refreshItem(obj, i);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public Parcelable saveState() {
        return this.mDelegate.saveState();
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        this.mDelegate.restoreState(parcelable, classLoader);
    }

    @Override // com.miui.gallery.widget.PagerAdapter
    public int getSlipWidth(int i, int i2) {
        return this.mDelegate.getSlipWidth(i, i2);
    }
}
