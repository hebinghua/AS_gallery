package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.miui.gallery.R;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.ui.EditTextPreIme;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.ArrayList;
import java.util.Iterator;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class PeopleRelationCreatorDialogFragment extends GalleryDialogFragment {
    public static final String[] INVALID_NAMES = {"自己", "孩子", "家人", "同事", "同学", "家人", "朋友", "未分组", "自定义", "Me", "Child", "Family", "Collegues", "Classmates", "Friends", "Ungrouped", "Custom"};
    public Button mConfirmButton;
    public View.OnClickListener mConfirmListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.PeopleRelationCreatorDialogFragment.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String obj = PeopleRelationCreatorDialogFragment.this.mInputView.mo49getText().toString();
            if (!PeopleRelationCreatorDialogFragment.this.verify(obj)) {
                return;
            }
            PeopleRelationCreatorDialogFragment.this.mDialog.dismiss();
            PeopleContactInfo.UserDefineRelation.addRelation(obj);
            if (PeopleRelationCreatorDialogFragment.this.mOnCreatedListener == null) {
                return;
            }
            PeopleRelationCreatorDialogFragment.this.mOnCreatedListener.onRelationCreated(obj);
        }
    };
    public View mCustomView;
    public String mDefaultName;
    public AlertDialog mDialog;
    public EditTextPreIme mInputView;
    public OnRelationCreatedListener mOnCreatedListener;

    /* loaded from: classes2.dex */
    public interface OnRelationCreatedListener {
        void onRelationCreated(String str);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.edit_text_dialog, (ViewGroup) null, false);
        this.mCustomView = inflate;
        EditTextPreIme editTextPreIme = (EditTextPreIme) inflate.findViewById(R.id.edit_text);
        this.mInputView = editTextPreIme;
        editTextPreIme.setHint(R.string.input_relation_name);
        String string = getString(R.string.default_relation_name);
        this.mDefaultName = string;
        this.mInputView.setText(string);
        this.mInputView.selectAll();
        this.mInputView.setOnBackKeyListener(new EditTextPreIme.OnBackKeyListener() { // from class: com.miui.gallery.ui.PeopleRelationCreatorDialogFragment.1
            @Override // com.miui.gallery.ui.EditTextPreIme.OnBackKeyListener
            public void onClose() {
                if (PeopleRelationCreatorDialogFragment.this.mDialog == null || !PeopleRelationCreatorDialogFragment.this.mDialog.isShowing()) {
                    return;
                }
                PeopleRelationCreatorDialogFragment.this.mDialog.dismiss();
            }
        });
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog create = new AlertDialog.Builder(getActivity()).setView(this.mCustomView).setTitle(R.string.default_relation_name).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        this.mDialog = create;
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.ui.PeopleRelationCreatorDialogFragment.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                PeopleRelationCreatorDialogFragment peopleRelationCreatorDialogFragment = PeopleRelationCreatorDialogFragment.this;
                peopleRelationCreatorDialogFragment.mConfirmButton = peopleRelationCreatorDialogFragment.mDialog.getButton(-1);
                PeopleRelationCreatorDialogFragment.this.mConfirmButton.setOnClickListener(PeopleRelationCreatorDialogFragment.this.mConfirmListener);
                BaseMiscUtil.showInputMethod(PeopleRelationCreatorDialogFragment.this.mInputView);
            }
        });
        return this.mDialog;
    }

    public final void selectAll() {
        EditTextPreIme editTextPreIme = this.mInputView;
        editTextPreIme.setText(editTextPreIme.mo49getText());
        this.mInputView.selectAll();
    }

    public final boolean verify(String str) {
        if (TextUtils.isEmpty(str.trim())) {
            ToastUtils.makeText(getActivity(), getString(R.string.empty_relation));
            this.mInputView.setText(this.mDefaultName);
            this.mInputView.selectAll();
            return false;
        } else if ("._".indexOf(str.charAt(0)) >= 0) {
            ToastUtils.makeText(getActivity(), (int) R.string.new_relation_invalid_prefix);
            return false;
        } else {
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                if ("/\\:@*?<>\r\n\t".indexOf(charAt) >= 0) {
                    if ("\r\n\t".indexOf(charAt) >= 0) {
                        charAt = ' ';
                    }
                    ToastUtils.makeText(getActivity(), getString(R.string.new_relation_invalid_char, Character.valueOf(charAt)));
                    return false;
                }
            }
            for (String str2 : INVALID_NAMES) {
                if (TextUtils.equals(str2, str)) {
                    ToastUtils.makeText(getActivity(), getString(R.string.new_relation_invalid_name, str));
                    selectAll();
                    return false;
                }
            }
            ArrayList<String> userDefineRelations = PeopleContactInfo.UserDefineRelation.getUserDefineRelations();
            if (BaseMiscUtil.isValid(userDefineRelations)) {
                Iterator<String> it = userDefineRelations.iterator();
                while (it.hasNext()) {
                    if (TextUtils.equals(it.next(), str)) {
                        ToastUtils.makeText(getActivity(), getString(R.string.new_relation_invalid_name, str));
                        selectAll();
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public void setOnCreatedListener(OnRelationCreatedListener onRelationCreatedListener) {
        this.mOnCreatedListener = onRelationCreatedListener;
    }
}
