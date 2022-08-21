package miuix.animation.controller;

import miuix.animation.IAnimTarget;
import miuix.animation.IStateContainer;

/* loaded from: classes3.dex */
public abstract class FolmeBase implements IStateContainer {
    public IFolmeStateStyle mState;

    public FolmeBase(IAnimTarget... iAnimTargetArr) {
        this.mState = StateComposer.composeStyle(iAnimTargetArr);
    }

    @Override // miuix.animation.IStateContainer
    public void clean() {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.clean();
        }
    }

    @Override // miuix.animation.ICancelableStyle
    public void end(Object... objArr) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        if (iFolmeStateStyle != null) {
            iFolmeStateStyle.end(objArr);
        }
    }
}
