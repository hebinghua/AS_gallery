package com.miui.gallery.util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miui.gallery.baseui.R$id;
import com.miui.gallery.baseui.R$layout;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class DialogUtil {
    public static AlertDialog createLoadingDialog(Context context, String str) {
        try {
            View inflate = LayoutInflater.from(context).inflate(R$layout.alert_dialog_loading, (ViewGroup) null);
            ((TextView) inflate.findViewById(R$id.message)).setText(str);
            return new AlertDialog.Builder(context).setView(inflate).setCancelable(false).create();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AlertDialog showInfoDialog(Context context, String str, String str2) {
        try {
            new AlertDialog.Builder(context).setMessage(str).setTitle(str2).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AlertDialog showInfoDialog(Context context, int i, int i2, int i3, int i4, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        try {
            return new AlertDialog.Builder(context).setMessage(i).setTitle(i2).setPositiveButton(i3, onClickListener).setNegativeButton(i4, onClickListener2).show();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AlertDialog showInfoDialog(Context context, String str, String str2, int i, DialogInterface.OnClickListener onClickListener) {
        try {
            return new AlertDialog.Builder(context).setMessage(str).setTitle(str2).setPositiveButton(i, onClickListener).show();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AlertDialog showInfoDialog(Context context, String str, String str2, int i, int i2, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        return showInfoDialog(context, true, str, str2, i, i2, onClickListener, onClickListener2);
    }

    public static AlertDialog showInfoDialog(Context context, String str, String str2, int i, int i2, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener) {
        return showInfoDialog(context, true, str, str2, i, i2, onClickListener, onClickListener2, onCancelListener);
    }

    public static AlertDialog showInfoDialog(Context context, boolean z, String str, String str2, int i, int i2, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        return showInfoDialog(context, z, str, str2, i, i2, onClickListener, onClickListener2, null);
    }

    public static AlertDialog showInfoDialog(Context context, boolean z, String str, String str2, int i, int i2, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener) {
        try {
            return new AlertDialog.Builder(context).setMessage(str).setTitle(str2).setCancelable(z).setPositiveButton(i, onClickListener).setNegativeButton(i2, onClickListener2).setOnCancelListener(onCancelListener).show();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AlertDialog showConfirmAlertWithCancel(Context context, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, String str, CharSequence charSequence, String str2, int i) {
        try {
            return new AlertDialog.Builder(context).setCancelable(true).setIconAttribute(16843605).setTitle(str).setMessage(charSequence).setPositiveButton(str2, onClickListener).setNegativeButton(i, onClickListener2).show();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AlertDialog showConfirmAlert(Context context, DialogInterface.OnClickListener onClickListener, String str, CharSequence charSequence, String str2) {
        try {
            return new AlertDialog.Builder(context).setCancelable(true).setIconAttribute(16843605).setTitle(str).setMessage(charSequence).setPositiveButton(str2, onClickListener).show();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ProgressDialog showProgressDialog(Context context, String str, String str2, boolean z) {
        return ProgressDialog.show(context, str, str2, true, z);
    }
}
