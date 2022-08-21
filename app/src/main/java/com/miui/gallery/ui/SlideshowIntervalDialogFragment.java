package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.EditTextPreIme;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class SlideshowIntervalDialogFragment extends GalleryDialogFragment {
    public AlertDialog mDialog;
    public CompleteListener mListener;

    /* loaded from: classes2.dex */
    public interface CompleteListener {
        void onComplete();
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        int slideShowInterval = GalleryPreferences.SlideShow.getSlideShowInterval();
        View inflate = LayoutInflater.from(activity).inflate(R.layout.edit_text_dialog, (ViewGroup) null, false);
        final EditTextPreIme editTextPreIme = (EditTextPreIme) inflate.findViewById(R.id.edit_text);
        editTextPreIme.setText(String.valueOf(slideShowInterval));
        editTextPreIme.selectAll();
        editTextPreIme.setRawInputType(2);
        editTextPreIme.setOnBackKeyListener(new EditTextPreIme.OnBackKeyListener() { // from class: com.miui.gallery.ui.SlideshowIntervalDialogFragment.1
            @Override // com.miui.gallery.ui.EditTextPreIme.OnBackKeyListener
            public void onClose() {
                if (SlideshowIntervalDialogFragment.this.mDialog == null || !SlideshowIntervalDialogFragment.this.mDialog.isShowing()) {
                    return;
                }
                SlideshowIntervalDialogFragment.this.mDialog.dismiss();
            }
        });
        AlertDialog create = new AlertDialog.Builder(activity).setView(inflate).setTitle(R.string.slideshow_interval_dialog_title).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        this.mDialog = create;
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.ui.SlideshowIntervalDialogFragment.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                SlideshowIntervalDialogFragment.this.mDialog.getButton(-1).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.SlideshowIntervalDialogFragment.2.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                        if (!SlideshowIntervalDialogFragment.this.isTextValid(editTextPreIme.mo49getText())) {
                            ToastUtils.makeText(SlideshowIntervalDialogFragment.this.getActivity(), String.format(SlideshowIntervalDialogFragment.this.getResources().getString(R.string.slideshow_interval_invalid), 3, 3600));
                            return;
                        }
                        GalleryPreferences.SlideShow.setSlideShowInterval(Integer.parseInt(editTextPreIme.mo49getText().toString()));
                        if (SlideshowIntervalDialogFragment.this.mListener != null) {
                            SlideshowIntervalDialogFragment.this.mListener.onComplete();
                        }
                        SlideshowIntervalDialogFragment.this.mDialog.dismiss();
                    }
                });
                BaseMiscUtil.showInputMethod(editTextPreIme);
            }
        });
        return this.mDialog;
    }

    public void setCompleteListener(CompleteListener completeListener) {
        this.mListener = completeListener;
    }

    public final boolean isTextValid(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return false;
        }
        try {
            int parseInt = Integer.parseInt(charSequence.toString());
            return parseInt >= 3 && parseInt <= 3600;
        } catch (NumberFormatException unused) {
            return false;
        }
    }
}
