package com.miui.gallery.ui;

import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.ui.BaseAlbumOperationDialogFragment;

/* loaded from: classes2.dex */
public class AlbumRenameDialogFragment extends BaseAlbumOperationDialogFragment {
    public long mAlbumId;
    public String mSource;

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public int getDialogTitle() {
        return R.string.rename_album;
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public int getOperationFailedString() {
        return R.string.rename_album_failed;
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public int getOperationSucceededString() {
        return R.string.rename_album_succeeded;
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public String getOperationTag() {
        return "rename_album";
    }

    public static AlbumRenameDialogFragment newInstance(long j, String str, String str2, BaseAlbumOperationDialogFragment.OnAlbumOperationListener onAlbumOperationListener) {
        AlbumRenameDialogFragment albumRenameDialogFragment = new AlbumRenameDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("key_album_id", j);
        bundle.putString("key_album_default_name", str);
        bundle.putString("key_source", str2);
        albumRenameDialogFragment.setArguments(bundle);
        albumRenameDialogFragment.setOnAlbumOperationListener(onAlbumOperationListener);
        return albumRenameDialogFragment;
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public void parseArguments() {
        Bundle arguments = getArguments();
        this.mAlbumId = arguments.getLong("key_album_id", -1L);
        this.mDefaultName = arguments.getString("key_album_default_name");
        this.mSource = arguments.getString("key_source");
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public Bundle execAlbumOperation(Context context, String str) {
        if ("AlbumDetailFragment".equals(this.mSource)) {
            TrackController.trackClick("403.15.2.1.11191", "403.15.1.1.11176");
        } else if ("BaseAlbumPageFragment".equals(this.mSource)) {
            TrackController.trackClick("403.7.4.1.10342", "403.7.4.1.10542");
        }
        return CloudUtils.renameAlbum(context, this.mAlbumId, str);
    }
}
