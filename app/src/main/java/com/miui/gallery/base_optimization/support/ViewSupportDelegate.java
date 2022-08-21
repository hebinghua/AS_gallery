package com.miui.gallery.base_optimization.support;

import android.view.View;
import android.widget.TextView;

/* loaded from: classes.dex */
public class ViewSupportDelegate implements IViewSupport {
    public ViewSupportDelegate() {
    }

    public static ViewSupportDelegate getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final ViewSupportDelegate INSTANCE = new ViewSupportDelegate();
    }

    @Override // com.miui.gallery.base_optimization.support.IViewSupport
    public void setText(TextView textView, CharSequence charSequence) {
        if (textView == null) {
            return;
        }
        textView.setText(charSequence);
    }

    @Override // com.miui.gallery.base_optimization.support.IViewSupport
    public void gone(View view) {
        if (view != null) {
            view.setVisibility(8);
        }
    }
}
