package com.miui.gallery.movie.core;

import android.content.Context;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.movie.entity.AudioResource;
import com.miui.gallery.movie.entity.ImageEntity;
import com.miui.gallery.movie.entity.MovieInfo;
import com.miui.gallery.movie.entity.TemplateResource;
import com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView;
import java.util.List;

/* loaded from: classes2.dex */
public interface IMovieManager<T> {
    void addImage(List<ImageEntity> list);

    IMultiThumbnailSequenceView createThumbnailSequenceView(Context context);

    void export(String str, MovieManager.EncodeStateInterface encodeStateInterface);

    /* renamed from: getThumbnailImages */
    T mo1161getThumbnailImages();

    int getTotalTime();

    void init(Context context);

    boolean isReadyForExport();

    boolean isReadyForSwitch();

    boolean isSupportVideo(String str);

    void moveImage(int i, int i2);

    void removeImageAtIndex(int i);

    void replay();

    void resetImage(List<ImageEntity> list, boolean z);

    int seekToIndex(int i);

    void setAudio(AudioResource audioResource);

    void setInfo(MovieInfo movieInfo);

    void setIsShortVideo(boolean z);

    void setTemplate(TemplateResource templateResource);
}
