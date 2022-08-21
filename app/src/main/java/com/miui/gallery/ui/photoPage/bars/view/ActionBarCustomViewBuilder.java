package com.miui.gallery.ui.photoPage.bars.view;

import android.view.View;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar;

/* loaded from: classes2.dex */
public class ActionBarCustomViewBuilder {
    public IPhotoPageTopBar mTopBar;

    /* loaded from: classes2.dex */
    public enum CustomViewType {
        TopBar,
        TopMenu,
        PadTopMenu
    }

    public ActionBarCustomViewBuilder(Builder builder) {
        this.mTopBar = AbstractPhotoPageTopMenuBar.BarFactory.create(builder.mType, builder.mActivity, builder.mListenerInfo, builder.mViewProvider, builder.mActionBarManager);
    }

    public IPhotoPageTopBar getTopBar() {
        return this.mTopBar;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public IPhotoPageActionBarManager mActionBarManager;
        public GalleryActivity mActivity;
        public AbstractPhotoPageTopMenuBar.ListenerInfo mListenerInfo;
        public CustomViewType mType;
        public IViewProvider mViewProvider;

        public Builder(GalleryActivity galleryActivity, CustomViewType customViewType, IViewProvider iViewProvider, IPhotoPageActionBarManager iPhotoPageActionBarManager) {
            this.mActivity = galleryActivity;
            this.mType = customViewType;
            this.mViewProvider = iViewProvider;
            this.mActionBarManager = iPhotoPageActionBarManager;
        }

        public Builder setOnBackClickListener(View.OnClickListener onClickListener) {
            if (this.mListenerInfo == null) {
                this.mListenerInfo = new AbstractPhotoPageTopMenuBar.ListenerInfo();
            }
            this.mListenerInfo.mOnBackClickListener = onClickListener;
            return this;
        }

        public Builder setOnRotateClickListener(View.OnClickListener onClickListener) {
            if (this.mListenerInfo == null) {
                this.mListenerInfo = new AbstractPhotoPageTopMenuBar.ListenerInfo();
            }
            this.mListenerInfo.mOnRotateClickListener = onClickListener;
            return this;
        }

        public Builder setOnWatchAllClickListener(View.OnClickListener onClickListener) {
            if (this.mListenerInfo == null) {
                this.mListenerInfo = new AbstractPhotoPageTopMenuBar.ListenerInfo();
            }
            this.mListenerInfo.mOnWatchAllClickListener = onClickListener;
            return this;
        }

        public Builder setOnLockClickListener(View.OnClickListener onClickListener) {
            if (this.mListenerInfo == null) {
                this.mListenerInfo = new AbstractPhotoPageTopMenuBar.ListenerInfo();
            }
            this.mListenerInfo.mOnLockClickListener = onClickListener;
            return this;
        }

        public Builder setOnLocationInfoClickListener(View.OnClickListener onClickListener) {
            if (this.mListenerInfo == null) {
                this.mListenerInfo = new AbstractPhotoPageTopMenuBar.ListenerInfo();
            }
            this.mListenerInfo.mOnLocationInfoClickListener = onClickListener;
            return this;
        }

        public ActionBarCustomViewBuilder build() {
            return new ActionBarCustomViewBuilder(this);
        }
    }
}
