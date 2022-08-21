package com.miui.gallery.map.quadtree;

import com.miui.gallery.map.projection.Bounds;
import com.miui.gallery.map.projection.Point;
import com.miui.gallery.map.quadtree.PointQuadTree.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public class PointQuadTree<T extends Item> {
    public final Bounds mBounds;
    public List<PointQuadTree<T>> mChildren;
    public final int mDepth;
    public List<T> mItems;

    /* loaded from: classes2.dex */
    public interface Item {
        Point getPoint();
    }

    public PointQuadTree(double d, double d2, double d3, double d4) {
        this(new Bounds(d, d2, d3, d4));
    }

    public PointQuadTree(Bounds bounds) {
        this(bounds, 0);
    }

    public PointQuadTree(double d, double d2, double d3, double d4, int i) {
        this(new Bounds(d, d2, d3, d4), i);
    }

    public PointQuadTree(Bounds bounds, int i) {
        this.mChildren = null;
        this.mBounds = bounds;
        this.mDepth = i;
    }

    public void add(T t) {
        Point point = t.getPoint();
        if (this.mBounds.contains(point.x, point.y)) {
            insert(point.x, point.y, t);
        }
    }

    public final void insert(double d, double d2, T t) {
        List<PointQuadTree<T>> list = this.mChildren;
        if (list != null) {
            Bounds bounds = this.mBounds;
            if (d2 < bounds.midY) {
                if (d < bounds.midX) {
                    list.get(0).insert(d, d2, t);
                    return;
                } else {
                    list.get(1).insert(d, d2, t);
                    return;
                }
            } else if (d < bounds.midX) {
                list.get(2).insert(d, d2, t);
                return;
            } else {
                list.get(3).insert(d, d2, t);
                return;
            }
        }
        if (this.mItems == null) {
            this.mItems = new ArrayList();
        }
        this.mItems.add(t);
        if (this.mItems.size() <= 50 || this.mDepth >= 40) {
            return;
        }
        split();
    }

    public final void split() {
        ArrayList arrayList = new ArrayList(4);
        this.mChildren = arrayList;
        Bounds bounds = this.mBounds;
        arrayList.add(new PointQuadTree(bounds.minX, bounds.midX, bounds.minY, bounds.midY, this.mDepth + 1));
        List<PointQuadTree<T>> list = this.mChildren;
        Bounds bounds2 = this.mBounds;
        list.add(new PointQuadTree<>(bounds2.midX, bounds2.maxX, bounds2.minY, bounds2.midY, this.mDepth + 1));
        List<PointQuadTree<T>> list2 = this.mChildren;
        Bounds bounds3 = this.mBounds;
        list2.add(new PointQuadTree<>(bounds3.minX, bounds3.midX, bounds3.midY, bounds3.maxY, this.mDepth + 1));
        List<PointQuadTree<T>> list3 = this.mChildren;
        Bounds bounds4 = this.mBounds;
        list3.add(new PointQuadTree<>(bounds4.midX, bounds4.maxX, bounds4.midY, bounds4.maxY, this.mDepth + 1));
        List<T> list4 = this.mItems;
        this.mItems = null;
        for (T t : list4) {
            insert(t.getPoint().x, t.getPoint().y, t);
        }
    }

    public void clear() {
        this.mChildren = null;
        List<T> list = this.mItems;
        if (list != null) {
            list.clear();
        }
    }

    public Collection<T> search(Bounds bounds) {
        ArrayList arrayList = new ArrayList();
        search(bounds, arrayList);
        return arrayList;
    }

    public final void search(Bounds bounds, Collection<T> collection) {
        if (!this.mBounds.intersects(bounds)) {
            return;
        }
        List<PointQuadTree<T>> list = this.mChildren;
        if (list != null) {
            for (PointQuadTree<T> pointQuadTree : list) {
                pointQuadTree.search(bounds, collection);
            }
        } else if (this.mItems != null) {
            if (bounds.contains(this.mBounds)) {
                collection.addAll(this.mItems);
                return;
            }
            for (T t : this.mItems) {
                if (bounds.contains(t.getPoint())) {
                    collection.add(t);
                }
            }
        }
    }
}
