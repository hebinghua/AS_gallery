package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.text.TextUtils;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.bars.menuitem.Favorite;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AbstractMenuItemDelegate.kt */
/* loaded from: classes2.dex */
public abstract class AbstractMenuItemDelegate implements IMenuItemDelegate {
    public ItemDataStateCache mItemDataStateCache;

    public abstract void cacheFilterResult(FilterResult filterResult);

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void applyFilterResult(FilterResult filterResult) {
        if (filterResult == null) {
            return;
        }
        cacheFilterResult(filterResult);
        boolean isSupport = isSupport();
        boolean support = filterResult.getSupport();
        if (isSupport != support) {
            setSupport(support);
        }
        boolean isVisible = isVisible();
        boolean visible = filterResult.getVisible();
        if (isVisible != visible) {
            setVisible(visible);
        }
        boolean isEnable = isEnable();
        boolean enable = filterResult.getEnable();
        if (isEnable != enable) {
            setEnable(enable);
        }
        boolean isResident = isResident();
        boolean resident = filterResult.getResident();
        if (isResident != resident) {
            setResident(resident);
        }
        int order = getOrder();
        int order2 = filterResult.getOrder();
        boolean z = true;
        boolean z2 = filterResult.getOrder() >= 0;
        if (order != order2 && z2) {
            setOrder(order2);
        }
        int titleId = getTitleId();
        int titleId2 = filterResult.getTitleId();
        boolean z3 = filterResult.getTitleId() > 0;
        if (titleId != titleId2 && z3) {
            setTitleId(titleId2);
        }
        CharSequence title = getTitle();
        CharSequence titleStr = filterResult.getTitleStr();
        boolean z4 = !TextUtils.isEmpty(filterResult.getTitleStr());
        if (!Intrinsics.areEqual(title, titleStr) && z4) {
            setTitle(titleStr);
        }
        int iconId = getIconId();
        int iconId2 = filterResult.getIconId();
        if (filterResult.getIconId() <= 0) {
            z = false;
        }
        if (iconId != iconId2 && z) {
            setIconId(iconId2);
        }
        boolean isCheckable = isCheckable();
        boolean checkable = filterResult.getCheckable();
        if (isCheckable != checkable) {
            setCheckable(checkable);
        }
        boolean isChecked = isChecked();
        boolean checked = filterResult.getChecked();
        if (isChecked != checked) {
            setChecked(checked);
        }
        if (this instanceof AddCloud) {
            AddCloud addCloud = (AddCloud) this;
            boolean isSupportAddSecret = addCloud.getIsSupportAddSecret();
            boolean supportAddSecret = filterResult.getSupportAddSecret();
            if (isSupportAddSecret != supportAddSecret) {
                addCloud.setIsSupportAddSecret(supportAddSecret);
            }
        }
        if (!(this instanceof Favorite)) {
            return;
        }
        new Favorite.FavoritesManager().refreshUI(filterResult.getFavorite(), false);
    }

    /* compiled from: AbstractMenuItemDelegate.kt */
    /* loaded from: classes2.dex */
    public static final class ItemDataStateCache {
        public int iconRes;
        public boolean isCheckable;
        public boolean isChecked;
        public boolean isEnabled;
        public boolean isVisible;
        public int titleRes;
        public CharSequence titleStr;

        public ItemDataStateCache(boolean z, boolean z2, int i, CharSequence charSequence, int i2, boolean z3, boolean z4) {
            this.isVisible = z;
            this.isEnabled = z2;
            this.titleRes = i;
            this.titleStr = charSequence;
            this.iconRes = i2;
            this.isCheckable = z3;
            this.isChecked = z4;
        }

        public final boolean isVisible() {
            return this.isVisible;
        }

        public final boolean isEnabled() {
            return this.isEnabled;
        }

        public final int getTitleRes() {
            return this.titleRes;
        }

        public final CharSequence getTitleStr() {
            return this.titleStr;
        }

        public final int getIconRes() {
            return this.iconRes;
        }

        public final boolean isCheckable() {
            return this.isCheckable;
        }

        public final boolean isChecked() {
            return this.isChecked;
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void saveDefaultState() {
        this.mItemDataStateCache = new ItemDataStateCache(isVisible(), isEnable(), getTitleId(), getTitle(), getIconId(), isCheckable(), isChecked());
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public ItemDataStateCache getDefaultState() {
        return this.mItemDataStateCache;
    }
}
