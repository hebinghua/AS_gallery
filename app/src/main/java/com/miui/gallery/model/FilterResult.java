package com.miui.gallery.model;

import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.AbstractMenuItemDelegate;
import java.util.Arrays;
import java.util.Locale;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;

/* compiled from: FilterResult.kt */
/* loaded from: classes2.dex */
public final class FilterResult {
    public boolean checkable;
    public boolean checked;
    public boolean enable;
    public boolean favorite;
    public int iconId;
    public PhotoPageMenuManager.MenuItemType key;
    public int order;
    public boolean resident;
    public boolean support;
    public boolean supportAddSecret;
    public int titleId;
    public CharSequence titleStr;
    public boolean visible;

    public final PhotoPageMenuManager.MenuItemType getKey() {
        return this.key;
    }

    public final void setKey(PhotoPageMenuManager.MenuItemType menuItemType) {
        this.key = menuItemType;
    }

    public final boolean getSupport() {
        return this.support;
    }

    public final void setSupport(boolean z) {
        this.support = z;
    }

    public final boolean getEnable() {
        return this.enable;
    }

    public final void setEnable(boolean z) {
        this.enable = z;
    }

    public final boolean getSupportAddSecret() {
        return this.supportAddSecret;
    }

    public final void setSupportAddSecret(boolean z) {
        this.supportAddSecret = z;
    }

    public final int getIconId() {
        return this.iconId;
    }

    public final void setIconId(int i) {
        this.iconId = i;
    }

    public final int getTitleId() {
        return this.titleId;
    }

    public final void setTitleId(int i) {
        this.titleId = i;
    }

    public final CharSequence getTitleStr() {
        return this.titleStr;
    }

    public final void setTitleStr(CharSequence charSequence) {
        this.titleStr = charSequence;
    }

    public final boolean getResident() {
        return this.resident;
    }

    public final void setResident(boolean z) {
        this.resident = z;
    }

    public final boolean getVisible() {
        return this.visible;
    }

    public final void setVisible(boolean z) {
        this.visible = z;
    }

    public final boolean getCheckable() {
        return this.checkable;
    }

    public final void setCheckable(boolean z) {
        this.checkable = z;
    }

    public final boolean getChecked() {
        return this.checked;
    }

    public final void setChecked(boolean z) {
        this.checked = z;
    }

    public final int getOrder() {
        return this.order;
    }

    public final void setOrder(int i) {
        this.order = i;
    }

    public final boolean getFavorite() {
        return this.favorite;
    }

    public final void setFavorite(boolean z) {
        this.favorite = z;
    }

    public final void applyDefaultState(AbstractMenuItemDelegate.ItemDataStateCache stateCache) {
        Intrinsics.checkNotNullParameter(stateCache, "stateCache");
        this.visible = stateCache.isVisible();
        this.enable = stateCache.isEnabled();
        this.checkable = stateCache.isCheckable();
        this.checked = stateCache.isChecked();
        this.titleId = stateCache.getTitleRes();
        this.titleStr = stateCache.getTitleStr();
        this.iconId = stateCache.getIconRes();
    }

    public String toString() {
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String format = String.format(Locale.US, "{support = [%s] enable = [%s] resident = [%s] favorite = [%s]}", Arrays.copyOf(new Object[]{Boolean.valueOf(this.support), Boolean.valueOf(this.enable), Boolean.valueOf(this.resident), Boolean.valueOf(this.favorite)}, 4));
        Intrinsics.checkNotNullExpressionValue(format, "format(locale, format, *args)");
        return format;
    }
}
