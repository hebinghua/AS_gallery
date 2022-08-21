package com.miui.gallery.ui.photodetail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import androidx.preference.PreferenceManager;
import ch.qos.logback.classic.pattern.CallerDataConverter;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.ui.GalleryInputDialogFragment;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class PhotoRenameDialogFragment extends GalleryInputDialogFragment {
    public String mFilePath;
    public OnIntendToRename mOnIntendToRename;

    /* loaded from: classes2.dex */
    public interface OnIntendToRename {
        void onIntendToRename(String str);
    }

    public static PhotoRenameDialogFragment newInstance(String str, String str2, int i, OnIntendToRename onIntendToRename) {
        PhotoRenameDialogFragment photoRenameDialogFragment = new PhotoRenameDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key_photo_default_name", str);
        bundle.putString("key_file_path", str2);
        bundle.putInt("key_enter_type", i);
        photoRenameDialogFragment.setArguments(bundle);
        photoRenameDialogFragment.setOnIntendToRename(onIntendToRename);
        return photoRenameDialogFragment;
    }

    @Override // com.miui.gallery.ui.GalleryInputDialogFragment, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDefaultName = getArguments().getString("key_photo_default_name");
        this.mFilePath = getArguments().getString("key_photo_default_name");
        this.mInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60) { // from class: com.miui.gallery.ui.photodetail.PhotoRenameDialogFragment.1
            @Override // android.text.InputFilter.LengthFilter, android.text.InputFilter
            public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
                CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
                if (filter != null && spanned.length() == getMax() && (spanned.toString().length() < charSequence.toString().length() || charSequence.toString().length() >= 1)) {
                    ToastUtils.makeText(PhotoRenameDialogFragment.this.getContext(), (int) R.string.photodetail_rename_too_long);
                }
                return filter;
            }
        }});
        this.mInputView.setText(BaseFileUtils.getFileTitle(this.mDefaultName));
        this.mInputView.selectAll();
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog create = new AlertDialog.Builder(getActivity()).setView(this.mCustomView).setTitle(R.string.photodetail_rename_title).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        ((GalleryInputDialogFragment) this).mDialog = create;
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.ui.photodetail.PhotoRenameDialogFragment.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                BaseMiscUtil.showInputMethod(PhotoRenameDialogFragment.this.mInputView);
                PhotoRenameDialogFragment photoRenameDialogFragment = PhotoRenameDialogFragment.this;
                photoRenameDialogFragment.mConfirmButton = ((GalleryInputDialogFragment) photoRenameDialogFragment).mDialog.getButton(-1);
                PhotoRenameDialogFragment.this.mConfirmButton.setEnabled(!TextUtils.isEmpty(PhotoRenameDialogFragment.this.mInputView.mo49getText()));
                PhotoRenameDialogFragment.this.mConfirmButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photodetail.PhotoRenameDialogFragment.2.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        String appendFileTitleWithExtention = BaseFileUtils.appendFileTitleWithExtention(PhotoRenameDialogFragment.this.mInputView.mo49getText().toString(), PhotoRenameDialogFragment.this.mDefaultName);
                        if (!appendFileTitleWithExtention.equals(PhotoRenameDialogFragment.this.mDefaultName)) {
                            if (PhotoRenameDialogFragment.this.mInputView.mo49getText().toString().matches(".*[:/\\*?<>|\"\r\t\n\\\\].*") || ".".equals(PhotoRenameDialogFragment.this.mInputView.mo49getText().toString()) || PhotoRenameDialogFragment.this.mInputView.mo49getText().toString().startsWith(CallerDataConverter.DEFAULT_RANGE_DELIMITER)) {
                                ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.photodetail_rename_invalid);
                                PhotoRenameDialogFragment.this.mInputView.setText(BaseFileUtils.getFileTitle(PhotoRenameDialogFragment.this.mDefaultName));
                                return;
                            }
                            PreferenceManager.getDefaultSharedPreferences(GalleryApp.sGetAndroidContext()).edit().putBoolean("is_renamed", true).apply();
                            PhotoRenameDialogFragment.this.mOnIntendToRename.onIntendToRename(appendFileTitleWithExtention);
                            return;
                        }
                        PhotoRenameDialogFragment.this.dismissSafely();
                    }
                });
            }
        });
        return ((GalleryInputDialogFragment) this).mDialog;
    }

    public final void setOnIntendToRename(OnIntendToRename onIntendToRename) {
        this.mOnIntendToRename = onIntendToRename;
    }

    @Override // com.miui.gallery.ui.GalleryInputDialogFragment, com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        if (getArguments().getInt("key_enter_type") == 1) {
            dismissSafely();
        } else {
            super.onConfigurationChanged(configuration);
        }
    }
}
