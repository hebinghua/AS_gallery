package com.meicam.themehelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meicam.sdk.NvsVideoTransition;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class NvsThemeHelper {
    private static final int DEFAULT_FONT_COLOR = -1;
    private static final String FONT_MAX_LENGTH = "一二三四五六七八九十";
    private static final int NVS_MAX_IMAGE_FILE_COUNT = 20;
    private static final long NVS_MAX_TIME_LINE_LENGTH = 90000000;
    private static final int NVS_MIN_IMAGE_FILE_COUNT = 3;
    private static final int NVS_USE_ALL_PHOTO = -2;
    private static final int NVS_USE_CURRENT_DURATION = -1;
    private static final String TAG = "NvsThemeHelper";
    private Context m_context;
    private String m_defaultRhythm10sPath;
    private String m_defaultRhythmPath;
    private ArrayList<NvsImageFileDesc> m_inputIamgeInfo;
    private String m_stickerId1;
    private String m_stickerId2;
    private String m_themeAssetID;
    private NvsTimeline m_timeline;
    public static Random rand = new Random(1000);
    public static float m_timelineRatio = 0.5625f;
    private int DEFAULT_FONT1_SIZE = 71;
    private int DEFAULT_FONT2_SIZE = 39;
    private NvsStreamingContext m_streamingContext = null;
    private long seed = 1000;
    private long timeBase = 1000000;
    private long clipMaxLen = 1000000 * 10;
    private boolean m_is10sMode = false;
    private boolean m_showCaption = false;
    private NvsTimelineAnimatedSticker m_cafSticker = null;
    private NvsTimelineAnimatedSticker m_caption1 = null;
    private NvsTimelineAnimatedSticker m_caption2 = null;
    private String m_caption1Text = new String();
    private String m_caption2Text = new String();
    private String m_captionBitmapPath1 = new String();
    private String m_captionBitmapPath2 = new String();
    private StringBuilder m_cafStickerId = new StringBuilder();
    private int m_selectedMusicType = -1;
    private float m_musicVolumeGain = 1.0f;
    private int m_selectedExtMusicIdx = -1;
    private String m_selectedLocalMusic = null;
    private long m_selectedLocalMusicStart = -1;
    private long m_selectedLocalMusicEnd = -1;
    private long m_maxTotalTime = NVS_MAX_TIME_LINE_LENGTH;
    private RectF coverStartROI = null;
    private RectF coverEndROI = null;
    private int m_fxTransClipCount = 0;
    private Map<String, NvsThemeAsset> mThemeAssetMap = new HashMap();
    private ArrayList<NvsMusicFileDesc> mMusicFileList = new ArrayList<>();

    private int getMaxImgCount() {
        return 20;
    }

    public boolean initHelper(Context context, NvsTimeline nvsTimeline, ArrayList<NvsImageFileDesc> arrayList) {
        this.m_context = context;
        if (context == null) {
            Log.d(TAG, "Context can not be NULL");
            return false;
        } else if (nvsTimeline == null) {
            Log.d(TAG, "timeline can not be NULL");
            return false;
        } else if (arrayList == null || arrayList.size() < 1) {
            Log.d(TAG, "file list is empty");
            return false;
        } else {
            this.DEFAULT_FONT1_SIZE = Utils.dip2px(context, 26.0f);
            this.DEFAULT_FONT2_SIZE = Utils.dip2px(context, 15.0f);
            this.m_inputIamgeInfo = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                this.m_inputIamgeInfo.add(arrayList.get(i).copy());
            }
            this.m_timeline = nvsTimeline;
            NvsVideoResolution videoRes = nvsTimeline.getVideoRes();
            m_timelineRatio = (videoRes.imageWidth * 1.0f) / videoRes.imageHeight;
            return true;
        }
    }

    public void randomFx() {
        this.seed = System.nanoTime();
    }

    public boolean setDefaultRhythmPath(String str, String str2) {
        this.m_defaultRhythmPath = str;
        this.m_defaultRhythm10sPath = str2;
        if (str == null || str2 == null) {
            return false;
        }
        this.m_defaultRhythmPath = str.replace("assets:/", "");
        this.m_defaultRhythm10sPath = this.m_defaultRhythm10sPath.replace("assets:/", "");
        return true;
    }

    public boolean applyTimelineTheme(String str, boolean z, boolean z2) {
        if (this.m_context == null || this.m_inputIamgeInfo.isEmpty() || str == null) {
            Log.d(TAG, "helper need init");
            return false;
        } else if (str.isEmpty()) {
            return false;
        } else {
            if (!installThemeAsset(z ? null : this.m_context, str, this.mThemeAssetMap.get(str))) {
                Log.d(TAG, "install theme asset error!, id:" + str);
                return false;
            }
            NvsThemeAsset nvsThemeAsset = this.mThemeAssetMap.get(str);
            if (nvsThemeAsset == null) {
                return false;
            }
            NvsStreamingContext nvsStreamingContext = NvsStreamingContext.getInstance();
            if (nvsStreamingContext != null) {
                nvsStreamingContext.stop();
            }
            this.m_themeAssetID = str;
            this.m_showCaption = z2;
            this.m_selectedMusicType = 0;
            this.m_selectedExtMusicIdx = -1;
            this.m_musicVolumeGain = 1.0f;
            this.m_selectedLocalMusic = null;
            this.m_selectedLocalMusicStart = -1L;
            this.m_selectedLocalMusicEnd = -1L;
            reBuildTimeLine(this.m_timeline, nvsThemeAsset, this.m_inputIamgeInfo, -1L, null, true);
            return true;
        }
    }

    public boolean changeTimelineDuration(boolean z) {
        if (z) {
            reBuildTimeLineExt(this.timeBase * 10, true);
        } else {
            reBuildTimeLineExt(-2L, true);
        }
        return true;
    }

    public long getTimelinePosition(int i) {
        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> thumbnailImages;
        NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc;
        if (i >= 0 && (thumbnailImages = getThumbnailImages()) != null && i < thumbnailImages.size() && (thumbnailSequenceDesc = thumbnailImages.get(i)) != null) {
            return (thumbnailSequenceDesc.inPoint + thumbnailSequenceDesc.outPoint) / 2;
        }
        return 0L;
    }

    public boolean changeCaptionText(String str, int i) {
        if (str == null) {
            return false;
        }
        if (i == 0) {
            this.m_caption1Text = str;
        } else if (i != 1) {
            return false;
        } else {
            this.m_caption2Text = str;
        }
        addCaption(this.m_timeline);
        return true;
    }

    public String getCaptionText(int i) {
        String str;
        if (i == 0) {
            if (this.m_caption1 != null) {
                str = this.m_caption1Text;
            }
            str = "";
        } else {
            if (this.m_caption2 != null) {
                str = this.m_caption2Text;
            }
            str = "";
        }
        return str.replace("\n", "");
    }

    public boolean changeMusic(String str, int i) {
        NvsMusicFileDesc nvsMusicFileDesc;
        NvsStreamingContext nvsStreamingContext = NvsStreamingContext.getInstance();
        if (nvsStreamingContext != null) {
            nvsStreamingContext.stop();
        }
        if (str == null) {
            this.m_musicVolumeGain = 0.0f;
            if (this.m_selectedMusicType == 0) {
                this.m_timeline.setThemeMusicVolumeGain(0.0f, 0.0f);
            } else {
                NvsAudioTrack audioTrackByIndex = this.m_timeline.getAudioTrackByIndex(0);
                if (audioTrackByIndex != null) {
                    float f = this.m_musicVolumeGain;
                    audioTrackByIndex.setVolumeGain(f, f);
                }
            }
            return true;
        }
        this.m_selectedMusicType = i;
        this.m_musicVolumeGain = 1.0f;
        if (i == 1) {
            NvsAVFileInfo aVFileInfo = nvsStreamingContext.getAVFileInfo(str);
            if (aVFileInfo == null) {
                Log.d(TAG, "loacl music format is not suported!");
                return false;
            }
            changeLocalMusic(str, 0L, aVFileInfo.getAudioStreamDuration(0));
            return true;
        } else if (i != 2) {
            return false;
        } else {
            int i2 = 0;
            while (true) {
                if (i2 >= this.mMusicFileList.size()) {
                    nvsMusicFileDesc = null;
                    break;
                }
                nvsMusicFileDesc = this.mMusicFileList.get(i2);
                if (nvsMusicFileDesc.id.equals(str)) {
                    this.m_selectedExtMusicIdx = i2;
                    break;
                }
                i2++;
            }
            boolean z = nvsMusicFileDesc == null;
            if (z) {
                String readFile = Utils.readFile(str + "/info.json", null);
                if (readFile == null) {
                    Log.d(TAG, "read music info json error!");
                    return false;
                }
                try {
                    nvsMusicFileDesc = NvsParseHelper.GetMusicFileFromJsonObject(new JSONObject(readFile), str);
                    nvsMusicFileDesc.isDownloadFile = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (nvsMusicFileDesc == null) {
                Log.d(TAG, "currentSelected music item is null!");
                return false;
            }
            if (nvsMusicFileDesc.pointsDesc.size() <= 0) {
                String readFile2 = Utils.readFile(nvsMusicFileDesc.jsonFile, z ? null : this.m_context.getAssets());
                if (readFile2 != null) {
                    nvsMusicFileDesc.musicLen = NvsParseHelper.readMusicPoint(readFile2, nvsMusicFileDesc.pointsDesc, nvsMusicFileDesc.transDesc);
                }
                String readFile3 = Utils.readFile(nvsMusicFileDesc.jsonFile10s, z ? null : this.m_context.getAssets());
                if (readFile3 != null) {
                    nvsMusicFileDesc.musicLen10s = NvsParseHelper.readMusicPoint(readFile3, nvsMusicFileDesc.pointsDesc10s, null);
                }
            }
            if (z) {
                this.mMusicFileList.add(nvsMusicFileDesc);
                this.m_selectedExtMusicIdx = this.mMusicFileList.size() - 1;
            }
            this.m_selectedLocalMusic = null;
            this.m_selectedLocalMusicStart = -1L;
            this.m_selectedLocalMusicEnd = -1L;
            reBuildTimeLineExt(-1L, true);
            return true;
        }
    }

    public boolean moveClip(int i, int i2) {
        ArrayList<NvsImageFileDesc> arrayList;
        if (i >= this.m_inputIamgeInfo.size() || i2 >= this.m_inputIamgeInfo.size() || i < 0 || i2 < 0 || this.m_timeline == null || (arrayList = this.m_inputIamgeInfo) == null) {
            return false;
        }
        NvsImageFileDesc copy = arrayList.get(i).copy();
        this.m_inputIamgeInfo.remove(i);
        this.m_inputIamgeInfo.add(i2, copy);
        randomFx();
        reBuildTimeLineExt(-1L, true);
        return true;
    }

    public boolean resetClip(ArrayList<NvsImageFileDesc> arrayList) {
        if (this.m_inputIamgeInfo == null || arrayList == null || arrayList.size() == 0) {
            Log.d(TAG, "invalid clip!");
            return false;
        }
        this.m_inputIamgeInfo.clear();
        return insertClip(arrayList);
    }

    public boolean insertClip(ArrayList<NvsImageFileDesc> arrayList) {
        boolean z;
        if (this.m_inputIamgeInfo == null || arrayList == null || arrayList.size() == 0) {
            Log.d(TAG, "invalid clip!");
            return false;
        }
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            NvsImageFileDesc nvsImageFileDesc = arrayList.get(i);
            if (nvsImageFileDesc.alternative) {
                int i2 = 0;
                while (true) {
                    if (i2 >= this.m_inputIamgeInfo.size()) {
                        z = false;
                        break;
                    }
                    NvsImageFileDesc nvsImageFileDesc2 = this.m_inputIamgeInfo.get(i2);
                    if (nvsImageFileDesc2.filePath.contains(nvsImageFileDesc.filePath)) {
                        Log.d(TAG, "exist file!");
                        nvsImageFileDesc2.show = true;
                        z = true;
                        break;
                    }
                    i2++;
                }
                if (!z) {
                    NvsImageFileDesc copy = nvsImageFileDesc.copy();
                    copy.show = true;
                    arrayList2.add(copy);
                }
            } else {
                NvsImageFileDesc copy2 = nvsImageFileDesc.copy();
                copy2.show = true;
                arrayList2.add(copy2);
            }
        }
        this.m_inputIamgeInfo.addAll(arrayList2);
        randomFx();
        reBuildTimeLineExt(-1L, true);
        return true;
    }

    public boolean deleteClip(int i) {
        ArrayList<NvsImageFileDesc> arrayList = this.m_inputIamgeInfo;
        if (arrayList == null) {
            Log.d(TAG, "invalid file!");
            return false;
        } else if (arrayList.size() <= getMinImgCount()) {
            Log.d(TAG, "less than min clip count!");
            return false;
        } else if (this.m_inputIamgeInfo.size() <= i) {
            Log.d(TAG, "invalid clip index!");
            return false;
        } else {
            this.m_inputIamgeInfo.remove(i);
            randomFx();
            reBuildTimeLineExt(-1L, true);
            return true;
        }
    }

    public String getCurrentThemeName() {
        return this.m_themeAssetID;
    }

    public boolean changeMusicVolumeGain(float f) {
        int i = this.m_selectedMusicType;
        if (i == 0) {
            this.m_timeline.setThemeMusicVolumeGain(f, f);
        } else if (i != 1 && i != 2) {
            return false;
        } else {
            NvsAudioTrack audioTrackByIndex = this.m_timeline.getAudioTrackByIndex(0);
            if (audioTrackByIndex != null) {
                audioTrackByIndex.setVolumeGain(f, f);
            }
        }
        this.m_musicVolumeGain = f;
        return true;
    }

    public void setThemeEnabled(boolean z) {
        if (this.m_timeline == null || this.mThemeAssetMap == null || this.m_themeAssetID == null) {
            return;
        }
        if (z) {
            reBuildTimeLineExt(-1L, true);
        } else {
            reBuildTimeLineExt(-1L, false);
        }
    }

    public ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> getThumbnailImages() {
        int i;
        NvsTimeline nvsTimeline = this.m_timeline;
        String str = null;
        if (nvsTimeline == null) {
            return null;
        }
        int i2 = 0;
        NvsVideoTrack videoTrackByIndex = nvsTimeline.getVideoTrackByIndex(0);
        if (videoTrackByIndex == null) {
            return null;
        }
        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList = new ArrayList<>();
        while (i2 < videoTrackByIndex.getClipCount()) {
            NvsVideoClip clipByIndex = videoTrackByIndex.getClipByIndex(i2);
            if (clipByIndex == null) {
                i = i2;
            } else {
                NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
                thumbnailSequenceDesc.mediaFilePath = clipByIndex.getFilePath();
                thumbnailSequenceDesc.trimIn = clipByIndex.getTrimIn();
                thumbnailSequenceDesc.trimOut = clipByIndex.getTrimOut();
                thumbnailSequenceDesc.inPoint = clipByIndex.getInPoint();
                thumbnailSequenceDesc.outPoint = clipByIndex.getOutPoint();
                thumbnailSequenceDesc.stillImageHint = true;
                String obj = clipByIndex.getAttachment("hasFxTrans") != null ? clipByIndex.getAttachment("hasFxTrans").toString() : str;
                if (obj != null && obj.equalsIgnoreCase("true")) {
                    String obj2 = clipByIndex.getAttachment("fxFilePath").toString();
                    long longValue = Integer.valueOf(clipByIndex.getAttachment("fxInpoint").toString()).longValue() * 1000;
                    thumbnailSequenceDesc.trimOut = thumbnailSequenceDesc.trimIn + longValue;
                    thumbnailSequenceDesc.outPoint = thumbnailSequenceDesc.inPoint + longValue;
                    arrayList.add(thumbnailSequenceDesc);
                    NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc2 = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
                    thumbnailSequenceDesc2.mediaFilePath = obj2;
                    thumbnailSequenceDesc2.inPoint = thumbnailSequenceDesc.outPoint;
                    long outPoint = clipByIndex.getOutPoint();
                    thumbnailSequenceDesc2.outPoint = outPoint;
                    i = i2;
                    thumbnailSequenceDesc2.trimIn = 0L;
                    thumbnailSequenceDesc2.trimOut = outPoint - thumbnailSequenceDesc2.inPoint;
                    thumbnailSequenceDesc2.stillImageHint = true;
                    if (clipByIndex.getAttachment("fxFileV3Path") != null) {
                        String obj3 = clipByIndex.getAttachment("fxFileV3Path").toString();
                        long longValue2 = Integer.valueOf(clipByIndex.getAttachment("fxV3Inpoint").toString()).longValue() * 1000;
                        thumbnailSequenceDesc2.trimOut = thumbnailSequenceDesc2.trimIn + longValue2;
                        thumbnailSequenceDesc2.outPoint = thumbnailSequenceDesc2.inPoint + longValue2;
                        arrayList.add(thumbnailSequenceDesc2);
                        NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc3 = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
                        thumbnailSequenceDesc3.mediaFilePath = obj3;
                        thumbnailSequenceDesc3.inPoint = thumbnailSequenceDesc2.outPoint;
                        long outPoint2 = clipByIndex.getOutPoint();
                        thumbnailSequenceDesc3.outPoint = outPoint2;
                        thumbnailSequenceDesc3.trimIn = 0L;
                        thumbnailSequenceDesc3.trimOut = outPoint2 - thumbnailSequenceDesc3.inPoint;
                        thumbnailSequenceDesc3.stillImageHint = true;
                        arrayList.add(thumbnailSequenceDesc3);
                    } else {
                        arrayList.add(thumbnailSequenceDesc2);
                    }
                } else {
                    i = i2;
                    arrayList.add(thumbnailSequenceDesc);
                }
            }
            i2 = i + 1;
            str = null;
        }
        return arrayList;
    }

    private void changeLocalMusic(String str, long j, long j2) {
        this.m_selectedExtMusicIdx = -1;
        this.m_selectedLocalMusic = str;
        this.m_selectedLocalMusicStart = j;
        this.m_selectedLocalMusicEnd = j2;
        reBuildTimeLineExt(-1L, true);
    }

    private void setImageMotion(NvsVideoClip nvsVideoClip, NvsImageFileDesc nvsImageFileDesc, int i) {
        RectF rectF;
        RectF rectF2;
        RectF rectF3;
        if (nvsVideoClip == null) {
            return;
        }
        if (nvsImageFileDesc.imgRatio == 0.0f) {
            NvsAVFileInfo aVFileInfo = this.m_streamingContext.getAVFileInfo(nvsVideoClip.getFilePath());
            nvsImageFileDesc.imgRatio = Utils.getImgRatio(aVFileInfo);
            nvsImageFileDesc.isLargeImg = isLargeImg(aVFileInfo.getVideoStreamDimension(0));
        }
        if (i > 1) {
            Utils.clipImage(nvsVideoClip, nvsImageFileDesc.imgRatio);
            nvsVideoClip.setImageMotionAnimationEnabled(false);
        } else {
            RectF rectF4 = null;
            if (nvsImageFileDesc.hasFace && (rectF3 = nvsImageFileDesc.faceRect) != null) {
                rectF4 = rectF3;
            }
            if (nvsImageFileDesc.isCover && (rectF = this.coverStartROI) != null && (rectF2 = this.coverEndROI) != null) {
                nvsVideoClip.setImageMotionROI(rectF, rectF2);
                nvsVideoClip.setImageMotionMode(2);
            } else {
                Utils.setImageMotion(nvsVideoClip, nvsImageFileDesc.imgRatio, nvsImageFileDesc.isLargeImg, rectF4);
            }
            if (nvsVideoClip.getOutPoint() - nvsVideoClip.getInPoint() < this.timeBase) {
                nvsVideoClip.setImageMotionAnimationEnabled(false);
            }
        }
        nvsVideoClip.setSourceBackgroundMode(1);
    }

    private boolean isLargeImg(NvsSize nvsSize) {
        NvsSize nvsSize2 = new NvsSize(nvsSize.width, nvsSize.height);
        if (nvsSize2.width >= nvsSize2.height) {
            nvsSize2.width = nvsSize.height;
            nvsSize2.height = nvsSize.width;
        }
        NvsVideoResolution videoRes = this.m_timeline.getVideoRes();
        double min = Math.min(Math.max(videoRes.imageWidth / nvsSize2.width, videoRes.imageHeight / nvsSize2.height), 1.0d);
        int i = nvsSize2.width;
        int i2 = nvsSize2.height;
        if (min < 1.0d) {
            i = (int) ((i * min) + 0.5d);
            i2 = (int) ((i2 * min) + 0.5d);
        }
        return i > 8192 || i2 > 8192;
    }

    private int getClipCountInDuation(ArrayList<NvsMusicPointDesc> arrayList, long j) {
        int size = arrayList.size();
        if (size < 1) {
            return getMinImgCount();
        }
        if (j > arrayList.get(size - 1).cutPoint * 1000) {
            return arrayList.size() + 1;
        }
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            if (arrayList.get(size2).cutPoint * 1000 <= (this.timeBase + j) - 1) {
                return size2 + 1;
            }
        }
        return size;
    }

    private void cleanUpTimeLine(NvsTimeline nvsTimeline) {
        nvsTimeline.removeCurrentTheme();
        NvsTimelineVideoFx firstTimelineVideoFx = nvsTimeline.getFirstTimelineVideoFx();
        while (firstTimelineVideoFx != null) {
            firstTimelineVideoFx = nvsTimeline.removeTimelineVideoFx(firstTimelineVideoFx);
        }
        NvsTimelineAnimatedSticker firstAnimatedSticker = nvsTimeline.getFirstAnimatedSticker();
        while (firstAnimatedSticker != null) {
            firstAnimatedSticker = nvsTimeline.removeAnimatedSticker(firstAnimatedSticker);
        }
        this.m_cafSticker = null;
        this.m_caption1 = null;
        this.m_caption2 = null;
        int videoTrackCount = nvsTimeline.videoTrackCount();
        for (int i = 0; i < videoTrackCount; i++) {
            nvsTimeline.removeVideoTrack(i);
        }
        int audioTrackCount = nvsTimeline.audioTrackCount();
        for (int i2 = 0; i2 < audioTrackCount; i2++) {
            nvsTimeline.removeAudioTrack(i2);
        }
        nvsTimeline.setThemeMusicVolumeGain(1.0f, 1.0f);
    }

    private void reBuildTimeLineExt(long j, boolean z) {
        NvsMusicFileDesc nvsMusicFileDesc;
        NvsMusicFileDesc nvsMusicFileDesc2;
        NvsThemeAsset nvsThemeAsset = this.mThemeAssetMap.get(this.m_themeAssetID);
        if (nvsThemeAsset == null) {
            return;
        }
        int i = this.m_selectedExtMusicIdx;
        if (i >= 0 && i < this.mMusicFileList.size()) {
            nvsMusicFileDesc2 = this.mMusicFileList.get(this.m_selectedExtMusicIdx);
        } else if (this.m_selectedLocalMusic != null) {
            nvsMusicFileDesc2 = new NvsMusicFileDesc();
            String str = this.m_selectedLocalMusic;
            nvsMusicFileDesc2.musicFile = str;
            nvsMusicFileDesc2.musicFile10s = str;
            String readFile = Utils.readFile(this.m_defaultRhythmPath, this.m_context.getAssets());
            if (readFile != null) {
                nvsMusicFileDesc2.musicLen = NvsParseHelper.readMusicPoint(readFile, nvsMusicFileDesc2.pointsDesc, nvsMusicFileDesc2.transDesc);
            }
            String readFile2 = Utils.readFile(this.m_defaultRhythm10sPath, this.m_context.getAssets());
            if (readFile2 != null) {
                nvsMusicFileDesc2.musicLen10s = NvsParseHelper.readMusicPoint(readFile2, nvsMusicFileDesc2.pointsDesc10s, null);
            }
            nvsMusicFileDesc2.jsonFile = null;
            nvsMusicFileDesc2.jsonFile10s = null;
        } else {
            nvsMusicFileDesc = null;
            reBuildTimeLine(this.m_timeline, nvsThemeAsset, this.m_inputIamgeInfo, j, nvsMusicFileDesc, z);
        }
        nvsMusicFileDesc = nvsMusicFileDesc2;
        reBuildTimeLine(this.m_timeline, nvsThemeAsset, this.m_inputIamgeInfo, j, nvsMusicFileDesc, z);
    }

    private void reBuildTimeLine(NvsTimeline nvsTimeline, NvsThemeAsset nvsThemeAsset, ArrayList<NvsImageFileDesc> arrayList, long j, NvsMusicFileDesc nvsMusicFileDesc, boolean z) {
        NvsTransDesc nvsTransDesc;
        long j2;
        boolean z2;
        boolean z3;
        int i;
        NvsVideoTrack nvsVideoTrack;
        ArrayList<NvsMusicPointDesc> arrayList2;
        ArrayList<NvsMusicPointDesc> arrayList3;
        String str;
        ArrayList<NvsMusicPointDesc> arrayList4;
        String str2;
        rand = new Random(this.seed);
        NvsStreamingContext nvsStreamingContext = NvsStreamingContext.getInstance();
        if (nvsStreamingContext == null || nvsTimeline == null || nvsThemeAsset == null || arrayList == null) {
            return;
        }
        nvsStreamingContext.clearCachedResources(false);
        arrayList.size();
        NvsTransDesc nvsTransDesc2 = nvsThemeAsset.m_transDesc;
        long j3 = nvsThemeAsset.m_musicLen;
        ArrayList<NvsMusicPointDesc> arrayList5 = nvsThemeAsset.m_musicPoints;
        if (nvsMusicFileDesc != null) {
            ArrayList<NvsMusicPointDesc> arrayList6 = nvsMusicFileDesc.pointsDesc;
            if (arrayList6 != null && arrayList6.size() > 0 && nvsMusicFileDesc.musicFile != null) {
                arrayList5 = nvsMusicFileDesc.pointsDesc;
            }
            NvsTransDesc nvsTransDesc3 = nvsMusicFileDesc.transDesc;
            if (nvsTransDesc3 != null) {
                nvsTransDesc2 = nvsTransDesc3;
            }
            boolean z4 = this.m_selectedMusicType == 1;
            nvsTransDesc = nvsTransDesc2;
            j2 = nvsMusicFileDesc.musicLen;
            z3 = true;
            z2 = z4;
        } else {
            nvsTransDesc = nvsTransDesc2;
            j2 = j3;
            z2 = false;
            z3 = false;
        }
        this.m_is10sMode = false;
        int i2 = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i2 > 0) {
            this.m_is10sMode = true;
            if (z3) {
                arrayList5 = nvsMusicFileDesc.pointsDesc10s;
            } else {
                arrayList5 = nvsThemeAsset.m_musicPoints10s;
            }
        }
        ArrayList<NvsImageFileDesc> rebuildFileList = getRebuildFileList(arrayList, arrayList5, (i2 <= 0 || arrayList5.size() <= 0) ? (int) j : getClipCountInDuation(arrayList5, j));
        if (rebuildFileList.size() < 1) {
            Log.e(TAG, "not any show file!");
            return;
        }
        if (rebuildFileList.size() <= 5) {
            this.m_is10sMode = true;
            if (z3) {
                arrayList5 = nvsMusicFileDesc.pointsDesc10s;
            } else {
                arrayList5 = nvsThemeAsset.m_musicPoints10s;
            }
        }
        ArrayList<NvsMusicPointDesc> arrayList7 = arrayList5;
        cleanUpTimeLine(nvsTimeline);
        NvsVideoTrack appendVideoTrack = nvsTimeline.appendVideoTrack();
        if (appendVideoTrack == null) {
            Log.e(TAG, "appendVideoTrack failed!");
            return;
        }
        ArrayList<NvsMusicPointDesc> arrayList8 = arrayList7;
        boolean z5 = z2;
        Map<Integer, NvsMusicPointDesc> buildVideoTrack = buildVideoTrack(rebuildFileList, appendVideoTrack, arrayList7, j2, j, this.m_is10sMode, nvsThemeAsset.version);
        String str3 = null;
        if (!z) {
            for (int i3 = 0; i3 < appendVideoTrack.getClipCount() - 1; i3++) {
                appendVideoTrack.setBuiltinTransition(i3, null);
            }
            return;
        }
        NvsVideoTrack nvsVideoTrack2 = appendVideoTrack;
        nvsTimeline.applyTheme((this.m_is10sMode ? nvsThemeAsset.m_theme10sId : nvsThemeAsset.m_themeId).toString());
        if (nvsThemeAsset.version > 1) {
            this.m_streamingContext.setThemeEndingEnabled(false);
        } else {
            this.m_streamingContext.setThemeEndingEnabled(true);
        }
        StringBuilder sb = this.m_is10sMode ? nvsThemeAsset.m_cafSticker10sId : nvsThemeAsset.m_cafStickerId;
        this.m_cafStickerId = sb;
        if (sb != null) {
            if (!this.m_showCaption || (this.m_caption1Text.isEmpty() && this.m_caption2Text.isEmpty())) {
                this.m_cafSticker = nvsTimeline.addAnimatedSticker(0L, (long) (this.timeBase * 3.48d), this.m_cafStickerId.toString());
            }
        }
        this.m_fxTransClipCount = 0;
        int i4 = 0;
        while (i4 < nvsVideoTrack2.getClipCount() - 1) {
            nvsVideoTrack2.setBuiltinTransition(i4, str3);
            int i5 = 1000;
            if (buildVideoTrack.containsKey(Integer.valueOf(this.m_fxTransClipCount + i4))) {
                NvsMusicPointDesc nvsMusicPointDesc = buildVideoTrack.get(Integer.valueOf(this.m_fxTransClipCount + i4));
                if (!nvsMusicPointDesc.transNames.isEmpty()) {
                    setTrans(nvsVideoTrack2, i4, nvsThemeAsset.m_transIDList, nvsMusicPointDesc);
                } else if (!nvsMusicPointDesc.fxNames.isEmpty()) {
                    String fxXml = getFxXml(i4, nvsVideoTrack2, nvsThemeAsset, nvsMusicPointDesc);
                    String str4 = "";
                    if (fxXml.contains("fxV3")) {
                        if (this.m_fxTransClipCount + i4 + 2 < arrayList8.size()) {
                            arrayList4 = arrayList8;
                            NvsMusicPointDesc nvsMusicPointDesc2 = arrayList4.get(this.m_fxTransClipCount + i4 + 2);
                            int i6 = nvsMusicPointDesc2.transLen;
                            if (!nvsMusicPointDesc2.transNames.isEmpty()) {
                                str4 = nvsMusicPointDesc2.transNames.get(0);
                            }
                            str2 = str4;
                            i5 = i6;
                        } else {
                            arrayList4 = arrayList8;
                            str2 = str4;
                        }
                        i = i4;
                        arrayList8 = arrayList4;
                        nvsVideoTrack = nvsVideoTrack2;
                        applyFxTransV3(nvsVideoTrack2, i4, fxXml, str2, i5, nvsThemeAsset);
                    } else {
                        i = i4;
                        nvsVideoTrack = nvsVideoTrack2;
                        if (i + this.m_fxTransClipCount + 1 < arrayList8.size()) {
                            arrayList3 = arrayList8;
                            NvsMusicPointDesc nvsMusicPointDesc3 = arrayList3.get(i + this.m_fxTransClipCount + 1);
                            int i7 = nvsMusicPointDesc3.transLen;
                            if (!nvsMusicPointDesc3.transNames.isEmpty()) {
                                str4 = nvsMusicPointDesc3.transNames.get(0);
                            }
                            str = str4;
                            i5 = i7;
                        } else {
                            arrayList3 = arrayList8;
                            str = str4;
                        }
                        arrayList8 = arrayList3;
                        applyFxTransV2(nvsVideoTrack, i, fxXml, str, i5, nvsThemeAsset);
                    }
                    arrayList2 = arrayList8;
                }
                i = i4;
                nvsVideoTrack = nvsVideoTrack2;
                arrayList2 = arrayList8;
            } else {
                i = i4;
                nvsVideoTrack = nvsVideoTrack2;
                arrayList2 = arrayList8;
                int i8 = i < arrayList8.size() ? arrayList2.get(i).transLen : 1000;
                if (nvsThemeAsset.m_hasFxTrans) {
                    setFxTrans(nvsVideoTrack, i8, nvsTransDesc, nvsThemeAsset, i);
                } else {
                    setTrans(nvsVideoTrack, i8, nvsTransDesc, nvsThemeAsset.m_transIDList, i);
                }
            }
            i4 = i + 1;
            arrayList8 = arrayList2;
            nvsVideoTrack2 = nvsVideoTrack;
            str3 = null;
        }
        if (z3) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("assets:/");
            sb2.append(this.m_is10sMode ? nvsMusicFileDesc.musicFile10s : nvsMusicFileDesc.musicFile);
            String sb3 = sb2.toString();
            if (z5 || nvsMusicFileDesc.isDownloadFile) {
                sb3 = this.m_is10sMode ? nvsMusicFileDesc.musicFile10s : nvsMusicFileDesc.musicFile;
            }
            addMusicFile(nvsTimeline, sb3, z5);
        }
        addCaption(nvsTimeline);
        addEndingFx(nvsTimeline, nvsThemeAsset, this.m_is10sMode);
        if (this.m_selectedMusicType == 0) {
            NvsTimeline nvsTimeline2 = this.m_timeline;
            float f = this.m_musicVolumeGain;
            nvsTimeline2.setThemeMusicVolumeGain(f, f);
        } else {
            NvsAudioTrack audioTrackByIndex = this.m_timeline.getAudioTrackByIndex(0);
            if (audioTrackByIndex != null) {
                float f2 = this.m_musicVolumeGain;
                audioTrackByIndex.setVolumeGain(f2, f2);
            }
        }
        long j4 = this.timeBase;
        long j5 = 5 * j4;
        if (this.m_is10sMode) {
            j5 = (long) (j4 * 1.5d);
        }
        nvsTimeline.setAudioFadeOutDuration(j5);
    }

    private String getFxXml(int i, NvsVideoTrack nvsVideoTrack, NvsThemeAsset nvsThemeAsset, NvsMusicPointDesc nvsMusicPointDesc) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (nvsMusicPointDesc.fxNames.get(0).equalsIgnoreCase("")) {
            return "";
        }
        String transType = getTransType(i, nvsVideoTrack);
        if (transType.equalsIgnoreCase("full")) {
            float f = m_timelineRatio;
            if (f >= 0.5625d) {
                arrayList = nvsThemeAsset.m_fullFx9v16List;
            } else if (f >= 0.5f) {
                arrayList = nvsThemeAsset.m_fullFx9v18List;
            } else if (f >= 0.4804270462633452d) {
                arrayList = nvsThemeAsset.m_fullFx9vx73List;
            } else {
                arrayList = nvsThemeAsset.m_fullFx9v19List;
            }
        } else if (transType.equalsIgnoreCase("half")) {
            float f2 = m_timelineRatio;
            if (f2 >= 0.5625d) {
                arrayList = nvsThemeAsset.m_halfFx9v16List;
            } else if (f2 >= 0.5f) {
                arrayList = nvsThemeAsset.m_halfFx9v18List;
            } else if (f2 >= 0.4804270462633452d) {
                arrayList = nvsThemeAsset.m_halfFx9vx73List;
            } else {
                arrayList = nvsThemeAsset.m_halfFx9v19List;
            }
        } else if (transType.equalsIgnoreCase("hf")) {
            float f3 = m_timelineRatio;
            if (f3 >= 0.5625d) {
                arrayList = nvsThemeAsset.m_hfFx9v16List;
            } else if (f3 >= 0.5f) {
                arrayList = nvsThemeAsset.m_hfFx9v18List;
            } else if (f3 >= 0.4804270462633452d) {
                arrayList = nvsThemeAsset.m_hfFx9vx73List;
            } else {
                arrayList = nvsThemeAsset.m_hfFx9v19List;
            }
        } else if (transType.equalsIgnoreCase("fh")) {
            float f4 = m_timelineRatio;
            if (f4 >= 0.5625d) {
                arrayList = nvsThemeAsset.m_fhFx9v16List;
            } else if (f4 >= 0.5f) {
                arrayList = nvsThemeAsset.m_fhFx9v18List;
            } else if (f4 >= 0.4804270462633452d) {
                arrayList = nvsThemeAsset.m_fhFx9vx73List;
            } else {
                arrayList = nvsThemeAsset.m_fhFx9v19List;
            }
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (arrayList.get(i2).contains(nvsMusicPointDesc.fxNames.get(0))) {
                return arrayList.get(i2);
            }
        }
        return "";
    }

    private ArrayList<NvsImageFileDesc> getRebuildFileList(ArrayList<NvsImageFileDesc> arrayList, ArrayList<NvsMusicPointDesc> arrayList2, int i) {
        if (i > arrayList2.size()) {
            i = arrayList2.size() + 1;
        }
        if (i > getMaxImgCount()) {
            i = getMaxImgCount();
        }
        ArrayList<NvsImageFileDesc> arrayList3 = new ArrayList<>();
        if (i == -1) {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                NvsImageFileDesc nvsImageFileDesc = arrayList.get(i2);
                if (nvsImageFileDesc.show) {
                    arrayList3.add(nvsImageFileDesc);
                }
            }
        } else if (i == -2) {
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                NvsImageFileDesc nvsImageFileDesc2 = arrayList.get(i3);
                nvsImageFileDesc2.show = true;
                arrayList3.add(nvsImageFileDesc2);
            }
        } else {
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                NvsImageFileDesc nvsImageFileDesc3 = arrayList.get(i4);
                if (i4 >= i) {
                    nvsImageFileDesc3.show = false;
                } else {
                    nvsImageFileDesc3.show = true;
                    arrayList3.add(nvsImageFileDesc3);
                }
            }
        }
        if (arrayList3.size() > 20) {
            ArrayList<NvsImageFileDesc> arrayList4 = new ArrayList<>();
            for (int i5 = 0; i5 < 20; i5++) {
                arrayList4.add(arrayList3.get(i5));
            }
            return arrayList4;
        }
        return arrayList3;
    }

    private void addCaption(NvsTimeline nvsTimeline) {
        StringBuilder sb;
        nvsTimeline.removeAnimatedSticker(this.m_caption1);
        nvsTimeline.removeAnimatedSticker(this.m_caption2);
        if ((!this.m_caption1Text.isEmpty() || !this.m_caption2Text.isEmpty()) && this.m_showCaption) {
            nvsTimeline.removeAnimatedSticker(this.m_cafSticker);
            this.m_cafSticker = null;
        } else if (this.m_cafSticker == null && (sb = this.m_cafStickerId) != null) {
            this.m_cafSticker = nvsTimeline.addAnimatedSticker(0L, (long) (this.timeBase * 3.48d), sb.toString());
        }
        String str = this.m_captionBitmapPath1;
        if (str != null && !str.isEmpty()) {
            File file = new File(this.m_captionBitmapPath1);
            if (file.exists()) {
                file.delete();
            }
        }
        String str2 = this.m_captionBitmapPath2;
        if (str2 != null && !str2.isEmpty()) {
            File file2 = new File(this.m_captionBitmapPath2);
            if (file2.exists()) {
                file2.delete();
            }
        }
        String str3 = this.m_stickerId1;
        if (str3 == null || this.m_stickerId2 == null) {
            Log.e("meicam", "m_stickerId is null");
        } else if (!this.m_showCaption) {
        } else {
            String str4 = str3.toString();
            String str5 = this.m_stickerId2.toString();
            if (!this.m_caption1Text.isEmpty()) {
                String createCaptionBitmap = createCaptionBitmap(manageStringLength(this.m_caption1Text, this.DEFAULT_FONT1_SIZE), this.DEFAULT_FONT1_SIZE);
                this.m_captionBitmapPath1 = createCaptionBitmap;
                if (createCaptionBitmap != null && !createCaptionBitmap.isEmpty()) {
                    if (new File(this.m_captionBitmapPath1).exists()) {
                        this.m_caption1 = nvsTimeline.addCustomAnimatedSticker(0L, (long) (this.timeBase * 3.48d), str4, this.m_captionBitmapPath1);
                    } else {
                        Log.e("meicam", "bitmap_file1 not exist");
                    }
                } else {
                    Log.e("meicam", "caption1_path is null");
                }
            }
            if (this.m_caption2Text.isEmpty()) {
                return;
            }
            String createCaptionBitmap2 = createCaptionBitmap(manageString2Length(this.m_caption2Text, this.DEFAULT_FONT2_SIZE), this.DEFAULT_FONT2_SIZE);
            this.m_captionBitmapPath2 = createCaptionBitmap2;
            if (createCaptionBitmap2 != null && !createCaptionBitmap2.isEmpty()) {
                if (new File(this.m_captionBitmapPath2).exists()) {
                    this.m_caption2 = nvsTimeline.addCustomAnimatedSticker(0L, (long) (this.timeBase * 3.48d), str5, this.m_captionBitmapPath2);
                    return;
                } else {
                    Log.e("meicam", "bitmap_file2 not exist");
                    return;
                }
            }
            Log.e("meicam", "caption2_path is null");
        }
    }

    private String createCaptionBitmap(String str, int i) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(i);
        textPaint.setColor(-1);
        if (Build.VERSION.SDK_INT >= 21) {
            textPaint.setLetterSpacing(0.03f);
        }
        StaticLayout staticLayout = new StaticLayout(str, textPaint, Utils.getScreenWidth(this.m_context), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        Bitmap createBitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);
        staticLayout.draw(new Canvas(createBitmap));
        return saveBitmapToSD(createBitmap);
    }

    private String saveBitmapToSD(Bitmap bitmap) {
        if (this.m_context == null) {
            Log.e("meicam", "m_context is null,can not make path!");
            return null;
        }
        File file = new File(this.m_context.getCacheDir(), "caption_bitmap");
        if (!file.exists() && !file.mkdirs()) {
            Log.e("meicam", "Failed to make caption bitmap directory");
            return null;
        }
        File file2 = new File(file, String.valueOf(System.nanoTime()) + ".png");
        if (file2.exists()) {
            file2.delete();
        }
        String absolutePath = file2.getAbsolutePath();
        if (bitmap == null || absolutePath.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("bt == null ");
            sb.append(bitmap == null);
            sb.append(" target_path.isEmpty(): ");
            sb.append(absolutePath.isEmpty());
            Log.e("meicam", sb.toString());
            return null;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(absolutePath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return absolutePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private String manageStringLength(String str, int i) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String trim = str.trim();
        Paint paint = new Paint();
        paint.setTextSize(i);
        float measureText = paint.measureText(FONT_MAX_LENGTH);
        float measureText2 = paint.measureText("😀") - paint.measureText("\ud83d");
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 >= trim.length()) {
                break;
            }
            i3 = i2 + 1;
            float measureText3 = paint.measureText(trim.substring(0, i3));
            if (measureText3 < measureText) {
                i2 = i3;
            } else if (i2 < trim.length() - 1) {
                int i4 = i2 + 2;
                if (paint.measureText(trim.substring(0, i4)) - measureText3 <= measureText2) {
                    i3 = i4;
                }
            } else {
                i3 = trim.length();
            }
        }
        if (i3 != trim.length()) {
            return trim.substring(0, i3) + "...";
        }
        return trim.substring(0, i3);
    }

    private String manageString2Length(String str, int i) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String trim = str.trim();
        Paint paint = new Paint();
        paint.setTextSize(i);
        float screenWidth = Utils.getScreenWidth(this.m_context) - 100.0f;
        float measureText = paint.measureText("😀") - paint.measureText("\ud83d");
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 >= trim.length()) {
                i2 = i3;
                break;
            }
            i3 = i2 + 1;
            float measureText2 = paint.measureText(trim.substring(0, i3));
            if (measureText2 < screenWidth) {
                i2 = i3;
            } else if (i2 <= trim.length() - 1 && measureText2 - paint.measureText(trim.substring(0, i2)) <= measureText) {
                i2--;
            }
        }
        return trim.substring(0, i2);
    }

    private Map<Integer, NvsMusicPointDesc> buildVideoTrack(ArrayList<NvsImageFileDesc> arrayList, NvsVideoTrack nvsVideoTrack, ArrayList<NvsMusicPointDesc> arrayList2, long j, long j2, boolean z, int i) {
        int i2;
        HashMap hashMap = new HashMap();
        int i3 = 0;
        int i4 = 0;
        while (i3 < arrayList.size()) {
            int i5 = (int) this.clipMaxLen;
            if (arrayList2.size() > 0) {
                if (i3 > 0) {
                    if (i4 >= arrayList2.size()) {
                        break;
                    }
                    NvsMusicPointDesc nvsMusicPointDesc = arrayList2.get(i4);
                    i4++;
                    if (i4 < arrayList2.size()) {
                        i2 = (arrayList2.get(i4).cutPoint - nvsMusicPointDesc.cutPoint) * 1000;
                    } else {
                        i2 = (int) ((z ? this.timeBase * 10 : j) - (nvsMusicPointDesc.cutPoint * 1000));
                    }
                    if (i2 > 0) {
                        if (!nvsMusicPointDesc.transNames.isEmpty() || !nvsMusicPointDesc.fxNames.isEmpty()) {
                            hashMap.put(Integer.valueOf(nvsVideoTrack.getClipCount() > 0 ? nvsVideoTrack.getClipCount() - 1 : 0), nvsMusicPointDesc);
                        }
                        i5 = i2;
                    } else {
                        i3++;
                    }
                } else {
                    i5 = arrayList2.get(0).cutPoint * 1000;
                }
            }
            NvsImageFileDesc nvsImageFileDesc = arrayList.get(i3);
            NvsVideoClip appendClip = nvsVideoTrack.appendClip(nvsImageFileDesc.filePath, 0L, i5);
            if (appendClip != null) {
                appendClip.setAttachment("indexOfShowList", String.valueOf(i3));
                setImageMotion(appendClip, nvsImageFileDesc, i);
            } else {
                arrayList.remove(i3);
                i3--;
            }
            i3++;
        }
        return hashMap;
    }

    private void addEndingFx(NvsTimeline nvsTimeline, NvsThemeAsset nvsThemeAsset, boolean z) {
        if (z) {
            nvsTimeline.setTimelineEndingFilter(nvsThemeAsset.m_endingFxId10s, nvsThemeAsset.m_endingFxImgPath, nvsThemeAsset.m_endingFx10sLen);
        } else {
            nvsTimeline.setTimelineEndingFilter(nvsThemeAsset.m_endingFxId, nvsThemeAsset.m_endingFxImgPath, nvsThemeAsset.m_endingFxLen);
        }
    }

    private void setTrans(NvsVideoTrack nvsVideoTrack, int i, ArrayList<StringBuilder> arrayList, NvsMusicPointDesc nvsMusicPointDesc) {
        ArrayList<String> arrayList2 = nvsMusicPointDesc.transNames;
        int i2 = 0;
        NvsVideoTransition nvsVideoTransition = null;
        String str = (arrayList2 == null || arrayList2.isEmpty()) ? null : nvsMusicPointDesc.transNames.get(0);
        if (str == null || str.equalsIgnoreCase("")) {
            nvsVideoTrack.setBuiltinTransition(i, null);
            return;
        }
        while (true) {
            if (i2 >= arrayList.size()) {
                break;
            }
            String sb = arrayList.get(i2).toString();
            if (sb.contains(str)) {
                nvsVideoTransition = nvsVideoTrack.setPackagedTransition(i, sb);
                break;
            }
            i2++;
        }
        if (nvsVideoTransition == null) {
            nvsVideoTransition = nvsVideoTrack.setBuiltinTransition(i, str);
        }
        if (nvsVideoTransition == null) {
            return;
        }
        nvsVideoTransition.setVideoTransitionDurationScaleFactor(nvsMusicPointDesc.transLen / 1000.0f);
    }

    private void setTrans(NvsVideoTrack nvsVideoTrack, int i, NvsTransDesc nvsTransDesc, ArrayList<StringBuilder> arrayList, int i2) {
        String trans;
        String transType = getTransType(i2, nvsVideoTrack);
        if (transType.equalsIgnoreCase("full")) {
            trans = getTrans(nvsTransDesc.m_fulltransList, arrayList);
        } else if (transType.equalsIgnoreCase("half")) {
            trans = getTrans(nvsTransDesc.m_halftransList, arrayList);
        } else {
            trans = getTrans(nvsTransDesc.m_hftransList, arrayList);
        }
        NvsVideoTransition nvsVideoTransition = null;
        if (trans.equalsIgnoreCase("")) {
            nvsVideoTrack.setBuiltinTransition(i2, null);
        } else if (trans.length() >= 30) {
            nvsVideoTransition = nvsVideoTrack.setPackagedTransition(i2, trans);
        } else {
            nvsVideoTransition = nvsVideoTrack.setBuiltinTransition(i2, trans);
        }
        if (nvsVideoTransition != null) {
            nvsVideoTransition.setVideoTransitionDurationScaleFactor(i / 1000.0f);
        }
    }

    private void setFxTrans(NvsVideoTrack nvsVideoTrack, int i, NvsTransDesc nvsTransDesc, NvsThemeAsset nvsThemeAsset, int i2) {
        ArrayList<String> arrayList;
        ArrayList<String> arrayList2;
        ArrayList<String> arrayList3;
        ArrayList<String> arrayList4;
        String transType = getTransType(i2, nvsVideoTrack);
        ArrayList<String> arrayList5 = new ArrayList<>();
        new ArrayList();
        if (transType.equalsIgnoreCase("full")) {
            float f = m_timelineRatio;
            if (f >= 0.5625d) {
                arrayList4 = nvsThemeAsset.m_fullFx9v16List;
            } else if (f >= 0.5f) {
                arrayList4 = nvsThemeAsset.m_fullFx9v18List;
            } else if (f >= 0.4804270462633452d) {
                arrayList4 = nvsThemeAsset.m_fullFx9vx73List;
            } else {
                arrayList4 = nvsThemeAsset.m_fullFx9v19List;
            }
            arrayList5 = getTrans(nvsTransDesc.m_fulltransList, nvsTransDesc.m_fulltransFxList, nvsThemeAsset.m_transIDList, arrayList4);
        } else if (transType.equalsIgnoreCase("half")) {
            float f2 = m_timelineRatio;
            if (f2 >= 0.5625d) {
                arrayList3 = nvsThemeAsset.m_halfFx9v16List;
            } else if (f2 >= 0.5f) {
                arrayList3 = nvsThemeAsset.m_halfFx9v18List;
            } else if (f2 >= 0.4804270462633452d) {
                arrayList3 = nvsThemeAsset.m_halfFx9vx73List;
            } else {
                arrayList3 = nvsThemeAsset.m_halfFx9v19List;
            }
            arrayList5 = getTrans(nvsTransDesc.m_halftransList, nvsTransDesc.m_halftransFxList, nvsThemeAsset.m_transIDList, arrayList3);
        } else if (transType.equalsIgnoreCase("fh")) {
            float f3 = m_timelineRatio;
            if (f3 >= 0.5625d) {
                arrayList2 = nvsThemeAsset.m_fhFx9v16List;
            } else if (f3 >= 0.5f) {
                arrayList2 = nvsThemeAsset.m_fhFx9v18List;
            } else if (f3 >= 0.4804270462633452d) {
                arrayList2 = nvsThemeAsset.m_fhFx9vx73List;
            } else {
                arrayList2 = nvsThemeAsset.m_fhFx9v19List;
            }
            arrayList5 = getTrans(nvsTransDesc.m_hftransList, nvsTransDesc.m_fhtransFxList, nvsThemeAsset.m_transIDList, arrayList2);
        } else if (transType.equalsIgnoreCase("hf")) {
            float f4 = m_timelineRatio;
            if (f4 >= 0.5625d) {
                arrayList = nvsThemeAsset.m_hfFx9v16List;
            } else if (f4 >= 0.5f) {
                arrayList = nvsThemeAsset.m_hfFx9v18List;
            } else if (f4 >= 0.4804270462633452d) {
                arrayList = nvsThemeAsset.m_hfFx9vx73List;
            } else {
                arrayList = nvsThemeAsset.m_hfFx9v19List;
            }
            arrayList5 = getTrans(nvsTransDesc.m_hftransList, nvsTransDesc.m_hftransFxList, nvsThemeAsset.m_transIDList, arrayList);
        }
        if (arrayList5.size() > 1) {
            String str = arrayList5.get(0);
            String str2 = arrayList5.get(1);
            if (str.contains("fxV3")) {
                applyFxTransV3(nvsVideoTrack, i2, str, str2, i, nvsThemeAsset);
                return;
            } else {
                applyFxTransV2(nvsVideoTrack, i2, str, str2, i, nvsThemeAsset);
                return;
            }
        }
        String str3 = arrayList5.get(0);
        NvsVideoTransition nvsVideoTransition = null;
        if (str3.equalsIgnoreCase("")) {
            nvsVideoTrack.setBuiltinTransition(i2, null);
        } else if (str3.length() >= 30) {
            nvsVideoTransition = nvsVideoTrack.setPackagedTransition(i2, str3);
        } else {
            nvsVideoTransition = nvsVideoTrack.setBuiltinTransition(i2, str3);
        }
        if (nvsVideoTransition == null) {
            return;
        }
        nvsVideoTransition.setVideoTransitionDurationScaleFactor(i / 1000.0f);
    }

    private void applyFxTransV2(NvsVideoTrack nvsVideoTrack, int i, String str, String str2, long j, NvsThemeAsset nvsThemeAsset) {
        long applyFxTrans = applyFxTrans(nvsVideoTrack, i, str, str2, j, nvsThemeAsset.isDownload, nvsThemeAsset.m_transOffset * 1000);
        if (nvsThemeAsset.m_blurFxId != null) {
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= nvsThemeAsset.m_blurWidthFxTransList.size()) {
                    break;
                } else if (str.contains(nvsThemeAsset.m_blurWidthFxTransList.get(i2))) {
                    z = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (!z) {
                return;
            }
            NvsTimeline nvsTimeline = this.m_timeline;
            long j2 = this.timeBase;
            nvsTimeline.addPackagedTimelineVideoFx(applyFxTrans - (j2 / 2), j2, nvsThemeAsset.m_blurFxId.toString()).setBooleanVal("No Background", true);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0181 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0182  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void applyFxTransV3(com.meicam.sdk.NvsVideoTrack r37, int r38, java.lang.String r39, java.lang.String r40, long r41, com.meicam.themehelper.NvsThemeHelper.NvsThemeAsset r43) {
        /*
            Method dump skipped, instructions count: 673
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.meicam.themehelper.NvsThemeHelper.applyFxTransV3(com.meicam.sdk.NvsVideoTrack, int, java.lang.String, java.lang.String, long, com.meicam.themehelper.NvsThemeHelper$NvsThemeAsset):void");
    }

    private long applyFxTrans(NvsVideoTrack nvsVideoTrack, int i, String str, String str2, long j, boolean z, int i2) {
        String str3;
        String str4;
        String changeROI;
        NvsVideoTransition builtinTransition;
        NvsVideoTransition transitionBySourceClipIndex;
        String str5 = str;
        if (str5.equalsIgnoreCase("")) {
            return 0L;
        }
        NvsVideoClip clipByIndex = nvsVideoTrack.getClipByIndex(i);
        int i3 = i + 1;
        NvsVideoClip clipByIndex2 = nvsVideoTrack.getClipByIndex(i3);
        String filePath = clipByIndex.getFilePath();
        String filePath2 = clipByIndex2.getFilePath();
        long outPoint = clipByIndex.getOutPoint() - clipByIndex.getInPoint();
        long outPoint2 = clipByIndex2.getOutPoint() - clipByIndex2.getInPoint();
        String obj = clipByIndex.getAttachment("fullscreenMode").toString();
        int intValue = Integer.valueOf(clipByIndex.getAttachment("indexOfShowList").toString()).intValue();
        int intValue2 = Integer.valueOf(clipByIndex2.getAttachment("indexOfShowList").toString()).intValue();
        int i4 = i - 1;
        if (i4 < 0 || (transitionBySourceClipIndex = nvsVideoTrack.getTransitionBySourceClipIndex(i4)) == null) {
            str3 = null;
        } else {
            str3 = transitionBySourceClipIndex.getVideoTransitionType() == 0 ? transitionBySourceClipIndex.getBuiltinVideoTransitionName() : transitionBySourceClipIndex.getVideoTransitionPackageId();
        }
        long j2 = str3 != null ? i2 : 0L;
        nvsVideoTrack.removeClip(i, false);
        nvsVideoTrack.removeClip(i, false);
        String str6 = str3;
        NvsVideoClip insertClip = nvsVideoTrack.insertClip(filePath, 0L, outPoint + outPoint2, i);
        insertClip.setAttachment("fullscreenMode", obj);
        insertClip.setAttachment("hasFxTrans", "true");
        insertClip.setAttachment("fxFilePath", filePath2);
        insertClip.setAttachment("fxInpoint", String.valueOf(outPoint / 1000));
        int index = insertClip.getIndex();
        nvsVideoTrack.setBuiltinTransition(index, null);
        int i5 = index - 1;
        if (i5 >= 0) {
            if (str6 != null) {
                if (str6.length() > 30) {
                    nvsVideoTrack.setPackagedTransition(i5, str6);
                } else {
                    nvsVideoTrack.setBuiltinTransition(i5, str6);
                }
            } else {
                nvsVideoTrack.setBuiltinTransition(i5, null);
            }
        }
        NvsVideoFx appendBuiltinFx = insertClip.appendBuiltinFx("Storyboard");
        long j3 = i2;
        long j4 = (outPoint - (j3 - j2)) / 1000;
        String changeFxDesc = changeFxDesc(str, filePath2, j4, ((((insertClip.getOutPoint() - insertClip.getInPoint()) + this.timeBase) - j3) / 1000) - j4, z);
        if (changeFxDesc == null) {
            return 0L;
        }
        if (str5.contains("hori")) {
            changeROI = Utils.changeHoriROI(this.m_inputIamgeInfo.get(intValue).imgRatio, this.m_inputIamgeInfo.get(intValue).faceRect, Utils.changeHoriROI(this.m_inputIamgeInfo.get(intValue2).imgRatio, this.m_inputIamgeInfo.get(intValue2).faceRect, changeFxDesc, null), insertClip);
            str4 = null;
        } else if (str5.contains("vert")) {
            str4 = null;
            changeROI = Utils.changeVertROI(this.m_inputIamgeInfo.get(intValue).imgRatio, this.m_inputIamgeInfo.get(intValue).faceRect, Utils.changeVertROI(this.m_inputIamgeInfo.get(intValue2).imgRatio, this.m_inputIamgeInfo.get(intValue2).faceRect, changeFxDesc, null), insertClip);
        } else {
            str4 = null;
            setImageMotion(insertClip, this.m_inputIamgeInfo.get(intValue), 1);
            changeROI = Utils.changeROI(this.m_inputIamgeInfo.get(intValue2).imgRatio, this.m_inputIamgeInfo.get(intValue2).faceRect, changeFxDesc);
        }
        appendBuiltinFx.setStringVal("Description String", changeROI);
        if (!z) {
            str5 = "assets:/" + str5;
        }
        appendBuiltinFx.setStringVal("Resource Dir", str5.substring(0, str5.lastIndexOf(h.g)));
        if (nvsVideoTrack.getClipByIndex(i3) != null) {
            String str7 = str4;
            if (str2.equalsIgnoreCase("")) {
                nvsVideoTrack.setBuiltinTransition(i, str7);
            } else {
                if (str2.length() > 30) {
                    builtinTransition = nvsVideoTrack.setPackagedTransition(i, str2);
                } else {
                    builtinTransition = nvsVideoTrack.setBuiltinTransition(i, str2);
                }
                if (builtinTransition != null) {
                    builtinTransition.setVideoTransitionDurationScaleFactor(((float) j) / 1000.0f);
                }
            }
        }
        this.m_fxTransClipCount++;
        return insertClip.getInPoint() + outPoint;
    }

    private String changeFxDesc(String str, String str2, long j, long j2, boolean z) {
        String readFile = Utils.readFile(str, z ? null : this.m_context.getAssets());
        if (readFile == null) {
            return null;
        }
        return readFile.replace("placeholder.jpg", str2).replace("xiaomiStartTime", String.valueOf(j)).replace("xiaomiDurationTime", String.valueOf(j2));
    }

    private String changeFxDescV3(String str, String str2, String str3, long j, long j2, long j3, boolean z) {
        String readFile = Utils.readFile(str, z ? null : this.m_context.getAssets());
        if (readFile == null) {
            return null;
        }
        return readFile.replace("placeholder.jpg", str2).replace("picture.jpg", str3).replace("xiaomiStartTime", String.valueOf(j)).replace("pictureShowUp", String.valueOf(j2)).replace("xiaomiDurationTime", String.valueOf(j3)).replace("pictureLoop", String.valueOf(j3)).replace("lastMove", String.valueOf(j2 + 1000));
    }

    private String getTransType(int i, NvsVideoTrack nvsVideoTrack) {
        NvsVideoClip clipByIndex = nvsVideoTrack.getClipByIndex(i);
        NvsVideoClip clipByIndex2 = nvsVideoTrack.getClipByIndex(i + 1);
        String obj = clipByIndex.getAttachment("fullscreenMode").toString();
        String obj2 = clipByIndex2.getAttachment("fullscreenMode").toString();
        return obj.equalsIgnoreCase("true") ? obj2.equalsIgnoreCase("true") ? "full" : "fh" : obj2.equalsIgnoreCase("true") ? "hf" : "half";
    }

    private String getTrans(ArrayList<String> arrayList, ArrayList<StringBuilder> arrayList2) {
        if (arrayList.isEmpty()) {
            return "";
        }
        int size = arrayList.size();
        if (hasNullTrans(arrayList)) {
            size *= 2;
        }
        int nextInt = rand.nextInt(size);
        if (nextInt >= arrayList.size() || nextInt < 0) {
            return "";
        }
        String str = arrayList.get(nextInt);
        if (arrayList2 != null && !arrayList2.isEmpty()) {
            for (int i = 0; i < arrayList2.size(); i++) {
                if (arrayList2.get(i).toString().equalsIgnoreCase(str)) {
                    return arrayList2.get(i).toString();
                }
            }
        }
        return str;
    }

    private ArrayList<String> getTrans(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<StringBuilder> arrayList3, ArrayList<String> arrayList4) {
        ArrayList<String> arrayList5 = new ArrayList<>();
        if (arrayList.isEmpty() && arrayList2.isEmpty()) {
            arrayList5.add("");
            return arrayList5;
        }
        int size = arrayList.size() + arrayList2.size();
        if (hasNullTrans(arrayList)) {
            size *= 2;
        }
        int nextInt = rand.nextInt(size);
        if (nextInt < 0) {
            arrayList5.add("");
            return arrayList5;
        }
        int i = 0;
        if (nextInt < arrayList.size()) {
            String str = arrayList.get(nextInt);
            if (arrayList3 != null && !arrayList3.isEmpty()) {
                while (i < arrayList3.size()) {
                    if (arrayList3.get(i).toString().equalsIgnoreCase(str)) {
                        arrayList5.add(arrayList3.get(i).toString());
                        return arrayList5;
                    }
                    i++;
                }
            }
            arrayList5.add(str);
            return arrayList5;
        }
        int size2 = nextInt - arrayList.size();
        if (size2 < arrayList2.size() && size2 >= 0) {
            String str2 = arrayList2.get(size2);
            if (str2.equalsIgnoreCase("")) {
                arrayList5.add("");
                return arrayList5;
            }
            if (arrayList4 != null && !arrayList4.isEmpty()) {
                while (i < arrayList4.size()) {
                    if (arrayList4.get(i).toString().contains(str2)) {
                        arrayList5.add(arrayList4.get(i).toString());
                        arrayList5.add(getTransFollowFx(arrayList, arrayList3));
                        return arrayList5;
                    }
                    i++;
                }
            }
            arrayList5.add("");
            return arrayList5;
        }
        arrayList5.add("");
        return arrayList5;
    }

    private String getTransFollowFx(ArrayList<String> arrayList, ArrayList<StringBuilder> arrayList2) {
        int nextInt;
        ArrayList arrayList3 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).length() >= 30) {
                arrayList3.add(arrayList.get(i));
            }
        }
        if (!arrayList3.isEmpty() && (nextInt = rand.nextInt(arrayList3.size())) >= 0 && nextInt < arrayList3.size()) {
            String str = (String) arrayList3.get(nextInt);
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                if (arrayList2.get(i2).toString().equalsIgnoreCase(str)) {
                    return arrayList2.get(i2).toString();
                }
            }
        }
        return "";
    }

    private boolean hasNullTrans(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals("")) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0076 A[LOOP:0: B:32:0x0074->B:33:0x0076, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void addMusicFile(com.meicam.sdk.NvsTimeline r23, java.lang.String r24, boolean r25) {
        /*
            r22 = this;
            r0 = r22
            r7 = r24
            if (r7 != 0) goto L7
            return
        L7:
            java.lang.String r1 = ""
            if (r7 != r1) goto Lc
            return
        Lc:
            java.lang.String r1 = "theme helper"
            android.util.Log.d(r1, r7)
            com.meicam.sdk.NvsStreamingContext r1 = r0.m_streamingContext
            com.meicam.sdk.NvsAVFileInfo r1 = r1.getAVFileInfo(r7)
            if (r1 != 0) goto L1a
            return
        L1a:
            int r2 = r1.getAudioStreamCount()
            r3 = 1
            if (r2 >= r3) goto L22
            return
        L22:
            long r2 = r23.getDuration()
            r4 = 0
            long r5 = r1.getAudioStreamDuration(r4)
            long r8 = r0.timeBase
            long r5 = r5 / r8
            long r5 = r5 * r8
            r10 = 0
            if (r25 == 0) goto L5f
            long r12 = r0.m_selectedLocalMusicStart
            int r1 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
            if (r1 < 0) goto L5f
            long r14 = r0.m_selectedLocalMusicEnd
            int r1 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1))
            if (r1 <= 0) goto L5f
            long r12 = r12 / r8
            long r12 = r12 * r8
            long r14 = r14 / r8
            long r14 = r14 * r8
            long r16 = r14 - r12
            r18 = 2
            long r20 = r8 * r18
            int r1 = (r16 > r20 ? 1 : (r16 == r20 ? 0 : -1))
            if (r1 <= 0) goto L54
            int r1 = (r14 > r5 ? 1 : (r14 == r5 ? 0 : -1))
            if (r1 <= 0) goto L56
            long r16 = r5 - r12
            goto L56
        L54:
            r16 = r5
        L56:
            long r8 = r8 * r18
            int r1 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1))
            if (r1 >= 0) goto L5d
            goto L5f
        L5d:
            r10 = r12
            goto L61
        L5f:
            r16 = r5
        L61:
            r1 = 0
            r5 = r23
            r5.setThemeMusicVolumeGain(r1, r1)
            com.meicam.sdk.NvsAudioTrack r8 = r23.appendAudioTrack()
            long r2 = r2 + r16
            long r5 = r0.timeBase
            long r2 = r2 - r5
            long r2 = r2 / r16
            int r9 = (int) r2
            r12 = r4
        L74:
            if (r12 >= r9) goto L82
            long r5 = r10 + r16
            r1 = r8
            r2 = r24
            r3 = r10
            r1.appendClip(r2, r3, r5)
            int r12 = r12 + 1
            goto L74
        L82:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.meicam.themehelper.NvsThemeHelper.addMusicFile(com.meicam.sdk.NvsTimeline, java.lang.String, boolean):void");
    }

    private void sortFileList(ArrayList<NvsImageFileDesc> arrayList) {
        if (arrayList == null || arrayList.size() < 1) {
            return;
        }
        NvsImageFileDesc nvsImageFileDesc = null;
        int i = 0;
        while (true) {
            if (i >= arrayList.size()) {
                i = 0;
                break;
            } else if (arrayList.get(i).isCover) {
                nvsImageFileDesc = arrayList.get(i);
                break;
            } else {
                i++;
            }
        }
        if (nvsImageFileDesc != null) {
            arrayList.remove(i);
        }
        NvsParseHelper.sortFileByName(arrayList);
        NvsParseHelper.sortFileByModifyTime(arrayList);
        if (nvsImageFileDesc == null) {
            return;
        }
        arrayList.add(0, nvsImageFileDesc);
    }

    /* loaded from: classes.dex */
    public class NvsThemeAsset {
        public StringBuilder m_blurFxId;
        public StringBuilder m_cafSticker10sId;
        public StringBuilder m_cafStickerId;
        public long m_endingFx10sLen;
        public String m_endingFxId;
        public String m_endingFxId10s;
        public String m_endingFxImgPath;
        public long m_musicLen;
        public long m_musicLen10s;
        public StringBuilder m_stickerId1;
        public StringBuilder m_stickerId2;
        public StringBuilder m_theme10sId;
        public StringBuilder m_themeId;
        public int m_transOffset;
        public int version = 1;
        public boolean isDownload = false;
        public boolean m_hasFxTrans = false;
        public ArrayList<String> m_blurWidthFxTransList = new ArrayList<>();
        public long m_endingFxLen = 1500000;
        public ArrayList<StringBuilder> m_transIDList = new ArrayList<>();
        public ArrayList<String> m_fullFx9v16List = new ArrayList<>();
        public ArrayList<String> m_halfFx9v16List = new ArrayList<>();
        public ArrayList<String> m_hfFx9v16List = new ArrayList<>();
        public ArrayList<String> m_fhFx9v16List = new ArrayList<>();
        public ArrayList<String> m_fullFx9v18List = new ArrayList<>();
        public ArrayList<String> m_halfFx9v18List = new ArrayList<>();
        public ArrayList<String> m_hfFx9v18List = new ArrayList<>();
        public ArrayList<String> m_fhFx9v18List = new ArrayList<>();
        public ArrayList<String> m_fullFx9v19List = new ArrayList<>();
        public ArrayList<String> m_halfFx9v19List = new ArrayList<>();
        public ArrayList<String> m_hfFx9v19List = new ArrayList<>();
        public ArrayList<String> m_fhFx9v19List = new ArrayList<>();
        public ArrayList<String> m_fullFx9vx73List = new ArrayList<>();
        public ArrayList<String> m_halfFx9vx73List = new ArrayList<>();
        public ArrayList<String> m_hfFx9vx73List = new ArrayList<>();
        public ArrayList<String> m_fhFx9vx73List = new ArrayList<>();
        public ArrayList<NvsMusicPointDesc> m_musicPoints = new ArrayList<>();
        public ArrayList<NvsMusicPointDesc> m_musicPoints10s = new ArrayList<>();
        public NvsTransDesc m_transDesc = new NvsTransDesc();

        public NvsThemeAsset() {
        }
    }

    private String getLicFilePath(String str) {
        String[] split = str.split("\\.");
        if (split.length == 0) {
            return null;
        }
        return split[0] + ".lic";
    }

    private StringBuilder installAssetToContext(String str, boolean z) {
        int i;
        if (this.m_streamingContext == null || str == null) {
            return null;
        }
        String licFilePath = getLicFilePath(str);
        int i2 = 4;
        if (str.contains(".captionstyle")) {
            i = 2;
        } else {
            if (str.contains(".videofx")) {
                i2 = 0;
            } else if (str.contains(".videotransition")) {
                i2 = 1;
            } else if (str.contains(".animatedsticker")) {
                i2 = 3;
            }
            i = i2;
        }
        StringBuilder sb = new StringBuilder();
        if (z) {
            if (this.m_streamingContext.getAssetPackageManager().upgradeAssetPackage(str, licFilePath, i, true, sb) != 0) {
                Log.d(TAG, "Failed to upgrade package! " + str);
                return null;
            }
        } else {
            int installAssetPackage = this.m_streamingContext.getAssetPackageManager().installAssetPackage(str, licFilePath, i, true, sb);
            if (installAssetPackage != 0 && installAssetPackage != 2) {
                Log.d(TAG, "Failed to install package! " + str);
                return null;
            } else if (installAssetPackage == 2 && checkUpdateAsset(sb.toString(), str, i) && this.m_streamingContext.getAssetPackageManager().upgradeAssetPackage(str, licFilePath, i, true, sb) != 0) {
                Log.d(TAG, "Failed to upgrade package! " + str);
                return null;
            }
        }
        return sb;
    }

    private boolean installThemeAsset(Context context, String str, NvsThemeAsset nvsThemeAsset) {
        if (this.m_streamingContext == null) {
            this.m_streamingContext = NvsStreamingContext.getInstance();
        }
        if (this.m_streamingContext == null) {
            return false;
        }
        String readFile = Utils.readFile(str + "/info.json", context == null ? null : context.getAssets());
        if (readFile == null) {
            Log.d(TAG, "read theme info json file error!");
            return false;
        }
        NvsThemeAsset nvsThemeAsset2 = new NvsThemeAsset();
        nvsThemeAsset2.isDownload = context == null;
        try {
            JSONObject jSONObject = new JSONObject(readFile);
            if (jSONObject.has("version")) {
                nvsThemeAsset2.version = jSONObject.getInt("version");
            }
            if (jSONObject.has("hasFxTrans")) {
                nvsThemeAsset2.m_hasFxTrans = jSONObject.getBoolean("hasFxTrans");
            }
            if (jSONObject.has("transOffset")) {
                nvsThemeAsset2.m_transOffset = jSONObject.getInt("transOffset");
            } else {
                nvsThemeAsset2.m_transOffset = 500;
            }
            String assetPath = getAssetPath(jSONObject, "theme", str, context == null);
            StringBuilder installAssetToContext = installAssetToContext(assetPath, checkUpdateAsset(nvsThemeAsset == null ? null : nvsThemeAsset.m_themeId.toString(), assetPath, 4));
            nvsThemeAsset2.m_themeId = installAssetToContext;
            if (installAssetToContext == null) {
                Log.d(TAG, "Failed to install theme package!");
                return false;
            }
            String assetPath2 = getAssetPath(jSONObject, "theme10s", str, context == null);
            StringBuilder installAssetToContext2 = installAssetToContext(assetPath2, checkUpdateAsset(nvsThemeAsset == null ? null : nvsThemeAsset.m_theme10sId.toString(), assetPath2, 4));
            nvsThemeAsset2.m_theme10sId = installAssetToContext2;
            if (installAssetToContext2 == null) {
                Log.d(TAG, "Failed to install theme 10s package!");
                return false;
            }
            if (jSONObject.has("cafSticker")) {
                String assetPath3 = getAssetPath(jSONObject, "cafSticker", str, context == null);
                nvsThemeAsset2.m_cafStickerId = installAssetToContext(assetPath3, checkUpdateAsset(nvsThemeAsset == null ? null : nvsThemeAsset.m_cafStickerId.toString(), assetPath3, 3));
            }
            if (jSONObject.has("cafSticker10s")) {
                String assetPath4 = getAssetPath(jSONObject, "cafSticker10s", str, context == null);
                nvsThemeAsset2.m_cafSticker10sId = installAssetToContext(assetPath4, checkUpdateAsset(nvsThemeAsset == null ? null : nvsThemeAsset.m_cafSticker10sId.toString(), assetPath4, 3));
            }
            String assetPath5 = getAssetPath(jSONObject, "sticker1", str, context == null);
            StringBuilder installAssetToContext3 = installAssetToContext(assetPath5, checkUpdateAsset(nvsThemeAsset == null ? null : nvsThemeAsset.m_stickerId1.toString(), assetPath5, 3));
            nvsThemeAsset2.m_stickerId1 = installAssetToContext3;
            if (installAssetToContext3 == null) {
                Log.d(TAG, "Failed to install sticker package!");
                return false;
            }
            this.m_stickerId1 = installAssetToContext3.toString();
            String assetPath6 = getAssetPath(jSONObject, "sticker2", str, context == null);
            StringBuilder installAssetToContext4 = installAssetToContext(assetPath6, checkUpdateAsset(nvsThemeAsset == null ? null : nvsThemeAsset.m_stickerId2.toString(), assetPath6, 3));
            nvsThemeAsset2.m_stickerId2 = installAssetToContext4;
            if (installAssetToContext4 == null) {
                Log.d(TAG, "Failed to install sticker2 package!");
                return false;
            }
            this.m_stickerId2 = installAssetToContext4.toString();
            if (jSONObject.has("blurFx")) {
                String assetPath7 = getAssetPath(jSONObject, "blurFx", str, context == null);
                nvsThemeAsset2.m_blurFxId = installAssetToContext(assetPath7, checkUpdateAsset(nvsThemeAsset == null ? null : nvsThemeAsset.m_blurFxId.toString(), assetPath7, 0));
                if (jSONObject.has("blurWithFxTrans")) {
                    JSONArray jSONArray = jSONObject.getJSONArray("blurWithFxTrans");
                    for (int i = 0; i < jSONArray.length(); i++) {
                        nvsThemeAsset2.m_blurWidthFxTransList.add(jSONArray.getString(i));
                    }
                }
            }
            try {
                nvsThemeAsset2.m_endingFxId = Utils.readFile(str + h.g + jSONObject.getString("endingVideoFX"), context == null ? null : this.m_context.getAssets());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jSONObject.has("endingVideoFXDuration")) {
                nvsThemeAsset2.m_endingFxLen = jSONObject.getLong("endingVideoFXDuration") * 1000;
            }
            try {
                nvsThemeAsset2.m_endingFxId10s = Utils.readFile(str + h.g + jSONObject.getString("endingVideoFX10s"), context == null ? null : this.m_context.getAssets());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            nvsThemeAsset2.m_endingFx10sLen = jSONObject.getLong("endingVideoFX10sDuration") * 1000;
            String str2 = "assets:/" + str + "/black_block.png";
            if (context == null) {
                str2 = str + "/black_block.png";
            }
            nvsThemeAsset2.m_endingFxImgPath = str2;
            String readFile2 = Utils.readFile(str + h.g + jSONObject.getString("musicrhythm"), context == null ? null : context.getAssets());
            if (readFile2 != null) {
                nvsThemeAsset2.m_musicLen = NvsParseHelper.readMusicPoint(readFile2, nvsThemeAsset2.m_musicPoints, nvsThemeAsset2.m_transDesc);
            }
            String readFile3 = Utils.readFile(str + h.g + jSONObject.getString("musicrhythm10s"), context == null ? null : context.getAssets());
            if (readFile3 != null) {
                nvsThemeAsset2.m_musicLen10s = NvsParseHelper.readMusicPoint(readFile3, nvsThemeAsset2.m_musicPoints10s, null);
            }
            if (jSONObject.has("transition")) {
                JSONArray jSONArray2 = jSONObject.getJSONArray("transition");
                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                    String string = jSONArray2.getString(i2);
                    if (string != null) {
                        String str3 = "assets:/" + str + h.g + string;
                        if (context == null) {
                            str3 = str + h.g + string;
                        }
                        StringBuilder installAssetToContext5 = installAssetToContext(str3, nvsThemeAsset != null ? checkUpdateAsset(getTransId(string, nvsThemeAsset), str3, 1) : false);
                        if (installAssetToContext5 != null) {
                            nvsThemeAsset2.m_transIDList.add(installAssetToContext5);
                        }
                    }
                }
            }
            if (jSONObject.has("transFx")) {
                JSONArray jSONArray3 = jSONObject.getJSONArray("transFx");
                for (int i3 = 0; i3 < jSONArray3.length(); i3++) {
                    String string2 = jSONArray3.getString(i3);
                    addToFxList(nvsThemeAsset2, str + h.g + string2, string2);
                }
            }
            if (nvsThemeAsset != null) {
                this.mThemeAssetMap.remove(nvsThemeAsset);
            }
            this.mThemeAssetMap.put(str, nvsThemeAsset2);
            return true;
        } catch (JSONException e3) {
            e3.printStackTrace();
            return false;
        }
    }

    private void addToFxList(NvsThemeAsset nvsThemeAsset, String str, String str2) {
        if (str2.contains("9v16")) {
            if (str2.contains("-full")) {
                nvsThemeAsset.m_fullFx9v16List.add(str);
            } else if (str2.contains("-half")) {
                nvsThemeAsset.m_halfFx9v16List.add(str);
            } else if (str2.contains("-hf")) {
                nvsThemeAsset.m_hfFx9v16List.add(str);
            } else if (str2.contains("-fh")) {
                nvsThemeAsset.m_fhFx9v16List.add(str);
            } else {
                nvsThemeAsset.m_fullFx9v16List.add(str);
                nvsThemeAsset.m_halfFx9v16List.add(str);
                nvsThemeAsset.m_hfFx9v16List.add(str);
                nvsThemeAsset.m_fhFx9v16List.add(str);
            }
        } else if (str2.contains("9v18")) {
            if (str2.contains("-full")) {
                nvsThemeAsset.m_fullFx9v18List.add(str);
            } else if (str2.contains("-half")) {
                nvsThemeAsset.m_halfFx9v18List.add(str);
            } else if (str2.contains("-hf")) {
                nvsThemeAsset.m_hfFx9v18List.add(str);
            } else if (str2.contains("-fh")) {
                nvsThemeAsset.m_fhFx9v18List.add(str);
            } else {
                nvsThemeAsset.m_fullFx9v18List.add(str);
                nvsThemeAsset.m_halfFx9v18List.add(str);
                nvsThemeAsset.m_hfFx9v18List.add(str);
                nvsThemeAsset.m_fhFx9v18List.add(str);
            }
        } else if (str2.contains("9v19")) {
            if (str2.contains("-full")) {
                nvsThemeAsset.m_fullFx9v19List.add(str);
            } else if (str2.contains("-half")) {
                nvsThemeAsset.m_halfFx9v19List.add(str);
            } else if (str2.contains("-hf")) {
                nvsThemeAsset.m_hfFx9v19List.add(str);
            } else if (str2.contains("-fh")) {
                nvsThemeAsset.m_fhFx9v19List.add(str);
            } else {
                nvsThemeAsset.m_fullFx9v19List.add(str);
                nvsThemeAsset.m_halfFx9v19List.add(str);
                nvsThemeAsset.m_hfFx9v19List.add(str);
                nvsThemeAsset.m_fhFx9v19List.add(str);
            }
        } else if (!str2.contains("9vx73")) {
        } else {
            if (str2.contains("-full")) {
                nvsThemeAsset.m_fullFx9vx73List.add(str);
            } else if (str2.contains("-half")) {
                nvsThemeAsset.m_halfFx9vx73List.add(str);
            } else if (str2.contains("-hf")) {
                nvsThemeAsset.m_hfFx9vx73List.add(str);
            } else if (str2.contains("-fh")) {
                nvsThemeAsset.m_fhFx9vx73List.add(str);
            } else {
                nvsThemeAsset.m_fullFx9vx73List.add(str);
                nvsThemeAsset.m_halfFx9vx73List.add(str);
                nvsThemeAsset.m_hfFx9vx73List.add(str);
                nvsThemeAsset.m_fhFx9vx73List.add(str);
            }
        }
    }

    private String getTransId(String str, NvsThemeAsset nvsThemeAsset) {
        if (nvsThemeAsset == null || nvsThemeAsset.m_transIDList == null) {
            return null;
        }
        for (int i = 0; i < nvsThemeAsset.m_transIDList.size(); i++) {
            String sb = nvsThemeAsset.m_transIDList.get(i).toString();
            if (sb.contains(str.replace(".videotransition", ""))) {
                return sb;
            }
        }
        return null;
    }

    private boolean checkUpdateAsset(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return false;
        }
        if (this.m_streamingContext == null) {
            this.m_streamingContext = NvsStreamingContext.getInstance();
        }
        NvsStreamingContext nvsStreamingContext = this.m_streamingContext;
        return nvsStreamingContext != null && nvsStreamingContext.getAssetPackageManager().getAssetPackageVersionFromAssetPackageFilePath(str2) > this.m_streamingContext.getAssetPackageManager().getAssetPackageVersion(str, i);
    }

    private String getAssetPath(JSONObject jSONObject, String str, String str2, boolean z) {
        String str3;
        try {
            str3 = jSONObject.getString(str);
        } catch (JSONException e) {
            e.printStackTrace();
            str3 = null;
        }
        if (str3 == null || str3.isEmpty()) {
            return null;
        }
        String str4 = "assets:/" + str2 + h.g + str3;
        if (!z) {
            return str4;
        }
        return str2 + h.g + str3;
    }

    private int getMinImgCount() {
        return (this.m_inputIamgeInfo.isEmpty() || !this.m_inputIamgeInfo.get(0).isCover) ? 3 : 4;
    }
}
