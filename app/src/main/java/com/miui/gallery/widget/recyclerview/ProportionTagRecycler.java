package com.miui.gallery.widget.recyclerview;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/* loaded from: classes3.dex */
public class ProportionTagRecycler<T> {
    public ProportionTagBaseAdapter<T> mAdapter;
    public Stack<ProportionTagView<T>> mCacheView = new Stack<>();
    public List<ProportionTagView<T>> mTagProportionViews = new ArrayList();

    public void setAdapter(ProportionTagBaseAdapter<T> proportionTagBaseAdapter) {
        this.mAdapter = proportionTagBaseAdapter;
    }

    public List<ProportionTagView<T>> calculateProportionTagsPosition(int i, int i2) {
        this.mTagProportionViews.clear();
        for (int i3 = 0; i3 < this.mAdapter.getItemSize(); i3++) {
            ProportionTagView<T> proportionTagView = null;
            if (i3 == 0) {
                if (!this.mCacheView.isEmpty()) {
                    proportionTagView = this.mCacheView.pop();
                }
                if (proportionTagView == null) {
                    proportionTagView = this.mAdapter.onCreatedView();
                }
                this.mAdapter.onBindView(proportionTagView, i3);
                proportionTagView.setPositionY(i);
                this.mTagProportionViews.add(proportionTagView);
            } else {
                List<ProportionTagView<T>> list = this.mTagProportionViews;
                ProportionTagView<T> proportionTagView2 = list.get(list.size() - 1);
                i = (int) (i + Math.ceil(i2 * this.mAdapter.getItem(i3 - 1).getProportion()));
                if (i - proportionTagView2.getPositionY() >= proportionTagView2.getViewHeight()) {
                    if (!this.mCacheView.isEmpty()) {
                        proportionTagView = this.mCacheView.pop();
                    }
                    if (proportionTagView == null) {
                        proportionTagView = this.mAdapter.onCreatedView();
                    }
                    this.mAdapter.onBindView(proportionTagView, i3);
                    proportionTagView.setPositionY(i);
                    this.mTagProportionViews.add(proportionTagView);
                }
            }
        }
        this.mCacheView.addAll(this.mTagProportionViews);
        return this.mTagProportionViews;
    }
}
