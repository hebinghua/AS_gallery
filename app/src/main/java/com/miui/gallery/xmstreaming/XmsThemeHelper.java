package com.miui.gallery.xmstreaming;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.miui.gallery.xmstreaming.TimelineTheme;
import com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView;
import com.miui.gallery.xmstreaming.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class XmsThemeHelper {
    private static final String DEFAULT_RHYTHM_10S_JSON = "assets:/xms/defaultRhythm10s.json";
    private static final String DEFAULT_RHYTHM_JSON = "assets:/xms/defaultRhythm.json";
    private static final String TAG = "XmsThemeHelper";
    public static final int XMS_LOCAL_MUSIC_SELECTED = 1;
    public static final int XMS_LOCATION_TYPE_DE = 0;
    public static final int XMS_LOCATION_TYPE_IN = 1;
    public static final int XMS_LOCATION_TYPE_INDIA = 2;
    public static final int XMS_MUSIC_PACKAGE_SELECTED = 2;
    public static final int XVS_MUSIC_NO_SELECTED = 3;
    private List<XmsClip> mClipList = new ArrayList();
    private Context mContext;
    private int mDuration;
    private boolean mIsCustomerMusic;
    private boolean mIsShortVideo;
    private int mLocationType;
    private String mMusicDir;
    private XmsMusicInfo mMusicInfo;
    private XmsTemplateInfo mTemplateInfo;
    private XmsTimeline mTimeline;
    private TimelineTheme mTimelineTheme;
    private XmsContext mXmsContext;

    public void setShortVideo(boolean z) {
        this.mIsShortVideo = z;
    }

    public void initHelper(Context context, XmsTimeline xmsTimeline, int i) {
        this.mContext = context;
        this.mTimeline = xmsTimeline;
        this.mXmsContext = XmsContext.getInstance();
        this.mLocationType = i;
    }

    public void setTemplate(String str) {
        XmsPackageInfo xmsPackageInfo = (XmsPackageInfo) new Gson().fromJson(Utils.readFile(str + File.separator + "info.json"), (Class<Object>) XmsPackageInfo.class);
        if (xmsPackageInfo != null) {
            int i = this.mLocationType;
            if (i == 1) {
                XmsTemplateInfo xmsTemplateInfo = xmsPackageInfo.in;
                if (xmsTemplateInfo != null) {
                    this.mTemplateInfo = xmsTemplateInfo;
                } else {
                    this.mTemplateInfo = xmsPackageInfo.de;
                }
            } else if (i == 2) {
                XmsTemplateInfo xmsTemplateInfo2 = xmsPackageInfo.india;
                if (xmsTemplateInfo2 != null) {
                    this.mTemplateInfo = xmsTemplateInfo2;
                } else {
                    XmsTemplateInfo xmsTemplateInfo3 = xmsPackageInfo.in;
                    if (xmsTemplateInfo3 != null) {
                        this.mTemplateInfo = xmsTemplateInfo3;
                    } else {
                        this.mTemplateInfo = xmsPackageInfo.de;
                    }
                }
            } else {
                this.mTemplateInfo = xmsPackageInfo.de;
            }
        }
        if (this.mTemplateInfo == null) {
            Log.e(TAG, "mTemplateInfo is null");
            this.mTemplateInfo = new XmsTemplateInfo();
        }
        this.mTimeline.setFilter(getPath(str, this.mTemplateInfo.filter));
        this.mTimeline.setSticker(getPath(str, this.mTemplateInfo.sticker), getPath(str, this.mTemplateInfo.sticker2));
        this.mTimeline.setMask(getPath(str, this.mTemplateInfo.mask));
        this.mMusicDir = str;
        this.mMusicInfo = this.mTemplateInfo;
        this.mIsCustomerMusic = false;
        buildMusicInfo(false);
    }

    private void buildMusicInfo(boolean z) {
        if (this.mIsShortVideo) {
            if (!this.mIsCustomerMusic) {
                setMusic(this.mMusicInfo.musicId10s);
            }
            setMusicRhythm(this.mMusicInfo.jsonId10s, z);
            return;
        }
        if (!this.mIsCustomerMusic) {
            setMusic(this.mMusicInfo.musicId);
        }
        setMusicRhythm(this.mMusicInfo.jsonId, z);
    }

    private String getPath(String str, String str2) {
        if (str2 == null) {
            return null;
        }
        String str3 = str + File.separator + str2;
        if (str3.startsWith("assets") || new File(str3).exists()) {
            return str3;
        }
        Log.e(TAG, " is not exist" + str3);
        return null;
    }

    public void changeMusic(String str, int i) {
        if (str == null) {
            this.mIsCustomerMusic = true;
            this.mTimeline.setMusic(null);
        } else if (i != 2) {
            if (i == 1) {
                this.mIsCustomerMusic = true;
                this.mTimeline.setMusic(str);
                return;
            }
            this.mIsCustomerMusic = true;
            setMusic(null);
        } else {
            this.mMusicDir = str;
            XmsMusicInfo xmsMusicInfo = (XmsMusicInfo) new Gson().fromJson(Utils.readFile(this.mMusicDir + File.separator + "info.json"), (Class<Object>) XmsMusicInfo.class);
            this.mMusicInfo = xmsMusicInfo;
            if (xmsMusicInfo == null) {
                Log.e(TAG, "XmsMusicInfo is null " + str);
                this.mMusicInfo = new XmsMusicInfo();
            }
            this.mIsCustomerMusic = false;
            buildMusicInfo(true);
        }
    }

    private void setMusic(String str) {
        this.mTimeline.setMusic(getPath(this.mMusicDir, str));
    }

    private void setMusicRhythm(String str, boolean z) {
        TimelineTheme timelineTheme;
        TimelineTheme timelineTheme2;
        String readFile = Utils.readFile(getPath(this.mMusicDir, str));
        int i = (!z || (timelineTheme2 = this.mTimelineTheme) == null) ? 0 : timelineTheme2.transType;
        TimelineTheme timelineTheme3 = (TimelineTheme) new Gson().fromJson(readFile, (Class<Object>) TimelineTheme.class);
        this.mTimelineTheme = timelineTheme3;
        if (timelineTheme3 == null) {
            Log.e(TAG, "mTimelineTheme is null");
            this.mTimelineTheme = (TimelineTheme) new Gson().fromJson(Utils.readFile(this.mIsShortVideo ? DEFAULT_RHYTHM_10S_JSON : DEFAULT_RHYTHM_JSON), (Class<Object>) TimelineTheme.class);
        }
        if (z && (timelineTheme = this.mTimelineTheme) != null) {
            timelineTheme.transType = i;
        }
        reBuildTimeLine();
    }

    public void changeTimelineDuration(boolean z) {
        if (this.mIsShortVideo != z) {
            this.mIsShortVideo = z;
            buildMusicInfo(false);
        }
    }

    public void reBuildTimeLine() {
        int size = this.mTimelineTheme.points.size();
        if (this.mIsShortVideo) {
            size = Math.min(size, 5);
        }
        int min = Math.min(size, this.mClipList.size());
        TimelineTheme timelineTheme = this.mTimelineTheme;
        int i = (timelineTheme.transType != 1 || min == timelineTheme.points.size()) ? 0 : 1000;
        int i2 = 0;
        for (int i3 = 0; i3 < min; i3++) {
            TimelineTheme.ImageTrans imageTrans = this.mTimelineTheme.points.get(i3);
            if (TextUtils.isEmpty(imageTrans.transName)) {
                imageTrans.transLen = 0;
            }
            String str = imageTrans.effects;
            XmsEffectTrack xmsEffectTrack = str != null ? (XmsEffectTrack) new Gson().fromJson(Utils.readFile(getPath(this.mMusicDir, str)), (Class<Object>) XmsEffectTrack.class) : null;
            int i4 = min - 1;
            if (i3 == i4) {
                imageTrans.transName = null;
            }
            if (i3 == i4) {
                this.mClipList.get(i3).setTrans((imageTrans.cutPoint - i2) + i, imageTrans, xmsEffectTrack);
            } else {
                this.mClipList.get(i3).setTrans(imageTrans.cutPoint - i2, imageTrans, xmsEffectTrack);
            }
            i2 = imageTrans.cutPoint - imageTrans.transLen;
        }
        this.mDuration = this.mTimelineTheme.points.get(min - 1).cutPoint + i;
        this.mTimeline.setTransType(this.mTimelineTheme.transType);
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void appendClip(String str) {
        XmsClip appendClip = this.mTimeline.appendClip(str);
        appendClip.setPath(str);
        this.mClipList.add(appendClip);
    }

    public void clearClip() {
        this.mTimeline.clearClip();
        this.mClipList.clear();
    }

    public ArrayList<XmsMultiThumbnailSequenceView.ThumbnailSequenceDesc> getThumbnailImages() {
        ArrayList<XmsMultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList = new ArrayList<>();
        TimelineTheme timelineTheme = this.mTimelineTheme;
        if (timelineTheme == null) {
            return arrayList;
        }
        int size = timelineTheme.points.size();
        if (this.mIsShortVideo) {
            size = Math.min(size, 5);
        }
        int i = 0;
        for (int i2 = 0; i2 < this.mClipList.size() && i2 < size; i2++) {
            XmsMultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = new XmsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
            thumbnailSequenceDesc.mediaFilePath = this.mClipList.get(i2).getPath();
            long j = i * 1000;
            thumbnailSequenceDesc.trimIn = j;
            i = this.mTimelineTheme.points.get(i2).cutPoint;
            thumbnailSequenceDesc.trimOut = i * 1000;
            thumbnailSequenceDesc.inPoint = j;
            thumbnailSequenceDesc.outPoint = i * 1000;
            thumbnailSequenceDesc.stillImageHint = true;
            arrayList.add(thumbnailSequenceDesc);
        }
        return arrayList;
    }

    public int geClipMiddleDuration(int i) {
        TimelineTheme timelineTheme;
        if (this.mClipList.size() <= i || (timelineTheme = this.mTimelineTheme) == null || timelineTheme.points.size() <= i) {
            return 0;
        }
        if (i == 0) {
            return this.mTimelineTheme.points.get(0).cutPoint / 2;
        }
        return (this.mTimelineTheme.points.get(i).cutPoint + this.mTimelineTheme.points.get(i - 1).cutPoint) / 2;
    }
}
