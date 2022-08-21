package miuix.animation;

import miuix.animation.base.AnimConfig;

/* loaded from: classes3.dex */
public interface IVisibleStyle extends IStateContainer {

    /* loaded from: classes3.dex */
    public enum VisibleType {
        SHOW,
        HIDE
    }

    void hide(AnimConfig... animConfigArr);

    IVisibleStyle setAlpha(float f, VisibleType... visibleTypeArr);

    IVisibleStyle setHide();

    IVisibleStyle setScale(float f, VisibleType... visibleTypeArr);

    IVisibleStyle setShowDelay(long j);

    void show(AnimConfig... animConfigArr);
}
