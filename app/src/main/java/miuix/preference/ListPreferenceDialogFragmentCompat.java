package miuix.preference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import ch.qos.logback.core.joran.action.Action;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes3.dex */
public class ListPreferenceDialogFragmentCompat extends androidx.preference.ListPreferenceDialogFragmentCompat {
    public PreferenceDialogFragmentCompatDelegate mDelegate;
    public IPreferenceDialogFragment mImpl;

    public static ListPreferenceDialogFragmentCompat newInstance(String str) {
        ListPreferenceDialogFragmentCompat listPreferenceDialogFragmentCompat = new ListPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString(Action.KEY_ATTRIBUTE, str);
        listPreferenceDialogFragmentCompat.setArguments(bundle);
        return listPreferenceDialogFragmentCompat;
    }

    public ListPreferenceDialogFragmentCompat() {
        IPreferenceDialogFragment iPreferenceDialogFragment = new IPreferenceDialogFragment() { // from class: miuix.preference.ListPreferenceDialogFragmentCompat.1
            @Override // miuix.preference.IPreferenceDialogFragment
            public boolean needInputMethod() {
                return false;
            }

            @Override // miuix.preference.IPreferenceDialogFragment
            public View onCreateDialogView(Context context) {
                return ListPreferenceDialogFragmentCompat.this.onCreateDialogView(context);
            }

            @Override // miuix.preference.IPreferenceDialogFragment
            public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
                ListPreferenceDialogFragmentCompat.this.onPrepareDialogBuilder(builder);
            }

            @Override // miuix.preference.IPreferenceDialogFragment
            public void onBindDialogView(View view) {
                ListPreferenceDialogFragmentCompat.this.onBindDialogView(view);
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

    @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
    public final void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        throw new UnsupportedOperationException("using miuix builder instead");
    }

    public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(new BuilderDelegate(getContext(), builder));
    }
}
