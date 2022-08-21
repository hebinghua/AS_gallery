package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.Arrays;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class CopyOrMoveDialog extends GalleryDialogFragment {
    public AlertDialog mDialog;
    public DialogInterface.OnClickListener mItemClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.CopyOrMoveDialog.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (CopyOrMoveDialog.this.mOnOperationSelectedListener != null) {
                if (i == -2) {
                    CopyOrMoveDialog.this.mOnOperationSelectedListener.onOperationSelected(CopyOrMoveDialog.this.getActivity(), 2);
                    dialogInterface.cancel();
                } else if (i < 0 || i >= 2) {
                } else {
                    DefaultLogger.d("CopyOrMoveDialog", "Creation select : %d", Integer.valueOf(i));
                    CopyOrMoveDialog.this.mOnOperationSelectedListener.onOperationSelected(CopyOrMoveDialog.this.getActivity(), i);
                }
            }
        }
    };
    public OnOperationSelectedListener mOnOperationSelectedListener;

    /* loaded from: classes2.dex */
    public interface OnOperationSelectedListener {
        void onOperationSelected(FragmentActivity fragmentActivity, int i);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void setOnOperationSelectedListener(OnOperationSelectedListener onOperationSelectedListener) {
        this.mOnOperationSelectedListener = onOperationSelectedListener;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] textArray = getResources().getTextArray(R.array.menu_copy_or_move);
        if (textArray.length > 2) {
            textArray = (CharSequence[]) Arrays.copyOf(textArray, 2);
        }
        builder.setTitle(R.string.select_image_operation).setItems(textArray, this.mItemClickListener).setNegativeButton(R.string.cancel, this.mItemClickListener);
        AlertDialog create = builder.create();
        this.mDialog = create;
        create.setCanceledOnTouchOutside(false);
        return this.mDialog;
    }
}
