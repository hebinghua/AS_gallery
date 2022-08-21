package com.miui.gallery.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.view.menu.MenuBuilder;
import miuix.appcompat.R$styleable;
import miuix.internal.util.AttributeResolver;

/* loaded from: classes2.dex */
public class ListMenuItemView extends LinearLayout implements MenuView$ItemView {
    public Drawable mBackground;
    public CheckBox mCheckBox;
    public Context mContext;
    public boolean mForceShowIcon;
    public ImageView mIconView;
    public LayoutInflater mInflater;
    public IMenuItem mItemData;
    public boolean mPreserveIconSpacing;
    public RadioButton mRadioButton;
    public View mRelativeLayout;
    public TextView mShortcutView;
    public int mTextAppearance;
    public Context mTextAppearanceContext;
    public TextView mTitleView;

    @Override // com.miui.gallery.view.menu.MenuView$ItemView
    public boolean prefersCondensedTitle() {
        return false;
    }

    public ListMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.MenuView, i, 0);
        this.mBackground = obtainStyledAttributes.getDrawable(5);
        this.mTextAppearance = obtainStyledAttributes.getResourceId(1, -1);
        this.mPreserveIconSpacing = obtainStyledAttributes.getBoolean(7, false);
        this.mTextAppearanceContext = context;
        obtainStyledAttributes.recycle();
    }

    public ListMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        setBackground(this.mBackground);
        TextView textView = (TextView) findViewById(R.id.title);
        this.mTitleView = textView;
        int i = this.mTextAppearance;
        if (i != -1) {
            textView.setTextAppearance(this.mTextAppearanceContext, i);
        }
        this.mShortcutView = (TextView) findViewById(R.id.shortcut);
        this.mRelativeLayout = getChildAt(0);
    }

    @Override // com.miui.gallery.view.menu.MenuView$ItemView
    public void initialize(IMenuItem iMenuItem, int i) {
        this.mItemData = iMenuItem;
        setVisibility(iMenuItem.isVisible() ? 0 : 8);
        setTitle(iMenuItem.getTitleForItemView(this));
        setCheckable(iMenuItem.isCheckable());
        setShortcut(iMenuItem.shouldShowShortcut(), iMenuItem.getShortcut());
        setIcon(iMenuItem.getIcon());
        setEnabled(iMenuItem.isEnabled());
    }

    public void setForceShowIcon(boolean z) {
        this.mForceShowIcon = z;
        this.mPreserveIconSpacing = z;
    }

    public void setTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.mTitleView.setText(charSequence);
            if (this.mTitleView.getVisibility() == 0) {
                return;
            }
            this.mTitleView.setVisibility(0);
        } else if (this.mTitleView.getVisibility() == 8) {
        } else {
            this.mTitleView.setVisibility(8);
        }
    }

    public IMenuItem getItemData() {
        return this.mItemData;
    }

    public void setCheckable(boolean z) {
        CompoundButton compoundButton;
        CompoundButton compoundButton2;
        if (!z && this.mRadioButton == null && this.mCheckBox == null) {
            return;
        }
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                insertRadioButton();
            }
            compoundButton = this.mRadioButton;
            compoundButton2 = this.mCheckBox;
        } else {
            if (this.mCheckBox == null) {
                insertCheckBox();
            }
            compoundButton = this.mCheckBox;
            compoundButton2 = this.mRadioButton;
        }
        int i = 0;
        if (z) {
            compoundButton.setChecked(this.mItemData.isChecked());
            if (compoundButton.getVisibility() != 0) {
                compoundButton.setVisibility(0);
            }
            if (compoundButton2 != null && compoundButton2.getVisibility() != 8) {
                compoundButton2.setVisibility(8);
            }
        } else {
            CheckBox checkBox = this.mCheckBox;
            if (checkBox != null) {
                checkBox.setVisibility(8);
            }
            RadioButton radioButton = this.mRadioButton;
            if (radioButton != null) {
                radioButton.setVisibility(8);
            }
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mRelativeLayout.getLayoutParams();
        if (!z || !this.mItemData.isExclusiveCheckable()) {
            i = AttributeResolver.resolveDimensionPixelSize(getContext(), 16843683);
        }
        marginLayoutParams.setMarginStart(i);
        this.mRelativeLayout.setLayoutParams(marginLayoutParams);
        setActivated(this.mItemData.isChecked());
    }

    public void setChecked(boolean z) {
        CompoundButton compoundButton;
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                insertRadioButton();
            }
            compoundButton = this.mRadioButton;
        } else {
            if (this.mCheckBox == null) {
                insertCheckBox();
            }
            compoundButton = this.mCheckBox;
        }
        compoundButton.setChecked(z);
        setActivated(z);
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker) {
        throw new UnsupportedOperationException();
    }

    public void setShortcut(boolean z, char c) {
        int i = (!z || !this.mItemData.shouldShowShortcut()) ? 8 : 0;
        if (i == 0) {
            this.mShortcutView.setText(this.mItemData.getShortcutLabel());
        }
        if (this.mShortcutView.getVisibility() != i) {
            this.mShortcutView.setVisibility(i);
        }
    }

    public void setIcon(Drawable drawable) {
        boolean z = this.mItemData.shouldShowIcon() || this.mForceShowIcon;
        if (z || this.mPreserveIconSpacing) {
            ImageView imageView = this.mIconView;
            if (imageView == null && drawable == null && !this.mPreserveIconSpacing) {
                return;
            }
            if (imageView == null) {
                insertIconView();
            }
            if (drawable != null || this.mPreserveIconSpacing) {
                ImageView imageView2 = this.mIconView;
                if (!z) {
                    drawable = null;
                }
                imageView2.setImageDrawable(drawable);
                if (this.mIconView.getVisibility() == 0) {
                    return;
                }
                this.mIconView.setVisibility(0);
                return;
            }
            this.mIconView.setVisibility(8);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (this.mIconView != null && this.mPreserveIconSpacing) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mIconView.getLayoutParams();
            int i3 = layoutParams.height;
            if (i3 > 0 && layoutParams2.width <= 0) {
                layoutParams2.width = i3;
            }
        }
        super.onMeasure(i, i2);
    }

    public final void insertIconView() {
        ImageView imageView = (ImageView) getInflater().inflate(R.layout.list_menu_item_icon, (ViewGroup) this, false);
        this.mIconView = imageView;
        addView(imageView, 0);
    }

    public final void insertRadioButton() {
        RadioButton radioButton = (RadioButton) getInflater().inflate(R.layout.list_menu_item_radio, (ViewGroup) this, false);
        this.mRadioButton = radioButton;
        addView(radioButton, 0);
    }

    public void insertCheckBox() {
        CheckBox checkBox = (CheckBox) getInflater().inflate(R.layout.list_menu_item_checkbox, (ViewGroup) this, false);
        this.mCheckBox = checkBox;
        addView(checkBox);
    }

    public LayoutInflater getInflater() {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(this.mContext);
        }
        return this.mInflater;
    }
}
