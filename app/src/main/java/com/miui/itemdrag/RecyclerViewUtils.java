package com.miui.itemdrag;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/* loaded from: classes3.dex */
public class RecyclerViewUtils {
    public static boolean isLinearLayoutType(int i) {
        return i == 3 || i == 4;
    }

    public static RecyclerView.ViewHolder findChildViewHolderUnderWithoutTranslation(RecyclerView recyclerView, float f, float f2) {
        View findChildViewUnderWithoutTranslation = findChildViewUnderWithoutTranslation(recyclerView, f, f2);
        if (findChildViewUnderWithoutTranslation != null) {
            return recyclerView.getChildViewHolder(findChildViewUnderWithoutTranslation);
        }
        return null;
    }

    public static int getLayoutType(RecyclerView recyclerView) {
        return getLayoutType(recyclerView.getLayoutManager());
    }

    public static View findChildViewUnderWithoutTranslation(ViewGroup viewGroup, float f, float f2) {
        for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = viewGroup.getChildAt(childCount);
            if (f >= childAt.getLeft() && f <= childAt.getRight() && f2 >= childAt.getTop() && f2 <= childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    public static int findFirstCompletelyVisibleItemPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        }
        return -1;
    }

    public static int findLastCompletelyVisibleItemPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        }
        return -1;
    }

    public static int findLastVisibleItemPosition(RecyclerView recyclerView, boolean z) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (z) {
                return findLastVisibleItemPositionIncludesPadding((LinearLayoutManager) layoutManager);
            }
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        return -1;
    }

    public static int getLayoutType(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getOrientation() == 0 ? 1 : 2;
        } else if (!(layoutManager instanceof LinearLayoutManager)) {
            return -1;
        } else {
            return ((LinearLayoutManager) layoutManager).getOrientation() == 0 ? 3 : 4;
        }
    }

    public static int getOrientation(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager;
        if (recyclerView == null || (layoutManager = recyclerView.getLayoutManager()) == null) {
            return -1;
        }
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getOrientation();
        }
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        }
        if (!(layoutManager instanceof StaggeredGridLayoutManager)) {
            return -1;
        }
        return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
    }

    public static int getSpanCount(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        if (!(layoutManager instanceof StaggeredGridLayoutManager)) {
            return 1;
        }
        return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
    }

    public static NestedScrollView findAncestorNestedScrollView(View view) {
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof NestedScrollView) {
                return (NestedScrollView) parent;
            }
        }
        return null;
    }

    public static int findFirstVisibleItemPosition(RecyclerView recyclerView, boolean z) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (z) {
                return findFirstVisibleItemPositionIncludesPadding((LinearLayoutManager) layoutManager);
            }
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        return -1;
    }

    public static int findFirstVisibleItemPositionIncludesPadding(LinearLayoutManager linearLayoutManager) {
        View findOneVisibleChildIncludesPadding = findOneVisibleChildIncludesPadding(linearLayoutManager, 0, linearLayoutManager.getChildCount(), false, true);
        if (findOneVisibleChildIncludesPadding == null) {
            return -1;
        }
        return linearLayoutManager.getPosition(findOneVisibleChildIncludesPadding);
    }

    public static int findLastVisibleItemPositionIncludesPadding(LinearLayoutManager linearLayoutManager) {
        View findOneVisibleChildIncludesPadding = findOneVisibleChildIncludesPadding(linearLayoutManager, linearLayoutManager.getChildCount() - 1, -1, false, true);
        if (findOneVisibleChildIncludesPadding == null) {
            return -1;
        }
        return linearLayoutManager.getPosition(findOneVisibleChildIncludesPadding);
    }

    public static View findOneVisibleChildIncludesPadding(LinearLayoutManager linearLayoutManager, int i, int i2, boolean z, boolean z2) {
        int i3 = 1;
        boolean z3 = linearLayoutManager.getOrientation() == 1;
        int height = z3 ? linearLayoutManager.getHeight() : linearLayoutManager.getWidth();
        if (i2 <= i) {
            i3 = -1;
        }
        View view = null;
        while (i != i2) {
            View childAt = linearLayoutManager.getChildAt(i);
            int top = z3 ? childAt.getTop() : childAt.getLeft();
            int bottom = z3 ? childAt.getBottom() : childAt.getRight();
            if (top < height && bottom > 0) {
                if (!z) {
                    return childAt;
                }
                if (top >= 0 && bottom <= height) {
                    return childAt;
                }
                if (z2 && view == null) {
                    view = childAt;
                }
            }
            i += i3;
        }
        return view;
    }

    public static Rect getLayoutMargins(View view, Rect rect) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            rect.left = marginLayoutParams.leftMargin;
            rect.right = marginLayoutParams.rightMargin;
            rect.top = marginLayoutParams.topMargin;
            rect.bottom = marginLayoutParams.bottomMargin;
        } else {
            rect.bottom = 0;
            rect.top = 0;
            rect.right = 0;
            rect.left = 0;
        }
        return rect;
    }

    public static int getSpanSize(RecyclerView.ViewHolder viewHolder) {
        View laidOutItemView = getLaidOutItemView(viewHolder);
        if (laidOutItemView == null) {
            return -1;
        }
        ViewGroup.LayoutParams layoutParams = laidOutItemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (!((StaggeredGridLayoutManager.LayoutParams) layoutParams).isFullSpan()) {
                return 1;
            }
            return getSpanCount((RecyclerView) laidOutItemView.getParent());
        } else if (layoutParams instanceof GridLayoutManager.LayoutParams) {
            return ((GridLayoutManager.LayoutParams) layoutParams).getSpanSize();
        } else {
            return layoutParams instanceof RecyclerView.LayoutParams ? 1 : -1;
        }
    }

    public static View getLaidOutItemView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder == null) {
            return null;
        }
        View view = viewHolder.itemView;
        if (ViewCompat.isLaidOut(view)) {
            return view;
        }
        return null;
    }

    public static boolean isGridlayoutAndIsSingleSpanCountLayoutType(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        return (layoutManager instanceof GridLayoutManager) && ((GridLayoutManager) layoutManager).getSpanCount() == 1;
    }

    public static int getChildAdapterPotision(RecyclerView recyclerView, int i) {
        if (recyclerView == null || i < 0) {
            return -1;
        }
        return recyclerView.getChildAdapterPosition(recyclerView.getChildAt(i));
    }

    public static int getSpanIndex(RecyclerView recyclerView, View view) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            return gridLayoutManager.getSpanSizeLookup().getSpanIndex(recyclerView.getChildLayoutPosition(view), gridLayoutManager.getSpanCount());
        }
        return -1;
    }

    public static int getSpanIndex(RecyclerView recyclerView, int i) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            return gridLayoutManager.getSpanSizeLookup().getSpanIndex(i, gridLayoutManager.getSpanCount());
        }
        return -1;
    }
}
