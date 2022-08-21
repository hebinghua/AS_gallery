package com.miui.gallery.ui;

import android.content.Context;
import android.content.DialogInterface;
import com.miui.gallery.baseui.R$string;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.DialogUtil;

/* loaded from: classes2.dex */
public class NetworkConsider {
    public static boolean sAgreedUsingMeteredNetwork;

    /* loaded from: classes2.dex */
    public interface OnConfirmed {
        void onConfirmed(boolean z, boolean z2);
    }

    /* renamed from: $r8$lambda$GesIJg-Nph6qwHj2RWhM_efXpTo */
    public static /* synthetic */ void m1523$r8$lambda$GesIJgNph6qwHj2RWhM_efXpTo(OnConfirmed onConfirmed, DialogInterface dialogInterface, int i) {
        lambda$consider$0(onConfirmed, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$LpOrP_NpqyLDkNJndXbjqTuFVSk(OnConfirmed onConfirmed, DialogInterface dialogInterface) {
        onConfirmed.onConfirmed(false, true);
    }

    /* renamed from: $r8$lambda$f52Q19hKp-CBNm6VSHRxIGLo70Y */
    public static /* synthetic */ void m1524$r8$lambda$f52Q19hKpCBNm6VSHRxIGLo70Y(OnConfirmed onConfirmed, DialogInterface dialogInterface, int i) {
        lambda$consider$4(onConfirmed, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$hkULf12oVoWJxWNMEfz9fVG9hQs(OnConfirmed onConfirmed, DialogInterface dialogInterface) {
        onConfirmed.onConfirmed(false, true);
    }

    public static /* synthetic */ void $r8$lambda$vQfgAp9j3dALqWP27oVyPSbAe8U(OnConfirmed onConfirmed, DialogInterface dialogInterface, int i) {
        lambda$consider$1(onConfirmed, dialogInterface, i);
    }

    /* renamed from: $r8$lambda$z-VHUjIUhTRXBWMv2CgvgosVfmg */
    public static /* synthetic */ void m1525$r8$lambda$zVHUjIUhTRXBWMv2CgvgosVfmg(OnConfirmed onConfirmed, DialogInterface dialogInterface, int i) {
        lambda$consider$3(onConfirmed, dialogInterface, i);
    }

    public static void consider(Context context, OnConfirmed onConfirmed) {
        if (sAgreedUsingMeteredNetwork) {
            nextConsider(onConfirmed);
        } else {
            consider(context, context.getString(R$string.toast_download_with_metered_network_title), context.getString(R$string.toast_download_with_metered_network_msg), onConfirmed);
        }
    }

    public static void consider(Context context, Boolean bool, OnConfirmed onConfirmed) {
        if (sAgreedUsingMeteredNetwork) {
            nextConsider(onConfirmed);
        } else {
            consider(context, bool, context.getString(R$string.toast_download_with_metered_network_title), context.getString(R$string.toast_download_with_metered_network_msg), onConfirmed);
        }
    }

    public static void consider(Context context, String str, String str2, final OnConfirmed onConfirmed) {
        if (BaseNetworkUtils.isActiveNetworkMetered()) {
            DialogUtil.showInfoDialog(context, str2, str, R$string.toast_download_with_metered_network_btn_continue, R$string.toast_download_with_metered_network_btn_cancel, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.NetworkConsider$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    NetworkConsider.m1523$r8$lambda$GesIJgNph6qwHj2RWhM_efXpTo(NetworkConsider.OnConfirmed.this, dialogInterface, i);
                }
            }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.NetworkConsider$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    NetworkConsider.$r8$lambda$vQfgAp9j3dALqWP27oVyPSbAe8U(NetworkConsider.OnConfirmed.this, dialogInterface, i);
                }
            }, new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.NetworkConsider$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    NetworkConsider.$r8$lambda$hkULf12oVoWJxWNMEfz9fVG9hQs(NetworkConsider.OnConfirmed.this, dialogInterface);
                }
            });
        } else {
            onConfirmed.onConfirmed(true, false);
        }
    }

    public static /* synthetic */ void lambda$consider$0(OnConfirmed onConfirmed, DialogInterface dialogInterface, int i) {
        sAgreedUsingMeteredNetwork = true;
        onConfirmed.onConfirmed(true, true);
    }

    public static /* synthetic */ void lambda$consider$1(OnConfirmed onConfirmed, DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
        onConfirmed.onConfirmed(false, true);
    }

    public static void consider(Context context, Boolean bool, String str, String str2, final OnConfirmed onConfirmed) {
        if (BaseNetworkUtils.isActiveNetworkMetered()) {
            DialogUtil.showInfoDialog(context, bool.booleanValue(), str2, str, R$string.toast_download_with_metered_network_btn_continue, R$string.toast_download_with_metered_network_btn_cancel, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.NetworkConsider$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    NetworkConsider.m1525$r8$lambda$zVHUjIUhTRXBWMv2CgvgosVfmg(NetworkConsider.OnConfirmed.this, dialogInterface, i);
                }
            }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.NetworkConsider$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    NetworkConsider.m1524$r8$lambda$f52Q19hKpCBNm6VSHRxIGLo70Y(NetworkConsider.OnConfirmed.this, dialogInterface, i);
                }
            }, new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.NetworkConsider$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    NetworkConsider.$r8$lambda$LpOrP_NpqyLDkNJndXbjqTuFVSk(NetworkConsider.OnConfirmed.this, dialogInterface);
                }
            });
        } else {
            onConfirmed.onConfirmed(true, false);
        }
    }

    public static /* synthetic */ void lambda$consider$3(OnConfirmed onConfirmed, DialogInterface dialogInterface, int i) {
        sAgreedUsingMeteredNetwork = true;
        onConfirmed.onConfirmed(true, true);
    }

    public static /* synthetic */ void lambda$consider$4(OnConfirmed onConfirmed, DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
        onConfirmed.onConfirmed(false, true);
    }

    public static void nextConsider(OnConfirmed onConfirmed) {
        onConfirmed.onConfirmed(true, BaseNetworkUtils.isActiveNetworkMetered());
    }
}
