package com.miui.gallery.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.recommend.RecommendFragmentAdapter;
import com.miui.gallery.assistant.recommend.RecommendListLoader;
import com.miui.gallery.cloudcontrol.CloudControlRequestHelper;
import com.miui.gallery.cloudcontrol.strategies.RecommendStrategy;
import com.miui.gallery.reddot.DisplayStatusManager;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class RecommendFragment extends BaseFragment {
    public RecommendFragmentAdapter mFragmentAdapter;
    public View mLoadingView;
    public View mNetworkErrorView;
    public final LoaderManager.LoaderCallbacks<RecommendStrategy> mRecommendStrategyLoaderCallbacks = new LoaderManager.LoaderCallbacks<RecommendStrategy>() { // from class: com.miui.gallery.ui.RecommendFragment.1
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<RecommendStrategy> onCreateLoader(int i, Bundle bundle) {
            return new RecommendListLoader(RecommendFragment.this.mActivity);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<RecommendStrategy> loader, RecommendStrategy recommendStrategy) {
            if (recommendStrategy != null) {
                RecommendFragment.this.mFragmentAdapter.changeRecommendItems((ArrayList) recommendStrategy.getRecommendItems().clone());
                RecommendFragment.this.mLoadingView.setVisibility(8);
                RecommendFragment.this.mRecyclerView.setVisibility(0);
                DisplayStatusManager.setRedDotClicked("recommendation");
                new RequeryTask().execute(new Void[0]);
                return;
            }
            RecommendFragment.this.mLoadingView.setVisibility(0);
            RecommendFragment.this.mLoadingView.findViewById(R.id.icon).setVisibility(0);
            ((TextView) RecommendFragment.this.mLoadingView.findViewById(R.id.text)).setText(R.string.assistant_page_server_error);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<RecommendStrategy> loader) {
            RecommendFragment.this.mFragmentAdapter.changeRecommendItems(null);
        }
    };
    public GalleryRecyclerView mRecyclerView;

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (!BaseNetworkUtils.isNetworkConnected()) {
            this.mNetworkErrorView.setVisibility(0);
            this.mLoadingView.setVisibility(8);
            return;
        }
        this.mLoadingView.setVisibility(0);
        this.mNetworkErrorView.setVisibility(8);
        getLoaderManager().initLoader(1, null, this.mRecommendStrategyLoaderCallbacks);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.recommend_fragment, viewGroup, false);
        this.mFragmentAdapter = new RecommendFragmentAdapter(this.mActivity);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.recyclerview);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(this.mActivity));
        this.mRecyclerView.setAdapter(this.mFragmentAdapter);
        this.mLoadingView = inflate.findViewById(R.id.loading_view);
        this.mNetworkErrorView = inflate.findViewById(R.id.network_error);
        return inflate;
    }

    @Override // com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        View view = this.mNetworkErrorView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        ((EmptyPage) view).resumeMaml();
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        View view = this.mNetworkErrorView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        ((EmptyPage) view).pauseMaml();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        View view = this.mNetworkErrorView;
        if (view != null && (view instanceof EmptyPage)) {
            ((EmptyPage) view).destroyMaml();
        }
        super.onDestroyView();
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return this.mActivity.getTitle().toString();
    }

    /* loaded from: classes2.dex */
    public static class RequeryTask extends AsyncTask<Void, Integer, Void> {
        public RequeryTask() {
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            new CloudControlRequestHelper(GalleryApp.sGetAndroidContext()).execRecommendRequest();
            return null;
        }
    }
}
