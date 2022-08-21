package com.miui.gallery.biz.story;

import android.content.Context;
import android.graphics.RectF;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.miui.gallery.R;
import com.miui.gallery.adapter.BaseGalleryAdapter;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.card.core.LayoutParamsHelper;
import com.miui.gallery.card.ui.detail.StoryRecyclerViewItem;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.resource.bitmap.GalleryDownsampleStrategy;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.xiaomi.stat.a.j;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ObservableProperty;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* compiled from: StoryAlbumAdapter.kt */
/* loaded from: classes.dex */
public final class StoryAlbumAdapter extends BaseGalleryAdapter<MediaInfo, BaseViewHolder> {
    public static final /* synthetic */ KProperty<Object>[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(StoryAlbumAdapter.class, "data", "getData$app_cnRelease()Ljava/util/List;", 0))};
    public static final Companion Companion = new Companion(null);
    public static final String[] PROJECTION = {j.c, "microthumbfile", "thumbnailFile", "localFile", "mimeType", "alias_create_time", "location", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "exifImageWidth", "exifImageLength", "duration", "exifGPSLatitude", "exifGPSLatitudeRef", "exifGPSLongitude", "exifGPSLongitudeRef", "sha1", "alias_sync_state", "alias_clear_thumbnail", "alias_is_favorite", "exifOrientation", "serverType", "specialTypeFlags", "alias_micro_thumbnail"};
    public final ReadWriteProperty data$delegate;
    public final Lazy layoutParamsHelper$delegate;
    public final RequestOptions requestOptions;

    public final RectF getItemDecodeRectF(int i) {
        return null;
    }

    public StoryAlbumAdapter(Context context) {
        super(context);
        Delegates delegates = Delegates.INSTANCE;
        this.data$delegate = new ObservableProperty<List<? extends MediaInfo>>(CollectionsKt__CollectionsKt.emptyList(), this) { // from class: com.miui.gallery.biz.story.StoryAlbumAdapter$special$$inlined$observable$1
            public final /* synthetic */ Object $initialValue;
            public final /* synthetic */ StoryAlbumAdapter this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(r1);
                this.$initialValue = r1;
                this.this$0 = this;
            }

            @Override // kotlin.properties.ObservableProperty
            public void afterChange(KProperty<?> property, List<? extends MediaInfo> list, List<? extends MediaInfo> list2) {
                Intrinsics.checkNotNullParameter(property, "property");
                this.this$0.notifyDataSetChanged();
            }
        };
        this.layoutParamsHelper$delegate = LazyKt__LazyJVMKt.lazy(StoryAlbumAdapter$layoutParamsHelper$2.INSTANCE);
        GlideOptions autoClone = GlideOptions.downsampleOf(GalleryDownsampleStrategy.FIT_CENTER).mo958format(DecodeFormat.PREFER_RGB_565).mo972placeholder(R.drawable.image_default_cover).autoClone();
        Intrinsics.checkNotNullExpressionValue(autoClone, "downsampleOf(GalleryDown…ver)\n        .autoClone()");
        this.requestOptions = autoClone;
    }

    public final List<MediaInfo> getData$app_cnRelease() {
        return (List) this.data$delegate.getValue(this, $$delegatedProperties[0]);
    }

    public final void setData$app_cnRelease(List<MediaInfo> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.data$delegate.setValue(this, $$delegatedProperties[0], list);
    }

    public final LayoutParamsHelper getLayoutParamsHelper() {
        return (LayoutParamsHelper) this.layoutParamsHelper$delegate.mo119getValue();
    }

    public final void updateLayoutSizes(List<? extends LayoutParamsHelper.Size> list, int i, int i2) {
        getLayoutParamsHelper().updateLayoutSizes(list, i, i2);
        notifyDataSetChanged();
    }

    public final void updateLayoutParams(FlexboxLayoutManager.LayoutParams layoutParams, Size size) {
        if (size != null) {
            layoutParams.setHeight(size.getHeight());
            layoutParams.setWidth(size.getWidth());
        }
        layoutParams.setFlexGrow(1.0f);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(BaseViewHolder holder) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        super.onViewRecycled((StoryAlbumAdapter) holder);
        View view = holder.itemView;
        if (view instanceof MicroThumbGridItem) {
            ((MicroThumbGridItem) view).recycle();
        }
    }

    public final String getOptimalThumbFilePath(int i) {
        String thumbFilePath = getThumbFilePath(i);
        if (TextUtils.isEmpty(thumbFilePath)) {
            thumbFilePath = getOriginFilePath(i);
        }
        return TextUtils.isEmpty(thumbFilePath) ? getMicroThumbFilePath(i) : thumbFilePath;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkNotNullParameter(viewGroup, "viewGroup");
        BaseViewHolder create = BaseViewHolder.create(viewGroup, R.layout.story_album_item);
        Intrinsics.checkNotNullExpressionValue(create, "create(viewGroup, R.layout.story_album_item)");
        return create;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(BaseViewHolder holder, int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        StoryRecyclerViewItem storyRecyclerViewItem = (StoryRecyclerViewItem) holder.itemView;
        storyRecyclerViewItem.setTag(R.id.tag_item_unique_id, Long.valueOf(getItemId(i)));
        Size layoutSize = getLayoutParamsHelper().getLayoutSize(i);
        RequestOptions mo971override = layoutSize == null ? null : this.requestOptions.mo971override(layoutSize.getWidth(), layoutSize.getHeight());
        if (mo971override == null) {
            mo971override = this.requestOptions;
        }
        Intrinsics.checkNotNullExpressionValue(mo971override, "size?.run { requestOptio…ight) } ?: requestOptions");
        storyRecyclerViewItem.bindImage(getBindImagePath(i), getDownloadUri(i), DownloadType.THUMBNAIL, mo971override);
        storyRecyclerViewItem.bindIndicator(getMimeType(i), getDuration(i), getSpecialFlags(i));
        ViewGroup.LayoutParams layoutParams = storyRecyclerViewItem.getLayoutParams();
        if (layoutParams instanceof FlexboxLayoutManager.LayoutParams) {
            updateLayoutParams((FlexboxLayoutManager.LayoutParams) layoutParams, layoutSize);
        }
        storyRecyclerViewItem.setContentDescription(Intrinsics.stringPlus(storyRecyclerViewItem.getResources().getString(R.string.photo), Integer.valueOf(i + 1)));
        Folme.useAt(storyRecyclerViewItem).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(storyRecyclerViewItem, new AnimConfig[0]);
    }

    public final String getBindImagePath(int i) {
        if (IncompatibleMediaType.isUnsupportedMediaType(getMimeType(i)) && getDownloadUri(i) != null) {
            return getMicroThumbFilePath(i);
        }
        return getOptimalThumbFilePath(i);
    }

    public String getOriginFilePath(int i) {
        return getData$app_cnRelease().get(i).getFilePath();
    }

    public String getThumbFilePath(int i) {
        return getData$app_cnRelease().get(i).getThumbPath();
    }

    public String getMicroThumbFilePath(int i) {
        return Companion.getMicroPath(getData$app_cnRelease().get(i));
    }

    public Uri getDownloadUri(int i) {
        return Companion.getDownloadUri(getData$app_cnRelease().get(i).getSyncState(), getData$app_cnRelease().get(i).getId());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return getData$app_cnRelease().get(i).getId();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return getData$app_cnRelease().size();
    }

    public final String getMimeType(int i) {
        return getData$app_cnRelease().get(i).getMimeType();
    }

    public final long getDuration(int i) {
        return getData$app_cnRelease().get(i).getDuration();
    }

    public final long getSpecialFlags(int i) {
        return getData$app_cnRelease().get(i).getSpecialTypeFlags();
    }

    public final String getSha1(int i) {
        return getData$app_cnRelease().get(i).getSha1();
    }

    public long getFileLength(int i) {
        return getData$app_cnRelease().get(i).getSize();
    }

    @Override // com.miui.gallery.adapter.BaseRecyclerAdapter, com.miui.gallery.adapter.IBaseRecyclerAdapter
    /* renamed from: getItem */
    public MediaInfo mo1558getItem(int i) {
        return getData$app_cnRelease().get(i);
    }

    public final long getCreateTime(int i) {
        return getData$app_cnRelease().get(i).getCreateTime();
    }

    public final String getLocation(int i) {
        return getData$app_cnRelease().get(i).getLocation();
    }

    /* compiled from: StoryAlbumAdapter.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final String getMicroPath(MediaInfo mediaInfo) {
            Intrinsics.checkNotNullParameter(mediaInfo, "mediaInfo");
            String microPath = mediaInfo.getMicroPath();
            return (!TextUtils.isEmpty(microPath) || TextUtils.isEmpty(mediaInfo.getSha1())) ? microPath : CloudUtils.getMicroPath(mediaInfo.getSha1());
        }

        public final Uri getDownloadUri(int i, long j) {
            if (i == 0) {
                return getDownloadUri(j);
            }
            return null;
        }

        public final Uri getDownloadUri(long j) {
            Uri downloadUri = CloudUriAdapter.getDownloadUri(j);
            Intrinsics.checkNotNullExpressionValue(downloadUri, "getDownloadUri(id)");
            return downloadUri;
        }
    }
}
