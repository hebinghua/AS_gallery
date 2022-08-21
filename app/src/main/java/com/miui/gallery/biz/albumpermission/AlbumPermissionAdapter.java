package com.miui.gallery.biz.albumpermission;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.adapter.BaseGalleryAdapter;
import com.miui.gallery.biz.albumpermission.data.PermissionAlbum;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ObservableProperty;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;

/* compiled from: AlbumPermissionAdapter.kt */
/* loaded from: classes.dex */
public final class AlbumPermissionAdapter extends BaseGalleryAdapter<PermissionAlbum, BaseViewHolder> {
    public static final /* synthetic */ KProperty<Object>[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(AlbumPermissionAdapter.class, "nonGrantedData", "getNonGrantedData$app_cnRelease()Ljava/util/List;", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(AlbumPermissionAdapter.class, "grantedData", "getGrantedData$app_cnRelease()Ljava/util/List;", 0))};
    public static final Companion Companion = new Companion(null);
    public final FragmentActivity activity;
    public final ReadWriteProperty grantedData$delegate;
    public final ReadWriteProperty nonGrantedData$delegate;

    public AlbumPermissionAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.activity = fragmentActivity;
        Delegates delegates = Delegates.INSTANCE;
        this.nonGrantedData$delegate = new ObservableProperty<List<? extends PermissionAlbum>>(CollectionsKt__CollectionsKt.emptyList(), this) { // from class: com.miui.gallery.biz.albumpermission.AlbumPermissionAdapter$special$$inlined$observable$1
            public final /* synthetic */ Object $initialValue;
            public final /* synthetic */ AlbumPermissionAdapter this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(r1);
                this.$initialValue = r1;
                this.this$0 = this;
            }

            @Override // kotlin.properties.ObservableProperty
            public void afterChange(KProperty<?> property, List<? extends PermissionAlbum> list, List<? extends PermissionAlbum> list2) {
                Intrinsics.checkNotNullParameter(property, "property");
                this.this$0.notifyDataSetChanged();
            }
        };
        this.grantedData$delegate = new ObservableProperty<List<? extends PermissionAlbum>>(CollectionsKt__CollectionsKt.emptyList(), this) { // from class: com.miui.gallery.biz.albumpermission.AlbumPermissionAdapter$special$$inlined$observable$2
            public final /* synthetic */ Object $initialValue;
            public final /* synthetic */ AlbumPermissionAdapter this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(r1);
                this.$initialValue = r1;
                this.this$0 = this;
            }

            @Override // kotlin.properties.ObservableProperty
            public void afterChange(KProperty<?> property, List<? extends PermissionAlbum> list, List<? extends PermissionAlbum> list2) {
                Intrinsics.checkNotNullParameter(property, "property");
                this.this$0.notifyDataSetChanged();
            }
        };
    }

    public final List<PermissionAlbum> getNonGrantedData$app_cnRelease() {
        return (List) this.nonGrantedData$delegate.getValue(this, $$delegatedProperties[0]);
    }

    public final void setNonGrantedData$app_cnRelease(List<PermissionAlbum> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.nonGrantedData$delegate.setValue(this, $$delegatedProperties[0], list);
    }

    public final List<PermissionAlbum> getGrantedData$app_cnRelease() {
        return (List) this.grantedData$delegate.getValue(this, $$delegatedProperties[1]);
    }

    public final void setGrantedData$app_cnRelease(List<PermissionAlbum> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.grantedData$delegate.setValue(this, $$delegatedProperties[1], list);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1843onCreateViewHolder(ViewGroup parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        if (i == 1 || i == 2) {
            return new AlbumPermissionTitleViewHolder(new TextView(parent.getContext()));
        }
        if (i == 3) {
            return new AlbumPermissionDividerViewHolder(new ImageView(parent.getContext()));
        }
        FragmentActivity fragmentActivity = this.activity;
        View inflate = BaseViewHolder.getInflater(parent.getContext()).inflate(R.layout.album_permission_layout, parent, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "getInflater(parent.conte…on_layout, parent, false)");
        return new AlbumPermissionItemViewHolder(fragmentActivity, inflate, null, null, null, null, null, null, 252, null);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(BaseViewHolder holder, int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        int itemViewType = getItemViewType(i);
        if (itemViewType == 0) {
            PermissionAlbum mo1558getItem = mo1558getItem(i);
            Intrinsics.checkNotNull(mo1558getItem);
            ((AlbumPermissionItemViewHolder) holder).bind(mo1558getItem);
        } else if (itemViewType != 1 && itemViewType != 2) {
        } else {
            ((AlbumPermissionTitleViewHolder) holder).bind(itemViewType);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        if (!getNonGrantedData$app_cnRelease().isEmpty()) {
            if (i == 0) {
                return -1L;
            }
            if (i == getNonGrantedData$app_cnRelease().size() + 1) {
                return -3L;
            }
            if (i == getNonGrantedData$app_cnRelease().size() + 2) {
                return -2L;
            }
        }
        if (i == 0) {
            return -2L;
        }
        PermissionAlbum mo1558getItem = mo1558getItem(i);
        Intrinsics.checkNotNull(mo1558getItem);
        return mo1558getItem.getId();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (getNonGrantedData$app_cnRelease().isEmpty() & (!getGrantedData$app_cnRelease().isEmpty())) {
            return getGrantedData$app_cnRelease().size() + 1;
        }
        if (getGrantedData$app_cnRelease().isEmpty() & (!getNonGrantedData$app_cnRelease().isEmpty())) {
            return getNonGrantedData$app_cnRelease().size() + 1;
        }
        if (!getNonGrantedData$app_cnRelease().isEmpty() || !getGrantedData$app_cnRelease().isEmpty()) {
            return getNonGrantedData$app_cnRelease().size() + getGrantedData$app_cnRelease().size() + 3;
        }
        return 0;
    }

    @Override // com.miui.gallery.adapter.BaseRecyclerAdapter, com.miui.gallery.adapter.IBaseRecyclerAdapter
    /* renamed from: getItem */
    public PermissionAlbum mo1558getItem(int i) {
        if (!getNonGrantedData$app_cnRelease().isEmpty()) {
            if (i == 0 || i == getNonGrantedData$app_cnRelease().size() + 1 || i == getNonGrantedData$app_cnRelease().size() + 2) {
                return null;
            }
            if (i < getNonGrantedData$app_cnRelease().size() + 1) {
                return getNonGrantedData$app_cnRelease().get(i - 1);
            }
            if (i < getNonGrantedData$app_cnRelease().size() + getGrantedData$app_cnRelease().size() + 3) {
                return getGrantedData$app_cnRelease().get((i - getNonGrantedData$app_cnRelease().size()) - 3);
            }
        }
        if (i == 0) {
            return null;
        }
        return getGrantedData$app_cnRelease().get(i - 1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (!getNonGrantedData$app_cnRelease().isEmpty()) {
            if (i == 0) {
                return 1;
            }
            if (i == getNonGrantedData$app_cnRelease().size() + 1) {
                return 3;
            }
            if (i == getNonGrantedData$app_cnRelease().size() + 2) {
                return 2;
            }
        }
        return i == 0 ? 2 : 0;
    }

    /* compiled from: AlbumPermissionAdapter.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
