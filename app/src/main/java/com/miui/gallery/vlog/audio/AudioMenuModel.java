package com.miui.gallery.vlog.audio;

import com.miui.gallery.net.resource.CommonResponse;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.base.net.AudioRequest;
import com.miui.gallery.vlog.entity.AudioData;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class AudioMenuModel implements IBaseModel {
    public Callback mCallback;
    public ArrayList<AudioData> mAudioResources = new ArrayList<>();
    public CommonResponse.Callback<AudioData> mResourceCallback = new CommonResponse.Callback<AudioData>() { // from class: com.miui.gallery.vlog.audio.AudioMenuModel.1
        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onSuccess(List<AudioData> list) {
            if (BaseMiscUtil.isValid(list)) {
                DefaultLogger.d("AudioMenuModel", "load data success.");
                AudioMenuModel.this.mAudioResources.clear();
                AudioMenuModel.this.mAudioResources.add(AudioData.getDefaultItem());
                AudioMenuModel.this.mAudioResources.addAll(list);
                if (AudioMenuModel.this.mCallback == null) {
                    return;
                }
                AudioMenuModel.this.mCallback.loadDataSuccess(AudioMenuModel.this.mAudioResources);
            }
        }

        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onFail() {
            DefaultLogger.e("AudioMenuModel", "load data fail.");
            if (AudioMenuModel.this.mCallback != null) {
                AudioMenuModel.this.mCallback.loadDataFail();
            }
        }
    };
    public AudioRequest mAudioRequest = new AudioRequest();
    public CommonResponse mCommonResponse = new CommonResponse(this.mResourceCallback);

    /* loaded from: classes2.dex */
    public interface Callback {
        void loadDataFail();

        void loadDataSuccess(List<AudioData> list);
    }

    public AudioMenuModel(Callback callback) {
        this.mCallback = callback;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IBaseModel
    public void loadData() {
        DefaultLogger.d("AudioMenuModel", "start load data.");
        this.mAudioRequest.execute(this.mCommonResponse);
    }

    public void clear() {
        if (this.mCallback != null) {
            this.mCallback = null;
        }
        CommonResponse commonResponse = this.mCommonResponse;
        if (commonResponse != null) {
            commonResponse.setCallback(null);
        }
        AudioRequest audioRequest = this.mAudioRequest;
        if (audioRequest != null) {
            audioRequest.cancel();
            this.mAudioRequest = null;
        }
    }
}
