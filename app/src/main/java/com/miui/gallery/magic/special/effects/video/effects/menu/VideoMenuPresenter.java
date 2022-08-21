package com.miui.gallery.magic.special.effects.video.effects.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.fetch.AudioRequest;
import com.miui.gallery.magic.fetch.VideoRequest;
import com.miui.gallery.magic.special.effects.video.adapter.ListItem;
import com.miui.gallery.magic.special.effects.video.adapter.VideoMusicAdapter;
import com.miui.gallery.magic.special.effects.video.adapter.VideoSpecialAdapter;
import com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuPresenter;
import com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewFragment;
import com.miui.gallery.magic.special.effects.video.util.MiVideoFrameLoader;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.GetPathFromUri;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicNetUtil;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.net.fetch.FetchManager;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.c.b;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class VideoMenuPresenter extends BasePresenter<VideoMenuFragment, VideoModel, IMenu$VP> implements MiVideoFrameLoader.OnImageLoadedListener {
    public static int SELECT_AUDIO_FILE = 124;
    public int MusicIndex;
    public VideoSpecialAdapter mAdapter;
    public final HashMap<Integer, Boolean> mBodyImageMap = new HashMap<>();
    public GestureDetector mGestureListener;
    public VideoMusicAdapter mMusicAdapter;
    public MyGestureListener mMyGestureListener;
    public boolean mSelectAudio;
    public float mVideoDuration;

    /* renamed from: $r8$lambda$3pRx1xMJo9Q4S-L3TFqb2mllTUk */
    public static /* synthetic */ void m1063$r8$lambda$3pRx1xMJo9Q4SL3TFqb2mllTUk(VideoMenuPresenter videoMenuPresenter, int i, ListItem listItem, boolean z, boolean z2) {
        videoMenuPresenter.lambda$checkDownload$0(i, listItem, z, z2);
    }

    @Override // com.miui.gallery.magic.special.effects.video.util.MiVideoFrameLoader.OnImageLoadedListener
    public void onImageDisplayed() {
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public VideoModel getModelInstance() {
        return new VideoModel(this);
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
        if (i2 == -1 && i == SELECT_AUDIO_FILE) {
            String path = GetPathFromUri.getPath(getActivity(), intent.getData());
            this.mMusicAdapter.selectMusicItem(this.MusicIndex);
            this.mMusicAdapter.getItem(this.MusicIndex);
            getActivity().event(1006, path);
        } else if (i2 != 0 || i != SELECT_AUDIO_FILE) {
        } else {
            this.mMusicAdapter.cancel();
        }
    }

    /* loaded from: classes2.dex */
    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        public int index;
        public long startTime;

        public static /* synthetic */ void $r8$lambda$pFlCV_O5lhKzsGDG5jWs4h5ozgw(MyGestureListener myGestureListener, int i, ListItem listItem, boolean z, boolean z2) {
            myGestureListener.lambda$checkDownload$0(i, listItem, z, z2);
        }

        public MyGestureListener() {
            VideoMenuPresenter.this = r1;
        }

        public /* synthetic */ MyGestureListener(VideoMenuPresenter videoMenuPresenter, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            MagicLog.INSTANCE.showLog("sssssssssssssssss:  onDown");
            clearTime();
            View findChildViewUnder = ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).mRecycle.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (findChildViewUnder != null) {
                this.index = ((Integer) findChildViewUnder.getTag()).intValue();
            }
            return super.onDown(motionEvent);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            checkDownload(this.index, VideoMenuPresenter.this.mAdapter.getItem(this.index));
            return false;
        }

        public final void checkDownload(final int i, final ListItem listItem) {
            if (!listItem.isDownLoaded()) {
                if (!VideoMenuPresenter.this.mSelectAudio) {
                    return;
                }
                VideoMenuPresenter.this.getActivity().event(b.l, listItem);
                VideoMenuPresenter.this.mAdapter.selectItem(this.index);
            } else if (!MagicNetUtil.isNetworkAvailable(MagicUtils.getGalleryApp())) {
                MagicToast.showToast(MagicUtils.getGalleryApp(), R$string.magic_network_error);
            } else if (MagicNetUtil.IsMobileNetConnect(MagicUtils.getGalleryApp())) {
                BaseFragmentActivity activity = VideoMenuPresenter.this.getActivity();
                if (activity == null) {
                    return;
                }
                NetworkConsider.consider(activity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuPresenter$MyGestureListener$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                    public final void onConfirmed(boolean z, boolean z2) {
                        VideoMenuPresenter.MyGestureListener.$r8$lambda$pFlCV_O5lhKzsGDG5jWs4h5ozgw(VideoMenuPresenter.MyGestureListener.this, i, listItem, z, z2);
                    }
                });
            } else {
                fetchDownLoad(i, listItem);
            }
        }

        public /* synthetic */ void lambda$checkDownload$0(int i, ListItem listItem, boolean z, boolean z2) {
            if (z) {
                fetchDownLoad(i, listItem);
            }
        }

        public final void fetchDownLoad(final int i, final ListItem listItem) {
            Request videoRequest;
            if (VideoMenuPresenter.this.mSelectAudio) {
                videoRequest = new AudioRequest(listItem.getResKey(), listItem.getResId());
            } else {
                videoRequest = new VideoRequest(listItem.getResKey(), listItem.getResId());
            }
            videoRequest.setListener(new Request.Listener() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuPresenter.MyGestureListener.1
                @Override // com.miui.gallery.net.fetch.Request.Listener
                public void onFail() {
                }

                {
                    MyGestureListener.this = this;
                }

                @Override // com.miui.gallery.net.fetch.Request.Listener
                public void onStart() {
                    VideoMenuPresenter.this.mAdapter.notifyDownloading(i, true);
                }

                @Override // com.miui.gallery.net.fetch.Request.Listener
                public void onSuccess() {
                    VideoMenuPresenter.this.mAdapter.notifyItem(i, false, false);
                    listItem.setDownLoaded(false);
                    if (VideoMenuPresenter.this.mSelectAudio) {
                        VideoMenuPresenter.this.getActivity().event(b.l, listItem);
                        VideoMenuPresenter.this.mAdapter.selectItem(MyGestureListener.this.index);
                    }
                }
            });
            FetchManager.INSTANCE.enqueue(videoRequest);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            super.onLongPress(motionEvent);
            if (VideoMenuPresenter.this.mSelectAudio) {
                return;
            }
            VideoPreviewFragment.setIsEdit(true);
            ListItem item = VideoMenuPresenter.this.mAdapter.getItem(this.index);
            if (item.isDownLoaded()) {
                checkDownload(this.index, item);
                return;
            }
            MagicLog.INSTANCE.showLog("");
            this.startTime = System.currentTimeMillis();
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("addEffect-onLongPress", "start: " + this.startTime);
            VideoMenuPresenter.this.getActivity().event(1001, Integer.valueOf(this.index));
            if (TextUtils.isEmpty(item.getTitle())) {
                MagicSampler.getInstance().recordCategory("video_post", "effect_clear");
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, item.getTitle());
            MagicSampler.getInstance().recordCategory("video_post", "effect", hashMap);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return super.onFling(motionEvent, motionEvent2, f, f2);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return super.onScroll(motionEvent, motionEvent2, f, f2);
        }

        public void onActionUp(float f, float f2) {
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("addEffect-ActionUp", "end: " + this.startTime);
            if (this.startTime > 0) {
                VideoMenuPresenter.this.getActivity().event(b.k, Integer.valueOf(this.index));
            }
        }

        public void clearTime() {
            this.startTime = -1L;
        }
    }

    /* renamed from: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuPresenter$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements IMenu$VP {
        public RecyclerView.OnItemTouchListener mListener = new RecyclerView.OnItemTouchListener() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuPresenter.1.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
            public void onRequestDisallowInterceptTouchEvent(boolean z) {
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }

            {
                AnonymousClass1.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                VideoMenuPresenter.this.mGestureListener.onTouchEvent(motionEvent);
                if (Math.abs(motionEvent.getRawX() - AnonymousClass1.this.startRecyclerRowX) > 30.0f) {
                    AnonymousClass1.this.onActionUp(motionEvent.getX(), motionEvent.getY());
                }
                int action = motionEvent.getAction();
                if (action == 0) {
                    AnonymousClass1.this.startRecyclerRowX = motionEvent.getRawX();
                    return false;
                } else if (action != 1 && action != 3) {
                    return false;
                } else {
                    VideoMenuPresenter.this.mMyGestureListener.onActionUp(motionEvent.getX(), motionEvent.getY());
                    return false;
                }
            }
        };
        public float startRecyclerRowX;

        public static /* synthetic */ boolean $r8$lambda$UnbOqQnpiZW9aVZnicaIxy1Zc8Y(AnonymousClass1 anonymousClass1, RecyclerView recyclerView, View view, int i) {
            return anonymousClass1.lambda$setMusicAdapter$0(recyclerView, view, i);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void scrollToPositionMusicItem(int i) {
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void setProgress(float f, int i) {
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void startProgress(int i) {
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void switchToAudioTrack() {
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void switchToVideoEffect() {
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void undo() {
        }

        public AnonymousClass1() {
            VideoMenuPresenter.this = r1;
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void loadListData() {
            setAdapter(new VideoSpecialAdapter(((VideoModel) VideoMenuPresenter.this.mModel).getContract().getVideoData(), VideoMenuPresenter.this.getActivity()));
            setMusicAdapter(new VideoMusicAdapter(((VideoModel) VideoMenuPresenter.this.mModel).getContract().getAudioData(), VideoMenuPresenter.this.getActivity()));
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void setAdapter(VideoSpecialAdapter videoSpecialAdapter) {
            VideoMenuPresenter.this.mMyGestureListener = new MyGestureListener(VideoMenuPresenter.this, null);
            VideoMenuPresenter.this.mGestureListener = new GestureDetector(VideoMenuPresenter.this.getActivity(), VideoMenuPresenter.this.mMyGestureListener);
            VideoMenuPresenter.this.mAdapter = videoSpecialAdapter;
            ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).getContract().setAdapter(videoSpecialAdapter);
            ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).mRecycle.addOnItemTouchListener(this.mListener);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void setMusicAdapter(VideoMusicAdapter videoMusicAdapter) {
            VideoMenuPresenter.this.mMusicAdapter = videoMusicAdapter;
            ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).getContract().setMusicAdapter(videoMusicAdapter);
            videoMusicAdapter.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuPresenter$1$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
                public final boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                    return VideoMenuPresenter.AnonymousClass1.$r8$lambda$UnbOqQnpiZW9aVZnicaIxy1Zc8Y(VideoMenuPresenter.AnonymousClass1.this, recyclerView, view, i);
                }
            });
        }

        public /* synthetic */ boolean lambda$setMusicAdapter$0(RecyclerView recyclerView, View view, int i) {
            VideoMenuPresenter.this.clickMusicItem(i);
            return false;
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void selectFile() {
            MagicFileUtil.selectLocalAudio(VideoMenuPresenter.this.getActivity(), VideoMenuPresenter.SELECT_AUDIO_FILE);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void changeToolBar(int i) {
            if (VideoMenuPresenter.this.mAdapter == null) {
                return;
            }
            if (i == 1) {
                VideoMenuPresenter.this.mSelectAudio = false;
                ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).mRecycle.setVisibility(0);
                ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).mSimpleRecycle.setVisibility(8);
            } else if (i != 2) {
            } else {
                VideoMenuPresenter.this.mSelectAudio = true;
                ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).mRecycle.setVisibility(8);
                ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).mSimpleRecycle.setVisibility(0);
            }
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void addImageToBody(ImageView imageView, ViewGroup.LayoutParams layoutParams) {
            ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).getContract().addImageToBody(imageView, layoutParams);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void setBodyImage(ViewGroup viewGroup, Bitmap bitmap, float f) {
            VideoMenuPresenter.this.setBodyImage(viewGroup, bitmap, f);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void setProgressDuration(float f) {
            ((VideoMenuFragment) VideoMenuPresenter.this.mView.get()).getContract().setProgressDuration(f);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
        public void onActionUp(float f, float f2) {
            if (VideoMenuPresenter.this.mMyGestureListener != null) {
                VideoMenuPresenter.this.mMyGestureListener.onActionUp(f, f2);
                VideoMenuPresenter.this.mMyGestureListener.clearTime();
            }
        }
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IMenu$VP mo1070initContract() {
        return new AnonymousClass1();
    }

    public final void setBodyImage(ViewGroup viewGroup, Bitmap bitmap, float f) {
        if (viewGroup == null || viewGroup.getChildCount() == 0 || bitmap == null || this.mVideoDuration <= 0.0f) {
            return;
        }
        int childCount = viewGroup.getChildCount();
        int i = ((int) f) / ((int) (this.mVideoDuration / childCount));
        if (i >= childCount) {
            i = childCount - 1;
        }
        if (this.mBodyImageMap.get(Integer.valueOf(i)) != null) {
            return;
        }
        this.mBodyImageMap.put(Integer.valueOf(i), Boolean.TRUE);
        ((ImageView) viewGroup.getChildAt(i)).setImageBitmap(bitmap);
    }

    public final void clickMusicItem(int i) {
        ((VideoMenuFragment) this.mView.get()).getContract().scrollToPositionMusicItem(i);
        ListItem item = this.mMusicAdapter.getItem(i);
        if (i == 1 && this.mSelectAudio) {
            getContract().selectFile();
            this.MusicIndex = i;
            return;
        }
        checkDownload(i, item);
    }

    public void setVideoTime(float f) {
        this.mVideoDuration = f;
    }

    public void firstFrameFinish(int i, int i2, float f, String str) {
        BaseFragmentActivity activityWithSync = getActivityWithSync();
        if (activityWithSync == null) {
            return;
        }
        int i3 = R$id.magic_body_image;
        int height = activityWithSync.findViewById(i3).getHeight();
        int width = activityWithSync.findViewById(i3).getWidth();
        int ceil = (int) Math.ceil(((i * 1.0d) / i2) * height);
        int ceil2 = (int) Math.ceil((width * 1.0d) / ceil);
        getContract().setProgressDuration(f);
        for (int i4 = 0; i4 < ceil2; i4++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            getContract().addImageToBody(imageView, new ViewGroup.LayoutParams(ceil, -1));
        }
    }

    public void setBodyImage(Bitmap bitmap, float f) {
        VideoMenuFragment videoMenuFragment;
        WeakReference<V> weakReference = this.mView;
        if (weakReference == 0 || (videoMenuFragment = (VideoMenuFragment) weakReference.get()) == null) {
            return;
        }
        videoMenuFragment.getContract().setBodyImage(bitmap, f);
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void unBindView() {
        super.unBindView();
    }

    public final void checkDownload(final int i, final ListItem listItem) {
        if (listItem.isDownLoaded()) {
            if (!MagicNetUtil.isNetworkAvailable(MagicUtils.getGalleryApp())) {
                MagicToast.showToast(MagicUtils.getGalleryApp(), R$string.magic_network_error);
            } else if (MagicNetUtil.IsMobileNetConnect(MagicUtils.getGalleryApp())) {
                BaseFragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                NetworkConsider.consider(activity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuPresenter$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                    public final void onConfirmed(boolean z, boolean z2) {
                        VideoMenuPresenter.m1063$r8$lambda$3pRx1xMJo9Q4SL3TFqb2mllTUk(VideoMenuPresenter.this, i, listItem, z, z2);
                    }
                });
            } else {
                fetchDownload(i, listItem);
            }
        } else if (!this.mSelectAudio) {
        } else {
            getActivity().event(b.l, listItem);
            this.mMusicAdapter.selectItem(i);
        }
    }

    public /* synthetic */ void lambda$checkDownload$0(int i, ListItem listItem, boolean z, boolean z2) {
        if (z) {
            fetchDownload(i, listItem);
        }
    }

    public void fetchDownload(final int i, final ListItem listItem) {
        Request videoRequest;
        if (this.mSelectAudio) {
            videoRequest = new AudioRequest(listItem.getResKey(), listItem.getResId());
        } else {
            videoRequest = new VideoRequest(listItem.getResKey(), listItem.getResId());
        }
        videoRequest.setListener(new Request.Listener() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuPresenter.2
            @Override // com.miui.gallery.net.fetch.Request.Listener
            public void onFail() {
            }

            {
                VideoMenuPresenter.this = this;
            }

            @Override // com.miui.gallery.net.fetch.Request.Listener
            public void onStart() {
                if (VideoMenuPresenter.this.mSelectAudio) {
                    VideoMenuPresenter.this.mMusicAdapter.notifyDownloading(i, true);
                } else {
                    VideoMenuPresenter.this.mAdapter.notifyDownloading(i, true);
                }
            }

            @Override // com.miui.gallery.net.fetch.Request.Listener
            public void onSuccess() {
                listItem.setDownLoaded(false);
                if (VideoMenuPresenter.this.mSelectAudio) {
                    VideoMenuPresenter.this.mMusicAdapter.notifyItem(i, false, false);
                    VideoMenuPresenter.this.getActivity().event(b.l, listItem);
                    VideoMenuPresenter.this.mMusicAdapter.selectItem(i);
                    return;
                }
                VideoMenuPresenter.this.mAdapter.notifyItem(i, false, false);
            }
        });
        FetchManager.INSTANCE.enqueue(videoRequest);
    }
}
