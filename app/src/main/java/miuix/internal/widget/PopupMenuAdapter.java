package miuix.internal.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.internal.util.AnimHelper;
import miuix.internal.util.TaggingDrawableUtil;

/* loaded from: classes3.dex */
public class PopupMenuAdapter extends BaseAdapter {
    public ArrayList<MenuItem> mAvailableItems;
    public Context mContext;
    public LayoutInflater mInflater;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public PopupMenuAdapter(Context context, Menu menu) {
        this.mInflater = LayoutInflater.from(context);
        ArrayList<MenuItem> arrayList = new ArrayList<>();
        this.mAvailableItems = arrayList;
        if (menu != null) {
            buildMenuItems(menu, arrayList);
        }
        this.mContext = context;
    }

    public final void buildMenuItems(Menu menu, ArrayList<MenuItem> arrayList) {
        arrayList.clear();
        if (menu != null) {
            int size = menu.size();
            for (int i = 0; i < size; i++) {
                MenuItem item = menu.getItem(i);
                if (checkMenuItem(item)) {
                    arrayList.add(item);
                }
            }
        }
    }

    public boolean checkMenuItem(MenuItem menuItem) {
        return menuItem.isVisible();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mAvailableItems.size();
    }

    @Override // android.widget.Adapter
    /* renamed from: getItem */
    public MenuItem mo2619getItem(int i) {
        return this.mAvailableItems.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mInflater.inflate(R$layout.miuix_appcompat_popup_menu_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(16908294);
            viewHolder.title = (TextView) view.findViewById(16908308);
            view.setTag(R$id.tag_popup_menu_item, viewHolder);
            AnimHelper.addPressAnimWithBg(view);
        }
        TaggingDrawableUtil.updateItemBackground(view, i, getCount());
        Object tag = view.getTag(R$id.tag_popup_menu_item);
        if (tag != null) {
            ViewHolder viewHolder2 = (ViewHolder) tag;
            MenuItem mo2619getItem = mo2619getItem(i);
            if (mo2619getItem.getIcon() != null) {
                viewHolder2.icon.setImageDrawable(mo2619getItem.getIcon());
                viewHolder2.icon.setVisibility(0);
            } else {
                viewHolder2.icon.setVisibility(8);
            }
            viewHolder2.title.setText(mo2619getItem.getTitle());
        }
        return view;
    }

    public void update(Menu menu) {
        buildMenuItems(menu, this.mAvailableItems);
        notifyDataSetChanged();
    }

    /* loaded from: classes3.dex */
    public static class ViewHolder {
        public ImageView icon;
        public TextView title;

        public ViewHolder() {
        }
    }
}
