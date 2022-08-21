package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.miui.gallery.R;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.VerticalSeekBar;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;

/* loaded from: classes2.dex */
public class MiplayVolumeControlLayout extends FrameLayout {
    public AnimState mCollapseState;
    public View mContentLayout;
    public int mCurVolume;
    public AnimState mExpandState;
    public boolean mExpandable;
    public boolean mIsAnimating;
    public boolean mIsCollapsable;
    public boolean mIsPressing;
    public ImageButton mVolumeBtn;
    public MiplayVolumeChangeListener mVolumeListener;
    public VerticalSeekBar mVolumeSeekbar;
    public ImageView mVolumeSeekbarIcon;

    /* loaded from: classes2.dex */
    public interface MiplayVolumeChangeListener {
        void onVolumeChange(int i);
    }

    public static /* synthetic */ void $r8$lambda$_fjbVQYsQBNGb9ModeEygC7LSSo(MiplayVolumeControlLayout miplayVolumeControlLayout, View view) {
        miplayVolumeControlLayout.lambda$initView$0(view);
    }

    public static /* synthetic */ void $r8$lambda$_kvA7xNIUTQjdahUi9nl7_QgKMA(MiplayVolumeControlLayout miplayVolumeControlLayout, int i, boolean z) {
        miplayVolumeControlLayout.lambda$initView$1(i, z);
    }

    public MiplayVolumeControlLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MiplayVolumeControlLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public final void initView(Context context) {
        this.mContentLayout = LayoutInflater.from(context).inflate(R.layout.miplay_volume_control_layout, this);
        this.mVolumeBtn = (ImageButton) findViewById(R.id.miplay_volume_btn);
        this.mVolumeSeekbarIcon = (ImageView) findViewById(R.id.miplay_volume_seekbar_icon);
        this.mVolumeSeekbar = (VerticalSeekBar) findViewById(R.id.miplay_volume_seekbar);
        this.mVolumeBtn.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.MiplayVolumeControlLayout$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MiplayVolumeControlLayout.$r8$lambda$_fjbVQYsQBNGb9ModeEygC7LSSo(MiplayVolumeControlLayout.this, view);
            }
        });
        this.mVolumeSeekbar.setProgressChangeListener(new VerticalSeekBar.ProgressChangeListener() { // from class: com.miui.gallery.ui.MiplayVolumeControlLayout$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.widget.VerticalSeekBar.ProgressChangeListener
            public final void onProgressChange(int i, boolean z) {
                MiplayVolumeControlLayout.$r8$lambda$_kvA7xNIUTQjdahUi9nl7_QgKMA(MiplayVolumeControlLayout.this, i, z);
            }
        });
        this.mExpandable = true;
    }

    public /* synthetic */ void lambda$initView$0(View view) {
        expand();
    }

    public /* synthetic */ void lambda$initView$1(int i, boolean z) {
        if (i < 0) {
            i = 0;
        }
        if (i > this.mVolumeSeekbar.getMax()) {
            i = this.mVolumeSeekbar.getMax();
        }
        DefaultLogger.d("MiplayVolumeControlLayout", "onProgressChange:progress->%s, isLocal->%s, mIsPressing->%s", Integer.valueOf(i), Boolean.valueOf(z), Boolean.valueOf(this.mIsPressing));
        this.mCurVolume = i;
        if (this.mVolumeListener == null || !z || this.mIsPressing) {
            return;
        }
        DefaultLogger.d("MiplayVolumeControlLayout", "notify sdk volume change to ->%s", Integer.valueOf(i));
        this.mVolumeListener.onVolumeChange(i);
        this.mVolumeSeekbar.setContentDescription(getResources().getString(R.string.miplay_adjust_volume, Integer.valueOf(i)));
    }

    public void expand() {
        if (!this.mExpandable) {
            return;
        }
        setVisibility(0);
        setLayoutParams(this, -2, -2);
        animExpand();
        this.mExpandable = false;
        this.mIsCollapsable = true;
    }

    public void collapse() {
        if (!this.mIsCollapsable) {
            return;
        }
        this.mIsCollapsable = false;
        setVisibility(0);
        setLayoutParams(this, -2, -2);
        animCollapse();
        this.mIsCollapsable = false;
        this.mExpandable = true;
    }

    public final void setLayoutParams(View view, int i, int i2) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i2;
        view.setLayoutParams(layoutParams);
    }

    public final void animExpand() {
        if (this.mIsAnimating) {
            return;
        }
        if (this.mExpandState == null) {
            AnimState animState = new AnimState("expand");
            this.mExpandState = animState;
            animState.add(ViewProperty.WIDTH, getResources().getDimensionPixelSize(R.dimen.miplay_circle_btn_bg_size));
            this.mExpandState.add(ViewProperty.HEIGHT, getResources().getDimensionPixelSize(R.dimen.miplay_volume_seekbar_height));
        }
        FolmeUtil.animShowHide(this.mVolumeBtn, false, true);
        FolmeUtil.animShowHide(this.mVolumeSeekbar, true, true);
        FolmeUtil.animShowHide(this.mVolumeSeekbarIcon, true, false);
        Folme.useAt(this.mContentLayout).state().to(this.mExpandState, new AnimConfig().addListeners(new TransitionListener() { // from class: com.miui.gallery.ui.MiplayVolumeControlLayout.1
            {
                MiplayVolumeControlLayout.this = this;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                super.onBegin(obj);
                MiplayVolumeControlLayout.this.mIsAnimating = true;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                MiplayVolumeControlLayout.this.mIsAnimating = false;
            }
        }));
    }

    public final void animCollapse() {
        if (this.mIsAnimating) {
            return;
        }
        if (this.mCollapseState == null) {
            AnimState animState = new AnimState("collapse");
            this.mCollapseState = animState;
            animState.add(ViewProperty.WIDTH, getResources().getDimensionPixelSize(R.dimen.miplay_circle_btn_bg_size));
            this.mCollapseState.add(ViewProperty.HEIGHT, getResources().getDimensionPixelSize(R.dimen.miplay_circle_btn_bg_size));
        }
        FolmeUtil.animShowHide(this.mVolumeBtn, true, true);
        FolmeUtil.animShowHide(this.mVolumeSeekbar, false, true);
        FolmeUtil.animShowHide(this.mVolumeSeekbarIcon, false, false);
    }

    public void setVolume(long j, boolean z, boolean z2) {
        VerticalSeekBar verticalSeekBar = this.mVolumeSeekbar;
        if (verticalSeekBar != null) {
            verticalSeekBar.notifyProgressChange((int) j, z, z2);
        }
    }

    public void onVolumeBtnKeyDown(boolean z) {
        this.mIsPressing = true;
        if (this.mExpandable) {
            expand();
        } else {
            setVolume(z ? this.mCurVolume + 1 : this.mCurVolume - 1, true, true);
        }
    }

    public void onVolumeBtnKeyUp() {
        this.mIsPressing = false;
        setVolume(this.mCurVolume, true, true);
    }

    public void setVolumeChangeListener(MiplayVolumeChangeListener miplayVolumeChangeListener) {
        this.mVolumeListener = miplayVolumeChangeListener;
    }
}
