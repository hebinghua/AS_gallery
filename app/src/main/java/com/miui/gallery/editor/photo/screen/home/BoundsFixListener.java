package com.miui.gallery.editor.photo.screen.home;

import android.animation.Animator;
import com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener;

/* loaded from: classes2.dex */
public class BoundsFixListener extends OneShotAnimateListener {
    public BoundsFixCallback mCallback;

    public BoundsFixListener(BoundsFixCallback boundsFixCallback) {
        this.mCallback = boundsFixCallback;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        super.onAnimationEnd(animator);
        this.mCallback.onDone(false);
    }
}
