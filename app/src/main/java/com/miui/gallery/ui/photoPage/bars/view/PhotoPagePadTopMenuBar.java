package com.miui.gallery.ui.photoPage.bars.view;

import android.app.Activity;
import android.content.res.Configuration;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;

/* loaded from: classes2.dex */
public class PhotoPagePadTopMenuBar extends PhotoPageTopMenuBar {
    public PhotoPagePadTopMenuBar(Activity activity, AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo, IViewProvider iViewProvider, IPhotoPageActionBarManager iPhotoPageActionBarManager, ActionBarCustomViewBuilder.CustomViewType customViewType) {
        super(activity, listenerInfo, iViewProvider, iPhotoPageActionBarManager, customViewType);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageTopMenuBar
    public void init(AbstractPhotoPageTopMenuBar.ListenerInfo listenerInfo) {
        super.init(listenerInfo);
        this.mSubTitle = (TextView) this.mRootView.findViewById(R.id.top_bar_subtitle);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopMenuBar, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar
    public void onActivityConfigurationChanged(Configuration configuration) {
        super.onActivityConfigurationChanged(configuration);
        ImageView imageView = this.mActionBackView;
        if (imageView != null) {
            imageView.requestFocus();
        }
    }
}
