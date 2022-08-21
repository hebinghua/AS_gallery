package com.miui.gallery.editor.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.editor.R$id;
import com.miui.gallery.editor.R$layout;

/* loaded from: classes2.dex */
public class EditorToast extends PopupWindow {
    public Runnable mDismissRunnable;
    public Handler mHandler;
    public TextView mTextView;

    public EditorToast(Context context) {
        super(View.inflate(context, R$layout.common_editor_inner_toast_layout, null), -2, -2);
        this.mDismissRunnable = new Runnable() { // from class: com.miui.gallery.editor.ui.view.EditorToast.1
            @Override // java.lang.Runnable
            public void run() {
                EditorToast.this.dismiss();
            }
        };
        setBackgroundDrawable(new ColorDrawable(0));
        setFocusable(false);
        setTouchable(false);
        setOutsideTouchable(false);
        this.mTextView = (TextView) getContentView().findViewById(R$id.text);
        this.mHandler = new Handler();
    }

    public final void showInner(TipInfo tipInfo) {
        this.mTextView.setText(tipInfo.message);
        showAsDropDown(tipInfo.contextView, tipInfo.xOffset, tipInfo.yOffset, tipInfo.gravity);
        this.mHandler.removeCallbacks(this.mDismissRunnable);
        this.mHandler.postDelayed(this.mDismissRunnable, 2000L);
    }

    public int getToastWidth(String str) {
        TextView textView = this.mTextView;
        if (textView != null) {
            textView.setText(str);
            this.mTextView.measure(0, 0);
            return this.mTextView.getMeasuredWidth();
        }
        return 0;
    }

    public int getToastHeight() {
        TextView textView = this.mTextView;
        if (textView != null) {
            return textView.getContext().getResources().getDimensionPixelSize(R$dimen.editor_inner_toast_hieght);
        }
        return 0;
    }

    public void show(String str, View view, int i, int i2, int i3) {
        TipInfo tipInfo = new TipInfo(str, view, i, i2, i3);
        dismiss();
        showInner(tipInfo);
    }

    /* loaded from: classes2.dex */
    public static class TipInfo {
        public View contextView;
        public int gravity;
        public String message;
        public int xOffset;
        public int yOffset;

        public TipInfo(String str, View view, int i, int i2, int i3) {
            this.message = str;
            this.contextView = view;
            this.gravity = i;
            this.xOffset = i2;
            this.yOffset = i3;
        }
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        super.dismiss();
        this.mHandler.removeCallbacks(this.mDismissRunnable);
    }
}
