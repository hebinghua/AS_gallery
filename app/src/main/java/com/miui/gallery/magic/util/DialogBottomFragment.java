package com.miui.gallery.magic.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class DialogBottomFragment extends GalleryDialogFragment implements View.OnClickListener {
    public View.OnClickListener onClickListener;

    /* renamed from: $r8$lambda$_9I7_nzMC42EC-n1wclDhGej1dI */
    public static /* synthetic */ void m1073$r8$lambda$_9I7_nzMC42ECn1wclDhGej1dI(DialogBottomFragment dialogBottomFragment, AlertDialog alertDialog, DialogInterface dialogInterface) {
        dialogBottomFragment.lambda$onCreateDialog$0(alertDialog, dialogInterface);
    }

    public static DialogBottomFragment newInstance(Bundle bundle) {
        DialogBottomFragment dialogBottomFragment = new DialogBottomFragment();
        dialogBottomFragment.setArguments(bundle);
        return dialogBottomFragment;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R$style.AlertDialog_Theme_Dark);
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.ts_magic_id_photo_dialog_save, (ViewGroup) null, false);
        initView(inflate);
        builder.setTitle(getResources().getString(R$string.magic_idp_save_type)).setView(inflate).setNegativeButton((CharSequence) null, (DialogInterface.OnClickListener) null);
        final AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.magic.util.DialogBottomFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                DialogBottomFragment.m1073$r8$lambda$_9I7_nzMC42ECn1wclDhGej1dI(DialogBottomFragment.this, create, dialogInterface);
            }
        });
        return create;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(AlertDialog alertDialog, DialogInterface dialogInterface) {
        TextView textView;
        int identifier = alertDialog.getContext().getResources().getIdentifier("alertTitle", "id", getContext().getPackageName());
        if (identifier <= 0 || (textView = (TextView) alertDialog.findViewById(identifier)) == null) {
            return;
        }
        textView.setTextSize(0, getResources().getDimensionPixelSize(R$dimen.magic_text_size_52));
    }

    public final void initView(View view) {
        View findViewById = view.findViewById(R$id.magic_idp_save_one);
        View findViewById2 = view.findViewById(R$id.magic_idp_save_two);
        View findViewById3 = view.findViewById(R$id.magic_idp_save_m);
        View findViewById4 = view.findViewById(R$id.magic_idp_save_cancel);
        FolmeUtil.setCustomTouchAnim(findViewById4, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, null, true);
        findViewById.setOnClickListener(this);
        findViewById2.setOnClickListener(this);
        findViewById3.setOnClickListener(this);
        findViewById4.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        dismiss();
        View.OnClickListener onClickListener = this.onClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
