package com.miui.gallery.magic.special.effects.video.effects.menu;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.miui.gallery.magic.special.effects.video.adapter.VideoMusicAdapter;
import com.miui.gallery.magic.special.effects.video.adapter.VideoSpecialAdapter;

/* loaded from: classes2.dex */
public interface IMenu$VP {
    void addImageToBody(ImageView imageView, ViewGroup.LayoutParams layoutParams);

    void changeToolBar(int i);

    void loadListData();

    void onActionUp(float f, float f2);

    void scrollToPositionMusicItem(int i);

    void selectFile();

    void setAdapter(VideoSpecialAdapter videoSpecialAdapter);

    default void setBodyImage(Bitmap bitmap, float f) {
    }

    default void setBodyImage(ViewGroup viewGroup, Bitmap bitmap, float f) {
    }

    void setMusicAdapter(VideoMusicAdapter videoMusicAdapter);

    void setProgress(float f, int i);

    void setProgressDuration(float f);

    void startProgress(int i);

    void switchToAudioTrack();

    void switchToVideoEffect();

    void undo();
}
