package com.miui.gallery.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PickPeopleCoverAdapter;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.PickPeopleCoverFragment;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.SpanSizeProvider;
import com.miui.gallery.widget.recyclerview.grouped.GroupedItemManager;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class PickPeopleCoverFragment extends BaseMediaFragment {
    public PickPeopleCoverAdapter mAdapter;
    public GroupedItemManager mGroupedItemManager;
    public long mLocalIdOfAlbum;
    public PhotoLoaderCallback mPhotoLoaderCallback;
    public GalleryRecyclerView mRecyclerView;
    public String mServerIdOfAlbum;
    public RecyclerView.Adapter mTimeLineAdapterWrapper;

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "pick_people_cover";
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle extras = this.mActivity.getIntent().getExtras();
        this.mServerIdOfAlbum = extras.getString("server_id_of_album");
        this.mLocalIdOfAlbum = extras.getLong("local_id_of_album");
        this.mPhotoLoaderCallback = new PhotoLoaderCallback();
        getLoaderManager().initLoader(1, null, this.mPhotoLoaderCallback);
        this.mActivity.getAppCompatActionBar().setTitle(R.string.pick_people_cover_page_title);
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAdapter = new PickPeopleCoverAdapter(this.mActivity, getLifecycle());
        GroupedItemManager groupedItemManager = new GroupedItemManager();
        this.mGroupedItemManager = groupedItemManager;
        this.mTimeLineAdapterWrapper = groupedItemManager.createWrappedAdapter(this.mAdapter);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.pick_people_cover_page, viewGroup, false);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setItemAnimator(null);
        this.mRecyclerView.addItemDecoration(new GridItemSpacingDecoration(this.mRecyclerView, false, this.mActivity.getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), this.mActivity.getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing)));
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.ui.PickPeopleCoverFragment.1
            {
                PickPeopleCoverFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanSize(int i) {
                if (GroupedItemManager.getPackedPositionChild(PickPeopleCoverFragment.this.mGroupedItemManager.getExpandablePosition(i)) == -1) {
                    return gridLayoutManager.getSpanCount();
                }
                return 1;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanIndex(int i, int i2) {
                int packedPositionChild = GroupedItemManager.getPackedPositionChild(PickPeopleCoverFragment.this.mGroupedItemManager.getExpandablePosition(i));
                if (packedPositionChild == -1) {
                    return 0;
                }
                return packedPositionChild % i2;
            }
        }));
        this.mRecyclerView.setLayoutManager(gridLayoutManager);
        this.mRecyclerView.setAdapter(this.mTimeLineAdapterWrapper);
        this.mRecyclerView.setOnItemClickListener(new AnonymousClass2());
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
        return inflate;
    }

    /* renamed from: com.miui.gallery.ui.PickPeopleCoverFragment$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements ItemClickSupport.OnItemClickListener {
        /* renamed from: $r8$lambda$FbHM-PeO9fFOYeQilLHrHk4XnHQ */
        public static /* synthetic */ Boolean m1560$r8$lambda$FbHMPeO9fFOYeQilLHrHk4XnHQ(AnonymousClass2 anonymousClass2, int i, Void[] voidArr) {
            return anonymousClass2.lambda$onItemClick$0(i, voidArr);
        }

        public AnonymousClass2() {
            PickPeopleCoverFragment.this = r1;
        }

        @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
        public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
            final int unwrapPosition = PickPeopleCoverFragment.this.unwrapPosition(i);
            if (unwrapPosition == -1 || unwrapPosition >= PickPeopleCoverFragment.this.mAdapter.getItemCount()) {
                return false;
            }
            final String clearThumbFilePath = PickPeopleCoverFragment.this.mAdapter.getClearThumbFilePath(unwrapPosition);
            final RectF faceRegionRectF = PickPeopleCoverFragment.this.mAdapter.getFaceRegionRectF(unwrapPosition);
            ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.ui.PickPeopleCoverFragment$2$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                public final Object doProcess(Object[] objArr) {
                    return PickPeopleCoverFragment.AnonymousClass2.m1560$r8$lambda$FbHMPeO9fFOYeQilLHrHk4XnHQ(PickPeopleCoverFragment.AnonymousClass2.this, unwrapPosition, (Void[]) objArr);
                }
            });
            processTask.setCompleteListener(new ProcessTask.OnCompleteListener<Boolean>() { // from class: com.miui.gallery.ui.PickPeopleCoverFragment.2.1
                {
                    AnonymousClass2.this = this;
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
                public void onCompleteProcess(Boolean bool) {
                    Intent intent = new Intent();
                    if (bool.booleanValue()) {
                        intent.putExtra("pick_people_cover_success", true);
                        intent.putExtra("face_album_cover", clearThumbFilePath);
                        intent.putExtra("face_position_rect", faceRegionRectF);
                        SamplingStatHelper.recordCountEvent("face", "face_album_pick_cover_success");
                    } else {
                        intent.putExtra("pick_people_cover_success", false);
                        SamplingStatHelper.recordCountEvent("face", "face_album_pick_cover_fail");
                    }
                    PickPeopleCoverFragment.this.mActivity.setResult(-1, intent);
                    PickPeopleCoverFragment.this.mActivity.finish();
                }
            });
            PickPeopleCoverFragment pickPeopleCoverFragment = PickPeopleCoverFragment.this;
            processTask.showProgress(pickPeopleCoverFragment.mActivity, pickPeopleCoverFragment.getString(R.string.pick_people_cover_processing));
            processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            return true;
        }

        public /* synthetic */ Boolean lambda$onItemClick$0(int i, Void[] voidArr) {
            return Boolean.valueOf(NormalPeopleFaceMediaSet.setPeopleCover(PickPeopleCoverFragment.this.mLocalIdOfAlbum, PickPeopleCoverFragment.this.mAdapter.getFaceServerId(i)));
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        PickPeopleCoverAdapter pickPeopleCoverAdapter = this.mAdapter;
        if (pickPeopleCoverAdapter != null) {
            pickPeopleCoverAdapter.notifyDataSetChanged();
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        return Collections.singletonList(LoaderManager.getInstance(this).getLoader(1));
    }

    /* loaded from: classes2.dex */
    public class PhotoLoaderCallback implements LoaderManager.LoaderCallbacks {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public PhotoLoaderCallback() {
            PickPeopleCoverFragment.this = r1;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(PickPeopleCoverFragment.this.mActivity);
            cursorLoader.setUri(GalleryContract.PeopleFace.ONE_PERSON_URI.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).build());
            cursorLoader.setProjection(PickPeopleCoverAdapter.PROJECTION);
            cursorLoader.setSelectionArgs(new String[]{PickPeopleCoverFragment.this.mServerIdOfAlbum, String.valueOf(PickPeopleCoverFragment.this.mLocalIdOfAlbum)});
            cursorLoader.setSortOrder("dateTaken DESC ");
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            PickPeopleCoverFragment.this.mAdapter.swapCursor((Cursor) obj);
        }
    }

    public int unwrapPosition(int i) {
        long expandablePosition = this.mGroupedItemManager.getExpandablePosition(i);
        int packedPositionGroup = GroupedItemManager.getPackedPositionGroup(expandablePosition);
        int packedPositionChild = GroupedItemManager.getPackedPositionChild(expandablePosition);
        if (packedPositionChild == -1) {
            return -1;
        }
        return this.mAdapter.packDataPosition(packedPositionGroup, packedPositionChild);
    }
}
