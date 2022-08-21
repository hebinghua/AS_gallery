package com.miui.gallery.biz.story;

import com.miui.gallery.biz.story.data.MediaInfo;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: StoryAlbumViewModel.kt */
/* loaded from: classes.dex */
public final class StoryAlbumViewModel$durationComparator$2 extends Lambda implements Function0<MediaInfo.DurationComparator> {
    public static final StoryAlbumViewModel$durationComparator$2 INSTANCE = new StoryAlbumViewModel$durationComparator$2();

    public StoryAlbumViewModel$durationComparator$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final MediaInfo.DurationComparator mo1738invoke() {
        return new MediaInfo.DurationComparator();
    }
}
