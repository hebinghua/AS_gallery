package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.provider.cache.IMedia;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.StatusHandleHelper;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.context.SearchContext;
import com.miui.gallery.search.core.display.BaseSuggestionAdapter;
import com.miui.gallery.search.core.display.LocationListAdapter;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.resultpage.SearchResultFragment;
import com.miui.gallery.ui.MapViewModel;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class LocationListFragment extends SearchResultFragment {
    public BaseSuggestion mEmptyMapSuggestion;
    public BaseSuggestion mMapSuggestion;
    public MapViewModel mMapViewModel;
    public QueryInfo mQueryInfo;
    public SuggestionCursor mSuggestionCursor;

    public static /* synthetic */ void $r8$lambda$OeAISDF0OgM3Yfc77dLXEUqlIqw(LocationListFragment locationListFragment, List list) {
        locationListFragment.lambda$onActivityCreated$0(list);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment, com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public int getLayoutResource() {
        return R.layout.search_location_list_fragment;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase, com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (MapInitializerImpl.checkMapAvailable()) {
            this.mEmptyMapSuggestion = SearchConfig.get().genMapSuggestion(requireActivity(), null);
            MapViewModel mapViewModel = (MapViewModel) new ViewModelProvider(requireActivity()).get(MapViewModel.class);
            this.mMapViewModel = mapViewModel;
            mapViewModel.queryMapAlbumCovers();
            this.mMapViewModel.getMapCoverItems().observe(this, new Observer() { // from class: com.miui.gallery.search.resultpage.LocationListFragment$$ExternalSyntheticLambda0
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    LocationListFragment.$r8$lambda$OeAISDF0OgM3Yfc77dLXEUqlIqw(LocationListFragment.this, (List) obj);
                }
            });
        }
    }

    public /* synthetic */ void lambda$onActivityCreated$0(List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            IRecord iRecord = (IRecord) it.next();
            if (arrayList.size() == 4) {
                break;
            } else if (iRecord instanceof IMedia) {
                IMedia iMedia = (IMedia) iRecord;
                if (iMedia.hasValidLocationInfo() && iMedia.getSmallSizeThumb() != null) {
                    arrayList.add(iMedia.getSmallSizeThumb());
                }
            }
        }
        if (arrayList.size() != 0) {
            this.mMapSuggestion = SearchConfig.get().genMapSuggestion(requireActivity(), arrayList);
        }
        if (this.mMapSuggestion == null || !isMapAlbumAvailable(this.mQueryInfo, this.mSuggestionCursor)) {
            return;
        }
        ((ListSuggestionCursor) this.mSuggestionCursor).replace(0, this.mMapSuggestion);
        this.mResultAdapter.changeSuggestionsByIndex(this.mQueryInfo, this.mSuggestionCursor, 0);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment
    public void configAdapter() {
        LocationListAdapter locationListAdapter = new LocationListAdapter(this.mActivity, SearchContext.getInstance().getSuggestionViewFactory(), this.mFrom, getOnActionClickListener(getContext()));
        this.mResultAdapter = locationListAdapter;
        locationListAdapter.setOnLoadMoreListener(this);
        ((LocationListAdapter) this.mResultAdapter).setMapAlbumAvailable(MapInitializerImpl.checkMapAvailable());
        this.mResultView.setAdapter(this.mResultAdapter);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment, com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public StatusHandleHelper.AbstractErrorViewAdapter getErrorViewAdapter() {
        if (this.mErrorViewAdapter == null) {
            this.mErrorViewAdapter = new LocationListErrorViewAdapter(this.mActivity);
        }
        return this.mErrorViewAdapter;
    }

    /* loaded from: classes2.dex */
    public class LocationListErrorViewAdapter extends SearchResultFragment.ErrorViewAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public LocationListErrorViewAdapter(Context context) {
            super(context);
            LocationListFragment.this = r1;
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter
        public int getIconResForStatus(int i, StatusHandleHelper.InfoViewPosition infoViewPosition) {
            if (infoViewPosition == StatusHandleHelper.InfoViewPosition.FULL_SCREEN) {
                return R.drawable.ic_pic_empty_location;
            }
            if (infoViewPosition != StatusHandleHelper.InfoViewPosition.FOOTER) {
                return 0;
            }
            return R.drawable.search_connection_error_icon;
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter
        public String getInfoTitleForStatus(int i, StatusHandleHelper.InfoViewPosition infoViewPosition) {
            boolean z = infoViewPosition == StatusHandleHelper.InfoViewPosition.FULL_SCREEN;
            int i2 = R.string.places_album_empty_title;
            if (i != 1) {
                if (i == 10) {
                    i2 = R.string.search_syncing;
                } else if (i == 13) {
                    i2 = R.string.ai_album_requesting_title;
                } else if (i != 3) {
                    if (i != 4) {
                        if (!z) {
                            i2 = R.string.search_error_and_retry;
                        }
                    } else if (!z) {
                        i2 = R.string.search_backup_title;
                    }
                } else if (!z) {
                    i2 = R.string.search_login_title;
                }
            } else if (!z) {
                i2 = R.string.search_connection_error_and_set;
            }
            return this.mContext.getString(i2);
        }
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment, com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void changeSuggestions(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        if (this.mResultAdapter == null || queryInfo == null || suggestionCursor == null) {
            return;
        }
        this.mSuggestionCursor = suggestionCursor;
        this.mQueryInfo = queryInfo;
        if (isMapAlbumAvailable(queryInfo, suggestionCursor)) {
            ListSuggestionCursor listSuggestionCursor = (ListSuggestionCursor) suggestionCursor;
            BaseSuggestion baseSuggestion = this.mMapSuggestion;
            if (baseSuggestion == null) {
                baseSuggestion = this.mEmptyMapSuggestion;
            }
            listSuggestionCursor.add(0, baseSuggestion);
        }
        this.mResultAdapter.changeSuggestions(queryInfo, suggestionCursor);
    }

    public boolean isMapAlbumAvailable(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        return MapInitializerImpl.checkMapAvailable() && queryInfo != null && "locationList".equals(queryInfo.getParam(nexExportFormat.TAG_FORMAT_TYPE));
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragment
    public void trackLoadComplete() {
        BaseSuggestionAdapter<SuggestionCursor> baseSuggestionAdapter = this.mResultAdapter;
        TimeMonitor.trackTimeMonitor("403.48.0.1.13784", baseSuggestionAdapter == null ? 0L : baseSuggestionAdapter.getItemCount());
    }
}
