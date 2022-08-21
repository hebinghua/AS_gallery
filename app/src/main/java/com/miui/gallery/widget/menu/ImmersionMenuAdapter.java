package com.miui.gallery.widget.menu;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.view.menu.MenuView$ItemView;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ImmersionMenuAdapter extends BaseAdapter {
    public LayoutInflater mInflater;
    public final boolean mIsExcludeActionButton;
    public OnItemCheckChangeListener mOnItemCheckChangeListener;
    public ArrayList<ImmersionMenuItem> mVisibleItems;

    /* loaded from: classes2.dex */
    public static class CheckableWithoutCheckBoxHolder extends TextHolder {
    }

    /* loaded from: classes2.dex */
    public interface OnItemCheckChangeListener {
        void onItemCheckChanged(ImmersionMenuItem immersionMenuItem, boolean z);
    }

    /* loaded from: classes2.dex */
    public static class TextHolder {
        public ImageView iconView;
        public TextView informationText;
        public ProgressBar loadingIcon;
        public View redDotView;
        public TextView summaryText;
        public TextView textView;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 4;
    }

    public ImmersionMenuAdapter(Context context, ImmersionMenu immersionMenu, boolean z) {
        this.mIsExcludeActionButton = z;
        this.mInflater = LayoutInflater.from(context);
        ArrayList<ImmersionMenuItem> arrayList = new ArrayList<>();
        this.mVisibleItems = arrayList;
        buildVisibleItems(immersionMenu, arrayList);
    }

    public final void buildVisibleItems(ImmersionMenu immersionMenu, ArrayList<ImmersionMenuItem> arrayList) {
        arrayList.clear();
        if (immersionMenu != null) {
            int size = immersionMenu.size();
            for (int i = 0; i < size; i++) {
                ImmersionMenuItem mo1823getItem = immersionMenu.mo1823getItem(i);
                if (mo1823getItem != null && mo1823getItem.isVisible() && (!this.mIsExcludeActionButton || !mo1823getItem.isActionButton())) {
                    arrayList.add(mo1823getItem);
                }
            }
        }
    }

    public void setOnItemCheckChangeListener(OnItemCheckChangeListener onItemCheckChangeListener) {
        this.mOnItemCheckChangeListener = onItemCheckChangeListener;
    }

    public void toggleCheckableItem(View view) {
        if (view != null) {
            Object tag = view.getTag();
            if (tag instanceof CheckableHolder) {
                ((CheckableHolder) tag).checkBox.performClick();
            } else if (!(tag instanceof CheckableWithoutCheckBoxHolder)) {
            } else {
                ((CheckableWithoutCheckBoxHolder) tag).textView.setTextColor(view.getResources().getColor(R.color.immersion_menu_item_selected_color));
            }
        }
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        ImmersionMenuItem immersionMenuItem = this.mVisibleItems.get(i);
        if (immersionMenuItem.isListMenuItemView()) {
            return 3;
        }
        if (immersionMenuItem.isCheckable()) {
            return 1;
        }
        return immersionMenuItem.isCheckableWithoutCheckBox() ? 2 : 0;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mVisibleItems.size();
    }

    @Override // android.widget.Adapter
    /* renamed from: getItem */
    public ImmersionMenuItem mo1824getItem(int i) {
        return this.mVisibleItems.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        int itemViewType = getItemViewType(i);
        int i2 = 0;
        if (itemViewType == 0) {
            if (view == null) {
                view = this.mInflater.inflate(R.layout.immersion_popup_menu_item, viewGroup, false);
                TextHolder textHolder = new TextHolder();
                textHolder.iconView = (ImageView) view.findViewById(R.id.icon);
                textHolder.textView = (TextView) view.findViewById(R.id.text);
                textHolder.summaryText = (TextView) view.findViewById(R.id.summary);
                textHolder.informationText = (TextView) view.findViewById(R.id.information);
                textHolder.loadingIcon = (ProgressBar) view.findViewById(R.id.loading_icon);
                textHolder.redDotView = view.findViewById(R.id.red_dot);
                view.setTag(textHolder);
            }
        } else if (itemViewType == 1) {
            if (view == null) {
                view = this.mInflater.inflate(R.layout.immersion_popup_menu_checkable_item, viewGroup, false);
                CheckableHolder checkableHolder = new CheckableHolder();
                checkableHolder.textView = (TextView) view.findViewById(R.id.text);
                checkableHolder.summaryText = (TextView) view.findViewById(R.id.summary);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.check_box);
                checkableHolder.checkBox = checkBox;
                checkBox.setOnCheckedChangeListener(checkableHolder);
                view.setTag(checkableHolder);
            }
        } else if (itemViewType == 2) {
            if (view == null) {
                view = this.mInflater.inflate(R.layout.immersion_popup_menu_item, viewGroup, false);
                CheckableWithoutCheckBoxHolder checkableWithoutCheckBoxHolder = new CheckableWithoutCheckBoxHolder();
                checkableWithoutCheckBoxHolder.iconView = (ImageView) view.findViewById(R.id.icon);
                checkableWithoutCheckBoxHolder.textView = (TextView) view.findViewById(R.id.text);
                checkableWithoutCheckBoxHolder.summaryText = (TextView) view.findViewById(R.id.summary);
                view.setTag(checkableWithoutCheckBoxHolder);
            }
        } else if (itemViewType == 3) {
            if (view == null) {
                view = this.mInflater.inflate(R.layout.action_bar_immersion_menu_item_layout, viewGroup, false);
            }
            ((MenuView$ItemView) view).initialize(mo1824getItem(i), 0);
            setPadding(view, i);
            return view;
        }
        Object tag = view.getTag();
        ImmersionMenuItem mo1824getItem = mo1824getItem(i);
        if (tag != null) {
            TextHolder textHolder2 = (TextHolder) tag;
            textHolder2.textView.setText(mo1824getItem.getTitle());
            if (!TextUtils.isEmpty(mo1824getItem.getSummary())) {
                textHolder2.summaryText.setVisibility(0);
                textHolder2.summaryText.setText(mo1824getItem.getSummary());
            } else {
                textHolder2.summaryText.setVisibility(8);
            }
            if (textHolder2.iconView != null) {
                if (mo1824getItem.getIcon() != null) {
                    textHolder2.iconView.setVisibility(0);
                    textHolder2.iconView.setImageDrawable(mo1824getItem.getIcon());
                } else {
                    textHolder2.iconView.setVisibility(8);
                }
            }
            if (textHolder2.informationText != null) {
                if (!TextUtils.isEmpty(mo1824getItem.getInformation())) {
                    textHolder2.informationText.setVisibility(0);
                    textHolder2.informationText.setText(mo1824getItem.getInformation());
                } else {
                    textHolder2.informationText.setVisibility(8);
                }
            }
            if (textHolder2.loadingIcon != null) {
                if (mo1824getItem.getLoadingStatus()) {
                    textHolder2.loadingIcon.setVisibility(0);
                } else {
                    textHolder2.loadingIcon.setVisibility(8);
                }
            }
            View view2 = textHolder2.redDotView;
            if (view2 != null) {
                if (!mo1824getItem.isRedDotDisplayed()) {
                    i2 = 4;
                }
                view2.setVisibility(i2);
            }
            if (tag instanceof CheckableHolder) {
                CheckableHolder checkableHolder2 = (CheckableHolder) tag;
                checkableHolder2.data = mo1824getItem;
                checkableHolder2.checkBox.setChecked(mo1824getItem.isChecked());
            }
            if (tag instanceof CheckableWithoutCheckBoxHolder) {
                ((CheckableWithoutCheckBoxHolder) tag).textView.setTextColor(mo1824getItem.isChecked() ? view.getResources().getColor(R.color.immersion_menu_item_selected_color) : view.getResources().getColor(R.color.immersion_menu_item_unselected_color));
            }
        }
        setPadding(view, i);
        return view;
    }

    public final void setPadding(View view, int i) {
        int dimensionPixelSize;
        int dimensionPixelSize2;
        int paddingStart = view.getPaddingStart();
        view.getPaddingTop();
        int paddingEnd = view.getPaddingEnd();
        view.getPaddingBottom();
        if (getCount() == 1) {
            dimensionPixelSize = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
            dimensionPixelSize2 = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
        } else if (i == 0) {
            dimensionPixelSize = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_large);
            dimensionPixelSize2 = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
        } else if (i == getCount() - 1) {
            dimensionPixelSize = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
            dimensionPixelSize2 = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_large);
        } else {
            dimensionPixelSize = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
            dimensionPixelSize2 = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
        }
        view.setPaddingRelative(paddingStart, dimensionPixelSize, paddingEnd, dimensionPixelSize2);
    }

    /* loaded from: classes2.dex */
    public class CheckableHolder extends TextHolder implements CompoundButton.OnCheckedChangeListener {
        public CheckBox checkBox;
        public ImmersionMenuItem data;

        public CheckableHolder() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (ImmersionMenuAdapter.this.mOnItemCheckChangeListener == null || !(this.data.isChecked() ^ z)) {
                return;
            }
            ImmersionMenuAdapter.this.mOnItemCheckChangeListener.onItemCheckChanged(this.data, z);
        }
    }

    public void update(ImmersionMenu immersionMenu) {
        buildVisibleItems(immersionMenu, this.mVisibleItems);
        notifyDataSetChanged();
    }
}
