package com.miui.gallery.vlog.caption;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.MenuFragment;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.interfaces.IVlogActivity;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.caption.CaptionListView;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.home.VlogPresenter;
import com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback;
import com.miui.gallery.vlog.sdk.callbacks.SimplePlaybackCallback;
import com.miui.gallery.vlog.sdk.interfaces.ICaptionManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoCaptionManager;
import com.miui.gallery.vlog.sdk.models.NvsCompoundCaptionWrapper;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;

/* loaded from: classes2.dex */
public class AddCaptionFragment extends MenuFragment implements View.OnClickListener {
    public TextView mAllTimeView;
    public Button mBtnAdd;
    public Callback mCallback;
    public ImageView mCancel;
    public CaptionListView mCaptionListView;
    public MiVideoCaptionManager mCaptionManager;
    public Context mContext;
    public TextView mCurrentTimeView;
    public MiVideoSdkManager mNvSdkManager;
    public ImageView mOk;
    public View mOperationView;
    public double mPixelPerMicroSeconds;
    public ImageView mPlay;
    public FrameLayout mPlayLayout;
    public ImageView mPlayView;
    public VlogPresenter mVlogPresenter;
    public View mWhiteView;
    public boolean mScrollFromCode = false;
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.caption.AddCaptionFragment.1
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (AddCaptionFragment.this.mCaptionListView.getChildAt(0).getWidth() > 0) {
                AddCaptionFragment.this.removeOnGlobalLayoutListener();
                AddCaptionFragment addCaptionFragment = AddCaptionFragment.this;
                addCaptionFragment.scrollThumbAndCaptionToTime(addCaptionFragment.getMiVideoSdkManager().getCurrentTimeMicro());
            }
        }
    };
    public CaptionListView.InteractionCallback mInteractionCallback = new CaptionListView.InteractionCallback() { // from class: com.miui.gallery.vlog.caption.AddCaptionFragment.2
        @Override // com.miui.gallery.vlog.caption.CaptionListView.InteractionCallback
        public void onCaptionDragRelease(int i) {
        }

        @Override // com.miui.gallery.vlog.caption.CaptionListView.InteractionCallback
        public void onSettleStatusChange(int i) {
        }

        @Override // com.miui.gallery.vlog.caption.CaptionListView.InteractionCallback
        public void onCaptionDragging(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper, int i, long j, long j2) {
            if (AddCaptionFragment.this.mCaptionManager != null) {
                AddCaptionFragment.this.mCaptionManager.updateCaptionInOutPoint(nvsCompoundCaptionWrapper, j, j2);
            }
            if (i == 1 || i == 0) {
                AddCaptionFragment.this.seek(nvsCompoundCaptionWrapper.mMiCaption.getInPoint());
            } else if (i != 2) {
            } else {
                AddCaptionFragment.this.seek(nvsCompoundCaptionWrapper.mMiCaption.getOutPoint() - 1000);
            }
        }

        @Override // com.miui.gallery.vlog.caption.CaptionListView.InteractionCallback
        public void onScrollChange(View view, int i, int i2) {
            if (!AddCaptionFragment.this.mScrollFromCode && AddCaptionFragment.this.mCaptionListView.getSettleStatus() == 0) {
                AddCaptionFragment.this.seek((long) Math.floor((i / AddCaptionFragment.this.mPixelPerMicroSeconds) + 0.5d));
            }
        }

        @Override // com.miui.gallery.vlog.caption.CaptionListView.InteractionCallback
        public void onSelectCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
            AddCaptionFragment.this.seek(nvsCompoundCaptionWrapper.mMiCaption.getInPoint());
            if (AddCaptionFragment.this.mCallback != null) {
                AddCaptionFragment.this.mCallback.onSelectCaption(nvsCompoundCaptionWrapper);
            }
        }

        @Override // com.miui.gallery.vlog.caption.CaptionListView.InteractionCallback
        public void onUnSelectCaption() {
            if (AddCaptionFragment.this.mCallback != null) {
                AddCaptionFragment.this.mCallback.onSelectCaption(null);
            }
        }
    };
    public PlaybackCallback mPlaybackCallback = new SimplePlaybackCallback() { // from class: com.miui.gallery.vlog.caption.AddCaptionFragment.3
        @Override // com.miui.gallery.vlog.sdk.callbacks.SimplePlaybackCallback, com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback
        public void onPlaybackStopped() {
            AddCaptionFragment.this.updatePlayViewState(false);
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback
        public void onPlaybackEOF() {
            AddCaptionFragment.this.updatePlayViewState(false);
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.SimplePlaybackCallback, com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback
        public void onPlaybackTimelinePositionMicro(long j) {
            AddCaptionFragment.this.updatePlayViewState(true);
            AddCaptionFragment.this.scrollThumbAndCaptionToTime(j);
            AddCaptionFragment.this.updateAddButton(j);
            AddCaptionFragment.this.updateTime(j);
            AddCaptionFragment.this.updatePlayViewIndicator(j);
        }
    };

    /* loaded from: classes2.dex */
    public interface Callback {
        void onAddCaption(long j);

        void onCancel();

        void onSave();

        void onSelectCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter */
    public BasePresenter mo1801createPresenter() {
        return null;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public boolean isSetTopMenuView() {
        return false;
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        return null;
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        if (context instanceof IVlogActivity) {
            this.mVlogPresenter = ((IVlogActivity) context).getVlogPresenter();
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R$layout.vlog_caption_add_layout, viewGroup, false);
        this.mNvSdkManager = ((VlogModel) VlogUtils.getViewModel(getActivity(), VlogModel.class)).getSdkManager();
        changeTitleViewStatus(true);
        return inflate;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        this.mCurrentTimeView = (TextView) view.findViewById(R$id.tv_time);
        this.mAllTimeView = (TextView) view.findViewById(R$id.tv_all_time);
        Button button = (Button) view.findViewById(R$id.btn_add);
        this.mBtnAdd = button;
        button.setOnClickListener(this);
        this.mCaptionListView = (CaptionListView) view.findViewById(R$id.caption_list);
        this.mWhiteView = view.findViewById(R$id.white_view);
        int i = R$id.cancel;
        this.mCancel = (ImageView) view.findViewById(i);
        int i2 = R$id.ok;
        this.mOk = (ImageView) view.findViewById(i2);
        this.mCancel = (ImageView) view.findViewById(i);
        this.mOk = (ImageView) view.findViewById(i2);
        this.mPlay = (ImageView) view.findViewById(R$id.iv_vlog_play);
        this.mCancel.setOnClickListener(this);
        this.mOk.setOnClickListener(this);
        this.mPlay.setOnClickListener(this);
        View generateOperationView = generateOperationView();
        this.mOperationView = generateOperationView;
        this.mIVlogView.updateDisplayOperationView(generateOperationView, true);
        FolmeUtilsEditor.animButton(this.mOk);
        FolmeUtilsEditor.animButton(this.mCancel);
        this.mPixelPerMicroSeconds = VlogConfig.sPixelPerMicroSecond;
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mCaptionListView);
        view.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        initCaptionBarView();
        FolmeUtil.setCustomTouchAnim(this.mPlay, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mBtnAdd, new AnimParams.Builder().setAlpha(1.0f).setTint(0.08f, 0.0f, 0.0f, 0.0f).setScale(0.85f).build(), null, null, true);
    }

    public final void initCaptionBarView() {
        this.mCaptionListView.setPixelPerMicroSeconds(VlogConfig.sPixelPerMicroSecond);
        this.mCaptionListView.setVideoDuration(this.mNvSdkManager.getDuration());
        this.mCaptionListView.setCaptions(this.mCaptionManager.getAllCaptions());
        this.mCaptionListView.setThumbnailImageList(getMiVideoSdkManager().getThumbnailImages());
        this.mCaptionListView.setInteractionCallback(this.mInteractionCallback);
    }

    public final View generateOperationView() {
        if (this.mOperationView == null) {
            View inflate = LayoutInflater.from(getContext()).inflate(R$layout.vlog_add_caption_operation_layout, (ViewGroup) null, false);
            this.mOperationView = inflate;
            FrameLayout frameLayout = (FrameLayout) inflate.findViewById(R$id.play_layout);
            this.mPlayLayout = frameLayout;
            frameLayout.setOnClickListener(this);
            FolmeUtil.setCustomTouchAnim(this.mPlayLayout, new AnimParams.Builder().setAlpha(0.6f).setScale(1.0f).build(), null, null, true);
            this.mPlayView = (ImageView) this.mOperationView.findViewById(R$id.play_view);
        }
        return this.mOperationView;
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        getMiVideoSdkManager().addPlayCallback(this.mPlaybackCallback);
        updateTime(getMiVideoSdkManager().getCurrentTimeMicro());
        showTimeView(false);
        updateAddButton(getMiVideoSdkManager().getCurrentTimeMicro());
        getMiVideoSdkManager().pause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        changeTitleViewStatus(false);
        super.onDestroyView();
    }

    public final void removeOnGlobalLayoutListener() {
        getView().getViewTreeObserver().removeOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
    }

    public final void showTimeView(boolean z) {
        getIVlogView().updateTimeView(z);
    }

    public final void updateTime(long j) {
        getIVlogView().updateTimeView(false);
        MiVideoSdkManager miVideoSdkManager = getMiVideoSdkManager();
        this.mCurrentTimeView.setText(VlogUtils.getFormatTime(j / 1000));
        if (miVideoSdkManager != null) {
            this.mAllTimeView.setText(VlogUtils.getFormatTime(miVideoSdkManager.getDuration() / 1000));
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            changeTitleViewStatus(true);
            getMiVideoSdkManager().addPlayCallback(this.mPlaybackCallback);
            updateAddButton(getMiVideoSdkManager().getCurrentTimeMicro());
            updatePlayViewIndicator(getMiVideoSdkManager().getCurrentTimeMicro());
            this.mCaptionListView.reInit();
            this.mCaptionListView.setVideoDuration(getMiVideoSdkManager().getDuration());
            this.mCaptionListView.setCaptions(this.mCaptionManager.getAllCaptions());
            this.mCaptionListView.setThumbnailImageList(getMiVideoSdkManager().getThumbnailImages());
            scrollThumbAndCaptionToTime(getMiVideoSdkManager().getCurrentTimeMicro());
            getMiVideoSdkManager().pause();
            updateTime(getMiVideoSdkManager().getCurrentTimeMicro());
            return;
        }
        this.mCaptionListView.release();
        getMiVideoSdkManager().removePlayCallback(this.mPlaybackCallback);
    }

    public void changeTitleViewStatus(boolean z) {
        this.mIVlogView.updateDisplayOperationView(this.mOperationView, z);
        if (z) {
            getIVlogView().hideApplyView();
            getIVlogView().showCustomTitleView(getTitleViewWithCustomTitle(getContext().getResources().getString(R$string.vlog_caption_add)));
            getIVlogView().setPlayProgressEnable(false);
            getIVlogView().hideProgressView();
            return;
        }
        getIVlogView().hideCustomTitleView();
        getIVlogView().showApplyView();
        getIVlogView().setPlayProgressEnable(true);
    }

    public final void seek(long j) {
        long validTime = validTime(j);
        this.mNvSdkManager.seek(validTime);
        updatePlayViewIndicator(validTime);
        updateAddButton(validTime);
        updateTime(validTime);
    }

    public final long validTime(long j) {
        return Math.max(0L, Math.min(j, getMiVideoSdkManager().getDuration() - 1));
    }

    public final void updatePlayViewIndicator(long j) {
        getIVlogView().setPlayViewProgress(j);
    }

    public final void updateAddButton(long j) {
        if (this.mCaptionManager.checkAddCaptionStatusAtTime(j) == 0) {
            this.mBtnAdd.setEnabled(true);
        } else {
            this.mBtnAdd.setEnabled(false);
        }
    }

    public final void scrollThumbAndCaptionToTime(long j) {
        this.mScrollFromCode = true;
        this.mCaptionListView.scrollTo((int) (j * this.mPixelPerMicroSeconds), 0);
        this.mScrollFromCode = false;
    }

    public boolean onCancelPressed() {
        changeTitleViewStatus(false);
        this.mCaptionManager.revoke();
        hideSelf();
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCancel();
            return true;
        }
        return true;
    }

    public void onSavePressed() {
        changeTitleViewStatus(false);
        hideSelf();
        this.mCaptionManager.dropSnapshot();
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onSave();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Callback callback;
        if (view.getId() == R$id.btn_add) {
            getMiVideoSdkManager().pause();
            long currentTimeMicro = getMiVideoSdkManager().getCurrentTimeMicro();
            LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_LIGHT);
            if (this.mCaptionManager.checkAddCaptionStatusAtTime(currentTimeMicro) != 0 || (callback = this.mCallback) == null) {
                return;
            }
            callback.onAddCaption(currentTimeMicro);
        } else if (view.getId() == R$id.cancel) {
            onCancelPressed();
        } else if (view.getId() == R$id.ok) {
            onSavePressed();
        } else if ((view.getId() != R$id.iv_vlog_play && view.getId() != R$id.play_layout) || this.mVlogPresenter == null) {
        } else {
            updatePlayViewState(!this.mNvSdkManager.isPlay());
            this.mCaptionListView.unSelectCaption();
            this.mVlogPresenter.doPlayViewClickEvent();
        }
    }

    public final void hideSelf() {
        getFragmentManager().beginTransaction().hide(this).commit();
    }

    public void setCaptionManager(ICaptionManager iCaptionManager) {
        this.mCaptionManager = (MiVideoCaptionManager) iCaptionManager;
    }

    public void updatePlayViewState(boolean z) {
        ImageView imageView = this.mPlay;
        if (imageView != null && imageView.isSelected() != z) {
            this.mPlay.setSelected(z);
        }
        ImageView imageView2 = this.mPlayView;
        if (imageView2 == null || imageView2.isSelected() == z) {
            return;
        }
        this.mPlayView.setSelected(z);
    }

    public void onClearCaptions() {
        CaptionListView captionListView = this.mCaptionListView;
        if (captionListView != null) {
            captionListView.clearCaptions();
        }
        updateAddButton(getMiVideoSdkManager().getCurrentTimeMicro());
    }

    public void onDeleteCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        CaptionListView captionListView = this.mCaptionListView;
        if (captionListView != null) {
            captionListView.deleteCaption(nvsCompoundCaptionWrapper);
        }
        updateAddButton(getMiVideoSdkManager().getCurrentTimeMicro());
    }

    public void onEditCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        CaptionListView captionListView = this.mCaptionListView;
        if (captionListView != null) {
            captionListView.updateCaption(nvsCompoundCaptionWrapper);
        }
    }

    public void onAddCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        CaptionListView captionListView = this.mCaptionListView;
        if (captionListView != null) {
            captionListView.addCaption(nvsCompoundCaptionWrapper);
        }
        updateAddButton(getMiVideoSdkManager().getCurrentTimeMicro());
    }
}
