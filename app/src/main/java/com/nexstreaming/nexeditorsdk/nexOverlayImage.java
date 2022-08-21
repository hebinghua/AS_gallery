package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import com.nexstreaming.app.common.nexasset.overlay.OverlayAsset;
import com.nexstreaming.app.common.nexasset.overlay.OverlayAssetFactory;
import com.nexstreaming.app.common.util.FileType;
import com.nexstreaming.nexeditorsdk.exception.ClipIsNotVideoException;
import com.nexstreaming.nexeditorsdk.exception.nexSDKException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public class nexOverlayImage implements Cloneable {
    private static final String TAG = "nexOverlayImage";
    public static final int kOverlayType_Asset = 6;
    public static final int kOverlayType_ResourceImage = 1;
    public static final int kOverlayType_RunTimeImage = 4;
    public static final int kOverlayType_SolidColorImage = 5;
    public static final int kOverlayType_UserImage = 2;
    public static final int kOverlayType_UserVideo = 3;
    private static Map<String, nexOverlayImage> sOverlayImageItems;
    private int anchorPoint;
    private OverlayAsset cachedOverlayAsset;
    private boolean mAssetManager;
    private int mBitmapHeight;
    private int mBitmapInSample;
    private String mBitmapPath;
    private int mBitmapWidth;
    private int mHeight;
    private String mId;
    private runTimeMakeBitMap mMakeBitMap;
    public int mRecommandDuration;
    public int mResourceId;
    private int mSolidColor;
    private int mType;
    public boolean mUpdate;
    private boolean mUpdateInfo;
    private VideoClipInfo mVideoClipInfo;
    private int mWidth;

    /* loaded from: classes3.dex */
    public interface runTimeMakeBitMap {
        int getBitmapID();

        boolean isAniMate();

        Bitmap makeBitmap();
    }

    public void releaseBitmap() {
    }

    public static nexOverlayImage clone(nexOverlayImage nexoverlayimage) {
        nexOverlayImage nexoverlayimage2;
        nexOverlayImage nexoverlayimage3 = null;
        try {
            nexoverlayimage2 = (nexOverlayImage) nexoverlayimage.clone();
        } catch (CloneNotSupportedException e) {
            e = e;
        }
        try {
            VideoClipInfo videoClipInfo = nexoverlayimage.mVideoClipInfo;
            if (videoClipInfo != null) {
                nexoverlayimage2.mVideoClipInfo = VideoClipInfo.clone(videoClipInfo);
            }
            nexoverlayimage2.mId = nexoverlayimage.mId;
            return nexoverlayimage2;
        } catch (CloneNotSupportedException e2) {
            e = e2;
            nexoverlayimage3 = nexoverlayimage2;
            e.printStackTrace();
            return nexoverlayimage3;
        }
    }

    public static boolean registerOverlayImage(nexOverlayImage nexoverlayimage) {
        if (sOverlayImageItems == null) {
            sOverlayImageItems = new HashMap();
        }
        if (sOverlayImageItems.containsKey(nexoverlayimage.getId())) {
            return false;
        }
        sOverlayImageItems.put(nexoverlayimage.getId(), nexoverlayimage);
        return true;
    }

    public static boolean unregisterOverlayImage(String str) {
        if (sOverlayImageItems == null) {
            sOverlayImageItems = new HashMap();
        }
        sOverlayImageItems.remove(str);
        return true;
    }

    public static void allClearRegisterOverlayImage() {
        if (sOverlayImageItems == null) {
            sOverlayImageItems = new HashMap();
        }
        sOverlayImageItems.clear();
    }

    public static nexOverlayImage getOverlayImage(String str) {
        if (sOverlayImageItems == null) {
            sOverlayImageItems = new HashMap();
        }
        return sOverlayImageItems.get(str);
    }

    public String getId() {
        return this.mId;
    }

    private int getType() {
        int i = this.mType;
        if (i == 0) {
            if (this.mAssetManager) {
                this.mType = 6;
                return 6;
            } else if (this.mMakeBitMap != null) {
                this.mType = 4;
                return 4;
            } else if (this.mVideoClipInfo != null) {
                this.mType = 3;
                return 3;
            } else if (this.mResourceId != 0) {
                this.mType = 1;
                return 1;
            } else if (this.mSolidColor != 0) {
                this.mType = 5;
                return 5;
            } else if (this.mBitmapPath == null) {
                return i;
            } else {
                this.mType = 2;
                return 2;
            }
        }
        return i;
    }

    public nexOverlayImage(String str, int i, int i2, runTimeMakeBitMap runtimemakebitmap) {
        this.mBitmapInSample = 1;
        this.anchorPoint = 4;
        this.mUpdate = false;
        this.mRecommandDuration = -1;
        this.mId = str;
        this.mResourceId = 0;
        this.mMakeBitMap = runtimemakebitmap;
        this.mBitmapPath = null;
        this.mWidth = i;
        this.mHeight = i2;
        this.mUpdateInfo = true;
        this.mBitmapWidth = i;
        this.mBitmapHeight = i2;
        this.mVideoClipInfo = null;
        this.mSolidColor = 0;
    }

    public nexOverlayImage(String str, int i, int i2, String str2) {
        this.mBitmapInSample = 1;
        this.anchorPoint = 4;
        this.mUpdate = false;
        this.mRecommandDuration = -1;
        this.mId = str;
        this.mResourceId = 0;
        this.mMakeBitMap = null;
        this.mBitmapPath = str2;
        this.mWidth = i;
        this.mHeight = i2;
        this.mUpdateInfo = true;
        this.mBitmapWidth = i;
        this.mBitmapHeight = i2;
        this.mVideoClipInfo = null;
        this.mSolidColor = 0;
        initSample();
    }

    public nexOverlayImage(String str) {
        this.mBitmapInSample = 1;
        this.anchorPoint = 4;
        this.mUpdate = false;
        this.mRecommandDuration = -1;
        this.mId = str;
        this.mResourceId = 0;
        this.mMakeBitMap = null;
        this.mBitmapPath = null;
        this.mUpdateInfo = false;
        this.mBitmapWidth = this.mWidth;
        this.mBitmapHeight = this.mHeight;
        this.mVideoClipInfo = null;
        this.mSolidColor = 0;
    }

    public nexOverlayImage(String str, boolean z) {
        this.mBitmapInSample = 1;
        this.anchorPoint = 4;
        this.mUpdate = false;
        this.mRecommandDuration = -1;
        this.mId = str;
        this.mResourceId = 0;
        this.mMakeBitMap = null;
        this.mBitmapPath = null;
        this.mUpdateInfo = false;
        this.mBitmapWidth = this.mWidth;
        this.mBitmapHeight = this.mHeight;
        this.mVideoClipInfo = null;
        this.mSolidColor = 0;
        this.mAssetManager = z;
    }

    public nexOverlayImage(String str, String str2) {
        this.mBitmapInSample = 1;
        this.anchorPoint = 4;
        this.mUpdate = false;
        this.mRecommandDuration = -1;
        this.mId = str;
        this.mResourceId = 0;
        this.mMakeBitMap = null;
        this.mBitmapPath = str2;
        this.mUpdateInfo = true;
        this.mBitmapWidth = this.mWidth;
        this.mBitmapHeight = this.mHeight;
        this.mVideoClipInfo = null;
        this.mSolidColor = 0;
        FileType fromFile = FileType.fromFile(str2);
        if (fromFile != null && fromFile.isImage()) {
            this.mBitmapPath = str2;
            initSample();
            return;
        }
        this.mBitmapPath = null;
        nexClip supportedClip = nexClip.getSupportedClip(str2);
        if (supportedClip == null) {
            throw new ClipIsNotVideoException();
        }
        if (supportedClip.getClipType() == 4) {
            VideoClipInfo videoClipInfo = new VideoClipInfo();
            this.mVideoClipInfo = videoClipInfo;
            videoClipInfo.mHasAudio = supportedClip.hasAudio();
            this.mVideoClipInfo.mHasVideo = supportedClip.hasVideo();
            this.mVideoClipInfo.mPath = supportedClip.getRealPath();
            this.mVideoClipInfo.mTotalTime = supportedClip.getTotalTime();
            this.mVideoClipInfo.mWidth = supportedClip.getWidth();
            this.mVideoClipInfo.mHeight = supportedClip.getHeight();
            return;
        }
        throw new ClipIsNotVideoException();
    }

    public nexOverlayImage(String str, Context context, int i) {
        this.mBitmapInSample = 1;
        this.anchorPoint = 4;
        this.mUpdate = false;
        this.mRecommandDuration = -1;
        this.mId = str;
        this.mResourceId = i;
        this.mUpdateInfo = true;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getApplicationContext().getResources(), this.mResourceId, options);
        this.mWidth = options.outWidth;
        this.mHeight = options.outHeight;
        this.mMakeBitMap = null;
        this.mBitmapPath = null;
        this.mBitmapWidth = 0;
        this.mBitmapHeight = 0;
        this.mVideoClipInfo = null;
        this.mSolidColor = 0;
    }

    public nexOverlayImage(String str, runTimeMakeBitMap runtimemakebitmap) {
        this.mBitmapInSample = 1;
        this.anchorPoint = 4;
        this.mUpdate = false;
        this.mRecommandDuration = -1;
        this.mId = str;
        this.mResourceId = 0;
        this.mMakeBitMap = runtimemakebitmap;
        this.mBitmapPath = null;
        this.mBitmapWidth = 0;
        this.mBitmapHeight = 0;
        this.mVideoClipInfo = null;
        this.mSolidColor = 0;
    }

    public nexOverlayImage(String str, nexClip nexclip) {
        this.mBitmapInSample = 1;
        this.anchorPoint = 4;
        this.mUpdate = false;
        this.mRecommandDuration = -1;
        this.mId = str;
        this.mResourceId = 0;
        this.mMakeBitMap = null;
        this.mBitmapWidth = 0;
        this.mBitmapHeight = 0;
        if (nexclip.getClipType() == 4) {
            VideoClipInfo videoClipInfo = new VideoClipInfo();
            this.mVideoClipInfo = videoClipInfo;
            videoClipInfo.mHasAudio = nexclip.hasAudio();
            this.mVideoClipInfo.mHasVideo = nexclip.hasVideo();
            this.mVideoClipInfo.mPath = nexclip.getRealPath();
            this.mVideoClipInfo.mTotalTime = nexclip.getTotalTime();
            this.mVideoClipInfo.mWidth = nexclip.getWidth();
            this.mVideoClipInfo.mHeight = nexclip.getHeight();
            this.mSolidColor = 0;
            this.mBitmapPath = null;
        } else if (nexclip.getClipType() == 1) {
            if (nexclip.isSolid()) {
                this.mVideoClipInfo = null;
                this.mSolidColor = nexclip.getSolidColor();
                this.mBitmapPath = null;
                return;
            }
            this.mSolidColor = 0;
            this.mVideoClipInfo = null;
            this.mBitmapPath = nexclip.getRealPath();
            initSample();
        } else {
            this.mVideoClipInfo = null;
            this.mSolidColor = 0;
            this.mBitmapPath = null;
            throw new nexSDKException("Audio not supported!");
        }
    }

    public nexOverlayImage(String str, int i) {
        this.mBitmapInSample = 1;
        this.anchorPoint = 4;
        this.mUpdate = false;
        this.mRecommandDuration = -1;
        this.mId = str;
        this.mResourceId = 0;
        this.mMakeBitMap = null;
        this.mWidth = 32;
        this.mHeight = 18;
        this.mBitmapPath = null;
        this.mBitmapWidth = 32;
        this.mBitmapHeight = 18;
        this.mSolidColor = i;
        this.mVideoClipInfo = null;
        this.mUpdateInfo = true;
    }

    public int getResourceId() {
        return this.mResourceId;
    }

    public boolean isAniMate() {
        runTimeMakeBitMap runtimemakebitmap = this.mMakeBitMap;
        return runtimemakebitmap != null && runtimemakebitmap.isAniMate();
    }

    private void updateInfo() {
        if (!this.mUpdateInfo) {
            int type = getType();
            if (type != 1) {
                if (type == 3) {
                    this.mWidth = this.mVideoClipInfo.mWidth;
                    this.mHeight = this.mVideoClipInfo.mHeight;
                    this.mUpdateInfo = true;
                } else if (type != 5) {
                    if (type == 6) {
                        getUserBitmap();
                        this.mWidth = this.cachedOverlayAsset.getIntrinsicWidth();
                        this.mHeight = this.cachedOverlayAsset.getIntrinsicHeight();
                    } else {
                        Bitmap userBitmap = getUserBitmap();
                        this.mWidth = userBitmap.getWidth();
                        this.mHeight = userBitmap.getHeight();
                    }
                }
            }
            this.mUpdateInfo = true;
        }
    }

    public int getWidth() {
        updateInfo();
        return this.mWidth;
    }

    public int getHeight() {
        updateInfo();
        return this.mHeight;
    }

    public void getBound(Rect rect) {
        updateInfo();
        int i = this.mWidth;
        rect.left = 0 - (i / 2);
        int i2 = this.mHeight;
        rect.top = 0 - (i2 / 2);
        rect.right = (i / 2) + 0;
        rect.bottom = (i2 / 2) + 0;
    }

    public OverlayAsset getOverlayAssetBitmap() throws IOException, XmlPullParserException {
        if (this.mAssetManager) {
            if (this.cachedOverlayAsset == null) {
                this.cachedOverlayAsset = OverlayAssetFactory.forItem(this.mId);
            }
            return this.cachedOverlayAsset;
        }
        return null;
    }

    public Bitmap getUserBitmap() {
        int type = getType();
        if (type != 1) {
            if (type != 3) {
                if (type == 4) {
                    return this.mMakeBitMap.makeBitmap();
                }
                if (type == 5) {
                    return getSolidRect();
                }
                if (type == 6) {
                    try {
                        getOverlayAssetBitmap();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    if (this.mBitmapWidth == 0) {
                        this.mBitmapWidth = 1280;
                    }
                    if (this.mBitmapHeight == 0) {
                        this.mBitmapHeight = 720;
                    }
                    return decodeSampledBitmapFromFile(this.mBitmapPath, this.mBitmapWidth, this.mBitmapHeight);
                }
            }
            return null;
        }
        return BitmapFactory.decodeResource(com.nexstreaming.kminternal.kinemaster.config.a.a().b().getResources(), this.mResourceId);
    }

    @Deprecated
    public void setCrop(int i, int i2) {
        if (getType() == 2) {
            this.mBitmapWidth = i;
            this.mBitmapHeight = i2;
        }
    }

    @Deprecated
    public void resizeBitmap(int i, int i2) {
        if (getType() == 2) {
            this.mBitmapWidth = i;
            this.mBitmapHeight = i2;
        }
    }

    public String getUserBitmapID() {
        if (getType() == 4 && this.mMakeBitMap != null) {
            return this.mId + this.mMakeBitMap.getBitmapID();
        } else if (getType() == 2 && this.mBitmapPath != null) {
            return this.mId + this.mBitmapPath.hashCode();
        } else {
            return this.mId;
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        if (i3 > i2 || i4 > i) {
            int round = Math.round(i3 / i2);
            int round2 = Math.round(i4 / i);
            return round < round2 ? round : round2;
        }
        return 1;
    }

    private Bitmap decodeSampledBitmapFromFile(String str, int i, int i2) {
        int round;
        int round2;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = this.mBitmapInSample;
        options.inJustDecodeBounds = false;
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
        if (decodeFile != null) {
            if (decodeFile.getWidth() == i || decodeFile.getHeight() == i2) {
                return decodeFile;
            }
            float width = decodeFile.getWidth() / i2;
            float height = decodeFile.getHeight() / i;
            if (height > width) {
                round = Math.round(decodeFile.getWidth() * height);
                round2 = Math.round(height * decodeFile.getHeight());
            } else {
                round = Math.round(decodeFile.getWidth() * width);
                round2 = Math.round(width * decodeFile.getHeight());
            }
            return Bitmap.createScaledBitmap(decodeFile, round, round2, true);
        }
        return null;
    }

    public boolean isVideo() {
        return getType() == 3;
    }

    public boolean isUpdated() {
        return this.mUpdate;
    }

    public VideoClipInfo getVideoClipInfo() {
        return this.mVideoClipInfo;
    }

    /* loaded from: classes3.dex */
    public static class VideoClipInfo implements Cloneable {
        private boolean mHasAudio;
        private boolean mHasVideo;
        private int mHeight;
        private String mPath;
        private int mTotalTime;
        private int mWidth;

        public boolean hasVideo() {
            return this.mHasVideo;
        }

        public boolean hasAudio() {
            return this.mHasAudio;
        }

        public String getPath() {
            return this.mPath;
        }

        public int getTotalTime() {
            return this.mTotalTime;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public static VideoClipInfo clone(VideoClipInfo videoClipInfo) {
            VideoClipInfo videoClipInfo2 = null;
            try {
                VideoClipInfo videoClipInfo3 = (VideoClipInfo) videoClipInfo.clone();
                try {
                    videoClipInfo3.mPath = videoClipInfo.mPath;
                    return videoClipInfo3;
                } catch (CloneNotSupportedException e) {
                    e = e;
                    videoClipInfo2 = videoClipInfo3;
                    e.printStackTrace();
                    return videoClipInfo2;
                }
            } catch (CloneNotSupportedException e2) {
                e = e2;
            }
        }
    }

    public boolean isAssetManager() {
        return getType() == 6;
    }

    private Bitmap getSolidRect() {
        int i = this.mSolidColor;
        int[] iArr = new int[576];
        for (int i2 = 0; i2 < 576; i2++) {
            iArr[i2] = i;
        }
        return Bitmap.createBitmap(iArr, 32, 18, Bitmap.Config.ARGB_8888);
    }

    private void initSample() {
        if (this.mBitmapPath != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(this.mBitmapPath, options);
            int i = 720;
            int i2 = 1280;
            if (options.outHeight <= options.outWidth) {
                i2 = 720;
                i = 1280;
            }
            int calculateInSampleSize = calculateInSampleSize(options, i, i2);
            options.inSampleSize = calculateInSampleSize;
            this.mBitmapInSample = calculateInSampleSize;
            BitmapFactory.decodeFile(this.mBitmapPath, options);
            int i3 = options.outWidth;
            this.mWidth = i3;
            int i4 = options.outHeight;
            this.mHeight = i4;
            this.mBitmapWidth = i3;
            this.mBitmapHeight = i4;
        }
    }

    public void setAnchorPoint(int i) {
        this.anchorPoint = i;
    }

    public int getAnchorPoint() {
        return this.anchorPoint;
    }

    public int getDefaultDuration() {
        if (this.mRecommandDuration < 0) {
            int i = 0;
            if (getType() == 6) {
                try {
                    i = getOverlayAssetBitmap().getDefaultDuration();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e2) {
                    e2.printStackTrace();
                }
            }
            this.mRecommandDuration = i;
        }
        return this.mRecommandDuration;
    }
}
