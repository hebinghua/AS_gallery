package com.miui.gallery.collage.app.poster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.collage.app.common.AbstractPosterFragment;
import com.miui.gallery.collage.app.common.CollageMenuFragment;
import com.miui.gallery.collage.core.CollagePresenter;
import com.miui.gallery.collage.core.poster.PosterModel;
import com.miui.gallery.collage.core.poster.PosterPresenter;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PosterMenuFragment extends CollageMenuFragment<PosterPresenter, AbstractPosterFragment> {
    public PosterMenuAdapter mPosterMenuAdapter;
    public SimpleRecyclerView mRecyclerView;
    public List<PosterModel> mPosterModels = new ArrayList();
    public boolean mViewReady = false;
    public boolean mDataReady = false;
    public boolean mDataInit = false;
    public CollagePresenter.DataLoadListener mInitDataLoadListener = new CollagePresenter.DataLoadListener() { // from class: com.miui.gallery.collage.app.poster.PosterMenuFragment.1
        @Override // com.miui.gallery.collage.core.CollagePresenter.DataLoadListener
        public void onDataLoad() {
            PosterMenuFragment.this.mDataReady = true;
            PosterMenuFragment.this.reloadData();
            PosterMenuFragment.this.notifyDataInit();
        }
    };
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.collage.app.poster.PosterMenuFragment.2
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            recyclerView.smoothScrollToPosition(i);
            PosterMenuFragment.this.mPosterMenuAdapter.setSelection(i);
            PosterMenuFragment.this.onSelectModel(i);
            return true;
        }
    };

    @Override // com.miui.gallery.collage.app.common.CollageMenuFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ((PosterPresenter) this.mPresenter).loadDataFromResourceAsync(this.mInitDataLoadListener);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.collage_poster_menu, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        this.mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.list);
        PosterMenuAdapter posterMenuAdapter = new PosterMenuAdapter(getActivity(), this.mPosterModels, new Selectable.Selector(getResources().getDrawable(R.drawable.collage_item_background_selector)));
        this.mPosterMenuAdapter = posterMenuAdapter;
        posterMenuAdapter.setImageCount(((PosterPresenter) this.mPresenter).getImageCount());
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        customScrollerLinearLayoutManager.setOrientation(0);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.addItemDecoration(new BlankDivider(getResources(), R.dimen.collage_menu_item_margin_start));
        this.mRecyclerView.setAdapter(this.mPosterMenuAdapter);
        this.mViewReady = true;
        notifyDataInit();
    }

    public final void notifyDataInit() {
        if (!this.mViewReady || !this.mDataReady || this.mDataInit) {
            return;
        }
        onSelectModel(0);
        this.mDataInit = true;
        this.mPosterMenuAdapter.setOnItemClickListener(this.mOnItemClickListener);
    }

    public final void onSelectModel(int i) {
        if (this.mPosterModels.size() == 0) {
            return;
        }
        PosterModel posterModel = this.mPosterModels.get(i);
        getRenderFragment().onSelectModel(posterModel, ((PosterPresenter) this.mPresenter).getPosterCollageLayout(posterModel));
    }

    public final void reloadData() {
        this.mPosterModels.clear();
        List<PosterModel> posters = ((PosterPresenter) this.mPresenter).getPosters();
        if (posters != null) {
            this.mPosterModels.addAll(posters);
        }
        this.mPosterMenuAdapter.notifyDataSetChanged();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        DefaultLogger.d("PosterMenuFragment", "onDestroy");
    }
}
