package com.miui.gallery.vlog.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.VlogActivity;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.interfaces.IVlogActivity;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.base.widget.DisplayView;
import com.miui.gallery.vlog.base.widget.VlogPlayView;
import com.miui.gallery.vlog.entity.VideoClip;
import com.miui.gallery.vlog.home.VlogSavingFragment;
import com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback;
import com.miui.gallery.vlog.sdk.callbacks.SeekCallback;
import com.miui.gallery.vlog.sdk.interfaces.IAudioManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoAudioManager;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.mediaeditor.utils.FilePermissionUtils;
import com.xiaomi.milab.videosdk.interfaces.SurfaceCreatedCallback;
import java.io.File;
import java.util.List;

/* loaded from: classes2.dex */
public class VlogPresenter extends BasePresenter implements SurfaceCreatedCallback {
    public IVlogActivity mIVlogActivity;
    public VlogContract$IVlogModel mIVlogModel;
    public VlogContract$IVlogView mIVlogView;
    public Intent mIntent;
    public boolean mIsProjectLoaded;
    public VlogSavingFragment mVlogSavingFragment;

    public VlogPresenter(Context context, VlogContract$IVlogView vlogContract$IVlogView, IVlogActivity iVlogActivity) {
        super(context);
        this.mIVlogView = vlogContract$IVlogView;
        this.mIVlogActivity = iVlogActivity;
    }

    public void parseData(Intent intent) {
        this.mIntent = intent;
        this.mIVlogModel = initVlogModel(this.mContext);
    }

    public void verifyData() {
        if (!BaseMiscUtil.isValid(this.mIVlogModel.getVideoClips())) {
            this.mIVlogView.showToast(this.mContext.getString(R$string.vlog_video_selected_not_support));
            this.mIVlogActivity.onInitFailed();
        }
    }

    public void loadProject() {
        if (getSdkManager() == null || this.mIVlogView == null || this.mIsProjectLoaded) {
            return;
        }
        List<VideoClip> videoClips = this.mIVlogModel.getVideoClips();
        if (!BaseMiscUtil.isValid(videoClips)) {
            return;
        }
        this.mIsProjectLoaded = true;
        DisplayView displayView = (DisplayView) this.mIVlogView.getLiveWindow();
        displayView.setCreatedLister(this);
        getSdkManager().setDisplayView(displayView);
        getSdkManager().initOriginalSize(videoClips.get(0).getWidth(), videoClips.get(0).getHeight());
    }

    public void addPlayCallback(PlaybackCallback playbackCallback) {
        getSdkManager().addPlayCallback(playbackCallback);
    }

    public void removePlayCallback(PlaybackCallback playbackCallback) {
        getSdkManager().removePlayCallback(playbackCallback);
    }

    public void doPlayViewClickEvent() {
        if (getSdkManager().isPlay()) {
            getSdkManager().pause();
        } else {
            getSdkManager().resume();
        }
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void setPlayViewProgress(long j) {
        float duration = ((float) j) / ((float) getSdkManager().getDuration());
        VlogPlayView vlogPlayView = this.mIVlogView.getVlogPlayView();
        if (vlogPlayView == null || vlogPlayView.getDisplayView() == null) {
            return;
        }
        vlogPlayView.updatePlayProgress(duration);
    }

    public void doBackViewClickEvent() {
        IVlogActivity iVlogActivity = this.mIVlogActivity;
        if (iVlogActivity != null) {
            iVlogActivity.onBack();
        }
    }

    public void doVideoVoiceBtnClickEvent(boolean z) {
        MiVideoAudioManager miVideoAudioManager = (MiVideoAudioManager) this.mIVlogModel.getSdkManager().getManagerService(2);
        if (z) {
            miVideoAudioManager.setVideoTrackVolumeGain(100.0f, 100.0f);
        } else {
            miVideoAudioManager.setVideoTrackVolumeGain(0.0f, 0.0f);
        }
    }

    public void doExit() {
        this.mIVlogModel.getSdkManager().pause();
    }

    public void doSaveViewClickEvent() {
        if (!FilePermissionUtils.checkFileCreatePermission((FragmentActivity) this.mContext, getIVlogModel().getOutFilePath())) {
            return;
        }
        if (this.mVlogSavingFragment == null) {
            this.mVlogSavingFragment = new VlogSavingFragment();
        }
        if (isSaving()) {
            return;
        }
        getIVlogModel().setSaveStatus(true);
        final long currentTimeMillis = System.currentTimeMillis();
        this.mVlogSavingFragment.setOnSavingFinishListener(new VlogSavingFragment.OnSavingFinishListener() { // from class: com.miui.gallery.vlog.home.VlogPresenter.1
            @Override // com.miui.gallery.vlog.home.VlogSavingFragment.OnSavingFinishListener
            public void onCanceled() {
                VlogPresenter.this.getIVlogModel().setSaveStatus(false);
                MiVideoSdkManager sdkManager = VlogPresenter.this.getSdkManager();
                if (sdkManager != null) {
                    sdkManager.cancelExport();
                }
            }

            @Override // com.miui.gallery.vlog.home.VlogSavingFragment.OnSavingFinishListener
            public void onFinish(boolean z, String str) {
                VlogPresenter.this.getIVlogModel().setSaveStatus(false);
                if (TextUtils.isEmpty(str)) {
                    DefaultLogger.d("VlogPresenter_", "vlog export fail, the outFile is null.");
                } else if (z) {
                    VlogPresenter.this.mVlogSavingFragment.setOnSavingFinishListener(null);
                    DefaultLogger.d("VlogPresenter_", "vlog export outFile: %s , vlog export time : %s  ms.", str, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    if (VlogPresenter.this.mIVlogActivity == null) {
                        return;
                    }
                    VlogPresenter.this.mIVlogActivity.onSave(z, str);
                } else {
                    DefaultLogger.d("VlogPresenter_", "vlog export fail:.");
                }
            }
        });
        this.mVlogSavingFragment.export(this.mIVlogModel.getFragmentManager(), getSdkManager());
    }

    public void doDownloadMediaEditorAppClickEvent(VlogDependsModule.Callback callback) {
        if (this.mIVlogActivity instanceof VlogActivity) {
            ((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).installIfNotExist((FragmentActivity) this.mIVlogActivity, callback, true);
        }
    }

    public void removeInstallListener() {
        ((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).removeInstallListener();
    }

    public void cancelSaveEvent() {
        VlogSavingFragment vlogSavingFragment = this.mVlogSavingFragment;
        if (vlogSavingFragment != null) {
            vlogSavingFragment.cancelExport();
        }
    }

    public final MiVideoSdkManager getSdkManager() {
        VlogContract$IVlogModel vlogContract$IVlogModel = this.mIVlogModel;
        if (vlogContract$IVlogModel == null) {
            return null;
        }
        return vlogContract$IVlogModel.getSdkManager();
    }

    public final VlogContract$IVlogModel initVlogModel(Context context) {
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel(fragmentActivity, VlogModel.class);
            vlogModel.initData(fragmentActivity, this.mIntent);
            return vlogModel;
        }
        return null;
    }

    public void doSave(String str) {
        if (this.mIVlogModel.isSingleVideoEdit()) {
            Intent intent = new Intent();
            intent.setData(VlogUtils.translateToContent(str));
            this.mIVlogActivity.onSaved(true, intent);
            return;
        }
        VlogDependsModule vlogDependsModule = (VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class);
        if (vlogDependsModule == null || TextUtils.isEmpty(str)) {
            return;
        }
        Intent intent2 = new Intent((FragmentActivity) this.mIVlogActivity, vlogDependsModule.getPhotoPagerClass());
        intent2.setData(Uri.fromFile(new File(str)));
        intent2.putExtra("com.miui.gallery.extra.deleting_include_cloud", true);
        this.mIVlogActivity.onSaved(false, intent2);
    }

    public void setSeekCallback(SeekCallback seekCallback) {
        getSdkManager().setSeekCallback(seekCallback);
    }

    public VlogContract$IVlogModel getIVlogModel() {
        return this.mIVlogModel;
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
        MiVideoSdkManager sdkManager = getSdkManager();
        if (sdkManager != null) {
            sdkManager.onDestroy();
        }
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.SurfaceCreatedCallback
    public void SurfaceCreated() {
        DefaultLogger.d("VlogPresenter_", "SurfaceCreated");
        if (this.mIVlogModel.getSdkManager().getmXmsTimeline() == null || this.mIVlogModel.getSdkManager().isReleased()) {
            return;
        }
        List<VideoClip> videoClips = this.mIVlogModel.getVideoClips();
        getSdkManager().initializeSurface((DisplayView) this.mIVlogView.getLiveWindow());
        getSdkManager().appendClipParcels(videoClips);
        MiVideoSdkManager sdkManager = getSdkManager();
        this.mIVlogView.updateClipList();
        IAudioManager iAudioManager = (IAudioManager) sdkManager.getManagerService(2);
        if (iAudioManager != null) {
            iAudioManager.setVideoTrackVolumeGain(100.0f, 100.0f);
            iAudioManager.setAudioTrackVolumeGain(100.0f, 100.0f);
        }
        if (this.mIVlogModel.isSingleVideoEdit()) {
            getSdkManager().playAndStopTimeline(0L);
        } else {
            getSdkManager().play();
        }
    }

    public void updateLiveWindowLayout() {
        getSdkManager().updateLiveWindowLayoutAfterScreenSizeChanged();
    }

    public void updateScreenRelatedValues() {
        this.mIVlogModel.initScreenRelatedValues();
    }
}
