package com.miui.gallery.collage.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.ui.EditTextPreIme;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes.dex */
public class TextEditDialog extends GalleryDialogFragment {
    public AlertDialog mDialog;
    public EditTextPreIme mInputView;
    public TextWatcher mTextWatcher;
    public String mWillEditText;
    public boolean mWillSelection = false;
    public int mMax = -1;

    public static /* synthetic */ void $r8$lambda$WfCGgUxwUo_ei_d7eP72XKi61_Q(TextEditDialog textEditDialog) {
        textEditDialog.lambda$onCreateDialog$0();
    }

    public int getLayoutId() {
        return R.layout.text_append_edit_text;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        View inflate = LayoutInflater.from(getActivity()).inflate(getLayoutId(), (ViewGroup) null, false);
        EditTextPreIme editTextPreIme = (EditTextPreIme) inflate.findViewById(R.id.text_append_edit_text);
        this.mInputView = editTextPreIme;
        editTextPreIme.selectAll();
        this.mInputView.requestFocus();
        this.mInputView.setOnBackKeyListener(new EditTextPreIme.OnBackKeyListener() { // from class: com.miui.gallery.collage.utils.TextEditDialog$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.EditTextPreIme.OnBackKeyListener
            public final void onClose() {
                TextEditDialog.$r8$lambda$WfCGgUxwUo_ei_d7eP72XKi61_Q(TextEditDialog.this);
            }
        });
        AlertDialog create = new AlertDialog.Builder(getActivity()).setView(inflate).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        this.mDialog = create;
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.collage.utils.TextEditDialog.1
            {
                TextEditDialog.this = this;
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                if (TextEditDialog.this.mWillEditText != null) {
                    TextEditDialog textEditDialog = TextEditDialog.this;
                    textEditDialog.mInputView.setText(textEditDialog.mWillEditText);
                    if (TextEditDialog.this.mWillSelection) {
                        TextEditDialog textEditDialog2 = TextEditDialog.this;
                        textEditDialog2.mInputView.setSelection(0, textEditDialog2.mWillEditText.length());
                    } else {
                        TextEditDialog textEditDialog3 = TextEditDialog.this;
                        textEditDialog3.mInputView.setSelection(textEditDialog3.mWillEditText.length());
                    }
                }
                if (TextEditDialog.this.mTextWatcher != null) {
                    TextEditDialog textEditDialog4 = TextEditDialog.this;
                    textEditDialog4.mInputView.addTextChangedListener(textEditDialog4.mTextWatcher);
                }
                if (TextEditDialog.this.mMax > 0) {
                    TextEditDialog.this.mInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(TextEditDialog.this.mMax)});
                }
                TextEditDialog.this.checkAndSetSoftInputMode();
            }
        });
        this.mDialog.getWindow().getDecorView().setSystemUiVisibility(4);
        return this.mDialog;
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        dismissSafely();
        super.onPause();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    /* renamed from: onDetach */
    public void lambda$onCreateDialog$0() {
        super.onDetach();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        dismissSafely();
    }

    public void setTextWatch(TextWatcher textWatcher) {
        this.mTextWatcher = textWatcher;
    }

    public void setWillEditText(String str, boolean z) {
        this.mWillEditText = str;
        this.mWillSelection = z;
    }

    public void setMaxEditLength(int i) {
        this.mMax = i;
    }

    public final void checkAndSetSoftInputMode() {
        FragmentActivity activity = getActivity();
        if (activity != null && activity.getResources().getConfiguration().orientation == 2) {
            return;
        }
        BaseMiscUtil.showInputMethod(this.mInputView);
    }

    public boolean isShowing() {
        Dialog dialog = getDialog();
        return dialog != null && dialog.isShowing();
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        InputMethodManager inputMethodManager = (InputMethodManager) this.mInputView.getContext().getSystemService("input_method");
        if (inputMethodManager == null || !inputMethodManager.isAcceptingText()) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(this.mInputView.getWindowToken(), 0);
        this.mInputView.requestFocus();
        inputMethodManager.showSoftInput(this.mInputView, 0);
    }
}
