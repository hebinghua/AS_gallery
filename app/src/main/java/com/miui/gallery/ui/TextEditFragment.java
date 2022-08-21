package com.miui.gallery.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.permission.core.AppOpUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class TextEditFragment extends BaseFragment {
    public EditText mEditText;
    public String mOriginalText;

    public static /* synthetic */ void $r8$lambda$poKFEWJ_aKns0J_qpJOpk8MQMmQ(TextEditFragment textEditFragment, DialogInterface dialogInterface, int i) {
        textEditFragment.lambda$onOptionsItemSelected$1(dialogInterface, i);
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "TextEditFragment";
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public int getThemeRes() {
        return 2131952018;
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mOriginalText = getArguments().getString("key_editable_string", "");
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.text_edit_fragment, viewGroup, false);
        EditText editText = (EditText) inflate.findViewById(R.id.edit_text);
        this.mEditText = editText;
        editText.setText(this.mOriginalText);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_copy_to_clipboard) {
            SamplingStatHelper.recordCountEvent("OCR", "ocr_text_copy");
            TrackController.trackClick("403.43.0.1.11172", AutoTracking.getRef());
            if (AppOpUtils.isWriteClipBoard(getContext())) {
                ((ClipboardManager) getActivity().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(null, this.mEditText.getText().toString()));
                if (AppOpUtils.isWriteClipBoard(getContext())) {
                    ToastUtils.makeText(getContext(), (int) R.string.ocr_text_copied);
                }
            } else {
                DefaultLogger.d("TextEditFragment", "has not write clipboard permission");
                new AlertDialog.Builder(getContext()).setTitle(getString(R.string.grant_permission_title)).setMessage(getString(R.string.grant_permission_write_clipboard)).setNegativeButton(17039360, TextEditFragment$$ExternalSyntheticLambda1.INSTANCE).setPositiveButton(R.string.grant_permission_go_and_set_2, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.TextEditFragment$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        TextEditFragment.$r8$lambda$poKFEWJ_aKns0J_qpJOpk8MQMmQ(TextEditFragment.this, dialogInterface, i);
                    }
                }).show();
            }
            return true;
        } else if (itemId == R.id.action_save_to_note) {
            SamplingStatHelper.recordCountEvent("OCR", "ocr_text_save");
            TrackController.trackClick("403.43.0.1.11174", AutoTracking.getRef());
            IntentUtil.insertTextToNotes(getContext(), this.mEditText.getText().toString());
            return true;
        } else if (itemId != R.id.action_send) {
            return false;
        } else {
            SamplingStatHelper.recordCountEvent("OCR", "ocr_text_send");
            TrackController.trackClick("403.43.0.1.11175", AutoTracking.getRef());
            IntentUtil.shareText(getContext(), this.mEditText.getText().toString());
            return true;
        }
    }

    public static /* synthetic */ void lambda$onOptionsItemSelected$0(DialogInterface dialogInterface, int i) {
        TrackController.trackClick("403.43.1.1.14910", AutoTracking.getRef(), "cancel");
    }

    public /* synthetic */ void lambda$onOptionsItemSelected$1(DialogInterface dialogInterface, int i) {
        TrackController.trackClick("403.43.1.1.14910", AutoTracking.getRef(), "sure");
        IntentUtil.enterGalleryPermissionSetting(getContext());
    }
}
