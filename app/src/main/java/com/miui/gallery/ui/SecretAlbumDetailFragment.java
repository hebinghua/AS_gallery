package com.miui.gallery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;

/* loaded from: classes2.dex */
public class SecretAlbumDetailFragment extends ModernAlbumDetailFragment {
    public ImageView mLogoImageView;
    public RecyclerViewFooterItemDecoration mRecyclerViewFooterItemDecoration;

    @Override // com.miui.gallery.ui.AlbumDetailFragment, com.miui.gallery.ui.AlbumDetailFragmentBase, com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        View inflate = View.inflate(this.mActivity, R.layout.secret_album_footer_logo, null);
        ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.secret_album_footer_logo, (ViewGroup) onInflateView).findViewById(R.id.logo);
        this.mLogoImageView = imageView;
        imageView.setVisibility(8);
        RecyclerViewFooterItemDecoration recyclerViewFooterItemDecoration = new RecyclerViewFooterItemDecoration(inflate);
        this.mRecyclerViewFooterItemDecoration = recyclerViewFooterItemDecoration;
        this.mRecyclerView.addItemDecoration(recyclerViewFooterItemDecoration);
        return onInflateView;
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragment, com.miui.gallery.ui.AlbumDetailFragmentBase
    public void onEnterActionMode() {
        TrackController.trackExpose("403.51.0.1.11422");
        this.mRecyclerView.removeItemDecoration(this.mRecyclerViewFooterItemDecoration);
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public void onExitActionMode() {
        this.mRecyclerView.addItemDecoration(this.mRecyclerViewFooterItemDecoration);
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragment, com.miui.gallery.ui.PhotoListFragmentBase
    public void onEmptyViewVisibilityChanged(int i) {
        super.onEmptyViewVisibilityChanged(i);
        if (i == 0) {
            this.mLogoImageView.setVisibility(0);
        } else {
            this.mLogoImageView.setVisibility(8);
        }
    }
}
