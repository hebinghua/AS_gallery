package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class NewStoragePermissionDialogFragment extends GalleryDialogFragment {
    public String mActionBtnStr;
    public String mDescription;
    public DialogInterface.OnDismissListener mDismissListener;
    public GalleryDialogFragment.OnClickListener mNegativeClickListener;
    public GalleryDialogFragment.OnCancelListener mOnCancelListener;
    public GalleryDialogFragment.OnClickListener mPositiveClickListener;

    /* renamed from: $r8$lambda$Dqmud_3rv2-0BCdt1kbsKZ9ltiY */
    public static /* synthetic */ void m1526$r8$lambda$Dqmud_3rv20BCdt1kbsKZ9ltiY(NewStoragePermissionDialogFragment newStoragePermissionDialogFragment, DialogInterface dialogInterface) {
        newStoragePermissionDialogFragment.lambda$onCreateDialog$2(dialogInterface);
    }

    /* renamed from: $r8$lambda$io88GBmGjtUCQJkwO4pI-wtZFX8 */
    public static /* synthetic */ void m1527$r8$lambda$io88GBmGjtUCQJkwO4pIwtZFX8(NewStoragePermissionDialogFragment newStoragePermissionDialogFragment, DialogInterface dialogInterface, int i) {
        newStoragePermissionDialogFragment.lambda$onCreateDialog$1(dialogInterface, i);
    }

    /* renamed from: $r8$lambda$mZV8nCQTHhPGljK-8_JULSLW7Iw */
    public static /* synthetic */ void m1528$r8$lambda$mZV8nCQTHhPGljK8_JULSLW7Iw(NewStoragePermissionDialogFragment newStoragePermissionDialogFragment, DialogInterface dialogInterface, int i) {
        newStoragePermissionDialogFragment.lambda$onCreateDialog$0(dialogInterface, i);
    }

    public static NewStoragePermissionDialogFragment newInstance(String str, String str2, GalleryDialogFragment.OnClickListener onClickListener, GalleryDialogFragment.OnClickListener onClickListener2, GalleryDialogFragment.OnCancelListener onCancelListener) {
        NewStoragePermissionDialogFragment newStoragePermissionDialogFragment = new NewStoragePermissionDialogFragment();
        newStoragePermissionDialogFragment.setDescription(str);
        newStoragePermissionDialogFragment.setActionBtnStr(str2);
        newStoragePermissionDialogFragment.setOnCancelListener(onCancelListener);
        newStoragePermissionDialogFragment.setPositiveClickListener(onClickListener);
        newStoragePermissionDialogFragment.setNegativeClickListener(onClickListener2);
        return newStoragePermissionDialogFragment;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        View inflate = LayoutInflater.from(requireActivity()).inflate(R.layout.new_storage_permission_dialog, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.scope_storage_description)).setText(this.mDescription);
        ((TextView) inflate.findViewById(R.id.scope_storage_dialog_guide_content)).setText(Html.fromHtml(String.format(getString(R.string.scope_storage_dialog_guide_content), getString(R.string.scope_storage_dialog_confirm_btn), this.mActionBtnStr)));
        return new AlertDialog.Builder(requireActivity()).setTitle(ResourceUtils.getString(R.string.scope_storage_dialog_title)).setView(inflate).setPositiveButton(R.string.scope_storage_dialog_confirm_btn, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.NewStoragePermissionDialogFragment$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                NewStoragePermissionDialogFragment.m1528$r8$lambda$mZV8nCQTHhPGljK8_JULSLW7Iw(NewStoragePermissionDialogFragment.this, dialogInterface, i);
            }
        }).setNegativeButton(R.string.scope_storage_dialog_cancel_btn, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.NewStoragePermissionDialogFragment$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                NewStoragePermissionDialogFragment.m1527$r8$lambda$io88GBmGjtUCQJkwO4pIwtZFX8(NewStoragePermissionDialogFragment.this, dialogInterface, i);
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.NewStoragePermissionDialogFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                NewStoragePermissionDialogFragment.m1526$r8$lambda$Dqmud_3rv20BCdt1kbsKZ9ltiY(NewStoragePermissionDialogFragment.this, dialogInterface);
            }
        }).create();
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        GalleryDialogFragment.OnClickListener onClickListener = this.mPositiveClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, i);
        }
    }

    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        GalleryDialogFragment.OnClickListener onClickListener = this.mNegativeClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, i);
        }
    }

    public /* synthetic */ void lambda$onCreateDialog$2(DialogInterface dialogInterface) {
        GalleryDialogFragment.OnCancelListener onCancelListener = this.mOnCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(this);
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

    public void setDescription(String str) {
        this.mDescription = str;
    }

    public void setActionBtnStr(String str) {
        this.mActionBtnStr = str;
    }

    public void setPositiveClickListener(GalleryDialogFragment.OnClickListener onClickListener) {
        this.mPositiveClickListener = onClickListener;
    }

    public void setNegativeClickListener(GalleryDialogFragment.OnClickListener onClickListener) {
        this.mNegativeClickListener = onClickListener;
    }

    public void setOnCancelListener(GalleryDialogFragment.OnCancelListener onCancelListener) {
        this.mOnCancelListener = onCancelListener;
    }

    public void show(FragmentManager fragmentManager) {
        if (fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }
        showAllowingStateLoss(fragmentManager, "NewStoragePermissionDialogFragment");
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.mDismissListener = onDismissListener;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        GalleryDialogFragment.OnCancelListener onCancelListener = this.mOnCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(this);
        }
    }
}
