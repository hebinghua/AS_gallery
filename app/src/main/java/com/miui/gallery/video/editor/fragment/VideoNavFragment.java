package com.miui.gallery.video.editor.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.editor.ui.menu.type.EditNavMenuView;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.adapter.VideoNavAdapter;
import com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback;
import com.miui.gallery.video.editor.manager.SmartVideoJudgeManager;
import com.miui.gallery.video.editor.model.MenuFragmentData;
import com.miui.gallery.video.editor.ui.MenuFragment;
import com.miui.gallery.video.editor.util.ToolsUtil;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerViewNoSpring;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.QuarticEaseOutInterpolator;

/* loaded from: classes2.dex */
public class VideoNavFragment extends MenuFragment {
    public static int sAnimAppearDelay;
    public static int sAnimAppearDuration;
    public static int sAnimDisappearDuration;
    public static int sAnimOffset;
    public VideoNavAdapter mAdapter;
    public Dialog mDialog;
    public SimpleRecyclerViewNoSpring mNavRecyclerView;
    public TextView mTvDiscard;
    public TextView mTvExport;
    public VideoFramesManager mVideoFramesManager;
    public int mVideoFrames = 0;
    public boolean showAnimate = false;
    public List<MenuFragmentData> mNavigatorDataList = new ArrayList();
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.video.editor.fragment.VideoNavFragment.3
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            if (VideoNavFragment.this.mCallback.isLoadSuccess()) {
                MenuFragmentData menuFragmentData = (MenuFragmentData) VideoNavFragment.this.mNavigatorDataList.get(i);
                if (menuFragmentData == null) {
                    return true;
                }
                VideoNavFragment.this.mCallback.changeEditMenu(menuFragmentData);
                return true;
            }
            ToastUtils.makeText(VideoNavFragment.this.mContext, (int) R.string.video_editor_video_loading);
            return false;
        }
    };

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public List<String> getCurrentEffect() {
        return null;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public int getEffectId() {
        return R.id.video_editor_nav;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initAnimatorData();
        this.mVideoFramesManager = new VideoFramesManager(this);
    }

    public final void initAnimatorData() {
        if (sAnimOffset == 0) {
            sAnimOffset = getActivity().getResources().getDimensionPixelSize(R.dimen.video_editor_enter_sub_editor_main_menu_offset);
        }
        if (sAnimAppearDuration == 0) {
            sAnimAppearDuration = getActivity().getResources().getInteger(R.integer.video_editor_sub_editor_main_menu_appear_duration);
        }
        if (sAnimDisappearDuration == 0) {
            sAnimDisappearDuration = getActivity().getResources().getInteger(R.integer.video_editor_sub_editor_main_menu_disappear_duration);
        }
        if (sAnimAppearDelay == 0) {
            sAnimAppearDelay = getActivity().getResources().getInteger(R.integer.video_editor_sub_editor_main_menu_appear_delay);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new EditNavMenuView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mNavRecyclerView = (SimpleRecyclerViewNoSpring) view.findViewById(R.id.recycler_view);
        this.mTvExport = (TextView) view.findViewById(R.id.export);
        this.mTvDiscard = (TextView) view.findViewById(R.id.discard);
        TextView textView = this.mTvExport;
        VideoEditor videoEditor = this.mVideoEditor;
        textView.setEnabled(videoEditor != null && videoEditor.hasEdit());
        initListener();
        if (SmartVideoJudgeManager.isAvailable() && this.mVideoFrames == 0) {
            this.mVideoFramesManager.handleVideoFrames();
        } else {
            initRecyclerView();
        }
    }

    public final void initRecyclerView() {
        int dimensionPixelSize;
        int dimensionPixelSize2;
        initData();
        VideoNavAdapter videoNavAdapter = new VideoNavAdapter(this.mNavigatorDataList);
        this.mAdapter = videoNavAdapter;
        this.mNavRecyclerView.setAdapter(videoNavAdapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        if (this.mNavigatorDataList.get(0).module == 17) {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.video_editor_nav_item_has_smart_effect_start);
            dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.video_editor_nav_item_has_smart_effect_horizontal_interval);
        } else {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.video_editor_nav_item_start);
            dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.video_editor_nav_item_horizontal_interval);
        }
        int i = dimensionPixelSize;
        this.mNavRecyclerView.addItemDecoration(new BlankDivider(i, i, dimensionPixelSize2, 0, 0));
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mNavRecyclerView);
    }

    public final void initListener() {
        this.mTvExport.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.fragment.VideoNavFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
                VideoNavFragment.this.doApply();
            }
        });
        this.mTvDiscard.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.fragment.VideoNavFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
                VideoNavFragment.this.doCancel();
            }
        });
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment, androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        if (!this.showAnimate) {
            return null;
        }
        if (8194 == i && z) {
            return null;
        }
        ObjectAnimator objectAnimator = new ObjectAnimator();
        if (z) {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, sAnimOffset, 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            if (getView() != null) {
                getView().setAlpha(0.0f);
            }
            objectAnimator.setStartDelay(sAnimAppearDelay);
            objectAnimator.setDuration(sAnimAppearDuration);
        } else {
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f, -sAnimOffset);
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f));
            objectAnimator.setInterpolator(new QuarticEaseOutInterpolator());
            objectAnimator.setDuration(sAnimDisappearDuration);
        }
        return objectAnimator;
    }

    public final void initData() {
        DefaultLogger.d("VideoNavFragment", "the video fps is: %s", Integer.valueOf(this.mVideoFrames));
        this.mNavigatorDataList = this.mCallback.getNavigatorData(this.mVideoFrames);
    }

    public boolean doApply() {
        IVideoEditorListener$IVideoEditorFragmentCallback iVideoEditorListener$IVideoEditorFragmentCallback = this.mCallback;
        if (iVideoEditorListener$IVideoEditorFragmentCallback != null) {
            iVideoEditorListener$IVideoEditorFragmentCallback.onSave();
            return true;
        }
        return false;
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public boolean doCancel() {
        IVideoEditorListener$IVideoEditorFragmentCallback iVideoEditorListener$IVideoEditorFragmentCallback = this.mCallback;
        if (iVideoEditorListener$IVideoEditorFragmentCallback != null) {
            iVideoEditorListener$IVideoEditorFragmentCallback.onCancel();
            return true;
        }
        return false;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.dismiss();
            this.mDialog = null;
        }
        VideoFramesManager videoFramesManager = this.mVideoFramesManager;
        if (videoFramesManager != null) {
            videoFramesManager.onDestroy();
        }
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public void updateLastFragment(MenuFragment menuFragment) {
        if (menuFragment == null) {
            this.showAnimate = false;
        } else {
            this.showAnimate = true;
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoFramesManager {
        public WeakReference<VideoNavFragment> mFragmentRef;
        public Future<Void> mFuture;
        public FutureHandler<Void> mFutureHandler = new FutureHandler<Void>() { // from class: com.miui.gallery.video.editor.fragment.VideoNavFragment.VideoFramesManager.1
            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<Void> future) {
                VideoNavFragment fragment = VideoFramesManager.this.getFragment();
                if (future.isCancelled() || fragment == null) {
                    return;
                }
                fragment.initRecyclerView();
            }
        };
        public ThreadPool.Job mVideoFramesJob = new ThreadPool.Job() { // from class: com.miui.gallery.video.editor.fragment.VideoNavFragment.VideoFramesManager.2
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                VideoNavFragment fragment = VideoFramesManager.this.getFragment();
                if (jobContext.isCancelled() || fragment == null) {
                    return null;
                }
                fragment.mVideoFrames = ToolsUtil.getVideoFrameRate(fragment.mVideoEditor.getVideoPath());
                return null;
            }
        };

        public VideoFramesManager(VideoNavFragment videoNavFragment) {
            this.mFragmentRef = new WeakReference<>(videoNavFragment);
        }

        public void handleVideoFrames() {
            if (getFragment() == null) {
                DefaultLogger.d("VideoNavFragment", "the mFragmentRef is null. ");
                return;
            }
            Future<Void> future = this.mFuture;
            if (future != null) {
                future.cancel();
            }
            this.mFuture = ThreadManager.getMiscPool().submit(this.mVideoFramesJob, this.mFutureHandler);
        }

        public final VideoNavFragment getFragment() {
            WeakReference<VideoNavFragment> weakReference = this.mFragmentRef;
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
