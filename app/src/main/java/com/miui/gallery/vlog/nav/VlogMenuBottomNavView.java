package com.miui.gallery.vlog.nav;

import android.content.Context;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogContract$IVlogView;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.tools.PathNameUtil;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class VlogMenuBottomNavView extends FrameLayout {
    public boolean isClickRightTab;
    public VlogNavBaseAdapter mAdapter;
    public Context mContext;
    public int mCurPosition;
    public VlogContract$IVlogView mIVlogView;
    public SingleChoiceRecyclerView mRecyclerView;
    public ViewTreeObserver.OnGlobalLayoutListener mRecyclerViewLayoutListener;
    public List<VlogNavItem> mTitles;

    public VlogMenuBottomNavView(Context context) {
        super(context);
        this.mRecyclerViewLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.nav.VlogMenuBottomNavView.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                VlogMenuBottomNavView.this.mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (VlogMenuBottomNavView.this.mAdapter != null) {
                    VlogMenuBottomNavView.this.mAdapter.updateItemSize();
                }
            }
        };
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        if (context instanceof VlogContract$IVlogView) {
            this.mIVlogView = (VlogContract$IVlogView) context;
        }
        if (VlogUtils.isLandscape(context)) {
            FrameLayout.inflate(context, R$layout.vlog_menu_nav_layout_land, this);
        } else {
            FrameLayout.inflate(context, R$layout.vlog_menu_nav_layout, this);
        }
        initRecyclerView();
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this.mRecyclerViewLayoutListener);
    }

    public final void initRecyclerView() {
        if (this.mTitles == null) {
            this.mTitles = new ArrayList();
        }
        this.mTitles.addAll(VlogConfig.getVlogNavData(((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).isSingleVideoEdit()));
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getContext());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getContext()));
        if (VlogUtils.isLandscape(getContext())) {
            customScrollerLinearLayoutManager.setOrientation(1);
            this.mAdapter = new VlogNavLandscapeAdapter(this.mContext, this.mTitles);
        } else {
            customScrollerLinearLayoutManager.setOrientation(0);
            this.mAdapter = new VlogNavAdapter(this.mContext, this.mTitles);
        }
        SingleChoiceRecyclerView singleChoiceRecyclerView = (SingleChoiceRecyclerView) findViewById(R$id.recycler_view);
        this.mRecyclerView = singleChoiceRecyclerView;
        singleChoiceRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mAdapter.setItemSelectChangeListener(new VlogNavItemSelectedListener());
    }

    public void navToDefaultView() {
        if (this.mAdapter != null) {
            VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class);
            this.mCurPosition = vlogModel.getCurrentEffectMenuFragmentPosition();
            this.mAdapter.setSelectedItemPosition(this.mCurPosition);
            navToFragment(VlogConfig.getVlogNavData(vlogModel.isSingleVideoEdit()).get(this.mCurPosition));
        }
    }

    public final void navToFragment(VlogNavItem vlogNavItem) {
        if (vlogNavItem != null) {
            String string = this.mContext.getString(vlogNavItem.getTagId());
            String className = vlogNavItem.getClassName();
            VlogContract$IVlogView vlogContract$IVlogView = this.mIVlogView;
            if (vlogContract$IVlogView != null) {
                vlogContract$IVlogView.showEffectMenuFragment(this.mCurPosition, className, string);
            }
            VlogStatUtils.statEvent("nav", PathNameUtil.getPathSuffix(className));
        }
    }

    /* loaded from: classes2.dex */
    public class VlogNavItemSelectedListener implements SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener {
        public VlogNavItemSelectedListener() {
        }

        @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener
        public boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z) {
            boolean z2 = false;
            if (!z) {
                return false;
            }
            VlogMenuBottomNavView.this.mRecyclerView.smoothScrollToPosition(i);
            singleChoiceRecyclerViewAdapter.clearLastSelectedPostion();
            singleChoiceRecyclerViewAdapter.setSelectedItemPosition(i);
            VlogNavItem selectedItem = ((VlogNavBaseAdapter) singleChoiceRecyclerViewAdapter).getSelectedItem(i);
            VlogMenuBottomNavView vlogMenuBottomNavView = VlogMenuBottomNavView.this;
            if (i > vlogMenuBottomNavView.mCurPosition) {
                z2 = true;
            }
            vlogMenuBottomNavView.isClickRightTab = z2;
            VlogMenuBottomNavView.this.mCurPosition = i;
            VlogMenuBottomNavView.this.navToFragment(selectedItem);
            return true;
        }
    }

    public boolean isClickRightTab() {
        return this.isClickRightTab;
    }
}
