package com.miui.gallery.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes2.dex */
public abstract class PagerAdapter {
    public DataSetObservable mObservable = new DataSetObservable();

    public abstract void destroyItem(ViewGroup viewGroup, int i, Object obj);

    public void finishUpdate(View view) {
    }

    public abstract int getCount();

    public abstract int getItemPosition(Object obj, int i);

    public float getPageWidth(int i) {
        return 1.0f;
    }

    public int getSlipWidth(int i, int i2) {
        return 0;
    }

    public abstract Object instantiateItem(ViewGroup viewGroup, int i);

    public abstract boolean isViewFromObject(View view, Object obj);

    public void refreshItem(Object obj, int i) {
    }

    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }

    public Parcelable saveState() {
        return null;
    }

    public void setPrimaryItem(View view, int i, Object obj) {
    }

    public void startUpdate(View view) {
    }

    public void startUpdate(ViewGroup viewGroup) {
        startUpdate((View) viewGroup);
    }

    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        setPrimaryItem((View) viewGroup, i, obj);
    }

    public void finishUpdate(ViewGroup viewGroup) {
        finishUpdate((View) viewGroup);
    }

    public void notifyDataSetChanged() {
        this.mObservable.notifyChanged();
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.mObservable.registerObserver(dataSetObserver);
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.mObservable.unregisterObserver(dataSetObserver);
    }
}
