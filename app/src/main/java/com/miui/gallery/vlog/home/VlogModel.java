package com.miui.gallery.vlog.home;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.MenuFragment;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.entity.VideoClip;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.template.TemplateFilesManager;
import com.miui.gallery.vlog.tools.VlogVideoFileTools;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class VlogModel extends AndroidViewModel implements VlogContract$IVlogModel {
    public MenuFragment mCurrentEffectMenuFragment;
    public int mCurrentEffectMenuFragmentPosition;
    public FragmentActivity mFragmentActivity;
    public FragmentManager mFragmentManager;
    public VlogContract$IVlogView mIVlogView;
    public boolean mIsFavorite;
    public boolean mIsInitialized;
    public boolean mIsSaving;
    public MiVideoSdkManager mSdkManager;
    public View mSingleClipContentView;
    public View mSingleClipTopView;
    public String mSrcFilePath;
    public TemplateFilesManager mTemplateFilesManager;
    public String mTemplateName;
    public List<VideoClip> mVideoClips;
    public ArrayList<String> mVideoPathList;
    public boolean mVideoPathListHasChanged;
    public VlogConfig.VideoSource mVideoSource;

    public VlogModel(Application application) {
        super(application);
        this.mVideoPathList = new ArrayList<>();
        this.mVideoPathListHasChanged = false;
    }

    public void initData(FragmentActivity fragmentActivity, Intent intent) {
        this.mIsSaving = false;
        this.mFragmentActivity = fragmentActivity;
        this.mSdkManager = new MiVideoSdkManager(this.mFragmentActivity);
        this.mVideoClips = new ArrayList();
        this.mFragmentManager = fragmentActivity.getSupportFragmentManager();
        parseIntent(intent);
        this.mTemplateFilesManager = (TemplateFilesManager) this.mSdkManager.getManagerService(4);
        this.mIVlogView = (VlogContract$IVlogView) fragmentActivity;
        initVlogConfigInfo();
        initScreenRelatedValues();
        this.mIsInitialized = true;
    }

    public void reloadData(FragmentActivity fragmentActivity) {
        this.mFragmentActivity = fragmentActivity;
        this.mSdkManager.setContext(fragmentActivity);
        this.mFragmentManager = fragmentActivity.getSupportFragmentManager();
        this.mIVlogView = (VlogContract$IVlogView) fragmentActivity;
        initScreenRelatedValues();
    }

    public final void initVlogConfigInfo() {
        if (isSingleVideoEdit()) {
            double videoDuration = VlogVideoFileTools.getVideoDuration(getSrcFilePath()) * 1000;
            if (videoDuration <= 3.0E7d) {
                return;
            }
            VlogConfig.sScaleForPixelPerMicroSecond = videoDuration / 3.0E7d;
        }
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogModel
    public void initScreenRelatedValues() {
        double curScreenWidth = (ScreenUtils.getCurScreenWidth() / 10.0d) / 1000000.0d;
        VlogConfig.sPixelPerMicroSecond = curScreenWidth;
        double d = curScreenWidth / VlogConfig.sScaleForPixelPerMicroSecond;
        VlogConfig.sPixelPerMicroSecondForClip = d;
        VlogConfig.sMicroSecondPerTwoPixel = Math.round(2.0d / d);
    }

    public TemplateFilesManager getTemplateFilesManager() {
        return this.mTemplateFilesManager;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogModel
    public FragmentManager getFragmentManager() {
        return this.mFragmentManager;
    }

    public String getTemplateName() {
        return this.mTemplateName;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogModel
    public List<VideoClip> getVideoClips() {
        return this.mVideoClips;
    }

    public List<IVideoClip> getIVideoClips() {
        return this.mSdkManager.getVideoClips();
    }

    public VlogContract$IVlogView getIVlogView() {
        return this.mIVlogView;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogModel
    public boolean isSingleVideoEdit() {
        return this.mVideoSource == VlogConfig.VideoSource.FROM_OUTER_VIDEO_EDITOR;
    }

    public boolean isSaving() {
        return this.mIsSaving;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogModel
    public void setSaveStatus(boolean z) {
        this.mIsSaving = z;
    }

    public boolean isFavorite() {
        return this.mIsFavorite;
    }

    public final void parseIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        this.mTemplateName = intent.getStringExtra("com.miui.gallery.vlog.extra.template");
        Bundle bundleExtra = intent.getBundleExtra("com.miui.gallery.vlog.extra.clips");
        if (bundleExtra == null) {
            return;
        }
        VlogConfig.VideoSource videoSource = (VlogConfig.VideoSource) bundleExtra.getSerializable("vlog_video_source");
        this.mVideoSource = videoSource;
        if (videoSource == null) {
            this.mVideoSource = VlogConfig.VideoSource.FROM_OUTER_OTHER;
        } else if (isSingleVideoEdit()) {
            this.mIsFavorite = intent.getBooleanExtra("photo_is_favorite", false);
        }
        this.mSrcFilePath = bundleExtra.getString("video_editor_src_path");
        DefaultLogger.d("VlogModel_", "parseIntent: mSrcFilePath=" + this.mSrcFilePath);
        ArrayList parcelableArrayList = bundleExtra.getParcelableArrayList("com.miui.gallery.vlog.extra.clips");
        if (parcelableArrayList == null) {
            DefaultLogger.d("VlogModel_", "videoClips is null");
            return;
        }
        this.mVideoClips = parcelableArrayList;
        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("com.miui.gallery.vlog.extra.paths");
        if (stringArrayListExtra == null) {
            return;
        }
        this.mVideoPathList.addAll(stringArrayListExtra);
        DefaultLogger.d("VlogModel_", "parseIntent count %d, template %s", Integer.valueOf(parcelableArrayList.size()), this.mTemplateName);
    }

    public String getSrcFilePath() {
        return this.mSrcFilePath;
    }

    public VlogConfig.VideoSource getVideoSource() {
        return this.mVideoSource;
    }

    public void addNewVideo(String str) {
        if (this.mVideoPathList.contains(str)) {
            return;
        }
        this.mVideoPathListHasChanged = true;
        this.mVideoPathList.add(str);
    }

    public boolean shouldRematchTemplate(String str) {
        updateVideoPaths();
        if (!TextUtils.equals(str, getTemplateName())) {
            return true;
        }
        return this.mVideoPathListHasChanged;
    }

    public List<String> getCurrentVideoPaths() {
        return new ArrayList(this.mVideoPathList);
    }

    public void updateVideoPaths() {
        HashSet hashSet = new HashSet();
        List<String> videoPathList = this.mSdkManager.getVideoPathList();
        if (videoPathList != null && !videoPathList.isEmpty()) {
            hashSet.addAll(videoPathList);
        }
        Iterator<String> it = this.mVideoPathList.iterator();
        while (it.hasNext()) {
            if (!hashSet.contains(it.next())) {
                it.remove();
                this.mVideoPathListHasChanged = true;
            }
        }
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogModel
    public MenuFragment getCurrentEffectMenuFragment() {
        return this.mCurrentEffectMenuFragment;
    }

    public int getCurrentEffectMenuFragmentPosition() {
        return this.mCurrentEffectMenuFragmentPosition;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogModel
    public void setCurrentEffectMenuFragment(int i, MenuFragment menuFragment) {
        this.mCurrentEffectMenuFragment = menuFragment;
        this.mCurrentEffectMenuFragmentPosition = i;
    }

    public View getSingleClipContentView(ViewGroup viewGroup) {
        if (this.mSingleClipContentView == null) {
            this.mSingleClipContentView = LayoutInflater.from(this.mFragmentActivity).inflate(R$layout.vlog_single_clip_menu_content_layout, viewGroup, false);
        }
        return this.mSingleClipContentView;
    }

    public View getSingleClipTopView(ViewGroup viewGroup) {
        if (this.mSingleClipTopView == null) {
            this.mSingleClipTopView = LayoutInflater.from(this.mFragmentActivity).inflate(R$layout.vlog_single_clip_menu_operation_layout, viewGroup, false);
        }
        return this.mSingleClipTopView;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogModel
    public String getOutFilePath() {
        if (isSingleVideoEdit()) {
            return VlogStorage.getOutputMediaFilePath(getSrcFilePath());
        }
        return VlogStorage.getOutputMediaFilePath();
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogModel
    public MiVideoSdkManager getSdkManager() {
        return this.mSdkManager;
    }

    @Override // androidx.lifecycle.ViewModel
    public void onCleared() {
        super.onCleared();
        List<VideoClip> list = this.mVideoClips;
        if (list != null) {
            list.clear();
        }
    }
}
