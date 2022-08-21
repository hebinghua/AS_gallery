package com.miui.gallery.ui;

import android.util.Pair;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils;
import com.miui.gallery.widget.menu.ImmersionMenu;
import com.miui.gallery.widget.menu.ImmersionMenuItem;
import com.miui.gallery.widget.menu.ImmersionMenuListener;
import com.miui.gallery.widget.menu.PhoneImmersionMenu;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumDetailReplaceAlbumCoverImmersionMenu implements ImmersionMenuListener {
    public ReplaceAlbumCoverUtils.CallBack mCallBack;
    public WeakReference<GalleryFragment> mFragmentRef;
    public Album mOperationAlbum;
    public PhoneImmersionMenu mPhoneImmersionMenu;

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public boolean onPrepareImmersionMenu(ImmersionMenu immersionMenu) {
        return false;
    }

    public AlbumDetailReplaceAlbumCoverImmersionMenu(GalleryFragment galleryFragment, Album album, ReplaceAlbumCoverUtils.CallBack callBack) {
        this.mFragmentRef = new WeakReference<>(galleryFragment);
        this.mOperationAlbum = album;
        this.mCallBack = callBack;
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public void onCreateImmersionMenu(ImmersionMenu immersionMenu) {
        if (this.mFragmentRef.get() == null) {
            return;
        }
        ImmersionMenuItem add = immersionMenu.add(R.id.replace_album_cover_custom_mode, this.mFragmentRef.get().getString(R.string.operation_replace_album_cover_custom_mode));
        if (this.mOperationAlbum.isManualSetCover()) {
            immersionMenu.add(R.id.replace_album_cover_default_mode, this.mFragmentRef.get().getString(R.string.operation_replace_album_cover_default_mode)).setRemainWhenClick(false);
        }
        add.setRemainWhenClick(false);
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public void onImmersionMenuSelected(ImmersionMenu immersionMenu, ImmersionMenuItem immersionMenuItem) {
        switch (immersionMenuItem.getItemId()) {
            case R.id.replace_album_cover_custom_mode /* 2131363201 */:
                ReplaceAlbumCoverUtils.startPhotoPickerByReplaceAlbumCover(this.mFragmentRef.get(), this.mOperationAlbum);
                return;
            case R.id.replace_album_cover_default_mode /* 2131363202 */:
                ReplaceAlbumCoverUtils.doRecoverAlbumCover(this.mOperationAlbum, false, this.mFragmentRef.get(), new ReplaceAlbumCoverUtils.CallBack() { // from class: com.miui.gallery.ui.AlbumDetailReplaceAlbumCoverImmersionMenu.1
                    @Override // com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils.CallBack
                    public void onSuccess(List<Pair<Album, DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> list) {
                        if (AlbumDetailReplaceAlbumCoverImmersionMenu.this.mCallBack != null) {
                            AlbumDetailReplaceAlbumCoverImmersionMenu.this.mCallBack.onSuccess(list);
                        }
                    }

                    @Override // com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils.CallBack
                    public void onFailed(Collection<Album> collection, long j) {
                        if (AlbumDetailReplaceAlbumCoverImmersionMenu.this.mCallBack != null) {
                            AlbumDetailReplaceAlbumCoverImmersionMenu.this.mCallBack.onFailed(collection, j);
                        }
                    }
                });
                return;
            default:
                return;
        }
    }

    public void showImmersionMenu(View view) {
        if (this.mFragmentRef.get() == null) {
            return;
        }
        if (this.mOperationAlbum.isManualSetCover()) {
            if (this.mPhoneImmersionMenu == null) {
                this.mPhoneImmersionMenu = new PhoneImmersionMenu(this.mFragmentRef.get().getContext(), this);
            }
            this.mPhoneImmersionMenu.show(view, null);
            return;
        }
        ReplaceAlbumCoverUtils.startPhotoPickerByReplaceAlbumCover(this.mFragmentRef.get(), this.mOperationAlbum);
    }
}
