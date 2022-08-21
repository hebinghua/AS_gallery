package ch.qos.logback.core.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public abstract class AbstractComponentTracker<C> implements ComponentTracker<C> {
    private static final boolean ACCESS_ORDERED = true;
    public static final long LINGERING_TIMEOUT = 10000;
    public static final long WAIT_BETWEEN_SUCCESSIVE_REMOVAL_ITERATIONS = 1000;
    public int maxComponents = Integer.MAX_VALUE;
    public long timeout = ComponentTracker.DEFAULT_TIMEOUT;
    public LinkedHashMap<String, Entry<C>> liveMap = new LinkedHashMap<>(32, 0.75f, true);
    public LinkedHashMap<String, Entry<C>> lingerersMap = new LinkedHashMap<>(16, 0.75f, true);
    public long lastCheck = 0;
    private RemovalPredicator<C> byExcedent = new RemovalPredicator<C>() { // from class: ch.qos.logback.core.spi.AbstractComponentTracker.1
        @Override // ch.qos.logback.core.spi.AbstractComponentTracker.RemovalPredicator
        public boolean isSlatedForRemoval(Entry<C> entry, long j) {
            return AbstractComponentTracker.this.liveMap.size() > AbstractComponentTracker.this.maxComponents;
        }
    };
    private RemovalPredicator<C> byTimeout = new RemovalPredicator<C>() { // from class: ch.qos.logback.core.spi.AbstractComponentTracker.2
        @Override // ch.qos.logback.core.spi.AbstractComponentTracker.RemovalPredicator
        public boolean isSlatedForRemoval(Entry<C> entry, long j) {
            return AbstractComponentTracker.this.isEntryStale(entry, j);
        }
    };
    private RemovalPredicator<C> byLingering = new RemovalPredicator<C>() { // from class: ch.qos.logback.core.spi.AbstractComponentTracker.3
        @Override // ch.qos.logback.core.spi.AbstractComponentTracker.RemovalPredicator
        public boolean isSlatedForRemoval(Entry<C> entry, long j) {
            return AbstractComponentTracker.this.isEntryDoneLingering(entry, j);
        }
    };

    /* loaded from: classes.dex */
    public interface RemovalPredicator<C> {
        boolean isSlatedForRemoval(Entry<C> entry, long j);
    }

    /* renamed from: buildComponent */
    public abstract C mo170buildComponent(String str);

    public abstract boolean isComponentStale(C c);

    public abstract void processPriorToRemoval(C c);

    @Override // ch.qos.logback.core.spi.ComponentTracker
    public int getComponentCount() {
        return this.liveMap.size() + this.lingerersMap.size();
    }

    private Entry<C> getFromEitherMap(String str) {
        Entry<C> entry = this.liveMap.get(str);
        return entry != null ? entry : this.lingerersMap.get(str);
    }

    @Override // ch.qos.logback.core.spi.ComponentTracker
    public synchronized C find(String str) {
        Entry<C> fromEitherMap = getFromEitherMap(str);
        if (fromEitherMap == null) {
            return null;
        }
        return fromEitherMap.component;
    }

    @Override // ch.qos.logback.core.spi.ComponentTracker
    public synchronized C getOrCreate(String str, long j) {
        Entry<C> fromEitherMap;
        fromEitherMap = getFromEitherMap(str);
        if (fromEitherMap == null) {
            Entry<C> entry = new Entry<>(str, mo170buildComponent(str), j);
            this.liveMap.put(str, entry);
            fromEitherMap = entry;
        } else {
            fromEitherMap.setTimestamp(j);
        }
        return fromEitherMap.component;
    }

    @Override // ch.qos.logback.core.spi.ComponentTracker
    public void endOfLife(String str) {
        Entry<C> remove = this.liveMap.remove(str);
        if (remove == null) {
            return;
        }
        this.lingerersMap.put(str, remove);
    }

    @Override // ch.qos.logback.core.spi.ComponentTracker
    public synchronized void removeStaleComponents(long j) {
        if (isTooSoonForRemovalIteration(j)) {
            return;
        }
        removeExcedentComponents();
        removeStaleComponentsFromMainMap(j);
        removeStaleComponentsFromLingerersMap(j);
    }

    private void removeExcedentComponents() {
        genericStaleComponentRemover(this.liveMap, 0L, this.byExcedent);
    }

    private void removeStaleComponentsFromMainMap(long j) {
        genericStaleComponentRemover(this.liveMap, j, this.byTimeout);
    }

    private void removeStaleComponentsFromLingerersMap(long j) {
        genericStaleComponentRemover(this.lingerersMap, j, this.byLingering);
    }

    private void genericStaleComponentRemover(LinkedHashMap<String, Entry<C>> linkedHashMap, long j, RemovalPredicator<C> removalPredicator) {
        Iterator<Map.Entry<String, Entry<C>>> it = linkedHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<C> value = it.next().getValue();
            if (!removalPredicator.isSlatedForRemoval(value, j)) {
                return;
            }
            it.remove();
            processPriorToRemoval(value.component);
        }
    }

    private boolean isTooSoonForRemovalIteration(long j) {
        if (this.lastCheck + 1000 > j) {
            return true;
        }
        this.lastCheck = j;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEntryStale(Entry<C> entry, long j) {
        return isComponentStale(entry.component) || entry.timestamp + this.timeout < j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEntryDoneLingering(Entry<C> entry, long j) {
        return entry.timestamp + LINGERING_TIMEOUT < j;
    }

    @Override // ch.qos.logback.core.spi.ComponentTracker
    public Set<String> allKeys() {
        HashSet hashSet = new HashSet(this.liveMap.keySet());
        hashSet.addAll(this.lingerersMap.keySet());
        return hashSet;
    }

    @Override // ch.qos.logback.core.spi.ComponentTracker
    public Collection<C> allComponents() {
        ArrayList arrayList = new ArrayList();
        for (Entry<C> entry : this.liveMap.values()) {
            arrayList.add(entry.component);
        }
        for (Entry<C> entry2 : this.lingerersMap.values()) {
            arrayList.add(entry2.component);
        }
        return arrayList;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long j) {
        this.timeout = j;
    }

    public int getMaxComponents() {
        return this.maxComponents;
    }

    public void setMaxComponents(int i) {
        this.maxComponents = i;
    }

    /* loaded from: classes.dex */
    public static class Entry<C> {
        public C component;
        public String key;
        public long timestamp;

        public Entry(String str, C c, long j) {
            this.key = str;
            this.component = c;
            this.timestamp = j;
        }

        public void setTimestamp(long j) {
            this.timestamp = j;
        }

        public int hashCode() {
            return this.key.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Entry entry = (Entry) obj;
            String str = this.key;
            if (str == null) {
                if (entry.key != null) {
                    return false;
                }
            } else if (!str.equals(entry.key)) {
                return false;
            }
            C c = this.component;
            if (c == null) {
                if (entry.component != null) {
                    return false;
                }
            } else if (!c.equals(entry.component)) {
                return false;
            }
            return true;
        }

        public String toString() {
            return "(" + this.key + ", " + this.component + ")";
        }
    }
}
