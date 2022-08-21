package com.miui.gallery.vlog.clip;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.ui.view.switchview.MoreSlideSwitchConfig;
import com.miui.gallery.editor.ui.view.switchview.MoreSlideSwitchView;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.vlog.base.widget.EditTimelineSortView;
import com.miui.gallery.vlog.clip.TransResView;
import com.miui.gallery.vlog.clip.widget.MultiVideoEditView;
import com.miui.gallery.vlog.clip.widget.VlogTimeView;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogContract$IVlogView;
import com.miui.gallery.vlog.sdk.callbacks.ExportCallback2;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.xiaomi.stat.c.b;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class ClipMenuFragment extends BaseClipMenuFragment<ClipMenuPresenter> implements ClipMenuContract$IClipMenuView, View.OnClickListener, ExportCallback2 {
    public static final double[] SPEED = {3.0d, 2.0d, 1.0d, 0.5d, 0.25d};
    public ImageView mAddClipsView;
    public View mCenterLine;
    public ClipEditNavView mClipEditNavView;
    public FrameLayout mConsLayout;
    public FragmentManager mFragmentManager;
    public MiVideoSdkManager mMiVideoSdkManager;
    public MultiVideoEditView mMultiVideoEditView;
    public FrameLayout mNavLayout;
    public ViewGroup mOperationView;
    public ImageView mPlayView;
    public FrameLayout mPlayViewLayout;
    public FrameLayout mSpeedSwitchLayout;
    public MoreSlideSwitchView mSpeedSwitchView;
    public VlogTimeView mTimeView;
    public EditTimelineSortView mTimelineSortView;
    public TransResView mTransResView;
    public double mDefaultSeekTimeSnap = VlogConfig.sMicroSecondPerTwoPixel;
    public final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.clip.ClipMenuFragment.1
        {
            ClipMenuFragment.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            ClipMenuFragment.this.mMultiVideoEditView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            ClipMenuFragment.this.mDefaultSeekTimeSnap = VlogConfig.sMicroSecondPerTwoPixel;
            if (ClipMenuFragment.this.mMultiVideoEditView != null) {
                ClipMenuFragment.this.mMultiVideoEditView.seekTo(((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getCurrentTime(), false);
            }
        }
    };
    public TransResView.Callback mTransResViewCallback = new TransResView.Callback() { // from class: com.miui.gallery.vlog.clip.ClipMenuFragment.2
        {
            ClipMenuFragment.this = this;
        }

        @Override // com.miui.gallery.vlog.clip.TransResView.Callback
        public void onExit() {
            ClipMenuFragment.this.mIVlogView.updateEffectMenuView(true);
            VlogUtils.showViews(ClipMenuFragment.this.mMultiVideoEditView, ClipMenuFragment.this.mAddClipsView, ClipMenuFragment.this.mCenterLine);
            ClipMenuFragment.this.setTimeViewVisible(true);
            ((ClipMenuPresenter) ClipMenuPresenter.class.cast(ClipMenuFragment.this.mMenuPresenter)).exitTransMode();
            ClipMenuFragment.this.updateMultiVideoEditView();
            ClipMenuFragment.this.mIVlogView.setPlayProgressEnable(true);
            ClipMenuFragment.this.mIVlogView.showProgressView();
            ClipMenuFragment.this.mIVlogView.hideCustomTitleView();
            ClipMenuFragment.this.mIVlogView.showApplyView();
            ClipMenuFragment.this.setPlayViewVisible(true);
            ClipMenuFragment.this.setTimeViewVisible(true);
        }

        @Override // com.miui.gallery.vlog.clip.TransResView.Callback
        public void loadDataFail() {
            ClipMenuFragment.this.mIVlogView.showToast(ClipMenuFragment.this.mContext.getString(R$string.vlog_load_data_fail));
        }
    };
    public EditTimelineSortView.SortCallback mSortCallback = new EditTimelineSortView.SortCallback() { // from class: com.miui.gallery.vlog.clip.ClipMenuFragment.3
        public int fromSelectedPosition = 0;
        public boolean isChanged;

        {
            ClipMenuFragment.this = this;
        }

        @Override // com.miui.gallery.vlog.base.widget.EditTimelineSortView.SortCallback
        public void onExit(boolean z, int i) {
            int indexOf;
            int indexOf2;
            if (this.isChanged) {
                if (!z) {
                    List<IVideoClip> videoClips = ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips();
                    List<IVideoClip> tempIVideoClips = ClipMenuFragment.this.mTimelineSortView.getTempIVideoClips();
                    for (IVideoClip iVideoClip : tempIVideoClips) {
                        if (iVideoClip != null && (indexOf = videoClips.indexOf(iVideoClip)) != (indexOf2 = tempIVideoClips.indexOf(iVideoClip)) && ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).sortVideoClip(indexOf, indexOf2)) {
                            Collections.swap(videoClips, indexOf, indexOf2);
                        }
                    }
                    for (IVideoClip iVideoClip2 : ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips()) {
                        ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).updateVideoClipTransInfo(iVideoClip2);
                    }
                }
                this.isChanged = false;
                ClipMenuFragment.this.updateMultiVideoEditView();
            }
            ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).pause();
            IVideoClip iVideoClip3 = ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips().get(i);
            if (iVideoClip3 != null) {
                ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).seek((long) (iVideoClip3.getInPoint() + ClipMenuFragment.this.mDefaultSeekTimeSnap));
                ClipMenuFragment.this.seekMultiVideoEditView((long) (iVideoClip3.getInPoint() + ClipMenuFragment.this.mDefaultSeekTimeSnap));
            }
            VlogUtils.hideViews(ClipMenuFragment.this.mTimelineSortView);
            VlogUtils.showViews(ClipMenuFragment.this.mMultiVideoEditView, ClipMenuFragment.this.mNavLayout, ClipMenuFragment.this.mClipEditNavView, ClipMenuFragment.this.mAddClipsView, ClipMenuFragment.this.mCenterLine);
            ClipMenuFragment.this.setTimeViewVisible(true);
            ClipMenuFragment.this.mIVlogView.updateNavViewAfterExitClipSortView();
            ClipMenuFragment.this.mMultiVideoEditView.setClipMode(true);
            ClipMenuFragment.this.mIVlogView.setPlayProgressEnable(true);
            ClipMenuFragment.this.mIVlogView.showProgressView();
            ClipMenuFragment.this.mIVlogView.hideCustomTitleView();
            ClipMenuFragment.this.mIVlogView.showApplyView();
            ClipMenuFragment.this.setPlayViewVisible(true);
            ClipMenuFragment.this.setTimeViewVisible(true);
        }

        @Override // com.miui.gallery.vlog.base.widget.EditTimelineSortView.SortCallback
        public void onItemClick(RecyclerView.ViewHolder viewHolder) {
            this.fromSelectedPosition = viewHolder.getAdapterPosition();
            ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).seek((long) (((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips().get(this.fromSelectedPosition).getInPoint() + ClipMenuFragment.this.mDefaultSeekTimeSnap));
        }

        @Override // com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            this.fromSelectedPosition = i;
            ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).seek((long) (((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips().get(this.fromSelectedPosition).getInPoint() + ClipMenuFragment.this.mDefaultSeekTimeSnap));
            viewHolder.itemView.setZ(10.0f);
        }

        @Override // com.miui.gallery.vlog.base.widget.EditTimelineSortView.SortCallback, com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter
        public void onMoveFinished(RecyclerView.ViewHolder viewHolder, int i) {
            DefaultLogger.d("ClipMenuFragment", "onMoveFinished: fromPosition = %s, toPosition = %s", Integer.valueOf(this.fromSelectedPosition), Integer.valueOf(i));
            if (((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).sortVideoClip(this.fromSelectedPosition, i)) {
                boolean z = true;
                ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).updateVideoClipTransInfo(((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClip(this.fromSelectedPosition - 1));
                ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).updateVideoClipTransInfo(((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClip(i));
                ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).updateVideoClipTransInfo(((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClip(i - 1));
                if (this.fromSelectedPosition == i) {
                    z = false;
                }
                this.isChanged = z;
                viewHolder.itemView.setZ(1.0f);
                ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).seek((long) (((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips().get(i).getInPoint() + ClipMenuFragment.this.mDefaultSeekTimeSnap));
                ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).setPlayViewProgress((long) (((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips().get(i).getInPoint() + ClipMenuFragment.this.mDefaultSeekTimeSnap));
            }
        }
    };
    public MultiVideoEditView.MultiVideoEditListener mMultiVideoEditListener = new MultiVideoEditView.MultiVideoEditListener() { // from class: com.miui.gallery.vlog.clip.ClipMenuFragment.4
        public int mCurrentVideoClipIndex;

        {
            ClipMenuFragment.this = this;
        }

        @Override // com.miui.gallery.vlog.clip.widget.MultiVideoEditView.MultiVideoEditListener
        public void onVideoClipSelected(IVideoClip iVideoClip) {
            VlogUtils.showViews(ClipMenuFragment.this.mClipEditNavView);
            ClipMenuFragment.this.mClipEditNavView.updateDeleteItemState();
        }

        @Override // com.miui.gallery.vlog.clip.widget.MultiVideoEditView.MultiVideoEditListener
        public void onTransitionSelected(IVideoClip iVideoClip) {
            if (ClipMenuFragment.this.mCenterLine.getVisibility() != 0) {
                return;
            }
            VlogUtils.hideViews(ClipMenuFragment.this.mMultiVideoEditView, ClipMenuFragment.this.mClipEditNavView, ClipMenuFragment.this.mAddClipsView, ClipMenuFragment.this.mCenterLine);
            ClipMenuFragment.this.mTransResView.setCurrentVideoClip(iVideoClip);
            ClipMenuFragment.this.mTransResView.buildTransAnimator(true);
            ClipMenuFragment.this.mIVlogView.updateEffectMenuView(false);
            ClipMenuFragment.this.mIVlogView.setPlayProgressEnable(false);
            ClipMenuFragment.this.mIVlogView.hideProgressView();
            ClipMenuFragment.this.mIVlogView.hideApplyView();
            if (TextUtils.isEmpty(ClipMenuFragment.this.mAddTransTitle)) {
                ClipMenuFragment clipMenuFragment = ClipMenuFragment.this;
                clipMenuFragment.mAddTransTitle = clipMenuFragment.getResources().getString(R$string.vlog_clip_add_trans_title);
            }
            VlogContract$IVlogView vlogContract$IVlogView = ClipMenuFragment.this.mIVlogView;
            ClipMenuFragment clipMenuFragment2 = ClipMenuFragment.this;
            vlogContract$IVlogView.showCustomTitleView(clipMenuFragment2.getTitleViewWithCustomTitle(clipMenuFragment2.mAddTransTitle));
            ClipMenuFragment.this.hideOperationBar();
            VlogStatUtils.statEvent("clip", "click_trans");
        }

        @Override // com.miui.gallery.vlog.clip.widget.MultiVideoEditView.MultiVideoEditListener
        public void onVideoClipLongClick(IVideoClip iVideoClip) {
            VlogUtils.hideViews(ClipMenuFragment.this.mMultiVideoEditView, ClipMenuFragment.this.mClipEditNavView, ClipMenuFragment.this.mAddClipsView, ClipMenuFragment.this.mCenterLine);
            ClipMenuFragment.this.setTimeViewVisible(false);
            VlogUtils.showViews(ClipMenuFragment.this.mTimelineSortView);
            ClipMenuFragment.this.mTimelineSortView.setOriginClips(((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips());
            ClipMenuFragment.this.mTimelineSortView.setCurrentClip(iVideoClip);
            ClipMenuFragment.this.mTimelineSortView.buildSortAnimator(true);
            ClipMenuFragment.this.mIVlogView.updateEffectMenuView(false);
            ClipMenuFragment.this.mIVlogView.setPlayProgressEnable(false);
            ClipMenuFragment.this.mIVlogView.hideProgressView();
            ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).pause();
            ClipMenuFragment.this.mIVlogView.hideApplyView();
            if (TextUtils.isEmpty(ClipMenuFragment.this.mVideoExchangeOrderTitle)) {
                ClipMenuFragment clipMenuFragment = ClipMenuFragment.this;
                clipMenuFragment.mVideoExchangeOrderTitle = clipMenuFragment.getResources().getString(R$string.vlog_video_adjust_order);
            }
            ClipMenuFragment.this.hideOperationBar();
            VlogContract$IVlogView vlogContract$IVlogView = ClipMenuFragment.this.mIVlogView;
            ClipMenuFragment clipMenuFragment2 = ClipMenuFragment.this;
            vlogContract$IVlogView.showCustomTitleView(clipMenuFragment2.getTitleViewWithCustomTitle(clipMenuFragment2.mVideoExchangeOrderTitle));
            VlogStatUtils.statEvent("clip", "enter_sort");
        }

        @Override // com.miui.gallery.vlog.clip.widget.MultiVideoEditView.MultiVideoEditListener
        public void onVideoClipRegionChanged(IVideoClip iVideoClip, long j, long j2) {
            iVideoClip.changeTrimInPoint(j, true);
            iVideoClip.changeTrimOutPoint(j2, true);
        }

        @Override // com.miui.gallery.vlog.clip.widget.MultiVideoEditView.MultiVideoEditListener
        public void onVideoClipRegionEnd() {
            ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).onTimeLineDurationChanged();
            ClipMenuFragment.this.mMultiVideoEditView.updateVideoClipList(((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips());
            IVideoClip currentVideoClip = ClipMenuFragment.this.mMultiVideoEditView.getCurrentVideoClip();
            if (currentVideoClip == null) {
                return;
            }
            int index = currentVideoClip.getIndex() - 1;
            if (index >= 0) {
                ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).updateVideoClipTransInfo(((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).getVideoClips().get(index));
            }
            ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).updateVideoClipTransInfo(currentVideoClip);
            ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).seek(ClipMenuFragment.this.mMultiVideoEditView.getSeekTime());
            ClipMenuFragment clipMenuFragment = ClipMenuFragment.this;
            clipMenuFragment.updateAllTime(clipMenuFragment.mMultiVideoEditView.getDuration());
            ClipMenuFragment.this.mClipEditNavView.updateCutItemState(currentVideoClip);
            VlogStatUtils.statEvent("clip", "clip_region_change");
        }

        @Override // com.miui.gallery.vlog.clip.widget.MultiVideoEditView.MultiVideoEditListener
        public void onVideoSeek(long j, long j2, long j3) {
            ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).seek(j);
            ClipMenuFragment.this.updateCurrentTime(j2);
            ClipMenuFragment.this.updateAllTime(j3);
            if (ClipMenuFragment.this.isSpeedViewVisible()) {
                ClipMenuFragment.this.setSpeedViewVisible(false);
            }
            IVideoClip currentVideoClip = ClipMenuFragment.this.mMultiVideoEditView.getCurrentVideoClip();
            if (currentVideoClip == null) {
                return;
            }
            if (ClipMenuFragment.this.mClipEditNavView.getVisibility() != 8) {
                ClipMenuFragment.this.mClipEditNavView.updateReverseItemState(currentVideoClip);
            }
            if (currentVideoClip.getIndex() == this.mCurrentVideoClipIndex) {
                return;
            }
            this.mCurrentVideoClipIndex = currentVideoClip.getIndex();
            ClipMenuFragment.this.mClipEditNavView.updateSpeedXItemState(currentVideoClip);
        }

        @Override // com.miui.gallery.vlog.clip.widget.MultiVideoEditView.MultiVideoEditListener
        public void onCurrentClipChanged(IVideoClip iVideoClip) {
            ClipMenuFragment.this.mClipEditNavView.updateSpeedXItemState(iVideoClip);
            ClipMenuFragment.this.mClipEditNavView.updateCutItemState(iVideoClip);
        }

        @Override // com.miui.gallery.vlog.clip.widget.MultiVideoEditView.MultiVideoEditListener
        public void onClipModeChanged(boolean z) {
            ((ClipMenuPresenter) ClipMenuFragment.this.mMenuPresenter).onEnterEditMode(z);
        }
    };
    public MoreSlideSwitchView.OnSelectedListener mOnSelectedListener = new MoreSlideSwitchView.OnSelectedListener() { // from class: com.miui.gallery.vlog.clip.ClipMenuFragment$$ExternalSyntheticLambda0
        @Override // com.miui.gallery.editor.ui.view.switchview.MoreSlideSwitchView.OnSelectedListener
        public final void onSelected(int i, int i2, String str) {
            ClipMenuFragment.$r8$lambda$670Wn_tgxcnVA70kVVBCwdqRNvI(ClipMenuFragment.this, i, i2, str);
        }
    };

    public static /* synthetic */ void $r8$lambda$670Wn_tgxcnVA70kVVBCwdqRNvI(ClipMenuFragment clipMenuFragment, int i, int i2, String str) {
        clipMenuFragment.lambda$new$0(i, i2, str);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R$layout.vlog_menu_clip_layout, (ViewGroup) null);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        this.mNavLayout = (FrameLayout) view.findViewById(R$id.edit_nav);
        this.mAddClipsView = (ImageView) view.findViewById(R$id.iv_add_clips);
        this.mConsLayout = (FrameLayout) view.findViewById(R$id.clip_content);
        this.mClipEditNavView = (ClipEditNavView) view.findViewById(R$id.clip_edit_nav);
        this.mMultiVideoEditView = (MultiVideoEditView) view.findViewById(R$id.multi_video_edit_view);
        this.mTimelineSortView = (EditTimelineSortView) view.findViewById(R$id.edit_time_sort_view);
        this.mTransResView = (TransResView) view.findViewById(R$id.edit_trans_view);
        this.mCenterLine = view.findViewById(R$id.white_line);
        this.mClipEditNavView.setIClipMenuView(this);
        this.mAddClipsView.setOnClickListener(this);
        this.mTimelineSortView.setSortCallback(this.mSortCallback);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(view.getContext()).inflate(R$layout.vlog_menu_clip_operation_layout, (ViewGroup) null, false);
        this.mOperationView = viewGroup;
        this.mTimeView = (VlogTimeView) viewGroup.findViewById(R$id.time_view);
        setTimeViewVisible(true);
        FolmeUtil.setCustomTouchAnim(this.mAddClipsView, new AnimParams.Builder().setAlpha(1.0f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.85f).build(), null, null, true);
        FrameLayout frameLayout = (FrameLayout) this.mOperationView.findViewById(R$id.play_layout);
        this.mPlayViewLayout = frameLayout;
        frameLayout.setOnClickListener(this);
        FolmeUtil.setCustomTouchAnim(this.mPlayViewLayout, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
        this.mPlayView = (ImageView) this.mOperationView.findViewById(R$id.play_view);
        initSlideSwitchView();
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getOperationView() {
        return this.mOperationView;
    }

    public final void initSlideSwitchView() {
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this.mContext).inflate(R$layout.vlog_menu_clip_operation_slide_switch_view, this.mOperationView, false);
        this.mSpeedSwitchLayout = frameLayout;
        this.mSpeedSwitchView = (MoreSlideSwitchView) frameLayout.findViewById(R$id.speed_switch_view);
        this.mSpeedSwitchView.initParams(MoreSlideSwitchConfig.Builder.newBuilder().withSelectedWidth(this.mContext.getResources().getDimensionPixelSize(R$dimen.vlog_speed_selected_width)).withTitles(new String[]{this.mContext.getString(R$string.clip_speedX_3_0x), this.mContext.getString(R$string.clip_speedX_2_0x), this.mContext.getString(R$string.clip_speedX_1x), this.mContext.getString(R$string.clip_speedX_0_5x), this.mContext.getString(R$string.clip_speedX_0_25x)}).build());
        this.mSpeedSwitchView.setOnSelectedListener(this.mOnSelectedListener);
        this.mOperationView.addView(this.mSpeedSwitchLayout);
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mFragmentManager = getFragmentManager();
        this.mTimelineSortView.setVideoFrameLoader(getPresenter().getVideoFrameLoader());
        this.mMultiVideoEditView.setSingleVideoEdit(((ClipMenuPresenter) this.mMenuPresenter).isSingleVideoEdit());
        updateMultiVideoEditView();
        this.mMultiVideoEditView.setVideoFrameLoader(((ClipMenuPresenter) this.mMenuPresenter).getVideoFrameLoader());
        this.mMultiVideoEditView.setListener(this.mMultiVideoEditListener);
        seekMultiVideoEditView(((ClipMenuPresenter) this.mMenuPresenter).getCurrentTime());
        this.mClipEditNavView.updateDeleteItemState();
        this.mTransResView.setCallback(this.mTransResViewCallback);
        updateAllTime(((ClipMenuPresenter) this.mMenuPresenter).getDuration());
        updateCurrentTime(((ClipMenuPresenter) this.mMenuPresenter).getCurrentTime());
        MiVideoSdkManager miVideoSdkManager = getMiVideoSdkManager();
        this.mMiVideoSdkManager = miVideoSdkManager;
        miVideoSdkManager.setExportCallback2(this);
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (z) {
            VlogUtils.hideViews(this.mAddClipsView, this.mCenterLine, this.mClipEditNavView);
            setTimeViewVisible(false);
            this.mIVlogView.updateVlogBottomNaviView(true);
            return;
        }
        ((ClipMenuPresenter) ClipMenuPresenter.class.cast(this.mMenuPresenter)).pauseVideo();
        VlogUtils.showViews(this.mAddClipsView, this.mCenterLine);
        setTimeViewVisible(true);
        this.mMultiVideoEditView.updateVideoClipList(((ClipMenuPresenter) this.mMenuPresenter).getVideoClips());
        this.mMultiVideoEditView.seekTo(((ClipMenuPresenter) this.mMenuPresenter).getCurrentTime());
        updateAllTime(((ClipMenuPresenter) this.mMenuPresenter).getDuration());
        updateCurrentTime(((ClipMenuPresenter) this.mMenuPresenter).getCurrentTime());
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public FragmentManager getClipFragmentManager() {
        return this.mFragmentManager;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter */
    public ClipMenuPresenter mo1801createPresenter() {
        return new ClipMenuPresenter(this.mContext, this);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public ClipEditNavView getClipEditNavView() {
        return this.mClipEditNavView;
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public ClipMenuPresenter getPresenter() {
        return (ClipMenuPresenter) this.mMenuPresenter;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mAddClipsView) {
            LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_LIGHT);
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("video/*");
            intent.putExtra("pick-upper-bound", 100);
            intent.putExtra("pick-lower-bound", 1);
            intent.putExtra("pick-need-origin", true);
            intent.putExtra("pick_close_type", 3);
            Intent intent2 = new Intent("com.miui.gallery.VLOG_SELECT_TEMPLATE");
            Bundle bundle = new Bundle();
            bundle.putSerializable("vlog_video_source", VlogConfig.VideoSource.FORM_INNER_CLIP);
            intent2.putExtras(bundle);
            intent.putExtra("pick_intent", intent2);
            intent.setPackage("com.miui.gallery");
            startActivityForResult(intent, b.j);
            VlogStatUtils.statEvent("clip", "add_view");
        } else if (view != this.mPlayViewLayout) {
        } else {
            this.mPlayView.setSelected(!((ClipMenuPresenter) this.mMenuPresenter).isPlaying());
            if (((ClipMenuPresenter) this.mMenuPresenter).isPlaying()) {
                ((ClipMenuPresenter) this.mMenuPresenter).pauseVideo();
                this.mPlayViewLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_pause));
                return;
            }
            ((ClipMenuPresenter) this.mMenuPresenter).resumeVideo();
            this.mPlayViewLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_play));
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1010 && i2 == -1 && ((ClipMenuPresenter) ClipMenuPresenter.class.cast(this.mMenuPresenter)).getModel(this.mContext).parseIntent(intent, getActivity())) {
            ((ClipMenuPresenter) this.mMenuPresenter).insertVideoClips();
            ((ClipMenuPresenter) this.mMenuPresenter).onTimeLineDurationChanged();
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onScreenSizeChanged(int i) {
        super.onScreenSizeChanged(i);
        this.mMultiVideoEditView.getViewTreeObserver().addOnGlobalLayoutListener(this.mGlobalLayoutListener);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void showTimeView(boolean z) {
        setTimeViewVisible(z);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void enterEditState() {
        if (isHidden()) {
            return;
        }
        this.mIVlogView.updateVlogBottomNaviView(((ClipMenuPresenter) this.mMenuPresenter).isSingleVideoEdit());
        VlogUtils.showViews(this.mNavLayout, this.mClipEditNavView);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void exitEditMode() {
        getIVlogView().updateVlogBottomNaviView(true);
        VlogUtils.hideViews(this.mNavLayout);
        setSpeedViewVisible(false);
        this.mMultiVideoEditView.setListener(null);
        this.mMultiVideoEditView.setClipMode(false);
        this.mMultiVideoEditView.setListener(this.mMultiVideoEditListener);
        setPlayViewVisible(true);
        setTimeViewVisible(true);
        setSpeedViewVisible(false);
        updateDisplayOperationView(false);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void seekMultiVideoEditView(long j) {
        MultiVideoEditView multiVideoEditView = this.mMultiVideoEditView;
        if (multiVideoEditView != null) {
            multiVideoEditView.seekTo(j, true);
        }
    }

    public void updatePlayViewState(boolean z) {
        if (this.mPlayView.isSelected() != z) {
            this.mPlayView.setSelected(z);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackStopped() {
        super.onPlaybackStopped();
        updatePlayViewState(false);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackEOF() {
        super.onPlaybackEOF();
        updatePlayViewState(false);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void updateCurrentTime(long j) {
        if (j > ((ClipMenuPresenter) this.mMenuPresenter).getDuration()) {
            return;
        }
        this.mTimeView.updateCurrentTime(j);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onSeek(long j) {
        updateCurrentTime(j);
    }

    public void updateAllTime(long j) {
        this.mTimeView.updateAllTime(j);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void updateMultiVideoEditView() {
        MultiVideoEditView multiVideoEditView = this.mMultiVideoEditView;
        if (multiVideoEditView == null) {
            return;
        }
        multiVideoEditView.updateVideoClipList(((ClipMenuPresenter) this.mMenuPresenter).getVideoClips());
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public MultiVideoEditView getMultiVideoEditView() {
        return this.mMultiVideoEditView;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void updateClipList() {
        updateMultiVideoEditView();
        updateAllTime(((ClipMenuPresenter) this.mMenuPresenter).getDuration());
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackTimelinePosition(long j) {
        this.mTimeView.updateCurrentTime(j);
        if (getMiVideoSdkManager().getTimelineStatus() != 2) {
            this.mMultiVideoEditView.seekTo(j);
        }
        if (this.mClipEditNavView.getVisibility() != 8) {
            this.mClipEditNavView.updateReverseItemState(((ClipMenuPresenter) this.mMenuPresenter).getCurrentVideoClip());
            this.mClipEditNavView.updateSpeedXItemState(((ClipMenuPresenter) this.mMenuPresenter).getCurrentVideoClip());
        }
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void onInsertVideoCliped(long j) {
        updateMultiVideoEditView();
        seekMultiVideoEditView(j);
        updateAllTime(((ClipMenuPresenter) this.mMenuPresenter).getDuration());
        getClipEditNavView().updateDeleteItemState();
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void onDeleted(IVideoClip iVideoClip, long j) {
        updateMultiVideoEditView();
        seekMultiVideoEditView(j);
        this.mClipEditNavView.updateDeleteItemState();
        this.mClipEditNavView.updateSpeedXItemState(((ClipMenuPresenter) this.mMenuPresenter).getCurrentVideoClip());
        updateAllTime(((ClipMenuPresenter) this.mMenuPresenter).getDuration());
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void onCuted(IVideoClip iVideoClip, long j) {
        updateCurrentTime(j);
        getClipEditNavView().updateDeleteItemState();
        showTimeView(true);
        updateMultiVideoEditView();
        seekMultiVideoEditView(j);
        getClipEditNavView().updateSpeedXItemState(iVideoClip);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void onChangeSpeed(IVideoClip iVideoClip, long j) {
        updateAllTime(((ClipMenuPresenter) this.mMenuPresenter).getDuration());
        updateCurrentTime(j);
        updateMultiVideoEditView();
        seekMultiVideoEditView(j);
        getClipEditNavView().updateSpeedXItemState(iVideoClip);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void onSingleVideoInit() {
        this.mConsLayout.removeView(this.mAddClipsView);
    }

    public void hideOperationBar() {
        setPlayViewVisible(false);
        setTimeViewVisible(false);
        setSpeedViewVisible(false);
    }

    public void setPlayViewVisible(boolean z) {
        this.mPlayViewLayout.setVisibility(z ? 0 : 8);
    }

    public void setTimeViewVisible(boolean z) {
        this.mTimeView.setVisibility(z ? 0 : 8);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void setSpeedViewVisible(boolean z) {
        if (z) {
            this.mSpeedSwitchLayout.setVisibility(0);
            setPlayViewVisible(false);
            setTimeViewVisible(false);
            updateDisplayOperationView(true);
            return;
        }
        this.mSpeedSwitchLayout.setVisibility(8);
        setPlayViewVisible(true);
        setTimeViewVisible(true);
        this.mIVlogView.updateDisplayOperationView(this.mOperationView, false);
        getMenuTopView().removeView(this.mSpeedSwitchLayout);
        if (this.mOperationView.getParent() != null) {
            return;
        }
        setMenuTopView();
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public boolean isSpeedViewVisible() {
        return this.mSpeedSwitchLayout.getVisibility() == 0;
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void setSpeed(double d) {
        this.mSpeedSwitchView.setOnSelectedListener(null);
        DefaultLogger.d("ClipMenuFragment", "updateSpeedState: speed = %s", Double.valueOf(d));
        if (d == 3.0d) {
            this.mSpeedSwitchView.setCurSelected(0);
        } else if (d == 2.0d) {
            this.mSpeedSwitchView.setCurSelected(1);
        } else if (d == 1.0d) {
            this.mSpeedSwitchView.setCurSelected(2);
        } else if (d == 0.5d) {
            this.mSpeedSwitchView.setCurSelected(3);
        } else if (d == 0.25d) {
            this.mSpeedSwitchView.setCurSelected(4);
        } else {
            this.mSpeedSwitchView.setCurSelected(2);
        }
        this.mSpeedSwitchView.setOnSelectedListener(this.mOnSelectedListener);
    }

    @Override // com.miui.gallery.vlog.clip.ClipMenuContract$IClipMenuView
    public void setSpeedItemEnable(double d) {
        double d2 = SPEED[this.mSpeedSwitchView.getSelectedIndex()];
        for (int i = 0; i < 5; i++) {
            if (i != 2) {
                this.mSpeedSwitchView.setEnableViewByIndex(i, !(d / (SPEED[i] / d2) < 500.0d));
            }
        }
    }

    public /* synthetic */ void lambda$new$0(int i, int i2, String str) {
        ((ClipMenuPresenter) this.mMenuPresenter).doSpeed(i2 == 0 ? 3.0d : i2 == 1 ? 2.0d : i2 == 2 ? 1.0d : i2 == 3 ? 0.5d : 0.25d);
    }

    public void updateDisplayOperationView(boolean z) {
        if (z) {
            setPlayViewVisible(true);
            setTimeViewVisible(false);
            getMenuTopView().removeAllViews();
            if (this.mOperationView.getParent() == null) {
                this.mIVlogView.updateDisplayOperationView(this.mOperationView, true);
            }
            this.mOperationView.removeView(this.mSpeedSwitchLayout);
            if (this.mSpeedSwitchLayout.getParent() != null) {
                return;
            }
            this.mIVlogView.setTopView(this.mSpeedSwitchLayout);
            return;
        }
        setPlayViewVisible(true);
        setTimeViewVisible(true);
        this.mIVlogView.updateDisplayOperationView(this.mOperationView, false);
        getMenuTopView().removeView(this.mSpeedSwitchLayout);
        if (this.mSpeedSwitchLayout.getParent() == null) {
            this.mOperationView.addView(this.mSpeedSwitchLayout);
        }
        if (this.mOperationView.getParent() != null) {
            return;
        }
        setMenuTopView();
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public boolean onBackPressed() {
        if (this.mTransResView.getVisibility() == 0) {
            this.mTransResView.doCancelEvent();
            return true;
        } else if (this.mTimelineSortView.getVisibility() == 0) {
            this.mTimelineSortView.exit(false);
            return true;
        } else if (this.mNavLayout.getVisibility() == 0 && !((ClipMenuPresenter) this.mMenuPresenter).isSingleVideoEdit()) {
            exitEditMode();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    @Override // com.miui.gallery.vlog.sdk.callbacks.ExportCallback2
    public void onExportStart() {
        this.mMultiVideoEditView.stopScroll();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mMiVideoSdkManager.removeExportCallback2();
        MultiVideoEditView multiVideoEditView = this.mMultiVideoEditView;
        if (multiVideoEditView != null) {
            multiVideoEditView.setListener(null);
        }
        EditTimelineSortView editTimelineSortView = this.mTimelineSortView;
        if (editTimelineSortView != null) {
            editTimelineSortView.setSortCallback(null);
        }
        TransResView transResView = this.mTransResView;
        if (transResView != null) {
            transResView.setCallback(null);
        }
    }

    public static boolean containSpeed(double d) {
        return Arrays.toString(SPEED).contains(String.valueOf(d)) && d != 1.0d;
    }
}
