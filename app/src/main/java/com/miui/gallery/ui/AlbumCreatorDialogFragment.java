package com.miui.gallery.ui;

import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.util.BaseBuildUtil;

/* loaded from: classes2.dex */
public class AlbumCreatorDialogFragment extends BaseAlbumOperationDialogFragment {
    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public int getDialogTitle() {
        return R.string.create_new_album;
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public int getOperationFailedString() {
        return R.string.create_new_album_failed;
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public int getOperationSucceededString() {
        return 0;
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public String getOperationTag() {
        return "create_album";
    }

    public static AlbumCreatorDialogFragment newInstance() {
        return newInstance(null);
    }

    public static AlbumCreatorDialogFragment newInstance(Bundle bundle) {
        AlbumCreatorDialogFragment albumCreatorDialogFragment = new AlbumCreatorDialogFragment();
        if (bundle != null) {
            Bundle bundle2 = new Bundle();
            bundle2.putAll(bundle);
            albumCreatorDialogFragment.setArguments(bundle2);
        }
        return albumCreatorDialogFragment;
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment, com.miui.gallery.ui.GalleryInputDialogFragment, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public void parseArguments() {
        this.mDefaultName = "";
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public Bundle execAlbumOperation(Context context, String str) {
        return CloudUtils.create(context, str, getArguments());
    }

    @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment
    public boolean isHideSoftInputForLand() {
        return !BaseBuildUtil.isLargeScreenDevice();
    }
}
