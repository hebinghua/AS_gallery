package com.miui.gallery.collage.app.layout;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.collage.app.common.AbstractLayoutFragment;
import com.miui.gallery.collage.app.common.CollageMenuFragment;
import com.miui.gallery.collage.app.layout.LayoutMenuAdapter;
import com.miui.gallery.collage.core.CollagePresenter;
import com.miui.gallery.collage.core.layout.LayoutModel;
import com.miui.gallery.collage.core.layout.LayoutPresenter;
import com.miui.gallery.collage.render.CollageMargin;
import com.miui.gallery.collage.render.CollageRatio;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LayoutMenuFragment extends CollageMenuFragment<LayoutPresenter, AbstractLayoutFragment> {
    public LayoutMenuAdapter mLayoutMenuAdapter;
    public SimpleRecyclerView mRecyclerView;
    public List<LayoutModel> mLayoutModels = new ArrayList();
    public boolean mViewReady = false;
    public boolean mDataReady = false;
    public boolean mDataInit = false;
    public CollageMargin mCollageMargin = CollageMargin.NONE;
    public CollageRatio mCollageRatio = CollageRatio.RATIO_3_4;
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.collage.app.layout.LayoutMenuFragment.1
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            onClick(recyclerView, i);
            return true;
        }

        public void onClick(RecyclerView recyclerView, int i) {
            recyclerView.smoothScrollToPosition(i);
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(i);
            if (i == 0) {
                CollageMargin[] values = CollageMargin.values();
                CollageMargin collageMargin = values[(LayoutMenuFragment.this.mCollageMargin.ordinal() + 1) % values.length];
                ((LayoutMenuAdapter.MarginViewHolder) findViewHolderForAdapterPosition).getmMarginIcon().setImageResource(collageMargin.iconRes);
                findViewHolderForAdapterPosition.itemView.setContentDescription(collageMargin.talkbackName);
                LayoutMenuFragment.this.getRenderFragment().onSelectMargin(collageMargin.marginSize);
                LayoutMenuFragment.this.mCollageMargin = collageMargin;
                ((Animatable) ((LayoutMenuAdapter.MarginViewHolder) findViewHolderForAdapterPosition).getmMarginIcon().getDrawable()).start();
            } else if (i != 1) {
                LayoutMenuFragment.this.mLayoutMenuAdapter.setSelection(i);
                LayoutMenuFragment.this.onSelectModel(i);
            } else {
                CollageRatio[] values2 = CollageRatio.values();
                CollageRatio collageRatio = values2[(LayoutMenuFragment.this.mCollageRatio.ordinal() + 1) % values2.length];
                ((LayoutMenuAdapter.RatioViewHolder) findViewHolderForAdapterPosition).getmRatioIcon().setImageResource(collageRatio.iconRes);
                findViewHolderForAdapterPosition.itemView.setContentDescription(collageRatio.talkbackName);
                LayoutMenuFragment.this.getRenderFragment().onSelectRatio(collageRatio.ratio);
                LayoutMenuFragment.this.mCollageRatio = collageRatio;
            }
        }
    };
    public CollagePresenter.DataLoadListener mDataLoadListener = new CollagePresenter.DataLoadListener() { // from class: com.miui.gallery.collage.app.layout.LayoutMenuFragment.2
        @Override // com.miui.gallery.collage.core.CollagePresenter.DataLoadListener
        public void onDataLoad() {
            LayoutMenuFragment.this.mDataReady = true;
            LayoutMenuFragment.this.reloadData();
            LayoutMenuFragment.this.notifyDataInit();
        }
    };

    @Override // com.miui.gallery.collage.app.common.CollageMenuFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ((LayoutPresenter) this.mPresenter).loadDataFromResourceAsync(this.mDataLoadListener);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.collage_layout_menu, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        this.mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.collage_layout_list);
        this.mLayoutMenuAdapter = new LayoutMenuAdapter(getActivity(), this.mLayoutModels, new Selectable.Selector(getResources().getDrawable(R.drawable.collage_item_background_selector)));
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        customScrollerLinearLayoutManager.setOrientation(0);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.addItemDecoration(new BlankDivider(getResources().getDimensionPixelSize(R.dimen.collage_menu_item_margin_start), getResources().getDimensionPixelSize(R.dimen.collage_menu_item_margin_start), getResources().getDimensionPixelSize(R.dimen.collage_menu_item_margin_start), 0, 0));
        this.mRecyclerView.setAdapter(this.mLayoutMenuAdapter);
        this.mViewReady = true;
        notifyDataInit();
    }

    public final void notifyDataInit() {
        if (!this.mViewReady || !this.mDataReady || this.mDataInit) {
            return;
        }
        onSelectModel(2);
        this.mLayoutMenuAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mDataInit = true;
    }

    public final void reloadData() {
        this.mLayoutModels.clear();
        P p = this.mPresenter;
        List<LayoutModel> layouts = ((LayoutPresenter) p).getLayouts(((LayoutPresenter) p).getImageCount());
        layouts.add(0, new LayoutModel());
        layouts.add(1, new LayoutModel());
        this.mLayoutModels.addAll(layouts);
        this.mLayoutMenuAdapter.notifyDataSetChanged();
    }

    public final void onSelectModel(int i) {
        if (this.mLayoutModels.size() == 0) {
            return;
        }
        getRenderFragment().onSelectModel(this.mLayoutModels.get(i));
    }
}
