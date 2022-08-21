package com.miui.gallery.ui;

import android.content.res.Configuration;
import com.miui.gallery.adapter.HomePageAdapter2;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.ui.pictures.PinchManager;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.recyclerview.InterceptableRecyclerView;
import java.util.Map;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: HomePageFragment.kt */
@DebugMetadata(c = "com.miui.gallery.ui.HomePageFragment$setPictureViewMode$1", f = "HomePageFragment.kt", l = {505}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class HomePageFragment$setPictureViewMode$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ PictureViewMode $viewMode;
    public int label;
    public final /* synthetic */ HomePageFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$setPictureViewMode$1(HomePageFragment homePageFragment, PictureViewMode pictureViewMode, Continuation<? super HomePageFragment$setPictureViewMode$1> continuation) {
        super(2, continuation);
        this.this$0 = homePageFragment;
        this.$viewMode = pictureViewMode;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new HomePageFragment$setPictureViewMode$1(this.this$0, this.$viewMode, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((HomePageFragment$setPictureViewMode$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        InterceptableRecyclerView interceptableRecyclerView;
        PinchManager pinchManager;
        Map map;
        Map map2;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            this.this$0.mTagProportionChanged = true;
            this.this$0.mViewMode = this.$viewMode;
            if (this.$viewMode.isAggregated() && (interceptableRecyclerView = this.this$0.mHomeGridView) != null) {
                interceptableRecyclerView.setItemAnimator(null);
            }
            HomePageAdapter2 homePageAdapter2 = this.this$0.mHomePageAdapter;
            Intrinsics.checkNotNull(homePageAdapter2);
            PictureViewMode pictureViewMode = this.$viewMode;
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(pictureViewMode, this.this$0);
            this.label = 1;
            if (homePageAdapter2.reselectViewMode(pictureViewMode, false, anonymousClass1, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            ResultKt.throwOnFailure(obj);
        }
        HomePageFragment homePageFragment = this.this$0;
        Configuration configuration = homePageFragment.getResources().getConfiguration();
        Intrinsics.checkNotNullExpressionValue(configuration, "resources.configuration");
        homePageFragment.updateConfiguration(configuration);
        EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.setItemAnimEnable(!this.$viewMode.isAggregated());
        }
        pinchManager = this.this$0.mPinchManager;
        if (pinchManager != null) {
            pinchManager.changeMode(this.$viewMode);
        }
        map = HomePageFragment.sViewModePreferenceMap;
        if (map.containsKey(this.$viewMode)) {
            map2 = HomePageFragment.sViewModePreferenceMap;
            Object obj2 = map2.get(this.$viewMode);
            Intrinsics.checkNotNull(obj2);
            GalleryPreferences.HomePage.setHomePageViewMode(((Number) obj2).intValue());
        }
        return Unit.INSTANCE;
    }

    /* compiled from: HomePageFragment.kt */
    /* renamed from: com.miui.gallery.ui.HomePageFragment$setPictureViewMode$1$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends Lambda implements Function0<Unit> {
        public final /* synthetic */ PictureViewMode $viewMode;
        public final /* synthetic */ HomePageFragment this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(PictureViewMode pictureViewMode, HomePageFragment homePageFragment) {
            super(0);
            this.$viewMode = pictureViewMode;
            this.this$0 = homePageFragment;
        }

        @Override // kotlin.jvm.functions.Function0
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo1738invoke() {
            mo1738invoke();
            return Unit.INSTANCE;
        }

        @Override // kotlin.jvm.functions.Function0
        /* renamed from: invoke  reason: collision with other method in class */
        public final void mo1738invoke() {
            Map map;
            map = HomePageFragment.sViewModePreferenceMap;
            if (map.containsKey(this.$viewMode)) {
                HomePageFragment homePageFragment = this.this$0;
                HomePageAdapter2 homePageAdapter2 = homePageFragment.mHomePageAdapter;
                Intrinsics.checkNotNull(homePageAdapter2);
                homePageFragment.updateSnapshot(homePageAdapter2.getCurrentList());
            }
        }
    }
}
