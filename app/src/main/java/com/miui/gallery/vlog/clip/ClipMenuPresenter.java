package com.miui.gallery.vlog.clip;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.ITransManager;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;
import com.miui.gallery.vlog.sdk.manager.MiVideoClipManager;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.util.List;

/* loaded from: classes2.dex */
public class ClipMenuPresenter extends BasePresenter {
    public final String TAG;
    public MiVideoSdkManager.IVideoClipReverseCallback mCallback;
    public CancelReverseCallback mCancelReverseCallback;
    public ClipMenuModel mClipMenuModel;
    public ClipMenuContract$IClipMenuView mIClipMenuView;
    public MiVideoClipManager mNvsClipManager;
    public ITransManager mTransResManager;
    public VlogModel mVlogModel;

    /* loaded from: classes2.dex */
    public interface CancelReverseCallback {
        void onCancel();
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
    }

    public ClipMenuPresenter(Context context, ClipMenuContract$IClipMenuView clipMenuContract$IClipMenuView) {
        super(context);
        this.TAG = "ClipMenuPresenter";
        this.mCallback = new MiVideoSdkManager.IVideoClipReverseCallback() { // from class: com.miui.gallery.vlog.clip.ClipMenuPresenter.1
            @Override // com.miui.gallery.vlog.base.manager.MiVideoSdkManager.IVideoClipReverseCallback
            public void onSuccess(IVideoClip iVideoClip) {
                DebugLogUtils.endDebugLog("ClipMenuPresenter", "clip doReverse");
                int index = iVideoClip.getIndex();
                if (index > 0) {
                    ClipMenuPresenter clipMenuPresenter = ClipMenuPresenter.this;
                    clipMenuPresenter.updateVideoClipTransInfo(clipMenuPresenter.getVideoClip(index - 1));
                }
                ClipMenuPresenter clipMenuPresenter2 = ClipMenuPresenter.this;
                clipMenuPresenter2.updateVideoClipTransInfo(clipMenuPresenter2.getVideoClip(index));
                ClipMenuPresenter.this.mVlogModel.getSdkManager().reconnect();
                long inPoint = (long) (iVideoClip.getInPoint() + VlogConfig.sMicroSecondPerTwoPixel);
                ClipMenuPresenter.this.seek(inPoint);
                ClipMenuPresenter.this.mIClipMenuView.updateCurrentTime(inPoint);
                ClipMenuPresenter.this.mVlogModel.getSdkManager().pause();
                ClipMenuPresenter.this.mIClipMenuView.updateMultiVideoEditView();
                ClipMenuPresenter.this.mIClipMenuView.seekMultiVideoEditView(inPoint);
                ClipMenuPresenter.this.mIClipMenuView.getClipEditNavView().updateReverseItemState(iVideoClip);
            }

            @Override // com.miui.gallery.vlog.base.manager.MiVideoSdkManager.IVideoClipReverseCallback
            public void onFail() {
                DebugLogUtils.endDebugLog("ClipMenuPresenter", "clip doReverse");
                ClipMenuPresenter.this.mIClipMenuView.getIVlogView().showToast(ClipMenuPresenter.this.mContext.getString(R$string.vlog_reverse_fail));
            }
        };
        this.mCancelReverseCallback = new CancelReverseCallback() { // from class: com.miui.gallery.vlog.clip.ClipMenuPresenter.2
            @Override // com.miui.gallery.vlog.clip.ClipMenuPresenter.CancelReverseCallback
            public void onCancel() {
                ClipMenuPresenter.this.mIClipMenuView.getIVlogView().showToast(ClipMenuPresenter.this.mContext.getString(R$string.vlog_reverse_dialog_cancel));
            }
        };
        this.mIClipMenuView = clipMenuContract$IClipMenuView;
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        this.mClipMenuModel = (ClipMenuModel) VlogUtils.getViewModel(fragmentActivity, ClipMenuModel.class);
        VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel(fragmentActivity, VlogModel.class);
        this.mVlogModel = vlogModel;
        this.mNvsClipManager = (MiVideoClipManager) vlogModel.getSdkManager().getManagerService(3);
        this.mTransResManager = (ITransManager) this.mVlogModel.getSdkManager().getManagerService(9);
        this.mVlogModel.getSdkManager().setIVideoClipReverse(this.mCallback);
        this.mIClipMenuView.seekMultiVideoEditView(this.mVlogModel.getSdkManager().getCurrentTimeMicro());
        if (this.mVlogModel.getVideoSource() == VlogConfig.VideoSource.FROM_OUTER_VIDEO_EDITOR) {
            this.mIClipMenuView.onSingleVideoInit();
        }
        this.mVlogModel.getSdkManager().pause();
    }

    public List<IVideoClip> getVideoClips() {
        return this.mClipMenuModel.getVideoClips(this.mVlogModel.getSdkManager());
    }

    public VideoFrameLoader getVideoFrameLoader() {
        return this.mVlogModel.getSdkManager().getVideoFrameLoader();
    }

    public boolean isSingleVideoEdit() {
        return this.mVlogModel.isSingleVideoEdit();
    }

    public void exitTransMode() {
        this.mVlogModel.getSdkManager().pause();
    }

    public void onTimeLineDurationChanged() {
        this.mVlogModel.getSdkManager().onTimeLineDurationChanged();
    }

    public void doSpeed(final double d) {
        final IVideoClip currentVideoClip = getCurrentVideoClip();
        if (currentVideoClip != null) {
            this.mVlogModel.getSdkManager().doOperationCombined(new MiVideoSdkManager.IDoOperationCombined() { // from class: com.miui.gallery.vlog.clip.ClipMenuPresenter$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.vlog.base.manager.MiVideoSdkManager.IDoOperationCombined
                public final void combined() {
                    ClipMenuPresenter.this.lambda$doSpeed$0(currentVideoClip, d);
                }
            });
            long inPoint = (long) (currentVideoClip.getInPoint() + VlogConfig.sMicroSecondPerTwoPixel);
            seek(inPoint);
            this.mVlogModel.getSdkManager().pause();
            this.mIClipMenuView.onChangeSpeed(currentVideoClip, inPoint);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doSpeed$0(IVideoClip iVideoClip, double d) {
        this.mNvsClipManager.changeSpeed(iVideoClip.getIndex(), d);
        updateVideoClipTransInfo(iVideoClip);
        onTimeLineDurationChanged();
    }

    public void doCut() {
        IVideoClip iVideoClip;
        IVideoClip currentVideoClip = getCurrentVideoClip();
        if (currentVideoClip == null) {
            return;
        }
        int index = currentVideoClip.getIndex();
        this.mVlogModel.getSdkManager().disconnect();
        if (!this.mNvsClipManager.duCut(currentVideoClip, this.mIClipMenuView.getMultiVideoEditView().getSeekTime())) {
            this.mIClipMenuView.getIVlogView().showToast(VlogUtils.getGalleryApp().getString(R$string.vlog_clip_cut_fail_min_time, Double.valueOf(0.5d)));
            this.mVlogModel.getSdkManager().reconnect();
            return;
        }
        updateVideoClipTransInfo(getVideoClip(index));
        int i = index + 1;
        updateVideoClipTransInfo(getVideoClip(i));
        this.mVlogModel.getSdkManager().onTimeLineDurationChanged();
        this.mVlogModel.getSdkManager().reconnect();
        if (i >= getVideoClips().size() || (iVideoClip = getVideoClips().get(i)) == null) {
            return;
        }
        long inPoint = (long) (iVideoClip.getInPoint() + VlogConfig.sMicroSecondPerTwoPixel);
        seek(inPoint);
        this.mIClipMenuView.onCuted(iVideoClip, inPoint);
    }

    public void doReverse() {
        this.mIClipMenuView.showTimeView(true);
        IVideoClip currentVideoClip = getCurrentVideoClip();
        if (currentVideoClip == null) {
            return;
        }
        pauseVideo();
        int index = currentVideoClip.getIndex();
        this.mVlogModel.getSdkManager().disconnect();
        DebugLogUtils.startDebugLog("ClipMenuPresenter", "clip doReverse");
        this.mNvsClipManager.reverseClip(index, this.mCancelReverseCallback);
    }

    public void updateVideoClipTransInfo(IVideoClip iVideoClip) {
        if (iVideoClip == null) {
            return;
        }
        int index = iVideoClip.getIndex() + 1;
        String str = null;
        if (index < getVideoClips().size()) {
            IVideoClip iVideoClip2 = getVideoClips().get(index);
            if (iVideoClip2 == null) {
                return;
            }
            boolean isTransitionValid = iVideoClip.isTransitionValid(iVideoClip2, 1000000);
            ITransManager iTransManager = this.mTransResManager;
            int index2 = iVideoClip.getIndex();
            String transName = isTransitionValid ? iVideoClip.getTransName() : null;
            if (isTransitionValid) {
                str = iVideoClip.getTransParam();
            }
            iTransManager.buildTransitions(index2, transName, str);
            return;
        }
        this.mTransResManager.buildTransitions(iVideoClip.getIndex(), null, null);
    }

    public IVideoClip getVideoClip(int i) {
        List<IVideoClip> videoClips = getVideoClips();
        if (i < 0 || i >= videoClips.size()) {
            return null;
        }
        return videoClips.get(i);
    }

    public void onEnterEditMode(boolean z) {
        if (z) {
            this.mVlogModel.getSdkManager().pause();
            this.mIClipMenuView.enterEditState();
            return;
        }
        this.mIClipMenuView.exitEditMode();
    }

    public boolean sortVideoClip(int i, int i2) {
        return this.mNvsClipManager.sortVideoClip(i, i2);
    }

    public void insertVideoClips() {
        this.mVlogModel.getSdkManager().disconnect();
        int index = getCurrentVideoClip().getIndex();
        long insertVideoClips = this.mClipMenuModel.getNvsClipManager(this.mContext).insertVideoClips(this.mClipMenuModel.getAddedVideoClips());
        updateVideoClipTransInfo(getVideoClip(index));
        long j = (long) (insertVideoClips + VlogConfig.sMicroSecondPerTwoPixel);
        seek(j);
        this.mVlogModel.getSdkManager().reconnect();
        this.mIClipMenuView.onInsertVideoCliped(j);
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void setPlayViewProgress(long j) {
        if (this.mVlogModel.getIVlogView() != null) {
            this.mVlogModel.getIVlogView().setPlayViewProgress(j);
        }
    }

    public IVideoClip getCurrentVideoClip() {
        return this.mIClipMenuView.getMultiVideoEditView().getCurrentVideoClip();
    }

    public long getCurrentTime() {
        return this.mVlogModel.getSdkManager().getCurrentTimeMicro();
    }

    public ClipMenuModel getModel(Context context) {
        if (this.mClipMenuModel == null) {
            this.mClipMenuModel = (ClipMenuModel) VlogUtils.getViewModel((FragmentActivity) context, ClipMenuModel.class);
        }
        return this.mClipMenuModel;
    }

    public void readySpeed() {
        this.mVlogModel.getSdkManager().pause();
        this.mIClipMenuView.showTimeView(false);
    }

    public void resumeVideo() {
        this.mVlogModel.getSdkManager().resume();
    }

    public void pauseVideo() {
        this.mVlogModel.getSdkManager().pause();
    }

    public void doDelete() {
        long outPoint;
        this.mIClipMenuView.showTimeView(true);
        if (getVideoClips().size() <= 1) {
            return;
        }
        this.mVlogModel.getSdkManager().pause();
        IVideoClip currentVideoClip = getCurrentVideoClip();
        if (currentVideoClip == null) {
            return;
        }
        int index = currentVideoClip.getIndex();
        this.mVlogModel.getSdkManager().disconnect();
        if (this.mNvsClipManager.removeVideo(index)) {
            if (index == 0) {
                outPoint = 0;
                seek(0L);
                this.mIClipMenuView.updateCurrentTime(0L);
            } else {
                outPoint = this.mVlogModel.getSdkManager().getVideoClip(index - 1).getOutPoint();
                seek(outPoint);
                this.mIClipMenuView.updateCurrentTime(outPoint);
            }
            currentVideoClip.setDeleted(true);
            this.mIClipMenuView.onDeleted(currentVideoClip, outPoint);
            onTimeLineDurationChanged();
            if (index > 0) {
                updateVideoClipTransInfo(getVideoClip(index - 1));
            }
        }
        this.mVlogModel.getSdkManager().reconnect();
    }

    public long getDuration() {
        return this.mVlogModel.getSdkManager().getDuration();
    }
}
