package org.keyczar;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.keyczar.interfaces.Stream;

/* loaded from: classes3.dex */
class StreamQueue<T extends Stream> extends ConcurrentLinkedQueue<T> {
    private static final long serialVersionUID = 4914617278167817144L;
}
