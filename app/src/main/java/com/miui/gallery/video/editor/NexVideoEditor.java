package com.miui.gallery.video.editor;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import ch.qos.logback.core.net.SyslogConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.UriUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.NexVideoEditor;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.manager.NexAssetTemplateManager;
import com.miui.gallery.video.editor.util.FileHelper;
import com.miui.gallery.video.editor.util.IntentUtil;
import com.miui.gallery.video.editor.widget.DisplayWrapper;
import com.nexstreaming.nexeditorsdk.exception.ExpiredTimeException;
import com.nexstreaming.nexeditorsdk.nexAnimate;
import com.nexstreaming.nexeditorsdk.nexApplicationConfig;
import com.nexstreaming.nexeditorsdk.nexAspectProfile;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.nexstreaming.nexeditorsdk.nexColorEffect;
import com.nexstreaming.nexeditorsdk.nexCrop;
import com.nexstreaming.nexeditorsdk.nexEngine;
import com.nexstreaming.nexeditorsdk.nexEngineListener;
import com.nexstreaming.nexeditorsdk.nexEngineView;
import com.nexstreaming.nexeditorsdk.nexOverlayImage;
import com.nexstreaming.nexeditorsdk.nexOverlayItem;
import com.nexstreaming.nexeditorsdk.nexOverlayKineMasterText;
import com.nexstreaming.nexeditorsdk.nexOverlayPreset;
import com.nexstreaming.nexeditorsdk.nexProject;
import com.nexstreaming.nexeditorsdk.nexTemplateManager;
import com.xiaomi.miai.api.StatusCode;
import com.xiaomi.stat.b.h;
import com.xiaomi.stat.d;
import com.xiaomi.video.VideoDecoder;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class NexVideoEditor extends VideoEditor implements nexEngineListener {
    public static final String[] sHardwareSupportWhiteList = {"riva", "pine", "cereus", "cactus", "olive", "ginkgo", "olivelite", "lotus", "merlin", "merlinin"};
    public nexProject mCloneProject;
    public Context mContext;
    public VideoEditor.EnocdeStateInterface mCurrentEnocdeStateInterface;
    public TextClip mEndClip;
    public nexEngine mEngin;
    public volatile ExportParams mExportParams;
    public boolean mIsAudioFadeOut;
    public boolean mIsDestroy;
    public nexClip mMainVideoClip;
    public nexProject mNexProject;
    public TextClip mOpenClip;
    public VideoEditor.OnCompletedListener mPauseOnCompletedListener;
    public float mRatio;
    public VideoEditor.TrimStateInterface mTrimStateInterface;
    public int mVideoBitrate;
    public int mVideoFrames;
    public int mVideoHeight;
    public String mVideoPath;
    public int mVideoRotation;
    public int mVideoWidth;
    public VideoEditor.OnCompletedListener seekOnCompletedListener;
    public int mEngineState = -1;
    public int mSeekTarget = -1;
    public int mThumbnailPickCursor = 0;
    public Handler mHandler = new Handler(Looper.getMainLooper());
    public List<VideoThumbnail> preLoadVideoThumbnails = new ArrayList();
    public boolean mHasUsingAutoTrim = false;
    public boolean mHasApplyedSmartEffect = false;
    public boolean setTimeSuccess = false;
    public HashMap<String, Change> mTempEditValue = new HashMap<>();
    public HashMap<String, Change> mAppliedEditValue = new HashMap<>();
    public HashMap<String, Change> mSavedEditValue = new HashMap<>();
    public boolean mIsFirst = true;
    public boolean mIsTouchSeekBar = false;

    /* loaded from: classes2.dex */
    public interface Change {
        void applyChange();
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onCheckDirectExport(int i) {
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onClipInfoDone() {
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onFastPreviewStartDone(int i, int i2, int i3) {
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onFastPreviewStopDone(int i) {
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onFastPreviewTimeDone(int i) {
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onPreviewPeakMeter(int i, int i2) {
    }

    public NexVideoEditor(Context context) {
        this.mContext = context;
    }

    public void keepScreenOn(boolean z) {
        DisplayWrapper displayWrapper = this.mDisplayWrapper;
        if (displayWrapper != null) {
            displayWrapper.setKeepScreenOn(z);
        }
    }

    public boolean checkIDRSupport() {
        long currentTimeMillis = System.currentTimeMillis();
        nexClip nexclip = this.mMainVideoClip;
        boolean z = nexclip != null && nexclip.getSeekPointCount() > 0 && this.mMainVideoClip.getSeekPointInterval() <= 4000;
        DefaultLogger.d("NexVideoEditor", "get interval costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return z;
    }

    public final void loadMainThumbnail(String str) {
        Glide.with(this.mDisplayWrapper).mo985asBitmap().mo963load(UriUtils.toSafeString(Uri.fromFile(new File(str)))).mo959listener(new AnonymousClass1()).submit();
    }

    /* renamed from: com.miui.gallery.video.editor.NexVideoEditor$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestListener<Bitmap> {
        public static /* synthetic */ void $r8$lambda$Oixg1BjoUc2ErfYyfdffZSTjeVc(AnonymousClass1 anonymousClass1, Bitmap bitmap) {
            anonymousClass1.lambda$onResourceReady$0(bitmap);
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
            return false;
        }

        public AnonymousClass1() {
            NexVideoEditor.this = r1;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(final Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
            if (bitmap != null) {
                NexVideoEditor.this.mDisplayWrapper.post(new Runnable() { // from class: com.miui.gallery.video.editor.NexVideoEditor$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        NexVideoEditor.AnonymousClass1.$r8$lambda$Oixg1BjoUc2ErfYyfdffZSTjeVc(NexVideoEditor.AnonymousClass1.this, bitmap);
                    }
                });
                return true;
            }
            return true;
        }

        public /* synthetic */ void lambda$onResourceReady$0(Bitmap bitmap) {
            NexVideoEditor.this.mDisplayWrapper.showThumbnail(bitmap);
            if (NexVideoEditor.this.preLoadVideoThumbnails.size() == 0) {
                if (NexVideoEditor.this.mVideoRotation % nexClip.kClip_Rotate_180 == 0) {
                    NexVideoEditor.this.preLoadVideoThumbnails.add(new VideoThumbnail(0, 0, 0, Bitmap.createScaledBitmap(bitmap, SyslogConstants.LOG_LOCAL4, 90, false)));
                } else {
                    NexVideoEditor.this.preLoadVideoThumbnails.add(new VideoThumbnail(0, 0, 0, Bitmap.createScaledBitmap(bitmap, 90, SyslogConstants.LOG_LOCAL4, false)));
                }
                NexVideoEditor.this.notifyThumbnailChanged();
            }
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void load(final String str, final VideoEditor.OnCompletedListener onCompletedListener) {
        this.mVideoPath = str;
        loadMainThumbnail(str);
        final long currentTimeMillis = System.currentTimeMillis();
        setEngineState(100);
        NexEngine.init(this.mContext, 1, new Runnable() { // from class: com.miui.gallery.video.editor.NexVideoEditor.2
            {
                NexVideoEditor.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                long currentTimeMillis2 = System.currentTimeMillis();
                DefaultLogger.d("NexVideoEditor", "sdk init using :" + (currentTimeMillis2 - currentTimeMillis) + d.H);
                NexVideoEditor nexVideoEditor = NexVideoEditor.this;
                nexVideoEditor.mIsInit = true;
                try {
                    nexVideoEditor.mEngin = NexEngine.getEngine(nexVideoEditor.mContext);
                    NexVideoEditor.this.mEngin.setEventHandler(NexVideoEditor.this);
                    DefaultLogger.d("NexVideoEditor", "engine create time :" + (System.currentTimeMillis() - currentTimeMillis2) + d.H);
                    new AsyncTask<Void, Void, Boolean>() { // from class: com.miui.gallery.video.editor.NexVideoEditor.2.1
                        {
                            AnonymousClass2.this = this;
                        }

                        @Override // android.os.AsyncTask
                        public Boolean doInBackground(Void... voidArr) {
                            try {
                                long currentTimeMillis3 = System.currentTimeMillis();
                                AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                                NexVideoEditor.this.loadVideo(str);
                                NexVideoEditor.this.handleHardwareSupport();
                                NexVideoEditor.this.configEngine();
                                NexVideoEditor.this.loadProject();
                                NexVideoEditor.this.initExportParams();
                                DefaultLogger.d("NexVideoEditor", "load project using :" + (System.currentTimeMillis() - currentTimeMillis3) + d.H);
                                return Boolean.TRUE;
                            } catch (Exception e) {
                                DefaultLogger.e("NexVideoEditor", "doInBackground Exception : ", e);
                                return Boolean.FALSE;
                            }
                        }

                        @Override // android.os.AsyncTask
                        public void onPostExecute(Boolean bool) {
                            super.onPostExecute((AnonymousClass1) bool);
                            if (!NexVideoEditor.this.mIsDestroy) {
                                NexVideoEditor.this.mDisplayWrapper.getDisplayView().requestLayout();
                                NexVideoEditor nexVideoEditor2 = NexVideoEditor.this;
                                nexVideoEditor2.mDisplayWrapper.setAspectRatio(nexVideoEditor2.mRatio);
                                if (!bool.booleanValue()) {
                                    NexVideoEditor.this.setEngineState(500);
                                    return;
                                }
                                AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                                NexVideoEditor.this.loadThumbnail(onCompletedListener);
                                NexVideoEditor.this.mEngin.updateScreenMode();
                            }
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                } catch (Exception e) {
                    DefaultLogger.e("NexVideoEditor", "sdk init error: %s .", e.getMessage());
                    NexVideoEditor.this.setEngineState(-500);
                    StringBuilder sb = new StringBuilder();
                    sb.append("build manufacturer :");
                    String str2 = Build.MANUFACTURER;
                    sb.append(str2);
                    DefaultLogger.e("NexVideoEditor", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("build model :");
                    String str3 = Build.MODEL;
                    sb2.append(str3);
                    DefaultLogger.e("NexVideoEditor", sb2.toString());
                    HashMap hashMap = new HashMap();
                    hashMap.put("manufacturer", str2);
                    hashMap.put("model", str3);
                    SamplingStatHelper.recordCountEvent("video_editor", "video_editor_sdk_init_error", hashMap);
                }
            }
        });
    }

    public final void loadThumbnail(final VideoEditor.OnCompletedListener onCompletedListener) {
        int i = this.mVideoRotation;
        int i2 = i % nexClip.kClip_Rotate_180 == 0 ? 160 : 90;
        int i3 = i % nexClip.kClip_Rotate_180 == 0 ? 90 : 160;
        nexClip nexclip = this.mMainVideoClip;
        nexclip.getVideoClipDetailThumbnails(i2, i3, 0, nexclip.getTotalTime(), 50, this.mVideoRotation, new NexOnGetVideoClipDetailThumbnailsListener(new VideoEditor.OnGetVideoThumbnailListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.3
            {
                NexVideoEditor.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnGetVideoThumbnailListener
            public void onGetVideoThumbnailCompleted(List<VideoThumbnail> list) {
                if (!NexVideoEditor.this.mIsDestroy) {
                    if (list != null && list.size() > 0) {
                        NexVideoEditor.this.preLoadVideoThumbnails.clear();
                        NexVideoEditor.this.preLoadVideoThumbnails.addAll(list);
                        NexVideoEditor.this.notifyThumbnailChanged();
                    }
                    NexVideoEditor nexVideoEditor = NexVideoEditor.this;
                    nexVideoEditor.setDisplayView(nexVideoEditor.mDisplayWrapper.getDisplayView());
                    NexVideoEditor.this.setEngineState(0);
                    onCompletedListener.onCompleted();
                }
            }
        }));
    }

    public final void loadProject() {
        nexProject createProject = createProject(this.mMainVideoClip);
        this.mNexProject = createProject;
        createProject.getLastPrimaryClip().setRotateDegree(this.mMainVideoClip.getRotateInMeta());
        this.mNexProject.getLastPrimaryClip().getCrop().randomizeStartEndPosition(false, nexCrop.CropMode.FIT);
        this.mEngin.setProject(this.mNexProject);
        this.mEngin.updateProject();
    }

    public final void configEngine() {
        nexAspectProfile nexaspectprofile;
        if (this.mVideoRotation % nexClip.kClip_Rotate_180 == 0) {
            nexaspectprofile = new nexAspectProfile(this.mVideoWidth, this.mVideoHeight);
        } else {
            nexaspectprofile = new nexAspectProfile(this.mVideoHeight, this.mVideoWidth);
        }
        nexApplicationConfig.setAspectProfile(nexaspectprofile);
    }

    public final void loadVideo(String str) throws VideoEditor.NotSupportVideoException {
        nexClip supportedClip = nexClip.getSupportedClip(str, true);
        if (supportedClip != null) {
            this.mVideoWidth = supportedClip.getWidth();
            this.mVideoHeight = supportedClip.getHeight();
            this.mVideoRotation = supportedClip.getRotateInMeta();
            this.mVideoBitrate = supportedClip.getVideoBitrate();
            this.mVideoFrames = supportedClip.getFramesPerSecond();
            this.mMainVideoClip = supportedClip;
            if (this.mVideoRotation % nexClip.kClip_Rotate_180 == 0) {
                this.mRatio = this.mVideoWidth / this.mVideoHeight;
            } else {
                this.mRatio = this.mVideoHeight / this.mVideoWidth;
            }
            if (this.mExportParams == null) {
                return;
            }
            this.mExportParams.setFps(this.mVideoFrames).setBitrate(this.mVideoBitrate);
            return;
        }
        throw new VideoEditor.NotSupportVideoException();
    }

    public final boolean isHighFrames() {
        return this.mVideoFrames >= 100;
    }

    public final void handleHardwareSupport() throws VideoEditor.NotSupportVideoException {
        MediaCodecInfo[] codecInfos;
        MediaCodecInfo.VideoCapabilities videoCapabilities;
        if (Arrays.asList(sHardwareSupportWhiteList).contains(Build.DEVICE) && Build.VERSION.SDK_INT >= 21) {
            int width = this.mVideoRotation % nexClip.kClip_Rotate_180 == 0 ? this.mMainVideoClip.getWidth() : this.mMainVideoClip.getHeight();
            int height = this.mVideoRotation % nexClip.kClip_Rotate_180 == 0 ? this.mMainVideoClip.getHeight() : this.mMainVideoClip.getWidth();
            String videoCodecType = this.mMainVideoClip.getVideoCodecType();
            for (MediaCodecInfo mediaCodecInfo : new MediaCodecList(0).getCodecInfos()) {
                if (mediaCodecInfo != null && !mediaCodecInfo.isEncoder()) {
                    String[] supportedTypes = mediaCodecInfo.getSupportedTypes();
                    for (int i = 0; supportedTypes != null && i < supportedTypes.length; i++) {
                        if (!TextUtils.isEmpty(supportedTypes[i]) && supportedTypes[i].contains("video/") && ((supportedTypes[i].contains(videoCodecType) || ((videoCodecType.equalsIgnoreCase("video/h264") && supportedTypes[i].equalsIgnoreCase("video/avc")) || (videoCodecType.equalsIgnoreCase("video/avc") && supportedTypes[i].equalsIgnoreCase("video/h264")))) && (videoCapabilities = mediaCodecInfo.getCapabilitiesForType(supportedTypes[i]).getVideoCapabilities()) != null && videoCapabilities.areSizeAndRateSupported(width, height, this.mMainVideoClip.getFramesPerSecond()))) {
                            DefaultLogger.d("NexVideoEditor", String.format("the MediaCodecInfo name is: %s, and the  phone is supported to edit:  %s", mediaCodecInfo.getName(), "true"));
                            return;
                        }
                    }
                }
            }
            DefaultLogger.e("NexVideoEditor", "the device can not support to edit the video.");
            throw new VideoEditor.NotSupportVideoException();
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void saveEditState() {
        this.mSavedEditValue.clear();
        this.mSavedEditValue.putAll(this.mAppliedEditValue);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void restoreEditState() {
        if (haveChange("filter")) {
            FilterEffect filterEffect = (FilterEffect) this.mSavedEditValue.get("filter");
            if (filterEffect == null) {
                filterEffect = new FilterEffect(null);
            }
            this.mTempEditValue.put("filter", filterEffect);
        }
        if (haveChange("bg_audio")) {
            BGMEdit bGMEdit = (BGMEdit) this.mSavedEditValue.get("bg_audio");
            if (bGMEdit == null) {
                bGMEdit = new BGMEdit(null);
            }
            this.mTempEditValue.put("bg_audio", bGMEdit);
        }
        if (haveChange("source_audio")) {
            SourceAudioChange sourceAudioChange = (SourceAudioChange) this.mSavedEditValue.get("source_audio");
            if (sourceAudioChange == null) {
                sourceAudioChange = new SourceAudioChange(true);
            }
            this.mTempEditValue.put("source_audio", sourceAudioChange);
        }
        if (haveChange("edit_type_water_mark")) {
            WaterMark waterMark = (WaterMark) this.mSavedEditValue.get("edit_type_water_mark");
            if (waterMark == null) {
                waterMark = new WaterMark();
            }
            this.mTempEditValue.put("edit_type_water_mark", waterMark);
        }
        if (haveChange("edit_type_auto_water_mark_text")) {
            TextClip textClip = (TextClip) this.mSavedEditValue.get("edit_type_auto_water_mark_text");
            if (textClip == null) {
                textClip = new TextClip(this, null);
            }
            this.mTempEditValue.put("edit_type_auto_water_mark_text", textClip);
        }
        if (haveChange("edit_type_adjust")) {
            VideoEditorAdjust videoEditorAdjust = (VideoEditorAdjust) this.mSavedEditValue.get("edit_type_adjust");
            if (videoEditorAdjust == null) {
                videoEditorAdjust = new VideoEditorAdjust(false);
            }
            this.mTempEditValue.put("edit_type_adjust", videoEditorAdjust);
        }
        if (haveChange("edit_type_smart_effect_template")) {
            SmartEffectTemplate smartEffectTemplate = (SmartEffectTemplate) this.mSavedEditValue.get("edit_type_smart_effect_template");
            if (smartEffectTemplate == null) {
                smartEffectTemplate = new SmartEffectTemplate(null);
            }
            this.mTempEditValue.put("edit_type_smart_effect_template", smartEffectTemplate);
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public int getVideoStartTime() {
        nexClip nexclip = this.mMainVideoClip;
        if (nexclip != null) {
            return nexclip.getProjectStartTime();
        }
        return 0;
    }

    public final void setTrimInfo(TrimInfo trimInfo) {
        this.mTempEditValue.put("trim", trimInfo);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean isSupportAutoTrim() {
        return this.mMainVideoClip != null && getProjectTotalTime() > 30000 && !this.mHasApplyedSmartEffect;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void toThirdEditor(Context context) {
        int i = this.mEngineState;
        if (i == 1 || i == 2 || i == 0) {
            try {
                try {
                    nexProject nexproject = this.mNexProject;
                    if (nexproject == null) {
                        return;
                    }
                    try {
                        Intent makeKineMasterIntent = nexproject.makeKineMasterIntent();
                        if (makeKineMasterIntent == null) {
                            return;
                        }
                        context.startActivity(makeKineMasterIntent);
                        SamplingStatHelper.recordCountEvent("video_editor", "video_editor_to_3rd_exitor");
                    } catch (Exception unused) {
                    }
                } catch (ActivityNotFoundException unused2) {
                    context.startActivity(IntentUtil.makeMarketIntent("com.nexstreaming.app.kinemasterfree", true));
                    SamplingStatHelper.recordCountEvent("video_editor", "video_editor_to_market");
                }
            } catch (ActivityNotFoundException unused3) {
                DefaultLogger.e("NexVideoEditor", "no market found !!!!");
            }
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void cancelExport(final VideoEditor.OnCompletedListener onCompletedListener) {
        if (this.mEngineState == 105) {
            this.mEngin.stop(new nexEngine.OnCompletionListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.5
                {
                    NexVideoEditor.this = this;
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngine.OnCompletionListener
                public void onComplete(int i) {
                    NexVideoEditor.this.setEngineState(0);
                    VideoEditor.OnCompletedListener onCompletedListener2 = onCompletedListener;
                    if (onCompletedListener2 != null) {
                        onCompletedListener2.onCompleted();
                    }
                }
            });
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public String getVideoPath() {
        return TextUtils.isEmpty(this.mVideoPath) ? "" : this.mVideoPath;
    }

    public final boolean haveChange(String str) {
        Change change = this.mSavedEditValue.get(str);
        Change change2 = this.mAppliedEditValue.get(str);
        if (change == null && change2 == null) {
            return false;
        }
        return change == null || !change.equals(change2);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void release() {
        destroy();
        NexEngine.releaseEngine();
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void resume() {
        if (this.mEngineState == 2) {
            if (this.mEngin == null) {
                return;
            }
            setEngineState(107);
            this.mEngin.resume();
            playVideo(this.mEngin, true);
            return;
        }
        DefaultLogger.e("NexVideoEditor", "resume is not allowed at EngineState :" + this.mEngineState);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void play() {
        if (this.mEngineState == 0) {
            if (this.mEngin == null) {
                return;
            }
            setEngineState(103);
            playVideo(this.mEngin, false);
            return;
        }
        DefaultLogger.d("NexVideoEditor", "play is not allowed at EngineState :" + this.mEngineState);
    }

    public final void playVideo(nexEngine nexengine, boolean z) {
        nexProject project;
        if (nexengine == null || (project = nexengine.getProject()) == null) {
            return;
        }
        if (this.mTempEditValue.get("edit_type_smart_effect_template") != null || this.mAppliedEditValue.get("edit_type_smart_effect_template") != null) {
            this.mIsAudioFadeOut = true;
        }
        if (!this.mIsFirst && !this.mIsAudioFadeOut) {
            project.setProjectAudioFadeOutTime(0);
            this.mEngin.updateProject();
            this.mIsAudioFadeOut = true;
        } else {
            this.mIsFirst = false;
        }
        if (z) {
            this.mEngin.resume();
        } else {
            this.mEngin.play();
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void pause() {
        pause(null);
    }

    public final void pause(VideoEditor.OnCompletedListener onCompletedListener) {
        if (this.mEngineState == 1 && this.mEngin != null) {
            this.mPauseOnCompletedListener = onCompletedListener;
            setEngineState(102);
            this.mEngin.pause();
            return;
        }
        DefaultLogger.e("NexVideoEditor", "pause is not allowed at EngineState :" + this.mEngineState);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void seek(final int i, VideoEditor.OnCompletedListener onCompletedListener) {
        this.seekOnCompletedListener = onCompletedListener;
        int i2 = this.mEngineState;
        if (i2 != 0) {
            if (i2 == 1) {
                pause(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.7
                    {
                        NexVideoEditor.this = this;
                    }

                    @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                    public void onCompleted() {
                        NexVideoEditor.this.doSeek(i);
                    }
                });
                return;
            } else if (i2 != 2 && i2 != 104) {
                DefaultLogger.e("NexVideoEditor", "seek is not allowed at EngineState :" + this.mEngineState);
                return;
            }
        }
        doSeek(i);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean isSourceAudioEnable() {
        nexClip nexclip = this.mMainVideoClip;
        return nexclip == null || nexclip.getClipVolume() == 100;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean resetProject(VideoEditor.OnCompletedListener onCompletedListener) {
        return addChange(new Change() { // from class: com.miui.gallery.video.editor.NexVideoEditor.8
            {
                NexVideoEditor.this = this;
            }

            @Override // com.miui.gallery.video.editor.NexVideoEditor.Change
            public void applyChange() {
                NexVideoEditor nexVideoEditor = NexVideoEditor.this;
                nexVideoEditor.mMainVideoClip = nexClip.dup(nexVideoEditor.mMainVideoClip);
                NexVideoEditor.this.mMainVideoClip.setRotateDegree(NexVideoEditor.this.mVideoRotation);
                NexVideoEditor.this.mNexProject = new nexProject();
                NexVideoEditor.this.mNexProject.add(NexVideoEditor.this.mMainVideoClip);
                NexVideoEditor.this.mEngin.setProject(NexVideoEditor.this.mNexProject);
                NexVideoEditor.this.mAppliedEditValue.clear();
                NexVideoEditor.this.mOpenClip = null;
                NexVideoEditor.this.mEndClip = null;
            }
        }, onCompletedListener);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public int getVideoFrames() {
        return this.mVideoFrames;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public float getCurrentDisplayRatio() {
        return this.mRatio;
    }

    public final void doSeek(int i) {
        int i2 = this.mEngineState;
        if (i2 == 0 || i2 == 2) {
            this.setTimeSuccess = false;
            setEngineState(104);
            this.mEngin.seek(i);
        } else if (i2 == 104) {
            this.mSeekTarget = i;
            updataSeek();
        } else {
            DefaultLogger.e("NexVideoEditor", "setFilter is not allowed at EngineState :" + this.mEngineState);
        }
    }

    public final void updataSeek() {
        if (this.mEngineState == 104) {
            if (this.mSeekTarget != -1) {
                if (Math.abs(this.mEngin.getCurrentPlayTimeTime() - this.mSeekTarget) > 150) {
                    this.mEngin.seek(this.mSeekTarget);
                    DefaultLogger.d("NexVideoEditor", "SEEK IDR progress :" + this.mSeekTarget);
                } else {
                    this.mEngin.seek(this.mSeekTarget);
                    DefaultLogger.d("NexVideoEditor", "progress :" + this.mSeekTarget);
                }
                this.mSeekTarget = -1;
                return;
            }
            if (this.setTimeSuccess) {
                setEngineState(2);
            } else {
                setEngineState(0);
            }
            VideoEditor.OnCompletedListener onCompletedListener = this.seekOnCompletedListener;
            if (onCompletedListener == null) {
                return;
            }
            onCompletedListener.onCompleted();
        }
    }

    public final void setEngineState(int i) {
        if (i == -500) {
            setState(-500);
        } else if (i == 500) {
            setState(500);
            keepScreenOn(false);
        } else if (i == 0) {
            setState(0);
            keepScreenOn(false);
        } else if (i == 1) {
            setState(1);
            keepScreenOn(true);
            this.mDisplayWrapper.hideThumbnail();
        } else if (i == 2) {
            setState(2);
            keepScreenOn(false);
        } else {
            switch (i) {
                case 100:
                case 101:
                case 102:
                case 103:
                case 107:
                    setState(200);
                    break;
                case 104:
                    setState(3);
                    this.mDisplayWrapper.hideThumbnail();
                    break;
                case 105:
                case 106:
                    keepScreenOn(true);
                    setState(200);
                    break;
            }
        }
        this.mEngineState = i;
    }

    public void setDisplayView(View view) {
        this.mEngin.setView((nexEngineView) view);
    }

    public final nexColorEffect finEffect(String str) {
        if (str != null) {
            for (nexColorEffect nexcoloreffect : nexColorEffect.getPresetList()) {
                if (str.equals(nexcoloreffect.getPresetName())) {
                    return nexcoloreffect;
                }
            }
            return null;
        }
        return null;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean apply(VideoEditor.OnCompletedListener onCompletedListener) {
        return applyAsync(onCompletedListener);
    }

    public boolean applyAsync(final VideoEditor.OnCompletedListener onCompletedListener) {
        if (this.mTempEditValue.size() == 0) {
            onCompletedListener.onCompleted();
            return true;
        }
        final Change[] changeArr = new Change[this.mTempEditValue.size()];
        this.mTempEditValue.values().toArray(changeArr);
        this.mTempEditValue.clear();
        int i = this.mEngineState;
        if (i != 0) {
            if (i == 1) {
                setEngineState(101);
                this.mEngin.stop(new nexEngine.OnCompletionListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.9
                    {
                        NexVideoEditor.this = this;
                    }

                    @Override // com.nexstreaming.nexeditorsdk.nexEngine.OnCompletionListener
                    public void onComplete(int i2) {
                        NexVideoEditor.this.setEngineState(100);
                        new ApplyTask(onCompletedListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, changeArr);
                    }
                });
                return true;
            } else if (i != 2) {
                return false;
            }
        }
        setEngineState(100);
        new ApplyTask(onCompletedListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, changeArr);
        return true;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public Bitmap pickThumbnail(int i) {
        this.mThumbnailPickCursor = 0;
        return pickThumbnailSerial(i);
    }

    public Bitmap pickThumbnailSerial(int i) {
        List<VideoThumbnail> list = this.preLoadVideoThumbnails;
        Bitmap bitmap = null;
        if (list != null && list.size() > 0) {
            int size = this.preLoadVideoThumbnails.size();
            int i2 = Integer.MAX_VALUE;
            for (int i3 = this.mThumbnailPickCursor; i3 < size; i3++) {
                VideoThumbnail videoThumbnail = this.preLoadVideoThumbnails.get(i3);
                if (Math.abs(videoThumbnail.getIntrinsicTime() - i) >= i2) {
                    break;
                }
                i2 = Math.abs(videoThumbnail.getIntrinsicTime() - i);
                this.mThumbnailPickCursor = i3;
                bitmap = videoThumbnail.getThumbnail();
            }
        }
        return bitmap;
    }

    /* loaded from: classes2.dex */
    public class ApplyTask extends AsyncTask<Change, Void, Boolean> {
        public VideoEditor.OnCompletedListener mOnCompletedListener;

        public ApplyTask(VideoEditor.OnCompletedListener onCompletedListener) {
            NexVideoEditor.this = r1;
            this.mOnCompletedListener = onCompletedListener;
        }

        @Override // android.os.AsyncTask
        public Boolean doInBackground(Change... changeArr) {
            if (changeArr != null) {
                for (Change change : changeArr) {
                    change.applyChange();
                }
            }
            NexVideoEditor.this.mEngin.updateProject();
            return Boolean.TRUE;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            super.onPostExecute((ApplyTask) bool);
            NexVideoEditor.this.mTempEditValue.clear();
            NexVideoEditor.this.setEngineState(0);
            VideoEditor.OnCompletedListener onCompletedListener = this.mOnCompletedListener;
            if (onCompletedListener != null) {
                onCompletedListener.onCompleted();
                this.mOnCompletedListener = null;
            }
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void setBackgroundMusic(String str) {
        this.mTempEditValue.put("bg_audio", new BGMEdit(str));
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void setFilter(Filter filter) {
        this.mTempEditValue.put("filter", new FilterEffect(filter));
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void setSourceAudioEnable(Boolean bool) {
        this.mTempEditValue.put("source_audio", new SourceAudioChange(bool.booleanValue()));
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean setWarterMark(int i, String str) {
        this.mTempEditValue.put("edit_type_water_mark", new WaterMark(i, str));
        return false;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean setAutoWaterMark(String str, int i) {
        this.mTempEditValue.put("edit_type_auto_water_mark_text", new TextClip(this, str, i, null));
        return false;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void setTrimInfo(int i, int i2) {
        setTrimInfo(new TrimInfo(i, i2));
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void autoTrim(int i, VideoEditor.TrimStateInterface trimStateInterface) {
        if (this.mEngineState != 0 || !isSupportAutoTrim() || i >= this.mMainVideoClip.getTotalTime()) {
            return;
        }
        this.mTrimStateInterface = trimStateInterface;
        setEngineState(106);
        this.mEngin.autoTrim(this.mMainVideoClip.getPath(), true, i / 5, 5, 0, new AnonymousClass10(i));
        VideoEditor.TrimStateInterface trimStateInterface2 = this.mTrimStateInterface;
        if (trimStateInterface2 == null) {
            return;
        }
        trimStateInterface2.onTrimStart();
    }

    /* renamed from: com.miui.gallery.video.editor.NexVideoEditor$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 extends nexEngine.OnAutoTrimResultListener {
        public final /* synthetic */ int val$time;

        public AnonymousClass10(int i) {
            NexVideoEditor.this = r1;
            this.val$time = i;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexEngine.OnAutoTrimResultListener
        public void onAutoTrimResult(int i, int[] iArr) {
            int min;
            if (iArr == null || iArr.length == 0) {
                return;
            }
            NexVideoEditor.this.mNexProject.allClear(true);
            int totalTime = NexVideoEditor.this.mMainVideoClip.getTotalTime();
            for (int i2 = 0; i2 < iArr.length; i2++) {
                nexClip dup = nexClip.dup(NexVideoEditor.this.mMainVideoClip);
                NexVideoEditor.this.mNexProject.add(dup);
                int i3 = iArr[i2];
                if (i3 >= totalTime) {
                    min = iArr[i2] + (this.val$time / 5);
                } else {
                    min = Math.min(iArr[i2] + (this.val$time / 5), totalTime);
                }
                dup.getVideoClipEdit().setTrim(i3, min);
                dup.setRotateDegree(NexVideoEditor.this.mVideoRotation);
                NexVideoEditor.this.mEngin.setProject(NexVideoEditor.this.mNexProject);
            }
            final String generateTempOutputFilePath = FileHelper.generateTempOutputFilePath(NexVideoEditor.this.mMainVideoClip.getPath());
            if (NexVideoEditor.this.mTrimStateInterface != null) {
                NexVideoEditor.this.mTrimStateInterface.onTrimEnd(generateTempOutputFilePath);
            }
            NexVideoEditor.this.setEngineState(0);
            NexVideoEditor.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.video.editor.NexVideoEditor.10.1
                {
                    AnonymousClass10.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    NexVideoEditor.this.export(generateTempOutputFilePath, new VideoEditor.EnocdeStateInterface() { // from class: com.miui.gallery.video.editor.NexVideoEditor.10.1.1
                        {
                            AnonymousClass1.this = this;
                        }

                        @Override // com.miui.gallery.video.editor.VideoEditor.EnocdeStateInterface
                        public void onEncodeStart() {
                            if (NexVideoEditor.this.mTrimStateInterface != null) {
                                NexVideoEditor.this.mTrimStateInterface.onEncodeStart();
                            }
                        }

                        @Override // com.miui.gallery.video.editor.VideoEditor.EnocdeStateInterface
                        public void onEncodeProgress(int i4) {
                            if (NexVideoEditor.this.mTrimStateInterface != null) {
                                NexVideoEditor.this.mTrimStateInterface.onEncodeProgress(i4);
                            }
                        }

                        @Override // com.miui.gallery.video.editor.VideoEditor.EnocdeStateInterface
                        public void onEncodeEnd(boolean z, int i4, int i5) {
                            if (z) {
                                NexVideoEditor.this.mHasUsingAutoTrim = true;
                            }
                            if (NexVideoEditor.this.mTrimStateInterface != null) {
                                NexVideoEditor.this.mTrimStateInterface.onEncodeEnd(z, i4, i5);
                            }
                        }
                    });
                }
            });
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void export(final String str, final VideoEditor.EnocdeStateInterface enocdeStateInterface) {
        DefaultLogger.d("NexVideoEditor", "export engine state: %d ", Integer.valueOf(this.mEngineState));
        int i = this.mEngineState;
        if (i != 0) {
            if (i == 1) {
                setEngineState(101);
                this.mEngin.stop(new nexEngine.OnCompletionListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.11
                    {
                        NexVideoEditor.this = this;
                    }

                    @Override // com.nexstreaming.nexeditorsdk.nexEngine.OnCompletionListener
                    public void onComplete(int i2) {
                        NexVideoEditor.this.setEngineState(0);
                        NexVideoEditor.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.video.editor.NexVideoEditor.11.1
                            {
                                AnonymousClass11.this = this;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass11 anonymousClass11 = AnonymousClass11.this;
                                NexVideoEditor.this.export(str, enocdeStateInterface);
                            }
                        });
                    }
                });
                return;
            } else if (i != 2) {
                DefaultLogger.e("NexVideoEditor", "export is not allowed at EngineState :" + this.mEngineState);
                enocdeStateInterface.onEncodeEnd(false, -1, -1);
                return;
            }
        }
        setEngineState(105);
        this.mCurrentEnocdeStateInterface = enocdeStateInterface;
        export(str);
        enocdeStateInterface.onEncodeStart();
    }

    public final void initExportParams() {
        if (this.mExportParams == null) {
            this.mExportParams = new ExportParams();
            this.mExportParams.setWidth(this.mVideoRotation % nexClip.kClip_Rotate_180 == 0 ? this.mVideoWidth : this.mVideoHeight).setHeight(this.mVideoRotation % nexClip.kClip_Rotate_180 == 0 ? this.mVideoHeight : this.mVideoWidth);
            nexProject project = this.mEngin.getProject();
            int i = 0;
            if (project == null) {
                DefaultLogger.d("NexVideoEditor", "init export params  is invalid.");
                this.mExportParams.mIsValid = false;
                return;
            }
            DefaultLogger.d("NexVideoEditor", "project total clip count : %d", Integer.valueOf(project.getTotalClipCount(true)));
            while (true) {
                if (i < project.getTotalClipCount(true)) {
                    nexClip clip = project.getClip(i, true);
                    if (clip != null && clip.getClipType() == 4) {
                        this.mExportParams.setProfileValue(getProfileValue(clip)).setLevelValue(getLevelValue(clip)).setCodecValue(nexEngine.ExportCodec_AVC);
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            this.mExportParams.setFps(this.mVideoFrames).setBitrate(this.mVideoBitrate).setValid(true);
            DefaultLogger.d("NexVideoEditor", "init export params profileValue: %s, levelValue: %s .", Integer.valueOf(this.mExportParams.mProfileValue), Integer.valueOf(this.mExportParams.mLevelValue));
        }
        checkExportParams();
    }

    public final void export(String str) {
        if (this.mExportParams != null && this.mExportParams.isValid() && !isHighFrames()) {
            this.mEngin.exportNoException(str, this.mExportParams.mWidth, this.mExportParams.mHeight, this.mExportParams.mBitrate, Long.MAX_VALUE, 0, 0, this.mExportParams.mProfileValue, this.mExportParams.mLevelValue, this.mExportParams.mFps, this.mExportParams.mCodecValue);
            return;
        }
        nexEngine nexengine = this.mEngin;
        int i = this.mVideoRotation;
        nexengine.export(str, i % nexClip.kClip_Rotate_180 == 0 ? this.mVideoWidth : this.mVideoHeight, i % nexClip.kClip_Rotate_180 == 0 ? this.mVideoHeight : this.mVideoWidth, this.mVideoBitrate, Long.MAX_VALUE, 0);
    }

    public final int getProfileValue(nexClip nexclip) {
        int aVCProfile = nexclip.getAVCProfile();
        if (aVCProfile != 66) {
            if (aVCProfile == 77) {
                return 2;
            }
            return (aVCProfile == 100 || aVCProfile == 110 || aVCProfile == 122 || aVCProfile == 244) ? 4 : 0;
        }
        return 1;
    }

    public final void checkExportParams() {
        MediaCodecInfo[] codecInfos;
        MediaCodecInfo.CodecProfileLevel[] codecProfileLevelArr;
        if (this.mExportParams != null && Build.VERSION.SDK_INT >= 21) {
            int i = 0;
            int i2 = 0;
            for (MediaCodecInfo mediaCodecInfo : new MediaCodecList(0).getCodecInfos()) {
                if (mediaCodecInfo != null) {
                    String[] supportedTypes = mediaCodecInfo.getSupportedTypes();
                    for (int i3 = 0; supportedTypes != null && i3 < supportedTypes.length; i3++) {
                        MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(supportedTypes[i3]);
                        if (mediaCodecInfo.getName().toLowerCase().contains("avc") && mediaCodecInfo.getName().toLowerCase().contains("encoder")) {
                            for (MediaCodecInfo.CodecProfileLevel codecProfileLevel : capabilitiesForType.profileLevels) {
                                if (codecProfileLevel != null) {
                                    i = Math.max(i, codecProfileLevel.profile);
                                    i2 = Math.max(i, codecProfileLevel.level);
                                }
                            }
                        }
                    }
                }
            }
            DefaultLogger.d("NexVideoEditor", "checkExportParams: maxProfile = %d , maxLevel = %d ", Integer.valueOf(i), Integer.valueOf(i2));
            this.mExportParams.mProfileValue = Math.min(this.mExportParams.mProfileValue, i);
            this.mExportParams.mLevelValue = Math.min(this.mExportParams.mLevelValue, i2);
        }
    }

    public final int getLevelValue(nexClip nexclip) {
        switch (nexclip.getAVCLevel()) {
            case 20:
                return 32;
            case 21:
                return 64;
            case 22:
                return 128;
            case 30:
                return 256;
            case 31:
                return 512;
            case 32:
                return 1024;
            case 40:
                return 2048;
            case 41:
                return 4096;
            case 42:
                return 8192;
            case 50:
                return 16384;
            case 51:
                return 32768;
            case 52:
                return 65536;
            default:
                return 0;
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean hasEdit() {
        return this.mAppliedEditValue.size() > 0 || this.mHasUsingAutoTrim;
    }

    public void destroy() {
        nexEngine nexengine = this.mEngin;
        if (nexengine != null) {
            nexengine.setView((nexEngineView) null);
            this.mEngin.stop();
            nexApplicationConfig.releaseNativeEngine(this.mEngin);
            keepScreenOn(false);
        }
        this.mExportParams = null;
        this.mIsDestroy = true;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void startPreview() {
        startPreview(null);
    }

    public void startPreview(VideoEditor.OnCompletedListener onCompletedListener) {
        nexEngine nexengine;
        int i = this.mEngineState;
        if ((i == 0 || i == 2) && (nexengine = this.mEngin) != null) {
            int currentPlayTimeTime = nexengine.getCurrentPlayTimeTime();
            if (currentPlayTimeTime == 0) {
                currentPlayTimeTime = 100;
            }
            seek(currentPlayTimeTime, onCompletedListener);
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public int getProjectTotalTime() {
        nexProject project;
        nexEngine nexengine = this.mEngin;
        if (nexengine == null || (project = nexengine.getProject()) == null) {
            return 0;
        }
        return project.getTotalTime();
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public int getVideoTotalTime() {
        nexClip nexclip = this.mMainVideoClip;
        if (nexclip != null) {
            return nexclip.getTotalTime();
        }
        nexProject nexproject = this.mNexProject;
        if (nexproject == null) {
            return 0;
        }
        return nexproject.getTotalTime();
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean haveSavedEditState() {
        return !this.mSavedEditValue.isEmpty();
    }

    public final nexProject createProject(nexClip nexclip) {
        nexProject nexproject = this.mNexProject;
        if (nexproject != null) {
            nexproject.allClear(true);
            this.mNexProject = null;
        }
        if (nexclip != null) {
            nexclip.setRotateDegree(this.mVideoRotation);
            nexProject nexproject2 = new nexProject();
            nexproject2.add(nexclip);
            nexclip.getCrop().randomizeStartEndPosition(true, nexCrop.CropMode.FIT);
            return nexproject2;
        }
        return null;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onStateChange(int i, int i2) {
        if (i == 2 && i2 == 1 && this.mEngineState == 102) {
            setEngineState(2);
            VideoEditor.OnCompletedListener onCompletedListener = this.mPauseOnCompletedListener;
            if (onCompletedListener != null) {
                onCompletedListener.onCompleted();
                this.mPauseOnCompletedListener = null;
            }
        }
        DefaultLogger.d("NexVideoEditor", "nexEngineState : old state :" + i + " new state :" + i2);
    }

    public final boolean addChange(final Change change, final VideoEditor.OnCompletedListener onCompletedListener) {
        int i = this.mEngineState;
        if (i != 0) {
            if (i == 1) {
                setEngineState(101);
                nexEngine nexengine = this.mEngin;
                if (nexengine != null) {
                    nexengine.stop(new nexEngine.OnCompletionListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.12
                        {
                            NexVideoEditor.this = this;
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexEngine.OnCompletionListener
                        public void onComplete(int i2) {
                            change.applyChange();
                            if (NexVideoEditor.this.mEngin != null) {
                                NexVideoEditor.this.mEngin.updateProject();
                            }
                            NexVideoEditor.this.setEngineState(0);
                            VideoEditor.OnCompletedListener onCompletedListener2 = onCompletedListener;
                            if (onCompletedListener2 != null) {
                                onCompletedListener2.onCompleted();
                            }
                        }
                    });
                }
                return true;
            } else if (i != 2) {
                DefaultLogger.e("NexVideoEditor", "addChange is not allowed at EngineState :" + this.mEngineState);
                return false;
            }
        }
        change.applyChange();
        this.mEngin.updateProject();
        setEngineState(0);
        if (onCompletedListener != null) {
            onCompletedListener.onCompleted();
        }
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onTimeChange(int i) {
        if (this.mEngineState == 1) {
            onTimeChanged(i);
        }
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onSetTimeDone(int i) {
        this.setTimeSuccess = true;
        updataSeek();
        DefaultLogger.d("NexVideoEditor", "onSetTimeDone : " + i);
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onSetTimeFail(int i) {
        updataSeek();
        DefaultLogger.d("NexVideoEditor", "onSetTimeFail");
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onSetTimeIgnored() {
        updataSeek();
        DefaultLogger.d("NexVideoEditor", "onSetTimeIgnored");
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onEncodingDone(boolean z, int i) {
        VideoEditor.EnocdeStateInterface enocdeStateInterface = this.mCurrentEnocdeStateInterface;
        if (enocdeStateInterface != null) {
            enocdeStateInterface.onEncodeEnd(!z, i, 0);
        }
        setEngineState(0);
        DefaultLogger.i("NexVideoEditor", "onEncodingDone errorCode : %s", Integer.valueOf(i));
        if (this.mAppliedEditValue.size() > 0) {
            HashMap hashMap = new HashMap();
            for (Map.Entry<String, Change> entry : this.mAppliedEditValue.entrySet()) {
                String key = entry.getKey();
                if ("bg_audio".equals(key)) {
                    hashMap.put("effect", "usingBGM" + String.valueOf(((BGMEdit) entry.getValue()).mBGMUri));
                } else if ("filter".equals(key)) {
                    hashMap.put("effect", "usingFilter" + String.valueOf(((FilterEffect) entry.getValue()).mFilter.getFilterId()));
                } else if ("source_audio".equals(key)) {
                    hashMap.put("effect", "disable_source_audiotrue");
                } else if ("trim".equals(key)) {
                    hashMap.put("effect", "usingTRIMtrim");
                } else if ("edit_type_auto_water_mark_text".equals(key)) {
                    hashMap.put("effect", "usingTexttext");
                }
            }
            SamplingStatHelper.recordCountEvent("video_editor", "video_editor_using_effect", hashMap);
        }
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onPlayEnd() {
        if (this.mEngineState != 105) {
            setEngineState(0);
            seek(0, null);
            DefaultLogger.d("NexVideoEditor", "onPlayEnd");
        }
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onPlayFail(int i, int i2) {
        VideoEditor.EnocdeStateInterface enocdeStateInterface;
        DefaultLogger.e("NexVideoEditor", "onPlayFail : error code :" + i, "clipId" + i2);
        HashMap hashMap = new HashMap();
        hashMap.put("error", String.valueOf(i));
        hashMap.put("state", String.valueOf(this.mEngineState));
        SamplingStatHelper.recordCountEvent("video_editor", "video_editor_play_failed", hashMap);
        if (this.mEngineState == 105 && (enocdeStateInterface = this.mCurrentEnocdeStateInterface) != null) {
            enocdeStateInterface.onEncodeEnd(false, -1, -1);
        }
        setEngineState(0);
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onPlayStart() {
        if (this.mEngineState != 105) {
            setEngineState(1);
            DefaultLogger.d("NexVideoEditor", "onPlayStart");
        }
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onSeekStateChanged(boolean z) {
        DefaultLogger.e("NexVideoEditor", "onSeekStateChanged " + z);
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onEncodingProgress(int i) {
        VideoEditor.EnocdeStateInterface enocdeStateInterface = this.mCurrentEnocdeStateInterface;
        if (enocdeStateInterface != null) {
            enocdeStateInterface.onEncodeProgress(i);
        }
        DefaultLogger.d("NexVideoEditor", "engine encoding : " + i);
    }

    @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
    public void onProgressThumbnailCaching(int i, int i2) {
        VideoEditor.TrimStateInterface trimStateInterface = this.mTrimStateInterface;
        if (trimStateInterface != null) {
            trimStateInterface.onTrimProgress(i);
        }
        DefaultLogger.d("NexVideoEditor", "engine onProgressThumbnailCaching : " + i + h.g + i2);
    }

    /* loaded from: classes2.dex */
    public class BGMEdit implements Change {
        public String mBGMUri;

        public BGMEdit(String str) {
            NexVideoEditor.this = r1;
            this.mBGMUri = str;
        }

        @Override // com.miui.gallery.video.editor.NexVideoEditor.Change
        public void applyChange() {
            String crop;
            if (TextUtils.isEmpty(this.mBGMUri)) {
                if (!NexVideoEditor.this.mHasApplyedSmartEffect || NexVideoEditor.this.mCloneProject == null) {
                    NexVideoEditor.this.mNexProject.setBackgroundMusicPath(this.mBGMUri);
                } else {
                    SmartEffectTemplate smartEffectTemplate = (SmartEffectTemplate) NexVideoEditor.this.mAppliedEditValue.get("edit_type_smart_effect_template");
                    if (smartEffectTemplate != null) {
                        smartEffectTemplate.applyChange();
                    }
                }
                NexVideoEditor.this.mAppliedEditValue.remove("bg_audio");
                return;
            }
            if (AnonymousClass18.$SwitchMap$com$miui$gallery$util$Scheme[Scheme.ofUri(this.mBGMUri).ordinal()] == 1) {
                crop = Scheme.FILE.crop(this.mBGMUri);
            } else {
                crop = this.mBGMUri;
            }
            if (!NexVideoEditor.this.mHasApplyedSmartEffect || NexVideoEditor.this.mCloneProject == null) {
                NexVideoEditor.this.mNexProject.setBackgroundMusicPath(crop);
            } else {
                NexVideoEditor.this.mCloneProject.setBackgroundMusicPath(crop);
            }
            NexVideoEditor.this.mAppliedEditValue.put("bg_audio", this);
            if (new File(this.mBGMUri).exists()) {
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put("manufacturer", Build.MANUFACTURER);
            hashMap.put("model", Build.MODEL);
            SamplingStatHelper.recordCountEvent("video_editor", "video_editor_bgm_empty_file_error", hashMap);
        }
    }

    /* renamed from: com.miui.gallery.video.editor.NexVideoEditor$18 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass18 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$util$Scheme;

        static {
            int[] iArr = new int[Scheme.values().length];
            $SwitchMap$com$miui$gallery$util$Scheme = iArr;
            try {
                iArr[Scheme.FILE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public class SourceAudioChange implements Change {
        public boolean mEnable;

        public SourceAudioChange(boolean z) {
            NexVideoEditor.this = r1;
            this.mEnable = z;
        }

        @Override // com.miui.gallery.video.editor.NexVideoEditor.Change
        public void applyChange() {
            if (NexVideoEditor.this.mMainVideoClip != null) {
                if (this.mEnable) {
                    NexVideoEditor.this.mMainVideoClip.setClipVolume(100);
                    NexVideoEditor.this.mAppliedEditValue.remove("source_audio");
                    return;
                }
                NexVideoEditor.this.mMainVideoClip.setClipVolume(0);
                NexVideoEditor.this.mAppliedEditValue.put("source_audio", this);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class FilterEffect implements Change {
        public Filter mFilter;

        public FilterEffect(Filter filter) {
            NexVideoEditor.this = r1;
            this.mFilter = filter;
        }

        @Override // com.miui.gallery.video.editor.NexVideoEditor.Change
        public void applyChange() {
            if (this.mFilter != null) {
                if (!NexVideoEditor.this.mHasApplyedSmartEffect || NexVideoEditor.this.mCloneProject == null) {
                    NexVideoEditor.this.mMainVideoClip.setColorEffect(NexVideoEditor.this.finEffect(this.mFilter.getFilterId()));
                } else {
                    int totalClipCount = NexVideoEditor.this.mCloneProject.getTotalClipCount(true);
                    nexColorEffect finEffect = NexVideoEditor.this.finEffect(this.mFilter.getFilterId());
                    for (int i = 0; i < totalClipCount; i++) {
                        NexVideoEditor.this.mCloneProject.getClip(i, true).setColorEffect(finEffect);
                    }
                }
                if ("NONE".equals(this.mFilter.getFilterId())) {
                    NexVideoEditor.this.mAppliedEditValue.remove("filter");
                    return;
                } else {
                    NexVideoEditor.this.mAppliedEditValue.put("filter", this);
                    return;
                }
            }
            NexVideoEditor.this.mMainVideoClip.setColorEffect(null);
            NexVideoEditor.this.mAppliedEditValue.remove("filter");
        }
    }

    /* loaded from: classes2.dex */
    public class TrimInfo implements Change {
        public int end;
        public int start;

        public TrimInfo(int i, int i2) {
            NexVideoEditor.this = r1;
            this.start = i;
            this.end = i2;
        }

        @Override // com.miui.gallery.video.editor.NexVideoEditor.Change
        public void applyChange() {
            if (NexVideoEditor.this.mMainVideoClip != null) {
                int i = this.start;
                int i2 = this.end;
                if (i < i2 && i2 <= NexVideoEditor.this.getVideoTotalTime()) {
                    NexVideoEditor.this.mMainVideoClip.getVideoClipEdit().setTrim(this.start, this.end);
                    if (this.start != 0 || this.end != NexVideoEditor.this.getVideoTotalTime()) {
                        NexVideoEditor.this.mAppliedEditValue.put("trim", this);
                    } else {
                        NexVideoEditor.this.mAppliedEditValue.remove("trim");
                    }
                    if (NexVideoEditor.this.mEndClip != null) {
                        NexVideoEditor.this.mEndClip.applyChange();
                    }
                } else {
                    DefaultLogger.d("NexVideoEditor", "TrimInfo: end time is lower than start time ");
                    return;
                }
            }
            SmartEffectTemplate smartEffectTemplate = (SmartEffectTemplate) NexVideoEditor.this.mAppliedEditValue.get("edit_type_smart_effect_template");
            if (smartEffectTemplate != null) {
                smartEffectTemplate.applyChange();
            }
        }
    }

    /* loaded from: classes2.dex */
    public class TextClip implements Change {
        public int mShowTime;
        public nexOverlayItem mTextOverLayItem;
        public int x;
        public int y;

        public /* synthetic */ TextClip(NexVideoEditor nexVideoEditor, AnonymousClass1 anonymousClass1) {
            this();
        }

        public /* synthetic */ TextClip(NexVideoEditor nexVideoEditor, String str, int i, AnonymousClass1 anonymousClass1) {
            this(str, i);
        }

        public TextClip() {
            NexVideoEditor.this = r1;
            this.mShowTime = 0;
        }

        public TextClip(String str, int i) {
            NexVideoEditor.this = r1;
            this.mShowTime = 0;
            if (!TextUtils.isEmpty(str)) {
                this.mShowTime = i;
                initXY();
                this.mTextOverLayItem = createTextNexOverLayItem(str);
            }
        }

        public final nexOverlayItem getTextOverLayItem() {
            return this.mTextOverLayItem;
        }

        public final void initXY() {
            int i = NexVideoEditor.this.mVideoRotation % nexClip.kClip_Rotate_180 == 0 ? NexVideoEditor.this.mVideoWidth : NexVideoEditor.this.mVideoHeight;
            int i2 = NexVideoEditor.this.mVideoRotation % nexClip.kClip_Rotate_180 == 0 ? NexVideoEditor.this.mVideoHeight : NexVideoEditor.this.mVideoWidth;
            if (i > i2) {
                this.x = Math.round((i / i2) * 720.0f) / 2;
                this.y = 360;
            } else if (i == i2) {
                this.x = 360;
                this.y = 360;
            } else {
                int round = Math.round((i2 / i) * 720.0f);
                this.x = 360;
                this.y = round / 2;
            }
        }

        public final nexOverlayItem createTextNexOverLayItem(String str) {
            nexOverlayKineMasterText nexoverlaykinemastertext = new nexOverlayKineMasterText(NexVideoEditor.this.mContext, str, 59);
            nexoverlaykinemastertext.setTextColor(-1);
            nexoverlaykinemastertext.setFontId(null);
            int projectTotalTime = NexVideoEditor.this.getProjectTotalTime();
            int i = this.mShowTime;
            if (i == 0) {
                nexOverlayItem nexoverlayitem = new nexOverlayItem(nexoverlaykinemastertext, this.x, this.y, 0, projectTotalTime);
                nexoverlayitem.addAnimate(nexAnimate.getAlpha(2000, StatusCode.BAD_REQUEST, 1.0f, 0.0f));
                return nexoverlayitem;
            } else if (i != 1) {
                if (i != 2) {
                    return null;
                }
                return new nexOverlayItem(nexoverlaykinemastertext, this.x, this.y, 0, projectTotalTime);
            } else {
                nexOverlayItem nexoverlayitem2 = new nexOverlayItem(nexoverlaykinemastertext, this.x, this.y, (projectTotalTime - 2000) - 400, projectTotalTime);
                nexoverlayitem2.addAnimate(nexAnimate.getAlpha(2000, StatusCode.BAD_REQUEST, 1.0f, 0.0f));
                return nexoverlayitem2;
            }
        }

        @Override // com.miui.gallery.video.editor.NexVideoEditor.Change
        public void applyChange() {
            delete((TextClip) NexVideoEditor.this.mAppliedEditValue.get("edit_type_auto_water_mark_text"));
            if (this.mTextOverLayItem != null) {
                NexVideoEditor.this.mAppliedEditValue.put("edit_type_auto_water_mark_text", this);
                NexVideoEditor.this.mNexProject.addOverlay(this.mTextOverLayItem);
            }
            SmartEffectTemplate smartEffectTemplate = (SmartEffectTemplate) NexVideoEditor.this.mAppliedEditValue.get("edit_type_smart_effect_template");
            if (smartEffectTemplate != null) {
                smartEffectTemplate.applyChange();
            }
        }

        public final void delete(TextClip textClip) {
            if (textClip == null || textClip.getTextOverLayItem() == null) {
                return;
            }
            NexVideoEditor.this.mNexProject.removeOverlay(textClip.getTextOverLayItem().getId());
            NexVideoEditor.this.mAppliedEditValue.remove("edit_type_auto_water_mark_text");
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void setVideoEditorAdjust(boolean z) {
        this.mAppliedEditValue.put("edit_type_adjust", new VideoEditorAdjust(z));
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void adjustBrightness(int i) {
        int i2 = this.mEngineState;
        if (i2 == 0 || i2 == 2) {
            apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.13
                {
                    NexVideoEditor.this = this;
                }

                @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                public void onCompleted() {
                    NexVideoEditor.this.play();
                }
            });
        }
        this.mEngin.setBrightness(i);
        this.mEngin.fastPreview(nexEngine.FastPreviewOption.adj_brightness, i);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void adjustSaturation(int i) {
        int i2 = this.mEngineState;
        if (i2 == 0 || i2 == 2) {
            apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.14
                {
                    NexVideoEditor.this = this;
                }

                @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                public void onCompleted() {
                    NexVideoEditor.this.play();
                }
            });
        }
        this.mEngin.setSaturation(i);
        this.mEngin.fastPreview(nexEngine.FastPreviewOption.adj_saturation, i);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void adjustContrast(int i) {
        int i2 = this.mEngineState;
        if (i2 == 0 || i2 == 2) {
            apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.15
                {
                    NexVideoEditor.this = this;
                }

                @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                public void onCompleted() {
                    NexVideoEditor.this.play();
                }
            });
        }
        this.mEngin.setContrast(i);
        this.mEngin.fastPreview(nexEngine.FastPreviewOption.adj_contrast, i);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void adjustVignetteRange(int i) {
        int i2 = this.mEngineState;
        if (i2 == 0 || i2 == 2) {
            apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.16
                {
                    NexVideoEditor.this = this;
                }

                @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                public void onCompleted() {
                    NexVideoEditor.this.play();
                }
            });
        }
        this.mEngin.setVignetteRange(i);
        this.mEngin.fastPreview(nexEngine.FastPreviewOption.adj_vignetteRange, i);
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void adjustSharpness(int i) {
        int i2 = this.mEngineState;
        if (i2 == 0 || i2 == 2) {
            apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.NexVideoEditor.17
                {
                    NexVideoEditor.this = this;
                }

                @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                public void onCompleted() {
                    NexVideoEditor.this.play();
                }
            });
        }
        this.mEngin.setSharpness(i);
        this.mEngin.fastPreview(nexEngine.FastPreviewOption.adj_sharpness, i);
    }

    /* loaded from: classes2.dex */
    public class VideoEditorAdjust implements Change {
        public boolean mAdjust;

        public VideoEditorAdjust(boolean z) {
            NexVideoEditor.this = r1;
            this.mAdjust = z;
        }

        @Override // com.miui.gallery.video.editor.NexVideoEditor.Change
        public void applyChange() {
            if (!this.mAdjust) {
                delete();
            }
        }

        public final void delete() {
            NexVideoEditor.this.mAppliedEditValue.remove("edit_type_adjust");
        }
    }

    /* loaded from: classes2.dex */
    public class WaterMark implements Change {
        public int anchorPoint;
        public int mAlphaDurationTime;
        public int mEndTime;
        public int mStartTime;
        public String mTemplateId;
        public nexOverlayImage mWaterMarkOverlay;
        public nexOverlayItem mWaterMarkOverlayItem;
        public float scale;
        public int timeType;
        public int x;
        public int y;

        public WaterMark() {
            NexVideoEditor.this = r1;
            this.mStartTime = 0;
            this.mEndTime = 0;
            this.timeType = 0;
            this.anchorPoint = 0;
            this.mAlphaDurationTime = StatusCode.BAD_REQUEST;
            this.scale = 0.0f;
        }

        public WaterMark(int i, String str) {
            NexVideoEditor.this = r1;
            this.mStartTime = 0;
            this.mEndTime = 0;
            this.timeType = 0;
            this.anchorPoint = 0;
            this.mAlphaDurationTime = StatusCode.BAD_REQUEST;
            this.scale = 0.0f;
            this.timeType = i;
            this.mTemplateId = str;
            initOverLayItemParams();
            initOverLayItem();
        }

        public final void initOverLayItemParams() {
            int i = NexVideoEditor.this.mVideoRotation % nexClip.kClip_Rotate_180 == 0 ? NexVideoEditor.this.mVideoWidth : NexVideoEditor.this.mVideoHeight;
            int i2 = NexVideoEditor.this.mVideoRotation % nexClip.kClip_Rotate_180 == 0 ? NexVideoEditor.this.mVideoHeight : NexVideoEditor.this.mVideoWidth;
            if (i > i2) {
                int round = Math.round((i / i2) * 720.0f);
                this.x = round / 2;
                this.y = 360;
                this.scale = 970.0f / round;
            } else if (i == i2) {
                this.x = 360;
                this.y = 360;
                this.scale = 0.576f;
            } else {
                int round2 = Math.round((i2 / i) * 720.0f);
                this.x = 360;
                this.y = round2 / 2;
                this.scale = 0.576f;
            }
        }

        public final void initOverLayItem() {
            this.mEndTime = NexVideoEditor.this.getProjectTotalTime();
            if (TextUtils.isEmpty(this.mTemplateId) || this.mEndTime <= 0) {
                return;
            }
            int i = this.timeType;
            if (i == 0) {
                this.mStartTime = 0;
                this.mWaterMarkOverlay = nexOverlayPreset.getOverlayPreset(NexVideoEditor.this.mContext.getApplicationContext()).getOverlayImage(this.mTemplateId);
                this.mWaterMarkOverlayItem = new nexOverlayItem(this.mWaterMarkOverlay, this.anchorPoint, false, this.x, this.y, this.mStartTime, this.mEndTime);
                nexOverlayImage nexoverlayimage = this.mWaterMarkOverlay;
                if (nexoverlayimage != null) {
                    this.mWaterMarkOverlayItem.addAnimate(nexAnimate.getAlpha(nexoverlayimage.getDefaultDuration() + 1500, this.mAlphaDurationTime, 1.0f, 0.0f));
                }
            } else if (i == 2) {
                this.mStartTime = 0;
                this.mWaterMarkOverlay = nexOverlayPreset.getOverlayPreset(NexVideoEditor.this.mContext.getApplicationContext()).getOverlayImage(this.mTemplateId);
                this.mWaterMarkOverlayItem = new nexOverlayItem(this.mWaterMarkOverlay, this.anchorPoint, false, this.x, this.y, this.mStartTime, this.mEndTime);
            } else if (i == 1) {
                nexOverlayImage overlayImage = nexOverlayPreset.getOverlayPreset(NexVideoEditor.this.mContext.getApplicationContext()).getOverlayImage(this.mTemplateId);
                this.mWaterMarkOverlay = overlayImage;
                this.mStartTime = ((this.mEndTime - 800) - overlayImage.getDefaultDuration()) + VideoDecoder.DecodeCallback.ERROR_START;
                this.mWaterMarkOverlayItem = new nexOverlayItem(this.mWaterMarkOverlay, this.anchorPoint, false, this.x, this.y, this.mStartTime, this.mEndTime);
                this.mWaterMarkOverlayItem.addAnimate(nexAnimate.getAlpha(this.mWaterMarkOverlay.getDefaultDuration() + 500, 800, 1.0f, 0.0f));
            }
            nexOverlayItem nexoverlayitem = this.mWaterMarkOverlayItem;
            float f = this.scale;
            nexoverlayitem.setScale(f, f);
        }

        @Override // com.miui.gallery.video.editor.NexVideoEditor.Change
        public void applyChange() {
            delete((WaterMark) NexVideoEditor.this.mAppliedEditValue.get("edit_type_water_mark"));
            if (this.mWaterMarkOverlayItem != null) {
                NexVideoEditor.this.mAppliedEditValue.put("edit_type_water_mark", this);
                NexVideoEditor.this.mNexProject.addOverlay(this.mWaterMarkOverlayItem);
            }
            SmartEffectTemplate smartEffectTemplate = (SmartEffectTemplate) NexVideoEditor.this.mAppliedEditValue.get("edit_type_smart_effect_template");
            if (smartEffectTemplate != null) {
                smartEffectTemplate.applyChange();
                DefaultLogger.d("NexVideoEditor", "WaterMark change: have  smartEffect;");
            }
            BGMEdit bGMEdit = (BGMEdit) NexVideoEditor.this.mAppliedEditValue.get("bg_audio");
            if (bGMEdit != null) {
                bGMEdit.applyChange();
                DefaultLogger.d("NexVideoEditor", "WaterMark change: have  audio;");
            }
            FilterEffect filterEffect = (FilterEffect) NexVideoEditor.this.mAppliedEditValue.get("filter");
            if (filterEffect != null) {
                filterEffect.applyChange();
                DefaultLogger.d("NexVideoEditor", "WaterMark change: have  filter;");
            }
        }

        public final void delete(WaterMark waterMark) {
            if (waterMark == null || waterMark.mWaterMarkOverlayItem == null) {
                return;
            }
            NexVideoEditor.this.mNexProject.removeOverlay(waterMark.mWaterMarkOverlayItem.getId());
            NexVideoEditor.this.mAppliedEditValue.remove("edit_type_water_mark");
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean setSmartEffectTemplate(SmartEffect smartEffect) {
        this.mTempEditValue.put("edit_type_smart_effect_template", new SmartEffectTemplate(smartEffect));
        return false;
    }

    /* loaded from: classes2.dex */
    public class SmartEffectTemplate implements Change {
        public int mLimitTime;
        public nexTemplateManager.Template template;

        public SmartEffectTemplate(SmartEffect smartEffect) {
            NexVideoEditor.this = r1;
            if (smartEffect != null) {
                this.template = smartEffect.getTemplate();
                this.mLimitTime = smartEffect.getLongTime();
            }
        }

        public final String checkTemplate() {
            String message;
            if (NexVideoEditor.this.mNexProject.getTotalTime() <= 0) {
                return "Project is empty";
            }
            if (this.template == null) {
                return "Template did not selected";
            }
            if (NexVideoEditor.this.mCloneProject != null) {
                NexVideoEditor.this.mCloneProject.allClear(true);
            }
            NexVideoEditor nexVideoEditor = NexVideoEditor.this;
            nexVideoEditor.mCloneProject = nexProject.clone(nexVideoEditor.mNexProject);
            int i = this.mLimitTime;
            if (i > 0 && i < NexVideoEditor.this.getVideoTotalTime()) {
                int totalClipCount = NexVideoEditor.this.mCloneProject.getTotalClipCount(true);
                for (int i2 = 0; i2 < totalClipCount; i2++) {
                    NexVideoEditor.this.mCloneProject.getClip(i2, true).getVideoClipEdit().setTrim(0, this.mLimitTime);
                }
            }
            NexVideoEditor.this.mEngin.setProject(NexVideoEditor.this.mCloneProject);
            try {
                NexAssetTemplateManager.getInstance();
                message = !NexAssetTemplateManager.getKmTemplateManager().applyTemplateToProjectById(NexVideoEditor.this.mCloneProject, this.template.id()) ? "Fail to apply template on project" : "";
            } catch (ExpiredTimeException unused) {
                message = "This is expired asset!";
            } catch (Exception e) {
                message = e.getMessage();
            }
            DefaultLogger.d("NexVideoEditor", "check template info : %s ", message);
            return message;
        }

        @Override // com.miui.gallery.video.editor.NexVideoEditor.Change
        public void applyChange() {
            if (this.template == null || !TextUtils.isEmpty(checkTemplate())) {
                NexVideoEditor.this.mCloneProject = null;
                NexVideoEditor.this.mEngin.setProject(NexVideoEditor.this.mNexProject);
                delete();
                return;
            }
            delete();
            NexVideoEditor.this.mAppliedEditValue.put("edit_type_smart_effect_template", this);
            NexVideoEditor.this.mHasApplyedSmartEffect = true;
        }

        public final void delete() {
            NexVideoEditor.this.mHasApplyedSmartEffect = false;
            NexVideoEditor.this.mAppliedEditValue.remove("edit_type_smart_effect_template");
        }
    }

    /* loaded from: classes2.dex */
    public static class ExportParams {
        public boolean mIsValid;
        public int mProfileValue = 0;
        public int mBitrate = 0;
        public int mLevelValue = 0;
        public int mCodecValue = 0;
        public int mFps = 0;
        public int mWidth = 0;
        public int mHeight = 0;

        public ExportParams setFps(int i) {
            this.mFps = i * 100;
            return this;
        }

        public ExportParams setBitrate(int i) {
            this.mBitrate = i;
            return this;
        }

        public ExportParams setProfileValue(int i) {
            this.mProfileValue = i;
            return this;
        }

        public ExportParams setLevelValue(int i) {
            this.mLevelValue = i;
            return this;
        }

        public ExportParams setCodecValue(int i) {
            this.mCodecValue = i;
            return this;
        }

        public ExportParams setWidth(int i) {
            this.mWidth = i;
            return this;
        }

        public ExportParams setHeight(int i) {
            this.mHeight = i;
            return this;
        }

        public ExportParams setValid(boolean z) {
            this.mIsValid = z;
            return this;
        }

        public boolean isValid() {
            return this.mIsValid;
        }
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public void setTouchSeekBar(boolean z) {
        this.mIsTouchSeekBar = z;
    }

    @Override // com.miui.gallery.video.editor.VideoEditor
    public boolean isTouchSeekBar() {
        return this.mIsTouchSeekBar;
    }
}
