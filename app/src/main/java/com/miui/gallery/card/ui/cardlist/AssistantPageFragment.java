package com.miui.gallery.card.ui.cardlist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.miui.gallery.R;
import com.miui.gallery.adapter.AssistantPageHeaderAdapter;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.app.screenChange.ScreenSize;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.ui.cardlist.AssistantPageFragment;
import com.miui.gallery.card.ui.cardlist.CardCoverSizeUtil;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.BaseFragment;
import com.miui.gallery.ui.ImmersionMenuSupport;
import com.miui.gallery.util.ArtStillEntranceUtils;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FeatureUtil;
import com.miui.gallery.util.IDPhotoEntranceUtils;
import com.miui.gallery.util.MIUICommunityGalleryEntranceUtils;
import com.miui.gallery.util.MagicMattingEntranceUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.PhotoMovieEntranceUtils;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.VideoPostEntranceUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.MacaronInstaller;
import com.miui.gallery.vlog.VlogEntranceUtils;
import com.miui.gallery.widget.EmptyPageWithoutSBL;
import com.miui.gallery.widget.LoadMoreLayout;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.SpanSizeProvider;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AssistantPageFragment extends BaseFragment implements ImmersionMenuSupport {
    public static int CARD_CHANGED_RUNNABLE_DELAY = 300;
    public static final Object DATA_CHANGED_SIGNAL = new Object();
    public CardAdapter mAdapter;
    public AssistantPageAdapterWrapper mAdapterWrapper;
    public GridItemDecoration mDecoration;
    public EmptyPageWithoutSBL mEmptyPage;
    public WrapContentGridLayoutManager mLayoutManager;
    public boolean mLoadFinished;
    public LoadMoreLayout mLoadMoreLayout;
    public CreativityFunctionSwitchReceiver mReceiver;
    public GalleryRecyclerView mRecyclerView;
    public int mRecyclerViewPadding;
    public View mSpringBackView;
    public boolean mIsLoading = false;
    public boolean mHasMore = true;
    public boolean mIsFirstLoad = true;
    public boolean mIsFunctionSwitched = false;
    public final Runnable mScrollToBottomListener = new Runnable() { // from class: com.miui.gallery.card.ui.cardlist.AssistantPageFragment$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            AssistantPageFragment.m655$r8$lambda$vq1TABA9fyxwLUYqdTaJUz0GoQ(AssistantPageFragment.this);
        }
    };
    public final RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() { // from class: com.miui.gallery.card.ui.cardlist.AssistantPageFragment.3
        {
            AssistantPageFragment.this = this;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            if (AssistantPageFragment.this.mAdapter.getDataItemSize() < 20) {
                AssistantPageFragment.this.loadMoreCard();
            }
        }
    };
    public final CardManager.CardObserver mCardObserver = new CardManager.CardObserver() { // from class: com.miui.gallery.card.ui.cardlist.AssistantPageFragment.4
        {
            AssistantPageFragment.this = this;
        }

        @Override // com.miui.gallery.card.CardManager.CardObserver
        public void onCardsToShow(List<Card> list) {
            AssistantPageFragment.this.updateCardList(false);
        }

        @Override // com.miui.gallery.card.CardManager.CardObserver
        public void onCardAdded(int i, Card card) {
            AssistantPageFragment.this.updateCardList(true);
        }

        @Override // com.miui.gallery.card.CardManager.CardObserver
        public void onCardDeleted(int i, Card card) {
            AssistantPageFragment.this.updateCardList(false);
        }

        @Override // com.miui.gallery.card.CardManager.CardObserver
        public void onCardUpdated(int i, Card card) {
            AssistantPageFragment.this.updateCardList(false);
            AssistantPageFragment.this.mAdapter.notifyDataSetChanged();
        }
    };
    public Runnable mRunnable = new AnonymousClass5();

    /* renamed from: $r8$lambda$vq1TABA9fy-xwLUYqdTaJUz0GoQ */
    public static /* synthetic */ void m655$r8$lambda$vq1TABA9fyxwLUYqdTaJUz0GoQ(AssistantPageFragment assistantPageFragment) {
        assistantPageFragment.loadMoreCard();
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "assistant";
    }

    @Override // com.miui.gallery.ui.ImmersionMenuSupport
    public int getSupportedAction() {
        return 8;
    }

    @Override // com.miui.gallery.ui.ImmersionMenuSupport
    public void onActionClick(int i) {
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public boolean recordPageByDefault() {
        return false;
    }

    /* loaded from: classes.dex */
    public class WrapContentGridLayoutManager extends GridLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public WrapContentGridLayoutManager(Context context, int i) {
            super(context, i);
            AssistantPageFragment.this = r1;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public int computeVerticalScrollRange(RecyclerView.State state) {
            if (AssistantPageFragment.this.mLoadFinished) {
                return super.computeVerticalScrollRange(state);
            }
            return Integer.MAX_VALUE;
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mReceiver = new CreativityFunctionSwitchReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miui.gallery.action.SWITCH_CREATIVITY_FUNCTION");
        LocalBroadcastManager.getInstance(this.mActivity).registerReceiver(this.mReceiver, intentFilter);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.assistant_page, viewGroup, false);
        this.mSpringBackView = inflate.findViewById(R.id.assistant_page_spring_back);
        this.mRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.recycler_view);
        WrapContentGridLayoutManager wrapContentGridLayoutManager = new WrapContentGridLayoutManager(this.mActivity, CardCoverSizeUtil.getSpanCount());
        this.mLayoutManager = wrapContentGridLayoutManager;
        wrapContentGridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.card.ui.cardlist.AssistantPageFragment.1
            {
                AssistantPageFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanSize(int i) {
                int headerWrapperPos;
                if (!AssistantPageFragment.this.mAdapterWrapper.isHeaderPosition(i) && (headerWrapperPos = AssistantPageFragment.this.mAdapterWrapper.getHeaderWrapperPos(i)) < AssistantPageFragment.this.mAdapter.getDataItemSize()) {
                    return AssistantPageFragment.this.mAdapter.getDataItem(headerWrapperPos).mCoverItemInfo.getSpanSize();
                }
                return AssistantPageFragment.this.mLayoutManager.getSpanCount();
            }
        }));
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        GridItemDecoration gridItemDecoration = new GridItemDecoration(this.mActivity.getResources().getDimensionPixelSize(R.dimen.card_divider_height));
        this.mDecoration = gridItemDecoration;
        this.mRecyclerView.addItemDecoration(gridItemDecoration);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        MiscUtil.setRecyclerViewScrollToBottomListener(this.mRecyclerView, this.mScrollToBottomListener);
        CardAdapter cardAdapter = new CardAdapter(this, getLifecycle());
        this.mAdapter = cardAdapter;
        cardAdapter.registerAdapterDataObserver(this.mAdapterDataObserver);
        this.mLoadMoreLayout = (LoadMoreLayout) layoutInflater.inflate(R.layout.load_more_layout, (ViewGroup) this.mRecyclerView, false);
        EmptyPageWithoutSBL emptyPageWithoutSBL = (EmptyPageWithoutSBL) layoutInflater.inflate(R.layout.cardlist_page_empty_view, (ViewGroup) this.mRecyclerView, false);
        this.mEmptyPage = emptyPageWithoutSBL;
        emptyPageWithoutSBL.setActionButtonVisible(false);
        this.mEmptyPage.setDescription(GalleryPreferences.Assistant.hasCardEver() ? R.string.empty_description_no_cards : R.string.empty_description_conditions_new);
        this.mAdapter.setEmptyView(this.mEmptyPage);
        AssistantPageAdapterWrapper assistantPageAdapterWrapper = new AssistantPageAdapterWrapper(this.mActivity, this.mAdapter);
        this.mAdapterWrapper = assistantPageAdapterWrapper;
        this.mRecyclerView.setAdapter(assistantPageAdapterWrapper);
        this.mRecyclerView.getRecycledViewPool().setMaxRecycledViews(4, 1);
        CardManager.getInstance().registerObserver(this.mCardObserver);
        addScreenChangeListener(new IScreenChange.OnLargeScreenChangeListener() { // from class: com.miui.gallery.card.ui.cardlist.AssistantPageFragment.2
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
            public void onCreatedWhileNormalDevice(ScreenSize screenSize) {
            }

            {
                AssistantPageFragment.this = this;
            }

            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
            public void onCreatedWhileLargeDevice(ScreenSize screenSize) {
                refreshUI();
            }

            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
            public void onScreenSizeToLargeOrNormal(ScreenSize screenSize) {
                refreshUI();
            }

            public final void refreshUI() {
                ((ViewGroup.MarginLayoutParams) AssistantPageFragment.this.mSpringBackView.getLayoutParams()).topMargin = AssistantPageFragment.this.getResources().getDimensionPixelOffset(R.dimen.tab_or_assistant_place_holder_height);
            }
        });
        updateConfiguration(getResources().getConfiguration());
        loadMoreCard();
        return inflate;
    }

    /* loaded from: classes.dex */
    public class GridItemDecoration extends RecyclerView.ItemDecoration {
        public int mSpace;

        public GridItemDecoration(int i) {
            AssistantPageFragment.this = r1;
            this.mSpace = i;
        }

        public void setSpace(int i) {
            this.mSpace = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.getSpanCount();
            int childLayoutPosition = recyclerView.getChildLayoutPosition(view);
            if (!AssistantPageFragment.this.mAdapterWrapper.isHeaderPosition(childLayoutPosition)) {
                int headerWrapperPos = AssistantPageFragment.this.mAdapterWrapper.getHeaderWrapperPos(childLayoutPosition);
                if (AssistantPageFragment.this.mAdapter.getCoverModel() != null && AssistantPageFragment.this.mAdapter.getDataItem(headerWrapperPos) != null) {
                    int i = this.mSpace;
                    rect.left = i / 2;
                    rect.right = i / 2;
                }
                rect.top = this.mSpace;
                return;
            }
            rect.left = 0;
            rect.right = 0;
            rect.top = 0;
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        notifyHeaderDataChanged();
        EmptyPageWithoutSBL emptyPageWithoutSBL = this.mEmptyPage;
        if (emptyPageWithoutSBL != null) {
            emptyPageWithoutSBL.resumeMaml();
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        EmptyPageWithoutSBL emptyPageWithoutSBL = this.mEmptyPage;
        if (emptyPageWithoutSBL != null) {
            emptyPageWithoutSBL.pauseMaml();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        EmptyPageWithoutSBL emptyPageWithoutSBL = this.mEmptyPage;
        if (emptyPageWithoutSBL != null) {
            emptyPageWithoutSBL.destroyMaml();
        }
        super.onDestroyView();
    }

    public final void notifyHeaderDataChanged() {
        RecyclerView.Adapter headerAdapter = this.mAdapterWrapper.getHeaderAdapter();
        if (headerAdapter != null) {
            if (this.mIsFunctionSwitched) {
                this.mIsFunctionSwitched = false;
                headerAdapter.notifyDataSetChanged();
                return;
            }
            headerAdapter.notifyItemChanged(0, DATA_CHANGED_SIGNAL);
        }
    }

    /* loaded from: classes.dex */
    public static class AssistantPageAdapterWrapper extends AbstractHeaderFooterWrapperAdapter<BaseViewHolder, BaseViewHolder> {
        public FragmentActivity mActivity;
        public int mCount;
        public AssistantPageHeaderAdapter mHeaderAdapter;
        public GalleryRecyclerView mHeaderView;

        @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
        public int getFooterItemCount() {
            return 0;
        }

        @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
        public int getHeaderItemCount() {
            return 1;
        }

        @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
        public void onBindFooterItemViewHolder(BaseViewHolder baseViewHolder, int i) {
        }

        @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
        public void onBindHeaderItemViewHolder(BaseViewHolder baseViewHolder, int i) {
        }

        @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
        public /* bridge */ /* synthetic */ void onBindHeaderItemViewHolder(BaseViewHolder baseViewHolder, int i, List list) {
            onBindHeaderItemViewHolder2(baseViewHolder, i, (List<Object>) list);
        }

        public AssistantPageAdapterWrapper(FragmentActivity fragmentActivity, RecyclerView.Adapter adapter) {
            this.mActivity = fragmentActivity;
            setAdapter(adapter);
        }

        @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
        public BaseViewHolder onCreateHeaderItemViewHolder(ViewGroup viewGroup, int i) {
            this.mHeaderView = (GalleryRecyclerView) BaseViewHolder.getView(viewGroup, R.layout.assistant_page_header);
            this.mCount = 1;
            if (PhotoMovieEntranceUtils.isPhotoMovieUseMiSDK()) {
                this.mCount++;
            }
            if (VlogEntranceUtils.isAvailable()) {
                this.mCount++;
            }
            if (MacaronInstaller.isFunctionOn()) {
                this.mCount++;
            }
            if (FeatureUtil.isReplaceAssistantPageRecommend()) {
                this.mCount++;
            } else if (!MagicMattingEntranceUtils.isAvailable()) {
                this.mCount++;
            }
            if (MagicMattingEntranceUtils.isAvailable()) {
                this.mCount++;
            }
            if (IDPhotoEntranceUtils.isAvailable()) {
                this.mCount++;
            }
            if (ArtStillEntranceUtils.isAvailable()) {
                this.mCount++;
            }
            if (VideoPostEntranceUtils.isAvailable()) {
                this.mCount++;
            }
            if (MIUICommunityGalleryEntranceUtils.IS_AVAILABLE) {
                this.mCount++;
            }
            this.mHeaderView.setLayoutManager(new GridLayoutManager(this.mActivity, Math.min(this.mActivity.getResources().getInteger(R.integer.assistant_header_count), this.mCount)));
            AssistantPageHeaderAdapter assistantPageHeaderAdapter = new AssistantPageHeaderAdapter(this.mActivity, GalleryPreferences.Assistant.isCreativityFunctionOn());
            this.mHeaderAdapter = assistantPageHeaderAdapter;
            assistantPageHeaderAdapter.setHasStableIds(true);
            this.mHeaderView.setAdapter(this.mHeaderAdapter);
            this.mHeaderView.setOnItemClickListener(AssistantPageFragment$AssistantPageAdapterWrapper$$ExternalSyntheticLambda0.INSTANCE);
            configHeaderMargin(this.mActivity.getResources().getConfiguration());
            return new BaseViewHolder(this.mHeaderView);
        }

        public static /* synthetic */ boolean lambda$onCreateHeaderItemViewHolder$0(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
            ((AssistantPageHeaderAdapter) recyclerView.getAdapter()).mo1558getItem(i).onClick(view);
            return true;
        }

        public void configHeaderMargin(Configuration configuration) {
            if (this.mHeaderView == null || this.mHeaderAdapter.getItemCount() == 0) {
                return;
            }
            ((GridLayoutManager) this.mHeaderView.getLayoutManager()).setSpanCount(Math.min(this.mActivity.getResources().getInteger(R.integer.assistant_header_count), this.mCount));
            if (BaseBuildUtil.isLargeScreenDevice()) {
                if (CardCoverSizeUtil.isPadModel()) {
                    if (configuration.orientation == 2 && this.mHeaderAdapter.getItemCount() == 4) {
                        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mHeaderView.getLayoutParams();
                        int screenHeight = ((ScreenUtils.getScreenHeight() - ScreenUtils.getScreenWidth()) / 2) + this.mActivity.getResources().getDimensionPixelOffset(R.dimen.card_cover_padding_reverse);
                        int dimensionPixelOffset = this.mActivity.getResources().getDimensionPixelOffset(R.dimen.assistant_page_header_margin_bottom);
                        marginLayoutParams.leftMargin = screenHeight;
                        marginLayoutParams.rightMargin = screenHeight;
                        marginLayoutParams.bottomMargin = dimensionPixelOffset;
                        this.mHeaderView.setLayoutParams(marginLayoutParams);
                        return;
                    }
                    ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mHeaderView.getLayoutParams();
                    int screenHorizontal = (ScreenUtils.getScreenHorizontal(this.mActivity) - (this.mActivity.getResources().getDimensionPixelOffset(R.dimen.assistant_header_width) * this.mHeaderAdapter.getItemCount())) / 2;
                    int dimensionPixelOffset2 = this.mActivity.getResources().getDimensionPixelOffset(R.dimen.assistant_page_header_margin_bottom);
                    marginLayoutParams2.leftMargin = screenHorizontal;
                    marginLayoutParams2.rightMargin = screenHorizontal;
                    marginLayoutParams2.bottomMargin = dimensionPixelOffset2;
                    this.mHeaderView.setLayoutParams(marginLayoutParams2);
                    return;
                }
                ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) this.mHeaderView.getLayoutParams();
                int dimensionPixelOffset3 = this.mActivity.getResources().getDimensionPixelOffset(R.dimen.card_cover_padding_reverse);
                int dimensionPixelOffset4 = this.mActivity.getResources().getDimensionPixelOffset(R.dimen.assistant_page_header_margin_bottom);
                marginLayoutParams3.leftMargin = dimensionPixelOffset3;
                marginLayoutParams3.rightMargin = dimensionPixelOffset3;
                marginLayoutParams3.bottomMargin = dimensionPixelOffset4;
                this.mHeaderView.setLayoutParams(marginLayoutParams3);
                return;
            }
            ViewGroup.MarginLayoutParams marginLayoutParams4 = (ViewGroup.MarginLayoutParams) this.mHeaderView.getLayoutParams();
            int dimensionPixelOffset5 = this.mActivity.getResources().getDimensionPixelOffset(R.dimen.assistant_page_header_margin_horizontal_reverse);
            marginLayoutParams4.bottomMargin = this.mActivity.getResources().getDimensionPixelOffset(R.dimen.assistant_page_header_margin_bottom);
            marginLayoutParams4.leftMargin = dimensionPixelOffset5;
            marginLayoutParams4.rightMargin = dimensionPixelOffset5;
            this.mHeaderView.setLayoutParams(marginLayoutParams4);
        }

        @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
        public BaseViewHolder onCreateFooterItemViewHolder(ViewGroup viewGroup, int i) {
            throw new IllegalStateException();
        }

        /* renamed from: onBindHeaderItemViewHolder */
        public void onBindHeaderItemViewHolder2(BaseViewHolder baseViewHolder, int i, List<Object> list) {
            RecyclerView.Adapter adapter;
            if (list.size() <= 0 || (adapter = ((RecyclerView) baseViewHolder.itemView).getAdapter()) == null) {
                return;
            }
            adapter.notifyDataSetChanged();
        }

        public boolean isHeaderPosition(int i) {
            return getHeaderItemCount() > 0 && i < getHeaderItemCount();
        }

        public int getHeaderWrapperPos(int i) {
            return i - getHeaderItemCount();
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        CardAdapter cardAdapter = this.mAdapter;
        if (cardAdapter != null) {
            cardAdapter.onUserVisibleChanged(z);
        }
        if (z) {
            SamplingStatHelper.recordCountEvent("assistant", "assistant_tab_page_view");
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        ThreadManager.getMainHandler().removeCallbacks(this.mRunnable);
        CardManager.getInstance().unregisterObserver(this.mCardObserver);
        CardAdapter cardAdapter = this.mAdapter;
        if (cardAdapter != null) {
            cardAdapter.unregisterAdapterDataObserver(this.mAdapterDataObserver);
        }
        TimeMonitor.cancelTimeMonitor("403.8.0.1.13765");
        if (this.mReceiver != null) {
            LocalBroadcastManager.getInstance(this.mActivity).unregisterReceiver(this.mReceiver);
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
    }

    public final void updateConfiguration(Configuration configuration) {
        if (configuration.screenWidthDp > configuration.screenHeightDp) {
            int screenHorizontal = ScreenUtils.getScreenHorizontal(getContext());
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.card_cover_width_land);
            int dimensionPixelOffset2 = getResources().getDimensionPixelOffset(R.dimen.card_cover_land_min_padding);
            this.mRecyclerViewPadding = Math.max(dimensionPixelOffset2, (screenHorizontal - dimensionPixelOffset) / 2);
            DefaultLogger.d("AssistantPageFragment", "updateConfiguration => screenHorizontal is %d, minPadding is %d, realPadding is %d", Integer.valueOf(screenHorizontal), Integer.valueOf(dimensionPixelOffset2), Integer.valueOf(this.mRecyclerViewPadding));
        } else {
            this.mRecyclerViewPadding = getResources().getDimensionPixelOffset(R.dimen.card_cover_padding);
        }
        if (this.mRecyclerView != null) {
            int dimensionPixelSize = this.mActivity.getResources().getDimensionPixelSize(R.dimen.card_divider_height) / 2;
            GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
            int i = this.mRecyclerViewPadding;
            galleryRecyclerView.setPaddingRelative(i - dimensionPixelSize, 0, i - dimensionPixelSize, getResources().getDimensionPixelOffset(R.dimen.safe_distance_bottom));
            this.mLayoutManager.setSpanCount(CardCoverSizeUtil.getSpanCount());
            this.mDecoration.setSpace(this.mActivity.getResources().getDimensionPixelSize(R.dimen.card_divider_height));
            CardAdapter cardAdapter = this.mAdapter;
            if (cardAdapter != null && cardAdapter.getDataList() != null) {
                this.mAdapter.updateConfig();
            }
            this.mRecyclerView.invalidateItemDecorations();
            this.mAdapterWrapper.configHeaderMargin(configuration);
        }
    }

    public final void updateCardList(boolean z) {
        ThreadManager.getMainHandler().removeCallbacks(this.mRunnable);
        ThreadManager.getMainHandler().postDelayed(this.mRunnable, (!z || this.mAdapter.getDataItemSize() == 0) ? 0 : CARD_CHANGED_RUNNABLE_DELAY);
    }

    /* renamed from: com.miui.gallery.card.ui.cardlist.AssistantPageFragment$5 */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements Runnable {
        /* renamed from: $r8$lambda$vDdkKb-IA_rIPUyXiV1-rFyKMfk */
        public static /* synthetic */ void m656$r8$lambda$vDdkKbIA_rIPUyXiV1rFyKMfk(AnonymousClass5 anonymousClass5) {
            anonymousClass5.lambda$run$0();
        }

        public AnonymousClass5() {
            AssistantPageFragment.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            List<Card> loadedCard = CardManager.getInstance().getLoadedCard();
            ArrayList arrayList = loadedCard == null ? new ArrayList() : new ArrayList(loadedCard);
            List<CardCoverSizeUtil.CardData> dataList = AssistantPageFragment.this.mAdapter.getDataList();
            AssistantPageFragment.this.mAdapter.updateDataList(arrayList);
            if (!BaseMiscUtil.isValid(dataList) || !BaseMiscUtil.isValid(arrayList)) {
                AssistantPageFragment.this.mAdapter.notifyDataSetChanged();
            } else {
                DiffUtil.calculateDiff(new CardDiffCallback(dataList, AssistantPageFragment.this.mAdapter.getDataList())).dispatchUpdatesTo(AssistantPageFragment.this.mAdapter);
            }
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.card.ui.cardlist.AssistantPageFragment$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AssistantPageFragment.AnonymousClass5.m656$r8$lambda$vDdkKbIA_rIPUyXiV1rFyKMfk(AssistantPageFragment.AnonymousClass5.this);
                }
            });
        }

        public /* synthetic */ void lambda$run$0() {
            AssistantPageFragment.this.mRecyclerView.invalidateItemDecorations();
            if (AssistantPageFragment.this.mLoadFinished) {
                AssistantPageFragment.this.mLoadMoreLayout.setLoadComplete();
                TimeMonitor.trackTimeMonitor("403.8.0.1.13765", AssistantPageFragment.this.mAdapter.getDataItemSize());
            }
        }
    }

    public final void loadMoreCard() {
        if (this.mIsLoading || !this.mHasMore) {
            return;
        }
        DefaultLogger.d("AssistantPageFragment", "loadMoreCard");
        this.mIsLoading = true;
        this.mLoadMoreLayout.startLoad();
        ThreadManager.getMiscPool().submit(AssistantPageFragment$$ExternalSyntheticLambda0.INSTANCE, new FutureHandler<List<Card>>() { // from class: com.miui.gallery.card.ui.cardlist.AssistantPageFragment.6
            {
                AssistantPageFragment.this = this;
            }

            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<List<Card>> future) {
                if (AssistantPageFragment.this.mActivity == null) {
                    return;
                }
                List<Card> list = future.get();
                if (list == null || list.size() < 20) {
                    AssistantPageFragment.this.mLoadFinished = true;
                    List<Card> loadedCard = CardManager.getInstance().getLoadedCard();
                    if (loadedCard != null && loadedCard.size() > 20) {
                        AssistantPageFragment.this.mAdapter.setFooter(AssistantPageFragment.this.mLoadMoreLayout, true);
                    }
                    AssistantPageFragment.this.mHasMore = false;
                } else if (AssistantPageFragment.this.mIsFirstLoad) {
                    AssistantPageFragment.this.mAdapter.setFooter(AssistantPageFragment.this.mLoadMoreLayout, true);
                }
                AssistantPageFragment.this.updateCardList(true);
                AssistantPageFragment.this.mIsLoading = false;
                AssistantPageFragment.this.mIsFirstLoad = false;
            }
        });
    }

    public static /* synthetic */ List lambda$loadMoreCard$0(ThreadPool.JobContext jobContext) {
        return CardManager.getInstance().loadMoreCard();
    }

    /* loaded from: classes.dex */
    public static class CardDiffCallback extends DiffUtil.Callback {
        public List<CardCoverSizeUtil.CardData> mNewList;
        public List<CardCoverSizeUtil.CardData> mOldList;

        public CardDiffCallback(List<CardCoverSizeUtil.CardData> list, List<CardCoverSizeUtil.CardData> list2) {
            this.mOldList = list;
            this.mNewList = list2;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            List<CardCoverSizeUtil.CardData> list = this.mOldList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            List<CardCoverSizeUtil.CardData> list = this.mNewList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            List<CardCoverSizeUtil.CardData> list = this.mOldList;
            long j = -1;
            long uniqueId = (list == null || i >= list.size()) ? -1L : this.mOldList.get(i).getUniqueId();
            List<CardCoverSizeUtil.CardData> list2 = this.mNewList;
            if (list2 != null && i2 < list2.size()) {
                j = this.mNewList.get(i2).getUniqueId();
            }
            return uniqueId == j;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            List<CardCoverSizeUtil.CardData> list = this.mOldList;
            String str = null;
            String contentIdentifier = (list == null || i >= list.size()) ? null : this.mOldList.get(i).getContentIdentifier();
            List<CardCoverSizeUtil.CardData> list2 = this.mNewList;
            if (list2 != null && i2 < list2.size()) {
                str = this.mNewList.get(i2).getContentIdentifier();
            }
            return !(i == 0 && i2 == 0) && TextUtils.equals(str, contentIdentifier) && this.mOldList.get(i).mCoverItemInfo.getSpanSize() == this.mNewList.get(i2).mCoverItemInfo.getSpanSize();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        CardAdapter cardAdapter;
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && (cardAdapter = this.mAdapter) != null) {
            cardAdapter.backTranslation(intent);
        }
    }

    @Override // com.miui.gallery.app.fragment.MiuiFragment, com.miui.gallery.listener.OnVisibilityChangeListener
    public void onVisibleChange(boolean z) {
        super.onVisibleChange(z);
        if (z) {
            String ref = AutoTracking.getRef();
            CardAdapter cardAdapter = this.mAdapter;
            AutoTracking.trackView("403.8.0.1.11136", ref, cardAdapter == null ? 0 : cardAdapter.getDataItemSize());
        }
    }

    public final void switchWithoutAnimator() {
        AssistantPageAdapterWrapper assistantPageAdapterWrapper = this.mAdapterWrapper;
        if (assistantPageAdapterWrapper == null || assistantPageAdapterWrapper.mHeaderAdapter == null) {
            return;
        }
        if (GalleryPreferences.Assistant.isCreativityFunctionOn()) {
            this.mAdapterWrapper.mHeaderAdapter.resetEntranceList();
        } else {
            this.mAdapterWrapper.mHeaderAdapter.clearEntranceList();
        }
    }

    /* loaded from: classes.dex */
    public class CreativityFunctionSwitchReceiver extends BroadcastReceiver {
        public CreativityFunctionSwitchReceiver() {
            AssistantPageFragment.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("com.miui.gallery.action.SWITCH_CREATIVITY_FUNCTION")) {
                AssistantPageFragment assistantPageFragment = AssistantPageFragment.this;
                assistantPageFragment.mIsFunctionSwitched = !assistantPageFragment.mIsFunctionSwitched;
                AssistantPageFragment.this.switchWithoutAnimator();
            }
        }
    }
}
