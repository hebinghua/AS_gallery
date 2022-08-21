package com.miui.gallery.biz.albumpermission;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.AbstractSavedStateViewModelFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: ViewModelX.kt */
/* loaded from: classes.dex */
public final class AlbumPermissionFragment$special$$inlined$assistedViewModel$1 extends Lambda implements Function0<ViewModelProvider.Factory> {
    public final /* synthetic */ Fragment $this_assistedViewModel;
    public final /* synthetic */ AlbumPermissionFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AlbumPermissionFragment$special$$inlined$assistedViewModel$1(Fragment fragment, AlbumPermissionFragment albumPermissionFragment) {
        super(0);
        this.$this_assistedViewModel = fragment;
        this.this$0 = albumPermissionFragment;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final ViewModelProvider.Factory mo1738invoke() {
        return new AbstractSavedStateViewModelFactory(this.$this_assistedViewModel.getArguments(), this.this$0) { // from class: com.miui.gallery.biz.albumpermission.AlbumPermissionFragment$special$$inlined$assistedViewModel$1.1
            public final /* synthetic */ AlbumPermissionFragment this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(Fragment.this, r2);
                this.this$0 = r3;
            }

            @Override // androidx.lifecycle.AbstractSavedStateViewModelFactory
            public <T extends ViewModel> T create(String key, Class<T> modelClass, SavedStateHandle handle) {
                Intrinsics.checkNotNullParameter(key, "key");
                Intrinsics.checkNotNullParameter(modelClass, "modelClass");
                Intrinsics.checkNotNullParameter(handle, "handle");
                return this.this$0.getVmFactory$app_cnRelease().create();
            }
        };
    }
}
