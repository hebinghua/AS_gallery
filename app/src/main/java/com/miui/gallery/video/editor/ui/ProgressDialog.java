package com.miui.gallery.video.editor.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.widget.GalleryDialogFragment;

/* loaded from: classes2.dex */
public class ProgressDialog extends GalleryDialogFragment {
    public ImageButton mCancelImageButton;
    public TextView mMsgTextView;
    public ProgressBar mProgressBar;
    public ProgressDialogInterface mProgressDialogInterface;
    public String mTempMsg;
    public View mView;
    public int mTempMax = -1;
    public int mTempProgress = -1;
    public int mTempMsgId = -1;

    /* loaded from: classes2.dex */
    public interface ProgressDialogInterface {
        boolean onCancelClicked();
    }

    public void setProgressDialogInterface(ProgressDialogInterface progressDialogInterface) {
        this.mProgressDialogInterface = progressDialogInterface;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.video_editor_dialog_auto_trim_progress, (ViewGroup) null);
        this.mView = inflate;
        this.mMsgTextView = (TextView) inflate.findViewById(R.id.video_editor_progress_dialog_msg_textview);
        this.mProgressBar = (ProgressBar) this.mView.findViewById(R.id.video_editor_progress_dialog_progressbar);
        this.mCancelImageButton = (ImageButton) this.mView.findViewById(R.id.video_editor_progress_dialog_cancel_button);
        this.mProgressBar.setMax(100);
        this.mCancelImageButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.ProgressDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ProgressDialog.this.mProgressDialogInterface == null || ProgressDialog.this.mProgressDialogInterface.onCancelClicked()) {
                    ProgressDialog.this.dismissAllowingStateLoss();
                }
            }
        });
        int i = this.mTempMax;
        if (i != -1) {
            this.mProgressBar.setMax(i);
        }
        int i2 = this.mTempProgress;
        if (i2 != -1) {
            this.mProgressBar.setProgress(i2);
        }
        int i3 = this.mTempMsgId;
        if (i3 != -1) {
            this.mMsgTextView.setText(i3);
        }
        String str = this.mTempMsg;
        if (str != null) {
            this.mMsgTextView.setText(str);
        }
    }

    public void setCancleButtonEnable(boolean z) {
        ImageButton imageButton = this.mCancelImageButton;
        if (imageButton != null) {
            imageButton.setVisibility(z ? 0 : 4);
        }
    }

    public void setProgress(int i) {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            progressBar.setProgress(i);
        } else {
            this.mTempProgress = i;
        }
    }

    public void setMax(int i) {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            progressBar.setMax(i);
        } else {
            this.mTempMax = i;
        }
    }

    public void setMsg(int i) {
        TextView textView = this.mMsgTextView;
        if (textView != null) {
            textView.setText(i);
        } else {
            this.mTempMsgId = i;
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        Dialog dialog = new Dialog(getActivity(), 2131952405);
        dialog.setContentView(this.mView, new ViewGroup.LayoutParams(getActivity().getWindowManager().getDefaultDisplay().getWidth(), -2));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(80);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.miui.gallery.video.editor.ui.ProgressDialog.2
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
        return dialog;
    }
}
