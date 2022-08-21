package com.miui.gallery.editor.photo.core.imports.text.editdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public abstract class DialogSubMenu<T, E> {
    public T mListener;
    public View mNavigationButton;
    public int mNavigationDrawableRes;
    public ImageView mNavigationIconIv;
    public int mNavigationStringRes;
    public TextView mRadioButton;
    public ViewGroup mSubMenuView;

    public void OnConfigurationChanged() {
    }

    public abstract ViewGroup initSubMenuView(Context context, ViewGroup viewGroup);

    public abstract void initializeData(E e);

    public void release() {
    }

    public DialogSubMenu(Context context, ViewGroup viewGroup, int i, int i2) {
        this.mNavigationStringRes = i;
        this.mNavigationDrawableRes = i2;
        this.mNavigationButton = initNavigationRadioButton(context);
        this.mSubMenuView = initSubMenuView(context, viewGroup);
    }

    public final View initNavigationRadioButton(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.text_edit_dialog_navigation_button, (ViewGroup) null);
        this.mRadioButton = (TextView) inflate.findViewById(R.id.text_edit_dialog_navigation_button);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.text_edit_dialog_navigation_icon_iv);
        this.mNavigationIconIv = imageView;
        imageView.setBackground(context.getResources().getDrawable(this.mNavigationDrawableRes));
        this.mRadioButton.setText(context.getString(this.mNavigationStringRes));
        return inflate;
    }

    public View getNavigationButton() {
        return this.mNavigationButton;
    }

    public ViewGroup getSubMenuView() {
        return this.mSubMenuView;
    }

    public void setChecked(boolean z) {
        this.mRadioButton.setSelected(z);
        this.mNavigationIconIv.setSelected(z);
    }
}
