package com.miui.gallery.ui.album.main.base.config;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.config.CommonGridItemViewDisplaySetting;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.addtoalbum.viewbean.AddToAlbumItemViewBean;
import com.miui.gallery.ui.album.common.RecyclerViewItemModel;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.common.viewbean.AlbumTabGroupTitleViewBean;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P;
import com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences;
import com.miui.gallery.ui.album.main.utils.splitgroup.IAlbumPageComponentVersion;
import com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumTabComponentInfo;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.DimensionUtils;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.ResourceUtils;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumPageConfig {
    public boolean isAddToAlbumOperation;
    public final LazyValue<Void, AddToAlbumPage> mAddToAlbumPageLazyValue;
    public final LazyValue<Void, AlbumTabPage> mAlbumTabPageLazyValue;

    public static AlbumPageConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final AlbumPageConfig INSTANCE = new AlbumPageConfig();
    }

    public AlbumPageConfig() {
        this.isAddToAlbumOperation = false;
        this.mAlbumTabPageLazyValue = new LazyValue<Void, AlbumTabPage>() { // from class: com.miui.gallery.ui.album.main.base.config.AlbumPageConfig.1
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public AlbumTabPage mo1272onInit(Void r1) {
                return new AlbumTabPage();
            }
        };
        this.mAddToAlbumPageLazyValue = new LazyValue<Void, AddToAlbumPage>() { // from class: com.miui.gallery.ui.album.main.base.config.AlbumPageConfig.2
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public AddToAlbumPage mo1272onInit(Void r1) {
                return new AddToAlbumPage();
            }
        };
    }

    public static AlbumTabPage getAlbumTabConfig() {
        return getInstance().mAlbumTabPageLazyValue.get(null);
    }

    public static AddToAlbumPage getAddToAlbumConfig() {
        return getInstance().mAddToAlbumPageLazyValue.get(null);
    }

    public <P extends BaseAlbumTabContract$P> IAlbumPageComponentVersion getComponent(P p) {
        return new AlbumTabComponentInfo(p);
    }

    public boolean isGridPageMode() {
        return getAlbumTabConfig().isGridPageMode();
    }

    public boolean isAddToAlbumOperation() {
        return this.isAddToAlbumOperation;
    }

    public void setAddToAlbumOperation(boolean z) {
        this.isAddToAlbumOperation = z;
    }

    public boolean shouldUseGridLayout() {
        return isGridPageMode() || isAddToAlbumOperation();
    }

    public boolean isNeedUseHorizontalSettingSpanCount(Activity activity) {
        if (activity == null) {
            return GalleryApp.sGetAndroidContext().getResources().getConfiguration().orientation == 2 || BuildUtil.isPcMode(GalleryApp.sGetAndroidContext());
        }
        return ((activity.getResources().getConfiguration().orientation == 2) && !ActivityCompat.isInMultiWindowMode(activity)) || BuildUtil.isPcMode(activity);
    }

    /* loaded from: classes2.dex */
    public static class AlbumTabPage {
        public Boolean isGridMode;
        public GridAlbumPageStyle mGridAlbumConfig;
        public LinearAlbumPageStyle mLinearAlbumConfig;

        public boolean isGridModeByAlbumTabToolGroup() {
            return false;
        }

        public final void initConfigsIfNeed(boolean z) {
            if (z) {
                if (this.mGridAlbumConfig != null) {
                    return;
                }
                this.mGridAlbumConfig = new GridAlbumPageStyle();
            } else if (this.mLinearAlbumConfig != null) {
            } else {
                this.mLinearAlbumConfig = new LinearAlbumPageStyle();
            }
        }

        public void updateConfigResource(Configuration configuration) {
            if (this.isGridMode.booleanValue()) {
                gridAlbumConfig().onConfigurationChanged(configuration);
            } else {
                linearAlbumConfig().onConfigurationChanged(configuration);
            }
        }

        public static GridAlbumPageStyle getGridAlbumConfig() {
            return AlbumPageConfig.getAlbumTabConfig().gridAlbumConfig();
        }

        public static LinearAlbumPageStyle getLinearAlbumConfig() {
            return AlbumPageConfig.getAlbumTabConfig().linearAlbumConfig();
        }

        public static BaseAlbumPageStyle getCurrentAlbumConfig() {
            return AlbumPageConfig.getInstance().isGridPageMode() ? getGridAlbumConfig() : getLinearAlbumConfig();
        }

        public final GridAlbumPageStyle gridAlbumConfig() {
            initConfigsIfNeed(true);
            return this.mGridAlbumConfig;
        }

        public final LinearAlbumPageStyle linearAlbumConfig() {
            initConfigsIfNeed(false);
            return this.mLinearAlbumConfig;
        }

        public boolean isNeedShowDragTip() {
            return AlbumConfigSharedPreferences.getInstance().getBoolean(GalleryPreferences.PrefKeys.IS_FIRST_SHOW_DRAG_TIP_VIEW, true);
        }

        public boolean toggleAlbumPageMode() {
            AlbumConfigSharedPreferences albumConfigSharedPreferences = AlbumConfigSharedPreferences.getInstance();
            Boolean valueOf = Boolean.valueOf(!AlbumConfigSharedPreferences.getInstance().getBoolean("is_grid_album_page", true));
            this.isGridMode = valueOf;
            albumConfigSharedPreferences.putBoolean("is_grid_album_page", valueOf.booleanValue());
            return this.isGridMode.booleanValue();
        }

        public boolean isGridPageMode() {
            if (this.isGridMode == null) {
                this.isGridMode = Boolean.valueOf(AlbumConfigSharedPreferences.getInstance().getBoolean("is_grid_album_page", true));
            }
            return this.isGridMode.booleanValue();
        }

        public boolean isMediaTypeItemNormalStyle() {
            return this.mGridAlbumConfig.getMediaTypeItemStyle() == 0;
        }
    }

    /* loaded from: classes2.dex */
    public static class AddToAlbumPage {
        public int mContentPaddingEnd;
        public int mContentPaddingStart;
        public int mHeaderCoverSize;
        public int mHeaderFirstItemStartSpaing;
        public int mHeaderItemHorizontalSpacing;
        public int mHeaderItemVerticalSpacing;
        public int mNormalGroupGridSpanCount;
        public int mNormalItemHorizontalSpacing;
        public int mNormalItemVerticalSpacing;
        public GridAlbumPageStyle mPageStyle;

        public long getCreateAlbumButtonId() {
            return 2131361919L;
        }

        public long getSecretAlbumButtonId() {
            return -1000L;
        }

        public void updateConfigResource(Activity activity, Configuration configuration) {
            initResource(activity, configuration);
        }

        public final void initResource(Activity activity, Configuration configuration) {
            if (this.mPageStyle == null) {
                this.mPageStyle = AlbumPageConfig.getAlbumTabConfig().gridAlbumConfig();
            }
            if (configuration != null) {
                this.mPageStyle.onConfigurationChanged(configuration);
            }
            this.mHeaderCoverSize = (int) DimensionUtils.getDimensionPixelSize(R.dimen.add_to_album_header_cover_size);
            this.mHeaderItemHorizontalSpacing = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.add_to_album_header_item_horizontal_spacing)) / 2;
            this.mHeaderItemVerticalSpacing = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.add_to_album_header_item_vertical_spacing)) / 2;
            this.mNormalItemVerticalSpacing = this.mPageStyle.getItemVerticalSpacing();
            this.mNormalItemHorizontalSpacing = this.mPageStyle.getItemHorizontalSpacing();
            this.mNormalGroupGridSpanCount = this.mPageStyle.getSpanCount(activity);
            int dimensionPixelSize = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.add_to_album_page_padding_start_and_end)) - this.mNormalItemHorizontalSpacing;
            this.mContentPaddingStart = dimensionPixelSize;
            this.mContentPaddingEnd = dimensionPixelSize;
            this.mHeaderFirstItemStartSpaing = 0;
        }

        public RecyclerView.ItemDecoration getNormalGroupItemDecoration() {
            return new RecyclerView.ItemDecoration() { // from class: com.miui.gallery.ui.album.main.base.config.AlbumPageConfig.AddToAlbumPage.1
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    rect.set(AddToAlbumPage.this.mNormalItemHorizontalSpacing, AddToAlbumPage.this.mNormalItemVerticalSpacing, AddToAlbumPage.this.mNormalItemHorizontalSpacing, AddToAlbumPage.this.mNormalItemVerticalSpacing);
                }
            };
        }

        public RecyclerView.LayoutManager getNormalGroupLayoutManager(RecyclerView recyclerView, EpoxyAdapter epoxyAdapter) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), this.mNormalGroupGridSpanCount);
            gridLayoutManager.setSpanSizeLookup(epoxyAdapter.getSpanSizeLookup(this.mNormalGroupGridSpanCount));
            return gridLayoutManager;
        }

        public CommonGridItemViewDisplaySetting getNormalGroupItemDisplaySetting() {
            return new CommonGridItemViewDisplaySetting.DisplayConfig().setShowSubTitleView(true).create();
        }

        public CommonGridItemViewDisplaySetting getHeaderGroupSetting() {
            CommonGridItemViewDisplaySetting.DisplayConfig titleSize = new CommonGridItemViewDisplaySetting.DisplayConfig().setShowSubTitleView(false).setTitleViewCenterHorizontal(true).titleSize(R.dimen.add_to_album_header_item_text_size);
            int i = this.mHeaderCoverSize;
            return titleSize.setImageSize(i, i).create();
        }

        public RecyclerViewItemModel.ConfigBean.Builder<BaseViewBean> getHeaderItemConfigBuilder(long j) {
            return new RecyclerViewItemModel.ConfigBean.Builder(j).setProvider(new RecyclerViewItemModel.ConfigBean.RecyclerViewInitProvider() { // from class: com.miui.gallery.ui.album.main.base.config.AlbumPageConfig.AddToAlbumPage.2
                @Override // com.miui.gallery.ui.album.common.RecyclerViewItemModel.ConfigBean.RecyclerViewInitProvider
                public List<RecyclerView.ItemDecoration> getItemDecorations(RecyclerView recyclerView) {
                    return Collections.singletonList(new RecyclerView.ItemDecoration() { // from class: com.miui.gallery.ui.album.main.base.config.AlbumPageConfig.AddToAlbumPage.2.1
                        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView2, RecyclerView.State state) {
                            rect.set(recyclerView2.getChildAdapterPosition(view) == 0 ? AddToAlbumPage.this.mHeaderFirstItemStartSpaing : AddToAlbumPage.this.mHeaderItemHorizontalSpacing, 0, AddToAlbumPage.this.mHeaderItemHorizontalSpacing, 0);
                        }
                    });
                }
            }).useLinearLayoutManager(0, false);
        }

        public int getNormalGroupGridSpanCount() {
            return this.mNormalGroupGridSpanCount;
        }

        public int getContentPaddingStart() {
            return this.mContentPaddingStart;
        }

        public int getContentPaddingEnd() {
            return this.mContentPaddingEnd;
        }

        public BaseViewBean getCreateAlbumButtonBean() {
            AddToAlbumItemViewBean addToAlbumItemViewBean = new AddToAlbumItemViewBean();
            addToAlbumItemViewBean.setAlbumName(ResourceUtils.getString(R.string.create_new_album));
            addToAlbumItemViewBean.setIsRecent(true);
            addToAlbumItemViewBean.setId(getCreateAlbumButtonId());
            addToAlbumItemViewBean.setAlbumCoverPath(ResourceUtils.getResourceUriPath(R.drawable.add_to_album_page_create_album));
            return addToAlbumItemViewBean;
        }

        public BaseViewBean getSecretAlbumButtonBean() {
            AddToAlbumItemViewBean addToAlbumItemViewBean = new AddToAlbumItemViewBean();
            addToAlbumItemViewBean.setAlbumName(ResourceUtils.getString(R.string.secret_album_display_name));
            addToAlbumItemViewBean.setIsRecent(true);
            addToAlbumItemViewBean.setId(getSecretAlbumButtonId());
            addToAlbumItemViewBean.setAlbumCoverPath(ResourceUtils.getResourceUriPath(R.drawable.add_to_album_page_secret_album));
            return addToAlbumItemViewBean;
        }

        public AlbumTabGroupTitleViewBean getHeaderGroupGap() {
            return new AlbumTabGroupTitleViewBean(2131362612L, 0, 1);
        }
    }
}
