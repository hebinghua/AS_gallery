package com.miui.gallery.widget.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.miui.gallery.R;
import com.miui.gallery.widget.menu.ImmersionMenuAdapter;
import miuix.internal.widget.ListPopup;

/* loaded from: classes2.dex */
public class PhoneImmersionMenu extends ListPopup {
    public ImmersionMenuAdapter mAdapter;
    public ImmersionMenuListener mImmersionMenuListener;
    public boolean mIsUseForTopMenu;
    public View mLastAnchor;
    public ViewGroup mLastParent;
    public ImmersionMenu mMenu;

    public PhoneImmersionMenu(Context context, ImmersionMenuListener immersionMenuListener) {
        this(context, immersionMenuListener, false);
    }

    public PhoneImmersionMenu(Context context, ImmersionMenuListener immersionMenuListener, boolean z) {
        super(context);
        setMaxAllowedHeight(context.getResources().getDimensionPixelOffset(R.dimen.miuix_appcompat_menu_popup_max_height));
        this.mIsUseForTopMenu = z;
        this.mImmersionMenuListener = immersionMenuListener;
        if (immersionMenuListener != null) {
            ImmersionMenu immersionMenu = new ImmersionMenu(context);
            this.mMenu = immersionMenu;
            immersionMenuListener.onCreateImmersionMenu(immersionMenu);
        }
        ImmersionMenuAdapter immersionMenuAdapter = new ImmersionMenuAdapter(context, this.mMenu, z);
        this.mAdapter = immersionMenuAdapter;
        setAdapter(immersionMenuAdapter);
        setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.miui.gallery.widget.menu.PhoneImmersionMenu.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                ImmersionMenuItem mo1824getItem = PhoneImmersionMenu.this.mAdapter.mo1824getItem(i);
                if (mo1824getItem.isCheckable()) {
                    PhoneImmersionMenu.this.mAdapter.toggleCheckableItem(view);
                    return;
                }
                if (mo1824getItem.isCheckableWithoutCheckBox()) {
                    PhoneImmersionMenu.this.mAdapter.toggleCheckableItem(view);
                }
                if (PhoneImmersionMenu.this.mImmersionMenuListener == null) {
                    return;
                }
                PhoneImmersionMenu.this.mImmersionMenuListener.onImmersionMenuSelected(PhoneImmersionMenu.this.mMenu, mo1824getItem);
                if (mo1824getItem.isRemainWhenClick()) {
                    return;
                }
                PhoneImmersionMenu.this.dismiss();
            }
        });
        this.mAdapter.setOnItemCheckChangeListener(new ImmersionMenuAdapter.OnItemCheckChangeListener() { // from class: com.miui.gallery.widget.menu.PhoneImmersionMenu.2
            @Override // com.miui.gallery.widget.menu.ImmersionMenuAdapter.OnItemCheckChangeListener
            public void onItemCheckChanged(ImmersionMenuItem immersionMenuItem, boolean z2) {
                if (PhoneImmersionMenu.this.mImmersionMenuListener == null) {
                    return;
                }
                PhoneImmersionMenu.this.mImmersionMenuListener.onImmersionMenuSelected(PhoneImmersionMenu.this.mMenu, immersionMenuItem);
            }
        });
    }

    public void update(ImmersionMenu immersionMenu) {
        this.mAdapter.update(immersionMenu);
    }

    @Override // miuix.internal.widget.ListPopup
    public void show(View view, ViewGroup viewGroup) {
        this.mLastAnchor = view;
        this.mLastParent = viewGroup;
        ImmersionMenuListener immersionMenuListener = this.mImmersionMenuListener;
        if (immersionMenuListener != null && immersionMenuListener.onPrepareImmersionMenu(this.mMenu)) {
            update(this.mMenu);
        }
        super.show(view, viewGroup);
    }

    public ImmersionMenu getImmersionMenu() {
        return this.mMenu;
    }
}
