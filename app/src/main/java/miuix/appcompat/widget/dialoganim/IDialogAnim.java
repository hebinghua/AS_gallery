package miuix.appcompat.widget.dialoganim;

import android.view.View;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.widget.DialogAnimHelper;

/* loaded from: classes3.dex */
public interface IDialogAnim {
    void cancelAnimator();

    void executeDismissAnim(View view, View view2, DialogAnimHelper.OnDismiss onDismiss);

    void executeShowAnim(View view, View view2, boolean z, AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener);
}
