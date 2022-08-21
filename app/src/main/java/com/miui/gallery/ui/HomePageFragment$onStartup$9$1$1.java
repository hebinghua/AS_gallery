package com.miui.gallery.ui;

import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.adapter.HomePageAdapter2;
import com.miui.gallery.model.ImageLoadParams;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment$onStartup$9$1$1 extends Lambda implements Function1<ImageLoadParams.Builder, Unit> {
    public final /* synthetic */ int $position;
    public final /* synthetic */ HomePageFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$onStartup$9$1$1(HomePageFragment homePageFragment, int i) {
        super(1);
        this.this$0 = homePageFragment;
        this.$position = i;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(ImageLoadParams.Builder builder) {
        invoke2(builder);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(ImageLoadParams.Builder ImageLoadParams) {
        Intrinsics.checkNotNullParameter(ImageLoadParams, "$this$ImageLoadParams");
        HomePageAdapter2 homePageAdapter2 = this.this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter2);
        ImageLoadParams.m1104setKey(homePageAdapter2.getItemKey(this.$position));
        HomePageAdapter2 homePageAdapter22 = this.this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter22);
        ImageLoadParams.m1100setFilePath(homePageAdapter22.getBindImagePath(this.$position));
        ImageLoadParams.m1110setTargetSize(Config$ThumbConfig.get().sMicroTargetSize);
        HomePageAdapter2 homePageAdapter23 = this.this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter23);
        ImageLoadParams.m1107setRegionRect(homePageAdapter23.getItemDecodeRectF(this.$position));
        ImageLoadParams.m1103setInitPosition(this.$position);
        HomePageAdapter2 homePageAdapter24 = this.this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter24);
        ImageLoadParams.m1106setMimeType(homePageAdapter24.getMimeType(this.$position));
        HomePageAdapter2 homePageAdapter25 = this.this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter25);
        ImageLoadParams.m1099setFileLength(homePageAdapter25.getFileLength(this.$position));
        HomePageAdapter2 homePageAdapter26 = this.this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter26);
        ImageLoadParams.m1102setImageWidth(homePageAdapter26.getImageWidth(this.$position));
        HomePageAdapter2 homePageAdapter27 = this.this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter27);
        ImageLoadParams.m1101setImageHeight(homePageAdapter27.getImageHeight(this.$position));
        HomePageAdapter2 homePageAdapter28 = this.this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter28);
        ImageLoadParams.m1098setCreateTime(homePageAdapter28.getCreateTime(this.$position));
        HomePageAdapter2 homePageAdapter29 = this.this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter29);
        ImageLoadParams.m1105setLocation(homePageAdapter29.getLocation(this.$position));
    }
}
