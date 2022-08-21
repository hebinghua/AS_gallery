package com.miui.gallery.movie.picker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.picker.PickerFragment;
import com.miui.gallery.picker.albumdetail.IPickAlbumDetail;
import com.miui.gallery.picker.helper.CursorUtils;
import com.miui.gallery.picker.helper.PickableSimpleAdapterWrapper;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class MovieStoryPickFragment extends PickerFragment implements IPickAlbumDetail {
    public PickableSimpleAdapterWrapper mAdapter;
    public long mCardId;
    public EmptyPage mEmptyView;
    public Intent mIntent;
    public LoaderManager.LoaderCallbacks mLoaderCallbacks = new LoaderManager.LoaderCallbacks() { // from class: com.miui.gallery.movie.picker.MovieStoryPickFragment.2
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(MovieStoryPickFragment.this.mActivity);
            cursorLoader.setUri(MovieStoryPickFragment.this.getUri());
            cursorLoader.setProjection(MovieStoryPickFragment.this.getProjection());
            cursorLoader.setSelection(MovieStoryPickFragment.this.getSelection());
            cursorLoader.setSelectionArgs(MovieStoryPickFragment.this.getSelectionArgs());
            cursorLoader.setSortOrder(MovieStoryPickFragment.this.getOrder());
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            MovieStoryPickFragment.this.mAdapter.swapCursor((Cursor) obj);
        }
    };
    public GalleryRecyclerView mRecyclerView;
    public StoryMoviePickAdapter mStoryMoviePickAdapter;

    public final String getOrder() {
        return "alias_create_time DESC ";
    }

    @Override // com.miui.gallery.picker.PickerCompatFragment
    public String getPageName() {
        return "story_picker_home";
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public boolean supportFoldBurstItems() {
        return false;
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public int unwrapPosition(int i) {
        return i;
    }

    @Override // com.miui.gallery.picker.PickerCompatFragment, androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mIntent = ((MovieStoryPickActivity) this.mActivity).getResultIntent();
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.story_movie_pick_fragment, viewGroup, false);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid_view);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.movie.picker.MovieStoryPickFragment.1
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                Cursor mo1558getItem = MovieStoryPickFragment.this.mStoryMoviePickAdapter.mo1558getItem(i);
                new PhotoPageIntent.Builder(MovieStoryPickFragment.this.mActivity, InternalPhotoPageActivity.class).setAdapterView(MovieStoryPickFragment.this.mRecyclerView).setUri(MovieStoryPickFragment.this.getPreviewUri()).setSelection(MovieStoryPickFragment.this.getPreviewSelection(mo1558getItem)).setSelectionArgs(MovieStoryPickFragment.this.getPreviewSelectionArgs(mo1558getItem)).setOrderBy(MovieStoryPickFragment.this.getPreviewOrder()).setImageLoadParams(new ImageLoadParams.Builder().setKey(MovieStoryPickFragment.this.mStoryMoviePickAdapter.getItemKey(i)).setFilePath(MovieStoryPickFragment.this.mStoryMoviePickAdapter.getBindImagePath(i)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setInitPosition(0).setMimeType(MovieStoryPickFragment.this.mStoryMoviePickAdapter.getMimeType(i)).setFileLength(MovieStoryPickFragment.this.mStoryMoviePickAdapter.getFileLength(i)).fromFace(MovieStoryPickFragment.this.isPreviewFace()).setCreateTime(MovieStoryPickFragment.this.mStoryMoviePickAdapter.getCreateTime(i)).setLocation(MovieStoryPickFragment.this.mStoryMoviePickAdapter.getLocation(i)).build()).setIdForPicker(MovieStoryPickFragment.this.getKey(mo1558getItem)).setUnfoldBurst(!MovieStoryPickFragment.this.supportFoldBurstItems()).setPreview(true).build().gotoPhotoPage();
                return true;
            }
        });
        this.mStoryMoviePickAdapter = new StoryMoviePickAdapter(this.mActivity, this.mPicker);
        PickableSimpleAdapterWrapper pickableSimpleAdapterWrapper = new PickableSimpleAdapterWrapper(this, this.mPicker, this.mStoryMoviePickAdapter);
        this.mAdapter = pickableSimpleAdapterWrapper;
        this.mRecyclerView.setAdapter(pickableSimpleAdapterWrapper);
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait));
        GalleryRecyclerView galleryRecyclerView2 = this.mRecyclerView;
        galleryRecyclerView2.addItemDecoration(new GridItemSpacingDecoration(galleryRecyclerView2, false, getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing)));
        EmptyPage emptyPage = (EmptyPage) inflate.findViewById(16908292);
        this.mEmptyView = emptyPage;
        this.mRecyclerView.setEmptyView(emptyPage);
        this.mEmptyView.setVisibility(8);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (parseIntent()) {
            getLoaderManager().initLoader(17, null, this.mLoaderCallbacks);
        }
    }

    public final boolean parseIntent() {
        Intent intent = this.mIntent;
        if (intent == null) {
            DefaultLogger.d("StoryMoviePickFragment", "parseIntent is fail. ");
            this.mActivity.finish();
            return false;
        }
        this.mCardId = intent.getLongExtra("card_id", 0L);
        ArrayList<String> stringArrayListExtra = this.mIntent.getStringArrayListExtra("pick_sha1");
        if (BaseMiscUtil.isValid(stringArrayListExtra)) {
            Iterator<String> it = stringArrayListExtra.iterator();
            while (it.hasNext()) {
                this.mPicker.pick(it.next());
            }
        }
        DefaultLogger.d("StoryMoviePickFragment", "parseIntent is success. ");
        return true;
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public Uri getUri() {
        return GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(true)).build();
    }

    public final String[] getProjection() {
        return StoryMoviePickAdapter.PROJECTION;
    }

    public String[] getSelectionArgs() {
        if (getPicker().getMediaType() == Picker.MediaType.IMAGE) {
            return new String[]{String.valueOf(1)};
        }
        if (getPicker().getMediaType() != Picker.MediaType.VIDEO) {
            return null;
        }
        return new String[]{String.valueOf(2)};
    }

    public String getSelection() {
        Card cardByCardId = CardManager.getInstance().getCardByCardId(this.mCardId);
        StringBuilder sb = new StringBuilder();
        if (cardByCardId != null) {
            List<String> selectedMediaSha1s = cardByCardId.getSelectedMediaSha1s();
            sb.append(getFilterSelectionWithMimeType(getPicker().getFilterMimeTypes()));
            if (this.mPicker.getMediaType() != Picker.MediaType.ALL) {
                sb.append(" AND ");
                sb.append("serverType");
                sb.append(" =? ");
            }
            sb.append(" AND ");
            sb.append(String.format("%s IN ('%s')", "sha1", TextUtils.join("','", selectedMediaSha1s)));
        }
        return sb.toString();
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public String genKeyFromCursor(Cursor cursor) {
        return CursorUtils.getSha1(cursor);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        PickableSimpleAdapterWrapper pickableSimpleAdapterWrapper = this.mAdapter;
        if (pickableSimpleAdapterWrapper != null) {
            pickableSimpleAdapterWrapper.swapCursor(null);
        }
        super.onDestroy();
    }
}
