package com.miui.gallery.search.widget.bannerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewAnimator;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class BannerView extends ViewAnimator implements ILoopController {
    public int mDefaultItemDisplayDuration;
    public boolean mIsStarted;
    public int mPosition;
    public AnimRunnable mRunnable;
    public boolean mShouldStartLoopingWhenAttached;

    public BannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDefaultItemDisplayDuration = 3000;
        this.mPosition = -1;
        this.mShouldStartLoopingWhenAttached = false;
        this.mRunnable = new AnimRunnable();
        init(context, attributeSet);
    }

    public final void init(Context context, AttributeSet attributeSet) {
        setAnimateFirstView(false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BannerView);
        this.mDefaultItemDisplayDuration = obtainStyledAttributes.getInteger(0, this.mDefaultItemDisplayDuration);
        obtainStyledAttributes.recycle();
    }

    public void setAdapter(BaseBannerAdapter baseBannerAdapter) {
        throw new RuntimeException("adapter must not be null");
    }

    public void startLoop() {
        throw new RuntimeException("You must call setContentAdapter() before start");
    }

    public void stopLoop() {
        removeCallbacks(this.mRunnable);
        this.mIsStarted = false;
    }

    public void performNext() {
        View currentView = getCurrentView();
        if (currentView == null) {
            return;
        }
        getChildAt((((Integer) currentView.getTag()).intValue() + 1) % getChildCount());
        getCircledNextPosition();
        throw null;
    }

    public boolean isLooping() {
        return this.mIsStarted;
    }

    @Override // com.miui.gallery.search.widget.bannerView.ILoopController
    public int getCurrentPosition() {
        return this.mPosition;
    }

    public final void postDelayed(int i) {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCircledNextPosition() {
        throw null;
    }

    /* loaded from: classes2.dex */
    public class AnimRunnable implements Runnable {
        public AnimRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            BannerView.this.performNext();
            BannerView bannerView = BannerView.this;
            bannerView.mPosition = bannerView.getCircledNextPosition();
            BannerView bannerView2 = BannerView.this;
            bannerView2.postDelayed(bannerView2.mPosition);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isLooping()) {
            stopLoop();
            this.mShouldStartLoopingWhenAttached = true;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isLooping() || !this.mShouldStartLoopingWhenAttached) {
            return;
        }
        startLoop();
    }
}
