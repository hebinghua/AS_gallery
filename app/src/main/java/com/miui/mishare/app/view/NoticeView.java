package com.miui.mishare.app.view;

import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.miui.mishare.R$drawable;

/* loaded from: classes3.dex */
public class NoticeView extends AppCompatImageView {
    public AnimationCallback callback;

    public NoticeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void start() {
        setVisibility(0);
        Drawable drawable = getDrawable();
        if (drawable == null) {
            drawable = getResources().getDrawable(R$drawable.wave_anim);
            setImageDrawable(drawable);
        }
        if (drawable instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            if (animatedVectorDrawable.isRunning()) {
                return;
            }
            AnimationCallback animationCallback = new AnimationCallback();
            this.callback = animationCallback;
            animatedVectorDrawable.registerAnimationCallback(animationCallback);
            animatedVectorDrawable.start();
        }
    }

    public void stop() {
        Drawable drawable = getDrawable();
        if (drawable instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            if (animatedVectorDrawable.isRunning()) {
                animatedVectorDrawable.unregisterAnimationCallback(this.callback);
                animatedVectorDrawable.stop();
            }
        }
        setVisibility(4);
    }

    /* loaded from: classes3.dex */
    public static class AnimationCallback extends Animatable2.AnimationCallback {
        public AnimationCallback() {
        }

        @Override // android.graphics.drawable.Animatable2.AnimationCallback
        public void onAnimationEnd(Drawable drawable) {
            ((AnimatedVectorDrawable) drawable).start();
        }
    }
}
