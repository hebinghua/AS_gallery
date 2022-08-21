package com.miui.gallery.share;

import android.content.Context;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class ShareUserAdapter extends ShareUserAdapterBase {
    public final boolean mAddSharer;

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

    public ShareUserAdapter(Context context, boolean z, String str) {
        super(context, str, R.layout.share_user_item);
        this.mAddSharer = z;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mShareUsers.size() + (this.mAddSharer ? 1 : 0);
    }
}
