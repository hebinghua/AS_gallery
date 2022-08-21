package com.miui.gallery.widget.menu;

import android.content.Context;
import com.miui.gallery.view.menu.MenuItemImpl;

/* loaded from: classes2.dex */
public class ImmersionMenuItem extends MenuItemImpl {
    public boolean isListMenuItemView;
    public Context mContext;
    public CharSequence mInformation;
    public boolean mIsLoading;
    public boolean mIsRedDotDisplayed;
    public boolean mIsRemainWhenClick;
    public CharSequence mSummary;

    public ImmersionMenuItem(ImmersionMenu immersionMenu, Context context, int i, int i2, int i3, int i4, CharSequence charSequence, int i5) {
        super(immersionMenu, i, i2, i3, i4, charSequence, i5);
        this.mContext = context;
    }

    public ImmersionMenuItem setIconResource(int i) {
        super.setIcon(this.mContext.getResources().getDrawable(i));
        return this;
    }

    public CharSequence getSummary() {
        return this.mSummary;
    }

    public boolean isCheckableWithoutCheckBox() {
        return (this.mFlags & 48) == 48;
    }

    public ImmersionMenuItem setCheckableWithoutCheckBox(boolean z) {
        this.mFlags = (z ? 48 : 0) | (this.mFlags & (-49));
        return this;
    }

    public ImmersionMenuItem setInformation(CharSequence charSequence) {
        this.mInformation = charSequence;
        return this;
    }

    public ImmersionMenuItem setInformation(int i) {
        return setInformation(this.mContext.getString(i));
    }

    public CharSequence getInformation() {
        return this.mInformation;
    }

    public void setRemainWhenClick(boolean z) {
        this.mIsRemainWhenClick = z;
    }

    public boolean isRemainWhenClick() {
        return this.mIsRemainWhenClick;
    }

    public void setIsRedDotDisplayed(boolean z) {
        this.mIsRedDotDisplayed = z;
    }

    public boolean isRedDotDisplayed() {
        return this.mIsRedDotDisplayed;
    }

    public boolean isListMenuItemView() {
        return this.isListMenuItemView;
    }

    public void setListMenuItemView(boolean z) {
        this.isListMenuItemView = z;
    }

    public boolean getLoadingStatus() {
        return this.mIsLoading;
    }
}
