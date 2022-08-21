package com.miui.gallery.vlog.clip;

import android.content.Context;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.clip.TransMenuModel;
import com.miui.gallery.vlog.entity.TransData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class TranResPresenter extends BasePresenter {
    public ClipMenuModel mClipMenuModel;
    public long mClipStartPos;
    public Context mContext;
    public TransResView mTransResView;
    public VlogModel mVlogModel;

    public TranResPresenter(Context context, TransResView transResView) {
        super(context);
        this.mClipStartPos = -1L;
        this.mContext = context;
        this.mTransResView = transResView;
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        this.mVlogModel = (VlogModel) VlogUtils.getViewModel(fragmentActivity, VlogModel.class);
        this.mClipMenuModel = (ClipMenuModel) VlogUtils.getViewModel(fragmentActivity, ClipMenuModel.class);
        this.mIBaseModel = new TransMenuModel(new TransMenuModel.Callback() { // from class: com.miui.gallery.vlog.clip.TranResPresenter.1
            @Override // com.miui.gallery.vlog.clip.TransMenuModel.Callback
            public void loadDataSuccess(ArrayList<TransData> arrayList) {
                Collections.sort(arrayList);
                TranResPresenter.this.refreshData(arrayList);
                TranResPresenter.this.mTransResView.loadRecyclerView(arrayList, true);
            }

            @Override // com.miui.gallery.vlog.clip.TransMenuModel.Callback
            public void loadDataFail() {
                ToastUtils.makeText(TranResPresenter.this.mContext, TranResPresenter.this.mContext.getString(R$string.vlog_download_failed_for_notwork));
                ArrayList<TransData> arrayList = new ArrayList<>();
                arrayList.add(TransData.getDefaultItem());
                TranResPresenter.this.mTransResView.loadRecyclerView(arrayList, false);
            }
        });
    }

    public int findMatchTransIndex(String str, String str2, ArrayList<TransData> arrayList) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return 0;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            TransData transData = arrayList.get(i);
            if (TextUtils.equals(str, transData.getTransName()) && TextUtils.equals(str2, transData.getTransPath())) {
                return i;
            }
        }
        return -1;
    }

    public final void refreshData(ArrayList<TransData> arrayList) {
        Iterator<TransData> it = arrayList.iterator();
        while (it.hasNext()) {
            TransData next = it.next();
            if (next != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(VlogConfig.TRANS_PATH);
                String str = File.separator;
                sb.append(str);
                sb.append(next.getNameKey());
                sb.append(str);
                sb.append(next.getFileName());
                String sb2 = sb.toString();
                if (new File(sb2).exists()) {
                    next.setDownloadState(17);
                    next.setTransPath(sb2);
                } else {
                    String templateKey = next.getTemplateKey();
                    if (!TextUtils.isEmpty(templateKey)) {
                        checkWithTemplate(next, templateKey);
                    }
                }
            }
        }
    }

    public final void checkWithTemplate(TransData transData, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(VlogConfig.TEMPALTE_PATH);
        String str2 = File.separator;
        sb.append(str2);
        sb.append(str);
        sb.append(str2);
        sb.append(transData.getKey());
        String sb2 = sb.toString();
        if (new File(sb2).exists()) {
            transData.setDownloadState(17);
            transData.setTransPath(sb2);
        }
    }

    public void removeTrans(IVideoClip iVideoClip, boolean z) {
        IVideoClip videoClip;
        if (iVideoClip == null || (videoClip = this.mVlogModel.getSdkManager().getVideoClip(iVideoClip.getIndex() + 1)) == null) {
            return;
        }
        this.mVlogModel.getSdkManager().disconnect();
        this.mClipMenuModel.getTransResManager(this.mContext).setBuiltinTransition(iVideoClip.getIndex(), null, null);
        if (z) {
            previewTrans(iVideoClip, videoClip);
        }
        this.mVlogModel.getSdkManager().reconnect();
        this.mVlogModel.getSdkManager().playbackTimeline();
        DebugLogUtils.HAS_LOADED_SELECT_TRANS = true;
    }

    public final void previewTrans(IVideoClip iVideoClip, IVideoClip iVideoClip2) {
        if (iVideoClip == null || iVideoClip2 == null) {
            return;
        }
        long outPoint = iVideoClip.getOutPoint() - 500000;
        this.mVlogModel.getSdkManager().setTimelineInout(outPoint / 1000, (iVideoClip2.getInPoint() + 500000) / 1000, false);
        this.mClipStartPos = outPoint;
    }

    public void applyTrans(String str, IVideoClip iVideoClip, boolean z) {
        if (iVideoClip == null) {
            return;
        }
        this.mVlogModel.getSdkManager().disconnect();
        this.mClipMenuModel.getTransResManager(this.mContext).buildTransitions(iVideoClip.getIndex(), str);
        IVideoClip videoClip = this.mVlogModel.getSdkManager().getVideoClip(iVideoClip.getIndex() + 1);
        if (videoClip == null) {
            return;
        }
        if (z) {
            previewTrans(iVideoClip, videoClip);
        }
        this.mVlogModel.getSdkManager().reconnect();
        this.mVlogModel.getSdkManager().playbackTimeline();
        DebugLogUtils.HAS_LOADED_SELECT_TRANS = true;
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
        this.mVlogModel.getSdkManager().resetTimelineInout();
        if (this.mClipStartPos != -1) {
            this.mVlogModel.getSdkManager().seek(this.mClipStartPos);
        }
        IBaseModel iBaseModel = this.mIBaseModel;
        if (iBaseModel instanceof TransMenuModel) {
            ((TransMenuModel) iBaseModel).setCallback(null);
            ((TransMenuModel) this.mIBaseModel).clear();
        }
    }
}
