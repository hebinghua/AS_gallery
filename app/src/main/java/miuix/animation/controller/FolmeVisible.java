package miuix.animation.controller;

import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.Collection;
import miuix.animation.IAnimTarget;
import miuix.animation.IVisibleStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.EaseManager;

/* loaded from: classes3.dex */
public class FolmeVisible extends FolmeBase implements IVisibleStyle {
    public final AnimConfig mDefConfig;
    public boolean mHasMove;
    public boolean mHasScale;
    public boolean mSetBound;

    public FolmeVisible(IAnimTarget... iAnimTargetArr) {
        super(iAnimTargetArr);
        this.mDefConfig = new AnimConfig().addListeners(new TransitionListener() { // from class: miuix.animation.controller.FolmeVisible.1
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj, Collection<UpdateInfo> collection) {
                if (!obj.equals(IVisibleStyle.VisibleType.SHOW) || !FolmeVisible.this.mSetBound) {
                    return;
                }
                AnimState.alignState(FolmeVisible.this.mState.getState(IVisibleStyle.VisibleType.HIDE), collection);
            }
        });
        useAutoAlpha(true);
    }

    @Override // miuix.animation.controller.FolmeBase, miuix.animation.IStateContainer
    public void clean() {
        super.clean();
        this.mHasScale = false;
        this.mHasMove = false;
    }

    public IVisibleStyle useAutoAlpha(boolean z) {
        ViewProperty viewProperty = ViewProperty.AUTO_ALPHA;
        ViewProperty viewProperty2 = ViewProperty.ALPHA;
        if (z) {
            this.mState.getState(IVisibleStyle.VisibleType.SHOW).remove(viewProperty2).add(viewProperty, 1.0d);
            this.mState.getState(IVisibleStyle.VisibleType.HIDE).remove(viewProperty2).add(viewProperty, SearchStatUtils.POW);
        } else {
            this.mState.getState(IVisibleStyle.VisibleType.SHOW).remove(viewProperty).add(viewProperty2, 1.0d);
            this.mState.getState(IVisibleStyle.VisibleType.HIDE).remove(viewProperty).add(viewProperty2, SearchStatUtils.POW);
        }
        return this;
    }

    @Override // miuix.animation.IVisibleStyle
    public IVisibleStyle setAlpha(float f, IVisibleStyle.VisibleType... visibleTypeArr) {
        this.mState.getState(getType(visibleTypeArr)).add(ViewProperty.AUTO_ALPHA, f);
        return this;
    }

    @Override // miuix.animation.IVisibleStyle
    public IVisibleStyle setScale(float f, IVisibleStyle.VisibleType... visibleTypeArr) {
        this.mHasScale = true;
        double d = f;
        this.mState.getState(getType(visibleTypeArr)).add(ViewProperty.SCALE_Y, d).add(ViewProperty.SCALE_X, d);
        return this;
    }

    public final IVisibleStyle.VisibleType getType(IVisibleStyle.VisibleType... visibleTypeArr) {
        return visibleTypeArr.length > 0 ? visibleTypeArr[0] : IVisibleStyle.VisibleType.HIDE;
    }

    @Override // miuix.animation.IVisibleStyle
    public IVisibleStyle setShowDelay(long j) {
        this.mState.getState(IVisibleStyle.VisibleType.SHOW).getConfig().delay = j;
        return this;
    }

    @Override // miuix.animation.IVisibleStyle
    public void show(AnimConfig... animConfigArr) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        IVisibleStyle.VisibleType visibleType = IVisibleStyle.VisibleType.SHOW;
        iFolmeStateStyle.to(visibleType, getConfig(visibleType, animConfigArr));
    }

    @Override // miuix.animation.IVisibleStyle
    public void hide(AnimConfig... animConfigArr) {
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        IVisibleStyle.VisibleType visibleType = IVisibleStyle.VisibleType.HIDE;
        iFolmeStateStyle.to(visibleType, getConfig(visibleType, animConfigArr));
    }

    @Override // miuix.animation.IVisibleStyle
    public IVisibleStyle setHide() {
        this.mState.setTo(IVisibleStyle.VisibleType.HIDE);
        return this;
    }

    public final AnimConfig[] getConfig(IVisibleStyle.VisibleType visibleType, AnimConfig... animConfigArr) {
        EaseManager.EaseStyle style;
        EaseManager.EaseStyle style2;
        EaseManager.EaseStyle style3;
        EaseManager.EaseStyle style4;
        boolean z = this.mHasScale;
        if (!z && !this.mHasMove) {
            AnimConfig animConfig = this.mDefConfig;
            if (visibleType == IVisibleStyle.VisibleType.SHOW) {
                style4 = EaseManager.getStyle(16, 300.0f);
            } else {
                style4 = EaseManager.getStyle(-2, 1.0f, 0.15f);
            }
            animConfig.setEase(style4);
        } else if (z && !this.mHasMove) {
            AnimConfig animConfig2 = this.mDefConfig;
            if (visibleType == IVisibleStyle.VisibleType.SHOW) {
                style3 = EaseManager.getStyle(-2, 0.6f, 0.35f);
            } else {
                style3 = EaseManager.getStyle(-2, 0.75f, 0.2f);
            }
            animConfig2.setEase(style3);
        } else if (!z) {
            AnimConfig animConfig3 = this.mDefConfig;
            if (visibleType == IVisibleStyle.VisibleType.SHOW) {
                style2 = EaseManager.getStyle(-2, 0.75f, 0.35f);
            } else {
                style2 = EaseManager.getStyle(-2, 0.75f, 0.25f);
            }
            animConfig3.setEase(style2);
        } else {
            AnimConfig animConfig4 = this.mDefConfig;
            if (visibleType == IVisibleStyle.VisibleType.SHOW) {
                style = EaseManager.getStyle(-2, 0.65f, 0.35f);
            } else {
                style = EaseManager.getStyle(-2, 0.75f, 0.25f);
            }
            animConfig4.setEase(style);
        }
        return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mDefConfig);
    }
}
