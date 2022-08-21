package com.miui.gallery.editor.photo.app.filter;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.editor.photo.app.CategoryAdapter;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorCheckHelper;
import com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorDownloadStateListener;
import com.miui.gallery.editor.photo.app.filter.res.FilterResourceFetcher;
import com.miui.gallery.editor.photo.app.menu.FilterView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.FilterCategory;
import com.miui.gallery.editor.photo.core.common.model.FilterData;
import com.miui.gallery.editor.photo.core.imports.filter.FilterItem;
import com.miui.gallery.editor.photo.core.imports.filter.FilterRenderFragment;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.editor.photo.utils.ScreenAdaptationHelper;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.EditorThreadPoolUtils;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.bokeh.MiPortraitSegmenter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class FilterFragment extends MenuFragment<AbstractEffectFragment, SdkProvider<FilterCategory, AbstractEffectFragment>> {
    public List<FilterCategory> mCategories;
    public FilterAdapter mFilterAdapter;
    public Map<Integer, List<? extends FilterData>> mFilterMap;
    public FilterAdapter mFilterPortraitAdapter;
    public CategoryAdapter mHeaderAdapter;
    public MiPortraitSegmenter.IPortraitSegmentListener mIPortraitSegmentListener;
    public Disposable mInitDataDisposable;
    public boolean mIsItemClick;
    public FrameLayout mLayoutBottomArea;
    public FrameLayout mLyaoutContentArea;
    public OnItemClickListener mOnHeaderItemClickListener;
    public OnItemClickListener mOnItemClickListener;
    public RecyclerView.OnScrollListener mOnScrollListener;
    public PortraitColorDownloadStateListener mPortraitColorDownloadStateListener;
    public boolean mPortraitColorDownloading;
    public int mPortraitColorItemPosition;
    public SimpleRecyclerView mRecyclerView;
    public BubbleSeekBar mSeekBar;
    public BubbleSeekBar.ProgressListener mSeekBarChangeListener;
    public FilterData mSelectedItem;
    public SimpleRecyclerView mSimpleRecyclerView;
    public LinearLayout mTopPanel;
    public Scheduler mWorkerScheduler;

    public FilterFragment() {
        super(Effect.FILTER);
        this.mPortraitColorItemPosition = -1;
        this.mFilterMap = new HashMap();
        this.mWorkerScheduler = Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR);
        this.mOnScrollListener = new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.editor.photo.app.filter.FilterFragment.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (FilterFragment.this.mIsItemClick) {
                    FilterFragment.this.mIsItemClick = false;
                    return;
                }
                FilterFragment.this.setSelectedTabPosition(((LinearLayoutManager) FilterFragment.this.mSimpleRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
            }
        };
        this.mOnHeaderItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.filter.FilterFragment.5
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                recyclerView.smoothScrollToPosition(i);
                FilterFragment.this.mHeaderAdapter.setSelection(i);
                FilterFragment.this.setSelectedTabWithOffset(i);
                if (FilterFragment.this.mPortraitColorDownloading) {
                    FilterFragment.this.setPortraitColorDataDownloadState(18);
                    return true;
                }
                return true;
            }
        };
        this.mPortraitColorDownloadStateListener = new AnonymousClass6();
        this.mIPortraitSegmentListener = new MiPortraitSegmenter.IPortraitSegmentListener() { // from class: com.miui.gallery.editor.photo.app.filter.FilterFragment.7
            @Override // com.xiaomi.bokeh.MiPortraitSegmenter.IPortraitSegmentListener
            public void segment(boolean z) {
                FilterAdapter portraitAdapter;
                ((FilterRenderFragment) FilterFragment.this.getRenderFragment()).hideProgressView();
                if (FilterFragment.this.mPortraitColorItemPosition <= 0 || (portraitAdapter = FilterFragment.this.getPortraitAdapter()) == null) {
                    return;
                }
                FilterFragment filterFragment = FilterFragment.this;
                filterFragment.doPortraitColor((FilterItem) portraitAdapter.getItemData(FilterFragment.this.mPortraitColorItemPosition), portraitAdapter, filterFragment.mPortraitColorItemPosition, z);
            }
        };
        this.mSeekBarChangeListener = new BubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.editor.photo.app.filter.FilterFragment.8
            public boolean mImmersive;

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressStartChange(BubbleSeekBar bubbleSeekBar, int i) {
                EditorMiscHelper.enterImmersive(FilterFragment.this.mLyaoutContentArea, FilterFragment.this.mLayoutBottomArea);
                ((AbstractEffectFragment) FilterFragment.this.getRenderFragment()).enterImmersive();
                this.mImmersive = true;
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
                if (i == 0 || i == 100) {
                    LinearMotorHelper.performHapticFeedback(FilterFragment.this.mSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                }
                if (FilterFragment.this.mSeekBar.getVisibility() != 0) {
                    return;
                }
                FilterFragment.this.doConfig(i);
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
                if (this.mImmersive) {
                    ((AbstractEffectFragment) FilterFragment.this.getRenderFragment()).exitImmersive();
                    EditorMiscHelper.exitImmersive(FilterFragment.this.mLyaoutContentArea, FilterFragment.this.mLayoutBottomArea);
                    this.mImmersive = false;
                    if (FilterFragment.this.mSelectedItem == null || FilterFragment.this.mSelectedItem.isNone()) {
                        return;
                    }
                    FilterFragment.this.mSeekBar.setContentDescription(String.format("%s%s%d", FilterFragment.this.mSelectedItem.name, FilterFragment.this.getResources().getString(R.string.vlog_talkback_filter_seekbar), Integer.valueOf(FilterFragment.this.mSelectedItem.progress)));
                }
            }
        };
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.filter.FilterFragment.9
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            @SuppressLint({"CheckResult"})
            public boolean OnItemClick(RecyclerView recyclerView, View view, final int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                FilterFragment.this.setSelectedTabOnClick(recyclerView, i);
                FilterRenderFragment filterRenderFragment = (FilterRenderFragment) FilterFragment.this.getRenderFragment();
                filterRenderFragment.hideProgressView();
                final FilterAdapter currentAdapter = FilterFragment.this.getCurrentAdapter();
                final FilterItem filterItem = (FilterItem) currentAdapter.getItemData(i);
                if (filterItem == null) {
                    DefaultLogger.e("FilterFragment", "FilterAdapter get filterData null:pos is %d", Integer.valueOf(i));
                    return true;
                }
                if (PortraitColorCheckHelper.isPortraitEnable() && !filterItem.isPortraitColor()) {
                    MiPortraitSegmenter.getInstance().setSegmentListener(null);
                    MiPortraitSegmenter.getInstance().setEnhanceListener(null);
                }
                if (filterItem.equals(FilterFragment.this.mSelectedItem)) {
                    if (currentAdapter.isEditable(i)) {
                        if (currentAdapter.isInEditMode()) {
                            currentAdapter.exitEditMode();
                            FilterFragment.this.showTopPanel(false);
                        } else if (!filterItem.isNone()) {
                            currentAdapter.enterEditMode();
                            FilterFragment.this.mSeekBar.setCurrentProgress(filterItem.progress);
                            FilterFragment.this.showTopPanel(true);
                        }
                    }
                } else {
                    if (filterItem.isNone() && currentAdapter.isInEditMode()) {
                        currentAdapter.exitEditMode();
                        FilterFragment.this.showTopPanel(false);
                    }
                    if (!filterItem.isNone()) {
                        FilterFragment.this.sampleItemClick(filterItem);
                    }
                    if (filterItem.isPortraitColor()) {
                        if (PortraitColorCheckHelper.getInstance().isNeedDownload()) {
                            DefaultLogger.d("FilterFragment", "the sdk is need download.");
                            PortraitColorCheckHelper.getInstance().startDownloadWithCheck(FilterFragment.this.getActivity());
                            return false;
                        } else if (PortraitColorCheckHelper.getInstance().isDownloading()) {
                            DefaultLogger.d("FilterFragment", "the sdk is downloading.");
                            FilterFragment.this.setPortraitColorDataDownloadState(18);
                            return false;
                        } else {
                            DefaultLogger.d("FilterFragment", "the sdk is downloaded.");
                            FilterFragment.this.mPortraitColorItemPosition = i;
                            filterRenderFragment.showProgressView(0L);
                            MiPortraitSegmenter.getInstance().waitSegment(filterRenderFragment.getBitmap(), FilterFragment.this.mIPortraitSegmentListener);
                            return true;
                        }
                    } else if (filterItem.isBuiltIn()) {
                        FilterFragment.this.doSelectRender(filterItem, currentAdapter, i);
                    } else {
                        int i2 = filterItem.state;
                        if (i2 == 17 || i2 == 0) {
                            FilterFragment.this.doSelectRender(filterItem, currentAdapter, i);
                        } else if (i2 == 19 || i2 == 20) {
                            filterItem.state = 18;
                            FilterResourceFetcher.INSTANCE.checkFetch(FilterFragment.this.getActivity(), filterItem, new Request.Listener() { // from class: com.miui.gallery.editor.photo.app.filter.FilterFragment.9.1
                                @Override // com.miui.gallery.net.fetch.Request.Listener
                                public void onStart() {
                                    filterItem.state = 18;
                                    currentAdapter.notifyItemChanged(i);
                                }

                                @Override // com.miui.gallery.net.fetch.Request.Listener
                                public void onSuccess() {
                                    filterItem.state = 0;
                                    currentAdapter.notifyItemChanged(i);
                                    FilterFragment.this.doSelectRender(filterItem, currentAdapter, i);
                                }

                                @Override // com.miui.gallery.net.fetch.Request.Listener
                                public void onFail() {
                                    filterItem.state = 20;
                                    currentAdapter.notifyItemChanged(i);
                                }
                            });
                        }
                    }
                }
                return true;
            }
        };
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ArrayList arrayList = new ArrayList();
        this.mCategories = arrayList;
        arrayList.addAll(this.mSdkProvider.list());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new FilterView(getContext()), (ViewGroup) getView(), true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FilterView filterView = new FilterView(viewGroup.getContext());
        this.mTopPanel = (LinearLayout) filterView.findViewById(R.id.top_panel);
        this.mLyaoutContentArea = (FrameLayout) filterView.findViewById(R.id.layout_content_area);
        this.mLayoutBottomArea = (FrameLayout) filterView.findViewById(R.id.layout_bottom_area);
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) filterView.findViewById(R.id.bubble_seekbar);
        this.mSeekBar = bubbleSeekBar;
        bubbleSeekBar.setProgressListener(this.mSeekBarChangeListener);
        this.mSeekBar.setMaxProgress(100);
        this.mSeekBar.setContentDescription(getResources().getString(R.string.photo_editor_talkback_seekbar));
        this.mRecyclerView = (SimpleRecyclerView) filterView.findViewById(R.id.filter_sky_type_list);
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) filterView.findViewById(R.id.recycler_view);
        this.mSimpleRecyclerView = simpleRecyclerView;
        simpleRecyclerView.setEnableItemClickWhileSettling(true);
        initFilterData();
        if (PortraitColorCheckHelper.isPortraitEnable() && !PortraitColorCheckHelper.isPortraitColorAvailable()) {
            setPortraitColorDataDownloadState(18);
            PortraitColorCheckHelper.getInstance().setDownloadStateListener(this.mPortraitColorDownloadStateListener);
            PortraitColorCheckHelper.getInstance().startDownloadWithWifi();
        }
        return filterView;
    }

    public final void initView() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), this.mCategories);
        this.mHeaderAdapter = categoryAdapter;
        this.mRecyclerView.setAdapter(categoryAdapter);
        this.mHeaderAdapter.setOnItemClickListener(this.mOnHeaderItemClickListener);
        FilterCategory filterCategory = this.mCategories.get(0);
        ArrayList arrayList = new ArrayList();
        for (Integer num : this.mFilterMap.keySet()) {
            arrayList.addAll(this.mFilterMap.get(num));
        }
        this.mFilterPortraitAdapter = new FilterPortraitColorAdapter(arrayList, filterCategory.subHighlighColor, filterCategory.subItemSize);
        FilterAdapter filterAdapter = new FilterAdapter(arrayList, filterCategory.subHighlighColor, filterCategory.subItemSize);
        this.mFilterAdapter = filterAdapter;
        filterAdapter.setOnItemClickListener(this.mOnItemClickListener);
        FilterAdapter filterAdapter2 = this.mFilterAdapter;
        filterAdapter2.setSelection(findSelected(filterAdapter2));
        this.mSimpleRecyclerView.setAdapter(getCurrentAdapter());
        this.mSimpleRecyclerView.addItemDecoration(new EditorBlankDivider(getResources().getDimensionPixelSize(R.dimen.filter_mentu_item_margin_start)));
        this.mSimpleRecyclerView.addOnScrollListener(this.mOnScrollListener);
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mSeekBar);
    }

    public final void initFilterData() {
        this.mInitDataDisposable = Observable.create(new ObservableOnSubscribe<Object>() { // from class: com.miui.gallery.editor.photo.app.filter.FilterFragment.4
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                for (FilterCategory filterCategory : FilterFragment.this.mCategories) {
                    FilterFragment.this.mFilterMap.put(Integer.valueOf(filterCategory.getFilterCategory()), filterCategory.getFilterDatas());
                    FilterResourceFetcher.INSTANCE.getDownloadStatusData((List) FilterFragment.this.mFilterMap.get(Integer.valueOf(filterCategory.getFilterCategory())));
                }
                observableEmitter.onNext(new Object());
                observableEmitter.onComplete();
            }
        }).subscribeOn(this.mWorkerScheduler).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() { // from class: com.miui.gallery.editor.photo.app.filter.FilterFragment.2
            @Override // io.reactivex.functions.Consumer
            public void accept(Object obj) throws Exception {
                FilterFragment.this.initView();
            }
        }, new Consumer<Throwable>() { // from class: com.miui.gallery.editor.photo.app.filter.FilterFragment.3
            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) throws Exception {
                th.printStackTrace();
            }
        });
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public final void setSelectedTabWithOffset(int i) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_category_gap);
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            List<? extends FilterData> list = this.mFilterMap.get(Integer.valueOf(this.mCategories.get(i3).getFilterCategory()));
            if (list != null) {
                i2 += list.size();
            }
        }
        ((LinearLayoutManager) this.mSimpleRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i2, dimensionPixelSize);
    }

    /* renamed from: com.miui.gallery.editor.photo.app.filter.FilterFragment$6  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements PortraitColorDownloadStateListener {
        public AnonymousClass6() {
        }

        @Override // com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorDownloadStateListener
        public void onDownloadStart() {
            DefaultLogger.d("FilterFragment", "portrait start download.");
            FilterFragment.this.setPortraitColorDataDownloadState(18);
        }

        @Override // com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorDownloadStateListener
        public void onDownloading(int i) {
            DefaultLogger.d("FilterFragment", "portrait  download  %s.", Integer.valueOf(i));
            if (!FilterFragment.this.mPortraitColorDownloading) {
                FilterFragment.this.setPortraitColorDataDownloadState(18);
            }
        }

        @Override // com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorDownloadStateListener
        public void onFinish(boolean z, int i) {
            DefaultLogger.d("FilterFragment", "portrait download end %s.", Boolean.valueOf(z));
            if (z) {
                FilterFragment.this.setPortraitColorDataDownloadState(0);
                FilterFragment.this.getHostAbility().showInnerToast(FilterFragment.this.getString(R.string.photo_portrait_color_download_success));
                return;
            }
            FilterFragment.this.setPortraitColorDataDownloadState(20);
            if (i == 101) {
                AgreementsUtils.showUserAgreements(FilterFragment.this.getActivity(), FilterFragment$6$$ExternalSyntheticLambda0.INSTANCE);
            } else {
                ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.photo_portrait_color_download_fail);
            }
        }

        public static /* synthetic */ void lambda$onFinish$0(boolean z) {
            if (z) {
                PortraitColorCheckHelper.getInstance().startDownloadWithWifi();
            }
        }
    }

    public void setPortraitColorDataDownloadState(int i) {
        FilterData portraitColorData;
        FilterAdapter portraitAdapter = getPortraitAdapter();
        if (portraitAdapter == null || (portraitColorData = ((FilterPortraitColorAdapter) portraitAdapter).getPortraitColorData()) == null) {
            return;
        }
        portraitColorData.state = i;
        this.mPortraitColorDownloading = i == 18;
        portraitAdapter.notifyDataSetChanged();
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        getHostAbility().hideInnerToast();
        if (PortraitColorCheckHelper.isPortraitEnable()) {
            PortraitColorCheckHelper.getInstance().release();
        }
        disposeDisposable(this.mInitDataDisposable);
        FilterResourceFetcher.INSTANCE.cancelAll();
    }

    public static void disposeDisposable(Disposable disposable) {
        if (disposable == null || disposable.isDisposed()) {
            return;
        }
        disposable.dispose();
    }

    public final void doRender(FilterData filterData, Object obj) {
        getRenderFragment().clear();
        getRenderFragment().add(filterData, obj);
        getRenderFragment().render();
    }

    public final void doConfig(int i) {
        updateProgressValue(i);
        FilterData filterData = this.mSelectedItem;
        if (filterData != null) {
            filterData.progress = i;
        }
        getRenderFragment().clear();
        getRenderFragment().add(this.mSelectedItem, Integer.valueOf(i));
        getRenderFragment().render();
    }

    public void showTopPanel(boolean z) {
        int i = 8;
        this.mTopPanel.setVisibility(z ? 0 : 8);
        this.mSeekBar.setVisibility(z ? 0 : 8);
        SimpleRecyclerView simpleRecyclerView = this.mRecyclerView;
        if (!z) {
            i = 0;
        }
        simpleRecyclerView.setVisibility(i);
        if (z) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            objectAnimator.setTarget(this.mTopPanel);
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, getResources().getDimension(R.dimen.photo_editor_filter_tab_offset), 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            objectAnimator.setDuration(getResources().getInteger(R.integer.photo_editor_filter_tab_appear_duration));
            objectAnimator.start();
            ObjectAnimator objectAnimator2 = new ObjectAnimator();
            objectAnimator2.setTarget(this.mRecyclerView);
            objectAnimator2.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f, -getResources().getDimension(R.dimen.photo_editor_filter_tab_offset)), PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f));
            objectAnimator2.setInterpolator(new CubicEaseOutInterpolator());
            objectAnimator2.setDuration(getResources().getInteger(R.integer.photo_editor_filter_tab_disappear_duration));
            objectAnimator2.start();
            return;
        }
        ObjectAnimator objectAnimator3 = new ObjectAnimator();
        objectAnimator3.setTarget(this.mRecyclerView);
        objectAnimator3.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -getResources().getDimension(R.dimen.photo_editor_filter_tab_offset), 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
        objectAnimator3.setInterpolator(new CubicEaseOutInterpolator());
        objectAnimator3.setDuration(getResources().getInteger(R.integer.photo_editor_filter_tab_appear_duration));
        objectAnimator3.start();
        ObjectAnimator objectAnimator4 = new ObjectAnimator();
        objectAnimator4.setTarget(this.mTopPanel);
        objectAnimator4.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f, getResources().getDimension(R.dimen.photo_editor_filter_tab_offset)), PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f));
        objectAnimator4.setInterpolator(new CubicEaseOutInterpolator());
        objectAnimator4.setDuration(getResources().getInteger(R.integer.photo_editor_filter_tab_disappear_duration));
        objectAnimator4.start();
    }

    public void updateProgressValue(int i) {
        FilterAdapter currentAdapter = getCurrentAdapter();
        if (currentAdapter == null || !currentAdapter.isInEditMode()) {
            return;
        }
        currentAdapter.update(i);
    }

    public final void clearOtherSelector(int i) {
        FilterAdapter currentAdapter = getCurrentAdapter();
        if (currentAdapter == null || this.mSelectedItem == null) {
            return;
        }
        currentAdapter.setSelection(i);
    }

    public final FilterAdapter getCurrentAdapter() {
        if (this.mCategories.get(0).getFilterCategory() == 3) {
            return this.mFilterPortraitAdapter;
        }
        return this.mFilterAdapter;
    }

    public final FilterAdapter getPortraitAdapter() {
        return this.mFilterPortraitAdapter;
    }

    public final int findSelected(FilterAdapter filterAdapter) {
        FilterData filterData = this.mSelectedItem;
        if (filterData == null || filterData.isNone()) {
            return 0;
        }
        for (int i = 0; i < filterAdapter.getItemCount(); i++) {
            if (filterAdapter.getItemData(i).equals(this.mSelectedItem)) {
                filterAdapter.getItemData(i).progress = this.mSelectedItem.progress;
                return i;
            }
        }
        return -1;
    }

    public final void setSelectedTabOnClick(RecyclerView recyclerView, int i) {
        this.mIsItemClick = true;
        recyclerView.smoothScrollToPosition(i);
        setSelectedTabPosition(i);
    }

    public final void setSelectedTabPosition(int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < this.mCategories.size(); i3++) {
            List<? extends FilterData> list = this.mFilterMap.get(Integer.valueOf(this.mCategories.get(i3).getFilterCategory()));
            if (list != null && i < (i2 = i2 + list.size())) {
                this.mHeaderAdapter.setSelection(i3);
                this.mRecyclerView.setSpringEnabled(false);
                this.mRecyclerView.smoothScrollToPosition(i3);
                return;
            }
        }
    }

    public final void sampleItemClick(FilterData filterData) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, filterData.name);
        SamplingStatHelper.recordCountEvent("photo_editor", "sky_filter_click", hashMap);
    }

    public final void doPortraitColor(FilterItem filterItem, FilterAdapter filterAdapter, int i, boolean z) {
        if (z) {
            doSelectRender(filterItem, filterAdapter, i);
        } else {
            getHostAbility().showInnerToast(getString(R.string.photo_portrait_color_no_face));
        }
    }

    public final void doSelectRender(FilterItem filterItem, FilterAdapter filterAdapter, int i) {
        doRender(filterItem, Integer.valueOf(filterItem.progress));
        filterAdapter.setSelection(i);
        this.mSelectedItem = filterItem;
        if (!filterAdapter.isEditable(i) && filterAdapter.isInEditMode()) {
            filterAdapter.exitEditMode();
            showTopPanel(false);
        }
        if (filterAdapter.isEditable(i) && filterAdapter.isInEditMode() && !filterItem.isNone()) {
            this.mSeekBar.setCurrentProgress(filterItem.progress);
        }
        clearOtherSelector(i);
    }
}
