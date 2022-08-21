package com.miui.gallery.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.miui.gallery.ui.ModernAlbumDetailFragment;
import java.util.List;

/* loaded from: classes.dex */
public class AlbumDetailGroupingAdapter extends FragmentStateAdapter {
    public final List<Long> mChildAlbumIdList;

    public AlbumDetailGroupingAdapter(FragmentActivity fragmentActivity, List<Long> list) {
        super(fragmentActivity);
        this.mChildAlbumIdList = list;
    }

    @Override // androidx.viewpager2.adapter.FragmentStateAdapter
    public Fragment createFragment(int i) {
        return ModernAlbumDetailFragment.Companion.newInstance(this.mChildAlbumIdList.get(i).longValue());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mChildAlbumIdList.size();
    }
}
