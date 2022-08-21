package miuix.popupwidget.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import miuix.popupwidget.R$dimen;
import miuix.popupwidget.R$layout;
import miuix.popupwidget.internal.widget.ArrowPopupView;

/* loaded from: classes3.dex */
public class ArrowPopupWindow extends PopupWindow {
    public ArrowPopupView mArrowPopupView;
    public boolean mAutoDismiss;
    public Context mContext;
    public int mListViewMaxHeight;

    public void onPrepareWindow() {
    }

    public ArrowPopupWindow(Context context) {
        this(context, null);
    }

    public ArrowPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ArrowPopupWindow(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ArrowPopupWindow(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mAutoDismiss = true;
        this.mContext = context;
        this.mAutoDismiss = true;
        setupPopupWindow();
    }

    public void setAutoDismiss(boolean z) {
        this.mAutoDismiss = z;
    }

    public Context getContext() {
        return this.mContext;
    }

    public final void setupPopupWindow() {
        this.mListViewMaxHeight = this.mContext.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_window_list_max_height);
        ArrowPopupView arrowPopupView = (ArrowPopupView) getLayoutInflater().inflate(R$layout.miuix_appcompat_arrow_popup_view, (ViewGroup) null, false);
        this.mArrowPopupView = arrowPopupView;
        super.setContentView(arrowPopupView);
        super.setWidth(-1);
        super.setHeight(-1);
        setSoftInputMode(3);
        this.mArrowPopupView.setArrowPopupWindow(this);
        super.setTouchInterceptor(getDefaultOnTouchListener());
        if (Build.VERSION.SDK_INT >= 21) {
            this.mArrowPopupView.addShadow();
        }
        onPrepareWindow();
        update();
    }

    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(this.mContext);
    }

    @Override // android.widget.PopupWindow
    public final void setContentView(View view) {
        this.mArrowPopupView.setContentView(view);
    }

    @Override // android.widget.PopupWindow
    public View getContentView() {
        return this.mArrowPopupView.getContentView();
    }

    public void setArrowMode(int i) {
        this.mArrowPopupView.setArrowMode(i);
    }

    public void show(View view, int i, int i2) {
        this.mArrowPopupView.setAnchor(view);
        this.mArrowPopupView.setOffset(i, i2);
        showAtLocation(view, 8388659, 0, 0);
        this.mArrowPopupView.setAutoDismiss(this.mAutoDismiss);
        this.mArrowPopupView.animateToShow();
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i, int i2) {
        show(view, i, i2);
    }

    public void dismiss(boolean z) {
        if (z) {
            this.mArrowPopupView.animateToDismiss();
        } else {
            dismiss();
        }
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i, int i2, int i3) {
        show(view, i, i2);
    }

    @Override // android.widget.PopupWindow
    public void update(int i, int i2, int i3, int i4, boolean z) {
        super.update(0, 0, -2, -2, z);
        setContentHeight(i4);
    }

    @Override // android.widget.PopupWindow
    public void setTouchInterceptor(View.OnTouchListener onTouchListener) {
        this.mArrowPopupView.setTouchInterceptor(onTouchListener);
    }

    @Override // android.widget.PopupWindow
    public int getWidth() {
        return getContentWidth();
    }

    public int getContentWidth() {
        View contentView = getContentView();
        if (contentView != null) {
            return contentView.getWidth();
        }
        return 0;
    }

    @Override // android.widget.PopupWindow
    public void setWidth(int i) {
        setContentWidth(i);
    }

    public void setContentWidth(int i) {
        View contentView = getContentView();
        if (contentView != null) {
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            layoutParams.width = i;
            contentView.setLayoutParams(layoutParams);
        }
    }

    @Override // android.widget.PopupWindow
    public int getHeight() {
        return getContentHeight();
    }

    public int getContentHeight() {
        View contentView = getContentView();
        if (contentView != null) {
            return contentView.getHeight();
        }
        return 0;
    }

    @Override // android.widget.PopupWindow
    public void setHeight(int i) {
        setContentHeight(i);
    }

    public void setContentHeight(int i) {
        int i2;
        View contentView = getContentView();
        if ((contentView instanceof ListView) && i > (i2 = this.mListViewMaxHeight)) {
            i = i2;
        }
        if (contentView != null) {
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            layoutParams.height = i;
            contentView.setLayoutParams(layoutParams);
        }
    }

    public View.OnTouchListener getDefaultOnTouchListener() {
        return this.mArrowPopupView;
    }
}
