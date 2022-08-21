package com.miui.gallery.vlog.rule;

import com.miui.gallery.vlog.entity.VideoClip;
import java.util.List;

/* loaded from: classes2.dex */
public class MatchedTemplate {
    public List<VideoClip> mMatchClips;
    public String mName;

    public MatchedTemplate(String str, List<VideoClip> list) {
        this.mName = str;
        this.mMatchClips = list;
    }
}
