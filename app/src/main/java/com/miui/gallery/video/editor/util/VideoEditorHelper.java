package com.miui.gallery.video.editor.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.fragment.FilterAdjustFragment;
import com.miui.gallery.video.editor.fragment.VideoNavFragment;
import com.miui.gallery.video.editor.manager.SmartVideoJudgeManager;
import com.miui.gallery.video.editor.model.MenuFragmentData;
import com.miui.gallery.video.editor.ui.AudioFragment;
import com.miui.gallery.video.editor.ui.EncodeProgressDialog;
import com.miui.gallery.video.editor.ui.MenuFragment;
import com.miui.gallery.video.editor.ui.SmartEffectFragment;
import com.miui.gallery.video.editor.ui.TrimFragment;
import com.miui.gallery.video.editor.ui.VideoEditorFragment;
import com.miui.gallery.video.editor.ui.WaterMarkFragment;
import com.miui.gallery.video.editor.widget.DisplayWrapper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoEditorHelper {
    public FragmentActivity mActivity;
    public Context mContext;
    public int mCurrentEditType;
    public MenuFragment mCurrentMenuFragment;
    public DisplayWrapper mDisplayWrapperView;
    public int mEffectContainerId;
    public VideoEditorFragment mFragment;
    public FragmentManager mFragmentManager;
    public String mLastTag;
    public List<MenuFragmentData> mNavigatorDataList = new ArrayList();
    public LinkedHashMap<Integer, MenuFragmentData> mNavigatorDataMap;
    public VideoEditor mVideoEditor;
    public VideoSaveManager mVideoSaveManager;
    public boolean mVideoSaving;

    public VideoEditorHelper(Context context, VideoEditorFragment videoEditorFragment) {
        this.mContext = context;
        this.mFragment = videoEditorFragment;
        FragmentActivity activity = videoEditorFragment.getActivity();
        this.mActivity = activity;
        this.mFragmentManager = activity.getSupportFragmentManager();
        VideoEditor videoEditor = this.mFragment.getVideoEditor();
        this.mVideoEditor = videoEditor;
        this.mDisplayWrapperView = videoEditor.getDisplayWrapper();
        this.mEffectContainerId = this.mFragment.getEffectMenuContainerId();
        if (this.mNavigatorDataMap == null) {
            this.mNavigatorDataMap = new LinkedHashMap<>();
            initNavgatorData();
        }
        this.mVideoSaveManager = new VideoSaveManager(this);
        initListener();
    }

    public final void initNavgatorData() {
        this.mNavigatorDataMap.put(16, new MenuFragmentData(VideoNavFragment.class, 0, 0, 16));
        this.mNavigatorDataMap.put(17, new MenuFragmentData(SmartEffectFragment.class, R.drawable.video_editor_icon_smareffects, R.string.video_editor_intelligent_special_effect, 17));
        this.mNavigatorDataMap.put(18, new MenuFragmentData(TrimFragment.class, R.drawable.video_editor_icon_trim, R.string.video_editor_trim, 18));
        this.mNavigatorDataMap.put(19, new MenuFragmentData(FilterAdjustFragment.class, R.drawable.video_editor_icon_filter, R.string.video_editor_filter_and_regulator, 19));
        this.mNavigatorDataMap.put(20, new MenuFragmentData(AudioFragment.class, R.drawable.video_editor_icon_music, R.string.video_editor_audio, 20));
        this.mNavigatorDataMap.put(21, new MenuFragmentData(WaterMarkFragment.class, R.drawable.video_editor_icon_text, R.string.video_editor_watermark, 21));
        this.mNavigatorDataMap.put(22, new MenuFragmentData(null, R.drawable.video_editor_icon_more, R.string.video_editor_more, 22));
    }

    public final void initListener() {
        this.mDisplayWrapperView.setAudioVoiceListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.util.VideoEditorHelper.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                boolean audioVoiceSelected = VideoEditorHelper.this.mDisplayWrapperView.getAudioVoiceSelected();
                VideoEditorHelper.this.mDisplayWrapperView.setAudioVoiceSelected(!audioVoiceSelected);
                VideoEditorHelper.this.mCurrentMenuFragment.updateVoiceState(audioVoiceSelected);
            }
        });
    }

    public List<MenuFragmentData> getNavigatorData(int i) {
        if (this.mNavigatorDataList.isEmpty()) {
            for (MenuFragmentData menuFragmentData : this.mNavigatorDataMap.values()) {
                if (menuFragmentData != null && !menuFragmentData.isNavModule()) {
                    boolean z = SmartVideoJudgeManager.isAvailable() && i <= 120;
                    DefaultLogger.d("VideoEditorHelper", "smartVideoAvailable: %s", Boolean.valueOf(z));
                    if (menuFragmentData.module != 17 || z) {
                        this.mNavigatorDataList.add(menuFragmentData);
                    }
                }
            }
        }
        return this.mNavigatorDataList;
    }

    public void changeEditMenu(MenuFragmentData menuFragmentData) {
        if (menuFragmentData == null) {
            return;
        }
        String tag = menuFragmentData.getTag();
        if (menuFragmentData.isModuleMore()) {
            this.mVideoEditor.toThirdEditor(this.mContext);
            return;
        }
        MenuFragment menuFragment = (MenuFragment) this.mFragmentManager.findFragmentByTag(makeMenuFragmentTag(this.mLastTag));
        if (menuFragment == null) {
            menuFragment = (MenuFragment) this.mFragmentManager.findFragmentById(this.mEffectContainerId);
        }
        MenuFragment menuFragment2 = (MenuFragment) this.mFragmentManager.findFragmentByTag(makeMenuFragmentTag(tag));
        if (menuFragment2 != null && menuFragment2.equals(menuFragment)) {
            menuFragment = null;
        }
        FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
        if (menuFragment2 != null) {
            beginTransaction.attach(menuFragment2);
            this.mCurrentMenuFragment = menuFragment2;
        } else {
            Class<? extends MenuFragment> menu = menuFragmentData.getMenu();
            if (menu != null) {
                try {
                    MenuFragment newInstance = menu.newInstance();
                    beginTransaction.add(this.mEffectContainerId, newInstance, makeMenuFragmentTag(tag));
                    this.mCurrentMenuFragment = newInstance;
                } catch (Exception e) {
                    DefaultLogger.d("VideoEditorHelper", (Throwable) e);
                }
            }
        }
        this.mCurrentMenuFragment.updateLastFragment(menuFragment);
        if (menuFragment != null) {
            beginTransaction.detach(menuFragment);
        }
        beginTransaction.commitAllowingStateLoss();
        this.mLastTag = tag;
        this.mCurrentEditType = this.mCurrentMenuFragment.getEffectId();
    }

    public final String makeMenuFragmentTag(String str) {
        return VideoEditorHelper.class.getName() + str;
    }

    public final void onMenuFragCancel() {
        MenuFragment menuFragment = this.mCurrentMenuFragment;
        if (menuFragment != null) {
            menuFragment.doCancel();
        }
    }

    public void onVideoLoadCompleted() {
        MenuFragment menuFragment = this.mCurrentMenuFragment;
        if (menuFragment != null) {
            menuFragment.onVideoLoadCompleted();
        }
    }

    public void onPlayButtonClicked() {
        MenuFragment menuFragment = this.mCurrentMenuFragment;
        if (menuFragment != null) {
            menuFragment.onPlayButtonClicked();
        }
    }

    public final boolean isTrimEditMenu() {
        return R.id.video_editor_trim == this.mCurrentEditType;
    }

    public final boolean isAudioEditMenu() {
        return R.id.video_editor_audio == this.mCurrentEditType;
    }

    public final boolean isNavEditMenu() {
        return R.id.video_editor_nav == this.mCurrentEditType;
    }

    public boolean isWaterMarkEditMenu() {
        return R.id.video_editor_water_mark == this.mCurrentEditType;
    }

    public void showNavEditMenu() {
        changeEditMenu(this.mNavigatorDataMap.get(16));
    }

    public void onCancel() {
        onBackPressed();
    }

    public void onSave() {
        if (this.mVideoEditor == null || this.mVideoSaving) {
            DefaultLogger.e("VideoEditorHelper", "the video is saving:  %s ", Boolean.valueOf(this.mVideoSaving));
            return;
        }
        DefaultLogger.d("VideoEditorHelper", "start save video！");
        Uri data = this.mFragment.getData();
        setVideoSaving(true);
        EncodeProgressDialog.startEncode(this.mVideoEditor, FileHelper.generateOutputFilePath(data.getPath()), new EncodeProgressDialog.OnCompletedListener() { // from class: com.miui.gallery.video.editor.util.VideoEditorHelper.2
            @Override // com.miui.gallery.video.editor.ui.EncodeProgressDialog.OnCompletedListener
            public void onCompleted(String str, boolean z, int i, int i2) {
                DefaultLogger.d("VideoEditorHelper", "video save success: %s, and video encode error errorCode :%s", Boolean.valueOf(z), String.valueOf(i));
                if (z) {
                    SamplingStatHelper.recordCountEvent("video_editor", "video_editor_export_success");
                    VideoEditorHelper.this.mVideoSaveManager.handleVideoSave(str);
                } else if (i2 == -1) {
                    VideoEditorHelper.this.setVideoSaving(false);
                } else {
                    HashMap hashMap = new HashMap();
                    hashMap.put("error", String.valueOf(i));
                    SamplingStatHelper.recordCountEvent("video_editor", "video_editor_export_failed", hashMap);
                    ToastUtils.makeText(VideoEditorHelper.this.mActivity, VideoEditorHelper.this.mActivity.getResources().getString(R.string.video_editor_encode_video_error));
                    VideoEditorHelper.this.setVideoSaving(false);
                }
            }
        }, this.mFragment.getFragmentManager());
    }

    public boolean onBackPressed() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor == null) {
            return false;
        }
        if (!videoEditor.isInit()) {
            return true;
        }
        if (this.mVideoEditor.hasEdit()) {
            if (!isNavEditMenu()) {
                onMenuFragCancel();
                return true;
            }
            ConfirmDialog.showConfirmDialog(this.mFragmentManager, this.mFragment.getString(R.string.video_editor_abandon_changes_tip_title), this.mFragment.getString(R.string.video_editor_abandon_changes_tip_message), this.mFragment.getString(R.string.video_editor_cancel), this.mFragment.getString(R.string.video_editor_ok), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.video.editor.util.VideoEditorHelper.3
                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onCancel(DialogFragment dialogFragment) {
                }

                @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                public void onConfirm(DialogFragment dialogFragment) {
                    SamplingStatHelper.recordCountEvent("video_editor", "cancel");
                    if (VideoEditorHelper.this.isNavEditMenu()) {
                        VideoEditorHelper.this.mFragment.exit();
                    } else {
                        VideoEditorHelper.this.onMenuFragCancel();
                    }
                }
            });
        } else {
            SamplingStatHelper.recordCountEvent("video_editor", "cancel");
            if (isNavEditMenu()) {
                this.mFragment.exit();
            } else if (isTrimEditMenu()) {
                onMenuFragCancel();
            } else {
                showNavEditMenu();
            }
        }
        return true;
    }

    public void setVideoSaving(boolean z) {
        this.mVideoSaving = z;
    }

    public void updatePlayView() {
        if (this.mVideoSaving) {
            this.mDisplayWrapperView.showPlayBtn(false);
        } else if (isWaterMarkEditMenu()) {
            this.mDisplayWrapperView.showPlayBtn(false);
        } else {
            int state = this.mVideoEditor.getState();
            if (state == 2 || state == 0) {
                this.mDisplayWrapperView.showPlayBtn(true);
            } else if (isTrimEditMenu() && (state == 3 || state == 200)) {
                this.mDisplayWrapperView.showPlayBtn(true);
            } else {
                this.mDisplayWrapperView.showPlayBtn(false);
            }
        }
    }

    public void updateAutoTrimView() {
        boolean z = false;
        if (isTrimEditMenu()) {
            DisplayWrapper displayWrapper = this.mDisplayWrapperView;
            VideoEditor videoEditor = this.mVideoEditor;
            if (videoEditor != null && videoEditor.isSupportAutoTrim()) {
                z = true;
            }
            displayWrapper.showAutoTrimBtn(z);
            return;
        }
        this.mDisplayWrapperView.showAutoTrimBtn(false);
    }

    public void updateAudioVoiceView(boolean z) {
        if (isAudioEditMenu()) {
            this.mDisplayWrapperView.showAudioVoice(true);
            this.mDisplayWrapperView.setAudioVoiceSelected(!z);
            return;
        }
        this.mDisplayWrapperView.showAudioVoice(false);
    }

    public final void onVideoSaved(String str) {
        Intent intent = new Intent();
        intent.setData(GalleryOpenProvider.translateToContent(str));
        this.mActivity.setResult(-1, intent);
        setVideoSaving(false);
        this.mFragment.exit();
    }

    public void onDestroy() {
        VideoSaveManager videoSaveManager = this.mVideoSaveManager;
        if (videoSaveManager != null) {
            videoSaveManager.onDestroy();
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoSaveManager {
        public Future<Void> mFuture;
        public WeakReference<VideoEditorHelper> mHelperWeakReference;

        public VideoSaveManager(VideoEditorHelper videoEditorHelper) {
            this.mHelperWeakReference = new WeakReference<>(videoEditorHelper);
        }

        public void handleVideoSave(final String str) {
            if (TextUtils.isEmpty(str)) {
                DefaultLogger.d("VideoEditorHelper", "the video path is null. ");
            } else if (getVideoEditorHelper() == null) {
                DefaultLogger.d("VideoEditorHelper", "the VideoEditorHelper is null. ");
            } else {
                Future<Void> future = this.mFuture;
                if (future != null) {
                    future.cancel();
                }
                this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.video.editor.util.VideoEditorHelper.VideoSaveManager.1
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run  reason: collision with other method in class */
                    public Void mo1807run(ThreadPool.JobContext jobContext) {
                        VideoEditorHelper videoEditorHelper = VideoSaveManager.this.getVideoEditorHelper();
                        if (videoEditorHelper == null || VideoSaveManager.this.mFuture.isCancelled()) {
                            return null;
                        }
                        FragmentActivity fragmentActivity = videoEditorHelper.mActivity;
                        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("VideoEditorHelper", "handleVideoSave");
                        ScannerEngine.getInstance().scanFile(fragmentActivity, str, 13);
                        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                        if (documentFile != null) {
                            StorageSolutionProvider.get().apply(documentFile);
                        }
                        if (fragmentActivity.getIntent().getExtras() == null || !fragmentActivity.getIntent().getExtras().getBoolean("photo_is_favorite")) {
                            return null;
                        }
                        CloudUtils.addToFavoritesByPath(fragmentActivity, str);
                        return null;
                    }
                }, new FutureHandler<Void>() { // from class: com.miui.gallery.video.editor.util.VideoEditorHelper.VideoSaveManager.2
                    @Override // com.miui.gallery.concurrent.FutureHandler
                    public void onPostExecute(Future<Void> future2) {
                        VideoEditorHelper videoEditorHelper = VideoSaveManager.this.getVideoEditorHelper();
                        if (future2.isCancelled() || videoEditorHelper == null) {
                            return;
                        }
                        videoEditorHelper.onVideoSaved(str);
                    }
                });
            }
        }

        public final VideoEditorHelper getVideoEditorHelper() {
            WeakReference<VideoEditorHelper> weakReference = this.mHelperWeakReference;
            if (weakReference == null) {
                return null;
            }
            return weakReference.get();
        }

        public void onDestroy() {
            Future<Void> future = this.mFuture;
            if (future != null) {
                future.cancel();
                this.mFuture = null;
            }
        }
    }
}
