package com.miui.gallery.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

@TargetApi(11)
/* loaded from: classes2.dex */
public class AlertDialogFragment extends GalleryDialogFragment {
    public DialogInterface.OnCancelListener mCancelListener;
    public boolean mCanceledOnTouchOutside;
    public CharSequence mCheckBoxMessage;
    public DialogInterface.OnDismissListener mDismissListener;
    public boolean mIsChecked;
    public CharSequence mMessage;
    public DialogInterface.OnClickListener mNegativeButtonListener;
    public CharSequence mNegativeButtonText;
    public DialogInterface.OnClickListener mPositiveButtonListener;
    public CharSequence mPositiveButtonText;
    public CharSequence mTitle;

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog create = new AlertDialog.Builder(getActivity()).setTitle(this.mTitle).setMessage(this.mMessage).setCheckBox(this.mIsChecked, this.mCheckBoxMessage).setPositiveButton(this.mPositiveButtonText, this.mPositiveButtonListener).setNegativeButton(this.mNegativeButtonText, this.mNegativeButtonListener).create();
        create.setCanceledOnTouchOutside(this.mCanceledOnTouchOutside);
        return create;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        DialogInterface.OnCancelListener onCancelListener = this.mCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        DialogInterface.OnDismissListener onDismissListener = this.mDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialogInterface);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
    }

    public void setMessage(CharSequence charSequence) {
        this.mMessage = charSequence;
    }

    public void setCheckBox(boolean z, CharSequence charSequence) {
        this.mIsChecked = z;
        this.mCheckBoxMessage = charSequence;
    }

    public void setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.mNegativeButtonText = charSequence;
        this.mNegativeButtonListener = onClickListener;
    }

    public void setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.mPositiveButtonText = charSequence;
        this.mPositiveButtonListener = onClickListener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.mCancelListener = onCancelListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.mDismissListener = onDismissListener;
    }

    public void setCanceledOnTouchOutside(boolean z) {
        this.mCanceledOnTouchOutside = z;
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        public DialogInterface.OnCancelListener mCancelListener;
        public boolean mCancelable = true;
        public boolean mCanceledOnTouchOutside = true;
        public CharSequence mCheckBoxMessage;
        public DialogInterface.OnDismissListener mDismissListener;
        public boolean mIsChecked;
        public CharSequence mMessage;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public CharSequence mTitle;

        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = charSequence;
            return this;
        }

        public Builder setMessage(CharSequence charSequence) {
            this.mMessage = charSequence;
            return this;
        }

        public Builder setCheckBox(boolean z, CharSequence charSequence) {
            this.mIsChecked = z;
            this.mCheckBoxMessage = charSequence;
            return this;
        }

        public Builder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.mNegativeButtonText = charSequence;
            this.mNegativeButtonListener = onClickListener;
            return this;
        }

        public Builder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.mPositiveButtonText = charSequence;
            this.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.mCancelable = z;
            return this;
        }

        public AlertDialogFragment create() {
            AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
            alertDialogFragment.setTitle(this.mTitle);
            alertDialogFragment.setMessage(this.mMessage);
            alertDialogFragment.setCheckBox(this.mIsChecked, this.mCheckBoxMessage);
            alertDialogFragment.setNegativeButton(this.mNegativeButtonText, this.mNegativeButtonListener);
            alertDialogFragment.setPositiveButton(this.mPositiveButtonText, this.mPositiveButtonListener);
            alertDialogFragment.setOnCancelListener(this.mCancelListener);
            alertDialogFragment.setOnDismissListener(this.mDismissListener);
            alertDialogFragment.setCancelable(this.mCancelable);
            alertDialogFragment.setCanceledOnTouchOutside(this.mCanceledOnTouchOutside);
            return alertDialogFragment;
        }
    }
}
