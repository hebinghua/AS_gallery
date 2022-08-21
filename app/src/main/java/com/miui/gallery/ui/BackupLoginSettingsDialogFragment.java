package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class BackupLoginSettingsDialogFragment extends GalleryDialogFragment {
    public boolean mFinishActivity;
    public boolean mIsCreated;
    public SettingsDialog mSettingDialog;

    public static /* synthetic */ void $r8$lambda$ectPk5uoE6PFEsK4c6_26dF27Cs(BackupLoginSettingsDialogFragment backupLoginSettingsDialogFragment, DialogInterface dialogInterface, int i) {
        backupLoginSettingsDialogFragment.lambda$onCreateDialog$0(dialogInterface, i);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        if (this.mSettingDialog == null) {
            SettingsDialog settingsDialog = new SettingsDialog(getActivity());
            this.mSettingDialog = settingsDialog;
            settingsDialog.setPositiveButtonOnClickListener(new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.BackupLoginSettingsDialogFragment$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    BackupLoginSettingsDialogFragment.$r8$lambda$ectPk5uoE6PFEsK4c6_26dF27Cs(BackupLoginSettingsDialogFragment.this, dialogInterface, i);
                }
            });
        }
        this.mIsCreated = true;
        return this.mSettingDialog;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        onDone(this.mSettingDialog);
    }

    public final void onDone(DialogInterface dialogInterface) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        dismissAllowingStateLoss();
        if (!this.mFinishActivity) {
            return;
        }
        activity.finish();
    }

    public void finishActivityWhenDone(boolean z) {
        this.mFinishActivity = z;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (!this.mIsCreated) {
            this.mIsCreated = false;
            return;
        }
        BackupLoginSettingsFragment backupLoginSettingsFragment = (BackupLoginSettingsFragment) getChildFragmentManager().findFragmentByTag("BackupLoginSettingsFragment");
        if (backupLoginSettingsFragment == null) {
            backupLoginSettingsFragment = new BackupLoginSettingsFragment();
        }
        getChildFragmentManager().beginTransaction().replace(R.id.backup_login, backupLoginSettingsFragment, "BackupLoginSettingsFragment").commit();
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        onDone(dialogInterface);
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        onDone(dialogInterface);
    }

    /* loaded from: classes2.dex */
    public class SettingsDialog extends AlertDialog implements DialogInterface.OnClickListener {
        public DialogInterface.OnClickListener mPositiveListener;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SettingsDialog(Context context) {
            super(context);
            BackupLoginSettingsDialogFragment.this = r3;
            setView(((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.backup_login_settings, (ViewGroup) null));
            setButton(-3, r3.getResources().getText(R.string.cloud_login_ok), this.mPositiveListener);
        }

        public SettingsDialog setPositiveButtonOnClickListener(DialogInterface.OnClickListener onClickListener) {
            this.mPositiveListener = onClickListener;
            return this;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            DialogInterface.OnClickListener onClickListener;
            if (i == -3 && (onClickListener = this.mPositiveListener) != null) {
                onClickListener.onClick(dialogInterface, i);
            }
        }
    }
}
