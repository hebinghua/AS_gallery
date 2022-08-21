package com.miui.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import com.miui.gallery.R;
import com.miui.gallery.activity.AlbumDetailGroupingActivity;
import com.miui.gallery.activity.facebaby.BabyAlbumDetailActivity;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.share.ShareAlbumCacheManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.RecentTimeLineGridHeaderItem;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.util.AlbumsCursorHelper;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.xiaomi.stat.b.h;
import java.util.HashMap;

/* loaded from: classes.dex */
public class RecentDiscoveryAdapter extends AlbumDetailAdapter {
    public static final int COLUMN_INDEX_ALBUM_ID;
    public static final int COLUMN_INDEX_DATE_MODIFIED;
    public static String[] PROJECTION;
    public boolean isDataValidShareAlbum;
    public AlbumsCursorHelper mAlbumsCursorHelper;

    public static /* synthetic */ void $r8$lambda$doMTxQfRKS55xwLaSbi4n_kbsYg(RecentDiscoveryAdapter recentDiscoveryAdapter, long j, String str, View view) {
        recentDiscoveryAdapter.lambda$doBindGroupViewHolder$0(j, str, view);
    }

    static {
        String[] strArr = AlbumDetailAdapter.PROJECTION_INTERNAL;
        int length = strArr.length;
        COLUMN_INDEX_ALBUM_ID = length;
        int i = length + 1;
        COLUMN_INDEX_DATE_MODIFIED = i;
        String[] strArr2 = new String[strArr.length + 2];
        PROJECTION = strArr2;
        System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        String[] strArr3 = PROJECTION;
        strArr3[length] = "localGroupId";
        strArr3[i] = "dateModified";
    }

    public RecentDiscoveryAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, Lifecycle lifecycle) {
        super(context, syncStateDisplay$DisplayScene, lifecycle);
        setAlbumType(AlbumType.ALL_PHOTOS);
        this.mAlbumsCursorHelper = new AlbumsCursorHelper(this.mContext);
    }

    public void setShareAlbums(Cursor cursor) {
        this.isDataValidShareAlbum = true;
        ShareAlbumCacheManager.getInstance().putSharedAlbums(cursor);
    }

    public void setAllAlbums(Cursor cursor) {
        this.mAlbumsCursorHelper.setAlbumsData(cursor);
        if (getItemCount() <= 0 || cursor == null || cursor.getCount() <= 0) {
            return;
        }
        notifyDataSetChanged();
    }

    public final void recordAlbumClick(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("name", str);
        SamplingStatHelper.recordCountEvent("recent_album", "recent_album_click_album_name", hashMap);
    }

    public final Intent newAlbumFromClickedIntent(long j) {
        Intent intent;
        long attributes = this.mAlbumsCursorHelper.getAttributes(j);
        String serverId = this.mAlbumsCursorHelper.getServerId(j);
        String albumName = this.mAlbumsCursorHelper.getAlbumName(j);
        String albumLocalPath = this.mAlbumsCursorHelper.getAlbumLocalPath(Long.valueOf(j));
        boolean z = !TextUtils.isEmpty(albumLocalPath) && albumLocalPath.startsWith(h.g);
        boolean isBabyAlbum = this.mAlbumsCursorHelper.isBabyAlbum(j);
        boolean equals = String.valueOf(-2147483645L).equals(serverId);
        if (isBabyAlbum) {
            intent = new Intent(this.mContext, BabyAlbumDetailActivity.class);
            intent.putExtra("people_id", this.mAlbumsCursorHelper.getBabyAlbumPeopleId(j));
            intent.putExtra("baby_info", this.mAlbumsCursorHelper.getBabyInfo(j));
            intent.putExtra("thumbnail_info_of_baby", this.mAlbumsCursorHelper.getThumbnailInfoOfBaby(j));
            intent.putExtra("baby_sharer_info", this.mAlbumsCursorHelper.getBabySharerInfo(j));
        } else if (equals) {
            intent = new Intent(this.mContext, AlbumDetailGroupingActivity.class);
            intent.putExtra("group_first_album_id", AlbumCacheManager.getInstance().getScreenshotsAlbumId());
            intent.putExtra("group_second_album_id", AlbumCacheManager.getInstance().getScreenRecordersAlbumId());
            intent.putExtra("group_first_album_name", this.mContext.getString(R.string.album_screenshot_name));
            intent.putExtra("group_second_album_name", this.mContext.getString(R.string.album_screen_record));
        } else {
            intent = new Intent("com.miui.gallery.action.VIEW_ALBUM_DETAIL");
        }
        boolean equals2 = String.valueOf(-2147483647L).equals(serverId);
        boolean isOtherShareAlbum = this.mAlbumsCursorHelper.isOtherShareAlbum(j);
        boolean isOwnerShareAlbum = ShareAlbumHelper.isOwnerShareAlbum(j);
        boolean isLocalAlbum = this.mAlbumsCursorHelper.isLocalAlbum(j);
        intent.putExtra("other_share_album", isOtherShareAlbum);
        intent.putExtra("owner_share_album", isOwnerShareAlbum);
        intent.putExtra("is_local_album", isLocalAlbum);
        intent.putExtra("screenshot_recorder_album", equals);
        intent.putExtra("video_album", equals2);
        intent.putExtra("album_id", j);
        intent.putExtra("album_name", albumName);
        intent.putExtra("album_server_id", serverId);
        intent.putExtra("attributes", attributes);
        intent.putExtra("album_unwriteable", z);
        intent.putExtra("album_local_path", albumLocalPath);
        return intent;
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateGroupViewHolder */
    public BaseViewHolder mo1338onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        RecentTimeLineGridHeaderItem recentTimeLineGridHeaderItem = new RecentTimeLineGridHeaderItem(viewGroup.getContext(), null);
        recentTimeLineGridHeaderItem.setLayoutParams(new ViewGroup.LayoutParams(-1, 0));
        return new BaseViewHolder(recentTimeLineGridHeaderItem);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public void doBindGroupViewHolder(BaseViewHolder baseViewHolder, int i, PictureViewMode pictureViewMode) {
        int childCount = this.mClusterAdapter.getChildCount(i, this.mSpanCount);
        int groupStartPosition = this.mClusterAdapter.getGroupStartPosition(i);
        String groupLabel = this.mClusterAdapter.getGroupLabel(i);
        final long j = mo1558getItem(groupStartPosition).getLong(COLUMN_INDEX_ALBUM_ID);
        if (j == AlbumCacheManager.getInstance().getScreenshotsAlbumId() || j == AlbumCacheManager.getInstance().getScreenRecordersAlbumId()) {
            j = 2147483645;
        }
        final String albumName = this.mAlbumsCursorHelper.isAlbumDataValid(j) ? this.mAlbumsCursorHelper.getAlbumName(j) : null;
        ((RecentTimeLineGridHeaderItem) baseViewHolder.itemView).bindData(groupLabel, childCount, albumName, this.mShowTimeLine);
        ((RecentTimeLineGridHeaderItem) baseViewHolder.itemView).setAlbumFromClickedListener(new View.OnClickListener() { // from class: com.miui.gallery.adapter.RecentDiscoveryAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RecentDiscoveryAdapter.$r8$lambda$doMTxQfRKS55xwLaSbi4n_kbsYg(RecentDiscoveryAdapter.this, j, albumName, view);
            }
        });
    }

    public /* synthetic */ void lambda$doBindGroupViewHolder$0(long j, String str, View view) {
        if (!this.mAlbumsCursorHelper.isAlbumDataValid(j) || !this.isDataValidShareAlbum) {
            return;
        }
        this.mContext.startActivity(newAlbumFromClickedIntent(j));
        if (TextUtils.isEmpty(str)) {
            return;
        }
        recordAlbumClick(str);
    }
}
