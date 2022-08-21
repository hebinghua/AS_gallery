package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import ch.qos.logback.core.util.FileSize;
import com.market.sdk.XiaomiUpdateAgent;
import com.miui.gallery.R;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.Iterator;
import java.util.LinkedList;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class UpdateDialogFragment extends GalleryDialogFragment {
    public CheckBox mCheckBox;
    public ConstraintLayout mContainer;
    public LinkedList<OnDialogButtonClickListener> mListeners = new LinkedList<>();
    public TextView mMessageTv;

    /* loaded from: classes2.dex */
    public interface OnDialogButtonClickListener {
        void onBackClick();

        void onDelayClick(boolean z, int i);

        void onUpdateClick(int i);
    }

    public static /* synthetic */ void $r8$lambda$3DEkt2CYjxQbbBI7jePH7DTE8Vw(UpdateDialogFragment updateDialogFragment, DialogInterface dialogInterface, int i) {
        updateDialogFragment.lambda$onCreateDialog$2(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$7OkUC3S4oFikgqYVLhAUlsD55VI(UpdateDialogFragment updateDialogFragment, Bundle bundle, DialogInterface dialogInterface, int i) {
        updateDialogFragment.lambda$onCreateDialog$3(bundle, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$QDbrGZTDD037kcTBslD_xDS9qbo(UpdateDialogFragment updateDialogFragment, DialogInterface dialogInterface, int i) {
        updateDialogFragment.lambda$onCreateDialog$1(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$oZw7RzRCSTOEU39klajGOXNA3VE(UpdateDialogFragment updateDialogFragment, DialogInterface dialogInterface, int i) {
        updateDialogFragment.lambda$onCreateDialog$0(dialogInterface, i);
    }

    public static UpdateDialogFragment newInstance(Bundle bundle) {
        UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
        updateDialogFragment.setArguments(bundle);
        return updateDialogFragment;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update, (ViewGroup) null, false);
        final Bundle bundle2 = getArguments().getBundle("key_update_response");
        boolean z = getArguments().getBoolean("key_is_force_update", false);
        initView(inflate, bundle2);
        builder.setView(inflate);
        AlertDialog create = builder.create();
        if (z) {
            this.mCheckBox.setVisibility(8);
            create.setButton(-1, getString(R.string.update_btn_positive), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.UpdateDialogFragment$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    UpdateDialogFragment.$r8$lambda$oZw7RzRCSTOEU39klajGOXNA3VE(UpdateDialogFragment.this, dialogInterface, i);
                }
            });
            create.setButton(-2, getString(R.string.update_btn_exit), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.UpdateDialogFragment$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    UpdateDialogFragment.$r8$lambda$QDbrGZTDD037kcTBslD_xDS9qbo(UpdateDialogFragment.this, dialogInterface, i);
                }
            });
        } else {
            create.setButton(-1, getString(R.string.update_btn_positive), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.UpdateDialogFragment$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    UpdateDialogFragment.$r8$lambda$3DEkt2CYjxQbbBI7jePH7DTE8Vw(UpdateDialogFragment.this, dialogInterface, i);
                }
            });
            create.setButton(-2, getString(R.string.update_btn_negative), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.UpdateDialogFragment$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    UpdateDialogFragment.$r8$lambda$7OkUC3S4oFikgqYVLhAUlsD55VI(UpdateDialogFragment.this, bundle2, dialogInterface, i);
                }
            });
        }
        return create;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        dismissSafely();
        Iterator<OnDialogButtonClickListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onUpdateClick(8);
        }
        XiaomiUpdateAgent.arrange();
    }

    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        Iterator<OnDialogButtonClickListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onBackClick();
        }
    }

    public /* synthetic */ void lambda$onCreateDialog$2(DialogInterface dialogInterface, int i) {
        dismissSafely();
        Iterator<OnDialogButtonClickListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onUpdateClick(7);
        }
        XiaomiUpdateAgent.arrange();
    }

    public /* synthetic */ void lambda$onCreateDialog$3(Bundle bundle, DialogInterface dialogInterface, int i) {
        dismissSafely();
        Iterator<OnDialogButtonClickListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onDelayClick(this.mCheckBox.isChecked(), bundle.getInt("key_update_response_version_code", 0));
        }
    }

    public final void initView(View view, Bundle bundle) {
        TextView textView = (TextView) view.findViewById(R.id.update_title_tv);
        this.mMessageTv = (TextView) view.findViewById(R.id.update_message_tv);
        TextView textView2 = (TextView) view.findViewById(R.id.update_size_tv);
        this.mCheckBox = (CheckBox) view.findViewById(R.id.update_ignore_cb);
        this.mContainer = (ConstraintLayout) view.findViewById(R.id.update_container);
        String string = bundle.getString("key_update_response_version_name", "");
        String string2 = bundle.getString("key_update_response_update_log", "");
        long j = bundle.getLong("key_update_response_apk_size");
        long j2 = bundle.getLong("key_update_response_diff_size");
        textView.setText(String.format(getString(R.string.update_title), string.split("-")[0]));
        textView.setContentDescription(String.format(getString(R.string.update_title), string));
        this.mMessageTv.setText(string2);
        this.mMessageTv.setContentDescription(string2);
        this.mMessageTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.mMessageTv.setGravity(getResources().getConfiguration().getLayoutDirection() == 0 ? 3 : 5);
        String string3 = getString(R.string.update_apk_size);
        Object[] objArr = new Object[1];
        int i = (j2 > 0L ? 1 : (j2 == 0L ? 0 : -1));
        objArr[0] = Long.valueOf(i == 0 ? getMByte(j) : getMByte(j2));
        textView2.setText(String.format(string3, objArr));
        String string4 = getString(R.string.update_apk_size);
        Object[] objArr2 = new Object[1];
        objArr2[0] = Long.valueOf(i == 0 ? getMByte(j) : getMByte(j2));
        textView2.setContentDescription(String.format(string4, objArr2));
    }

    public final long getMByte(long j) {
        return (j / FileSize.KB_COEFFICIENT) / FileSize.KB_COEFFICIENT;
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ConstraintLayout constraintLayout = this.mContainer;
        if (constraintLayout != null) {
            constraintLayout.setPadding(getResources().getDimensionPixelSize(R.dimen.update_dialog_horizontal_padding), 0, 0, 0);
        }
        TextView textView = this.mMessageTv;
        if (textView != null) {
            textView.setMaxHeight(getResources().getDimensionPixelSize(R.dimen.update_dialog_log_max_height));
            this.mMessageTv.setGravity(getResources().getConfiguration().getLayoutDirection() == 0 ? 3 : 5);
            ((ViewGroup.MarginLayoutParams) ((ConstraintLayout.LayoutParams) this.mMessageTv.getLayoutParams())).topMargin = getResources().getDimensionPixelSize(R.dimen.update_dialog_log_top_margin);
        }
        CheckBox checkBox = this.mCheckBox;
        if (checkBox != null) {
            ((ViewGroup.MarginLayoutParams) ((ConstraintLayout.LayoutParams) checkBox.getLayoutParams())).topMargin = getResources().getDimensionPixelSize(R.dimen.update_dialog_checkbox_top_margin);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        release();
    }

    public void addOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        if (onDialogButtonClickListener == null || this.mListeners.contains(onDialogButtonClickListener)) {
            return;
        }
        this.mListeners.add(onDialogButtonClickListener);
    }

    public void release() {
        LinkedList<OnDialogButtonClickListener> linkedList = this.mListeners;
        if (linkedList == null || linkedList.size() <= 0) {
            return;
        }
        this.mListeners.clear();
    }
}
