package com.miui.gallery.magic.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.fragment.app.FragmentActivity;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import miuix.androidbasewidget.widget.EditText;

/* loaded from: classes2.dex */
public class DialogUtil {
    public static void hintKbTwo(EditText editText, Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @SuppressLint({"ResourceAsColor"})
    public static void magicSelectDialog(FragmentActivity fragmentActivity, View.OnClickListener onClickListener) {
        DialogBottomFragment newInstance = DialogBottomFragment.newInstance(null);
        newInstance.setOnClickListener(onClickListener);
        newInstance.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), nexExportFormat.TAG_FORMAT_TAG);
    }
}
