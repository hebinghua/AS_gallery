package com.nexstreaming.nexeditorsdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import com.nexstreaming.app.common.task.ResultTask;
import com.nexstreaming.app.common.task.Task;
import com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo;
import com.nexstreaming.kminternal.kinemaster.mediainfo.h;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader;
import com.nexstreaming.nexeditorsdk.exception.ClipIsNotVideoException;
import com.nexstreaming.nexeditorsdk.exception.ProjectNotAttachedException;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class nexClip implements Cloneable {
    public static final int AVC_Profile_Baseline = 66;
    public static final int AVC_Profile_Extended = 88;
    public static final int AVC_Profile_High = 100;
    public static final int AVC_Profile_High10 = 100;
    public static final int AVC_Profile_High422 = 122;
    public static final int AVC_Profile_High444 = 244;
    public static final int AVC_Profile_Main = 77;
    public static final int AVC_Profile_Unknown = 0;
    public static final int HDR_TYPE_10HLG = 18;
    public static final int HDR_TYPE_10PQ = 16;
    private static final String TAG = "nexClip";
    public static final int kCLIP_Supported = 0;
    public static final int kCLIP_TYPE_AUDIO = 3;
    public static final int kCLIP_TYPE_IMAGE = 1;
    public static final int kCLIP_TYPE_NONE = 0;
    public static final int kCLIP_TYPE_VIDEO = 4;
    public static final int kCLIP_VIDEORENDERMODE_360VIDE = 1;
    public static final int kCLIP_VIDEORENDERMODE_NORMAL = 0;
    public static final int kClip_NotSupported = 12;
    public static final int kClip_NotSupported_AudioCodec = 2;
    public static final int kClip_NotSupported_AudioProfile = 3;
    public static final int kClip_NotSupported_Container = 4;
    public static final int kClip_NotSupported_DurationTooShort = 6;
    public static final int kClip_NotSupported_ResolutionTooHigh = 5;
    public static final int kClip_NotSupported_ResolutionTooLow = 7;
    public static final int kClip_NotSupported_VideoCodec = 9;
    public static final int kClip_NotSupported_VideoFPS = 10;
    public static final int kClip_NotSupported_VideoLevel = 11;
    public static final int kClip_NotSupported_VideoProfile = 8;
    public static final int kClip_Rotate_0 = 0;
    public static final int kClip_Rotate_180 = 180;
    public static final int kClip_Rotate_270 = 270;
    public static final int kClip_Rotate_90 = 90;
    public static final int kClip_Supported_NeedFPSTranscoding = 14;
    public static final int kClip_Supported_NeedResolutionTranscoding = 13;
    public static final int kClip_Supported_Unknown = 1;
    private static long sVideoClipDetailThumbnailsDiskLimitSize = 100000000;
    private int count;
    private int index;
    private boolean isMotionTrackedVideo;
    private int mAVSyncAudioStartTime;
    private nexAudioEdit mAudioEdit;
    private nexAudioEnvelop mAudioEnvelop;
    private boolean mAudioOnOff;
    private nexClipEffect mClipEffect;
    private String mCollageDrawInfoID;
    private nexColorEffect mColorEffect;
    private nexCrop mCrop;
    private int mCustomLUT_A;
    private int mCustomLUT_B;
    private int mCustomLUT_Power;
    private List<nexDrawInfo> mDrawInfos;
    public int mDuration;
    public int mEndTime;
    private int mFaceDetected;
    private Rect mFaceRect;
    private boolean mFacedetectProcessed;
    private ClipInfo mInfo;
    private boolean mIsAssetResource;
    private boolean mMediaInfoUseCache;
    private b mObserver;
    private boolean mOverlappedTransition;
    private String mPath;
    private boolean mProjectAttachment;
    public boolean mPropertySlowVideoMode;
    private int mRotate;
    private transient WeakReference<Bitmap> mSingleThumbnail;
    public int mStartTime;
    private int mTemplateAudioPos;
    private int mTemplateEffectID;
    private h mThumbnails;
    public int mTitleEffectEndTime;
    public int mTitleEffectStartTime;
    private String mTransCodingPath;
    private nexTransitionEffect mTransitionEffect;
    private nexVideoClipEdit mVideoEdit;
    private boolean mVignette;
    private int m_BGMVolume;
    private int m_Brightness;
    private int m_ClipVolume;
    private int m_Contrast;
    private int m_Saturation;
    private boolean m_getThumbnailsFailed;
    private transient boolean m_gettingPcmData;
    private boolean m_gettingThumbnails;
    private MediaInfo mediainfo;
    private boolean misMustDownSize;

    /* loaded from: classes3.dex */
    public static abstract class OnGetAudioPcmLevelsResultListener {
        public abstract void onGetAudioPcmLevelsResult(byte[] bArr);
    }

    /* loaded from: classes3.dex */
    public static abstract class OnGetVideoClipDetailThumbnailsListener {
        public static int kEvent_Completed = 1;
        public static int kEvent_Fail = -1;
        public static int kEvent_Ok = 0;
        public static int kEvent_UserCancel = -3;
        public static int kEvent_systemError = -2;

        public abstract void onGetDetailThumbnailResult(int i, Bitmap bitmap, int i2, int i3, int i4);
    }

    @Deprecated
    /* loaded from: classes3.dex */
    public static abstract class OnGetVideoClipIDR2YOnlyThumbnailsListener {
        public static int kEvent_Completed = 1;
        public static int kEvent_Fail = -1;
        public static int kEvent_Ok = 0;
        public static int kEvent_UserCancel = -3;
        public static int kEvent_systemError = -2;

        public abstract void onGetVideoClipIDR2YOnlyThumbnailsResult(int i, byte[] bArr, int i2, int i3, int i4);
    }

    /* loaded from: classes3.dex */
    public static abstract class OnLoadVideoClipThumbnailListener {
        public static int kEvent_Ok = 0;
        public static int kEvent_Running = 2;
        public static int kEvent_loadCompleted = 1;
        public static int kEvent_loadFail = -1;
        public static int kEvent_mustRetry = 3;
        public static int kEvent_systemError = -2;

        public abstract void onLoadThumbnailResult(int i);
    }

    @Deprecated
    public static void setThumbTempDir(String str) {
        if (str == null) {
            MediaInfo.a((File) null);
            return;
        }
        File file = new File(str);
        if (file.isDirectory()) {
            MediaInfo.a(file);
            return;
        }
        Log.e(TAG, "setThumbTempDir not dir=" + str);
    }

    public boolean setBrightness(int i) {
        if (i < -255 || i > 255) {
            return false;
        }
        this.m_Brightness = i;
        setProjectUpdateSignal(true);
        return true;
    }

    public boolean setContrast(int i) {
        if (i < -255 || i > 255) {
            return false;
        }
        this.m_Contrast = i;
        setProjectUpdateSignal(true);
        return true;
    }

    public boolean setSaturation(int i) {
        if (i < -255 || i > 255) {
            return false;
        }
        this.m_Saturation = i;
        setProjectUpdateSignal(true);
        return true;
    }

    public void setColorEffect(nexColorEffect nexcoloreffect) {
        this.mColorEffect = nexcoloreffect;
        setProjectUpdateSignal(true);
    }

    public nexColorEffect getColorEffect() {
        return this.mColorEffect;
    }

    public String getColorEffectID() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return nexColorEffect.NONE.getKineMasterID();
        }
        return nexcoloreffect.getKineMasterID();
    }

    public int getBrightness() {
        return this.m_Brightness;
    }

    public int getContrast() {
        return this.m_Contrast;
    }

    public int getSaturation() {
        return this.m_Saturation;
    }

    public int getCombinedBrightness() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return this.m_Brightness;
        }
        return this.m_Brightness + ((int) (nexcoloreffect.getBrightness() * 255.0f));
    }

    public int getCombinedContrast() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return this.m_Contrast;
        }
        return this.m_Contrast + ((int) (nexcoloreffect.getContrast() * 255.0f));
    }

    public int getCombinedSaturation() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return this.m_Saturation;
        }
        return this.m_Saturation + ((int) (nexcoloreffect.getSaturation() * 255.0f));
    }

    public int getTintColor() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return 0;
        }
        return nexcoloreffect.getTintColor();
    }

    public int getLUTId() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return 0;
        }
        return nexcoloreffect.getLUTId();
    }

    public int getCustomLUTA() {
        return this.mCustomLUT_A;
    }

    public int getCustomLUTB() {
        return this.mCustomLUT_B;
    }

    public int getCustomLUTPower() {
        return this.mCustomLUT_Power;
    }

    public void setCustomLUTA(int i) {
        this.mCustomLUT_A = i;
    }

    public void setCustomLUTB(int i) {
        this.mCustomLUT_B = i;
    }

    public void setCustomLUTPower(int i) {
        this.mCustomLUT_Power = i;
    }

    /* loaded from: classes3.dex */
    public static class ClipInfo implements Cloneable {
        public int mAudioBitrate;
        public String mAudioCodecType;
        public int mAudioTotalTime;
        public int mDisplayHeight;
        public int mDisplayWidth;
        public boolean mExistAudio;
        public boolean mExistVideo;
        public int mFramesPerSecond;
        public int mH264Level;
        public int mH264Profile;
        public int mHeight;
        public String mMimeType;
        public int mRotateDegreeInMeta;
        public int mSeekPointCount;
        public int mTotalTime;
        public int mVideoBitrate;
        public String mVideoCodecType;
        public int mVideoHDRType;
        public int mVideoRenderMode;
        public int mVideoTotalTime;
        public byte[] mVideoUUID;
        public int mWidth;
        public int mClipType = 0;
        public int mSuppertedResult = 1;

        public static ClipInfo clone(ClipInfo clipInfo) {
            ClipInfo clipInfo2;
            ClipInfo clipInfo3 = null;
            try {
                clipInfo2 = (ClipInfo) clipInfo.clone();
            } catch (CloneNotSupportedException e) {
                e = e;
            }
            try {
                clipInfo2.mMimeType = clipInfo.mMimeType;
                clipInfo2.mVideoCodecType = clipInfo.mVideoCodecType;
                clipInfo2.mAudioCodecType = clipInfo.mAudioCodecType;
                byte[] bArr = clipInfo.mVideoUUID;
                if (bArr == null) {
                    clipInfo2.mVideoUUID = null;
                } else {
                    byte[] bArr2 = new byte[bArr.length];
                    clipInfo2.mVideoUUID = bArr2;
                    System.arraycopy(clipInfo.mVideoUUID, 0, bArr2, 0, bArr2.length);
                }
                return clipInfo2;
            } catch (CloneNotSupportedException e2) {
                e = e2;
                clipInfo3 = clipInfo2;
                e.printStackTrace();
                return clipInfo3;
            }
        }
    }

    public nexClip(String str) {
        this.mPath = null;
        this.mTransCodingPath = null;
        this.mClipEffect = null;
        this.mTransitionEffect = null;
        this.mSingleThumbnail = null;
        this.mThumbnails = null;
        this.m_gettingThumbnails = false;
        this.m_getThumbnailsFailed = false;
        this.mAudioOnOff = true;
        this.mFacedetectProcessed = false;
        this.mFaceDetected = 0;
        this.mFaceRect = new Rect();
        this.index = 0;
        this.count = 0;
        this.mAudioEnvelop = null;
        this.mediainfo = null;
        this.mTemplateEffectID = 0;
        this.mTemplateAudioPos = 0;
        this.mDrawInfos = new ArrayList();
        this.mOverlappedTransition = true;
        this.mMediaInfoUseCache = true;
        this.mStartTime = 0;
        this.mEndTime = 0;
        this.mDuration = 6000;
        this.mCustomLUT_A = 0;
        this.mCustomLUT_B = 0;
        this.mCustomLUT_Power = nexCrop.ABSTRACT_DIMENSION;
        this.mColorEffect = nexColorEffect.NONE;
        this.mAVSyncAudioStartTime = 0;
        this.m_ClipVolume = 100;
        this.m_BGMVolume = 100;
        this.mRotate = 0;
        this.isMotionTrackedVideo = false;
        this.mIsAssetResource = false;
        if (str.startsWith("@assetItem:")) {
            this.mPath = str.substring(11);
            this.mTransCodingPath = str;
            this.mMediaInfoUseCache = false;
            ClipInfo clipInfo = new ClipInfo();
            this.mInfo = clipInfo;
            clipInfo.mClipType = 1;
            Rect assetPackageMediaOptions = nexAssetPackageManager.getAssetPackageMediaOptions(this.mPath);
            this.mInfo.mWidth = assetPackageMediaOptions.width();
            this.mInfo.mHeight = assetPackageMediaOptions.height();
            return;
        }
        this.mPath = str;
        String transCodingPath = transCodingPath(str);
        this.mTransCodingPath = transCodingPath;
        if (transCodingPath != null && transCodingPath.startsWith("@assetItem:")) {
            this.mMediaInfoUseCache = false;
            ClipInfo clipInfo2 = new ClipInfo();
            this.mInfo = clipInfo2;
            clipInfo2.mClipType = 1;
            Rect assetPackageMediaOptions2 = nexAssetPackageManager.getAssetPackageMediaOptions(this.mPath);
            this.mInfo.mWidth = assetPackageMediaOptions2.width();
            this.mInfo.mHeight = assetPackageMediaOptions2.height();
            return;
        }
        this.mMediaInfoUseCache = true;
        setMediaInfo(MediaInfo.a(getRealPath(), this.mMediaInfoUseCache));
        this.mInfo = resolveClip(getRealPath(), this.mMediaInfoUseCache, getMediaInfo(), false);
    }

    public nexClip(String str, boolean z) {
        this.mPath = null;
        this.mTransCodingPath = null;
        this.mClipEffect = null;
        this.mTransitionEffect = null;
        this.mSingleThumbnail = null;
        this.mThumbnails = null;
        this.m_gettingThumbnails = false;
        this.m_getThumbnailsFailed = false;
        this.mAudioOnOff = true;
        this.mFacedetectProcessed = false;
        this.mFaceDetected = 0;
        this.mFaceRect = new Rect();
        this.index = 0;
        this.count = 0;
        this.mAudioEnvelop = null;
        this.mediainfo = null;
        this.mTemplateEffectID = 0;
        this.mTemplateAudioPos = 0;
        this.mDrawInfos = new ArrayList();
        this.mOverlappedTransition = true;
        this.mMediaInfoUseCache = true;
        this.mStartTime = 0;
        this.mEndTime = 0;
        this.mDuration = 6000;
        this.mCustomLUT_A = 0;
        this.mCustomLUT_B = 0;
        this.mCustomLUT_Power = nexCrop.ABSTRACT_DIMENSION;
        this.mColorEffect = nexColorEffect.NONE;
        this.mAVSyncAudioStartTime = 0;
        this.m_ClipVolume = 100;
        this.m_BGMVolume = 100;
        this.mRotate = 0;
        this.isMotionTrackedVideo = false;
        this.mIsAssetResource = false;
        if (str.startsWith("@assetItem:")) {
            this.mPath = str.substring(11);
            this.mTransCodingPath = str;
            this.mMediaInfoUseCache = false;
            ClipInfo clipInfo = new ClipInfo();
            this.mInfo = clipInfo;
            clipInfo.mClipType = 1;
            Rect assetPackageMediaOptions = nexAssetPackageManager.getAssetPackageMediaOptions(this.mPath);
            this.mInfo.mWidth = assetPackageMediaOptions.width();
            this.mInfo.mHeight = assetPackageMediaOptions.height();
            return;
        }
        this.mPath = str;
        String transCodingPath = transCodingPath(str);
        this.mTransCodingPath = transCodingPath;
        if (transCodingPath != null && transCodingPath.startsWith("@assetItem:")) {
            this.mMediaInfoUseCache = false;
            ClipInfo clipInfo2 = new ClipInfo();
            this.mInfo = clipInfo2;
            clipInfo2.mClipType = 1;
            Rect assetPackageMediaOptions2 = nexAssetPackageManager.getAssetPackageMediaOptions(this.mPath);
            this.mInfo.mWidth = assetPackageMediaOptions2.width();
            this.mInfo.mHeight = assetPackageMediaOptions2.height();
            return;
        }
        this.mMediaInfoUseCache = true;
        setMediaInfo(MediaInfo.a(getRealPath(), this.mMediaInfoUseCache));
        this.mInfo = resolveClip(getRealPath(), this.mMediaInfoUseCache, getMediaInfo(), false);
    }

    private nexClip() {
        this.mPath = null;
        this.mTransCodingPath = null;
        this.mClipEffect = null;
        this.mTransitionEffect = null;
        this.mSingleThumbnail = null;
        this.mThumbnails = null;
        this.m_gettingThumbnails = false;
        this.m_getThumbnailsFailed = false;
        this.mAudioOnOff = true;
        this.mFacedetectProcessed = false;
        this.mFaceDetected = 0;
        this.mFaceRect = new Rect();
        this.index = 0;
        this.count = 0;
        this.mAudioEnvelop = null;
        this.mediainfo = null;
        this.mTemplateEffectID = 0;
        this.mTemplateAudioPos = 0;
        this.mDrawInfos = new ArrayList();
        this.mOverlappedTransition = true;
        this.mMediaInfoUseCache = true;
        this.mStartTime = 0;
        this.mEndTime = 0;
        this.mDuration = 6000;
        this.mCustomLUT_A = 0;
        this.mCustomLUT_B = 0;
        this.mCustomLUT_Power = nexCrop.ABSTRACT_DIMENSION;
        this.mColorEffect = nexColorEffect.NONE;
        this.mAVSyncAudioStartTime = 0;
        this.m_ClipVolume = 100;
        this.m_BGMVolume = 100;
        this.mRotate = 0;
        this.isMotionTrackedVideo = false;
        this.mIsAssetResource = false;
    }

    private static String transCodingPath(String str) {
        if (str == null) {
            Log.d(TAG, "transCodingPath path null");
            return null;
        } else if (str.startsWith("nexasset://")) {
            Log.d(TAG, "transCodingPath path asset");
            return null;
        } else if (str.startsWith("@solid:")) {
            Log.d(TAG, "transCodingPath path solid");
            return null;
        } else if (new File(str).isFile()) {
            Log.d(TAG, "transCodingPath path is file");
            return null;
        } else {
            return nexAssetPackageManager.getAssetPackageMediaPath(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), str);
        }
    }

    public nexClip(nexClip nexclip) {
        this.mPath = null;
        this.mTransCodingPath = null;
        this.mClipEffect = null;
        this.mTransitionEffect = null;
        this.mSingleThumbnail = null;
        this.mThumbnails = null;
        this.m_gettingThumbnails = false;
        this.m_getThumbnailsFailed = false;
        this.mAudioOnOff = true;
        this.mFacedetectProcessed = false;
        this.mFaceDetected = 0;
        this.mFaceRect = new Rect();
        this.index = 0;
        this.count = 0;
        this.mAudioEnvelop = null;
        this.mediainfo = null;
        this.mTemplateEffectID = 0;
        this.mTemplateAudioPos = 0;
        this.mDrawInfos = new ArrayList();
        this.mOverlappedTransition = true;
        this.mMediaInfoUseCache = true;
        this.mStartTime = 0;
        this.mEndTime = 0;
        this.mDuration = 6000;
        this.mCustomLUT_A = 0;
        this.mCustomLUT_B = 0;
        this.mCustomLUT_Power = nexCrop.ABSTRACT_DIMENSION;
        this.mColorEffect = nexColorEffect.NONE;
        this.mAVSyncAudioStartTime = 0;
        this.m_ClipVolume = 100;
        this.m_BGMVolume = 100;
        this.mRotate = 0;
        this.isMotionTrackedVideo = false;
        this.mIsAssetResource = false;
        this.mPath = nexclip.mPath;
        this.mTransCodingPath = nexclip.mTransCodingPath;
        this.misMustDownSize = nexclip.misMustDownSize;
        this.mMediaInfoUseCache = nexclip.mMediaInfoUseCache;
        this.mPropertySlowVideoMode = nexclip.mPropertySlowVideoMode;
        if (nexclip.mInfo != null) {
            if (this.mInfo == null) {
                this.mInfo = new ClipInfo();
            }
            ClipInfo clipInfo = this.mInfo;
            ClipInfo clipInfo2 = nexclip.mInfo;
            clipInfo.mMimeType = clipInfo2.mMimeType;
            clipInfo.mClipType = clipInfo2.mClipType;
            clipInfo.mWidth = clipInfo2.mWidth;
            clipInfo.mHeight = clipInfo2.mHeight;
            clipInfo.mDisplayWidth = clipInfo2.mDisplayWidth;
            clipInfo.mDisplayHeight = clipInfo2.mDisplayHeight;
            clipInfo.mExistVideo = clipInfo2.mExistVideo;
            clipInfo.mExistAudio = clipInfo2.mExistAudio;
            clipInfo.mTotalTime = clipInfo2.mTotalTime;
            clipInfo.mFramesPerSecond = clipInfo2.mFramesPerSecond;
            clipInfo.mRotateDegreeInMeta = clipInfo2.mRotateDegreeInMeta;
            clipInfo.mH264Profile = clipInfo2.mH264Profile;
            clipInfo.mH264Level = clipInfo2.mH264Level;
            clipInfo.mSuppertedResult = clipInfo2.mSuppertedResult;
            clipInfo.mVideoBitrate = clipInfo2.mVideoBitrate;
            clipInfo.mAudioBitrate = clipInfo2.mAudioBitrate;
            clipInfo.mVideoTotalTime = clipInfo2.mVideoTotalTime;
            clipInfo.mAudioTotalTime = clipInfo2.mAudioTotalTime;
            clipInfo.mSeekPointCount = clipInfo2.mSeekPointCount;
            clipInfo.mVideoRenderMode = clipInfo2.mVideoRenderMode;
            clipInfo.mVideoHDRType = clipInfo2.mVideoHDRType;
            String str = clipInfo2.mAudioCodecType;
            if (str != null) {
                clipInfo.mAudioCodecType = new String(str);
            }
            String str2 = nexclip.mInfo.mVideoCodecType;
            if (str2 != null) {
                this.mInfo.mVideoCodecType = new String(str2);
            }
            ClipInfo clipInfo3 = nexclip.mInfo;
            byte[] bArr = clipInfo3.mVideoUUID;
            if (bArr != null) {
                ClipInfo clipInfo4 = this.mInfo;
                byte[] bArr2 = new byte[bArr.length];
                clipInfo4.mVideoUUID = bArr2;
                System.arraycopy(clipInfo3.mVideoUUID, 0, bArr2, 0, bArr2.length);
            }
        }
        MediaInfo mediaInfo = nexclip.mediainfo;
        if (mediaInfo != null) {
            this.mediainfo = mediaInfo;
        }
    }

    public int replaceClip(String str) {
        String transCodingPath = transCodingPath(str);
        this.mTransCodingPath = transCodingPath;
        if (transCodingPath == null) {
            transCodingPath = str;
        }
        ClipInfo resolveClip = resolveClip(transCodingPath, this.mMediaInfoUseCache, getMediaInfo(), false);
        int i = resolveClip.mSuppertedResult;
        if (i != 0) {
            return i;
        }
        if (this.mInfo.mClipType != resolveClip.mClipType) {
            return -1;
        }
        this.mPath = str;
        this.mInfo = resolveClip;
        setProjectUpdateSignal(false);
        return 0;
    }

    public void setProjectUpdateSignal(boolean z) {
        b bVar;
        if (!this.mProjectAttachment || (bVar = this.mObserver) == null) {
            return;
        }
        bVar.updateTimeLine(false);
    }

    public static nexClip dup(nexClip nexclip) {
        return new nexClip(nexclip);
    }

    public static nexClip clone(nexClip nexclip) {
        nexClip nexclip2 = null;
        try {
            nexClip nexclip3 = (nexClip) nexclip.clone();
            try {
                nexClipEffect nexclipeffect = nexclip.mClipEffect;
                if (nexclipeffect != null) {
                    nexclip3.mClipEffect = nexClipEffect.clone(nexclipeffect);
                }
                nexTransitionEffect nextransitioneffect = nexclip.mTransitionEffect;
                if (nextransitioneffect != null) {
                    nexclip3.mTransitionEffect = nexTransitionEffect.clone(nextransitioneffect);
                }
                nexCrop nexcrop = nexclip.mCrop;
                if (nexcrop != null) {
                    nexclip3.mCrop = nexCrop.clone(nexcrop);
                }
                nexVideoClipEdit nexvideoclipedit = nexclip.mVideoEdit;
                if (nexvideoclipedit != null) {
                    nexclip3.mVideoEdit = nexVideoClipEdit.clone(nexvideoclipedit);
                }
                nexColorEffect nexcoloreffect = nexclip.mColorEffect;
                if (nexcoloreffect != null) {
                    nexclip3.mColorEffect = nexColorEffect.clone(nexcoloreffect);
                }
                ClipInfo clipInfo = nexclip.mInfo;
                if (clipInfo != null) {
                    nexclip3.mInfo = ClipInfo.clone(clipInfo);
                }
                nexAudioEnvelop nexaudioenvelop = nexclip.mAudioEnvelop;
                if (nexaudioenvelop != null) {
                    nexclip3.mAudioEnvelop = nexAudioEnvelop.clone(nexaudioenvelop);
                }
                nexAudioEdit nexaudioedit = nexclip.mAudioEdit;
                if (nexaudioedit != null) {
                    nexclip3.mAudioEdit = nexAudioEdit.clone(nexclip3, nexaudioedit);
                }
                nexclip3.mTransCodingPath = nexclip.mTransCodingPath;
                nexclip3.mSingleThumbnail = nexclip.mSingleThumbnail;
                nexclip3.mThumbnails = nexclip.mThumbnails;
                return nexclip3;
            } catch (CloneNotSupportedException e) {
                e = e;
                nexclip2 = nexclip3;
                e.printStackTrace();
                return nexclip2;
            }
        } catch (CloneNotSupportedException e2) {
            e = e2;
        }
    }

    public nexClip(String str, String str2) {
        this.mPath = null;
        this.mTransCodingPath = null;
        this.mClipEffect = null;
        this.mTransitionEffect = null;
        this.mSingleThumbnail = null;
        this.mThumbnails = null;
        this.m_gettingThumbnails = false;
        this.m_getThumbnailsFailed = false;
        this.mAudioOnOff = true;
        this.mFacedetectProcessed = false;
        this.mFaceDetected = 0;
        this.mFaceRect = new Rect();
        this.index = 0;
        this.count = 0;
        this.mAudioEnvelop = null;
        this.mediainfo = null;
        this.mTemplateEffectID = 0;
        this.mTemplateAudioPos = 0;
        this.mDrawInfos = new ArrayList();
        this.mOverlappedTransition = true;
        this.mMediaInfoUseCache = true;
        this.mStartTime = 0;
        this.mEndTime = 0;
        this.mDuration = 6000;
        this.mCustomLUT_A = 0;
        this.mCustomLUT_B = 0;
        this.mCustomLUT_Power = nexCrop.ABSTRACT_DIMENSION;
        this.mColorEffect = nexColorEffect.NONE;
        this.mAVSyncAudioStartTime = 0;
        this.m_ClipVolume = 100;
        this.m_BGMVolume = 100;
        this.mRotate = 0;
        this.isMotionTrackedVideo = false;
        this.mIsAssetResource = false;
        this.mPath = str;
        this.mTransCodingPath = str2;
        this.mMediaInfoUseCache = false;
        ClipInfo clipInfo = new ClipInfo();
        this.mInfo = clipInfo;
        clipInfo.mClipType = 1;
        Rect assetPackageMediaOptions = nexAssetPackageManager.getAssetPackageMediaOptions(this.mPath);
        this.mInfo.mWidth = assetPackageMediaOptions.width();
        this.mInfo.mHeight = assetPackageMediaOptions.height();
        Log.d(TAG, "@assetItem: w=" + this.mInfo.mWidth + ", h=" + this.mInfo.mHeight);
    }

    private nexClip(String str, String str2, ClipInfo clipInfo, boolean z, MediaInfo mediaInfo) {
        this.mPath = null;
        this.mTransCodingPath = null;
        this.mClipEffect = null;
        this.mTransitionEffect = null;
        this.mSingleThumbnail = null;
        this.mThumbnails = null;
        this.m_gettingThumbnails = false;
        this.m_getThumbnailsFailed = false;
        this.mAudioOnOff = true;
        this.mFacedetectProcessed = false;
        this.mFaceDetected = 0;
        this.mFaceRect = new Rect();
        this.index = 0;
        this.count = 0;
        this.mAudioEnvelop = null;
        this.mediainfo = null;
        this.mTemplateEffectID = 0;
        this.mTemplateAudioPos = 0;
        this.mDrawInfos = new ArrayList();
        this.mOverlappedTransition = true;
        this.mMediaInfoUseCache = true;
        this.mStartTime = 0;
        this.mEndTime = 0;
        this.mDuration = 6000;
        this.mCustomLUT_A = 0;
        this.mCustomLUT_B = 0;
        this.mCustomLUT_Power = nexCrop.ABSTRACT_DIMENSION;
        this.mColorEffect = nexColorEffect.NONE;
        this.mAVSyncAudioStartTime = 0;
        this.m_ClipVolume = 100;
        this.m_BGMVolume = 100;
        this.mRotate = 0;
        this.isMotionTrackedVideo = false;
        this.mIsAssetResource = false;
        this.mPath = str;
        this.mInfo = clipInfo;
        if (str.compareTo(str2) != 0) {
            this.mTransCodingPath = str2;
        }
        this.mMediaInfoUseCache = z;
        if (mediaInfo != null) {
            setMediaInfo(mediaInfo);
        } else {
            setMediaInfo(MediaInfo.a(str2, z));
        }
    }

    public static nexClip getSupportedClip(String str) {
        return getSupportedClip(str, true);
    }

    public static nexClip getSupportedClip(String str, boolean z) {
        return getSupportedClip(str, z, 0);
    }

    public static nexClip getSupportedClip(String str, boolean z, int i) {
        return getSupportedClip(str, z, i, false);
    }

    public static nexClip getSupportedClip(String str, boolean z, int i, boolean z2) {
        if (str == null) {
            return null;
        }
        if (str.startsWith("@solid:")) {
            return getSolidClip(str);
        }
        if (str.startsWith("@assetItem:")) {
            return new nexClip(str.substring(11), str);
        }
        String transCodingPath = transCodingPath(str);
        String str2 = transCodingPath == null ? str : transCodingPath;
        if (str2.startsWith("@assetItem:")) {
            return new nexClip(str, str2);
        }
        MediaInfo a = MediaInfo.a(str2, z);
        ClipInfo resolveClip = resolveClip(str2, z, a, z2);
        if (resolveClip.mSuppertedResult == 0) {
            int i2 = resolveClip.mClipType;
            if (i2 == 1) {
                return new nexClip(str, str2, resolveClip, z, a);
            }
            if (i2 == 4) {
                if (i > 0 && resolveClip.mWidth * resolveClip.mHeight * resolveClip.mFramesPerSecond > i) {
                    return null;
                }
                return new nexClip(str, str2, resolveClip, z, a);
            } else if (i2 == 3) {
                return new nexClip(str, str2, resolveClip, z, a);
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static com.nexstreaming.nexeditorsdk.nexClip.ClipInfo resolveClip(java.lang.String r24, boolean r25, com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo r26, boolean r27) {
        /*
            Method dump skipped, instructions count: 658
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexClip.resolveClip(java.lang.String, boolean, com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo, boolean):com.nexstreaming.nexeditorsdk.nexClip$ClipInfo");
    }

    public int getFramesPerSecond() {
        return this.mInfo.mFramesPerSecond;
    }

    public boolean hasAudio() {
        return this.mInfo.mExistAudio;
    }

    public boolean hasVideo() {
        return this.mInfo.mExistVideo;
    }

    public String getPath() {
        return this.mPath;
    }

    public String getRealPath() {
        String str = this.mTransCodingPath;
        return str != null ? str : this.mPath;
    }

    public int getClipType() {
        return this.mInfo.mClipType;
    }

    public int getTotalTime() {
        return this.mInfo.mTotalTime;
    }

    public int getWidth() {
        return this.mInfo.mWidth;
    }

    public int getHeight() {
        return this.mInfo.mHeight;
    }

    public int getDisplayWidth() {
        return this.mInfo.mDisplayWidth;
    }

    public int getDisplayHeight() {
        return this.mInfo.mDisplayHeight;
    }

    public int getSupportedResult() {
        return this.mInfo.mSuppertedResult;
    }

    public int getAVCProfile() {
        return this.mInfo.mH264Profile;
    }

    public int getAVCLevel() {
        return this.mInfo.mH264Level;
    }

    public int getRotateInMeta() {
        return this.mInfo.mRotateDegreeInMeta;
    }

    public int getVideoBitrate() {
        return this.mInfo.mVideoBitrate;
    }

    public int getAudioBitrate() {
        return this.mInfo.mAudioBitrate;
    }

    public int getVideoDuration() {
        return this.mInfo.mVideoTotalTime;
    }

    public int getAudioDuration() {
        return this.mInfo.mAudioTotalTime;
    }

    public int getSeekPointCount() {
        return this.mInfo.mSeekPointCount;
    }

    public int getSeekPointInterval() {
        ClipInfo clipInfo = this.mInfo;
        int i = clipInfo.mSeekPointCount;
        if (i == 0) {
            return clipInfo.mVideoTotalTime;
        }
        return clipInfo.mVideoTotalTime / i;
    }

    public int getVideoRenderMode() {
        return this.mInfo.mVideoRenderMode;
    }

    public int getVideoHDRType() {
        return this.mInfo.mVideoHDRType;
    }

    public String getVideoCodecType() {
        return this.mInfo.mVideoCodecType;
    }

    public String getAudioCodecType() {
        return this.mInfo.mAudioCodecType;
    }

    public final boolean getAttachmentState() {
        return this.mProjectAttachment;
    }

    public final void setAttachmentState(boolean z, b bVar) {
        nexTransitionEffect nextransitioneffect;
        this.mObserver = bVar;
        this.mProjectAttachment = z;
        if (z || (nextransitioneffect = this.mTransitionEffect) == null) {
            return;
        }
        nextransitioneffect.setObserver(null);
    }

    public nexClipEffect getClipEffect() {
        if (this.mProjectAttachment) {
            if (this.mClipEffect == null) {
                this.mClipEffect = new nexClipEffect();
            }
            return this.mClipEffect;
        }
        throw new ProjectNotAttachedException();
    }

    public nexTransitionEffect getTransitionEffect() {
        if (this.mProjectAttachment) {
            if (this.mTransitionEffect == null) {
                this.mTransitionEffect = new nexTransitionEffect(this.mObserver);
            }
            return this.mTransitionEffect;
        }
        throw new ProjectNotAttachedException();
    }

    public nexVideoClipEdit getVideoClipEdit() {
        if (this.mProjectAttachment) {
            if (this.mInfo.mClipType != 4) {
                return null;
            }
            if (this.mVideoEdit == null) {
                this.mVideoEdit = nexVideoClipEdit.getnexVideoClipEdit(this);
            }
            return this.mVideoEdit;
        }
        throw new ProjectNotAttachedException();
    }

    public void setImageClipDuration(int i) {
        if (this.mDuration != i) {
            setProjectUpdateSignal(false);
        }
        this.mDuration = i;
    }

    public int getImageClipDuration() {
        return this.mDuration;
    }

    public int getProjectStartTime() {
        if (this.mProjectAttachment) {
            return this.mStartTime;
        }
        return 0;
    }

    public int getProjectEndTime() {
        if (this.mProjectAttachment) {
            return this.mEndTime;
        }
        return 0;
    }

    public void setAudioOnOff(boolean z) {
        this.mAudioOnOff = z;
        setProjectUpdateSignal(true);
    }

    public boolean getAudioOnOff() {
        return this.mAudioOnOff;
    }

    public void setMainThumbnail(Bitmap bitmap) {
        WeakReference<Bitmap> weakReference = this.mSingleThumbnail;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.mSingleThumbnail = new WeakReference<>(bitmap);
    }

    public Bitmap getMainThumbnail(float f, float f2) {
        ClipInfo clipInfo = this.mInfo;
        Bitmap bitmap = null;
        if (clipInfo.mClipType == 4) {
            h hVar = this.mThumbnails;
            if (hVar == null) {
                return null;
            }
            return hVar.a(0, clipInfo.mTotalTime / 2, false, false);
        }
        WeakReference<Bitmap> weakReference = this.mSingleThumbnail;
        if (weakReference != null) {
            bitmap = weakReference.get();
        }
        if (bitmap == null && (bitmap = makeThumbnail(f, f2)) != null) {
            this.mSingleThumbnail = new WeakReference<>(bitmap);
        }
        return bitmap;
    }

    public int[] getVideoClipTimeLineOfThumbnail() {
        h hVar;
        if (this.mInfo.mClipType == 4 && (hVar = this.mThumbnails) != null) {
            return hVar.b();
        }
        throw new ClipIsNotVideoException();
    }

    public Bitmap getVideoClipTimeLineThumbnail(int i, int i2, boolean z, boolean z2) {
        h hVar;
        if (this.mInfo.mClipType == 4 && (hVar = this.mThumbnails) != null) {
            return hVar.a(i, i2, z, z2);
        }
        throw new ClipIsNotVideoException();
    }

    public int loadVideoClipThumbnails(final OnLoadVideoClipThumbnailListener onLoadVideoClipThumbnailListener) {
        if (this.mInfo.mClipType != 4) {
            return -1;
        }
        h hVar = this.mThumbnails;
        if (hVar == null && !this.m_gettingThumbnails && !this.m_getThumbnailsFailed) {
            MediaInfo mediaInfo = getMediaInfo();
            if (mediaInfo == null) {
                if (onLoadVideoClipThumbnailListener != null) {
                    onLoadVideoClipThumbnailListener.onLoadThumbnailResult(OnLoadVideoClipThumbnailListener.kEvent_systemError);
                }
                return OnLoadVideoClipThumbnailListener.kEvent_systemError;
            }
            this.m_gettingThumbnails = true;
            mediaInfo.b().onResultAvailable(new ResultTask.OnResultAvailableListener<h>() { // from class: com.nexstreaming.nexeditorsdk.nexClip.5
                @Override // com.nexstreaming.app.common.task.ResultTask.OnResultAvailableListener
                /* renamed from: a */
                public void onResultAvailable(ResultTask<h> resultTask, Task.Event event, h hVar2) {
                    nexClip.this.m_gettingThumbnails = false;
                    nexClip.this.mThumbnails = hVar2;
                    OnLoadVideoClipThumbnailListener onLoadVideoClipThumbnailListener2 = onLoadVideoClipThumbnailListener;
                    if (onLoadVideoClipThumbnailListener2 != null) {
                        onLoadVideoClipThumbnailListener2.onLoadThumbnailResult(OnLoadVideoClipThumbnailListener.kEvent_Ok);
                    }
                }
            }).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.nexeditorsdk.nexClip.1
                @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                public void onFail(Task task, Task.Event event, Task.TaskError taskError) {
                    nexClip.this.m_gettingThumbnails = false;
                    if (taskError != NexEditor.ErrorCode.INPROGRESS_GETCLIPINFO) {
                        nexClip.this.m_getThumbnailsFailed = true;
                    }
                    if (onLoadVideoClipThumbnailListener != null) {
                        if (nexClip.this.m_getThumbnailsFailed) {
                            onLoadVideoClipThumbnailListener.onLoadThumbnailResult(OnLoadVideoClipThumbnailListener.kEvent_loadFail);
                        } else {
                            onLoadVideoClipThumbnailListener.onLoadThumbnailResult(OnLoadVideoClipThumbnailListener.kEvent_mustRetry);
                        }
                    }
                }
            });
            return 0;
        } else if (this.m_gettingThumbnails) {
            if (onLoadVideoClipThumbnailListener != null) {
                onLoadVideoClipThumbnailListener.onLoadThumbnailResult(OnLoadVideoClipThumbnailListener.kEvent_Running);
            }
            return OnLoadVideoClipThumbnailListener.kEvent_Running;
        } else if (this.m_getThumbnailsFailed) {
            if (onLoadVideoClipThumbnailListener != null) {
                onLoadVideoClipThumbnailListener.onLoadThumbnailResult(OnLoadVideoClipThumbnailListener.kEvent_loadFail);
            }
            return OnLoadVideoClipThumbnailListener.kEvent_loadFail;
        } else if (hVar == null || onLoadVideoClipThumbnailListener == null) {
            return 0;
        } else {
            onLoadVideoClipThumbnailListener.onLoadThumbnailResult(OnLoadVideoClipThumbnailListener.kEvent_loadCompleted);
            return 0;
        }
    }

    @Deprecated
    public int getVideoClipIFrameThumbnails(int i, int i2, OnGetVideoClipDetailThumbnailsListener onGetVideoClipDetailThumbnailsListener) {
        return getVideoClipDetailThumbnails(i, i2, 0, 0, 3600, 0, onGetVideoClipDetailThumbnailsListener);
    }

    @Deprecated
    public int getVideoClipIDR2YOnlyThumbnails(int i, int i2, int i3, int i4, int i5, final OnGetVideoClipIDR2YOnlyThumbnailsListener onGetVideoClipIDR2YOnlyThumbnailsListener) {
        if (this.mInfo.mClipType == 4 && onGetVideoClipIDR2YOnlyThumbnailsListener != null) {
            MediaInfo mediaInfo = getMediaInfo();
            if (mediaInfo == null) {
                onGetVideoClipIDR2YOnlyThumbnailsListener.onGetVideoClipIDR2YOnlyThumbnailsResult(OnGetVideoClipIDR2YOnlyThumbnailsListener.kEvent_systemError, null, 0, 0, 0);
                return OnGetVideoClipIDR2YOnlyThumbnailsListener.kEvent_systemError;
            }
            final int[] d = mediaInfo.d();
            mediaInfo.a(i, i2, i3, i4, i5, 131074, null, new com.nexstreaming.kminternal.kinemaster.mediainfo.e() { // from class: com.nexstreaming.nexeditorsdk.nexClip.8
                private int d = 0;

                @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.e
                public void a(byte[] bArr, int i6, int i7, int i8) {
                    if (bArr == null) {
                        Log.d(nexClip.TAG, "YonlyThumbTest processThumbnail : Y=null index=" + i6 + " totalCount=" + i7 + " timestamp=" + i8);
                    } else {
                        Log.d(nexClip.TAG, "YonlyThumbTest processThumbnail : Y=[" + bArr.length + "] index=" + i6 + " totalCount=" + i7 + " timestamp=" + i8);
                    }
                    int indexSeekTabNearTimeStamp = nexClip.this.getIndexSeekTabNearTimeStamp(d, this.d, i8);
                    if (indexSeekTabNearTimeStamp >= 0) {
                        Log.d(nexClip.TAG, "YonlyThumbTest processThumbnail : timestamp =" + i8 + ", seektable=" + d[indexSeekTabNearTimeStamp] + ", lastIndex=" + this.d + ", index=" + indexSeekTabNearTimeStamp);
                        i8 = d[indexSeekTabNearTimeStamp];
                        this.d = indexSeekTabNearTimeStamp + 1;
                    } else {
                        Log.d(nexClip.TAG, "YonlyThumbTest processThumbnail : timestamp =" + i8 + ", lastIndex=" + this.d);
                    }
                    onGetVideoClipIDR2YOnlyThumbnailsListener.onGetVideoClipIDR2YOnlyThumbnailsResult(OnGetVideoClipIDR2YOnlyThumbnailsListener.kEvent_Ok, bArr, i6, i7, i8);
                }
            }).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.nexeditorsdk.nexClip.7
                @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                public void onFail(Task task, Task.Event event, Task.TaskError taskError) {
                    Log.d(nexClip.TAG, "YonlyThumbTest onFail : " + taskError.getMessage());
                    if (taskError == NexEditor.ErrorCode.GETCLIPINFO_USER_CANCEL) {
                        onGetVideoClipIDR2YOnlyThumbnailsListener.onGetVideoClipIDR2YOnlyThumbnailsResult(OnGetVideoClipIDR2YOnlyThumbnailsListener.kEvent_UserCancel, null, 0, 0, 0);
                    } else {
                        onGetVideoClipIDR2YOnlyThumbnailsListener.onGetVideoClipIDR2YOnlyThumbnailsResult(OnGetVideoClipIDR2YOnlyThumbnailsListener.kEvent_Fail, null, 0, 0, 0);
                    }
                }
            }).mo1850onComplete(new Task.OnTaskEventListener() { // from class: com.nexstreaming.nexeditorsdk.nexClip.6
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task, Task.Event event) {
                    Log.d(nexClip.TAG, "YonlyThumbTest onComplete");
                    onGetVideoClipIDR2YOnlyThumbnailsListener.onGetVideoClipIDR2YOnlyThumbnailsResult(OnGetVideoClipIDR2YOnlyThumbnailsListener.kEvent_Completed, null, 0, 0, 0);
                }
            });
            return 0;
        }
        return -1;
    }

    public int getVideoClipDetailThumbnails(int i, int i2, int i3, int i4, int i5, int i6, OnGetVideoClipDetailThumbnailsListener onGetVideoClipDetailThumbnailsListener) {
        boolean z;
        long a = com.nexstreaming.app.common.util.e.a(Environment.getDataDirectory().getAbsoluteFile());
        if (a < sVideoClipDetailThumbnailsDiskLimitSize) {
            Log.d(TAG, "getVideoClipDetailThumbnails() disk low. run no cache mode. disk size=" + a + ", limit=" + sVideoClipDetailThumbnailsDiskLimitSize);
            z = true;
        } else {
            z = false;
        }
        return getVideoClipDetailThumbnails(i, i2, i3, i4, i5, i6, z, null, onGetVideoClipDetailThumbnailsListener);
    }

    public int getVideoClipDetailThumbnails(int i, int i2, int[] iArr, int i3, OnGetVideoClipDetailThumbnailsListener onGetVideoClipDetailThumbnailsListener) {
        boolean z;
        long a = com.nexstreaming.app.common.util.e.a(Environment.getDataDirectory().getAbsoluteFile());
        if (a < sVideoClipDetailThumbnailsDiskLimitSize) {
            Log.d(TAG, "getVideoClipDetailThumbnails() disk low. run no cache mode. disk size=" + a + ", limit=" + sVideoClipDetailThumbnailsDiskLimitSize);
            z = true;
        } else {
            z = false;
        }
        return getVideoClipDetailThumbnails(i, i2, 0, 0, iArr != null ? iArr.length : 0, i3, z, iArr, onGetVideoClipDetailThumbnailsListener);
    }

    @Deprecated
    public static void setVideoClipDetailThumbnailsDiskLimit(long j) {
        sVideoClipDetailThumbnailsDiskLimitSize = j;
    }

    public int getVideoClipDetailThumbnails(int i, int i2, int i3, int i4, int i5, int i6, boolean z, int[] iArr, final OnGetVideoClipDetailThumbnailsListener onGetVideoClipDetailThumbnailsListener) {
        if (this.mInfo.mClipType == 4 && onGetVideoClipDetailThumbnailsListener != null) {
            MediaInfo mediaInfo = getMediaInfo();
            if (mediaInfo == null) {
                onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_systemError, null, 0, 0, 0);
                return OnGetVideoClipDetailThumbnailsListener.kEvent_systemError;
            }
            int i7 = i6 != 90 ? i6 != 180 ? i6 != 270 ? 0 : 64 : 32 : 16;
            if (z) {
                i7 |= nexEngine.ExportHEVCMainTierLevel52;
            }
            mediaInfo.a(i, i2, i3, i4, i5, i7, iArr, new com.nexstreaming.kminternal.kinemaster.mediainfo.d() { // from class: com.nexstreaming.nexeditorsdk.nexClip.11
                @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.d
                public void a(Bitmap bitmap, int i8, int i9, int i10) {
                    if (bitmap == null) {
                        Log.d(nexClip.TAG, "detailThumbTest processThumbnail : bm=null index=" + i8 + " totalCount=" + i9 + " timestamp=" + i10);
                    } else {
                        Log.d(nexClip.TAG, "detailThumbTest processThumbnail : bm=[" + bitmap.getWidth() + "x" + bitmap.getHeight() + " cfg=" + bitmap.getConfig().name() + "] index=" + i8 + " totalCount=" + i9 + " timestamp=" + i10);
                    }
                    onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_Ok, bitmap, i8, i9, i10);
                }
            }).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.nexeditorsdk.nexClip.10
                @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                public void onFail(Task task, Task.Event event, Task.TaskError taskError) {
                    Log.d(nexClip.TAG, "detailThumbTest onFail : " + taskError.getMessage());
                    if (taskError == NexEditor.ErrorCode.GETCLIPINFO_USER_CANCEL) {
                        onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_UserCancel, null, 0, 0, 0);
                    } else {
                        onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_Fail, null, 0, 0, 0);
                    }
                }
            }).mo1850onComplete(new Task.OnTaskEventListener() { // from class: com.nexstreaming.nexeditorsdk.nexClip.9
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task, Task.Event event) {
                    Log.d(nexClip.TAG, "detailThumbTest onComplete");
                    onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_Completed, null, 0, 0, 0);
                }
            });
            return 0;
        }
        return -1;
    }

    public int getVideoClipDetailThumbnails(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2, int[] iArr, OnGetVideoClipDetailThumbnailsListener onGetVideoClipDetailThumbnailsListener) {
        return getVideoClipDetailThumbnails(i, i2, i3, i4, i5, i6, z, z2, false, false, null, onGetVideoClipDetailThumbnailsListener);
    }

    public int getVideoClipDetailThumbnails(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2, boolean z3, boolean z4, int[] iArr, final OnGetVideoClipDetailThumbnailsListener onGetVideoClipDetailThumbnailsListener) {
        int[] iArr2;
        if (this.mInfo.mClipType == 4 && onGetVideoClipDetailThumbnailsListener != null) {
            MediaInfo mediaInfo = getMediaInfo();
            if (mediaInfo == null) {
                onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_systemError, null, 0, 0, 0);
                return OnGetVideoClipDetailThumbnailsListener.kEvent_systemError;
            }
            int i7 = i6 != 90 ? i6 != 180 ? i6 != 270 ? 0 : 64 : 32 : 16;
            if (z) {
                i7 |= nexEngine.ExportHEVCMainTierLevel52;
            }
            if (z2) {
                i7 |= 256;
                iArr2 = null;
            } else {
                iArr2 = iArr;
            }
            if (z3) {
                i7 |= nexEngine.ExportHEVCHighTierLevel52;
            }
            if (z4) {
                i7 |= nexEngine.ExportHEVCMainTierLevel6;
            }
            mediaInfo.a(i, i2, i3, i4, i5, i7, iArr2, new com.nexstreaming.kminternal.kinemaster.mediainfo.d() { // from class: com.nexstreaming.nexeditorsdk.nexClip.3
                @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.d
                public void a(Bitmap bitmap, int i8, int i9, int i10) {
                    if (bitmap == null) {
                        Log.d(nexClip.TAG, "detailThumbTest processThumbnail : bm=null index=" + i8 + " totalCount=" + i9 + " timestamp=" + i10);
                    } else {
                        Log.d(nexClip.TAG, "detailThumbTest processThumbnail : bm=[" + bitmap.getWidth() + "x" + bitmap.getHeight() + " cfg=" + bitmap.getConfig().name() + "] index=" + i8 + " totalCount=" + i9 + " timestamp=" + i10);
                    }
                    onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_Ok, bitmap, i8, i9, i10);
                }
            }).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.nexeditorsdk.nexClip.2
                @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                public void onFail(Task task, Task.Event event, Task.TaskError taskError) {
                    Log.d(nexClip.TAG, "detailThumbTest onFail : " + taskError.getMessage());
                    if (taskError == NexEditor.ErrorCode.GETCLIPINFO_USER_CANCEL) {
                        onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_UserCancel, null, 0, 0, 0);
                    } else {
                        onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_Fail, null, 0, 0, 0);
                    }
                }
            }).mo1850onComplete(new Task.OnTaskEventListener() { // from class: com.nexstreaming.nexeditorsdk.nexClip.12
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task, Task.Event event) {
                    Log.d(nexClip.TAG, "detailThumbTest onComplete");
                    onGetVideoClipDetailThumbnailsListener.onGetDetailThumbnailResult(OnGetVideoClipDetailThumbnailsListener.kEvent_Completed, null, 0, 0, 0);
                }
            });
            return 0;
        }
        return -1;
    }

    private Bitmap makeThumbnail(float f, float f2) {
        Bitmap a;
        if (this.mInfo.mClipType == 1) {
            if (isSolid()) {
                int parseLong = (int) Long.parseLong(this.mPath.substring(7, 15), 16);
                int[] iArr = new int[576];
                for (int i = 0; i < 576; i++) {
                    iArr[i] = parseLong;
                }
                a = Bitmap.createBitmap(iArr, 32, 18, Bitmap.Config.ARGB_8888);
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(this.mPath, options);
                options.inJustDecodeBounds = false;
                a = NexImageLoader.loadBitmap(this.mPath, Math.min((int) (f2 * 100.0f), (int) ((options.outWidth / options.outHeight) * f)) * 2, ((int) f) * 2).a();
            }
            if (a != null) {
                return Bitmap.createScaledBitmap(a, Math.min((int) (f2 * 100.0f), (int) ((a.getWidth() / a.getHeight()) * f)), (int) f, true);
            }
        }
        return null;
    }

    public void setRotateDegree(int i) {
        if (i >= 360) {
            i = 0;
        }
        this.mRotate = i;
        nexCrop nexcrop = this.mCrop;
        if (nexcrop != null) {
            nexcrop.setRotate(i);
        }
        setProjectUpdateSignal(true);
    }

    public int getRotateDegree() {
        return this.mRotate;
    }

    public int runDuration() {
        ClipInfo clipInfo = this.mInfo;
        int i = clipInfo.mClipType;
        if (i != 4) {
            if (i == 1) {
                return this.mDuration;
            }
            return clipInfo.mTotalTime;
        }
        nexVideoClipEdit nexvideoclipedit = this.mVideoEdit;
        if (nexvideoclipedit == null) {
            return clipInfo.mTotalTime;
        }
        return ((clipInfo.mTotalTime - nexvideoclipedit.mTrimStartDuration) - nexvideoclipedit.mTrimEndDuration) * Math.round(nexvideoclipedit.getSpeedControl() / 100);
    }

    public boolean isFaceDetectProcessed() {
        return this.mFacedetectProcessed;
    }

    public Rect getFaceRect() {
        return this.mFaceRect;
    }

    public boolean isFaceDetected() {
        return this.mFaceDetected > 0;
    }

    public void resetFaceDetectProcessed() {
        this.mFacedetectProcessed = false;
    }

    public void setFaceDetectProcessed(boolean z, Rect rect) {
        this.mFacedetectProcessed = true;
        this.mFaceDetected = z ? 1 : 0;
        this.mFaceRect = rect;
    }

    public nexCrop getCrop() {
        int i;
        int i2;
        int i3;
        nexCrop nexcrop = this.mCrop;
        if (nexcrop == null) {
            ClipInfo clipInfo = this.mInfo;
            int i4 = clipInfo.mWidth;
            int i5 = clipInfo.mHeight;
            if (clipInfo.mClipType == 1 && ((i3 = clipInfo.mRotateDegreeInMeta) == 90 || i3 == 270)) {
                i2 = i4;
                i = i5;
            } else {
                i = i4;
                i2 = i5;
            }
            this.mCrop = new nexCrop(this.mPath, i, i2, this.mRotate, getProjectDuration());
        } else {
            nexcrop.setRotate(this.mRotate);
            this.mCrop.setClipDuration(getProjectDuration());
        }
        return this.mCrop;
    }

    public boolean setBGMVolume(int i) {
        if (i < 0 || i > 100) {
            return false;
        }
        this.m_BGMVolume = i;
        setProjectUpdateSignal(true);
        return true;
    }

    public int getBGMVolume() {
        return this.m_BGMVolume;
    }

    public boolean setClipVolume(int i) {
        if (i < 0 || i > 200) {
            return false;
        }
        this.m_ClipVolume = i;
        setProjectUpdateSignal(true);
        return true;
    }

    public int getClipVolume() {
        return this.m_ClipVolume;
    }

    public int getProjectDuration() {
        ClipInfo clipInfo = this.mInfo;
        int i = clipInfo.mClipType;
        if (i != 1) {
            if (i == 3) {
                return clipInfo.mTotalTime;
            }
            if (i != 4) {
                return 0;
            }
            int i2 = clipInfo.mTotalTime;
            nexVideoClipEdit nexvideoclipedit = this.mVideoEdit;
            if (nexvideoclipedit == null) {
                return i2;
            }
            int i3 = nexvideoclipedit.mTrimStartDuration;
            if (i3 != 0 || nexvideoclipedit.mTrimEndDuration != 0) {
                i2 = (i2 - i3) - nexvideoclipedit.mTrimEndDuration;
            }
            int speedControl = nexvideoclipedit.getSpeedControl();
            return speedControl != 100 ? Math.round((i2 * 100) / speedControl) : i2;
        }
        return this.mDuration;
    }

    public int[] getSeekPointsSync() {
        MediaInfo mediaInfo;
        if (this.mInfo.mClipType == 4 && (mediaInfo = getMediaInfo()) != null) {
            return mediaInfo.d();
        }
        return null;
    }

    public static boolean cancelThumbnails() {
        return MediaInfo.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getIndexSeekTabNearTimeStamp(int[] iArr, int i, int i2) {
        while (i < iArr.length) {
            if (iArr[i] == i2) {
                return i;
            }
            if (iArr[i] > i2) {
                if (i == 0) {
                    return i;
                }
                int i3 = i - 1;
                return iArr[i] - i2 > i2 - iArr[i3] ? i3 : i;
            }
            i++;
        }
        if (i >= iArr.length) {
            return iArr.length - 1;
        }
        return -1;
    }

    public static nexClip getSolidClip(int i) {
        return getSolidClip(String.format("@solid:%08X.jpg", Integer.valueOf(i)));
    }

    private static nexClip getSolidClip(String str) {
        nexClip nexclip = new nexClip();
        nexclip.mPath = str;
        ClipInfo clipInfo = new ClipInfo();
        nexclip.mInfo = clipInfo;
        clipInfo.mClipType = 1;
        clipInfo.mWidth = 32;
        clipInfo.mHeight = 18;
        return nexclip;
    }

    public boolean isSolid() {
        String str = this.mPath;
        return str != null && str.startsWith("@solid:") && this.mPath.endsWith(".jpg");
    }

    public boolean setSolidColor(int i) {
        if (isSolid()) {
            this.mPath = String.format("@solid:%08X.jpg", Integer.valueOf(i));
            return true;
        }
        return false;
    }

    public int getSolidColor() {
        if (!isSolid()) {
            return 0;
        }
        return (int) Long.parseLong(this.mPath.substring(7, 15), 16);
    }

    public boolean isMotionTrackedVideo() {
        return this.isMotionTrackedVideo;
    }

    public void setMotionTrackedVideo(boolean z) {
        this.isMotionTrackedVideo = z;
    }

    public nexAudioEnvelop getAudioEnvelop() {
        if (this.mAudioEnvelop == null) {
            this.mAudioEnvelop = new nexAudioEnvelop(this);
        }
        return this.mAudioEnvelop;
    }

    public nexClipEffect getClipEffect(boolean z) {
        if (!this.mProjectAttachment) {
            Log.e(TAG, "getClipEffect not project attachment startTime=" + this.mStartTime);
        }
        if (this.mClipEffect == null) {
            this.mClipEffect = new nexClipEffect();
        }
        return this.mClipEffect;
    }

    public nexTransitionEffect getTransitionEffect(boolean z) {
        if (!this.mProjectAttachment) {
            Log.e(TAG, "getTransitionEffect not project attachment startTime=" + this.mStartTime);
        }
        if (this.mTransitionEffect == null) {
            this.mTransitionEffect = new nexTransitionEffect(this.mObserver);
        }
        return this.mTransitionEffect;
    }

    @Deprecated
    public void setVoiceChangerFactor(int i) {
        int i2 = this.mInfo.mClipType;
        if (i2 == 4 || i2 == 3) {
            getAudioEdit().setVoiceChangerFactor(i);
        }
    }

    @Deprecated
    public int getVoiceChangerFactor() {
        int i = this.mInfo.mClipType;
        if (i == 4 || i == 3) {
            return getAudioEdit().getVoiceChangerFactor();
        }
        return 0;
    }

    private int setMediaInfo(MediaInfo mediaInfo) {
        this.mediainfo = mediaInfo;
        return 1;
    }

    public MediaInfo getMediaInfo() {
        if (this.mediainfo == null) {
            this.mediainfo = MediaInfo.a(getRealPath(), this.mMediaInfoUseCache);
        }
        return this.mediainfo;
    }

    public nexAudioEdit getAudioEdit() {
        if (this.mProjectAttachment) {
            int i = this.mInfo.mClipType;
            if (i != 4 && i != 3) {
                return null;
            }
            if (this.mAudioEdit == null) {
                this.mAudioEdit = nexAudioEdit.getnexAudioEdit(this);
            }
            return this.mAudioEdit;
        }
        throw new ProjectNotAttachedException();
    }

    @Deprecated
    public byte[] getVideoUUID() {
        ClipInfo clipInfo = this.mInfo;
        if (clipInfo.mVideoRenderMode == 0) {
            return null;
        }
        return clipInfo.mVideoUUID;
    }

    @Deprecated
    public void setVignetteEffect(boolean z) {
        this.mVignette = z;
    }

    @Deprecated
    public boolean getVignetteEffect() {
        return this.mVignette;
    }

    public boolean isAssetResource() {
        return this.mIsAssetResource;
    }

    public void setAssetResource(boolean z) {
        this.mIsAssetResource = z;
    }

    public int getTemplateEffectID() {
        return this.mTemplateEffectID;
    }

    public void setTemplateEffectID(int i) {
        this.mTemplateEffectID = i;
    }

    public String getCollageDrawInfoID() {
        return this.mCollageDrawInfoID;
    }

    public void setCollageDrawInfoID(String str) {
        this.mCollageDrawInfoID = str;
    }

    public int getTemplateAudioPos() {
        return this.mTemplateAudioPos;
    }

    public void setTemplateAudioPos(int i) {
        this.mTemplateAudioPos = i;
    }

    public boolean getTemplateOverlappedTransition() {
        return this.mOverlappedTransition;
    }

    public void setTemplateOverlappedTransition(boolean z) {
        this.mOverlappedTransition = z;
    }

    public void clearDrawInfos() {
        this.mDrawInfos.clear();
    }

    public void addDrawInfo(nexDrawInfo nexdrawinfo) {
        this.mDrawInfos.add(nexdrawinfo);
    }

    public void addDrawInfos(List<nexDrawInfo> list) {
        for (nexDrawInfo nexdrawinfo : list) {
            this.mDrawInfos.add(nexdrawinfo);
        }
    }

    public void removeDrawInfos(int i) {
        for (nexDrawInfo nexdrawinfo : this.mDrawInfos) {
            if (nexdrawinfo.getSubEffectID() == i) {
                this.mDrawInfos.remove(nexdrawinfo);
                return;
            }
        }
    }

    public List<nexDrawInfo> getDrawInfos() {
        return this.mDrawInfos;
    }

    public boolean getAudioPcmLevels(final OnGetAudioPcmLevelsResultListener onGetAudioPcmLevelsResultListener) {
        MediaInfo mediaInfo;
        if (hasAudio() && (mediaInfo = getMediaInfo()) != null && !this.m_gettingPcmData) {
            this.m_gettingPcmData = true;
            mediaInfo.c().onResultAvailable(new ResultTask.OnResultAvailableListener<com.nexstreaming.kminternal.kinemaster.mediainfo.b>() { // from class: com.nexstreaming.nexeditorsdk.nexClip.4
                @Override // com.nexstreaming.app.common.task.ResultTask.OnResultAvailableListener
                /* renamed from: a */
                public void onResultAvailable(ResultTask<com.nexstreaming.kminternal.kinemaster.mediainfo.b> resultTask, Task.Event event, com.nexstreaming.kminternal.kinemaster.mediainfo.b bVar) {
                    nexClip.this.m_gettingPcmData = false;
                    OnGetAudioPcmLevelsResultListener onGetAudioPcmLevelsResultListener2 = onGetAudioPcmLevelsResultListener;
                    if (onGetAudioPcmLevelsResultListener2 != null) {
                        onGetAudioPcmLevelsResultListener2.onGetAudioPcmLevelsResult(bVar.a());
                    }
                }
            });
            return true;
        }
        return false;
    }

    public void setAVSyncTime(int i) {
        if (getClipType() != 4 || !hasAudio()) {
            return;
        }
        if (i < -500) {
            this.mAVSyncAudioStartTime = -500;
        } else if (i > 500) {
            this.mAVSyncAudioStartTime = 500;
        } else {
            this.mAVSyncAudioStartTime = i;
        }
    }

    public int getAVSyncTime() {
        return this.mAVSyncAudioStartTime;
    }

    public boolean ableToDecoding() {
        boolean hasVideo = hasVideo();
        boolean hasAudio = hasAudio();
        if (!hasVideo && !hasAudio) {
            Log.d(TAG, "ableToDecoding video not found!");
            return true;
        }
        MediaInfo mediaInfo = getMediaInfo();
        if (mediaInfo == null) {
            Log.d(TAG, "ableToDecoding getMediaInfo fail!");
            return false;
        }
        NexEditor.ErrorCode a = mediaInfo.a(hasVideo, hasAudio);
        if (a == NexEditor.ErrorCode.NONE) {
            return true;
        }
        Log.d(TAG, "ableToDecoding fail!=" + a);
        return false;
    }

    public boolean setClipPropertySlowVideoMode(boolean z) {
        ClipInfo clipInfo = this.mInfo;
        if (clipInfo.mClipType != 4) {
            Log.d(TAG, "was  not video");
            return false;
        } else if (clipInfo.mFramesPerSecond < 60) {
            Log.d(TAG, "not supported fps=" + this.mInfo.mFramesPerSecond);
            return false;
        } else {
            int i = clipInfo.mSeekPointCount;
            if (i <= 0) {
                i = 1;
            }
            int i2 = clipInfo.mVideoTotalTime / i;
            if (i2 > 600) {
                Log.d(TAG, "not supported idr dur=" + i2);
                return false;
            }
            this.mPropertySlowVideoMode = z;
            return true;
        }
    }

    public boolean getClipPropertySlowVideoMode() {
        return this.mPropertySlowVideoMode;
    }

    public boolean isEncryptedAssetClip() {
        String str = this.mTransCodingPath;
        return str != null && str.startsWith("@assetItem:");
    }

    public nexSaveDataFormat.nexClipOf getSaveData() {
        nexSaveDataFormat.nexClipOf nexclipof = new nexSaveDataFormat.nexClipOf();
        nexclipof.m_BGMVolume = this.m_BGMVolume;
        nexclipof.mPath = this.mPath;
        nexclipof.mTransCodingPath = this.mTransCodingPath;
        nexClipEffect nexclipeffect = this.mClipEffect;
        if (nexclipeffect == null) {
            nexclipof.mClipEffect = null;
        } else {
            nexclipof.mClipEffect = nexclipeffect.getSaveData();
        }
        nexTransitionEffect nextransitioneffect = this.mTransitionEffect;
        if (nextransitioneffect == null) {
            nexclipof.mTransitionEffect = null;
        } else {
            nexclipof.mTransitionEffect = nextransitioneffect.getSaveData();
        }
        nexclipof.misMustDownSize = this.misMustDownSize;
        nexclipof.mAudioOnOff = this.mAudioOnOff;
        nexclipof.m_Brightness = this.m_Brightness;
        nexclipof.m_Contrast = this.m_Contrast;
        nexclipof.m_Saturation = this.m_Saturation;
        nexclipof.mVignette = this.mVignette;
        nexclipof.mFacedetectProcessed = this.mFacedetectProcessed;
        nexclipof.mFaceDetected = this.mFaceDetected;
        nexclipof.mFaceRect = this.mFaceRect;
        nexCrop nexcrop = this.mCrop;
        if (nexcrop == null) {
            nexclipof.mCrop = null;
        } else {
            nexclipof.mCrop = nexcrop.getSaveData();
        }
        int i = this.mTitleEffectStartTime;
        nexclipof.mTitleEffectStartTime = i;
        nexclipof.mTitleEffectEndTime = i;
        nexAudioEnvelop nexaudioenvelop = this.mAudioEnvelop;
        if (nexaudioenvelop == null) {
            nexclipof.mAudioEnvelop = null;
        } else {
            nexclipof.mAudioEnvelop = nexaudioenvelop.getSaveData();
        }
        nexAudioEdit nexaudioedit = this.mAudioEdit;
        if (nexaudioedit == null) {
            nexclipof.mAudioEdit = null;
        } else {
            nexclipof.mAudioEdit = nexaudioedit.getSaveData();
        }
        nexclipof.mTemplateEffectID = this.mTemplateEffectID;
        nexclipof.mCollageDrawInfoID = this.mCollageDrawInfoID;
        nexclipof.mTemplateAudioPos = this.mTemplateAudioPos;
        nexclipof.mDrawInfos = this.mDrawInfos;
        nexclipof.mOverlappedTransition = this.mOverlappedTransition;
        nexclipof.mMediaInfoUseCache = this.mMediaInfoUseCache;
        nexclipof.mStartTime = this.mStartTime;
        nexclipof.mEndTime = this.mEndTime;
        nexclipof.mDuration = this.mDuration;
        nexclipof.mInfo = this.mInfo;
        nexVideoClipEdit nexvideoclipedit = this.mVideoEdit;
        if (nexvideoclipedit == null) {
            nexclipof.mVideoEdit = null;
        } else {
            nexclipof.mVideoEdit = nexvideoclipedit.getSaveData();
        }
        nexclipof.mCustomLUT_A = this.mCustomLUT_A;
        nexclipof.mCustomLUT_B = this.mCustomLUT_B;
        nexclipof.mCustomLUT_Power = this.mCustomLUT_Power;
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            nexclipof.mColorEffect = null;
        } else {
            nexclipof.mColorEffect = nexcoloreffect.getSaveData();
        }
        nexclipof.mPropertySlowVideoMode = this.mPropertySlowVideoMode;
        nexclipof.mAVSyncAudioStartTime = this.mAVSyncAudioStartTime;
        nexclipof.m_ClipVolume = this.m_ClipVolume;
        nexclipof.m_BGMVolume = this.m_BGMVolume;
        nexclipof.mRotate = this.mRotate;
        return nexclipof;
    }

    public void loadSaveData(nexSaveDataFormat.nexClipOf nexclipof) {
        if (this.mPath.compareTo(nexclipof.mPath) != 0) {
            Log.d(TAG, "loadSaveData invaild path=" + this.mPath + ", input=" + nexclipof.mPath);
            return;
        }
        this.m_BGMVolume = nexclipof.m_BGMVolume;
        nexSaveDataFormat.nexEffectOf nexeffectof = nexclipof.mClipEffect;
        if (nexeffectof == null) {
            this.mClipEffect = null;
        } else {
            this.mClipEffect = new nexClipEffect(nexeffectof);
        }
        if (nexclipof.mTransitionEffect == null) {
            this.mTransitionEffect = null;
        } else {
            this.mTransitionEffect = new nexTransitionEffect(this.mObserver, nexclipof.mTransitionEffect);
        }
        this.misMustDownSize = nexclipof.misMustDownSize;
        this.mAudioOnOff = nexclipof.mAudioOnOff;
        this.m_Brightness = nexclipof.m_Brightness;
        this.m_Contrast = nexclipof.m_Contrast;
        this.m_Saturation = nexclipof.m_Saturation;
        this.mVignette = nexclipof.mVignette;
        this.mFacedetectProcessed = nexclipof.mFacedetectProcessed;
        this.mFaceDetected = nexclipof.mFaceDetected;
        this.mFaceRect = nexclipof.mFaceRect;
        nexSaveDataFormat.nexCropOf nexcropof = nexclipof.mCrop;
        if (nexcropof == null) {
            this.mCrop = null;
        } else {
            this.mCrop = new nexCrop(this.mPath, nexcropof);
        }
        this.mTitleEffectStartTime = nexclipof.mTitleEffectStartTime;
        this.mTitleEffectStartTime = nexclipof.mTitleEffectEndTime;
        nexSaveDataFormat.nexAudioEnvelopOf nexaudioenvelopof = nexclipof.mAudioEnvelop;
        if (nexaudioenvelopof == null) {
            this.mAudioEnvelop = null;
        } else {
            this.mAudioEnvelop = new nexAudioEnvelop(nexaudioenvelopof);
        }
        this.mTemplateEffectID = nexclipof.mTemplateEffectID;
        this.mCollageDrawInfoID = nexclipof.mCollageDrawInfoID;
        this.mTemplateAudioPos = nexclipof.mTemplateAudioPos;
        this.mDrawInfos = nexclipof.mDrawInfos;
        this.mOverlappedTransition = nexclipof.mOverlappedTransition;
        this.mMediaInfoUseCache = nexclipof.mMediaInfoUseCache;
        this.mStartTime = nexclipof.mStartTime;
        this.mEndTime = nexclipof.mEndTime;
        this.mDuration = nexclipof.mDuration;
        this.mInfo = nexclipof.mInfo;
        this.mCustomLUT_A = nexclipof.mCustomLUT_A;
        this.mCustomLUT_B = nexclipof.mCustomLUT_B;
        this.mCustomLUT_Power = nexclipof.mCustomLUT_Power;
        this.mRotate = nexclipof.mRotate;
        if (nexclipof.mColorEffect == null) {
            this.mColorEffect = null;
        } else {
            this.mColorEffect = new nexColorEffect(nexclipof.mColorEffect);
        }
        this.mPropertySlowVideoMode = nexclipof.mPropertySlowVideoMode;
        this.mAVSyncAudioStartTime = nexclipof.mAVSyncAudioStartTime;
        this.m_ClipVolume = nexclipof.m_ClipVolume;
        this.m_BGMVolume = nexclipof.m_BGMVolume;
        if (nexclipof.mVideoEdit == null) {
            this.mVideoEdit = null;
        } else {
            this.mVideoEdit = new nexVideoClipEdit(this, nexclipof.mVideoEdit);
        }
        nexSaveDataFormat.nexAudioEditOf nexaudioeditof = nexclipof.mAudioEdit;
        if (nexaudioeditof == null) {
            this.mAudioEdit = null;
        } else {
            this.mAudioEdit = new nexAudioEdit(this, nexaudioeditof);
        }
    }

    public nexClip(b bVar, nexSaveDataFormat.nexClipOf nexclipof) {
        this.mPath = null;
        this.mTransCodingPath = null;
        this.mClipEffect = null;
        this.mTransitionEffect = null;
        this.mSingleThumbnail = null;
        this.mThumbnails = null;
        this.m_gettingThumbnails = false;
        this.m_getThumbnailsFailed = false;
        this.mAudioOnOff = true;
        this.mFacedetectProcessed = false;
        this.mFaceDetected = 0;
        this.mFaceRect = new Rect();
        this.index = 0;
        this.count = 0;
        this.mAudioEnvelop = null;
        this.mediainfo = null;
        this.mTemplateEffectID = 0;
        this.mTemplateAudioPos = 0;
        this.mDrawInfos = new ArrayList();
        this.mOverlappedTransition = true;
        this.mMediaInfoUseCache = true;
        this.mStartTime = 0;
        this.mEndTime = 0;
        this.mDuration = 6000;
        this.mCustomLUT_A = 0;
        this.mCustomLUT_B = 0;
        this.mCustomLUT_Power = nexCrop.ABSTRACT_DIMENSION;
        this.mColorEffect = nexColorEffect.NONE;
        this.mAVSyncAudioStartTime = 0;
        this.m_ClipVolume = 100;
        this.m_BGMVolume = 100;
        this.mRotate = 0;
        this.isMotionTrackedVideo = false;
        this.mIsAssetResource = false;
        this.m_BGMVolume = nexclipof.m_BGMVolume;
        this.mPath = nexclipof.mPath;
        this.mTransCodingPath = nexclipof.mTransCodingPath;
        this.mProjectAttachment = true;
        nexSaveDataFormat.nexEffectOf nexeffectof = nexclipof.mClipEffect;
        if (nexeffectof == null) {
            this.mClipEffect = null;
        } else {
            this.mClipEffect = new nexClipEffect(nexeffectof);
        }
        if (nexclipof.mTransitionEffect == null) {
            this.mTransitionEffect = null;
        } else {
            this.mTransitionEffect = new nexTransitionEffect(bVar, nexclipof.mTransitionEffect);
        }
        this.misMustDownSize = nexclipof.misMustDownSize;
        this.mObserver = bVar;
        this.mAudioOnOff = nexclipof.mAudioOnOff;
        this.m_Brightness = nexclipof.m_Brightness;
        this.m_Contrast = nexclipof.m_Contrast;
        this.m_Saturation = nexclipof.m_Saturation;
        this.mVignette = nexclipof.mVignette;
        this.mFacedetectProcessed = nexclipof.mFacedetectProcessed;
        this.mFaceDetected = nexclipof.mFaceDetected;
        this.mFaceRect = nexclipof.mFaceRect;
        nexSaveDataFormat.nexCropOf nexcropof = nexclipof.mCrop;
        if (nexcropof == null) {
            this.mCrop = null;
        } else {
            this.mCrop = new nexCrop(this.mPath, nexcropof);
        }
        this.mTitleEffectStartTime = nexclipof.mTitleEffectStartTime;
        this.mTitleEffectStartTime = nexclipof.mTitleEffectEndTime;
        nexSaveDataFormat.nexAudioEnvelopOf nexaudioenvelopof = nexclipof.mAudioEnvelop;
        if (nexaudioenvelopof == null) {
            this.mAudioEnvelop = null;
        } else {
            this.mAudioEnvelop = new nexAudioEnvelop(nexaudioenvelopof);
        }
        this.mTemplateEffectID = nexclipof.mTemplateEffectID;
        this.mCollageDrawInfoID = nexclipof.mCollageDrawInfoID;
        this.mTemplateAudioPos = nexclipof.mTemplateAudioPos;
        this.mDrawInfos = nexclipof.mDrawInfos;
        this.mOverlappedTransition = nexclipof.mOverlappedTransition;
        this.mMediaInfoUseCache = nexclipof.mMediaInfoUseCache;
        this.mStartTime = nexclipof.mStartTime;
        this.mEndTime = nexclipof.mEndTime;
        this.mDuration = nexclipof.mDuration;
        this.mInfo = nexclipof.mInfo;
        this.mCustomLUT_A = nexclipof.mCustomLUT_A;
        this.mCustomLUT_B = nexclipof.mCustomLUT_B;
        this.mCustomLUT_Power = nexclipof.mCustomLUT_Power;
        this.mRotate = nexclipof.mRotate;
        if (nexclipof.mColorEffect == null) {
            this.mColorEffect = null;
        } else {
            this.mColorEffect = new nexColorEffect(nexclipof.mColorEffect);
        }
        this.mPropertySlowVideoMode = nexclipof.mPropertySlowVideoMode;
        this.mAVSyncAudioStartTime = nexclipof.mAVSyncAudioStartTime;
        this.m_ClipVolume = nexclipof.m_ClipVolume;
        this.m_BGMVolume = nexclipof.m_BGMVolume;
        if (nexclipof.mVideoEdit == null) {
            this.mVideoEdit = null;
        } else {
            this.mVideoEdit = new nexVideoClipEdit(this, nexclipof.mVideoEdit);
        }
        nexSaveDataFormat.nexAudioEditOf nexaudioeditof = nexclipof.mAudioEdit;
        if (nexaudioeditof == null) {
            this.mAudioEdit = null;
        } else {
            this.mAudioEdit = new nexAudioEdit(this, nexaudioeditof);
        }
    }
}
