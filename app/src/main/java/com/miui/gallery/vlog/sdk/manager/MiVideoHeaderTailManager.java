package com.miui.gallery.vlog.sdk.manager;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.caption.entity.HeaderTailInfoEntity;
import com.miui.gallery.vlog.caption.entity.HeaderTailPermanentEntity;
import com.miui.gallery.vlog.caption.entity.HeaderTailStaticEntity;
import com.miui.gallery.vlog.caption.entity.HeaderTailTextListBean;
import com.miui.gallery.vlog.sdk.interfaces.BaseVlogManager;
import com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager;
import com.miui.gallery.vlog.sdk.interfaces.IRemoveHeadTailCallback;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.xiaomi.milab.videosdk.XmsDecorateTrack;
import com.xiaomi.milab.videosdk.XmsTimeline;
import com.xiaomi.milab.videosdk.XmsVideoClip;
import com.xiaomi.milab.videosdk.XmsVideoTrack;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import miuix.core.util.FileUtils;

/* loaded from: classes2.dex */
public class MiVideoHeaderTailManager extends BaseVlogManager implements IHeaderTailManager, OnDurationChangeListener {
    public final String FOLDER_PATH_ASSET;
    public final int TYPE_ANALYTIC_ONE;
    public final int TYPE_ANALYTIC_THREE;
    public final int TYPE_ANALYTIC_TWO;
    public XmsVideoClip mDecorateClip;
    public XmsDecorateTrack mDecorateTrack;
    public String mFolderPath;
    public Gson mGson;
    public HeaderTailInfoEntity mHeaderTailInfoEntity;
    public String mHeaderTailLabel;
    public String mHeaderTailParam;
    public int mHeaderTailStatus;
    public IRemoveHeadTailCallback mIRemoveHeadTail;
    public String mMixerName;
    public String mMixerParam;
    public XmsDecorateTrack mPermanentDecorateTrack;
    public List<HeaderTailPermanentEntity> mPermanentEntityList;
    public String mPermanentFolderPath;
    public int mResourceType;

    public final boolean isCaptionType(int i) {
        return i == 1 || i == 2;
    }

    public MiVideoHeaderTailManager(MiVideoSdkManager miVideoSdkManager, XmsTimeline xmsTimeline) {
        super(miVideoSdkManager, xmsTimeline);
        this.FOLDER_PATH_ASSET = "headertail/custom";
        this.TYPE_ANALYTIC_ONE = 0;
        this.TYPE_ANALYTIC_TWO = 1;
        this.TYPE_ANALYTIC_THREE = 2;
        this.mPermanentEntityList = new ArrayList();
        miVideoSdkManager.addDurationChangeListener(this);
        this.mGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public void setHeaderTail(boolean z, String str, int i, String str2) {
        setHeaderTail(z, str, i, 5000000L, str2);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public void setHeaderTail(boolean z, String str, int i, long j, String str2) {
        if (str == null || str.length() == 0 || this.mXmsTimeline == null) {
            return;
        }
        this.mHeaderTailStatus = z ? 1 : 2;
        this.mFolderPath = str;
        this.mResourceType = i;
        if (i == 3) {
            this.mHeaderTailInfoEntity = null;
            setDynamicHeaderTail(z, str);
        } else if ("headertail/custom".equals(str) || i == 4) {
            HeaderTailInfoEntity headerTailInfoEntity = this.mHeaderTailInfoEntity;
            if (headerTailInfoEntity != null && headerTailInfoEntity.getTextList().size() > 0) {
                setCustomHeaderTail(z, this.mHeaderTailInfoEntity.getTextList().get(0).getText());
            }
        } else {
            this.mHeaderTailInfoEntity = null;
            setStaticHeaderTail(z, str);
        }
        setHeadTailLabel(str2);
    }

    public final void setHeaderTail(boolean z, String str, String str2, String str3, String str4) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (z && !new File(str).exists()) {
            return;
        }
        XmsTimeline xmsTimeline = this.mXmsTimeline;
        if (xmsTimeline == null) {
            return;
        }
        if (this.mDecorateTrack == null) {
            this.mDecorateTrack = xmsTimeline.appendDecorateTrack();
        }
        boolean z2 = false;
        if (this.mXmsTimeline.getVideoTrack(0) == null) {
            return;
        }
        long duration = this.mXmsTimeline.getVideoTrack(0).getDuration();
        if (this.mHeaderTailStatus == 1) {
            z2 = true;
        }
        setHeaderTail(this.mDecorateTrack, true, z2 ? 0L : duration - 5000, z2 ? 5000L : duration, z, str, str2, str3, str4);
    }

    public final void setHeaderTail(XmsDecorateTrack xmsDecorateTrack, boolean z, long j, long j2, boolean z2, String str, String str2, String str3, String str4) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (z2 && !new File(str).exists()) {
            return;
        }
        XmsTimeline xmsTimeline = this.mXmsTimeline;
        if (xmsTimeline == null || xmsDecorateTrack == null) {
            return;
        }
        XmsVideoTrack videoTrack = xmsTimeline.getVideoTrack(0);
        if (j >= videoTrack.getDuration()) {
            return;
        }
        long j3 = 0;
        if (j >= 0) {
            j3 = j;
        }
        long duration = j2 > videoTrack.getDuration() ? videoTrack.getDuration() : j2;
        disconnect();
        if (z) {
            xmsDecorateTrack.removeAllClips();
        }
        if (z2) {
            this.mDecorateClip = xmsDecorateTrack.insertDecorationClip(str, j3, duration);
        } else {
            this.mDecorateClip = xmsDecorateTrack.insertSeqframeClip(str, j3, duration);
        }
        if (!TextUtils.isEmpty(str2)) {
            this.mDecorateClip.appendVideoEffect("movit.filter.sticker_anim", str2);
        }
        this.mXmsTimeline.mixVideoTrack(videoTrack.getTrackIndex(), xmsDecorateTrack.getTrackIndex(), str3, str4);
        reconnect();
    }

    public void setHeaderTail(HeaderTailInfoEntity headerTailInfoEntity) {
        String materialFromInfoEntity;
        if (headerTailInfoEntity == null || this.mXmsTimeline == null) {
            return;
        }
        if ("headertail/custom".equals(this.mFolderPath)) {
            materialFromInfoEntity = getMaterialFromAsset(headerTailInfoEntity.getTextList().get(0).getText());
        } else {
            materialFromInfoEntity = getMaterialFromInfoEntity(this.mFolderPath, headerTailInfoEntity);
        }
        setHeaderTail(false, materialFromInfoEntity, this.mHeaderTailParam, this.mMixerName, this.mMixerParam);
    }

    public final String getMaterialFromAsset(String str) {
        HeaderTailStaticEntity headerTailStaticEntity = (HeaderTailStaticEntity) this.mGson.fromJson(VlogUtils.getJsonFromAssetDir(VlogUtils.getGalleryApp(), "headertail/custom/material.json"), (Class<Object>) HeaderTailStaticEntity.class);
        headerTailStaticEntity.getAnimList().get(0).setText(str);
        return this.mGson.toJson(headerTailStaticEntity);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0210 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0211  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.String getMaterialFromInfoEntity(java.lang.String r11, com.miui.gallery.vlog.caption.entity.HeaderTailInfoEntity r12) {
        /*
            Method dump skipped, instructions count: 537
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.vlog.sdk.manager.MiVideoHeaderTailManager.getMaterialFromInfoEntity(java.lang.String, com.miui.gallery.vlog.caption.entity.HeaderTailInfoEntity):java.lang.String");
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public void setHeaderTail(boolean z, String str, HeaderTailInfoEntity headerTailInfoEntity, String str2) {
        try {
            this.mHeaderTailStatus = z ? 1 : 2;
            this.mFolderPath = str;
            this.mHeaderTailInfoEntity = headerTailInfoEntity;
            if (headerTailInfoEntity != null && headerTailInfoEntity.getMaterialList() != null && this.mHeaderTailInfoEntity.getMaterialList().size() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                String str3 = File.separator;
                sb.append(str3);
                sb.append(this.mHeaderTailInfoEntity.getParam());
                String sb2 = sb.toString();
                if (!new File(sb2).exists()) {
                    return;
                }
                this.mHeaderTailParam = FileUtils.readFileAsString(sb2);
                this.mMixerName = this.mHeaderTailInfoEntity.getMixerName();
                this.mMixerParam = this.mHeaderTailInfoEntity.getMixerParam();
                if (this.mHeaderTailInfoEntity.isIsDynamic()) {
                    String str4 = str + str3 + this.mHeaderTailInfoEntity.getMaterialList().get(0);
                    if (new File(str4).exists()) {
                        setHeaderTail(true, str4, this.mHeaderTailParam, this.mMixerName, this.mMixerParam);
                    }
                } else {
                    setHeaderTail(false, getMaterialFromInfoEntity(this.mFolderPath, this.mHeaderTailInfoEntity), this.mHeaderTailParam, this.mMixerName, this.mMixerParam);
                }
                setHeadTailLabel(str2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public void setHeaderTail(boolean z, String str, HeaderTailPermanentEntity headerTailPermanentEntity) {
        XmsTimeline xmsTimeline;
        try {
            if (!TextUtils.isEmpty(str) && headerTailPermanentEntity != null && (xmsTimeline = this.mXmsTimeline) != null) {
                this.mPermanentFolderPath = str;
                if (this.mPermanentDecorateTrack == null) {
                    this.mPermanentDecorateTrack = xmsTimeline.appendDecorateTrack();
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                String str2 = File.separator;
                sb.append(str2);
                sb.append(headerTailPermanentEntity.getName());
                String sb2 = sb.toString();
                String str3 = null;
                if (!TextUtils.isEmpty(headerTailPermanentEntity.getParam())) {
                    str3 = FileUtils.readFileAsString(str + str2 + headerTailPermanentEntity.getParam());
                }
                String str4 = str3;
                if (!headerTailPermanentEntity.isDynamic()) {
                    sb2 = FileUtils.readFileAsString(sb2);
                }
                setHeaderTail(this.mPermanentDecorateTrack, false, headerTailPermanentEntity.getInTime(), headerTailPermanentEntity.getOutTime(), headerTailPermanentEntity.isDynamic(), sb2, str4, headerTailPermanentEntity.getMixerName(), headerTailPermanentEntity.getMixerParam());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public void setPermanentEntityList(List<HeaderTailPermanentEntity> list) {
        this.mPermanentEntityList.clear();
        if (list != null) {
            this.mPermanentEntityList.addAll(list);
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public void setCustomHeaderTail(boolean z, String str) {
        this.mHeaderTailStatus = z ? 1 : 2;
        this.mResourceType = 1;
        this.mFolderPath = "headertail/custom";
        String jsonFromAssetDir = VlogUtils.getJsonFromAssetDir(VlogUtils.getGalleryApp(), "headertail/custom/info.json");
        String jsonFromAssetDir2 = VlogUtils.getJsonFromAssetDir(VlogUtils.getGalleryApp(), "headertail/custom/param.json");
        HeaderTailInfoEntity headerTailInfoEntity = (HeaderTailInfoEntity) this.mGson.fromJson(jsonFromAssetDir, (Class<Object>) HeaderTailInfoEntity.class);
        this.mHeaderTailInfoEntity = headerTailInfoEntity;
        this.mHeaderTailParam = jsonFromAssetDir2;
        this.mMixerName = headerTailInfoEntity.getMixerName();
        this.mMixerParam = this.mHeaderTailInfoEntity.getMixerParam();
        this.mHeaderTailInfoEntity.getTextList().get(0).setText(str);
        setHeaderTail(false, getMaterialFromAsset(str), this.mHeaderTailParam, this.mMixerName, this.mMixerParam);
        setHeadTailLabel("custom");
    }

    public final void setStaticHeaderTail(boolean z, String str) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            String str2 = File.separator;
            sb.append(str2);
            sb.append("info.json");
            String sb2 = sb.toString();
            if (!new File(sb2).exists()) {
                return;
            }
            HeaderTailInfoEntity headerTailInfoEntity = (HeaderTailInfoEntity) this.mGson.fromJson((Reader) new FileReader(sb2), (Class<Object>) HeaderTailInfoEntity.class);
            this.mHeaderTailInfoEntity = headerTailInfoEntity;
            if (headerTailInfoEntity == null) {
                return;
            }
            String str3 = str + str2 + this.mHeaderTailInfoEntity.getParam();
            if (!new File(str3).exists()) {
                return;
            }
            this.mHeaderTailParam = FileUtils.readFileAsString(str3);
            this.mMixerName = this.mHeaderTailInfoEntity.getMixerName();
            this.mMixerParam = this.mHeaderTailInfoEntity.getMixerParam();
            setHeaderTail(this.mHeaderTailInfoEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void setDynamicHeaderTail(boolean z, String str) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            String str2 = File.separator;
            sb.append(str2);
            sb.append("info.json");
            String sb2 = sb.toString();
            if (!new File(sb2).exists()) {
                return;
            }
            HeaderTailInfoEntity headerTailInfoEntity = (HeaderTailInfoEntity) this.mGson.fromJson((Reader) new FileReader(sb2), (Class<Object>) HeaderTailInfoEntity.class);
            this.mHeaderTailInfoEntity = headerTailInfoEntity;
            if (headerTailInfoEntity == null) {
                return;
            }
            String str3 = str + str2 + this.mHeaderTailInfoEntity.getMaterialList().get(0);
            String str4 = str + str2 + this.mHeaderTailInfoEntity.getParam();
            if (new File(str3).exists() && new File(str4).exists()) {
                this.mHeaderTailParam = FileUtils.readFileAsString(str4);
                this.mMixerName = this.mHeaderTailInfoEntity.getMixerName();
                String mixerParam = this.mHeaderTailInfoEntity.getMixerParam();
                this.mMixerParam = mixerParam;
                setHeaderTail(true, str3, this.mHeaderTailParam, this.mMixerName, mixerParam);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public void removeHeaderTail() {
        if (this.mXmsTimeline == null) {
            return;
        }
        XmsDecorateTrack xmsDecorateTrack = this.mDecorateTrack;
        if (xmsDecorateTrack != null) {
            xmsDecorateTrack.removeAllClips();
        }
        this.mDecorateClip = null;
        this.mHeaderTailStatus = 0;
        this.mFolderPath = null;
        this.mResourceType = 0;
        this.mHeaderTailInfoEntity = null;
        setHeadTailLabel("");
        IRemoveHeadTailCallback iRemoveHeadTailCallback = this.mIRemoveHeadTail;
        if (iRemoveHeadTailCallback == null) {
            return;
        }
        iRemoveHeadTailCallback.onRemoved();
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public void removePermanentHeaderTail() {
        this.mPermanentEntityList.clear();
        XmsDecorateTrack xmsDecorateTrack = this.mPermanentDecorateTrack;
        if (xmsDecorateTrack != null) {
            xmsDecorateTrack.removeAllClips();
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public void setIRemoveHeadTail(IRemoveHeadTailCallback iRemoveHeadTailCallback) {
        this.mIRemoveHeadTail = iRemoveHeadTailCallback;
    }

    public long getInPoint() {
        XmsVideoClip xmsVideoClip;
        XmsDecorateTrack xmsDecorateTrack = this.mDecorateTrack;
        if (xmsDecorateTrack == null || (xmsVideoClip = this.mDecorateClip) == null) {
            return 0L;
        }
        return xmsDecorateTrack.getClipStartPos(xmsVideoClip.getIndex()) * 1000;
    }

    public long getOutPoint() {
        long duration;
        XmsVideoClip xmsVideoClip;
        XmsDecorateTrack xmsDecorateTrack = this.mDecorateTrack;
        if (xmsDecorateTrack != null && (xmsVideoClip = this.mDecorateClip) != null) {
            duration = xmsDecorateTrack.getClipStartPos(xmsVideoClip.getIndex()) + this.mDecorateClip.getDuration();
        } else {
            duration = this.mXmsTimeline.getDuration();
        }
        return duration * 1000;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public void setHeaderTailText(String str, String str2) {
        HeaderTailInfoEntity headerTailInfoEntity;
        if (isCaptionType(this.mResourceType) && (headerTailInfoEntity = this.mHeaderTailInfoEntity) != null) {
            if (headerTailInfoEntity.getTextList().size() >= 1 && !TextUtils.isEmpty(str)) {
                HeaderTailTextListBean headerTailTextListBean = this.mHeaderTailInfoEntity.getTextList().get(0);
                if (str == null) {
                    str = "";
                }
                headerTailTextListBean.setText(str);
            }
            if (this.mHeaderTailInfoEntity.getTextList().size() >= 2 && !TextUtils.isEmpty(str2)) {
                HeaderTailTextListBean headerTailTextListBean2 = this.mHeaderTailInfoEntity.getTextList().get(1);
                if (str2 == null) {
                    str2 = "";
                }
                headerTailTextListBean2.setText(str2);
            }
            setHeaderTail(this.mHeaderTailInfoEntity);
        }
    }

    public void exchangeHeaderTail(boolean z) {
        if (!hasHeaderTail()) {
            return;
        }
        this.mHeaderTailStatus = z ? 1 : 2;
        int i = this.mResourceType;
        if (i == 3) {
            setHeaderTail(z, this.mFolderPath, i, this.mHeaderTailLabel);
        } else {
            setHeaderTail(this.mHeaderTailInfoEntity);
        }
    }

    public String getHeaderTailText(int i) {
        HeaderTailInfoEntity headerTailInfoEntity;
        if (isCaptionType(this.mResourceType) && (headerTailInfoEntity = this.mHeaderTailInfoEntity) != null && headerTailInfoEntity.getTextList().size() > i) {
            return this.mHeaderTailInfoEntity.getTextList().get(i).getText();
        }
        return null;
    }

    public int getHeaderTailStatus() {
        return this.mHeaderTailStatus;
    }

    public boolean hasHeaderTail() {
        return (this.mDecorateTrack == null || this.mDecorateClip == null) ? false : true;
    }

    public boolean hasHeader() {
        return this.mHeaderTailStatus == 1;
    }

    public boolean hasTail() {
        return this.mHeaderTailStatus == 2;
    }

    public void setHeadTailLabel(String str) {
        this.mHeaderTailLabel = str;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager
    public String getHeadTailLabel() {
        return this.mHeaderTailLabel;
    }

    @Override // com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener
    public void onDurationChanged() {
        XmsTimeline xmsTimeline;
        if (hasHeader() && (xmsTimeline = this.mXmsTimeline) != null && xmsTimeline.getVideoTrack(0).getDuration() < 5000000) {
            setHeaderTail(true, this.mFolderPath, this.mResourceType, this.mHeaderTailLabel);
        } else if (hasTail()) {
            setHeaderTail(false, this.mFolderPath, this.mResourceType, this.mHeaderTailLabel);
        }
        if (TextUtils.isEmpty(this.mPermanentFolderPath) || this.mPermanentEntityList.size() <= 0) {
            return;
        }
        XmsDecorateTrack xmsDecorateTrack = this.mPermanentDecorateTrack;
        if (xmsDecorateTrack != null) {
            xmsDecorateTrack.removeAllClips();
        }
        for (int i = 0; i < this.mPermanentEntityList.size(); i++) {
            setHeaderTail(true, this.mPermanentFolderPath, this.mPermanentEntityList.get(i));
        }
    }
}
