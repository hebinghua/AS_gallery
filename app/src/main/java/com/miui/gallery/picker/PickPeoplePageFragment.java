package com.miui.gallery.picker;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
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
import com.miui.gallery.adapter.PeoplePageAdapter;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.PeoplePageGridItem;
import com.miui.gallery.util.face.PeopleCursorHelper;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.recyclerview.EditableListSpanSizeProvider;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class PickPeoplePageFragment extends PickerFragment {
    public EditableListViewWrapper mEditableWrapper;
    public boolean mIsOnlyPickPeople;
    public PeoplePageAdapter mPeoplePageAdapter;
    public PeoplePagePhotoLoaderCallback mPeoplePagePhotoLoaderCallback;
    public GalleryRecyclerView mRecyclerView;

    public static /* synthetic */ boolean $r8$lambda$rj2ZHqYvw854A0zmasfnbu9Q2RM(PickPeoplePageFragment pickPeoplePageFragment, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        return pickPeoplePageFragment.lambda$onInflateView$0(recyclerView, view, i, j, f, f2);
    }

    @Override // com.miui.gallery.picker.PickerCompatFragment
    public String getPageName() {
        return "picker_people";
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public Uri getUri() {
        return null;
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.people_page, viewGroup, false);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mRecyclerView = galleryRecyclerView;
        this.mRecyclerView.addItemDecoration(new GridItemSpacingDecoration(galleryRecyclerView, false, this.mActivity.getResources().getDimensionPixelSize(R.dimen.people_face_horizontal_spacing), this.mActivity.getResources().getDimensionPixelSize(R.dimen.people_face_vertical_spacing)));
        this.mEditableWrapper = new EditableListViewWrapper(this.mRecyclerView);
        this.mPeoplePageAdapter = new PeoplePageAdapter(this.mActivity);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new EditableListSpanSizeProvider(this.mEditableWrapper, gridLayoutManager)));
        this.mEditableWrapper.setLayoutManager(gridLayoutManager);
        this.mEditableWrapper.setAdapter(this.mPeoplePageAdapter);
        this.mEditableWrapper.setHandleTouchAnimItemType(PeoplePageGridItem.class.getSimpleName());
        this.mEditableWrapper.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.picker.PickPeoplePageFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public final boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                return PickPeoplePageFragment.$r8$lambda$rj2ZHqYvw854A0zmasfnbu9Q2RM(PickPeoplePageFragment.this, recyclerView, view, i, j, f, f2);
            }
        });
        updateConfiguration(getResources().getConfiguration());
        return inflate;
    }

    public /* synthetic */ boolean lambda$onInflateView$0(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        Intent intent = new Intent();
        String peopleIdOfItem = this.mPeoplePageAdapter.getPeopleIdOfItem(i);
        String peopleLocalIdOfItem = this.mPeoplePageAdapter.getPeopleLocalIdOfItem(i);
        intent.putExtra("server_id_of_album", peopleIdOfItem);
        intent.putExtra("local_id_of_album", peopleLocalIdOfItem);
        intent.putExtra("album_name", ((PeoplePageGridItem) view).getName());
        if (this.mIsOnlyPickPeople) {
            this.mActivity.setResult(-1, intent);
            this.mActivity.finish();
            return false;
        }
        intent.putExtra("relationType", this.mPeoplePageAdapter.getRelationTypeOfItem(i));
        intent.setClass(this.mActivity, PickFaceAlbumActivity.class);
        Picker picker = ((PickerActivity) this.mActivity).getPicker();
        intent.putExtra("pick-upper-bound", picker.capacity());
        intent.putExtra("pick-lower-bound", this.mPicker.baseline());
        intent.putExtra("picker_media_type", picker.getMediaType().ordinal());
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(picker));
        intent.putExtra("picker_result_type", picker.getResultType().ordinal());
        intent.putExtra("ai_album", true);
        this.mActivity.startActivityForResult(intent, 1);
        return true;
    }

    public void setIsPickPeople(boolean z) {
        this.mIsOnlyPickPeople = z;
    }

    /* loaded from: classes2.dex */
    public class PeoplePagePhotoLoaderCallback implements LoaderManager.LoaderCallbacks {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public PeoplePagePhotoLoaderCallback() {
            PickPeoplePageFragment.this = r1;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(PickPeoplePageFragment.this.mActivity);
            cursorLoader.setUri(GalleryContract.PeopleFace.PERSONS_URI);
            cursorLoader.setProjection(PeopleCursorHelper.PROJECTION);
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            Cursor cursor = (Cursor) obj;
            PickPeoplePageFragment.this.mPeoplePageAdapter.setUserDefineRelationMap(PickPeoplePageFragment.this.computeUserDefineRelation(cursor));
            PickPeoplePageFragment.this.mPeoplePageAdapter.swapCursor(cursor);
        }
    }

    public final HashMap<String, Integer> computeUserDefineRelation(Cursor cursor) {
        String relationText;
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        HashMap<String, Integer> hashMap = new HashMap<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (PeopleContactInfo.isUserDefineRelation(PeopleCursorHelper.getRelationType(cursor)) && (relationText = PeopleCursorHelper.getRelationText(cursor)) != null && !hashMap.containsKey(relationText)) {
                hashMap.put(relationText, Integer.valueOf(hashMap.size()));
            }
            cursor.moveToNext();
        }
        return hashMap;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mPeoplePagePhotoLoaderCallback = new PeoplePagePhotoLoaderCallback();
        getLoaderManager().initLoader(1, null, this.mPeoplePagePhotoLoaderCallback);
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
    }

    public final void updateConfiguration(Configuration configuration) {
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        if (configuration.orientation == 2) {
            ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(getResources().getInteger(R.integer.people_face_grid_view_columns_land));
        } else {
            ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(getResources().getInteger(R.integer.people_face_grid_view_columns));
        }
        this.mRecyclerView.scrollToPosition(findFirstVisibleItemPosition);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        PeoplePageAdapter peoplePageAdapter = this.mPeoplePageAdapter;
        if (peoplePageAdapter != null) {
            peoplePageAdapter.swapCursor(null);
        }
        super.onDestroy();
    }
}
