package com.miui.gallery.vlog.caption.entity;

import java.util.List;

/* loaded from: classes2.dex */
public class HeaderTailInfoEntity {
    private int analyticType;
    private boolean isDynamic;
    private int lineCount;
    private List<String> materialList;
    private String mixerName;
    private String mixerParam;
    private String param;
    private List<HeaderTailTextListBean> textList;

    public void setEntity(HeaderTailInfoEntity headerTailInfoEntity) {
        if (headerTailInfoEntity == null) {
            return;
        }
        this.param = headerTailInfoEntity.param;
        this.mixerName = headerTailInfoEntity.mixerName;
        this.mixerParam = headerTailInfoEntity.mixerParam;
        this.isDynamic = headerTailInfoEntity.isDynamic;
        this.analyticType = headerTailInfoEntity.analyticType;
        this.lineCount = headerTailInfoEntity.lineCount;
        this.textList = headerTailInfoEntity.textList;
        this.materialList = headerTailInfoEntity.materialList;
    }

    public String getParam() {
        return this.param;
    }

    public void setParam(String str) {
        this.param = str;
    }

    public String getMixerName() {
        return this.mixerName;
    }

    public void setMixerName(String str) {
        this.mixerName = str;
    }

    public String getMixerParam() {
        return this.mixerParam;
    }

    public void setMixerParam(String str) {
        this.mixerParam = str;
    }

    public boolean isIsDynamic() {
        return this.isDynamic;
    }

    public void setIsDynamic(boolean z) {
        this.isDynamic = z;
    }

    public int getAnalyticType() {
        return this.analyticType;
    }

    public void setAnalyticType(int i) {
        this.analyticType = i;
    }

    public int getLineCount() {
        return this.lineCount;
    }

    public void setLineCount(int i) {
        this.lineCount = i;
    }

    public List<HeaderTailTextListBean> getTextList() {
        return this.textList;
    }

    public void setTextList(List<HeaderTailTextListBean> list) {
        this.textList = list;
    }

    public List<String> getMaterialList() {
        return this.materialList;
    }

    public void setMaterialList(List<String> list) {
        this.materialList = list;
    }
}
