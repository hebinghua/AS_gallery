package com.miui.gallery.search.core.display;

import android.app.Activity;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;

/* loaded from: classes2.dex */
public class LocationListAdapter extends BaseSuggestionAdapter<SuggestionCursor> {
    public boolean mIsMapAlbumAvailable;

    public LocationListAdapter(Activity activity, SuggestionViewFactory suggestionViewFactory, String str, OnActionClickListener onActionClickListener) {
        super(activity, suggestionViewFactory, str, onActionClickListener);
        this.mIsMapAlbumAvailable = false;
    }

    public void setMapAlbumAvailable(boolean z) {
        this.mIsMapAlbumAvailable = z;
    }

    @Override // com.miui.gallery.search.core.display.BaseSuggestionAdapter, com.miui.gallery.search.core.display.QuickAdapterBase
    public int getInnerItemViewType(int i) {
        if (this.mIsMapAlbumAvailable && i == 0) {
            i = -1;
        }
        return super.getInnerItemViewType(i);
    }
}
