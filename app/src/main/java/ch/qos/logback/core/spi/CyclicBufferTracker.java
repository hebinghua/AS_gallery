package ch.qos.logback.core.spi;

import ch.qos.logback.core.helpers.CyclicBuffer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CyclicBufferTracker<E> extends AbstractComponentTracker<CyclicBuffer<E>> {
    public static final int DEFAULT_BUFFER_SIZE = 256;
    public static final int DEFAULT_NUMBER_OF_BUFFERS = 64;
    public int bufferSize = 256;

    public boolean isComponentStale(CyclicBuffer<E> cyclicBuffer) {
        return false;
    }

    @Override // ch.qos.logback.core.spi.AbstractComponentTracker
    public /* bridge */ /* synthetic */ boolean isComponentStale(Object obj) {
        return isComponentStale((CyclicBuffer) ((CyclicBuffer) obj));
    }

    @Override // ch.qos.logback.core.spi.AbstractComponentTracker
    public /* bridge */ /* synthetic */ void processPriorToRemoval(Object obj) {
        processPriorToRemoval((CyclicBuffer) ((CyclicBuffer) obj));
    }

    public CyclicBufferTracker() {
        setMaxComponents(64);
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setBufferSize(int i) {
        this.bufferSize = i;
    }

    public void processPriorToRemoval(CyclicBuffer<E> cyclicBuffer) {
        cyclicBuffer.clear();
    }

    @Override // ch.qos.logback.core.spi.AbstractComponentTracker
    /* renamed from: buildComponent */
    public CyclicBuffer<E> mo170buildComponent(String str) {
        return new CyclicBuffer<>(this.bufferSize);
    }

    public List<String> liveKeysAsOrderedList() {
        return new ArrayList(this.liveMap.keySet());
    }

    public List<String> lingererKeysAsOrderedList() {
        return new ArrayList(this.lingerersMap.keySet());
    }
}
