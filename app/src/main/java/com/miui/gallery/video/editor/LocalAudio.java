package com.miui.gallery.video.editor;

import android.text.TextUtils;
import com.google.common.collect.ImmutableMap;
import com.miui.gallery.R;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.video.editor.model.VideoEditorBaseModel;
import java.io.File;

/* loaded from: classes2.dex */
public class LocalAudio extends VideoEditorBaseModel {
    public static final ImmutableMap<String, Integer> sAudioNames = ImmutableMap.builder().put("audio_none", Integer.valueOf((int) R.string.video_editor_audio_none)).put("audio_memory", Integer.valueOf((int) R.string.video_editor_audio_summertime)).put("audio_sad", Integer.valueOf((int) R.string.video_editor_audio_quiet)).put("audio_rock", Integer.valueOf((int) R.string.video_editor_audio_electronics)).put("audio_love", Integer.valueOf((int) R.string.video_editor_audio_dream)).put("audio_young", Integer.valueOf((int) R.string.video_editor_audio_antiquity)).put("audio_dynamic", Integer.valueOf((int) R.string.video_editor_audio_vitality)).put("audio_custom", Integer.valueOf((int) R.string.video_editor_audio_custom)).build();
    public int mBgColor;
    public int mIconResId;
    public int mNameResId;
    public String mSrcPath;

    public LocalAudio() {
        this.mIconResId = 0;
    }

    public LocalAudio(LocalResource localResource) {
        this.mIconResId = 0;
        if (localResource != null) {
            this.mID = localResource.id;
            this.mNameKey = localResource.nameKey;
            this.mLabel = localResource.label;
            this.mIconResId = localResource.imageId;
            this.mIconUrl = localResource.icon;
            String str = localResource.type;
            this.mType = str;
            boolean equals = "ve_type_extra".equals(str);
            this.mExtra = equals;
            if (equals) {
                return;
            }
            this.mDownloadState = 17;
        }
    }

    public LocalAudio(String str, int i, String str2, boolean z) {
        this.mIconResId = 0;
        this.mNameKey = str;
        this.mIconResId = i;
        this.mType = str2;
        boolean equals = "ve_type_extra".equals(str2);
        this.mExtra = equals;
        if (!equals) {
            this.mDownloadState = 17;
        }
    }

    public String getFileName() {
        String str = this.mNameKey.trim() + ".aac";
        this.mFileName = str;
        return str;
    }

    public int getNameResId() {
        if (this.mNameResId == 0) {
            ImmutableMap<String, Integer> immutableMap = sAudioNames;
            if (immutableMap.containsKey(this.mNameKey)) {
                this.mNameResId = immutableMap.get(this.mNameKey).intValue();
            }
        }
        return this.mNameResId;
    }

    public void setNameResId(int i) {
        this.mNameResId = i;
    }

    public int getBgColor() {
        return this.mBgColor;
    }

    public String getSrcPath() {
        if (!TextUtils.isEmpty(this.mUnZipPath)) {
            this.mSrcPath = this.mUnZipPath + File.separator + getFileName();
        }
        return this.mSrcPath;
    }

    public void setIconResId(int i) {
        this.mIconResId = i;
    }

    public int getIconResId() {
        return this.mIconResId;
    }

    public void setColor(int i) {
        this.mBgColor = i;
    }

    public void setSrcPath(String str) {
        this.mSrcPath = str;
    }
}
