package com.miui.gallery.search.resultpage;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewStub;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.context.SearchContext;
import com.miui.gallery.search.core.display.BaseSuggestionAdapter;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.widget.tsd.DrawerState;
import com.miui.gallery.widget.tsd.INestedTwoStageDrawer;

/* loaded from: classes2.dex */
public class FilterBarController {
    public Activity mActivity;
    public BaseSuggestionAdapter mAdapter;
    public View mContainer;
    public INestedTwoStageDrawer mDrawer;
    public String mFrom;
    public RecyclerView mRecyclerView;

    public FilterBarController(Activity activity, INestedTwoStageDrawer iNestedTwoStageDrawer, String str) {
        this.mActivity = activity;
        this.mDrawer = iNestedTwoStageDrawer;
        this.mFrom = str;
    }

    public void swapCursor(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        if (suggestionCursor == null && this.mContainer == null) {
            return;
        }
        if (this.mContainer == null) {
            View findViewById = ((View) this.mDrawer).findViewById(R.id.filter_bar_container);
            this.mContainer = findViewById;
            this.mRecyclerView = (RecyclerView) ((ViewStub) findViewById.findViewById(R.id.filter_bar_stub)).inflate().findViewById(R.id.filter_bar);
            BaseSuggestionAdapter baseSuggestionAdapter = new BaseSuggestionAdapter(this.mActivity, SearchContext.getInstance().getSuggestionViewFactory(), this.mFrom);
            this.mAdapter = baseSuggestionAdapter;
            this.mRecyclerView.setAdapter(baseSuggestionAdapter);
            this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mActivity, 0, false));
            this.mRecyclerView.addItemDecoration(new PaddingDecoration(this.mActivity.getResources().getDimensionPixelSize(R.dimen.filter_bar_edge_padding)));
        }
        if (suggestionCursor != null && this.mRecyclerView.getLayoutParams() != null) {
            this.mRecyclerView.getLayoutParams().height = this.mActivity.getResources().getDimensionPixelSize(suggestionCursor.getExtras().getInt("filter_style", 0) == 1 ? R.dimen.filter_bar_height : R.dimen.filter_bar_height_no_icon);
        }
        this.mAdapter.changeSuggestions(queryInfo, suggestionCursor);
    }

    public boolean isFilterBarVisible() {
        return this.mContainer.getVisibility() == 0;
    }

    public void showFilterBar(boolean z) {
        if (z && !isFilterBarVisible()) {
            this.mContainer.setVisibility(0);
            this.mContainer.post(new Runnable() { // from class: com.miui.gallery.search.resultpage.FilterBarController.1
                @Override // java.lang.Runnable
                public void run() {
                    FilterBarController.this.mDrawer.setDrawerState(DrawerState.OPEN, true, null);
                }
            });
        } else if (z || !isFilterBarVisible()) {
        } else {
            this.mContainer.setVisibility(8);
            this.mContainer.post(new Runnable() { // from class: com.miui.gallery.search.resultpage.FilterBarController.2
                @Override // java.lang.Runnable
                public void run() {
                    FilterBarController.this.mDrawer.setDrawerState(DrawerState.CLOSE, true, null);
                }
            });
        }
    }

    public int getFilterDataCount() {
        BaseSuggestionAdapter baseSuggestionAdapter = this.mAdapter;
        if (baseSuggestionAdapter != null) {
            return baseSuggestionAdapter.getItemCount();
        }
        return 0;
    }

    /* loaded from: classes2.dex */
    public class PaddingDecoration extends RecyclerView.ItemDecoration {
        public final int mMargin;

        public PaddingDecoration(int i) {
            this.mMargin = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            if (childAdapterPosition != 0) {
                if (childAdapterPosition == FilterBarController.this.mAdapter.getItemCount() - 1) {
                    rect.set(this.mMargin, 0, 0, 0);
                    return;
                }
                int i = this.mMargin;
                rect.set(i, 0, i, 0);
                return;
            }
            rect.set(0, 0, this.mMargin, 0);
        }
    }
}
