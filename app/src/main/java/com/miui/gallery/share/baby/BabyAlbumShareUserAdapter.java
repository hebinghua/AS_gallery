package com.miui.gallery.share.baby;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.share.CloudUserCacheEntry;
import com.miui.gallery.share.ShareUserAdapterBase;
import com.miui.gallery.share.UserInfo;

/* loaded from: classes2.dex */
public class BabyAlbumShareUserAdapter extends ShareUserAdapterBase {
    @Override // com.miui.gallery.share.ShareUserAdapterBase
    public int getAbsentSharerIcon(CloudUserCacheEntry cloudUserCacheEntry) {
        return R.drawable.album_add_sharer;
    }

    @Override // com.miui.gallery.share.ShareUserAdapterBase
    public int getDefaultIcon(CloudUserCacheEntry cloudUserCacheEntry) {
        return R.drawable.album_sharer_default;
    }

    @Override // com.miui.gallery.share.ShareUserAdapterBase
    public int getIconEffect() {
        return R.drawable.ic_baby_album_sharer_effect;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 3;
    }

    public BabyAlbumShareUserAdapter(Context context, String str) {
        super(context, str, R.layout.baby_album_share_user_item);
    }

    @Override // com.miui.gallery.share.ShareUserAdapterBase
    public String getDisplayName(Resources resources, UserInfo userInfo, CloudUserCacheEntry cloudUserCacheEntry) {
        if (TextUtils.isEmpty(cloudUserCacheEntry.mRelationText) || (userInfo != null && TextUtils.equals(userInfo.getUserId(), GalleryCloudUtils.getAccountName()))) {
            return super.getDisplayName(resources, userInfo, cloudUserCacheEntry);
        }
        return cloudUserCacheEntry.mRelationText;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        CloudUserCacheEntry mo1387getItem = mo1387getItem(i);
        if (mo1387getItem != null) {
            if (TextUtils.equals(mo1387getItem.mRelation, "father")) {
                return 0;
            }
            return TextUtils.equals(mo1387getItem.mRelation, "mother") ? 1 : 2;
        }
        return 2;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mShareUsers.size();
    }
}
