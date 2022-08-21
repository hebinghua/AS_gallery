package com.miui.gallery.search.core.display;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.display.QuickAdapterBase;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.HashMap;
import java.util.Objects;

/* loaded from: classes2.dex */
public class BaseSuggestionAdapter<S extends SuggestionCursor> extends QuickAdapterBase<BaseSuggestionViewHolder> {
    public OnActionClickListener mActionClickListener;
    public String mFrom;
    public QueryInfo mQueryInfo;
    public S mSuggestionCursor;
    public final SuggestionViewFactory mViewFactory;
    public final HashMap<String, Integer> mViewTypeMap;
    public final HashMap<Integer, String> mViewTypeReverseMap;

    public void notifyDataSetInvalidated() {
    }

    public BaseSuggestionAdapter(Activity activity, SuggestionViewFactory suggestionViewFactory, String str, OnActionClickListener onActionClickListener) {
        super(activity);
        this.mViewFactory = suggestionViewFactory;
        this.mFrom = str;
        this.mActionClickListener = onActionClickListener;
        this.mViewTypeMap = new HashMap<>();
        this.mViewTypeReverseMap = new HashMap<>();
        for (String str2 : suggestionViewFactory.getSuggestionViewTypes()) {
            if (!this.mViewTypeMap.containsKey(str2)) {
                HashMap<String, Integer> hashMap = this.mViewTypeMap;
                hashMap.put(str2, Integer.valueOf(hashMap.size() + 16));
                HashMap<Integer, String> hashMap2 = this.mViewTypeReverseMap;
                hashMap2.put(Integer.valueOf(hashMap2.size() + 16), str2);
            }
        }
    }

    public BaseSuggestionAdapter(Activity activity, SuggestionViewFactory suggestionViewFactory, String str) {
        this(activity, suggestionViewFactory, str, new DefaultActionClickListener(activity));
    }

    public void setActionClickListener(OnActionClickListener onActionClickListener) {
        this.mActionClickListener = onActionClickListener;
    }

    public void changeSuggestions(QueryInfo queryInfo, S s) {
        changeSuggestions(queryInfo, s, true);
    }

    public void changeSuggestionsByIndex(QueryInfo queryInfo, S s, int i) {
        S s2 = this.mSuggestionCursor;
        if (s2 != null) {
            s2.close();
        }
        this.mSuggestionCursor = s;
        this.mQueryInfo = queryInfo;
        if (s != null) {
            notifyItemChanged(i);
        } else {
            notifyDataSetInvalidated();
        }
    }

    public void changeSuggestions(QueryInfo queryInfo, S s, boolean z) {
        if (!Objects.equals(this.mSuggestionCursor, s) || this.mQueryInfo != queryInfo) {
            if (z) {
                S s2 = this.mSuggestionCursor;
                this.mSuggestionCursor = s;
                this.mQueryInfo = queryInfo;
                notifyDataChanged(new SuggestionDiffCallback(s2, s));
                if (s2 == null) {
                    return;
                }
                s2.close();
                return;
            }
            S s3 = this.mSuggestionCursor;
            if (s3 != null) {
                s3.close();
            }
            this.mSuggestionCursor = s;
            this.mQueryInfo = queryInfo;
            if (s != null) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class SuggestionDiffCallback extends QuickAdapterBase.InnerDiffCallback {
        public SuggestionCursor mNewSuggestionCursor;
        public SuggestionCursor mOldSuggestionCursor;

        public SuggestionDiffCallback(SuggestionCursor suggestionCursor, SuggestionCursor suggestionCursor2) {
            this.mOldSuggestionCursor = suggestionCursor;
            this.mNewSuggestionCursor = suggestionCursor2;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            SuggestionCursor suggestionCursor;
            SuggestionCursor suggestionCursor2 = this.mOldSuggestionCursor;
            if (suggestionCursor2 == null || i >= suggestionCursor2.getCount() || (suggestionCursor = this.mNewSuggestionCursor) == null || i2 >= suggestionCursor.getCount()) {
                return false;
            }
            this.mOldSuggestionCursor.moveToPosition(i);
            this.mNewSuggestionCursor.moveToPosition(i2);
            return Objects.equals(this.mOldSuggestionCursor.getCurrent(), this.mNewSuggestionCursor.getCurrent());
        }
    }

    public S getSuggestionCursor() {
        return this.mSuggestionCursor;
    }

    public void bindActionClickListener(final View view, final int i, final SuggestionCursor suggestionCursor, final int i2, final String str) {
        view.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.core.display.BaseSuggestionAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String str2 = str;
                Bundle bundle = null;
                if (str2 != null) {
                    bundle = SearchStatUtils.buildSearchEventExtras(null, new String[]{"from"}, new String[]{str2});
                }
                suggestionCursor.moveToPosition(i2);
                BaseSuggestionAdapter.this.getActionClickListener().onClick(view, i, suggestionCursor, bundle);
            }
        });
    }

    public OnActionClickListener getActionClickListener() {
        return this.mActionClickListener;
    }

    @Override // com.miui.gallery.search.core.display.QuickAdapterBase
    public int getInnerItemViewType(int i) {
        this.mSuggestionCursor.moveToPosition(i);
        String viewType = this.mViewFactory.getViewType(this.mQueryInfo, this.mSuggestionCursor, i);
        if (!this.mViewTypeMap.containsKey(viewType)) {
            throw new IllegalStateException("Unknown viewType " + viewType);
        }
        return this.mViewTypeMap.get(viewType).intValue();
    }

    @Override // com.miui.gallery.search.core.display.QuickAdapterBase
    public BaseSuggestionViewHolder createInnerItemViewHolder(ViewGroup viewGroup, int i) {
        if (!this.mViewTypeReverseMap.containsKey(Integer.valueOf(i))) {
            throw new IllegalStateException("Unknown viewType " + i);
        }
        return this.mViewFactory.createViewHolder(viewGroup, this.mViewTypeReverseMap.get(Integer.valueOf(i)));
    }

    @Override // com.miui.gallery.search.core.display.QuickAdapterBase
    public void bindInnerItemViewHolder(BaseSuggestionViewHolder baseSuggestionViewHolder, int i) {
        this.mSuggestionCursor.moveToPosition(i);
        if (this.mSuggestionCursor.getIntentActionURI() != null && baseSuggestionViewHolder.getClickView() != null) {
            bindActionClickListener(baseSuggestionViewHolder.getClickView(), 0, this.mSuggestionCursor, i, this.mFrom);
        }
        this.mViewFactory.bindViewHolder(this.mQueryInfo, this.mSuggestionCursor, i, baseSuggestionViewHolder, getActionClickListener());
    }

    @Override // com.miui.gallery.search.core.display.QuickAdapterBase
    public int getInnerItemViewCount() {
        S s = this.mSuggestionCursor;
        if (s == null) {
            return 0;
        }
        return s.getCount();
    }

    public boolean isEmpty() {
        return getInnerItemViewCount() <= 0;
    }
}
