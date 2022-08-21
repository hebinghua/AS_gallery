package com.miui.gallery.biz.story;

import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.model.ImageLoadParams;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: StoryAlbumFragment.kt */
/* loaded from: classes.dex */
public final class StoryAlbumFragment$multiChoiceModeListener$1$gotoPreviewSelectPage$1 extends Lambda implements Function1<ImageLoadParams.Builder, Unit> {
    public final /* synthetic */ int $initPos;
    public final /* synthetic */ StoryAlbumFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StoryAlbumFragment$multiChoiceModeListener$1$gotoPreviewSelectPage$1(StoryAlbumFragment storyAlbumFragment, int i) {
        super(1);
        this.this$0 = storyAlbumFragment;
        this.$initPos = i;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(ImageLoadParams.Builder builder) {
        invoke2(builder);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(ImageLoadParams.Builder ImageLoadParams) {
        StoryAlbumAdapter storyAlbumAdapter;
        StoryAlbumAdapter storyAlbumAdapter2;
        StoryAlbumAdapter storyAlbumAdapter3;
        StoryAlbumAdapter storyAlbumAdapter4;
        StoryAlbumAdapter storyAlbumAdapter5;
        StoryAlbumAdapter storyAlbumAdapter6;
        StoryAlbumAdapter storyAlbumAdapter7;
        Intrinsics.checkNotNullParameter(ImageLoadParams, "$this$ImageLoadParams");
        storyAlbumAdapter = this.this$0.adapter;
        StoryAlbumAdapter storyAlbumAdapter8 = null;
        if (storyAlbumAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter = null;
        }
        ImageLoadParams.m1104setKey(storyAlbumAdapter.getItemId(this.$initPos));
        storyAlbumAdapter2 = this.this$0.adapter;
        if (storyAlbumAdapter2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter2 = null;
        }
        ImageLoadParams.m1100setFilePath(storyAlbumAdapter2.getBindImagePath(this.$initPos));
        ImageLoadParams.m1110setTargetSize(Config$ThumbConfig.get().sMicroTargetSize);
        storyAlbumAdapter3 = this.this$0.adapter;
        if (storyAlbumAdapter3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter3 = null;
        }
        ImageLoadParams.m1107setRegionRect(storyAlbumAdapter3.getItemDecodeRectF(this.$initPos));
        ImageLoadParams.m1103setInitPosition(this.$initPos);
        storyAlbumAdapter4 = this.this$0.adapter;
        if (storyAlbumAdapter4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter4 = null;
        }
        ImageLoadParams.m1106setMimeType(storyAlbumAdapter4.getMimeType(this.$initPos));
        storyAlbumAdapter5 = this.this$0.adapter;
        if (storyAlbumAdapter5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter5 = null;
        }
        ImageLoadParams.m1099setFileLength(storyAlbumAdapter5.getFileLength(this.$initPos));
        storyAlbumAdapter6 = this.this$0.adapter;
        if (storyAlbumAdapter6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter6 = null;
        }
        ImageLoadParams.m1098setCreateTime(storyAlbumAdapter6.getCreateTime(this.$initPos));
        storyAlbumAdapter7 = this.this$0.adapter;
        if (storyAlbumAdapter7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
        } else {
            storyAlbumAdapter8 = storyAlbumAdapter7;
        }
        ImageLoadParams.m1105setLocation(storyAlbumAdapter8.getLocation(this.$initPos));
    }
}
