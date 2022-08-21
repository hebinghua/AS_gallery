package com.miui.gallery.ui.album.main.component.multichoice;

import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.ui.album.common.MultiChoiceModeDataProvider;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.AlbumTabContract$P;
import com.miui.gallery.ui.album.main.AlbumTabContract$V;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class AlbumTabMultiChoiceModeDataProvider<P extends AlbumTabContract$P> implements MultiChoiceModeDataProvider {
    public WeakReference<AlbumTabContract$V<P>> mView;

    public AlbumTabMultiChoiceModeDataProvider(AlbumTabContract$V<P> albumTabContract$V) {
        this.mView = new WeakReference<>(albumTabContract$V);
    }

    public final AlbumTabContract$V<P> getView() {
        return this.mView.get();
    }

    @Override // com.miui.gallery.ui.album.common.MultiChoiceModeDataProvider
    public int providerCheckedCount() {
        if (getView() == null) {
            return 0;
        }
        return getView().getCheckedCount();
    }

    @Override // com.miui.gallery.ui.album.common.MultiChoiceModeDataProvider
    public Collection<Album> providerCurrentOperationAlbums() {
        AlbumTabContract$V<P> view = getView();
        if (view == null) {
            return CollectionUtils.emptyList();
        }
        final int length = view.getCheckedItemIds().length;
        if (length != 0) {
            return (Collection) Arrays.stream(getView().getCheckedItemOrderedPositions()).mapToObj(new IntFunction<Album>() { // from class: com.miui.gallery.ui.album.main.component.multichoice.AlbumTabMultiChoiceModeDataProvider.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.function.IntFunction
                /* renamed from: apply */
                public Album mo1595apply(int i) {
                    BaseViewBean data = AlbumTabMultiChoiceModeDataProvider.this.getView().getData(i);
                    if (data == null) {
                        return null;
                    }
                    BaseViewBean baseViewBean = data;
                    Object source = baseViewBean.getSource();
                    if ((source instanceof Album) && AlbumConstants.isAlbumCheckable((int) baseViewBean.getId())) {
                        return (Album) source;
                    }
                    return null;
                }
            }).filter(new Predicate<Album>() { // from class: com.miui.gallery.ui.album.main.component.multichoice.AlbumTabMultiChoiceModeDataProvider.1
                @Override // java.util.function.Predicate
                public boolean test(Album album) {
                    return album != null;
                }
            }).collect(Collectors.toCollection(new Supplier<Collection<Album>>() { // from class: com.miui.gallery.ui.album.main.component.multichoice.AlbumTabMultiChoiceModeDataProvider.3
                @Override // java.util.function.Supplier
                public Collection<Album> get() {
                    return new ArrayList(length);
                }
            }));
        }
        return null;
    }
}
