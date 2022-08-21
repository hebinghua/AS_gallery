package com.miui.gallery.editor.photo.app.sky;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.CategoryAdapter;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.menu.SkyView;
import com.miui.gallery.editor.photo.app.sky.SkyFragment;
import com.miui.gallery.editor.photo.app.sky.res.ResourceFetcher;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderFragment;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.SkyCategory;
import com.miui.gallery.editor.photo.core.common.model.SkyData;
import com.miui.gallery.editor.photo.core.imports.sky.SkyCategoryImpl;
import com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.editor.photo.utils.ScreenAdaptationHelper;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.EditorThreadPoolUtils;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.skytransfer.SkyTranFilter;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import miuix.appcompat.app.ProgressDialog;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class SkyFragment extends MenuFragment<AbstractEffectFragment, SdkProvider<SkyCategory, AbstractEffectFragment>> {
    public SkyAdapter mAdapter;
    public List<SkyCategoryImpl> mCategories;
    public Handler mHandler;
    public Disposable mInitDataDisposable;
    public boolean mIsItemClick;
    public ItemClickListener mItemClickListener;
    public FrameLayout mLayoutBottomArea;
    public FrameLayout mLyaoutContentArea;
    public RecyclerView.OnScrollListener mOnScrollListener;
    public OnItemClickListener mOnSkyCategoryItemClickLister;
    public ProgressDialog mProgressDialog;
    public ResourceFetcher mResourceFetcher;
    public BubbleSeekBar mSeekBar;
    public BubbleSeekBar.ProgressListener mSeekBarChangeListener;
    public Disposable mSegmentDisposable;
    public CountDownLatch mSegmentLatch;
    public Runnable mSegmentRunnable;
    public SkyData mSelectedData;
    public Runnable mShowLoadingProcessRunnable;
    public SimpleRecyclerView mSimpleRecyclerView;
    public CategoryAdapter mSkyCategoryAdapter;
    public SimpleRecyclerView mSkyCategoryRecyclerView;
    public List<SkyData> mSkyDataList;
    public int mSkySegmentMode;
    public int mSkySegmentStatus;
    public boolean mToClearProgress;
    public LinearLayout mTopPanel;
    public Disposable mWaitSegmentDisposable;
    public Scheduler mWorkerScheduler;

    public static /* synthetic */ void $r8$lambda$WHGDF11AHchiIlYX54VmxktznOU(SkyFragment skyFragment, Integer num) {
        skyFragment.lambda$doSegmentSky$1(num);
    }

    public static /* synthetic */ void $r8$lambda$hBojXGcxXDima3gDWy4CQN8OPmE(SkyFragment skyFragment) {
        skyFragment.showLoadingProcessDialog();
    }

    /* renamed from: $r8$lambda$pMe_FSPVvS4rDq3SqZ5QnPhx-Jo */
    public static /* synthetic */ void m772$r8$lambda$pMe_FSPVvS4rDq3SqZ5QnPhxJo(SkyFragment skyFragment, ObservableEmitter observableEmitter) {
        skyFragment.lambda$doSegmentSky$0(observableEmitter);
    }

    public static /* synthetic */ void lambda$doSegmentSky$2(Throwable th) throws Exception {
    }

    public SkyFragment() {
        super(Effect.SKY);
        this.mWorkerScheduler = Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR);
        this.mItemClickListener = new ItemClickListener();
        this.mHandler = new Handler();
        this.mSegmentLatch = new CountDownLatch(1);
        this.mOnScrollListener = new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment.1
            {
                SkyFragment.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i != 0) {
                    return;
                }
                if (SkyFragment.this.mIsItemClick) {
                    SkyFragment.this.mIsItemClick = false;
                    return;
                }
                SkyFragment.this.setSelectedTabPosition(((LinearLayoutManager) SkyFragment.this.mSimpleRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
            }
        };
        this.mOnSkyCategoryItemClickLister = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment.2
            {
                SkyFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                recyclerView.smoothScrollToPosition(i);
                if (SkyFragment.this.mSkyCategoryAdapter.getSelection() == i) {
                    return true;
                }
                SkyFragment.this.mSkyCategoryAdapter.setSelection(i);
                SkyFragment skyFragment = SkyFragment.this;
                skyFragment.sampleTabClick(String.valueOf(((SkyCategoryImpl) skyFragment.mCategories.get(i)).id));
                SkyFragment.this.setSelectedTabWithOffset(i);
                return true;
            }
        };
        this.mShowLoadingProcessRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                SkyFragment.$r8$lambda$hBojXGcxXDima3gDWy4CQN8OPmE(SkyFragment.this);
            }
        };
        this.mSegmentRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment.6
            {
                SkyFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                SkyFragment.this.doSegmentSky();
            }
        };
        this.mSeekBarChangeListener = new BubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment.7
            public boolean mImmersive;

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
            }

            {
                SkyFragment.this = this;
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressStartChange(BubbleSeekBar bubbleSeekBar, int i) {
                SkyFragment.this.getHostAbility().hideInnerToast();
                EditorMiscHelper.enterImmersive(SkyFragment.this.mLyaoutContentArea, SkyFragment.this.mLayoutBottomArea);
                ((AbstractEffectFragment) SkyFragment.this.getRenderFragment()).enterImmersive();
                this.mImmersive = true;
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
                if (SkyFragment.this.mSeekBar.getVisibility() != 0) {
                    return;
                }
                if (i == 0 || i == 100) {
                    LinearMotorHelper.performHapticFeedback(bubbleSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                }
                if (SkyFragment.this.mToClearProgress) {
                    SkyFragment.this.clearOtherProgress();
                    SkyFragment.this.mToClearProgress = false;
                }
                SkyFragment.this.doProgress(i);
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
                if (this.mImmersive) {
                    EditorMiscHelper.exitImmersive(SkyFragment.this.mLyaoutContentArea, SkyFragment.this.mLayoutBottomArea);
                    ((AbstractEffectFragment) SkyFragment.this.getRenderFragment()).exitImmersive();
                    this.mImmersive = false;
                    if (SkyFragment.this.mSelectedData == null || SkyFragment.this.mSelectedData.isNone()) {
                        return;
                    }
                    SkyFragment.this.mSeekBar.setContentDescription(String.format("%s%s%d", SkyFragment.this.mSelectedData.name, SkyFragment.this.getResources().getString(R.string.photo_editor_talkback_seekbar), Integer.valueOf(SkyFragment.this.mSelectedData.getProgress())));
                }
            }
        };
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        SkyView skyView = new SkyView(layoutInflater.getContext());
        this.mTopPanel = (LinearLayout) skyView.findViewById(R.id.top_panel);
        this.mLyaoutContentArea = (FrameLayout) skyView.findViewById(R.id.layout_content_area);
        this.mLayoutBottomArea = (FrameLayout) skyView.findViewById(R.id.layout_bottom_area);
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) skyView.findViewById(R.id.bubble_seekbar);
        this.mSeekBar = bubbleSeekBar;
        bubbleSeekBar.setProgressListener(this.mSeekBarChangeListener);
        this.mSeekBar.setMaxProgress(100);
        this.mSeekBar.setContentDescription(getResources().getString(R.string.photo_editor_talkback_seekbar));
        this.mSkyCategoryRecyclerView = (SimpleRecyclerView) skyView.findViewById(R.id.filter_sky_type_list);
        this.mSimpleRecyclerView = (SimpleRecyclerView) skyView.findViewById(R.id.recycler_view);
        return skyView;
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new SkyView(getContext()), getView(), true);
    }

    public final void initViewPager() {
        if (!isAdded()) {
            return;
        }
        DefaultLogger.d("SkyFragment", "initViewPager");
        this.mSimpleRecyclerView.setEnableItemClickWhileSettling(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), this.mCategories);
        this.mSkyCategoryAdapter = categoryAdapter;
        categoryAdapter.setOnItemClickListener(this.mOnSkyCategoryItemClickLister);
        this.mSkyCategoryAdapter.setSelection(0);
        this.mSkyCategoryRecyclerView.setAdapter(this.mSkyCategoryAdapter);
        this.mSkyDataList = new ArrayList();
        for (int i = 0; i < this.mCategories.size(); i++) {
            this.mSkyDataList.addAll(this.mCategories.get(i).getDataList());
        }
        SkyAdapter skyAdapter = new SkyAdapter(this.mSkyDataList);
        this.mAdapter = skyAdapter;
        skyAdapter.setOnItemClickListener(this.mItemClickListener);
        this.mSimpleRecyclerView.setAdapter(this.mAdapter);
        this.mSimpleRecyclerView.addItemDecoration(new EditorBlankDivider(getResources().getDimensionPixelSize(R.dimen.filter_mentu_item_margin_start)));
        this.mSimpleRecyclerView.addOnScrollListener(this.mOnScrollListener);
    }

    public final void setSelectedTabWithOffset(int i) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_category_gap);
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            List<SkyData> dataList = this.mCategories.get(i3).getDataList();
            if (dataList != null) {
                i2 += dataList.size();
            }
        }
        ((LinearLayoutManager) this.mSimpleRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i2, dimensionPixelSize);
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mSeekBar);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment
    public void setRenderFragmentInSingleMode(RenderFragment renderFragment) {
        super.setRenderFragmentInSingleMode(renderFragment);
        this.mHandler.postDelayed(this.mSegmentRunnable, MenuFragment.sAnimAppearDuration);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mCategories = SkyCategoryImpl.getCategoryData();
        this.mResourceFetcher = new ResourceFetcher();
        initData();
        if (!this.mSingleEffectMode) {
            this.mHandler.postDelayed(this.mSegmentRunnable, MenuFragment.sAnimAppearDuration);
        }
    }

    public final void initData() {
        this.mHandler.postDelayed(this.mShowLoadingProcessRunnable, 500L);
        this.mInitDataDisposable = Observable.create(new ObservableOnSubscribe<Object>() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment.5
            {
                SkyFragment.this = this;
            }

            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                SkyFragment.this.mResourceFetcher.prepare();
                for (int i = 0; i < SkyFragment.this.mCategories.size(); i++) {
                    SkyFragment.this.mResourceFetcher.addDownloadStatus(((SkyCategoryImpl) SkyFragment.this.mCategories.get(i)).getDataList());
                }
                observableEmitter.onNext(new Object());
                observableEmitter.onComplete();
            }
        }).subscribeOn(this.mWorkerScheduler).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment.3
            {
                SkyFragment.this = this;
            }

            @Override // io.reactivex.functions.Consumer
            public void accept(Object obj) throws Exception {
                SkyFragment.this.hideLoadingProcessDialog();
                SkyFragment.this.initViewPager();
            }
        }, new Consumer<Throwable>() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment.4
            {
                SkyFragment.this = this;
            }

            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) throws Exception {
                SkyFragment.this.hideLoadingProcessDialog();
                th.printStackTrace();
            }
        });
    }

    public final void showLoadingProcessDialog() {
        if (this.mProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            this.mProgressDialog = progressDialog;
            progressDialog.setMessage(getActivity().getString(R.string.remover_menu_processing));
            this.mProgressDialog.setCanceledOnTouchOutside(false);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setIndeterminate(true);
        }
        this.mProgressDialog.show();
    }

    public final void hideLoadingProcessDialog() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
        this.mHandler.removeCallbacks(this.mShowLoadingProcessRunnable);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        getHostAbility().hideInnerToast();
        hideLoadingProcessDialog();
        ResourceFetcher resourceFetcher = this.mResourceFetcher;
        if (resourceFetcher != null) {
            resourceFetcher.release();
        }
        this.mHandler.removeCallbacks(this.mSegmentRunnable);
        disposeDisposable(this.mSegmentDisposable);
        disposeDisposable(this.mWaitSegmentDisposable);
        disposeDisposable(this.mInitDataDisposable);
    }

    public static void disposeDisposable(Disposable disposable) {
        if (disposable == null || disposable.isDisposed()) {
            return;
        }
        disposable.dispose();
    }

    @SuppressLint({"CheckResult"})
    public final void doSegmentSky() {
        if (getPreview() == null) {
            DefaultLogger.d("SkyFragment", "doSkyTransferProcess before load preview");
        } else if (this.mSkySegmentStatus >= 1) {
            DefaultLogger.d("SkyFragment", "SkySegmentStart ready");
        } else {
            this.mSkySegmentStatus = 1;
            disposeDisposable(this.mSegmentDisposable);
            this.mSegmentDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment$$ExternalSyntheticLambda0
                @Override // io.reactivex.ObservableOnSubscribe
                public final void subscribe(ObservableEmitter observableEmitter) {
                    SkyFragment.m772$r8$lambda$pMe_FSPVvS4rDq3SqZ5QnPhxJo(SkyFragment.this, observableEmitter);
                }
            }).subscribeOn(this.mWorkerScheduler).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment$$ExternalSyntheticLambda1
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    SkyFragment.$r8$lambda$WHGDF11AHchiIlYX54VmxktznOU(SkyFragment.this, (Integer) obj);
                }
            }, SkyFragment$$ExternalSyntheticLambda2.INSTANCE);
        }
    }

    public /* synthetic */ void lambda$doSegmentSky$0(ObservableEmitter observableEmitter) throws Exception {
        int segmentSeq = SkyTranFilter.getInstance().segmentSeq(getPreview());
        if (getRenderFragment() != null) {
            ((SkyRenderFragment) getRenderFragment()).notifySegmentFinish(segmentSeq);
        }
        observableEmitter.onNext(Integer.valueOf(segmentSeq));
    }

    public /* synthetic */ void lambda$doSegmentSky$1(Integer num) throws Exception {
        this.mSkySegmentStatus = 2;
        this.mSkySegmentMode = num.intValue();
        this.mSegmentLatch.countDown();
        HashMap hashMap = new HashMap();
        hashMap.put("result", String.valueOf(num));
        SamplingStatHelper.recordCountEvent("photo_editor", "sky_filter_result", hashMap);
    }

    public void showTopPanel(boolean z) {
        int i = 8;
        this.mTopPanel.setVisibility(z ? 0 : 8);
        this.mSeekBar.setVisibility(z ? 0 : 8);
        SimpleRecyclerView simpleRecyclerView = this.mSkyCategoryRecyclerView;
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
            objectAnimator2.setTarget(this.mSkyCategoryRecyclerView);
            objectAnimator2.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f, -getResources().getDimension(R.dimen.photo_editor_filter_tab_offset)), PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f));
            objectAnimator2.setInterpolator(new CubicEaseOutInterpolator());
            objectAnimator2.setDuration(getResources().getInteger(R.integer.photo_editor_filter_tab_disappear_duration));
            objectAnimator2.start();
            return;
        }
        ObjectAnimator objectAnimator3 = new ObjectAnimator();
        objectAnimator3.setTarget(this.mSkyCategoryRecyclerView);
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

    public final void setSelectedTabOnClick(RecyclerView recyclerView, int i) {
        this.mIsItemClick = true;
        recyclerView.smoothScrollToPosition(i);
        setSelectedTabPosition(i);
    }

    public final void setSelectedTabPosition(int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < this.mCategories.size(); i3++) {
            List<SkyData> dataList = this.mCategories.get(i3).getDataList();
            if (dataList != null && i < (i2 = i2 + dataList.size())) {
                this.mSkyCategoryAdapter.setSelection(i3);
                this.mSkyCategoryRecyclerView.setSpringEnabled(false);
                this.mSkyCategoryRecyclerView.smoothScrollToPosition(i3);
                return;
            }
        }
    }

    /* loaded from: classes2.dex */
    public class ItemClickListener implements OnItemClickListener {
        public int mClickedPosition;
        public RecyclerView mClickedRecyclerView;
        public SkyAdapter mWaitAdapter;
        public SkyData mWaitData;
        public int mWaitPosition;
        public RecyclerView mWaitRecyclerView;

        public static /* synthetic */ void $r8$lambda$3CzLiqmCZ11b2_GAw4CncnFpjvA(ItemClickListener itemClickListener, Integer num) {
            itemClickListener.lambda$waitSegmentAndSelectItem$0(num);
        }

        public static /* synthetic */ void lambda$waitSegmentAndSelectItem$1(Throwable th) throws Exception {
        }

        public ItemClickListener() {
            SkyFragment.this = r1;
        }

        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(final RecyclerView recyclerView, View view, final int i) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            if (SkyFragment.this.getRenderFragment() == null) {
                return false;
            }
            this.mClickedRecyclerView = recyclerView;
            this.mClickedPosition = i;
            final SkyAdapter skyAdapter = (SkyAdapter) recyclerView.getAdapter();
            if (skyAdapter == null) {
                return false;
            }
            final SkyData itemData = skyAdapter.getItemData(i);
            int downloadState = itemData.getDownloadState();
            if (itemData.isNone()) {
                doSelectItem(recyclerView, skyAdapter, i, itemData);
                ((SkyRenderFragment) SkyFragment.this.getRenderFragment()).changeUi(false, false);
                return true;
            }
            if (downloadState == 17 || downloadState == 0) {
                waitSegmentAndSelectItem(recyclerView, skyAdapter, itemData, i);
            } else if (downloadState == 19 || downloadState == 20) {
                itemData.setDownloadState(18);
                if (SkyFragment.this.mResourceFetcher == null) {
                    return false;
                }
                SkyFragment.this.mResourceFetcher.checkFetch(SkyFragment.this.getActivity(), itemData, new Request.Listener() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment.ItemClickListener.1
                    {
                        ItemClickListener.this = this;
                    }

                    @Override // com.miui.gallery.net.fetch.Request.Listener
                    public void onStart() {
                        itemData.setDownloadState(18);
                        skyAdapter.notifyItemChanged(i);
                    }

                    @Override // com.miui.gallery.net.fetch.Request.Listener
                    public void onSuccess() {
                        itemData.setDownloadState(0);
                        skyAdapter.notifyItemChanged(i);
                        if (!SkyFragment.this.checkResolutionIsSupport()) {
                            SkyFragment.this.getHostAbility().showInnerToast(SkyFragment.this.getString(R.string.filter_sky_detect_forbidden_tips));
                        } else {
                            ItemClickListener.this.waitSegmentAndSelectItem(recyclerView, skyAdapter, itemData, i);
                        }
                    }

                    @Override // com.miui.gallery.net.fetch.Request.Listener
                    public void onFail() {
                        itemData.setDownloadState(20);
                        skyAdapter.notifyItemChanged(i);
                    }
                });
            }
            return false;
        }

        public void waitSegmentAndSelectItem(RecyclerView recyclerView, SkyAdapter skyAdapter, SkyData skyData, int i) {
            if (!SkyFragment.this.isAdded() || SkyFragment.this.getRenderFragment() == null || recyclerView != this.mClickedRecyclerView || i != this.mClickedPosition) {
                return;
            }
            if (SkyFragment.this.mSkySegmentStatus < 2) {
                if (SkyFragment.this.mWaitSegmentDisposable == null) {
                    this.mWaitPosition = i;
                    this.mWaitRecyclerView = recyclerView;
                    this.mWaitAdapter = skyAdapter;
                    this.mWaitData = skyData;
                    ((SkyRenderFragment) SkyFragment.this.getRenderFragment()).showProgressBar();
                    SkyFragment.this.mWaitSegmentDisposable = Observable.create(new ObservableOnSubscribe<Integer>() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment.ItemClickListener.2
                        {
                            ItemClickListener.this = this;
                        }

                        @Override // io.reactivex.ObservableOnSubscribe
                        public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                            try {
                                SkyFragment.this.mSegmentLatch.await(50L, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                DefaultLogger.e("SkyFragment", e);
                                SkyFragment.this.mSkySegmentMode = 2;
                            }
                            if (SkyFragment.this.getRenderFragment() != null) {
                                ((SkyRenderFragment) SkyFragment.this.getRenderFragment()).notifySegmentFinish(SkyFragment.this.mSkySegmentMode);
                            }
                            observableEmitter.onNext(Integer.valueOf(SkyFragment.this.mSkySegmentMode));
                            observableEmitter.onComplete();
                        }
                    }).subscribeOn(Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.editor.photo.app.sky.SkyFragment$ItemClickListener$$ExternalSyntheticLambda0
                        @Override // io.reactivex.functions.Consumer
                        public final void accept(Object obj) {
                            SkyFragment.ItemClickListener.$r8$lambda$3CzLiqmCZ11b2_GAw4CncnFpjvA(SkyFragment.ItemClickListener.this, (Integer) obj);
                        }
                    }, SkyFragment$ItemClickListener$$ExternalSyntheticLambda1.INSTANCE);
                    return;
                }
                this.mWaitPosition = i;
                this.mWaitRecyclerView = recyclerView;
                this.mWaitAdapter = skyAdapter;
                this.mWaitData = skyData;
                return;
            }
            checkSegmentAndSelectItem(recyclerView, skyAdapter, skyData, i);
        }

        public /* synthetic */ void lambda$waitSegmentAndSelectItem$0(Integer num) throws Exception {
            SkyFragment.this.mSkySegmentMode = num.intValue();
            checkSegmentAndSelectItem(this.mWaitRecyclerView, this.mWaitAdapter, this.mWaitData, this.mWaitPosition);
        }

        public void checkSegmentAndSelectItem(RecyclerView recyclerView, SkyAdapter skyAdapter, SkyData skyData, int i) {
            if (!SkyFragment.this.isAdded() || SkyFragment.this.getRenderFragment() == null) {
                return;
            }
            DefaultLogger.d("SkyFragment", "checkSegmentAndSelectItem %d %d", Integer.valueOf(i), Integer.valueOf(this.mClickedPosition));
            if (recyclerView != this.mClickedRecyclerView || i != this.mClickedPosition) {
                return;
            }
            if (!skyData.isDynamic()) {
                if (SkyFragment.this.mSkySegmentMode == 2) {
                    ((SkyRenderFragment) SkyFragment.this.getRenderFragment()).hideProgressBar();
                    SkyFragment.this.getHostAbility().showInnerToast(SkyFragment.this.getResources().getString(R.string.filter_sky_detect_forbidden_tips));
                    return;
                }
                doSelectItem(recyclerView, skyAdapter, i, skyData);
            } else if (skyData.dependOnSegment() && SkyFragment.this.mSkySegmentMode != 1) {
                ((SkyRenderFragment) SkyFragment.this.getRenderFragment()).hideProgressBar();
                SkyFragment.this.getHostAbility().showInnerToast(SkyFragment.this.getResources().getString(R.string.filter_sky_detect_forbidden_tips));
            } else {
                doSelectItem(recyclerView, skyAdapter, i, skyData);
            }
        }

        public void doSelectItem(RecyclerView recyclerView, SkyAdapter skyAdapter, int i, SkyData skyData) {
            if (!SkyFragment.this.isAdded() || SkyFragment.this.getRenderFragment() == null) {
                return;
            }
            if (!skyData.isNone() && !SkyFragment.this.checkResolutionIsSupport()) {
                SkyFragment.this.getHostAbility().showInnerToast(SkyFragment.this.getResources().getString(R.string.filter_sky_detect_forbidden_tips));
                return;
            }
            DefaultLogger.d("SkyFragment", "doSelectItem %d", Integer.valueOf(i));
            if (skyData.equals(SkyFragment.this.mSelectedData)) {
                if (skyAdapter.isInEditMode()) {
                    skyAdapter.exitEditMode();
                    SkyFragment.this.showTopPanel(false);
                    return;
                } else if (skyData.isNone()) {
                    return;
                } else {
                    skyAdapter.enterEditMode();
                    SkyFragment.this.mSeekBar.setCurrentProgress(skyData.getProgress());
                    SkyFragment.this.showTopPanel(true);
                    return;
                }
            }
            DefaultLogger.d("SkyFragment", "doSelectItem render %d", Integer.valueOf(i));
            SkyFragment skyFragment = SkyFragment.this;
            skyFragment.mToClearProgress = skyFragment.mSelectedData != null && (SkyFragment.this.mSelectedData.isDynamic() ^ skyData.isDynamic());
            SkyFragment.this.mSelectedData = skyData;
            skyAdapter.setSelection(i);
            SkyFragment.this.setSelectedTabOnClick(recyclerView, i);
            ((AbstractEffectFragment) SkyFragment.this.getRenderFragment()).add(skyData, null);
            ((AbstractEffectFragment) SkyFragment.this.getRenderFragment()).render();
            if (skyAdapter.isInEditMode()) {
                if (!skyData.isNone()) {
                    SkyFragment.this.mSeekBar.setCurrentProgress(skyData.getProgress());
                    BubbleSeekBar bubbleSeekBar = SkyFragment.this.mSeekBar;
                    bubbleSeekBar.setContentDescription(skyData.name + SkyFragment.this.getResources().getString(R.string.photo_editor_talkback_seekbar) + skyData.getProgress());
                } else {
                    skyAdapter.exitEditMode();
                    SkyFragment.this.showTopPanel(false);
                }
            }
            if (SkyFragment.this.mSingleEffectMode) {
                SkyFragment.this.setSaveEnable(!skyData.isNone());
            }
            SkyFragment.this.clearOtherSelector(i);
            SkyFragment.this.sampleItemClick(skyData);
        }
    }

    public final boolean checkResolutionIsSupport() {
        if (getRenderFragment() == null || getRenderFragment().getBitmap() == null) {
            return false;
        }
        return getRenderFragment().getBitmap().getWidth() >= 128 && getRenderFragment().getBitmap().getHeight() >= 128;
    }

    public final void sampleTabClick(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        SamplingStatHelper.recordCountEvent("photo_editor", "sky_tab_click", hashMap);
    }

    public final void sampleItemClick(SkyData skyData) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, skyData.name);
        SamplingStatHelper.recordCountEvent("photo_editor", "sky_item_click", hashMap);
    }

    public final void doProgress(int i) {
        if (!isAdded() || getRenderFragment() == null) {
            return;
        }
        if (this.mSelectedData != null) {
            updateProgress(i);
        }
        getRenderFragment().clear();
        getRenderFragment().add(this.mSelectedData, null);
        getRenderFragment().render();
    }

    public final void updateProgress(int i) {
        SkyAdapter skyAdapter = this.mAdapter;
        if (skyAdapter != null) {
            skyAdapter.update(i);
        }
    }

    public final void clearOtherSelector(int i) {
        SkyAdapter skyAdapter = this.mAdapter;
        if (skyAdapter == null || this.mSelectedData == null) {
            return;
        }
        skyAdapter.setSelection(i);
    }

    public final void clearOtherProgress() {
        if (this.mAdapter != null) {
            for (int i = 0; i < this.mAdapter.getItemCount(); i++) {
                this.mAdapter.getItemData(i).resetProgress();
            }
        }
    }
}
