package com.miui.gallery.editor.photo.app.sticker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.menu.StickerView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.StickerCategory;
import com.miui.gallery.editor.photo.core.common.model.StickerData;
import com.miui.gallery.editor.photo.core.common.provider.AbstractStickerProvider;
import com.miui.gallery.editor.photo.core.imports.sticker.StickerFragment;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.ScrollHelper;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerViewNoSpring;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class StickerMenuFragment extends MenuFragment<AbstractEffectFragment, AbstractStickerProvider> {
    public List<StickerCategory> mCategories;
    public TextView mClear;
    public HeaderAdapter mHeaderAdapter;
    public SimpleRecyclerViewNoSpring mHeaderView;
    public OnItemClickListener mItemClickListener;
    public View.OnClickListener mOnClearClickListener;
    public ViewPager2.OnPageChangeCallback mOnPageChangeCallback;
    public OnItemClickListener mOnStickerSelectedListener;
    public View mRecentView;
    public ViewPager2 mStickerPager;
    public StickerPagerAdapter mStickerPagerAdapter;

    public StickerMenuFragment() {
        super(Effect.STICKER);
        this.mOnPageChangeCallback = new ViewPager2.OnPageChangeCallback() { // from class: com.miui.gallery.editor.photo.app.sticker.StickerMenuFragment.3
            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public void onPageSelected(int i) {
                super.onPageSelected(i);
                if (i == 0) {
                    if (!StickerMenuFragment.this.mRecentView.isSelected()) {
                        StickerMenuFragment.this.mRecentView.setSelected(true);
                    }
                    StickerMenuFragment.this.mHeaderAdapter.setSelection(-1);
                    StickerMenuFragment.this.mStickerPager.setCurrentItem(0, true);
                    return;
                }
                if (StickerMenuFragment.this.mRecentView.isSelected()) {
                    StickerMenuFragment.this.mRecentView.setSelected(false);
                }
                int i2 = i - 1;
                StickerMenuFragment.this.mHeaderAdapter.setSelection(i2);
                StickerMenuFragment.this.mStickerPager.setCurrentItem(i, true);
                ScrollHelper.onItemClick(StickerMenuFragment.this.mHeaderView, i2);
            }

            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public void onPageScrollStateChanged(int i) {
                super.onPageScrollStateChanged(i);
                if (i == 0) {
                    StickerMenuFragment.this.mStickerPager.setUserInputEnabled(true);
                } else if (i != 2) {
                } else {
                    StickerMenuFragment.this.mStickerPager.setUserInputEnabled(false);
                }
            }
        };
        this.mOnClearClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.sticker.StickerMenuFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (StickerMenuFragment.this.getRenderFragment() != null) {
                    ((AbstractEffectFragment) StickerMenuFragment.this.getRenderFragment()).clear();
                }
                StickerMenuFragment.this.mClear.setVisibility(8);
            }
        };
        this.mItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.sticker.StickerMenuFragment.5
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                StickerMenuFragment.this.mStickerPager.setCurrentItem(i + 1, true);
                if (StickerMenuFragment.this.mRecentView.isSelected()) {
                    StickerMenuFragment.this.mRecentView.setSelected(false);
                }
                StickerMenuFragment.this.mHeaderAdapter.setSelection(i);
                ScrollHelper.onItemClick(recyclerView, i);
                return true;
            }
        };
        this.mOnStickerSelectedListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.sticker.StickerMenuFragment.6
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                recyclerView.smoothScrollToPosition(i);
                ((AbstractEffectFragment) StickerMenuFragment.this.getRenderFragment()).add(((CategoryDetailAdapter) recyclerView.getAdapter()).getDataList().get(i), null);
                StickerMenuFragment.this.mClear.setVisibility(0);
                return true;
            }
        };
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ArrayList arrayList = new ArrayList();
        this.mCategories = arrayList;
        arrayList.addAll(((AbstractStickerProvider) this.mSdkProvider).list());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new StickerView(getContext()), getView(), true);
        this.mStickerPagerAdapter.notifyDataSetChanged();
        this.mStickerPager.setOrientation(!isLayoutPortrait());
        int currentItem = this.mStickerPager.getCurrentItem();
        this.mStickerPager.setCurrentItem(currentItem, false);
        if (currentItem > 0) {
            this.mHeaderView.scrollToPosition(currentItem - 1);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new StickerView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mHeaderAdapter = new HeaderAdapter(new ArrayList(this.mCategories), new Selectable.Selector(getActivity().getResources().getColor(R.color.sticker_select_background)));
        this.mCategories.add(0, new CategoryRecent());
        SimpleRecyclerViewNoSpring simpleRecyclerViewNoSpring = (SimpleRecyclerViewNoSpring) view.findViewById(R.id.category);
        this.mHeaderView = simpleRecyclerViewNoSpring;
        simpleRecyclerViewNoSpring.setAdapter(this.mHeaderAdapter);
        this.mHeaderView.setBackgroundColor(getResources().getColor(R.color.black));
        this.mHeaderAdapter.setOnItemClickListener(this.mItemClickListener);
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mHeaderView);
        this.mStickerPagerAdapter = new StickerPagerAdapter();
        ViewPager2 viewPager2 = (ViewPager2) view.findViewById(R.id.view_pager);
        this.mStickerPager = viewPager2;
        viewPager2.setOrientation(!isLayoutPortrait());
        this.mStickerPager.setAdapter(this.mStickerPagerAdapter);
        this.mStickerPager.setCurrentItem(1, false);
        this.mStickerPager.registerOnPageChangeCallback(this.mOnPageChangeCallback);
        TextView textView = (TextView) view.findViewById(R.id.title);
        this.mClear = textView;
        textView.setText(R.string.sticker_clear);
        this.mClear.setVisibility(8);
        this.mClear.setOnClickListener(this.mOnClearClickListener);
        View findViewById = view.findViewById(R.id.recent);
        this.mRecentView = findViewById;
        findViewById.setBackground(getResources().getDrawable(R.drawable.sticker_tab_bg_color_selector));
        this.mRecentView.setContentDescription(getResources().getString(R.string.sticker_tab_talkback_recent));
        this.mRecentView.setSelected(false);
        this.mRecentView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.sticker.StickerMenuFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!StickerMenuFragment.this.mRecentView.isSelected()) {
                    StickerMenuFragment.this.mRecentView.setSelected(true);
                }
                StickerMenuFragment.this.mHeaderAdapter.setSelection(-1);
                StickerMenuFragment.this.mStickerPager.setCurrentItem(0, true);
            }
        });
        if (getRenderFragment() instanceof StickerFragment) {
            ((StickerFragment) getRenderFragment()).setCallback(new StickerFragment.Callback() { // from class: com.miui.gallery.editor.photo.app.sticker.StickerMenuFragment.2
                @Override // com.miui.gallery.editor.photo.core.imports.sticker.StickerFragment.Callback
                public void onEmpty() {
                    StickerMenuFragment.this.mClear.setVisibility(8);
                }
            });
        }
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(view.findViewById(R.id.cancel), build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(view.findViewById(R.id.ok), build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mClear, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mRecentView, build, null, null, null, true);
    }

    /* loaded from: classes2.dex */
    public class StickerPagerAdapter extends RecyclerView.Adapter<VH> {
        public AbstractStickerProvider mProvider = (AbstractStickerProvider) SdkManager.INSTANCE.getProvider(Effect.STICKER);

        public StickerPagerAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public VH mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new VH(new StickerRecyclerView(StickerMenuFragment.this.getContext()));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(VH vh, int i) {
            List<StickerData> list;
            StickerRecyclerView stickerRecyclerView = (StickerRecyclerView) vh.itemView;
            stickerRecyclerView.setFlingVelocityScale(1.0f);
            stickerRecyclerView.setAlwaysDisableSpring(true);
            RecyclerView.LayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, !StickerMenuFragment.this.isLayoutPortrait() ? 1 : 0);
            stickerRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            stickerRecyclerView.setNestedScrollingEnabled(true);
            stickerRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            long j = ((StickerCategory) StickerMenuFragment.this.mCategories.get(i)).id;
            if (j == -9223372036854775807L) {
                list = this.mProvider.recent();
                stickerRecyclerView.setTag("recent_tag");
            } else {
                list = this.mProvider.list(j);
            }
            CategoryDetailAdapter categoryDetailAdapter = new CategoryDetailAdapter(StickerMenuFragment.this.getActivity(), list);
            categoryDetailAdapter.setOnItemClickListener(StickerMenuFragment.this.mOnStickerSelectedListener);
            stickerRecyclerView.setAdapter(categoryDetailAdapter);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return StickerMenuFragment.this.mCategories.size();
        }
    }

    /* loaded from: classes2.dex */
    public static class VH extends RecyclerView.ViewHolder {
        public VH(View view) {
            super(view);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }
}
