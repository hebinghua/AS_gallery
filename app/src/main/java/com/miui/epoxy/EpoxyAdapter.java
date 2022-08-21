package com.miui.epoxy;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.eventhook.EventHook;
import com.miui.epoxy.eventhook.EventHookHelper;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import miuix.core.util.Pools;

/* loaded from: classes.dex */
public class EpoxyAdapter<M extends EpoxyModel<?>> extends RecyclerView.Adapter<EpoxyViewHolder> {
    public static final DefaultPayload DEFAULT_PAYLOAD = new DefaultPayload();
    public static final String LOG_TAG = "EpoxyAdapter";
    public int spanCount;
    public final GridLayoutManager.SpanSizeLookup spanSizeLookup;
    public ModelList<M> models = new ModelList<>();
    public final EventHookHelper eventHookHelper = new EventHookHelper(this);
    public boolean isAttached = false;

    /* loaded from: classes.dex */
    public static class DefaultPayload {
    }

    /* loaded from: classes.dex */
    public interface IViewHolderCreator<VH extends EpoxyViewHolder> {
        /* renamed from: create */
        VH mo1603create(View view, View view2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(EpoxyViewHolder epoxyViewHolder, int i, List list) {
        onBindViewHolder2(epoxyViewHolder, i, (List<Object>) list);
    }

    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup(int i) {
        setSpanCount(i);
        return this.spanSizeLookup;
    }

    public void setSpanCount(int i) {
        this.spanCount = i;
    }

    public EpoxyAdapter() {
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() { // from class: com.miui.epoxy.EpoxyAdapter.1
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                EpoxyModel<?> model = EpoxyAdapter.this.getModel(i);
                if (model != null) {
                    return model.getSpanSize(EpoxyAdapter.this.spanCount, i, EpoxyAdapter.this.getItemCount());
                }
                return 1;
            }
        };
        this.spanSizeLookup = spanSizeLookup;
        this.spanCount = 1;
        setHasStableIds(true);
        spanSizeLookup.setSpanIndexCacheEnabled(true);
    }

    public void internalSetModels(ModelList<M> modelList) {
        this.models = modelList;
    }

    public EpoxyModel<?> getModel(int i) {
        if (i < 0 || i >= this.models.size()) {
            return null;
        }
        return (EpoxyModel) this.models.get(i);
    }

    public List<EpoxyModel<?>> getModels() {
        return this.models;
    }

    public boolean isEmpty() {
        ModelList<M> modelList = this.models;
        return modelList == null || modelList.isEmpty();
    }

    public boolean containsModel(EpoxyModel<?> epoxyModel) {
        return this.models.indexOf(epoxyModel) >= 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public EpoxyViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        EpoxyViewHolder create = ((ModelList) this.models).viewHolderFactory.create(i, viewGroup);
        this.eventHookHelper.bind(create);
        return create;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(EpoxyViewHolder epoxyViewHolder, int i) {
        onBindViewHolder2(epoxyViewHolder, i, Collections.emptyList());
    }

    /* renamed from: onBindViewHolder  reason: avoid collision after fix types in other method */
    public void onBindViewHolder2(EpoxyViewHolder epoxyViewHolder, int i, List<Object> list) {
        EpoxyModel<?> model = getModel(i);
        if (epoxyViewHolder == null || model == null) {
            return;
        }
        epoxyViewHolder.bind(model, list);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(EpoxyViewHolder epoxyViewHolder) {
        if (epoxyViewHolder == null) {
            return;
        }
        epoxyViewHolder.unbind();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewAttachedToWindow(EpoxyViewHolder epoxyViewHolder) {
        EpoxyModel epoxyModel = epoxyViewHolder.model;
        if (epoxyModel == null) {
            return;
        }
        epoxyModel.attachedToWindow(epoxyViewHolder);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewDetachedFromWindow(EpoxyViewHolder epoxyViewHolder) {
        EpoxyModel epoxyModel = epoxyViewHolder.model;
        if (epoxyModel == null) {
            return;
        }
        epoxyModel.detachedFromWindow(epoxyViewHolder);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.models.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        EpoxyModel<?> model = getModel(i);
        if (model == null) {
            Log.e(LOG_TAG, "getItemViewType error ,no model,position is %d" + i + "---->and current models is" + this.models);
        }
        this.models.checkAndFix(model);
        if (model == null) {
            return -1;
        }
        return model.getViewType();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        EpoxyModel<?> model = getModel(i);
        if (model == null) {
            return -1L;
        }
        return model.id();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.isAttached = true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.isAttached = false;
    }

    /* loaded from: classes.dex */
    public static class ViewHolderFactory {
        public final SparseArray<Pair<Integer, IViewHolderCreator>> creatorSparseArray;
        public LayoutInflater mInflater;

        public ViewHolderFactory() {
            this.creatorSparseArray = new SparseArray<>();
        }

        public void setLayoutInflater(LayoutInflater layoutInflater) {
            this.mInflater = layoutInflater;
        }

        public void register(EpoxyModel epoxyModel) {
            int viewType = epoxyModel.getViewType();
            if (viewType == -1) {
                throw new RuntimeException("illegal viewType=" + viewType);
            } else if (this.creatorSparseArray.get(viewType) != null) {
            } else {
                this.creatorSparseArray.put(viewType, Pair.create(Integer.valueOf(epoxyModel.getLayoutRes()), epoxyModel.mo541getViewHolderCreator()));
            }
        }

        public void register(Collection<? extends EpoxyModel> collection) {
            for (EpoxyModel epoxyModel : collection) {
                if (epoxyModel != null) {
                    register(epoxyModel);
                }
            }
        }

        public EpoxyViewHolder create(int i, ViewGroup viewGroup) {
            Pair<Integer, IViewHolderCreator> pair = this.creatorSparseArray.get(i);
            if (pair == null) {
                throw new RuntimeException("cannot find viewHolderCreator for viewType=" + i + " current viewTypes=" + this.creatorSparseArray);
            }
            try {
                if (this.mInflater == null) {
                    this.mInflater = LayoutInflater.from(viewGroup.getContext());
                }
                return ((IViewHolderCreator) pair.second).mo1603create(((Integer) pair.first).intValue() == 0 ? null : this.mInflater.inflate(((Integer) pair.first).intValue(), viewGroup, false), viewGroup);
            } catch (Exception e) {
                throw new RuntimeException("cannot inflate view=" + viewGroup.getContext().getResources().getResourceName(((Integer) pair.first).intValue()) + "\nreason:" + e.getMessage(), e);
            }
        }

        public void clear() {
            this.creatorSparseArray.clear();
        }
    }

    /* loaded from: classes.dex */
    public static class ModelList<M extends EpoxyModel<?>> extends LinkedList<M> {
        private final ViewHolderFactory viewHolderFactory = new ViewHolderFactory();

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ void add(int i, Object obj) {
            add(i, (int) ((EpoxyModel) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
        public /* bridge */ /* synthetic */ boolean add(Object obj) {
            return add((ModelList<M>) ((EpoxyModel) obj));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object set(int i, Object obj) {
            return set(i, (int) ((EpoxyModel) obj));
        }

        public void checkAndFix(EpoxyModel epoxyModel) {
            this.viewHolderFactory.register(epoxyModel);
        }

        public boolean add(M m) {
            this.viewHolderFactory.register(m);
            return super.add((ModelList<M>) m);
        }

        public void add(int i, M m) {
            this.viewHolderFactory.register(m);
            super.add(i, (int) m);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public boolean addAll(int i, Collection<? extends M> collection) {
            this.viewHolderFactory.register(collection);
            return super.addAll(i, collection);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.LinkedList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean addAll(Collection<? extends M> collection) {
            this.viewHolderFactory.register(collection);
            return super.addAll(collection);
        }

        public M set(int i, M m) {
            this.viewHolderFactory.register(m);
            return (M) super.set(i, (int) m);
        }

        @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            super.clear();
            printViewTypes();
            printStackTrace();
            this.viewHolderFactory.clear();
        }

        public void printStackTrace() {
            String str = EpoxyAdapter.LOG_TAG;
            Log.d(str, "stackTrace:" + TextUtils.join("\n", Thread.currentThread().getStackTrace()));
        }

        public void printViewTypes() {
            StringBuilder acquire = Pools.getStringBuilderPool().acquire();
            try {
                int size = this.viewHolderFactory.creatorSparseArray.size();
                acquire.append("viewTypes{");
                for (int i = 0; i < size; i++) {
                    acquire.append(this.viewHolderFactory.creatorSparseArray.keyAt(i));
                    acquire.append(",");
                }
                acquire.append("}");
                Log.d(EpoxyAdapter.LOG_TAG, acquire.toString());
            } finally {
                Pools.getStringBuilderPool().release(acquire);
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class WrapperViewHolderCreator<VH extends EpoxyWrapperViewHolder<MVH>, MVH extends EpoxyViewHolder> implements IViewHolderCreator<VH> {
        public final int childLayoutRes;
        public final IViewHolderCreator<MVH> childViewHolderCreator;

        public int childViewId() {
            return -1;
        }

        public abstract VH create(View view, MVH mvh);

        public WrapperViewHolderCreator(int i, IViewHolderCreator<MVH> iViewHolderCreator) {
            this.childLayoutRes = i;
            this.childViewHolderCreator = iViewHolderCreator;
        }

        @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
        /* renamed from: create  reason: collision with other method in class */
        public VH mo1603create(View view, View view2) {
            ViewStub viewStub = (ViewStub) view.findViewById(R$id.view_model_child_stub);
            if (viewStub == null) {
                throw new IllegalStateException("layout must have ViewStub{id=view_model_child_stub}");
            }
            viewStub.setLayoutResource(this.childLayoutRes);
            int childViewId = childViewId();
            if (-1 != childViewId) {
                viewStub.setInflatedId(childViewId);
            }
            return create(view, (View) this.childViewHolderCreator.mo1603create(viewStub.inflate(), view2));
        }
    }

    public <VH extends EpoxyViewHolder> void addEventHook(EventHook<VH> eventHook) {
        if (this.isAttached) {
            Log.w(LOG_TAG, "addEventHook is called after adapter attached");
        }
        this.eventHookHelper.add(eventHook);
    }

    public void notifyModelChanged(EpoxyModel<?> epoxyModel) {
        notifyModelChanged(epoxyModel, null);
    }

    public void notifyModelChanged(EpoxyModel<?> epoxyModel, Object obj) {
        int indexOf = this.models.indexOf(epoxyModel);
        if (indexOf != -1) {
            notifyItemChanged(indexOf, obj);
        }
    }

    public void setLayoutInflate(LayoutInflater layoutInflater) {
        ModelList<M> modelList = this.models;
        if (modelList == null) {
            return;
        }
        ((ModelList) modelList).viewHolderFactory.setLayoutInflater(layoutInflater);
    }
}
