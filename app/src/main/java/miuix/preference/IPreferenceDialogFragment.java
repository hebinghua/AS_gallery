package miuix.preference;

import android.content.Context;
import android.view.View;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes3.dex */
public interface IPreferenceDialogFragment {
    boolean needInputMethod();

    void onBindDialogView(View view);

    View onCreateDialogView(Context context);

    void onPrepareDialogBuilder(AlertDialog.Builder builder);
}
