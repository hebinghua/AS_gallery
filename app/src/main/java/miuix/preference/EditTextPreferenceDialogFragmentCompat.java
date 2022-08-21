package miuix.preference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import ch.qos.logback.core.joran.action.Action;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes3.dex */
public class EditTextPreferenceDialogFragmentCompat extends androidx.preference.EditTextPreferenceDialogFragmentCompat {
    public PreferenceDialogFragmentCompatDelegate mDelegate;
    public IPreferenceDialogFragment mImpl;

    public static EditTextPreferenceDialogFragmentCompat newInstance(String str) {
        EditTextPreferenceDialogFragmentCompat editTextPreferenceDialogFragmentCompat = new EditTextPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString(Action.KEY_ATTRIBUTE, str);
        editTextPreferenceDialogFragmentCompat.setArguments(bundle);
        return editTextPreferenceDialogFragmentCompat;
    }

    public EditTextPreferenceDialogFragmentCompat() {
        IPreferenceDialogFragment iPreferenceDialogFragment = new IPreferenceDialogFragment() { // from class: miuix.preference.EditTextPreferenceDialogFragmentCompat.1
            @Override // miuix.preference.IPreferenceDialogFragment
            public boolean needInputMethod() {
                return true;
            }

            @Override // miuix.preference.IPreferenceDialogFragment
            public View onCreateDialogView(Context context) {
                return EditTextPreferenceDialogFragmentCompat.this.onCreateDialogView(context);
            }

            @Override // miuix.preference.IPreferenceDialogFragment
            public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
                EditTextPreferenceDialogFragmentCompat.this.onPrepareDialogBuilder(builder);
            }

            @Override // miuix.preference.IPreferenceDialogFragment
            public void onBindDialogView(View view) {
                EditTextPreferenceDialogFragmentCompat.this.onBindDialogView(view);
            }
        };
        this.mImpl = iPreferenceDialogFragment;
        this.mDelegate = new PreferenceDialogFragmentCompatDelegate(iPreferenceDialogFragment, this);
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        return this.mDelegate.onCreateDialog(bundle);
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat
    public final void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        throw new UnsupportedOperationException("using miuix builder instead");
    }

    public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(new BuilderDelegate(getContext(), builder));
    }
}
