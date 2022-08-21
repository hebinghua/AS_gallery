package com.miui.gallery.vlog.home;

import androidx.fragment.app.FragmentManager;
import com.miui.gallery.vlog.MenuFragment;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.entity.VideoClip;
import java.util.List;

/* loaded from: classes2.dex */
public interface VlogContract$IVlogModel {
    MenuFragment getCurrentEffectMenuFragment();

    FragmentManager getFragmentManager();

    String getOutFilePath();

    MiVideoSdkManager getSdkManager();

    List<VideoClip> getVideoClips();

    void initScreenRelatedValues();

    boolean isSingleVideoEdit();

    void setCurrentEffectMenuFragment(int i, MenuFragment menuFragment);

    void setSaveStatus(boolean z);
}
