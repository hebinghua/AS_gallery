package com.miui.gallery.vlog.base;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public abstract class BasePresenter {
    public Context mContext;
    public IBaseModel mIBaseModel;
    public VlogModel mVlogModel;

    public abstract void destroy();

    public void setPlayViewProgress(long j) {
    }

    public BasePresenter(Context context) {
        this.mContext = context;
        this.mVlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) context, VlogModel.class);
    }

    public void seek(long j) {
        ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).getSdkManager().seek(j);
        setPlayViewProgress(j);
    }

    public void pause() {
        ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).getSdkManager().pause();
    }

    public boolean isPlaying() {
        return ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).getSdkManager().isPlay();
    }

    public void loadData() {
        IBaseModel iBaseModel = this.mIBaseModel;
        if (iBaseModel != null) {
            iBaseModel.loadData();
        }
    }

    public boolean isSaving() {
        return ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).isSaving();
    }
}
