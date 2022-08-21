package com.miui.gallery.ui.album.common;

import android.util.Pair;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.gallery.R;
import com.miui.gallery.adapter.GallerySimpleEpoxyAdapter;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryViewHolder;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.util.thread.ThreadManager;
import com.miui.gallery.widget.recyclerview.BasicRecyclerView;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class RecyclerViewItemModel extends BaseGalleryItemModel<ConfigBean<BaseViewBean>, VH> {
    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return R.layout.album_common_list_group_item;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getSpanSize(int i, int i2, int i3) {
        return i;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((VH) epoxyViewHolder, (List<Object>) list);
    }

    public RecyclerViewItemModel(ConfigBean configBean) {
        super(configBean.getId(), configBean);
    }

    @Override // com.miui.epoxy.EpoxyModel
    public void bindData(VH vh) {
        super.bindData((RecyclerViewItemModel) vh);
        bindRecyclerView(vh, getItemData());
    }

    public void bindPartialData(VH vh, List<Object> list) {
        Object obj = list.get(0);
        if (obj instanceof ConfigBean) {
            bindRecyclerView(vh, (ConfigBean) obj);
        }
    }

    public final void bindRecyclerView(VH vh, ConfigBean<BaseViewBean> configBean) {
        if (vh.mRecyclerView.getAdapter() == null) {
            GallerySimpleEpoxyAdapter gallerySimpleEpoxyAdapter = new GallerySimpleEpoxyAdapter(ThreadManager.getExecutor(79), null);
            gallerySimpleEpoxyAdapter.setDatasAndModels(configBean.getDatas(), configBean.getModels(), false);
            vh.mRecyclerView.setAdapter(gallerySimpleEpoxyAdapter);
            RecyclerView.LayoutManager layoutManager = configBean.getLayoutManager(vh.mRecyclerView);
            if (layoutManager != null) {
                vh.mRecyclerView.setLayoutManager(layoutManager);
            }
            List<RecyclerView.ItemDecoration> itemDecorations = configBean.getItemDecorations(vh.mRecyclerView);
            if (itemDecorations != null) {
                for (RecyclerView.ItemDecoration itemDecoration : itemDecorations) {
                    vh.mRecyclerView.addItemDecoration(itemDecoration);
                }
            }
            if (!configBean.isIsManualSetItemAnimator()) {
                return;
            }
            vh.mRecyclerView.setItemAnimator(configBean.getItemAnimator(vh.mRecyclerView));
        } else if (configBean.getDatas() != null && configBean.getModels() != null) {
            vh.getAdapter().setDatasAndModels(configBean.getDatas(), configBean.getModels(), false);
        }
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getViewType() {
        return (int) getItemData().getId();
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<VH>() { // from class: com.miui.gallery.ui.album.common.RecyclerViewItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public VH mo1603create(View view, View view2) {
                return new VH(view);
            }
        };
    }

    @Override // com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof RecyclerViewItemModel) {
            ConfigBean<BaseViewBean> itemData = ((RecyclerViewItemModel) epoxyModel).getItemData();
            ConfigBean configBean = new ConfigBean();
            if (!getItemData().mDatas.equals(itemData.mDatas)) {
                configBean.mDatas = itemData.mDatas;
                configBean.mModels = itemData.mModels;
            }
            return configBean;
        }
        return super.getDiffChangeResult(epoxyModel);
    }

    /* loaded from: classes2.dex */
    public static class ConfigBean<T> extends BaseViewBean {
        public List<T> mDatas;
        public Pair<Integer, GridLayoutManager.SpanSizeLookup> mGridLayoutConfig;
        public RecyclerViewInitProvider mInitProvider;
        public boolean mIsManualSetItemAnimator;
        public RecyclerView.ItemAnimator mItemAnimator;
        public List<RecyclerView.ItemDecoration> mItemDecorations;
        public RecyclerView.LayoutManager mLayoutManager;
        public Pair<Integer, Boolean> mLinearLayoutConfig;
        public List mModels;

        /* loaded from: classes2.dex */
        public interface RecyclerViewInitProvider {
            default RecyclerView.ItemAnimator getItemAnimator(RecyclerView recyclerView) {
                return null;
            }

            default List<RecyclerView.ItemDecoration> getItemDecorations(RecyclerView recyclerView) {
                return null;
            }

            default RecyclerView.LayoutManager getLayoutManager(RecyclerView recyclerView) {
                return null;
            }
        }

        @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
        public long getId() {
            return this.id;
        }

        public List<T> getDatas() {
            return this.mDatas;
        }

        public List getModels() {
            return this.mModels;
        }

        public boolean isIsManualSetItemAnimator() {
            return this.mIsManualSetItemAnimator;
        }

        public RecyclerView.ItemAnimator getItemAnimator(RecyclerView recyclerView) {
            RecyclerView.ItemAnimator itemAnimator = this.mItemAnimator;
            if (itemAnimator != null) {
                return itemAnimator;
            }
            RecyclerViewInitProvider recyclerViewInitProvider = this.mInitProvider;
            if (recyclerViewInitProvider == null) {
                return null;
            }
            return recyclerViewInitProvider.getItemAnimator(recyclerView);
        }

        public RecyclerView.LayoutManager getLayoutManager(RecyclerView recyclerView) {
            if (this.mGridLayoutConfig != null) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), ((Integer) this.mGridLayoutConfig.first).intValue());
                gridLayoutManager.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup) this.mGridLayoutConfig.second);
                return gridLayoutManager;
            } else if (this.mLinearLayoutConfig != null) {
                return new LinearLayoutManager(recyclerView.getContext(), ((Integer) this.mLinearLayoutConfig.first).intValue(), ((Boolean) this.mLinearLayoutConfig.second).booleanValue());
            } else {
                RecyclerViewInitProvider recyclerViewInitProvider = this.mInitProvider;
                if (recyclerViewInitProvider != null) {
                    return recyclerViewInitProvider.getLayoutManager(recyclerView);
                }
                return this.mLayoutManager;
            }
        }

        public List<RecyclerView.ItemDecoration> getItemDecorations(RecyclerView recyclerView) {
            List<RecyclerView.ItemDecoration> list = this.mItemDecorations;
            if (list != null) {
                return list;
            }
            RecyclerViewInitProvider recyclerViewInitProvider = this.mInitProvider;
            if (recyclerViewInitProvider == null) {
                return null;
            }
            return recyclerViewInitProvider.getItemDecorations(recyclerView);
        }

        /* loaded from: classes2.dex */
        public static class Builder<T> {
            public final long id;
            public List<T> mDatas;
            public Pair<Integer, GridLayoutManager.SpanSizeLookup> mGridLayoutConfig;
            public RecyclerViewInitProvider mInfoProvider;
            public boolean mIsManualSetItemAnimator;
            public RecyclerView.ItemAnimator mItemAnimator;
            public List<RecyclerView.ItemDecoration> mItemDecorations;
            public RecyclerView.LayoutManager mLayoutManager;
            public Pair<Integer, Boolean> mLinearLayoutConfig;
            public List mModels;

            public Builder(long j) {
                this.id = j;
            }

            public Builder<T> useLinearLayoutManager(int i, boolean z) {
                this.mLinearLayoutConfig = new Pair<>(Integer.valueOf(i), Boolean.valueOf(z));
                return this;
            }

            public Builder<T> setDatas(List<T> list, List list2) {
                this.mDatas = list;
                this.mModels = list2;
                return this;
            }

            public Builder<T> setProvider(RecyclerViewInitProvider recyclerViewInitProvider) {
                this.mInfoProvider = recyclerViewInitProvider;
                return this;
            }

            public ConfigBean<T> build() {
                ConfigBean<T> configBean = new ConfigBean<>();
                configBean.id = this.id;
                configBean.mGridLayoutConfig = this.mGridLayoutConfig;
                configBean.mLinearLayoutConfig = this.mLinearLayoutConfig;
                configBean.mItemDecorations = this.mItemDecorations;
                configBean.mDatas = this.mDatas;
                configBean.mModels = this.mModels;
                configBean.mLayoutManager = this.mLayoutManager;
                configBean.mInitProvider = this.mInfoProvider;
                configBean.mIsManualSetItemAnimator = this.mIsManualSetItemAnimator;
                configBean.mItemAnimator = this.mItemAnimator;
                return configBean;
            }
        }

        @Override // com.miui.gallery.ui.album.common.base.BaseViewBean, java.util.Comparator
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass() && super.equals(obj)) {
                return Objects.equals(this.mDatas, ((ConfigBean) obj).mDatas);
            }
            return false;
        }

        @Override // com.miui.gallery.ui.album.common.base.BaseViewBean
        public int hashCode() {
            return Objects.hash(Integer.valueOf(super.hashCode()), this.mDatas);
        }
    }

    /* loaded from: classes2.dex */
    public static class VH extends BaseGalleryViewHolder {
        public BasicRecyclerView mRecyclerView;

        public VH(View view) {
            super(view);
            this.mRecyclerView = (BasicRecyclerView) findViewById(R.id.recycle_view);
        }

        public BasicRecyclerView getRecyclerView() {
            return this.mRecyclerView;
        }

        public GallerySimpleEpoxyAdapter<BaseViewBean> getAdapter() {
            if (this.mRecyclerView.getAdapter() instanceof GallerySimpleEpoxyAdapter) {
                return (GallerySimpleEpoxyAdapter) this.mRecyclerView.getAdapter();
            }
            return null;
        }
    }
}
