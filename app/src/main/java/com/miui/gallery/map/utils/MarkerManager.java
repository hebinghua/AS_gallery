package com.miui.gallery.map.utils;

import com.miui.gallery.map.view.IMapContainer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public class MarkerManager implements OnMarkerClickListener {
    public final IMapContainer mMap;
    public final Lock mWriteLock = new ReentrantLock();
    public final Map<IMarker, Collection> mAllMarkers = new HashMap();

    public MarkerManager(IMapContainer iMapContainer) {
        this.mMap = iMapContainer;
    }

    public boolean remove(IMarker iMarker) {
        boolean z;
        this.mWriteLock.lock();
        try {
            Collection collection = this.mAllMarkers.get(iMarker);
            if (collection != null) {
                if (collection.remove(iMarker)) {
                    z = true;
                    return z;
                }
            }
            z = false;
            return z;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public Collection newCollection() {
        return new Collection();
    }

    @Override // com.miui.gallery.map.utils.OnMarkerClickListener
    public boolean onMarkerClick(IMarker iMarker) {
        Collection collection = this.mAllMarkers.get(iMarker);
        if (collection == null || collection.mMarkerClickListener == null) {
            return false;
        }
        return collection.mMarkerClickListener.onMarkerClick(iMarker);
    }

    /* loaded from: classes2.dex */
    public class Collection {
        public OnMarkerClickListener mMarkerClickListener;
        public final Set<IMarker> mMarkers = new HashSet();

        public Collection() {
        }

        public IMarker addMarker(IMarkerOptions iMarkerOptions) {
            MarkerManager.this.mWriteLock.lock();
            try {
                IMarker addOverlay = MarkerManager.this.mMap.addOverlay(iMarkerOptions);
                this.mMarkers.add(addOverlay);
                MarkerManager.this.mAllMarkers.put(addOverlay, this);
                return addOverlay;
            } finally {
                MarkerManager.this.mWriteLock.unlock();
            }
        }

        public boolean remove(IMarker iMarker) {
            if (this.mMarkers.remove(iMarker)) {
                MarkerManager.this.mAllMarkers.remove(iMarker);
                iMarker.remove();
                return true;
            }
            return false;
        }

        public void clear() {
            for (IMarker iMarker : this.mMarkers) {
                iMarker.remove();
                MarkerManager.this.mAllMarkers.remove(iMarker);
            }
            this.mMarkers.clear();
        }

        public void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
            this.mMarkerClickListener = onMarkerClickListener;
        }
    }
}
