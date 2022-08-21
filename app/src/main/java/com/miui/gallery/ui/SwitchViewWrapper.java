package com.miui.gallery.ui;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.SwitchView;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ViewProperty;

/* loaded from: classes2.dex */
public class SwitchViewWrapper {
    public boolean afterAnimTrigger;
    public boolean isHideAnimating;
    public long mCancelTimeWhenDelay;
    public Context mContext;
    public DiscoveryDot mDiscoverDot;
    public int mOriginDelayTime;
    public boolean mShowEnable;
    public Runnable mShowRunnable;
    public Runnable mShowWithAnimRunnable;
    public SwitchView mSwitchView;

    public static /* synthetic */ void $r8$lambda$lOMrmIyMq97RlElQBOwfjGDRwrI(SwitchViewWrapper switchViewWrapper) {
        switchViewWrapper.lambda$showSwitchViewDelay$0();
    }

    public SwitchViewWrapper(Context context, SwitchView switchView) {
        this.mContext = context;
        this.mSwitchView = switchView;
    }

    public void setDiscoverDot(DiscoveryDot discoveryDot) {
        this.mDiscoverDot = discoveryDot;
    }

    public void setShowEnable(boolean z) {
        this.mShowEnable = z;
    }

    public void showSwitchViewByAnim(final int i) {
        if (!this.mShowEnable) {
            return;
        }
        if (i > 0 && this.isHideAnimating) {
            this.mCancelTimeWhenDelay = System.currentTimeMillis();
            this.mOriginDelayTime = i;
            this.afterAnimTrigger = true;
            return;
        }
        Runnable runnable = this.mShowWithAnimRunnable;
        if (runnable == null) {
            this.mShowWithAnimRunnable = new Runnable() { // from class: com.miui.gallery.ui.SwitchViewWrapper.1
                {
                    SwitchViewWrapper.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    final int top = SwitchViewWrapper.this.mSwitchView.getTop();
                    FolmeUtil.setStateAnim(SwitchViewWrapper.this.mSwitchView, null, new AnimState().add(ViewProperty.Y, top), null, new TransitionListener() { // from class: com.miui.gallery.ui.SwitchViewWrapper.1.1
                        {
                            AnonymousClass1.this = this;
                        }

                        @Override // miuix.animation.listener.TransitionListener
                        public void onComplete(Object obj) {
                            super.onComplete(obj);
                            if (top != SwitchViewWrapper.this.mSwitchView.getTop()) {
                                AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                                SwitchViewWrapper.this.showSwitchViewByAnim(i);
                            }
                        }

                        @Override // miuix.animation.listener.TransitionListener
                        public void onCancel(Object obj) {
                            super.onCancel(obj);
                            SwitchViewWrapper.this.mSwitchView.setY(SwitchViewWrapper.this.mSwitchView.getTop());
                        }
                    });
                }
            };
        } else {
            this.mSwitchView.removeCallbacks(runnable);
        }
        this.mSwitchView.postDelayed(this.mShowWithAnimRunnable, i);
    }

    public void hideSwitchViewByAnim() {
        FolmeUtil.setStateAnim(this.mSwitchView, null, new AnimState().add(ViewProperty.Y, this.mSwitchView.getBottom() + (this.mContext.getResources().getDimensionPixelOffset(R.dimen.switch_view_margin_bottom) * 2)), null, new TransitionListener() { // from class: com.miui.gallery.ui.SwitchViewWrapper.2
            {
                SwitchViewWrapper.this = this;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                super.onBegin(obj);
                SwitchViewWrapper.this.isHideAnimating = true;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj, UpdateInfo updateInfo) {
                super.onComplete(obj, updateInfo);
                SwitchViewWrapper.this.isHideAnimating = false;
                if (SwitchViewWrapper.this.afterAnimTrigger) {
                    int currentTimeMillis = SwitchViewWrapper.this.mOriginDelayTime - ((int) (System.currentTimeMillis() - SwitchViewWrapper.this.mCancelTimeWhenDelay));
                    if (currentTimeMillis < 0) {
                        currentTimeMillis = 0;
                    }
                    SwitchViewWrapper.this.showSwitchViewByAnim(currentTimeMillis);
                    SwitchViewWrapper.this.afterAnimTrigger = false;
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onCancel(Object obj) {
                super.onCancel(obj);
                SwitchViewWrapper.this.isHideAnimating = false;
            }
        });
    }

    public void showSwitchViewDelay() {
        if (!this.mShowEnable) {
            return;
        }
        if (this.mShowRunnable == null) {
            this.mShowRunnable = new Runnable() { // from class: com.miui.gallery.ui.SwitchViewWrapper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SwitchViewWrapper.$r8$lambda$lOMrmIyMq97RlElQBOwfjGDRwrI(SwitchViewWrapper.this);
                }
            };
        }
        this.mSwitchView.postDelayed(this.mShowRunnable, 500L);
    }

    /* renamed from: showSwitchView */
    public void lambda$showSwitchViewDelay$0() {
        if (this.mShowEnable && this.mSwitchView.getVisibility() == 8) {
            SwitchView switchView = this.mSwitchView;
            switchView.setY(switchView.getTop());
            this.mSwitchView.setVisibility(0);
        }
    }

    public void hideSwitchView() {
        Runnable runnable = this.mShowRunnable;
        if (runnable != null) {
            this.mSwitchView.removeCallbacks(runnable);
        }
        if (this.mSwitchView.getVisibility() == 0) {
            this.mSwitchView.setVisibility(8);
        }
    }

    public int getDiscoverCount() {
        DiscoveryDot discoveryDot = this.mDiscoverDot;
        if (discoveryDot != null) {
            return discoveryDot.getPhotoCount();
        }
        return 0;
    }
}
