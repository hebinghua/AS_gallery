package miuix.animation.controller;

import java.lang.reflect.Array;
import miuix.animation.IAnimTarget;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.internal.AnimRunner;
import miuix.animation.internal.PredictTask;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.FloatProperty;
import miuix.animation.utils.LogUtils;

/* loaded from: classes3.dex */
public class FolmeState implements IFolmeStateStyle {
    public IAnimTarget mTarget;
    public StateManager mStateMgr = new StateManager();
    public AnimConfigLink mConfigLink = new AnimConfigLink();
    public boolean mEnableAnim = true;

    public FolmeState(IAnimTarget iAnimTarget) {
        this.mTarget = iAnimTarget;
    }

    @Override // miuix.animation.controller.IFolmeStateStyle
    public IAnimTarget getTarget() {
        return this.mTarget;
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle setTo(Object obj) {
        return setTo(obj, new AnimConfig[0]);
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle setTo(Object obj, AnimConfig... animConfigArr) {
        return setTo(obj, AnimConfigLink.linkConfig(animConfigArr));
    }

    public final IStateStyle setTo(final Object obj, final AnimConfigLink animConfigLink) {
        IAnimTarget iAnimTarget = this.mTarget;
        if (iAnimTarget == null) {
            return this;
        }
        if ((obj instanceof Integer) || (obj instanceof Float)) {
            return setTo(obj, animConfigLink);
        }
        iAnimTarget.executeOnInitialized(new Runnable() { // from class: miuix.animation.controller.FolmeState.1
            @Override // java.lang.Runnable
            public void run() {
                AnimState state = FolmeState.this.getState(obj);
                IAnimTarget target = FolmeState.this.getTarget();
                if (LogUtils.isLogEnabled()) {
                    LogUtils.debug("FolmeState.setTo, state = " + state, new Object[0]);
                }
                target.animManager.setTo(state, animConfigLink);
                FolmeState.this.mStateMgr.clearTempState(state);
            }
        });
        return this;
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle to(AnimConfig... animConfigArr) {
        return to(getCurrentState(), animConfigArr);
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle to(Object obj, AnimConfig... animConfigArr) {
        if ((obj instanceof AnimState) || this.mStateMgr.hasState(obj)) {
            return fromTo((Object) null, getState(obj), animConfigArr);
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            Object[] objArr = new Object[animConfigArr.length + length];
            System.arraycopy(obj, 0, objArr, 0, length);
            System.arraycopy(animConfigArr, 0, objArr, length, animConfigArr.length);
            return to(objArr);
        }
        return to(obj, animConfigArr);
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle then(Object obj, AnimConfig... animConfigArr) {
        this.mStateMgr.setStateFlags(obj, 1L);
        return to(obj, animConfigArr);
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle fromTo(Object obj, Object obj2, AnimConfig... animConfigArr) {
        AnimConfigLink configLink = getConfigLink();
        for (AnimConfig animConfig : animConfigArr) {
            configLink.add(animConfig, new boolean[0]);
        }
        return fromTo(obj, obj2, configLink);
    }

    public final IStateStyle fromTo(Object obj, Object obj2, AnimConfigLink animConfigLink) {
        if (this.mEnableAnim) {
            this.mStateMgr.setup(obj2);
            if (obj != null) {
                setTo(obj);
            }
            AnimState state = getState(obj2);
            this.mStateMgr.addTempConfig(state, animConfigLink);
            AnimRunner.getInst().run(this.mTarget, getState(obj), getState(obj2), animConfigLink);
            this.mStateMgr.clearTempState(state);
            animConfigLink.clear();
        }
        return this;
    }

    @Override // miuix.animation.IStateContainer
    public void clean() {
        cancel();
    }

    @Override // miuix.animation.ICancelableStyle
    public void cancel() {
        AnimRunner.getInst().cancel(this.mTarget, null);
    }

    @Override // miuix.animation.ICancelableStyle
    public void end(Object... objArr) {
        if (objArr.length > 0) {
            if (objArr[0] instanceof FloatProperty) {
                FloatProperty[] floatPropertyArr = new FloatProperty[objArr.length];
                System.arraycopy(objArr, 0, floatPropertyArr, 0, objArr.length);
                AnimRunner.getInst().end(this.mTarget, floatPropertyArr);
                return;
            }
            String[] strArr = new String[objArr.length];
            System.arraycopy(objArr, 0, strArr, 0, objArr.length);
            AnimRunner.getInst().end(this.mTarget, strArr);
        }
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle setTo(Object... objArr) {
        AnimConfigLink configLink = getConfigLink();
        setTo(this.mStateMgr.getSetToState(getTarget(), configLink, objArr), configLink);
        return this;
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle to(Object... objArr) {
        return fromTo((Object) null, this.mStateMgr.getToState(getTarget(), getConfigLink(), objArr), new AnimConfig[0]);
    }

    @Override // miuix.animation.IStateStyle
    public long predictDuration(Object... objArr) {
        IAnimTarget target = getTarget();
        AnimConfigLink configLink = getConfigLink();
        AnimState toState = this.mStateMgr.getToState(target, configLink, objArr);
        long predictDuration = PredictTask.predictDuration(target, null, toState, configLink);
        this.mStateMgr.clearTempState(toState);
        configLink.clear();
        return predictDuration;
    }

    public final AnimConfigLink getConfigLink() {
        return this.mConfigLink;
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle setFlags(long j) {
        getTarget().setFlags(j);
        return this;
    }

    @Override // miuix.animation.controller.IFolmeStateStyle
    public AnimState getState(Object obj) {
        return this.mStateMgr.getState(obj);
    }

    @Override // miuix.animation.controller.IFolmeStateStyle
    public void addState(AnimState animState) {
        this.mStateMgr.addState(animState);
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle setup(Object obj) {
        this.mStateMgr.setup(obj);
        return this;
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle set(Object obj) {
        this.mStateMgr.setup(obj);
        return this;
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle addListener(TransitionListener transitionListener) {
        this.mStateMgr.addListener(transitionListener);
        return this;
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle add(FloatProperty floatProperty, float f) {
        this.mStateMgr.add(floatProperty, f);
        return this;
    }

    @Override // miuix.animation.IStateStyle
    public IStateStyle add(String str, int i) {
        this.mStateMgr.add(str, i);
        return this;
    }

    @Override // miuix.animation.IStateStyle
    public AnimState getCurrentState() {
        return this.mStateMgr.getCurrentState();
    }
}
