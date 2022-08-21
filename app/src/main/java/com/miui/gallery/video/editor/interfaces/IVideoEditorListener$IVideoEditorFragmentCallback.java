package com.miui.gallery.video.editor.interfaces;

import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.model.MenuFragmentData;
import java.util.List;

/* loaded from: classes2.dex */
public interface IVideoEditorListener$IVideoEditorFragmentCallback {
    void changeEditMenu(MenuFragmentData menuFragmentData);

    List<MenuFragmentData> getNavigatorData(int i);

    VideoEditor getVideoEditor();

    boolean isLoadSuccess();

    void onCancel();

    void onSave();

    void showNavEditMenu();

    void updateAudioVoiceView(boolean z);

    void updateAutoTrimView();

    void updatePlayBtnView();
}
