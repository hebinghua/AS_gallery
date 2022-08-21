package com.miui.epoxy;

import android.util.Log;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncListDiffer$ListListener;
import androidx.recyclerview.widget.DiffUtil;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.common.BaseItemModel;
import com.miui.epoxy.common.BaseWrapperItemModel;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.epoxy.diff.AsyncDifferConfig;
import com.miui.epoxy.diff.AsyncListDiffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public abstract class AsyncDiffEpoxyAdapter<T> extends EpoxyAdapter<EpoxyModel<?>> {
    public static final InternalDiffItemCallback sInternalDiffItemCallback = new InternalDiffItemCallback();
    public final AsyncListDiffer<EpoxyModel<?>> mAsyncDiffer;
    public OnDataChangeListener mDataChangeListener;

    /* loaded from: classes.dex */
    public interface OnDataChangeListener<T> {
        void onDataChange(T t);
    }

    public abstract EpoxyModel<?> transData(T t);

    public AsyncDiffEpoxyAdapter() {
        this(new AsyncDifferConfig.Builder(sInternalDiffItemCallback).setBackgroundThreadExecutor(null).setListGenerator(new ModelWrapperListGenerator()).build(), null);
    }

    public AsyncDiffEpoxyAdapter(Executor executor) {
        this(new AsyncDifferConfig.Builder(sInternalDiffItemCallback).setBackgroundThreadExecutor(executor).setListGenerator(new ModelWrapperListGenerator()).build(), null);
    }

    public AsyncDiffEpoxyAdapter(AsyncDifferConfig<EpoxyModel<?>> asyncDifferConfig, OnDataChangeListener onDataChangeListener) {
        AsyncListDiffer<EpoxyModel<?>> asyncListDiffer = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), asyncDifferConfig);
        this.mAsyncDiffer = asyncListDiffer;
        this.mDataChangeListener = onDataChangeListener;
        asyncListDiffer.addListListener(new AsyncListDiffer$ListListener<EpoxyModel<?>>() { // from class: com.miui.epoxy.AsyncDiffEpoxyAdapter.1
            @Override // androidx.recyclerview.widget.AsyncListDiffer$ListListener
            public void onCurrentListChanged(List<EpoxyModel<?>> list, List<EpoxyModel<?>> list2) {
                EpoxyAdapter.ModelList modelList = (EpoxyAdapter.ModelList) list2;
                AsyncDiffEpoxyAdapter.this.internalSetModels(modelList);
                AsyncDiffEpoxyAdapter.this.mDataChangeListener.onDataChange(list2);
                modelList.printViewTypes();
            }
        });
    }

    public List<EpoxyModel<?>> transDataList(Collection<T> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (T t : collection) {
            arrayList.add(transData(t));
        }
        return arrayList;
    }

    public ModelWrapper<T, EpoxyModel<?>> internalTransDataList(Collection<T> collection) {
        ModelWrapper<T, EpoxyModel<?>> modelWrapper = new ModelWrapper<>();
        for (T t : collection) {
            modelWrapper.add((ModelWrapper<T, EpoxyModel<?>>) transData(t));
        }
        return modelWrapper;
    }

    public void clearDatas() {
        if (isEmpty()) {
            return;
        }
        removeAllModels();
    }

    public List<T> getDatas() {
        if (!isEmpty() && (super.getModels() instanceof ModelWrapper)) {
            return ((ModelWrapper) super.getModels()).getSourceDatas();
        }
        return CollectionUtils.emptyList();
    }

    public List<T> getDiffingDatas() {
        if (this.mAsyncDiffer.getCurrentDiffingList() instanceof ModelWrapper) {
            return ((ModelWrapper) this.mAsyncDiffer.getCurrentDiffingList()).getSourceDatas();
        }
        return null;
    }

    public T getData(int i) {
        List<T> datas = getDatas();
        if (datas.isEmpty()) {
            return null;
        }
        return datas.get(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void addData(T t) {
        addDatas(t);
    }

    public void addDatas(T... tArr) {
        addDatas(CollectionUtils.arrayToList(tArr));
    }

    public void addDatas(List<T> list) {
        addDatas(getItemCount(), list);
    }

    public void addDatas(int i, List<T> list) {
        if (list.isEmpty()) {
            return;
        }
        addModels(Math.min(i, getItemCount()), internalTransDataList(list));
    }

    public void removeData(T t) {
        removeModel(transData(t));
    }

    public void removeDatas(List<T> list) {
        if (list.isEmpty()) {
            return;
        }
        this.mAsyncDiffer.submitListByRemove(internalTransDataList(list));
    }

    public final boolean updateData(int i, T t) {
        if (-1 == i) {
            return false;
        }
        this.mAsyncDiffer.submitListByUpdate(i, transData(t));
        return true;
    }

    public boolean moveItem(int i, int i2, boolean z) {
        if (i < 0 || i2 < 0 || i == i2 || i >= getItemCount()) {
            return false;
        }
        this.models.add(i2, (int) ((EpoxyModel) this.models.remove(i)));
        if (!z) {
            return true;
        }
        notifyItemMoved(i, i2);
        return true;
    }

    public boolean addModels(int i, Collection<? extends EpoxyModel<?>> collection) {
        this.mAsyncDiffer.submitListByAdd(i, collection);
        return true;
    }

    public boolean insertModelsBefore(Collection<? extends EpoxyModel<?>> collection, EpoxyModel<?> epoxyModel) {
        int indexOf = this.models.indexOf(epoxyModel);
        if (indexOf == -1) {
            return false;
        }
        return addModels(indexOf, collection);
    }

    public boolean removeModel(int i) {
        return this.mAsyncDiffer.submitListByRemove(i);
    }

    public boolean removeModel(EpoxyModel<?> epoxyModel) {
        this.mAsyncDiffer.submitListByRemove((AsyncListDiffer<EpoxyModel<?>>) epoxyModel);
        return true;
    }

    public void removeAllModels() {
        this.mAsyncDiffer.submitListByClear();
    }

    public boolean replaceAllDataModels(List<T> list, boolean z, Runnable runnable) {
        return replaceAllModels(internalTransDataList(list), z, runnable);
    }

    public boolean replaceAllModels(List<EpoxyModel<?>> list, boolean z, Runnable runnable) {
        if (z) {
            this.models.clear();
            this.models.addAll(list);
            notifyItemRangeChanged(0, this.models.size());
            if (runnable == null) {
                return true;
            }
            runnable.run();
            return true;
        }
        this.mAsyncDiffer.submitList(list, runnable);
        return true;
    }

    /* loaded from: classes.dex */
    public static final class ModelWrapperListGenerator<T> implements AsyncListDiffer.ListGenerator<T> {
        public ModelWrapperListGenerator() {
        }

        @Override // com.miui.epoxy.diff.AsyncListDiffer.ListGenerator
        public List<T> generate(List<T> list) {
            if (list == null) {
                return new ModelWrapper();
            }
            if (!(list instanceof ModelWrapper)) {
                return null;
            }
            return new ModelWrapper(((ModelWrapper) list).getSourceDatas(), list);
        }
    }

    /* loaded from: classes.dex */
    public static class ModelWrapper<T, M extends EpoxyModel<?>> extends EpoxyAdapter.ModelList<M> {
        public final List<T> mSourceDatas;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.miui.epoxy.EpoxyAdapter.ModelList, java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ void add(int i, Object obj) {
            add(i, (int) ((EpoxyModel) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.miui.epoxy.EpoxyAdapter.ModelList, java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
        public /* bridge */ /* synthetic */ boolean add(Object obj) {
            return add((ModelWrapper<T, M>) ((EpoxyModel) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.miui.epoxy.EpoxyAdapter.ModelList, java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object set(int i, Object obj) {
            return set(i, (int) ((EpoxyModel) obj));
        }

        public ModelWrapper() {
            this(new LinkedList());
        }

        public ModelWrapper(List<T> list) {
            this(list, new LinkedList());
        }

        public ModelWrapper(List<T> list, List<M> list2) {
            this.mSourceDatas = list;
            super.addAll(0, list2);
        }

        @Override // com.miui.epoxy.EpoxyAdapter.ModelList, java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.mSourceDatas.clear();
            super.clear();
        }

        @Override // java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        /* renamed from: remove */
        public M mo444remove(int i) {
            if (i >= this.mSourceDatas.size()) {
                return null;
            }
            this.mSourceDatas.remove(i);
            return (M) super.remove(i);
        }

        @Override // java.util.LinkedList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque
        public boolean remove(Object obj) {
            T data = getData(obj);
            if (data == null) {
                Log.e("ModelWrapper", "set data parameter not BaseItemModel/BaseWrapperItemModel");
                return false;
            }
            this.mSourceDatas.remove(data);
            return super.remove(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean removeAll(Collection<?> collection) {
            boolean z = true;
            if (collection instanceof ModelWrapper) {
                return this.mSourceDatas.removeAll(((ModelWrapper) collection).getSourceDatas()) && super.removeAll(collection);
            }
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                T data = getData(it.next());
                if (data != null) {
                    int indexOf = this.mSourceDatas.indexOf(data);
                    if (indexOf != -1) {
                        this.mSourceDatas.remove(indexOf);
                        super.remove(indexOf);
                    }
                } else {
                    Log.e("ModelWrapper", "removeAll Datas indexof = -1");
                    z = false;
                }
            }
            return z;
        }

        @Override // com.miui.epoxy.EpoxyAdapter.ModelList
        public M set(int i, M m) {
            T data = getData(m);
            if (data == null) {
                Log.e("ModelWrapper", "set data parameter not BaseItemModel/BaseWrapperItemModel");
                return null;
            }
            this.mSourceDatas.set(i, data);
            return (M) super.set(i, (int) m);
        }

        @Override // com.miui.epoxy.EpoxyAdapter.ModelList, java.util.LinkedList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean addAll(Collection<? extends M> collection) {
            return super.addAll(collection);
        }

        @Override // com.miui.epoxy.EpoxyAdapter.ModelList, java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public boolean addAll(int i, Collection<? extends M> collection) {
            boolean z = true;
            if (collection instanceof ModelWrapper) {
                return this.mSourceDatas.addAll(i, ((ModelWrapper) collection).getSourceDatas()) && super.addAll(i, collection);
            }
            for (M m : collection) {
                T data = getData(m);
                if (data != null) {
                    this.mSourceDatas.add(i, data);
                    super.add(i, (int) m);
                    i++;
                } else {
                    z = false;
                }
            }
            return z;
        }

        @Override // com.miui.epoxy.EpoxyAdapter.ModelList
        public void add(int i, M m) {
            T data = getData(m);
            if (data == null) {
                return;
            }
            this.mSourceDatas.add(i, data);
            super.add(i, (int) m);
        }

        @Override // com.miui.epoxy.EpoxyAdapter.ModelList
        public boolean add(M m) {
            T data = getData(m);
            if (data == null) {
                return false;
            }
            this.mSourceDatas.add(data);
            return super.add((ModelWrapper<T, M>) m);
        }

        public List<T> getSourceDatas() {
            return this.mSourceDatas;
        }

        public final T getData(Object obj) {
            if (obj instanceof BaseItemModel) {
                return (T) ((BaseItemModel) obj).getItemData();
            }
            if (!(obj instanceof BaseWrapperItemModel)) {
                return null;
            }
            return (T) ((BaseWrapperItemModel) obj).getItemData();
        }
    }

    public void setDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.mDataChangeListener = onDataChangeListener;
    }

    /* loaded from: classes.dex */
    public static class InternalDiffItemCallback extends DiffUtil.ItemCallback<EpoxyModel<?>> {
        public InternalDiffItemCallback() {
        }

        @Override // androidx.recyclerview.widget.DiffUtil.ItemCallback
        public boolean areItemsTheSame(EpoxyModel<?> epoxyModel, EpoxyModel<?> epoxyModel2) {
            return epoxyModel.getClass().equals(epoxyModel2.getClass()) && epoxyModel.id() == epoxyModel2.id();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.ItemCallback
        public boolean areContentsTheSame(EpoxyModel<?> epoxyModel, EpoxyModel<?> epoxyModel2) {
            return epoxyModel.getClass().getCanonicalName().equals(epoxyModel2.getClass().getCanonicalName()) && epoxyModel.isContentTheSame(epoxyModel2);
        }

        @Override // androidx.recyclerview.widget.DiffUtil.ItemCallback
        public Object getChangePayload(EpoxyModel<?> epoxyModel, EpoxyModel<?> epoxyModel2) {
            if (epoxyModel.id() != epoxyModel2.id()) {
                return super.getChangePayload(epoxyModel, epoxyModel2);
            }
            return epoxyModel.getDiffChangeResult(epoxyModel2);
        }
    }
}
