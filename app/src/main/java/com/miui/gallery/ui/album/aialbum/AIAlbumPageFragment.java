package com.miui.gallery.ui.album.aialbum;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.eventhook.OnClickEventHook;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.MapAlbumCoverItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.MapViewModel;
import com.miui.gallery.ui.album.common.CustomViewItemViewBean;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.viewbean.MapAlbumViewBean;
import com.miui.gallery.ui.album.main.viewbean.ai.PeopleFaceAlbumViewBean;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.DimensionUtils;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class AIAlbumPageFragment extends AIAlbumPageContract$V<AIAlbumPagePresenter> {
    public boolean isRtl;
    public RecyclerView.ItemDecoration mAIAlbumItemDecoration;
    public int mColumn;
    public float mDivideLineHeight;
    public float mDivideLineOffset;
    public GridLayoutManager mGridLayoutManager;
    public boolean mIsLargeScreenDevice;
    public int mLocationGroupTipMarginTop;
    public CustomViewItemViewBean mLocationGroupTitleViewBean;
    public View mLocationGroupView;
    public MapViewModel mMapCoverViewModel;
    public int mOtherItemHorizontalSpacing;
    public Paint mPaint;
    public int mPeopleFaceItemHorizontalSpacing;
    public int mPeopleFaceItemVerticalSpacing;
    public int mPeopleGroupTipMarginBottom;
    public CustomViewItemViewBean mPeopleGroupTitleViewBean;
    public View mPeopleGroupView;
    public CustomViewItemViewBean mTagGroupTitleViewBean;
    public View mTagsGroupView;
    public CustomViewItemViewBean.CustomViewLayoutParamConfig mTitleConfig;
    public boolean isFirstInitItemClickListener = true;
    public boolean mIsMultiWindow = false;

    public static /* synthetic */ void $r8$lambda$4H19_h_c95XqIzUJC9_AANfFCyg(AIAlbumPageFragment aIAlbumPageFragment, Configuration configuration) {
        aIAlbumPageFragment.lambda$initRecyclerView$0(configuration);
    }

    public static /* synthetic */ void $r8$lambda$HwOezkaF2QeJbRxyZ_zdKJZh4IU(AIAlbumPageFragment aIAlbumPageFragment, List list) {
        aIAlbumPageFragment.lambda$onActivityCreated$1(list);
    }

    public static AIAlbumPageFragment newInstance() {
        Bundle bundle = new Bundle();
        AIAlbumPageFragment aIAlbumPageFragment = new AIAlbumPageFragment();
        aIAlbumPageFragment.setArguments(bundle);
        return aIAlbumPageFragment;
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.base_optimization.mvp.view.Fragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mIsMultiWindow = !BaseBuildUtil.isLargeHorizontalWindow() && ActivityCompat.isInMultiWindowMode(getSafeActivity());
        this.mIsLargeScreenDevice = BaseBuildUtil.isLargeScreenDevice();
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        this.isRtl = ScreenUtils.isRtl(recyclerView.getContext());
        this.mDivideLineHeight = DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_group_divide_line_height);
        this.mDivideLineOffset = DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_group_divide_line_offset);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(getResources().getColor(R.color.black_10_transparent, null));
        if (getResources().getConfiguration().orientation == 1 || this.mIsMultiWindow) {
            this.mColumn = getResources().getInteger(R.integer.album_ai_grid_view_columns);
        } else {
            this.mColumn = getResources().getInteger(R.integer.album_ai_grid_view_columns_land);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), this.mColumn);
        this.mGridLayoutManager = gridLayoutManager;
        GridLayoutManager.SpanSizeLookup spanSizeLookup = this.mAdapter.getSpanSizeLookup(gridLayoutManager.getSpanCount());
        spanSizeLookup.setSpanIndexCacheEnabled(true);
        this.mGridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        this.mRecyclerView.setLayoutManager(this.mGridLayoutManager);
        this.mRecyclerView.setClipToPadding(false);
        setLoadingPage(R.layout.default_loading_page);
        this.mTitleConfig = new CustomViewItemViewBean.CustomViewLayoutParamConfig(null, -1, -2);
        addScreenChangeListener(new IScreenChange.OnScreenLayoutSizeChangeListener() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnScreenLayoutSizeChangeListener
            public final void onScreenLayoutSizeChange(Configuration configuration) {
                AIAlbumPageFragment.$r8$lambda$4H19_h_c95XqIzUJC9_AANfFCyg(AIAlbumPageFragment.this, configuration);
            }
        });
    }

    public /* synthetic */ void lambda$initRecyclerView$0(Configuration configuration) {
        updateConfiguration();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((AIAlbumPagePresenter) getPresenter()).initData();
        if (isMapAlbumAvailable()) {
            MapViewModel mapViewModel = (MapViewModel) new ViewModelProvider(requireActivity()).get(MapViewModel.class);
            this.mMapCoverViewModel = mapViewModel;
            mapViewModel.queryMapAlbumCovers();
            this.mMapCoverViewModel.getMapCoverItems().observe(requireActivity(), new Observer() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment$$ExternalSyntheticLambda0
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    AIAlbumPageFragment.$r8$lambda$HwOezkaF2QeJbRxyZ_zdKJZh4IU(AIAlbumPageFragment.this, (List) obj);
                }
            });
        }
    }

    public /* synthetic */ void lambda$onActivityCreated$1(List list) {
        ((AIAlbumPagePresenter) getPresenter()).generateMapAlbums(list);
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public CustomViewItemViewBean generatePeopleGroupTitle() {
        if (this.mPeopleGroupTitleViewBean == null) {
            CustomViewItemViewBean customViewItemViewBean = new CustomViewItemViewBean(this.mPeopleGroupView, this.mTitleConfig, true);
            this.mPeopleGroupTitleViewBean = customViewItemViewBean;
            customViewItemViewBean.getView().setTag(GalleryContract.Common.URI_PEOPLE_LIST_PAGE);
        }
        return this.mPeopleGroupTitleViewBean;
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public CustomViewItemViewBean generateLocationGroupTitle() {
        if (this.mLocationGroupTitleViewBean == null) {
            CustomViewItemViewBean customViewItemViewBean = new CustomViewItemViewBean(this.mLocationGroupView, this.mTitleConfig, true);
            this.mLocationGroupTitleViewBean = customViewItemViewBean;
            customViewItemViewBean.getView().setTag(GalleryContract.Search.URI_LOCATION_LIST_PAGE);
        }
        return this.mLocationGroupTitleViewBean;
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public CustomViewItemViewBean generateTagsGroupTitle() {
        if (this.mTagGroupTitleViewBean == null) {
            CustomViewItemViewBean customViewItemViewBean = new CustomViewItemViewBean(this.mTagsGroupView, this.mTitleConfig, true);
            this.mTagGroupTitleViewBean = customViewItemViewBean;
            customViewItemViewBean.getView().setTag(GalleryContract.Search.URI_TAG_LIST_PAGE);
        }
        return this.mTagGroupTitleViewBean;
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public int getLocationAlbumLoadNumber() {
        int integer = getResources().getInteger(R.integer.album_ai_location_load_number);
        return isMapAlbumAvailable() ? integer : integer + 1;
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public int getTagAlbumLoadNumber() {
        return getResources().getInteger(R.integer.album_ai_tag_load_number);
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public int getFaceAlbumLoadNumber() {
        return getResources().getInteger(R.integer.album_ai_face_load_number);
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public void loadPageDatasIsSuccess() {
        this.mPeopleFaceItemHorizontalSpacing = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_people_item_horizontal_spacing);
        this.mPeopleFaceItemVerticalSpacing = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_people_item_vertical_spacing);
        this.mOtherItemHorizontalSpacing = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_other_item_horizontal_spacing);
        this.mPeopleGroupTipMarginBottom = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_people_group_margin_bottom);
        int dimensionPixelSize = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_album_margin_start_end);
        configTitleView();
        this.mRecyclerView.setClipToPadding(false);
        setRecyclerviewPadding(dimensionPixelSize, 0, dimensionPixelSize, ((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_page_recycler_view_margin_bottom)) - this.mPeopleFaceItemVerticalSpacing);
    }

    public final void configTitleView() {
        LayoutInflater layoutInflater = getSafeActivity().getLayoutInflater();
        this.mLocationGroupTipMarginTop = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_location_and_tags_margin_top);
        View inflate = layoutInflater.inflate(R.layout.album_ai_group_more_tip, (ViewGroup) null);
        this.mPeopleGroupView = inflate;
        ((TextView) inflate.findViewById(R.id.tvTitle)).setText(R.string.album_name_people);
        this.mPeopleGroupView.setId(R.id.album_group_people_title);
        this.mPeopleGroupView.setPadding(0, (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_people_group_margin_top), 0, 0);
        View inflate2 = layoutInflater.inflate(R.layout.album_ai_group_more_tip, (ViewGroup) null);
        this.mLocationGroupView = inflate2;
        ((TextView) inflate2.findViewById(R.id.tvTitle)).setText(R.string.album_name_locations);
        this.mLocationGroupView.setId(R.id.album_group_location_title);
        this.mLocationGroupView.setPadding(0, this.mLocationGroupTipMarginTop - this.mPeopleFaceItemVerticalSpacing, 0, 0);
        View inflate3 = layoutInflater.inflate(R.layout.album_ai_group_more_tip, (ViewGroup) null);
        this.mTagsGroupView = inflate3;
        ((TextView) inflate3.findViewById(R.id.tvTitle)).setText(R.string.album_name_tags);
        this.mTagsGroupView.setId(R.id.album_group_tags_title);
        this.mTagsGroupView.setPadding(0, this.mLocationGroupTipMarginTop - this.mPeopleFaceItemVerticalSpacing, 0, 0);
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public void showPageDatas(final List<BaseViewBean> list, List<EpoxyModel<?>> list2) {
        if (list2 == null || list2.isEmpty()) {
            return;
        }
        setDatasAndModels(list, list2, false, new Runnable() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment.1
            {
                AIAlbumPageFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (list != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("tip", "403.16.0.1.13782");
                    hashMap.put(MiStat.Param.COUNT, Integer.valueOf(((AIAlbumPagePresenter) AIAlbumPageFragment.this.getPresenter()).getLocationTypeItemStartPosition() - 1));
                    hashMap.put("count_extra", Integer.valueOf((((AIAlbumPagePresenter) AIAlbumPageFragment.this.getPresenter()).getTagTypeItemStartPosition() - ((AIAlbumPagePresenter) AIAlbumPageFragment.this.getPresenter()).getLocationTypeItemStartPosition()) - 1));
                    hashMap.put("count_plus", Integer.valueOf((list.size() - ((AIAlbumPagePresenter) AIAlbumPageFragment.this.getPresenter()).getTagTypeItemStartPosition()) - 1));
                    TimeMonitor.trackTimeMonitor("403.16.0.1.13782", hashMap);
                }
            }
        });
        if (!this.isFirstInitItemClickListener) {
            return;
        }
        this.mAdapter.addEventHook(new OnClickEventHook<EpoxyViewHolder>(EpoxyViewHolder.class) { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment.2
            {
                AIAlbumPageFragment.this = this;
            }

            @Override // com.miui.epoxy.eventhook.OnClickEventHook
            public void onClick(View view, EpoxyViewHolder epoxyViewHolder, int i, EpoxyModel epoxyModel) {
                Object itemData = ((BaseGalleryItemModel) epoxyModel).getItemData();
                if (itemData instanceof PeopleFaceAlbumViewBean) {
                    PeopleFaceAlbumViewBean peopleFaceAlbumViewBean = (PeopleFaceAlbumViewBean) itemData;
                    if (peopleFaceAlbumViewBean.getFaceRectF() == null || peopleFaceAlbumViewBean.isMoreStyle()) {
                        TrackController.trackClick("403.16.2.1.11246", AutoTracking.getRef());
                        IntentUtil.gotoPeopleListAlbumPage(AIAlbumPageFragment.this.getSafeActivity());
                        return;
                    }
                    TimeMonitor.createNewTimeMonitor("403.17.0.1.13786");
                    TrackController.trackClick("403.16.2.1.11247", AutoTracking.getRef());
                    AIAlbumPageFragment.this.gotoFaceDetailPage(peopleFaceAlbumViewBean);
                } else if (itemData instanceof MapAlbumViewBean) {
                    TrackController.trackClick("403.16.2.1.15340");
                    IntentUtil.goToMapAlbumDirectly(AIAlbumPageFragment.this.getSafeActivity());
                } else {
                    IntentUtil.gotoAIAlbumPage(AIAlbumPageFragment.this.getSafeActivity(), ((AIAlbumPagePresenter) AIAlbumPageFragment.this.getPresenter()).getLocationOrTagsIntentUri(itemData));
                }
            }

            @Override // com.miui.epoxy.eventhook.EventHook
            public View onBind(EpoxyViewHolder epoxyViewHolder) {
                View view = epoxyViewHolder.itemView;
                if (isCoverItem(epoxyViewHolder)) {
                    FolmeUtil.setDefaultTouchAnim(view, null, true);
                }
                return view;
            }

            public final boolean isCoverItem(EpoxyViewHolder epoxyViewHolder) {
                return (epoxyViewHolder instanceof CommonAlbumItemModel.ViewHolder) || (epoxyViewHolder instanceof MapAlbumCoverItemModel.VH);
            }
        });
        this.isFirstInitItemClickListener = false;
    }

    public final void gotoFaceDetailPage(PeopleFaceAlbumViewBean peopleFaceAlbumViewBean) {
        IntentUtil.gotoPeopleDetailFacePage(getSafeActivity(), peopleFaceAlbumViewBean.getPeopleServerId(), String.valueOf(peopleFaceAlbumViewBean.getPeopleLocalId()), TextUtils.isEmpty(peopleFaceAlbumViewBean.getTitle()) ? getString(R.string.people_page_unname) : peopleFaceAlbumViewBean.getTitle(), peopleFaceAlbumViewBean.getRelationType(), peopleFaceAlbumViewBean.getCoverPath(), peopleFaceAlbumViewBean.getFaceRectF());
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIsMultiWindow = !BaseBuildUtil.isLargeHorizontalWindow() && ActivityCompat.isInMultiWindowMode(getSafeActivity());
        if (!this.isRtl && configuration.getLayoutDirection() == 1) {
            this.isRtl = true;
            this.mRecyclerView.invalidateItemDecorations();
        } else if (this.isRtl && configuration.getLayoutDirection() != 1) {
            this.isRtl = false;
            this.mRecyclerView.invalidateItemDecorations();
        }
        updateConfiguration();
        ((AIAlbumPagePresenter) getPresenter()).onConfigurationChanged();
    }

    public final void updateConfiguration() {
        this.mPeopleFaceItemHorizontalSpacing = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_people_item_horizontal_spacing);
        this.mOtherItemHorizontalSpacing = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_other_item_horizontal_spacing);
        this.mPeopleFaceItemVerticalSpacing = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_people_item_vertical_spacing);
        if (getResources().getConfiguration().orientation == 1 || this.mIsMultiWindow) {
            this.mColumn = getResources().getInteger(R.integer.album_ai_grid_view_columns);
        } else {
            this.mColumn = getResources().getInteger(R.integer.album_ai_grid_view_columns_land);
        }
        this.mGridLayoutManager.setSpanCount(this.mColumn);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = this.mAdapter.getSpanSizeLookup(this.mGridLayoutManager.getSpanCount());
        spanSizeLookup.setSpanIndexCacheEnabled(true);
        this.mGridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        int dimensionPixelSize = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_album_margin_start_end);
        setRecyclerviewPadding(dimensionPixelSize, 0, dimensionPixelSize, ((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_ai_page_recycler_view_margin_bottom)) - this.mPeopleFaceItemVerticalSpacing);
        this.mRecyclerView.invalidateItemDecorations();
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return new RecyclerView.ItemDecoration[]{getAIAlbumItemDecoration()};
    }

    public final RecyclerView.ItemDecoration getAIAlbumItemDecoration() {
        RecyclerView.ItemDecoration itemDecoration = this.mAIAlbumItemDecoration;
        if (itemDecoration == null) {
            RecyclerView.ItemDecoration itemDecoration2 = new RecyclerView.ItemDecoration() { // from class: com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment.3
                {
                    AIAlbumPageFragment.this = this;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    int i;
                    int i2;
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int spanCount = gridLayoutManager.getSpanCount();
                    int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                    int spanIndex = gridLayoutManager.getSpanSizeLookup().getSpanIndex(childAdapterPosition, spanCount);
                    if (!commandCustomView(recyclerView.getChildItemId(view), rect)) {
                        if (childAdapterPosition >= ((AIAlbumPagePresenter) AIAlbumPageFragment.this.getPresenter()).getLocationTypeItemStartPosition()) {
                            i = ((spanCount - 1) * AIAlbumPageFragment.this.mOtherItemHorizontalSpacing) / spanCount;
                            i2 = AIAlbumPageFragment.this.mOtherItemHorizontalSpacing;
                        } else {
                            i = ((spanCount - 1) * AIAlbumPageFragment.this.mPeopleFaceItemHorizontalSpacing) / spanCount;
                            i2 = AIAlbumPageFragment.this.mPeopleFaceItemHorizontalSpacing;
                        }
                        int i3 = (spanIndex % spanCount) * (i2 - i);
                        int i4 = i - i3;
                        if (AIAlbumPageFragment.this.isRtl) {
                            rect.set(i4, 0, i3, AIAlbumPageFragment.this.mPeopleFaceItemVerticalSpacing);
                        } else {
                            rect.set(i3, 0, i4, AIAlbumPageFragment.this.mPeopleFaceItemVerticalSpacing);
                        }
                    }
                }

                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
                    super.onDraw(canvas, recyclerView, state);
                    if (AIAlbumPageFragment.this.mDivideLineHeight == 0.0f) {
                        return;
                    }
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
                        View childAt = recyclerView.getChildAt(i);
                        long childItemId = recyclerView.getChildItemId(childAt);
                        if (childItemId == 2131361950 || childItemId == 2131361953) {
                            float left = childAt.getLeft();
                            float right = childAt.getRight();
                            float top = ((childAt.getTop() + AIAlbumPageFragment.this.mLocationGroupTipMarginTop) - AIAlbumPageFragment.this.mPeopleFaceItemVerticalSpacing) - AIAlbumPageFragment.this.mDivideLineOffset;
                            canvas.drawRect(left, top, right, top + AIAlbumPageFragment.this.mDivideLineHeight, AIAlbumPageFragment.this.mPaint);
                        }
                    }
                }

                public final boolean commandCustomView(long j, Rect rect) {
                    boolean z = true;
                    if (j == 2131361952) {
                        rect.top = 0;
                    } else if (j == 2131361950 || j == 2131361953) {
                        rect.top = 0;
                    } else {
                        z = false;
                    }
                    if (z) {
                        rect.bottom = AIAlbumPageFragment.this.mPeopleGroupTipMarginBottom;
                    }
                    return z;
                }
            };
            this.mAIAlbumItemDecoration = itemDecoration2;
            return itemDecoration2;
        }
        return itemDecoration;
    }

    @Override // com.miui.gallery.app.base.BaseFragment
    public void onActivityDestroy() {
        TimeMonitor.cancelTimeMonitor("403.16.0.1.13782");
        super.onActivityDestroy();
    }

    @Override // com.miui.gallery.ui.album.aialbum.AIAlbumPageContract$V
    public boolean isMapAlbumAvailable() {
        return MapInitializerImpl.checkMapAvailable();
    }
}
