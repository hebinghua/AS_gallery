package com.miui.gallery.widget;

import android.content.Context;
import android.view.View;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class BaseMamlView {
    public MamlView mMamlView;

    public BaseMamlView(Context context, String str) {
        context.getFilesDir().getAbsolutePath();
        this.mMamlView = new MamlView(context);
    }

    public MamlView getView() {
        return this.mMamlView;
    }

    public void resume() {
        MamlView mamlView = this.mMamlView;
        if (mamlView != null) {
            mamlView.onResume();
            DefaultLogger.d("GalleryMamlView", "resume");
        }
    }

    public void pause() {
        MamlView mamlView = this.mMamlView;
        if (mamlView != null) {
            mamlView.onPause();
            DefaultLogger.d("GalleryMamlView", "pause");
        }
    }

    public void destroy() {
        MamlView mamlView = this.mMamlView;
        if (mamlView != null) {
            mamlView.onDestory();
            this.mMamlView = null;
            DefaultLogger.d("GalleryMamlView", "destroy");
        }
    }

    /* loaded from: classes2.dex */
    public static class MamlView extends View {
        public void onDestory() {
        }

        public void onPause() {
        }

        public void onResume() {
        }

        public void putVariableNumber(String str, double d) {
        }

        public MamlView(Context context) {
            super(context);
        }
    }
}
