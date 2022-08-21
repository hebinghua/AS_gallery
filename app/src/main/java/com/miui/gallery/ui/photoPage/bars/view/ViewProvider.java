package com.miui.gallery.ui.photoPage.bars.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.internal.CompatHandler;
import com.miui.gallery.R;
import com.miui.gallery.ui.AsyncViewPrefetcher;
import com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class ViewProvider implements IViewProvider {
    public BasePhotoPageBottomMenu mBottomMenu;
    public final Context mContext;
    public IDataProvider mDataProvider;
    public final LayoutInflater mInflater;
    public Runnable mPrefetchRunnable;
    public final List<AsyncViewPrefetcher.PrefetchSpec> mPrefetchSpec = new ArrayList();
    public AsyncViewPrefetcher mPrefetcher;
    public PhotoPageTopMenu mTopMenu;

    /* loaded from: classes2.dex */
    public enum PrefetchSpec {
        GALLERY_BOTTOM_MENU_VIEW(R.layout.gallery_bottom_menu, 1, "gallery_bottom_menu"),
        GALLERY_TOP_PORTRAIT_BAR_VIEW(R.layout.photo_page_top_bar, 1, "photo_page_top_bar"),
        GALLERY_TOP_PORTRAIT_BAR_VIEW_REFORGE(R.layout.photo_page_top_bar_reforge, 1, "photo_page_top_bar_reforge"),
        GALLERY_BOTTOM_MENU_ITEM_VIEW(R.layout.action_menu_item_layout, 4, "action_menu_item_layout"),
        GALLERY_BOTTOM_MENU_MORE_ITEM_VIEW(R.layout.action_menu_more_item_layout, 1, "action_menu_item_layout"),
        GALLERY_BOTTOM_MORE_MENU_ITEM_VIEW(R.layout.action_bar_list_menu_item_layout, 5, "action_bar_list_menu_item_layout"),
        GALLERY_VIDEO_FRAME_SEEK_BAR_VIEW(R.layout.video_frame_seek_bar, 1, "photo_page_video_frame_seek_bar"),
        GALLERY_TOP_LANDSCAPE_BAR_VIEW(R.layout.photo_page_top_menu_bar, 1, "photo_page_top_menu_bar"),
        GALLERY_TOP_LANDSCAPE_BAR_VIEW_REFORGE(R.layout.photo_page_top_menu_bar_reforge, 1, "photo_page_top_menu_bar_reforge"),
        GALLERY_TOP_LANDSCAPE_MENU_ITEM_VIEW(R.layout.photo_page_top_menu_bar_immersion_menu_bar_item_layout, 5, "immersion_menu_bar_item_layout"),
        GALLERY_TOP_LANDSCAPE_MORE_MENU_ITEM_VIEW(R.layout.action_bar_immersion_menu_item_layout, 5, "action_bar_immersion_menu_item_layout"),
        GALLERY_TOP_LANDSCAPE_MORE_MENU_FAVORITE_ITEM_VIEW(R.layout.action_bar_immersion_menu_non_resident_favorite_item_layout, 1, "action_bar_immersion_menu_non_resident_favorite_item_layout"),
        GALLERY_TOP_PAD_BAR_VIEW(R.layout.pad_photo_page_top_menu_bar, 1, "pad_photo_page_top_menu_bar");
        
        public final int count;
        public final String desc;
        public final int res;
        public final int type;

        PrefetchSpec(int i, int i2, String str) {
            this.res = i;
            this.type = i;
            this.count = i2;
            this.desc = str;
        }
    }

    public ViewProvider(Context context, IDataProvider iDataProvider) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDataProvider = iDataProvider;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public void prepareMenuViews() {
        if (MiscUtil.isLandMode(this.mContext)) {
            addPrefetchSpec(PrefetchSpec.GALLERY_TOP_LANDSCAPE_MENU_ITEM_VIEW);
            addPrefetchSpec(PrefetchSpec.GALLERY_TOP_LANDSCAPE_MORE_MENU_ITEM_VIEW);
            addPrefetchSpec(PrefetchSpec.GALLERY_TOP_LANDSCAPE_MORE_MENU_FAVORITE_ITEM_VIEW);
        } else {
            addPrefetchSpec(PrefetchSpec.GALLERY_BOTTOM_MENU_VIEW);
            addPrefetchSpec(PrefetchSpec.GALLERY_BOTTOM_MENU_ITEM_VIEW);
            addPrefetchSpec(PrefetchSpec.GALLERY_BOTTOM_MENU_MORE_ITEM_VIEW);
            addPrefetchSpec(PrefetchSpec.GALLERY_BOTTOM_MORE_MENU_ITEM_VIEW);
        }
        addPrefetchSpec(PrefetchSpec.GALLERY_VIDEO_FRAME_SEEK_BAR_VIEW);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public void prepareActionBarViews() {
        boolean isLandMode = MiscUtil.isLandMode(this.mContext);
        IDataProvider iDataProvider = this.mDataProvider;
        if (iDataProvider == null || !iDataProvider.getFieldData().isVideoPlayerSupportActionBarAdjust) {
            if (isLandMode) {
                addPrefetchSpec(PrefetchSpec.GALLERY_TOP_LANDSCAPE_BAR_VIEW);
            } else {
                addPrefetchSpec(PrefetchSpec.GALLERY_TOP_PORTRAIT_BAR_VIEW);
            }
        } else if (isLandMode) {
            addPrefetchSpec(PrefetchSpec.GALLERY_TOP_LANDSCAPE_BAR_VIEW_REFORGE);
        } else {
            addPrefetchSpec(PrefetchSpec.GALLERY_TOP_PORTRAIT_BAR_VIEW);
        }
        addPrefetchSpec(PrefetchSpec.GALLERY_TOP_PAD_BAR_VIEW);
    }

    public final void addPrefetchSpec(PrefetchSpec prefetchSpec) {
        this.mPrefetchSpec.add(new AsyncViewPrefetcher.PrefetchSpec(prefetchSpec.type, prefetchSpec.count));
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public void startPrepare() {
        AsyncViewPrefetcher asyncViewPrefetcher = new AsyncViewPrefetcher(this.mContext, new FrameLayout(this.mContext), this.mPrefetchSpec) { // from class: com.miui.gallery.ui.photoPage.bars.view.ViewProvider.1
            @Override // com.miui.gallery.ui.ViewProvider
            public int getViewResId(int i) {
                return i;
            }
        };
        this.mPrefetcher = asyncViewPrefetcher;
        asyncViewPrefetcher.setLayoutFactory(GalleryViewCreator.getViewFactory());
        final AsyncViewPrefetcher asyncViewPrefetcher2 = this.mPrefetcher;
        Objects.requireNonNull(asyncViewPrefetcher2);
        this.mPrefetchRunnable = new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.view.ViewProvider$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AsyncViewPrefetcher.this.prefetch();
            }
        };
        ThreadManager.getWorkHandler().post(this.mPrefetchRunnable);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public IPhotoPageMenu getGalleryMenu(IPhotoPageMenuManager iPhotoPageMenuManager, Context context, IMenuItemDelegate.ClickHelper clickHelper, ActionBarCustomViewBuilder.CustomViewType customViewType) {
        if (customViewType == ActionBarCustomViewBuilder.CustomViewType.PadTopMenu || customViewType == ActionBarCustomViewBuilder.CustomViewType.TopMenu) {
            return getTopMenuInstance(iPhotoPageMenuManager, context, clickHelper);
        }
        return getBottomMenuInstance(iPhotoPageMenuManager, context, clickHelper);
    }

    public final IPhotoPageMenu getTopMenuInstance(IPhotoPageMenuManager iPhotoPageMenuManager, Context context, IMenuItemDelegate.ClickHelper clickHelper) {
        PhotoPageTopMenu photoPageTopMenu = this.mTopMenu;
        if (photoPageTopMenu != null) {
            return photoPageTopMenu;
        }
        PhotoPageTopMenu photoPageTopMenu2 = new PhotoPageTopMenu(iPhotoPageMenuManager, context, this, clickHelper);
        this.mTopMenu = photoPageTopMenu2;
        return photoPageTopMenu2;
    }

    public final IPhotoPageMenu getBottomMenuInstance(IPhotoPageMenuManager iPhotoPageMenuManager, Context context, IMenuItemDelegate.ClickHelper clickHelper) {
        if (BaseBuildUtil.isLargeHorizontalWindow()) {
            BasePhotoPageBottomMenu basePhotoPageBottomMenu = this.mBottomMenu;
            if (basePhotoPageBottomMenu instanceof MoreActionPopupListPhotoPageBottomMenu) {
                return basePhotoPageBottomMenu;
            }
            this.mBottomMenu = new MoreActionPopupListPhotoPageBottomMenu(iPhotoPageMenuManager, context, this, clickHelper);
        } else {
            BasePhotoPageBottomMenu basePhotoPageBottomMenu2 = this.mBottomMenu;
            if (basePhotoPageBottomMenu2 instanceof CommonPhotoPageBottomMenu) {
                return basePhotoPageBottomMenu2;
            }
            this.mBottomMenu = new CommonPhotoPageBottomMenu(iPhotoPageMenuManager, context, this, clickHelper);
        }
        return this.mBottomMenu;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public View getBottomMenuView(ViewGroup viewGroup) {
        return getViewInternal(PrefetchSpec.GALLERY_BOTTOM_MENU_VIEW, viewGroup);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public View getTopBarView(ViewGroup viewGroup, ActionBarCustomViewBuilder.CustomViewType customViewType) {
        BasePhotoPageBottomMenu basePhotoPageBottomMenu;
        if (customViewType == ActionBarCustomViewBuilder.CustomViewType.PadTopMenu) {
            return getViewInternal(PrefetchSpec.GALLERY_TOP_PAD_BAR_VIEW, viewGroup);
        }
        if (customViewType == ActionBarCustomViewBuilder.CustomViewType.TopMenu) {
            PhotoPageTopMenu photoPageTopMenu = this.mTopMenu;
            if ((photoPageTopMenu != null && photoPageTopMenu.mMenuManager.isVideoPlayerSupportActionBarAdjust()) || ((basePhotoPageBottomMenu = this.mBottomMenu) != null && basePhotoPageBottomMenu.mMenuManager.isVideoPlayerSupportActionBarAdjust())) {
                return getViewInternal(PrefetchSpec.GALLERY_TOP_LANDSCAPE_BAR_VIEW_REFORGE, viewGroup);
            }
            return getViewInternal(PrefetchSpec.GALLERY_TOP_LANDSCAPE_BAR_VIEW, viewGroup);
        }
        return getViewInternal(PrefetchSpec.GALLERY_TOP_PORTRAIT_BAR_VIEW, viewGroup);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public View getTopMenuView(ViewGroup viewGroup) {
        if (this.mTopMenu == null) {
            DefaultLogger.w("ViewProvider", "getTopMenuView -> view is empty!!");
        }
        return this.mTopMenu;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public View getBottomItemView(ViewGroup viewGroup, boolean z) {
        return getViewInternal(z ? PrefetchSpec.GALLERY_BOTTOM_MENU_MORE_ITEM_VIEW : PrefetchSpec.GALLERY_BOTTOM_MENU_ITEM_VIEW, viewGroup);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public View getBottomMenuMoreItemView(ViewGroup viewGroup) {
        return getViewInternal(PrefetchSpec.GALLERY_BOTTOM_MORE_MENU_ITEM_VIEW, viewGroup);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public View getTopItemView(ViewGroup viewGroup) {
        return getViewInternal(PrefetchSpec.GALLERY_TOP_LANDSCAPE_MENU_ITEM_VIEW, viewGroup);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public View getTopMenuMoreItemView(ViewGroup viewGroup, int i) {
        return getViewInternal(i == 0 ? PrefetchSpec.GALLERY_TOP_LANDSCAPE_MORE_MENU_ITEM_VIEW : PrefetchSpec.GALLERY_TOP_LANDSCAPE_MORE_MENU_FAVORITE_ITEM_VIEW, viewGroup);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public View getVideoSeekBarView(ViewGroup viewGroup) {
        return getViewInternal(PrefetchSpec.GALLERY_VIDEO_FRAME_SEEK_BAR_VIEW, viewGroup);
    }

    public final View getViewInternal(PrefetchSpec prefetchSpec, ViewGroup viewGroup) {
        AsyncViewPrefetcher asyncViewPrefetcher = this.mPrefetcher;
        View viewByType = asyncViewPrefetcher != null ? asyncViewPrefetcher.getViewByType(prefetchSpec.type) : null;
        if (viewByType == null) {
            View inflate = this.mInflater.inflate(prefetchSpec.res, viewGroup, false);
            DefaultLogger.w("PhotoPageFragment", "sync inflate => " + prefetchSpec.desc);
            return inflate;
        }
        return viewByType;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IViewProvider
    public void release() {
        if (this.mPrefetcher != null) {
            ThreadManager.getWorkHandler().removeCallbacks(this.mPrefetchRunnable);
            CompatHandler workHandler = ThreadManager.getWorkHandler();
            final AsyncViewPrefetcher asyncViewPrefetcher = this.mPrefetcher;
            Objects.requireNonNull(asyncViewPrefetcher);
            workHandler.post(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.view.ViewProvider$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AsyncViewPrefetcher.this.release();
                }
            });
        }
        List<AsyncViewPrefetcher.PrefetchSpec> list = this.mPrefetchSpec;
        if (list != null) {
            list.clear();
        }
        this.mBottomMenu = null;
    }
}
