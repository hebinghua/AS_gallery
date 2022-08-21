package com.miui.gallery.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.loader.content.Loader;
import com.miui.gallery.dao.PeopleAlbumTableServices;
import com.miui.gallery.model.datalayer.utils.OnLoaderContentChange;
import com.miui.gallery.model.datalayer.utils.loader.CustomCursorLoader;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.face.PeopleCursorHelper;
import com.miui.gallery.util.face.PeopleItem;
import com.miui.gallery.util.thread.ThreadManager;

/* loaded from: classes.dex */
public class PeopleAlbumTableServices {
    public static PeopleAlbumTableServices getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final PeopleAlbumTableServices INSTANCE = new PeopleAlbumTableServices();
    }

    public PeopleAlbumTableServices() {
    }

    public Loader getQueryPeopleFaceCoversLoader(Context context, int i, OnLoaderContentChange onLoaderContentChange) {
        return new FaceCoverLoader(context, i, onLoaderContentChange);
    }

    /* loaded from: classes.dex */
    public static final class FaceCoverLoader extends CustomCursorLoader {
        public OnLoaderContentChange mListener;

        public static /* synthetic */ void $r8$lambda$eSuA0yPT4VsnX6TKHEHvpiIhdRM(FaceCoverLoader faceCoverLoader) {
            faceCoverLoader.lambda$onContentChanged$1();
        }

        public static /* synthetic */ void $r8$lambda$zcNN0BgHQNJy5F4rq2Q0geDgtUQ(FaceCoverLoader faceCoverLoader) {
            faceCoverLoader.lambda$onContentChanged$0();
        }

        public FaceCoverLoader(Context context, int i, OnLoaderContentChange onLoaderContentChange) {
            super(context);
            this.mListener = onLoaderContentChange;
            Uri uri = GalleryContract.PeopleFace.PEOPLE_FACE_COVER_URI;
            setUri(i > 0 ? UriUtil.appendLimit(uri, i) : uri);
        }

        @Override // androidx.loader.content.Loader
        public void onContentChanged() {
            if (this.mListener == null) {
                super.onContentChanged();
            } else {
                ThreadManager.execute(31, new Runnable() { // from class: com.miui.gallery.dao.PeopleAlbumTableServices$FaceCoverLoader$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeopleAlbumTableServices.FaceCoverLoader.$r8$lambda$eSuA0yPT4VsnX6TKHEHvpiIhdRM(PeopleAlbumTableServices.FaceCoverLoader.this);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onContentChanged$1() {
            OnLoaderContentChange onLoaderContentChange = this.mListener;
            if (onLoaderContentChange == null || !onLoaderContentChange.onContentChange()) {
                com.miui.gallery.util.concurrent.ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.dao.PeopleAlbumTableServices$FaceCoverLoader$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeopleAlbumTableServices.FaceCoverLoader.$r8$lambda$zcNN0BgHQNJy5F4rq2Q0geDgtUQ(PeopleAlbumTableServices.FaceCoverLoader.this);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onContentChanged$0() {
            super.onContentChanged();
        }
    }

    public Cursor queryPeopleFaceCoversSnapShot(Context context, int i) {
        Uri uri = GalleryContract.PeopleFace.PEOPLE_SNAPSHOT_URI;
        if (i > 0) {
            uri = UriUtil.appendLimit(uri, i);
        }
        return context.getContentResolver().query(uri, PeopleItem.COMPAT_PROJECTION, null, null, null);
    }

    public Loader<Cursor> getQueryPersonsLoader(Context context, int i, boolean z) {
        Uri uri = z ? GalleryContract.PeopleFace.IGNORE_PERSONS_URI : GalleryContract.PeopleFace.PERSONS_URI;
        if (i > 0) {
            uri = UriUtil.appendLimit(uri, i);
        }
        CustomCursorLoader customCursorLoader = new CustomCursorLoader(context, true);
        customCursorLoader.setUri(uri);
        customCursorLoader.setProjection(PeopleCursorHelper.PROJECTION);
        return customCursorLoader;
    }
}
