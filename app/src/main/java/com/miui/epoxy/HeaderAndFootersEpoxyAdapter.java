package com.miui.epoxy;

import android.util.Log;
import android.util.Pair;
import com.miui.epoxy.AsyncDiffEpoxyAdapter;
import com.miui.epoxy.common.BaseItemModel;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.epoxy.loadmore.EpoxyLoadMoreModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public abstract class HeaderAndFootersEpoxyAdapter<T> extends AsyncDiffEpoxyAdapter<T> {
    public EpoxyModel<?> emptyViewModel;
    public final OrderedMap<Long, Pair<T, EpoxyModel<?>>> footers;
    public boolean hasMore;
    public final OrderedMap<Long, Pair<T, EpoxyModel<?>>> headers;
    public EpoxyLoadMoreModel<?> loadMoreModel;
    public onEmptyModelStatusChangeListener mEmptyModelStatusChangeListener;
    public AsyncDiffEpoxyAdapter.OnDataChangeListener<AsyncDiffEpoxyAdapter.ModelWrapper<T, EpoxyModel<?>>> mInternalDataChangeListener;
    public Boolean mLastIsEmptyDatas;

    /* loaded from: classes.dex */
    public interface onEmptyModelStatusChangeListener {
        void onChange(boolean z);
    }

    public final void setHasMore(boolean z) {
    }

    public HeaderAndFootersEpoxyAdapter() {
        this.emptyViewModel = null;
        this.headers = new OrderedMap<>();
        this.footers = new OrderedMap<>();
        this.hasMore = false;
        AsyncDiffEpoxyAdapter.OnDataChangeListener<AsyncDiffEpoxyAdapter.ModelWrapper<T, EpoxyModel<?>>> onDataChangeListener = new AsyncDiffEpoxyAdapter.OnDataChangeListener<AsyncDiffEpoxyAdapter.ModelWrapper<T, EpoxyModel<?>>>() { // from class: com.miui.epoxy.HeaderAndFootersEpoxyAdapter.1
            @Override // com.miui.epoxy.AsyncDiffEpoxyAdapter.OnDataChangeListener
            public /* bridge */ /* synthetic */ void onDataChange(Object obj) {
                onDataChange((AsyncDiffEpoxyAdapter.ModelWrapper) ((AsyncDiffEpoxyAdapter.ModelWrapper) obj));
            }

            public void onDataChange(AsyncDiffEpoxyAdapter.ModelWrapper<T, EpoxyModel<?>> modelWrapper) {
                HeaderAndFootersEpoxyAdapter.this.notifyEmptyStatusChangeListener(modelWrapper);
            }
        };
        this.mInternalDataChangeListener = onDataChangeListener;
        setDataChangeListener(onDataChangeListener);
    }

    public HeaderAndFootersEpoxyAdapter(Executor executor, Executor executor2) {
        super(executor);
        this.emptyViewModel = null;
        this.headers = new OrderedMap<>();
        this.footers = new OrderedMap<>();
        this.hasMore = false;
        AsyncDiffEpoxyAdapter.OnDataChangeListener<AsyncDiffEpoxyAdapter.ModelWrapper<T, EpoxyModel<?>>> onDataChangeListener = new AsyncDiffEpoxyAdapter.OnDataChangeListener<AsyncDiffEpoxyAdapter.ModelWrapper<T, EpoxyModel<?>>>() { // from class: com.miui.epoxy.HeaderAndFootersEpoxyAdapter.1
            @Override // com.miui.epoxy.AsyncDiffEpoxyAdapter.OnDataChangeListener
            public /* bridge */ /* synthetic */ void onDataChange(Object obj) {
                onDataChange((AsyncDiffEpoxyAdapter.ModelWrapper) ((AsyncDiffEpoxyAdapter.ModelWrapper) obj));
            }

            public void onDataChange(AsyncDiffEpoxyAdapter.ModelWrapper<T, EpoxyModel<?>> modelWrapper) {
                HeaderAndFootersEpoxyAdapter.this.notifyEmptyStatusChangeListener(modelWrapper);
            }
        };
        this.mInternalDataChangeListener = onDataChangeListener;
        setDataChangeListener(onDataChangeListener);
    }

    public final void setEmptyViewModel(EpoxyModel<?> epoxyModel) {
        EpoxyModel<?> epoxyModel2 = this.emptyViewModel;
        if (epoxyModel2 == epoxyModel) {
            return;
        }
        if (epoxyModel2 != null) {
            removeModel(epoxyModel2);
        }
        this.emptyViewModel = epoxyModel;
    }

    public boolean isHaveEmptyModel() {
        return this.emptyViewModel != null;
    }

    public final void notifyEmptyStatusChangeListener(List<EpoxyModel<?>> list) {
        int indexOf;
        boolean z = list.isEmpty() || list.contains(this.emptyViewModel);
        if (this.mEmptyModelStatusChangeListener != null) {
            Boolean bool = this.mLastIsEmptyDatas;
            if (bool != null && bool.booleanValue() == z) {
                return;
            }
            if (z) {
                EpoxyModel<?> epoxyModel = this.emptyViewModel;
                if (epoxyModel != null && !containsModel(epoxyModel) && (this.emptyViewModel instanceof BaseItemModel)) {
                    addModels(this.headers.size(), new AsyncDiffEpoxyAdapter.ModelWrapper(Collections.singletonList(((BaseItemModel) this.emptyViewModel).getItemData()), Collections.singletonList(this.emptyViewModel)));
                }
            } else if (this.emptyViewModel != null && (indexOf = getDatas().indexOf(this.emptyViewModel)) >= 0 && indexOf < getDatas().size()) {
                removeModel(indexOf);
            }
            onEmptyModelStatusChangeListener onemptymodelstatuschangelistener = this.mEmptyModelStatusChangeListener;
            Boolean valueOf = Boolean.valueOf(z);
            this.mLastIsEmptyDatas = valueOf;
            onemptymodelstatuschangelistener.onChange(valueOf.booleanValue());
        }
    }

    public void setEmptyModelStatusChangeListener(onEmptyModelStatusChangeListener onemptymodelstatuschangelistener) {
        this.mEmptyModelStatusChangeListener = onemptymodelstatuschangelistener;
    }

    @Override // com.miui.epoxy.AsyncDiffEpoxyAdapter
    public void clearDatas() {
        super.clearDatas();
        if (!isHaveEmptyModel() || containsModel(this.emptyViewModel)) {
            return;
        }
        addModels(0, new AsyncDiffEpoxyAdapter.ModelWrapper(CollectionUtils.singletonList(((BaseItemModel) this.emptyViewModel).getItemData()), Collections.singletonList(this.emptyViewModel)));
    }

    public final EpoxyModel<?> getLoadMoreOrFirstFooter() {
        if (this.hasMore) {
            return this.loadMoreModel;
        }
        if (this.footers.getFirstOrNull() == null) {
            return null;
        }
        return (EpoxyModel) this.footers.getFirstOrNull().second;
    }

    public final boolean addFooter(T t) {
        EpoxyModel<?> transData = transData(t);
        if (transData != null && !this.footers.checkExistAndConsistency(Long.valueOf(transData.id()))) {
            addData(t);
            this.footers.put(Long.valueOf(transData.id()), new Pair<>(t, transData));
            return true;
        }
        return false;
    }

    public final <M extends EpoxyModel> boolean removeFooter(T t) {
        EpoxyModel<?> transData = transData(t);
        if (transData != null && this.footers.checkExistAndConsistency(Long.valueOf(transData.id()))) {
            removeData(t);
            this.footers.remove(Long.valueOf(transData.id()));
            return true;
        }
        return false;
    }

    public final EpoxyModel<?> getFooterModelById(long j) {
        if (this.footers.checkExistAndConsistency(Long.valueOf(j))) {
            return (EpoxyModel) this.footers.get(Long.valueOf(j)).second;
        }
        return null;
    }

    @Override // com.miui.epoxy.AsyncDiffEpoxyAdapter
    public void addDatas(int i, List<T> list) {
        addDatas(i, list, this.hasMore);
    }

    public final void addDatas(int i, List<T> list, boolean z) {
        if (list.isEmpty()) {
            return;
        }
        if (i > getItemCount()) {
            i = getItemCount();
        }
        setHasMore(z);
        addDataModels(i, new AsyncDiffEpoxyAdapter.ModelWrapper(list, transDataList(list)));
    }

    public final void setDatasAndModels(List<T> list, List<EpoxyModel<?>> list2, boolean z) {
        setDatasAndModels(list, list2, z, null);
    }

    public final void setDatasAndModels(List<T> list, List<EpoxyModel<?>> list2, boolean z, Runnable runnable) {
        if ((list == null && list2 == null) || (list != null && list.isEmpty() && list2 == null)) {
            clearDatas();
        } else if ((list == null || list.isEmpty()) && !list2.isEmpty()) {
        } else {
            boolean z2 = this.hasMore;
            if (list2 == null || list2.isEmpty()) {
                replaceAllDataModels(list, z, runnable);
            } else {
                replaceAllModels(new AsyncDiffEpoxyAdapter.ModelWrapper(list, list2), z, runnable);
            }
        }
    }

    public final boolean addDataModels(int i, AsyncDiffEpoxyAdapter.ModelWrapper modelWrapper) {
        EpoxyModel<?> loadMoreOrFirstFooter = getLoadMoreOrFirstFooter();
        if (loadMoreOrFirstFooter == null) {
            return addModels(i, modelWrapper);
        }
        return insertModelsBefore(modelWrapper, loadMoreOrFirstFooter);
    }

    @Override // com.miui.epoxy.AsyncDiffEpoxyAdapter
    public final boolean replaceAllDataModels(List<T> list, boolean z, Runnable runnable) {
        EpoxyModel<?> epoxyModel;
        if (this.headers.size() == 0 && this.footers.size() == 0 && !list.isEmpty() && !isEmpty()) {
            return super.replaceAllDataModels(list, z, runnable);
        }
        int size = list.size() + this.headers.size() + this.footers.size();
        ArrayList arrayList = new ArrayList(size);
        ArrayList arrayList2 = new ArrayList(size);
        for (Pair<T, EpoxyModel<?>> pair : this.headers.values()) {
            arrayList2.add(pair.first);
            arrayList.add((EpoxyModel) pair.second);
        }
        if (list.isEmpty() && isEmpty() && (epoxyModel = this.emptyViewModel) != null) {
            arrayList.add(epoxyModel);
        } else {
            arrayList2.addAll(list);
            arrayList.addAll(transDataList(list));
            boolean z2 = this.hasMore;
        }
        for (Pair<T, EpoxyModel<?>> pair2 : this.footers.values()) {
            arrayList2.add(pair2.first);
            arrayList.add((EpoxyModel) pair2.second);
        }
        return replaceAllModels(new AsyncDiffEpoxyAdapter.ModelWrapper(arrayList2, arrayList), z, runnable);
    }

    @Override // com.miui.epoxy.EpoxyAdapter
    public boolean isEmpty() {
        Boolean bool;
        if (this.emptyViewModel == null || (bool = this.mLastIsEmptyDatas) == null || !bool.booleanValue()) {
            return super.isEmpty();
        }
        return true;
    }

    @Override // com.miui.epoxy.AsyncDiffEpoxyAdapter
    public List<T> getDatas() {
        Boolean bool;
        if (this.emptyViewModel != null && (bool = this.mLastIsEmptyDatas) != null && bool.booleanValue()) {
            if (super.getDatas() == null) {
                return CollectionUtils.emptyList();
            }
            ArrayList arrayList = new ArrayList(super.getDatas());
            arrayList.remove(((BaseItemModel) this.emptyViewModel).getItemData());
            Log.d("HeaderAndFootersEpoxyAdapter", "getDatas and remove emptyPageBean");
            return arrayList;
        }
        return super.getDatas();
    }
}
