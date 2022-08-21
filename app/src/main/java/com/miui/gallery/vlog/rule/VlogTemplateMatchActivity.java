package com.miui.gallery.vlog.rule;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.VideoAnalyticLibraryLoaderHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.VlogActivity;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import com.miui.gallery.vlog.tools.VlogExtendFormatUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.vlog.transcode.VlogTransCodeManager;
import com.nexstreaming.nexeditorsdk.nexEngine;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class VlogTemplateMatchActivity extends BaseActivity implements LibraryLoaderHelper.DownloadStartListener, LibraryLoaderHelper.DownloadStateListener {
    public Disposable mCheckDisposable;
    public boolean mIsFavorite;
    public boolean mIsMatchedNoAlg;
    public ArrayList<String> mPaths;
    public ProgressDialog mProcessingDialog;
    public String mSrcFilePath;
    public String mTargetTemplate;
    public TemplateMatcher mTemplateMatcher;
    public VlogTransCodeManager mTransCodeManager;
    public VlogConfig.VideoSource mVideoSource = VlogConfig.VideoSource.FROM_OUTER_OTHER;
    public VlogTransCodeManager.Callback mCallback = new VlogTransCodeManager.Callback() { // from class: com.miui.gallery.vlog.rule.VlogTemplateMatchActivity.1
        @Override // com.miui.gallery.vlog.transcode.VlogTransCodeManager.Callback
        public void handleResult(List<String> list) {
            VlogTemplateMatchActivity.this.promptFilterVideoIfNeed(list);
            VlogTemplateMatchActivity.this.updatePaths(list);
            VlogTemplateMatchActivity.this.checkSource();
        }

        @Override // com.miui.gallery.vlog.transcode.VlogTransCodeManager.Callback
        public void onTransCode(boolean z) {
            VlogTemplateMatchActivity vlogTemplateMatchActivity;
            int i;
            if (!VlogTemplateMatchActivity.this.isSingleVideoEdit() || z) {
                ProgressDialog progressDialog = VlogTemplateMatchActivity.this.mProcessingDialog;
                if (z) {
                    vlogTemplateMatchActivity = VlogTemplateMatchActivity.this;
                    i = R.string.vlog_video_trans_code_cons;
                } else {
                    vlogTemplateMatchActivity = VlogTemplateMatchActivity.this;
                    i = R.string.vlog_template_rule_importing;
                }
                progressDialog.setMessage(vlogTemplateMatchActivity.getString(i));
                VlogTemplateMatchActivity.this.showProcessingDialog();
            }
        }
    };
    public DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.vlog.rule.VlogTemplateMatchActivity.5
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            VlogTemplateMatchActivity.this.finish();
        }
    };

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
    public void onDownloading() {
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        DebugLogUtils.startDebugLogSpecialTime("VlogTemplateMatchActivity_", "VlogTemplateMatchActivity Create");
        DebugLogUtils.HAS_LOADED_TEMPLATE_DEFAULT = false;
        DebugLogUtils.IS_FIRST_FRAME_LOADED_INTO_VLOG = false;
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.mProcessingDialog = progressDialog;
        progressDialog.setOnCancelListener(this.mOnCancelListener);
        this.mProcessingDialog.setCanceledOnTouchOutside(false);
        parseIntent(getIntent());
        ArrayList<String> arrayList = this.mPaths;
        if (arrayList == null || arrayList.size() == 0) {
            finish();
            return;
        }
        boolean isMatchedNoAlg = isMatchedNoAlg();
        this.mIsMatchedNoAlg = isMatchedNoAlg;
        this.mTemplateMatcher = isMatchedNoAlg ? new TemplateMatcherNoAlg(getAssets()) : new TemplateMatcherAlg(getAssets());
        DefaultLogger.d("VlogTemplateMatchActivity_", "match with Algorithm: %s ", Boolean.valueOf(!this.mIsMatchedNoAlg));
        VlogConfig.init();
        VlogTransCodeManager vlogTransCodeManager = new VlogTransCodeManager(this, this.mPaths, isSingleVideoEdit());
        this.mTransCodeManager = vlogTransCodeManager;
        vlogTransCodeManager.processTransCoding(this.mCallback);
    }

    public final boolean isMatchedNoAlg() {
        VlogConfig.VideoSource videoSource;
        return BaseBuildUtil.isInternational() || !AnalyticFaceAndSceneManager.isDeviceSupportVideo() || (videoSource = this.mVideoSource) == VlogConfig.VideoSource.FROM_OUTER_VIDEO_EDITOR || videoSource == VlogConfig.VideoSource.FORM_INNER_CLIP;
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        dismissProcessingDialog();
        if (!BaseBuildUtil.isInternational()) {
            VideoAnalyticLibraryLoaderHelper.getInstance().removeDownloadStateListener(this);
        }
        TemplateMatcher templateMatcher = this.mTemplateMatcher;
        if (templateMatcher != null) {
            templateMatcher.release();
        }
        Disposable disposable = this.mCheckDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mCheckDisposable.dispose();
        }
        VlogTransCodeManager vlogTransCodeManager = this.mTransCodeManager;
        if (vlogTransCodeManager != null) {
            vlogTransCodeManager.release();
            this.mTransCodeManager = null;
        }
    }

    public final void promptFilterVideoIfNeed(List<String> list) {
        int size = this.mPaths.size() - list.size();
        if (size > 0) {
            DefaultLogger.d("VlogTemplateMatchActivity_", "errorCount: %d", Integer.valueOf(size));
            ToastUtils.makeText(this, String.format(VlogUtils.getGalleryApp().getResources().getString(R.string.vlog_filter_no_support_nums), Integer.valueOf(size)));
        }
    }

    public final void promptUnsupportVideoIfNeed(List<String> list) {
        if (list.size() == 0) {
            ToastUtils.makeText(this, VlogUtils.getGalleryApp().getResources().getString(R.string.vlog_video_selected_not_support));
        }
    }

    public final void updatePaths(List<String> list) {
        this.mPaths.clear();
        this.mPaths.addAll(list);
    }

    public final void checkSource() {
        this.mCheckDisposable = Observable.create(new ObservableOnSubscribe<Object>() { // from class: com.miui.gallery.vlog.rule.VlogTemplateMatchActivity.4
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                VlogTemplateMatchActivity.this.filterVideo();
                observableEmitter.onNext(new Object());
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() { // from class: com.miui.gallery.vlog.rule.VlogTemplateMatchActivity.2
            @Override // io.reactivex.functions.Consumer
            public void accept(Object obj) throws Exception {
                if (VlogTemplateMatchActivity.this.mVideoSource == VlogConfig.VideoSource.FORM_INNER_CLIP) {
                    VlogTemplateMatchActivity.this.dismissProcessingDialog();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("tran_code_path", VlogTemplateMatchActivity.this.mPaths);
                    intent.putExtras(bundle);
                    VlogTemplateMatchActivity.this.setResult(-1, intent);
                    VlogTemplateMatchActivity.this.finish();
                    return;
                }
                VlogTemplateMatchActivity.this.goOnMatchingAfterCheck();
            }
        }, new Consumer<Throwable>() { // from class: com.miui.gallery.vlog.rule.VlogTemplateMatchActivity.3
            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) throws Exception {
            }
        });
    }

    public final boolean filterVideo() {
        ArrayList<String> arrayList = this.mPaths;
        if (arrayList == null || arrayList.size() <= 0) {
            return true;
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator<String> it = this.mPaths.iterator();
        while (it.hasNext()) {
            String next = it.next();
            VideoInfo extractVideoInfo = Util.extractVideoInfo(next);
            if (extractVideoInfo.getWidth() <= 0) {
                DefaultLogger.e("VlogTemplateMatchActivity_", "filterVideo path: %s  width: %s", next, Integer.valueOf(extractVideoInfo.getWidth()));
            } else if (extractVideoInfo.getWidth() >= 2160 && extractVideoInfo.getHeight() >= 2160 && extractVideoInfo.getFrameRate() > 75) {
                DefaultLogger.e("VlogTemplateMatchActivity_", "filterVideo path: %s  width: %s, %s", next, Integer.valueOf(extractVideoInfo.getWidth()), Integer.valueOf(extractVideoInfo.getFrameRate()));
            } else if (Util.is8KResolution(extractVideoInfo.getWidth(), extractVideoInfo.getHeight())) {
                DefaultLogger.e("VlogTemplateMatchActivity_", "filterVideo is8KVideo path: %s", next);
            } else {
                arrayList2.add(next);
            }
        }
        promptUnsupportVideoIfNeed(arrayList2);
        updatePaths(arrayList2);
        return true;
    }

    public final void filterVideoByExtendFormat() {
        ArrayList<String> arrayList = this.mPaths;
        int size = arrayList == null ? 0 : arrayList.size();
        ArrayList<String> filterVideoByExtendFormat = VlogExtendFormatUtils.filterVideoByExtendFormat(this.mPaths);
        this.mPaths = filterVideoByExtendFormat;
        if (filterVideoByExtendFormat == null || filterVideoByExtendFormat.size() == 0) {
            ToastUtils.makeText(this, (int) R.string.vlog_video_selected_not_support);
        }
        int size2 = size - this.mPaths.size();
        if (size2 <= 0 || this.mPaths.size() <= 0) {
            return;
        }
        ToastUtils.makeText(this, String.format(VlogUtils.getGalleryApp().getResources().getString(R.string.vlog_filter_no_support_nums), Integer.valueOf(size2)));
    }

    public final void goOnMatchingAfterCheck() {
        ArrayList<String> arrayList = this.mPaths;
        if (arrayList == null || arrayList.size() == 0) {
            DefaultLogger.d("VlogTemplateMatchActivity_", "there is no supported video, return");
            ToastUtils.makeText(this, (int) R.string.vlog_video_selected_not_support);
            finish();
        } else if (this.mIsMatchedNoAlg) {
            matchTemplate();
        } else {
            LibraryLoaderHelper videoAnalyticLibraryLoaderHelper = VideoAnalyticLibraryLoaderHelper.getInstance();
            videoAnalyticLibraryLoaderHelper.addDownloadStateListener(this);
            if (!videoAnalyticLibraryLoaderHelper.checkAbleOrDownload(this, this)) {
                return;
            }
            matchTemplate();
        }
    }

    public final void matchTemplate() {
        DebugLogUtils.startDebugLog("VlogTemplateMatchActivity_", "VlogTemplateMatchActivity matchTemplate");
        if (!this.mTemplateMatcher.matchTemplateAsync(this.mTargetTemplate, this.mPaths, new OnTemplateMatchedCallback() { // from class: com.miui.gallery.vlog.rule.VlogTemplateMatchActivity.6
            @Override // com.miui.gallery.vlog.rule.OnTemplateMatchedCallback
            public void onTemplateMatched(MatchedTemplate matchedTemplate) {
                DebugLogUtils.endDebugLog("VlogTemplateMatchActivity_", "VlogTemplateMatchActivity matchTemplate");
                VlogTemplateMatchActivity.this.jumpToVlog(matchedTemplate);
            }
        })) {
            finish();
        }
    }

    public final void jumpToVlog(MatchedTemplate matchedTemplate) {
        if (matchedTemplate == null) {
            DefaultLogger.d("VlogTemplateMatchActivity_", "matched template is null, finish");
            finish();
            return;
        }
        dismissProcessingDialog();
        Intent intent = new Intent(this, VlogActivity.class);
        intent.addFlags(nexEngine.ExportHEVCHighTierLevel62);
        intent.putExtra("com.miui.gallery.vlog.extra.template", matchedTemplate.mName);
        Bundle bundle = new Bundle();
        bundle.putSerializable("vlog_video_source", this.mVideoSource);
        bundle.putString("video_editor_src_path", this.mSrcFilePath);
        bundle.putParcelableArrayList("com.miui.gallery.vlog.extra.clips", new ArrayList<>(matchedTemplate.mMatchClips));
        intent.putExtra("com.miui.gallery.vlog.extra.clips", bundle);
        intent.putStringArrayListExtra("com.miui.gallery.vlog.extra.paths", this.mPaths);
        if (isSingleVideoEdit()) {
            intent.putExtra("photo_is_favorite", this.mIsFavorite);
        }
        startActivity(intent);
        finish();
        DefaultLogger.d("VlogTemplateMatchActivity_", "jump to vlog activity");
    }

    public final void showProcessingDialog() {
        ProgressDialog progressDialog = this.mProcessingDialog;
        if (progressDialog == null || progressDialog.isShowing()) {
            return;
        }
        this.mProcessingDialog.show();
    }

    public final void dismissProcessingDialog() {
        ProgressDialog progressDialog = this.mProcessingDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mProcessingDialog.dismiss();
    }

    public final boolean isSingleVideoEdit() {
        return this.mVideoSource == VlogConfig.VideoSource.FROM_OUTER_VIDEO_EDITOR;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStartListener
    public void onDownloadStart() {
        showProcessingDialog();
        this.mProcessingDialog.setMessage(getString(R.string.vlog_template_rule_downloading));
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
    public void onFinish(boolean z, int i) {
        if (z) {
            this.mProcessingDialog.setMessage(getString(R.string.vlog_template_rule_importing));
            matchTemplate();
            return;
        }
        finish();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        super.finish();
        ProgressDialog progressDialog = this.mProcessingDialog;
        if (progressDialog != null) {
            progressDialog.setOnCancelListener(null);
            this.mProcessingDialog.cancel();
        }
    }

    public final void parseIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mVideoSource = (VlogConfig.VideoSource) extras.getSerializable("vlog_video_source");
        }
        this.mTargetTemplate = intent.getStringExtra("com.miui.gallery.vlog.extra.template");
        DefaultLogger.d("VlogTemplateMatchActivity_", "parseIntent: mVideoSource=" + this.mVideoSource + ", mTargetTemplate=" + this.mTargetTemplate);
        if (TextUtils.isEmpty(this.mTargetTemplate)) {
            ClipData clipData = intent.getClipData();
            if (clipData == null) {
                return;
            }
            int itemCount = clipData.getItemCount();
            this.mPaths = new ArrayList<>();
            for (int i = 0; i < itemCount; i++) {
                Uri uri = clipData.getItemAt(i).getUri();
                if (uri != null) {
                    String lastPathSegment = uri.getLastPathSegment();
                    if (!TextUtils.isEmpty(lastPathSegment)) {
                        this.mPaths.add(lastPathSegment);
                    }
                }
            }
        } else {
            this.mPaths = intent.getStringArrayListExtra("com.miui.gallery.vlog.extra.paths");
        }
        if (isSingleVideoEdit()) {
            this.mSrcFilePath = this.mPaths.get(0);
            this.mIsFavorite = extras.getBoolean("photo_is_favorite");
        }
        filterVideoByExtendFormat();
    }
}
