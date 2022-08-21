package miuix.animation.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.ObjectPool;

/* loaded from: classes3.dex */
public class ListenerNotifier {
    public final Map<Object, List<TransitionListener>> mListenerMap = new ConcurrentHashMap();
    public final IAnimTarget mTarget;
    public static final BeginNotifier sBegin = new BeginNotifier();
    public static final PropertyBeginNotifier sPropertyBegin = new PropertyBeginNotifier();
    public static final MassUpdateNotifier sMassUpdate = new MassUpdateNotifier();
    public static final UpdateNotifier sUpdate = new UpdateNotifier();
    public static final PropertyEndNotifier sPropertyEnd = new PropertyEndNotifier();
    public static final CancelNotifier sCancelAll = new CancelNotifier();
    public static final EndNotifier sEndAll = new EndNotifier();

    /* loaded from: classes3.dex */
    public interface INotifier {
        void doNotify(Object obj, TransitionListener transitionListener, Collection<UpdateInfo> collection, UpdateInfo updateInfo);
    }

    /* loaded from: classes3.dex */
    public static class BeginNotifier implements INotifier {
        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection<UpdateInfo> collection, UpdateInfo updateInfo) {
            transitionListener.onBegin(obj);
        }
    }

    /* loaded from: classes3.dex */
    public static class PropertyBeginNotifier implements INotifier {
        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection<UpdateInfo> collection, UpdateInfo updateInfo) {
            transitionListener.onBegin(obj, collection);
        }
    }

    /* loaded from: classes3.dex */
    public static class MassUpdateNotifier implements INotifier {
        public static final List<UpdateInfo> sEmptyList = new ArrayList();

        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection<UpdateInfo> collection, UpdateInfo updateInfo) {
            transitionListener.onUpdate(obj, sEmptyList);
        }
    }

    /* loaded from: classes3.dex */
    public static class UpdateNotifier implements INotifier {
        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection<UpdateInfo> collection, UpdateInfo updateInfo) {
            if (collection != null && collection.size() <= 4000) {
                for (UpdateInfo updateInfo2 : collection) {
                    notifySingleProperty(obj, transitionListener, updateInfo2);
                }
            }
            transitionListener.onUpdate(obj, collection);
        }

        public final void notifySingleProperty(Object obj, TransitionListener transitionListener, UpdateInfo updateInfo) {
            transitionListener.onUpdate(obj, updateInfo.property, updateInfo.getFloatValue(), updateInfo.isCompleted);
            if (updateInfo.useInt) {
                transitionListener.onUpdate(obj, (IIntValueProperty) updateInfo.property, updateInfo.getIntValue(), (float) updateInfo.velocity, updateInfo.isCompleted);
            } else {
                transitionListener.onUpdate(obj, updateInfo.property, updateInfo.getFloatValue(), (float) updateInfo.velocity, updateInfo.isCompleted);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class PropertyEndNotifier implements INotifier {
        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection<UpdateInfo> collection, UpdateInfo updateInfo) {
            for (UpdateInfo updateInfo2 : collection) {
                if (updateInfo2.isCompleted && updateInfo2.animInfo.justEnd) {
                    updateInfo2.animInfo.justEnd = false;
                    if (updateInfo2.animInfo.op == 3) {
                        transitionListener.onComplete(obj, updateInfo2);
                    } else {
                        transitionListener.onCancel(obj, updateInfo2);
                    }
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class CancelNotifier implements INotifier {
        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection<UpdateInfo> collection, UpdateInfo updateInfo) {
            transitionListener.onCancel(obj);
        }
    }

    /* loaded from: classes3.dex */
    public static class EndNotifier implements INotifier {
        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection<UpdateInfo> collection, UpdateInfo updateInfo) {
            transitionListener.onComplete(obj);
        }
    }

    public ListenerNotifier(IAnimTarget iAnimTarget) {
        this.mTarget = iAnimTarget;
    }

    public boolean addListeners(Object obj, AnimConfig animConfig) {
        if (animConfig.listeners.isEmpty()) {
            return false;
        }
        CommonUtils.addTo(animConfig.listeners, getListenerSet(obj));
        return true;
    }

    public void removeListeners(Object obj) {
        ObjectPool.release(this.mListenerMap.remove(obj));
    }

    public final List<TransitionListener> getListenerSet(Object obj) {
        List<TransitionListener> list = this.mListenerMap.get(obj);
        if (list == null) {
            List<TransitionListener> list2 = (List) ObjectPool.acquire(ArrayList.class, new Object[0]);
            this.mListenerMap.put(obj, list2);
            return list2;
        }
        return list;
    }

    public void notifyBegin(Object obj, Object obj2) {
        notify(obj, obj2, sBegin, null, null);
    }

    public void notifyPropertyBegin(Object obj, Object obj2, Collection<UpdateInfo> collection) {
        notify(obj, obj2, sPropertyBegin, collection, null);
    }

    public void notifyMassUpdate(Object obj, Object obj2) {
        notify(obj, obj2, sMassUpdate, null, null);
    }

    public void notifyUpdate(Object obj, Object obj2, Collection<UpdateInfo> collection) {
        notify(obj, obj2, sUpdate, collection, null);
    }

    public void notifyPropertyEnd(Object obj, Object obj2, Collection<UpdateInfo> collection) {
        notify(obj, obj2, sPropertyEnd, collection, null);
    }

    public void notifyCancelAll(Object obj, Object obj2) {
        notify(obj, obj2, sCancelAll, null, null);
    }

    public void notifyEndAll(Object obj, Object obj2) {
        notify(obj, obj2, sEndAll, null, null);
    }

    public final void notify(Object obj, Object obj2, INotifier iNotifier, Collection<UpdateInfo> collection, UpdateInfo updateInfo) {
        List<TransitionListener> list = this.mListenerMap.get(obj);
        if (list == null || list.isEmpty()) {
            return;
        }
        notifyListenerSet(obj2, list, iNotifier, collection, updateInfo);
    }

    public static void notifyListenerSet(Object obj, List<TransitionListener> list, INotifier iNotifier, Collection<UpdateInfo> collection, UpdateInfo updateInfo) {
        Set set = (Set) ObjectPool.acquire(HashSet.class, new Object[0]);
        for (TransitionListener transitionListener : list) {
            if (set.add(transitionListener)) {
                iNotifier.doNotify(obj, transitionListener, collection, updateInfo);
            }
        }
        ObjectPool.release(set);
    }
}
