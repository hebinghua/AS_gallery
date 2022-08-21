package com.miui.gallery.editor.photo.app.navigator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.AbstractNaviFragment;
import com.miui.gallery.editor.ui.menu.type.EditNavMenuView;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerViewNoSpring;
import java.util.List;

/* loaded from: classes2.dex */
public class ScreenshotNaviFragment extends AbstractNaviFragment {
    public Adapter mAdapter;
    public SimpleRecyclerViewNoSpring mNavigator;
    public OnItemClickListener mOnItemSelectedListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.navigator.ScreenshotNaviFragment.1
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            ScreenshotNaviFragment screenshotNaviFragment = ScreenshotNaviFragment.this;
            screenshotNaviFragment.notifyNavigate(((AbstractNaviFragment.NavigatorData) screenshotNaviFragment.getNaviData().get(i)).effect);
            return true;
        }
    };

    @Override // com.miui.gallery.editor.photo.app.AbstractNaviFragment
    public View onCreateNavigator(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new EditNavMenuView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.app.AbstractNaviFragment
    public void onNavigatorCreated(View view, Bundle bundle) {
        this.mNavigator = (SimpleRecyclerViewNoSpring) view.findViewById(R.id.recycler_view);
        Adapter adapter = new Adapter(getNaviData());
        this.mAdapter = adapter;
        this.mNavigator.setAdapter(adapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemSelectedListener);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.photo_editor_screen_nav_item_start);
        int size = getNaviData() == null ? 0 : getNaviData().size();
        int i = 1;
        if (size > 1) {
            i = size - 1;
        }
        this.mNavigator.addItemDecoration(new BlankDivider(dimensionPixelSize, dimensionPixelSize, ((ScreenUtils.getScreenWidth() - (dimensionPixelSize * 2)) - (size * getResources().getDimensionPixelSize(R.dimen.video_editor_nav_item_width))) / i, 0, 0));
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mNavigator);
    }

    /* loaded from: classes2.dex */
    public static final class Adapter extends com.miui.gallery.widget.recyclerview.Adapter<NavigatorHolder> {
        public List<AbstractNaviFragment.NavigatorData> mNavigatorData;

        public Adapter(List<AbstractNaviFragment.NavigatorData> list) {
            this.mNavigatorData = list;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public NavigatorHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new NavigatorHolder(getInflater().inflate(R.layout.common_editor_navigator_item, viewGroup, false));
        }

        @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(NavigatorHolder navigatorHolder, int i) {
            super.onBindViewHolder((Adapter) navigatorHolder, i);
            navigatorHolder.bind(this.mNavigatorData.get(i));
        }

        @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mNavigatorData.size();
        }
    }

    /* loaded from: classes2.dex */
    public static final class NavigatorHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mView;

        public NavigatorHolder(View view) {
            super(view);
            this.mView = (TextView) view.findViewById(R.id.label);
            this.mImageView = (ImageView) view.findViewById(R.id.img_nav);
        }

        public void bind(AbstractNaviFragment.NavigatorData navigatorData) {
            this.mView.setText(navigatorData.name);
            this.mImageView.setImageResource(navigatorData.icon);
        }
    }
}
