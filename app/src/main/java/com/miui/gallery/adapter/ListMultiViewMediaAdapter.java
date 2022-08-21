package com.miui.gallery.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.lifecycle.Lifecycle;
import com.miui.gallery.data.ClusteredList;
import com.miui.gallery.provider.cache.IMedia;
import com.miui.gallery.provider.cache.IMediaSnapshot;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.ISection;
import com.miui.gallery.provider.cache.MediaGroup;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.widget.recyclerview.AsyncListDiffer;
import java.util.ArrayList;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ListMultiViewMediaAdapter.kt */
/* loaded from: classes.dex */
public abstract class ListMultiViewMediaAdapter<T extends IMediaSnapshot> extends MultiViewMediaAdapter<IRecord, List<? extends IRecord>> implements IListAdapter<IRecord>, AsyncListDiffer.ListListener<IRecord> {
    public final Lazy adapterDelegate$delegate;

    /* renamed from: $r8$lambda$EWYiWftwz6J-TePZDlpdt01Js9I */
    public static /* synthetic */ void m505$r8$lambda$EWYiWftwz6JTePZDlpdt01Js9I(Function0 function0) {
        m506reselectViewMode$lambda3(function0);
    }

    /* renamed from: getMediaItem */
    public abstract T mo485getMediaItem(int i);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.adapter.GroupedMediaAdapter
    public List<IRecord> processBursts(List<? extends IRecord> list) {
        return list;
    }

    @Override // com.miui.gallery.adapter.IListAdapter
    public Object submitList(List<? extends IRecord> list, boolean z, Runnable runnable, Continuation<? super Unit> continuation) {
        return submitList$suspendImpl(this, list, z, runnable, continuation);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ListMultiViewMediaAdapter(Context context, SyncStateDisplay$DisplayScene scene, int i, Lifecycle lifecycle) {
        super(context, scene, i, lifecycle);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(scene, "scene");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        this.adapterDelegate$delegate = LazyKt__LazyJVMKt.lazy(LazyThreadSafetyMode.NONE, new ListMultiViewMediaAdapter$adapterDelegate$2(this));
    }

    public final ListAdapterDelegate getAdapterDelegate() {
        return (ListAdapterDelegate) this.adapterDelegate$delegate.mo119getValue();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ListMultiViewMediaAdapter(Context context, SyncStateDisplay$DisplayScene scene, Lifecycle lifecycle) {
        this(context, scene, 7, lifecycle);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(scene, "scene");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
    }

    public final ISection getGroupItem(int i) {
        return getAdapterDelegate().getGroup(i);
    }

    public final AsyncListDiffer<IRecord> getDiffer() {
        return getAdapterDelegate().getDiffer();
    }

    public final int getSyncState(IMediaSnapshot media) {
        Intrinsics.checkNotNullParameter(media, "media");
        return getSyncStateInternal(media.getSyncState());
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public String getOptimalThumbFilePath(int i, boolean z) {
        T mo485getMediaItem = mo485getMediaItem(i);
        if (z) {
            return BaseMediaAdapter.getMicroPath(mo485getMediaItem.getClearThumbnail(), mo485getMediaItem.getSha1());
        }
        return HomePageAdapter2.Companion.getDefaultThumbFilePath(mo485getMediaItem);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMicroThumbFilePath(int i) {
        T mo485getMediaItem = mo485getMediaItem(i);
        return BaseMediaAdapter.getMicroPath(mo485getMediaItem.getSmallSizeThumb(), mo485getMediaItem.getSha1());
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOriginFilePath(int i) {
        return mo485getMediaItem(i).getFilePath();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getThumbFilePath(int i) {
        return mo485getMediaItem(i).getThumbnail();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public Uri getDownloadUri(int i) {
        T mo485getMediaItem = mo485getMediaItem(i);
        return BaseMediaAdapter.getDownloadUri(mo485getMediaItem.getSyncState(), mo485getMediaItem.getId());
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getItemKey(int i) {
        return mo485getMediaItem(i).getId();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getSha1(int i) {
        return mo485getMediaItem(i).getSha1();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMimeType(int i) {
        return mo485getMediaItem(i).getMimeType();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getFileLength(int i) {
        return mo485getMediaItem(i).getSize();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public int getImageWidth(int i) {
        return mo485getMediaItem(i).getWidth();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public int getImageHeight(int i) {
        return mo485getMediaItem(i).getHeight();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getCreateTime(int i) {
        return mo485getMediaItem(i).getCreateTime();
    }

    public String getLocation(int i) {
        return mo485getMediaItem(i).getLocation();
    }

    public final ArrayList<Long> getBurstItemKeys(int i) {
        T mo485getMediaItem = mo485getMediaItem(i);
        if (mo485getMediaItem instanceof MediaGroup) {
            List<IMedia> medias = ((MediaGroup) mo485getMediaItem).getMedias();
            ArrayList<Long> arrayList = new ArrayList<>(medias.size());
            for (IMedia iMedia : medias) {
                arrayList.add(Long.valueOf(iMedia.getId()));
            }
            return arrayList;
        }
        return CollectionsKt__CollectionsKt.arrayListOf(Long.valueOf(mo485getMediaItem.getId()));
    }

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public long getGroupId(int i) {
        ISection groupItem;
        if (!this.mShowTimeLine || (groupItem = getGroupItem(i)) == null) {
            return 0L;
        }
        return groupItem.getId();
    }

    public static /* synthetic */ Object submitList$suspendImpl(ListMultiViewMediaAdapter listMultiViewMediaAdapter, List list, boolean z, Runnable runnable, Continuation continuation) {
        Object submitList = listMultiViewMediaAdapter.getAdapterDelegate().submitList(list, z, runnable, continuation);
        return submitList == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? submitList : Unit.INSTANCE;
    }

    public List<IRecord> getCurrentList() {
        return getAdapterDelegate().getCurrentList();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return getAdapterDelegate().getItemId(i);
    }

    @Override // com.miui.gallery.adapter.BaseRecyclerAdapter, com.miui.gallery.adapter.IBaseRecyclerAdapter
    /* renamed from: getItem */
    public IRecord mo1558getItem(int i) {
        return getAdapterDelegate().getItem(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return getAdapterDelegate().getItemCount();
    }

    @Override // com.miui.gallery.adapter.GroupedMediaAdapter
    public void processClusters(List<? extends IRecord> list, List<? extends IRecord> list2) {
        if (list != null) {
            this.mClusterAdapter.setViewMode(this.mViewMode);
            this.mClusterAdapter.swapData(list);
        }
        this.mHorizontalSpacing = this.mViewMode.getSpacing();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Object reselectViewMode$default(ListMultiViewMediaAdapter listMultiViewMediaAdapter, PictureViewMode pictureViewMode, boolean z, Function0 function0, Continuation continuation, int i, Object obj) {
        if (obj == null) {
            if ((i & 4) != 0) {
                function0 = ListMultiViewMediaAdapter$reselectViewMode$2.INSTANCE;
            }
            return listMultiViewMediaAdapter.reselectViewMode(pictureViewMode, z, function0, continuation);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: reselectViewMode");
    }

    public final Object reselectViewMode(PictureViewMode pictureViewMode, boolean z, final Function0<Unit> function0, Continuation<? super Unit> continuation) {
        this.mClusterAdapter.setViewMode(pictureViewMode);
        this.mHorizontalSpacing = pictureViewMode.getSpacing();
        this.mViewMode = pictureViewMode;
        int clusterKey = pictureViewMode.getClusterKey();
        List<IRecord> currentList = getAdapterDelegate().getCurrentList();
        boolean z2 = false;
        if (currentList instanceof ClusteredList) {
            ClusteredList clusteredList = (ClusteredList) currentList;
            if (!clusteredList.hasCluster(clusterKey)) {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Unrecognized cluster key: ", Boxing.boxInt(clusterKey)).toString());
            }
            Object submitList = getAdapterDelegate().submitList(clusteredList.reselect(clusterKey, z), false, new Runnable() { // from class: com.miui.gallery.adapter.ListMultiViewMediaAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ListMultiViewMediaAdapter.m505$r8$lambda$EWYiWftwz6JTePZDlpdt01Js9I(Function0.this);
                }
            }, continuation);
            return submitList == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? submitList : Unit.INSTANCE;
        }
        if (currentList == null || currentList.isEmpty()) {
            z2 = true;
        }
        if (!z2) {
            notifyDataSetChanged();
        }
        return Unit.INSTANCE;
    }

    /* renamed from: reselectViewMode$lambda-3 */
    public static final void m506reselectViewMode$lambda3(Function0 tmp0) {
        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
        tmp0.mo1738invoke();
    }
}
