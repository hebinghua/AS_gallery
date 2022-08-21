package com.miui.gallery.vlog.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.base.interfaces.IVlogActivity;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.vlog.home.VlogPresenter;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.mediaeditor.utils.MediaEditorUtils;

/* loaded from: classes2.dex */
public class VlogTitleView extends FrameLayout implements View.OnClickListener {
    public TextView mBackView;
    public LottieAnimationView mDownloadMediaEditorAppView;
    public VlogDependsModule.Callback mInstallMediaEditorCallback;
    public TextView mSaveView;
    public VlogPresenter mVlogPresenter;

    /* renamed from: $r8$lambda$fhUJMHaF3Iv8xVlfFyh7-Gfv09Q */
    public static /* synthetic */ void m1806$r8$lambda$fhUJMHaF3Iv8xVlfFyh7Gfv09Q(VlogTitleView vlogTitleView) {
        vlogTitleView.lambda$init$0();
    }

    public VlogTitleView(Context context) {
        super(context);
        this.mInstallMediaEditorCallback = new VlogDependsModule.Callback() { // from class: com.miui.gallery.vlog.view.VlogTitleView.1
            {
                VlogTitleView.this = this;
            }

            @Override // com.miui.gallery.imodule.modules.VlogDependsModule.Callback
            public void onInstallSuccess() {
                if (VlogTitleView.this.mDownloadMediaEditorAppView != null) {
                    VlogTitleView.this.mDownloadMediaEditorAppView.setVisibility(MediaEditorUtils.isMediaEditorAvailable() ? 8 : 0);
                }
            }
        };
        init(context);
    }

    public final void init(Context context) {
        if (context instanceof IVlogActivity) {
            this.mVlogPresenter = ((IVlogActivity) context).getVlogPresenter();
        }
        if (VlogUtils.isLandscape(context)) {
            FrameLayout.inflate(context, R$layout.vlog_title_layout_land, this);
        } else {
            FrameLayout.inflate(context, R$layout.vlog_title_layout, this);
        }
        this.mBackView = (TextView) findViewById(R$id.tv_back);
        this.mSaveView = (TextView) findViewById(R$id.tv_save);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(com.miui.gallery.editor.R$id.download_mediaeditor_app_view);
        this.mDownloadMediaEditorAppView = lottieAnimationView;
        lottieAnimationView.post(new Runnable() { // from class: com.miui.gallery.vlog.view.VlogTitleView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VlogTitleView.m1806$r8$lambda$fhUJMHaF3Iv8xVlfFyh7Gfv09Q(VlogTitleView.this);
            }
        });
        this.mSaveView.setOnClickListener(this);
        this.mBackView.setOnClickListener(this);
        this.mDownloadMediaEditorAppView.setOnClickListener(this);
        FolmeUtilsEditor.animButton(this.mSaveView);
        FolmeUtilsEditor.animButton(this.mBackView);
    }

    public /* synthetic */ void lambda$init$0() {
        this.mDownloadMediaEditorAppView.setVisibility(MediaEditorUtils.isMediaEditorAvailable() ? 8 : 0);
        this.mDownloadMediaEditorAppView.playAnimation();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mVlogPresenter == null) {
            return;
        }
        if (view == this.mBackView) {
            LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
            this.mVlogPresenter.doBackViewClickEvent();
            VlogStatUtils.statEvent("back");
        } else if (view == this.mSaveView) {
            LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
            this.mVlogPresenter.doSaveViewClickEvent();
            VlogStatUtils.statEvent("export");
        } else if (view != this.mDownloadMediaEditorAppView) {
        } else {
            LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
            this.mVlogPresenter.doDownloadMediaEditorAppClickEvent(this.mInstallMediaEditorCallback);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        VlogPresenter vlogPresenter = this.mVlogPresenter;
        if (vlogPresenter != null) {
            vlogPresenter.removeInstallListener();
        }
    }
}
