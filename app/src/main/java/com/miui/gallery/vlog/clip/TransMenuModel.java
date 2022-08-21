package com.miui.gallery.vlog.clip;

import com.miui.gallery.net.resource.CommonResponse;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.entity.TransData;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TransMenuModel implements IBaseModel {
    public Callback mCallback;
    public ArrayList<TransData> mTransDataList = new ArrayList<>();
    public CommonResponse.Callback<TransData> mResourceCallback = new CommonResponse.Callback<TransData>() { // from class: com.miui.gallery.vlog.clip.TransMenuModel.1
        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onSuccess(List<TransData> list) {
            if (BaseMiscUtil.isValid(list)) {
                DefaultLogger.d("TransMenuModel", "load data success.");
                TransMenuModel.this.mTransDataList.clear();
                TransMenuModel.this.mTransDataList.add(TransData.getDefaultItem());
                TransMenuModel.this.mTransDataList.addAll(list);
                if (TransMenuModel.this.mCallback == null) {
                    return;
                }
                TransMenuModel.this.mCallback.loadDataSuccess(TransMenuModel.this.mTransDataList);
            }
        }

        @Override // com.miui.gallery.net.resource.CommonResponse.Callback
        public void onFail() {
            DefaultLogger.e("TransMenuModel", "load data fail.");
            if (TransMenuModel.this.mCallback != null) {
                TransMenuModel.this.mCallback.loadDataFail();
            }
        }
    };
    public TransRequest mTransRequest = new TransRequest();
    public CommonResponse mCommonResponse = new CommonResponse(this.mResourceCallback);

    /* loaded from: classes2.dex */
    public interface Callback {
        void loadDataFail();

        void loadDataSuccess(ArrayList<TransData> arrayList);
    }

    public TransMenuModel(Callback callback) {
        this.mCallback = callback;
    }

    public void clear() {
        if (this.mCallback != null) {
            this.mCallback = null;
        }
        CommonResponse commonResponse = this.mCommonResponse;
        if (commonResponse != null) {
            commonResponse.setCallback(null);
        }
        TransRequest transRequest = this.mTransRequest;
        if (transRequest != null) {
            transRequest.cancel();
            this.mTransRequest = null;
        }
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IBaseModel
    public void loadData() {
        this.mTransDataList.clear();
        add("vlog_trans_none", R$drawable.vlog_tans_icon_none, "type_none", null);
        add("vlog_trans_heichang", R$drawable.vlog_trans_heichang, "type_extra", "movit.transition.fade.black");
        add("vlog_trans_diehua", R$drawable.vlog_trans_diehua, "type_extra", "movit.transition.alpha");
        add("vlog_trans_fangda", R$drawable.vlog_trans_fangda, "type_extra", "movit.transition.scale.radical.blur.zoomin");
        add("vlog_trans_suoxiao", R$drawable.vlog_trans_suoxiao, "type_extra", "movit.transition.scale.radical.blur.zoomout");
        add("vlog_trans_fangda_rotate", R$drawable.vlog_trans_fangda_rotate, "type_extra", "movit.transition.scale.rotation.up");
        add("vlog_trans_suoxiao_rotate", R$drawable.vlog_trans_suoxiao_rotate, "type_extra", "movit.transition.scale.rotation.down");
        add("vlog_trans_mohu", R$drawable.vlog_trans_mohu, "type_extra", "movit.transition.mean.blur");
        add("vlog_trans_right", R$drawable.vlog_trans_right, "type_extra", "movit.transition.oritation.blur.right");
        add("vlog_trans_left", R$drawable.vlog_trans_left, "type_extra", "movit.transition.oritation.blur.left");
        add("vlog_trans_up", R$drawable.vlog_trans_up, "type_extra", "movit.transition.oritation.blur.up");
        add("vlog_trans_down", R$drawable.vlog_trans_down, "type_extra", "movit.transition.oritation.blur.down");
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.loadDataSuccess(this.mTransDataList);
        }
    }

    public final void add(String str, int i, String str2, String str3) {
        TransData transData = new TransData();
        transData.nameKey = str;
        transData.imageId = i;
        transData.type = str2;
        transData.setDownloadState(17);
        transData.setTransName("movit.transition");
        transData.setTransPath(str3);
        this.mTransDataList.add(transData);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
