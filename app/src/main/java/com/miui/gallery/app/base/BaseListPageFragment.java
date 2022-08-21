package com.miui.gallery.app.base;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.HeaderAndFootersEpoxyAdapter;
import com.miui.gallery.R;
import com.miui.gallery.adapter.GallerySimpleEpoxyAdapter;
import com.miui.gallery.adapter.itemmodel.DefaultEmptyPageItemModel;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import com.miui.gallery.widget.EmptyPageWithoutSBL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public abstract class BaseListPageFragment<T, P extends IPresenter> extends BaseFragment<P> {
    public boolean isNeedRestoreRecyclerViewPadding;
    public GallerySimpleEpoxyAdapter<T> mAdapter;
    public EpoxyModel mCurrentEmptyPageModel;
    public EmptyPageWithoutSBL.EmptyConfig mEmptyConfig;
    public boolean mLastClickPadding;
    public View mLoadingView;
    public RecyclerView mRecyclerView;
    public int[] mRecyclerViewPaddings;

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment
    public int getLayoutId() {
        return R.layout.sample_list_page;
    }

    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return null;
    }

    public abstract void initRecyclerView(RecyclerView recyclerView);

    public boolean shouldResetRecyclerViewStatusWhenShowEmptyView() {
        return true;
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment
    public void initView(View view, Bundle bundle, View view2) {
        RecyclerView findRecyclerView = findRecyclerView();
        this.mRecyclerView = findRecyclerView;
        if (findRecyclerView == null) {
            throw new IllegalStateException("can't find recycleView from layout");
        }
        GallerySimpleEpoxyAdapter<T> gallerySimpleEpoxyAdapter = new GallerySimpleEpoxyAdapter<>(ThreadManager.getExecutor(79), null);
        this.mAdapter = gallerySimpleEpoxyAdapter;
        initConfigAdapter(gallerySimpleEpoxyAdapter);
        this.mEmptyConfig = new EmptyPageWithoutSBL.EmptyConfig().setBackground(R.drawable.window_background).setIcon(R.drawable.ic_pic_empty_albums).disableActionButton();
        this.mRecyclerView.setAdapter(this.mAdapter);
        initRecyclerView(this.mRecyclerView);
    }

    public void initConfigAdapter(GallerySimpleEpoxyAdapter<T> gallerySimpleEpoxyAdapter) {
        LayoutInflater from;
        if (Build.VERSION.SDK_INT >= 26) {
            from = getLayoutInflater();
        } else {
            from = LayoutInflater.from(getContext());
        }
        LayoutInflater cloneInContext = from.cloneInContext(getContext());
        try {
            cloneInContext.setFactory2(GalleryViewCreator.getAlbumPageViewFactory());
        } catch (Exception e) {
            DefaultLogger.e("BaseListPageFragment", e);
        }
        gallerySimpleEpoxyAdapter.setLayoutInflate(cloneInContext);
    }

    public RecyclerView findRecyclerView() {
        return (RecyclerView) findViewById(R.id.recyclerViewList);
    }

    public void addData(T t) {
        addDatas(Collections.singletonList(t));
    }

    public void addDatas(List list) {
        addDatas(getDataSize(), list);
    }

    public void addDatas(final int i, final List list) {
        if (Objects.isNull(list) || Objects.isNull(this.mAdapter) || Objects.isNull(this.mRecyclerView)) {
            return;
        }
        if (i < 0 || list.isEmpty()) {
            DefaultLogger.e("BaseListPageFragment", "addDatas() Parameter datas cannot be empty");
        } else if (isEmptyDatas()) {
            setDatas(list);
        } else {
            goneProgress();
            if (isComputingLayout()) {
                postRunnableToRecycleView(new Runnable() { // from class: com.miui.gallery.app.base.BaseListPageFragment.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BaseListPageFragment.this.addDatas(i, list);
                    }
                });
            } else {
                this.mAdapter.addDatas(i, list);
            }
        }
    }

    public void updateDataByIdIfNeed(long j, T t) {
        int findDataIndexById;
        if (Objects.isNull(t) || Objects.isNull(this.mAdapter) || Objects.isNull(this.mRecyclerView) || -1 == (findDataIndexById = findDataIndexById(j)) || getData(findDataIndexById).equals(t)) {
            return;
        }
        updateData(findDataIndexById, t);
    }

    public void updateDataById(long j, T t) {
        int findDataIndexById;
        if (Objects.isNull(t) || Objects.isNull(this.mAdapter) || Objects.isNull(this.mRecyclerView) || -1 == (findDataIndexById = findDataIndexById(j))) {
            return;
        }
        updateData(findDataIndexById, t);
    }

    public void updateData(final int i, final T t) {
        if (Objects.isNull(t) || Objects.isNull(this.mAdapter) || Objects.isNull(this.mRecyclerView)) {
            return;
        }
        if (isComputingLayout()) {
            postRunnableToRecycleView(new Runnable() { // from class: com.miui.gallery.app.base.BaseListPageFragment.3
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    BaseListPageFragment.this.updateData(i, t);
                }
            });
        } else {
            this.mAdapter.updateData(i, t);
        }
    }

    public void setDatas(List list) {
        setDatas(list, false);
    }

    public void setDatas(List list, boolean z) {
        setDatasAndModels(list, null, z);
    }

    public void setDatasAndModels(List<T> list, List<EpoxyModel<?>> list2, boolean z) {
        setDatasAndModels(list, list2, z, null);
    }

    public void setDatasAndModels(final List<T> list, final List<EpoxyModel<?>> list2, final boolean z, final Runnable runnable) {
        if (Objects.isNull(this.mAdapter) || Objects.isNull(this.mRecyclerView)) {
            return;
        }
        if (isEmptyDatas() && !Objects.isNull(list) && !list.isEmpty()) {
            installItemDecoration(true);
        }
        goneProgress();
        if (isComputingLayout()) {
            postRunnableToRecycleView(new Runnable() { // from class: com.miui.gallery.app.base.BaseListPageFragment.4
                @Override // java.lang.Runnable
                public void run() {
                    BaseListPageFragment.this.setDatasAndModels(list, list2, z, runnable);
                }
            });
        } else {
            this.mAdapter.setDatasAndModels(list, list2, z, runnable);
        }
    }

    public void removeDatas(final List<T> list) {
        if (Objects.isNull(this.mAdapter) || Objects.isNull(this.mRecyclerView) || Objects.isNull(list)) {
            return;
        }
        if (isComputingLayout()) {
            postRunnableToRecycleView(new Runnable() { // from class: com.miui.gallery.app.base.BaseListPageFragment.7
                @Override // java.lang.Runnable
                public void run() {
                    BaseListPageFragment.this.removeDatas(list);
                }
            });
        } else {
            this.mAdapter.removeDatas(list);
        }
    }

    public boolean moveData(int i, int i2) {
        return moveData(i, i2, true);
    }

    public boolean moveData(int i, int i2, boolean z) {
        GallerySimpleEpoxyAdapter<T> gallerySimpleEpoxyAdapter;
        if (isEmptyDatas() || (gallerySimpleEpoxyAdapter = this.mAdapter) == null) {
            return false;
        }
        return gallerySimpleEpoxyAdapter.moveItem(i, i2, z);
    }

    @Deprecated
    public T findDataById(long j) {
        GallerySimpleEpoxyAdapter<T> gallerySimpleEpoxyAdapter = this.mAdapter;
        if (gallerySimpleEpoxyAdapter != null && !gallerySimpleEpoxyAdapter.getDatas().isEmpty()) {
            List<T> diffingDatas = this.mAdapter.getDiffingDatas();
            int indexOf = diffingDatas != null ? diffingDatas.indexOf(new BaseViewBean(j)) : -1;
            if (-1 == indexOf) {
                indexOf = this.mAdapter.getDatas().indexOf(new BaseViewBean(j));
            }
            if (-1 != indexOf) {
                return this.mAdapter.getData(indexOf);
            }
        }
        return null;
    }

    @Deprecated
    public int findDataIndexById(long j) {
        GallerySimpleEpoxyAdapter<T> gallerySimpleEpoxyAdapter = this.mAdapter;
        if (gallerySimpleEpoxyAdapter == null) {
            return -1;
        }
        List<T> datas = gallerySimpleEpoxyAdapter.getDatas();
        if (!datas.isEmpty()) {
            return datas.indexOf(new BaseViewBean(j));
        }
        return -1;
    }

    public List getDatas() {
        GallerySimpleEpoxyAdapter<T> gallerySimpleEpoxyAdapter = this.mAdapter;
        if (gallerySimpleEpoxyAdapter == null) {
            return null;
        }
        return gallerySimpleEpoxyAdapter.getDatas();
    }

    public int getDataSize() {
        GallerySimpleEpoxyAdapter<T> gallerySimpleEpoxyAdapter = this.mAdapter;
        if (gallerySimpleEpoxyAdapter == null) {
            return 0;
        }
        return gallerySimpleEpoxyAdapter.getItemCount();
    }

    public boolean isEmptyDatas() {
        return this.mAdapter.isEmpty();
    }

    public T getData(int i) {
        GallerySimpleEpoxyAdapter<T> gallerySimpleEpoxyAdapter = this.mAdapter;
        if (gallerySimpleEpoxyAdapter == null || gallerySimpleEpoxyAdapter.getDatas().isEmpty() || getDataSize() <= i) {
            return null;
        }
        return this.mAdapter.getDatas().get(i);
    }

    public final void installItemDecoration(boolean z) {
        RecyclerView.ItemDecoration[] recyclerViewDecorations;
        if ((z || this.mRecyclerView.getItemDecorationCount() <= 0) && (recyclerViewDecorations = getRecyclerViewDecorations()) != null) {
            for (RecyclerView.ItemDecoration itemDecoration : recyclerViewDecorations) {
                addItemDecoration(itemDecoration);
            }
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView == null || itemDecoration == null) {
            return;
        }
        recyclerView.removeItemDecoration(itemDecoration);
        this.mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void clearItemDecoration() {
        if (this.mRecyclerView == null) {
            return;
        }
        while (this.mRecyclerView.getItemDecorationCount() > 0) {
            this.mRecyclerView.removeItemDecorationAt(0);
        }
        this.mRecyclerView.invalidateItemDecorations();
    }

    public void setRecyclerviewPadding(int i, int i2, int i3, int i4) {
        this.mRecyclerViewPaddings = new int[]{i, i2, i3, i4};
        this.mRecyclerView.setPadding(i, i2, i3, i4);
    }

    public int[] getRecyclerViewPadding() {
        return this.mRecyclerViewPaddings;
    }

    public void goneProgress() {
        View view = this.mLoadingView;
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        this.mLoadingView.setVisibility(8);
    }

    public void setLoadingPage(int i) {
        ViewStub viewStub = (ViewStub) findViewById(R.id.progress);
        viewStub.setLayoutResource(i);
        this.mLoadingView = viewStub.inflate();
    }

    public void setEmptyPage(int i, EmptyPageWithoutSBL.EmptyConfig emptyConfig) {
        if (emptyConfig == null) {
            return;
        }
        internalSetEmptyPage(new DefaultEmptyPageItemModel(new DefaultEmptyPageItemModel.DefaultEmptyPageBean(i, emptyConfig)));
    }

    public void invalidateItemDecorations() {
        if (this.mRecyclerView == null) {
            return;
        }
        postRunnableToRecycleView(new Runnable() { // from class: com.miui.gallery.app.base.BaseListPageFragment.8
            @Override // java.lang.Runnable
            public void run() {
                BaseListPageFragment.this.mRecyclerView.invalidateItemDecorations();
            }
        });
    }

    public void setEmptyPage(int i) {
        if (i == 0) {
            return;
        }
        internalSetEmptyPage(new DefaultEmptyPageItemModel(new DefaultEmptyPageItemModel.DefaultEmptyPageBean(i)));
    }

    public final void internalSetEmptyPage(EpoxyModel epoxyModel) {
        GallerySimpleEpoxyAdapter<T> gallerySimpleEpoxyAdapter = this.mAdapter;
        this.mCurrentEmptyPageModel = epoxyModel;
        gallerySimpleEpoxyAdapter.setEmptyViewModel(epoxyModel);
        this.mAdapter.setEmptyModelStatusChangeListener(new HeaderAndFootersEpoxyAdapter.onEmptyModelStatusChangeListener() { // from class: com.miui.gallery.app.base.BaseListPageFragment.9
            @Override // com.miui.epoxy.HeaderAndFootersEpoxyAdapter.onEmptyModelStatusChangeListener
            public void onChange(boolean z) {
                if (BaseListPageFragment.this.shouldResetRecyclerViewStatusWhenShowEmptyView()) {
                    if (!z) {
                        if (BaseListPageFragment.this.mRecyclerViewPaddings != null && BaseListPageFragment.this.isNeedRestoreRecyclerViewPadding) {
                            BaseListPageFragment baseListPageFragment = BaseListPageFragment.this;
                            baseListPageFragment.mRecyclerView.setPadding(baseListPageFragment.mRecyclerViewPaddings[0], BaseListPageFragment.this.mRecyclerViewPaddings[1], BaseListPageFragment.this.mRecyclerViewPaddings[2], BaseListPageFragment.this.mRecyclerViewPaddings[3]);
                            BaseListPageFragment baseListPageFragment2 = BaseListPageFragment.this;
                            baseListPageFragment2.mRecyclerView.setClipToPadding(baseListPageFragment2.mLastClickPadding);
                            BaseListPageFragment.this.installItemDecoration(false);
                            BaseListPageFragment.this.isNeedRestoreRecyclerViewPadding = false;
                        }
                    } else {
                        BaseListPageFragment baseListPageFragment3 = BaseListPageFragment.this;
                        baseListPageFragment3.mLastClickPadding = baseListPageFragment3.mRecyclerView.getClipToPadding();
                        BaseListPageFragment.this.mRecyclerView.setClipToPadding(true);
                        BaseListPageFragment.this.isNeedRestoreRecyclerViewPadding = true;
                    }
                    if (!z) {
                        return;
                    }
                    BaseListPageFragment.this.mRecyclerView.post(new Runnable() { // from class: com.miui.gallery.app.base.BaseListPageFragment.9.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BaseListPageFragment.this.mRecyclerView.suppressLayout(true);
                            BaseListPageFragment.this.mRecyclerView.setPadding(0, 0, 0, 0);
                            BaseListPageFragment.this.clearItemDecoration();
                            BaseListPageFragment.this.mRecyclerView.suppressLayout(false);
                        }
                    });
                }
            }
        });
    }

    public EmptyPageWithoutSBL.EmptyConfig getDefaultEmptyConfig() {
        return this.mEmptyConfig;
    }

    public boolean isComputingLayout() {
        RecyclerView recyclerView = this.mRecyclerView;
        return recyclerView != null && recyclerView.isComputingLayout();
    }

    public void postRunnableToRecycleView(Runnable runnable) {
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView == null) {
            return;
        }
        recyclerView.post(runnable);
    }

    public GallerySimpleEpoxyAdapter getSourceAdapter() {
        return this.mAdapter;
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null) {
            recyclerView.addOnScrollListener(onScrollListener);
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null) {
            recyclerView.clearOnScrollListeners();
        }
    }
}
