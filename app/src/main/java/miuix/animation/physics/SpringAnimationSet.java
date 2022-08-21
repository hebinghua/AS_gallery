package miuix.animation.physics;

import java.util.LinkedList;
import java.util.List;

/* loaded from: classes3.dex */
public class SpringAnimationSet {
    public List<SpringAnimation> mAnimationContainer = new LinkedList();

    public void start() {
        if (!this.mAnimationContainer.isEmpty()) {
            for (SpringAnimation springAnimation : this.mAnimationContainer) {
                if (springAnimation != null) {
                    springAnimation.start();
                }
            }
        }
    }

    public void cancel() {
        if (!this.mAnimationContainer.isEmpty()) {
            for (SpringAnimation springAnimation : this.mAnimationContainer) {
                if (springAnimation != null) {
                    springAnimation.cancel();
                }
            }
            this.mAnimationContainer.clear();
        }
    }

    public void endAnimation() {
        if (!this.mAnimationContainer.isEmpty()) {
            for (SpringAnimation springAnimation : this.mAnimationContainer) {
                if (springAnimation != null) {
                    springAnimation.skipToEnd();
                }
            }
            this.mAnimationContainer.clear();
        }
    }

    public void play(SpringAnimation springAnimation) {
        if (springAnimation != null) {
            this.mAnimationContainer.add(springAnimation);
        }
    }

    public void playTogether(SpringAnimation... springAnimationArr) {
        for (SpringAnimation springAnimation : springAnimationArr) {
            if (springAnimation != null) {
                this.mAnimationContainer.add(springAnimation);
            }
        }
    }
}
