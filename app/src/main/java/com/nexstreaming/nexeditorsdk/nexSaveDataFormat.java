package com.nexstreaming.nexeditorsdk;

import android.graphics.Rect;
import android.graphics.RectF;
import com.nexstreaming.nexeditorsdk.nexAssetPackageManager;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes3.dex */
public final class nexSaveDataFormat {
    public nexCollageOf collage;
    public int nexSaveDataFormatVersion = 0;
    public nexProjectOf project;

    /* loaded from: classes3.dex */
    public static class nexAudioEditOf {
        public int VCfactor = 0;
        public int mPitch = 0;
        public int mCompressor = 0;
        public int mProcessorStrength = -1;
        public int mBassStrength = -1;
        public int mMusicEffect = 0;
        public int mPanLeft = -111;
        public int mPanRight = -111;
    }

    /* loaded from: classes3.dex */
    public static class nexAudioEnvelopOf {
        public int m_totalTime;
        public int m_trimEndTime;
        public int m_trimStartTime;
        public ArrayList<Integer> m_volumeEnvelopeLevel;
        public ArrayList<Integer> m_volumeEnvelopeTime;
    }

    /* loaded from: classes3.dex */
    public static class nexAudioItemOf {
        public nexClipOf mClip;
        public int mId;
        public int mSpeedControl = 100;
        public int mTrimEndDuration;
        public int mTrimStartDuration;
    }

    /* loaded from: classes3.dex */
    public static class nexClipOf {
        public nexAudioEditOf mAudioEdit;
        public String mCollageDrawInfoID;
        public nexColorEffectOf mColorEffect;
        public nexCropOf mCrop;
        public List<nexDrawInfo> mDrawInfos;
        public Rect mFaceRect;
        public nexClip.ClipInfo mInfo;
        public boolean mPropertySlowVideoMode;
        public int mRotate;
        public int mTitleEffectEndTime;
        public int mTitleEffectStartTime;
        public nexVideoClipEditOf mVideoEdit;
        public boolean mVignette;
        public int m_Brightness;
        public int m_Contrast;
        public int m_Saturation;
        public boolean misMustDownSize;
        public String mPath = null;
        public String mTransCodingPath = null;
        public nexEffectOf mClipEffect = null;
        public nexEffectOf mTransitionEffect = null;
        public boolean mAudioOnOff = true;
        public boolean mFacedetectProcessed = false;
        public int mFaceDetected = 0;
        public nexAudioEnvelopOf mAudioEnvelop = null;
        public int mTemplateEffectID = 0;
        public int mTemplateAudioPos = 0;
        public boolean mOverlappedTransition = true;
        public boolean mMediaInfoUseCache = true;
        public int mStartTime = 0;
        public int mEndTime = 0;
        public int mDuration = 6000;
        public int mCustomLUT_A = 0;
        public int mCustomLUT_B = 0;
        public int mCustomLUT_Power = nexCrop.ABSTRACT_DIMENSION;
        public int mAVSyncAudioStartTime = 0;
        public int m_ClipVolume = 100;
        public int m_BGMVolume = 100;
    }

    /* loaded from: classes3.dex */
    public static class nexCollageDrawInfoOf {
        public RectF scaledRect;
        public String userLut;
    }

    /* loaded from: classes3.dex */
    public static class nexCollageOf {
        public List<nexCollageDrawInfoOf> drawInfos;
        public List<nexCollageTitleInfoOf> titleInfos;
    }

    /* loaded from: classes3.dex */
    public static class nexCollageTitleInfoOf {
        public String userDropShadowColor;
        public String userFillColor;
        public String userFont;
        public String userStrokeColor;
        public String userText;
    }

    /* loaded from: classes3.dex */
    public static class nexColorEffectOf {
        public String assetItemID;
        public float brightness;
        public float contrast;
        public String kineMasterID;
        public boolean lut_enabled_ = false;
        public int lut_resource_id_ = 0;
        public String presetName;
        public float saturation;
        public int tintColor;
    }

    /* loaded from: classes3.dex */
    public static class nexCropOf {
        public float m_faceBounds_bottom;
        public float m_faceBounds_left;
        public float m_faceBounds_right;
        public boolean m_faceBounds_set;
        public float m_faceBounds_top;
        public int m_startPositionLeft = 0;
        public int m_startPositionBottom = 0;
        public int m_startPositionRight = 0;
        public int m_startPositionTop = 0;
        public int m_endPositionLeft = 0;
        public int m_endPositionBottom = 0;
        public int m_endPositionRight = 0;
        public int m_endPositionTop = 0;
        public int m_rotatedStartPositionLeft = 0;
        public int m_rotatedStartPositionBottom = 0;
        public int m_rotatedStartPositionRight = 0;
        public int m_rotatedStartPositionTop = 0;
        public int m_rotatedEndPositionLeft = 0;
        public int m_rotatedEndPositionBottom = 0;
        public int m_rotatedEndPositionRight = 0;
        public int m_rotatedEndPositionTop = 0;
        public int m_facePositionLeft = 0;
        public int m_facePositionTop = 0;
        public int m_facePositionRight = 0;
        public int m_facePositionBottom = 0;
        public int m_rotatedFacePositionLeft = 0;
        public int m_rotatedFacePositionTop = 0;
        public int m_rotatedFacePositionRight = 0;
        public int m_rotatedFacePositionBottom = 0;
        public int m_rotation = 0;
        public int m_width = 0;
        public int m_height = 0;
    }

    /* loaded from: classes3.dex */
    public static class nexEffectOf {
        public nexAssetPackageManager.ItemMethodType itemMethodType;
        public String mAutoID;
        public int mDuration;
        public int mEffectOffset;
        public int mEffectOverlap;
        public String mID;
        public int mMaxDuration;
        public int mMinDuration;
        public String mName;
        public boolean mOptionsUpdate;
        public int mType;
        public HashMap<String, String> m_effectOptions;
        public String[] mTitles = null;
        public boolean mIsResolveOptions = false;
        public int mShowStartTime = 0;
        public int mShowEndTime = 10000;
    }

    /* loaded from: classes3.dex */
    public static class nexProjectOf {
        public int mBGMTrimEndTime;
        public int mBGMTrimStartTime;
        public List<nexClipOf> mPrimaryItems;
        public List<nexAudioItemOf> mSecondaryItems;
        public List<nexDrawInfo> mSubEffectInfo;
        public List<nexDrawInfo> mTopEffectInfo;
        public int mProjectVolume = 100;
        public int mManualVolCtl = 0;
        public int mAudioFadeInTime = 200;
        public int mAudioFadeOutTime = 5000;
        public String mOpeningTitle = null;
        public String mEndingTitle = null;
        public float mBGMVolumeScale = 0.5f;
        public boolean mUseThemeMusic2BGM = true;
        public boolean mLoopBGM = true;
        public int mStartTimeBGM = 0;
        public nexClipOf mBackGroundMusic = null;
        public int mTemplateApplyMode = 0;
        public boolean mTemplateOverlappedTransition = true;
    }

    /* loaded from: classes3.dex */
    public static class nexVideoClipEditOf {
        public int mMasterSpeedControl = 100;
        public int mTrimEndDuration;
        public int mTrimStartDuration;
    }
}
