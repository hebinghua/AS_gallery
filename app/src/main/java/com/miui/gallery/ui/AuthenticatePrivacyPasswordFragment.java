package com.miui.gallery.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.privacy.LockSettingsHelper;

/* loaded from: classes2.dex */
public class AuthenticatePrivacyPasswordFragment extends AndroidFragment {
    public FragmentActivity mActivity;
    public LockSettingsHelper mChooseLockSettingsHelper;
    public boolean mRemindFirstUse = true;

    public static void startAuthenticatePrivacyPassword(Fragment fragment) {
        startAuthenticatePrivacyPassword(fragment, true);
    }

    public static void startAuthenticatePrivacyPassword(Fragment fragment, boolean z) {
        FragmentTransaction beginTransaction = fragment.getChildFragmentManager().beginTransaction();
        AuthenticatePrivacyPasswordFragment authenticatePrivacyPasswordFragment = new AuthenticatePrivacyPasswordFragment();
        authenticatePrivacyPasswordFragment.setRemindFirstUse(z);
        beginTransaction.add(authenticatePrivacyPasswordFragment, "AuthenticatePrivacyPassword");
        beginTransaction.commitAllowingStateLoss();
    }

    public void setRemindFirstUse(boolean z) {
        this.mRemindFirstUse = z;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new View(getActivity());
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mActivity = getActivity();
        if (this.mChooseLockSettingsHelper == null) {
            this.mChooseLockSettingsHelper = new LockSettingsHelper(getActivity());
        }
        if (this.mChooseLockSettingsHelper.isPrivateGalleryEnabled()) {
            LockSettingsHelper.confirmPrivateGalleryPassword(this, 35);
        } else {
            authenticatePrivacyPassword();
        }
        DefaultLogger.d("AuthenticatePrivacyPassword", "create");
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
    }

    public final void showPasswordUpdateDialog() {
        FragmentActivity fragmentActivity = this.mActivity;
        if (fragmentActivity != null) {
            Resources resources = fragmentActivity.getResources();
            GalleryPreferences.Secret.setIsFirstUsePrivacyPassword(false);
            ConfirmDialog.showConfirmDialog(getFragmentManager(), resources.getString(this.mChooseLockSettingsHelper.isPrivacyPasswordEnabled() ? R.string.secret_album_password_update_dialog_title : R.string.secret_album_password_update_first_user_dialog_title), resources.getString(this.mChooseLockSettingsHelper.isPrivacyPasswordEnabled() ? R.string.secret_album_password_update_dialog_msg : R.string.secret_album_password_update_first_user_dialog_msg), resources.getString(R.string.cancel), resources.getString(R.string.ok), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.ui.AuthenticatePrivacyPasswordFragment.1
                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onConfirm(DialogFragment dialogFragment) {
                    AuthenticatePrivacyPasswordFragment.this.authenticatePrivacyPassword();
                }

                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onCancel(DialogFragment dialogFragment) {
                    AuthenticatePrivacyPasswordFragment.this.setResult(0);
                }
            });
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            setResult(0);
        } else if (i == 27 || i == 28) {
            setResult(-1);
        } else if (i != 35) {
        } else {
            this.mChooseLockSettingsHelper.setPrivateGalleryEnabled(false);
            showPasswordUpdateDialog();
        }
    }

    public final void authenticatePrivacyPassword() {
        if (this.mChooseLockSettingsHelper.isPrivacyPasswordEnabled()) {
            if (GalleryPreferences.Secret.isFirstUsePrivacyPassword() && this.mRemindFirstUse) {
                FragmentActivity fragmentActivity = this.mActivity;
                if (fragmentActivity == null) {
                    return;
                }
                Resources resources = fragmentActivity.getResources();
                GalleryPreferences.Secret.setIsFirstUsePrivacyPassword(false);
                ConfirmDialog.showConfirmDialog(getFragmentManager(), resources.getString(R.string.secret_album_password_first_use_title), resources.getString(R.string.secret_album_password_first_use_msg), resources.getString(R.string.cancel), resources.getString(R.string.ok), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.ui.AuthenticatePrivacyPasswordFragment.2
                    @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                    public void onConfirm(DialogFragment dialogFragment) {
                        LockSettingsHelper.startAuthenticatePasswordActivity(AuthenticatePrivacyPasswordFragment.this, 27);
                    }

                    @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                    public void onCancel(DialogFragment dialogFragment) {
                        AuthenticatePrivacyPasswordFragment.this.setResult(0);
                    }
                });
                return;
            }
            LockSettingsHelper.startAuthenticatePasswordActivity(this, 27);
        } else if (GalleryPreferences.Secret.isFirstUsePrivacyPassword() && this.mRemindFirstUse) {
            FragmentActivity fragmentActivity2 = this.mActivity;
            if (fragmentActivity2 == null) {
                return;
            }
            Resources resources2 = fragmentActivity2.getResources();
            GalleryPreferences.Secret.setIsFirstUsePrivacyPassword(false);
            ConfirmDialog.showConfirmDialog(getFragmentManager(), resources2.getString(R.string.secret_album_set_pc_password_dialog_title), resources2.getString(R.string.secret_album_set_pc_password_dialog_msg), resources2.getString(R.string.cancel), resources2.getString(R.string.ok), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.ui.AuthenticatePrivacyPasswordFragment.3
                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onConfirm(DialogFragment dialogFragment) {
                    LockSettingsHelper.startSetPrivacyPasswordActivity(AuthenticatePrivacyPasswordFragment.this, 28);
                }

                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onCancel(DialogFragment dialogFragment) {
                    AuthenticatePrivacyPasswordFragment.this.setResult(0);
                }
            });
        } else {
            LockSettingsHelper.startSetPrivacyPasswordActivity(this, 28);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    public final void setResult(int i) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            parentFragment.onActivityResult(36, i, getArguments() != null ? new Intent().putExtras(getArguments()) : null);
        }
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }
}
