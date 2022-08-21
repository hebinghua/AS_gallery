package com.miui.gallery.video.editor.ui;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.ui.ProgressDialog;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes2.dex */
public class EncodeProgressDialog extends ProgressDialog implements ProgressDialog.ProgressDialogInterface, VideoEditor.EnocdeStateInterface {
    public FragmentManager mFragmentManager;
    public OnCompletedListener mOnCompletedListener;
    public String mOutPutPath;
    public VideoEditor mVideoEditor;

    /* loaded from: classes2.dex */
    public interface OnCompletedListener {
        void onCompleted(String str, boolean z, int i, int i2);
    }

    public static void startEncode(VideoEditor videoEditor, String str, OnCompletedListener onCompletedListener, FragmentManager fragmentManager) {
        EncodeProgressDialog encodeProgressDialog = new EncodeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putString(nexExportFormat.TAG_FORMAT_PATH, str);
        encodeProgressDialog.setArguments(bundle);
        encodeProgressDialog.setVideoEditor(videoEditor);
        encodeProgressDialog.setOnCompletedListener(onCompletedListener);
        encodeProgressDialog.setFragmentManager(fragmentManager);
        videoEditor.export(str, encodeProgressDialog);
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
        setProgressDialogInterface(this);
        setMax(100);
        this.mOutPutPath = getArguments().getString(nexExportFormat.TAG_FORMAT_PATH);
    }

    @Override // com.miui.gallery.video.editor.ui.ProgressDialog.ProgressDialogInterface
    public boolean onCancelClicked() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor != null) {
            videoEditor.cancelExport(null);
            return false;
        }
        return false;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor.EnocdeStateInterface
    public void onEncodeStart() {
        showAllowingStateLoss(this.mFragmentManager, "EncodeProgressDialog");
        setMsg(R.string.video_editor_encode_video);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor.EnocdeStateInterface
    public void onEncodeProgress(int i) {
        setProgress(i);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor.EnocdeStateInterface
    public void onEncodeEnd(boolean z, int i, int i2) {
        setProgress(100);
        OnCompletedListener onCompletedListener = this.mOnCompletedListener;
        if (onCompletedListener != null) {
            onCompletedListener.onCompleted(this.mOutPutPath, z, i, i2);
        }
        dismissSafely();
    }
}
