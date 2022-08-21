package com.miui.gallery.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import com.miui.gallery.R;
import com.miui.gallery.ui.EditTextPreIme;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class GalleryInputDialogFragment extends GalleryDialogFragment {
    public Button mConfirmButton;
    public View mCustomView;
    public String mDefaultName;
    public AlertDialog mDialog;
    public EditTextPreIme mInputView;
    public TextWatcher mTextWatcher = new TextWatcher() { // from class: com.miui.gallery.ui.GalleryInputDialogFragment.1
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        {
            GalleryInputDialogFragment.this = this;
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Button button = GalleryInputDialogFragment.this.mConfirmButton;
            if (button != null) {
                button.setEnabled(!TextUtils.isEmpty(charSequence));
            }
        }
    };

    public static /* synthetic */ void $r8$lambda$1QUV2Gy5j27113pkWNaz8FUHZ6k(GalleryInputDialogFragment galleryInputDialogFragment) {
        galleryInputDialogFragment.lambda$onCreate$0();
    }

    public int getLayoutId() {
        return R.layout.edit_text_dialog;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View inflate = LayoutInflater.from(getActivity()).inflate(getLayoutId(), (ViewGroup) null, false);
        this.mCustomView = inflate;
        EditTextPreIme editTextPreIme = (EditTextPreIme) inflate.findViewById(R.id.edit_text);
        this.mInputView = editTextPreIme;
        editTextPreIme.selectAll();
        this.mInputView.addTextChangedListener(this.mTextWatcher);
        this.mInputView.setOnBackKeyListener(new EditTextPreIme.OnBackKeyListener() { // from class: com.miui.gallery.ui.GalleryInputDialogFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.EditTextPreIme.OnBackKeyListener
            public final void onClose() {
                GalleryInputDialogFragment.$r8$lambda$1QUV2Gy5j27113pkWNaz8FUHZ6k(GalleryInputDialogFragment.this);
            }
        });
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    /* renamed from: onDetach */
    public void lambda$onCreate$0() {
        super.onDetach();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        this.mDialog.dismiss();
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
