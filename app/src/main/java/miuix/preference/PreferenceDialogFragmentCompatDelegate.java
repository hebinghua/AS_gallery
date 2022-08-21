package miuix.preference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes3.dex */
public class PreferenceDialogFragmentCompatDelegate {
    public PreferenceDialogFragmentCompat mFragmentCompat;
    public IPreferenceDialogFragment mInternal;

    public PreferenceDialogFragmentCompatDelegate(IPreferenceDialogFragment iPreferenceDialogFragment, PreferenceDialogFragmentCompat preferenceDialogFragmentCompat) {
        this.mInternal = iPreferenceDialogFragment;
        this.mFragmentCompat = preferenceDialogFragmentCompat;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Context context = this.mFragmentCompat.getContext();
        DialogPreference preference = this.mFragmentCompat.getPreference();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        BuilderDelegate builderDelegate = new BuilderDelegate(context, builder);
        builderDelegate.setTitle(preference.getDialogTitle());
        builderDelegate.setIcon(preference.getDialogIcon());
        builderDelegate.setPositiveButton(preference.getPositiveButtonText(), this.mFragmentCompat);
        builderDelegate.setNegativeButton(preference.getNegativeButtonText(), this.mFragmentCompat);
        View onCreateDialogView = this.mInternal.onCreateDialogView(context);
        if (onCreateDialogView != null) {
            this.mInternal.onBindDialogView(onCreateDialogView);
            builderDelegate.setView(onCreateDialogView);
        } else {
            builderDelegate.setMessage(preference.getDialogMessage());
        }
        this.mInternal.onPrepareDialogBuilder(builder);
        AlertDialog create = builder.create();
        if (this.mInternal.needInputMethod()) {
            requestInputMethod(create);
        }
        return create;
    }

    public final void requestInputMethod(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(5);
    }
}
