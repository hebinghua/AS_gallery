package com.miui.gallery.magic.special.effects.video.cut.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.special.effects.video.VideoEffectsActivity;
import com.miui.gallery.magic.special.effects.video.util.ClipReverseHelper;
import com.miui.gallery.magic.special.effects.video.util.MiVideoFrameLoader;
import com.miui.gallery.magic.util.DimenUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.util.ScreenUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoMenuPresenter extends BasePresenter<VideoMenuFragment, VideoModel, IMenu$VP> implements MiVideoFrameLoader.OnImageLoadedListener, MiVideoFrameLoader.OnFrameCallback {
    public ClipReverseHelper mClipReverseHelper;
    public int mDuration;
    public String mFilePath;
    public List<WeakReference<ImageView>> mImageReferenceList = new ArrayList();
    public MiVideoFrameLoader mMiVideoFrameLoader;
    public int mScreenWidth;

    @Override // com.miui.gallery.magic.special.effects.video.util.MiVideoFrameLoader.OnImageLoadedListener
    public void onImageDisplayed() {
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public VideoModel getModelInstance() {
        return new VideoModel(this);
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IMenu$VP mo1070initContract() {
        return new IMenu$VP() { // from class: com.miui.gallery.magic.special.effects.video.cut.menu.VideoMenuPresenter.1
            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void setProgress(int i) {
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void loadListData() {
                VideoMenuPresenter.this.initFrameList();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void seekTo(long j, boolean z) {
                Message obtain = Message.obtain();
                obtain.obj = Long.valueOf(j);
                obtain.arg1 = z ? 1 : 0;
                VideoMenuPresenter.this.getActivity().event(2, obtain);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void onStopTrackingTouch() {
                VideoMenuPresenter.this.getActivity().event(10);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void addImageToBody(ImageView imageView) {
                ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).getContract().addImageToBody(imageView);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void initFinish(Size size) {
                VideoMenuPresenter videoMenuPresenter = VideoMenuPresenter.this;
                videoMenuPresenter.mDuration = ((Integer) videoMenuPresenter.getActivity().event(3)).intValue();
                VideoMenuPresenter videoMenuPresenter2 = VideoMenuPresenter.this;
                videoMenuPresenter2.mFilePath = (String) videoMenuPresenter2.getActivity().event(4);
                VideoMenuPresenter.this.mMiVideoFrameLoader = new MiVideoFrameLoader();
                VideoMenuPresenter.this.mMiVideoFrameLoader.setFirstFrameCallback(VideoMenuPresenter.this);
                View findViewById = VideoMenuPresenter.this.getActivity().findViewById(R$id.magic_cut_body_image);
                int height = findViewById.getHeight();
                int width = (int) (findViewById.getWidth() - (DimenUtils.dp2px(20, VideoMenuPresenter.this.getActivity()) * 2.0f));
                int ceil = (int) Math.ceil((width * 1.0d) / ((int) Math.ceil(((size.getWidth() * 1.0d) / size.getHeight()) * height)));
                int i = width / ceil;
                double d = (VideoMenuPresenter.this.mDuration * 1.0d) / ceil;
                VideoMenuPresenter.this.getContract().setDuration(VideoMenuPresenter.this.mDuration);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, height);
                for (int i2 = 0; i2 < ceil; i2++) {
                    ImageView imageView = new ImageView(VideoMenuPresenter.this.getActivity());
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    VideoMenuPresenter.this.mImageReferenceList.add(new WeakReference(imageView));
                    VideoMenuPresenter.this.mMiVideoFrameLoader.loadImage(imageView, VideoMenuPresenter.this.mFilePath, i, (long) (i2 * d));
                    VideoMenuPresenter.this.getContract().addImageToBody(imageView);
                }
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void cutVideo() {
                MagicLog.INSTANCE.startLog("ConstructGraph_cut", "视频分割开始");
                if (VideoMenuPresenter.this.mClipReverseHelper == null) {
                    VideoMenuPresenter videoMenuPresenter = VideoMenuPresenter.this;
                    videoMenuPresenter.mClipReverseHelper = new ClipReverseHelper(videoMenuPresenter.getActivity(), new ClipReverseHelper.Callback() { // from class: com.miui.gallery.magic.special.effects.video.cut.menu.VideoMenuPresenter.1.1
                        @Override // com.miui.gallery.magic.special.effects.video.util.ClipReverseHelper.Callback
                        public void onTranscodeProgress(int i) {
                        }

                        @Override // com.miui.gallery.magic.special.effects.video.util.ClipReverseHelper.Callback
                        public void onSuccess(String str, String str2, int i) {
                            VideoMenuPresenter.this.getActivity().removeLoadingDialog();
                            MagicLog.INSTANCE.endLog("ConstructGraph_cut", "视频分割结束");
                            Intent intent = new Intent(VideoMenuPresenter.this.getActivity(), VideoEffectsActivity.class);
                            intent.putExtra("video_slice", true);
                            intent.putExtra("VideoFile", str2);
                            VideoMenuPresenter.this.getActivity().startActivity(intent);
                            VideoMenuPresenter.this.getActivity().finish();
                            MagicLog.INSTANCE.showLog("dstFile:", str2);
                        }
                    });
                }
                VideoMenuPresenter.this.getActivity().showLoading();
                int[] currentTimes = VideoMenuPresenter.this.getContract().getCurrentTimes(VideoMenuPresenter.this.mDuration);
                MagicFileUtil.clearTempVideoFiles();
                VideoMenuPresenter.this.mClipReverseHelper.reverseFile(1, VideoMenuPresenter.this.mFilePath, 1, currentTimes[0], currentTimes[1]);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public int[] getCurrentTimes(int i) {
                return ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).getContract().getCurrentTimes(i);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void setDuration(int i) {
                ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).getContract().setDuration(i);
            }
        };
    }

    public final void initFrameList() {
        this.mScreenWidth = ScreenUtils.getScreenWidth();
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void unBindView() {
        super.unBindView();
        ClipReverseHelper clipReverseHelper = this.mClipReverseHelper;
        if (clipReverseHelper != null) {
            clipReverseHelper.release();
        }
        MiVideoFrameLoader miVideoFrameLoader = this.mMiVideoFrameLoader;
        if (miVideoFrameLoader != null) {
            miVideoFrameLoader.release();
        }
    }

    @Override // com.miui.gallery.magic.special.effects.video.util.MiVideoFrameLoader.OnFrameCallback
    public void onAvailableFrame(Bitmap bitmap) {
        ImageView imageView;
        if (this.mImageReferenceList.size() == 0 || bitmap == null) {
            return;
        }
        while (this.mImageReferenceList.size() != 0) {
            WeakReference<ImageView> remove = this.mImageReferenceList.remove(0);
            if (remove != null && (imageView = remove.get()) != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
