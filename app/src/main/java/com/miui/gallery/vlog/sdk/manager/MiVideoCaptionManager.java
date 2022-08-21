package com.miui.gallery.vlog.sdk.manager;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.caption.MiVideoCompoundCaption;
import com.miui.gallery.vlog.entity.Caption;
import com.miui.gallery.vlog.sdk.interfaces.BaseVlogManager;
import com.miui.gallery.vlog.sdk.interfaces.ICaptionManager;
import com.miui.gallery.vlog.sdk.models.NvsCompoundCaptionWrapper;
import com.miui.gallery.vlog.tools.BitmapUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.milab.videosdk.XmsTimeline;
import com.xiaomi.milab.videosdk.XmsVideoClip;
import com.xiaomi.milab.videosdk.XmsVideoTrack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class MiVideoCaptionManager extends BaseVlogManager implements ICaptionManager, OnDurationChangeListener {
    public List<NvsCompoundCaptionWrapper> mCaptions;
    public XmsVideoTrack mDecorationTrack;
    public String mDoubleStyleId;
    public float mLiveWindowHeight;
    public float mLiveWindowWidth;
    public String mSingleStyleId;
    public Snapshot mSnapshot;

    public MiVideoCaptionManager(MiVideoSdkManager miVideoSdkManager, XmsTimeline xmsTimeline) {
        super(miVideoSdkManager, xmsTimeline);
        this.mCaptions = new ArrayList();
        miVideoSdkManager.addDurationChangeListener(this);
    }

    public List<NvsCompoundCaptionWrapper> getAllCaptions() {
        return this.mCaptions;
    }

    public boolean hasCaption() {
        return this.mCaptions.size() > 0;
    }

    public void replaceCaptions(List<Caption> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        disconnect();
        clearAllCaption();
        for (Caption caption : list) {
            String str = caption.text;
            long j = caption.inPoint;
            addCaption(str, j, caption.outPoint - j);
        }
        reconnect();
    }

    public static String getWholeText(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        MiVideoCompoundCaption miVideoCompoundCaption;
        if (nvsCompoundCaptionWrapper == null || (miVideoCompoundCaption = nvsCompoundCaptionWrapper.mMiCaption) == null) {
            return null;
        }
        if (!TextUtils.isEmpty(miVideoCompoundCaption.getSecondLine())) {
            return nvsCompoundCaptionWrapper.mMiCaption.getFirstLine() + nvsCompoundCaptionWrapper.mMiCaption.getSecondLine();
        }
        return nvsCompoundCaptionWrapper.mMiCaption.getFirstLine();
    }

    public NvsCompoundCaptionWrapper addCaption(String str, long j, long j2) {
        String str2;
        String str3 = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.length() > 15) {
            String substring = str.substring(0, 15);
            str3 = str.substring(15);
            str2 = substring;
        } else {
            str2 = str;
        }
        return addCaption(str2, str3, j, j2);
    }

    public NvsCompoundCaptionWrapper addCaption(String str, String str2, long j, long j2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (j2 <= 0) {
            j2 = 3000000;
        }
        long min = Math.min(j2, rightBoundary(j) - j);
        if (min <= 0 || j >= this.mXmsTimeline.getDuration() * 1000) {
            return null;
        }
        long j3 = j + min;
        if (j3 > this.mXmsTimeline.getDuration() * 1000) {
            return null;
        }
        MiVideoSdkManager miVideoSdkManager = this.mSdkManager;
        Bitmap textAsBitmap = BitmapUtils.textAsBitmap(miVideoSdkManager.mContext, miVideoSdkManager.getLiveWindowValidSize()[0], str, str2, this.mSingleStyleId);
        NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper = new NvsCompoundCaptionWrapper();
        MiVideoCompoundCaption miVideoCompoundCaption = new MiVideoCompoundCaption();
        nvsCompoundCaptionWrapper.mMiCaption = miVideoCompoundCaption;
        miVideoCompoundCaption.setInPoint(j);
        nvsCompoundCaptionWrapper.mMiCaption.setOutPoint(j3);
        nvsCompoundCaptionWrapper.mMiCaption.setDuration(min);
        nvsCompoundCaptionWrapper.mMiCaption.setFirstLine(str);
        nvsCompoundCaptionWrapper.mMiCaption.setSecondLine(str2);
        nvsCompoundCaptionWrapper.mMiCaption.setRotation(SearchStatUtils.POW);
        updateCaptionBitmap(nvsCompoundCaptionWrapper, textAsBitmap);
        addCaptionFinal(nvsCompoundCaptionWrapper);
        this.mCaptions.add(nvsCompoundCaptionWrapper);
        return nvsCompoundCaptionWrapper;
    }

    public final void addCaptionFinal(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        MiVideoCompoundCaption miVideoCompoundCaption;
        if (nvsCompoundCaptionWrapper == null || (miVideoCompoundCaption = nvsCompoundCaptionWrapper.mMiCaption) == null || miVideoCompoundCaption.getCaptionBitmap() == null || nvsCompoundCaptionWrapper.mMiCaption.getInPoint() >= this.mXmsTimeline.getDuration() * 1000 || nvsCompoundCaptionWrapper.mMiCaption.getOutPoint() > this.mXmsTimeline.getDuration() * 1000) {
            return;
        }
        MiVideoCompoundCaption miVideoCompoundCaption2 = nvsCompoundCaptionWrapper.mMiCaption;
        miVideoCompoundCaption2.setBitmapWidth(miVideoCompoundCaption2.getBitmapWidth());
        miVideoCompoundCaption2.setBitmapHeight(miVideoCompoundCaption2.getBitmapHeight());
        XmsVideoTrack videoTrack = this.mXmsTimeline.getVideoTrack(0);
        if (this.mDecorationTrack == null) {
            this.mDecorationTrack = this.mXmsTimeline.appendDecorateTrack();
        }
        XmsVideoClip insertDecorationClip = this.mDecorationTrack.insertDecorationClip(miVideoCompoundCaption2.getCaptionBitmap(), miVideoCompoundCaption2.getInPoint() / 1000, miVideoCompoundCaption2.getOutPoint() / 1000);
        if (insertDecorationClip != null) {
            insertDecorationClip.appendVideoEffect("movit.filter.sticker_anim", miVideoCompoundCaption2.getJsonParamStr());
            this.mXmsTimeline.mixVideoTrack(videoTrack.getTrackIndex(), this.mDecorationTrack.getTrackIndex(), "movit.transition.blending", "alphablend");
        }
        nvsCompoundCaptionWrapper.mVideoClip = insertDecorationClip;
    }

    public void removeCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        XmsVideoTrack xmsVideoTrack = this.mDecorationTrack;
        if (xmsVideoTrack == null || this.mCaptions == null || nvsCompoundCaptionWrapper == null) {
            return;
        }
        xmsVideoTrack.removeDecorationClip(nvsCompoundCaptionWrapper.mVideoClip);
        this.mCaptions.remove(nvsCompoundCaptionWrapper);
    }

    public final NvsCompoundCaptionWrapper getCaptionByPosition(long j) {
        for (NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper : this.mCaptions) {
            if (nvsCompoundCaptionWrapper.mMiCaption.getInPoint() <= j && nvsCompoundCaptionWrapper.mMiCaption.getOutPoint() >= j) {
                return nvsCompoundCaptionWrapper;
            }
        }
        return null;
    }

    public void updateCaptionText(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper, String str) {
        if (nvsCompoundCaptionWrapper == null || nvsCompoundCaptionWrapper.mMiCaption == null) {
            return;
        }
        String str2 = null;
        if (str.length() > 15) {
            String substring = str.substring(0, 15);
            str2 = str.substring(15);
            str = substring;
        }
        MiVideoSdkManager miVideoSdkManager = this.mSdkManager;
        Bitmap textAsBitmap = BitmapUtils.textAsBitmap(miVideoSdkManager.mContext, miVideoSdkManager.getLiveWindowValidSize()[0], str, str2, this.mSingleStyleId);
        nvsCompoundCaptionWrapper.mMiCaption.setFirstLine(str);
        nvsCompoundCaptionWrapper.mMiCaption.setSecondLine(str2);
        updateCaptionBitmap(nvsCompoundCaptionWrapper, textAsBitmap);
        XmsVideoTrack xmsVideoTrack = this.mDecorationTrack;
        if (xmsVideoTrack != null) {
            xmsVideoTrack.removeDecorationClip(nvsCompoundCaptionWrapper.mVideoClip);
        }
        addCaptionFinal(nvsCompoundCaptionWrapper);
    }

    public final void updateCaptionBitmap(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper, Bitmap bitmap) {
        MiVideoCompoundCaption miVideoCompoundCaption;
        if (nvsCompoundCaptionWrapper == null || (miVideoCompoundCaption = nvsCompoundCaptionWrapper.mMiCaption) == null || bitmap == null) {
            return;
        }
        miVideoCompoundCaption.setCaptionBitmap(bitmap);
        nvsCompoundCaptionWrapper.mMiCaption.setBitmapWidth(bitmap.getWidth());
        nvsCompoundCaptionWrapper.mMiCaption.setBitmapHeight(bitmap.getHeight());
        setCaptionParamJsonStrAndTranYMap(nvsCompoundCaptionWrapper.mMiCaption);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final void setCaptionParamJsonStrAndTranYMap(MiVideoCompoundCaption miVideoCompoundCaption) {
        String str;
        MiVideoCaptionManager miVideoCaptionManager = this;
        if (miVideoCompoundCaption == null) {
            return;
        }
        int bitmapWidth = miVideoCompoundCaption.getBitmapWidth();
        int bitmapHeight = miVideoCompoundCaption.getBitmapHeight();
        int[] liveWindowValidSize = miVideoCaptionManager.mSdkManager.getLiveWindowValidSize();
        int originalWidth = miVideoCaptionManager.mSdkManager.getOriginalWidth();
        int originalHeight = miVideoCaptionManager.mSdkManager.getOriginalHeight();
        if (liveWindowValidSize == null || liveWindowValidSize.length != 2 || liveWindowValidSize[0] == 0 || liveWindowValidSize[1] == 0 || originalWidth == 0 || originalHeight == 0) {
            return;
        }
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        float dpToPixel = ScreenUtils.dpToPixel(20.0f);
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        ArrayList arrayList = new ArrayList();
        arrayList.add(0);
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(5);
        arrayList.add(6);
        arrayList.add(7);
        float f = 0.0f;
        try {
            Iterator it = arrayList.iterator();
            String str2 = "default";
            while (it.hasNext()) {
                Integer num = (Integer) it.next();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(nexExportFormat.TAG_FORMAT_WIDTH, bitmapWidth);
                jSONObject2.put(nexExportFormat.TAG_FORMAT_HEIGHT, bitmapHeight);
                Iterator it2 = it;
                float f2 = f;
                jSONObject2.put(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, SearchStatUtils.POW);
                jSONObject2.put("translateX", SearchStatUtils.POW);
                int i = liveWindowValidSize[0];
                int i2 = miVideoCaptionManager.mSdkManager.getRatioSize(i, liveWindowValidSize[1], num.intValue())[1];
                switch (num.intValue()) {
                    case 0:
                        str2 = "16x9";
                        str = str2;
                        break;
                    case 1:
                        str2 = "2.39x1";
                        str = str2;
                        break;
                    case 2:
                        str2 = "1x1";
                        str = str2;
                        break;
                    case 3:
                        str2 = "2.55x1";
                        str = str2;
                        break;
                    case 4:
                        str2 = "9x16";
                        str = str2;
                        break;
                    case 5:
                        str = "default";
                        break;
                    case 6:
                        str2 = "3x4";
                        str = str2;
                        break;
                    case 7:
                        str2 = "4x3";
                        str = str2;
                        break;
                    default:
                        str = str2;
                        break;
                }
                int i3 = (i2 / 2) * 2;
                float f3 = 1080.0f / i;
                float f4 = 1.0f;
                if (f3 <= 1.0f) {
                    f4 = f3;
                }
                float f5 = i3 != 0 ? ((-((i3 - bitmapHeight) - dpToPixel)) / 2.0f) / i3 : f2;
                hashMap.put(num, Float.valueOf(f5));
                hashMap2.put(num, Float.valueOf(f4));
                jSONObject2.put("scale", f4);
                jSONObject2.put(MapBundleKey.OfflineMapKey.OFFLINE_RATION, str);
                jSONObject2.put("translateY", f5);
                jSONArray.put(jSONObject2);
                miVideoCaptionManager = this;
                bitmapWidth = bitmapWidth;
                bitmapHeight = bitmapHeight;
                it = it2;
                str2 = str;
                f = f5;
            }
            jSONObject.put("supportRatios", jSONArray);
            miVideoCompoundCaption.setJsonParamStr(jSONObject.toString());
            miVideoCompoundCaption.setRatioToTransYMap(hashMap);
            miVideoCompoundCaption.setRatioToScaleMap(hashMap2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clearAllCaption() {
        for (NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper : this.mCaptions) {
            XmsVideoTrack xmsVideoTrack = this.mDecorationTrack;
            if (xmsVideoTrack != null) {
                xmsVideoTrack.removeDecorationClip(nvsCompoundCaptionWrapper.mVideoClip);
            }
        }
        this.mCaptions.clear();
    }

    public void dropSnapshot() {
        this.mSnapshot = null;
    }

    public void revoke() {
        disconnect();
        for (NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper : this.mCaptions) {
            XmsVideoTrack xmsVideoTrack = this.mDecorationTrack;
            if (xmsVideoTrack != null) {
                xmsVideoTrack.removeDecorationClip(nvsCompoundCaptionWrapper.mVideoClip);
            }
        }
        this.mCaptions.clear();
        Snapshot snapshot = this.mSnapshot;
        if (snapshot != null) {
            this.mSingleStyleId = snapshot.mSingleStyleId;
            this.mDoubleStyleId = this.mSnapshot.mDoubleStyleId;
            if (this.mSnapshot.mCaptionInfoList != null && this.mSnapshot.mCaptionInfoList.size() > 0) {
                for (CaptionInfo captionInfo : this.mSnapshot.mCaptionInfoList) {
                    addCaption(captionInfo.mFirstCaption, captionInfo.mSecondCaption, captionInfo.mInPoint, captionInfo.mOutPoint - captionInfo.mInPoint);
                }
            }
        }
        reconnect();
    }

    public void updateCaptionInOutPoint(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper, long j, long j2) {
        MiVideoCompoundCaption miVideoCompoundCaption;
        if (nvsCompoundCaptionWrapper == null || (miVideoCompoundCaption = nvsCompoundCaptionWrapper.mMiCaption) == null) {
            return;
        }
        long leftBoundary = leftBoundary(miVideoCompoundCaption.getInPoint() - 1);
        long rightBoundary = rightBoundary(nvsCompoundCaptionWrapper.mMiCaption.getOutPoint() + 1);
        nvsCompoundCaptionWrapper.mMiCaption.setInPoint(Math.max(leftBoundary, j));
        nvsCompoundCaptionWrapper.mMiCaption.setOutPoint(Math.min(rightBoundary, j2));
        MiVideoCompoundCaption miVideoCompoundCaption2 = nvsCompoundCaptionWrapper.mMiCaption;
        miVideoCompoundCaption2.setDuration(miVideoCompoundCaption2.getOutPoint() - nvsCompoundCaptionWrapper.mMiCaption.getInPoint());
        nvsCompoundCaptionWrapper.mVideoClip.setDecorationInAndOut(nvsCompoundCaptionWrapper.mMiCaption.getInPoint() / 1000, nvsCompoundCaptionWrapper.mMiCaption.getOutPoint() / 1000);
    }

    public final long leftBoundary(long j) {
        NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper = null;
        for (NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper2 : this.mCaptions) {
            MiVideoCompoundCaption miVideoCompoundCaption = nvsCompoundCaptionWrapper2.mMiCaption;
            if (miVideoCompoundCaption != null) {
                if (miVideoCompoundCaption.getInPoint() <= j && nvsCompoundCaptionWrapper2.mMiCaption.getOutPoint() >= j) {
                    return j;
                }
                if (nvsCompoundCaptionWrapper2.mMiCaption.getOutPoint() < j && (nvsCompoundCaptionWrapper == null || nvsCompoundCaptionWrapper2.mMiCaption.getOutPoint() > nvsCompoundCaptionWrapper.mMiCaption.getOutPoint())) {
                    nvsCompoundCaptionWrapper = nvsCompoundCaptionWrapper2;
                }
            }
        }
        if (nvsCompoundCaptionWrapper == null) {
            return 0L;
        }
        return nvsCompoundCaptionWrapper.mMiCaption.getOutPoint() + 1;
    }

    public final long rightBoundary(long j) {
        NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper = null;
        for (NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper2 : this.mCaptions) {
            MiVideoCompoundCaption miVideoCompoundCaption = nvsCompoundCaptionWrapper2.mMiCaption;
            if (miVideoCompoundCaption != null) {
                if (miVideoCompoundCaption.getInPoint() <= j && nvsCompoundCaptionWrapper2.mMiCaption.getOutPoint() >= j) {
                    return j;
                }
                if (nvsCompoundCaptionWrapper2.mMiCaption.getInPoint() > j && (nvsCompoundCaptionWrapper == null || nvsCompoundCaptionWrapper2.mMiCaption.getInPoint() < nvsCompoundCaptionWrapper.mMiCaption.getInPoint())) {
                    nvsCompoundCaptionWrapper = nvsCompoundCaptionWrapper2;
                }
            }
        }
        if (nvsCompoundCaptionWrapper == null) {
            return this.mXmsTimeline.getDuration() * 1000;
        }
        return nvsCompoundCaptionWrapper.mMiCaption.getInPoint() - 1;
    }

    public boolean hasCaptionAtTime(long j) {
        return getCaptionByPosition(j) != null;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.ICaptionManager
    public void applyCaptionStyle(String str, String str2) {
        disconnect();
        XmsVideoTrack xmsVideoTrack = this.mDecorationTrack;
        if (xmsVideoTrack != null) {
            xmsVideoTrack.removeAllClips();
        }
        for (NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper : this.mCaptions) {
            MiVideoSdkManager miVideoSdkManager = this.mSdkManager;
            updateCaptionBitmap(nvsCompoundCaptionWrapper, BitmapUtils.textAsBitmap(miVideoSdkManager.mContext, miVideoSdkManager.getLiveWindowValidSize()[0], nvsCompoundCaptionWrapper.mMiCaption.getFirstLine(), nvsCompoundCaptionWrapper.mMiCaption.getSecondLine(), str));
            addCaptionFinal(nvsCompoundCaptionWrapper);
        }
        reconnect();
        this.mSingleStyleId = str;
        this.mDoubleStyleId = str2;
    }

    public int checkAddCaptionStatusAtTime(long j) {
        if (hasCaptionAtTime(j)) {
            return 1;
        }
        return rightBoundary(j) < j + 500000 ? 2 : 0;
    }

    public final void checkCaptions() {
        if (this.mCaptions == null) {
            return;
        }
        long duration = this.mSdkManager.getDuration();
        ArrayList arrayList = new ArrayList();
        for (NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper : this.mCaptions) {
            MiVideoCompoundCaption miVideoCompoundCaption = nvsCompoundCaptionWrapper.mMiCaption;
            if (miVideoCompoundCaption != null) {
                if (miVideoCompoundCaption.getInPoint() >= duration - 500000) {
                    XmsVideoTrack xmsVideoTrack = this.mDecorationTrack;
                    if (xmsVideoTrack != null) {
                        xmsVideoTrack.removeDecorationClip(nvsCompoundCaptionWrapper.mVideoClip);
                    }
                } else {
                    if (nvsCompoundCaptionWrapper.mMiCaption.getOutPoint() > duration) {
                        MiVideoCompoundCaption miVideoCompoundCaption2 = nvsCompoundCaptionWrapper.mMiCaption;
                        miVideoCompoundCaption2.setOutPoint(Math.min(miVideoCompoundCaption2.getOutPoint(), duration));
                        nvsCompoundCaptionWrapper.mVideoClip.setInAndOut(nvsCompoundCaptionWrapper.mMiCaption.getInPoint() / 1000, nvsCompoundCaptionWrapper.mMiCaption.getOutPoint() / 1000);
                    }
                    arrayList.add(nvsCompoundCaptionWrapper);
                }
            }
        }
        this.mCaptions = arrayList;
    }

    @Override // com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener
    public void onDurationChanged() {
        checkCaptions();
    }

    public void captureSnapshot() {
        ArrayList arrayList = new ArrayList();
        for (NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper : this.mCaptions) {
            CaptionInfo captionInfo = new CaptionInfo();
            captionInfo.mInPoint = nvsCompoundCaptionWrapper.mMiCaption.getInPoint();
            captionInfo.mOutPoint = nvsCompoundCaptionWrapper.mMiCaption.getOutPoint();
            captionInfo.mFirstCaption = nvsCompoundCaptionWrapper.mMiCaption.getFirstLine();
            captionInfo.mSecondCaption = nvsCompoundCaptionWrapper.mMiCaption.getSecondLine();
            arrayList.add(captionInfo);
        }
        Snapshot snapshot = new Snapshot();
        this.mSnapshot = snapshot;
        snapshot.mCaptionInfoList = arrayList;
        this.mSnapshot.mSingleStyleId = this.mSingleStyleId;
        this.mSnapshot.mDoubleStyleId = this.mDoubleStyleId;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.ICaptionManager
    public String getSingleStyleId() {
        return this.mSingleStyleId;
    }

    public void updateLiveWindowSize(float f, float f2) {
        this.mLiveWindowWidth = f;
        this.mLiveWindowHeight = f2;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.ICaptionManager
    public void reAddCaptions() {
        disconnect();
        XmsVideoTrack xmsVideoTrack = this.mDecorationTrack;
        if (xmsVideoTrack != null) {
            xmsVideoTrack.removeAllClips();
        }
        Iterator<NvsCompoundCaptionWrapper> it = this.mCaptions.iterator();
        while (it.hasNext()) {
            NvsCompoundCaptionWrapper next = it.next();
            if (next.mMiCaption.getInPoint() >= this.mXmsTimeline.getDuration() * 1000 || next.mMiCaption.getOutPoint() > this.mXmsTimeline.getDuration() * 1000) {
                it.remove();
            } else {
                addCaptionFinal(next);
            }
        }
        reconnect();
    }

    /* loaded from: classes2.dex */
    public static class Snapshot {
        public List<CaptionInfo> mCaptionInfoList;
        public String mDoubleStyleId;
        public String mSingleStyleId;

        public Snapshot() {
        }
    }

    /* loaded from: classes2.dex */
    public static class CaptionInfo {
        public String mFirstCaption;
        public long mInPoint;
        public long mOutPoint;
        public String mSecondCaption;

        public CaptionInfo() {
        }
    }
}
