package miuix.appcompat.internal.view.menu;

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
import miuix.appcompat.R$layout;
import miuix.internal.util.AnimHelper;
import miuix.internal.util.TaggingDrawableUtil;

/* loaded from: classes3.dex */
public class ImmersionMenuAdapter extends BaseAdapter {
    public ArrayList<MenuItem> mAvailableItems;
    public Context mContext;
    public LayoutInflater mInflater;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public ImmersionMenuAdapter(Context context, Menu menu) {
        this.mInflater = LayoutInflater.from(context);
        ArrayList<MenuItem> arrayList = new ArrayList<>();
        this.mAvailableItems = arrayList;
        buildVisibleItems(menu, arrayList);
        this.mContext = context;
    }

    public final void buildVisibleItems(Menu menu, ArrayList<MenuItem> arrayList) {
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
    public MenuItem mo2603getItem(int i) {
        return this.mAvailableItems.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mInflater.inflate(R$layout.miuix_appcompat_immersion_popup_menu_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(16908294);
            viewHolder.title = (TextView) view.findViewById(16908308);
            view.setTag(viewHolder);
            AnimHelper.addPressAnimWithBg(view);
        }
        TaggingDrawableUtil.updateItemPadding(view, i, getCount());
        Object tag = view.getTag();
        if (tag != null) {
            ViewHolder viewHolder2 = (ViewHolder) tag;
            MenuItem mo2603getItem = mo2603getItem(i);
            if (mo2603getItem.getIcon() != null) {
                viewHolder2.icon.setImageDrawable(mo2603getItem.getIcon());
                viewHolder2.icon.setVisibility(0);
            } else {
                viewHolder2.icon.setVisibility(8);
            }
            viewHolder2.title.setText(mo2603getItem.getTitle());
        }
        return view;
    }

    public void update(Menu menu) {
        buildVisibleItems(menu, this.mAvailableItems);
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
