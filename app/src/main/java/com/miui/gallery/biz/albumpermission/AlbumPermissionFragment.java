package com.miui.gallery.biz.albumpermission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentViewModelLazyKt;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.miui.gallery.R;
import com.miui.gallery.biz.albumpermission.AlbumPermissionViewModel;
import com.miui.gallery.biz.albumpermission.data.PermissionAlbum;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.tsd.INestedTwoStageDrawer;
import com.miui.gallery.widget.tsd.NestedTwoStageDrawer;
import java.util.List;
import java.util.stream.Collectors;
import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

/* compiled from: AlbumPermissionFragment.kt */
/* loaded from: classes.dex */
public final class AlbumPermissionFragment extends Hilt_AlbumPermissionFragment {
    public static final Companion Companion = new Companion(null);
    public AlbumPermissionAdapter adapter;
    public final Lazy viewModel$delegate = FragmentViewModelLazyKt.createViewModelLazy(this, Reflection.getOrCreateKotlinClass(AlbumPermissionViewModel.class), new AlbumPermissionFragment$special$$inlined$assistedViewModel$3(new AlbumPermissionFragment$special$$inlined$assistedViewModel$2(this)), new AlbumPermissionFragment$special$$inlined$assistedViewModel$1(this, this));
    public AlbumPermissionViewModel.AssistedVMFactory vmFactory;

    public static /* synthetic */ void $r8$lambda$4ryTJPrBYcpSenyUZXqLGD6_OfY(AlbumPermissionFragment albumPermissionFragment, List list) {
        m576subscribe$lambda4(albumPermissionFragment, list);
    }

    public final AlbumPermissionViewModel.AssistedVMFactory getVmFactory$app_cnRelease() {
        AlbumPermissionViewModel.AssistedVMFactory assistedVMFactory = this.vmFactory;
        if (assistedVMFactory != null) {
            return assistedVMFactory;
        }
        Intrinsics.throwUninitializedPropertyAccessException("vmFactory");
        return null;
    }

    @Override // com.miui.gallery.arch.platform.UIComponent
    /* renamed from: getViewModel */
    public AlbumPermissionViewModel mo617getViewModel() {
        return (AlbumPermissionViewModel) this.viewModel$delegate.mo119getValue();
    }

    @Override // com.miui.gallery.arch.platform.BaseUIFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setThemeRes(2131952018);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        Intrinsics.checkNotNullParameter(container, "container");
        AlbumPermissionAdapter albumPermissionAdapter = new AlbumPermissionAdapter(getActivity());
        albumPermissionAdapter.setHasStableIds(true);
        this.adapter = albumPermissionAdapter;
        subscribe();
        INestedTwoStageDrawer create = NestedTwoStageDrawer.create(getContext());
        create.setMarginEnabled(false);
        create.setHeaderView(createHeaderView());
        create.setContentView(createContentView());
        return (View) create;
    }

    public final void subscribe() {
        mo617getViewModel().getAlbums().observe(getViewLifecycleOwner(), new Observer() { // from class: com.miui.gallery.biz.albumpermission.AlbumPermissionFragment$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                AlbumPermissionFragment.$r8$lambda$4ryTJPrBYcpSenyUZXqLGD6_OfY(AlbumPermissionFragment.this, (List) obj);
            }
        });
    }

    /* renamed from: subscribe$lambda-4 */
    public static final void m576subscribe$lambda4(AlbumPermissionFragment this$0, List list) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        AlbumPermissionAdapter albumPermissionAdapter = this$0.adapter;
        AlbumPermissionAdapter albumPermissionAdapter2 = null;
        if (albumPermissionAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            albumPermissionAdapter = null;
        }
        Object collect = list.stream().filter(AlbumPermissionFragment$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
        Intrinsics.checkNotNullExpressionValue(collect, "it.stream().filter { alb…lect(Collectors.toList())");
        albumPermissionAdapter.setNonGrantedData$app_cnRelease((List) collect);
        AlbumPermissionAdapter albumPermissionAdapter3 = this$0.adapter;
        if (albumPermissionAdapter3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
        } else {
            albumPermissionAdapter2 = albumPermissionAdapter3;
        }
        Object collect2 = list.stream().filter(AlbumPermissionFragment$$ExternalSyntheticLambda2.INSTANCE).collect(Collectors.toList());
        Intrinsics.checkNotNullExpressionValue(collect2, "it.stream().filter { alb…lect(Collectors.toList())");
        albumPermissionAdapter2.setGrantedData$app_cnRelease((List) collect2);
    }

    /* renamed from: subscribe$lambda-4$lambda-2 */
    public static final boolean m577subscribe$lambda4$lambda2(PermissionAlbum permissionAlbum) {
        return !permissionAlbum.getGranted() && permissionAlbum.getApplicable();
    }

    public final View createHeaderView() {
        TextView textView = new TextView(getContext());
        textView.setBackgroundResource(R.drawable.album_permission_tip_background);
        textView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.album_permission_tip_text_size));
        textView.setTextColor(getResources().getColor(R.color.highlighted_text, textView.getContext().getTheme()));
        textView.setPadding(getResources().getDimensionPixelSize(R.dimen.album_permission_tip_padding_start_end), getResources().getDimensionPixelSize(R.dimen.album_permission_tip_padding_top_bottom), getResources().getDimensionPixelSize(R.dimen.album_permission_tip_padding_start_end), getResources().getDimensionPixelSize(R.dimen.album_permission_tip_padding_top_bottom));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        layoutParams.setMarginStart(getResources().getDimensionPixelSize(R.dimen.album_permission_tip_margin_start_end));
        layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.album_permission_tip_margin_start_end));
        layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.album_permission_tip_margin_top);
        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.album_permission_tip_margin_bottom);
        textView.setLayoutParams(layoutParams);
        textView.setText(getString(R.string.album_permission_tip_text));
        return textView;
    }

    public final View createContentView() {
        GalleryRecyclerView galleryRecyclerView = new GalleryRecyclerView(getContext());
        galleryRecyclerView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        AlbumPermissionAdapter albumPermissionAdapter = this.adapter;
        if (albumPermissionAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            albumPermissionAdapter = null;
        }
        galleryRecyclerView.setAdapter(albumPermissionAdapter);
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        galleryRecyclerView.setPadding(getResources().getDimensionPixelSize(R.dimen.album_permission_content_margin_start_end), 0, getResources().getDimensionPixelSize(R.dimen.album_permission_content_margin_start_end), 0);
        return galleryRecyclerView;
    }

    /* compiled from: AlbumPermissionFragment.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
