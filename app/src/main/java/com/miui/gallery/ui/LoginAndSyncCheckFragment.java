package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import miui.cloud.util.SyncAutoSettingUtil;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class LoginAndSyncCheckFragment extends AndroidFragment implements DialogInterface.OnCancelListener {
    public boolean mCheckGallerySync;
    public int mCheckingType = 0;
    public Dialog mDialog;

    public static void checkLoginAndSyncState(Fragment fragment, Bundle bundle) {
        FragmentTransaction beginTransaction = fragment.getChildFragmentManager().beginTransaction();
        LoginAndSyncCheckFragment loginAndSyncCheckFragment = new LoginAndSyncCheckFragment();
        loginAndSyncCheckFragment.setArguments(bundle);
        beginTransaction.add(loginAndSyncCheckFragment, "LoginAndSyncCheckFragment");
        beginTransaction.commitAllowingStateLoss();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Bundle arguments = getArguments();
        boolean z = false;
        this.mCheckingType = 0;
        if (arguments != null && arguments.getBoolean("key_check_gallery_sync", false)) {
            z = true;
        }
        this.mCheckGallerySync = z;
        return new View(getActivity());
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        int i = this.mCheckingType;
        if (i == 0) {
            doCheck();
        } else if (i != 1) {
            if (i != 2) {
                if (i == 3) {
                    if (!SyncUtil.isGalleryCloudSyncable(getActivity())) {
                        ToastUtils.makeText(getActivity(), (int) R.string.gallery_sync_disable_toast);
                        setResult(0);
                    } else {
                        doCheck();
                    }
                } else {
                    setResult(0);
                }
            } else if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
                ToastUtils.makeText(getActivity(), (int) R.string.sync_diabled_toast);
                setResult(0);
            } else {
                doCheck();
            }
        } else if (!SyncUtil.existXiaomiAccount(getActivity())) {
            ToastUtils.makeText(getActivity(), (int) R.string.xiaomi_account_not_exists_toast);
            setResult(0);
        } else {
            doCheck();
        }
        DefaultLogger.d("LoginAndSyncCheckFragment", "onStart isCheckPending %d", Integer.valueOf(this.mCheckingType));
    }

    public final void doCheck() {
        if (!SyncUtil.existXiaomiAccount(getActivity())) {
            checkLogin();
        } else if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
            checkSystemSync();
        } else if (this.mCheckGallerySync && !SyncUtil.isGalleryCloudSyncable(getActivity())) {
            checkGallerySync();
        } else {
            setResult(-1);
        }
    }

    public final void checkLogin() {
        IntentUtil.guideToLoginXiaomiAccount(getActivity(), getArguments());
        this.mCheckingType = 1;
        DefaultLogger.d("LoginAndSyncCheckFragment", "checkLogin");
    }

    public final void checkSystemSync() {
        this.mCheckingType = 2;
        showDialog(R.string.to_enable_sync_dialog_title, R.string.to_enable_sync_dialog_message, R.string.to_enable_sync_dialog_positive_button_text, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.LoginAndSyncCheckFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginAndSyncCheckFragment.this.getActivity().startActivity(new Intent("com.xiaomi.action.MICLOUD_MAIN"));
            }
        }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.LoginAndSyncCheckFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ToastUtils.makeText(LoginAndSyncCheckFragment.this.getActivity(), (int) R.string.sync_diabled_toast);
                LoginAndSyncCheckFragment.this.setResult(0);
            }
        });
        DefaultLogger.d("LoginAndSyncCheckFragment", "checkSystemSync");
    }

    public final void checkGallerySync() {
        this.mCheckingType = 3;
        showDialog(R.string.to_enable_sync_dialog_title, R.string.to_enable_gallery_sync_dialog_message, R.string.to_enable_sync_dialog_positive_button_text, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.LoginAndSyncCheckFragment.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                IntentUtil.enterGallerySetting(LoginAndSyncCheckFragment.this.getActivity());
            }
        }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.LoginAndSyncCheckFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginAndSyncCheckFragment.this.setResult(0);
            }
        });
        DefaultLogger.d("LoginAndSyncCheckFragment", "checkGallerySync");
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.setOnCancelListener(null);
            this.mDialog.dismiss();
        }
        DefaultLogger.d("LoginAndSyncCheckFragment", "onDestroy");
    }

    public final void setResult(int i) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            parentFragment.onActivityResult(29, i, getArguments() != null ? new Intent().putExtras(getArguments()) : null);
        }
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }

    public final void showDialog(int i, int i2, int i3, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.setOnCancelListener(null);
            this.mDialog.dismiss();
        }
        AlertDialog create = new AlertDialog.Builder(getActivity()).setTitle(i).setMessage(i2).setPositiveButton(i3, onClickListener).setNegativeButton(17039360, onClickListener2).create();
        this.mDialog = create;
        create.setOnCancelListener(this);
        this.mDialog.show();
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        int i = this.mCheckingType;
        if (i == 2) {
            ToastUtils.makeText(getActivity(), (int) R.string.sync_diabled_toast);
            setResult(0);
        } else if (i == 3) {
            ToastUtils.makeText(getActivity(), (int) R.string.gallery_sync_disable_toast);
            setResult(0);
        } else {
            setResult(0);
        }
    }
}
