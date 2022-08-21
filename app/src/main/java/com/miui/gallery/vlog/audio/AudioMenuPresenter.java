package com.miui.gallery.vlog.audio;

import android.content.Context;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.audio.AudioMenuModel;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.entity.AudioClip;
import com.miui.gallery.vlog.entity.AudioData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IAudioManager;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miuix.core.util.FileUtils;

/* loaded from: classes2.dex */
public class AudioMenuPresenter extends BasePresenter {
    public final String TAG;
    public IAudioManager mAudioManager;
    public AudioMenuContract$IAudioMenuView mIAudioMenuView;
    public int mStopForApplyAvailableCount;
    public VlogModel mVlogModel;

    public AudioMenuPresenter(Context context, AudioMenuContract$IAudioMenuView audioMenuContract$IAudioMenuView, AudioZipFileConfig audioZipFileConfig) {
        super(context);
        this.TAG = "AudioMenuPresenter";
        this.mIAudioMenuView = audioMenuContract$IAudioMenuView;
        this.mIBaseModel = new AudioMenuModel(new AudioMenuModel.Callback() { // from class: com.miui.gallery.vlog.audio.AudioMenuPresenter.1
            @Override // com.miui.gallery.vlog.audio.AudioMenuModel.Callback
            public void loadDataSuccess(List<AudioData> list) {
                AudioMenuPresenter.this.refreshData(list);
                if (list != null && list.size() > 0) {
                    list.add(1, AudioData.getLocalItem());
                }
                AudioMenuPresenter.this.mIAudioMenuView.loadRecyclerView(list);
            }

            @Override // com.miui.gallery.vlog.audio.AudioMenuModel.Callback
            public void loadDataFail() {
                ToastUtils.makeText(AudioMenuPresenter.this.mContext, AudioMenuPresenter.this.mContext.getString(R$string.vlog_download_failed_for_notwork));
                ArrayList arrayList = new ArrayList();
                arrayList.add(AudioData.getDefaultItem());
                arrayList.add(AudioData.getLocalItem());
                AudioMenuPresenter.this.mIAudioMenuView.loadRecyclerView(arrayList);
            }
        });
        VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class);
        this.mVlogModel = vlogModel;
        this.mAudioManager = (IAudioManager) vlogModel.getSdkManager().getManagerService(2);
    }

    public boolean hasAudioLabel() {
        return !TextUtils.isEmpty(this.mAudioManager.getAudioLabel());
    }

    public void refreshData(List<AudioData> list) {
        Collections.sort(list);
        for (AudioData audioData : list) {
            if (audioData != null) {
                String str = VlogConfig.AUDIO_PATH + File.separator + audioData.getFileName();
                if (new File(str).exists()) {
                    audioData.setDownloadState(17);
                    audioData.setAudioPath(str);
                }
            }
        }
    }

    public void doPlayViewClickEvent() {
        if (this.mVlogModel.getSdkManager().isPlay()) {
            this.mIAudioMenuView.updatePlayViewState(false);
            this.mVlogModel.getSdkManager().pause();
            return;
        }
        this.mIAudioMenuView.updatePlayViewState(true);
        this.mVlogModel.getSdkManager().resume();
    }

    public void play() {
        this.mVlogModel.getSdkManager().play();
    }

    public void updateSelectedItem() {
        this.mIAudioMenuView.updateSelectedItem(this.mAudioManager.getAudioPath());
    }

    public int findIndexByAudioPath(List<AudioData> list) {
        String audioPath = this.mAudioManager.getAudioPath();
        if (!TextUtils.isEmpty(audioPath) && BaseMiscUtil.isValid(list)) {
            for (AudioData audioData : list) {
                if (audioData != null && audioPath.equals(audioData.getAudioPath())) {
                    return list.indexOf(audioData);
                }
            }
        }
        return -1;
    }

    public boolean isSupportMusic(String str) {
        String lowerCase = FileUtils.getExtension(str).toLowerCase();
        DefaultLogger.d("AudioMenuPresenter", "isSupportVideo extension=" + lowerCase);
        return "mp3".equals(lowerCase) || "aac".equals(lowerCase);
    }

    public final void printLogStart() {
        DebugLogUtils.HAS_LOADED_SELECT_MUSIC = false;
        DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_MUSIC = false;
        DebugLogUtils.startDebugLogSpecialTime("AudioMenuPresenter", "vlog applyAudio");
    }

    public AudioClip applyAudio(String str) {
        printLogStart();
        this.mStopForApplyAvailableCount = 2;
        this.mVlogModel.getSdkManager().disconnect();
        AudioClip applyAudio = this.mAudioManager.applyAudio(str, "");
        this.mVlogModel.getSdkManager().reconnect();
        DebugLogUtils.HAS_LOADED_SELECT_MUSIC = true;
        return applyAudio;
    }

    public void updateApplyViewPlayState() {
        this.mIAudioMenuView.updatePlayViewState(this.mVlogModel.getSdkManager().isPlay());
    }

    public boolean hasVideoVoice() {
        return this.mAudioManager.getVideoTrackVolumeGain() != 0.0f;
    }

    public void removeAudio() {
        this.mAudioManager.removeAudio();
        this.mVlogModel.getSdkManager().resume();
    }

    public void audioClip(long j, long j2) {
        this.mVlogModel.getSdkManager().disconnect();
        this.mAudioManager.audioClip(j, j2);
        this.mVlogModel.getSdkManager().reconnect();
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
        IBaseModel iBaseModel = this.mIBaseModel;
        if (iBaseModel instanceof AudioMenuModel) {
            ((AudioMenuModel) iBaseModel).setCallback(null);
            ((AudioMenuModel) this.mIBaseModel).clear();
        }
    }
}
