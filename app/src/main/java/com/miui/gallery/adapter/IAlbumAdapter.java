package com.miui.gallery.adapter;

import android.content.res.Configuration;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.widget.recyclerview.ProportionTagModel;
import com.miui.gallery.widget.recyclerview.transition.ITransitionalAdapter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public interface IAlbumAdapter extends IMediaAdapter, ITransitionalAdapter {
    List<ProportionTagModel<Integer>> calculateTagProportionList();

    ArrayList<Long> getBurstItemKeys(int i);

    String getCreatorId(int i);

    boolean isAllPhotosAlbum();

    boolean isBabyAlbum();

    void notifyDataChanged();

    void setAlbumType(AlbumType albumType);

    void setConfiguration(Configuration configuration);

    void setViewMode(PictureViewMode pictureViewMode, LifecycleOwner lifecycleOwner);
}
