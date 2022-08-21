package com.miui.gallery.editor.utils;

import java.util.Collection;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;

/* loaded from: classes2.dex */
public class SpringAnimationUtils {
    public static void setFractionAnimationWithSpring(SpringTransitionListener springTransitionListener, AnimConfig animConfig) {
        if (springTransitionListener == null) {
            return;
        }
        Folme.useValue(new Object[0]).addListener(springTransitionListener).setTo("progress", Float.valueOf(0.0f)).to("progress", Float.valueOf(1.0f), animConfig);
    }

    /* loaded from: classes2.dex */
    public static abstract class SpringTransitionListener extends TransitionListener {
        public abstract void onUpdate(float f);

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
            super.onUpdate(obj, collection);
            onUpdate(UpdateInfo.findByName(collection, "progress").getFloatValue());
        }
    }
}
