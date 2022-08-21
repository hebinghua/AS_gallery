package com.miui.gallery.ui.photoPage.bars.data;

import android.util.Pair;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import com.miui.gallery.model.BaseDataItem;

/* loaded from: classes2.dex */
public abstract class DataPrepareHelper implements Observer<Pair<BaseDataItem, Integer>> {
    public FragmentActivity mContext;
    public IDataProvider mDataProvider;

    public abstract void doPrepare(BaseDataItem baseDataItem, int i);

    public DataPrepareHelper(IDataProvider iDataProvider, FragmentActivity fragmentActivity, LifecycleOwner lifecycleOwner) {
        iDataProvider.getViewModelData().addPrepareDataItemObserver(fragmentActivity, lifecycleOwner, this);
        this.mDataProvider = iDataProvider;
        this.mContext = fragmentActivity;
    }

    @Override // androidx.lifecycle.Observer
    public final void onChanged(Pair<BaseDataItem, Integer> pair) {
        if (pair == null || pair.first == null || ((Integer) pair.second).intValue() < 0) {
            return;
        }
        doPrepare((BaseDataItem) pair.first, ((Integer) pair.second).intValue());
    }
}
