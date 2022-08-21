package com.miui.gallery.search.core.display;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class BaseSuggestionViewHolder extends RecyclerView.ViewHolder {
    public ImageView mAlbumCoverBig;
    public ImageView mAlbumCoverFirst;
    public ImageView mAlbumCoverFourth;
    public ImageView mAlbumCoverSecond;
    public ImageView mAlbumCoverThird;
    public List<ImageView> mBackupIconView;
    public View mClickView;
    public ImageView mIconView;
    public TextView mSubTitle;
    public TextView mTitle;
    public String mViewType;

    public BaseSuggestionViewHolder(View view) {
        super(view);
        this.mClickView = view.findViewById(R.id.click_view);
        this.mIconView = (ImageView) view.findViewById(R.id.icon);
        this.mTitle = (TextView) view.findViewById(R.id.title);
        this.mSubTitle = (TextView) view.findViewById(R.id.subtitle);
        this.mAlbumCoverFirst = (ImageView) view.findViewById(R.id.album_cover_first);
        this.mAlbumCoverSecond = (ImageView) view.findViewById(R.id.album_cover_second);
        this.mAlbumCoverThird = (ImageView) view.findViewById(R.id.album_cover_third);
        this.mAlbumCoverFourth = (ImageView) view.findViewById(R.id.album_cover_fourth);
        this.mAlbumCoverBig = (ImageView) view.findViewById(R.id.album_cover_big);
    }

    public String getViewType() {
        return this.mViewType;
    }

    public void setViewType(String str) {
        this.mViewType = str;
    }

    public ImageView getIconView() {
        return this.mIconView;
    }

    public TextView getTitle() {
        return this.mTitle;
    }

    public TextView getSubTitle() {
        return this.mSubTitle;
    }

    public View getClickView() {
        return this.mClickView;
    }

    public boolean hasSuggestionIconsView() {
        return (this.mAlbumCoverFirst == null || this.mAlbumCoverSecond == null || this.mAlbumCoverThird == null || this.mAlbumCoverFourth == null || this.mAlbumCoverBig == null) ? false : true;
    }

    public List<ImageView> getSuggestionIconViewList() {
        if (this.mBackupIconView == null) {
            ArrayList arrayList = new ArrayList();
            this.mBackupIconView = arrayList;
            arrayList.add(this.mAlbumCoverFirst);
            this.mBackupIconView.add(this.mAlbumCoverSecond);
            this.mBackupIconView.add(this.mAlbumCoverThird);
            this.mBackupIconView.add(this.mAlbumCoverFourth);
        }
        showOrHideBackupIconViewList(true);
        showOrHideBackupBigView(false);
        return this.mBackupIconView;
    }

    public final void showOrHideBackupIconViewList(boolean z) {
        ImageView imageView = this.mAlbumCoverFirst;
        int i = 0;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
        }
        ImageView imageView2 = this.mAlbumCoverSecond;
        if (imageView2 != null) {
            imageView2.setVisibility(z ? 0 : 8);
        }
        ImageView imageView3 = this.mAlbumCoverThird;
        if (imageView3 != null) {
            imageView3.setVisibility(z ? 0 : 8);
        }
        ImageView imageView4 = this.mAlbumCoverFourth;
        if (imageView4 != null) {
            if (!z) {
                i = 8;
            }
            imageView4.setVisibility(i);
        }
    }

    public final void showOrHideBackupBigView(boolean z) {
        ImageView imageView = this.mAlbumCoverBig;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
        }
    }

    public ImageView getSuggestionIconBigView() {
        showOrHideBackupIconViewList(false);
        showOrHideBackupBigView(true);
        return this.mAlbumCoverBig;
    }
}
