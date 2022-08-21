package com.miui.gallery.data;

import android.util.SparseArray;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.UnaryOperator;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* compiled from: ClusteredList.kt */
/* loaded from: classes.dex */
public final class ClusteredList<ITEM> implements List<ITEM>, KMappedMarker {
    public static final Companion Companion = new Companion(null);
    public final SparseArray<Cluster> clusters;
    public final List<ITEM> contents;
    public List<? extends ITEM> delegate;
    public int delegatedClusterKey;
    public boolean isFlatten;
    public final SparseArray<List<ITEM>> sections;

    @Override // java.util.List
    public void add(int i, ITEM item) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(ITEM item) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public boolean addAll(int i, Collection<? extends ITEM> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends ITEM> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public ITEM remove(int i) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public void replaceAll(UnaryOperator<ITEM> unaryOperator) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public ITEM set(int i, ITEM item) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public void sort(Comparator<? super ITEM> comparator) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] array) {
        Intrinsics.checkNotNullParameter(array, "array");
        return (T[]) CollectionToArray.toArray(this, array);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ClusteredList(List<? extends ITEM> contents, SparseArray<List<ITEM>> sections, SparseArray<Cluster> clusters, boolean z, int i) {
        Intrinsics.checkNotNullParameter(contents, "contents");
        Intrinsics.checkNotNullParameter(sections, "sections");
        Intrinsics.checkNotNullParameter(clusters, "clusters");
        this.contents = contents;
        this.sections = sections;
        this.clusters = clusters;
        this.isFlatten = z;
        this.delegatedClusterKey = i;
        if (i >= 0) {
            List<ITEM> list = sections.get(this.delegatedClusterKey);
            Intrinsics.checkNotNullExpressionValue(list, "sections[delegatedClusterKey]");
            Cluster cluster = clusters.get(this.delegatedClusterKey);
            Intrinsics.checkNotNullExpressionValue(cluster, "clusters[delegatedClusterKey]");
            this.delegate = new SectionedList(contents, list, cluster, this.isFlatten);
        }
    }

    public /* synthetic */ ClusteredList(List list, SparseArray sparseArray, SparseArray sparseArray2, boolean z, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(list, sparseArray, sparseArray2, z, (i2 & 16) != 0 ? -1 : i);
    }

    @Override // java.util.List, java.util.Collection
    public final /* bridge */ int size() {
        return getSize();
    }

    public final boolean hasCluster(int i) {
        return this.clusters.indexOfKey(i) >= 0;
    }

    public final Cluster clusterOf(int i) {
        Cluster cluster = this.clusters.get(i);
        Intrinsics.checkNotNullExpressionValue(cluster, "clusters[key]");
        return cluster;
    }

    public final ITEM rawGetContent(int i) {
        return this.contents.get(i);
    }

    public final ITEM rawGetSection(int i) {
        return this.sections.get(this.delegatedClusterKey).get(i);
    }

    public final int getContentCount() {
        return this.contents.size();
    }

    public final void select(int i, boolean z) {
        if (!(this.clusters.indexOfKey(i) >= 0)) {
            throw new IllegalStateException(Intrinsics.stringPlus("Illegal cluster key ", Integer.valueOf(i)).toString());
        }
        if (this.delegatedClusterKey == i && this.isFlatten == z) {
            DefaultLogger.w("ClusteredList", "Nothing changed, clusterKey: " + i + ", isFlatten: " + z + " - " + hashCode());
            return;
        }
        DefaultLogger.w("ClusteredList", "Reselect, clusterKey: " + i + ", isFlatten: " + z + " - " + hashCode());
        this.delegatedClusterKey = i;
        this.isFlatten = z;
        List<ITEM> list = this.contents;
        List<ITEM> list2 = this.sections.get(i);
        Intrinsics.checkNotNullExpressionValue(list2, "sections[delegatedClusterKey]");
        Cluster cluster = this.clusters.get(i);
        Intrinsics.checkNotNullExpressionValue(cluster, "clusters[delegatedClusterKey]");
        this.delegate = new SectionedList(list, list2, cluster, z);
    }

    public final ClusteredList<ITEM> reselect(int i, boolean z) {
        return new ClusteredList<>(this.contents, this.sections, this.clusters, z, i);
    }

    public int getSize() {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return list.size();
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object obj) {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return list.contains(obj);
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<? extends Object> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return list.containsAll(elements);
    }

    @Override // java.util.List
    public ITEM get(int i) {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return list.get(i);
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return list.indexOf(obj);
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return list.isEmpty();
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator<ITEM> iterator() {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return (Iterator<? extends ITEM>) list.iterator();
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return list.lastIndexOf(obj);
    }

    @Override // java.util.List
    public ListIterator<ITEM> listIterator() {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return (ListIterator<? extends ITEM>) list.listIterator();
    }

    @Override // java.util.List
    public ListIterator<ITEM> listIterator(int i) {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return (ListIterator<? extends ITEM>) list.listIterator(i);
    }

    @Override // java.util.List
    public List<ITEM> subList(int i, int i2) {
        List<? extends ITEM> list = this.delegate;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("delegate");
            list = null;
        }
        return (List<? extends ITEM>) list.subList(i, i2);
    }

    /* compiled from: ClusteredList.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
