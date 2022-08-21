package com.miui.gallery.biz.albumpermission;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.biz.albumpermission.data.PermissionAlbum;
import com.miui.gallery.glide.GlideApp;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.provider.cache.IAlbum;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AlbumPermissionItemViewHolder.kt */
/* loaded from: classes.dex */
public final class AlbumPermissionItemViewHolder extends BaseViewHolder {
    public final WeakReference<FragmentActivity> activityRef;
    public final ImageView albumCover;
    public final TextView albumName;
    public final View arrow;
    public final View grantButton;
    public final TextView grantText;
    public final TextView subAlbums;

    /* renamed from: $r8$lambda$0jT-U1wvk1X4F4sE5HKEd00vGvQ */
    public static /* synthetic */ void m583$r8$lambda$0jTU1wvk1X4F4sE5HKEd00vGvQ(AlbumPermissionItemViewHolder albumPermissionItemViewHolder, PermissionAlbum permissionAlbum, View view) {
        m584bind$lambda4(albumPermissionItemViewHolder, permissionAlbum, view);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public /* synthetic */ AlbumPermissionItemViewHolder(androidx.fragment.app.FragmentActivity r11, android.view.View r12, android.widget.ImageView r13, android.widget.TextView r14, android.widget.TextView r15, android.view.View r16, android.widget.TextView r17, android.view.View r18, int r19, kotlin.jvm.internal.DefaultConstructorMarker r20) {
        /*
            r10 = this;
            r2 = r12
            r0 = r19
            r1 = r0 & 4
            java.lang.String r3 = "<init>"
            if (r1 == 0) goto L17
            r1 = 2131361927(0x7f0a0087, float:1.834362E38)
            android.view.View r1 = r12.findViewById(r1)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r3)
            android.widget.ImageView r1 = (android.widget.ImageView) r1
            r4 = r1
            goto L18
        L17:
            r4 = r13
        L18:
            r1 = r0 & 8
            if (r1 == 0) goto L2a
            r1 = 2131361931(0x7f0a008b, float:1.8343628E38)
            android.view.View r1 = r12.findViewById(r1)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r3)
            android.widget.TextView r1 = (android.widget.TextView) r1
            r5 = r1
            goto L2b
        L2a:
            r5 = r14
        L2b:
            r1 = r0 & 16
            if (r1 == 0) goto L3d
            r1 = 2131361930(0x7f0a008a, float:1.8343626E38)
            android.view.View r1 = r12.findViewById(r1)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r3)
            android.widget.TextView r1 = (android.widget.TextView) r1
            r6 = r1
            goto L3e
        L3d:
            r6 = r15
        L3e:
            r1 = r0 & 32
            if (r1 == 0) goto L4e
            r1 = 2131362456(0x7f0a0298, float:1.8344693E38)
            android.view.View r1 = r12.findViewById(r1)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r3)
            r7 = r1
            goto L50
        L4e:
            r7 = r16
        L50:
            r1 = r0 & 64
            if (r1 == 0) goto L62
            r1 = 2131362457(0x7f0a0299, float:1.8344695E38)
            android.view.View r1 = r12.findViewById(r1)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r3)
            android.widget.TextView r1 = (android.widget.TextView) r1
            r8 = r1
            goto L64
        L62:
            r8 = r17
        L64:
            r0 = r0 & 128(0x80, float:1.794E-43)
            if (r0 == 0) goto L74
            r0 = 2131361993(0x7f0a00c9, float:1.8343754E38)
            android.view.View r0 = r12.findViewById(r0)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r3)
            r9 = r0
            goto L76
        L74:
            r9 = r18
        L76:
            r0 = r10
            r1 = r11
            r2 = r12
            r3 = r4
            r4 = r5
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.biz.albumpermission.AlbumPermissionItemViewHolder.<init>(androidx.fragment.app.FragmentActivity, android.view.View, android.widget.ImageView, android.widget.TextView, android.widget.TextView, android.view.View, android.widget.TextView, android.view.View, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AlbumPermissionItemViewHolder(FragmentActivity fragmentActivity, View view, ImageView albumCover, TextView albumName, TextView subAlbums, View grantButton, TextView grantText, View arrow) {
        super(view);
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(albumCover, "albumCover");
        Intrinsics.checkNotNullParameter(albumName, "albumName");
        Intrinsics.checkNotNullParameter(subAlbums, "subAlbums");
        Intrinsics.checkNotNullParameter(grantButton, "grantButton");
        Intrinsics.checkNotNullParameter(grantText, "grantText");
        Intrinsics.checkNotNullParameter(arrow, "arrow");
        this.albumCover = albumCover;
        this.albumName = albumName;
        this.subAlbums = subAlbums;
        this.grantButton = grantButton;
        this.grantText = grantText;
        this.arrow = arrow;
        this.activityRef = new WeakReference<>(fragmentActivity);
        ViewGroup.LayoutParams layoutParams = albumCover.getLayoutParams();
        layoutParams.width = albumCover.getContext().getResources().getDimensionPixelSize(R.dimen.album_permission_album_cover_width_height);
        layoutParams.height = albumCover.getContext().getResources().getDimensionPixelSize(R.dimen.album_permission_album_cover_width_height);
    }

    public final void bind(final PermissionAlbum album) {
        String quantityString;
        Intrinsics.checkNotNullParameter(album, "album");
        GlideApp.with(this.albumCover).mo990load(album.getCoverPath()).mo946apply((BaseRequestOptions<?>) GlideOptions.microThumbOf()).into(this.albumCover);
        this.albumName.setText(album.getName());
        TextView textView = this.subAlbums;
        if (album.getAlbums().size() == 1) {
            Resources resources = this.subAlbums.getContext().getResources();
            Object[] objArr = new Object[2];
            List<IAlbum> albums = album.getAlbums();
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(albums, 10));
            for (IAlbum iAlbum : albums) {
                arrayList.add(iAlbum.getName());
            }
            objArr[0] = TextUtils.join(",", arrayList);
            objArr[1] = Integer.valueOf(album.getAlbums().size());
            quantityString = resources.getString(R.string.album_permission_subtitle_single, objArr);
        } else {
            Resources resources2 = this.subAlbums.getContext().getResources();
            int size = album.getAlbums().size();
            Object[] objArr2 = new Object[2];
            List<IAlbum> albums2 = album.getAlbums();
            ArrayList arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(albums2, 10));
            for (IAlbum iAlbum2 : albums2) {
                arrayList2.add(iAlbum2.getName());
            }
            objArr2[0] = TextUtils.join(",", arrayList2);
            objArr2[1] = Integer.valueOf(album.getAlbums().size());
            quantityString = resources2.getQuantityString(R.plurals.album_permission_subtitle_multiple, size, objArr2);
        }
        textView.setText(quantityString);
        if (album.getGranted() || !album.getApplicable()) {
            TextView textView2 = this.grantText;
            textView2.setText(textView2.getContext().getString(R.string.album_permission_state_granted));
            this.arrow.setVisibility(8);
            this.grantButton.setOnClickListener(null);
            return;
        }
        TextView textView3 = this.grantText;
        textView3.setText(textView3.getContext().getString(R.string.album_permission_state_non_granted));
        this.arrow.setVisibility(0);
        this.grantButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.biz.albumpermission.AlbumPermissionItemViewHolder$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlbumPermissionItemViewHolder.m583$r8$lambda$0jTU1wvk1X4F4sE5HKEd00vGvQ(AlbumPermissionItemViewHolder.this, album, view);
            }
        });
    }

    /* renamed from: bind$lambda-4 */
    public static final void m584bind$lambda4(AlbumPermissionItemViewHolder this$0, PermissionAlbum album, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(album, "$album");
        FragmentActivity fragmentActivity = this$0.activityRef.get();
        if (fragmentActivity == null) {
            return;
        }
        String[] localPaths = album.getLocalPaths();
        int length = localPaths.length;
        int i = 0;
        while (i < length) {
            String str = localPaths[i];
            i++;
            StorageSolutionProvider.get().requestPermission(fragmentActivity, str, IStoragePermissionStrategy.Permission.INSERT, IStoragePermissionStrategy.Permission.DELETE, IStoragePermissionStrategy.Permission.UPDATE, IStoragePermissionStrategy.Permission.QUERY, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY);
        }
    }
}
