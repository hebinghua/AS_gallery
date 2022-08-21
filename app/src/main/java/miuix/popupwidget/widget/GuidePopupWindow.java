package miuix.popupwidget.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatTextView;
import miuix.popupwidget.R$attr;
import miuix.popupwidget.R$dimen;
import miuix.popupwidget.R$layout;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes3.dex */
public class GuidePopupWindow extends ArrowPopupWindow {
    public Runnable mDismissRunnable;
    public LinearLayout mGuideView;
    public int mShowDuration;

    public GuidePopupWindow(Context context) {
        super(context);
        this.mDismissRunnable = new Runnable() { // from class: miuix.popupwidget.widget.GuidePopupWindow.1
            @Override // java.lang.Runnable
            public void run() {
                GuidePopupWindow.this.dismiss(true);
            }
        };
    }

    @Override // miuix.popupwidget.widget.ArrowPopupWindow
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
        for (String str2 : split) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(getContext(), null, R$attr.guidePopupTextStyle);
            appCompatTextView.setMaxWidth(getContext().getResources().getDimensionPixelSize(R$dimen.miuix_popup_guide_text_view_max_width));
            appCompatTextView.setText(str2);
            this.mGuideView.addView(appCompatTextView);
        }
    }

    public void show(View view, boolean z) {
        show(view, 0, 0, z);
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
