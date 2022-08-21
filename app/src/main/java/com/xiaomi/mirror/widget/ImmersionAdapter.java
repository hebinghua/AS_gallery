package com.xiaomi.mirror.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xiaomi.mirror.opensdk.R;
import java.util.List;

/* loaded from: classes3.dex */
public class ImmersionAdapter extends BaseAdapter {
    public static final int VIEW_TYPE_COUNT = 1;
    public static final int VIEW_TYPE_SETTING = 0;
    private LayoutInflater mInflater;
    private List<ViewEntry> mViewEntries;

    /* loaded from: classes3.dex */
    public static class ViewEntry {
        private Drawable mIcon;
        private CharSequence mTitle;
        private final int mViewType;

        public ViewEntry(int i) {
            this.mViewType = i;
        }

        public Drawable getIcon() {
            return this.mIcon;
        }

        public CharSequence getTitle() {
            return this.mTitle;
        }

        public int getViewType() {
            return this.mViewType;
        }

        public void setIcon(Drawable drawable) {
            this.mIcon = drawable;
        }

        public void setTitle(CharSequence charSequence) {
            this.mTitle = charSequence;
        }
    }

    public ImmersionAdapter(Context context, List<ViewEntry> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mViewEntries = list;
    }

    public static ViewEntry buildSettingViewEntry(CharSequence charSequence) {
        return buildSettingViewEntry(charSequence, null);
    }

    public static ViewEntry buildSettingViewEntry(CharSequence charSequence, Drawable drawable) {
        ViewEntry viewEntry = new ViewEntry(0);
        viewEntry.setTitle(charSequence);
        if (drawable != null) {
            viewEntry.setIcon(drawable);
        }
        return viewEntry;
    }

    private View getSettingView(int i, View view, ViewGroup viewGroup) {
        Resources resources;
        int i2;
        View inflate = this.mInflater.inflate(R.layout.immersion_popup_menu_item, viewGroup, false);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        if (getCount() == 1) {
            resources = inflate.getContext().getResources();
            i2 = R.dimen.menu_item_height_one;
        } else if (getCount() == 2) {
            resources = inflate.getContext().getResources();
            i2 = R.dimen.menu_item_height_double;
        } else {
            resources = inflate.getContext().getResources();
            i2 = R.dimen.menu_item_height_multi;
        }
        layoutParams.height = resources.getDimensionPixelOffset(i2);
        inflate.setLayoutParams(inflate.getLayoutParams());
        ViewEntry mo1928getItem = mo1928getItem(i);
        if (mo1928getItem == null) {
            return inflate;
        }
        ImageView imageView = (ImageView) inflate.findViewById(R.id.icon);
        TextView textView = (TextView) inflate.findViewById(R.id.text);
        if (textView != null) {
            textView.setTextAlignment(5);
            textView.setText(mo1928getItem.getTitle());
            textView.setTextColor(inflate.getContext().getResources().getColor(R.color.list_item_text_color));
        }
        if (imageView != null && mo1928getItem.getIcon() != null) {
            imageView.setImageDrawable(mo1928getItem.getIcon());
            imageView.setVisibility(0);
        }
        return inflate;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mViewEntries.size();
    }

    @Override // android.widget.Adapter
    /* renamed from: getItem */
    public ViewEntry mo1928getItem(int i) {
        return this.mViewEntries.get(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        return this.mViewEntries.get(i).getViewType();
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (getItemViewType(i) == 0) {
            return getSettingView(i, view, viewGroup);
        }
        throw new IllegalStateException("Invalid view type ID " + getItemViewType(i));
    }

    public List<ViewEntry> getViewEntries() {
        return this.mViewEntries;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 1;
    }

    public void update(List<ViewEntry> list) {
        this.mViewEntries = list;
        notifyDataSetChanged();
    }
}
