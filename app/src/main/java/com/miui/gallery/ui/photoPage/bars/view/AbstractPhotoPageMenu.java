package com.miui.gallery.ui.photoPage.bars.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import androidx.lifecycle.Observer;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.video.VideoFrameSeekBar;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class AbstractPhotoPageMenu extends LinearLayout implements IPhotoPageMenu {
    public boolean isMoreActionsShowing;
    public AccessibilityDelegate mAccessibilityDelegate;
    public Rect mBaseInnerInsets;
    public final IMenuItemDelegate.ClickHelper mItemClickHelper;
    public final IPhotoPageMenuManager mMenuManager;
    public final ArrayList<IMenuItemDelegate> mNonResident;
    public final IViewProvider mViewProvider;

    /* renamed from: $r8$lambda$AMgBo-CKycuEW085-oHdNo95z74 */
    public static /* synthetic */ void m1637$r8$lambda$AMgBoCKycuEW085oHdNo95z74(AbstractPhotoPageMenu abstractPhotoPageMenu, ArrayList arrayList) {
        abstractPhotoPageMenu.lambda$getResidentMenuHelper$0(arrayList);
    }

    public static /* synthetic */ void $r8$lambda$fztYsf7DcQlImptssAR8opofj1w(AbstractPhotoPageMenu abstractPhotoPageMenu, ArrayList arrayList) {
        abstractPhotoPageMenu.lambda$getNonResidentMenuHelper$1(arrayList);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public abstract /* synthetic */ int getMenuCollapsedHeight();

    public abstract void refreshAllNonResidentItems();

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void refreshMoreActionsMaxHeight(float f) {
    }

    public void relayoutForItemsChanged(ArrayList<IMenuItemDelegate> arrayList) {
    }

    public abstract void removeResidentMenuItems();

    public abstract boolean residentCountChanged(int i);

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public abstract /* synthetic */ void setFrameBar(VideoFrameSeekBar videoFrameSeekBar);

    public AbstractPhotoPageMenu(IPhotoPageMenuManager iPhotoPageMenuManager, Context context, IViewProvider iViewProvider, IMenuItemDelegate.ClickHelper clickHelper) {
        super(context);
        this.mNonResident = new ArrayList<>();
        this.mMenuManager = iPhotoPageMenuManager;
        this.mViewProvider = iViewProvider;
        this.mItemClickHelper = clickHelper;
        this.mBaseInnerInsets = new Rect();
        this.mAccessibilityDelegate = new AccessibilityDelegate();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public Observer<ArrayList<IMenuItemDelegate>> getResidentMenuHelper() {
        return new Observer() { // from class: com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                AbstractPhotoPageMenu.m1637$r8$lambda$AMgBoCKycuEW085oHdNo95z74(AbstractPhotoPageMenu.this, (ArrayList) obj);
            }
        };
    }

    public /* synthetic */ void lambda$getResidentMenuHelper$0(ArrayList arrayList) {
        if (arrayList == null) {
            return;
        }
        relayoutForItemsChanged(arrayList);
        if (arrayList.isEmpty()) {
            removeResidentMenuItems();
        } else if (!residentCountChanged(arrayList.size())) {
        } else {
            reAddResidentMenuItems(arrayList);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public Observer<ArrayList<IMenuItemDelegate>> getNonResidentMenuHelper() {
        return new Observer() { // from class: com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu$$ExternalSyntheticLambda1
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                AbstractPhotoPageMenu.$r8$lambda$fztYsf7DcQlImptssAR8opofj1w(AbstractPhotoPageMenu.this, (ArrayList) obj);
            }
        };
    }

    public /* synthetic */ void lambda$getNonResidentMenuHelper$1(ArrayList arrayList) {
        if (arrayList == null) {
            return;
        }
        this.mNonResident.clear();
        this.mNonResident.addAll(arrayList);
        refreshAllNonResidentItems();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestApplyInsets();
    }

    public boolean isLayoutHideNavigation() {
        return (getWindowSystemUiVisibility() & 512) != 0;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public boolean isMoreActionsShown() {
        return this.isMoreActionsShowing;
    }

    public void setViewAccessibilityDelegate(View view) {
        if (view == null || !this.mMenuManager.isInTalkBackModel()) {
            return;
        }
        view.setAccessibilityDelegate(this.mAccessibilityDelegate);
    }

    /* loaded from: classes2.dex */
    public class AccessibilityDelegate extends View.AccessibilityDelegate {
        public AccessibilityDelegate() {
            AbstractPhotoPageMenu.this = r1;
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            IPhotoPageMenuManager iPhotoPageMenuManager = AbstractPhotoPageMenu.this.mMenuManager;
            if (iPhotoPageMenuManager != null) {
                iPhotoPageMenuManager.setCurrentFocusView(view);
            }
            return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }
    }
}
