package com.miui.gallery.vlog.ratio;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.MenuFragment;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.vlog.entity.RatioData;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.ratio.MiVideoRatioManager;
import com.miui.gallery.vlog.tools.VlogOrientationProvider;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class RatioMenuFragment extends MenuFragment<RatioMenuPresenter> implements View.OnClickListener {
    public RatioAdapter mAdapter;
    public int mCurrentIndex;
    public boolean mIsValidOriginalRatio;
    public int mLastRatioType;
    public MiVideoRatioManager mMiRatioManager;
    public MiVideoSdkManager mMiVideoSdkManager;
    public RatioData mNoTemplateSelectedData;
    public View mOperationView;
    public ImageView mPlayView;
    public FrameLayout mPlayViewLayout;
    public ImageView mRatioImage;
    public ConstraintLayout mRatioPanel;
    public TextView mRatioText;
    public SimpleRecyclerView mRecyclerView;
    public View mSeparatedView;
    public VlogModel mVlogModel;
    public List<RatioData> mList = new ArrayList();
    public MiVideoRatioManager.Callback mCallback = new MiVideoRatioManager.Callback() { // from class: com.miui.gallery.vlog.ratio.RatioMenuFragment.1
        @Override // com.miui.gallery.vlog.ratio.MiVideoRatioManager.Callback
        public void onCallBack(String str, int i) {
            RatioMenuFragment ratioMenuFragment;
            boolean z = true;
            RatioMenuFragment.this.mIsValidOriginalRatio = !ratioMenuFragment.mMiRatioManager.hasTemplate();
            RatioData ratioData = (RatioData) RatioMenuFragment.this.mList.get(0);
            if (ratioData == null) {
                return;
            }
            if ((!ratioData.isOriginRatio() || RatioMenuFragment.this.mIsValidOriginalRatio) && (ratioData.isOriginRatio() || !RatioMenuFragment.this.mIsValidOriginalRatio)) {
                return;
            }
            RatioMenuFragment.this.updateRatioData();
            if (RatioMenuFragment.this.mNoTemplateSelectedData == null || !RatioMenuFragment.this.mNoTemplateSelectedData.isOriginRatio()) {
                z = false;
            }
            if (RatioMenuFragment.this.mIsValidOriginalRatio) {
                if (z) {
                    RatioMenuFragment.this.mCurrentIndex = 0;
                    RatioMenuFragment ratioMenuFragment2 = RatioMenuFragment.this;
                    ratioMenuFragment2.doSelectRender(ratioMenuFragment2.mNoTemplateSelectedData);
                } else {
                    RatioMenuFragment.access$508(RatioMenuFragment.this);
                }
            }
            if (RatioMenuFragment.this.mIsValidOriginalRatio) {
                return;
            }
            if (z) {
                RatioMenuFragment.this.mCurrentIndex = 0;
                RatioMenuFragment.this.mMiRatioManager.setLiveWindowRatio(i);
                return;
            }
            RatioMenuFragment.access$510(RatioMenuFragment.this);
        }
    };
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.vlog.ratio.RatioMenuFragment.2
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        @SuppressLint({"CheckResult"})
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            recyclerView.smoothScrollToPosition(i);
            RatioData itemData = RatioMenuFragment.this.mAdapter.getItemData(i);
            if (itemData == null) {
                return true;
            }
            if (RatioMenuFragment.this.mMiVideoSdkManager.isPlay()) {
                RatioMenuFragment.this.mMiVideoSdkManager.pause();
                RatioMenuFragment.this.setPlayViewSelected(false);
            }
            RatioMenuFragment.this.mCurrentIndex = i;
            RatioMenuFragment.this.mAdapter.setSelection(i);
            RatioMenuFragment.this.doSelectRender(itemData);
            VlogStatUtils.statEvent(MapBundleKey.OfflineMapKey.OFFLINE_RATION, itemData.getName());
            return true;
        }
    };

    public static /* synthetic */ int access$508(RatioMenuFragment ratioMenuFragment) {
        int i = ratioMenuFragment.mCurrentIndex;
        ratioMenuFragment.mCurrentIndex = i + 1;
        return i;
    }

    public static /* synthetic */ int access$510(RatioMenuFragment ratioMenuFragment) {
        int i = ratioMenuFragment.mCurrentIndex;
        ratioMenuFragment.mCurrentIndex = i - 1;
        return i;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        if (isLandscape()) {
            return layoutInflater.inflate(R$layout.vlog_menu_ratio_layout_land, viewGroup, false);
        }
        return layoutInflater.inflate(R$layout.vlog_menu_ratio_layout, viewGroup, false);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        initDirectionView();
        ConstraintLayout constraintLayout = (ConstraintLayout) getViewById(R$id.ratio_panel);
        this.mRatioPanel = constraintLayout;
        constraintLayout.setOnClickListener(this);
        this.mRatioImage = (ImageView) this.mRatioPanel.findViewById(com.miui.gallery.editor.R$id.icon);
        this.mRatioText = (TextView) this.mRatioPanel.findViewById(com.miui.gallery.editor.R$id.text);
        FolmeUtil.setDefaultTouchAnim(this.mRatioPanel, null, true);
        this.mPlayViewLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_play));
        this.mPlayViewLayout.setOnClickListener(this);
        FolmeUtil.setCustomTouchAnim(this.mPlayViewLayout, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter  reason: collision with other method in class */
    public RatioMenuPresenter mo1801createPresenter() {
        return new RatioMenuPresenter(this.mContext);
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class);
        this.mVlogModel = vlogModel;
        MiVideoSdkManager sdkManager = vlogModel.getSdkManager();
        this.mMiVideoSdkManager = sdkManager;
        MiVideoRatioManager miVideoRatioManager = (MiVideoRatioManager) sdkManager.getManagerService(10);
        this.mMiRatioManager = miVideoRatioManager;
        miVideoRatioManager.setCallback(this.mCallback);
        this.mIsValidOriginalRatio = !this.mMiRatioManager.hasTemplate();
        int ratioType = this.mMiVideoSdkManager.getRatioType();
        this.mLastRatioType = ratioType;
        changeRatioType(ratioType);
        setPlayViewSelected(this.mMiVideoSdkManager.isPlay());
        updateOriginalRatioState();
        updateRatioData();
        initRecyclerView();
    }

    public final void initDirectionView() {
        if (isLandscape()) {
            View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_menu_ratio_operation_layout, (ViewGroup) null, false);
            this.mSeparatedView = inflate;
            this.mPlayViewLayout = (FrameLayout) inflate.findViewById(R$id.play_layout);
            this.mPlayView = (ImageView) this.mSeparatedView.findViewById(R$id.play_view);
            return;
        }
        View inflate2 = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_menu_ratio_operation_layout, (ViewGroup) null, false);
        this.mOperationView = inflate2;
        this.mPlayViewLayout = (FrameLayout) inflate2.findViewById(R$id.play_layout);
        this.mPlayView = (ImageView) this.mOperationView.findViewById(R$id.play_view);
    }

    public final void initRecyclerView() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.vlog_common_menu_recyclerview_item_gap);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.editor_ratio_recycler_view_first_item_start);
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) getViewById(R$id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) simpleRecyclerView.getLayoutParams();
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        if (VlogUtils.isLandscape(getContext())) {
            customScrollerLinearLayoutManager.setOrientation(1);
            layoutParams.width = -2;
            layoutParams.height = -1;
        } else {
            customScrollerLinearLayoutManager.setOrientation(0);
            layoutParams.width = -1;
            layoutParams.height = -2;
        }
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        if (this.mRecyclerView.getItemDecorationCount() == 0) {
            BlankDivider blankDivider = new BlankDivider(dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize, 0, 0);
            blankDivider.setOrientationProvider(new VlogOrientationProvider());
            this.mRecyclerView.addItemDecoration(blankDivider);
        }
        RatioAdapter ratioAdapter = new RatioAdapter(this.mList);
        this.mAdapter = ratioAdapter;
        this.mRecyclerView.setAdapter(ratioAdapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        if (!this.mVlogModel.isSingleVideoEdit()) {
            this.mCurrentIndex = this.mIsValidOriginalRatio ? 1 : 0;
        } else {
            this.mCurrentIndex = ((RatioMenuPresenter) this.mMenuPresenter).findCurrentIndex(this.mList);
        }
        this.mAdapter.setSelection(this.mCurrentIndex);
        this.mRecyclerView.smoothScrollToPosition(this.mCurrentIndex);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        MiVideoRatioManager miVideoRatioManager = this.mMiRatioManager;
        if (miVideoRatioManager != null) {
            miVideoRatioManager.setCallback(null);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getOperationView() {
        return this.mOperationView;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getSeparatedView() {
        return this.mSeparatedView;
    }

    public final void updateRatioData() {
        if (this.mList.size() > 0) {
            this.mList.clear();
        }
        if (this.mIsValidOriginalRatio) {
            List<RatioData> list = this.mList;
            Resources resources = getResources();
            int i = R$string.vlog_float_view_original_ratio;
            list.add(new RatioData(5, resources.getString(i), getResources().getString(i), R$drawable.vlog_menu_ratio_type_origin_selector));
        }
        this.mList.add(new RatioData(0, "16:9", getResources().getString(R$string.vlog_talkback_ratio_16_9), R$drawable.vlog_menu_ratio_type_16_9_selector));
        this.mList.add(new RatioData(4, "9:16", getResources().getString(R$string.vlog_talkback_ratio_9_16), R$drawable.vlog_menu_ratio_type_9_16_selector));
        this.mList.add(new RatioData(6, "3:4", getResources().getString(R$string.vlog_talkback_ratio_3_4), R$drawable.vlog_menu_ratio_type_3_4_selector));
        this.mList.add(new RatioData(7, "4:3", getResources().getString(R$string.vlog_talkback_ratio_4_3), R$drawable.vlog_menu_ratio_type_4_3_selector));
        this.mList.add(new RatioData(1, "2.39:1", getResources().getString(R$string.vlog_editor_talkback_ratio_239_1), R$drawable.vlog_menu_ratio_type_239_1_selector));
        this.mList.add(new RatioData(2, "1:1", getResources().getString(R$string.vlog_talkback_ratio_1_1), R$drawable.vlog_menu_ratio_type_1_1_selector));
        this.mList.add(new RatioData(3, "2.55:1", getResources().getString(R$string.vlog_talkback_ratio_255_1), R$drawable.vlog_menu_ratio_type_255_1_selector));
    }

    public final void updateOriginalRatioState() {
        RatioAdapter ratioAdapter = this.mAdapter;
        if (ratioAdapter != null) {
            ratioAdapter.updateData(this.mList);
            this.mAdapter.setSelection(this.mCurrentIndex);
            this.mRecyclerView.scrollToPosition(this.mCurrentIndex);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            changeRatioType(this.mMiVideoSdkManager.getRatioType());
            this.mLastRatioType = this.mMiVideoSdkManager.getRatioType();
            setPlayViewSelected(this.mMiVideoSdkManager.isPlay());
            updateOriginalRatioState();
        }
    }

    public final void setPlayViewSelected(boolean z) {
        DefaultLogger.d("RatioMenuFragment", "setPlayViewSelected  -> " + z);
        if (this.mPlayView.isSelected() != z) {
            this.mPlayView.setSelected(z);
            this.mPlayViewLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_play));
            return;
        }
        this.mPlayViewLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_pause));
    }

    public final void doSelectRender(RatioData ratioData) {
        if (ratioData != null) {
            this.mMiRatioManager.setLiveWindowRatio(ratioData.getRatio());
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.ratio_panel) {
            changeRatioType(this.mMiVideoSdkManager.getRatioType() == 0 ? 1 : 0);
        } else if (view.getId() != R$id.play_layout) {
        } else {
            if (this.mMiVideoSdkManager.isPlay()) {
                this.mMiVideoSdkManager.pause();
            } else {
                this.mMiVideoSdkManager.resume();
            }
            setPlayViewSelected(this.mMiVideoSdkManager.isPlay());
        }
    }

    public final void changeRatioType(int i) {
        int i2 = i == 0 ? R$string.vlog_ratio_fullsize : R$string.vlog_ratio_original;
        int i3 = i == 0 ? R$drawable.vlog_menu_ratio_type_full_fixed_nor : R$drawable.vlog_menu_ratio_type_origin_fixed_nor;
        this.mRatioText.setText(i2);
        this.mRatioImage.setImageResource(i3);
        this.mMiRatioManager.setRatioType(i);
        if (!this.mMiVideoSdkManager.isPlay()) {
            MiVideoSdkManager miVideoSdkManager = this.mMiVideoSdkManager;
            miVideoSdkManager.seek(miVideoSdkManager.getCurrentTimeMicro());
        }
        VlogStatUtils.statEvent(MapBundleKey.OfflineMapKey.OFFLINE_RATION, i == 0 ? "ration_fullsize" : "ratio_original");
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackStopped() {
        if (!this.mMiVideoSdkManager.isPlay()) {
            setPlayViewSelected(false);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackEOF() {
        super.onPlaybackEOF();
        if (!this.mMiVideoSdkManager.isPlay()) {
            setPlayViewSelected(false);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackTimelinePosition(long j) {
        setPlayViewSelected(true);
    }
}
