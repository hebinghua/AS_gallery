package com.miui.gallery.vlog.clip;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$integer;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.base.net.VlogResource;
import com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.vlog.entity.TransData;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.io.File;
import java.util.ArrayList;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class TransResView extends LinearLayout implements View.OnClickListener {
    public static int sAnimAppearDelay;
    public static int sAnimAppearDuration;
    public static int sAnimDisappearDuration;
    public static int sAnimOffset;
    public TransViewAdapter mAdapter;
    public Callback mCallback;
    public ImageView mCancelView;
    public Context mContext;
    public IVideoClip mCurrentVideoClip;
    public ResourceDownloadManager.IDownloadTaskListener mIDownloadTaskListener;
    public TransData mInitTransData;
    public int mLastPosition;
    public String mMatchTransName;
    public String mMatchTransParams;
    public ImageView mOkView;
    public OnItemClickListener mOnItemClickListener;
    public SimpleRecyclerView mRecyclerView;
    public ResourceDownloadManager mResourceDownloadManager;
    public TranResPresenter mTranResPresenter;
    public ArrayList<TransData> mTransDatas;
    public TransZipFileConfig mZipFileConfig;

    /* loaded from: classes2.dex */
    public interface Callback {
        void loadDataFail();

        void onExit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getView() {
        return this;
    }

    public TransResView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTransDatas = new ArrayList<>();
        this.mLastPosition = -1;
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.vlog.clip.TransResView.2
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                TransViewAdapter transViewAdapter = (TransViewAdapter) recyclerView.getAdapter();
                recyclerView.smoothScrollToPosition(i);
                TransData itemData = transViewAdapter.getItemData(i);
                if (itemData.isNone()) {
                    TransResView.this.printLogStart();
                    transViewAdapter.setSelection(i);
                    TransResView.this.mTranResPresenter.removeTrans(TransResView.this.mCurrentVideoClip, true);
                } else if (itemData.isExtra()) {
                    if (itemData.isDownloaded()) {
                        TransResView.this.performItem(itemData, i);
                    } else {
                        TransResView.this.mResourceDownloadManager.createDownloadCommand(itemData, i);
                    }
                }
                VlogStatUtils.statEvent("clip", itemData.getNameKey());
                return true;
            }
        };
        this.mIDownloadTaskListener = new ResourceDownloadManager.IDownloadTaskListener<VlogResource>() { // from class: com.miui.gallery.vlog.clip.TransResView.3
            @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
            public void onCommandStart(VlogResource vlogResource, int i) {
                if (vlogResource != null) {
                    vlogResource.setDownloadState(18);
                    TransResView.this.notifyDateSetChanged(i);
                    DefaultLogger.d("TransResView", "download start: %s", vlogResource.getLabel());
                }
            }

            @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
            public void onCommandSuccess(VlogResource vlogResource, int i) {
                if (vlogResource instanceof TransData) {
                    vlogResource.setDownloadState(17);
                    TransData transData = (TransData) vlogResource;
                    StringBuilder sb = new StringBuilder();
                    sb.append(TransResView.this.mZipFileConfig.getUnzipPath());
                    String str = File.separator;
                    sb.append(str);
                    sb.append(vlogResource.getNameKey());
                    sb.append(str);
                    sb.append(transData.getFileName());
                    transData.setTransPath(sb.toString());
                    DefaultLogger.d("TransResView", "download success: %s", vlogResource.getLabel());
                    TransResView.this.performItem(transData, i);
                }
            }

            @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
            public void onCommandFail(VlogResource vlogResource, int i) {
                if (vlogResource != null) {
                    vlogResource.setDownloadState(20);
                    TransResView.this.notifyDateSetChanged(i);
                    DefaultLogger.d("TransResView", "download fail: %s", vlogResource.getLabel());
                }
            }

            @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
            public void onTaskCancel(VlogResource vlogResource, int i) {
                if (vlogResource != null) {
                    int downloadState = vlogResource.getDownloadState();
                    if (downloadState != 0 && downloadState != 17) {
                        vlogResource.setDownloadState(20);
                        TransResView.this.notifyDateSetChanged(i);
                    }
                    DefaultLogger.d("TransResView", "download cancel: %s", vlogResource.getLabel());
                }
            }
        };
        init(context);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public final void init(Context context) {
        this.mContext = context;
        LinearLayout.inflate(context, R$layout.vlog_trans_res_view_layout, this);
        this.mRecyclerView = (SimpleRecyclerView) findViewById(R$id.recycler_view);
        this.mCancelView = (ImageView) findViewById(R$id.cancel);
        ImageView imageView = (ImageView) findViewById(R$id.ok);
        this.mOkView = imageView;
        imageView.setOnClickListener(this);
        this.mCancelView.setOnClickListener(this);
        FolmeUtilsEditor.animButton(this.mOkView);
        FolmeUtilsEditor.animButton(this.mCancelView);
        TranResPresenter tranResPresenter = new TranResPresenter(this.mContext, this);
        this.mTranResPresenter = tranResPresenter;
        tranResPresenter.loadData();
        this.mZipFileConfig = new TransZipFileConfig();
        ResourceDownloadManager resourceDownloadManager = new ResourceDownloadManager(this.mContext, ((VlogModel) VlogUtils.getViewModel((FragmentActivity) context, VlogModel.class)).getFragmentManager(), this.mZipFileConfig);
        this.mResourceDownloadManager = resourceDownloadManager;
        resourceDownloadManager.setDownloadTaskListener(this.mIDownloadTaskListener);
        initAnimatorData();
    }

    public final void initAnimatorData() {
        Resources resources = VlogUtils.getGalleryApp().getResources();
        if (sAnimOffset == 0) {
            sAnimOffset = resources.getDimensionPixelSize(R$dimen.vlog_enter_sub_editor_main_menu_offset);
        }
        if (sAnimAppearDuration == 0) {
            sAnimAppearDuration = resources.getInteger(R$integer.vlog_sub_editor_sub_menu_appear_duration);
        }
        if (sAnimDisappearDuration == 0) {
            sAnimDisappearDuration = resources.getInteger(R$integer.vlog_sub_editor_sub_menu_disappear_duration);
        }
        if (sAnimAppearDelay == 0) {
            sAnimAppearDelay = resources.getInteger(R$integer.vlog_sub_editor_sub_menu_appear_delay);
        }
    }

    public void buildTransAnimator(boolean z) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        if (z) {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, sAnimOffset, 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            if (getView() != null) {
                getView().setVisibility(4);
            }
            objectAnimator.setStartDelay(sAnimAppearDelay);
            objectAnimator.setDuration(sAnimAppearDuration);
            objectAnimator.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.vlog.clip.TransResView.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    VlogUtils.showViews(TransResView.this.getView());
                }
            });
        } else {
            PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            objectAnimator.setValues(ofFloat);
            objectAnimator.setDuration(sAnimDisappearDuration);
            VlogUtils.hideViews(getView());
        }
        objectAnimator.setTarget(getView());
        objectAnimator.start();
    }

    public void loadRecyclerView(ArrayList<TransData> arrayList, boolean z) {
        Callback callback;
        if (!z && (callback = this.mCallback) != null) {
            callback.loadDataFail();
        }
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(this.mContext);
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(this.mContext));
        customScrollerLinearLayoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.vlog_trans_item_start);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.vlog_trans_item_gap);
        if (this.mRecyclerView.getItemDecorationCount() == 0) {
            this.mRecyclerView.addItemDecoration(new BlankDivider(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize2, 0, 0));
        }
        this.mTransDatas = arrayList;
        TransViewAdapter transViewAdapter = new TransViewAdapter(arrayList);
        this.mAdapter = transViewAdapter;
        this.mRecyclerView.setAdapter(transViewAdapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
    }

    public final void initMatchTrans() {
        IVideoClip iVideoClip = this.mCurrentVideoClip;
        if (iVideoClip == null) {
            return;
        }
        this.mMatchTransName = iVideoClip.getTransName();
        String transParam = this.mCurrentVideoClip.getTransParam();
        this.mMatchTransParams = transParam;
        int findMatchTransIndex = this.mTranResPresenter.findMatchTransIndex(this.mMatchTransName, transParam, this.mAdapter.getEffects());
        DefaultLogger.d("TransResView", "initMatchTrans index: %s   trans: %s", Integer.valueOf(findMatchTransIndex), this.mMatchTransParams);
        this.mLastPosition = findMatchTransIndex;
        this.mInitTransData = this.mAdapter.getItemData(findMatchTransIndex);
        if (findMatchTransIndex >= 0) {
            this.mAdapter.setSelection(findMatchTransIndex);
            this.mRecyclerView.scrollToPosition(findMatchTransIndex);
            return;
        }
        this.mRecyclerView.scrollToPosition(0);
    }

    public void setCurrentVideoClip(IVideoClip iVideoClip) {
        this.mCurrentVideoClip = iVideoClip;
        initMatchTrans();
    }

    public final void printLogStart() {
        DebugLogUtils.HAS_LOADED_SELECT_TRANS = false;
        DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_TRANS = false;
        DebugLogUtils.startDebugLogSpecialTime("TransResView", "vlog applyTrans");
    }

    public final void performItem(TransData transData, int i) {
        this.mAdapter.setSelection(i);
        if (transData != null) {
            printLogStart();
            this.mTranResPresenter.applyTrans(transData.getTransPath(), this.mCurrentVideoClip, true);
            DefaultLogger.d("TransResView", "apply trans effect: %s ,label: %s.", transData.getTransPath(), transData.getLabel());
        }
    }

    public final void notifyDateSetChanged(int i) {
        TransViewAdapter transViewAdapter = this.mAdapter;
        if (transViewAdapter != null) {
            transViewAdapter.notifyItemChanged(i, 1);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mOkView) {
            this.mLastPosition = this.mAdapter.getSelection();
            exit();
        } else if (view != this.mCancelView) {
        } else {
            doCancelEvent();
        }
    }

    public void doCancelEvent() {
        printLogStart();
        TranResPresenter tranResPresenter = this.mTranResPresenter;
        TransData transData = this.mInitTransData;
        tranResPresenter.applyTrans(transData != null ? transData.getTransPath() : this.mMatchTransParams, this.mCurrentVideoClip, false);
        exit();
    }

    public final void exit() {
        buildTransAnimator(false);
        this.mCurrentVideoClip = null;
        this.mLastPosition = -1;
        this.mInitTransData = null;
        this.mMatchTransParams = null;
        this.mMatchTransName = null;
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onExit();
        }
        destroy();
    }

    public void destroy() {
        TranResPresenter tranResPresenter = this.mTranResPresenter;
        if (tranResPresenter != null) {
            tranResPresenter.destroy();
        }
    }
}
