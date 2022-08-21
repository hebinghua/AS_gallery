package com.miui.gallery.ui;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.AlbumDetailAdapter;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.SpanSizeProvider;
import com.miui.gallery.widget.recyclerview.grouped.GroupedItemManager;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes2.dex */
public class AlbumNewPhotoDetailFragment extends PhotoListFragmentBase<AlbumDetailAdapter> {
    public AlbumDetailAdapter mAdapter;
    public GroupedItemManager mGroupedItemManager;
    public boolean mIsOtherShareAlbum;

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public int getLayoutSource() {
        return R.layout.album_new_photo_detail;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "album_new_photo_detail";
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        final GalleryGridLayoutManager galleryGridLayoutManager = new GalleryGridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        this.mRecyclerView.setLayoutManager(galleryGridLayoutManager);
        galleryGridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.ui.AlbumNewPhotoDetailFragment.1
            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanSize(int i) {
                if (GroupedItemManager.getPackedPositionChild(AlbumNewPhotoDetailFragment.this.mGroupedItemManager.getExpandablePosition(i)) == -1) {
                    return galleryGridLayoutManager.getSpanCount();
                }
                return 1;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanIndex(int i, int i2) {
                int packedPositionChild = GroupedItemManager.getPackedPositionChild(AlbumNewPhotoDetailFragment.this.mGroupedItemManager.getExpandablePosition(i));
                if (packedPositionChild == -1) {
                    return 0;
                }
                return packedPositionChild % i2;
            }
        }));
        GroupedItemManager groupedItemManager = new GroupedItemManager();
        this.mGroupedItemManager = groupedItemManager;
        this.mRecyclerView.setAdapter(groupedItemManager.createWrappedAdapter(mo1564getAdapter()));
        this.mRecyclerView.setOnItemClickListener(getGridViewOnItemClickListener());
        Intent intent = this.mActivity.getIntent();
        String stringExtra = intent.getStringExtra("album_name");
        this.mAlbumName = stringExtra;
        if (!TextUtils.isEmpty(stringExtra)) {
            this.mActivity.getAppCompatActionBar().setTitle(this.mAlbumName);
        }
        this.mAlbumId = intent.getLongExtra("album_id", -1L);
        this.mIsOtherShareAlbum = intent.getBooleanExtra("other_share_album", false);
        this.mRecyclerView.setLongClickable(false);
        return onInflateView;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public ItemClickSupport.OnItemClickListener getGridViewOnItemClickListener() {
        return new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.AlbumNewPhotoDetailFragment.2
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                int unwrapPosition = AlbumNewPhotoDetailFragment.this.unwrapPosition(i);
                if (unwrapPosition == -1) {
                    return false;
                }
                new PhotoPageIntent.Builder(AlbumNewPhotoDetailFragment.this, InternalPhotoPageActivity.class).setAdapterView(recyclerView).setUri(AlbumNewPhotoDetailFragment.this.getUri()).setInitPosition(unwrapPosition).setCount(AlbumNewPhotoDetailFragment.this.mAdapter.getItemCount()).setSelection("(" + AlbumNewPhotoDetailFragment.this.getSelection() + ") AND (localGroupId != -1000)").setSelectionArgs(AlbumNewPhotoDetailFragment.this.getSelectionArgs()).setOrderBy(AlbumNewPhotoDetailFragment.this.getCurrentSortOrder()).setImageLoadParams(new ImageLoadParams.Builder().setKey(AlbumNewPhotoDetailFragment.this.mAdapter.getItemKey(unwrapPosition)).setFilePath(AlbumNewPhotoDetailFragment.this.mAdapter.getBindImagePath(unwrapPosition)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setRegionRect(AlbumNewPhotoDetailFragment.this.mAdapter.getItemDecodeRectF(unwrapPosition)).setInitPosition(unwrapPosition).setMimeType(AlbumNewPhotoDetailFragment.this.mAdapter.getMimeType(unwrapPosition)).setSecretKey(AlbumNewPhotoDetailFragment.this.mAdapter.getItemSecretKey(unwrapPosition)).setFileLength(AlbumNewPhotoDetailFragment.this.mAdapter.getFileLength(unwrapPosition)).setImageWidth(AlbumNewPhotoDetailFragment.this.mAdapter.getImageWidth(unwrapPosition)).setImageHeight(AlbumNewPhotoDetailFragment.this.mAdapter.getImageHeight(unwrapPosition)).setCreateTime(AlbumNewPhotoDetailFragment.this.mAdapter.getCreateTime(unwrapPosition)).setLocation(AlbumNewPhotoDetailFragment.this.mAdapter.getLocation(unwrapPosition)).build()).setAlbumId(AlbumNewPhotoDetailFragment.this.mAlbumId).setAlbumName(AlbumNewPhotoDetailFragment.this.mAlbumName).setOperationMask(AlbumNewPhotoDetailFragment.this.getSupportOperationMask()).setUnfoldBurst(!AlbumNewPhotoDetailFragment.this.mo1564getAdapter().supportFoldBurstItems()).build().gotoPhotoPage();
                HashMap hashMap = new HashMap();
                hashMap.put("from", AlbumNewPhotoDetailFragment.this.getPageName());
                hashMap.put("position", Integer.valueOf(unwrapPosition));
                SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", hashMap);
                return true;
            }
        };
    }

    public int unwrapPosition(int i) {
        long expandablePosition = this.mGroupedItemManager.getExpandablePosition(i);
        int packedPositionGroup = GroupedItemManager.getPackedPositionGroup(expandablePosition);
        int packedPositionChild = GroupedItemManager.getPackedPositionChild(expandablePosition);
        if (packedPositionChild == -1) {
            return -1;
        }
        return mo1564getAdapter().packDataPosition(packedPositionGroup, packedPositionChild);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    /* renamed from: getAdapter */
    public AlbumDetailAdapter mo1564getAdapter() {
        if (this.mAdapter == null) {
            this.mAdapter = new AlbumDetailAdapter(this.mActivity, getLifecycle());
        }
        return this.mAdapter;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public Uri getUri() {
        if (this.mIsOtherShareAlbum) {
            return ContentUris.withAppendedId(GalleryContract.Media.URI_OTHER_ALBUM_MEDIA, this.mAlbumId);
        }
        return GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String getSelection() {
        return this.mIsOtherShareAlbum ? String.format(Locale.US, "(%s >= %s)", "serverTag", String.valueOf(GalleryPreferences.Baby.getMinServerTagOfNewPhoto(this.mAlbumId))) : String.format(Locale.US, "(%s >= ? AND %s = ?)", "serverTag", "localGroupId");
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String[] getSelectionArgs() {
        if (this.mIsOtherShareAlbum) {
            return null;
        }
        return new String[]{String.valueOf(GalleryPreferences.Baby.getMinServerTagOfNewPhoto(this.mAlbumId)), String.valueOf(this.mAlbumId)};
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        GalleryPreferences.Baby.saveMinServerTagOfNewPhoto(this.mAlbumId, 0L);
        super.onDestroy();
    }
}
