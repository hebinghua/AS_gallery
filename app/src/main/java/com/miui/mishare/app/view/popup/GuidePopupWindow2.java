package com.miui.mishare.app.view.popup;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatTextView;
import com.miui.mishare.R$layout;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes3.dex */
public class GuidePopupWindow2 extends ArrowPopupWindow {
    public Runnable mDismissRunnable;
    public LinearLayout mGuideView;
    public int mShowDuration;

    public GuidePopupWindow2(Context context) {
        super(context);
        this.mDismissRunnable = new Runnable() { // from class: com.miui.mishare.app.view.popup.GuidePopupWindow2.1
            @Override // java.lang.Runnable
            public void run() {
                GuidePopupWindow2.this.dismiss(true);
            }
        };
    }

    @Override // com.miui.mishare.app.view.popup.ArrowPopupWindow
    public void onPrepareWindow() {
        super.onPrepareWindow();
        this.mShowDuration = 5000;
        setFocusable(true);
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R$layout.miuix_appcompat_guide_popup_content_view, (ViewGroup) null, false);
        this.mGuideView = linearLayout;
        setContentView(linearLayout);
        this.mArrowPopupView.enableShowingAnimation(false);
    }

    public void setGuideText(String str) {
        addGuideTextView(str);
    }

    public void setGuideText(int i) {
        setGuideText(getContext().getString(i));
    }

    public final void addGuideTextView(String str) {
        String[] split;
        if (TextUtils.isEmpty(str) || (split = str.split("\n")) == null || split.length == 0) {
            return;
        }
        LayoutInflater layoutInflater = getLayoutInflater();
        for (String str2 : split) {
            AppCompatTextView appCompatTextView = (AppCompatTextView) layoutInflater.inflate(R$layout.guide_text, (ViewGroup) null);
            appCompatTextView.setText(str2);
            this.mGuideView.addView(appCompatTextView);
        }
    }

    public void show(View view, int i, int i2, boolean z) {
        setAutoDismiss(z);
        show(view, i, i2);
        if (z) {
            this.mArrowPopupView.postDelayed(this.mDismissRunnable, this.mShowDuration);
        }
        HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_POPUP_LIGHT);
    }
}
