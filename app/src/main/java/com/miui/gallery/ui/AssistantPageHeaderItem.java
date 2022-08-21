package com.miui.gallery.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.R;
import com.miui.gallery.adapter.AssistantPageHeaderAdapter;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyLibraryLoaderHelper;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.reddot.DisplayStatusManager;
import com.miui.gallery.util.ArtStillLibraryLoaderHelper;
import com.miui.gallery.util.FeatureUtil;
import com.miui.gallery.util.IDPhotoLibraryLoaderHelper;
import com.miui.gallery.util.MagicMattingLibraryLoaderHelper;
import com.miui.gallery.util.MovieLibraryLoaderHelper;
import com.miui.gallery.util.VideoPostDownloadManager;
import com.miui.gallery.util.VlogLibraryLoaderHelper;

/* loaded from: classes2.dex */
public class AssistantPageHeaderItem extends ConstraintLayout {
    public LibraryLoaderHelper.DownloadStateListener mArtStillDownloadStateListener;
    public LibraryLoaderHelper.DownloadStateListener mIDPhotoDownloadStateListener;
    public ImageView mIcon;
    public LibraryLoaderHelper.DownloadStateListener mMagicMattingDownloadStateListener;
    public LibraryLoaderHelper.DownloadStateListener mMovieDownloadStateListener;
    public ImageView mRedDot;
    public LibraryLoaderHelper.DownloadStateListener mSkyDownloadStateListener;
    public TextView mTitle;
    public VideoPostDownloadManager.DownloadStateListener mVideoPostDownloadListener;
    public LibraryLoaderHelper.DownloadStateListener mVlogDownloadStateListener;

    public AssistantPageHeaderItem(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AssistantPageHeaderItem(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mIcon = (ImageView) findViewById(R.id.icon);
        this.mTitle = (TextView) findViewById(R.id.title);
        this.mRedDot = (ImageView) findViewById(R.id.red_dot);
    }

    public View getIcon() {
        return this.mIcon;
    }

    public void bindData(Context context, final AssistantPageHeaderAdapter.Entrance entrance) {
        this.mIcon.setImageDrawable(context.getResources().getDrawable(entrance.mIconRes));
        this.mTitle.setText(entrance.mNameRes);
        this.mRedDot.setVisibility(DisplayStatusManager.getRedDotStatus(entrance.getFeatureName()) ? 0 : 4);
        if (entrance instanceof AssistantPageHeaderAdapter.PhotoMovieEntrance) {
            if (this.mMovieDownloadStateListener == null) {
                this.mMovieDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.AssistantPageHeaderItem.1
                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        AssistantPageHeaderItem.this.mTitle.setText(R.string.loading);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i) {
                        AssistantPageHeaderItem.this.mTitle.setText(entrance.mNameRes);
                    }
                };
            }
        } else {
            this.mMovieDownloadStateListener = null;
        }
        if (entrance instanceof AssistantPageHeaderAdapter.VlogEntrance) {
            if (this.mVlogDownloadStateListener == null) {
                this.mVlogDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.AssistantPageHeaderItem.2
                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        AssistantPageHeaderItem.this.mTitle.setText(R.string.loading);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i) {
                        AssistantPageHeaderItem.this.mTitle.setText(entrance.mNameRes);
                    }
                };
            }
        } else {
            this.mVlogDownloadStateListener = null;
        }
        if (FeatureUtil.isReplaceAssistantPageRecommend() && (entrance instanceof AssistantPageHeaderAdapter.FilterSkyEntrance)) {
            if (this.mSkyDownloadStateListener == null) {
                this.mSkyDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.AssistantPageHeaderItem.3
                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        AssistantPageHeaderItem.this.mTitle.setText(R.string.loading);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i) {
                        AssistantPageHeaderItem.this.mTitle.setText(entrance.mNameRes);
                    }
                };
            }
        } else {
            this.mSkyDownloadStateListener = null;
        }
        if (entrance instanceof AssistantPageHeaderAdapter.MagicMattingEntrance) {
            if (this.mMagicMattingDownloadStateListener == null) {
                this.mMagicMattingDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.AssistantPageHeaderItem.4
                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        AssistantPageHeaderItem.this.mTitle.setText(R.string.loading);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i) {
                        AssistantPageHeaderItem.this.mTitle.setText(entrance.mNameRes);
                    }
                };
            }
        } else {
            this.mMagicMattingDownloadStateListener = null;
        }
        if (entrance instanceof AssistantPageHeaderAdapter.IDPhotoEntrance) {
            if (this.mIDPhotoDownloadStateListener == null) {
                this.mIDPhotoDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.AssistantPageHeaderItem.5
                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        AssistantPageHeaderItem.this.mTitle.setText(R.string.loading);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i) {
                        AssistantPageHeaderItem.this.mTitle.setText(entrance.mNameRes);
                    }
                };
            }
        } else {
            this.mIDPhotoDownloadStateListener = null;
        }
        if (entrance instanceof AssistantPageHeaderAdapter.ArtStillEntrance) {
            if (this.mArtStillDownloadStateListener == null) {
                this.mArtStillDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.AssistantPageHeaderItem.6
                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        AssistantPageHeaderItem.this.mTitle.setText(R.string.loading);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i) {
                        AssistantPageHeaderItem.this.mTitle.setText(entrance.mNameRes);
                    }
                };
            }
        } else {
            this.mArtStillDownloadStateListener = null;
        }
        if (entrance instanceof AssistantPageHeaderAdapter.VideoPostEntrance) {
            if (this.mVideoPostDownloadListener != null) {
                return;
            }
            this.mVideoPostDownloadListener = new VideoPostDownloadManager.DownloadStateListener() { // from class: com.miui.gallery.ui.AssistantPageHeaderItem.7
                @Override // com.miui.gallery.util.VideoPostDownloadManager.DownloadStateListener
                public void onDownloading() {
                    AssistantPageHeaderItem.this.mTitle.setText(R.string.loading);
                }

                @Override // com.miui.gallery.util.VideoPostDownloadManager.DownloadStateListener
                public void onFinish(boolean z) {
                    AssistantPageHeaderItem.this.mTitle.setText(entrance.mNameRes);
                }
            };
            return;
        }
        this.mVideoPostDownloadListener = null;
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mIcon.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = getResources().getDimensionPixelOffset(R.dimen.assistant_page_header_icon_margin_top);
        this.mIcon.setLayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mMovieDownloadStateListener != null) {
            MovieLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mMovieDownloadStateListener);
        }
        LibraryLoaderHelper.DownloadStateListener downloadStateListener = this.mSkyDownloadStateListener;
        if (downloadStateListener != null) {
            SkyLibraryLoaderHelper.INSTANCE.addDownloadStateListener(downloadStateListener);
        }
        if (this.mVlogDownloadStateListener != null) {
            VlogLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mVlogDownloadStateListener);
        }
        if (this.mMagicMattingDownloadStateListener != null) {
            MagicMattingLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mMagicMattingDownloadStateListener);
        }
        if (this.mIDPhotoDownloadStateListener != null) {
            IDPhotoLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mIDPhotoDownloadStateListener);
        }
        if (this.mArtStillDownloadStateListener != null) {
            ArtStillLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mArtStillDownloadStateListener);
        }
        if (this.mVideoPostDownloadListener != null) {
            VideoPostDownloadManager.getInstance().setDownloadStateListener(this.mVideoPostDownloadListener);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mMovieDownloadStateListener != null) {
            MovieLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mMovieDownloadStateListener);
        }
        LibraryLoaderHelper.DownloadStateListener downloadStateListener = this.mSkyDownloadStateListener;
        if (downloadStateListener != null) {
            SkyLibraryLoaderHelper.INSTANCE.removeDownloadStateListener(downloadStateListener);
        }
        if (this.mVlogDownloadStateListener != null) {
            VlogLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mVlogDownloadStateListener);
        }
        if (this.mMagicMattingDownloadStateListener != null) {
            MagicMattingLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mMagicMattingDownloadStateListener);
        }
        if (this.mIDPhotoDownloadStateListener != null) {
            IDPhotoLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mIDPhotoDownloadStateListener);
        }
        if (this.mArtStillDownloadStateListener != null) {
            ArtStillLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mArtStillDownloadStateListener);
        }
        if (this.mVideoPostDownloadListener != null) {
            VideoPostDownloadManager.getInstance().removeDownloadStateListener();
        }
    }
}
