package miuix.appcompat.internal.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$styleable;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuView;

/* loaded from: classes3.dex */
public class ListMenuItemView extends LinearLayout implements MenuView.ItemView {
    public Drawable mBackground;
    public AppCompatCheckBox mCheckBox;
    public Context mContext;
    public boolean mForceShowIcon;
    public AppCompatImageView mIconView;
    public LayoutInflater mInflater;
    public MenuItemImpl mItemData;
    public boolean mPreserveIconSpacing;
    public AppCompatRadioButton mRadioButton;
    public View mRelativeLayout;
    public TextView mShortcutView;
    public int mTextAppearance;
    public Context mTextAppearanceContext;
    public TextView mTitleView;

    @Override // miuix.appcompat.internal.view.menu.MenuView.ItemView
    public boolean prefersCondensedTitle() {
        return false;
    }

    public ListMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.MenuView, i, 0);
        this.mBackground = obtainStyledAttributes.getDrawable(R$styleable.MenuView_android_itemBackground);
        this.mTextAppearance = obtainStyledAttributes.getResourceId(R$styleable.MenuView_android_itemTextAppearance, -1);
        this.mPreserveIconSpacing = obtainStyledAttributes.getBoolean(R$styleable.MenuView_preserveIconSpacing, false);
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
        TextView textView = (TextView) findViewById(R$id.title);
        this.mTitleView = textView;
        int i = this.mTextAppearance;
        if (i != -1) {
            textView.setTextAppearance(this.mTextAppearanceContext, i);
        }
        this.mShortcutView = (TextView) findViewById(R$id.shortcut);
        this.mRelativeLayout = getChildAt(0);
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView.ItemView
    public void initialize(MenuItemImpl menuItemImpl, int i) {
        this.mItemData = menuItemImpl;
        setVisibility(menuItemImpl.isVisible() ? 0 : 8);
        setTitle(menuItemImpl.getTitleForItemView(this));
        setCheckable(menuItemImpl.isCheckable());
        setShortcut(menuItemImpl.shouldShowShortcut(), menuItemImpl.getShortcut());
        setIcon(menuItemImpl.getIcon());
        setEnabled(menuItemImpl.isEnabled());
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

    @Override // miuix.appcompat.internal.view.menu.MenuView.ItemView
    public MenuItemImpl getItemData() {
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
        if (z) {
            compoundButton.setChecked(this.mItemData.isChecked());
            if (compoundButton.getVisibility() != 0) {
                compoundButton.setVisibility(0);
            }
            if (compoundButton2 == null || compoundButton2.getVisibility() == 8) {
                return;
            }
            compoundButton2.setVisibility(8);
            return;
        }
        AppCompatCheckBox appCompatCheckBox = this.mCheckBox;
        if (appCompatCheckBox != null) {
            appCompatCheckBox.setVisibility(8);
        }
        AppCompatRadioButton appCompatRadioButton = this.mRadioButton;
        if (appCompatRadioButton == null) {
            return;
        }
        appCompatRadioButton.setVisibility(8);
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
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView.ItemView
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
            AppCompatImageView appCompatImageView = this.mIconView;
            if (appCompatImageView == null && drawable == null && !this.mPreserveIconSpacing) {
                return;
            }
            if (appCompatImageView == null) {
                insertIconView();
            }
            if (drawable != null || this.mPreserveIconSpacing) {
                AppCompatImageView appCompatImageView2 = this.mIconView;
                if (!z) {
                    drawable = null;
                }
                appCompatImageView2.setImageDrawable(drawable);
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
        AppCompatImageView appCompatImageView = (AppCompatImageView) getInflater().inflate(R$layout.miuix_appcompat_list_menu_item_icon, (ViewGroup) this, false);
        this.mIconView = appCompatImageView;
        addView(appCompatImageView, 0);
    }

    public final void insertRadioButton() {
        AppCompatRadioButton appCompatRadioButton = (AppCompatRadioButton) getInflater().inflate(R$layout.miuix_appcompat_list_menu_item_radio, (ViewGroup) this, false);
        this.mRadioButton = appCompatRadioButton;
        addView(appCompatRadioButton, 0);
    }

    public final void insertCheckBox() {
        AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox) getInflater().inflate(R$layout.miuix_appcompat_list_menu_item_checkbox, (ViewGroup) this, false);
        this.mCheckBox = appCompatCheckBox;
        addView(appCompatCheckBox);
    }

    private LayoutInflater getInflater() {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(this.mContext);
        }
        return this.mInflater;
    }
}
