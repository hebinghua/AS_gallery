package com.miui.gallery.editor.photo.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class AlertDialogFragment extends GalleryDialogFragment {
    public Callbacks mCallbacks;
    public DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.AlertDialogFragment.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (dialogInterface == AlertDialogFragment.this.getDialog() && AlertDialogFragment.this.mCallbacks != null) {
                AlertDialogFragment.this.mCallbacks.onClick(AlertDialogFragment.this, i);
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface Callbacks {
        void onCancel(AlertDialogFragment alertDialogFragment);

        void onClick(AlertDialogFragment alertDialogFragment, int i);

        void onDismiss(AlertDialogFragment alertDialogFragment);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 2131951621);
        Bundle arguments = getArguments();
        int i = arguments.getInt("AlertDialogFragment:message_resource");
        if (i != 0) {
            builder.setMessage(i);
        }
        int i2 = arguments.getInt("AlertDialogFragment:negative_resource");
        if (i2 != 0) {
            builder.setNegativeButton(i2, this.mOnClickListener);
        }
        int i3 = arguments.getInt("AlertDialogFragment:positive_resource");
        if (i3 != 0) {
            builder.setPositiveButton(i3, this.mOnClickListener);
        }
        return builder.create();
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        Callbacks callbacks = this.mCallbacks;
        if (callbacks != null) {
            callbacks.onCancel(this);
        }
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        Callbacks callbacks = this.mCallbacks;
        if (callbacks != null) {
            callbacks.onDismiss(this);
        }
    }

    public final void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mCallbacks = null;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public boolean mCancellable;
        public int mMessageId;
        public int mNegativeId;
        public int mPositiveId;

        public Builder setMessage(int i) {
            this.mMessageId = i;
            return this;
        }

        public Builder setPositiveButton(int i) {
            this.mPositiveId = i;
            return this;
        }

        public Builder setNegativeButton(int i) {
            this.mNegativeId = i;
            return this;
        }

        public Builder setCancellable(boolean z) {
            this.mCancellable = z;
            return this;
        }

        public AlertDialogFragment build() {
            Bundle bundle = new Bundle();
            int i = this.mMessageId;
            if (i != 0) {
                bundle.putInt("AlertDialogFragment:message_resource", i);
            }
            int i2 = this.mPositiveId;
            if (i2 != 0) {
                bundle.putInt("AlertDialogFragment:positive_resource", i2);
            }
            int i3 = this.mNegativeId;
            if (i3 != 0) {
                bundle.putInt("AlertDialogFragment:negative_resource", i3);
            }
            AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
            alertDialogFragment.setCancelable(this.mCancellable);
            alertDialogFragment.setArguments(bundle);
            return alertDialogFragment;
        }
    }
}
