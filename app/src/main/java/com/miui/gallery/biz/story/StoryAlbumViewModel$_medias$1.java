package com.miui.gallery.biz.story;

import android.database.Cursor;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.assistant.model.TinyMediaFeature;
import com.miui.gallery.biz.story.data.MediaInfo;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: StoryAlbumViewModel.kt */
/* loaded from: classes.dex */
public final class StoryAlbumViewModel$_medias$1 extends Lambda implements Function1<Cursor, List<? extends MediaInfo>> {
    public static final StoryAlbumViewModel$_medias$1 INSTANCE = new StoryAlbumViewModel$_medias$1();

    public StoryAlbumViewModel$_medias$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final List<MediaInfo> mo2577invoke(Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "cursor");
        if (cursor.isClosed() || cursor.getCount() <= 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        ArrayList arrayList = new ArrayList(cursor.getCount());
        if (!cursor.moveToFirst()) {
            return arrayList;
        }
        do {
            MediaInfo mediaInfo = new MediaInfo(cursor);
            TinyMediaFeature imageFeature = MediaFeatureCacheManager.getInstance().getImageFeature(mediaInfo.getId());
            if (imageFeature != null) {
                mediaInfo.setScore(imageFeature.getScore());
            }
            arrayList.add(mediaInfo);
        } while (cursor.moveToNext());
        return arrayList;
    }
}
