package com.miui.gallery.search.navigationpage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.display.BaseSuggestionViewHolder;
import com.miui.gallery.search.core.display.OnActionClickListener;
import com.miui.gallery.search.core.display.SuggestionViewFactory;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class NavigationSectionAdapter extends BaseAdapter implements Adapter {
    public OnActionClickListener mActionClickListener;
    public boolean mAppendMoreItemToItems;
    public Context mContext;
    public String mFrom;
    public LayoutInflater mInflater;
    public SuggestionSection mSection;
    public SuggestionViewFactory mViewFactory;

    public static /* synthetic */ void $r8$lambda$u7XD5HDvMj6AulNwzuVNKTiAY6Y(NavigationSectionAdapter navigationSectionAdapter, int i, View view) {
        navigationSectionAdapter.lambda$getView$0(i, view);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public NavigationSectionAdapter(Context context, SuggestionViewFactory suggestionViewFactory, SuggestionSection suggestionSection, String str, boolean z) {
        this.mContext = context;
        this.mViewFactory = suggestionViewFactory;
        this.mInflater = LayoutInflater.from(context);
        this.mSection = suggestionSection;
        this.mFrom = str;
        this.mAppendMoreItemToItems = z;
    }

    public void changeSectionData(SuggestionSection suggestionSection) {
        if (suggestionSection == this.mSection) {
            return;
        }
        this.mSection = suggestionSection;
        notifyDataSetChanged();
    }

    public void setActionClickListener(OnActionClickListener onActionClickListener) {
        this.mActionClickListener = onActionClickListener;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        BaseSuggestionViewHolder baseSuggestionViewHolder;
        final int i2 = i == getItemCount() ? -3 : i;
        if (view == null) {
            baseSuggestionViewHolder = this.mViewFactory.createViewHolder(viewGroup, this.mViewFactory.getViewType(this.mSection.getQueryInfo(), this.mSection, i2));
            baseSuggestionViewHolder.itemView.setTag(baseSuggestionViewHolder);
        } else {
            baseSuggestionViewHolder = (BaseSuggestionViewHolder) view.getTag();
        }
        if (i2 >= 0 || i2 == -3) {
            if (mo1336getItem(i2).getIntentActionURI() != null && baseSuggestionViewHolder.getClickView() != null) {
                baseSuggestionViewHolder.getClickView().setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.navigationpage.NavigationSectionAdapter.1
                    {
                        NavigationSectionAdapter.this = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        if (NavigationSectionAdapter.this.mActionClickListener == null) {
                            return;
                        }
                        FolmeUtil.setFakeTouchAnim(view2, 0.9f, 200L);
                        NavigationSectionAdapter navigationSectionAdapter = NavigationSectionAdapter.this;
                        Bundle buildSearchEventExtras = SearchStatUtils.buildSearchEventExtras(null, new String[]{"from", "sectionType"}, new String[]{navigationSectionAdapter.mFrom, navigationSectionAdapter.mSection.getSectionTypeString()});
                        NavigationSectionAdapter navigationSectionAdapter2 = NavigationSectionAdapter.this;
                        navigationSectionAdapter2.mActionClickListener.onClick(view2, 0, navigationSectionAdapter2.mo1336getItem(i2), buildSearchEventExtras);
                    }
                });
            }
            if (this.mSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_HISTORY && mo1336getItem(i2).getIntentActionURI() != null && baseSuggestionViewHolder.getIconView() != null) {
                baseSuggestionViewHolder.getIconView().setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.navigationpage.NavigationSectionAdapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        NavigationSectionAdapter.$r8$lambda$u7XD5HDvMj6AulNwzuVNKTiAY6Y(NavigationSectionAdapter.this, i2, view2);
                    }
                });
            }
        }
        this.mViewFactory.bindViewHolder(this.mSection.getQueryInfo(), this.mSection, i2, baseSuggestionViewHolder, this.mActionClickListener);
        return baseSuggestionViewHolder.itemView;
    }

    public /* synthetic */ void lambda$getView$0(int i, View view) {
        this.mActionClickListener.onClick(view, 3, mo1336getItem(i), SearchStatUtils.buildSearchEventExtras(null, new String[]{"from"}, new String[]{"from_navigation_history"}));
    }

    public boolean hasMoreItem() {
        return this.mSection.moveToMore() != null;
    }

    @Override // android.widget.Adapter
    /* renamed from: getItem */
    public Suggestion mo1336getItem(int i) {
        if (i == -3) {
            return this.mSection.moveToMore();
        }
        if (i < 0) {
            return null;
        }
        this.mSection.moveToPosition(i);
        return this.mSection.getCurrent();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return getItemCount() + ((!this.mAppendMoreItemToItems || !hasMoreItem()) ? 0 : 1);
    }

    public int getItemCount() {
        return Math.min(this.mSection.getCount(), SearchConfig.get().getNavigationConfig().getSectionMaxItemCount(this.mSection.getSectionType()));
    }
}
