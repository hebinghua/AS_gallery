package com.miui.gallery.video.editor.ui;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.ui.ProgressDialog;

/* loaded from: classes2.dex */
public class AutoTrimProgressDialog extends ProgressDialog implements ProgressDialog.ProgressDialogInterface, VideoEditor.TrimStateInterface {
    public int mCurrentWorkState = -1;
    public FragmentManager mFragmentManager;
    public OnCompletedListener mOnCompletedListener;
    public String mOutPutFilePath;
    public VideoEditor mVideoEditor;

    /* loaded from: classes2.dex */
    public interface OnCompletedListener {
        void onCompleted(boolean z, String str, int i, String str2);
    }

    public static void startAutoTrim(VideoEditor videoEditor, OnCompletedListener onCompletedListener, FragmentManager fragmentManager) {
        AutoTrimProgressDialog autoTrimProgressDialog = new AutoTrimProgressDialog();
        autoTrimProgressDialog.setVideoEditor(videoEditor);
        autoTrimProgressDialog.setOnCompletedListener(onCompletedListener);
        autoTrimProgressDialog.setFragmentManager(fragmentManager);
        videoEditor.autoTrim(autoTrimProgressDialog);
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public void setOnCompletedListener(OnCompletedListener onCompletedListener) {
        this.mOnCompletedListener = onCompletedListener;
    }

    public void setVideoEditor(VideoEditor videoEditor) {
        this.mVideoEditor = videoEditor;
    }

    @Override // com.miui.gallery.video.editor.ui.ProgressDialog, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancleButtonEnable(false);
        setProgressDialogInterface(this);
    }

    @Override // com.miui.gallery.video.editor.ui.ProgressDialog.ProgressDialogInterface
    public boolean onCancelClicked() {
        if (this.mCurrentWorkState == 2) {
            this.mVideoEditor.cancelExport(null);
            return false;
        }
        return false;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor.TrimStateInterface
    public void onTrimStart() {
        showAllowingStateLoss(this.mFragmentManager, "AutoTrimProgressDialog");
        this.mCurrentWorkState = 1;
        setMsg(R.string.video_editor_auto_trim_tip);
        setCancelable(false);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor.TrimStateInterface
    public void onTrimProgress(int i) {
        setProgress((int) ((i / 100.0f) * 5.0f));
    }

    @Override // com.miui.gallery.video.editor.VideoEditor.TrimStateInterface
    public void onTrimEnd(String str) {
        this.mOutPutFilePath = str;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor.EnocdeStateInterface
    public void onEncodeStart() {
        this.mCurrentWorkState = 2;
        setProgress(5);
        setCancleButtonEnable(true);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor.EnocdeStateInterface
    public void onEncodeProgress(int i) {
        setProgress(((int) ((i / 100.0f) * 95.0f)) + 5);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor.EnocdeStateInterface
    public void onEncodeEnd(boolean z, int i, int i2) {
        OnCompletedListener onCompletedListener = this.mOnCompletedListener;
        if (onCompletedListener != null) {
            onCompletedListener.onCompleted(z, this.mOutPutFilePath, i, "");
        }
        dismissSafely();
    }
}
