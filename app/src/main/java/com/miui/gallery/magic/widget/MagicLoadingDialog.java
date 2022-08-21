package com.miui.gallery.magic.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$style;

/* loaded from: classes2.dex */
public class MagicLoadingDialog extends Dialog {
    public Context context;
    public Callback mDoCancelBack;
    public long mLastBackPressedTime;
    public View view;
    public View viewCancel;

    /* loaded from: classes2.dex */
    public interface Callback {
        void doCancel();
    }

    public MagicLoadingDialog(Context context) {
        super(context, R$style.magic_loading_dialog);
        this.context = context;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.miui.gallery.magic.widget.MagicLoadingDialog.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == 4 && keyEvent.getAction() == 0) {
                    return MagicLoadingDialog.this.onBackPress();
                }
                return false;
            }
        });
    }

    public final boolean onBackPress() {
        if (this.mDoCancelBack == null) {
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
            this.mLastBackPressedTime = currentTimeMillis;
            return false;
        }
        this.mLastBackPressedTime = 0L;
        Callback callback = this.mDoCancelBack;
        if (callback == null) {
            return false;
        }
        callback.doCancel();
        return false;
    }

    public void setDoCancelBack(Callback callback) {
        this.viewCancel.setVisibility(0);
        this.mDoCancelBack = callback;
    }

    @Override // android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View inflate = LayoutInflater.from(this.context).inflate(R$layout.ts_magic_loading_dialog, (ViewGroup) null);
        this.view = inflate;
        this.viewCancel = inflate.findViewById(R$id.view_loading_dialog);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) this.view.findViewById(R$id.magic_loading);
        lottieAnimationView.setAnimation("loading.json");
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();
        setContentView(this.view);
        this.viewCancel.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.widget.MagicLoadingDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MagicLoadingDialog.this.mDoCancelBack != null) {
                    MagicLoadingDialog.this.mDoCancelBack.doCancel();
                }
            }
        });
        setCaptionDialogFragmentSize();
    }

    public void setCaptionDialogFragmentSize() {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = -1;
        attributes.height = -1;
        if (Build.VERSION.SDK_INT >= 28) {
            attributes.layoutInDisplayCutoutMode = 1;
        }
        getWindow().setAttributes(attributes);
        getWindow().addFlags(Integer.MIN_VALUE);
        getWindow().setNavigationBarColor(-16777216);
        getWindow().setWindowAnimations(R$style.magic_loading_dialog_animation);
    }

    public void showLoadingDialog() {
        if (isShowing()) {
            dismiss();
        }
        show();
        this.viewCancel.setVisibility(8);
    }

    public void removeLoadingDialog() {
        if (isShowing()) {
            dismiss();
        }
    }
}
