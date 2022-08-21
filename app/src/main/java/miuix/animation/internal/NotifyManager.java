package miuix.animation.internal;

import java.util.Collection;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.ListenerNotifier;
import miuix.animation.listener.UpdateInfo;

/* loaded from: classes3.dex */
public class NotifyManager {
    public AnimConfig mConfig = new AnimConfig();
    public ListenerNotifier mNotifier;
    public ListenerNotifier mSetToNotifier;
    public IAnimTarget mTarget;

    public NotifyManager(IAnimTarget iAnimTarget) {
        this.mTarget = iAnimTarget;
        this.mSetToNotifier = new ListenerNotifier(iAnimTarget);
        this.mNotifier = new ListenerNotifier(iAnimTarget);
    }

    public void setToNotify(AnimState animState, AnimConfigLink animConfigLink) {
        if (animConfigLink == null) {
            return;
        }
        Object tag = animState.getTag();
        this.mConfig.copy(animState.getConfig());
        animConfigLink.addTo(this.mConfig);
        if (!this.mSetToNotifier.addListeners(tag, this.mConfig)) {
            this.mConfig.clear();
            return;
        }
        this.mSetToNotifier.notifyBegin(tag, tag);
        Collection<UpdateInfo> values = this.mTarget.animManager.mUpdateMap.values();
        this.mSetToNotifier.notifyPropertyBegin(tag, tag, values);
        this.mSetToNotifier.notifyUpdate(tag, tag, values);
        this.mSetToNotifier.notifyPropertyEnd(tag, tag, values);
        this.mSetToNotifier.notifyEndAll(tag, tag);
        this.mSetToNotifier.removeListeners(tag);
        this.mConfig.clear();
    }

    public ListenerNotifier getNotifier() {
        return this.mNotifier;
    }
}
