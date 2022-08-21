package miuix.preference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import ch.qos.logback.core.joran.action.Action;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes3.dex */
public class MultiSelectListPreferenceDialogFragmentCompat extends androidx.preference.MultiSelectListPreferenceDialogFragmentCompat {
    public PreferenceDialogFragmentCompatDelegate mDelegate;
    public IPreferenceDialogFragment mImpl;

    public static MultiSelectListPreferenceDialogFragmentCompat newInstance(String str) {
        MultiSelectListPreferenceDialogFragmentCompat multiSelectListPreferenceDialogFragmentCompat = new MultiSelectListPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString(Action.KEY_ATTRIBUTE, str);
        multiSelectListPreferenceDialogFragmentCompat.setArguments(bundle);
        return multiSelectListPreferenceDialogFragmentCompat;
    }

    public MultiSelectListPreferenceDialogFragmentCompat() {
        IPreferenceDialogFragment iPreferenceDialogFragment = new IPreferenceDialogFragment() { // from class: miuix.preference.MultiSelectListPreferenceDialogFragmentCompat.1
            @Override // miuix.preference.IPreferenceDialogFragment
            public boolean needInputMethod() {
                return false;
            }

            @Override // miuix.preference.IPreferenceDialogFragment
            public View onCreateDialogView(Context context) {
                return MultiSelectListPreferenceDialogFragmentCompat.this.onCreateDialogView(context);
            }

            @Override // miuix.preference.IPreferenceDialogFragment
            public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
                MultiSelectListPreferenceDialogFragmentCompat.this.onPrepareDialogBuilder(builder);
            }

            @Override // miuix.preference.IPreferenceDialogFragment
            public void onBindDialogView(View view) {
                MultiSelectListPreferenceDialogFragmentCompat.this.onBindDialogView(view);
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

    @Override // androidx.preference.MultiSelectListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
    public final void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        throw new UnsupportedOperationException("using miuix builder instead");
    }

    public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(new BuilderDelegate(getContext(), builder));
    }
}
