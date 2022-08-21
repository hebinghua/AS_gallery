package com.miui.gallery.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import com.miui.gallery.R;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.PagerGridLayout;
import java.util.Collections;
import java.util.List;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class RecommendFacePageFragment extends BaseMediaFragment implements PagerGridLayout.OnPageChangedListener {
    public Button confirmButton;
    public EmptyPage mEmptyView;
    public ViewStub mEmptyViewStub;
    public PagerGridLayout mFaceGroup;
    public long mLocalIdOfAlbum;
    public View mNormalView;
    public String mPeopleName;
    public RecommendFaceGroupAdapter mRecommendFaceAdapter;
    public String mRecommendFaceIds;
    public String mServerIdOfAlbum;
    public Handler mHandler = new Handler();
    public boolean mNoMoreRecommendations = false;

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "face_recommend";
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.recommend_face_page, (ViewGroup) null, false);
        PagerGridLayout pagerGridLayout = (PagerGridLayout) inflate.findViewById(R.id.face_group);
        this.mFaceGroup = pagerGridLayout;
        pagerGridLayout.setOnPageChangedListener(this);
        this.confirmButton = (Button) inflate.findViewById(R.id.confirm_recommend);
        this.mEmptyViewStub = (ViewStub) inflate.findViewById(R.id.recommend_face_page_empty_view);
        this.mNormalView = inflate.findViewById(R.id.normal_view);
        FolmeUtil.setDefaultTouchAnim(this.confirmButton, null, false, false, true);
        this.confirmButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.RecommendFacePageFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RecommendFacePageFragment.this.changeToNextPage();
            }
        });
        return inflate;
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        PagerGridLayout pagerGridLayout = this.mFaceGroup;
        if (pagerGridLayout != null) {
            pagerGridLayout.freshCurrentPage();
        }
        EmptyPage emptyPage = this.mEmptyView;
        if (emptyPage != null) {
            emptyPage.resumeMaml();
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        EmptyPage emptyPage = this.mEmptyView;
        if (emptyPage != null) {
            emptyPage.pauseMaml();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        EmptyPage emptyPage = this.mEmptyView;
        if (emptyPage != null) {
            emptyPage.destroyMaml();
        }
        super.onDestroyView();
    }

    @Override // com.miui.gallery.widget.PagerGridLayout.OnPageChangedListener
    public void onPageChanged(int i, int i2, boolean z) {
        if (z) {
            this.mNormalView.setVisibility(8);
            this.mNoMoreRecommendations = true;
            EmptyPage emptyPage = (EmptyPage) this.mEmptyViewStub.inflate();
            this.mEmptyView = emptyPage;
            emptyPage.setTopDividerLineVisible(true);
            this.mEmptyView.setTitle(R.string.empty_recommend_face_page_view);
            this.mEmptyView.setIcon(R.drawable.ic_pic_empty_person);
            this.mEmptyView.setActionButtonVisible(true);
            this.mEmptyView.setActionButtonText(R.string.empty_recommend_back);
            this.mEmptyView.setVisibility(0);
            this.mEmptyView.setOnActionButtonClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.RecommendFacePageFragment.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RecommendFacePageFragment.this.mActivity.finish();
                }
            });
        }
    }

    public final void setTitle() {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity != null) {
            appCompatActivity.getAppCompatActionBar().setTitle(getString(R.string.more_face));
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mServerIdOfAlbum = this.mActivity.getIntent().getStringExtra("server_id_of_album");
        this.mLocalIdOfAlbum = this.mActivity.getIntent().getLongExtra("local_id_of_album", -1L);
        this.mPeopleName = this.mActivity.getIntent().getStringExtra("album_name");
        this.mRecommendFaceIds = this.mActivity.getIntent().getStringExtra("server_ids_of_faces");
        setTitle();
        this.mRecommendFaceAdapter = new RecommendFaceGroupAdapter(this, this.mServerIdOfAlbum, Long.valueOf(this.mLocalIdOfAlbum)) { // from class: com.miui.gallery.ui.RecommendFacePageFragment.3
            @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
            public int getColumnsCount() {
                return 3;
            }

            @Override // com.miui.gallery.ui.RecommendFaceGroupAdapter
            public int getLayoutId() {
                return R.layout.recommend_face_cover_item_large;
            }

            @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
            public int getRowsCount() {
                return 4;
            }
        };
        getLoaderManager().initLoader(2, null, new FaceRecommendPhotoLoaderCallback());
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        return Collections.emptyList();
    }

    /* loaded from: classes2.dex */
    public class FaceRecommendPhotoLoaderCallback implements LoaderManager.LoaderCallbacks {
        public final String getOrderBy() {
            return "dateTaken DESC ";
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public FaceRecommendPhotoLoaderCallback() {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(RecommendFacePageFragment.this.getActivity());
            cursorLoader.setUri(getUri());
            cursorLoader.setProjection(RecommendFaceGroupAdapter.PROJECTION);
            cursorLoader.setSelectionArgs(new String[]{RecommendFacePageFragment.this.mRecommendFaceIds});
            cursorLoader.setSortOrder(getOrderBy());
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            RecommendFacePageFragment.this.mRecommendFaceAdapter.swapCursor((Cursor) obj);
            RecommendFacePageFragment.this.mFaceGroup.setAdapter(RecommendFacePageFragment.this.mRecommendFaceAdapter);
        }

        public final Uri getUri() {
            return GalleryContract.PeopleFace.RECOMMEND_FACES_OF_ONE_PERSON_URI;
        }
    }

    public void changeToNextPage() {
        PagerGridLayout pagerGridLayout = this.mFaceGroup;
        if (pagerGridLayout != null) {
            pagerGridLayout.changeToNextPage();
        }
    }

    public void onActivityFinish() {
        if (this.mNoMoreRecommendations) {
            Intent intent = new Intent();
            intent.putExtra("all_faces_confirmed", true);
            this.mActivity.setResult(-1, intent);
        }
    }
}
