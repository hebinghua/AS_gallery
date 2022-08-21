package miuix.animation;

import android.view.View;
import miuix.animation.base.AnimConfig;

/* loaded from: classes3.dex */
public interface ITouchStyle extends IStateContainer {

    /* loaded from: classes3.dex */
    public enum TouchType {
        UP,
        DOWN
    }

    void handleTouchOf(View view, boolean z, AnimConfig... animConfigArr);

    void handleTouchOf(View view, AnimConfig... animConfigArr);

    ITouchStyle setAlpha(float f, TouchType... touchTypeArr);

    ITouchStyle setBackgroundColor(float f, float f2, float f3, float f4);

    ITouchStyle setScale(float f, TouchType... touchTypeArr);

    ITouchStyle setTint(float f, float f2, float f3, float f4);

    ITouchStyle setTint(int i);

    ITouchStyle setTintMode(int i);

    void setTouchUp();

    void touchDown(AnimConfig... animConfigArr);

    void touchUp(AnimConfig... animConfigArr);
}
