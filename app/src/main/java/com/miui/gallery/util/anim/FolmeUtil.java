package com.miui.gallery.util.anim;

import android.view.View;
import com.miui.gallery.util.anim.AnimParams;
import miuix.animation.Folme;
import miuix.animation.IFolme;
import miuix.animation.IStateStyle;
import miuix.animation.ITouchStyle;
import miuix.animation.IVisibleStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;

/* loaded from: classes2.dex */
public class FolmeUtil {
    /* renamed from: $r8$lambda$q8ryH8ZPxqoYxcZ0HfVV6G-TB_4 */
    public static /* synthetic */ void m1713$r8$lambda$q8ryH8ZPxqoYxcZ0HfVV6GTB_4(View view) {
        lambda$setFakeTouchAnim$0(view);
    }

    public static void setCustomTouchAnim(View view, AnimParams animParams, AnimParams animParams2, TransitionListener transitionListener, boolean z) {
        setCustomTouchAnim(view, animParams, animParams2, null, transitionListener, z);
    }

    public static void setCustomTouchAnim(View view, AnimParams animParams, AnimParams animParams2, AnimConfig animConfig, TransitionListener transitionListener, boolean z) {
        Folme.clean(view);
        ITouchStyle iTouchStyle = Folme.useAt(view).touch();
        if (animParams != null) {
            float alpha = animParams.getAlpha();
            float scale = animParams.getScale();
            AnimParams.Tint tint = animParams.getTint();
            if (alpha != -1.0f) {
                iTouchStyle.setAlpha(alpha, ITouchStyle.TouchType.DOWN);
            }
            if (scale != -1.0f) {
                iTouchStyle.setScale(scale, ITouchStyle.TouchType.DOWN);
            }
            if (tint != null) {
                iTouchStyle.setTint(tint.getA(), tint.getR(), tint.getG(), tint.getB());
            }
        }
        if (animParams2 != null) {
            float alpha2 = animParams2.getAlpha();
            float scale2 = animParams2.getScale();
            if (alpha2 != -1.0f) {
                iTouchStyle.setAlpha(alpha2, ITouchStyle.TouchType.UP);
            }
            if (scale2 != -1.0f) {
                iTouchStyle.setScale(scale2, ITouchStyle.TouchType.UP);
            }
        }
        if (animConfig == null) {
            animConfig = new AnimConfig();
        }
        if (transitionListener != null) {
            animConfig.addListeners(transitionListener);
        }
        iTouchStyle.handleTouchOf(view, z || view.hasOnClickListeners(), animConfig);
    }

    public static void setDefaultTouchAnim(View view, TransitionListener transitionListener, boolean z) {
        setDefaultTouchAnim(view, transitionListener, true, true, z);
    }

    public static void setDefaultTouchAnim(View view, TransitionListener transitionListener, boolean z, boolean z2, boolean z3) {
        setDefaultTouchAnim(view, view, transitionListener, z, z2, z3);
    }

    public static void setDefaultTouchAnim(View view, View view2, TransitionListener transitionListener, boolean z, boolean z2, boolean z3) {
        ITouchStyle iTouchStyle = Folme.useAt(view2).touch();
        if (z) {
            iTouchStyle.setTint(0.0f, 0.0f, 0.0f, 0.0f);
        }
        if (!z2) {
            iTouchStyle.setScale(1.0f, new ITouchStyle.TouchType[0]);
        }
        AnimConfig animConfig = new AnimConfig();
        if (transitionListener != null) {
            animConfig.addListeners(transitionListener);
        }
        iTouchStyle.handleTouchOf(view, z3 || view2.hasOnClickListeners(), animConfig);
    }

    public static void setCustomVisibleAnim(View view, boolean z, AnimParams animParams, TransitionListener transitionListener) {
        setCustomVisibleAnim(view, z, animParams, null, transitionListener);
    }

    public static void setCustomVisibleAnim(View view, boolean z, AnimParams animParams, AnimConfig animConfig, TransitionListener transitionListener) {
        IVisibleStyle.VisibleType visibleType;
        Folme.clean(view);
        IVisibleStyle visible = Folme.useAt(view).visible();
        if (z) {
            visibleType = IVisibleStyle.VisibleType.SHOW;
        } else {
            visibleType = IVisibleStyle.VisibleType.HIDE;
        }
        if (animParams != null) {
            float alpha = animParams.getAlpha();
            float scale = animParams.getScale();
            long delay = animParams.getDelay();
            animParams.getBounds();
            animParams.getMove();
            if (alpha != -1.0f) {
                visible.setAlpha(alpha, visibleType);
            }
            if (scale != -1.0f) {
                visible.setScale(scale, visibleType);
            }
            if (delay != -1) {
                visible.setShowDelay(delay);
            }
        }
        if (animConfig == null) {
            animConfig = new AnimConfig();
        }
        if (transitionListener != null) {
            animConfig.addListeners(transitionListener);
        }
        if (z) {
            visible.show(animConfig);
        } else {
            visible.hide(animConfig);
        }
    }

    public static void setStateAnim(View view, AnimState animState, AnimState animState2, AnimConfig animConfig, TransitionListener transitionListener) {
        Folme.clean(view);
        IStateStyle state = Folme.useAt(view).state();
        if (animConfig == null) {
            animConfig = new AnimConfig();
        }
        if (transitionListener != null) {
            animConfig.addListeners(transitionListener);
        }
        state.fromTo(animState, animState2, animConfig);
    }

    public static void addAlphaPressAnim(View view) {
        Folme.useAt(view).touch().setScale(1.0f, new ITouchStyle.TouchType[0]).setAlpha(0.6f, new ITouchStyle.TouchType[0]).handleTouchOf(view, new AnimConfig[0]);
    }

    public static void setFakeTouchAnim(final View view, float f, long j) {
        if (view == null) {
            return;
        }
        Folme.clean(view);
        Folme.useAt(view).touch().setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(f, ITouchStyle.TouchType.DOWN).touchDown(new AnimConfig[0]);
        view.postDelayed(new Runnable() { // from class: com.miui.gallery.util.anim.FolmeUtil$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FolmeUtil.m1713$r8$lambda$q8ryH8ZPxqoYxcZ0HfVV6GTB_4(view);
            }
        }, j);
    }

    public static /* synthetic */ void lambda$setFakeTouchAnim$0(View view) {
        Folme.useAt(view).touch().setScale(1.0f, ITouchStyle.TouchType.UP).setTouchUp();
    }

    public static void animShowHide(View view, boolean z, boolean z2) {
        if (view == null) {
            return;
        }
        int i = 0;
        IFolme useAt = Folme.useAt(view);
        useAt.visible().clean();
        view.clearAnimation();
        if (!z2) {
            if (!z) {
                i = 8;
            }
            view.setVisibility(i);
        } else if (z) {
            view.setAlpha(0.4f);
            useAt.visible().setAlpha(1.0f, IVisibleStyle.VisibleType.SHOW).show(new AnimConfig[0]);
        } else {
            view.setAlpha(0.2f);
            useAt.visible().setAlpha(0.0f, IVisibleStyle.VisibleType.HIDE).hide(new AnimConfig[0]);
        }
    }
}
