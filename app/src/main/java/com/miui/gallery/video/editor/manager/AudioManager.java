package com.miui.gallery.video.editor.manager;

import com.miui.gallery.R;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.video.editor.LocalAudio;
import com.miui.gallery.video.editor.factory.VideoEditorModuleFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class AudioManager {
    public static final int[] sBgColor = {R.drawable.video_editor_audio_color_1, R.drawable.video_editor_audio_color_2, R.drawable.video_editor_audio_color_3, R.drawable.video_editor_audio_color_4, R.drawable.video_editor_audio_color_5, R.drawable.video_editor_audio_color_6};

    public static ArrayList<LocalAudio> initDataWithBgColor() {
        int[] iArr;
        ArrayList<LocalAudio> arrayList = new ArrayList<>();
        arrayList.add(getLocalNoneAudio());
        for (int i : sBgColor) {
            LocalAudio localAudio = new LocalAudio();
            localAudio.setIconResId(i);
            arrayList.add(localAudio);
        }
        arrayList.add(getLocalCustomAudio());
        return arrayList;
    }

    public static LocalAudio getLocalNoneAudio() {
        LocalAudio localAudio = new LocalAudio();
        localAudio.setIconResId(R.drawable.video_editor_audio_none);
        localAudio.setNameResId(R.string.video_editor_audio_none);
        localAudio.setDownloadState(17);
        return localAudio;
    }

    public static LocalAudio getLocalCustomAudio() {
        LocalAudio localAudio = new LocalAudio();
        localAudio.setIconResId(R.drawable.video_editor_audio_custom);
        localAudio.setNameResId(R.string.video_editor_audio_custom);
        localAudio.setDownloadState(17);
        return localAudio;
    }

    public static ArrayList<LocalAudio> loadAudioData(VideoEditorModuleFactory videoEditorModuleFactory, List<LocalResource> list) {
        int i;
        ArrayList<LocalAudio> arrayList = new ArrayList<>();
        if (!BaseMiscUtil.isValid(list)) {
            return arrayList;
        }
        for (LocalResource localResource : list) {
            if (localResource != null) {
                LocalAudio localAudio = new LocalAudio(localResource);
                if (localAudio.isNone()) {
                    localAudio.setIconResId(R.drawable.video_editor_audio_none);
                } else if (localAudio.isExtra()) {
                    int indexOf = list.indexOf(localResource);
                    int[] iArr = sBgColor;
                    if (indexOf < 1 || indexOf - 1 >= iArr.length) {
                        i = 0;
                    }
                    localAudio.setColor(iArr[i]);
                } else if (localAudio.isCustom()) {
                    localAudio.setIconResId(R.drawable.video_editor_audio_custom);
                }
                String str = videoEditorModuleFactory.getTemplatePath(localResource.id) + File.separator + localAudio.getFileName();
                if (new File(str).exists()) {
                    localAudio.setDownloadState(17);
                    localAudio.setSrcPath(str);
                }
                arrayList.add(localAudio);
            }
        }
        return arrayList;
    }
}
