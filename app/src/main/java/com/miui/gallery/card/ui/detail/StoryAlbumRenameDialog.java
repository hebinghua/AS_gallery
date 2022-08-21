package com.miui.gallery.card.ui.detail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.ui.GalleryInputDialogFragment;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes.dex */
public class StoryAlbumRenameDialog extends GalleryInputDialogFragment {
    public final View.OnClickListener mConfirmListener = new View.OnClickListener() { // from class: com.miui.gallery.card.ui.detail.StoryAlbumRenameDialog.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String obj = StoryAlbumRenameDialog.this.mInputView.mo49getText().toString();
            if (!StoryAlbumRenameDialog.this.verify(obj)) {
                return;
            }
            if (!TextUtils.equals(StoryAlbumRenameDialog.this.mDefaultName, obj)) {
                StoryAlbumRenameDialog.this.mConfirmButton.setEnabled(true);
                OnRenameDoneListener onRenameDoneListener = StoryAlbumRenameDialog.this.mOnRenameDoneListener;
                if (onRenameDoneListener != null) {
                    onRenameDoneListener.onOperationDone(obj);
                }
            }
            ((GalleryInputDialogFragment) StoryAlbumRenameDialog.this).mDialog.dismiss();
        }
    };
    public OnRenameDoneListener mOnRenameDoneListener;

    /* loaded from: classes.dex */
    public interface OnRenameDoneListener {
        void onOperationDone(String str);
    }

    @Override // com.miui.gallery.ui.GalleryInputDialogFragment, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        parseArguments();
        this.mInputView.setHint(R.string.rename_dialog_hint);
        this.mInputView.setText(this.mDefaultName);
        this.mInputView.selectAll();
    }

    public final void parseArguments() {
        this.mDefaultName = getArguments().getString("card_name");
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog create = new AlertDialog.Builder(getActivity()).setView(this.mCustomView).setTitle(R.string.rename_dialog_title).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        ((GalleryInputDialogFragment) this).mDialog = create;
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.card.ui.detail.StoryAlbumRenameDialog.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                StoryAlbumRenameDialog storyAlbumRenameDialog = StoryAlbumRenameDialog.this;
                storyAlbumRenameDialog.mConfirmButton = ((GalleryInputDialogFragment) storyAlbumRenameDialog).mDialog.getButton(-1);
                StoryAlbumRenameDialog.this.mConfirmButton.setEnabled(!TextUtils.isEmpty(StoryAlbumRenameDialog.this.mInputView.mo49getText()));
                StoryAlbumRenameDialog.this.mConfirmButton.setOnClickListener(StoryAlbumRenameDialog.this.mConfirmListener);
                BaseMiscUtil.showInputMethod(StoryAlbumRenameDialog.this.mInputView);
            }
        });
        return ((GalleryInputDialogFragment) this).mDialog;
    }

    public final boolean verify(String str) {
        if (TextUtils.isEmpty(str.trim())) {
            this.mInputView.selectAll();
            return false;
        } else if ("._".indexOf(str.charAt(0)) >= 0) {
            ToastUtils.makeText(getActivity(), (int) R.string.story_rename_invalid_prefix);
            return false;
        } else {
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                if ("/\\:@*?<>\r\n\t".indexOf(charAt) >= 0) {
                    if ("\r\n\t".indexOf(charAt) >= 0) {
                        charAt = ' ';
                    }
                    ToastUtils.makeText(getActivity(), getString(R.string.story_rename_invalid_char, Character.valueOf(charAt)));
                    return false;
                }
            }
            return true;
        }
    }

    public void setOnRenameDoneListener(OnRenameDoneListener onRenameDoneListener) {
        this.mOnRenameDoneListener = onRenameDoneListener;
    }
}
