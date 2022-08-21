package miuix.appcompat.widget;

import android.view.View;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.widget.dialoganim.IDialogAnim;
import miuix.appcompat.widget.dialoganim.PadDialogAnim;
import miuix.appcompat.widget.dialoganim.PhoneDialogAnim;
import miuix.internal.util.DeviceHelper;

/* loaded from: classes3.dex */
public class DialogAnimHelper {
    public static IDialogAnim sDialogAnim;

    /* loaded from: classes3.dex */
    public interface OnDismiss {
        void end();
    }

    public static void executeShowAnim(View view, View view2, boolean z, AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener) {
        if (sDialogAnim == null || DeviceHelper.isFoldDevice()) {
            if (DeviceHelper.isTablet(view.getContext())) {
                sDialogAnim = new PadDialogAnim();
            } else {
                sDialogAnim = new PhoneDialogAnim();
            }
        }
        sDialogAnim.executeShowAnim(view, view2, z, onDialogShowAnimListener);
    }

    public static void cancelAnimator() {
        IDialogAnim iDialogAnim = sDialogAnim;
        if (iDialogAnim != null) {
            iDialogAnim.cancelAnimator();
        }
    }

    public static void executeDismissAnim(View view, View view2, OnDismiss onDismiss) {
        IDialogAnim iDialogAnim = sDialogAnim;
        if (iDialogAnim != null) {
            iDialogAnim.executeDismissAnim(view, view2, onDismiss);
        }
    }
}
