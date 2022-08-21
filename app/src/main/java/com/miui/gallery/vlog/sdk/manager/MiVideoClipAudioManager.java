package com.miui.gallery.vlog.sdk.manager;

import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.sdk.interfaces.BaseVlogManager;
import com.miui.gallery.vlog.sdk.interfaces.IClipAudioManager;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.xiaomi.milab.videosdk.AudioExtraction;
import com.xiaomi.milab.videosdk.XmsContext;
import com.xiaomi.milab.videosdk.XmsTimeline;
import com.xiaomi.milab.videosdk.interfaces.AudioExtractCallback;

/* loaded from: classes2.dex */
public class MiVideoClipAudioManager extends BaseVlogManager implements IClipAudioManager {
    public IClipAudioManager.AudioConvertCallback mAudioConvertCallback;
    public AudioExtractCallback mAudioExtractCallback;
    public AudioExtraction mAudioExtraction;
    public String mDst;

    public MiVideoClipAudioManager(MiVideoSdkManager miVideoSdkManager, XmsTimeline xmsTimeline) {
        super(miVideoSdkManager, xmsTimeline);
        this.mAudioExtractCallback = new AudioExtractCallback() { // from class: com.miui.gallery.vlog.sdk.manager.MiVideoClipAudioManager.1
            @Override // com.xiaomi.milab.videosdk.interfaces.AudioExtractCallback
            public void onExtractProgress(int i) {
            }

            @Override // com.xiaomi.milab.videosdk.interfaces.AudioExtractCallback
            public void onExtractFinished() {
                if (MiVideoClipAudioManager.this.mAudioConvertCallback != null) {
                    MiVideoClipAudioManager.this.mAudioConvertCallback.onFinish(0L, null, MiVideoClipAudioManager.this.mDst);
                }
            }
        };
        this.mAudioExtraction = new AudioExtraction();
        XmsContext.getInstance().setAudioExtractCallback(this.mAudioExtractCallback);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IClipAudioManager
    public void setAudioConvertCallback(IClipAudioManager.AudioConvertCallback audioConvertCallback) {
        this.mAudioConvertCallback = audioConvertCallback;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IClipAudioManager
    public long getAudio16ks16leAsync(IVideoClip iVideoClip, String str) {
        if (iVideoClip == null) {
            return -1L;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("MiVideoClipAudioManager", "getAudio16ks16leAsync"));
        if (documentFile != null) {
            documentFile.delete();
        }
        this.mDst = str;
        this.mAudioExtraction.setInputFile(iVideoClip.getFilePath());
        this.mAudioExtraction.setRegion((int) (iVideoClip.getTrimIn() / 1000), (int) (iVideoClip.getTrimOut() / 1000));
        this.mAudioExtraction.setOutputFile(str, 1, 16000);
        this.mAudioExtraction.extract();
        return 0L;
    }
}
