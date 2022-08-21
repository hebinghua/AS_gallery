package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.view.View;
import java.lang.reflect.Field;
import miuix.popupwidget.widget.GuidePopupWindow;

/* loaded from: classes2.dex */
public class ToolPopupWindow extends GuidePopupWindow {
    public View mArrowRef;

    public ToolPopupWindow(Context context) {
        super(context);
    }

    public final void getArrowRef() {
        try {
            Field declaredField = this.mArrowPopupView.getClass().getDeclaredField("mArrow");
            if (declaredField == null) {
                return;
            }
            declaredField.setAccessible(true);
            this.mArrowRef = (View) declaredField.get(this.mArrowPopupView);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void setArrowVisible(int i) {
        if (this.mArrowRef == null) {
            getArrowRef();
        }
        View view = this.mArrowRef;
        if (view != null) {
            view.setVisibility(i);
        }
    }
}
